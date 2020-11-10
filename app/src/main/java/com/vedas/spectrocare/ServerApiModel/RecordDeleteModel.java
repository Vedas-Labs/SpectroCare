package com.vedas.spectrocare.ServerApiModel;

import com.google.gson.annotations.SerializedName;

public class RecordDeleteModel {
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
    @SerializedName("medical_record_id")
    private String medical_record_id;

    public String getMedical_personnel_id() {
        return medical_personnel_id;
    }

    public void setMedical_personnel_id(String medical_personnel_id) {
        this.medical_personnel_id = medical_personnel_id;
    }

    public String getHospital_reg_num() {
        return hospital_reg_num;
    }

    public void setHospital_reg_num(String hospital_reg_num) {
        this.hospital_reg_num = hospital_reg_num;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
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

    public String getMedical_record_id() {
        return medical_record_id;
    }

    public void setMedical_record_id(String medical_record_id) {
        this.medical_record_id = medical_record_id;
    }
}
