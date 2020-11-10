package com.vedas.spectrocare.DataBaseModels;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

public class ScreeningRecordModel {
    @DatabaseField(generatedId = true)
    private int Id;

    @DatabaseField
    private String patientId;

    @DatabaseField
    private String medicalRecordId;

    @DatabaseField
    private String medicalPersonId;

    @DatabaseField
    private String screeningID;

    @DatabaseField
    private String moreInfo;

    @DatabaseField
    private String attachment;

    @DatabaseField(columnName = "p_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public PatientlProfileModel patientlProfileModel;


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

    public String getScreeningID() {
        return screeningID;
    }

    public void setScreeningID(String screeningID) {
        this.screeningID = screeningID;
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

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
