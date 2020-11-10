package com.vedas.spectrocare.PatientModule;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.MedicationRecordDataController;
import com.vedas.spectrocare.DataBase.MedicinesRecordDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBaseModels.MedicinesRecordModel;
import com.vedas.spectrocare.PatientNotificationModule.MyButtonClickListener;
import com.vedas.spectrocare.PatientNotificationModule.MySwipeHelper;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicationRecordModel;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicationServerObjectDataController;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicinesRecordModel;
import com.vedas.spectrocare.PatinetControllers.PatientFamilyDataController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.SingleTapDetector;
import com.vedas.spectrocare.adapter.ViewMedicalAdapter;
import com.vedas.spectrocare.patientModuleAdapter.PatientViewMedicalAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class  PatientMedicinesRecordActivity extends AppCompatActivity {
    FloatingActionButton addViewMedicalRecord;
    BottomSheetDialog bottomSheetDialog;
    ImageView imgView;
    CheckBox btnMorning, btnAfternoon, btnNight;
    TextView duration;
    Button add_btn;
    EditText medicineName, dosage, purpose, moreInfo;
    private float dX, dY;
    int lastAction;
    RecyclerView medicalRecordRecyclerView;
    int selectdPos=-1;
    GestureDetector gestureDetector;
    ProgressBar progressBar;
    RefreshShowingDialog alertDilogue;
    public  static AlertDialog alertDialog;
    JSONObject params;
    TextView showText;
    String str_operation="";
    Button btn_submit;
    ImageView txt_add;
    PatientViewMedicalAdapter viewMedicalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medical);
        alertDilogue = new RefreshShowingDialog(PatientMedicinesRecordActivity.this);
        gestureDetector = new GestureDetector(this, new SingleTapDetector());
        addViewMedicalRecord = findViewById(R.id.add_view_medical_record);
        showText=findViewById(R.id.text_medical_disc);
        medicalRecordRecyclerView = findViewById(R.id.medical_record_recycler_view);
        btn_submit=findViewById(R.id.btn_submit);
        txt_add=findViewById(R.id.txt_add);

        if(PatientMedicalRecordsController.getInstance().currentMedicationRecordModel != null) {
            PatientMedicalRecordsController.getInstance().viewMedicalModelList = PatientMedicalRecordsController.getInstance().currentMedicationRecordModel.getMedicinesRecordModels();
        }

       /* if(PatientMedicalRecordsController.getInstance().viewMedicalModelList.size()>0){
           PatientMedicalRecordsController.getInstance().currentListviewModel=PatientMedicalRecordsController.getInstance().viewMedicalModelList.get(PatientMedicalRecordsController.getInstance().viewMedicalModelList.size()-1);
            Log.e("testing", "call"+PatientMedicalRecordsController.getInstance().currentListviewModel.getDurationDays());
         }*/

        txt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientMedicalRecordsController.getInstance().currentListviewModel=null;
               // PatientMedicalRecordsController.getInstance().viewMedicalModelList.clear();
                str_operation="";
                startActivity(new Intent(getApplicationContext(),PatientAddMedicineActivity.class));
               // bottomDialogSheet();
            }
        });
        ImageView imgButton = findViewById(R.id.img_back);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        accessInterfaceMethods();

        loadSpinner();
        recyclerView();
        loadPlusButton();
        processAddApi();
        MySwipeHelper mySwipeHelper = new MySwipeHelper(PatientMedicinesRecordActivity.this,medicalRecordRecyclerView,200) {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buffer) {
                buffer.add(new MyButton(PatientMedicinesRecordActivity.this,"",0,R.drawable.delete_sweep,
                        Color.parseColor("#FBF8F8"),
                        new MyButtonClickListener(){
                            @Override
                            public void onClick(int pos) {
                                Log.e("checkk","check");
                                deleteScreeningData(pos);
                              //  Toast.makeText(PatientMedicinesRecordActivity.this, "deleteClick", Toast.LENGTH_SHORT).show();
                            }
                        }));
            }
        };

    }
    @Override
    public void onResume() {
        super.onResume();
        Log.e("onResume", "call");
        recyclerView();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(PatientViewMedicalAdapter.MessageEvent event) {
        String resultData = event.message.trim();
        str_operation=event.message.trim();
        selectdPos=event.position;
        if (resultData.equals("editModel")) {
            PatientMedicalRecordsController.getInstance().currentListviewModel=PatientMedicalRecordsController.getInstance().viewMedicalModelList.get(event.getSelectPos());
            startActivity(new Intent(getApplicationContext(),PatientAddMedicineActivity.class));
            //bottomDialogSheet();
        }else if (resultData.equals("showModel")) {
            PatientMedicalRecordsController.getInstance().currentListviewModel=PatientMedicalRecordsController.getInstance().viewMedicalModelList.get(event.getSelectPos());
           // bottomDialogSheet();
            add_btn.setVisibility(View.GONE);
        }else if (resultData.equals("deleteModel")) {
            PatientMedicalRecordsController.getInstance().currentListviewModel=PatientMedicalRecordsController.getInstance().viewMedicalModelList.get(event.getSelectPos());
            PatientMedicalRecordsController.getInstance().viewMedicalModelList.remove(PatientMedicalRecordsController.getInstance().currentListviewModel);
            recyclerView();
        }
    }
    private void deleteScreeningData(final int model){
        final AlertDialog.Builder builder = new AlertDialog.Builder(PatientMedicinesRecordActivity.this, R.style.MyAlertDialogStyle);
        builder.setCancelable(false);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete this record");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EventBus.getDefault().post(new PatientViewMedicalAdapter.MessageEvent("deleteModel",model));
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void bottomDialogSheet() {
        View dialogFamilyView = getLayoutInflater().inflate(R.layout.medication_bottom_sheet, null);
        bottomSheetDialog = new BottomSheetDialog(Objects.requireNonNull(PatientMedicinesRecordActivity.this),R.style.BottomSheetDialogTheme);
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
        if(PatientMedicalRecordsController.getInstance().currentListviewModel!=null){
            add_btn.setText("Update");
            medicineName.setText(PatientMedicalRecordsController.getInstance().currentListviewModel.getName());
            dosage.setText(PatientMedicalRecordsController.getInstance().currentListviewModel.getDosage());
            purpose.setText(PatientMedicalRecordsController.getInstance().currentListviewModel.getPurpose());
            duration.setText(PatientMedicalRecordsController.getInstance().currentListviewModel.getDurationDays());
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
                if(str_operation.equals("editModel")){
                    objModel.setIllnessMedicationID(PatientMedicalRecordsController.getInstance().currentListviewModel.getIllnessMedicationID());
                    PatientMedicalRecordsController.getInstance().viewMedicalModelList.set(selectdPos,objModel);
                }else if(str_operation.equals("showModel")){
                    objModel.setIllnessMedicationID(PatientMedicalRecordsController.getInstance().currentListviewModel.getIllnessMedicationID());
                    PatientMedicalRecordsController.getInstance().viewMedicalModelList.set(selectdPos,objModel);
                }else {
                    PatientMedicalRecordsController.getInstance().viewMedicalModelList.add(objModel);
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
        if(PatientMedicalRecordsController.getInstance().viewMedicalModelList.size()>0){
            showText.setVisibility(View.GONE);
            btn_submit.setVisibility(View.VISIBLE);
        }else {
            showText.setVisibility(View.VISIBLE);
            btn_submit.setVisibility(View.GONE);

        }
        viewMedicalAdapter = new PatientViewMedicalAdapter(PatientMedicinesRecordActivity.this, PatientMedicalRecordsController.getInstance().viewMedicalModelList);
        medicalRecordRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicalRecordRecyclerView.setHasFixedSize(true);
        medicalRecordRecyclerView.setAdapter(viewMedicalAdapter);
        viewMedicalAdapter.notifyDataSetChanged();

    }
    private void loadSpinner() {
        LayoutInflater inflater = PatientMedicinesRecordActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alertbox_layout, null);
        AlertDialog.Builder dialog1 = new AlertDialog.Builder(PatientMedicinesRecordActivity.this);
        progressBar = dialogView.findViewById(R.id.progressBar);
        dialog1.setView(dialogView);
        alertDialog = dialog1.create();
    }
    private void processAddApi(){
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                JSONArray jsonArray = new JSONArray();
                for(int i=0;i<PatientMedicalRecordsController.getInstance().viewMedicalModelList.size();i++){
                    PatientMedicinesRecordModel obj=PatientMedicalRecordsController.getInstance().viewMedicalModelList.get(i);
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
                    alertDilogue.showAlert();
                  //  alertDialog.show();
                    if(PatientMedicalRecordsController.getInstance().currentMedicationRecordModel!=null){
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
            params.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
            params.put("byWhom","patient");
            params.put("byWhomID",PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("patientID",PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("medical_record_id",PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
            params.put("illnessID", PatientFamilyDataController.getInstance().selectedIllnessRecord.getIllnessID());
            params.put("doctorMedicalPersonnelID", "xyz123");
            params.put("doctorName", "Chandu");
            params.put("medications", medicationArray);
            if(isupdate){
                params.put("illnessMedicationID", PatientMedicalRecordsController.getInstance().currentMedicationRecordModel.getIllnessMedicationID());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + isupdate+gsonObject.toString());
        PatientMedicationServerObjectDataController.getInstance().addMedicationRecord(gsonObject,isupdate);
    }
    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                Log.e("accessInterfaceMethods", "onResponse: " + curdOpetaton);
                if (curdOpetaton.equals("insert")) {
                    try {
                        Log.e("medicationmanully", "call" + jsonObject.getString("illnessMedicationID"));
                        String medicationId=jsonObject.getString("illnessMedicationID");
                        PatientMedicationRecordModel historyModel = new PatientMedicationRecordModel();
                        historyModel.setPatientID(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
                        historyModel.setMedical_record_id(PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
                        historyModel.setMedical_personnel_id(PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalPerson_id());
                        historyModel.setIllnessMedicationID(medicationId);
                        historyModel.setIllnessID(PatientFamilyDataController.getInstance().selectedIllnessRecord.getIllnessID());
                        historyModel.setHospital_reg_num(PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
                        historyModel.setDoctorName("Chandu");
                        historyModel.setDate(String.valueOf(System.currentTimeMillis() / 1000));
                        historyModel.setMedicinesRecordModels(PatientMedicalRecordsController.getInstance().viewMedicalModelList);
                        PatientMedicalRecordsController.getInstance().allMedicationList.add(historyModel);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                   // bottomSheetDialog.dismiss();
                    finish();
                }else if (curdOpetaton.equals("update")) {
                    PatientMedicalRecordsController.getInstance().currentMedicationRecordModel.setMedicinesRecordModels( PatientMedicalRecordsController.getInstance().viewMedicalModelList);
                    /*for (int i = 0; i < PatientMedicalRecordsController.getInstance().viewMedicalModelList.size(); i++) {
                        PatientMedicinesRecordModel singleObject = PatientMedicalRecordsController.getInstance().viewMedicalModelList.get(i);
                        PatientMedicinesRecordModel model = new PatientMedicinesRecordModel();
                        model.setPurpose(singleObject.getPurpose());
                        model.setName(singleObject.getName());
                        model.setMoreDetails(singleObject.getMoreDetails());
                        model.setIllnessMedicationID(PatientMedicalRecordsController.getInstance().currentMedicationRecordModel.getIllnessMedicationID());
                        model.setFreq(singleObject.getFreq());
                        model.setDurationDays(singleObject.getDurationDays());
                        model.setDosage(singleObject.getDosage());
                        model.setUniqueId(String.valueOf(i));
                        PatientMedicalRecordsController.getInstance().viewMedicalModelList.set(i,model);
                        PatientMedicalRecordsController.getInstance().currentMedicationRecordModel.setMedicinesRecordModels( PatientMedicalRecordsController.getInstance().viewMedicalModelList);
                    }*/
                    // bottomSheetDialog.dismiss();
                      finish();
                }
                alertDilogue.hideRefreshDialog();
             //   alertDialog.dismiss();
                recyclerView();
            }

            @Override
            public void failureCallBack(String failureMsg) {
                alertDilogue.hideRefreshDialog();
              //  alertDialog.dismiss();
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
                    PatientMedicalRecordsController.getInstance().currentListviewModel=null;
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
