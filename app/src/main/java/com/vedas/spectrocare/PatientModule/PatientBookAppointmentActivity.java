package com.vedas.spectrocare.PatientModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.vedas.spectrocare.PatinetControllers.PaymentControll;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.model.PaymentModel;
import com.vedas.spectrocare.patientModuleAdapter.BookPatientAppointmentAdapted;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class PatientBookAppointmentActivity extends AppCompatActivity {
    RecyclerView slotGridView;
    ArrayList slotMorningTimingsList,slotNoonTimingsList;
    BookPatientAppointmentAdapted patientAppointmentAdapted;
    ImageView imgRight,imgLeft;
    Button btnProceed;
    String formattedDate,docName,docProfession,docId;
    PaymentModel paymentModel;
    EditText edtReason;
    TextView txtTimings;
    int i;
    RadioButton btnOnline,btnOnSite,btnFirst,btnScond,btnThird;
    String appointmentType,currentDate,duration,timeSlot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_book_appointment);
       intial();
       horizontalCalendarView();

    }
    public void recyclerMethod(ArrayList list){
        patientAppointmentAdapted = new BookPatientAppointmentAdapted(PatientBookAppointmentActivity.this,list);
        slotGridView.setLayoutManager(new GridLayoutManager(this, 3));
        slotGridView.setAdapter(patientAppointmentAdapted);

    }
    public void horizontalCalendarView(){
        Calendar endDate = Calendar.getInstance();

      //  endDate.add(Calendar.MONTH, 1);
        /** start before 1 month from now */ Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, 0);

        Log.e("fytfgyu","gvhj"+startDate.getTime());
        Log.e("fytfgyu","gvhj"+endDate.getTime());

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)

                .startDate(startDate.getTime())

              //  .endDate(endDate.getTime())

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

                .showMonthName(false)
// Show or Hide month text
                .selectorColor(Color.parseColor("#00000000"))
                .textColor(Color.LTGRAY, Color.WHITE)

                .defaultSelectedDate(Calendar.getInstance().getTime())  // Date to be seleceted at start (default to Today)

                .build();



        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {

                                                   @Override

                                                   public void onDateSelected(Date date, int position) {
                                                       Log.e("dadf","dd"+date.getTime());
                                                       currentDate= String.valueOf(date.getTime());
                                                       horizontalCalendar.setSelectedDateBackground(PatientBookAppointmentActivity.this.getResources().getDrawable(R.drawable.edt_round));
                                                       Date currentDate = new Date(date.getTime());
                                                       SimpleDateFormat jdff = new SimpleDateFormat("yyyy-MM-dd");
                                                       jdff.setTimeZone(TimeZone.getDefault());
                                                       String java_date = jdff.format(currentDate);
                                                       SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                                                       SimpleDateFormat parseFormat = new SimpleDateFormat("dd EEEE , MMMM "); sdf.setTimeZone(TimeZone.getDefault());
                                                       Date clickedDate = null;
                                                       try {
                                                           clickedDate = jdff.parse(java_date);

                                                           if (i == 0) {

                                                               formattedDate = parseFormat.format(clickedDate);
                                                              // formattedDate = sdf.format(clickedDate);
                                                               Log.e("forrr","ff"+formattedDate);

                                                           }

                                                       } catch (ParseException e) {
                                                           e.printStackTrace();
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
    public void intial(){
        slotMorningTimingsList = new ArrayList();
        slotMorningTimingsList = new ArrayList();
        slotNoonTimingsList = new ArrayList();
        paymentModel = new PaymentModel();
        slotGridView =findViewById(R.id.slot_grid_view);
        imgLeft = findViewById(R.id.img_right);
        imgRight = findViewById(R.id.img_left);
        txtTimings = findViewById(R.id.text_day);
        edtReason = findViewById(R.id.edt_reason);
        btnOnline = findViewById(R.id.btn_online);
        btnOnSite = findViewById(R.id.btn_on_site);
        btnProceed = findViewById(R.id.btn_proceed);
        btnFirst = findViewById(R.id.btn_first);
        btnScond = findViewById(R.id.btn_second);
        btnThird = findViewById(R.id.btn_third);

        slotMorningTimingsList.add("9:00 AM");
        slotMorningTimingsList.add("9:30 AM");
        slotMorningTimingsList.add("9:40 AM");
        slotMorningTimingsList.add("10:00 AM");
        slotMorningTimingsList.add("10:30 AM");
        slotMorningTimingsList.add("10:40 AM");
        slotMorningTimingsList.add("11:00 AM");
        slotMorningTimingsList.add("11:30 AM");
        slotMorningTimingsList.add("11:40 AM");

        slotNoonTimingsList.add("01:00 PM");
        slotNoonTimingsList.add("01:30 PM");
        slotNoonTimingsList.add("02:00 PM");
        slotNoonTimingsList.add("02:30 PM");
        slotNoonTimingsList.add("03:00 PM");
        slotNoonTimingsList.add("03:20 PM");
        slotNoonTimingsList.add("03:40 PM");

        recyclerMethod(slotMorningTimingsList);

        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtTimings.setText("Afternoon");
                recyclerMethod(slotNoonTimingsList);
            }
        });
        imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtTimings.setText("Morning");
                recyclerMethod(slotMorningTimingsList);
            }
        });
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnOnSite.isChecked()){
                    Log.e("check","check1");
                    appointmentType="Onsite";
                }if (btnOnline.isChecked()){
                    Log.e("check","check2");
                    appointmentType ="Online";
                }
                if (btnFirst.isChecked()){
                    Log.e("radio","check1");
                    duration="10 mins";
                }if (btnScond.isChecked()){
                    Log.e("radio","check2");
                    duration="20 mins";
                }if (btnThird.isChecked()){
                    Log.e("radio","check3");
                    duration="30 mins";
                }
                Intent intent = getIntent();
                docName = intent.getStringExtra("docName");
                docProfession = intent.getStringExtra("docProfi");
                if (intent.hasExtra("docId")){
                    docId = intent.getStringExtra("docId");
                }else{
                    docId  ="";
                }
                timeSlot = patientAppointmentAdapted.getSelectrdPosition();
                paymentModel.setAppointmentType(appointmentType);
                paymentModel.setDuration(duration);
                paymentModel.setTimeSlot(timeSlot);
                paymentModel.setDocName(docName);
                paymentModel.setDocID(docId);
                if (!edtReason.getText().toString().isEmpty()){
                    paymentModel.setReasonForVisit(edtReason.getText().toString());
                }
                paymentModel.setDocProfession(docProfession);
                paymentModel.setFormattedDate(currentDate);
                Log.e("paadf","sgdg"+duration);
                Log.e("paadf","df"+patientAppointmentAdapted.getSelectrdPosition());
                Log.e("intentss","fdds"+appointmentType+"/n"+duration+"/n"+currentDate);
                startActivity(new Intent(PatientBookAppointmentActivity.this,PatientPaymentActivity.class));
                PaymentControll.getInstance().setPaymentModel(paymentModel);
            }

        });
    }

}

