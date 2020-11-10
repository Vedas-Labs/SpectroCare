package com.vedas.spectrocare.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.Location.LocationTracker;
import com.vedas.spectrocare.PatientModule.PatientHomeActivity;
import com.vedas.spectrocare.R;

public class SelectUserActivity extends AppCompatActivity {
    Button btnMedicalPersonnel,btnAdministration,btnPatient;
    public static boolean isFromMedicalPerson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);
        LocationTracker.getInstance().fillContext(getApplicationContext());
        LocationTracker.getInstance().startLocation();
        MedicalProfileDataController.getInstance().fillContext(getApplicationContext());
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        PatientLoginDataController.getInstance().fetchPatientlProfileData();

        if(MedicalProfileDataController.getInstance().allMedicalProfile.size()>0){
            Intent intent = new Intent(SelectUserActivity.this, HomeActivity.class);
            startActivity(intent);
        }

        requestStoragePermission();
        btnAdministration = findViewById(R.id.btn_administration);
        btnMedicalPersonnel = findViewById(R.id.btn_medical_personnal);
        btnPatient = findViewById(R.id.btn_patient);

        btnPatient.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                isFromMedicalPerson = false;
                btnPatient.setBackground(SelectUserActivity.this.getResources().getDrawable(R.drawable.btn_bck_color));
                btnPatient.setTextColor(Color.parseColor("#ffffff"));
                btnAdministration.setTextColor(Color.parseColor("#3E454C"));
                btnMedicalPersonnel.setTextColor(Color.parseColor("#3E454C"));
                btnAdministration.setBackground(SelectUserActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));
                btnMedicalPersonnel.setBackground(SelectUserActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));
                /*Intent patientIntent = new Intent(SelectUserActivity.this,LoginActivity.class);
                patientIntent.putExtra("patient","1");
                startActivity(patientIntent);*/
                 if(PatientLoginDataController.getInstance().allPatientlProfile.size()>0){
                    startActivity(new Intent(getApplicationContext(), PatientHomeActivity.class));
                }else {
                    Intent patientIntent = new Intent(SelectUserActivity.this,LoginActivity.class);
                    patientIntent.putExtra("patient","1");
                    startActivity(patientIntent);
                    finish();
                }
            }
        });

        btnAdministration.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                btnAdministration.setBackground(SelectUserActivity.this.getResources().getDrawable(R.drawable.btn_bck_color));
                btnMedicalPersonnel.setBackground(SelectUserActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));
                btnPatient.setBackground(SelectUserActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));
                btnAdministration.setTextColor(Color.parseColor("#ffffff"));
                btnMedicalPersonnel.setTextColor(Color.parseColor("#3E454C"));
                btnPatient.setTextColor(Color.parseColor("#3E454C"));

            }
        });


        btnMedicalPersonnel.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                isFromMedicalPerson =true;
                btnMedicalPersonnel.setBackground(SelectUserActivity.this.getResources().getDrawable(R.drawable.btn_bck_color));
                btnPatient.setBackground(SelectUserActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));
                btnPatient.setTextColor(Color.parseColor("#3E454C"));
                btnAdministration.setTextColor(Color.parseColor("#3E454C"));
                btnMedicalPersonnel.setTextColor(Color.parseColor("#ffffff"));
                btnAdministration.setBackground(SelectUserActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));
                Intent medicalSignUpIntent = new Intent(SelectUserActivity.this,LoginActivity.class);
                startActivity(medicalSignUpIntent);
                finish();
            }
        });

    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Permission Info")
                    .setMessage("Gallery Permission is needed for adding your Profile Image")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(SelectUserActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                        }
                    })
                    .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(SelectUserActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
        }
    }
}
