package com.vedas.spectrocare.Controllers;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.DataBase.IllnessDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBase.VaccineDataController;
import com.vedas.spectrocare.DataBase.VaccineTrackInfoDataController;
import com.vedas.spectrocare.DataBaseModels.IllnessRecordModel;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.DataBaseModels.VaccineModel;
import com.vedas.spectrocare.DataBaseModels.VaccineTrackInfoModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApiModel.IllnessServerObject;
import com.vedas.spectrocare.ServerApiModel.TrackingServerObject;
import com.vedas.spectrocare.ServerApiModel.VaccineServerObject;
import com.vedas.spectrocare.activities.MedicalHistoryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class IllnessServerObjectDataController {
    public static IllnessServerObjectDataController myObj;
    Context context;
    public JSONObject params;
    public static IllnessServerObjectDataController getInstance() {
        if (myObj == null) {
            myObj = new IllnessServerObjectDataController();
        }
        return myObj;
    }
     public void fillContent(Context context1) {
        context = context1;
    }

    public void illnessFetchApiCall() {
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        JSONObject params = new JSONObject();
        try {
            params.put("hospital_reg_num", currentMedical.getHospital_reg_number());
            params.put("patientID", PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("medical_record_id", PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
            params.put("byWhom","medical personnel");
            params.put("byWhomID", currentMedical.getMedical_person_id());
          //  params.put("medical_personnel_id", currentMedical.getMedical_person_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.fetchMedicalRecordIllness(currentMedical.getAccessToken(), gsonObject), "fetch");
    }
    public void updateIllnessJsonParams(IllnessRecordModel illnessRecordModel) {
         params = new JSONObject();
        try {
            params.put("hospital_reg_num",illnessRecordModel.getHospitalRegNum() );
            params.put("patientID",illnessRecordModel.getPatientId());
           // params.put("medical_personnel_id",illnessRecordModel.getMedicalPersonId());
            params.put("medical_record_id",illnessRecordModel.getMedicalRecordId());
            params.put("illnessCondition","Getting fever isFromMedicalPerson 10 days");
            params.put("symptoms",illnessRecordModel.getSymptoms());
            params.put("currentStatus",illnessRecordModel.getCurrentStatus());
            params.put("description",illnessRecordModel.getMoreInfo());
            params.put("isCurrentIllness",illnessRecordModel.getIsCurrentIllness());
            params.put("illnessID",illnessRecordModel.getIllnessRecordId());
            params.put("byWhom","medical personnel");
            params.put("byWhomID", MedicalProfileDataController.getInstance().currentMedicalProfile.getMedical_person_id());
            params.put("startDate","");
            params.put("endDate","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
       ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.updateMedicalIllness(
               MedicalProfileDataController.getInstance().currentMedicalProfile.getAccessToken(), gsonObject), "update");
    }
    public void deleteIllnessJsonParams(IllnessRecordModel illnessRecordModel) {
        JSONObject params = new JSONObject();
        try {
            params.put("hospital_reg_num", illnessRecordModel.getHospitalRegNum());
            params.put("patientID", illnessRecordModel.getPatientId());
          //  params.put("medical_personnel_id",illnessRecordModel.getMedicalPersonId());
            params.put("medical_record_id",illnessRecordModel.getMedicalRecordId());
            params.put("illnessID",illnessRecordModel.getIllnessRecordId());
            params.put("byWhom","medical personnel");
            params.put("byWhomID",MedicalProfileDataController.getInstance().currentMedicalProfile.getMedical_person_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.deleteMedicalIllness(
                MedicalProfileDataController.getInstance().currentMedicalProfile.getAccessToken(), gsonObject), "delete");
    }
    public void processAddIllnessData(IllnessServerObject userIdentifier) {
        IllnessRecordModel historyModel = new IllnessRecordModel();
        historyModel.setPatientId(userIdentifier.getPatientID());
        historyModel.setMedicalPersonId(userIdentifier.getMedical_personnel_id());
        historyModel.setMedicalRecordId(userIdentifier.getMedical_record_id());
        historyModel.setHospitalRegNum(PatientProfileDataController.getInstance().currentPatientlProfile.getHospital_reg_number());

        historyModel.setIllnessRecordId(userIdentifier.getIllnessID());
        historyModel.setSymptoms(userIdentifier.getSymptoms());
        historyModel.setMoreInfo(userIdentifier.getMoreInfo());
        historyModel.setCurrentStatus(userIdentifier.getCurrentStatus());
        historyModel.setIsCurrentIllness(userIdentifier.getIsCurrentIllness());

        historyModel.setPatientlProfileModel(PatientProfileDataController.getInstance().currentPatientlProfile);

        if (IllnessDataController.getInstance().insertIllnesData(historyModel)) {
            IllnessDataController.getInstance().fetchIllnesData(PatientProfileDataController.getInstance().currentPatientlProfile);
        }

       /* AllergyTrackInfoModel trackInfoModel=new AllergyTrackInfoModel();
        trackInfoModel.setAllergieModel(historyModel);
        trackInfoModel.setAllergyRecordId( historyModel.getAllergyRecordId());
        trackInfoModel.setByWhom(PatientProfileDataController.getInstance().currentPatientlProfile.getFirstName()+" "+ PatientProfileDataController.getInstance().currentPatientlProfile.getLastName());
        trackInfoModel.setByWhomId(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
        trackInfoModel.setDate(String.valueOf(System.currentTimeMillis() / 1000L));
        Log.e("trackingServerObjects", "call" + trackInfoModel.getByWhomId());
        if(AllergyTrackInfoDataController.getInstance().insertTrackData(trackInfoModel)){
            //FamilyTrackInfoDataController.getInstance().fetchTrackData(historyModel);
        }*/
    }
    public void processAndfetchIllnessData(IllnessServerObject userIdentifier, ArrayList<TrackingServerObject> trackArray) {
        IllnessRecordModel historyModel = new IllnessRecordModel();
        historyModel.setPatientId(userIdentifier.getPatientID());
        historyModel.setMedicalPersonId(userIdentifier.getMedical_personnel_id());
        historyModel.setMedicalRecordId(userIdentifier.getMedical_record_id());
        historyModel.setHospitalRegNum(PatientProfileDataController.getInstance().currentPatientlProfile.getHospital_reg_number());

        historyModel.setIllnessRecordId(userIdentifier.getIllnessID());
        historyModel.setSymptoms(userIdentifier.getSymptoms());
        historyModel.setMoreInfo(userIdentifier.getMoreInfo());
        historyModel.setCurrentStatus(userIdentifier.getCurrentStatus());
        historyModel.setIsCurrentIllness(userIdentifier.getIsCurrentIllness());

        historyModel.setPatientlProfileModel(PatientProfileDataController.getInstance().currentPatientlProfile);

        if (IllnessDataController.getInstance().insertIllnesData(historyModel)) {
            IllnessDataController.getInstance().fetchIllnesData(PatientProfileDataController.getInstance().currentPatientlProfile);
        }
       /* if (trackArray.size() > 0) {
            for (int index = 0; index < trackArray.size(); index++) {
                processFamilytrackData(trackArray.get(index),historyModel);
            }
        }*/
    }
    public void processIllnessgyupdateData(IllnessServerObject userIdentifier) {
        IllnessRecordModel  historyModel = new IllnessRecordModel();
        historyModel.setPatientId(userIdentifier.getPatientID());
        historyModel.setMedicalPersonId(userIdentifier.getMedical_personnel_id());
        historyModel.setMedicalRecordId(userIdentifier.getMedical_record_id());
        historyModel.setHospitalRegNum(userIdentifier.getHospital_reg_num());

        historyModel.setIllnessRecordId(userIdentifier.getIllnessID());
        historyModel.setSymptoms(userIdentifier.getSymptoms());
        historyModel.setMoreInfo(userIdentifier.getMoreInfo());
        historyModel.setCurrentStatus(userIdentifier.getCurrentStatus());
        historyModel.setIsCurrentIllness(userIdentifier.getIsCurrentIllness());

        historyModel.setPatientlProfileModel(PatientProfileDataController.getInstance().currentPatientlProfile);

        if (IllnessDataController.getInstance().updateIllnesData(historyModel)) {
            IllnessDataController.getInstance().fetchIllnesData(PatientProfileDataController.getInstance().currentPatientlProfile);
        }

       /* ArrayList<AllergyTrackInfoModel> trackInfoModels= AllergyTrackInfoDataController.getInstance().fetchFamilyTrackingBasedOnAllergyRecorsID(AllergyDataController.getInstance().currentAllergieModel);
        if(trackInfoModels.size()>0){
            AllergyTrackInfoModel trackInfoModel=trackInfoModels.get(trackInfoModels.size()-1);
            trackInfoModel.setDate(String.valueOf(System.currentTimeMillis() / 1000L));
            if(AllergyTrackInfoDataController.getInstance().updateTrackData(trackInfoModel)){
            }
        }*/
    }
}
