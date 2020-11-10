package com.vedas.spectrocare.PatientModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddPatientImmunizationActivity extends AppCompatActivity implements MedicalPersonaSignupView {
    private int mHour, mMinute;
    CardView clockView;
    EditText edtTime,edtDate;
    ImageView imgClock,imgCal;
    private String format = "";
    Button btnOk;
    int i,k,j;
    Calendar calendar;
    private DatePicker datePicker;
    private int year, month, day;
    TextView txtHours,txtMin,txtAm,txtPm;
    CalendarView calendarView;
    String hours,minits;
    PopupWindow popUp;
    View mView;
    ImageView imgHrUp,imgHrDown,imgMinUp,imgMinDwn;
    EditText ed_immunizationName,ed_startDate,ed_time,ed_doctorNmae,ed_note;
    RefreshShowingDialog refreshShowingDialog;
    Button btn_save;
    PatientAllergyAdapter allergyAdapter;
    RecyclerView recyclerView;
    CardView rl_recyclerView;
    ArrayList<String> typeList = new ArrayList<>();
    int selectedPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient_immunization);
        ImageView imgBack = findViewById(R.id.img_back_arrow);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loadRecyclerView();
        casting();
        clickListeners();
        accessInterfaceMethods();

    }

    public void casting() {
        refreshShowingDialog=new RefreshShowingDialog(AddPatientImmunizationActivity.this);
        btn_save=findViewById(R.id.btn_save_changes);
        imgCal = findViewById(R.id.img_calender);
        edtTime = findViewById(R.id.edt_time);
        edtTime.setFocusable(false);
        edtDate = findViewById(R.id.edt_start_date);
        edtDate.setFocusable(false);
        imgClock = findViewById(R.id.img_clock);
        clockView = findViewById(R.id.card_clock_view);

        ed_immunizationName=findViewById(R.id.edt_immun_name);
        ed_startDate=findViewById(R.id.edt_start_date);
        ed_time=findViewById(R.id.edt_time);
        ed_doctorNmae=findViewById(R.id.edt_symptoms);
        ed_note=findViewById(R.id.edt_notes);

        ed_immunizationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_recyclerView.setVisibility(View.VISIBLE);
                btn_save.setVisibility(View.GONE);
            }
        });
    }
    private void loadRecyclerView(){
        typeList.add("Adenovirus");
        typeList.add("Anthrax");
        typeList.add("Cholera");
        typeList.add("Diphtheria");
        typeList.add("Hepatitis A");
        typeList.add("Hepatitis B");
        typeList.add("Haemophilus influenzae type b (Hib)");
        typeList.add("Human Papillomavirus (HPV)");
        typeList.add("Seasonal Influenza (Flu) only");
        typeList.add("Japanese Encephalitis");
        typeList.add("Measles");
        typeList.add("Meningococcal");
        typeList.add("Mumps");

        rl_recyclerView = findViewById(R.id.rl);
        recyclerView = findViewById(R.id.recycler_view);
        allergyAdapter = new PatientAllergyAdapter(AddPatientImmunizationActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(AddPatientImmunizationActivity.this));
        recyclerView.setAdapter(allergyAdapter);

    }
    public void clickListeners() {
        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(edtDate);
                calendar = Calendar.getInstance();
                mView = LayoutInflater.from(AddPatientImmunizationActivity.this).inflate(R.layout.popup_calender_view, null, false);
                popUp = new PopupWindow(mView, LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, false);
                popUp.setTouchable(true);
                popUp.setFocusable(true);
                popUp.setBackgroundDrawable(new BitmapDrawable());
                popUp.setFocusable(true);
                popUp.setOutsideTouchable(true);
                //Solution
                popUp.showAsDropDown(edtDate);
                calendarView = mView.findViewById(R.id.calendar_view);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                calendar.add(Calendar.YEAR, 1);
                calendarView.setMaxDate(Calendar.getInstance().getTimeInMillis());
                if (!edtDate.getText().toString().isEmpty()){
                    String[] parts = edtDate.getText().toString().split("/");
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
                        edtDate.setText(msg);
                        popUp.dismiss();
                    }
                });
            }
        });
        edtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(edtTime);
                // pickerDialogs();
                mView = LayoutInflater.from(AddPatientImmunizationActivity.this).inflate(R.layout.timepicker_layout, null, false);
                popUp = new PopupWindow(mView, LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, false);
                popUp.setTouchable(true);
                popUp.setFocusable(true);
                popUp.setBackgroundDrawable(new BitmapDrawable());
                popUp.setFocusable(true);
                popUp.setOutsideTouchable(true);
                //Solution
                popUp.showAsDropDown(edtTime);
                imgHrUp = mView.findViewById(R.id.img_arrow_up);
                imgHrDown = mView.findViewById(R.id.img_arrow_down);
                imgMinUp = mView.findViewById(R.id.img_up_arrow);
                imgMinDwn = mView.findViewById(R.id.img_dwn_arrow);
                TimePicker simpleTimePicker = mView.findViewById(R.id.clock_picker);
                simpleTimePicker.setIs24HourView(false);
                txtHours = mView.findViewById(R.id.txt_hour);
                txtMin = mView.findViewById(R.id.txt_minit);
                Button okBtn = mView.findViewById(R.id.button_ok);
                txtAm = mView.findViewById(R.id.txt_am);
                txtPm = mView.findViewById(R.id.txt_pm);
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int hour, minute;
                        String am_pm;
                        if (Build.VERSION.SDK_INT >= 23 ){
                            hour = simpleTimePicker.getHour();
                            minute = simpleTimePicker.getMinute();
                        }
                        else{
                            hour = simpleTimePicker.getCurrentHour();
                            minute = simpleTimePicker.getCurrentMinute();
                        }
                        if(hour > 12) {
                            am_pm = "PM";
                            hour = hour - 12;

                        }
                        else
                        {
                            am_pm="AM";
                        }
                        if (hour<10){
                            hours = "0"+hour;
                        }else{
                            hours= String.valueOf(hour);
                        }
                        if (minute<10){
                            minits = "0"+minute;
                        }else{
                            minits = String.valueOf(minute);
                        }

                        edtTime.setText( hours +" : "+ minits+" "+am_pm);
                        popUp.dismiss();

                    }

                });

            }
        });

