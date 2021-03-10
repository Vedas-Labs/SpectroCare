package com.vedas.spectrocare.PatientModule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicinesRecordModel;
import com.vedas.spectrocare.R;

public class PatientAddMedicineActivity extends AppCompatActivity {
    TextView duration;
    Button add_btn;
    EditText medicineName, dosage, purpose, moreInfo;
    CheckBox btnMorning, btnAfternoon, btnNight;
    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_add_medicine);
        loadIDs();
        loadarray();
    }
    private void loadarray(){
        if(PatientMedicalRecordsController.getInstance().currentMedicationRecordModel != null) {
            PatientMedicalRecordsController.getInstance().viewMedicalModelList = PatientMedicalRecordsController.getInstance().currentMedicationRecordModel.getMedicinesRecordModels();
        }
        /*if(PatientMedicalRecordsController.getInstance().viewMedicalModelList.size()>0){
            PatientMedicalRecordsController.getInstance().currentListviewModel=PatientMedicalRecordsController.getInstance().viewMedicalModelList.get(PatientMedicalRecordsController.getInstance().viewMedicalModelList.size()-1);
            Log.e("testing", "call"+PatientMedicalRecordsController.getInstance().currentListviewModel.getDurationDays());
        }*/
    }
    private void loadIDs(){
        duration = findViewById(R.id.txt_duration);
        dosage = findViewById(R.id.edt_dosage);
        purpose = findViewById(R.id.edt_purpose);
        moreInfo = findViewById(R.id.edt_medication_description);
        medicineName = findViewById(R.id.edt_medicine);
        btnMorning = findViewById(R.id.btn_radio_day);
        btnAfternoon = findViewById(R.id.btn_radio_afternoon);
        btnNight = findViewById(R.id.btn_radio_night);
        final EditText duration = findViewById(R.id.txt_duration);
        add_btn = findViewById(R.id.btn_add_medication_record);
        imgView = findViewById(R.id.img_close);
        add_btn.setText("ADD");
        if(PatientMedicalRecordsController.getInstance().currentListviewModel!=null){
            add_btn.setText("Update");
            medicineName.setText(PatientMedicalRecordsController.getInstance().currentListviewModel.getName());
            dosage.setText(PatientMedicalRecordsController.getInstance().currentListviewModel.getDosage());
            purpose.setText(PatientMedicalRecordsController.getInstance().currentListviewModel.getPurpose());
            duration.setText(PatientMedicalRecordsController.getInstance().currentListviewModel.getDurationDays()+" days");
            moreInfo.setText(PatientMedicalRecordsController.getInstance().currentListviewModel.getMoreDetails());
            String array[]=PatientMedicalRecordsController.getInstance().currentListviewModel.getFreq().split("-");
            if(Integer.parseInt(array[0]) >= 1){
                btnMorning.setChecked(true);
            }else {
                btnMorning.setChecked(false);
            }
            if(Integer.parseInt(array[1]) >= 1){
                btnAfternoon.setChecked(true);
            }else {
                btnAfternoon.setChecked(false);
            }
            if(Integer.parseInt(array[2]) >= 1){
                btnNight.setChecked(true);
            }else {
                btnNight.setChecked(false);
            }
        }
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("aaaaaaaaaaa", "call" + btnMorning.isChecked() + btnAfternoon.isChecked() + btnNight.isChecked());
                int morning = btnMorning.isChecked() ? 1 : 0;
                int afternonn = btnAfternoon.isChecked() ? 1 : 0;
                int night = btnNight.isChecked() ? 1 : 0;
                String frequency = morning + "-" + afternonn + "-" + night;
                Log.e("frequency", "call" + frequency);
                PatientMedicinesRecordModel objModel = new PatientMedicinesRecordModel();
                  objModel.setName(medicineName.getText().toString());
                  objModel.setDosage(dosage.getText().toString());
                  objModel.setDurationDays(duration.getText().toString());
                  objModel.setFreq(frequency);
                  objModel.setMoreDetails(moreInfo.getText().toString());
                  objModel.setPurpose(purpose.getText().toString());

                if(PatientMedicalRecordsController.getInstance().currentListviewModel!=null){
                    objModel.setIllnessMedicationID(PatientMedicalRecordsController.getInstance().currentListviewModel.getIllnessMedicationID());
                    PatientMedicalRecordsController.getInstance().currentListviewModel.setName(medicineName.getText().toString());
                    PatientMedicalRecordsController.getInstance().currentListviewModel.setDosage(dosage.getText().toString());
                    PatientMedicalRecordsController.getInstance().currentListviewModel.setDurationDays(duration.getText().toString());
                    PatientMedicalRecordsController.getInstance().currentListviewModel.setFreq(frequency);
                    PatientMedicalRecordsController.getInstance().currentListviewModel.setMoreDetails(moreInfo.getText().toString());
                    PatientMedicalRecordsController.getInstance().currentListviewModel.setPurpose(purpose.getText().toString());
                }/* else if(str_operation.equals("showModel")){
                    objModel.setIllnessMedicationID(currentListviewModel.getIllnessMedicationID());
                    PatientMedicalRecordsController.getInstance().viewMedicalModelList.set(selectdPos,objModel);
                }*/else {
                    PatientMedicalRecordsController.getInstance().viewMedicalModelList.add(objModel);
                }
                finish();
            }
        });
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
