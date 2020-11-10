package com.vedas.spectrocare.PatientServerApiModel;

import java.util.ArrayList;

public class PatientMedicationResponsModel {
    private String response;
    private String message;

    private ArrayList<IllnessMedicationRecords> illnessMedicationRecords;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<IllnessMedicationRecords> getIllnessMedicationRecords() {
        return illnessMedicationRecords;
    }

    public void setIllnessMedicationRecords(ArrayList<IllnessMedicationRecords> illnessMedicationRecords) {
        this.illnessMedicationRecords = illnessMedicationRecords;
    }

}
