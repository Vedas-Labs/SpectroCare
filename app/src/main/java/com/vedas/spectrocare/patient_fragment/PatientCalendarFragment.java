package com.vedas.spectrocare.patient_fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.DataBase.AppointmentDataController;
import com.vedas.spectrocare.DataBaseModels.AppointmentModel;
import com.vedas.spectrocare.PatientAppointmentModule.PatientAppointmentsDataController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.adapter.CalendarAdapter;
import com.vedas.spectrocare.model.AppointmetModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class PatientCalendarFragment extends Fragment {
    NumberPicker yearPicker, monthPicker;
    AppointmetModel model;
    CalendarView calView;
    String selectedYear = "2020", selectedMonth = "jan";
    ImageView calBack, imgToday;
    /* String calMonth,calDay;
     String curDate;*/
    Calendar calendar;
    TextView txtMonth, noAppontments;
    RecyclerView calendarRecyclerView;
    String currentDay;
    com.applandeo.materialcalendarview.CalendarView mCalendarView;
    ArrayList<AppointmetModel> calanderitemsList;
    ArrayList<AppointmentModel> calanderArrayList = new ArrayList<>();
    ArrayList dateList = new ArrayList();
    String monthArray[] = new String[]{"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"};
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    TextView txt_current_date;

    public PatientCalendarFragment() {
        // Required empty public constructor
    }

    public static PatientCalendarFragment newInstance(String param1, String param2) {
        PatientCalendarFragment fragment = new PatientCalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View calendarView = inflater.inflate(R.layout.fragment_patient_calendar, container, false);
        PersonalInfoController.getInstance().loadYearArray();
        txtMonth = calendarView.findViewById(R.id.txt_month);
        calView = calendarView.findViewById(R.id.cal);
        calBack = calendarView.findViewById(R.id.back_arrow);
        imgToday = calendarView.findViewById(R.id.img_today);
        noAppontments = calendarView.findViewById(R.id.txt_no_appointments);
        calendar = Calendar.getInstance();
        mCalendarView = calendarView.findViewById(R.id.m_calendar);
        calendarRecyclerView = calendarView.findViewById(R.id.calendar_recycle_view);
        calanderitemsList = new ArrayList<>();
        txt_current_date = calendarView.findViewById(R.id.txt_current_year);

        // mCalendarView.setDate(calendar.getTime());
        //  calanderArrayList = AppointmentDataController.getInstance().allAppointmentList;
        Log.e("arrayList", "listtttt" + calanderArrayList.size());
        imgToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("cccceeeuu", "dam");
                mCalendarView.setDate(calendar.getTime());
            }
        });
        txt_current_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCurrentDate();
            }
        });
        txtMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("month", "mm" + txtMonth.getText().toString());
                loadYearPicker();
            }
        });


        final SimpleDateFormat ss = new SimpleDateFormat("MMM dd, yyyy");
        //   Date date = new Date();
        currentDay = ss.format(calendar.getTimeInMillis());
        Log.e("currentDate", "" + currentDay);


        if (!PatientAppointmentsDataController.isNull()) {
           /* Log.e("afsafsfd","adf"+PatientAppointmentController.getInstance().getAppointmentList().get(0)
                    .getDate());*/
            if (PatientAppointmentsDataController.getInstance().getAppointmentsList() != null) {
                for (int i = 0; i <= PatientAppointmentsDataController.getInstance().getAppointmentsList().size() - 1; i++) {
                    if(!PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i).getAppointmentDetails().getAppointmentDate().contains("/")){
                        long ll = Long.parseLong(PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i).getAppointmentDetails().getAppointmentDate());
                        Date currentDate = new Date(ll);
                        SimpleDateFormat jdff = new SimpleDateFormat("MMM dd, yyyy");
                        jdff.setTimeZone(TimeZone.getDefault());
                        String java_date = jdff.format(currentDate);
                        if (currentDay.equals(java_date)) {
                            dateList.add(PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i));
                        }
                    }
                }
            }
        }
        recyclerView(dateList, noAppontments);

        calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {

                Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
                calendar.getTimeInMillis();
                String timeStamp = String.valueOf((calendar.getTimeInMillis()));
                Log.e("ageee", "" + timeStamp + ss.format(calendar.getTime()));

                String curDate = ss.format(calendar.getTime());//String.valueOf(year+"-"+calMonth+"-"+calDay);
                dateList.clear();
                if (!PatientAppointmentsDataController.isNull()) {
                    if (PatientAppointmentsDataController.getInstance().getAppointmentsList() != null) {
                        for (int i = 0; i <= PatientAppointmentsDataController.getInstance().getAppointmentsList().size() - 1; i++) {
                            if (!PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i).getAppointmentDetails().getAppointmentDate().contains("/")) {
                                long ll = Long.parseLong(PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i).getAppointmentDetails().getAppointmentDate());
                                Date currentDate = new Date(ll);
                                SimpleDateFormat jdff = new SimpleDateFormat("MMM dd, yyyy");
                                jdff.setTimeZone(TimeZone.getDefault());
                                String java_date = jdff.format(currentDate);
                                Log.e("afdsa", "adfa" + java_date);
                                if (curDate.equals(java_date)) {
                                    //   Log.e("arrayDate","kkk"+calanderitemsList.get(i).getDate());
                                    dateList.add(PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i));
                                    Log.e("chekList", "" + dateList.size());
                                }
                            }
                        }
                    }
                }
