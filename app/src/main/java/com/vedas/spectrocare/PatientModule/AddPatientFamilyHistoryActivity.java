package com.vedas.spectrocare.PatientModule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.Controllers.FamilyRecordServerDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientServerApiModel.FamilyDetaislModel;
import com.vedas.spectrocare.PatientServerApiModel.FamilyTrackingInfo;
import com.vedas.spectrocare.PatientServerApiModel.PatientFamilyAddServerObject;
import com.vedas.spectrocare.PatientServerApiModel.RecordModel;
import com.vedas.spectrocare.PatinetControllers.PatientFamilyDataController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.MedicalPersonaSignupView;
import com.vedas.spectrocare.activities.MedicalPersonalSignupPresenter;
import com.vedas.spectrocare.patientModuleAdapter.PatientFamilyHistoryAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

public class AddPatientFamilyHistoryActivity extends AppCompatActivity implements MedicalPersonaSignupView {
    EditText edtDiseaseName,edtNote,edtAge,edtRelation;
    Button btnSave;
    ImageView imgBack;
    Spinner relationSpinner;
    JSONArray array;
    JSONObject addObject;
    PatientFamilyAddServerObject familyAddServerObject;
    ArrayList<FamilyDetaislModel> detaislModelArrayList;
    FamilyDetaislModel familyDetaislModel;
    FamilyTrackingInfo trackingInfo;
    RecordModel recordModel;
    String selectedUserType="";

