package com.vedas.spectrocare.DataBaseModels;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

@DatabaseTable(tableName = "MedicinesRecord")
public class MedicinesRecordModel {
    @DatabaseField(generatedId = true)
    private int Id;

    @DatabaseField
    private String dosage;

    @DatabaseField
    private String uniqueId;

    @DatabaseField
    private String durationDays;

    @DatabaseField
    private String freq;

    @DatabaseField
    private String illnessMedicationID;

    @DatabaseField
    private String moreDetails;

    @DatabaseField
    private String name;

    @DatabaseField
    private String purpose;

    @DatabaseField(columnName = "me_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public MedicationRecordModel medicationRecordModel;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(String durationDays) {
        this.durationDays = durationDays;
    }

    public String getFreq() {
        return freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }

    public String getIllnessMedicationID() {
        return illnessMedicationID;
    }

    public void setIllnessMedicationID(String illnessMedicationID) {
        this.illnessMedicationID = illnessMedicationID;
    }

    public String getMoreDetails() {
        return moreDetails;
    }

    public void setMoreDetails(String moreDetails) {
        this.moreDetails = moreDetails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public MedicationRecordModel getMedicationRecordModel() {
        return medicationRecordModel;
    }

    public void setMedicationRecordModel(MedicationRecordModel medicationRecordModel) {
        this.medicationRecordModel = medicationRecordModel;
    }
}
