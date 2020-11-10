package com.vedas.spectrocare.Controllers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.DataBase.FamilyHistoryDataController;
import com.vedas.spectrocare.DataBase.FamilyTrackInfoDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBaseModels.FamilyHistoryModel;
import com.vedas.spectrocare.DataBaseModels.FamilyTrackInfoModel;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.ServerApiModel.FamilyRecordServerObject;
import com.vedas.spectrocare.ServerApiModel.RetrofitInstance;
import com.vedas.spectrocare.ServerApiModel.TrackingServerObject;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FamilyRecordServerDataController {

    public static FamilyRecordServerDataController myObj;
    Context context;

    public static FamilyRecordServerDataController getInstance() {
        if (myObj == null) {
            myObj = new FamilyRecordServerDataController();
        }
        return myObj;
    }

    public void fillContent(Context context1) {
        context = context1;
    }

    public void processfetchFamilyAddData(FamilyRecordServerObject userIdentifier, ArrayList<TrackingServerObject> trackArray) {

        FamilyHistoryModel  historyModel = new FamilyHistoryModel();
        historyModel.setPatientId(userIdentifier.getPatientID());
        historyModel.setMedicalPersonId(userIdentifier.getMedical_personnel_id());
        historyModel.setMedicalRecordId(userIdentifier.getMedical_record_id());
        historyModel.setRelation(userIdentifier.getRelationship());
        historyModel.setMedicalCondition(userIdentifier.getHealthCondition());
        historyModel.setDieseaseName(userIdentifier.getDieseaseName());
        historyModel.setAge(userIdentifier.getAge());
        historyModel.setFamilyRecordId(userIdentifier.getFamily_history_record_id());
        historyModel.setDiscription(userIdentifier.getMoreInfo());
        historyModel.setPatientlProfileModel(PatientProfileDataController.getInstance().currentPatientlProfile);

        if (FamilyHistoryDataController.getInstance().insertFamilyData(historyModel)) {
            FamilyHistoryDataController.getInstance().fetchFamilyData(PatientProfileDataController.getInstance().currentPatientlProfile);
        }

        if (trackArray.size() > 0) {
            for (int index = 0; index < trackArray.size(); index++) {
                processFamilytrackData(trackArray.get(index),historyModel);
            }
        }
    }

    public void processAddFamilyAddData(FamilyRecordServerObject userIdentifier) {

        FamilyHistoryModel  historyModel = new FamilyHistoryModel();
        historyModel.setPatientId(userIdentifier.getPatientID());
        historyModel.setMedicalPersonId(userIdentifier.getMedical_personnel_id());
        historyModel.setMedicalRecordId(userIdentifier.getMedical_record_id());
        historyModel.setRelation(userIdentifier.getRelationship());
        historyModel.setMedicalCondition(userIdentifier.getHealthCondition());
        historyModel.setAge(userIdentifier.getAge());
        historyModel.setDieseaseName(userIdentifier.getDieseaseName());
        historyModel.setFamilyRecordId(userIdentifier.getFamily_history_record_id());
        historyModel.setDiscription(userIdentifier.getMoreInfo());
        historyModel.setPatientlProfileModel(PatientProfileDataController.getInstance().currentPatientlProfile);

        if (FamilyHistoryDataController.getInstance().insertFamilyData(historyModel)) {
            FamilyHistoryDataController.getInstance().fetchFamilyData(PatientProfileDataController.getInstance().currentPatientlProfile);
        }

        FamilyTrackInfoModel trackInfoModel=new FamilyTrackInfoModel();
        trackInfoModel.setFamilyHistoryModel(historyModel);
        trackInfoModel.setFamilyRecordId( historyModel.getFamilyRecordId());
        trackInfoModel.setByWhom(PatientProfileDataController.getInstance().currentPatientlProfile.getFirstName()+" "+ PatientProfileDataController.getInstance().currentPatientlProfile.getLastName());
        trackInfoModel.setByWhomId(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
        trackInfoModel.setDate(String.valueOf(System.currentTimeMillis() / 1000L));
        Log.e("trackingServerObjects", "call" + trackInfoModel.getByWhomId());

        if(FamilyTrackInfoDataController.getInstance().insertTrackData(trackInfoModel)){
            //FamilyTrackInfoDataController.getInstance().fetchTrackData(historyModel);
        }


    }
    public void processFamilyupdateData(FamilyRecordServerObject userIdentifier) {
       FamilyHistoryModel  historyModel = new FamilyHistoryModel();
        historyModel.setPatientId(userIdentifier.getPatientID());
        historyModel.setMedicalPersonId(userIdentifier.getMedical_personnel_id());
        historyModel.setMedicalRecordId(userIdentifier.getMedical_record_id());
        historyModel.setRelation(userIdentifier.getRelationship());
        historyModel.setMedicalCondition(userIdentifier.getHealthCondition());
        historyModel.setAge(userIdentifier.getAge());
        historyModel.setFamilyRecordId(userIdentifier.getFamily_history_record_id());
        historyModel.setDiscription(userIdentifier.getMoreInfo());
        historyModel.setPatientlProfileModel(PatientProfileDataController.getInstance().currentPatientlProfile);

        if (FamilyHistoryDataController.getInstance().updateFamilyData(historyModel)) {
            FamilyHistoryDataController.getInstance().fetchFamilyData(PatientProfileDataController.getInstance().currentPatientlProfile);
        }

        ArrayList<FamilyTrackInfoModel> trackInfoModels= FamilyTrackInfoDataController.getInstance().fetchFamilyTrackingBasedOnFamilyRecorsID(FamilyHistoryDataController.getInstance().currentFamilyHistoryModel);
        if(trackInfoModels.size()>0){
            FamilyTrackInfoModel trackInfoModel=trackInfoModels.get(trackInfoModels.size()-1);
            trackInfoModel.setDate(String.valueOf(System.currentTimeMillis() / 1000L));
            if(FamilyTrackInfoDataController.getInstance().updateTrackData(trackInfoModel)){

            }
        }


    }
    public void processFamilytrackData(TrackingServerObject serverObject,FamilyHistoryModel historyModel) {
        FamilyTrackInfoModel trackInfoModel=new FamilyTrackInfoModel();
        trackInfoModel.setFamilyHistoryModel(historyModel);
        trackInfoModel.setFamilyRecordId( historyModel.getFamilyRecordId());
        trackInfoModel.setByWhom(serverObject.getByWhom());
        trackInfoModel.setByWhomId(serverObject.getByWhomID());
        Long l=Long.parseLong(serverObject.getDate())/1000;
        trackInfoModel.setDate(String.valueOf(l));
        Log.e("trackingServerObjects", "call" + trackInfoModel.getByWhomId());

        if(FamilyTrackInfoDataController.getInstance().insertTrackData(trackInfoModel)){
            //FamilyTrackInfoDataController.getInstance().fetchTrackData(historyModel);
        }

    }
    public void newFamilyFetchApi(){
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        JSONObject params = new JSONObject();
        try {
            params.put("hospital_reg_num", currentMedical.getHospital_reg_number());
            params.put("byWhom","medical personnel");
            params.put("byWhomID", currentMedical.getMedical_person_id());
            params.put("patientID", PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("medical_record_id", PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.fetchFamilyHistory1(currentMedical.getAccessToken(), gsonObject), "fetch");

    }

    public void familyFetchApiCall() {
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        JSONObject params = new JSONObject();
        try {
            params.put("hospital_reg_num", currentMedical.getHospital_reg_number());
            params.put("medical_personnel_id", currentMedical.getMedical_person_id());
            params.put("medical_record_id", PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
            params.put("patientID", PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.fetchFamilyHistory1(currentMedical.getAccessToken(), gsonObject), "fetch");
    }
 /*   public void addFamilyHistoryApi(final FamilyRecordServerObject serverObject) {
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();

        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        serverObject.setHospital_reg_num(currentMedical.getHospital_reg_number());
        serverObject.setMedical_personnel_id(currentMedical.getMedical_person_id());
        serverObject.setMedical_record_id(PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
        serverObject.setPatientID(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());

        Log.e("alldata", "call" + serverObject.getMedical_personnel_id() + "aaa" + serverObject.getHospital_reg_num() +
                serverObject.getMedical_record_id() + "aa" + serverObject.getPatientID() + "zzz" + currentMedical.getAccessToken());

        Log.e("alldata", "call" + serverObject.getHealthCondition() + "aaa" + serverObject.getAge() +
                serverObject.getMoreInfo() + "aa" + serverObject.getRelationship());

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ServerApi familyApi = retrofit.create(ServerApi.class);

        Call<FamilyRecordServerObject> familyCall = familyApi.addFamilyHistory(currentMedical.getAccessToken(),
                serverObject);
        familyCall.enqueue(new Callback<FamilyRecordServerObject>() {
            @Override
            public void onResponse(Call<FamilyRecordServerObject> call, Response<FamilyRecordServerObject> response) {
                if (response.body() != null) {
                    String responseCode = response.body().getResponse();
                    if (responseCode == "3") {
                        FamilyHistoryModel historyModel = new FamilyHistoryModel();
                        historyModel.setPatientId(serverObject.getPatientID());
                        historyModel.setMedicalPersonId(serverObject.getMedical_personnel_id());
                        historyModel.setMedicalRecordId(serverObject.getMedical_record_id());
                        historyModel.setPatientId(serverObject.getPatientID());

                        historyModel.setRelation(serverObject.getRelationship());
                        historyModel.setMedicalCondition(serverObject.getHealthCondition());
                        historyModel.setAge(serverObject.getAge());
                        historyModel.setDiscription(serverObject.getMoreInfo());
                        historyModel.setFamilyRecordId(response.body().getFamily_history_record_id());
                        Log.e("ressss", "sssss" + response.body().getFamily_history_record_id());

                        historyModel.setPatientlProfileModel(PatientProfileDataController.getInstance().currentPatientlProfile);

                        if (FamilyHistoryDataController.getInstance().insertFamilyData(historyModel)) {
                            FamilyHistoryDataController.getInstance().fetchFamilyData(PatientProfileDataController.getInstance().currentPatientlProfile);
                        }
                        EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("addFamilyData"));
                    }
                }
            }

            @Override
            public void onFailure(Call<FamilyRecordServerObject> call, Throwable t) {
                Log.e("ressss", "sssss");

            }
        });
    }

    public void fetchFamilyHistoryApi() {
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        FamilyRecordServerObject serverObject = new FamilyRecordServerObject();

        serverObject.setHospital_reg_num(currentMedical.getHospital_reg_number());
        serverObject.setMedical_personnel_id(currentMedical.getMedical_person_id());
        serverObject.setMedical_record_id(PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
        serverObject.setPatientID(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());

        Log.e("alldata", "call" + serverObject.getMedical_personnel_id() + "aaa" + serverObject.getHospital_reg_num() +
                serverObject.getMedical_record_id() + "aa" + serverObject.getPatientID() + "zzz" + currentMedical.getAccessToken());

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ServerApi familyApi = retrofit.create(ServerApi.class);

        Call<FamilyRecordServerObject> familyCall = familyApi.fetchFamilyHistory(currentMedical.getAccessToken(),
                serverObject);
        familyCall.enqueue(new Callback<FamilyRecordServerObject>() {
            @Override
            public void onResponse(Call<FamilyRecordServerObject> call, Response<FamilyRecordServerObject> response) {
                if (response.body() != null) {
                    String responseCode = response.body().getResponse();
                    if (responseCode == "3") {
                        FamilyHistoryDataController.getInstance().deleteFamilyData(PatientProfileDataController.getInstance().allPatientlProfile);
                        for (int i = 0; i < response.body().getFamily_history_records().size(); i++) {
                            FamilyRecordServerObject familyObject = response.body().getFamily_history_records().get(i);
                            FamilyHistoryModel historyModel = new FamilyHistoryModel();
                            historyModel.setPatientId(familyObject.getPatientID());
                            historyModel.setMedicalPersonId(familyObject.getMedical_personnel_id());
                            historyModel.setMedicalRecordId(familyObject.getMedical_record_id());
                            historyModel.setPatientId(familyObject.getPatientID());

                            historyModel.setRelation(familyObject.getRelationship());
                            historyModel.setMedicalCondition(familyObject.getHealthCondition());
                            historyModel.setAge(familyObject.getAge());
                            historyModel.setFamilyRecordId(familyObject.getFamily_history_record_id());
                            historyModel.setDiscription(familyObject.getMoreInfo());
                            historyModel.setPatientlProfileModel(PatientProfileDataController.getInstance().currentPatientlProfile);

                            if (FamilyHistoryDataController.getInstance().insertFamilyData(historyModel)) {
                                FamilyHistoryDataController.getInstance().fetchFamilyData(PatientProfileDataController.getInstance().currentPatientlProfile);
                            }
                        }
                        EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("fetchFamilyData"));
                    }
                }
            }

            @Override
            public void onFailure(Call<FamilyRecordServerObject> call, Throwable t) {
                Log.e("ressss", "sssss"+t.getMessage());
            }
        });
    }

    public void deletePhysicalExamServerApi() {

        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;

        FamilyRecordServerObject serverObject = new FamilyRecordServerObject();

        serverObject.setFamily_history_record_id(FamilyHistoryDataController.getInstance().currentFamilyHistoryModel.getFamilyRecordId());
        serverObject.setHospital_reg_num(currentMedical.getHospital_reg_number());
        serverObject.setMedical_personnel_id(currentMedical.getMedical_person_id());
        serverObject.setMedical_record_id(PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
        serverObject.setPatientID(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());

        Log.e("alldata", "call" + serverObject.getHospital_reg_num() + "zzz" + serverObject.getFamily_history_record_id() + "zzzz" +
                serverObject.getMedical_personnel_id() + "zzz" + serverObject.getMedical_record_id() + "zzzz" + serverObject.getPatientID());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerApi serverApi = retrofit.create(ServerApi.class);
        Call<FamilyRecordServerObject> callable = serverApi.deleteFamilyHistory(currentMedical.getAccessToken(), serverObject);

        callable.enqueue(new Callback<FamilyRecordServerObject>() {
            @Override
            public void onResponse(Call<FamilyRecordServerObject> call, Response<FamilyRecordServerObject> response) {
                if (response.body() != null) {
                    String statusCode = response.body().getResponse();
                    String messageStatus = response.body().getMessage();
                    Log.e("codefor3", "call" + statusCode);
                    if (statusCode.equals("3")) {
                        FamilyHistoryDataController.getInstance().deleteFamilyHistoryModelData(PatientProfileDataController.getInstance().currentPatientlProfile, FamilyHistoryDataController.getInstance().currentFamilyHistoryModel);
                        EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("deleteFamilyData"));
                    } else if (statusCode.equals("0")) {
                        Toast.makeText(context, messageStatus, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, messageStatus, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<FamilyRecordServerObject> call, Throwable t) {
            }
        });
    }
*/
}
