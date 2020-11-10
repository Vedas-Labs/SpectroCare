package com.vedas.spectrocare.Controllers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.DataBase.IllnessDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.MedicationAttachmentRecordDataController;
import com.vedas.spectrocare.DataBase.MedicationRecordDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.DataBaseModels.MedicationAttachmentModel;
import com.vedas.spectrocare.DataBaseModels.MedicationRecordModel;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.ServerApiModel.MedicationAttachServerObject;
import com.vedas.spectrocare.ServerApiModel.MedicationManullayServerObject;
import com.vedas.spectrocare.adapter.ViewMedicalAdapter;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MedicationServerObjectDataController {

    public static MedicationServerObjectDataController myObj;
    Context context;
    public boolean isFromStaring = false;

    public static MedicationServerObjectDataController getInstance() {
        if (myObj == null) {
            myObj = new MedicationServerObjectDataController();
        }

        return myObj;
    }

    public void fillContent(Context context1) {
        context = context1;
    }

    public void addMedicationRecord(JsonObject medicationObject, boolean isUpdate) {
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        Log.e("getAccessToken", "onResponse: " + isUpdate);
        if (isUpdate) {
            ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.updateMedicationRecord(currentMedical.getAccessToken(), medicationObject), "update");
        } else {
            ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.addMedicationRecord(currentMedical.getAccessToken(), medicationObject), "insert");
        }
    }

    public void medicationDeleteApiCall(MedicationRecordModel medicationRecordModel) {
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        JSONObject params = new JSONObject();
        try {
            params.put("hospital_reg_num", currentMedical.getHospital_reg_number());
            params.put("byWhom","medical personnel");
            params.put("byWhomID",currentMedical.getMedical_person_id());
            params.put("patientID", PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("medical_record_id", PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
            params.put("illnessID", medicationRecordModel.getIllnessID());
            params.put("illnessMedicationID", medicationRecordModel.getIllnessMedicationID());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.deleteMEdicationRecord(currentMedical.getAccessToken(), gsonObject), "delete");
    }

    public void medicinesFetchApiCall() {
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        JSONObject params = new JSONObject();
        try {
            params.put("hospital_reg_num", currentMedical.getHospital_reg_number());
            params.put("byWhom","medical personnel");
            params.put("byWhomID",currentMedical.getMedical_person_id());
            params.put("patientID", PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("medical_record_id", PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
            params.put("illnessID", IllnessDataController.getInstance().currentIllnessRecordModel.getIllnessRecordId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.fetchMedicinesRecord(currentMedical.getAccessToken(), gsonObject), "fetch");
    }

    public void processAddMedicationAddData(MedicationManullayServerObject userIdentifier) {
        //  Log.e("getMannualPrescriptions", "call" + userIdentifier.getMannualPrescriptions().toString());
        JsonObject jsonObject = userIdentifier.getMannualPrescriptions();
      //  JsonObject onjectJson = jsonObject.getAsJsonObject("addedDate");
        String name = jsonObject.get("doctorName").getAsString();
        String date = jsonObject.get("addedDate").getAsString();
        String updateDate = jsonObject.get("updateDate").getAsString();
        Log.e("namedate", "" + name + date + updateDate);
        Long l = Long.parseLong(updateDate) / 1000;

        MedicationRecordModel historyModel = new MedicationRecordModel();
        historyModel.setPatientID(userIdentifier.getPatientID());
        historyModel.setMedical_record_id(userIdentifier.getMedical_record_id());
        historyModel.setMedical_personnel_id(userIdentifier.getMedical_personnel_id());
        historyModel.setIllnessMedicationID(userIdentifier.getIllnessMedicationID());
        historyModel.setIllnessID(userIdentifier.getIllnessID());
        historyModel.setHospital_reg_num(userIdentifier.getHospital_reg_num());
        historyModel.setDoctorName(name);
        historyModel.setDate(String.valueOf(l));
        historyModel.setIllnessRecordModel(IllnessDataController.getInstance().currentIllnessRecordModel);
        if (MedicationRecordDataController.getInstance().insertMedicationData(historyModel)) {
            MedicationRecordDataController.getInstance().fetchMedicationData(IllnessDataController.getInstance().currentIllnessRecordModel);
        }

    }

    public void processAttachmentsMedicationAddData(MedicationManullayServerObject userIdentifier) {
        //  Log.e("getMannualPrescriptions", "call" + userIdentifier.getMannualPrescriptions().toString());
        JsonObject jsonObject = userIdentifier.getAttachedPrescriptions();
        String name = jsonObject.get("doctorName").getAsString();
        String date = jsonObject.get("addedDate").getAsString();
        Log.e("adfasfasf","dd"+date);
       // String updateDate = jsonObject.get("lastUpdateDate").getAsString();
        String updateDate = jsonObject.get("updateDate").getAsString();
        Log.e("namedate", "" + name + date + updateDate);
        Long l = Long.parseLong(updateDate) / 1000;

        MedicationRecordModel historyModel = new MedicationRecordModel();
        historyModel.setPatientID(userIdentifier.getPatientID());
        historyModel.setMedical_record_id(userIdentifier.getMedical_record_id());
        historyModel.setMedical_personnel_id(userIdentifier.getMedical_personnel_id());
        historyModel.setIllnessMedicationID(userIdentifier.getIllnessMedicationID());
        historyModel.setIllnessID(userIdentifier.getIllnessID());
        historyModel.setHospital_reg_num(userIdentifier.getHospital_reg_num());
        historyModel.setDoctorName(name);
        historyModel.setDate(String.valueOf(l));
        historyModel.setIllnessRecordModel(IllnessDataController.getInstance().currentIllnessRecordModel);
        if (MedicationRecordDataController.getInstance().insertMedicationData(historyModel)) {
            MedicationRecordDataController.getInstance().fetchMedicationData(IllnessDataController.getInstance().currentIllnessRecordModel);
        }

    }

    public void addApiCallForAttchmet(final MedicationAttachServerObject serverObject) {
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        Log.e("checkMessage", "" + serverObject.getPrescription1() + serverObject.getPrescription2());
        Log.e("checkMessage", "" + serverObject.getPrescription1MoreDetails() + serverObject.getPrescription2MoreDetails1() + serverObject.getIllnessID());

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create()).build();

        final ServerApi s = retrofit.create(ServerApi.class);
        MultipartBody.Part file1 = null, file2 = null;

        if (serverObject.getPrescription1() != null) {
            RequestBody requestBodyfile1 = RequestBody.create(MediaType.parse("image/*"), serverObject.getPrescription1());
            file1 = MultipartBody.Part.createFormData("prescription1", serverObject.getPrescription1().getName(), requestBodyfile1);
        }
        if (serverObject.getPrescription2() != null) {
            RequestBody requestBodyfile2 = RequestBody.create(MediaType.parse("image/*"), serverObject.getPrescription2());
            file2 = MultipartBody.Part.createFormData("prescription2", serverObject.getPrescription2().getName(), requestBodyfile2);
        }
        RequestBody more1 = RequestBody.create(MediaType.parse("text/plain"), serverObject.getPrescription1MoreDetails());
        RequestBody more2 = RequestBody.create(MediaType.parse("text/plain"), serverObject.getPrescription2MoreDetails1());
        RequestBody doctorName = RequestBody.create(MediaType.parse("text/plain"), serverObject.getDoctorName());
        RequestBody illnessID = RequestBody.create(MediaType.parse("text/plain"), serverObject.getIllnessID());
        RequestBody medicaperSoinId = RequestBody.create(MediaType.parse("text/plain"), "5041dw");
        RequestBody byWhom = RequestBody.create(MediaType.parse("text/plain"), "medical personnel");
        RequestBody byWhomID = RequestBody.create(MediaType.parse("text/plain"),currentMedical.getMedical_person_id() );
        RequestBody hospitalRegNum = RequestBody.create(MediaType.parse("text/plain"),currentMedical.getHospital_reg_number());
        RequestBody medicalRecordID = RequestBody.create(MediaType.parse("text/plain"),PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
        RequestBody patientID = RequestBody.create(MediaType.parse("text/plain"), PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());


        Call<MedicationAttachServerObject> call = null;

        if (serverObject.getPrescription1() != null && serverObject.getPrescription2() == null) {
            if (file1!=null){ Log.e("first", "null");}

            call = s.addMedicationFirstAttachmentRecord(currentMedical.getAccessToken(),
                    illnessID, file1, more1, medicaperSoinId, doctorName,patientID,medicalRecordID,byWhom,byWhomID,hospitalRegNum);
          Log.e("tokenn","tok"+currentMedical.getAccessToken());
          Log.e("sendDetails",""+serverObject.getIllnessID()+","+serverObject.getPrescription1MoreDetails()+","+serverObject.getDoctorName()+","+PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId()+
          ","+PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId()+","+currentMedical.getMedical_person_id()+
          ","+currentMedical.getHospital_reg_number());
        }

        if (serverObject.getPrescription2() != null && serverObject.getPrescription1() == null) {
            Log.e("second", "null");
            call = s.addMedicationSecondAttachmentRecord(currentMedical.getAccessToken(),
                    illnessID, file2, more2, medicaperSoinId, doctorName,patientID,medicalRecordID,byWhom,byWhomID,hospitalRegNum);
        }
        if (serverObject.getPrescription2() != null && serverObject.getPrescription1() != null) {
            Log.e("alldata", "call");
            call = s.addMedicationAllAttachmentRecord(currentMedical.getAccessToken(),
                    illnessID, file1,file2, more1, more2, medicaperSoinId, doctorName,patientID,medicalRecordID,byWhom,byWhomID,hospitalRegNum);
        }
        call.enqueue(new Callback<MedicationAttachServerObject>() {
            @Override
            public void onResponse(Call<MedicationAttachServerObject> call, Response<MedicationAttachServerObject> response) {
                Log.e("response",""+response);

                if (response.body() != null) {
                    String message = response.body().getMessage();
                    String respons = response.body().getResponse();
                    Log.e("checkMessage", "" + message);
                    if (respons.equals("3")) {
                        String screeningID = response.body().getIllnessMedicationID();
                        String filePath1 = response.body().getPrescription1FilePath();
                        String filePath2 = response.body().getPrescription2FilePath();
                        Log.e("screeningIDs", "call" + screeningID + filePath1 + filePath2);
                        MedicationRecordModel historyModel = new MedicationRecordModel();
                        historyModel.setPatientID(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
                        historyModel.setMedical_record_id(PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
                        historyModel.setMedical_personnel_id(MedicalProfileDataController.getInstance().currentMedicalProfile.getMedical_person_id());
                        historyModel.setIllnessMedicationID(screeningID);
                        historyModel.setIllnessID(IllnessDataController.getInstance().currentIllnessRecordModel.getIllnessRecordId());
                        historyModel.setHospital_reg_num(MedicalProfileDataController.getInstance().currentMedicalProfile.getHospital_reg_number());
                        historyModel.setDoctorName("Chandu");
                        historyModel.setDate(String.valueOf(System.currentTimeMillis() / 1000));
                        historyModel.setIllnessRecordModel(IllnessDataController.getInstance().currentIllnessRecordModel);
                        if (MedicationRecordDataController.getInstance().insertMedicationData(historyModel)) {
                            MedicationRecordDataController.getInstance().fetchMedicationData(IllnessDataController.getInstance().currentIllnessRecordModel);
                        }
                        if (filePath1 != null || !filePath1.isEmpty()) {
                            MedicationAttachmentModel attachmentModel = new MedicationAttachmentModel();
                            attachmentModel.setIllnessMedicationID(screeningID);
                            attachmentModel.setFilePath(filePath1);
                            attachmentModel.setFilePath2(filePath1);
                            if (MedicationRecordDataController.getInstance().getMedicationForMedicationId(screeningID) != null) {
                                attachmentModel.setMedicationRecordModel(MedicationRecordDataController.getInstance().getMedicationForMedicationId(screeningID));
                                Log.e("testmodel", "call" + historyModel.getIllnessMedicationID());
                            }
                            if (MedicationAttachmentRecordDataController.getInstance().insertAttachmentData(attachmentModel)) {
                                MedicationAttachmentRecordDataController.getInstance().fetchAttachmentData(MedicationRecordDataController.getInstance().currentMedicationRecordModel);
                            }
                        }
                        if (filePath2 != null || !filePath2.isEmpty()) {
                            MedicationAttachmentModel attachmentModel1 = new MedicationAttachmentModel();
                            attachmentModel1.setIllnessMedicationID(screeningID);
                            attachmentModel1.setFilePath(filePath2);
                            attachmentModel1.setFilePath2(filePath2);
                            if (MedicationRecordDataController.getInstance().getMedicationForMedicationId(screeningID) != null) {
                                attachmentModel1.setMedicationRecordModel(MedicationRecordDataController.getInstance().getMedicationForMedicationId(screeningID));
                                Log.e("testmodel1", "call" + historyModel.getIllnessMedicationID());
                            }
                            if (MedicationAttachmentRecordDataController.getInstance().insertAttachmentData(attachmentModel1)) {
                                MedicationAttachmentRecordDataController.getInstance().fetchAttachmentData(MedicationRecordDataController.getInstance().currentMedicationRecordModel);
                            }
                        }
                        EventBus.getDefault().post(new ViewMedicalAdapter.MessageEvent("addattach",0));
                    }
                }
            }

            @Override
            public void onFailure(Call<MedicationAttachServerObject> call, Throwable t) {
                EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("fail"));
                Log.e("screerroreningIDs", "call" + t.getMessage());
            }
        });
    }
}
