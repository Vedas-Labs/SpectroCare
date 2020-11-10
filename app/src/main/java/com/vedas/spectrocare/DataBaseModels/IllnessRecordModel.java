package com.vedas.spectrocare.DataBaseModels;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Illness")
public class IllnessRecordModel {

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

   /*
    String addedDate;
    String updatedDate;
    String endDate;
    String startDate;
    */
    @DatabaseField
    private String IllnessRecordId;

    @DatabaseField
    private String isCurrentIllness;

    @DatabaseField
    private String symptoms;

    @DatabaseField
    private String currentStatus;

    @DatabaseField
    private String moreInfo;

    @DatabaseField(columnName = "p_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public PatientlProfileModel patientlProfileModel;


    @ForeignCollectionField
    private  ForeignCollection<MedicalScreeningRecordModel> medicalScreeningRecordModels;

    @ForeignCollectionField
    private  ForeignCollection<SurgicalRecordModel> surgicalRecordModels;

    public ForeignCollection<SurgicalRecordModel> getSurgicalRecordModels() {
        return surgicalRecordModels;
    }

    @ForeignCollectionField
    private ForeignCollection<MedicationRecordModel> medicationRecordModels;

    public ForeignCollection<MedicationRecordModel> getMedicationRecordModels() {
        return medicationRecordModels;
    }

    public void setMedicationRecordModels(ForeignCollection<MedicationRecordModel> medicationRecordModels) {
        this.medicationRecordModels = medicationRecordModels;
    }

    public void setSurgicalRecordModels(ForeignCollection<SurgicalRecordModel> surgicalRecordModels) {
        this.surgicalRecordModels = surgicalRecordModels;
    }

    public ForeignCollection<MedicalScreeningRecordModel> getMedicalScreeningRecordModels() {
        return medicalScreeningRecordModels;
    }


    public void setMedicalScreeningRecordModels(ForeignCollection<MedicalScreeningRecordModel> medicalScreeningRecordModels) {
        this.medicalScreeningRecordModels = medicalScreeningRecordModels;
    }
    //fo

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

    public String getIllnessRecordId() {
        return IllnessRecordId;
    }

    public void setIllnessRecordId(String illnessRecordId) {
        IllnessRecordId = illnessRecordId;
    }

    public String getIsCurrentIllness() {
        return isCurrentIllness;
    }

    public void setIsCurrentIllness(String isCurrentIllness) {
        this.isCurrentIllness = isCurrentIllness;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public PatientlProfileModel getPatientlProfileModel() {
        return patientlProfileModel;
    }

    public void setPatientlProfileModel(PatientlProfileModel patientlProfileModel) {
        this.patientlProfileModel = patientlProfileModel;
    }
}
