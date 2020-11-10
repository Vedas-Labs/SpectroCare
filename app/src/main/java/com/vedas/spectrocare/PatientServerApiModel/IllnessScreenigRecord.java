package com.vedas.spectrocare.PatientServerApiModel;

import java.util.ArrayList;

public class IllnessScreenigRecord {
    ArrayList<FamilyTrackingInfo> tracking;
    String patientID;
    String medical_record_id;
    String hospital_reg_num;
    String illnessScreeningID;
    String recordName;
    String recordFilePath;
    String recordMoreDetails;
    String addedDate;

    public ArrayList<FamilyTrackingInfo> getTracking() {
        return tracking;
    }

    public void setTracking(ArrayList<FamilyTrackingInfo> tracking) {
        this.tracking = tracking;
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

    public String getHospital_reg_num() {
        return hospital_reg_num;
    }

    public void setHospital_reg_num(String hospital_reg_num) {
        this.hospital_reg_num = hospital_reg_num;
    }

    public String getIllnessScreeningID() {
        return illnessScreeningID;
    }

    public void setIllnessScreeningID(String illnessScreeningID) {
        this.illnessScreeningID = illnessScreeningID;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getRecordFilePath() {
        return recordFilePath;
    }

    public void setRecordFilePath(String recordFilePath) {
        this.recordFilePath = recordFilePath;
    }

    public String getRecordMoreDetails() {
        return recordMoreDetails;
    }

    public void setRecordMoreDetails(String recordMoreDetails) {
        this.recordMoreDetails = recordMoreDetails;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }
}
