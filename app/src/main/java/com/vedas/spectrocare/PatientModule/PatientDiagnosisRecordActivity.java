package com.vedas.spectrocare.PatientModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.vedas.spectrocare.R;
import com.vedas.spectrocare.patientModuleAdapter.DiagnosisAdapter;

public class PatientDiagnosisRecordActivity extends AppCompatActivity {
    RecyclerView docDiagnosisList;
    ImageView imgBack;
    DiagnosisAdapter diagnosisAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_diagnosis_record);
        imgBack = findViewById(R.id.img_back_arrow);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        docDiagnosisList = findViewById(R.id.doc_diagnosis_list);
        diagnosisAdapter = new DiagnosisAdapter(PatientDiagnosisRecordActivity.this,"from");
        docDiagnosisList.setLayoutManager(new LinearLayoutManager(PatientDiagnosisRecordActivity.this));
        docDiagnosisList.setAdapter(diagnosisAdapter);
    }
}
