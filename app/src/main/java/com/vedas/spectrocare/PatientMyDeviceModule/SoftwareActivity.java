package com.vedas.spectrocare.PatientMyDeviceModule;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vedas.spectrocare.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SoftwareActivity extends AppCompatActivity {
    ImageView imagerotateanalizine;
    RotateAnimation rotate;
    RelativeLayout rlLoading, rlupdateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software);
        ButterKnife.bind(this);
        imagerotateanalizine = (ImageView) findViewById(R.id.rlimg);
        rlLoading = (RelativeLayout) findViewById(R.id.rlloading);
        rlupdateView = (RelativeLayout) findViewById(R.id.rldevice);
        rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(2000);
        rotate.setRepeatCount(Animation.INFINITE);
        imagerotateanalizine.setAnimation(rotate);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rotate.cancel();
                rlLoading.setVisibility(View.GONE);
                rlupdateView.setVisibility(View.VISIBLE);
            }
        }, 3000);

    }
    @OnClick(R.id.back) void backAction() {
        // TODO call server...
        onBackPressed();
    }
    @OnClick(R.id.btn_download) void downloadAction() {
        // TODO call server...
       showDialog();
    }
    public void showDialog(){
        final Dialog dialog = new Dialog(SoftwareActivity.this);
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

        txt_title.setText("No Updates Available");
        txt_msg.setText("The software of this device");
        txt_msg1.setText("is up to date. No update is available");

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
