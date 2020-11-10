package com.vedas.spectrocare.PatientServerApiModel;

public class ImmunizationObject {
    private String hospital_reg_num;
    private String immunizationName;
    private String immunizationDate;
    private String notes;
    private String byWhom;
    private String byWhomID;
    private String patientID;
    private String startDate;
    private String time;
    private String medical_record_id;
    private String doctorMPID;
    private String doctorName;
    private String immunization_record_id;
    private String createdDate;
    private String updatedDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getImmunization_record_id() {
        return immunization_record_id;
    }

    public void setImmunization_record_id(String immunization_record_id) {
        this.immunization_record_id = immunization_record_id;
    }

    public String getHospital_reg_num() {
        return hospital_reg_num;
    }

    public void setHospital_reg_num(String hospital_reg_num) {
        this.hospital_reg_num = hospital_reg_num;
    }

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

    public String getDoctorMPID() {
        return doctorMPID;
    }

    public void setDoctorMPID(String doctorMPID) {
        this.doctorMPID = doctorMPID;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}
