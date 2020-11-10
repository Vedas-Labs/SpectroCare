package com.vedas.spectrocare.ServerApiModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VaccineServerObject {
    String medical_personnel_id;
    String hospital_reg_num;
    String patientID;
    String medical_record_id;

    String immunizationName;
    String immunizationDate;
    String notes;
    String immunization_record_id;

    String response;
    String message;

    @SerializedName("immunization_records")
        ArrayList<VaccineServerObject> allergyRecordtArrayList;

    @SerializedName("tracking")
    ArrayList<TrackingServerObject> tracking;

    public String getImmunizationName() {
        return immunizationName;
    }

    public void setImmunizationName(String immunizationName) {
        this.immunizationName = immunizationName;
    }

    public String getImmunizationDate() {
        return immunizationDate;
    }

    public void setImmunizationDate(String immunizationDate) {
        this.immunizationDate = immunizationDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getImmunization_record_id() {
        return immunization_record_id;
    }

    public void setImmunization_record_id(String immunization_record_id) {
        this.immunization_record_id = immunization_record_id;
    }

    public ArrayList<VaccineServerObject> getAllergyRecordtArrayList() {
        return allergyRecordtArrayList;
    }

    public void setAllergyRecordtArrayList(ArrayList<VaccineServerObject> allergyRecordtArrayList) {
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
