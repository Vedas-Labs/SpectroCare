package com.vedas.spectrocare.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.Controllers.AppointmentsServerObjectDataController;
import com.vedas.spectrocare.Controllers.DoctorInfoServerObjectDataController;
import com.vedas.spectrocare.DataBase.AppointmentDataController;
import com.vedas.spectrocare.DataBase.DoctorInfoDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBaseModels.AppointmentModel;
import com.vedas.spectrocare.DataBaseModels.DoctorInfoModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.adapter.DoctorsRecycleAdapter;
import com.vedas.spectrocare.adapter.ViewMedicalAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public class BookAppointmentsActivity extends AppCompatActivity implements MedicalPersonaSignupView {
    TextView txtDate, txtFromTime, txtTotime;
    String tempbirthDayObj = "";
    ImageView imgBack;
    int year, month, day;
    RadioButton radio_online, radio_onsite;
    EditText edt_reason;
    TextView ed_doctor, ed_department;
    Button btn_booknow;
    RefreshShowingDialog showingDialog;
    AppointmentModel appointmentModel = new AppointmentModel();
    boolean isSelectFromTime = false;
    ArrayList<DoctorInfoModel> doctorInfoModelsList = new ArrayList<>();
    BottomSheetDialog doctorsDialog;
    RecyclerView doctorsRecycleView;
    DoctorsRecycleAdapter doctorsRecycleAdapter;
  //  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dateFormat1 = new SimpleDateFormat("MMM dd, yyyy");
    String timeStamp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointments);
        intializeIds();
        accessInterfaceMethods();
        DoctorInfoDataController.getInstance().fetchDoctorInfoData();
        if (DoctorInfoDataController.getInstance().allDoctorInfo.size() == 0) {
            showingDialog.showAlert();
            DoctorInfoServerObjectDataController.getInstance().fetchHospitaldoctors();
        }
        loadSelectdAppointment();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("onResume", "call");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ViewMedicalAdapter.MessageEvent event) {
        String resultData = event.message.trim();
        if (resultData.equals("loadDoctor")) {// load isFromMedicalPerson doctorsadapter to this
            if (DoctorInfoDataController.getInstance().currentDoctorInfo != null) {
                ed_doctor.setText(DoctorInfoDataController.getInstance().currentDoctorInfo.getDoctorName());
                ed_department.setText(DoctorInfoDataController.getInstance().currentDoctorInfo.getDepartment());
                doctorsDialog.dismiss();
            }
        }

    }

    private void loadSelectdAppointment() {
        if (AppointmentDataController.getInstance().currentAppointmentModel != null) {
            AppointmentModel objModel = AppointmentDataController.getInstance().currentAppointmentModel;
            /*txtDate.setText(objModel.getAppointmentDate());
            txtFromTime.setText(objModel.getAppointmentTimeFrom());
            txtTotime.setText(objModel.getAppointmentTimeTo());*/
            ed_doctor.setText(objModel.getDoctorName());
            ed_department.setText(objModel.getDepartment());
            edt_reason.setText(objModel.getReasonForVisit());
            Log.e("getvisitetype", "call" + objModel.getVisitType());
            if (objModel.getVisitType().equals("Online")) {
                radio_online.setChecked(true);
            } else {
                radio_onsite.setChecked(true);
            }
            btn_booknow.setText("Reschedule");

        }
    }

    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                if (curdOpetaton.equals("fetch")) {
                    Log.e("allergies", "call" + jsonObject.toString());
                } else if (curdOpetaton.equals("insert")) {
                    try {
                        String status = jsonObject.getString("appointmentStatus");
                        String appointmentID = jsonObject.getString("appointmentID");
                        String message = jsonObject.getString("message");
                        String profilePic = jsonObject.getString("doctorProfilePic");

                        Log.e("appointmentdata", "call" + appointmentID + status + profilePic);
                        appointmentModel.setAppointmentID(appointmentID);
                        appointmentModel.setAppointmentStatus(status);
                        appointmentModel.setDoctorProfilePic(ServerApi.img_home_url + profilePic);
                        appointmentModel.setPatientlProfileModel(PatientProfileDataController.getInstance().currentPatientlProfile);
                        if (AppointmentDataController.getInstance().insertAppointmentData(appointmentModel)) {
                            AppointmentDataController.getInstance().fetchAppointmentData(PatientProfileDataController.getInstance().currentPatientlProfile);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (curdOpetaton.equals("reschudule")) {
                    String visitType = null;
                    if (radio_online.isChecked()) {
                        visitType = "Online";
                    } else {
                        visitType = "Onsite";
                    }
                    AppointmentDataController.getInstance().currentAppointmentModel.setAppointmentStatus("Waiting For Confirmation");
                    AppointmentDataController.getInstance().currentAppointmentModel.setAppointmentDate(txtDate.getText().toString());
                    AppointmentDataController.getInstance().currentAppointmentModel.setAppointmentTimeFrom(txtFromTime.getText().toString());
                    AppointmentDataController.getInstance().currentAppointmentModel.setAppointmentTimeTo(txtTotime.getText().toString());
                    AppointmentDataController.getInstance().currentAppointmentModel.setVisitType(visitType);
                    AppointmentDataController.getInstance().currentAppointmentModel.setDoctorName(ed_doctor.getText().toString());
                    AppointmentDataController.getInstance().currentAppointmentModel.setDepartment(ed_department.getText().toString());
                    AppointmentDataController.getInstance().currentAppointmentModel.setReasonForVisit(edt_reason.getText().toString());
                    AppointmentDataController.getInstance().updateAppointmentData(AppointmentDataController.getInstance().currentAppointmentModel);
                    showingDialog.hideRefreshDialog();
                    finish();
                    //startActivity(new Intent(getApplicationContext(),AppointmentsActivity.class));
                } else if (curdOpetaton.equals("fetchDoctor")) {
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("doctors");
                        Log.e("fetchDoctor", "call" + jsonArray.length());
                        DoctorInfoServerObjectDataController.getInstance().processDoctorsData(jsonArray);
                    } catch (JSONException e) {

                    }
                }
                showingDialog.hideRefreshDialog();
            }

            @Override
            public void failureCallBack(String failureMsg) {
                showingDialog.hideRefreshDialog();
                Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void intializeIds() {
        showingDialog = new RefreshShowingDialog(BookAppointmentsActivity.this);
        txtDate = findViewById(R.id.txt_date);
        txtFromTime = findViewById(R.id.txt_time);
        txtTotime = findViewById(R.id.txt_totime);
        imgBack = findViewById(R.id.back_img);
        ed_doctor = findViewById(R.id.edt_doctor);
        ed_department = findViewById(R.id.edt_depart);
        edt_reason = findViewById(R.id.edt_visiting_reason);
        radio_online = findViewById(R.id.btn_on_line);
        radio_onsite = findViewById(R.id.btn_on_site);
        btn_booknow = findViewById(R.id.btn_booknow);
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        tempbirthDayObj = dateFormat.format(currentDate);

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDatePicker();
            }
        });
        txtFromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelectFromTime = true;
                timePicker();
            }
        });
        txtTotime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelectFromTime = false;
                timePicker();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ed_doctor.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                doctorsBottomSheet();
            }
        });
        ed_department.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                doctorsBottomSheet();
            }
        });
        btn_booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validations();
            }
        });
    }

    public void loadDatePicker() {
        final DatePickerDialog dialog;
        if (txtDate.getText().toString().isEmpty()) {
            Log.e("ifcall", "call");
            String[] txtBirthdayArray = tempbirthDayObj.split("-");
            year = Integer.parseInt(txtBirthdayArray[0]);
            month = Integer.parseInt(txtBirthdayArray[1]);
            day = Integer.parseInt(txtBirthdayArray[2]);
            Log.e("textdate", "call" + year + "-" + month + "-" + day);
            dialog = new DatePickerDialog(this, null, year, month - 1, day);
            // dialog.getDatePicker().setMaxDate(new Date().getTime());
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            dialog.show();
        } else {
            tempbirthDayObj = txtDate.getText().toString();
            String[] txtBirthdayArray = tempbirthDayObj.split("-");
            year = Integer.parseInt(txtBirthdayArray[0]);
            month = Integer.parseInt(txtBirthdayArray[1]);
            day = Integer.parseInt(txtBirthdayArray[2]);
            Log.e("textdate", "call" + year + "-" + month + "-" + day);
            dialog = new DatePickerDialog(this, null, year, month - 1, day);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            dialog.show();
        }
        dialog.setButton(DialogInterface.BUTTON_POSITIVE,
                "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog1, int which) {
                        DatePicker objDatePicker = dialog.getDatePicker();

                        year = objDatePicker.getYear();
                        month = objDatePicker.getMonth();
                        day = objDatePicker.getDayOfMonth();
                      //  int month1 = month + 1;
                        //txtDate.setText(year + "-" + checkDigit(month1) + "-" + day);
                        Calendar calendar = new GregorianCalendar(year,month, day);
                        calendar.getTimeInMillis();
                         timeStamp= String.valueOf((calendar.getTimeInMillis()/1000L));
                        Log.e("ageee", "" +timeStamp+dateFormat1.format(calendar.getTime()));
                        txtDate.setText(dateFormat1.format(calendar.getTime()));
                    }
                });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                    }
                });
    }

    public void timePicker() {
        final Calendar c = Calendar.getInstance();
        final int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(BookAppointmentsActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Log.e("txt_dob", "" + txtDate.getText().toString());
                        Calendar datetime = Calendar.getInstance();
                        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        datetime.set(Calendar.MINUTE, minute);
                        if (txtDate.getText().toString().length() > 0) {
                            Date d1 = null;
                            try {
                                d1 = dateFormat1.parse(txtDate.getText().toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Date d2 = new Date();
                            System.out.println("The date 1 is: " + dateFormat1.format(d1));
                            System.out.println("The date 2 is: " + dateFormat1.format(d2));
                            System.out.println("The date 2 is: " + d2.compareTo(d1));
                            if (d1.compareTo(d2) > 0) {
                                System.out.println("Date 1 occurs after Date 2");
                                loadtime(minute,hourOfDay);
                            } else if (d2.compareTo(d1)==1/*dateFormat.format(d1).equals(dateFormat.format(d2))*/) {
                                System.out.println("Date 1 equals Date 2");
                                Calendar calendar = Calendar.getInstance();
                                if(datetime.getTimeInMillis()>=calendar.getTimeInMillis()){
                                    loadtime(minute,hourOfDay);
                                }else {
                                    Toast.makeText(BookAppointmentsActivity.this, "Choose correct time", Toast.LENGTH_LONG).show();
                                }
                            } /*else {
                                Toast.makeText(BookAppointmentsActivity.this, "Choose correct time", Toast.LENGTH_LONG).show();
                            }*/
                        }else {
                            Toast.makeText(BookAppointmentsActivity.this, "Please select Date", Toast.LENGTH_LONG).show();
                        }


                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }
    private void loadtime(int minute,int hourOfDay){
        if (minute > 0 && minute < 9) {
            minute = Integer.parseInt("0" + minute);
        }
        if (hourOfDay > 12) {
            hourOfDay = hourOfDay - 12;
            if (isSelectFromTime) {
                txtFromTime.setText(hourOfDay + ":" + checkDigit(minute) + " PM");
            } else {
                txtTotime.setText(hourOfDay + ":" + checkDigit(minute) + " PM");
            }
        } else {
            if (isSelectFromTime) {
                txtFromTime.setText(hourOfDay + ":" + checkDigit(minute) + " AM");
            } else {
                txtTotime.setText(hourOfDay + ":" + checkDigit(minute) + " AM");
            }
        }
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    private void validations() {
        if (txtDate.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please select date", "Ok");
        } else if (txtFromTime.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please select time ", "Ok");

        } else if (txtTotime.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please select time ", "Ok");

        } else if (ed_doctor.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please enter doctorName", "Ok");

        } else if (ed_department.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please enter department", "Ok");

        } else if (edt_reason.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please enter reason for visit", "Ok");

        } else {
            processDataForBookAppointment();
        }
    }

    private void processDataForBookAppointment() {
        String visitType = null;
        if (radio_online.isChecked()) {
            visitType = "Online";
        } else {
            visitType = "Onsite";
        }
        appointmentModel.setHospital_reg_num(PatientProfileDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
        appointmentModel.setAppointmentDate(txtDate.getText().toString());
        appointmentModel.setAppointmentTimeFrom(txtFromTime.getText().toString());
        appointmentModel.setAppointmentTimeTo(txtTotime.getText().toString());
        appointmentModel.setVisitType(visitType);
        appointmentModel.setDoctorName(ed_doctor.getText().toString());
        appointmentModel.setDepartment(ed_department.getText().toString());
        appointmentModel.setDoctorMedicalPersonnelID(PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalPerson_id());
        appointmentModel.setPatientName(PatientProfileDataController.getInstance().currentPatientlProfile.getFirstName());
        appointmentModel.setPatientID(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
        appointmentModel.setReasonForVisit(edt_reason.getText().toString());
        appointmentModel.setMedicalRecordID(PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
        appointmentModel.setCreatorName(PatientProfileDataController.getInstance().currentPatientlProfile.getFirstName());
        appointmentModel.setCreaterMedicalPersonnelID(PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalPerson_id());

        if (isConn()) {
            showingDialog.showAlert();
            if (AppointmentDataController.getInstance().currentAppointmentModel != null) {
                appointmentModel.setAppointmentID(AppointmentDataController.getInstance().currentAppointmentModel.getAppointmentID());
                AppointmentsServerObjectDataController.getInstance().reschuduleAppointment(appointmentModel);
            } else {
                AppointmentsServerObjectDataController.getInstance().addAppointment(appointmentModel);
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void doctorsBottomSheet() {
        doctorInfoModelsList = DoctorInfoDataController.getInstance().allDoctorInfo;
        View view = getLayoutInflater().inflate(R.layout.doctors_dialog_sheet, null);
        doctorsDialog = new BottomSheetDialog(Objects.requireNonNull(BookAppointmentsActivity.this), R.style.BottomSheetDialogTheme);
        doctorsDialog.setContentView(view);
        doctorsDialog.setCancelable(true);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        doctorsDialog.show();
        ImageView imgClose = doctorsDialog.findViewById(R.id.img_canc);
        EditText ed_search = doctorsDialog.findViewById(R.id.search);
        doctorsRecycleView = doctorsDialog.findViewById(R.id.doc_recycler_view);
        doctorsRecycleView.setNestedScrollingEnabled(false);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doctorsDialog.dismiss();
            }
        });
        getRecyclerView(DoctorInfoDataController.getInstance().allDoctorInfo);
        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateSearchResults(s.toString().toLowerCase());
            }
            @Override
            public void afterTextChanged(Editable s) {
                updateSearchResults(s.toString().toLowerCase());

            }
        });
    }

    private void updateSearchResults(String text) {
        ArrayList<DoctorInfoModel> sortedArray = new ArrayList<>();
        for (DoctorInfoModel object : DoctorInfoDataController.getInstance().allDoctorInfo) {
            if (object.getDoctorName().toLowerCase().startsWith(text) || object.getDepartment().toLowerCase().startsWith(text)) {
                sortedArray.add(object);
            }
        }
        getRecyclerView(sortedArray);
        doctorsRecycleAdapter.notifyDataSetChanged();
    }

    private void getRecyclerView(ArrayList<DoctorInfoModel> list) {
        doctorsRecycleView = doctorsDialog.findViewById(R.id.doc_recycler_view);
        doctorsRecycleAdapter = new DoctorsRecycleAdapter(BookAppointmentsActivity.this, list);
      /*  doctorsRecycleView.setHasFixedSize(true);
        doctorsRecycleView.setNestedScrollingEnabled(false);
        doctorsRecycleView.setVerticalScrollBarEnabled(false);
        doctorsRecycleView.setHorizontalScrollBarEnabled(false);*/
        doctorsRecycleView.setLayoutManager(new LinearLayoutManager(this));
        doctorsRecycleView.setAdapter(doctorsRecycleAdapter);
    }

    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter = new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(BookAppointmentsActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);
    }

    public boolean isConn() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
    }
}
