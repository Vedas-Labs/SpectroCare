package com.vedas.spectrocare.DataBaseModels;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "family")
public class FamilyHistoryModel {
    @DatabaseField(generatedId = true)
    private int Id;

    @DatabaseField
    private String patientId;

    @DatabaseField
    private String familyRecordId;

    @DatabaseField
    private String dieseaseName;

    @DatabaseField
    private String medicalRecordId;

    @DatabaseField
    private String medicalPersonId;

    @DatabaseField
    private String Relation;

    @DatabaseField
    private String age;

    @DatabaseField
    private String discription;

    @DatabaseField
    private String medicalCondition;

    @DatabaseField(columnName = "p_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public PatientlProfileModel patientlProfileModel;

    @ForeignCollectionField
    private ForeignCollection<FamilyTrackInfoModel> familyTrackInfoModels;

    public ForeignCollection<FamilyTrackInfoModel> getFamilyTrackInfoModels() {
        return familyTrackInfoModels;
    }

    public void setFamilyTrackInfoModels(ForeignCollection<FamilyTrackInfoModel> familyTrackInfoModels) {
        this.familyTrackInfoModels = familyTrackInfoModels;
    }

    public int getId() {
        return Id;
    }

    public String getDieseaseName() {
        return dieseaseName;
    }

    public void setDieseaseName(String dieseaseName) {
        this.dieseaseName = dieseaseName;
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

    public String getFamilyRecordId() {
        return familyRecordId;
    }

    public void setFamilyRecordId(String familyRecordId) {
        this.familyRecordId = familyRecordId;
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

    public String getRelation() {
        return Relation;
    }

    public void setRelation(String relation) {
        Relation = relation;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getMedicalCondition() {
        return medicalCondition;
    }

    public void setMedicalCondition(String medicalCondition) {
        this.medicalCondition = medicalCondition;
    }

    public PatientlProfileModel getPatientlProfileModel() {
        return patientlProfileModel;
    }

    public void setPatientlProfileModel(PatientlProfileModel patientlProfileModel) {
        this.patientlProfileModel = patientlProfileModel;
    }
}
