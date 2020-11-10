package com.vedas.spectrocare.PatientMoreModule;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.vedas.spectrocare.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
public class NotificationActivity extends AppCompatActivity {
    ToggleButton all_toggle,toggle_appointment,togle_bill,toggle_chat;
    LinearLayout li_all;
    String appointmentStr="0";
    String chatStr="0";
    String billStr="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        loadIds();
        readData();
    }
    @OnClick(R.id.back) void backAction() {
        // TODO call server...
        onBackPressed();
    }
    private void loadIds(){
        all_toggle=findViewById(R.id.all);

        toggle_appointment=findViewById(R.id.appointment);
        toggle_chat=findViewById(R.id.chat);
        togle_bill=findViewById(R.id.bill);

        li_all=findViewById(R.id.li_all);
        li_all.setVisibility(View.GONE);

        all_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              if(isChecked){
                  li_all.setVisibility(View.VISIBLE);
                  SettingsActivity.editor.putBoolean("allnotification",true);
                  SettingsActivity.editor.commit();
              }else{
                  li_all.setVisibility(View.GONE);
                  SettingsActivity.editor.putBoolean("allnotification",false);
                  SettingsActivity.editor.commit();
              }
            }
        });
        toggle_appointment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                   appointmentStr="1";
                    SettingsActivity.editor.putBoolean("appointment",true);
                    SettingsActivity.editor.commit();
                }else{
                    appointmentStr="0";
                    SettingsActivity.editor.putBoolean("appointment",false);
                    SettingsActivity.editor.commit();
                }
                checkNotification();
            }
        });
        toggle_chat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    chatStr="1";
                    SettingsActivity.editor.putBoolean("chat",true);
                    SettingsActivity.editor.commit();
                }else{
                    chatStr="0";
                    SettingsActivity.editor.putBoolean("chat",false);
                    SettingsActivity.editor.commit();
                }
                checkNotification();
            }
        });
        togle_bill.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    billStr="1";
                    SettingsActivity.editor.putBoolean("bill",true);
                    SettingsActivity.editor.commit();
                }else{
                    billStr="0";
                    SettingsActivity.editor.putBoolean("bill",false);
                    SettingsActivity.editor.commit();
                }
                checkNotification();
            }
        });
    }
    private void checkNotification(){
        if(toggle_appointment.isChecked()==false && toggle_chat.isChecked()==false && togle_bill.isChecked()==false){
            all_toggle.setChecked(false);
        }else {
            if(toggle_appointment.isChecked()==true || toggle_chat.isChecked()==true || togle_bill.isChecked()==true){
                all_toggle.setChecked(true);
            }
        }
    }
    private void readData(){
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean notification = sharedPreferences.getBoolean("allnotification", false);
        boolean appointment = sharedPreferences.getBoolean("appointment", false);
        boolean chat = sharedPreferences.getBoolean("chat", false);
        boolean bill = sharedPreferences.getBoolean("bill", false);

        if(notification){
            all_toggle.setChecked(true);
            li_all.setVisibility(View.VISIBLE);
        }else{
            all_toggle.setChecked(false);
            li_all.setVisibility(View.GONE);
        }

        if(appointment){
           toggle_appointment .setChecked(true);
        }else{
            toggle_appointment.setChecked(false);
        }

        if(chat){
            toggle_chat .setChecked(true);
        }else{
            toggle_chat.setChecked(false);
        }

        if(bill){
            togle_bill .setChecked(true);
        }else{
            togle_bill.setChecked(false);
        }

    }
}
