package com.vedas.spectrocare.model;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.Serializable;

public class AppointmetModel implements Serializable {
    int docImg;
    String docName;
    String date;
    String time;
    String approve;
    String timeSlot;
    String reason;
    String paymentStatus;
    String paymentMethod;
    String cardNo;
    String appointmentStatus;
    String specialization;
    String doctorID;

    public AppointmetModel(){}

    public AppointmetModel(int docImg, String docName, String date, String time, String approve,
                           String timeSlot, String reason, String paymentStatus, String paymentMethod,
                           String specialization) {
        this.docImg = docImg;
        this.docName = docName;
        this.date = date;
        this.time = time;
        this.approve = approve;
        this.timeSlot = timeSlot;
        this.reason = reason;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.specialization = specialization;
    }

    public AppointmetModel(int docImg, String docName, String date, String time, String approve) {
        this.docImg = docImg;
        this.docName = docName;
        this.date = date;
        this.time = time;
        this.approve = approve;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public int getDocImg() {
        return docImg;
    }

    public void setDocImg(int docImg) {
        this.docImg = docImg;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getApprove() {
        return approve;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setApprove(String approve) {
        this.approve = approve;
    }
}
