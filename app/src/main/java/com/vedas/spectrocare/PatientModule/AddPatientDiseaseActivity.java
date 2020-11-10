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
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientDocResponseModel.TrackingModel;
import com.vedas.spectrocare.PatientServerApiModel.FamilyTrackingInfo;
import com.vedas.spectrocare.PatientServerApiModel.IllnessPatientRecord;
import com.vedas.spectrocare.PatientServerApiModel.PatientIllnessServerResponse;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.PatinetControllers.PatientFamilyDataController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApiModel.IllnessServerObject;
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
import java.util.Locale;
import java.util.TimeZone;

public class AddPatientDiseaseActivity extends AppCompatActivity implements MedicalPersonaSignupView {
    ImageView imgCal, imgSche,imgClear,clearImg;
    boolean cal,ed;
    EditText edtDiseaseName, edtStartDate, edtEndDate, edtSymptoms, edtDescription, edtRemark;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    CalendarView calendarView;
    PopupWindow popUp;
    RefreshShowingDialog refreshShowingDialog;
    Button btnSave;
    boolean currentIllness;
    View mView;
    CheckBox checkIllness;
    String date,sD,eD;
    IllnessPatientRecord patientIllnessRecord;
    PatientIllnessServerResponse illnessServerResponse;
    FamilyTrackingInfo trackingInfo;
    ArrayList<FamilyTrackingInfo> listInfo;
    ArrayList<IllnessPatientRecord> recordArrayList;
    long kk;
    JSONObject addIllnessObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ed=true;
        currentIllness=false;
        setContentView(R.layout.activity_add_patient_disease);
        ImageView imgBack = findViewById(R.id.img_back_arrow);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        casting();
        clickEvents();
    }
    public void casting() {
        imgClear = findViewById(R.id.img_clear);
        clearImg = findViewById(R.id.img_clear2);
        refreshShowingDialog = new RefreshShowingDialog(AddPatientDiseaseActivity.this);
        patientIllnessRecord = new IllnessPatientRecord();
        edtDiseaseName = findViewById(R.id.edt_disease_name);
        edtStartDate = findViewById(R.id.edt_start_date);
        edtEndDate = findViewById(R.id.edt_end_date);
        checkIllness = findViewById(R.id.check_illness);
        edtSymptoms = findViewById(R.id.edt_symptoms);
        imgCal = findViewById(R.id.img_calender);
        imgSche = findViewById(R.id.img_schedule);
        edtDescription = findViewById(R.id.edt_description);
        edtRemark = findViewById(R.id.edt_remark);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        btnSave = findViewById(R.id.btn_save);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        trackingInfo = new FamilyTrackingInfo();
        listInfo = new ArrayList<>();
        recordArrayList = new ArrayList<>();
        illnessServerResponse = new PatientIllnessServerResponse();
        clearDate(edtStartDate,imgClear,imgCal);
        clearDate(edtEndDate,clearImg,imgSche);

    }
    private void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager)AddPatientDiseaseActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
    public void clickEvents() {

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
               // patientDiseaseAddApi();
            }
        });
        edtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*cal = true;
                showDialog(999);*/
                ed=true;
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
                hideKeyboard(edtStartDate);


            }
        });
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
                patientDiseaseAddApi();
            } else {
                refreshShowingDialog.dismiss();
                dialogeforCheckavilability("Error", "Please check internet connection", "ok");
            }
        }
    }

    public void selectDate(EditText editText,ImageView img){
        calendar = Calendar.getInstance();
        mView = LayoutInflater.from(AddPatientDiseaseActivity.this).inflate(R.layout.popup_calender_view, null, false);
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
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String msg = dayOfMonth + "/" + (month + 1) + "/" + year;
              //  editText.setText(msg);
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
                Log.e("dd","ddd"+sD);
                Log.e("dd","d"+eD);

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
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    public void patientDiseaseAddApi(){
        addIllnessObject =new JSONObject();
        try {
            addIllnessObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile
                    .getHospital_reg_number());
            addIllnessObject.put("patientID",PatientLoginDataController.getInstance().currentPatientlProfile
                    .getPatientId());
            addIllnessObject.put("medical_record_id",PatientLoginDataController.getInstance().currentPatientlProfile
                    .getMedicalRecordId());
            addIllnessObject.put("illnessCondition",edtRemark.getText().toString());
            addIllnessObject.put("symptoms",edtSymptoms.getText().toString());
            addIllnessObject.put("currentStatus",edtDiseaseName.getText().toString());
            addIllnessObject.put("description",edtDescription.getText().toString());
            if (checkIllness.isChecked()){
                Log.e("checkd","no");
                currentIllness = true;
            }else{
                Log.e("checking","no");
                currentIllness = false;
            }
            Log.e("isIllness",":"+currentIllness);
            addIllnessObject.put("isCurrentIllness",currentIllness);
            addIllnessObject.put("byWhom","patient");
            addIllnessObject.put("byWhomID",PatientLoginDataController.getInstance().currentPatientlProfile
                    .getPatientId());
            if (edtEndDate.getText().toString().isEmpty()){
                eD=null;
            }
            if (edtEndDate.getText().toString().isEmpty()){
                sD=null;
            }
            addIllnessObject.put("startDate",sD);
            addIllnessObject.put("endDate",eD);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("objecttt", "" + addIllnessObject);

        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(addIllnessObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.addPatientIllness(PatientLoginDataController.getInstance()
                .currentPatientlProfile.getAccessToken(),gsonObject),"add");
        accessInterfaceMethods();

    }
    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.
                ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {

                if (curdOpetaton.equals("add")) {
                    refreshShowingDialog.hideRefreshDialog();
                    try {
                        if (jsonObject.getString("response").equals("3")){
                            Log.e("response","rr"+jsonObject.getString("response"));
                            String id = jsonObject.getString("illnessID");
                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                           // System.out.println(timestamp.getTime());
                            Log.e("illness","ID "+id);
                            Log.e("iess","ID "+timestamp.getTime());
                            trackingInfo.setByWhom("patient");
                            trackingInfo.setByWhomID(PatientLoginDataController.getInstance().currentPatientlProfile
                                    .getPatientId());
                            trackingInfo.setByWhomName(PatientLoginDataController.getInstance().currentPatientlProfile
                                    .getFirstName().trim()+" "+PatientLoginDataController.getInstance().currentPatientlProfile.getLastName().trim());
                            trackingInfo.setDate(String.valueOf(timestamp.getTime()));
                            trackingInfo.setInfo("Illness record successfully added");
                            listInfo.add(trackingInfo);
                            patientIllnessRecord.setTracking(listInfo);

                            Toast.makeText(getApplicationContext(), "Illness record add Successfully", Toast.LENGTH_SHORT).show();
                            Log.e("settings response","call"+jsonObject.toString());
                            if (edtEndDate.getText().toString().isEmpty()){
                                eD=null;
                            }
                            if (edtEndDate.getText().toString().isEmpty()){
                                sD=null;
                            }
                            patientIllnessRecord.setStartDate(sD);
                            patientIllnessRecord.setIllnessID(id);
                            patientIllnessRecord.setEndDate(eD);
                           // PatientFamilyDataController.getInstance().selectedIllnessRecord.setTracking(listInfo);
                            patientIllnessRecord.setMedical_record_id(PatientLoginDataController.getInstance().currentPatientlProfile
                                    .getMedicalRecordId());
                            patientIllnessRecord.setPatientID(PatientLoginDataController.getInstance().currentPatientlProfile
                                    .getPatientId());
                            patientIllnessRecord.setCurrentIllness(currentIllness);
                            patientIllnessRecord.setAddedDate(String.valueOf(timestamp.getTime()));
                            patientIllnessRecord.setUpdatedDate(String.valueOf(timestamp.getTime()));
                            patientIllnessRecord.setCurrentStatus(edtDiseaseName.getText().toString());
                            patientIllnessRecord.setIllnessCondition(edtRemark.getText().toString());
                            patientIllnessRecord.setHospital_reg_num(PatientLoginDataController.getInstance().currentPatientlProfile
                                    .getHospital_reg_number());
                            patientIllnessRecord.setSymptoms(edtSymptoms.getText().toString());
                            patientIllnessRecord.setDescription(edtDescription.getText().toString());
                            recordArrayList.add(patientIllnessRecord);
                            if (PatientFamilyDataController.getInstance().getIllnessPatientList()!=null){
                                Log.e("ddaf","a");
                                PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords().add(patientIllnessRecord);
                            }else{
                                PatientFamilyDataController.getInstance().setIllnessPatientList(recordArrayList);

                                Log.e("ddaf","b");
                                illnessServerResponse.setMessage("Added Sucessfully");
                                illnessServerResponse.setResponse(3);
                                illnessServerResponse.setIllnessRecords(recordArrayList);
                                PatientFamilyDataController.getInstance().setIllnessServerResponse(illnessServerResponse);

                            }
                                                   
                            if (PatientMedicalRecordsController.getInstance().isFromMedication) {
                                startActivity(new Intent(getApplicationContext(), DiseasesListActivity.class));
                                finish();
                            } else {
                                startActivity(new Intent(AddPatientDiseaseActivity.this, PatientMedicalHistoryActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("disease", "disease"));
                                finish();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void failureCallBack(String failureMsg) {
                Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter = new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AddPatientDiseaseActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);
    }
}
