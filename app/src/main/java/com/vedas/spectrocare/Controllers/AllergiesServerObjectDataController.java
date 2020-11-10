package com.vedas.spectrocare.Controllers;

import android.content.Context;
import android.util.Log;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.DataBase.AllergyDataController;
import com.vedas.spectrocare.DataBase.AllergyTrackInfoDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBaseModels.AllergieModel;
import com.vedas.spectrocare.DataBaseModels.AllergyTrackInfoModel;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApiModel.AllergyServerObject;
import com.vedas.spectrocare.ServerApiModel.TrackingServerObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class AllergiesServerObjectDataController {

    public static AllergiesServerObjectDataController myObj;
    Context context;

    public static AllergiesServerObjectDataController getInstance() {
        if (myObj == null) {
            myObj = new AllergiesServerObjectDataController();
        }
        return myObj;
    }
   /* public void fillContent(Context context1) {
        context = context1;
    }*/
    public void allergyFetchApiCall() {
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        JSONObject params = new JSONObject();
        try {
            params.put("hospital_reg_num",currentMedical.getHospital_reg_number());
            params.put("byWhom", "medical personnel");
            params.put("byWhomID", PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalPerson_id());
            params.put("patientID", PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("medical_record_id", PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.fetchAllergy(currentMedical.getAccessToken(), gsonObject), "fetchAllergy");
    }
    public void processAndfetchAllegryData(AllergyServerObject userIdentifier, ArrayList<TrackingServerObject> trackArray) {
        AllergieModel historyModel = new AllergieModel();
        historyModel.setPatientId(userIdentifier.getPatientID());
        historyModel.setMedicalPersonId(userIdentifier.getMedical_personnel_id());
        historyModel.setMedicalRecordId(userIdentifier.getMedical_record_id());
        historyModel.setHospitalRegNum(PatientProfileDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
        historyModel.setName(userIdentifier.getName());
        historyModel.setNote(userIdentifier.getNote());
        historyModel.setAllergyInfo(userIdentifier.getAllergyInformation());
        historyModel.setAllergyRecordId(userIdentifier.getAllergy_record_id());

        historyModel.setPatientlProfileModel(PatientProfileDataController.getInstance().currentPatientlProfile);

        if (AllergyDataController.getInstance().insertAllergyData(historyModel)) {
            AllergyDataController.getInstance().fetchAllergyData(PatientProfileDataController.getInstance().currentPatientlProfile);
        }

        if (trackArray.size() > 0) {
            for (int index = 0; index < trackArray.size(); index++) {
               processFamilytrackData(trackArray.get(index),historyModel);
            }
        }
    }
    public void processFamilytrackData(TrackingServerObject serverObject,AllergieModel historyModel) {
        AllergyTrackInfoModel trackInfoModel=new AllergyTrackInfoModel();
        trackInfoModel.setAllergieModel(historyModel);
        trackInfoModel.setAllergyRecordId( historyModel.getAllergyRecordId());
        trackInfoModel.setByWhom(serverObject.getByWhom());
        trackInfoModel.setByWhomId(serverObject.getByWhomID());
        Long l=Long.parseLong(serverObject.getDate())/1000;
        trackInfoModel.setDate(String.valueOf(l));
        Log.e("trackingServerObjects", "call" + trackInfoModel.getByWhomId());

        if(AllergyTrackInfoDataController.getInstance().insertTrackData(trackInfoModel)){
            //FamilyTrackInfoDataController.getInstance().fetchTrackData(historyModel);
        }

    }
    public void processAddAllergyData(AllergyServerObject userIdentifier) {

        AllergieModel historyModel = new AllergieModel();
        historyModel.setPatientId(userIdentifier.getPatientID());
        historyModel.setMedicalPersonId(userIdentifier.getMedical_personnel_id());
        historyModel.setMedicalRecordId(userIdentifier.getMedical_record_id());
        historyModel.setHospitalRegNum(PatientProfileDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
        historyModel.setName(userIdentifier.getName());
        historyModel.setNote(userIdentifier.getNote());
        historyModel.setAllergyInfo(userIdentifier.getAllergyInformation());
        historyModel.setAllergyRecordId(userIdentifier.getAllergy_record_id());

        historyModel.setPatientlProfileModel(PatientProfileDataController.getInstance().currentPatientlProfile);

        if (AllergyDataController.getInstance().insertAllergyData(historyModel)) {
            AllergyDataController.getInstance().fetchAllergyData(PatientProfileDataController.getInstance().currentPatientlProfile);
        }

        AllergyTrackInfoModel trackInfoModel=new AllergyTrackInfoModel();
        trackInfoModel.setAllergieModel(historyModel);
        trackInfoModel.setAllergyRecordId( historyModel.getAllergyRecordId());
        trackInfoModel.setByWhom(PatientProfileDataController.getInstance().currentPatientlProfile.getFirstName()+" "+ PatientProfileDataController.getInstance().currentPatientlProfile.getLastName());
        trackInfoModel.setByWhomId(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
        trackInfoModel.setDate(String.valueOf(System.currentTimeMillis() / 1000L));
        Log.e("trackingServerObjects", "call" + trackInfoModel.getByWhomId());
        if(AllergyTrackInfoDataController.getInstance().insertTrackData(trackInfoModel)){
            //FamilyTrackInfoDataController.getInstance().fetchTrackData(historyModel);
        }
    }
    public void processAllergyupdateData(AllergyServerObject userIdentifier) {
        AllergieModel  historyModel = new AllergieModel();
        historyModel.setPatientId(userIdentifier.getPatientID());
        historyModel.setMedicalPersonId(userIdentifier.getMedical_personnel_id());
        historyModel.setMedicalRecordId(userIdentifier.getMedical_record_id());
        historyModel.setHospitalRegNum(userIdentifier.getHospital_reg_num());
        historyModel.setName(userIdentifier.getName());
        historyModel.setNote(userIdentifier.getNote());

        historyModel.setAllergyRecordId(userIdentifier.getAllergy_record_id());
        historyModel.setAllergyInfo(userIdentifier.getAllergyInformation());

        historyModel.setPatientlProfileModel(PatientProfileDataController.getInstance().currentPatientlProfile);

        if (AllergyDataController.getInstance().updateAllergyData(historyModel)) {
            AllergyDataController.getInstance().fetchAllergyData(PatientProfileDataController.getInstance().currentPatientlProfile);
        }

        ArrayList<AllergyTrackInfoModel> trackInfoModels= AllergyTrackInfoDataController.getInstance().fetchFamilyTrackingBasedOnAllergyRecorsID(AllergyDataController.getInstance().currentAllergieModel);
        if(trackInfoModels.size()>0){
            AllergyTrackInfoModel trackInfoModel=trackInfoModels.get(trackInfoModels.size()-1);
            trackInfoModel.setDate(String.valueOf(System.currentTimeMillis() / 1000L));
            if(AllergyTrackInfoDataController.getInstance().updateTrackData(trackInfoModel)){
            }
        }

    }
}
