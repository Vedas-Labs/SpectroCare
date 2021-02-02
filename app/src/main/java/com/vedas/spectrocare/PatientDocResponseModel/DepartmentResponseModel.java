package com.vedas.spectrocare.PatientDocResponseModel;

import java.util.ArrayList;

public class DepartmentResponseModel {
    int response;
    String message;
    ArrayList<MedicalPersonnelModel> medicalPersonnels;

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

    public ArrayList<MedicalPersonnelModel> getMedicalPersonnels() {
        return medicalPersonnels;
    }

    public void setMedicalPersonnels(ArrayList<MedicalPersonnelModel> medicalPersonnels) {
        this.medicalPersonnels = medicalPersonnels;
    }
}
