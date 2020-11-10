package com.vedas.spectrocare.Controllers;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.DataBase.IllnessDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBase.SurgricalRecordDataControll;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.DataBaseModels.SurgeryAttachModel;
import com.vedas.spectrocare.DataBaseModels.SurgicalRecordModel;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.ServerApiModel.AttachmentServerObjects;
import com.vedas.spectrocare.ServerApiModel.SurgicalServerObject;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SurgicalRecordServerObjectDataController {
    public static SurgicalRecordServerObjectDataController myObj;
    Context context;
    public JSONObject params;

    public static SurgicalRecordServerObjectDataController getInstance() {
        if (myObj == null) {
            myObj = new SurgicalRecordServerObjectDataController();
        }
        return myObj;
    }

    public void fillContent(Context context1) {
        context = context1;
    }


    public void addSurgicalCallForAttchmet(final File file, final String fielnmae, String discription, String nameOfDoc,
                                           String dateOfSurg, String procedureOfsurg) {
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;

        final SurgicalServerObject surgicalObject = new SurgicalServerObject();
        surgicalObject.setFilePath(fielnmae);
        surgicalObject.setIllnessID(IllnessDataController.getInstance().currentIllnessRecordModel.getIllnessRecordId());
        surgicalObject.setMoreDetails(discription);
        surgicalObject.setDoctorMedicalPersonnelId("58024Ap9");
        surgicalObject.setDoctorName(nameOfDoc);
        surgicalObject.setSurgeryDate(dateOfSurg);
        surgicalObject.setSurgicalProcedure(procedureOfsurg);
        surgicalObject.setSurgeryInformation(discription);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ServerApi s = retrofit.create(ServerApi.class);

        MultipartBody.Part screeningRecord;
        RequestBody image = RequestBody.create(MediaType.parse("image/*"), file);
        screeningRecord = MultipartBody.Part.createFormData("surgicalRecord", file.getName(), image);
        RequestBody illnessId = RequestBody.create(MediaType.parse("text/plain"), surgicalObject.getIllnessID());
        RequestBody hospitalRegNum = RequestBody.create(MediaType.parse("text/plain"),currentMedical.getHospital_reg_number());
        RequestBody patientID = RequestBody.create(MediaType.parse("text/plain"),PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
        RequestBody byWhom = RequestBody.create(MediaType.parse("text/plain"), "medical personnel");
        RequestBody byWhomID = RequestBody.create(MediaType.parse("text/plain"),currentMedical.getMedical_person_id() );
        RequestBody medicalRecordID = RequestBody.create(MediaType.parse("text/plain"),PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
        RequestBody documentMedicId = RequestBody.create(MediaType.parse("text/plain"), surgicalObject.getDoctorMedicalPersonnelId());
        RequestBody moreDetails = RequestBody.create(MediaType.parse("text/plain"), surgicalObject.getMoreDetails());
        RequestBody doctorName = RequestBody.create(MediaType.parse("text/plain"), surgicalObject.getDoctorName());
        RequestBody surgeryProcedure = RequestBody.create(MediaType.parse("text/plain"), surgicalObject.getSurgicalProcedure());
        RequestBody surgeryDate = RequestBody.create(MediaType.parse("text/plain"), surgicalObject.getSurgeryDate());
        RequestBody surgeryIm = RequestBody.create(MediaType.parse("text/plain"), "");//surgicalObject.getSurgeryInformation());

        Log.e("finalData",""+PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId()+","+PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId()
        +","+currentMedical.getHospital_reg_number()+","+currentMedical.getMedical_person_id()+","+surgicalObject.getIllnessID()+","+
                surgicalObject.getMoreDetails()+","+surgicalObject.getDoctorMedicalPersonnelId()+",");
/*
        Call<SurgicalServerObject> call = s.addIllnessSurgicalRecord(
                currentMedical.getAccessToken(), illnessId, screeningRecord, moreDetails,
                documentMedicId, doctorName, surgeryProcedure,
                surgeryDate,surgeryIm);
*/
        Call<SurgicalServerObject> call = s.addIllnessSurgicalRecord(
                currentMedical.getAccessToken(),illnessId,screeningRecord,moreDetails,documentMedicId,doctorName,surgeryProcedure,
                surgeryDate,surgeryIm,byWhomID,medicalRecordID,byWhom,hospitalRegNum, patientID);

        call.enqueue(new Callback<SurgicalServerObject>() {
            @Override
            public void onResponse(Call<SurgicalServerObject> call, Response<SurgicalServerObject> response) {
                Log.e("rrrrrr",""+response);
                Log.e("checkMessage", "" + response.code());
                if (response.body() != null) {
                    String message = response.body().getMessage();
                    Log.e("cMessage", "" + response.body().getMessage());
                    String respons = response.body().getResponse();
                    if (respons.equals("3")) {
                        String screeningID = response.body().getIllnessSurgicalID();
                        String filePath = response.body().getFilePath();
                        Log.e("idSuirg", "" + screeningID);
                        Log.e("screeningIDs", "call" + screeningID + filePath);
                        SurgicalRecordModel recordModel = new SurgicalRecordModel();
                        recordModel.setIllnessID(IllnessDataController.getInstance().currentIllnessRecordModel.getIllnessRecordId());
                        recordModel.setIllnessSurgicalId(screeningID);
                        recordModel.setAttachment(filePath);
                        recordModel.setMoreInfo(surgicalObject.getMoreDetails());
                        recordModel.setDoctorMedicalPersonnelId(surgicalObject.getDoctorMedicalPersonnelId());
                        recordModel.setDoctorName(surgicalObject.getDoctorName());
                        recordModel.setSurgeryProcedure(surgicalObject.getSurgicalProcedure());
                        recordModel.setSurgeryDate(surgicalObject.getSurgeryDate());
                        recordModel.setSurgeryInformation(surgicalObject.getMoreDetails());
                        recordModel.setIllnessRecordModel(IllnessDataController.getInstance().currentIllnessRecordModel);
                        if (SurgricalRecordDataControll.getInstance().insertSurgicalData(recordModel)) {
                            SurgricalRecordDataControll.getInstance().fetchingSurgicalData(IllnessDataController.getInstance().currentIllnessRecordModel);
                        }
                        EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("addSurgical"));
                    }
                }
            }

            @Override
            public void onFailure(Call<SurgicalServerObject> call, Throwable t) {
                Log.e("Error", "" + t);
                Log.e("ttoto",""+t);
                t.printStackTrace();

            }
        });
    }

    public void fetchSurgicalRecoreds() {
        JSONObject params = new JSONObject();
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
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
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.fetchSurgicalRecord(currentMedical.getAccessToken(), gsonObject), "fetch");
    }

    public void deleteSurgicalRecord(SurgicalRecordModel surgicalRecordModel) {
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
            params.put("illnessSurgicalID", SurgricalRecordDataControll.getInstance().currentSurgicalmodel.getIllnessSurgicalId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.deleteSurgicalRecord(currentMedical.getAccessToken(), gsonObject), "delete");
    }

    public void processfetchSurgeryAddData(SurgicalServerObject surgicalObject, ArrayList<AttachmentServerObjects> attachArray) {
        SurgicalRecordModel recordModel = new SurgicalRecordModel();
        recordModel.setIllnessID(IllnessDataController.getInstance().currentIllnessRecordModel.getIllnessRecordId());
        recordModel.setIllnessSurgicalId(surgicalObject.getIllnessSurgicalID());
      //  recordModel.setMoreInfo(surgicalObject.getMoreDetails());
        recordModel.setDoctorMedicalPersonnelId(surgicalObject.getDoctorMedicalPersonnelId());
        recordModel.setDoctorName(surgicalObject.getDoctorName());
        recordModel.setSurgeryProcedure(surgicalObject.getSurgicalProcedure());
        recordModel.setSurgeryDate(surgicalObject.getSurgeryDate());
        recordModel.setSurgeryInformation(surgicalObject.getSurgeryInformation());
        recordModel.setIllnessRecordModel(IllnessDataController.getInstance().currentIllnessRecordModel);
        recordModel.setAttachment(attachArray.get(0).getFilePath());
        recordModel.setMoreInfo(attachArray.get(0).getMoreDetails());
        Log.e("attachServerObjects", "call" + recordModel.getAttachment());
        String lll = "/images/MedicalRecords/LrqXK1589538023714";
        Log.e("adfa",""+lll.length());
        Log.e("infoRR",""+attachArray.get(0).getMoreDetails());

        if (SurgricalRecordDataControll.getInstance().insertSurgicalData(recordModel)) {
            SurgricalRecordDataControll.getInstance().fetchingSurgicalData(IllnessDataController.getInstance().currentIllnessRecordModel);
        }
    }
}


