package com.vedas.spectrocare.DataBaseModels;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

@DatabaseTable(tableName = "surgicalRecord")
public class SurgicalRecordModel {

    @DatabaseField(generatedId = true)
    private int Id;

    @DatabaseField
    private String illnessID;

    @DatabaseField
    private String attachment;

    @DatabaseField
    private String moreInfo;

    @DatabaseField
    private String illnessSurgicalId;

    @DatabaseField
    private String doctorMedicalPersonnelId;

    @DatabaseField
    private String doctorName;

    @DatabaseField
    private String surgeryProcedure;

    @DatabaseField
    private String surgeryDate;


    @DatabaseField
    private  String surgeryInformation;
    @DatabaseField(columnName = "surgery_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public IllnessRecordModel illnessRecordModel;

    @ForeignCollectionField
    private ForeignCollection<SurgeryAttachModel> attachModels;

    public ForeignCollection<SurgeryAttachModel> getAttachModels() {
        return attachModels;
    }

    public void setAttachModels(ForeignCollection<SurgeryAttachModel> attachModels) {
        this.attachModels = attachModels;
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

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public String getIllnessSurgicalId() {
        return illnessSurgicalId;
    }

    public void setIllnessSurgicalId(String illnessSurgicalId) {
        this.illnessSurgicalId = illnessSurgicalId;
    }

    public String getDoctorMedicalPersonnelId() {
        return doctorMedicalPersonnelId;
    }

    public void setDoctorMedicalPersonnelId(String doctorMedicalPersonnelId) {
        this.doctorMedicalPersonnelId = doctorMedicalPersonnelId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSurgeryProcedure() {
        return surgeryProcedure;
    }

    public void setSurgeryProcedure(String surgeryProcedure) {
        this.surgeryProcedure = surgeryProcedure;
    }

    public String getSurgeryDate() {
        return surgeryDate;
    }

    public void setSurgeryDate(String surgeryDate) {
        this.surgeryDate = surgeryDate;
    }

    public String getSurgeryInformation() {
        return surgeryInformation;
    }

    public void setSurgeryInformation(String surgeryInformation) {
        this.surgeryInformation = surgeryInformation;
    }

    public IllnessRecordModel getIllnessRecordModel() {
        return illnessRecordModel;
    }

    public void setIllnessRecordModel(IllnessRecordModel illnessRecordModel) {
        this.illnessRecordModel = illnessRecordModel;
    }
}
