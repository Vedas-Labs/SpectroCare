package com.vedas.spectrocare.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.model.GetMedicalRecordResponse;
import com.vedas.spectrocare.model.MedicalRecordModel;
import com.vedas.spectrocare.model.RecordList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MedicalRecordActivity extends AppCompatActivity {

    /*List<RecordList> RecordLists;
    MedicalRecordAdapter recordAdapter;
    FloatingActionButton addRecordBtn;
    private String hosRegID,medicID,accessToken;
    ProgressBar progressBar;
    AlertDialog.Builder dialog2;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_record);
       // PersonalInfoController.getInstance().fillContent(getApplicationContext());

        dialog2 = new AlertDialog.Builder(MedicalRecordActivity.this);
        LayoutInflater inflater = MedicalRecordActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progress, null);
        progressBar = dialogView.findViewById(R.id.progressBar);
        dialog2.setView(dialogView);
        progressBar.setVisibility(View.VISIBLE);
        alertDialog = dialog2.create();
        alertDialog.show();
        TextView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        addRecordBtn = findViewById(R.id.btn_medical_record_detail);
        addRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent medicalRecordIntent = new Intent(MedicalRecordActivity.this,AddMedicalRecordActivity.class);
                medicalRecordIntent.putExtra("patientID",getIntent().getStringExtra("patientID"));
                Log.e("dmf",""+getIntent().getStringExtra("patientID"));
                startActivity(medicalRecordIntent);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        String medicalPersonnelId = sharedPreferences.getString("userInfo", "");
        String IDpatient = getIntent().getStringExtra("patientID");
        Log.e("idPatient",""+IDpatient);

        try {
            JSONObject jsonObject=new JSONObject(medicalPersonnelId);
            JSONObject medicalPersonnelJsonObject = jsonObject.getJSONObject("medicalPersonnel");
            accessToken = jsonObject.getString("access_token");
            medicID=medicalPersonnelJsonObject.getString("medical_personnel_id");
            hosRegID=medicalPersonnelJsonObject.getString("hospital_reg_num");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MedicalRecordModel medicalRecordModel = new MedicalRecordModel(medicID,hosRegID,IDpatient);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ServerApi apiServer = retrofit.create(ServerApi.class);
        Call<GetMedicalRecordResponse> recordResponseCall = apiServer.getMedicalRecordList(accessToken,medicalRecordModel);
        recordResponseCall.enqueue(new Callback<GetMedicalRecordResponse>() {
            @Override
            public void onResponse(Call<GetMedicalRecordResponse> call, Response<GetMedicalRecordResponse> response) {
                Log.e("recordResponse",""+response.body().getMedical_records());
                SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
                RecordLists = new ArrayList<>();
                RecordLists = response.body().getMedical_records();
                Log.e("listOfRecord",""+RecordLists);
                if (RecordLists==null){
                    TextView medicDisc = findViewById(R.id.text_medicDisc);
                    medicDisc.setVisibility(View.VISIBLE);
                }
                String httpParamJSONList = new Gson().toJson(RecordLists);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putStringSet("key", Collections.singleton(httpParamJSONList));
                Log.e("list",""+Collections.singleton(httpParamJSONList));
                editor.apply();
                recordAdapter = new MedicalRecordAdapter(MedicalRecordActivity.this,RecordLists);
                RecyclerView recyclerView = findViewById(R.id.record_view);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(MedicalRecordActivity.this));
                recyclerView.setAdapter(recordAdapter);
                alertDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetMedicalRecordResponse> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
       // PersonalInfoController.getInstance().fillContent(getApplicationContext());

    }*/
}
