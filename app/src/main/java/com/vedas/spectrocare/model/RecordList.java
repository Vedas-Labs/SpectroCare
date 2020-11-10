package com.vedas.spectrocare.model;

public class RecordList {
    String medical_record_id;
    String modified;
    String Created;
    String patientID;

    public RecordList(String medical_record_id, String modified, String created,String patientID) {
        this.medical_record_id = medical_record_id;
        this.modified = modified;
        this.patientID = patientID;
        Created = created;
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

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getCreated() {
        return Created;
    }

    public void setCreated(String created) {
        Created = created;
    }
}
