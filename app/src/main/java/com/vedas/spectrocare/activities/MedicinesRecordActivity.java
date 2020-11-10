package com.vedas.spectrocare.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.Controllers.MedicationServerObjectDataController;
import com.vedas.spectrocare.DataBase.IllnessDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.MedicationRecordDataController;
import com.vedas.spectrocare.DataBase.MedicinesRecordDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBaseModels.MedicationRecordModel;
import com.vedas.spectrocare.DataBaseModels.MedicinesRecordModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApiModel.MedicationManullayServerObject;
import com.vedas.spectrocare.SingleTapDetector;
import com.vedas.spectrocare.adapter.ViewMedicalAdapter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class MedicinesRecordActivity extends AppCompatActivity {
    FloatingActionButton addViewMedicalRecord;
    BottomSheetDialog bottomSheetDialog;
    ImageView imgView;
    CheckBox btnMorning, btnAfternoon, btnNight;
    TextView duration;
    Button add_btn;
    private float dX, dY;
    int lastAction;
    RecyclerView medicalRecordRecyclerView;
    ArrayList<MedicinesRecordModel> viewMedicalModelList = new ArrayList<>();
    MedicinesRecordModel currentListviewModel=null;
    int selectdPos=-1;
    GestureDetector gestureDetector;
    EditText medicineName, dosage, purpose, moreInfo;
    ProgressBar progressBar;
    public  static AlertDialog alertDialog;
    JSONObject params;
    TextView showText;
    String str_operation="";
    Button btn_submit;
    TextView txt_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medical);
        gestureDetector = new GestureDetector(this, new SingleTapDetector());
        addViewMedicalRecord = findViewById(R.id.add_view_medical_record);
        showText=findViewById(R.id.text_medical_disc);
        medicalRecordRecyclerView = findViewById(R.id.medical_record_recycler_view);
        btn_submit=findViewById(R.id.btn_submit);
        txt_add=findViewById(R.id.txt_add);

        viewMedicalModelList=MedicinesRecordDataController.getInstance().fetchMedicinesData( MedicationRecordDataController.getInstance().currentMedicationRecordModel);

        if(viewMedicalModelList.size()>0){
           currentListviewModel=viewMedicalModelList.get(viewMedicalModelList.size()-1);
            Log.e("testing", "call"+currentListviewModel.getDurationDays());
       }
        txt_add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
               currentListviewModel=null;
                str_operation="";
                bottomDialogSheet();
            }
        });
        ImageView imgButton = findViewById(R.id.img_back);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        accessInterfaceMethods();

        loadSpinner();
        recyclerView();
        loadPlusButton();
        processAddApi();
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.e("onResume", "call");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ViewMedicalAdapter.MessageEvent event) {
        String resultData = event.message.trim();
        str_operation=event.message.trim();
        selectdPos=event.position;
        if (resultData.equals("editModel")) {
            currentListviewModel=viewMedicalModelList.get(event.getSelectPos());
            bottomDialogSheet();
        }else if (resultData.equals("showModel")) {
            currentListviewModel=viewMedicalModelList.get(event.getSelectPos());
            bottomDialogSheet();
            add_btn.setVisibility(View.GONE);
        }else if (resultData.equals("deleteModel")) {
            currentListviewModel=viewMedicalModelList.get(event.getSelectPos());
            viewMedicalModelList.remove(currentListviewModel);
            recyclerView();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void bottomDialogSheet() {
        View dialogFamilyView = getLayoutInflater().inflate(R.layout.medication_bottom_sheet, null);
        bottomSheetDialog = new BottomSheetDialog(Objects.requireNonNull(MedicinesRecordActivity.this),R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(dialogFamilyView);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.setCancelable(true);
        duration = bottomSheetDialog.findViewById(R.id.txt_duration);
        dosage = bottomSheetDialog.findViewById(R.id.edt_dosage);
        purpose = bottomSheetDialog.findViewById(R.id.edt_purpose);
        moreInfo = bottomSheetDialog.findViewById(R.id.edt_medication_description);
        medicineName = bottomSheetDialog.findViewById(R.id.edt_medicine);
        btnMorning = bottomSheetDialog.findViewById(R.id.btn_radio_day);
        btnAfternoon = bottomSheetDialog.findViewById(R.id.btn_radio_afternoon);
        btnNight = bottomSheetDialog.findViewById(R.id.btn_radio_night);

        final EditText duration = bottomSheetDialog.findViewById(R.id.txt_duration);

        add_btn = bottomSheetDialog.findViewById(R.id.btn_add_medication_record);
        imgView = bottomSheetDialog.findViewById(R.id.img_close);
        add_btn.setText("ADD");
        if(currentListviewModel!=null){
            add_btn.setText("Update");
            medicineName.setText(currentListviewModel.getName());
            dosage.setText(currentListviewModel.getDosage());
            purpose.setText(currentListviewModel.getPurpose());
            duration.setText(currentListviewModel.getDurationDays());
            moreInfo.setText(currentListviewModel.getMoreDetails());
            String array[]=currentListviewModel.getFreq().split("-");
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
                MedicinesRecordModel objModel = new MedicinesRecordModel();
                objModel.setName(medicineName.getText().toString());
                objModel.setDosage(dosage.getText().toString());
                objModel.setDurationDays(duration.getText().toString());
                objModel.setFreq(frequency);
                objModel.setMoreDetails(moreInfo.getText().toString());
                objModel.setPurpose(purpose.getText().toString());
                if(str_operation.equals("editModel")){
                    objModel.setIllnessMedicationID(currentListviewModel.getIllnessMedicationID());
                    viewMedicalModelList.set(selectdPos,objModel);
                }else if(str_operation.equals("showModel")){
                    objModel.setIllnessMedicationID(currentListviewModel.getIllnessMedicationID());
                    viewMedicalModelList.set(selectdPos,objModel);
                }else {
                    viewMedicalModelList.add(objModel);
                }
                recyclerView();
                bottomSheetDialog.dismiss();
            }
        });
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();
            }
        });
        bottomSheetDialog.show();
    }
    public void recyclerView() {
        if(viewMedicalModelList.size()>0){
            showText.setVisibility(View.GONE);
            btn_submit.setVisibility(View.VISIBLE);
        }else {
            showText.setVisibility(View.VISIBLE);
            btn_submit.setVisibility(View.GONE);

        }
        ViewMedicalAdapter viewMedicalAdapter = new ViewMedicalAdapter(MedicinesRecordActivity.this, viewMedicalModelList);
        medicalRecordRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicalRecordRecyclerView.setHasFixedSize(true);
        medicalRecordRecyclerView.setAdapter(viewMedicalAdapter);
        viewMedicalAdapter.notifyDataSetChanged();

    }
    private void loadSpinner() {
        LayoutInflater inflater = MedicinesRecordActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alertbox_layout, null);
        AlertDialog.Builder dialog1 = new AlertDialog.Builder(MedicinesRecordActivity.this);
        progressBar = dialogView.findViewById(R.id.progressBar);
        dialog1.setView(dialogView);
        alertDialog = dialog1.create();
    }
    private void processAddApi(){
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray jsonArray = new JSONArray();
                for(int i=0;i<viewMedicalModelList.size();i++){
                    MedicinesRecordModel obj=viewMedicalModelList.get(i);
                    try {
                        JSONObject json = new JSONObject();
                        json.put("name", obj.getName());
                        json.put("dosage",obj.getDosage());
                        json.put("freq", obj.getFreq());
                        json.put("purpose",obj.getPurpose());
                        json.put("durationDays", obj.getDurationDays());
                        json.put("moreDetails",obj.getMoreDetails());
                        jsonArray.put(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (isConn()) {
                    alertDialog.show();
                    if(currentListviewModel!=null){
                        Log.e("notnull", "true");
                        processJsonParams(jsonArray,true);
                    }else {
                        Log.e("null", "false");
                        processJsonParams(jsonArray,false);
                    }
                }
            }
        });
    }
    private void processJsonParams(JSONArray medicationArray,boolean isupdate) {
        params = new JSONObject();
        try {
            params.put("hospital_reg_num", MedicalProfileDataController.getInstance().currentMedicalProfile.getHospital_reg_number());
            params.put("byWhom","medical personnel");
            params.put("byWhomID",MedicalProfileDataController.getInstance().currentMedicalProfile.getMedical_person_id());
            params.put("patientID",PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("medical_record_id",PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
            params.put("illnessID", IllnessDataController.getInstance().currentIllnessRecordModel.getIllnessRecordId());
            params.put("doctorMedicalPersonnelID", "DcM2345");
            params.put("doctorName", "Chandu");
            params.put("medications", medicationArray);
            if(isupdate){
                params.put("illnessMedicationID", currentListviewModel.getIllnessMedicationID());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + isupdate+gsonObject.toString());
        MedicationServerObjectDataController.getInstance().addMedicationRecord(gsonObject,isupdate);
    }
    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                if (curdOpetaton.equals("insert")) {
                    try {
                        Log.e("medicationmanully", "call" + jsonObject.getString("illnessMedicationID"));
                        String medicationId=jsonObject.getString("illnessMedicationID");
                        MedicationRecordModel historyModel = new MedicationRecordModel();
                        historyModel.setPatientID(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
                        historyModel.setMedical_record_id(PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
                        historyModel.setMedical_personnel_id(MedicalProfileDataController.getInstance().currentMedicalProfile.getMedical_person_id());
                        historyModel.setIllnessMedicationID(medicationId);
                        historyModel.setIllnessID(IllnessDataController.getInstance().currentIllnessRecordModel.getIllnessRecordId());
                        historyModel.setHospital_reg_num(MedicalProfileDataController.getInstance().currentMedicalProfile.getHospital_reg_number());
                        historyModel.setDoctorName("Chandu");
                        historyModel.setDate(String.valueOf(System.currentTimeMillis() / 1000));
                        historyModel.setIllnessRecordModel(IllnessDataController.getInstance().currentIllnessRecordModel);
                        if (MedicationRecordDataController.getInstance().insertMedicationData(historyModel)) {
                            MedicationRecordDataController.getInstance().fetchMedicationData(IllnessDataController.getInstance().currentIllnessRecordModel);
                        }
                        for (int i = 0; i < viewMedicalModelList.size(); i++) {
                            MedicinesRecordModel singleObject = viewMedicalModelList.get(i);
                            MedicinesRecordModel model = new MedicinesRecordModel();
                            model.setPurpose(singleObject.getPurpose());
                            model.setName(singleObject.getName());
                            model.setMoreDetails(singleObject.getMoreDetails());
                            model.setIllnessMedicationID(medicationId);
                            model.setFreq(singleObject.getFreq());
                            model.setDurationDays(singleObject.getDurationDays());
                            model.setDosage(singleObject.getDosage());
                            model.setUniqueId(String.valueOf(i));
                            if (MedicationRecordDataController.getInstance().getMedicationForMedicationId(medicationId) != null) {
                                model.setMedicationRecordModel(MedicationRecordDataController.getInstance().getMedicationForMedicationId(medicationId));
                            }
                            if (MedicinesRecordDataController.getInstance().insertMedicinesData(model)) {
                                MedicinesRecordDataController.getInstance().fetchMedicinesData(MedicationRecordDataController.getInstance().currentMedicationRecordModel);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    bottomSheetDialog.dismiss();
                    finish();
                }else if (curdOpetaton.equals("update")) {
                    MedicinesRecordDataController.getInstance().deleteMedicinesData(MedicationRecordDataController.getInstance().currentMedicationRecordModel);
                    for (int i = 0; i < viewMedicalModelList.size(); i++) {
                        MedicinesRecordModel singleObject = viewMedicalModelList.get(i);
                        MedicinesRecordModel model = new MedicinesRecordModel();
                        model.setPurpose(singleObject.getPurpose());
                        model.setName(singleObject.getName());
                        model.setMoreDetails(singleObject.getMoreDetails());
                        model.setIllnessMedicationID(MedicationRecordDataController.getInstance().currentMedicationRecordModel.getIllnessMedicationID());
                        model.setFreq(singleObject.getFreq());
                        model.setDurationDays(singleObject.getDurationDays());
                        model.setDosage(singleObject.getDosage());
                        model.setUniqueId(String.valueOf(i));
                        model.setMedicationRecordModel(MedicationRecordDataController.getInstance().currentMedicationRecordModel);
                        if (MedicinesRecordDataController.getInstance().insertMedicinesData(model)) {
                            MedicinesRecordDataController.getInstance().fetchMedicinesData(MedicationRecordDataController.getInstance().currentMedicationRecordModel);
                        }
                    }
                   /* MedicinesRecordModel medicinesRecordModel=viewMedicalModelList.get(selectdPos);
                    Log.e("updaeinterface", "onResponse: " + medicinesRecordModel.getIllnessMedicationID());
                    if (MedicinesRecordDataController.getInstance().updateMedicinesData(medicinesRecordModel)) {
                        MedicinesRecordDataController.getInstance().fetchMedicinesData(MedicationRecordDataController.getInstance().currentMedicationRecordModel);
                    }*/
                     bottomSheetDialog.dismiss();
                      finish();
                    //  startActivity(new Intent(getApplicationContext(),MedicationRecordActivity.class));
                }
                alertDialog.dismiss();
                recyclerView();
            }

            @Override
            public void failureCallBack(String failureMsg) {
                alertDialog.dismiss();
                Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean isConn() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
    }
    private void loadPlusButton() {
        addViewMedicalRecord.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    currentListviewModel=null;
                    str_operation="";
                    bottomDialogSheet();
                } else {
                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            dX = v.getX() - event.getRawX();
                            dY = v.getY() - event.getRawY();
                            lastAction = MotionEvent.ACTION_DOWN;
                            break;

                        case MotionEvent.ACTION_MOVE:
                            v.setY(event.getRawY() + dY);
                            v.setX(event.getRawX() + dX);
                            lastAction = MotionEvent.ACTION_MOVE;
                            break;

                        case MotionEvent.ACTION_UP:
                            if (lastAction == MotionEvent.ACTION_DOWN)
                                // Toast.makeText(DraggableView.this, "Clicked!", Toast.LENGTH_SHORT).show();
                                break;

                        default:
                            return false;
                    }

                }

                return true;
            }
        });
    }
}
