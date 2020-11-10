package com.vedas.spectrocare.DataBaseModels;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "bmi")
public class BMIModel {
    @DatabaseField(generatedId = true)
    private int Id;

    @DatabaseField
    private String patientId;

    @DatabaseField
    private String physicalExamId;

    @DatabaseField
    private String medicalRecordId;

    @DatabaseField
    private String height;

    @DatabaseField
    private String weight;

    @DatabaseField
    private String bmi;

    @DatabaseField
    private String bloodPressure;

    @DatabaseField
    private String waistLine;

    @DatabaseField(columnName = "patient_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public PhysicalExamsDataModel physicalExamsDataModel;

    public String getPhysicalExamId() {
        return physicalExamId;
    }

    public void setPhysicalExamId(String physicalExamId) {
        this.physicalExamId = physicalExamId;
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

    public String getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(String medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getWaistLine() {
        return waistLine;
    }

    public void setWaistLine(String waistLine) {
        this.waistLine = waistLine;
    }

    public PhysicalExamsDataModel getPhysicalExamsDataModel() {
        return physicalExamsDataModel;
    }

    public void setPhysicalExamsDataModel(PhysicalExamsDataModel physicalExamsDataModel) {
        this.physicalExamsDataModel = physicalExamsDataModel;
    }
}
