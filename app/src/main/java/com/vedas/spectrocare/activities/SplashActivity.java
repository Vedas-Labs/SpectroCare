package com.vedas.spectrocare.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientModule.PatientHomeActivity;
import com.vedas.spectrocare.R;


public class SplashActivity extends AppCompatActivity {
 private  static int SPLASH_TIME_OUT =3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        MedicalProfileDataController.getInstance().fillContext(getApplicationContext());
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        PatientLoginDataController.getInstance().fetchPatientlProfileData();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(PatientLoginDataController.getInstance().allPatientlProfile.size()>0){
                    startActivity(new Intent(getApplicationContext(), PatientHomeActivity.class));
                    finish();

                }else {
                    Intent patientIntent = new Intent(SplashActivity.this,SelectUserActivity.class);
                    patientIntent.putExtra("patient","1");
                    startActivity(patientIntent);
                    finish();

                }
               // startActivity(new Intent(SplashActivity.this,SelectUserActivity.class));
            }
        }, SPLASH_TIME_OUT);
    }
}
