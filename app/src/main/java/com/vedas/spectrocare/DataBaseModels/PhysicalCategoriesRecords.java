package com.vedas.spectrocare.DataBaseModels;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "physical")
public class PhysicalCategoriesRecords {

    @DatabaseField(generatedId = true)
    private int Id;

    @DatabaseField
    private String patientId;

    @DatabaseField
    private String result;

    @DatabaseField
    private String category;

    @DatabaseField
    private String medicalRecordId;

    @DatabaseField
    private String description;

    @DatabaseField
    private String physicalExamId;

    @DatabaseField
    private String physicianComment;

    @DatabaseField(columnName = "patien_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public PhysicalExamsDataModel physicalExamsDataModel;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(String medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhysicalExamId() {
        return physicalExamId;
    }

    public void setPhysicalExamId(String physicalExamId) {
        this.physicalExamId = physicalExamId;
    }

    public String getPhysicianComment() {
        return physicianComment;
    }

    public void setPhysicianComment(String physicianComment) {
        this.physicianComment = physicianComment;
    }

    public PhysicalExamsDataModel getPhysicalExamsDataModel() {
        return physicalExamsDataModel;
    }

    public void setPhysicalExamsDataModel(PhysicalExamsDataModel physicalExamsDataModel) {
        this.physicalExamsDataModel = physicalExamsDataModel;
    }
}
