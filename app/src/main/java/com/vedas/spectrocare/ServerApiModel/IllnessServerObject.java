package com.vedas.spectrocare.ServerApiModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class IllnessServerObject {
    String medical_personnel_id;
    String hospital_reg_num;
    String patientID;
    String medical_record_id;

    String illnessID;
    String isCurrentIllness;
    String symptoms;
    String currentStatus;
    String description;
    String addedDate;
    String updatedDate;
    String endDate;
    String startDate;

    String response;
    String message;

    @SerializedName("illnessRecords")
    ArrayList<IllnessServerObject> illnessServerObjects;

    @SerializedName("tracking")
    ArrayList<TrackingServerObject> tracking;

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

    public String getIllnessID() {
        return illnessID;
    }

    public void setIllnessID(String illnessID) {
        this.illnessID = illnessID;
    }

    public String getIsCurrentIllness() {
        return isCurrentIllness;
    }

    public void setIsCurrentIllness(String isCurrentIllness) {
        this.isCurrentIllness = isCurrentIllness;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getMoreInfo() {
        return description;
    }

    public void setMoreInfo(String moreInfo) {
        this.description = moreInfo;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
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

    public ArrayList<IllnessServerObject> getIllnessServerObjects() {
        return illnessServerObjects;
    }

    public void setIllnessServerObjects(ArrayList<IllnessServerObject> illnessServerObjects) {
        this.illnessServerObjects = illnessServerObjects;
    }

    public ArrayList<TrackingServerObject> getTracking() {
        return tracking;
    }

    public void setTracking(ArrayList<TrackingServerObject> tracking) {
        this.tracking = tracking;
    }
}
