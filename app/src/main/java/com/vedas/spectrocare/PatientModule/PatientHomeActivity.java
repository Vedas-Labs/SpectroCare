package com.vedas.spectrocare.PatientModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import io.socket.client.Socket;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBaseModels.PatientModel;
import com.vedas.spectrocare.PatientAppointmentModule.AppointmentArrayModel;
import com.vedas.spectrocare.PatientAppointmentModule.PatientAppointmentsDataController;
import com.vedas.spectrocare.PatientChat.SocketIOHelper;
import com.vedas.spectrocare.PatientChat.ViewController;
import com.vedas.spectrocare.PatientMoreModule.SettingsActivity;
import com.vedas.spectrocare.PatientNotificationModule.PatientNotificationFragment;
import com.vedas.spectrocare.PatientServerApiModel.AllergyListObject;
import com.vedas.spectrocare.PatientServerApiModel.AllergyObject;
import com.vedas.spectrocare.PatientServerApiModel.ChatRoomMessageModel;
import com.vedas.spectrocare.PatientServerApiModel.DiagnosisObject;
import com.vedas.spectrocare.PatientServerApiModel.IllnessMedicationRecords;
import com.vedas.spectrocare.PatientServerApiModel.IllnessPatientRecord;
import com.vedas.spectrocare.PatientServerApiModel.ImmunizationObject;
import com.vedas.spectrocare.PatientServerApiModel.InboxNotificationModel;
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
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.activities.ChangePasswordActivity;
import com.vedas.spectrocare.activities.HomeActivity;
import com.vedas.spectrocare.activities.LoginActivity;
import com.vedas.spectrocare.activities.MedicalPersonaSignupView;
import com.vedas.spectrocare.activities.MedicalPersonalSignupPresenter;
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


public class PatientHomeActivity extends AppCompatActivity implements MedicalPersonaSignupView {

    ArrayList patientsList;
    IllnessPatientRecord illnessPatientRecord;
    ArrayList<IllnessPatientRecord> illnessList;
    BottomNavigationView bottomNavigation;
    PatientFamilyAddServerObject responseObject;
    PatientIllnessServerResponse serverResponse;
    ArrayList<AppointmentArrayModel> modelArrayList;
    RefreshShowingDialog refreshShowingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        refreshShowingDialog = new RefreshShowingDialog(PatientHomeActivity.this);
        ApiCallDataController.getInstance().fillContent(getApplicationContext());
        PatientAppointmentController.setNull();
        PatientSurgicalRecordServerObjectDataController.getInstance().fillContent(getApplicationContext());
        PatientMedicalRecordsController.getInstance().fillContent(getApplicationContext());
        PatientAppointmentsDataController.getInstance().fillContent(getApplicationContext());

        accessInterfaceMethods();
        // accessCharRoomMsgInterfaceMethod();
        PersonalInfoController.getInstance().fillContent(getApplicationContext());
        ViewController.getInstance().fillContent(getApplicationContext());
        SocketIOHelper.getInstance().fillContent(getApplicationContext());
        SocketIOHelper.getInstance().socketConnect();
        modelArrayList = new ArrayList<>();

        //   accessLoginDeviceInterfaceMethods();

        fetchAppointmentDetails();
        fetchAllergiesApi();
        fetchPAtientImmunizationApi();
        //fetchChatRooomMessagesDetails();
        inboxMessagesFetchApi();
        fetchPatientFamilyApi();
        fetchPatientIllnessRecordApi();

        patientsList = new ArrayList();
        illnessPatientRecord = new IllnessPatientRecord();
        illnessList = new ArrayList<>();

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        Intent bundle = getIntent();
        Log.e("extrasss", "asfnj" + getIntent().getStringExtra("isFromNotify"));
        if (bundle != null) {
            Log.e("getIntent", "asfnj" + bundle.getStringExtra("isFromNotify"));
            if (bundle.getStringExtra("isFromNotify") != null) {
                if (bundle.getStringExtra("isFromNotify").equals("true"))
                    bottomNavigation.setSelectedItemId(R.id.icon_notification);
                openFragment(PatientNotificationFragment.newInstance("", ""));
            } else if (PatientMedicalRecordsController.getInstance().invoiceObject != null) {
                bottomNavigation.setSelectedItemId(R.id.navigation_card);
                openFragment(BillingAndPaymentFragment.newInstance("", ""));
            } else {
                bottomNavigation.setSelectedItemId(R.id.navigation_home);
                openFragment(HomeFragment.newInstance("", ""));
            }
        }/*else if (PatientMedicalRecordsController.getInstance().invoiceObject != null) {
            bottomNavigation.setSelectedItemId(R.id.navigation_card);
            openFragment(BillingAndPaymentFragment.newInstance("", ""));
        } else {
            bottomNavigation.setSelectedItemId(R.id.navigation_home);
            openFragment(HomeFragment.newInstance("", ""));
        }*/

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
        Log.e("ImmunizationApiService", "onResponse: " + gsonObject.toString());
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

