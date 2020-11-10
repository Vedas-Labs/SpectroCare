package com.vedas.spectrocare.DataBaseModels;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "allergyTrack")
public class AllergyTrackInfoModel {

    @DatabaseField(generatedId = true)
    private int Id;

    @DatabaseField
    private String allergyRecordId;

    @DatabaseField
    private String byWhom;

    @DatabaseField
    private String byWhomId;

    @DatabaseField
    private String date;

    @DatabaseField(columnName = "at_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public AllergieModel allergieModel;


    public String getAllergyRecordId() {
        return allergyRecordId;
    }

    public void setAllergyRecordId(String allergyRecordId) {
        this.allergyRecordId = allergyRecordId;
    }

    public AllergieModel getAllergieModel() {
        return allergieModel;
    }

    public void setAllergieModel(AllergieModel allergieModel) {
        this.allergieModel = allergieModel;
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

}
