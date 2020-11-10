package com.vedas.spectrocare.PatientModule;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBaseModels.PatientModel;
import com.vedas.spectrocare.PatientServerApiModel.FamilyDetaislModel;
import com.vedas.spectrocare.PatinetControllers.PatientFamilyDataController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.MedicalPersonaSignupView;
import com.vedas.spectrocare.activities.MedicalPersonalSignupPresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PatientFamilyHistoryActivity extends AppCompatActivity implements MedicalPersonaSignupView {
    ImageView imgEdt,imgBack;
    EditText edtFamilyName,edtRelation,edtFamilyNote,edtAge;
    Button familySaveBtn;
    TextView txtDate,txtTime;
    String position;
    JSONObject addObject;
    TextView txtPatientName,ageTxt;
    Spinner relationSpinner;
    JSONArray array;
    PatientModel objModel;
    ArrayAdapter adapter;
    RefreshShowingDialog refreshShowingDialog;
    String clockTime;
    ArrayList<FamilyDetaislModel> detaislModelArrayList;
    ArrayList<FamilyDetaislModel> detaislArrayList;
    FamilyDetaislModel editDetailItem;
    FamilyDetaislModel familyDetaislModel;
    String selectedUserType="";
    String[] relationArray = {"", "Father", "Mother", "Husband","Wife","Sister","Brother"};
    int value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_family_history);
        objModel = PatientLoginDataController.getInstance().currentPatientlProfile;

        detaislModelArrayList = new ArrayList<>();
        familyDetaislModel = new FamilyDetaislModel();
        Intent intent = getIntent();
        position = intent.getStringExtra("position");
        value = Integer.parseInt(position);
        Log.e("position","empty"+position);
        casting();
        getItemData();
        clickListener();
    }
    public void getItemData(){
        String fName=PatientFamilyDataController.getInstance().getResponseObject().getRecords().getFamliyDiseases()
                .get(value).getDieseaseName();
        edtFamilyName.setText(fName);
        String fAge=PatientFamilyDataController.getInstance().getResponseObject().getRecords().getFamliyDiseases()
                .get(value).getAge();
        edtAge.setText(fAge);
        String fNote=PatientFamilyDataController.getInstance().getResponseObject().getRecords().getFamliyDiseases()
                .get(value).getNote();
        edtFamilyNote.setText(fNote);
        String fRelation=PatientFamilyDataController.getInstance().getResponseObject().getRecords().getFamliyDiseases()
                .get(value).getRelationship();
        edtRelation.setText(fRelation);
        String entredDate =  PatientFamilyDataController.getInstance().getResponseObject().getRecords().getCreatedDate();
        Log.e("string","date"+entredDate);
        long l = Long.parseLong(entredDate);
        Date currentDate = new Date(l);
        SimpleDateFormat jdff = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        jdff.setTimeZone(TimeZone.getDefault());
        String java_date = jdff.format(currentDate);
        Date clickedDate = null;
        try {
            clickedDate = jdff.parse(java_date);

            String  formattedDate = jdff.format(clickedDate);
            Log.e("forrr","ff"+formattedDate);
            String[] two = formattedDate.split(" ");
            txtDate.setText(two[0]);
            String[] timeSplit = two[1].split(":");
            if (12 < Integer.parseInt(timeSplit[0])){
                int hr = Integer.parseInt(timeSplit[0])-12;
                clockTime = String.valueOf(hr)+":"+timeSplit[1]+"PM";
            }else{
                clockTime = two[1]+"AM";
            }
            txtTime.setText(clockTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
    public void casting(){
        refreshShowingDialog = new RefreshShowingDialog(PatientFamilyHistoryActivity.this);
        edtFamilyName = findViewById(R.id.edt_disease_name);
        edtRelation = findViewById(R.id.edt_relation);
        edtFamilyNote = findViewById(R.id.edt_note);
        edtAge = findViewById(R.id.edt_age);
        txtDate = findViewById(R.id.txt_doc_date);
        txtTime = findViewById(R.id.txt_doc_time);
        relationSpinner = findViewById(R.id.spin_family_spinner);
        txtPatientName = findViewById(R.id.txt_name_of_doc);
        ageTxt = findViewById(R.id.txt_doc_specail);
        familySaveBtn = findViewById(R.id.btn_save_changes);
        imgBack = findViewById(R.id.img_back_arrow);
        imgEdt = findViewById(R.id.pic_edt);
        ageTxt.setText(objModel.getAge()+" yrs");
        txtPatientName.setText(objModel.getFirstName().trim()+" "+objModel.getLastName().trim());

    }
    public void loadSpinner(){
         adapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,relationArray){
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
               // imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                imm.hideSoftInputFromWindow(new View(PatientFamilyHistoryActivity.this).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

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

    public void clickListener(){
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        familySaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected()){
                    refreshShowingDialog.showAlert();
                    updateFamilyData();
                    accessInterfaceMethods();
                }else{
                    refreshShowingDialog.hideRefreshDialog();
                    dialogeforCheckavilability("Error", "Please check internet connection", "ok");
                }
            }
        });

        imgEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                familySaveBtn.setVisibility(View.VISIBLE);
                edtFamilyName.setFocusableInTouchMode(true);
                edtRelation.setFocusable(false);
                edtAge.setFocusableInTouchMode(true);
                edtFamilyNote.setFocusableInTouchMode(true);
                edtFamilyName.setBackgroundResource(R.drawable.edt_txt_background);
                edtAge.setBackgroundResource(R.drawable.edt_txt_background);
                edtRelation.setBackgroundColor(Color.parseColor("#FFFFFF"));
                edtFamilyNote.setBackgroundResource(R.drawable.edt_txt_background);
                edtFamilyName.setSelection(edtFamilyName.length());
                relationSpinner.setBackgroundResource(R.drawable.edt_txt_background);
                loadSpinner();
                edtRelation.setSelection(edtRelation.length());
                edtAge.setSelection(edtAge.length());
                edtFamilyNote.setSelection(edtFamilyNote.length());
                String compareValue = edtRelation.getText().toString();
                if (compareValue != null) {
                    int spinnerPosition = adapter.getPosition(compareValue);
                    relationSpinner.setSelection(spinnerPosition);
                }

            }
        });
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    public void updateFamilyData() {

        if (PatientFamilyDataController.getInstance().getResponseObject()!=null){
            Log.e("nl","nanana");
            detaislModelArrayList =  PatientFamilyDataController.getInstance().getResponseObject().getRecords().getFamliyDiseases();
        }else{
            Log.e("nulll","nanana");
        }
        Log.e("soc","df"+detaislModelArrayList.size());
        JSONObject detailsObj = new JSONObject();

      /*  familyDetaislModel.setDieseaseName(edtFamilyName.getText().toString());
        familyDetaislModel.setAge(edtAge.getText().toString());
        familyDetaislModel.setRelationship(edtRelation.getText().toString());
        familyDetaislModel.setNote(edtFamilyNote.getText().toString());
        detaislModelArrayList.add(familyDetaislModel);
*/
        Gson gson1 = new Gson();
        String json1 = gson1.toJson(detaislModelArrayList);
        Log.e("arrayDetailsList","rrrr"+json1);


        //    Log.e("arrayList","rrrr1  "+detaislModelArrayList.get(0).getNote());

        array = new JSONArray();
        //    Log.e("arrayList","rrrr2  "+detaislModelArrayList.get(1).getNote());
        detaislArrayList = new ArrayList<>();
        //    Log.e("arrayList","rrrr3  "+detaislModelArrayList.get(2).getNote());
        for (int i=0;detaislModelArrayList.size()>i;i++) {
            try {

                detailsObj=new JSONObject();
                if (i!=value){
                    detaislArrayList.add(detaislModelArrayList.get(i));
                    detailsObj.put("dieseaseName",detaislModelArrayList.get(i).getDieseaseName());
                    detailsObj.put("relationship",detaislModelArrayList.get(i).getRelationship());
                    detailsObj.put("age",detaislModelArrayList.get(i).getAge());
                    detailsObj.put("note",detaislModelArrayList.get(i).getNote());
                }else{

                    detailsObj.put("dieseaseName",edtFamilyName.getText().toString());
                    detailsObj.put("relationship",edtRelation.getText().toString());
                    detailsObj.put("age",edtAge.getText().toString());
                    detailsObj.put("note",edtFamilyNote.getText().toString());
                }
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
            addObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile
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
      /*  ApiCallDataController.getInstance().serverApi.addPatientFamilyHistory(PatientLoginDataController.getInstance()
                .currentPatientlProfile.getAccessToken(),gsonObject);
      */
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
                    try {
                        if (jsonObject.get("response").equals(3)){
                            editDetailItem = new FamilyDetaislModel();
                            editDetailItem.setNote(edtFamilyNote.getText().toString());
                            editDetailItem.setAge(edtAge.getText().toString());
                            editDetailItem.setRelationship(edtRelation.getText().toString());
                            editDetailItem.setDieseaseName(edtFamilyName.getText().toString());

                            Log.e("nbfgg","fh"+detaislArrayList.size());

                            detaislArrayList.add(value,editDetailItem);

                            Log.e("nbfgg","fh"+detaislArrayList.size());
                           // detaislArrayList.add(editDetailItem);
                            PatientFamilyDataController.getInstance().getResponseObject().getRecords().setFamliyDiseases(detaislArrayList);
                            Toast.makeText(getApplicationContext(), "Family Records are updated Successfully", Toast.LENGTH_SHORT).show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("settings response","call"+jsonObject.toString());

                    Log.e("shdsb","djnfjb");
                    PatientFamilyDataController patientFamilyDataController=PatientFamilyDataController.getInstance();
                    Log.e("shdsb","fjgjg");
                    startActivity(new Intent(PatientFamilyHistoryActivity.this,PatientMedicalHistoryActivity.class)
                            .putExtra("family","family"));
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


    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter = new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PatientFamilyHistoryActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);
    }
/*
    public void frequency(){
        final Dialog dialog = new Dialog(PatientFamilyHistoryActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.spinner_date_alert);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        NumberPicker picker1 = dialog.findViewById(R.id.numbr1);
        NumberPicker picker2 = dialog.findViewById(R.id.numbr2);
        NumberPicker picker3 = dialog.findViewById(R.id.numbr3);
        picker1.setMaxValue(1);
        picker2.setMaxValue(1);
        picker3.setMaxValue(1);
        dialog.show();


    }
*/

}