    private void fetchChatRooomMessagesDetails() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
            jsonObject.put("userID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject body = (JsonObject) jsonParser.parse(jsonObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.getChatRooms(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken()
                , body), "fetchRooms");
    }

    /* public void accessCharRoomMsgInterfaceMethod() {
         ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
             @Override
             public void successCallBack(JSONObject jsonObject, String opetation) {
                 //refreshShowingDialog.hideRefreshDialog();
                 try {
                     if (jsonObject.getString("response").equals("3")) {
                         PatientMedicalRecordsController.getInstance().chatRoomMessageList.clear();
                         if (opetation.equals("fetchRooms")) {
                             Log.e("fetchRooms", "length" + jsonObject.toString());
                             JSONArray chatList = jsonObject.getJSONArray("chatList");
                             Log.e("chatList", "length" + chatList.length());
                             for (int i = 0; i < chatList.length(); i++) {

                                 JSONObject object = chatList.getJSONObject(i);
                                 String roomID=object.getString("roomID");
                                 Log.e("roomID", "length" + roomID);
                                 Gson gson = new Gson();
                                 JSONArray messageList = object.getJSONArray("messages");

                                 ArrayList<ChatRoomMessageModel> tempList = new ArrayList<>();

                                 for(int k=0;k<messageList.length();k++){

                                     JSONObject jsonString = messageList.getJSONObject(k);
                                     ChatRoomMessageModel chatRoomMessageModel=new ChatRoomMessageModel();
                                     chatRoomMessageModel = gson.fromJson(jsonString.toString(), ChatRoomMessageModel.class);
                                     chatRoomMessageModel.setRoomID(roomID);

                                     AppointmentArrayModel appointmentArrayModel=loadDoctorsDetails(chatRoomMessageModel.getRoomID());
                                     if(appointmentArrayModel!=null) {
                                         chatRoomMessageModel.setDoctorName("Dr. " + appointmentArrayModel.getDoctorDetails().getProfile().getUserProfile().getFirstName() + " " +
                                                 appointmentArrayModel.getDoctorDetails().getProfile().getUserProfile().getLastName());

                                         chatRoomMessageModel.setProfile(ServerApi.img_home_url + appointmentArrayModel.getDoctorDetails().getProfile().getUserProfile().getProfilePic());
                                     }
                                     tempList.add(chatRoomMessageModel);
                                     Log.e("xxxxxxxx", "length" + chatRoomMessageModel.getRoomID());
                                 }
                                 Log.e("tempList", "length" + i+tempList.size());
                                 PatientMedicalRecordsController.getInstance().chatRoomMessageList.add(i,tempList);
                             }
                             *//*for (int i = 0; i < chatList.length(); i++) {

                                JSONObject object = chatList.getJSONObject(i);
                                Gson gson = new Gson();
                                JSONArray messageList = object.getJSONArray("messages");

                                ArrayList<ChatRoomMessageModel> tempList = new ArrayList<>();

                                for(int k=0;k<messageList.length();k++){
                                    JSONObject jsonString = messageList.getJSONObject(k);
                                    ChatRoomMessageModel chatRoomMessageModel = gson.fromJson(jsonString.toString(), ChatRoomMessageModel.class);
                                    tempList.add(chatRoomMessageModel);
                                }
                                Log.e("tempList", "length" + i+tempList.size());
                                PatientMedicalRecordsController.getInstance().chatRoomMessageList.add(i,tempList);
                            }*//*
                            Log.e("all", "length" + PatientMedicalRecordsController.getInstance().chatRoomMessageList.size());

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failureCallBack(String failureMsg) {
                refreshShowingDialog.hideRefreshDialog();
            }
        });
    }*/
    private AppointmentArrayModel loadDoctorsDetails(String roomID) {
        Log.e("zzzzz", "cll" + roomID);
        if (PatientAppointmentsDataController.getInstance().allappointmentsList.size() > 0) {
            for (int i = 0; i < PatientAppointmentsDataController.getInstance().allappointmentsList.size(); i++) {
                AppointmentArrayModel model = PatientAppointmentsDataController.getInstance().allappointmentsList.get(i);
                Log.e("model", "cll" + model.getAppointmentDetails().getAppointmentID());
                if (roomID.equals(model.getAppointmentDetails().getAppointmentID())) {
                    return model;
                }
            }
        }
        return null;
    }

    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                if (curdOpetaton.equals("fetchRooms")) {
                    try {
                        if (jsonObject.getString("response").equals("3")) {
                            PatientMedicalRecordsController.getInstance().chatRoomMessageList.clear();
                            Log.e("fetchRooms", "length" + jsonObject.toString());
                            JSONArray chatList = jsonObject.getJSONArray("chatList");
                            Log.e("chatList", "length" + chatList.length());
                            for (int i = 0; i < chatList.length(); i++) {

                                JSONObject object = chatList.getJSONObject(i);
                                String roomID = object.getString("roomID");
                                Log.e("roomID", "length" + roomID);
                                Gson gson = new Gson();
                                JSONArray messageList = object.getJSONArray("messages");

                                ArrayList<ChatRoomMessageModel> tempList = new ArrayList<>();

                                for (int k = 0; k < messageList.length(); k++) {

                                    JSONObject jsonString = messageList.getJSONObject(k);
                                    ChatRoomMessageModel chatRoomMessageModel = new ChatRoomMessageModel();
                                    chatRoomMessageModel = gson.fromJson(jsonString.toString(), ChatRoomMessageModel.class);
                                    chatRoomMessageModel.setRoomID(roomID);

                                    AppointmentArrayModel appointmentArrayModel = loadDoctorsDetails(chatRoomMessageModel.getRoomID());
                                    if (appointmentArrayModel != null) {
                                        chatRoomMessageModel.setDoctorName("Dr. " + appointmentArrayModel.getDoctorDetails().getProfile().getUserProfile().getFirstName() + " " +
                                                appointmentArrayModel.getDoctorDetails().getProfile().getUserProfile().getLastName());

                                        chatRoomMessageModel.setProfile(ServerApi.img_home_url + appointmentArrayModel.getDoctorDetails().getProfile().getUserProfile().getProfilePic());
                                    }
                                    tempList.add(chatRoomMessageModel);
                                    Log.e("xxxxxxxx", "length" + chatRoomMessageModel.getRoomID());
                                }
                                Log.e("tempList", "length" + i + tempList.size());
                                PatientMedicalRecordsController.getInstance().chatRoomMessageList.add(i, tempList);
                            }
                            Log.e("all", "length" + PatientMedicalRecordsController.getInstance().chatRoomMessageList.size());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (curdOpetaton.equals("fetchAppointment")) {
                    try {
                        if (jsonObject.getString("response").equals("3")) {
                            PatientAppointmentsDataController.getInstance().allappointmentsList.clear();
                            PatientAppointmentsDataController.getInstance().upcomingAppointmentsList.clear();
                            PatientAppointmentsDataController.getInstance().pastAppointmentsList.clear();

                            JSONArray appointmentArray = jsonObject.getJSONArray("appointments");
                            Log.e("appontment", "length" + appointmentArray.length());

                            for (int l = 0; l < appointmentArray.length(); l++) {
                                Gson gson = new Gson();
                                String jsonString = jsonObject.getJSONArray("appointments").getJSONObject(l).toString();
                                AppointmentArrayModel appointmentList = gson.fromJson(jsonString, AppointmentArrayModel.class);
                                PatientAppointmentsDataController.getInstance().allappointmentsList.add(appointmentList);
                            }
                            PatientAppointmentsDataController.getInstance().setAppointmentsList(PatientAppointmentsDataController.getInstance().allappointmentsList);
                            Log.e("appointmentsize", "dd" + PatientAppointmentsDataController.getInstance().allappointmentsList.size());

                            if (PatientAppointmentsDataController.getInstance().allappointmentsList.size() > 0) {
                                fetchChatRooomMessagesDetails();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
               /* if (curdOpetaton.equals("fetchAppointment")) {
                    try {
                        if (jsonObject.getString("response").equals("3")) {
                            PatientAppointmentsDataController.getInstance().allappointmentsList.clear();
                            JSONArray appointmentArray = jsonObject.getJSONArray("appointments");
                            Log.e("appontment", "length" + jsonObject.toString());

                            for (int l = 0; l < appointmentArray.length(); l++) {
                                Gson gson = new Gson();
                                String jsonString = jsonObject.getJSONArray("appointments").getJSONObject(l).toString();
                                AppointmentArrayModel appointmentList = gson.fromJson(jsonString, AppointmentArrayModel.class);
                                PatientAppointmentsDataController.getInstance().allappointmentsList.add(appointmentList);
                            }
                            PatientAppointmentsDataController.getInstance().setAppointmentsList(PatientAppointmentsDataController.getInstance().allappointmentsList);
                            Log.e("appointmentsize", "dd" + PatientAppointmentsDataController.getInstance().allappointmentsList.size());
                            fetchChatRooomMessagesDetails();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/
                else if (curdOpetaton.equals("fetch")) {
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
                } else if (curdOpetaton.equals("fetchImmunization")) {
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
                } else if (curdOpetaton.equals("fetchFamily")) {
                    responseObject = new PatientFamilyAddServerObject();
                    responseObject = JSON.parseObject(jsonObject.toString(), PatientFamilyAddServerObject.class);
                    Gson gson = new Gson();
                    String json = gson.toJson(responseObject.getRecords());
                    Log.e("FamilyRecord", "rrrr" + json);
                    PatientFamilyDataController.getInstance().setResponseObject(responseObject);
                    Log.e("list", "familyList" + PatientFamilyDataController.getInstance().getResponseObject().getRecords().getFamliyDiseases());

                } else if (curdOpetaton.equals("fetchdiagnosis")) {
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
                } else if (curdOpetaton.equals("fetchMedications")) {
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
                } else if (curdOpetaton.equals("illnessFetch")) {
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
                } else if (curdOpetaton.equals("deleteDevice")) {
                    try {
                        Log.e("accessInterfaceMethods", "dd" + jsonObject.toString());
                        if (jsonObject.getString("response").equals("3")) {
                            Log.e("kkkkkk", "dd" + jsonObject.getString("message"));
                            SocketIOHelper.getInstance().socketDisConnected();
                            if (PatientLoginDataController.getInstance().deletePatientData(PatientLoginDataController.getInstance().currentPatientlProfile)) {
                                Log.e("ddd", "ss");
                                refreshShowingDialog.hideRefreshDialog();
                                // clearSharedPreferenceData();
                                startActivity(new Intent(getApplicationContext(), SelectUserActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                finish();
                            }
                        } else if (jsonObject.getString("response").equals("0")) {
                            Log.e("kkkkkk", "dd" + jsonObject.getString("message"));
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (curdOpetaton.equals("invoiceFetch")) {
                    try {
                        Log.e("invoiceFetch", "call" + jsonObject.toString());
                        JSONArray resultArray = jsonObject.getJSONArray("result");
                        if (resultArray.length() > 0) {
                            PatientMedicalRecordsController.getInstance().inboxNotificationList.clear();
                            for (int i = 0; i < resultArray.length(); i++) {
                                JSONObject obj = resultArray.getJSONObject(i);
                                Gson gson = new Gson();
                                InboxNotificationModel inboxNotificationModel = gson.fromJson(obj.toString(), InboxNotificationModel.class);
                                PatientMedicalRecordsController.getInstance().inboxNotificationList.add(inboxNotificationModel);
                            }
                            Log.e("inboxlist", "call" + PatientMedicalRecordsController.getInstance().inboxNotificationList.size());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void failureCallBack(String failureMsg) {
                // refreshShowingDialog.hideRefreshDialog();
                // Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
                //  Toast.makeText(getApplicationContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
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
        btnYes.setText("Logout");

        TextView txt_title = dialog.findViewById(R.id.title);
        TextView txt_msg = dialog.findViewById(R.id.msg);
        TextView txt_msg1 = dialog.findViewById(R.id.msg1);

        txt_title.setText("Logout");
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
                if (isNetworkConnected()) {
                    dialog.dismiss();
                    if (PatientLoginDataController.getInstance().deletePatientData(PatientLoginDataController.getInstance().currentPatientlProfile)) {
                        Log.e("ddd", "ss");
                        refreshShowingDialog.hideRefreshDialog();
                        startActivity(new Intent(getApplicationContext(), SelectUserActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                   /* refreshShowingDialog.showAlert();
                    deleteLoginDevice();*/
                } else {
                    dialogeforCheckavilability("Error", "Please check internet connection", "ok");
                }
            }
        });
        dialog.show();
    }
    public void clearSharedPreferenceData() {
        SharedPreferences preferences = getSharedPreferences("tokendeviceids", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
    private void deleteLoginDevice() {
        SharedPreferences sharedPreferencesTOken = getApplicationContext().getSharedPreferences("tokendeviceids", 0);
        String deviceID = sharedPreferencesTOken.getString("deviceId", null);
        String deviceToken = sharedPreferencesTOken.getString("tokenid", null);

        Log.e("deleteLoginDevice", "ID ::" + deviceID + " token :: " + deviceToken);
        JSONObject fetchObject = new JSONObject();
        try {
            fetchObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile
                    .getHospital_reg_number());
            fetchObject.put("deviceType", "mobile");
            fetchObject.put("deviceID", deviceID);
            fetchObject.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            fetchObject.put("deviceToken", deviceToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(fetchObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(
                ApiCallDataController.getInstance().serverApi.
                        deleteLoginDeviceApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "deleteDevice");
    }
    private void inboxMessagesFetchApi() {
        JSONObject fetchObject = new JSONObject();
        try {
            fetchObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile
                    .getHospital_reg_number());
            fetchObject.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(fetchObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(
                ApiCallDataController.getInstance().serverApi.
                        getInboxFetchApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "invoiceFetch");
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter = new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PatientHomeActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);
    }
}


