package com.vedas.spectrocare.Controllers;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.DataBase.IllnessDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.MedicalScreeningRecordDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBase.ScreeningRecordDataController;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.DataBaseModels.MedicalScreeningRecordModel;
import com.vedas.spectrocare.DataBaseModels.ScreeningRecordModel;
import com.vedas.spectrocare.ServerApi;
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


public class MedicalScreeningServerObjectDataController {
    public static MedicalScreeningServerObjectDataController myObj;
    Context context;
    public JSONObject params;
    public static MedicalScreeningServerObjectDataController getInstance() {
        if (myObj == null) {
            myObj = new MedicalScreeningServerObjectDataController();
        }
        return myObj;
    }
    public void fillContent(Context context1) {
        context = context1;
    }

    public void deleteMedicalScreeningRecord(MedicalScreeningRecordModel screeningRecordModel) {
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        JSONObject params = new JSONObject();
        try {
            params.put("hospital_reg_num",currentMedical.getHospital_reg_number());
            params.put("byWhom", "medical personnel");
            params.put("byWhomID", currentMedical.getMedical_person_id());
            params.put("patientID", IllnessDataController.getInstance().currentIllnessRecordModel.getPatientId());
            params.put("medical_record_id", IllnessDataController.getInstance().currentIllnessRecordModel.getMedicalRecordId());
            params.put("illnessScreeningID", screeningRecordModel.getScreeningID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.deleteMedicalScreenRecord(currentMedical.getAccessToken(), gsonObject), "delete");
    }

    //MedicalScreeningRecord Api's

    public void addMedicalScreeningRecord(final File file,String fileName,String description){

        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        final ScreeningServerObject serverObject=new ScreeningServerObject();
        Log.e("ifIllness",""+ IllnessDataController.getInstance().currentIllnessRecordModel.getIllnessRecordId());
        Log.e("patientId",""+IllnessDataController.getInstance().currentIllnessRecordModel.getPatientId());
        Log.e("whoemId",""+IllnessDataController.getInstance().currentIllnessRecordModel.getPatientId());
        Log.e("whoemName",""+PatientProfileDataController.getInstance().currentPatientlProfile.getFirstName());
        Log.e("medicalRecordID",""+IllnessDataController.getInstance().currentIllnessRecordModel.getMedicalRecordId());

        serverObject.setRecordName(fileName);
        serverObject.setByWhomType("medical personnel");
        serverObject.setByWhomName(PatientProfileDataController.getInstance().currentPatientlProfile.getFirstName());
        serverObject.setByWhomID(IllnessDataController.getInstance().currentIllnessRecordModel.getMedicalPersonId());
        serverObject.setIllnessID( IllnessDataController.getInstance().currentIllnessRecordModel.getIllnessRecordId());
        serverObject.setRecordMoreDetails(description);
        serverObject.setMedical_record_id(IllnessDataController.getInstance().currentIllnessRecordModel.getMedicalRecordId());
        serverObject.setPatientID(IllnessDataController.getInstance().currentIllnessRecordModel.getPatientId());

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ServerApi s = retrofit.create(ServerApi.class);
        MultipartBody.Part medicalScreeningRecord;

        RequestBody image = RequestBody.create(MediaType.parse("image/*"), file);
        medicalScreeningRecord = MultipartBody.Part.createFormData("screeningRecord", file.getName(), image);

        RequestBody patienid = RequestBody.create(MediaType.parse("text/plain"),serverObject.getPatientID());
        RequestBody medicalRecordId = RequestBody.create(MediaType.parse("text/plain"),serverObject.getMedical_record_id());
        RequestBody illnessid = RequestBody.create(MediaType.parse("text/plain"),serverObject.getIllnessID());
        RequestBody moreDetails = RequestBody.create(MediaType.parse("text/plain"),serverObject.getRecordMoreDetails());
        RequestBody recordName = RequestBody.create(MediaType.parse("text/plain"),serverObject.getRecordName());
        RequestBody whomname = RequestBody.create(MediaType.parse("text/plain"),serverObject.getByWhomName());
        RequestBody whomType = RequestBody.create(MediaType.parse("text/plain"),"medical personnel");
        RequestBody whomid = RequestBody.create(MediaType.parse("text/plain"),currentMedical.getMedical_person_id());
        RequestBody hospitalRegNum = RequestBody.create(MediaType.parse("text/plain"),currentMedical.getHospital_reg_number());

/*
        Call<ScreeningServerObject> call = s.addIllnessScreeninRecord( currentMedical.getAccessToken(),illnessid,patienid,moreDetails,
                recordName,whomname,whomType,
                whomid,medicalPersonId,medicalScreeningRecord);
*/
        Call<ScreeningServerObject> call = s.addIllnessScreeninRecord( currentMedical.getAccessToken(),patienid,medicalScreeningRecord,
                moreDetails,recordName
                ,whomType,
                whomid,medicalRecordId,hospitalRegNum,illnessid);


        call.enqueue(new Callback<ScreeningServerObject>() {
            @Override
            public void onResponse(Call<ScreeningServerObject> call, Response<ScreeningServerObject> response) {
                Log.e("response",""+response.code());
                if (response.body() != null) {
                    String message = response.body().getMessage();
                    String respons = response.body().getResponse();
                    String screeningID = response.body().getIllnessScreeningID();
                    String filePath = response.body().getFilePath();
                    Log.e("checkMessage", "" + message);
                    MedicalScreeningRecordModel historyModel = new MedicalScreeningRecordModel();
                    historyModel.setPatientID(serverObject.getPatientID());
                    historyModel.setMedicalRecordId(serverObject.getMedical_record_id());
                    historyModel.setIllnessRecordModel(IllnessDataController.getInstance().currentIllnessRecordModel);
                    historyModel.setScreeningID(screeningID);
                    historyModel.setMoreInfo(serverObject.getRecordMoreDetails());
                    historyModel.setAttachment(filePath);
                    if (MedicalScreeningRecordDataController.getInstance().insertMedicalScreeningData(historyModel)) {
                        MedicalScreeningRecordDataController.getInstance().fetchMedicalScreeningData(IllnessDataController.getInstance().currentIllnessRecordModel);
                    }
                    EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("addIllnessScreening"));
                }
            }

            @Override
            public void onFailure(Call<ScreeningServerObject> call, Throwable t) {
                Log.e("checkResponse", "" + t);

            }
        });

    }
    public void  fetchMedicalScreeningRecord(){

        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        JSONObject medicalScreeningObject = new JSONObject();
        try {
            medicalScreeningObject.put("hospital_reg_num",currentMedical.getHospital_reg_number());
            medicalScreeningObject.put("byWhom", "medical personnel");
            medicalScreeningObject.put("byWhomID",currentMedical.getMedical_person_id());
            medicalScreeningObject.put("patientID", IllnessDataController.getInstance().currentIllnessRecordModel.getPatientId());
            medicalScreeningObject.put("medical_record_id", IllnessDataController.getInstance().currentIllnessRecordModel.getMedicalRecordId());
            /*medicalScreeningObject.put("byWhomType", "medical personnel");
            medicalScreeningObject.put("byWhomID", IllnessDataController.getInstance().currentIllnessRecordModel.getMedicalPersonId());
            */medicalScreeningObject.put("illnessID",IllnessDataController.getInstance().currentIllnessRecordModel.getIllnessRecordId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("tttt",""+  currentMedical.getAccessToken());
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonScreeningObject = (JsonObject) jsonParser.parse(medicalScreeningObject.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonScreeningObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.fetchMedicalScreenRecord(
                currentMedical.getAccessToken(), gsonScreeningObject), "fetch");

    }

    public void processfetchMedicalScreenAddData(ScreeningServerObject userIdentifier, ArrayList<TrackingServerObject> trackArray) {

        MedicalScreeningRecordModel historyModel = new MedicalScreeningRecordModel();
        historyModel.setPatientID(userIdentifier.getPatientID());
        //historyModel.setMedicalPersonId(userIdentifier.get());
        historyModel.setMedicalRecordId(userIdentifier.getMedical_record_id());
        historyModel.setIllnessRecordModel(IllnessDataController.getInstance().currentIllnessRecordModel);
        historyModel.setScreeningID(userIdentifier.getIllnessScreeningID());
        historyModel.setMoreInfo(userIdentifier.getRecordMoreDetails());
        historyModel.setAttachment(userIdentifier.getRecordFilePath());


        if (MedicalScreeningRecordDataController.getInstance().insertMedicalScreeningData(historyModel)) {
            MedicalScreeningRecordDataController.getInstance().fetchMedicalScreeningData(IllnessDataController.getInstance().currentIllnessRecordModel);
        }
        /*if (trackArray.size() > 0) {
            for (int index = 0; index < trackArray.size(); index++) {
                processFamilytrackData(trackArray.get(index),historyModel);
            }
        }*/
    }



}
