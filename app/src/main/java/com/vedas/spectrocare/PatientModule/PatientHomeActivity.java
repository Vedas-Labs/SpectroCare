package com.vedas.spectrocare.PatientModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBaseModels.PatientModel;
import com.vedas.spectrocare.PatientAppointmentModule.AppointmentArrayModel;
import com.vedas.spectrocare.PatientAppointmentModule.PatientAppointmentsDataController;
import com.vedas.spectrocare.PatientMoreModule.SettingsActivity;
import com.vedas.spectrocare.PatientNotificationModule.PatientNotificationFragment;
import com.vedas.spectrocare.PatientServerApiModel.AllergyListObject;
import com.vedas.spectrocare.PatientServerApiModel.AllergyObject;
import com.vedas.spectrocare.PatientServerApiModel.DiagnosisObject;
import com.vedas.spectrocare.PatientServerApiModel.IllnessMedicationRecords;
import com.vedas.spectrocare.PatientServerApiModel.IllnessPatientRecord;
import com.vedas.spectrocare.PatientServerApiModel.ImmunizationObject;
import com.vedas.spectrocare.PatientServerApiModel.MannualPrescriptions;
import com.vedas.spectrocare.PatientServerApiModel.Medicines;
import com.vedas.spectrocare.PatientServerApiModel.PatientFamilyAddServerObject;
import com.vedas.spectrocare.PatientServerApiModel.PatientIllnessServerResponse;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicationArrayObject;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicationObject;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicationResponsModel;
import com.vedas.spectrocare.PatientServerApiModel.PatientSurgicalObject;
import com.vedas.spectrocare.PatientServerApiModel.PatientSurgicalRecordServerObjectDataController;
import com.vedas.spectrocare.PatinetControllers.PatientAppointmentController;
import com.vedas.spectrocare.PatinetControllers.PatientFamilyDataController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.ChangePasswordActivity;
import com.vedas.spectrocare.activities.LoginActivity;
import com.vedas.spectrocare.activities.SelectUserActivity;
import com.vedas.spectrocare.patientModuleAdapter.PatientDiseaseAdapter;
import com.vedas.spectrocare.patient_fragment.BillingAndPaymentFragment;
import com.vedas.spectrocare.patient_fragment.HomeFragment;
/*import com.vedas.spectrocare.patient_fragment.MoreAlertFragment;*/
import com.vedas.spectrocare.patient_fragment.MoreFragment;
import com.vedas.spectrocare.patient_fragment.PatientCalendarFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import static com.vedas.spectrocare.PatinetControllers.CardDetailsController.obj;

