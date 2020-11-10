package com.vedas.spectrocare.PatientAppointmentModule;

import java.io.Serializable;
import java.util.ArrayList;

public class AppointmentDetailsModel implements Serializable {
    ArrayList doctorComments;
    String appointmentID;
    String appointmentDate;
    String appointmentTime;
    String appointmentDuration;
    String visitType;
    String reasonForVisit;
    String appointmentStatus;
    PaymentDetailsModel paymentDetails;

    public ArrayList getDoctorComments() {
        return doctorComments;
    }

    public void setDoctorComments(ArrayList doctorComments) {
        this.doctorComments = doctorComments;
    }

    public String getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentDuration() {
        return appointmentDuration;
    }

    public void setAppointmentDuration(String appointmentDuration) {
        this.appointmentDuration = appointmentDuration;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public String getReasonForVisit() {
        return reasonForVisit;
    }

    public void setReasonForVisit(String reasonForVisit) {
        this.reasonForVisit = reasonForVisit;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public PaymentDetailsModel getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetailsModel paymentDetails) {
        this.paymentDetails = paymentDetails;
    }
}