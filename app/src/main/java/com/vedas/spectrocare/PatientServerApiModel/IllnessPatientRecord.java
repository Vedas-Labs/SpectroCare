package com.vedas.spectrocare.PatientServerApiModel;


import java.util.ArrayList;

public class IllnessPatientRecord {
    public boolean isCurrentIllness;
    ArrayList<FamilyTrackingInfo> tracking;
    String illnessID;
    String patientID;
    String hospital_reg_num;
    String medical_record_id;
    String illnessCondition;
    String symptoms;
    String currentStatus;
    String description;
    String startDate;
    String endDate;
    String addedDate;
    String updatedDate;

    public boolean isCurrentIllness() {
        return isCurrentIllness;
    }

    public void setCurrentIllness(boolean currentIllness) {
        isCurrentIllness = currentIllness;
    }

    public ArrayList<FamilyTrackingInfo> getTracking() {
        return tracking;
    }

    public void setTracking(ArrayList<FamilyTrackingInfo> tracking) {
        this.tracking = tracking;
    }

    public String getIllnessID() {
        return illnessID;
    }

    public void setIllnessID(String illnessID) {
        this.illnessID = illnessID;
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

    public String getMedical_record_id() {
        return medical_record_id;
    }

    public void setMedical_record_id(String medical_record_id) {
        this.medical_record_id = medical_record_id;
    }

    public String getIllnessCondition() {
        return illnessCondition;
    }

    public void setIllnessCondition(String illnessCondition) {
        this.illnessCondition = illnessCondition;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
}
