package com.vedas.spectrocare.PatientServerApiModel;

import com.vedas.spectrocare.ServerApiModel.BodyIndexServerObject;
import com.vedas.spectrocare.ServerApiModel.PhysicalRecordServerObject;

import java.util.ArrayList;

public class PatientPhysicalModel {
    String patientID;
    String medical_record_id;
    String physical_exam_id;
    String hospital_reg_num;
    String attachment;
    String other;
    String physicianCommentsOrRecomdations;
    String createdDate;
    String updatedDate;
    ArrayList<PhysicalRecordServerObject> physicalExamination;
    BodyIndexServerObject bodyIndex;
    ArrayList<FamilyTrackingInfo> tracking;

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

    public String getPhysical_exam_id() {
        return physical_exam_id;
    }

    public void setPhysical_exam_id(String physical_exam_id) {
        this.physical_exam_id = physical_exam_id;
    }

    public String getHospital_reg_num() {
        return hospital_reg_num;
    }

    public void setHospital_reg_num(String hospital_reg_num) {
        this.hospital_reg_num = hospital_reg_num;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getPhysicianCommentsOrRecomdations() {
        return physicianCommentsOrRecomdations;
    }

    public void setPhysicianCommentsOrRecomdations(String physicianCommentsOrRecomdations) {
        this.physicianCommentsOrRecomdations = physicianCommentsOrRecomdations;
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

    public ArrayList<PhysicalRecordServerObject> getPhysicalExamination() {
        return physicalExamination;
    }

    public void setPhysicalExamination(ArrayList<PhysicalRecordServerObject> physicalExamination) {
        this.physicalExamination = physicalExamination;
    }

    public BodyIndexServerObject getBodyIndex() {
        return bodyIndex;
    }

    public void setBodyIndex(BodyIndexServerObject bodyIndex) {
        this.bodyIndex = bodyIndex;
    }

    public ArrayList<FamilyTrackingInfo> getTracking() {
        return tracking;
    }

    public void setTracking(ArrayList<FamilyTrackingInfo> tracking) {
        this.tracking = tracking;
    }
}
