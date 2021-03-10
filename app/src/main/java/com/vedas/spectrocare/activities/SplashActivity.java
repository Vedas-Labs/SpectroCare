package com.vedas.spectrocare.activities;

import androidx.appcompat.app.AppCompatActivity;
import io.socket.client.Socket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientAppointmentModule.AppointmentArrayModel;
import com.vedas.spectrocare.PatientAppointmentModule.DoctorProfileModel;
import com.vedas.spectrocare.PatientAppointmentModule.PatientAppointmentsDataController;
import com.vedas.spectrocare.PatientChat.PatientChatActivity;
import com.vedas.spectrocare.PatientChat.SocketIOHelper;
import com.vedas.spectrocare.PatientDocResponseModel.MedicalPersonnelModel;
import com.vedas.spectrocare.PatientModule.PatientAppointmentsTabsActivity;
import com.vedas.spectrocare.PatientModule.PatientHomeActivity;
import com.vedas.spectrocare.PatientNotificationModule.PatientNotificationFragment;
import com.vedas.spectrocare.PatientVideoCallModule.VideoActivity;
import com.vedas.spectrocare.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    public static SharedPreferences sharedPreferencesTOken;
    public static SharedPreferences.Editor sharedPreferencesTOkenEditor;
    String strDocPic,strDocName,strDocID,strAppointmentID="";
    MedicalPersonnelModel docProfilemodel;
    Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        MedicalProfileDataController.getInstance().fillContext(getApplicationContext());
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        PatientLoginDataController.getInstance().fetchPatientlProfileData();
        ApiCallDataController.getInstance().fillContent(getApplicationContext());
        sharedPreferencesTOken = getApplicationContext().getSharedPreferences("tokendeviceids", 0);
        sharedPreferencesTOkenEditor = sharedPreferencesTOken.edit();
        String token = FirebaseInstanceId.getInstance().getToken();
        String ID = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.e("divce", "ID ::" + ID + " token :: " + token);
        Intent intentNotify = getIntent();

        if( intentNotify.getExtras()!=null){

           strAppointmentID = intentNotify.getStringExtra("appointmentID");
           Log.e("intentHas","Extraas1:: ");
           Log.e("intentHas","Extraas:: "+strAppointmentID);
           fetchAppointmentDetails();
           accessInterfaceMethod();
       }
        SplashActivity.sharedPreferencesTOkenEditor.putString("deviceId", ID);
        SplashActivity.sharedPreferencesTOkenEditor.apply();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PatientLoginDataController.getInstance().allPatientlProfile.size() > 0) {
                    Log.e("splashscreen", "ID ::" +getIntent().getStringExtra("isFromNotificaton"));
                    /*if (bundle != null) {
                        Log.e("getIntent", "asfnj" + bundle.getStringExtra("isFromNotificaton"));
                        if (bundle.getStringExtra("isFromNotificaton") != null && bundle.getStringExtra("isFromNotificaton").contains("invoice")) {

                        } else if (bundle.getStringExtra("isFromNotificaton") != null && bundle.getStringExtra("isFromNotificaton").contains("Calling")) {
                            Log.e("isFromNotificaton", "asfnj" + "getting from calling alert");

                        }else if (bundle.getStringExtra("isFromNotificaton") != null && bundle.getStringExtra("isFromNotificaton").contains("appointment")) {
                            Log.e("isFromNotificaton", "asfnj" + "getting from appoinmnet alert");

                        }
                    }*/
                    if (getIntent().getStringExtra("isFromNotificaton") != null && getIntent().getStringExtra("isFromNotificaton").equals("notification")) {
                        Intent intent = new Intent(getApplicationContext(), PatientHomeActivity.class)
                                .putExtra("isFromNotificaton", "true").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else if (getIntent().getStringExtra("isFromNotificaton") != null && getIntent().getStringExtra("isFromNotificaton").equals("Calling")) {
                        Intent intent = new Intent(getApplicationContext(), VideoActivity.class)
                                .putExtra("roomID", getIntent().getStringExtra("roomID"))
                                 .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);;
                        startActivity(intent);
                    } else if (getIntent().getStringExtra("isFromNotificaton") != null && getIntent().getStringExtra("isFromNotificaton").equals("appointment")) {
                        Intent intent = new Intent(getApplicationContext(), PatientHomeActivity.class);
                        startActivity(intent);
                    } else if (getIntent().getStringExtra("messageType")!=null){
                        if (getIntent().getStringExtra("messageType").equals("ChatMessage")){
                            mSocket = SocketIOHelper.getInstance().socket;
                            mSocket.connect();
                            joinChat(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId(),strAppointmentID);
                            Gson gson = new Gson();
                            String details = gson.toJson(docProfilemodel);
                            Log.e("detailsDoc",":: "+details);
                            Intent chatIntent = new Intent(SplashActivity.this, PatientChatActivity.class)
                                    .putExtra("docName",docProfilemodel.getProfile().getUserProfile().getFirstName()+" "+
                                            docProfilemodel.getProfile().getUserProfile().getLastName())
                                    .putExtra("isOnline",true)
                                    .putExtra("appointmentID",strAppointmentID)
                                    .putExtra("docPic",docProfilemodel.getProfile().getUserProfile().getProfilePic())
                                    .putExtra("docId",docProfilemodel.getProfile().getUserProfile().getMedical_personnel_id());
                            startActivity(chatIntent);
                        }else
                        startActivity(new Intent(SplashActivity.this,PatientAppointmentsTabsActivity.class));
                    }/*else if (getIntent().getStringExtra("messageType").equals("ChatMessage")){
                        mSocket = SocketIOHelper.getInstance().socket;
                        SocketIOHelper.getInstance().socketConnect();
                        joinChat(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId(),strAppointmentID);
                        Gson gson = new Gson();
                        String details = gson.toJson(docProfilemodel);
                        Log.e("detailsDoc",":: "+details);
                        Intent chatIntent = new Intent(SplashActivity.this, PatientChatActivity.class)
                                .putExtra("docName",docProfilemodel.getProfile().getUserProfile().getFirstName()+" "+
                                        docProfilemodel.getProfile().getUserProfile().getLastName())
                                .putExtra("isOnline",true)
                                .putExtra("appointmentID",strAppointmentID)
                                .putExtra("docPic",docProfilemodel.getProfile().getUserProfile().getProfilePic())
                                .putExtra("docId",docProfilemodel.getProfile().getUserProfile().getMedical_personnel_id());
                      startActivity(chatIntent);
                    }*/
                    else {
                        startActivity(new Intent(getApplicationContext(), PatientHomeActivity.class));
                        finish();
                    }
                } else {
                    Intent patientIntent = new Intent(SplashActivity.this, SelectUserActivity.class);
                    patientIntent.putExtra("patient", "1");
                    startActivity(patientIntent);
                    finish();

                }
            }
        }, SPLASH_TIME_OUT);
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
    public void accessInterfaceMethod() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String opetation) {

                if (opetation.equals("fetchAppointment")) {
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
                                Log.e("appontmentSingle", "length" +jsonString);
                                AppointmentArrayModel appointmentList = gson.fromJson(jsonString, AppointmentArrayModel.class);
                                PatientAppointmentsDataController.getInstance().allappointmentsList.add(appointmentList);
                            }
                            PatientAppointmentsDataController.getInstance().setAppointmentsList(PatientAppointmentsDataController.getInstance().allappointmentsList);
                            Log.e("appointmentsize", "dd" +  PatientAppointmentsDataController.getInstance().allappointmentsList.size());
                           if ( PatientAppointmentsDataController.getInstance().allappointmentsList.size()>0){
                               for (int b=0;b<PatientAppointmentsDataController.getInstance().allappointmentsList.size();b++){
                                   if ( PatientAppointmentsDataController.getInstance().allappointmentsList.get(b).getAppointmentDetails().getAppointmentID().equals(strAppointmentID)){
                                       mSocket = SocketIOHelper.getInstance().socket;
                                       Log.e("remoteMessagecaaaa", "asfnj3 " + strAppointmentID);
                                       docProfilemodel =PatientAppointmentsDataController.getInstance().allappointmentsList.get(b).getDoctorDetails();
                                     //  Log.e("Docdataaa",":: "+gson.toJson(docProfilemodel));
                                   }
                               }
                           }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void failureCallBack(String failureMsg) {
            }
        });
    }

    private void joinChat(String userID, String roomid) {
        JsonObject feedObj = new JsonObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("roomID", roomid);
            jsonObject.put("userID", userID);
            JsonParser jsonParser = new JsonParser();
            feedObj = (JsonObject) jsonParser.parse(jsonObject.toString());
            Log.e("ChatJSON:", " " + feedObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("socket11", "message" + mSocket.id());
        mSocket.emit("subscribe", jsonObject);

    }
}
