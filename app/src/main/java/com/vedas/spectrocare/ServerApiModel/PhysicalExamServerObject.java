package com.vedas.spectrocare.ServerApiModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PhysicalExamServerObject {

    @SerializedName("medical_personnel_id")
    private String medical_personnel_id;

    @SerializedName("hospital_reg_num")
    private  String hospital_reg_num;

    @SerializedName("patientID")
    String patientID;

    @SerializedName("medical_record_id")
    private String medical_record_id;

    @SerializedName("physical_exam_id")
    private  String physical_exam_id;

    @SerializedName("byWhom")
    private  String byWhom;

    @SerializedName("byWhomID")
    private  String byWhomID;


    @SerializedName("response")
    private String response;

    @SerializedName("message")
    private  String message;

    @SerializedName("other")
    private  String other;

    @SerializedName("attachment")
    private  String attachment;

    @SerializedName("physicianCommentsOrRecomdations")
    private  String physicianCommentsOrRecomdations;

    @SerializedName("bodyIndex")
    private BodyIndexServerObject bodyIndexArrayList;

    @SerializedName("physicalExamination")
    private ArrayList<PhysicalRecordServerObject> physicalExaminations;

    //for fetching data

    @SerializedName("createdDate")
    private  String createdDate;

    @SerializedName("updatedDate")
    private  String updatedDate;

    @SerializedName("physical_exam_records")
    private ArrayList<PhysicalExamServerObject> physical_exam_records;

    @SerializedName("tracking")
    private ArrayList<TrackingServerObject> trackingServerObjects;

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public ArrayList<TrackingServerObject> getTrackingServerObjects() {
        return trackingServerObjects;
    }

    public void setTrackingServerObjects(ArrayList<TrackingServerObject> trackingServerObjects) {
        this.trackingServerObjects = trackingServerObjects;
    }

    public ArrayList<PhysicalExamServerObject> getPhysical_exam_records() {
        return physical_exam_records;
    }

    public String getByWhom() {
        return byWhom;
    }

    public void setByWhom(String byWhom) {
        this.byWhom = byWhom;
    }

    public String getByWhomID() {
        return byWhomID;
    }

    public void setByWhomID(String byWhomID) {
        this.byWhomID = byWhomID;
    }

    public void setPhysical_exam_records(ArrayList<PhysicalExamServerObject> physical_exam_records) {
        this.physical_exam_records = physical_exam_records;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getPhysicianCommentsOrRecomdations() {
        return physicianCommentsOrRecomdations;
    }

    public void setPhysicianCommentsOrRecomdations(String physicianCommentsOrRecomdations) {
        this.physicianCommentsOrRecomdations = physicianCommentsOrRecomdations;
    }

    public BodyIndexServerObject getBodyIndexArrayList() {
        return bodyIndexArrayList;
    }

    public void setBodyIndexArrayList(BodyIndexServerObject bodyIndexArrayList) {
        this.bodyIndexArrayList = bodyIndexArrayList;
    }

    public String getPhysical_exam_id() {
        return physical_exam_id;
    }

    public void setPhysical_exam_id(String physical_exam_id) {
        this.physical_exam_id = physical_exam_id;
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

    public ArrayList<PhysicalRecordServerObject> getPhysicalExaminations() {
        return physicalExaminations;
    }

    public void setPhysicalExaminations(ArrayList<PhysicalRecordServerObject> physicalExaminations) {
        this.physicalExaminations = physicalExaminations;
    }
}