/*
        imgClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pickerDialogs();
              //  clockView.setVisibility(View.VISIBLE);
               mView = LayoutInflater.from(AddPatientImmunizationActivity.this).inflate(R.layout.clock_poup_up_layout, null, false);
               popUp = new PopupWindow(mView, LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, false);
                popUp.setTouchable(true);
                popUp.setFocusable(true);
                popUp.setBackgroundDrawable(new BitmapDrawable());
                popUp.setFocusable(true);
                popUp.setOutsideTouchable(true);
                //Solution
                popUp.showAsDropDown(edtTime);
                imgHrUp = mView.findViewById(R.id.img_arrow_up);
                imgHrDown = mView.findViewById(R.id.img_arrow_down);
                imgMinUp = mView.findViewById(R.id.img_up_arrow);
                imgMinDwn = mView.findViewById(R.id.img_dwn_arrow);
                txtHours = mView.findViewById(R.id.txt_hour);
                txtMin = mView.findViewById(R.id.txt_minit);
                btnOk = mView.findViewById(R.id.btn_ok);
                txtAm = mView.findViewById(R.id.txt_am);
                txtPm = mView.findViewById(R.id.txt_pm);

                clockItemsClickListners();

            }
        });
*/
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
    private void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager)AddPatientImmunizationActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
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

        edtDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    public void showTime(int hour, int min) {
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        edtTime.setText(new StringBuilder().append(hour).append(" : ").append(min)
                .append(" ").append(format));
    }

    public void pickerDialogs() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        showTime(hourOfDay, minute);
                        //   edtTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                if (curdOpetaton.equals("add")) {
                    try {
                        if (jsonObject.getString("response").equals("3")) {
                            Log.e("allergyresponse", "call" + jsonObject.toString());
                            refreshShowingDialog.hideRefreshDialog();
                            Gson gson = new Gson();
                            ImmunizationObject immunizationObject = gson.fromJson(jsonObject.toString(), ImmunizationObject.class);

                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                            immunizationObject.setCreatedDate(String.valueOf(timestamp.getTime()));

                            immunizationObject.setNotes(ed_note.getText().toString());
                            immunizationObject.setImmunizationName(ed_immunizationName.getText().toString());
                            immunizationObject.setDoctorName(ed_doctorNmae.getText().toString());
                            immunizationObject.setStartDate(edtDate.getText().toString());
                            immunizationObject.setTime(edtTime.getText().toString());
                            Log.e("startDate", "call" + edtDate.getText().toString()+ed_startDate.getText().toString());

                            PatientMedicalRecordsController.getInstance().immunizationArrayList.add(immunizationObject);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(),PatientMedicalHistoryActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("immunization","immunization"));
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
    private void validations() {
        if (ed_time.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please select time", "ok");
        } else if (ed_immunizationName.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please enter immunization name", "ok");
        } else if (ed_startDate.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please enter startDate", "ok");
        } else if (ed_doctorNmae.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please enter doctorName", "ok");
        } else if (ed_note.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please enter note", "ok");
        } else {
            if (isNetworkConnected()) {
                refreshShowingDialog.showAlert();
                loadAddImmunizationApi();
            } else {
                dialogeforCheckavilability("Error", "Please check internet connection", "ok");
            }
        }
    }

    private void loadAddImmunizationApi() {
        JSONObject params = new JSONObject();
        try {
            params.put("byWhom", "patient");
            params.put("byWhomID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("medical_record_id", PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
            params.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
            params.put("doctorMPID",PatientLoginDataController.getInstance().currentPatientlProfile.getEmailId() );
            params.put("doctorName", ed_doctorNmae.getText().toString());
            params.put("immunizationName",ed_immunizationName.getText().toString() );
            params.put("immunizationDate", ed_startDate.getText().toString());
            params.put("notes",ed_note.getText().toString() );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.addImmunizationApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "add");
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter = new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AddPatientImmunizationActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);
    }
    public class PatientAllergyAdapter extends RecyclerView.Adapter<PatientAllergyAdapter.AllergyHolder> {
        Context context;

        public PatientAllergyAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public AllergyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View allergyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_text, parent, false);
            return new AllergyHolder(allergyView);
        }

        @Override
        public void onBindViewHolder(@NonNull AllergyHolder holder, int position) {
            holder.allergyName.setText(typeList.get(position));
            if (selectedPos == position) {
                holder.relativeLayout.setBackgroundColor(Color.parseColor("#E9F9FB"));
                ed_immunizationName.setText(typeList.get(selectedPos));
                rl_recyclerView.setVisibility(View.GONE);
                btn_save.setVisibility(View.VISIBLE);
            } else {
                holder.relativeLayout.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedPos != position) {
                        selectedPos = position;
                        notifyDataSetChanged();
                    } else {
                        selectedPos = -1;
                        notifyDataSetChanged();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return typeList.size();
        }

        public class AllergyHolder extends RecyclerView.ViewHolder {
            TextView allergyName;
            RelativeLayout relativeLayout;

            public AllergyHolder(@NonNull View itemView) {
                super(itemView);
                relativeLayout = itemView.findViewById(R.id.layout_status);
                allergyName = itemView.findViewById(R.id.item_spinner);
            }
        }
    }
}
