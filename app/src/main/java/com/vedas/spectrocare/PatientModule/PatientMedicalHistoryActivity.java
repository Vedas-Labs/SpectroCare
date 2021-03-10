package com.vedas.spectrocare.PatientModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.Controllers.FamilyRecordServerDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBaseModels.PatientModel;
import com.vedas.spectrocare.PatientNotificationModule.MyButtonClickListener;
import com.vedas.spectrocare.PatientNotificationModule.MySwipeHelper;
import com.vedas.spectrocare.PatientServerApiModel.AllergyListObject;
import com.vedas.spectrocare.PatientServerApiModel.DiagnosisObject;
import com.vedas.spectrocare.PatientServerApiModel.FamilyDetaislModel;
import com.vedas.spectrocare.PatientServerApiModel.IllnessPatientRecord;
import com.vedas.spectrocare.PatientServerApiModel.PatientFamilyAddServerObject;
import com.vedas.spectrocare.PatientServerApiModel.PatientIllnessServerResponse;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.PatientServerApiModel.PatientSurgicalObject;
import com.vedas.spectrocare.PatinetControllers.PatientFamilyDataController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.LoginActivity;
import com.vedas.spectrocare.activities.MedicalPersonaSignupView;
import com.vedas.spectrocare.activities.MedicalPersonalSignupPresenter;
import com.vedas.spectrocare.patientModuleAdapter.DiagnosisAdapter;
import com.vedas.spectrocare.patientModuleAdapter.MedicalHistoryTabAdapter;
import com.vedas.spectrocare.patientModuleAdapter.PatientAllergyAdapter;
import com.vedas.spectrocare.patientModuleAdapter.PatientDiseaseAdapter;
import com.vedas.spectrocare.patientModuleAdapter.PatientFamilyHistoryAdapter;
import com.vedas.spectrocare.patientModuleAdapter.PatientImmunizationAdapter;
import com.vedas.spectrocare.patientModuleAdapter.PatientSurgeryAdapter;
import com.vedas.spectrocare.patientModuleAdapter.TabsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PatientMedicalHistoryActivity extends AppCompatActivity implements MedicalPersonaSignupView {
    RelativeLayout diseaseLayout, diagnosisLayout, allergyLayout, surgeryLayout, immunizationLayout, familyLayout;
    RecyclerView allRecycleView;
    PatientAllergyAdapter allergyAdapter;
    DiagnosisAdapter diagnosisAdapter;
    PatientFamilyAddServerObject responseObject;
    PatientIllnessServerResponse serverResponse;
    PatientSurgeryAdapter surgeryAdapter;
    PatientImmunizationAdapter immunizationAdapter;
    PatientFamilyHistoryAdapter familyHistoryAdapter;
    PatientDiseaseAdapter diseaseAdapter;
    SharedPreferences preferences;
    boolean isHourFormat;
    ArrayList<FamilyDetaislModel> familyList;
    // ArrayList<IllnessPatientRecord> illnessList=new ArrayList<>();
    // FamilyDetaislModel familyDetaislModel;
    int deletePos;
    CardView diagnosisCardView;
    JSONObject fetchObject, addObject;
    boolean conditon;
    TextView txtRecordName, txtDelete, txtDiagnosis, txtCount, txtAllergy, txtSurgery, txtImmune, txtDisease, txtFamily;
    ImageView imgBack, imgAdd, imgDiagnosis, imgDisease, imgFamily, imgSurgery, imgImmune, imgAllergy;
    JSONObject detailsObj;
    JSONArray array;
    String name, json;
    RefreshShowingDialog refreshShowingDialog;

    public void fetchDiagnosisRecords() {
        PatientModel currentModel = PatientLoginDataController.getInstance().currentPatientlProfile;
        JSONObject params = new JSONObject();
        try {
            params.put("hospital_reg_num", currentModel.getHospital_reg_number());
            params.put("byWhom", "patient");
            params.put("byWhomID", currentModel.getPatientId());
            params.put("patientID", currentModel.getPatientId());
            params.put("medical_record_id", currentModel.getMedicalRecordId());
            if (PatientFamilyDataController.getInstance().getIllnessPatientList()!=null) {
                params.put("illnessID", PatientFamilyDataController.getInstance().getIllnessPatientList().get(0).getIllnessID());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.fetchDiagnosisApi(currentModel.getAccessToken(), gsonObject), "fetchdiagnosis");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_medical_history);
        PatientLoginDataController.getInstance().fetchPatientlProfileData();

        refreshShowingDialog = new RefreshShowingDialog(PatientMedicalHistoryActivity.this);
        name = "name";
        conditon = true;

        fetchObject = new JSONObject();
        casting();
        familyList = new ArrayList<>();
        clickListeners();
        accessInterfaceMethods();
        diagnosisLayout.performClick();
       // diseaseLayout.performClick();
        Intent intent = getIntent();
        if (intent.hasExtra("disease")) {
            String intetName = intent.getStringExtra("disease");
            if (intetName.equals("disease")) {
                diseaseLayout.performClick();
            }
        } else if (intent.hasExtra("family")) {
            String family = intent.getStringExtra("family");
            if (family.equals("family"))
                familyLayout.performClick();
        } else if (intent.hasExtra("allergy")) {
            String allergy = intent.getStringExtra("allergy");
            if (allergy.equals("allergy"))
                allergyLayout.performClick();
        } else if (intent.hasExtra("surgery")) {
            String surgery = intent.getStringExtra("surgery");
            if (surgery.equals("surgery"))
                surgeryLayout.performClick();
        } else if (intent.hasExtra("immunization")) {
            String surgery = intent.getStringExtra("immunization");
            if (surgery.equals("immunization"))
                immunizationLayout.performClick();
        } else if (intent.hasExtra("diagnosis")) {
            String surgery = intent.getStringExtra("diagnosis");
            if (surgery.equals("diagnosis"))
                diagnosisLayout.performClick();
        }
        /*  TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.addTab(tabLayout.newTab().setText("Doctor"));
        tabLayout.addTab(tabLayout.newTab().setText("Disease"));
        tabLayout.addTab(tabLayout.newTab().setText("Family History"));
        tabLayout.addTab(tabLayout.newTab().setText("Allergy"));
        tabLayout.addTab(tabLayout.newTab().setText("Surgery"));
        tabLayout.addTab(tabLayout.newTab().setText("Immunizations"));
        imgBack = findViewById(R.id.img_back_arrow);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager =(ViewPager)findViewById(R.id.patient_view_pager);
        MedicalHistoryTabAdapter medicalHistoryTabAdapter = new MedicalHistoryTabAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(medicalHistoryTabAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
    }*/
    }

    public void casting() {
        diagnosisLayout = findViewById(R.id.diagnosis_layout);
        allergyLayout = findViewById(R.id.layout_allergy);
        txtCount = findViewById(R.id.txt_length);
        immunizationLayout = findViewById(R.id.layout_immunization);
        surgeryLayout = findViewById(R.id.layout_surgery);
        txtDiagnosis = findViewById(R.id.txt_diagnosis);
        imgDiagnosis = findViewById(R.id.img_diagnosis);
        familyLayout = findViewById(R.id.layout_family);
        txtAllergy = findViewById(R.id.txt_allergy);
        txtFamily = findViewById(R.id.txt_family);
        txtDisease = findViewById(R.id.txt_disease);
        txtSurgery = findViewById(R.id.txt_surgery);
        txtImmune = findViewById(R.id.txt_immune);
        imgDisease = findViewById(R.id.img_disease);
        imgAllergy = findViewById(R.id.img_allergy);
        imgImmune = findViewById(R.id.img_immune);
        imgSurgery = findViewById(R.id.img_surgery);
        imgFamily = findViewById(R.id.img_family);
        diseaseLayout = findViewById(R.id.disease_layout);
        allRecycleView = findViewById(R.id.medic_recycle_view);
        imgBack = findViewById(R.id.img_back_arrow);
        txtDelete = findViewById(R.id.txt_delete);
        imgAdd = findViewById(R.id.img_add);
        diagnosisCardView = findViewById(R.id.diagnosis_card);
        preferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        isHourFormat =preferences.getBoolean("is24Hour",false);
        allergyAdapter = new PatientAllergyAdapter(PatientMedicalHistoryActivity.this, txtDelete, txtCount,isHourFormat);
        // diseaseAdapter = new PatientDiseaseAdapter(PatientMedicalHistoryActivity.this);
        immunizationAdapter = new PatientImmunizationAdapter(PatientMedicalHistoryActivity.this, txtDelete, txtCount,isHourFormat);
        diagnosisAdapter = new DiagnosisAdapter(PatientMedicalHistoryActivity.this, txtDelete, txtCount,isHourFormat);
        surgeryAdapter = new PatientSurgeryAdapter(PatientMedicalHistoryActivity.this, txtDelete, txtCount,isHourFormat);
        txtRecordName = findViewById(R.id.txt_records);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PatientMedicalHistoryActivity.this, PatientHomeActivity.class));
        finish();
        super.onBackPressed();
    }

    public void clickListeners() {
/*
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientMedicalRecordsController.getInstance().selectedObject=null;
                PatientMedicalRecordsController.getInstance().selectedDiagnosisObject=null;
                PatientMedicalRecordsController.getInstance().selectedImmunizationObject=null;
                PatientMedicalRecordsController.getInstance().selectedSurgeryObject=null;

                startActivity(new Intent(PatientMedicalHistoryActivity.this,PatientMedicalHistoryAddAcitivity.class));
            finish();
            }
        });
*/
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientMedicalRecordsController.getInstance().selectedObject = null;
                PatientMedicalRecordsController.getInstance().selectedDiagnosisObject = null;
                PatientMedicalRecordsController.getInstance().selectedImmunizationObject = null;
                PatientMedicalRecordsController.getInstance().selectedSurgeryObject = null;
                Intent intent = new Intent(PatientMedicalHistoryActivity.this, PatientMedicalHistoryAddAcitivity.class);

                startActivity(intent
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                // PendingIntent contentIntent = PendingIntent.getActivity(PatientMedicalHistoryActivity.this, 0, intent, 0);
                finish();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        allRecycleView.setVisibility(View.VISIBLE);
        allRecycleViewList(diagnosisAdapter);
        txtRecordName.setText("Diagnosis");
        diagnosisLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // allRecycleView.setVisibility(View.GONE);
                fetchDiagnosisRecords();
                Log.e("fetchDiagnosisRecords","call"+PatientMedicalRecordsController.getInstance().diagnosisObjectArrayList.size() );

                // Toast.makeText(PatientMedicalHistoryActivity.this, "Diagnosis are not available", Toast.LENGTH_SHORT).show();
                allRecycleViewList(diagnosisAdapter);
                txtRecordName.setText("Diagnosis");
                txtDelete.setVisibility(View.GONE);
                txtCount.setText("(" + 0 + ")");

                diagnosisLayout.setBackgroundResource(R.drawable.layout_background_blue);
                imgDiagnosis.setColorFilter(Color.parseColor("#FFFFFF"));
                txtDiagnosis.setTextColor(Color.parseColor("#FFFFFF"));
                layoutBackGround(diseaseLayout, familyLayout, allergyLayout, surgeryLayout, immunizationLayout,
                        imgAllergy, imgDisease, imgFamily, imgImmune, imgSurgery,
                        txtDisease, txtAllergy, txtFamily, txtImmune, txtSurgery);


            }
        });
        MySwipeHelper mySwipeHelper = new MySwipeHelper(PatientMedicalHistoryActivity.this, allRecycleView, 200) {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buffer) {
                buffer.add(new MyButton(PatientMedicalHistoryActivity.this, "", 0, R.drawable.delete_sweep,
                        Color.parseColor("#FBF8F8"),
                        new MyButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                Log.e("checkk", "check");
                                deletePos = pos;
                                showLogoutDialog();
                                // Toast.makeText(getContext(), "deleteClick", Toast.LENGTH_SHORT).show();
                            }
                        }));
            }
        };
        diseaseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allRecycleView.setVisibility(View.VISIBLE);
                if (PatientFamilyDataController.getInstance().getIllnessServerResponse() != null) {
                    txtDelete.setVisibility(View.VISIBLE);
                    diseaseAdapter = new PatientDiseaseAdapter(PatientMedicalHistoryActivity.this,
                            PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords(), txtDelete, txtCount,isHourFormat);
                    allRecycleViewList(diseaseAdapter);
                } else {
                    txtDelete.setVisibility(View.GONE);
                    txtCount.setText("(" + 0 + ")");
                    allRecycleView.setVisibility(View.GONE);
                }
                name = "immune";
                txtRecordName.setText("Disease");
                diseaseLayout.setBackgroundResource(R.drawable.layout_background_blue);
                imgDisease.setColorFilter(Color.parseColor("#FFFFFF"));
                txtDisease.setTextColor(Color.parseColor("#FFFFFF"));
                layoutBackGround(diagnosisLayout, familyLayout, allergyLayout, surgeryLayout, immunizationLayout,
                        imgAllergy, imgDiagnosis, imgFamily, imgImmune, imgSurgery,
                        txtDiagnosis, txtAllergy, txtFamily, txtImmune, txtSurgery);


            }
        });
        allergyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                allRecycleView.setVisibility(View.VISIBLE);
                allRecycleViewList(allergyAdapter);
                txtRecordName.setText("Allergies");
                allergyLayout.setBackgroundResource(R.drawable.layout_background_blue);
                imgAllergy.setColorFilter(Color.parseColor("#FFFFFF"));
                txtAllergy.setTextColor(Color.parseColor("#FFFFFF"));
                layoutBackGround(diagnosisLayout, familyLayout, diseaseLayout, surgeryLayout, immunizationLayout,
                        imgDisease, imgDiagnosis, imgFamily, imgImmune, imgSurgery,
                        txtDiagnosis, txtDisease, txtFamily, txtImmune, txtSurgery);

            }
        });
        surgeryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allRecycleViewList(surgeryAdapter);
                allRecycleView.setVisibility(View.VISIBLE);
                txtRecordName.setText("Surgery");
                surgeryLayout.setBackgroundResource(R.drawable.layout_background_blue);
                imgSurgery.setColorFilter(Color.parseColor("#FFFFFF"));
                txtSurgery.setTextColor(Color.parseColor("#FFFFFF"));
                layoutBackGround(diagnosisLayout, familyLayout, diseaseLayout, allergyLayout, immunizationLayout,
                        imgDisease, imgDiagnosis, imgFamily, imgImmune, imgAllergy,
                        txtDiagnosis, txtDisease, txtFamily, txtImmune, txtAllergy);


            }
        });
        immunizationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allRecycleView.setVisibility(View.VISIBLE);
                allRecycleViewList(immunizationAdapter);
                txtRecordName.setText("Immunization");
                immunizationLayout.setBackgroundResource(R.drawable.layout_background_blue);
                imgImmune.setColorFilter(Color.parseColor("#FFFFFF"));
                txtImmune.setTextColor(Color.parseColor("#FFFFFF"));
                layoutBackGround(diagnosisLayout, familyLayout, diseaseLayout, allergyLayout, surgeryLayout,
                        imgDisease, imgDiagnosis, imgFamily, imgSurgery, imgAllergy,
                        txtDiagnosis, txtDisease, txtFamily, txtSurgery, txtAllergy);

            }
        });
        txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conditon = false;
                showLogoutDialog();
            }
        });
        familyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                familyLayout.setBackgroundResource(R.drawable.layout_background_blue);
                imgFamily.setColorFilter(Color.parseColor("#FFFFFF"));
                txtFamily.setTextColor(Color.parseColor("#FFFFFF"));
                layoutBackGround(diagnosisLayout, immunizationLayout, diseaseLayout, allergyLayout, surgeryLayout,
                        imgDisease, imgDiagnosis, imgImmune, imgSurgery, imgAllergy,
                        txtDiagnosis, txtDisease, txtImmune, txtSurgery, txtAllergy);

                if (PatientFamilyDataController.getInstance().getResponseObject() != null) {
                    Log.e("non", "data");
                    allRecycleView.setVisibility(View.VISIBLE);
                    familyList = PatientFamilyDataController.getInstance().getResponseObject().getRecords().getFamliyDiseases();
                    Log.e("list", "size" + familyList.size());
                    familyHistoryAdapter = new PatientFamilyHistoryAdapter(PatientMedicalHistoryActivity.this, familyList, txtDelete, txtCount,isHourFormat);
                    allRecycleViewList(familyHistoryAdapter);
                }else {
                    txtDelete.setVisibility(View.GONE);
                    txtCount.setText("(" + 0 + ")");
                    allRecycleView.setVisibility(View.GONE);
                }
                txtRecordName.setText("Family History");
            }
        });
    }

    public void allRecycleViewList(RecyclerView.Adapter adapter) {
        allRecycleView.setLayoutManager(new LinearLayoutManager(PatientMedicalHistoryActivity.this));
        allRecycleView.setAdapter(adapter);
    }

    public void layoutBackGround(RelativeLayout r1, RelativeLayout r2, RelativeLayout r3, RelativeLayout r4, RelativeLayout r5
            , ImageView i1, ImageView i2, ImageView i3, ImageView i4, ImageView i5, TextView t1, TextView t2,
                                 TextView t3, TextView t4, TextView t5) {
        r1.setBackgroundResource(R.drawable.layout_background_green);
        r2.setBackgroundResource(R.drawable.layout_background_green);
        r3.setBackgroundResource(R.drawable.layout_background_green);
        r4.setBackgroundResource(R.drawable.layout_background_green);
        r5.setBackgroundResource(R.drawable.layout_background_green);
        i1.setColorFilter(Color.parseColor("#22928F"));
        i2.setColorFilter(Color.parseColor("#22928F"));
        i3.setColorFilter(Color.parseColor("#22928F"));
        i4.setColorFilter(Color.parseColor("#22928F"));
        i5.setColorFilter(Color.parseColor("#22928F"));
        t1.setTextColor(Color.parseColor("#3E454C"));
        t2.setTextColor(Color.parseColor("#3E454C"));
        t3.setTextColor(Color.parseColor("#3E454C"));
        t4.setTextColor(Color.parseColor("#3E454C"));
        t5.setTextColor(Color.parseColor("#3E454C"));

    }

    public void showLogoutDialog() {
        final Dialog dialog = new Dialog(PatientMedicalHistoryActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_abort);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
        btnNo.setText("Cancel");
        btnYes.setText("Delete");

        TextView txt_title = dialog.findViewById(R.id.title);
        TextView txt_msg = dialog.findViewById(R.id.msg);
        TextView txt_msg1 = dialog.findViewById(R.id.msg1);

        txt_title.setText("Delete");
        txt_msg.setText("Are you sure you");
        txt_msg1.setText("want to Delete ?");

        RelativeLayout main = (RelativeLayout) dialog.findViewById(R.id.rl_main);
        RelativeLayout main1 = (RelativeLayout) dialog.findViewById(R.id.rl_main1);

        GradientDrawable drawable = (GradientDrawable) main.getBackground();
        drawable.setColor(getResources().getColor(R.color.colorWhite));

        GradientDrawable drawable1 = (GradientDrawable) main1.getBackground();
        drawable1.setColor(getResources().getColor(R.color.colorWhite));

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  refreshShowingDialog.showAlert();
                if (!conditon) {


                    Log.e("DelALl", "dngjngdk");

                    refreshShowingDialog.showAlert();
                    Log.e("what", "da");
                    dialog.dismiss();
                    if (txtRecordName.getText().toString().contains("Allergies")) {
                        if (isNetworkConnected()) {
                            refreshShowingDialog.showAlert();
                            deleteAllAllergiesApi();
                        } else {
                            dialogeforCheckavilability("Error", "Please check internet connection", "ok");

                        }
                    } else if (txtRecordName.getText().toString().contains("Immunization")) {
                        refreshShowingDialog.showAlert();

                        if (isNetworkConnected()) {

                            deleteImmunizationSApi(false);
                        } else {
                            refreshShowingDialog.hideRefreshDialog();
                            dialogeforCheckavilability("Error", "Please check internet connection", "ok");

                        }
                    } else if (txtRecordName.getText().toString().contains("Surgery")) {
                        if (isNetworkConnected()) {
                            deleteAllSurgeryApi(false);
                        } else {
                            refreshShowingDialog.hideRefreshDialog();

                            dialogeforCheckavilability("Error", "Please check internet connection", "ok");

                        }
                    } else if (txtRecordName.getText().toString().contains("Disease")) {
                        if (isNetworkConnected()) {
                            recordsApi();
                        } else {
                            refreshShowingDialog.hideRefreshDialog();

                            dialogeforCheckavilability("Error", "Please check internet connection", "ok");

                        }
                    } else if (txtRecordName.getText().toString().contains("Family History")) {
                        if (isNetworkConnected()) {
                            recordsApi();
                            //accessInterfaceMethod();
                        } else {
                            refreshShowingDialog.hideRefreshDialog();

                            dialogeforCheckavilability("Error", "Please check internet connection", "ok");

                        }
                    } else if (txtRecordName.getText().toString().contains("Diagnosis")) {
                        if (isNetworkConnected()) {
                            Log.e("dddddddddddd", "call");
                            refreshShowingDialog.showAlert();
                            deleteAllDiagnosisApi(false);
                        } else {
                            refreshShowingDialog.hideRefreshDialog();
                            dialogeforCheckavilability("Error", "Please check internet connection", "ok");
                        }
                    }
                } else {

                    Log.e("posi", "aa" + deletePos);
                    if (txtRecordName.getText().toString().equals("Disease")) {
                        refreshShowingDialog.showAlert();
                        if (isNetworkConnected()) {
                            deleteIllnessApi(deletePos);
                            dialog.dismiss();
                            //  accessIllnessInterface(deletePos);

                        } else {
                            refreshShowingDialog.hideRefreshDialog();
                            dialogeforCheckavilability("Error", "Please check internet connection", "ok");
                        }

                    } else {
                        refreshShowingDialog.showAlert();

                        if (txtRecordName.getText().toString().contains("Family History")) {
                            if (isNetworkConnected()) {

                                Log.e("sjbfj", "jbfj");

                                deleteSingleItem(deletePos);
                                // accessInterfaceMethod();

                            } else {
                                refreshShowingDialog.hideRefreshDialog();
                                dialogeforCheckavilability("Error", "Please check internet connection", "ok");
                            }
                        }

                        if (txtRecordName.getText().toString().contains("Allergies")) {
                            if (PatientMedicalRecordsController.getInstance().allergyObjectArrayList.size() > 0) {
                                if (isNetworkConnected()) {
                                    if (PatientMedicalRecordsController.getInstance().noteallergyArray.size() > 0) {
                                        PatientMedicalRecordsController.getInstance().noteallergyArray.remove(deletePos);
                                        //  refreshShowingDialog.showAlert();
                                        loadSingleAllergiesApi();
                                    }
                                } else {
                                    refreshShowingDialog.showAlert();
                                    dialogeforCheckavilability("Error", "Please check internet connection", "ok");

                                }
                            }
                        } else if (txtRecordName.getText().toString().contains("Immunization")) {
                            if (isNetworkConnected()) {
                                if (PatientMedicalRecordsController.getInstance().immunizationArrayList.size() > 0) {
                                    //  refreshShowingDialog.showAlert();
                                    PatientMedicalRecordsController.getInstance().selectedImmunizationObject = PatientMedicalRecordsController.getInstance().immunizationArrayList.get(deletePos);
                                    deleteImmunizationSApi(true);
                                }
                            } else {
                                refreshShowingDialog.hideRefreshDialog();
                                dialogeforCheckavilability("Error", "Please check internet connection", "ok");

                            }
                        } else if (txtRecordName.getText().toString().contains("Surgery")) {
                            if (isNetworkConnected()) {
                                refreshShowingDialog.showAlert();
                                deleteAllSurgeryApi(true);
                            } else {
                                refreshShowingDialog.hideRefreshDialog();
                                dialogeforCheckavilability("Error", "Please check internet connection", "ok");

                            }
                        } else if (txtRecordName.getText().toString().contains("Diagnosis")) {
                            if (isNetworkConnected()) {
                                refreshShowingDialog.showAlert();
                                deleteAllDiagnosisApi(true);
                            } else {
                                refreshShowingDialog.hideRefreshDialog();
                                dialogeforCheckavilability("Error", "Please check internet connection", "ok");
                            }
                        }
                    }
                    dialog.dismiss();

                }
            }
        });
        dialog.show();

    }

    private void deleteAllDiagnosisApi(boolean isSigleDelete) {
        JSONObject params = new JSONObject();
        if (PatientMedicalRecordsController.getInstance().diagnosisObjectArrayList.size() > 0) {
            try {
                params.put("byWhom", "Patient");
                params.put("byWhomID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
                params.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
                params.put("medical_record_id", PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
                params.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
                if (PatientFamilyDataController.getInstance().getIllnessPatientList() != null) {
                    if (PatientFamilyDataController.getInstance().getIllnessPatientList().size() > 0) {
                        params.put("illnessID", PatientFamilyDataController.getInstance().getIllnessPatientList().get(0).getIllnessID());
                    }
                }
                if (isSigleDelete) {
                    params.put("illnessDiagnosisID", PatientMedicalRecordsController.getInstance().diagnosisObjectArrayList.get(deletePos).getIllnessDiagnosisID());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
            Log.e("diagnosisService", "onResponse: " + gsonObject.toString());
            if (isSigleDelete) {
                ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.deleteSingleDiagnosis(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "deleteSingleDiagnosis");
            } else {
                ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.deleteAllDiagnosis(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "deleteAllDiagnosis");
            }
        } else {
            Toast.makeText(getApplicationContext(), "No Records Found", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(PatientMedicalHistoryActivity.this.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public void recordsApi() {
        try {
            fetchObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile
                    .getHospital_reg_number());
            fetchObject.put("byWhom", "patient");
            fetchObject.put("byWhomID", PatientLoginDataController.getInstance().currentPatientlProfile
                    .getPatientId());
            fetchObject.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile
                    .getPatientId());
            fetchObject.put("medical_record_id", PatientLoginDataController.getInstance().currentPatientlProfile
                    .getMedicalRecordId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser deleteData = new JsonParser();
        JsonObject deleteObject = (JsonObject) deleteData.parse(fetchObject.toString());

        if (txtRecordName.getText().toString().contains("Family History")) {
            Log.e("delete", "ob " + PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken() + "," + deleteObject);
            ApiCallDataController.getInstance().loadjsonApiCall(
                    ApiCallDataController.getInstance().serverApi.
                            deleteAllPatientFamilyHistory(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), deleteObject), "deleteAllFamily");

        } else if (txtRecordName.getText().toString().contains("Disease")) {
            ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.
                    deleteAllPatientIllnessHistory(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), deleteObject), "deleteAllIllness");

        }
    }

    public void deleteSingleItem(int position) {
        Log.e("possss", "tionn" + position);
        if (PatientFamilyDataController.getInstance().getResponseObject() != null) {
            Log.e("lenghth", "ff" + PatientFamilyDataController.getInstance().getResponseObject().getRecords().getFamliyDiseases().size());
            PatientFamilyDataController.getInstance().getResponseObject().getRecords().getFamliyDiseases().remove(position);
            familyList = PatientFamilyDataController.getInstance().getResponseObject().getRecords()
                    .getFamliyDiseases();
            PatientFamilyDataController.getInstance().getResponseObject().getRecords()
                    .setFamliyDiseases(familyList);
            Log.e("lpao", "ff" + PatientFamilyDataController.getInstance().getResponseObject().getRecords().getFamliyDiseases().size());

            array = new JSONArray();
            for (int i = 0; familyList.size() > i; i++) {
                try {

                    detailsObj = new JSONObject();
                    detailsObj.put("dieseaseName", PatientFamilyDataController.getInstance().getResponseObject().getRecords()
                            .getFamliyDiseases().get(i).getDieseaseName());
                    detailsObj.put("relationship", PatientFamilyDataController.getInstance().getResponseObject().getRecords()
                            .getFamliyDiseases().get(i).getRelationship());
                    detailsObj.put("age", PatientFamilyDataController.getInstance().getResponseObject().getRecords()
                            .getFamliyDiseases().get(i).getAge());
                    detailsObj.put("note", PatientFamilyDataController.getInstance().getResponseObject().getRecords()
                            .getFamliyDiseases().get(i).getNote());

                    Log.e("dsjbnf", "sknf" + detailsObj.toString());

                    array.put(detailsObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            addObject = new JSONObject();

            try {
                addObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile
                        .getHospital_reg_number());
                addObject.put("byWhom", "patient");
                addObject.put("byWhomID", PatientLoginDataController.getInstance().currentPatientlProfile
                        .getPatientId());
                addObject.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile
                        .getPatientId());

                addObject.put("medical_record_id", PatientLoginDataController.getInstance().currentPatientlProfile
                        .getMedicalRecordId());
                if (PatientFamilyDataController.getInstance().getResponseObject() != null)
                    addObject.put("family_history_record_id", PatientFamilyDataController.getInstance().
                            getResponseObject().getRecords().getFamily_history_record_id());
                addObject.put("famliyDiseases", array);
                Log.e("objecttt", "" + addObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject) jsonParser.parse(addObject.toString());
            ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.addPatientFamilyHistory(PatientLoginDataController.getInstance()
                    .currentPatientlProfile.getAccessToken(), gsonObject), "deleteSingleFamily");

            Log.e("finalList", "ff" + PatientFamilyDataController.getInstance().getResponseObject().getRecords().getFamliyDiseases().size());

            Log.e("kkkkk", "lll");

        }
    }

    public void deleteIllnessApi(int pos) {
        addObject = new JSONObject();

        try {
            addObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile
                    .getHospital_reg_number());

            addObject.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile
                    .getPatientId());

            addObject.put("medical_record_id", PatientLoginDataController.getInstance().currentPatientlProfile
                    .getMedicalRecordId());
            if (PatientFamilyDataController.getInstance().getIllnessServerResponse() != null)
                addObject.put("illnessID", PatientFamilyDataController.getInstance().
                        getIllnessServerResponse().getIllnessRecords().get(pos).getIllnessID());
            addObject.put("byWhom", "patient");
            addObject.put("byWhomID", PatientLoginDataController.getInstance().currentPatientlProfile
                    .getPatientId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("delete", "dd" + addObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(addObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.deletePatientIllnessHistory(PatientLoginDataController.getInstance()
                .currentPatientlProfile.getAccessToken(), gsonObject), "deleteSingleIllness");

    }

    private void deleteAllAllergiesApi() {
        JSONObject params = new JSONObject();
        try {
            params.put("byWhom", "Patient");
            params.put("byWhomID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("medical_record_id", PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
            params.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.deleteAlergiesApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "deleteAll");
    }

    private void deleteImmunizationSApi(boolean isSingleObject) {
        JSONObject params = new JSONObject();
        try {
            params.put("byWhom", "patient");
            params.put("byWhomID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("medical_record_id", PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
            params.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
            if (isSingleObject) {
                params.put("immunization_record_id", PatientMedicalRecordsController.getInstance().selectedImmunizationObject.getImmunization_record_id());
            }
            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
            Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
            if (isSingleObject) {
                params.put("immunization_record_id", PatientMedicalRecordsController.getInstance().selectedImmunizationObject.getImmunization_record_id());
                ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.deleteSingleImmuApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "deleteSingleImmuni");
            } else {
                ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.deleteAllImmuApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "deleteImmuniMultiple");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                try {
                    if (jsonObject.getString("response").equals("3")) {
                        if (curdOpetaton.equals("deleteAll")) {
                            Log.e("deleteresponse", "call" + jsonObject.toString());
                            refreshShowingDialog.hideRefreshDialog();
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            PatientMedicalRecordsController.getInstance().allergyObjectArrayList.clear();
                            PatientMedicalRecordsController.getInstance().noteallergyArray.clear();
                            allergyAdapter.notifyDataSetChanged();
                        } else if (curdOpetaton.equals("deleteSingle")) {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            refreshShowingDialog.hideRefreshDialog();
                            allergyAdapter.notifyDataSetChanged();
                        } else if (curdOpetaton.equals("deleteSingleImmuni")) {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            refreshShowingDialog.hideRefreshDialog();
                            PatientMedicalRecordsController.getInstance().immunizationArrayList.remove(PatientMedicalRecordsController.getInstance().selectedImmunizationObject);
                            immunizationAdapter.notifyDataSetChanged();
                        } else if (curdOpetaton.equals("deleteImmuniMultiple")) {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            refreshShowingDialog.hideRefreshDialog();
                            PatientMedicalRecordsController.getInstance().immunizationArrayList.clear();
                            immunizationAdapter.notifyDataSetChanged();
                        } else if (curdOpetaton.equals("fetchSurgical")) {
                            PatientMedicalRecordsController.getInstance().surgeryObjectArrayList = new ArrayList<>();
                            Log.e("dffddddddddd", "clll" + jsonObject.toString());
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = jsonObject.getJSONArray("illnessSurgicalRecords");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Gson gson = new Gson();
                                    PatientSurgicalObject obj = null;
                                    try {
                                        obj = gson.fromJson(jsonArray.getJSONObject(i).toString(), PatientSurgicalObject.class);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Log.e("fetchSurgical", "clll" + obj.getSurgeryInformation() + obj.getMoreDetails());
                                    PatientMedicalRecordsController.getInstance().surgeryObjectArrayList.add(obj);
                                }
                                surgeryAdapter.notifyDataSetChanged();
                            }/*else {
PatientMedicalRecordsController.getInstance().surgeryObjectArrayList.clear();
surgeryAdapter.notifyDataSetChanged();
}*/
                        } else if (curdOpetaton.equals("deleteAllSurgical")) {
                            Log.e("deleteAllSurgical", "clll" + jsonObject.toString());
                            try {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            PatientMedicalRecordsController.getInstance().surgeryObjectArrayList.clear();
                            allRecycleViewList(surgeryAdapter);
                            surgeryAdapter.notifyDataSetChanged();
                            refreshShowingDialog.hideRefreshDialog();
                        } else if (curdOpetaton.equals("deleteSingleSurgical")) {
                            refreshShowingDialog.hideRefreshDialog();
                            try {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            PatientMedicalRecordsController.getInstance().surgeryObjectArrayList.remove(PatientMedicalRecordsController.getInstance().selectedSurgeryObject);
                            surgeryAdapter.notifyDataSetChanged();
                        } else if (curdOpetaton.equals("deleteSingleIllness")) {
                            refreshShowingDialog.hideRefreshDialog();
                            try {
                                if (jsonObject.get("response").equals(3)) {
                                    // illnessList.remove(posi);
                                    PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords().remove(deletePos);

                                    diseaseAdapter.notifyDataSetChanged();
                                    //  Toast.makeText(getApplicationContext(), "illness Records are deleted", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            diseaseAdapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "illness Record is deleted", Toast.LENGTH_SHORT).show();

                        } else if (curdOpetaton.equals("deleteAllIllness")) {
                            refreshShowingDialog.hideRefreshDialog();
                            try {
                                if (jsonObject.get("response").equals(3)) {
                                    PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords().clear();
                                    diseaseAdapter.notifyDataSetChanged();
                                    //  Toast.makeText(getApplicationContext(), "illness Records are deleted", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        if (curdOpetaton.equals("deleteAllFamily")) {
                            Log.e("dadfs", "sdasdf");
                            refreshShowingDialog.hideRefreshDialog();

                            // allRecycleView.setVisibility(View.GONE);
                            if (jsonObject.get("response").equals(3)) {
                                familyList.clear();
                                Log.e("clear", "list");
                                familyHistoryAdapter.notifyDataSetChanged();
                                Log.e("sfdf", "" + familyList.size());
                            }
                            PatientFamilyDataController.getInstance().getResponseObject().getRecords().getFamliyDiseases().clear();
                            Toast.makeText(getApplicationContext(), "Family Records are deleted Successfully", Toast.LENGTH_SHORT).show();

                        } else if (curdOpetaton.equals("deleteSingleFamily")) {
                            Log.e("del Sing", "ff" + PatientFamilyDataController.getInstance().getResponseObject().getRecords().getFamliyDiseases().size());
                            refreshShowingDialog.hideRefreshDialog();
                            try {
                                if (jsonObject.get("response").equals(3)) {
                                    refreshShowingDialog.hideRefreshDialog();
                                    Log.e("dfas", "dsa");
                                    Toast.makeText(getApplicationContext(), "Family Record is deleted", Toast.LENGTH_SHORT).show();
                                    familyList = PatientFamilyDataController.getInstance().getResponseObject().getRecords().getFamliyDiseases();
                                    Log.e("jkdbgfj", "jfn" + familyList.size());
                                    familyHistoryAdapter.notifyDataSetChanged();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //  Toast.makeText(getApplicationContext(), "Family Record is updated", Toast.LENGTH_SHORT).show();

                        } else if (curdOpetaton.equals("deleteSingleDiagnosis")) {
                            refreshShowingDialog.hideRefreshDialog();
                            PatientMedicalRecordsController.getInstance().diagnosisObjectArrayList.remove(PatientMedicalRecordsController.getInstance().diagnosisObjectArrayList.get(deletePos));
                            try {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            diagnosisAdapter.notifyDataSetChanged();
                        } else if (curdOpetaton.equals("deleteAllDiagnosis")) {
                            Log.e("deleteAllDiagnosis", "clll" + jsonObject.toString());
                            refreshShowingDialog.hideRefreshDialog();

                            PatientMedicalRecordsController.getInstance().diagnosisObjectArrayList.clear();
                            diagnosisAdapter.notifyDataSetChanged();
                            try {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                       else if (curdOpetaton.equals("fetchdiagnosis")) {
                            try {
                                if (jsonObject.getString("response").equals("3")) {
                                    PatientMedicalRecordsController.getInstance().diagnosisObjectArrayList.clear();
                                    JSONArray diagnosisArray = jsonObject.getJSONArray("illnessDiagnosisRecords");
                                    Log.e("diagnosisArray", "call" + diagnosisArray.length());
                                    if (diagnosisArray.length() > 0) {
                                        for (int i = 0; i < diagnosisArray.length(); i++) {
                                            JSONObject obj = diagnosisArray.getJSONObject(i);
                                            Gson gson = new Gson();
                                            DiagnosisObject immunizationObject = gson.fromJson(obj.toString(), DiagnosisObject.class);
                                            Log.e("idforimmnunization", "call" + immunizationObject.getIllnessDiagnosisID());
                                            PatientMedicalRecordsController.getInstance().diagnosisObjectArrayList.add(immunizationObject);
                                        }
                                        diagnosisAdapter.notifyDataSetChanged();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        refreshShowingDialog.hideRefreshDialog();
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void failureCallBack(String failureMsg) {
                refreshShowingDialog.hideRefreshDialog();
                Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSingleAllergiesApi() {
        JSONObject params = new JSONObject();
        JSONArray noteArray = new JSONArray();
        try {
            if (PatientMedicalRecordsController.getInstance().allergyObjectArrayList.size() > 0) {
                if (PatientMedicalRecordsController.getInstance().noteallergyArray.size() > 0) {
                    for (int i = 0; i < PatientMedicalRecordsController.getInstance().noteallergyArray.size(); i++) {
                        AllergyListObject noteList = PatientMedicalRecordsController.getInstance().noteallergyArray.get(i);
                        JSONObject noteSingleObject = new JSONObject();
                        noteSingleObject.put("name", noteList.getName());
                        noteSingleObject.put("note", noteList.getNote());
                        noteArray.put(noteSingleObject);
                    }
                }
                params.put("allergy_record_id", PatientMedicalRecordsController.getInstance().allergyObjectArrayList.get(0).getAllergy_record_id());
            }
            params.put("byWhom", "patient");
            params.put("byWhomID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("medical_record_id", PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
            params.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
            params.put("allergies", noteArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.addAlergiesApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "deleteSingle");
    }

    private void deleteAllSurgeryApi(boolean isSigleDelete) {
        JSONObject params = new JSONObject();
        try {
            params.put("byWhom", "Patient");
            params.put("byWhomID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("medical_record_id", PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
            params.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
            if (PatientFamilyDataController.getInstance().getIllnessPatientList() != null) {
                if (PatientFamilyDataController.getInstance().getIllnessPatientList().size() > 0) {
                    params.put("illnessID", PatientFamilyDataController.getInstance().getIllnessPatientList().get(0).getIllnessID());
                }
            }
            if (isSigleDelete) {
                params.put("illnessSurgicalID", PatientMedicalRecordsController.getInstance().selectedSurgeryObject.getIllnessSurgicalID());
                params.put("filePath", PatientMedicalRecordsController.getInstance().selectedSurgeryObject.getFilePath());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("surgeryServiceResponse", "onResponse: " + gsonObject.toString());
        if (isSigleDelete) {
            ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.deleteSingleSurgeryApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "deleteSingleSurgical");
        } else {
            ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.deleteAllSurgeryApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "deleteAllSurgical");
        }
    }

    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter = new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PatientMedicalHistoryActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);
    }
}
