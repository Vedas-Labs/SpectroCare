package com.vedas.spectrocare.PatientServerApiModel;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.Controllers.PhysicalServerObjectDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBaseModels.PatientModel;
import com.vedas.spectrocare.PatinetControllers.PatientFamilyDataController;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.ServerApiModel.AttachmentServerObjects;
import com.vedas.spectrocare.activities.AttachmentActivity;


import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.sql.Timestamp;
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

public class PatientSurgicalRecordServerObjectDataController {
    public static PatientSurgicalRecordServerObjectDataController myObj;
    Context context;
    public JSONObject params;

    public static PatientSurgicalRecordServerObjectDataController getInstance() {
        if (myObj == null) {
            myObj = new PatientSurgicalRecordServerObjectDataController();
        }
        return myObj;
    }

    public void fillContent(Context context1) {
        context = context1;
    }


    public void addSurgicalCallForAttchmet(boolean isupdate,final File file, final String fielnmae, String note, String doctorname,
                                           String date, String surgeryInfo) {

        Log.e("addSurgicalCa", "call" + note+doctorname+date+surgeryInfo);

        PatientLoginDataController.getInstance().fetchPatientlProfileData();
        PatientModel currentModel= PatientLoginDataController.getInstance().currentPatientlProfile;

        final PatientSurgicalObject surgicalObject = new PatientSurgicalObject();
        surgicalObject.setFilePath(fielnmae);
        if(isupdate){
            surgicalObject.setIllnessID( PatientMedicalRecordsController.getInstance().selectedSurgeryObject.getIllnessID());
        }else{
            surgicalObject.setIllnessID(PatientFamilyDataController.getInstance().selectedIllnessRecord.getIllnessID());
        }
        surgicalObject.setMoreDetails(note);
        surgicalObject.setDoctorMedicalPersonnelId("58024Ap9");
        surgicalObject.setDoctorName(doctorname);
        surgicalObject.setSurgeryDate(date);
        surgicalObject.setSurgeryProcedure(surgeryInfo);
        surgicalObject.setSurgeryInformation(surgeryInfo);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ServerApi s = retrofit.create(ServerApi.class);

        MultipartBody.Part screeningRecord = null;
        if(fielnmae != null){
            RequestBody image = RequestBody.create(MediaType.parse("image/*"), file);
            screeningRecord = MultipartBody.Part.createFormData("surgicalRecord", file.getName(), image);
        }
        RequestBody illnessId = RequestBody.create(MediaType.parse("text/plain"), surgicalObject.getIllnessID());
        RequestBody hospitalRegNum = RequestBody.create(MediaType.parse("text/plain"),currentModel.getHospital_reg_number());
        RequestBody patientID = RequestBody.create(MediaType.parse("text/plain"),currentModel.getPatientId());
        RequestBody byWhomID = RequestBody.create(MediaType.parse("text/plain"),currentModel.getMedicalPerson_id() );
        RequestBody medicalRecordID = RequestBody.create(MediaType.parse("text/plain"),currentModel.getMedicalRecordId());
        RequestBody documentMedicId = RequestBody.create(MediaType.parse("text/plain"), surgicalObject.getDoctorMedicalPersonnelId());
        RequestBody moreDetails = RequestBody.create(MediaType.parse("text/plain"), surgicalObject.getMoreDetails());
        RequestBody doctorName = RequestBody.create(MediaType.parse("text/plain"), surgicalObject.getDoctorName());
        RequestBody surgeryProcedure = RequestBody.create(MediaType.parse("text/plain"), surgicalObject.getSurgeryInformation());
        RequestBody surgeryDate = RequestBody.create(MediaType.parse("text/plain"), surgicalObject.getSurgeryDate());
        RequestBody surgeryIm = RequestBody.create(MediaType.parse("text/plain"), surgicalObject.getSurgeryInformation());//surgicalObject.getSurgeryInformation());
        RequestBody whom = RequestBody.create(MediaType.parse("text/plain"), "patient");//surgicalObject.getSurgeryInformation());

        Call<PatientSurgicalObject> call =null;
        if(!isupdate){
            call = s.addPatientSurgicalRecord(
                    currentModel.getAccessToken(),illnessId,screeningRecord,moreDetails,documentMedicId,doctorName,surgeryProcedure,
                    surgeryDate,surgeryIm,byWhomID,medicalRecordID,whom,hospitalRegNum, patientID);

        }else{
            RequestBody illnesssurgicalID = RequestBody.create(MediaType.parse("text/plain"),PatientMedicalRecordsController.getInstance().selectedSurgeryObject.getIllnessSurgicalID() );//surgicalObject.getSurgeryInformation());
            call = s.updatePatientSurgicalRecord(
                    currentModel.getAccessToken(),illnessId,screeningRecord,moreDetails,documentMedicId,doctorName,surgeryProcedure,
                    surgeryDate,surgeryIm,byWhomID,medicalRecordID,whom,hospitalRegNum, patientID,illnesssurgicalID);

        }

        call.enqueue(new Callback<PatientSurgicalObject>() {
            @Override
            public void onResponse(Call<PatientSurgicalObject> call, Response<PatientSurgicalObject> response) {
                Log.e("rrrrrr",""+response);
                Log.e("checkMessage", "" + response.code());
                if (response.body() != null) {
                    String message = response.body().getMessage();
                    Log.e("cMessage", "" + response.body().getMessage());
                    String respons = response.body().getResponse();
                    if (respons.equals("3")) {
                        if(!isupdate) {
                            String screeningID = response.body().getIllnessSurgicalID();
                            String filePath = response.body().getFilePath();
                            Log.e("idSuirg", "" + screeningID);
                            Log.e("screeningIDs", "call" + PatientFamilyDataController.getInstance().selectedIllnessRecord.getIllnessID() + screeningID + filePath);
                            surgicalObject.setIllnessSurgicalID(screeningID);
                            surgicalObject.setFilePath(filePath);
                            surgicalObject.setMoreDetails(note);

                            ArrayList<AttachmentServerObjects> objectsArrayList=new ArrayList<>();
                            AttachmentServerObjects attachmentServerObjects=new AttachmentServerObjects();
                            attachmentServerObjects.setFilePath(filePath);
                            attachmentServerObjects.setMoreDetails(note);
                            objectsArrayList.add(attachmentServerObjects);
                            Log.e("idSuirg", "" +objectsArrayList.size());

                            surgicalObject.setAttachmentLis(objectsArrayList);
                            surgicalObject.setSurgeryInformation(surgeryInfo);
                            surgicalObject.setIllnessID(PatientFamilyDataController.getInstance().selectedIllnessRecord.getIllnessID());
                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                            surgicalObject.setAddedDate(String.valueOf(timestamp.getTime()));
                            PatientMedicalRecordsController.getInstance().surgeryObjectArrayList.add(surgicalObject);
                            Log.e("idSuirg", "" + surgicalObject.getMoreDetails()+surgicalObject.getSurgeryInformation());
                            EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("addPatientSurgical"));
                        }else{
                            String filePath = response.body().getFilePath();

                            ArrayList<AttachmentServerObjects> objectsArrayList=new ArrayList<>();
                            AttachmentServerObjects attachmentServerObjects=new AttachmentServerObjects();
                            attachmentServerObjects.setFilePath(filePath);
                            attachmentServerObjects.setMoreDetails(note);

                            objectsArrayList.add(attachmentServerObjects);
                            Log.e("idSuirg", "" +objectsArrayList.size());

                            surgicalObject.setAttachmentLis(objectsArrayList);

                            PatientMedicalRecordsController.getInstance().selectedSurgeryObject.setFilePath(filePath);
                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                            PatientMedicalRecordsController.getInstance().selectedSurgeryObject.setDoctorName(surgicalObject.getDoctorName());
                            PatientMedicalRecordsController.getInstance().selectedSurgeryObject.setSurgeryDate(surgicalObject.getSurgeryDate());
                            PatientMedicalRecordsController.getInstance().selectedSurgeryObject.setMoreDetails(surgicalObject.getMoreDetails());
                            PatientMedicalRecordsController.getInstance().selectedSurgeryObject.setSurgeryInformation(surgicalObject.getSurgeryInformation());
                            PatientMedicalRecordsController.getInstance().selectedSurgeryObject.setAddedDate(String.valueOf(timestamp.getTime()));
                            PatientMedicalRecordsController.getInstance().selectedSurgeryObject.setSurgeryProcedure(surgicalObject.getSurgeryInformation());
                            Log.e("xxxxxxxxxxxx", "" + surgicalObject.getSurgeryInformation()+surgicalObject.getSurgeryInformation());
                            EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("updatePatientSurgical"));
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<PatientSurgicalObject> call, Throwable t) {
                Log.e("Error", "" + t);
                Log.e("ttoto",""+t);
                t.printStackTrace();

            }
        });
    }
    public void fetchSurgicalRecords() {
        PatientModel currentModel= PatientLoginDataController.getInstance().currentPatientlProfile;
        JSONObject params = new JSONObject();
        try {
            params.put("hospital_reg_num", currentModel.getHospital_reg_number());
            params.put("byWhom","patient");
            params.put("byWhomID",currentModel.getPatientId());
            params.put("patientID", currentModel.getPatientId());
            params.put("medical_record_id",currentModel.getMedicalRecordId());
            params.put("illnessID", PatientFamilyDataController.getInstance().selectedIllnessRecord.getIllnessID());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.patientfetchSurgeryApi(currentModel.getAccessToken(), gsonObject), "fetch");
    }
    public void deleteSingleSurgicalRecord(PatientSurgicalObject surgicalRecordModel) {
        PatientModel currentModel= PatientLoginDataController.getInstance().currentPatientlProfile;
        JSONObject params = new JSONObject();
        try {
            params.put("hospital_reg_num", currentModel.getHospital_reg_number());
            params.put("byWhom","patient");
            params.put("byWhomID",currentModel.getPatientId());
            params.put("patientID", currentModel.getPatientId());
            params.put("medical_record_id", currentModel.getMedicalRecordId());
            params.put("illnessID", surgicalRecordModel.getIllnessID());
            params.put("filePath", surgicalRecordModel.getFilePath());
            params.put("illnessSurgicalID", surgicalRecordModel.getIllnessSurgicalID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.deleteSingleSurgeryApi(currentModel.getAccessToken(), gsonObject), "deleteSingle");
    }
    public void deleteAllSurgicalRecord(PatientSurgicalObject surgicalRecordModel) {
        PatientModel currentModel= PatientLoginDataController.getInstance().currentPatientlProfile;
        JSONObject params = new JSONObject();
        try {
            params.put("byWhom","patient");
            params.put("byWhomID",currentModel.getPatientId());
            params.put("patientID", currentModel.getPatientId());
            params.put("medical_record_id", currentModel.getMedicalRecordId());
            params.put("illnessID", surgicalRecordModel.getIllnessID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.deleteAllSurgeryApi(currentModel.getAccessToken(), gsonObject), "deleteAll");
    }
}

