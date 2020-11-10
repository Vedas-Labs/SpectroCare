package com.vedas.spectrocare.PatientModule;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.vedas.spectrocare.R;

import androidx.appcompat.app.AppCompatActivity;

public class PatientAllergeyActivity extends AppCompatActivity {
    ImageView imgEdt,imgBack;
    EditText allergyName,allergyType,allergyNote;
    Button allergySaveBtn;
    Spinner allergySpinner;
    ArrayAdapter<String> dataAdapter;
    String[] allergyArrayNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_allergey);
        casting();
        clickListener();
    }
    public void casting(){
        allergyName = findViewById(R.id.edt_allergy_name);
        allergyType = findViewById(R.id.edt_type_of_allergy);
        allergyNote = findViewById(R.id.edt_allergy_note);
        allergySaveBtn = findViewById(R.id.btn_save_changes);
        allergySpinner = findViewById(R.id.spinner_allergy);
        imgBack = findViewById(R.id.img_back_arrow);
        imgEdt = findViewById(R.id.pic_edt);
        allergyArrayNames = new String[]{"Food","Medicine","Others"};
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allergyArrayNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        allergySpinner.setAdapter(dataAdapter);
    }
    public void clickListener(){
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allergySaveBtn.setVisibility(View.VISIBLE);
                allergySpinner.setVisibility(View.VISIBLE);
                allergySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                      /*  String item = parent.getItemAtPosition(position).toString();
                        allergyType.setText(item);*/
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                allergyName.setBackgroundResource(R.drawable.edt_txt_background);
                allergyType.setBackgroundResource(R.drawable.edt_txt_background);
                allergyNote.setBackgroundResource(R.drawable.edt_txt_background);
            }
        });
    }
}
