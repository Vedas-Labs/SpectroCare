package com.vedas.spectrocare.DataBaseModels;

import com.j256.ormlite.field.DatabaseField;

public class FamilyTrackInfoModel {

    @DatabaseField(generatedId = true)
    private int Id;


    @DatabaseField
    private String familyRecordId;

    @DatabaseField
    private String byWhom;

    @DatabaseField
    private String byWhomId;

    @DatabaseField
    private String date;

    @DatabaseField(columnName = "f_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public FamilyHistoryModel familyHistoryModel;

    public String getFamilyRecordId() {
        return familyRecordId;
    }

    public void setFamilyRecordId(String familyRecordId) {
        this.familyRecordId = familyRecordId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }


    public String getByWhom() {
        return byWhom;
    }

    public void setByWhom(String byWhom) {
        this.byWhom = byWhom;
    }

    public String getByWhomId() {
        return byWhomId;
    }

    public void setByWhomId(String byWhomId) {
        this.byWhomId = byWhomId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public FamilyHistoryModel getFamilyHistoryModel() {
        return familyHistoryModel;
    }

    public void setFamilyHistoryModel(FamilyHistoryModel familyHistoryModel) {
        this.familyHistoryModel = familyHistoryModel;
    }
}
