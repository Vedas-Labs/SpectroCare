package com.vedas.spectrocare.DataBaseModels;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

public class MedicationRecordModel {
    @DatabaseField(generatedId = true)
    private int Id;

    @DatabaseField
    private String hospital_reg_num;

    @DatabaseField
    private String illnessID;

    @DatabaseField
    private String illnessMedicationID;

    @DatabaseField
    private String medical_personnel_id;

    @DatabaseField
    private String medical_record_id;

    @DatabaseField
    private String patientID;

    @DatabaseField
    private String doctorName;

    @DatabaseField
    private String date;

    @DatabaseField(columnName = "ill_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public IllnessRecordModel illnessRecordModel;

    @ForeignCollectionField
    private ForeignCollection<MedicinesRecordModel> medicinesRecordModels;

    @ForeignCollectionField
    private ForeignCollection<MedicationAttachmentModel> medicationAttachmentModels;

    public ForeignCollection<MedicationAttachmentModel> getMedicationAttachmentModels() {
        return medicationAttachmentModels;
    }

    public void setMedicationAttachmentModels(ForeignCollection<MedicationAttachmentModel> medicationAttachmentModels) {
        this.medicationAttachmentModels = medicationAttachmentModels;
    }

    public ForeignCollection<MedicinesRecordModel> getMedicinesRecordModels() {
        return medicinesRecordModels;
    }

    public void setMedicinesRecordModels(ForeignCollection<MedicinesRecordModel> medicinesRecordModels) {
        this.medicinesRecordModels = medicinesRecordModels;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getHospital_reg_num() {
        return hospital_reg_num;
    }

    public void setHospital_reg_num(String hospital_reg_num) {
        this.hospital_reg_num = hospital_reg_num;
    }

    public String getIllnessID() {
        return illnessID;
    }

    public void setIllnessID(String illnessID) {
        this.illnessID = illnessID;
    }

    public String getIllnessMedicationID() {
        return illnessMedicationID;
    }

    public void setIllnessMedicationID(String illnessMedicationID) {
        this.illnessMedicationID = illnessMedicationID;
    }

    public String getMedical_personnel_id() {
        return medical_personnel_id;
    }

    public void setMedical_personnel_id(String medical_personnel_id) {
        this.medical_personnel_id = medical_personnel_id;
    }

    public String getMedical_record_id() {
        return medical_record_id;
    }

    public void setMedical_record_id(String medical_record_id) {
        this.medical_record_id = medical_record_id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public IllnessRecordModel getIllnessRecordModel() {
        return illnessRecordModel;
    }

    public void setIllnessRecordModel(IllnessRecordModel illnessRecordModel) {
        this.illnessRecordModel = illnessRecordModel;
    }
}
