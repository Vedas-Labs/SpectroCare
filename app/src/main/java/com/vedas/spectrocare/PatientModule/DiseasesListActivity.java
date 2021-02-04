package com.vedas.spectrocare.PatientModule;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.j256.ormlite.stmt.query.In;
import com.skyfishjy.library.RippleBackground;
import com.spectrochips.spectrumsdk.FRAMEWORK.SCConnectionHelper;
import com.spectrochips.spectrumsdk.FRAMEWORK.SpectroCareSDK;


import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.PatinetControllers.PatientFamilyDataController;
import com.vedas.spectrocare.R;

import java.util.ArrayList;

public class DiseasesListActivity extends AppCompatActivity {
    RelativeLayout rl_back,rl_nodata,rl_list;
    int selectedPosition = -1;
    devicesAdapter adapter;
    RecyclerView recyclerView;
    ImageView add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diseases_list);
        loadIDs();
    }
    @Override
    protected void onResume() {
        super.onResume();
       // loadRecyclerView();
    }
    private void loadIDs(){
        rl_back=findViewById(R.id.back);
        rl_nodata=findViewById(R.id.rl_nolist);
        rl_list=findViewById(R.id.rl_list);
        add=findViewById(R.id.add);

        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientMedicalRecordsController.getInstance().isFromMedication=false;
                onBackPressed();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), AddPatientDiseaseActivity.class));
            }
        });
        loadRecyclerView();

        if(PatientFamilyDataController.getInstance().getIllnessPatientList()!=null){
            rl_nodata.setVisibility(View.GONE);
            rl_list.setVisibility(View.VISIBLE);
        }else{
            rl_nodata.setVisibility(View.VISIBLE);
            rl_list.setVisibility(View.GONE);
        }
    }
    private void loadRecyclerView() {
        adapter = new devicesAdapter(this);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }
    public class devicesAdapter extends RecyclerView.Adapter<devicesAdapter.ViewHolder> {
        Context ctx;
        public devicesAdapter(Context ctx) {
            this.ctx = ctx;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diseases, parent, false);
            return new ViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.txt_name.setText(PatientFamilyDataController.getInstance().getIllnessPatientList().get(position).getIllnessCondition());
            if (selectedPosition == position) {
                PatientFamilyDataController.getInstance().selectedIllnessRecord=PatientFamilyDataController.getInstance().getIllnessPatientList().get(selectedPosition);
                  holder.rl_main.setBackgroundColor(Color.parseColor("#E9F9FB"));
                if(PatientMedicalRecordsController.getInstance().isFromDiagnosis){
                    startActivity(new Intent(getApplicationContext(),AddPatientDiagnosisActivity.class));
                    finish();
                }else if(PatientMedicalRecordsController.getInstance().isFromMedication){
                    // PatientMedicationsActivity.isFromMedication=false;
                    startActivity(new Intent(getApplicationContext(), PatientMedicinesRecordActivity.class));
                    finish();
                    //startActivity(new Intent(getApplicationContext(),PatientEditMedicationsRecordActivity.class));
                }else{
                    startActivity(new Intent(getApplicationContext(),AddPatientSurgeryActivity.class));
                    finish();
                }
            } else {
                holder.rl_main.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedPosition != position) {
                        selectedPosition = position;
                        notifyDataSetChanged();
                    } else {
                        selectedPosition = -1;
                        notifyDataSetChanged();
                    }
                }
            });
        }
        @Override
        public int getItemCount() {
            if(PatientFamilyDataController.getInstance().getIllnessPatientList().size()>0){
                return PatientFamilyDataController.getInstance().getIllnessPatientList().size();
            }else{
                return 0;
            }
        }
        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView txt_name;
            RelativeLayout rl_main;

            public ViewHolder(View itemView) {
                super(itemView);
                txt_name = (TextView) itemView.findViewById(R.id.txt_name);
                rl_main = (RelativeLayout) itemView.findViewById(R.id.rl);
                itemView.setOnClickListener(this);
            }
            @Override
            public void onClick(View v) {

            }
        }
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
