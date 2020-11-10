package com.vedas.spectrocare.ServerApiModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ScreeningServerObject {
    String illnessScreeningID;
    String recordFilePath;
    String illnessID;
    String filePath;
    String response;
    String message;

    String patientID;
    String recordMoreDetails;
    String recordName;
    String byWhom;
    String byWhomName;
    String byWhomType;
    String byWhomID;
    String medical_record_id;
    String medical_personnel_id;
    String screeningRecord;

    @SerializedName("illnessScreeningRecords")
    ArrayList<ScreeningServerObject> screeningServerObjects;

    @SerializedName("tracking")
    ArrayList<TrackingServerObject> tracking;

    public String getByWhom() {
        return byWhom;
    }

    public void setByWhom(String byWhom) {
        this.byWhom = byWhom;
    }

    public String getMedical_personnel_id() {
        return medical_personnel_id;
    }

    public void setMedical_personnel_id(String medical_personnel_id) {
        this.medical_personnel_id = medical_personnel_id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getRecordMoreDetails() {
        return recordMoreDetails;
    }

    public void setRecordMoreDetails(String recordMoreDetails) {
        this.recordMoreDetails = recordMoreDetails;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getByWhomName() {
        return byWhomName;
    }

    public void setByWhomName(String byWhomName) {
        this.byWhomName = byWhomName;
    }

    public String getByWhomType() {
        return byWhomType;
    }

    public void setByWhomType(String byWhomType) {
        this.byWhomType = byWhomType;
    }

    public String getByWhomID() {
        return byWhomID;
    }

    public void setByWhomID(String byWhomID) {
        this.byWhomID = byWhomID;
    }

    public String getMedical_record_id() {
        return medical_record_id;
    }

    public void setMedical_record_id(String medical_record_id) {
        this.medical_record_id = medical_record_id;
    }

    public String getScreeningRecord() {
        return screeningRecord;
    }

    public void setScreeningRecord(String screeningRecord) {
        this.screeningRecord = screeningRecord;
    }

    public String getIllnessScreeningID() {
        return illnessScreeningID;
    }

    public void setIllnessScreeningID(String illnessScreeningID) {
        this.illnessScreeningID = illnessScreeningID;
    }

    public String getRecordFilePath() {
        return recordFilePath;
    }

    public void setRecordFilePath(String recordFilePath) {
        this.recordFilePath = recordFilePath;
    }

    public String getIllnessID() {
        return illnessID;
    }

    public void setIllnessID(String illnessID) {
        this.illnessID = illnessID;
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

    public ArrayList<ScreeningServerObject> getScreeningServerObjects() {
        return screeningServerObjects;
    }

    public void setScreeningServerObjects(ArrayList<ScreeningServerObject> screeningServerObjects) {
        this.screeningServerObjects = screeningServerObjects;
    }

    public ArrayList<TrackingServerObject> getTracking() {
        return tracking;
    }

    public void setTracking(ArrayList<TrackingServerObject> tracking) {
        this.tracking = tracking;
    }
}
