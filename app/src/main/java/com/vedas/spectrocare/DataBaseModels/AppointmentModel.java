package com.vedas.spectrocare.DataBaseModels;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

public class AppointmentModel {
    @DatabaseField(generatedId = true)
    private int Id;

    @DatabaseField
    private String hospital_reg_num;

    @DatabaseField
    private String appointmentDate;

    @DatabaseField
    private String appointmentTimeFrom;

    @DatabaseField
    private String appointmentTimeTo;

    @DatabaseField
    private String visitType;

    @DatabaseField
    private String doctorName;

    @DatabaseField
    private String doctorProfilePic;

    @DatabaseField
    private String doctorMedicalPersonnelID;

    @DatabaseField
    private String patientName;

    @DatabaseField
    private String patientID;

    @DatabaseField
    private String department;

    @DatabaseField
    private String reasonForVisit;

    @DatabaseField
    private String medicalRecordID;

    @DatabaseField
    private String creatorName;

    @DatabaseField
    private String createrMedicalPersonnelID;

    @DatabaseField
    private String appointmentID;

    @DatabaseField
    private String appointmentStatus;

    @DatabaseField
    private String cancelledByWhom;

    @DatabaseField
    private String cancelReason;

    @DatabaseField
    private String cancelledPersonID;

    @DatabaseField(columnName = "pat_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public PatientlProfileModel patientlProfileModel;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getHospital_reg_num() {
        return hospital_reg_num;
    }

    public void setHospital_reg_num(String hospital_reg_num) {
        this.hospital_reg_num = hospital_reg_num;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getDoctorProfilePic() {
        return doctorProfilePic;
    }

    public void setDoctorProfilePic(String doctorProfilePic) {
        this.doctorProfilePic = doctorProfilePic;
    }

    public String getAppointmentTimeFrom() {
        return appointmentTimeFrom;
    }

    public void setAppointmentTimeFrom(String appointmentTimeFrom) {
        this.appointmentTimeFrom = appointmentTimeFrom;
    }

    public String getAppointmentTimeTo() {
        return appointmentTimeTo;
    }

    public void setAppointmentTimeTo(String appointmentTimeTo) {
        this.appointmentTimeTo = appointmentTimeTo;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorMedicalPersonnelID() {
        return doctorMedicalPersonnelID;
    }

    public void setDoctorMedicalPersonnelID(String doctorMedicalPersonnelID) {
        this.doctorMedicalPersonnelID = doctorMedicalPersonnelID;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getReasonForVisit() {
        return reasonForVisit;
    }

    public void setReasonForVisit(String reasonForVisit) {
        this.reasonForVisit = reasonForVisit;
    }

    public String getMedicalRecordID() {
        return medicalRecordID;
    }

    public void setMedicalRecordID(String medicalRecordID) {
        this.medicalRecordID = medicalRecordID;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreaterMedicalPersonnelID() {
        return createrMedicalPersonnelID;
    }

    public void setCreaterMedicalPersonnelID(String createrMedicalPersonnelID) {
        this.createrMedicalPersonnelID = createrMedicalPersonnelID;
    }

    public String getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getCancelledByWhom() {
        return cancelledByWhom;
    }

    public void setCancelledByWhom(String cancelledByWhom) {
        this.cancelledByWhom = cancelledByWhom;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getCancelledPersonID() {
        return cancelledPersonID;
    }

    public void setCancelledPersonID(String cancelledPersonID) {
        this.cancelledPersonID = cancelledPersonID;
    }

    public PatientlProfileModel getPatientlProfileModel() {
        return patientlProfileModel;
    }

    public void setPatientlProfileModel(PatientlProfileModel patientlProfileModel) {
        this.patientlProfileModel = patientlProfileModel;
    }
}
