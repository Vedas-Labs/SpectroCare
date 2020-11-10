package com.vedas.spectrocare.ServerApiModel;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

import java.util.ArrayList;

public class MedicationManullayServerObject {

    // add medication record
   /* call{"response":3,"message":"illness Medication records found",
            "illnessMedicationRecords":[{"illnessID":"IIDmr6Hzy","illnessMedicationID":"IMIDOXlPIg",
            "patientID":"PIDvfnNQ","medical_personnel_id":"MPID8VcW","hospital_reg_num":"AP2317293903",
            "medical_record_id":"MRIDcmfzjD",
            "mannualPrescriptions":{"addedDate":"1582278049738","lastUpdateDate":"1582278049738",
            "doctorMedicalPersonnelID":"DcM2345","doctorName":"Chandu",
            "medicines":[{"name":"ggggg","dosage":"60mg","freq":"0-0-0","purpose":"fever",
            "durationDays":"2","moreDetails":"for headache "}]}}]}*/

    String patientID;
    String medical_personnel_id;
    String medical_record_id;
    String hospital_reg_num;

    @SerializedName("mannualPrescriptions")
    private JsonObject mannualPrescriptions;

    @SerializedName("medicines")
    private JSONArray medicines;

    @SerializedName("attachedPrescriptions")
    private JsonObject attachedPrescriptions;

    @SerializedName("attachments")
    private JSONArray attachments;

    @SerializedName("illnessID")
    String illnessID;
    @SerializedName("doctorMedicalPersonnelID")
    String doctorMedicalPersonnelID;
    @SerializedName("doctorName")
    String doctorName;
    @SerializedName("response")
    private String response;
    @SerializedName("illnessMedicationID")
    String illnessMedicationID;
    @SerializedName("message")
    private  String message;
    @SerializedName("medications")
    private ArrayList<MedicinesRecordServerObject> madications;

    public JsonObject getAttachedPrescriptions() {
        return attachedPrescriptions;
    }

    public void setAttachedPrescriptions(JsonObject attachedPrescriptions) {
        this.attachedPrescriptions = attachedPrescriptions;
    }

    public JSONArray getAttachments() {
        return attachments;
    }

    public void setAttachments(JSONArray attachments) {
        this.attachments = attachments;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getMedical_personnel_id() {
        return medical_personnel_id;
    }

    public void setMedical_personnel_id(String medical_personnel_id) {
        this.medical_personnel_id = medical_personnel_id;
    }

    public String getMedical_record_id() {
        return medical_record_id;
    }

    public void setMedical_record_id(String medical_record_id) {
        this.medical_record_id = medical_record_id;
    }

    public String getHospital_reg_num() {
        return hospital_reg_num;
    }

    public void setHospital_reg_num(String hospital_reg_num) {
        this.hospital_reg_num = hospital_reg_num;
    }

    public JsonObject getMannualPrescriptions() {
        return mannualPrescriptions;
    }

    public void setMannualPrescriptions(JsonObject mannualPrescriptions) {
        this.mannualPrescriptions = mannualPrescriptions;
    }

    public JSONArray getMedicines() {
        return medicines;
    }

    public void setMedicines(JSONArray medicines) {
        this.medicines = medicines;
    }

    public String getIllnessID() {
        return illnessID;
    }

    public void setIllnessID(String illnessID) {
        this.illnessID = illnessID;
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

    public ArrayList<MedicinesRecordServerObject> getMadications() {
        return madications;
    }

    public void setMadications(ArrayList<MedicinesRecordServerObject> madications) {
        this.madications = madications;
    }

  /*  public IllnessMedicationServerObject getIllnessMedicationServerObject() {
        return illnessMedicationServerObject;
    }

    public void setIllnessMedicationServerObject(IllnessMedicationServerObject illnessMedicationServerObject) {
        this.illnessMedicationServerObject = illnessMedicationServerObject;
    }*/
}
