package com.vedas.spectrocare.DataBaseModels;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "allergy")
public class AllergieModel {

    @DatabaseField(generatedId = true)
    private int Id;

    @DatabaseField
    private String patientId;

    @DatabaseField
    private String medicalRecordId;

    @DatabaseField
    private String medicalPersonId;

    @DatabaseField
    private String hospitalRegNum;

    @DatabaseField
    private String name;

    @DatabaseField
    private String note;

    @DatabaseField
    private String allergyInfo;

    @DatabaseField
    private String allergyRecordId;

    @DatabaseField(columnName = "pt_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public PatientlProfileModel patientlProfileModel;

    @ForeignCollectionField
    private ForeignCollection<AllergyTrackInfoModel> allergyTrackInfoModels;

    public ForeignCollection<AllergyTrackInfoModel> getAllergyTrackInfoModels() {
        return allergyTrackInfoModels;
    }

    public void setAllergyTrackInfoModels(ForeignCollection<AllergyTrackInfoModel> allergyTrackInfoModels) {
        this.allergyTrackInfoModels = allergyTrackInfoModels;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(String medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public String getMedicalPersonId() {
        return medicalPersonId;
    }

    public void setMedicalPersonId(String medicalPersonId) {
        this.medicalPersonId = medicalPersonId;
    }

    public String getHospitalRegNum() {
        return hospitalRegNum;
    }

    public void setHospitalRegNum(String hospitalRegNum) {
        this.hospitalRegNum = hospitalRegNum;
    }

    public String getAllergyInfo() {
        return allergyInfo;
    }

    public void setAllergyInfo(String allergyInfo) {
        this.allergyInfo = allergyInfo;
    }

    public String getAllergyRecordId() {
        return allergyRecordId;
    }

    public void setAllergyRecordId(String allergyRecordId) {
        this.allergyRecordId = allergyRecordId;
    }

    public PatientlProfileModel getPatientlProfileModel() {
        return patientlProfileModel;
    }

    public void setPatientlProfileModel(PatientlProfileModel patientlProfileModel) {
        this.patientlProfileModel = patientlProfileModel;
    }
}
