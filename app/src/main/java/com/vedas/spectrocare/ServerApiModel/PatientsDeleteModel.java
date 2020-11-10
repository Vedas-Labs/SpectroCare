package com.vedas.spectrocare.ServerApiModel;

import com.google.gson.annotations.SerializedName;

public class PatientsDeleteModel {
    @SerializedName("medical_personnel_id")
    private String medical_personnel_id;
    @SerializedName("hospital_reg_num")
    private String hospital_reg_num;
    @SerializedName("patientID")
    private String patientID;
    @SerializedName("message")
    private String message;
    @SerializedName("response")
    private String response;

    public String getMedical_personnel_id() {
        return medical_personnel_id;
    }

    public void setMedical_personnel_id(String medical_personnel_id) {
        this.medical_personnel_id = medical_personnel_id;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getHospital_reg_num() {
        return hospital_reg_num;
    }

    public void setHospital_reg_num(String hospital_reg_num) {
        this.hospital_reg_num = hospital_reg_num;
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
}
