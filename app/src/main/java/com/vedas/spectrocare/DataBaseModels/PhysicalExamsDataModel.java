package com.vedas.spectrocare.DataBaseModels;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

public class PhysicalExamsDataModel {

    @DatabaseField(generatedId = true)
    private int Id;

    @DatabaseField
    private String patientId;

    @DatabaseField
    private String hospital_reg_number;

    @DatabaseField
    private String medicalPersonId;

    @DatabaseField
    private String medicalRecordId;

    @DatabaseField
    private String physicalExamId;

    @DatabaseField
    private String other;

    @DatabaseField
    private String attachment;

    @DatabaseField
    private String physicianComments;

    @DatabaseField(columnName = "patient_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public PatientlProfileModel patientlProfileModel;

    @ForeignCollectionField
    private ForeignCollection<BMIModel> bmiModels;

    @ForeignCollectionField
    private ForeignCollection<PhysicalCategoriesRecords> physicalCategoriesRecords;

    @ForeignCollectionField
    private ForeignCollection<PhysicalTrackInfoModel> physicalTrackInfoModels;

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

    public String getPhysicianComments() {
        return physicianComments;
    }

    public void setPhysicianComments(String physicianComments) {
        this.physicianComments = physicianComments;
    }

    public ForeignCollection<BMIModel> getBmiModels() {
        return bmiModels;
    }

    public void setBmiModels(ForeignCollection<BMIModel> bmiModels) {
        this.bmiModels = bmiModels;
    }

    public ForeignCollection<PhysicalCategoriesRecords> getPhysicalCategoriesRecords() {
        return physicalCategoriesRecords;
    }

    public void setPhysicalCategoriesRecords(ForeignCollection<PhysicalCategoriesRecords> physicalCategoriesRecords) {
        this.physicalCategoriesRecords = physicalCategoriesRecords;
    }

    public ForeignCollection<PhysicalTrackInfoModel> getPhysicalTrackInfoModels() {
        return physicalTrackInfoModels;
    }

    public void setPhysicalTrackInfoModels(ForeignCollection<PhysicalTrackInfoModel> physicalTrackInfoModels) {
        this.physicalTrackInfoModels = physicalTrackInfoModels;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getHospital_reg_number() {
        return hospital_reg_number;
    }

    public void setHospital_reg_number(String hospital_reg_number) {
        this.hospital_reg_number = hospital_reg_number;
    }

    public String getMedicalPersonId() {
        return medicalPersonId;
    }

    public void setMedicalPersonId(String medicalPersonId) {
        this.medicalPersonId = medicalPersonId;
    }

    public String getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(String medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public String getPhysicalExamId() {
        return physicalExamId;
    }

    public void setPhysicalExamId(String physicalExamId) {
        this.physicalExamId = physicalExamId;
    }

    public PatientlProfileModel getPatientlProfileModel() {
        return patientlProfileModel;
    }

    public void setPatientlProfileModel(PatientlProfileModel patientlProfileModel) {
        this.patientlProfileModel = patientlProfileModel;
    }
}
