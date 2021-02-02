package com.vedas.spectrocare.PatientModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientAppointmentModule.TimeSlotModel;
import com.vedas.spectrocare.PatientChat.ChatDataController;
import com.vedas.spectrocare.PatientChat.MessageModel;
import com.vedas.spectrocare.PatientChat.MessagesListModel;
import com.vedas.spectrocare.PatientChat.SocketIOHelper;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.PatinetControllers.PaymentControll;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.model.PaymentModel;
import com.vedas.spectrocare.model.ServicesModel;
import com.vedas.spectrocare.patientModuleAdapter.BookPatientAppointmentAdapted;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class PatientBookAppointmentActivity extends AppCompatActivity {
    RecyclerView slotGridView;
    ArrayList slotMorningTimingsList, slotNoonTimingsList;
    BookPatientAppointmentAdapted patientAppointmentAdapted;
    ImageView imgRight, imgBack, imgLeft;
    Button btnProceed;
    String formattedDate, docName, docProfession, docId, patientID, docProfile;
    PaymentModel paymentModel;
    EditText edtReason;
    TextView txtTimings, txtSlot, txtYear, txtCurrentDate, txtDocName, txtDocSpecial;
    int i, m, k, timeInminits;
    Socket mSocket;
    RadioButton btnOnline, btnOnSite, btnFirst, btnScond, btnThird;
    String appointmentType, currentDate, duration, timeSlot, java_date, pmTime;
    String timee[];
    CircularImageView docPicture;
    RefreshShowingDialog refreshShowingDialog;
    ArrayList<Integer> intTiming = new ArrayList<Integer>();
    ArrayList<Float> intTimeSlot = new ArrayList<Float>();
    ArrayList<TimeSlotModel> timSlotList = new ArrayList<>();
    HorizontalCalendar horizontalCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_book_appointment);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // when user select appointment , then need to null intialize the
        // invoice object for checking payment action on patientPayemntActivity class.
        PatientMedicalRecordsController.getInstance().invoiceObject = null;
        ////
        mSocket = SocketIOHelper.getInstance().socket;
        mSocket.connect();
        mSocket.on("getTimeSlots", onTimeSlotResponse);
        intial();
        horizontalCalendarView();
        refreshShowingDialog = new RefreshShowingDialog(PatientBookAppointmentActivity.this);
        accessInterfaceMethods();

        if (isNetworkConnected()) {
            refreshShowingDialog.showAlert();
            fetchServiceDetails();
        } else {
            refreshShowingDialog.hideRefreshDialog();
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        //for loading when app open first time with 10 min slot
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat jdff = new SimpleDateFormat("yyyy/MM/dd");
        jdff.setTimeZone(TimeZone.getDefault());
        java_date = jdff.format(currentDate);
        loadFirstSlot();
        ///
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    private void fetchServiceDetails() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
            jsonObject.put("requestedByWhom", "Patient");
            jsonObject.put("requestedPersonID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject body = (JsonObject) jsonParser.parse(jsonObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.fetchServices(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken()
                , body), "fetchServices");
    }
    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String opetation) {
                if (opetation.equals("fetchServices")) {
                    refreshShowingDialog.hideRefreshDialog();
                    try {
                        if (jsonObject.getString("response").equals("3")) {
                            PatientMedicalRecordsController.getInstance().servicesModelArrayList.clear();
                            JSONArray appointmentArray = jsonObject.getJSONArray("services");
                            for (int l = 0; l < appointmentArray.length(); l++) {
                                Gson gson = new Gson();
                                String jsonString = appointmentArray.getJSONObject(l).toString();
                                ServicesModel appointmentList = gson.fromJson(jsonString, ServicesModel.class);
                                PatientMedicalRecordsController.getInstance().servicesModelArrayList.add(appointmentList);
                                Log.e("servieslist", "call" + PatientMedicalRecordsController.getInstance().servicesModelArrayList.size());
                                PatientMedicalRecordsController.getInstance().currentServicesModel = PatientMedicalRecordsController.getInstance().servicesModelArrayList.get(0);
                                Log.e("radio", "check1" + PatientMedicalRecordsController.getInstance().currentServicesModel.getServiceCost());

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void failureCallBack(String failureMsg) {
                refreshShowingDialog.hideRefreshDialog();
                Log.e("erroree", "eer" + failureMsg);
            }
        });
    }

    public void recyclerMethod(ArrayList list) {
        Log.e("lstttt", "sizeeee" + list.size());
        patientAppointmentAdapted = new BookPatientAppointmentAdapted(PatientBookAppointmentActivity.this, list, txtSlot);
        slotGridView.setLayoutManager(new GridLayoutManager(this, 3));
        slotGridView.setAdapter(patientAppointmentAdapted);

    }

    public void horizontalCalendarView() {
        // Calendar endDate = Calendar.getInstance();
        // endDate.add(Calendar.MONTH, 1);
        //  endDate.add(Calendar.MONTH, 1);
        /** start before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        // startDate.add(Calendar.MONTH, 0);
        startDate.add(Calendar.MONTH, 0);

        //  Log.e("fytfgyu", "gvhj" + startDate.getTime());
        //Log.e("fytfgyu", "gvhj" + endDate.getTime());

        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)

                .startDate(startDate.getTime())

                // .endDate(endDate.getTime())

                .datesNumberOnScreen(5)
// Number of Dates cells shown on screen (Recommended 5)

                .dayNameFormat("EEE")
// WeekDay text format

                .dayNumberFormat("dd")
                // Date format

                .monthFormat("MMM")
                // Month format

                .showDayName(true)
// Show or Hide dayName text

                .showMonthName(true)

// Show or Hide month text
                .selectorColor(Color.parseColor("#00000000"))
                .textColor(Color.LTGRAY, Color.WHITE)

                .defaultSelectedDate(Calendar.getInstance().getTime())  // Date to be seleceted at start (default to Today)

                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {

                                                   @Override

                                                   public void onDateSelected(Date date, int position) {
                                                       Log.e("dadf", "dd" + date.getTime());
                                                       currentDate = String.valueOf(date.getTime());
                                                       Log.e("dfaa", "dd" + currentDate);
                                                       horizontalCalendar.setSelectedDateBackground(PatientBookAppointmentActivity.this.getResources().getDrawable(R.drawable.edt_round));
                                                       Date currentDate = new Date(date.getTime());
                                                       SimpleDateFormat jdff = new SimpleDateFormat("yyyy/MM/dd");
                                                       jdff.setTimeZone(TimeZone.getDefault());
                                                       java_date = jdff.format(currentDate);
                                                       SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                                                       SimpleDateFormat parseFormat = new SimpleDateFormat("dd EEEE , MMMM ");
                                                       sdf.setTimeZone(TimeZone.getDefault());
                                                       Date clickedDate = null;
                                                       String yearrr[] = java_date.split("/");
                                                       txtYear.setText(yearrr[0]);
                                                       try {
                                                           clickedDate = jdff.parse(java_date);
                                                           Log.e("fddaa", "dd" + java_date);

                                                           if (i == 0) {

                                                               formattedDate = parseFormat.format(clickedDate);
                                                               // formattedDate = sdf.format(clickedDate);
                                                               Log.e("forrr", "ff" + formattedDate);

                                                           }

                                                       } catch (ParseException e) {
                                                           e.printStackTrace();
                                                       }
                                                       if (isNetworkConnected()) {
                                                           if (btnFirst.isChecked()) {
                                                               checkTimeSlot("tenth", patientID, docId, timeInminits);
                                                               loadFirstSlot();
                                                           } else if (btnScond.isChecked()) {
                                                               checkTimeSlot("twenty", patientID, docId, timeInminits);
                                                               loadSecondSlot();
                                                           } else if (btnThird.isChecked()) {
                                                               checkTimeSlot("half", patientID, docId, timeInminits);
                                                               loadThirdSlot();
                                                           } else {

                                                           }
                                                       } else {
                                                           Toast.makeText(getApplicationContext(), "Please check your onnection", Toast.LENGTH_SHORT).show();
                                                       }
                                                   }

                                                   @Override

                                                   public void onCalendarScroll(HorizontalCalendarView calendarView,

                                                                                int dx, int dy) {

                                                   }

                                                   @Override

                                                   public boolean onDateLongClicked(Date date, int position) {

                                                       return true;


                                                   }

                                               }
        );

    }

    public void intial() {
        patientID = PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId();
        slotMorningTimingsList = new ArrayList();
        slotMorningTimingsList = new ArrayList();
        slotNoonTimingsList = new ArrayList();
        paymentModel = new PaymentModel();
        txtSlot = findViewById(R.id.txt_slot);
        txtYear = findViewById(R.id.txt_year);
        txtCurrentDate = findViewById(R.id.txt_current_year);
        docPicture = findViewById(R.id.img_doc_pic);
        slotGridView = findViewById(R.id.slot_grid_view);
        imgBack = findViewById(R.id.img_back_arrow);
        imgLeft = findViewById(R.id.img_right);
        imgRight = findViewById(R.id.img_left);
        txtTimings = findViewById(R.id.text_day);
        txtDocName = findViewById(R.id.txt_doc_name);
        txtDocSpecial = findViewById(R.id.txt_doc_profession);
        edtReason = findViewById(R.id.edt_reason);
        btnOnline = findViewById(R.id.btn_online);
        btnOnSite = findViewById(R.id.btn_on_site);
        btnProceed = findViewById(R.id.btn_proceed);
        btnFirst = findViewById(R.id.btn_first);
        btnFirst.setChecked(true);
        btnScond = findViewById(R.id.btn_second);
        btnThird = findViewById(R.id.btn_third);

        slotMorningTimingsList.add("9:00 AM");
        slotMorningTimingsList.add("9:30 AM");

        slotNoonTimingsList.add("01:00 PM");
        slotNoonTimingsList.add("01:30 PM");

        Intent intent = getIntent();
        docName = intent.getStringExtra("docName");
        docProfession = intent.getStringExtra("docProfi");
        docProfile = intent.getStringExtra("docProfile");
        if (intent.hasExtra("docId")) {
            docId = intent.getStringExtra("docId");
            Log.e("nameprofile", "odd" + ServerApi.img_home_url + docPicture);
            txtDocName.setText("Dr " + docName);
            txtDocSpecial.setText(docProfession);
            Picasso.get().load(ServerApi.img_home_url + docPicture).placeholder(R.drawable.profile_1).into(docPicture);

            Log.e("docIdd", "odd" + docId);//MPIDwxcf

        } else {
            docId = "";
        }
        Log.e("forrrr", "l13" + slotMorningTimingsList.size());

        long time = System.currentTimeMillis();
        Log.e("radio", "check2" + java_date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss z yyyy");
        String dateString = format.format(new Date(time));
        Log.e("timeee", "tiem" + dateString);
        timee = dateString.split(" ");
        Log.e("timein", "hr" + timee[3]);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        txtCurrentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //      horizontalCalendarView();
                //  horizontalCalendar.selectDate(Calendar.getInstance().getTime(),false);
                horizontalCalendar.goToday(true);

            }
        });
        btnFirst.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    loadFirstSlot();
                }

            }
        });
        btnScond.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    loadSecondSlot();
                }
            }
        });

        btnThird.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    loadThirdSlot();
                }
            }
        });
        if (btnFirst.isChecked()) {
            duration = "10 Mins";
        }
        if (btnScond.isChecked()) {
            Log.e("raddo", "charge");
            duration = "20 Mins";

            if (PatientMedicalRecordsController.getInstance().servicesModelArrayList.size() > 0)
                PatientMedicalRecordsController.getInstance().currentServicesModel = PatientMedicalRecordsController.getInstance().servicesModelArrayList.get(0);
        }
        if (btnThird.isChecked()) {
            Log.e("raddo", "Bo charge");
            duration = "30 Mins";
            if (PatientMedicalRecordsController.getInstance().servicesModelArrayList.size() == 1)
                PatientMedicalRecordsController.getInstance().currentServicesModel = PatientMedicalRecordsController.getInstance().servicesModelArrayList.get(1);
        }
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnOnSite.isChecked()) {
                    Log.e("check", "check1");
                    appointmentType = "Onsite";
                }
                if (btnOnline.isChecked()) {
                    Log.e("check", "check2");
                    appointmentType = "Online";
                }
                if (btnFirst.isChecked()) {
                    Log.e("radio", "check1");
                    duration = "10 mins";
                } else if (btnScond.isChecked()) {
                    duration = "20 mins";
                } else {
                    duration = "30 mins";
                }

                timeSlot = patientAppointmentAdapted.getSelectrdPosition();
                paymentModel.setAppointmentType(appointmentType);
                paymentModel.setDuration(duration);
                paymentModel.setTimeSlot(timeSlot);
                paymentModel.setDocName(docName);
                paymentModel.setDocID(docId);
                if (!edtReason.getText().toString().isEmpty()) {
                    paymentModel.setReasonForVisit(edtReason.getText().toString());
                }
                paymentModel.setDocProfession(docProfession);
                paymentModel.setFormattedDate(currentDate);
                if (PatientMedicalRecordsController.getInstance().currentServicesModel != null) {
                    paymentModel.setServiceCost(PatientMedicalRecordsController.getInstance().currentServicesModel.getServiceCost());
                    paymentModel.setCurrency(PatientMedicalRecordsController.getInstance().currentServicesModel.getCurrency());
                    paymentModel.setServiceVATPercent(PatientMedicalRecordsController.getInstance().currentServicesModel.getServiceVATPercent());
                    paymentModel.setServiceID(PatientMedicalRecordsController.getInstance().currentServicesModel.getServiceID());

                }
                PaymentControll.getInstance().currentPaymentModel = paymentModel;
                if (!timeSlot.isEmpty()) {
                    if (!edtReason.getText().toString().isEmpty()) {
                        Log.e("checkingcost",""+PaymentControll.getInstance().currentPaymentModel.getCurrency()+PaymentControll.getInstance().currentPaymentModel.getServiceCost());
                           startActivity(new Intent(PatientBookAppointmentActivity.this, PatientPaymentActivity.class));

                    } else {
                        Toast.makeText(PatientBookAppointmentActivity.this, "Please enter reason for visit", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PatientBookAppointmentActivity.this, "Please select time slot", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

    private final Emitter.Listener onTimeSlotResponse = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject data = (JSONObject) args[0];
                    Log.e("caaalll", "cc");
                    Log.e("response for socket", " chat message" + data.toString());
                    String response;
                    String message = null;
                    JSONObject jsonObj;
                    slotNoonTimingsList.clear();
                    slotMorningTimingsList.clear();
                    //  patientAppointmentAdapted.notifyDataSetChanged();

                    try {
                        jsonObj = new JSONObject(data.toString());
                        response = jsonObj.getString("response");
                        message = jsonObj.getString("message");
                        Log.e("rreeess", "ponse" + message);
                        if (response.equals("3")) {
                            timSlotList.clear();

                            JSONArray timesArray = data.getJSONArray("timeslots");

                            Log.e("size", "array" + timesArray.length());
                            for (int l = 0; l < timesArray.length(); l++) {
                                Gson gson = new Gson();
                                String jsonString = timesArray.getJSONObject(l).toString();
                                TimeSlotModel timeSlotModel = gson.fromJson(jsonString, TimeSlotModel.class);
                                String kk = gson.toJson(timeSlotModel);
                                Log.e("logaa", "dafaa" + kk);

                                timSlotList.add(timeSlotModel);

                            }
                            Log.e("sixxxxx", "ee " + timSlotList.size());
                            for (int k = 0; k < timSlotList.size(); k++) {
                                String vv = timSlotList.get(k).getTimeInFormat();
                                String cc[] = vv.split(":");
                                int ms = Integer.parseInt(cc[0]);
                                if (ms >= 12) {
                                    if (ms == 12) {
                                        pmTime = vv + " PM";
                                        Log.e("pmV", "alue " + pmTime);
                                    } else {
                                        int hrTime = ms - 12;
                                        String pmTime = hrTime + ":" + cc[1] + " PM";
                                        Log.e("timeIn", "pm " + pmTime);

                                        slotNoonTimingsList.add(pmTime);
                                        txtTimings.setText("Afternoon");

                                        imgLeft.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                txtTimings.setText("Afternoon");
                                                recyclerMethod(slotNoonTimingsList);
                                                Log.e("forrrr", "l2" + slotNoonTimingsList.size());

                                            }
                                        });
                                        Log.e("forrrr", "l0" + slotNoonTimingsList.size());

                                        // recyclerMethod(slotNoonTimingsList);

                                    }

                                } else {
                                    String times = vv + " AM";
                                    Log.e("timeIn", "am" + times);
                                    slotMorningTimingsList.add(times);
                                    Log.e("forrrr", "l1" + slotMorningTimingsList.size());

                                    // recyclerMethod(slotMorningTimingsList);
                                    imgRight.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            txtTimings.setText("Morning");
                                            Log.e("forrrr", "loop" + slotMorningTimingsList.size());
                                            recyclerMethod(slotMorningTimingsList);
                                        }
                                    });

                                }
                            }
                            if (slotMorningTimingsList.isEmpty()) {
                                txtTimings.setText("Afternoon");
                                recyclerMethod(slotNoonTimingsList);
                            } else {
                                txtTimings.setText("Morning");
                                recyclerMethod(slotMorningTimingsList);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Toast.makeText(Chating_activity.this,message,Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private void checkTimeSlot(String interval, String patientID, String docID, int time) {
        JsonObject feedObj = new JsonObject();
        JSONObject jsonObject = new JSONObject();
        Log.e("daaaagg", "da" + java_date);
        try {
            jsonObject.put("interval", interval);
            jsonObject.put("patientID", patientID);
            jsonObject.put("doctorID", docID);
            jsonObject.put("date", "2020/12/01");
            jsonObject.put("time", time);
            JsonParser jsonParser = new JsonParser();
            feedObj = (JsonObject) jsonParser.parse(jsonObject.toString());
            //print parameter
            Log.e("ChatJSON:", " " + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("socket12", "message" + mSocket.id());
        mSocket.emit("getTimeSlots", jsonObject);
        Log.e("socket11", "message" + mSocket.id());
    }

    private void loadFirstSlot() {
        if (timee[0].equals(java_date)) {
            String slitHr[] = timee[1].split(":");
            int hr = Integer.parseInt(slitHr[0]) * 60;
            int mint = Integer.parseInt(slitHr[1]);
            timeInminits = hr + mint;
        } else {
            timeInminits = 0;
        }
        duration = "10 Mins";
        if (isNetworkConnected()) {
            checkTimeSlot("tenth", patientID, docId, timeInminits);
            if (PatientMedicalRecordsController.getInstance().servicesModelArrayList.size() > 0) {
                PatientMedicalRecordsController.getInstance().currentServicesModel = PatientMedicalRecordsController.getInstance().servicesModelArrayList.get(0);
                Log.e("radio", "check1" + PatientMedicalRecordsController.getInstance().currentServicesModel.getServiceCost());
            }
        } else {
            // Toast.makeText(getApplicationContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadSecondSlot() {
        if (timee[0].equals(java_date)) {
            String slitHr[] = timee[1].split(":");
            int hr = Integer.parseInt(slitHr[0]) * 60;
            int mint = Integer.parseInt(slitHr[1]);
            timeInminits = hr + mint;
        } else {
            timeInminits = 0;
        }
        duration = "20 Mins";
        if (isNetworkConnected()) {
            checkTimeSlot("twenty", patientID, docId, timeInminits);
        } else {
            //  Toast.makeText(getApplicationContext(), "Please check your onnection", Toast.LENGTH_SHORT).show();
        }
        if (PatientMedicalRecordsController.getInstance().servicesModelArrayList.size() > 0)
            PatientMedicalRecordsController.getInstance().currentServicesModel = PatientMedicalRecordsController.getInstance().servicesModelArrayList.get(0);
        Log.e("radio", "check2" + PatientMedicalRecordsController.getInstance().currentServicesModel.getServiceCost());

    }

    private void loadThirdSlot() {
        if (timee[0].equals(java_date)) {
            String slitHr[] = timee[1].split(":");
            int hr = Integer.parseInt(slitHr[0]) * 60;
            int mint = Integer.parseInt(slitHr[1]);
            timeInminits = hr + mint;
        } else {
            timeInminits = 0;
        }
        duration = "30 Mins";
        Log.e("timeing", "hr half" + timeInminits);
        if (isNetworkConnected()) {
            checkTimeSlot("half", patientID, docId, timeInminits);
        } else {
            //  Toast.makeText(getApplicationContext(), "Please check your onnection", Toast.LENGTH_SHORT).show();
        }
        // checkTimeSlot("half",patientID,docId,timeInminits);
        if (PatientMedicalRecordsController.getInstance().servicesModelArrayList.size() > 0)
            PatientMedicalRecordsController.getInstance().currentServicesModel = PatientMedicalRecordsController.getInstance().servicesModelArrayList.get(1);
        Log.e("radio", "check3" + PatientMedicalRecordsController.getInstance().currentServicesModel.getServiceCost());


    }
}

