package com.vedas.spectrocare.model;

import java.util.List;

public class RecordResponseModel {
    String response;
    List medical_records;

    public RecordResponseModel(String response, List medical_records) {
        this.response = response;
        this.medical_records = medical_records;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List getMedical_records() {
        return medical_records;
    }

    public void setMedical_records(List medical_records) {
        this.medical_records = medical_records;
    }
}