/*
                if (dateList.size()==0){
                    noAppontments.setVisibility(View.VISIBLE);
                }
*/
                recyclerView(dateList, noAppontments);
            }
        });

        return calendarView;


    }

    public void recyclerView(ArrayList list, TextView txtView) {
        if (dateList.size() > 0) {
            noAppontments.setVisibility(View.VISIBLE);
        }
        CalendarAdapter calendarAdapter = new CalendarAdapter(getActivity(), list, txtView);
        calendarRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        calendarRecyclerView.setAdapter(calendarAdapter);
    }


    /*
        public  void recyclerView(){
            if (dateList.size() >0){
                noAppontments.setVisibility(View.GONE);
            }
            calanderitemsList.add(new AppointmetModel(R.drawable.profile_1,"DR.BHARATH KUMAR","10 march 2020",
                    "07:00 AM to 08:00 PM","Cancelled"));
            CalendarAdapter calendarAdapter = new CalendarAdapter(getActivity(),list);
            calendarRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            calendarRecyclerView.setAdapter(calendarAdapter);
        }
    */
    private void loadCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        long datre = calendar.getTimeInMillis();
        Date date = new Date(datre);
        calView.setDate(datre);
        mCalendarView.setDate(date);

        final SimpleDateFormat ss = new SimpleDateFormat("MMM dd, yyyy");
        //   Date date = new Date();
        currentDay = ss.format(calendar.getTimeInMillis());
        Log.e("currentDate", "" + currentDay);
        dateList.clear();
        if (!PatientAppointmentsDataController.isNull()) {
            if (PatientAppointmentsDataController.getInstance().getAppointmentsList() != null) {
                for (int i = 0; i <= PatientAppointmentsDataController.getInstance().getAppointmentsList().size() - 1; i++) {
                    long ll = Long.parseLong(PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i).getAppointmentDetails().getAppointmentDate());
                    Date currentDate = new Date(ll);
                    SimpleDateFormat jdff = new SimpleDateFormat("MMM dd, yyyy");
                    jdff.setTimeZone(TimeZone.getDefault());
                    String java_date = jdff.format(currentDate);
                    Log.e("afdsa", ", " + java_date + ", " + currentDay);
                    if (currentDay.equals(java_date)) {
                        dateList.add(PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i));
                    }
                }
            }
        }
        recyclerView(dateList, noAppontments);
    }

    private void loadYearPicker() {
        final Dialog mod = new Dialog(getActivity());
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
        yearPicker.setMaxValue(PersonalInfoController.getInstance().yearArray.size() - 1);
        String[] mStringArray = new String[PersonalInfoController.getInstance().yearArray.size()];
        mStringArray = PersonalInfoController.getInstance().yearArray.toArray(mStringArray);
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
                Log.e("values", "" + yearPicker.getValue());
                Log.e("measure", "" + monthPicker.getValue());
                int month = monthPicker.getValue();
                int year = Integer.parseInt(selectedYear);
                Log.e("checkYear", "" + year);

                calendar.set(year, month, 16);
                long datre = calendar.getTimeInMillis();
                Date date = new Date(datre);
                calView.setDate(datre);
                mCalendarView.setDate(date);

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
