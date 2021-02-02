package com.vedas.spectrocare.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientModule.PatientHomeActivity;
import com.vedas.spectrocare.R;


public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    public static SharedPreferences sharedPreferencesTOken;
    public static SharedPreferences.Editor sharedPreferencesTOkenEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        MedicalProfileDataController.getInstance().fillContext(getApplicationContext());
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        PatientLoginDataController.getInstance().fetchPatientlProfileData();
        ApiCallDataController.getInstance().fillContent(getApplicationContext());

        sharedPreferencesTOken = getApplicationContext().getSharedPreferences("tokendeviceids", 0);
        sharedPreferencesTOkenEditor = sharedPreferencesTOken.edit();
        String token = FirebaseInstanceId.getInstance().getToken();
        String ID = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.e("divce", "ID ::" + ID + " token :: " + token);

        SplashActivity.sharedPreferencesTOkenEditor.putString("deviceId", ID);
        SplashActivity.sharedPreferencesTOkenEditor.commit();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PatientLoginDataController.getInstance().allPatientlProfile.size() > 0) {
                    if (getIntent().getStringExtra("appointmentID") != null) { //for checking firebase appointment notifications.
                        if (!getIntent().getStringExtra("appointmentID").isEmpty()) {
                            Log.e("getStringExtra", "" + getIntent().getStringExtra("appointmentID"));
                            Intent intent = new Intent(getApplicationContext(), PatientHomeActivity.class)
                                    .putExtra("isFromNotify", "true").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }else {
                        startActivity(new Intent(getApplicationContext(), PatientHomeActivity.class));
                        finish();
                    }
                } else {
                    Intent patientIntent = new Intent(SplashActivity.this, SelectUserActivity.class);
                    patientIntent.putExtra("patient", "1");
                    startActivity(patientIntent);
                    finish();

                }
            }
        }, SPLASH_TIME_OUT);
    }
}
