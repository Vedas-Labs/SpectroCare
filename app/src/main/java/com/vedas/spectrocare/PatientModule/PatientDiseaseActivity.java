package com.vedas.spectrocare.PatientModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBaseModels.PatientModel;
import com.vedas.spectrocare.PatientServerApiModel.FamilyTrackingInfo;
import com.vedas.spectrocare.PatinetControllers.PatientFamilyDataController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.MedicalPersonaSignupView;
import com.vedas.spectrocare.activities.MedicalPersonalSignupPresenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class PatientDiseaseActivity extends AppCompatActivity implements MedicalPersonaSignupView {
    ImageView edtImg, imgCal, imgSche, backImg,imgClear,clearImg;
    Button btnSaveChanges;
    boolean cal;
    String position;
    int value;
    EditText edtDiseaseName, edtStartDate, edtEndDate, edtSymptoms, edtDescription, edtRemark;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    CalendarView calendarView;
    TextView txtDate,txtTime;
    String date,sD,eD,formattedDate,clockTime;
    long kk;
    PatientModel objModel;
    long d;
    int i;
    TextView txtPatientName,ageTxt;
    boolean ed,currentIllness;
    CheckBox checkIllness;
    FamilyTrackingInfo trackingInfo;
    ArrayList<FamilyTrackingInfo> listInfo;
    PopupWindow popUp;
    RefreshShowingDialog refreshShowingDialog;
    View mView;
    JSONObject updateIllnessObject;
    boolean isHourFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_disease);
        objModel = PatientLoginDataController.getInstance().currentPatientlProfile;
        ed = true;
        Intent intent = getIntent();
        position = intent.getStringExtra("position");
        isHourFormat = intent.getBooleanExtra("isHourFormat",false);
        value = Integer.parseInt(position);
        casting();
        clickEvents();

    }
    private void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager)PatientDiseaseActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
    public void updateIllnessApi(){
        updateIllnessObject =new JSONObject();
        try {
            updateIllnessObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile
                    .getHospital_reg_number());
            updateIllnessObject.put("byWhom","patient");
            updateIllnessObject.put("byWhomID",PatientLoginDataController.getInstance().currentPatientlProfile
                    .getPatientId());
            updateIllnessObject.put("patientID",PatientLoginDataController.getInstance().currentPatientlProfile
                    .getPatientId());
            updateIllnessObject.put("medical_record_id",PatientLoginDataController.getInstance().currentPatientlProfile
                    .getMedicalRecordId());
            updateIllnessObject.put("illnessID",PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords().get(value).getIllnessID());
            updateIllnessObject.put("illnessCondition",edtRemark.getText().toString());
            updateIllnessObject.put("symptoms",edtSymptoms.getText().toString());
            updateIllnessObject.put("currentStatus",edtDiseaseName.getText().toString());
            updateIllnessObject.put("description",edtDescription.getText().toString());
            if (checkIllness.isChecked()){
                Log.e("what","true");
                currentIllness = true;
            }else{
                Log.e("what","false");
                currentIllness = false;
            }
            updateIllnessObject.put("isCurrentIllness",currentIllness);
            if (!edtStartDate.getText().toString().isEmpty()){
                String[] parts = edtStartDate.getText().toString().split("/");
                //Solution
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

                Date date = null;
                try {
                    date = (Date)formatter.parse(parts[0]+"-"+parts[1]+"-"+parts[2]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long out = date.getTime();
                sD= String.valueOf(out);


            }
            if (!edtEndDate.getText().toString().isEmpty()){
                String[] parts = edtEndDate.getText().toString().split("/");
                //Solution
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

                Date date = null;
                try {
                    date = (Date)formatter.parse(parts[0]+"-"+parts[1]+"-"+parts[2]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long out = date.getTime();
                eD= String.valueOf(out);
            }


            updateIllnessObject.put("startDate",sD);
            updateIllnessObject.put("endDate",eD);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("objecttt", "" + updateIllnessObject);

        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(updateIllnessObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.updatePatientIllness(PatientLoginDataController.getInstance()
                .currentPatientlProfile.getAccessToken(),gsonObject),"update");
    }
    public void casting() {
        imgClear = findViewById(R.id.img_clear);
        clearImg = findViewById(R.id.img_clear2);
        refreshShowingDialog = new RefreshShowingDialog(PatientDiseaseActivity.this);
        edtDiseaseName = findViewById(R.id.edt_disease_name);
        edtStartDate = findViewById(R.id.edt_start_date);
        edtEndDate = findViewById(R.id.edt_end_date);
        checkIllness = findViewById(R.id.check_illness);
        edtSymptoms = findViewById(R.id.edt_symptoms);
        backImg = findViewById(R.id.img_back_arrow);
        imgCal = findViewById(R.id.img_calender);
        imgSche = findViewById(R.id.img_schedule);
        txtDate = findViewById(R.id.txt_doc_date);
        txtTime = findViewById(R.id.txt_doc_time);
        txtPatientName = findViewById(R.id.txt_name_of_doc);
        ageTxt = findViewById(R.id.txt_doc_specail);
        edtDescription = findViewById(R.id.edt_description);
        edtRemark = findViewById(R.id.edt_remark);
        btnSaveChanges = findViewById(R.id.btn_save_changes);
        edtImg = findViewById(R.id.pic_edt);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        ageTxt.setText(objModel.getAge()+" yrs");
        txtPatientName.setText(objModel.getFirstName().trim()+" "+objModel.getLastName().trim());
        trackingInfo = new FamilyTrackingInfo();
        listInfo = new ArrayList<>();
        getIllnessData();
    }
    public void clearDate(EditText edtText,ImageView img1,ImageView img2){
        edtText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!edtText.getText().toString().isEmpty()){
                    img1.setVisibility(View.VISIBLE);
                    img2.setVisibility(View.GONE);
                    img1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            edtText.getText().clear();
                            img1.setVisibility(View.GONE);
                            img2.setVisibility(View.VISIBLE);
                        }
                    });

                }

            }
        });
    }

    public void getIllnessData(){
        edtDiseaseName.setText(PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords().get(value).getCurrentStatus());
       /* edtEndDate.setText(PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords().get(value).getEndDate());
        edtStartDate.setText(PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords().get(value).getStartDate());*/
        edtSymptoms.setText(PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords().get(value).getSymptoms());
        edtRemark.setText(PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords().get(value).getIllnessCondition());
        edtDescription.setText(PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords().get(value).getDescription());
        if (PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords().get(value).getStartDate()!=null){
           Log.e("caaa","ddd");
            dateToStramp(edtStartDate,PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords().get(value).getStartDate());
        }
        if (PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords().get(value).getEndDate()!=null){
            Log.e("miaa","ddd");
            dateToStramp(edtEndDate,PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords().get(value).getEndDate());
        }
        currentIllness = PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords().get(value).isCurrentIllness();
       Log.e("cccc","dd"+PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords().get(value).isCurrentIllness());
        if (currentIllness){
            Log.e("cccc","d");
            checkIllness.setChecked(true);
        }else{
            Log.e("cccc","dd");
            checkIllness.setChecked(false);
        }
        String entredDate =  PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords().get(value).getUpdatedDate();
        Log.e("string","date"+entredDate);
        long l = Long.parseLong(entredDate);
        Date currentDate = new Date(l);
        SimpleDateFormat jdff = new SimpleDateFormat("dd/MM/yyyy HH:mm a");
        jdff.setTimeZone(TimeZone.getDefault());
        String java_date = jdff.format(currentDate);
        Date clickedDate = null;
        try {
            clickedDate = jdff.parse(java_date);

            String  formattedDate = jdff.format(clickedDate);
            Log.e("forrr","ff"+formattedDate);
            String[] two = formattedDate.split(" ");
            txtDate.setText(two[0]);
            String[] timeSplit = two[1].split(":");
            if (isHourFormat){
                clockTime = two[1];
            }else{
                if (12 < Integer.parseInt(timeSplit[0])){
                    int hr = Integer.parseInt(timeSplit[0])-12;
                    clockTime = String.valueOf(hr)+":"+timeSplit[1]+" "+two[2];
                }else{
                    clockTime = two[1]+" "+two[2];
                }
            }
            txtTime.setText(clockTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void clickEvents() {
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        edtImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtDiseaseName.setBackgroundResource(R.drawable.edt_txt_background);
                edtDiseaseName.setFocusableInTouchMode(true);
                checkIllness.setEnabled(true);
                edtStartDate.setBackgroundResource(R.drawable.edt_txt_background);
                edtEndDate.setBackgroundResource(R.drawable.edt_txt_background);
                edtSymptoms.setBackgroundResource(R.drawable.edt_txt_background);
                edtSymptoms.setFocusableInTouchMode(true);
                edtDescription.setBackgroundResource(R.drawable.edt_txt_background);
                edtDescription.setFocusableInTouchMode(true);
                edtRemark.setBackgroundResource(R.drawable.edt_txt_background);
                edtRemark.setFocusableInTouchMode(true);
                imgCal.setVisibility(View.VISIBLE);
                imgSche.setVisibility(View.VISIBLE);
                btnSaveChanges.setVisibility(View.VISIBLE);
                edtDiseaseName.setSelection(edtDiseaseName.length());
                edtSymptoms.setSelection(edtSymptoms.length());
                edtDescription.setSelection(edtDescription.length());
                edtRemark.setSelection(edtRemark.length());
                clearDate(edtStartDate,imgClear,imgCal);
                clearDate(edtEndDate,clearImg,imgSche);
                edtStartDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
              /*  cal = true;
                showDialog(999);*/
                        ed =true;
                        selectDate(edtStartDate,imgCal);
                        hideKeyboard(edtStartDate);

                    }
                });
                edtEndDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
               /* cal = false;
                showDialog(999);*/
                        ed=false;
                        selectDate(edtEndDate,imgSche);
                        hideKeyboard(edtEndDate);
                    }
                });
            }
        });

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });
    }
    public void selectDate(EditText editText,ImageView img){
        calendar = Calendar.getInstance();
        mView = LayoutInflater.from(PatientDiseaseActivity.this).inflate(R.layout.popup_calender_view, null, false);
        popUp = new PopupWindow(mView, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, false);
        popUp.setTouchable(true);
        popUp.setFocusable(true);
        popUp.setBackgroundDrawable(new BitmapDrawable());
        popUp.setFocusable(true);
        popUp.setOutsideTouchable(true);
        //Solution
        popUp.showAsDropDown(img);
        calendarView = mView.findViewById(R.id.calendar_view);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.YEAR, 1);
        calendarView.setMaxDate(Calendar.getInstance().getTimeInMillis());
        if (!editText.getText().toString().isEmpty()){
            String[] parts = editText.getText().toString().split("/");
            //Solution
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date date = (Date)formatter.parse(parts[0]+"-"+parts[1]+"-"+parts[2]);
                long out = date.getTime();
                Log.e("tte","af"+out);
                calendarView.setDate(out);
            } catch (ParseException e) {
                Log.e("tte","af"+e);
                e.printStackTrace();
            }


        }

       /* if(ed){

            Log.e("sjdb","sjbjd");

            calendarView.setDate(Long.parseLong(PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords().get(value).getStartDate()));
        }else{
            calendarView.setDate(Long.parseLong(PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords().get(value).getEndDate()));
        }
*/

       // calendarView.setDate(Long.parseLong(PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords().get(value).getStartDate()));

       // calendar.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE));

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String msg = dayOfMonth + "/" + (month + 1) + "/" + year;
                //  editText.setText(msg);
                Log.e("sjdbb","sjbjd"+msg);
                calendar.set(year,month,dayOfMonth);
                editText.setText(msg);

                kk = (calendar.getTimeInMillis())/1000 ;

                Log.e("gfjhfj","hgg:"+kk);
                date = String.valueOf(kk*1000);
                if(ed){
                    sD = date;
                }else{
                    eD = date;
                }
              /*  Log.e("dd","ddd"+sD);
                Log.e("dd","d"+eD);
*/
                popUp.dismiss();
                Date currentDate = new Date(kk*1000);
                SimpleDateFormat jdff = new SimpleDateFormat("dd/MM/yyyy");
                jdff.setTimeZone(TimeZone.getDefault());
                String java_date = jdff.format(currentDate);
                editText.setText(java_date);

                //  popUp.dismiss();
            }
        });
    }
    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.
                ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {

                if (curdOpetaton.equals("update")) {
                  refreshShowingDialog.hideRefreshDialog();
                    try {
                        if (jsonObject.get("response").equals(3)){
                            Log.e("update","updateeee");
                            PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords()
                                    .get(value).setSymptoms(edtSymptoms.getText().toString());
                            PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords()
                                    .get(value).setHospital_reg_num(PatientLoginDataController.getInstance().currentPatientlProfile
                                    .getHospital_reg_number());
                            PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords()
                                    .get(value).setMedical_record_id(PatientLoginDataController.getInstance().currentPatientlProfile
                                    .getMedicalRecordId());
                            PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords()
                                    .get(value).setPatientID(PatientLoginDataController.getInstance().currentPatientlProfile
                                    .getPatientId());
                            PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords()
                                    .get(value).setCurrentStatus(edtDiseaseName.getText().toString());
                            PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords()
                                    .get(value).setDescription(edtDescription.getText().toString());
                            PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords()
                                    .get(value).setIllnessCondition(edtRemark.getText().toString());
                            PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords()
                                    .get(value).setIllnessID( PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords().get(value).getIllnessID());
                            PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords()
                                    .get(value).setCurrentIllness(currentIllness);
/*
                            if (sD==null){

                                }
*/
                            if (!edtStartDate.getText().toString().isEmpty()){
                                String[] parts = edtStartDate.getText().toString().split("/");
                                //Solution
                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

                                Date date = (Date)formatter.parse(parts[0]+"-"+parts[1]+"-"+parts[2]);
                                long out = date.getTime();
                                sD= String.valueOf(out);


                            }
                            if (!edtEndDate.getText().toString().isEmpty()){
                                String[] parts = edtEndDate.getText().toString().split("/");
                                //Solution
                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

                                Date date = (Date)formatter.parse(parts[0]+"-"+parts[1]+"-"+parts[2]);
                                long out = date.getTime();
                                eD= String.valueOf(out);
                            }

/*
                            if (eD==null){
                                {
                                    */
/* eD= PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords()
                                            .get(value).getEndDate(); *//*

                                }                            }
*/
                            Log.e("dddd","j"+sD);
                            Log.e("dddd","jj"+eD);
                            PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords()
                                    .get(value).setStartDate(sD);
                            PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords()
                                    .get(value).setEndDate(eD);

                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                            trackingInfo.setByWhom("patient");
                            trackingInfo.setByWhomID(PatientLoginDataController.getInstance().currentPatientlProfile
                                    .getPatientId());
                            trackingInfo.setByWhomName(PatientLoginDataController.getInstance().currentPatientlProfile
                                    .getFirstName().trim()+" "+PatientLoginDataController.getInstance().currentPatientlProfile.getLastName().trim());
                            trackingInfo.setDate(String.valueOf(timestamp.getTime()));
                            trackingInfo.setInfo("Illness record successfully added");
                            listInfo.add(trackingInfo);
                            PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords()
                                    .get(value).setTracking(listInfo);
                            PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords()
                                    .get(value).setAddedDate(String.valueOf(timestamp.getTime()));
                            PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords()
                                    .get(value).setUpdatedDate(String.valueOf(timestamp.getTime()));

                            Toast.makeText(getApplicationContext(), "illness Records are updated Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PatientDiseaseActivity.this,PatientMedicalHistoryActivity.class)
                                    .putExtra("disease","disease"));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Log.e("settings response","call"+jsonObject.toString());

                }
            }
            @Override
            public void failureCallBack(String failureMsg) {
                refreshShowingDialog.dismiss();
                Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void validation(){

        if (edtDiseaseName.getText().toString().isEmpty()){
            dialogeforCheckavilability("Error", "Please enter disease name", "ok");
        }/*else if (edtStartDate.getText().toString().isEmpty()){
            dialogeforCheckavilability("Error", "Please select start date", "ok");
        }else if (edtEndDate.getText().toString().isEmpty()){
            dialogeforCheckavilability("Error", "Please select end date", "ok");
        }*/else if (edtSymptoms.getText().toString().isEmpty()){
            dialogeforCheckavilability("Error", "Please enter symptoms", "ok");
        }else if (edtDescription.getText().toString().isEmpty()){
            dialogeforCheckavilability("Error", "Please enter description", "ok");
        }else if (edtRemark.getText().toString().isEmpty()){
            dialogeforCheckavilability("Error", "Please enter Remarks", "ok");
        }else{
            if (isNetworkConnected()) {
                refreshShowingDialog.showAlert();
                updateIllnessApi();
                accessInterfaceMethods();
            } else {
                dialogeforCheckavilability("Error", "Please check internet connection", "ok");
            }
        }
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter = new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PatientDiseaseActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);
    }

    public void dateToStramp(EditText textEdit,String timestrap){
        Log.e("string","d"+timestrap);

           try {
               d = Long.parseLong(timestrap);

           } catch (NumberFormatException e) {
               e.printStackTrace();
           }

        Date date1 = null;
        date1 = new Date(d);
        calendar.setTime(date1);
// format of the date
        SimpleDateFormat jdf = new SimpleDateFormat("dd/MM/yyyy");
        jdf.setTimeZone(TimeZone.getDefault());
        String java_date = jdf.format(date1);
        Date date = null;

        try {
            date = jdf.parse(java_date);
            if (i == 0) {
                formattedDate = jdf.format(date);
                Log.e("forrr","fff"+formattedDate);

            }
            textEdit.setText(formattedDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
