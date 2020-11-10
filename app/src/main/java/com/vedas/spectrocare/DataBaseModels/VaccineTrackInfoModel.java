package com.vedas.spectrocare.DataBaseModels;

import com.j256.ormlite.field.DatabaseField;

public class VaccineTrackInfoModel {
    @DatabaseField(generatedId = true)
    private int Id;

    @DatabaseField
    private String vaccineRecordId;

    @DatabaseField
    private String byWhom;

    @DatabaseField
    private String byWhomId;

    @DatabaseField
    private String date;

    @DatabaseField(columnName = "va_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public VaccineModel vaccineModel;

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

    public VaccineModel getVaccineModel() {
        return vaccineModel;
    }

    public void setVaccineModel(VaccineModel vaccineModel) {
        this.vaccineModel = vaccineModel;
    }
}
