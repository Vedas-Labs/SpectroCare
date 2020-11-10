package com.vedas.spectrocare.Controllers;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.DataBase.IllnessDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBase.ScreeningRecordDataController;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.DataBaseModels.MedicalScreeningRecordModel;
import com.vedas.spectrocare.DataBaseModels.ScreeningRecordModel;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.ServerApiModel.IllnessServerObject;
import com.vedas.spectrocare.ServerApiModel.ScreeningServerObject;
import com.vedas.spectrocare.ServerApiModel.TrackingServerObject;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScreeningServerObjectDataController {
    public static ScreeningServerObjectDataController myObj;
    Context context;
    public JSONObject params;
    public static ScreeningServerObjectDataController getInstance() {
        if (myObj == null) {
            myObj = new ScreeningServerObjectDataController();
        }
        return myObj;
    }
     public void fillContent(Context context1) {
        context = context1;
    }

    public void fetchScreeningRecord() {
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        JSONObject params = new JSONObject();
        try {
            params.put("hospital_reg_num",currentMedical.getHospital_reg_number());
            params.put("byWhom", "Patient");
            params.put("byWhomID", PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("patientID", PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("medical_record_id", PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.fetchScreenRecord(currentMedical.getAccessToken(), gsonObject), "fetch");
    }
    public void deleteScreeningRecord(ScreeningRecordModel screeningRecordModel) {
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        JSONObject params = new JSONObject();
        try {
            params.put("hospital_reg_num",currentMedical.getHospital_reg_number());
            params.put("byWhomID", PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("byWhom", "Patient");
            params.put("patientID", PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("medical_record_id", PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
            params.put("illnessScreeningID", screeningRecordModel.getScreeningID());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.deleteScreenRecord(
                currentMedical.getAccessToken(), gsonObject), "delete");
    }
    public void addScreeningCallForAttchmet(final File file,String fielnmae,String discription) {
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        final ScreeningServerObject serverObject=new ScreeningServerObject();

        serverObject.setRecordName(fielnmae);
        serverObject.setByWhomType("Patient");
        serverObject.setByWhomName(PatientProfileDataController.getInstance().currentPatientlProfile.getFirstName());
        serverObject.setByWhomID(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
        serverObject.setRecordMoreDetails(discription);
        serverObject.setMedical_record_id(PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
        serverObject.setPatientID(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());

        Log.e("dssss","caa"+fielnmae+serverObject.getByWhomID()+serverObject.getByWhomName()+serverObject.getByWhomType());
        Log.e("dssss","caa"+serverObject.getRecordMoreDetails());

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ServerApi s = retrofit.create(ServerApi.class);

        MultipartBody.Part screeningRecord;

        RequestBody image = RequestBody.create(MediaType.parse("image/*"), file);
        screeningRecord = MultipartBody.Part.createFormData("screeningRecord", file.getName(), image);

        RequestBody medicalPersonId = RequestBody.create(MediaType.parse("text/plain"),serverObject.getMedical_record_id());
        RequestBody patienid = RequestBody.create(MediaType.parse("text/plain"),serverObject.getPatientID());
        RequestBody moreDetails = RequestBody.create(MediaType.parse("text/plain"),serverObject.getRecordMoreDetails());
        RequestBody recordName = RequestBody.create(MediaType.parse("text/plain"),serverObject.getRecordName());
        RequestBody hospitalRegNo = RequestBody.create(MediaType.parse("text/plain"),currentMedical.getHospital_reg_number());
      //  RequestBody whomname = RequestBody.create(MediaType.parse("text/plain"),serverObject.getByWhomName());
        RequestBody whomType = RequestBody.create(MediaType.parse("text/plain"),serverObject.getByWhomType());
        RequestBody whomid = RequestBody.create(MediaType.parse("text/plain"),serverObject.getByWhomID());

/*
        Call<ScreeningServerObject> call = s.addScreeninRecord(
                currentMedical.getAccessToken(),patienid,moreDetails,
                recordName,whomname,whomType,
                whomid,medicalPersonId,screeningRecord);
*/
        Call<ScreeningServerObject> call = s.addScreeninRecord(currentMedical.getAccessToken(),patienid,screeningRecord,moreDetails,
                recordName,whomType,
                whomid,medicalPersonId,hospitalRegNo);


        call.enqueue(new Callback<ScreeningServerObject>() {
            @Override
            public void onResponse(Call<ScreeningServerObject> call, Response<ScreeningServerObject> response) {
                if (response.body() != null) {
                    String message = response.body().getMessage();
                    String respons = response.body().getResponse();
                    Log.e("checkMessage", "" + message);

                    if (respons.equals("3")) {
                        String screeningID = response.body().getIllnessScreeningID();
                        String filePath = response.body().getFilePath();
                        Log.e("screeningIDs", "call" + screeningID+filePath);

                        ScreeningRecordModel historyModel = new ScreeningRecordModel();
                        historyModel.setPatientId(serverObject.getPatientID());
                        //historyModel.setMedicalPersonId(userIdentifier.get());
                        historyModel.setMedicalRecordId(serverObject.getMedical_record_id());
                        historyModel.setPatientlProfileModel(PatientProfileDataController.getInstance().currentPatientlProfile);
                        historyModel.setScreeningID(screeningID);
                        historyModel.setMoreInfo(serverObject.getRecordMoreDetails());
                        historyModel.setAttachment(filePath);
                        if (ScreeningRecordDataController.getInstance().insertScreeningData(historyModel)) {
                            ScreeningRecordDataController.getInstance().fetchScreeningData(PatientProfileDataController.getInstance().currentPatientlProfile);
                        }
                        EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("addScreening"));
                    }
                }
            }
            @Override
            public void onFailure(Call<ScreeningServerObject> call, Throwable t) {

            }
        });
    }
    public void processfetchScreenAddData(ScreeningServerObject userIdentifier, ArrayList<TrackingServerObject> trackArray) {

        ScreeningRecordModel historyModel = new ScreeningRecordModel();
        historyModel.setPatientId(userIdentifier.getPatientID());
        //historyModel.setMedicalPersonId(userIdentifier.get());
        historyModel.setMedicalRecordId(userIdentifier.getMedical_record_id());
        historyModel.setPatientlProfileModel(PatientProfileDataController.getInstance().currentPatientlProfile);
        historyModel.setScreeningID(userIdentifier.getIllnessScreeningID());
        historyModel.setMoreInfo(userIdentifier.getRecordMoreDetails());
        historyModel.setAttachment(userIdentifier.getRecordFilePath());


        if (ScreeningRecordDataController.getInstance().insertScreeningData(historyModel)) {
            ScreeningRecordDataController.getInstance().fetchScreeningData(PatientProfileDataController.getInstance().currentPatientlProfile);
        }
        /*if (trackArray.size() > 0) {
            for (int index = 0; index < trackArray.size(); index++) {
                processFamilytrackData(trackArray.get(index),historyModel);
            }
        }*/
    }

    //MedicalScreeningRecord Api's

/*
    public void addMedicalScreeningRecord(final File file,String fileName,String description){

        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        final ScreeningServerObject serverObject=new ScreeningServerObject();
        Log.e("ifIllness",""+ IllnessDataController.getInstance().currentIllnessRecordModel.getIllnessRecordId());

        serverObject.setRecordName(fileName);
        serverObject.setByWhomType("Patient");
        serverObject.setByWhomName(PatientProfileDataController.getInstance().currentPatientlProfile.getFirstName());
        serverObject.setByWhomID(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
        serverObject.setIllnessID( IllnessDataController.getInstance().currentIllnessRecordModel.getIllnessRecordId());
        serverObject.setRecordMoreDetails(description);
        serverObject.setMedical_record_id(PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
        serverObject.setPatientID(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ServerApi s = retrofit.create(ServerApi.class);
        MultipartBody.Part medicalScreeningRecord;

        RequestBody image = RequestBody.create(MediaType.parse("image/*"), file);
        medicalScreeningRecord = MultipartBody.Part.createFormData("screeningRecord", file.getName(), image);

        RequestBody medicalPersonId = RequestBody.create(MediaType.parse("text/plain"),serverObject.getMedical_record_id());
        RequestBody illnessid = RequestBody.create(MediaType.parse("text/plain"),serverObject.getIllnessID());
        RequestBody patienid = RequestBody.create(MediaType.parse("text/plain"),serverObject.getPatientID());
        RequestBody moreDetails = RequestBody.create(MediaType.parse("text/plain"),serverObject.getRecordMoreDetails());
        RequestBody recordName = RequestBody.create(MediaType.parse("text/plain"),serverObject.getRecordName());
        RequestBody whomname = RequestBody.create(MediaType.parse("text/plain"),serverObject.getByWhomName());
        RequestBody whomType = RequestBody.create(MediaType.parse("text/plain"),serverObject.getByWhomType());
        RequestBody whomid = RequestBody.create(MediaType.parse("text/plain"),serverObject.getByWhomID());

        Call<ScreeningServerObject> call = s.addIllnessScreeninRecord( currentMedical.getAccessToken(),illnessid,patienid,moreDetails,
                recordName,whomname,whomType,
                whomid,medicalPersonId,medicalScreeningRecord);

        call.enqueue(new Callback<ScreeningServerObject>() {
            @Override
            public void onResponse(Call<ScreeningServerObject> call, Response<ScreeningServerObject> response) {
                if (response.body() != null) {
                    String message = response.body().getMessage();
                    String respons = response.body().getResponse();
                    String screeningID = response.body().getIllnessScreeningID();
                    String filePath = response.body().getFilePath();
                    Log.e("checkMessage", "" + message);
                    MedicalScreeningRecordModel historyModel = new MedicalScreeningRecordModel();
                    //historyModel.setPatientId(serverObject.getPatientID());
                    //historyModel.setMedicalPersonId(userIdentifier.get());
                    historyModel.setPatientID(serverObject.getPatientID());
                    historyModel.setMedicalRecordId(serverObject.getMedical_record_id());
                    historyModel.setPatientlProfileModel(PatientProfileDataController.getInstance().currentPatientlProfile);
                    historyModel.setScreeningID(screeningID);
                    historyModel.setMoreInfo(serverObject.getRecordMoreDetails());
                    historyModel.setAttachment(filePath);
                    if (ScreeningRecordDataController.getInstance().insertMedicalScreeningData(historyModel)) {
                        ScreeningRecordDataController.getInstance().fetchScreeningData(PatientProfileDataController.getInstance().currentPatientlProfile);
                    }
                    EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("addScreening"));
                }
            }

            @Override
            public void onFailure(Call<ScreeningServerObject> call, Throwable t) {

            }
        });

    }
*/
 public void  fetchMedicalScreeningRecord(){

     MedicalProfileDataController.getInstance().fetchMedicalProfileData();
     MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
     JSONObject medicalScreeningObject = new JSONObject();
     try {
         medicalScreeningObject.put("byWhomID", PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId()); medicalScreeningObject.put("byWhomType", "Patient");
         medicalScreeningObject.put("byWhomType", "Patient");
         medicalScreeningObject.put("medical_record_id", PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
         medicalScreeningObject.put("patientID", PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
         medicalScreeningObject.put("illnessID",IllnessDataController.getInstance().currentIllnessRecordModel.getIllnessRecordId());

     } catch (JSONException e) {
         e.printStackTrace();
     }
     JsonParser jsonParser = new JsonParser();
     JsonObject gsonScreeningObject = (JsonObject) jsonParser.parse(medicalScreeningObject.toString());
     Log.e("ServiceResponse", "onResponse: " + gsonScreeningObject.toString());
     ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.fetchMedicalScreenRecord(currentMedical.getAccessToken(), gsonScreeningObject), "fetch");

 }

}
