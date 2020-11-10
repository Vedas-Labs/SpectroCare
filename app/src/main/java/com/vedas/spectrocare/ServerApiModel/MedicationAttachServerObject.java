package com.vedas.spectrocare.ServerApiModel;

import java.io.File;
import java.util.List;

public class MedicationAttachServerObject {
    private File  prescription1;
    private File prescription2;
    private String illnessID;
    private String prescription1MoreDetails;
    private String prescription2MoreDetails1;
    private String doctorMedicalPersonnelID;
    private String doctorName;
    private String response;
    private String illnessMedicationID;
    private String message;
    private String prescription1FilePath;
    private String prescription2FilePath;

    public String getPrescription1FilePath() {
        return prescription1FilePath;
    }

    public void setPrescription1FilePath(String prescription1FilePath) {
        this.prescription1FilePath = prescription1FilePath;
    }

    public String getPrescription2FilePath() {
        return prescription2FilePath;
    }

    public void setPrescription2FilePath(String prescription2FilePath) {
        this.prescription2FilePath = prescription2FilePath;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getIllnessMedicationID() {
        return illnessMedicationID;
    }

    public void setIllnessMedicationID(String illnessMedicationID) {
        this.illnessMedicationID = illnessMedicationID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public File getPrescription1() {
        return prescription1;
    }

    public void setPrescription1(File prescription1) {
        this.prescription1 = prescription1;
    }

    public File getPrescription2() {
        return prescription2;
    }

    public void setPrescription2(File prescription2) {
        this.prescription2 = prescription2;
    }

    public String getIllnessID() {
        return illnessID;
    }

    public void setIllnessID(String illnessID) {
        this.illnessID = illnessID;
    }

    public String getPrescription1MoreDetails() {
        return prescription1MoreDetails;
    }

    public void setPrescription1MoreDetails(String prescription1MoreDetails) {
        this.prescription1MoreDetails = prescription1MoreDetails;
    }

    public String getPrescription2MoreDetails1() {
        return prescription2MoreDetails1;
    }

    public void setPrescription2MoreDetails1(String prescription2MoreDetails1) {
        this.prescription2MoreDetails1 = prescription2MoreDetails1;
    }

    public String getDoctorMedicalPersonnelID() {
        return doctorMedicalPersonnelID;
    }

    public void setDoctorMedicalPersonnelID(String doctorMedicalPersonnelID) {
        this.doctorMedicalPersonnelID = doctorMedicalPersonnelID;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}
