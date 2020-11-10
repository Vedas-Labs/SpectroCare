package com.vedas.spectrocare.model;

public class MedicalRecordModel {
    private String medical_personnel_id;
    private String hospital_reg_num;
    private String patientID;


    public MedicalRecordModel(String medical_personnel_id, String hospital_reg_num, String patientID) {
        this.medical_personnel_id = medical_personnel_id;
        this.hospital_reg_num = hospital_reg_num;
        this.patientID = patientID;
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
}
