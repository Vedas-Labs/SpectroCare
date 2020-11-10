package com.vedas.spectrocare.ServerApiModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FamilyRecordServerObject {
    String medical_personnel_id;
    String hospital_reg_num;
    String patientID;
    String medical_record_id;
    String family_history_record_id;
    String dieseaseName;
    String healthCondition;
    String relationship;
    String age;
    String moreInfo;
    String response;
    String message;

  /*  @SerializedName("family_history_records")
    ArrayList<FamilyRecordServerObject> family_history_records;
*/
  @SerializedName("famliyDiseases")
  ArrayList<FamilyRecordServerObject> family_history_records;

   /* @SerializedName("tracking")
    ArrayList<TrackingServerObject> tracking;*/

    @SerializedName("tracking")
    ArrayList<TrackingServerObject> tracking;

    public ArrayList<FamilyRecordServerObject> getFamily_history_records() {
        return family_history_records;
    }

    public void setFamily_history_records(ArrayList<FamilyRecordServerObject> family_history_records) {
        this.family_history_records = family_history_records;
    }

    public ArrayList<TrackingServerObject> getTracking() {
        return tracking;
    }

    public void setTracking(ArrayList<TrackingServerObject> tracking) {
        this.tracking = tracking;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public String getDieseaseName() {
        return dieseaseName;
    }

    public void setDieseaseName(String dieseaseName) {
        this.dieseaseName = dieseaseName;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public String getMedical_personnel_id() {
        return medical_personnel_id;
    }

    public void setMedical_personnel_id(String medical_personnel_id) {
        this.medical_personnel_id = medical_personnel_id;
    }

    public String getHospital_reg_num() {
        return hospital_reg_num;
    }

    public void setHospital_reg_num(String hospital_reg_num) {
        this.hospital_reg_num = hospital_reg_num;
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

    public String getHealthCondition() {
        return healthCondition;
    }

    public void setHealthCondition(String healthCondition) {
        this.healthCondition = healthCondition;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getFamily_history_record_id() {
        return family_history_record_id;
    }

    public void setFamily_history_record_id(String family_history_record_id) {
        this.family_history_record_id = family_history_record_id;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
