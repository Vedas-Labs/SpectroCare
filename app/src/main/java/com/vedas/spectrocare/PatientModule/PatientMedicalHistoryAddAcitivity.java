package com.vedas.spectrocare.PatientModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.vedas.spectrocare.R;
import com.vedas.spectrocare.model.CategoryItemModel;
import com.vedas.spectrocare.patientModuleAdapter.PatientMedicalHistoryAddAdapter;

import java.util.ArrayList;

public class PatientMedicalHistoryAddAcitivity extends AppCompatActivity {
    ArrayList medicalList;
    RecyclerView medicRecyclerView;
    PatientMedicalHistoryAddAdapter medicalHistoryAddAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_medical_history_add_acitivity);
        ImageView imgBack = findViewById(R.id.img_back_arrow);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        medicalList = new ArrayList();
        medicRecyclerView = findViewById(R.id.grid_medical_list);
        medicalList.add(new CategoryItemModel(R.drawable.disease, "Diseases"));
        medicalList.add(new CategoryItemModel(R.drawable.people, "Family History"));
        medicalList.add(new CategoryItemModel(R.drawable.allergy, "Allergy"));
        medicalList.add(new CategoryItemModel(R.drawable.ic_surgery, "Surgery"));
        medicalList.add(new CategoryItemModel(R.drawable.immunaization, "Immunizations"));
        medicalList.add(new CategoryItemModel(R.drawable.doctors, "Diagnosis"));

        medicalHistoryAddAdapter = new PatientMedicalHistoryAddAdapter(PatientMedicalHistoryAddAcitivity.this,medicalList);
        medicRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        medicRecyclerView.setNestedScrollingEnabled(false);
        medicRecyclerView.setAdapter(medicalHistoryAddAdapter);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PatientMedicalHistoryAddAcitivity.this,PatientMedicalHistoryActivity.class));
        super.onBackPressed();
    }
}
