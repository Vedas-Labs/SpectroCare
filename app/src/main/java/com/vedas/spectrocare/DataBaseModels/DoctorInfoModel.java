package com.vedas.spectrocare.DataBaseModels;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

public class DoctorInfoModel {
    @DatabaseField(generatedId = true)
    private int Id;

    @DatabaseField
    private String emailID;

    @DatabaseField
    private String doctorName;

    @DatabaseField
    private String medicalPersonId;

    @DatabaseField
    private String hospitalRegNum;

    @DatabaseField
    private String department;

    @DatabaseField
    private String specialization;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getMedicalPersonId() {
        return medicalPersonId;
    }

    public void setMedicalPersonId(String medicalPersonId) {
        this.medicalPersonId = medicalPersonId;
    }

    public String getHospitalRegNum() {
        return hospitalRegNum;
    }

    public void setHospitalRegNum(String hospitalRegNum) {
        this.hospitalRegNum = hospitalRegNum;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
