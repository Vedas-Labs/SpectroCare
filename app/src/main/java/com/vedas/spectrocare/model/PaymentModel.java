package com.vedas.spectrocare.model;

public class PaymentModel {
    String docName;
    String docID;
    String docProfession;
    String appointmentType;
    String reasonForVisit;
    String formattedDate;
    String duration;
    String timeSlot;
    String serviceCost;
    String currency;
    String serviceVATPercent;
    String serviceID;

    /* "isAvailable": "true",
         "serviceID": "SIDhoTgdFyw",
         "adminUserID": "Viswanath3344",
         "hospital_reg_num": "AP2317293903",
         "serviceName": "Online Consultation - 30 Mins",
         "serviceCost": "20",
         "currency": "USD",
         "serviceVATPercent": "10",
         "createdDate": "1604986963440",
         "updatedDate": "1604986963440"*/
    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocProfession() {
        return docProfession;
    }

    public void setDocProfession(String docProfession) {
        this.docProfession = docProfession;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public String getReasonForVisit() {
        return reasonForVisit;
    }

    public void setReasonForVisit(String reasonForVisit) {
        this.reasonForVisit = reasonForVisit;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(String serviceCost) {
        this.serviceCost = serviceCost;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getServiceVATPercent() {
        return serviceVATPercent;
    }

    public void setServiceVATPercent(String serviceVATPercent) {
        this.serviceVATPercent = serviceVATPercent;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }
}
