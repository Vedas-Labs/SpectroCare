package com.vedas.spectrocare.ServerApiModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MedicalRecordModel {
    @SerializedName("medical_personnel_id")
    private String medical_personnel_id;
    @SerializedName("hospital_reg_num")
    private String hospital_reg_num;
    @SerializedName("patientID")
    private String patientID;
    @SerializedName("medical_record_id")
    private String medical_record_id;

    @SerializedName("bodyIndex")
    private BodyIndexServerObject bodyIndex;


    @SerializedName("familyHistory")
    private ArrayList<FamilyHistory> familyHistory;

    @SerializedName("physicalExamination")
    private ArrayList<PhysicalRecordServerObject> physicalExamination;

    @SerializedName("vaccination")
    private ArrayList<Vaccination> vaccination;

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

    public BodyIndexServerObject getBodyIndex() {
        return bodyIndex;
    }

    public void setBodyIndex(BodyIndexServerObject bodyIndex) {
        this.bodyIndex = bodyIndex;
    }

    public ArrayList<FamilyHistory> getFamilyHistory() {
        return familyHistory;
    }

    public void setFamilyHistory(ArrayList<FamilyHistory> familyHistory) {
        this.familyHistory = familyHistory;
    }

    public ArrayList<PhysicalRecordServerObject> getPhysicalExamination() {
        return physicalExamination;
    }

    public void setPhysicalExamination(ArrayList<PhysicalRecordServerObject> physicalExamination) {
        this.physicalExamination = physicalExamination;
    }

    public ArrayList<Vaccination> getVaccination() {
        return vaccination;
    }

    public void setVaccination(ArrayList<Vaccination> vaccination) {
        this.vaccination = vaccination;
    }
}