    ArrayList<FamilyTrackingInfo> listInfo;
    String[] relationArray = {"", "Father", "Mother", "Husband","Wife","Sister","Brother"};
    RefreshShowingDialog refreshShowingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient_family_history);
        PatientLoginDataController.getInstance().fetchPatientlProfileData();
        Log.e("id","d"+PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
        casting();
        loadSpinner();
        accessInterfaceMethods();
        edtRelation.setInputType(InputType.TYPE_NULL);
      //  edtRelation.setShowSoftInputOnFocus(false);
        detaislModelArrayList = new ArrayList<>();
        familyDetaislModel = new FamilyDetaislModel();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              validations();
            }
        });
    }
    public void casting(){
        refreshShowingDialog = new RefreshShowingDialog(AddPatientFamilyHistoryActivity.this);
        imgBack = findViewById(R.id.img_back_arrow);
        btnSave = findViewById(R.id.btn_save_changes);
        edtDiseaseName = findViewById(R.id.edt_disease_name);
        edtRelation = findViewById(R.id.edt_relation);
        edtNote = findViewById(R.id.edt_note);
        edtAge = findViewById(R.id.edt_age);
        recordModel = new RecordModel();
        relationSpinner = findViewById(R.id.spin_family_spinner);
        familyAddServerObject = new PatientFamilyAddServerObject();
        if (PatientFamilyDataController.getInstance().getResponseObject()!=null){
            Log.e("ddddd","family");
            detaislModelArrayList =  PatientFamilyDataController.getInstance().getResponseObject().getRecords().getFamliyDiseases();
        }
       /* edtRelation.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.onTouchEvent(event);
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
        });*/

       relationSpinner.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               hideSoftInput(AddPatientFamilyHistoryActivity.this);
               return false;
           }
       });
    }
    public static void hideSoftInput(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) view = new View(activity);
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showSoftInput(EditText edit, Context context) {
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(edit, 0);
        }
    }

    public static void toggleSoftInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    public void loadSpinner(){
        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,relationArray){
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
               // imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                imm.hideSoftInputFromWindow(new View(AddPatientFamilyHistoryActivity.this).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                View v = super.getDropDownView(position, convertView, parent);
                TextView t = new TextView(getContext());
                if (position == 0) {
                    t.setHeight(0);
                    t.setVisibility(View.GONE);
                    v = t;
                } else {
                    v = super.getDropDownView(position, null, parent);
                }
                return v;

            }
        };
        if(!selectedUserType.isEmpty()){

            Log.e("empvalueeeeeeeeee","dd"+ Arrays.asList(relationArray).indexOf(selectedUserType));
            edtRelation.setSelection(Arrays.asList(relationArray).indexOf(selectedUserType));
        }
        relationSpinner.setAdapter(adapter);
        relationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                edtRelation.setText(parent.getItemAtPosition(position).toString());
                View v = relationSpinner.getSelectedView();
                ((TextView)v).setTextColor(Color.parseColor("#00000000"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void addPatientFamilyRecord() {

     if (PatientFamilyDataController.getInstance().getResponseObject()!=null){
         Log.e("nl","nanana");
         detaislModelArrayList =  PatientFamilyDataController.getInstance().getResponseObject().getRecords().getFamliyDiseases();
     }
     Log.e("soc","df"+detaislModelArrayList.size());
        JSONObject detailsObj = new JSONObject();

            familyDetaislModel.setDieseaseName(edtDiseaseName.getText().toString());
            familyDetaislModel.setAge(edtAge.getText().toString());
            familyDetaislModel.setRelationship(edtRelation.getText().toString());
            familyDetaislModel.setNote(edtNote.getText().toString());
            detaislModelArrayList.add(familyDetaislModel);

        Gson gson1 = new Gson();
        String json1 = gson1.toJson(detaislModelArrayList);
        Log.e("arrayDetailsList","rrrr"+json1);

        array = new JSONArray();

        for (int i=0;detaislModelArrayList.size()>i;i++) {
            try {

                detailsObj=new JSONObject();
                detailsObj.put("dieseaseName",detaislModelArrayList.get(i).getDieseaseName());
                detailsObj.put("relationship",detaislModelArrayList.get(i).getRelationship());
                detailsObj.put("age",detaislModelArrayList.get(i).getAge());
                detailsObj.put("note",detaislModelArrayList.get(i).getNote());

                Log.e("dsjbnf","sknf"+detailsObj.toString());

                array.put(detailsObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Gson gson = new Gson();
        String json = gson.toJson(array);
        Log.e("arrayDetailsList","rrrr"+json);

        addObject =new JSONObject();
        try {
            addObject.put("hospital_reg_num",PatientLoginDataController.getInstance().currentPatientlProfile
            .getHospital_reg_number());
            addObject.put("byWhom","patient");
            addObject.put("byWhomID",PatientLoginDataController.getInstance().currentPatientlProfile
                    .getPatientId());
            addObject.put("patientID",PatientLoginDataController.getInstance().currentPatientlProfile
                    .getPatientId());

            addObject.put("medical_record_id",PatientLoginDataController.getInstance().currentPatientlProfile
                    .getMedicalRecordId());
            if (PatientFamilyDataController.getInstance().getResponseObject()!=null)
            addObject.put("family_history_record_id",PatientFamilyDataController.getInstance().
                    getResponseObject().getRecords().getFamily_history_record_id());
            addObject.put("famliyDiseases",array);
            Log.e("objecttt", "" + addObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(addObject.toString());

           ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.addPatientFamilyHistory(PatientLoginDataController.getInstance()
                   .currentPatientlProfile.getAccessToken(),gsonObject),"add");

    }
    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.
                ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                refreshShowingDialog.hideRefreshDialog();
                if (curdOpetaton.equals("add")) {
                    Log.e("response","check");
                    Log.e("settings response","call"+jsonObject.toString());
                    try {
                        if(jsonObject.get("response").equals(3)) {
                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                            listInfo = new ArrayList<>();
                            if(PatientFamilyDataController.getInstance().getResponseObject()==null){
                                trackingInfo = new FamilyTrackingInfo();
                                trackingInfo.setByWhom("patient");
                                trackingInfo.setByWhomID(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
                                trackingInfo.setByWhomName(PatientLoginDataController.getInstance().currentPatientlProfile
                                        .getFirstName().trim()+" "+PatientLoginDataController.getInstance().currentPatientlProfile.getLastName().trim());
                                trackingInfo.setDate(String.valueOf(timestamp.getTime()));
                                trackingInfo.setInfo("Family record successfully added");
                                listInfo.add(trackingInfo);
                                recordModel.setUpdatedDate(String.valueOf(timestamp.getTime()));
                                recordModel.setCreatedDate(String.valueOf(timestamp.getTime()));
                                recordModel.setTracking(listInfo);
                                recordModel.setFamliyDiseases(detaislModelArrayList);
                                recordModel.setHospital_reg_num(PatientLoginDataController.getInstance().currentPatientlProfile
                                        .getHospital_reg_number());
                                recordModel.setPatientID(PatientLoginDataController.getInstance().currentPatientlProfile
                                        .getPatientId());
                                recordModel.setMedical_record_id(PatientLoginDataController.getInstance().currentPatientlProfile
                                        .getMedicalRecordId());
                                recordModel.setFamily_history_record_id(jsonObject.getString("family_history_record_id"));
                                familyAddServerObject.setRecords(recordModel);
                                familyAddServerObject.setResponse(3);
                              PatientFamilyDataController.getInstance().setResponseObject(familyAddServerObject);
                                PatientFamilyDataController.getInstance().getResponseObject().setRecords(recordModel);

                            }
                        }
                         try {
                             Toast.makeText(AddPatientFamilyHistoryActivity.this, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(AddPatientFamilyHistoryActivity.this,PatientMedicalHistoryActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("family","family"));
                    finish();
                }
            }
            @Override
            public void failureCallBack(String failureMsg) {
                refreshShowingDialog.hideRefreshDialog();

                Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public void validations() {
        if(edtDiseaseName.getText().toString().isEmpty()){
            dialogeforCheckavilability("Error", "Please enter disease name", "ok");
        }else if (edtRelation.getText().toString().isEmpty()){
            dialogeforCheckavilability("Error", "Please enter relation", "ok");
        }else if (edtAge.getText().toString().isEmpty()){
            dialogeforCheckavilability("Error", "Please enter age", "ok");
        }else if (edtNote.getText().toString().isEmpty()){
            dialogeforCheckavilability("Error", "Please enter note", "ok");
        }else {
            if (isNetworkConnected()){
                refreshShowingDialog.showAlert();
                addPatientFamilyRecord();
            }
            else{
                refreshShowingDialog.dismiss();
                dialogeforCheckavilability("Error", "Please check internet connection", "ok");
            }
        }
    }

    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter = new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AddPatientFamilyHistoryActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);
    }
}
