package com.vedas.spectrocare.DataBaseModels;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "vaccine")
public class VaccineModel {

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
    private String vaccineName;

    @DatabaseField
    private String vaccineDate;

    @DatabaseField
    private String note;

    @DatabaseField
    private String vaccineRecordId;

    @DatabaseField(columnName = "pt_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public PatientlProfileModel patientlProfileModel;


    @ForeignCollectionField
    private ForeignCollection<VaccineTrackInfoModel> vaccineTrackInfoModels;

    public ForeignCollection<VaccineTrackInfoModel> getVaccineTrackInfoModels() {
        return vaccineTrackInfoModels;
    }

    public void setVaccineTrackInfoModels(ForeignCollection<VaccineTrackInfoModel> vaccineTrackInfoModels) {
        this.vaccineTrackInfoModels = vaccineTrackInfoModels;
    }

    public String getVaccineRecordId() {
        return vaccineRecordId;
    }

    public void setVaccineRecordId(String vaccineRecordId) {
        this.vaccineRecordId = vaccineRecordId;
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

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public String getVaccineDate() {
        return vaccineDate;
    }

    public void setVaccineDate(String vaccineDate) {
        this.vaccineDate = vaccineDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public PatientlProfileModel getPatientlProfileModel() {
        return patientlProfileModel;
    }

    public void setPatientlProfileModel(PatientlProfileModel patientlProfileModel) {
        this.patientlProfileModel = patientlProfileModel;
    }
}
