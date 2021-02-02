package com.vedas.spectrocare.PatientModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spectrochips.spectrumsdk.FRAMEWORK.TestFactors;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBase.TestFactorDataController;
import com.vedas.spectrocare.DataBase.UrineResultsDataController;
import com.vedas.spectrocare.DataBaseModels.UrineresultsModel;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.patientModuleAdapter.PatientTestRecordAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class PatientTestRecordActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ImageView edtImage, backImg;
    String data;
    public static boolean isFromTestRecord=false;
    RefreshShowingDialog refreshShowingDialog;
    PatientTestRecordAdapter patienTestTabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_test_record);
        refreshShowingDialog=new RefreshShowingDialog(PatientTestRecordActivity.this/*,"fetching.."*/);

        UrineResultsDataController.getInstance().fetchAllUrineResults();
        edtImage = findViewById(R.id.img_edit);
        backImg = findViewById(R.id.img_back_arrow);
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.addTab(tabLayout.newTab().setText("Urine Test"));
        tabLayout.addTab(tabLayout.newTab().setText("Blood Test"));
        final ViewPager viewPager = (ViewPager) findViewById(R.id.patient_view_pager);
        patienTestTabAdapter = new PatientTestRecordAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(patienTestTabAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        accessInterfaceMethods();
        if(PatientMedicalRecordsController.getInstance().isFromLogin){
            if(isConn()){
                refreshShowingDialog.showAlert();
                fetchingUrineResultApi();
            }
        }

    }

    public String sendData() {
        return data;
    }

    public void fetchingUrineResultApi() {
        JSONObject fetchObject = new JSONObject();
        try {
            fetchObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
            fetchObject.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            fetchObject.put("byWhomID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            fetchObject.put("byWhom","patient");
            fetchObject.put("medical_record_id", PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(fetchObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.
                fetchingUrineResultApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "fetch");
    }
    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                if (curdOpetaton.equals("fetch")) {
                    try {
                        if (jsonObject.getString("response").equals("3")) {
                            refreshShowingDialog.hideRefreshDialog();
                            PatientMedicalRecordsController.getInstance().isFromLogin=false;
                            JSONArray testResults = jsonObject.getJSONArray("testResults");//testFactors
                            for(int i=0;i<testResults.length();i++){
                                JSONObject items= (JSONObject) testResults.get(i);

                                UrineresultsModel objResult=new UrineresultsModel();
                                objResult.setTestReportNumber(items.getString("testReportNumber"));
                                objResult.setTestType("Urine");
                                objResult.setLatitude("0.0");
                                objResult.setLongitude("0.0");
                                objResult.setTestedTime(items.getString("testedTime"));
                                objResult.setIsFasting(String.valueOf(items.getBoolean("isFasting")));
                                objResult.setRelationtype("Patient");
                                objResult.setTest_id(items.getString("testID"));
                                objResult.setPatientId(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
                                objResult.setPatientModel(PatientLoginDataController.getInstance().currentPatientlProfile);

                                if (UrineResultsDataController.getInstance().insertUrineResultsForMember(objResult)) {
                                    JSONArray itemsArray=items.getJSONArray("testFactors");
                                    for(int k=0;k<itemsArray.length();k++){
                                        JSONObject obj=itemsArray.getJSONObject(k);
                                        com.vedas.spectrocare.DataBaseModels.TestFactors objTest = new com.vedas.spectrocare.DataBaseModels.TestFactors();
                                        objTest.setFlag(obj.getBoolean("flag"));
                                        objTest.setUnit(obj.getString("unit"));
                                        objTest.setHealthReferenceRanges(obj.getString("healthReferenceRanges"));
                                        objTest.setTestName(obj.getString("testName"));
                                        objTest.setResult(obj.getString("result"));
                                        objTest.setValue(obj.getString("value"));
                                        objTest.setUrineresultsModel(UrineResultsDataController.getInstance().currenturineresultsModel);
                                        if (TestFactorDataController.getInstance().insertTestFactorResults(objTest)) {

                                        }
                                    }
                                }
                            }
                            patienTestTabAdapter.notifyDataSetChanged();
                        }else{
                            refreshShowingDialog.hideRefreshDialog();
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void failureCallBack(String failureMsg) {
                refreshShowingDialog.hideRefreshDialog();
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
}
