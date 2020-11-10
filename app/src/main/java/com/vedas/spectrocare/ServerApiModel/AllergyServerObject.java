package com.vedas.spectrocare.ServerApiModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AllergyServerObject {
    String medical_personnel_id;
    String hospital_reg_num;
    String patientID;
    String medical_record_id;
    String name;
    String note;
    String allergy_record_id;
    String allergyInformation;

    String response;
    String message;

    @SerializedName("allergy_records")
    ArrayList<AllergyServerObject> allergyRecordtArrayList;

    @SerializedName("tracking")
    ArrayList<TrackingServerObject> tracking;

    public String getAllergy_record_id() {
        return allergy_record_id;
    }

    public void setAllergy_record_id(String allergy_record_id) {
        this.allergy_record_id = allergy_record_id;
    }

    public ArrayList<AllergyServerObject> getAllergyRecordtArrayList() {
        return allergyRecordtArrayList;
    }

    public void setAllergyRecordtArrayList(ArrayList<AllergyServerObject> allergyRecordtArrayList) {
        this.allergyRecordtArrayList = allergyRecordtArrayList;
    }

    public ArrayList<TrackingServerObject> getTracking() {
        return tracking;
    }

    public void setTracking(ArrayList<TrackingServerObject> tracking) {
        this.tracking = tracking;
    }



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

    public String getMedical_record_id() {
        return medical_record_id;
    }

    public void setMedical_record_id(String medical_record_id) {
        this.medical_record_id = medical_record_id;
    }

    public String getAllergyInformation() {
        return allergyInformation;
    }

    public void setAllergyInformation(String allergyInformation) {
        this.allergyInformation = allergyInformation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

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
}
