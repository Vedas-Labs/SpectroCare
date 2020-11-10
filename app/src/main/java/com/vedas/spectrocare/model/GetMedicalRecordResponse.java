package com.vedas.spectrocare.model;

import java.util.List;

public class GetMedicalRecordResponse {
    private String response;
    private String message;
    private List<RecordList> medical_records;

    public GetMedicalRecordResponse(String response, String message,List<RecordList> medical_records) {
        this.response = response;
        this.message = message;
        this.medical_records = medical_records;
    }

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

    public List<RecordList> getMedical_records() {
        return medical_records;
    }

    public void setMedical_records(List<RecordList> medical_records) {
        this.medical_records = medical_records;
    }
}
