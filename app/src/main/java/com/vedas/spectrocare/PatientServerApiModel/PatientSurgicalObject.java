package com.vedas.spectrocare.PatientServerApiModel;

import com.google.gson.annotations.SerializedName;
import com.vedas.spectrocare.ServerApiModel.AttachmentServerObjects;
import com.vedas.spectrocare.ServerApiModel.VaccineServerObject;

import java.util.ArrayList;

public class PatientSurgicalObject {

    String response;
    String message;
    String filePath;
    String PatientID;
    String medical_record_id;
    String medical_personnel_id;
    String hospital_reg_num;
    String addedDate;
    String lastUpdatedDate;
    String surgeryProcedure;
    String surgeryDate;
    String surgeryInformation;

    String illnessID;
    String moreDetails;
    String illnessSurgicalID;
    String doctorMedicalPersonnelId;
    String doctorName;

    public String getSurgeryProcedure() {
        return surgeryProcedure;
    }

    public void setSurgeryProcedure(String surgeryProcedure) {
        this.surgeryProcedure = surgeryProcedure;
    }

    @SerializedName("illnessSurgicalRecords")
    ArrayList<VaccineServerObject> surgicalRecordArrayList;

    @SerializedName("attachments")
    ArrayList<AttachmentServerObjects> attachmentLis;

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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getPatientID() {
        return PatientID;
    }

    public void setPatientID(String patientID) {
        PatientID = patientID;
    }

    public String getMedical_record_id() {
        return medical_record_id;
    }

    public void setMedical_record_id(String medical_record_id) {
        this.medical_record_id = medical_record_id;
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

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getSurgeryDate() {
        return surgeryDate;
    }

    public void setSurgeryDate(String surgeryDate) {
        this.surgeryDate = surgeryDate;
    }

    public String getSurgeryInformation() {
        return surgeryInformation;
    }

    public void setSurgeryInformation(String surgeryInformation) {
        this.surgeryInformation = surgeryInformation;
    }

    public String getIllnessID() {
        return illnessID;
    }

    public void setIllnessID(String illnessID) {
        this.illnessID = illnessID;
    }

    public String getMoreDetails() {
        return moreDetails;
    }

    public void setMoreDetails(String moreDetails) {
        this.moreDetails = moreDetails;
    }

    public String getIllnessSurgicalID() {
        return illnessSurgicalID;
    }

    public void setIllnessSurgicalID(String illnessSurgicalID) {
        this.illnessSurgicalID = illnessSurgicalID;
    }

    public String getDoctorMedicalPersonnelId() {
        return doctorMedicalPersonnelId;
    }

    public void setDoctorMedicalPersonnelId(String doctorMedicalPersonnelId) {
        this.doctorMedicalPersonnelId = doctorMedicalPersonnelId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public ArrayList<VaccineServerObject> getSurgicalRecordArrayList() {
        return surgicalRecordArrayList;
    }

    public void setSurgicalRecordArrayList(ArrayList<VaccineServerObject> surgicalRecordArrayList) {
        this.surgicalRecordArrayList = surgicalRecordArrayList;
    }

    public ArrayList<AttachmentServerObjects> getAttachmentLis() {
        return attachmentLis;
    }

    public void setAttachmentLis(ArrayList<AttachmentServerObjects> attachmentLis) {
        this.attachmentLis = attachmentLis;
    }
}
