package com.vedas.spectrocare.ServerApiModel;

public class MedicalRecordResponse {
    String response;
    String medicalRecordId;
    String message;

    public MedicalRecordResponse(String response, String medicalRecordId, String message) {
        this.response = response;
        this.medicalRecordId = medicalRecordId;
        this.message = message;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(String medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
