package com.vedas.spectrocare.DataBaseModels;

import com.j256.ormlite.field.DatabaseField;

public class PhysicalTrackInfoModel {

    @DatabaseField(generatedId = true)
    private int Id;

    @DatabaseField
    private String patientId;

    @DatabaseField
    private String physicalExamId;

    @DatabaseField
    private String byWhom;

    @DatabaseField
    private String byWhomId;

    @DatabaseField
    private String date;

    @DatabaseField(columnName = "pa_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public PhysicalExamsDataModel physicalExamsDataModel;

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


    public String getPhysicalExamId() {
        return physicalExamId;
    }

    public void setPhysicalExamId(String physicalExamId) {
        this.physicalExamId = physicalExamId;
    }

    public PhysicalExamsDataModel getPhysicalExamsDataModel() {
        return physicalExamsDataModel;
    }

    public void setPhysicalExamsDataModel(PhysicalExamsDataModel physicalExamsDataModel) {
        this.physicalExamsDataModel = physicalExamsDataModel;
    }
}
