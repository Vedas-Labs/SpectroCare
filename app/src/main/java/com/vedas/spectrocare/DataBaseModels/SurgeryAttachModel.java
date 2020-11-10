package com.vedas.spectrocare.DataBaseModels;

import com.j256.ormlite.field.DatabaseField;

public class SurgeryAttachModel {

    @DatabaseField(generatedId = true)
    private int Id;

    @DatabaseField
    private String filePath;

    @DatabaseField
    private String moreDetails;

    @DatabaseField(columnName = "aa_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public SurgicalRecordModel surgicalRecordModel;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMoreDetails() {
        return moreDetails;
    }

    public void setMoreDetails(String moreDetails) {
        this.moreDetails = moreDetails;
    }

    public SurgicalRecordModel getSurgicalRecordModel() {
        return surgicalRecordModel;
    }

    public void setSurgicalRecordModel(SurgicalRecordModel surgicalRecordModel) {
        this.surgicalRecordModel = surgicalRecordModel;
    }
}
