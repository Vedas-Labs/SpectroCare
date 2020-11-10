package com.vedas.spectrocare.PatientModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.DataBase.AppointmentDataController;
import com.vedas.spectrocare.DataBaseModels.AppointmentModel;
import com.vedas.spectrocare.PatinetControllers.PatientAppointmentController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.CalendarActivity;
import com.vedas.spectrocare.adapter.CalendarAdapter;
import com.vedas.spectrocare.model.AppointmetModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class PatientCalanderActivity extends AppCompatActivity {
    NumberPicker yearPicker, monthPicker;
    CalendarView calView;
    String selectedYear="2020",selectedMonth="jan";
    ImageView calBack;
    /* String calMonth,calDay;
     String curDate;*/
    Calendar calendar;
    TextView txtMonth,noAppontments;
    AppointmetModel model;
    RecyclerView calendarRecyclerView;
    ArrayList<AppointmetModel> calanderitemsList;
  //  ArrayList<AppointmentModel> calanderArrayList = new ArrayList<>();
    ArrayList dateList = new ArrayList();
    String monthArray[]=new String[]{"jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_calander);
        if (!PatientAppointmentController.isNull()){
            for (int i=0;i<PatientAppointmentController.getInstance().getAppointmentList().size();i++){
                model = new AppointmetModel();
                model.setDate(PatientAppointmentController.getInstance().getAppointmentList().get(i).getDate());
                model.setSpecialization(PatientAppointmentController.getInstance().getAppointmentList().get(i).getSpecialization());
                model.setTime(PatientAppointmentController.getInstance().getAppointmentList().get(i).getTimeSlot());
                model.setDocName(PatientAppointmentController.getInstance().getAppointmentList().get(i).getDocName());
                calanderitemsList.add(model);
            }

        }
        txtMonth = findViewById(R.id.txt_month);
        calView = findViewById(R.id.cal);
        calBack = findViewById(R.id.back_arrow);
        noAppontments = findViewById(R.id.txt_no_appointments);
        calendar = Calendar.getInstance();
        calendarRecyclerView = findViewById(R.id.calendar_recycle_view);
        calanderitemsList = new ArrayList<>();
      /*  calanderArrayList = AppointmentDataController.getInstance().allAppointmentList;
        Log.e("arrayList",""+calanderArrayList.size());*/
        txtMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadYearPicker();
            }
        });
        calBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        final SimpleDateFormat ss = new SimpleDateFormat("MMM dd, yyyy");
        Date date = new Date();
        String currentdate= ss.format(date);
        Log.e("currentDate",""+currentdate);

/*
        for (int i =0;i<=calanderArrayList.size()-1;i++){
            if (currentdate.equals(calanderArrayList.get(i).getAppointmentDate())){
                if(calanderArrayList.get(i)==null){
                    noAppontments.setVisibility(View.VISIBLE);
                }
                Log.e("arrayDate","kkk"+calanderArrayList.get(i).getAppointmentDate());
                dateList.add(calanderArrayList.get(i));

            }
        }
*/
        if (dateList.size()==0){
            noAppontments.setVisibility(View.VISIBLE);
        }else {
            noAppontments.setVisibility(View.GONE);
        }
        recyclerView(dateList);

        calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
               /* int m = month+1;
                int d = dayOfMonth;

                if (m<=9){
                    calMonth= String.valueOf("0"+m);
                }else{
                    calMonth=String.valueOf(m);
                }
                if (d<=9){
                    calDay= String.valueOf("0"+d);
                }else{
                    calDay=String.valueOf(d);
                }*/
                Calendar calendar = new GregorianCalendar(year,month, dayOfMonth);
                calendar.getTimeInMillis();
                String timeStamp= String.valueOf((calendar.getTimeInMillis()));
                Log.e("ageee", "" +timeStamp+ss.format(calendar.getTime()));

                String curDate =ss.format(calendar.getTime());//String.valueOf(year+"-"+calMonth+"-"+calDay);
                dateList.clear();
                for (int i =0;i<=calanderitemsList.size()-1;i++){
                    if (curDate.equals(calanderitemsList.get(i).getDate())){
                        Log.e("arrayDate","kkk"+calanderitemsList.get(i).getDate());
                        dateList.add(calanderitemsList.get(i));
                        Log.e("chekList",""+dateList);
                    }
                }
                if (dateList.size()==0){
                    noAppontments.setVisibility(View.VISIBLE);
                }
                recyclerView(dateList);
            }
        });


    }


    public  void recyclerView(ArrayList list){
        if (dateList.size() >0){
            noAppontments.setVisibility(View.GONE);
        }
        CalendarAdapter calendarAdapter = new CalendarAdapter(PatientCalanderActivity.this,list);
        calendarRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        calendarRecyclerView.setAdapter(calendarAdapter);
    }
    private void loadYearPicker(){
        final Dialog mod = new Dialog(PatientCalanderActivity.this);
        mod.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mod.setContentView(R.layout.alert_dailog);
        mod.show();
        TextView txtTitle = (TextView) mod.findViewById(R.id.title);
        txtTitle.setText("Select Year and month");
        yearPicker = mod.findViewById(R.id.value);

        ////for value array
        yearPicker.setDisplayedValues(null);
        int index = PersonalInfoController.getInstance().yearArray.indexOf(selectedYear);
        yearPicker.setMinValue(0);
        yearPicker.setWrapSelectorWheel(false);
        yearPicker.setMaxValue( PersonalInfoController.getInstance().yearArray.size() - 1);
        String[] mStringArray = new String[ PersonalInfoController.getInstance().yearArray.size()];
        mStringArray =  PersonalInfoController.getInstance().yearArray.toArray(mStringArray);
        yearPicker.setDisplayedValues(mStringArray);
        yearPicker.setValue(index);
        //for month
        monthPicker = (NumberPicker) mod.findViewById(R.id.name);
        monthPicker.setWrapSelectorWheel(false);
        monthPicker.setMaxValue(11);
        monthPicker.setMinValue(0);
        monthPicker.setDisplayedValues(monthArray);
        int monthIndex = Arrays.asList(monthArray).indexOf(selectedMonth);
        monthPicker.setValue(monthIndex);

        yearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedYear = PersonalInfoController.getInstance().yearArray.get(newVal);
            }
        });

        monthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedMonth = monthArray[newVal];
            }
        });


        Button btnOk = mod.findViewById(R.id.ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mod.dismiss();
                Log.e("values",""+yearPicker.getValue());
                Log.e("measure",""+monthPicker.getValue());
                int month = monthPicker.getValue();
                int year = Integer.parseInt(selectedYear);
                Log.e("checkYear",""+year);

                calendar.set(year,month,16);
                long datre = calendar.getTimeInMillis();
                calView.setDate(datre);

                //  edt_waistline.setText(selectedWaistLine+" "+selectedWaistMeasure);
            }
        });
        Button btnCancel = mod.findViewById(R.id.cancle);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mod.dismiss();
            }
        });
    }
}
