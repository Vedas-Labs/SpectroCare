package com.vedas.spectrocare.Controllers;
import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.DataBase.AppointmentDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBaseModels.AppointmentModel;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.ServerApiModel.AppointmentServerObjects;
import com.vedas.spectrocare.ServerApiModel.TrackingServerObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class AppointmentsServerObjectDataController {
    public static AppointmentsServerObjectDataController myObj;
    Context context;
    public static AppointmentsServerObjectDataController getInstance() {
        if (myObj == null) {
            myObj = new AppointmentsServerObjectDataController();
        }
        return myObj;
    }
    /* public void fillContent(Context context1) {
        context = context1;
    }*/
   public void fetchAppointment() {
       MedicalProfileDataController.getInstance().fetchMedicalProfileData();
       MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
       JSONObject params = new JSONObject();
       try {
           params.put("hospital_reg_num", currentMedical.getHospital_reg_number());
           params.put("patientID", PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
           //  params.put("createrMedicalPersonnelID", currentMedical.getMedical_person_id());
       } catch (JSONException e) {
           e.printStackTrace();
       }
       JsonParser jsonParser = new JsonParser();
       JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
       Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
       ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.fetcchAppointment(currentMedical.getAccessToken(), gsonObject), "fetch");
   }
    public void addAppointment(AppointmentModel serverObjects) {
        Log.e("addAppointment", "call");
        Log.e("ServiceResponse", "onResponse: " + serverObjects.getAppointmentTimeTo()+"ddd"+serverObjects.getAppointmentTimeFrom());

        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        JSONObject params = new JSONObject();
        try {
            params.put("hospital_reg_num", currentMedical.getHospital_reg_number());
            params.put("appointmentDate", serverObjects.getAppointmentDate());
            params.put("appointmentTimeFrom",serverObjects.getAppointmentTimeFrom() );
            params.put("appointmentTimeTo", serverObjects.getAppointmentTimeTo());
            params.put("visitType",serverObjects.getVisitType() );
            params.put("doctorName", serverObjects.getDoctorName());
            params.put("doctorMedicalPersonnelID", serverObjects.getDoctorMedicalPersonnelID());
            params.put("patientName",serverObjects.getPatientName() );
            params.put("patientID",serverObjects.getPatientID() );
            params.put("department", serverObjects.getDepartment());
            params.put("reasonForVisit",serverObjects.getReasonForVisit() );
            params.put("medicalRecordID",serverObjects.getMedicalRecordID() );
            params.put("creatorName",serverObjects.getCreatorName() );
            params.put("createrMedicalPersonnelID", serverObjects.getCreaterMedicalPersonnelID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.addAppointment(currentMedical.getAccessToken(), gsonObject), "insert");
    }

    public void cancelAppointment(AppointmentServerObjects serverObjects) {
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        JSONObject params = new JSONObject();
        try {
            params.put("hospital_reg_num", currentMedical.getHospital_reg_number());
            params.put("appointmentID",serverObjects.getAppointmentID() );
            params.put("cancelledByWhom", serverObjects.getCancelledByWhom());
            params.put("cancelledPersonID", serverObjects.getCancelledPersonID());
            params.put("cancelReason", serverObjects.getCancelReason());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.cancelAppointment(currentMedical.getAccessToken(), gsonObject), "cancel");
    }
    public void reschuduleAppointment(AppointmentModel serverObjects) {
        Log.e("reschuduleAppointment", "call");
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        JSONObject params = new JSONObject();
        try {
            params.put("hospital_reg_num", currentMedical.getHospital_reg_number());
            params.put("appointmentDate",serverObjects.getAppointmentDate() );
            params.put("appointmentTimeFrom", serverObjects.getAppointmentTimeFrom());
            params.put("appointmentTimeTo", serverObjects.getAppointmentTimeTo());
            params.put("appointmentID", serverObjects.getAppointmentID());
            params.put("visitType", serverObjects.getVisitType());
            params.put("creatorName", serverObjects.getCreatorName());
            params.put("createrMedicalPersonnelID", serverObjects.getCreaterMedicalPersonnelID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.reschuduleAppointment(currentMedical.getAccessToken(), gsonObject), "reschudule");
    }
    public void processAppointmentdata(AppointmentServerObjects serverObjects,ArrayList<TrackingServerObject> trackArray){
       AppointmentModel appointmentModel=new AppointmentModel();
        appointmentModel.setHospital_reg_num(PatientProfileDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
        appointmentModel.setAppointmentDate(serverObjects.getAppointmentDate());
        appointmentModel.setAppointmentTimeFrom(serverObjects.getAppointmentTimeFrom());
        appointmentModel.setAppointmentTimeTo(serverObjects.getAppointmentTimeTo());
        appointmentModel.setVisitType(serverObjects.getVisitType());
        appointmentModel.setDoctorName(serverObjects.getDoctorName());
        appointmentModel.setDepartment(serverObjects.getDepartment());
        appointmentModel.setDoctorMedicalPersonnelID(serverObjects.getDoctorMedicalPersonnelID());
        appointmentModel.setPatientName(serverObjects.getPatientName());
        appointmentModel.setPatientID(serverObjects.getPatientID());
        appointmentModel.setReasonForVisit(serverObjects.getReasonForVisit());
        appointmentModel.setMedicalRecordID(serverObjects.getMedicalRecordID());
        appointmentModel.setCreatorName(serverObjects.getCreatorName());
        appointmentModel.setCreaterMedicalPersonnelID(serverObjects.getCreaterMedicalPersonnelID());
        appointmentModel.setAppointmentID(serverObjects.getAppointmentID());
        appointmentModel.setAppointmentStatus(serverObjects.getAppointmentStatus());
        appointmentModel.setPatientlProfileModel(PatientProfileDataController.getInstance().currentPatientlProfile);
        appointmentModel.setDoctorProfilePic(ServerApi.img_home_url +serverObjects.getDoctorProfilePic());
        if( AppointmentDataController.getInstance().insertAppointmentData(appointmentModel)){
           // AppointmentDataController.getInstance().fetchAppointmentData(PatientProfileDataController.getInstance().currentPatientlProfile);
        }
    }
}
