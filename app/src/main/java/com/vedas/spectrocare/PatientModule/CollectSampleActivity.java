package com.vedas.spectrocare.PatientModule;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vedas.spectrocare.R;

import java.util.Calendar;

public class CollectSampleActivity extends AppCompatActivity {
    RelativeLayout rl_back;
    Button btn_start,btnCalender,btnClock;
    RelativeLayout calLayout,clockLayout;
    TextView txtTime,txtCal;
    Button btnOk;
    int i,k,j;
    Calendar calendar;
    TextView txtHours,txtMin,txtAm,txtPm;
    CalendarView calendarView;
    String hours,minits;
    PopupWindow popUp;
    View mView;
    ImageView imgHrUp,imgHrDown,imgMinUp,imgMinDwn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_sample);
        loadIds();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              startActivity(new Intent(getApplicationContext(),ResultPageViewActivity.class));
            }
        }, 5000);
    }
    private void loadIds(){
        txtTime = findViewById(R.id.txt_time);
        txtCal = findViewById(R.id.txt_cal);
        rl_back=findViewById(R.id.back);
        btnCalender = findViewById(R.id.btn_calender);
        btnClock = findViewById(R.id.btn_clock);
        clockLayout = findViewById(R.id.layout_time);
        calLayout = findViewById(R.id.rl_one);
        btn_start=findViewById(R.id.start);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnalyzealert();
            }
        });
        btnClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView = LayoutInflater.from(CollectSampleActivity.this).inflate(R.layout.clock_poup_up_layout, null, false);
                popUp = new PopupWindow(mView, LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, false);
                popUp.setTouchable(true);
                popUp.setFocusable(true);
                popUp.setBackgroundDrawable(new BitmapDrawable());
                popUp.setFocusable(true);
                popUp.setOutsideTouchable(true);
                //Solution
                popUp.showAsDropDown(clockLayout);
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
        btnCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                mView = LayoutInflater.from(CollectSampleActivity.this).inflate(R.layout.popup_calender_view, null, false);
                popUp = new PopupWindow(mView, LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, false);
                popUp.setTouchable(true);
                popUp.setFocusable(true);
                popUp.setBackgroundDrawable(new BitmapDrawable());
                popUp.setFocusable(true);
                popUp.setOutsideTouchable(true);
                //Solution
                popUp.showAsDropDown(calLayout);
                calendarView = mView.findViewById(R.id.calendar_view);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                calendar.add(Calendar.YEAR, 1);
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        String msg = dayOfMonth + "/" + (month + 1) + "/" + year;
                        txtCal.setText(msg);
                        popUp.dismiss();
                    }
                });


            }
        });
    }
    public void clockItemsClickListners(){
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clockView.setVisibility(View.GONE);
                String hour = txtHours.getText().toString();
                String minit = txtMin.getText().toString();
                String time = hour+" : "+minit;
                txtTime.setText(time);
                popUp.dismiss();

            }
        });
        txtAm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAm.setBackgroundResource(R.color.colorpink);
                txtPm.setBackgroundResource(R.color.textBackground);

            }
        });
        txtPm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAm.setBackgroundResource(R.color.colorpink);
                txtPm.setBackgroundResource(R.color.textBackground);
            }
        });
        imgMinUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minits = txtMin.getText().toString();
                k = Integer.parseInt(minits);

                if (k == 55){
                    k=0;
                    txtMin.setText(String.valueOf(k));
                }else{
                    i = k+5;
                    txtMin.setText(String.valueOf(i));
                }

            }
        });
        imgMinDwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minits = txtMin.getText().toString();
                k = Integer.parseInt(hours);

                if (k==0){
                    k=55;
                    txtMin.setText(String.valueOf(k));
                }else{
                    i = k-5;
                    txtMin.setText(String.valueOf(i));
                }
            }
        });

        imgHrUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minits = txtMin.getText().toString();
                j = Integer.parseInt(minits);

                hours = txtHours.getText().toString();
                k = Integer.parseInt(hours);

                if (k == 12){
                    k=0;
                    i = k+1;
                    txtHours.setText(String.valueOf(i));
                }else{
                    i = k+1;
                    txtHours.setText(String.valueOf(i));
                }

            }
        });
        imgHrDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hours = txtHours.getText().toString();
                k = Integer.parseInt(hours);

                if (k==1){
                    k=12;
                    txtHours.setText(String.valueOf(k));
                }else{
                    i = k-1;
                    txtHours.setText(String.valueOf(i));
                }
            }
        });

    }

    public void showAbortDialog(){
        final Dialog dialog = new Dialog(CollectSampleActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_abort);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);

        RelativeLayout main=(RelativeLayout)dialog.findViewById(R.id.rl_main);
        RelativeLayout main1=(RelativeLayout)dialog.findViewById(R.id.rl_main1);

        GradientDrawable drawable = (GradientDrawable) main.getBackground();
        drawable.setColor(getResources().getColor(R.color.colorWhite));

        GradientDrawable drawable1 = (GradientDrawable) main1.getBackground();
        drawable1.setColor(getResources().getColor(R.color.colorWhite));

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    private void showAnalyzealert(){
        final Dialog dialog = new Dialog(CollectSampleActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_analyze);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button btnAbort = (Button) dialog.findViewById(R.id.btn_abort);
        GradientDrawable drawable = (GradientDrawable) btnAbort.getBackground();
        drawable.setColor(getResources().getColor(R.color.colorOrange));

        btnAbort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showAbortDialog();
            }
        });
        dialog.show();


    }
}
