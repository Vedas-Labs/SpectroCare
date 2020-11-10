package com.vedas.spectrocare.PatientModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.vedas.spectrocare.R;
import com.vedas.spectrocare.patientModuleAdapter.DoctorMessageAdapter;

public class PatientSearchDoctorActivity extends AppCompatActivity {
RecyclerView doctorsListView;
DoctorMessageAdapter doctorMessageAdapter;
ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_search_doctor);
        casting();
        doctorsListView.setAdapter(doctorMessageAdapter);
        doctorsListView.setLayoutManager(new LinearLayoutManager(PatientSearchDoctorActivity.this));
    }
    public void casting(){
        imgBack = findViewById(R.id.img_back_arrow);
        doctorsListView = findViewById(R.id.doctors_list_view);
        doctorMessageAdapter = new DoctorMessageAdapter(PatientSearchDoctorActivity.this);
    }
}
