package com.vedas.spectrocare.PatientServerApiModel;
import com.vedas.spectrocare.PatientDocResponseModel.TrackingModel;

import java.util.ArrayList;

public class AllergyObject {
    private String  hospital_reg_num;
    private String  byWhom;
    private String  byWhomID;
    private String  patientID;
    private String  medical_record_id;
    private String  createdDate;
    private String  updatedDate;
    private String  allergy_record_id;
    private ArrayList<AllergyListObject> allergies;
    private  ArrayList<TrackingModel> trackingList;

    public ArrayList<TrackingModel> getTrackingList() {
        return trackingList;
    }

    public void setTrackingList(ArrayList<TrackingModel> trackingList) {
        this.trackingList = trackingList;
    }

    public String getAllergy_record_id() {
        return allergy_record_id;
    }

    public void setAllergy_record_id(String allergy_record_id) {
        this.allergy_record_id = allergy_record_id;
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


    public String getHospital_reg_num() {
        return hospital_reg_num;
    }

    public void setHospital_reg_num(String hospital_reg_num) {
        this.hospital_reg_num = hospital_reg_num;
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

    public ArrayList<AllergyListObject> getAllergies() {
        return allergies;
    }

    public void setAllergies(ArrayList<AllergyListObject> allergies) {
        this.allergies = allergies;
    }
}