public class PatientHomeActivity extends AppCompatActivity {
    ArrayList patientsList;
    IllnessPatientRecord illnessPatientRecord;
    ArrayList<IllnessPatientRecord> illnessList;
    BottomNavigationView bottomNavigation;
    PatientFamilyAddServerObject responseObject;
    PatientIllnessServerResponse serverResponse;
    ArrayList<AppointmentArrayModel> modelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);
        modelArrayList = new ArrayList<>();
        PatientAppointmentController.setNull();
        PatientSurgicalRecordServerObjectDataController.getInstance().fillContent(getApplicationContext());
        ApiCallDataController.getInstance().fillContent(getApplicationContext());
        PatientMedicalRecordsController.getInstance().fillContent(getApplicationContext());
        accessInterfaceMethods();
        fetchAllergiesApi();
        fetchPAtientImmunizationApi();
      //  fetchAppointmentDetails();
        fetchPatientFamilyApi();
        fetchPatientIllnessRecordApi();
        Log.e("AllHailToKing", "id :" + PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalPerson_id());

        patientsList = new ArrayList();
        illnessPatientRecord = new IllnessPatientRecord();
        illnessList = new ArrayList<>();

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        bottomNavigation.setSelectedItemId(R.id.navigation_home);
        openFragment(HomeFragment.newInstance("", ""));
        PatientLoginDataController.getInstance().fetchPatientlProfileData();

    }

    private void fetchAppointmentDetails() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
            jsonObject.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject body = (JsonObject) jsonParser.parse(jsonObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.fetchAppointmentDetaisl(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken()
                , body), "fetchAppointment");
    }

    private void fetchAllergiesApi() {
        JSONObject params = new JSONObject();
        try {
            params.put("byWhom", "patient");
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
        ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.fetchAlergiesApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "fetch");
    }

    private void fetchPAtientImmunizationApi() {
        JSONObject params = new JSONObject();
        try {
            params.put("byWhom", "patient");
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
        ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.fetchImmunizationApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "fetchImmunization");
    }

    public void fetchPatientIllnessRecordApi() {
        Log.e("adff", "aff");
        JSONObject fetchObject = new JSONObject();
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
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(fetchObject.toString());
        Log.e("send", "data" + gsonObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(
                ApiCallDataController.getInstance().serverJsonApi.
                        fetchAllPatientIllness(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "illnessFetch");

    }

    public void fetchPatientFamilyApi() {
        JSONObject fetchObject = new JSONObject();
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
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(fetchObject.toString());
        Log.e("send", "data" + gsonObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(
                ApiCallDataController.getInstance().serverJsonApi.
                        fetchPatientFamilyHistory(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "fetchFamily");

    }

    public void fetchMedicationRecords() {
        PatientModel currentModel = PatientLoginDataController.getInstance().currentPatientlProfile;
        JSONObject params = new JSONObject();
        try {
            params.put("hospital_reg_num", currentModel.getHospital_reg_number());
            params.put("byWhom", "patient");
            params.put("byWhomID", currentModel.getPatientId());
            params.put("patientID", currentModel.getPatientId());
            params.put("medical_record_id", currentModel.getMedicalRecordId());
            params.put("illnessID", illnessList.get(0).getIllnessID());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.fetchPatientMedicationApi(currentModel.getAccessToken(), gsonObject), "fetchMedications");
    }

    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {

                if (curdOpetaton.equals("fetchAppointment")) {

                    try {
                        Log.e("kkkkkk", "dd" + jsonObject.getString("response"));

                        if (jsonObject.getString("response").equals("3")) {
                            JSONArray appointmentArray = jsonObject.getJSONArray("appointments");
                            Log.e("appontment", "length" + appointmentArray.length());

                            for (int l = 0; l < appointmentArray.length(); l++) {
                                Gson gson = new Gson();
                                String jsonString = jsonObject.getJSONArray("appointments").getJSONObject(l).toString();
                                AppointmentArrayModel appointmentList = gson.fromJson(jsonString, AppointmentArrayModel.class);
                                String kk = gson.toJson(appointmentList);
                                Log.e("logaaaa", "daffff" + kk);
                                modelArrayList.add(appointmentList);
                                PatientAppointmentsDataController.getInstance().setAppointmentsList(modelArrayList);

                            }
                            Log.e("ffffaaaaa", "dgga" + PatientAppointmentsDataController.getInstance().getAppointmentsList().size());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (curdOpetaton.equals("fetch")) {
                    Log.e("allergyresponse", "call" + jsonObject.toString());
                    try {
                        if (jsonObject.getString("response").equals("3")) {
                            JSONObject recordsObj = jsonObject.getJSONObject("records");
                            JSONArray allergiesArray = recordsObj.getJSONArray("allergies");
                            AllergyObject allergieModel = new AllergyObject();
                            allergieModel.setHospital_reg_num(PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
                            allergieModel.setMedical_record_id(PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
                            allergieModel.setByWhom("Patient");
                            allergieModel.setByWhomID(PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
                            allergieModel.setPatientID(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
                            allergieModel.setCreatedDate(recordsObj.getString("createdDate"));
                            allergieModel.setAllergy_record_id(recordsObj.getString("allergy_record_id"));
                            if (allergiesArray.length() > 0) {
                                for (int i = 0; i < allergiesArray.length(); i++) {
                                    JSONObject allergyObj = (JSONObject) allergiesArray.get(i);
                                    AllergyListObject allergyListObject = new AllergyListObject();
                                    allergyListObject.setNote(allergyObj.getString("note"));
                                    allergyListObject.setName(allergyObj.getString("name"));
                                    PatientMedicalRecordsController.getInstance().noteallergyArray.add(allergyListObject);
                                    Log.e("notesize", "call" + PatientMedicalRecordsController.getInstance().noteallergyArray.size());
                                }
                                allergieModel.setAllergies(PatientMedicalRecordsController.getInstance().noteallergyArray);
                            } else {
                                allergieModel.setAllergies(null);
                            }
                            PatientMedicalRecordsController.getInstance().allergyObjectArrayList.add(allergieModel);
                            Log.e("allergyListsize", "call" + PatientMedicalRecordsController.getInstance().allergyObjectArrayList.size());

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (curdOpetaton.equals("fetchImmunization")) {
                    try {
                        if (jsonObject.getString("response").equals("3")) {
                            JSONArray immunizationArray = jsonObject.getJSONArray("immunization_records");
                            if (immunizationArray.length() > 0) {
                                for (int i = 0; i < immunizationArray.length(); i++) {
                                    JSONObject obj = immunizationArray.getJSONObject(i);
                                    Gson gson = new Gson();
                                    ImmunizationObject immunizationObject = gson.fromJson(obj.toString(), ImmunizationObject.class);
                                    Log.e("idforimmnunization", "call" + immunizationObject.getCreatedDate());
                                    PatientMedicalRecordsController.getInstance().immunizationArrayList.add(immunizationObject);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                        // surgeryAdapter.notifyDataSetChanged();
                    }
                }
                if (curdOpetaton.equals("fetchFamily")) {
                    responseObject = new PatientFamilyAddServerObject();
                    responseObject = JSON.parseObject(jsonObject.toString(), PatientFamilyAddServerObject.class);
                    Gson gson = new Gson();
                    String json = gson.toJson(responseObject.getRecords());
                    Log.e("FamilyRecord", "rrrr" + json);
                    PatientFamilyDataController.getInstance().setResponseObject(responseObject);
                    Log.e("list", "familyList" + PatientFamilyDataController.getInstance().getResponseObject().getRecords().getFamliyDiseases());

                }
                if (curdOpetaton.equals("fetchdiagnosis")) {
                    try {
                        if (jsonObject.getString("response").equals("3")) {
                            JSONArray diagnosisArray = jsonObject.getJSONArray("illnessDiagnosisRecords");
                            if (diagnosisArray.length() > 0) {
                                for (int i = 0; i < diagnosisArray.length(); i++) {
                                    JSONObject obj = diagnosisArray.getJSONObject(i);
                                    Gson gson = new Gson();
                                    DiagnosisObject immunizationObject = gson.fromJson(obj.toString(), DiagnosisObject.class);
                                    Log.e("idforimmnunization", "call" + immunizationObject.getIllnessDiagnosisID());
                                    PatientMedicalRecordsController.getInstance().diagnosisObjectArrayList.add(immunizationObject);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (curdOpetaton.equals("fetchMedications")) {
                    try {
                        if (jsonObject.getString("response").equals("3")) {
                            Gson gson = new Gson();
                            PatientMedicationResponsModel immunizationObject = gson.fromJson(jsonObject.toString(), PatientMedicationResponsModel.class);
                            Log.e("xxxxxxxx", "call" + immunizationObject.getIllnessMedicationRecords().size());
                            if (immunizationObject.getIllnessMedicationRecords().size() > 0) {
                                for (int j = 0; j < immunizationObject.getIllnessMedicationRecords().size(); j++) {

                                    IllnessMedicationRecords medicationRecords = immunizationObject.getIllnessMedicationRecords().get(j);

                                    PatientMedicationObject patientMedicationObject = new PatientMedicationObject();

                                    patientMedicationObject.setIllnessID(medicationRecords.getIllnessID());
                                    patientMedicationObject.setIllnessMedicationID(medicationRecords.getIllnessMedicationID());
                                    patientMedicationObject.setPatientID(medicationRecords.getPatientID());
                                    patientMedicationObject.setHospital_reg_num(medicationRecords.getHospital_reg_num());
                                    patientMedicationObject.setMedical_record_id(medicationRecords.getMedical_record_id());

                                    if (medicationRecords.getMannualPrescriptions() != null) {
                                        MannualPrescriptions mannualPrescriptions = medicationRecords.getMannualPrescriptions();

                                        patientMedicationObject.setDoctorMedicalPersonnelID(mannualPrescriptions.getDoctorMedicalPersonnelID());
                                        patientMedicationObject.setDoctorName(mannualPrescriptions.getDoctorName());

                                        ArrayList<PatientMedicationArrayObject> medicatioArrayObjectsList = new ArrayList<>();

                                        if (mannualPrescriptions.getMedicines().size() > 0) {
                                            for (int k = 0; k < mannualPrescriptions.getMedicines().size(); k++) {
                                                Medicines medicines = mannualPrescriptions.getMedicines().get(k);
                                                PatientMedicationArrayObject mediObj = new PatientMedicationArrayObject();
                                                mediObj.setName(medicines.getName());
                                                mediObj.setDosage(medicines.getDosage());
                                                mediObj.setFreq(medicines.getFreq());
                                                mediObj.setDurationDays(medicines.getDurationDays());
                                                mediObj.setPurpose(medicines.getPurpose());
                                                mediObj.setMoreDetails(medicines.getMoreDetails());
                                                medicatioArrayObjectsList.add(mediObj);
                                            }
                                            patientMedicationObject.setMedicatioArrayObjects(medicatioArrayObjectsList);
                                        }
                                    }
                                    PatientMedicalRecordsController.getInstance().medicationArrayList.add(patientMedicationObject);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (curdOpetaton.equals("illnessFetch")) {
                    try {
                        if (jsonObject.getString("response").equals("3")) {
                            JSONArray illnessArray = jsonObject.getJSONArray("illnessRecords");
                            Gson gson1 = new Gson();
                            String json1 = gson1.toJson(jsonObject);
                            Log.e("fdad", "dadfa" + json1);
                        }
                        Log.e("settings response", "call" + jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    serverResponse = new PatientIllnessServerResponse();
                    serverResponse = JSON.parseObject(jsonObject.toString(), PatientIllnessServerResponse.class);
                    Gson gson = new Gson();
                    String json = gson.toJson(serverResponse);
                    Log.e("response", "rrrr" + json);
                    PatientFamilyDataController.getInstance().setIllnessServerResponse(serverResponse);
                    if (PatientFamilyDataController.getInstance().getIllnessServerResponse() != null) {
                        try {
                            illnessList = PatientFamilyDataController.getInstance().getIllnessServerResponse().getIllnessRecords();
                            if (illnessList.size() > 0) {
                                fetchSurgicalRecords();
                                fetchDiagnosisRecords();
                                fetchMedicationRecords();
                            }
                            PatientFamilyDataController.getInstance().setIllnessPatientList(illnessList);
                            Log.e("size", "daa" + illnessList.size());

                        } catch (Exception e) {
                            e.printStackTrace();
                            // Toast.makeText(PatientHomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }

                }
            }

            @Override
            public void failureCallBack(String failureMsg) {
                // refreshShowingDialog.hideRefreshDialog();
                Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchDiagnosisRecords() {
        PatientModel currentModel = PatientLoginDataController.getInstance().currentPatientlProfile;
        JSONObject params = new JSONObject();
        try {
            params.put("hospital_reg_num", currentModel.getHospital_reg_number());
            params.put("byWhom", "patient");
            params.put("byWhomID", currentModel.getPatientId());
            params.put("patientID", currentModel.getPatientId());
            params.put("medical_record_id", currentModel.getMedicalRecordId());
            params.put("illnessID", illnessList.get(0).getIllnessID());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.fetchDiagnosisApi(currentModel.getAccessToken(), gsonObject), "fetchdiagnosis");
    }

    public void fetchSurgicalRecords() {
        PatientModel currentModel = PatientLoginDataController.getInstance().currentPatientlProfile;
        JSONObject params = new JSONObject();
        try {
            params.put("hospital_reg_num", currentModel.getHospital_reg_number());
            params.put("byWhom", "patient");
            params.put("byWhomID", currentModel.getPatientId());
            params.put("patientID", currentModel.getPatientId());
            params.put("medical_record_id", currentModel.getMedicalRecordId());
            params.put("illnessID", illnessList.get(0).getIllnessID());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.patientfetchSurgeryApi(currentModel.getAccessToken(), gsonObject), "fetchSurgical");
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_notification:
                            openFragment(PatientCalendarFragment.newInstance("", ""));
                            // openFragment(UpcomingAppointmentFragment.newInstance(0));
                            return true;
                        case R.id.icon_notification:
                            openFragment(PatientNotificationFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_home:
                            openFragment(HomeFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_card:
                            openFragment(BillingAndPaymentFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_more:
                            // openFragment(MoreFragment.newInstance("",""));
                            alertDailog();
                            return true;
                    }
                    return false;
                }
            };

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void alertDailog() {
        View view = getLayoutInflater().inflate(R.layout.more_alert, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(Objects.requireNonNull(PatientHomeActivity.this), R.style.BottomSheetDialogTheme);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();

        RelativeLayout rlPSW = dialog.findViewById(R.id.psw);
        RelativeLayout rlProfile = dialog.findViewById(R.id.profile);
        RelativeLayout rlLogout = dialog.findViewById(R.id.logout);
        RelativeLayout rlsettings = dialog.findViewById(R.id.settings);
        RelativeLayout rlHospital = dialog.findViewById(R.id.hospital);

        rlsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            }
        });
        rlHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                loadUrl();
            }
        });
        rlPSW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(getApplicationContext(), ChangePasswordActivity.class));
            }
        });
        rlProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(getApplicationContext(), PatientProfileActivity.class));
            }
        });
        rlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
                dialog.dismiss();

            }
        });
    }

    private void loadUrl() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://spectrochips.com"));
        startActivity(browserIntent);
    }

    public void showLogoutDialog() {
        Log.e("logggg", "out");
        final Dialog dialog = new Dialog(PatientHomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_abort);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
        dialog.show();
        btnNo.setText("Cancel");
        btnYes.setText("LogOut");

        TextView txt_title = dialog.findViewById(R.id.title);
        TextView txt_msg = dialog.findViewById(R.id.msg);
        TextView txt_msg1 = dialog.findViewById(R.id.msg1);

        txt_title.setText("LogOut");
        txt_msg.setText("Are you sure you");
        txt_msg1.setText("want to logout ?");

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
                if (PatientLoginDataController.getInstance().deletePatientData(PatientLoginDataController.getInstance().currentPatientlProfile)) {
                    Log.e("ddd", "ss");
                    startActivity(new Intent(getApplicationContext(), SelectUserActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();

    }

}


