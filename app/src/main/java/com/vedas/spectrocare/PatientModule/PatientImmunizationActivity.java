package com.vedas.spectrocare.PatientModule;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientServerApiModel.ImmunizationObject;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.MedicalPersonaSignupView;
import com.vedas.spectrocare.activities.MedicalPersonalSignupPresenter;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class PatientImmunizationActivity extends AppCompatActivity implements MedicalPersonaSignupView {
    ImageView edtImg, imgCal, imgClock;
    SimpleDateFormat formatter;
    RelativeLayout backImg;
    EditText edtVaccineName,edtTime,edtName,edtImmuneNote,edtStartDate;
    private Calendar calendar;
    CalendarView calendarView;
    PopupWindow popUp;
    Timestamp timestamp;
    View mView;
    Button btnImmuneChange;
    RefreshShowingDialog refreshShowingDialog;
    TextView txt_doctorName,txt_createdDate,txt_CreatedTime;
    String clockTime;
    boolean isHourFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_immunization);
        casting();
        clickEvents();
        loadCurrentObject();
        accessInterfaceMethods();
    }

    public void casting(){
        refreshShowingDialog=new RefreshShowingDialog(PatientImmunizationActivity.this);
        btnImmuneChange =findViewById(R.id.btn_save_changes);
        edtStartDate = findViewById(R.id.edt_start_date);
        imgClock = findViewById(R.id.img_time);
        edtImmuneNote = findViewById(R.id.edt_notes);
        edtVaccineName = findViewById(R.id.edt_immun_name);
        edtName = findViewById(R.id.edt_doc_name);
        edtImg = findViewById(R.id.pic_edt);
        edtTime = findViewById(R.id.edt_time);
        imgCal = findViewById(R.id.img_calender);
        backImg = findViewById(R.id.img_back_arrow);
        txt_doctorName=findViewById(R.id.txt_name_of_doc);
        txt_createdDate=findViewById(R.id.txt_doc_date);
        txt_CreatedTime=findViewById(R.id.txt_doc_time);
        edtName.setSelection(edtName.length());
        edtImmuneNote.setSelection(edtImmuneNote.length());
        isHourFormat = getIntent().getBooleanExtra("isHourFormat",false);
    }
    private void loadCurrentObject(){
        if(PatientMedicalRecordsController.getInstance().selectedImmunizationObject!=null){
            ImmunizationObject obj=PatientMedicalRecordsController.getInstance().selectedImmunizationObject;
            edtVaccineName.setText(obj.getImmunizationName());
            edtStartDate.setText(obj.getImmunizationDate());
            edtName.setText(obj.getDoctorName());
            edtImmuneNote.setText(obj.getNotes());
            long millis = Long.parseLong(obj.getCreatedDate());
            Date d = new Date(millis);
            Log.e("time","stamp"+obj.getImmunizationDate());
            SimpleDateFormat weekFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
            String weekString = weekFormatter.format(d);
            String time[]=weekString.split(" ");
            Log.e("weeekarray", "" + time[0]+time[1]+time[2]);
            txt_doctorName.setText("Dr."+obj.getDoctorName());
            txt_createdDate.setText(time[0]);
            txt_CreatedTime.setText(time[1]+" "+time[2]);
            String[] timeSplit = time[1].split(":");

            if (isHourFormat){
                clockTime = time[1];
            }else{
                if (12 < Integer.parseInt(timeSplit[0])){
                    int hr = Integer.parseInt(timeSplit[0])-12;
                    clockTime = String.valueOf(hr)+":"+timeSplit[1]+" "+time[2];
                }else{
                    clockTime = time[1]+" "+time[2];
                }
            }

        }
    }
    private void validations() {
       /* if (edtTime.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please select time", "ok");
        } else*/ if (edtVaccineName.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please enter immunization name", "ok");
        } else if (edtStartDate.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please enter startDate", "ok");
        } else if (edtName.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please enter doctorName", "ok");
        } else if (edtImmuneNote.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please enter note", "ok");
        } else {
            if (isNetworkConnected()) {
                refreshShowingDialog.showAlert();
                loadUpdateImmunizationApi();
            } else {
                dialogeforCheckavilability("Error", "Please check internet connection", "ok");
            }
        }
    }

    private void loadUpdateImmunizationApi() {
        JSONObject params = new JSONObject();
        try {
            params.put("byWhom", "patient");
            params.put("byWhomID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("medical_record_id", PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
            params.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
            params.put("doctorMPID",PatientLoginDataController.getInstance().currentPatientlProfile.getEmailId() );
            params.put("doctorName", edtName.getText().toString());
            params.put("immunizationName",edtVaccineName.getText().toString() );
            params.put("immunizationDate", edtStartDate.getText().toString());
            params.put("notes",edtImmuneNote.getText().toString() );
            params.put("immunization_record_id",PatientMedicalRecordsController.getInstance().selectedImmunizationObject.getImmunization_record_id() );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.updateImmunizationApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "update");
    }
    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                if (curdOpetaton.equals("update")) {
                    try {
                        if (jsonObject.getString("response").equals("3")) {
                            Log.e("allergyresponse", "call" + jsonObject.toString());
                            refreshShowingDialog.hideRefreshDialog();
                            ImmunizationObject obj=PatientMedicalRecordsController.getInstance().selectedImmunizationObject;
                            obj.setImmunizationName(edtVaccineName.getText().toString());
                            obj.setNotes(edtImmuneNote.getText().toString());
                            obj.setDoctorName(edtName.getText().toString());
                            obj.setStartDate(edtStartDate.getText().toString());
                            obj.setImmunizationDate(edtStartDate.getText().toString());
                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                            obj.setCreatedDate(String.valueOf(timestamp.getTime()));
                            obj.setUpdatedDate(String.valueOf(timestamp.getTime()));
                            PatientMedicalRecordsController.getInstance().immunizationArrayList.set(PatientMedicalRecordsController.getInstance().selectedPos,obj);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(),PatientMedicalHistoryActivity.class)
                                    .putExtra("immunization","immunization"));
                        }else{
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    refreshShowingDialog.hideRefreshDialog();
                }
            }
            @Override
            public void failureCallBack(String failureMsg) {
                refreshShowingDialog.hideRefreshDialog();
                //Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter = new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PatientImmunizationActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);
    }
    private void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager)PatientImmunizationActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
        private void loadCalander(){
            calendar = Calendar.getInstance();
            mView = LayoutInflater.from(PatientImmunizationActivity.this).inflate(R.layout.popup_calender_view, null, false);
            popUp = new PopupWindow(mView, LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, false);
            popUp.setTouchable(true);
            popUp.setFocusable(true);
            popUp.setBackgroundDrawable(new BitmapDrawable());
            popUp.setFocusable(true);
            popUp.setOutsideTouchable(true);
           // calendarView.setDate(millis);

            popUp.showAsDropDown(edtStartDate);
            calendarView = mView.findViewById(R.id.calendar_view);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.YEAR, 1);
            String[] parts = edtStartDate.getText().toString().split("/");
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

            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    String msg = dayOfMonth + "/" + (month + 1) + "/" + year;
                    edtStartDate.setText(msg);
                    popUp.dismiss();
                }
            });
        }
    public void clickEvents() {
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }

        });
/*
        edtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pickerDialogs();
                hideKeyboard(edtStartDate);
               loadCalander();
            }
        });
*/
        edtImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnImmuneChange.setVisibility(View.VISIBLE);
                edtStartDate.setBackgroundResource(R.drawable.edt_txt_background);
              //  edtStartDate.setFocusableInTouchMode(true);
                edtVaccineName.setBackgroundResource(R.drawable.edt_txt_background);
                edtVaccineName.setFocusableInTouchMode(true);
                edtTime.setBackgroundResource(R.drawable.edt_txt_background);
                edtTime.setFocusableInTouchMode(true);
                edtName.setBackgroundResource(R.drawable.edt_txt_background);
                edtName.setFocusableInTouchMode(true);
                edtImmuneNote.setBackgroundResource(R.drawable.edt_txt_background);
                edtImmuneNote.setFocusableInTouchMode(true);
                imgCal.setVisibility(View.VISIBLE);
                imgClock.setVisibility(View.VISIBLE);
                edtStartDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideKeyboard(edtStartDate);
                        loadCalander();
                    }
                });
                btnImmuneChange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        validations();
                    }
                });


            }
        });

    }
    }


