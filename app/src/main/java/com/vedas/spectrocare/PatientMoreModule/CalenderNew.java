package com.vedas.spectrocare.PatientMoreModule;

import androidx.appcompat.app.AppCompatActivity;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;

import android.graphics.Color;
import android.os.Bundle;

import com.vedas.spectrocare.R;

import java.util.Calendar;
import java.util.Date;

public class CalenderNew extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_new);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);
        /** start before 1 month from now */ Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);
        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)

                .startDate(startDate.getTime())

                .endDate(endDate.getTime())

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

                .textColor(Color.LTGRAY, Color.WHITE)
                // Text color for none selected Dates, Text color for selected Date.

               // .selectedDateBackground(Drawable)  // Background Drawable of the selected date cell.

                .selectorColor(Color.RED)
// Color of the selection indicator bar (default to colorAccent).

                .defaultSelectedDate(Calendar.getInstance().getTime())  // Date to be seleceted at start (default to Today)

                .build();
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {

                                                  @Override

                                                  public void onDateSelected(Date date, int position) {


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

}