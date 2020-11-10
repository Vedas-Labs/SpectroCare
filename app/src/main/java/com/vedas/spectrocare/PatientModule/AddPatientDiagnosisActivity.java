package com.vedas.spectrocare.PatientModule;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientServerApiModel.DiagnosisObject;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
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

public class AddPatientDiagnosisActivity extends AppCompatActivity implements MedicalPersonaSignupView {
    CardView clockView;
    ImageView imgClock, imgCal;
    Calendar calendar;
    int year, month, day;
    CalendarView calendarView;
    PopupWindow popUp;
    View mView;
    ImageView imgHrUp, imgHrDown, imgMinUp, imgMinDwn;
    EditText ed_diagnosisname, ed_diagnosisDate, ed_doctorname, ed_prescription, ed_remark;
    RefreshShowingDialog refreshShowingDialog;
    Button btn_save;
    CardView rl_recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient_diagnosis);
        ImageView imgBack = findViewById(R.id.img_back_arrow);
        PatientMedicalRecordsController.getInstance().isFromDiagnosis = false;
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        casting();
        clickListeners();
        accessInterfaceMethods();
       loadCurrentObject();
    }

    public void casting() {
        calendar = Calendar.getInstance();
        refreshShowingDialog = new RefreshShowingDialog(AddPatientDiagnosisActivity.this);
        btn_save = findViewById(R.id.btn_save_changes);
        imgCal = findViewById(R.id.img_calender);
        imgClock = findViewById(R.id.img_clock);
        clockView = findViewById(R.id.card_clock_view);
        ed_diagnosisname = findViewById(R.id.edt_diagnosis_name);
        ed_diagnosisDate = findViewById(R.id.edt_diagnosis_date);
        ed_doctorname = findViewById(R.id.edt_doctor_name);
        ed_prescription = findViewById(R.id.edt_pres);
        ed_remark = findViewById(R.id.edt_remark);
        ed_diagnosisname.setSelection(ed_diagnosisname.length());
        ed_doctorname.setSelection(ed_doctorname.length());
        ed_prescription.setSelection(ed_prescription.length());
        ed_remark.setSelection(ed_remark.length());
    }
    private void loadCurrentObject(){
        if(PatientMedicalRecordsController.getInstance().selectedDiagnosisObject!=null){
            DiagnosisObject obj=PatientMedicalRecordsController.getInstance().selectedDiagnosisObject;
            ed_diagnosisname .setText(obj.getDiagnosis());
            ed_diagnosisDate.setText(obj.getDiagnosisDate());
            ed_doctorname .setText(obj.getDoctorName());
            ed_prescription.setText(obj.getPrescription());
            ed_remark .setText(obj.getRemark());
        }
    }

    public void clickListeners() {
        ed_diagnosisDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(AddPatientDiagnosisActivity.this);

                mView = LayoutInflater.from(AddPatientDiagnosisActivity.this).inflate(R.layout.popup_calender_view, null, false);
                popUp = new PopupWindow(mView, LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, false);
                popUp.setTouchable(true);
                popUp.setFocusable(true);
                popUp.setBackgroundDrawable(new BitmapDrawable());
                popUp.setFocusable(true);
                popUp.setOutsideTouchable(true);
                //Solution
                popUp.showAsDropDown(ed_diagnosisDate);
                calendarView = mView.findViewById(R.id.calendar_view);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                calendar.add(Calendar.YEAR, 1);
                if (!ed_diagnosisDate.getText().toString().isEmpty()){
                    String[] parts = ed_diagnosisDate.getText().toString().split("/");
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
                        ed_diagnosisDate.setText(msg);
                        popUp.dismiss();
                    }
                });
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validations();
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDate(arg1, arg2 + 1, arg3);

                }
            };

    private void showDate(int year, int month, int day) {
        if (ed_diagnosisDate.getText().toString().length() > 0) {
            String date[] = ed_diagnosisDate.getText().toString().split("/");
            calendar.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
        } else {
            ed_diagnosisDate.setText(new StringBuilder().append(day).append("/")
                    .append(month).append("/").append(year));
        }
    }

    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                try {
                    if (jsonObject.getString("response").equals("3")) {
                        Log.e("allergyresponse", "call" + jsonObject.toString());
                        refreshShowingDialog.hideRefreshDialog();
                        if (curdOpetaton.equals("add")) {
                            Gson gson = new Gson();
                            DiagnosisObject diagnosisObject = gson.fromJson(jsonObject.toString(), DiagnosisObject.class);
                            Log.e("ddddddddddddddd", "call" + diagnosisObject.getIllnessDiagnosisID());
                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                            diagnosisObject.setAddedDate(String.valueOf(timestamp.getTime()));
                            diagnosisObject.setUpdatedDaate(String.valueOf(timestamp.getTime()));
                            diagnosisObject.setDiagnosisDate(ed_diagnosisDate.getText().toString());//(String.valueOf(timestamp.getTime()));
                            diagnosisObject.setRemark(ed_remark.getText().toString());
                            diagnosisObject.setDiagnosis(ed_diagnosisname.getText().toString());
                            diagnosisObject.setDoctorName(ed_doctorname.getText().toString());
                            diagnosisObject.setPrescription(ed_prescription.getText().toString());
                            diagnosisObject.setDoctorMedicalPersonnelID("xyz123");
                            if (PatientFamilyDataController.getInstance().selectedIllnessRecord != null) {
                                diagnosisObject.setIllnessID(PatientFamilyDataController.getInstance().selectedIllnessRecord.getIllnessID());
                            } else {
                                diagnosisObject.setIllnessID(PatientMedicalRecordsController.getInstance().selectedDiagnosisObject.getIllnessID());
                            }
                            diagnosisObject.setMedical_record_id(PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
                            diagnosisObject.setPatientID(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
                            diagnosisObject.setByWhomID(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
                            diagnosisObject.setByWhom("Patient");
                            diagnosisObject.setHospital_reg_num(PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());

                            PatientMedicalRecordsController.getInstance().diagnosisObjectArrayList.add(diagnosisObject);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), PatientMedicalHistoryActivity.class).putExtra("diagnosis","diagnosis"));
                        } else if (curdOpetaton.equals("update")) {
                            PatientMedicalRecordsController.getInstance().selectedDiagnosisObject.setDiagnosisDate(ed_diagnosisDate.getText().toString());//(String.valueOf(timestamp.getTime()));
                            PatientMedicalRecordsController.getInstance().selectedDiagnosisObject.setRemark(ed_remark.getText().toString());
                            PatientMedicalRecordsController.getInstance().selectedDiagnosisObject.setDiagnosis(ed_diagnosisname.getText().toString());
                            PatientMedicalRecordsController.getInstance().selectedDiagnosisObject.setDoctorName(ed_doctorname.getText().toString());
                            PatientMedicalRecordsController.getInstance().selectedDiagnosisObject.setPrescription(ed_prescription.getText().toString());
                            finish();
                            startActivity(new Intent(getApplicationContext(), PatientMedicalHistoryActivity.class).putExtra("diagnosis","diagnosis"));
                        finish();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void failureCallBack(String failureMsg) {
                refreshShowingDialog.hideRefreshDialog();
                //Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validations() {
        if (ed_diagnosisname.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please enter diagnosisname", "ok");
        } else if (ed_diagnosisDate.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please select diagnosis date", "ok");
        } else if (ed_doctorname.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please enter doctorname", "ok");
        } else if (ed_prescription.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please enter prescription", "ok");
        } else if (ed_remark.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please enter remark", "ok");
        } else {
            if (isNetworkConnected()) {
                refreshShowingDialog.showAlert();
                if(PatientMedicalRecordsController.getInstance().selectedDiagnosisObject!=null){
                    loadDiagnosisApi(true);
                }else{
                    loadDiagnosisApi(false);
                }
            } else {
                dialogeforCheckavilability("Error", "Please check internet connection", "ok");
            }
        }
    }
    private void loadDiagnosisApi(boolean isUpdate) {
        JSONObject params = new JSONObject();
        try {
            params.put("byWhom", "patient");
            params.put("byWhomID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("medical_record_id", PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
            params.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
            if (isUpdate) {
                params.put("illnessDiagnosisID", PatientMedicalRecordsController.getInstance().selectedDiagnosisObject.getIllnessDiagnosisID());
                params.put("illnessID", PatientMedicalRecordsController.getInstance().selectedDiagnosisObject.getIllnessID());
            } else {
                if (PatientFamilyDataController.getInstance().selectedIllnessRecord != null) {
                    params.put("illnessID", PatientFamilyDataController.getInstance().selectedIllnessRecord.getIllnessID());
                }
            }
            params.put("doctorMedicalPersonnelID", "xyz1234");
            params.put("doctorName", ed_doctorname.getText().toString());
            params.put("diagnosisDate", ed_diagnosisDate.getText().toString());
            params.put("diagnosis", ed_diagnosisname.getText().toString());
            params.put("prescription", ed_prescription.getText().toString());
            params.put("remark", ed_remark.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        if (isUpdate) {
            ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.updateDiagnosisApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "update");
        } else {
            ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.addDiagnosisApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "add");
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter = new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AddPatientDiagnosisActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);
    }
}
