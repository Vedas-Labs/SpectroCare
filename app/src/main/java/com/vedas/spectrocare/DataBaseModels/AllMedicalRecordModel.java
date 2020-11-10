package com.vedas.spectrocare.DataBaseModels;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.vedas.spectrocare.ServerApiModel.MedicationRecordServerObject;

import java.util.ArrayList;

@DatabaseTable(tableName = "medication")
public class AllMedicalRecordModel {

    @DatabaseField(generatedId = true)
    private int Id;

    @DatabaseField()
    String illnessID;
    @DatabaseField()
    String doctorMedicalPersonnelID;
    @DatabaseField()
    String doctorName;
    @DatabaseField(columnName = "medication",canBeNull = false,foreign = true,foreignAutoRefresh = true)
    public ArrayList<MedicationModel> medicationModelList;

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

    public String getDoctorMedicalPersonnelID() {
        return doctorMedicalPersonnelID;
    }

    public void setDoctorMedicalPersonnelID(String doctorMedicalPersonnelID) {
        this.doctorMedicalPersonnelID = doctorMedicalPersonnelID;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public ArrayList<MedicationModel> getMedicationModelList() {
        return medicationModelList;
    }

    public void setMedicationModelList(ArrayList<MedicationModel> medicationModelList) {
        this.medicationModelList = medicationModelList;
    }
}
