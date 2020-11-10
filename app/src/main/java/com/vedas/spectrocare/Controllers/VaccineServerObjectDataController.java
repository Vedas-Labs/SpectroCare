package com.vedas.spectrocare.Controllers;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBase.VaccineDataController;
import com.vedas.spectrocare.DataBase.VaccineTrackInfoDataController;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.DataBaseModels.VaccineModel;
import com.vedas.spectrocare.DataBaseModels.VaccineTrackInfoModel;
import com.vedas.spectrocare.ServerApiModel.TrackingServerObject;
import com.vedas.spectrocare.ServerApiModel.VaccineServerObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VaccineServerObjectDataController {
    public static VaccineServerObjectDataController myObj;
    //Context context;
    public static VaccineServerObjectDataController getInstance() {
        if (myObj == null) {
            myObj = new VaccineServerObjectDataController();
        }
        return myObj;
    }
    /* public void fillContent(Context context1) {
        context = context1;
    }*/
    public void vaccineFetchApiCall() {
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        JSONObject params = new JSONObject();
        try {
            params.put("hospital_reg_num", currentMedical.getHospital_reg_number());
            params.put("byWhom","medical personnel");
            params.put("byWhomID",currentMedical.getMedical_person_id());
           // params.put("medical_personnel_id", currentMedical.getMedical_person_id());
            params.put("patientID", PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("medical_record_id", PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.fetchVaccine(currentMedical.getAccessToken(), gsonObject), "fetch");
    }
    public void processAndfetchVaccineData(VaccineServerObject userIdentifier, ArrayList<TrackingServerObject> trackArray) {

        VaccineModel historyModel = new VaccineModel();
        historyModel.setPatientId(userIdentifier.getPatientID());
        historyModel.setMedicalPersonId(userIdentifier.getMedical_personnel_id());
        historyModel.setMedicalRecordId(userIdentifier.getMedical_record_id());
        historyModel.setHospitalRegNum(PatientProfileDataController.getInstance().currentPatientlProfile.getHospital_reg_number());

        historyModel.setVaccineName(userIdentifier.getImmunizationName());
        historyModel.setVaccineDate(userIdentifier.getImmunizationDate());
        historyModel.setNote(userIdentifier.getNotes());
        historyModel.setVaccineRecordId(userIdentifier.getImmunization_record_id());

        historyModel.setPatientlProfileModel(PatientProfileDataController.getInstance().currentPatientlProfile);

        if (VaccineDataController.getInstance().insertVaccineData(historyModel)) {
            VaccineDataController.getInstance().fetchVaccineData(PatientProfileDataController.getInstance().currentPatientlProfile);
        }

        if (trackArray.size() > 0) {
            for (int index = 0; index < trackArray.size(); index++) {
                processVaccinetrackData(trackArray.get(index),historyModel);
            }
        }
    }
    public void processVaccinetrackData(TrackingServerObject serverObject,VaccineModel historyModel) {
        VaccineTrackInfoModel trackInfoModel=new VaccineTrackInfoModel();

        trackInfoModel.setVaccineModel(historyModel);
        trackInfoModel.setVaccineRecordId( historyModel.getVaccineRecordId());
        trackInfoModel.setByWhom(serverObject.getByWhom());
        trackInfoModel.setByWhomId(serverObject.getByWhomID());
        Long l=Long.parseLong(serverObject.getDate())/1000;
        trackInfoModel.setDate(String.valueOf(l));
        Log.e("trackingServerObjects", "call" + trackInfoModel.getByWhomId());

        if(VaccineTrackInfoDataController.getInstance().insertTrackData(trackInfoModel)){
            //FamilyTrackInfoDataController.getInstance().fetchTrackData(historyModel);
        }

    }
    public void processAddVaccineData(VaccineServerObject userIdentifier) {

        VaccineModel historyModel = new VaccineModel();
        historyModel.setPatientId(userIdentifier.getPatientID());
        historyModel.setMedicalPersonId(userIdentifier.getMedical_personnel_id());
        historyModel.setMedicalRecordId(userIdentifier.getMedical_record_id());
        historyModel.setHospitalRegNum(PatientProfileDataController.getInstance().currentPatientlProfile.getHospital_reg_number());

        historyModel.setVaccineName(userIdentifier.getImmunizationName());
        historyModel.setVaccineDate(userIdentifier.getImmunizationDate());
        historyModel.setNote(userIdentifier.getNotes());
        historyModel.setVaccineRecordId(userIdentifier.getImmunization_record_id());

        historyModel.setPatientlProfileModel(PatientProfileDataController.getInstance().currentPatientlProfile);

        if (VaccineDataController.getInstance().insertVaccineData(historyModel)) {
            VaccineDataController.getInstance().fetchVaccineData(PatientProfileDataController.getInstance().currentPatientlProfile);
        }

        VaccineTrackInfoModel trackInfoModel=new VaccineTrackInfoModel();
        trackInfoModel.setVaccineModel(historyModel);
        trackInfoModel.setVaccineRecordId( historyModel.getVaccineRecordId());
        trackInfoModel.setByWhom(PatientProfileDataController.getInstance().currentPatientlProfile.getFirstName()+" "+ PatientProfileDataController.getInstance().currentPatientlProfile.getLastName());
        trackInfoModel.setByWhomId(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
        trackInfoModel.setDate(String.valueOf(System.currentTimeMillis() / 1000L));
        Log.e("trackingServerObjects", "call" + trackInfoModel.getByWhomId());
        if(VaccineTrackInfoDataController.getInstance().insertTrackData(trackInfoModel)){
            //FamilyTrackInfoDataController.getInstance().fetchTrackData(historyModel);
        }
    }
    public void processVaccineupdateData(VaccineServerObject userIdentifier) {
        VaccineModel  historyModel = new VaccineModel();

        historyModel.setPatientId(userIdentifier.getPatientID());
        historyModel.setMedicalPersonId(userIdentifier.getMedical_personnel_id());
        historyModel.setMedicalRecordId(userIdentifier.getMedical_record_id());
        historyModel.setHospitalRegNum(userIdentifier.getHospital_reg_num());

        historyModel.setVaccineName(userIdentifier.getImmunizationName());
        historyModel.setVaccineDate(userIdentifier.getImmunizationDate());
        historyModel.setNote(userIdentifier.getNotes());
        historyModel.setVaccineRecordId(userIdentifier.getImmunization_record_id());

        historyModel.setPatientlProfileModel(PatientProfileDataController.getInstance().currentPatientlProfile);

        if (VaccineDataController.getInstance().updateVaccineData(historyModel)) {
            VaccineDataController.getInstance().fetchVaccineData(PatientProfileDataController.getInstance().currentPatientlProfile);
        }

      ArrayList<VaccineTrackInfoModel> trackInfoModels= VaccineTrackInfoDataController.getInstance().fetchVaccineTrackingBasedOnVaccineRecorsID(VaccineDataController.getInstance().currentVaccineModel);
        if(trackInfoModels.size()>0){
            VaccineTrackInfoModel trackInfoModel=trackInfoModels.get(trackInfoModels.size()-1);
            trackInfoModel.setDate(String.valueOf(System.currentTimeMillis() / 1000L));
            if(VaccineTrackInfoDataController.getInstance().updateTrackData(trackInfoModel)){
            }
        }

    }
}
