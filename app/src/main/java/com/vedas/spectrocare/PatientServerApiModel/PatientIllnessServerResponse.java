package com.vedas.spectrocare.PatientServerApiModel;

import java.util.ArrayList;

public class PatientIllnessServerResponse {
    int response;
    String message;
    ArrayList<IllnessPatientRecord> illnessRecords;

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<IllnessPatientRecord> getIllnessRecords() {
        return illnessRecords;
    }

    public void setIllnessRecords(ArrayList<IllnessPatientRecord> illnessRecords) {
        this.illnessRecords = illnessRecords;
    }
}
