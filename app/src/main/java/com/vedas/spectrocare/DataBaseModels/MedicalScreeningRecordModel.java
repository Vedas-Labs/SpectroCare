package com.vedas.spectrocare.DataBaseModels;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "screeningIllness")
public class MedicalScreeningRecordModel {

    @DatabaseField(generatedId = true)
    private int Id;

    @DatabaseField
    private String illnessID;

    @DatabaseField
    private String patientID;

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

    @DatabaseField(columnName = "ill_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public IllnessRecordModel illnessRecordModel;

    public IllnessRecordModel getIllnessRecordModel() {
        return illnessRecordModel;
    }

    public void setIllnessRecordModel(IllnessRecordModel illnessRecordModel) {
        this.illnessRecordModel = illnessRecordModel;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getIllnessID() {
        return illnessID;
    }

    public void setIllnessID(String illnessID) {
        this.illnessID = illnessID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
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

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }


}
