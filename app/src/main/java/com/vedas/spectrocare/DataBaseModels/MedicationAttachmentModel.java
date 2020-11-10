package com.vedas.spectrocare.DataBaseModels;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

public class MedicationAttachmentModel {
    @DatabaseField(generatedId = true)
    private int Id;

    @DatabaseField
    private String filePath;

    @DatabaseField
    private String filePath2;

    @DatabaseField
    private String illnessMedicationID;

    @DatabaseField(columnName = "meat_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public MedicationRecordModel medicationRecordModel;

    public String getFilePath2() {
        return filePath2;
    }

    public void setFilePath2(String filePath2) {
        this.filePath2 = filePath2;
    }

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

    public String getIllnessMedicationID() {
        return illnessMedicationID;
    }

    public void setIllnessMedicationID(String illnessMedicationID) {
        this.illnessMedicationID = illnessMedicationID;
    }

    public MedicationRecordModel getMedicationRecordModel() {
        return medicationRecordModel;
    }

    public void setMedicationRecordModel(MedicationRecordModel medicationRecordModel) {
        this.medicationRecordModel = medicationRecordModel;
    }
}
