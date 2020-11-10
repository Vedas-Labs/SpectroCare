package com.vedas.spectrocare.ServerApiModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AppointmentServerObjects {
    String hospital_reg_num;
    String appointmentDate;
    String appointmentTimeFrom;
    String appointmentTimeTo;
    String visitType;
    String doctorName;
    String doctorMedicalPersonnelID;
    String patientName;//"patientName":
    String patientID;
    String department;
    String reasonForVisit;//"reasonForVisit":
    String medicalRecordID;
    String creatorName;
    String createrMedicalPersonnelID;
    // getting response
    String appointmentID;
    String appointmentStatus;
    // for cancel appointment
    String cancelledByWhom;
    String cancelReason;
    String cancelledPersonID;
    String doctorProfilePic;

    public String getDoctorProfilePic() {
        return doctorProfilePic;
    }

    public void setDoctorProfilePic(String doctorProfilePic) {
        this.doctorProfilePic = doctorProfilePic;
    }

    ArrayList<TrackingServerObject> tracking;

    public ArrayList<TrackingServerObject> getTracking() {
        return tracking;
    }

    public void setTracking(ArrayList<TrackingServerObject> tracking) {
        this.tracking = tracking;
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
}
