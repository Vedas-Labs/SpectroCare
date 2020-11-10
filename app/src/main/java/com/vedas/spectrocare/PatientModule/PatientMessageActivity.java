package com.vedas.spectrocare.PatientModule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.vedas.spectrocare.R;

public class PatientMessageActivity extends AppCompatActivity {
Button btnFindDoctor;
ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_message);
        btnFindDoctor = findViewById(R.id.btn_find_doctor);
        imgBack = findViewById(R.id.img_back_arrow);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnFindDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientMessageActivity.this,PatientSearchDoctorActivity.class));
            }
        });
    }
}
