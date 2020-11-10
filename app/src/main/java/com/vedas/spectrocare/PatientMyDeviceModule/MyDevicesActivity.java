package com.vedas.spectrocare.PatientMyDeviceModule;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.vedas.spectrocare.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyDevicesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_devices);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.back) void backAction() {
        // TODO call server...
        onBackPressed();
    }
    @OnClick(R.id.rlunpair) void Action() {
        showunpairDialog();
    }
    @OnClick(R.id.rlforgot) void forgotAction() {
        showForgotDialog();
    }
    @OnClick(R.id.softwaredevice) void aboutAction() {
        startActivity(new Intent(getApplicationContext(),SoftwareActivity.class));
    }
    @OnClick(R.id.aboutdevice) void aboutdeviceAction() {
        startActivity(new Intent(getApplicationContext(),AboutDeviceActiivty.class));
    }
    public void showunpairDialog(){
        final Dialog dialog = new Dialog(MyDevicesActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_abort);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
        btnNo.setText("Cancel");
        btnYes.setText("Yes");

        TextView txt_title=dialog.findViewById(R.id.title);
        TextView txt_msg=dialog.findViewById(R.id.msg);
        TextView txt_msg1=dialog.findViewById(R.id.msg1);

        txt_title.setText("Unpair Device");
        txt_msg.setText("You want to unpair");
        txt_msg1.setText("this device?");

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
    public void showForgotDialog(){
        final Dialog dialog = new Dialog(MyDevicesActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_abort);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
        btnNo.setText("Cancel");
        btnYes.setText("Yes");

        TextView txt_title=dialog.findViewById(R.id.title);
        TextView txt_msg=dialog.findViewById(R.id.msg);
        TextView txt_msg1=dialog.findViewById(R.id.msg1);

        txt_title.setText("Forgot Device");
        txt_msg.setText("You want to forgot the");
        txt_msg1.setText("settings of the device?");

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
}
