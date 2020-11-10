package com.vedas.spectrocare.LoginResponseModel;

public class PatientLoginResponseModel {
    int response;
    PatientInfoModel patientInfo;
    String accessToken;

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public PatientInfoModel getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(PatientInfoModel patientInfo) {
        this.patientInfo = patientInfo;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
