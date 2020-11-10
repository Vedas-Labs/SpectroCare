package com.vedas.spectrocare.model;

import com.google.gson.annotations.SerializedName;
import com.vedas.spectrocare.ServerApiModel.TrackingServerObject;

import java.util.List;

public class GetPatientsResponseModel {
    private String response;
    private String password;
    private String userID;
    private String accessToken;
    private List<PatientList> patientsInfo;


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


    public PatientList getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(PatientList patientInfo) {
        this.patientInfo = patientInfo;
    }

    private PatientList patientInfo;
    private String message;

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    public List<PatientList> getPatientsInfo() {
        return patientsInfo;
    }

    public void setPatientsInfo(List<PatientList> patientsInfo) {
        this.patientsInfo = patientsInfo;
    }
    /*public GetPatientsResponseModel(String response, List<PatientList> general_info) {
        this.response = response;
        this.patientsInfo = general_info;
    }*/
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
