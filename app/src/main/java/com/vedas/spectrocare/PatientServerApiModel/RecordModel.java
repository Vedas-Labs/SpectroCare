package com.vedas.spectrocare.PatientServerApiModel;

import com.vedas.spectrocare.DataBaseModels.FamilyTrackInfoModel;
import com.vedas.spectrocare.DataBaseModels.TrackInfoModel;
import com.vedas.spectrocare.PatientDocResponseModel.TrackingModel;

import java.util.ArrayList;

public class RecordModel {
    ArrayList<FamilyDetaislModel> famliyDiseases=new ArrayList<>();
    ArrayList<FamilyTrackingInfo> tracking;
    String patientID;
    String medical_record_id;
    String family_history_record_id;
    String hospital_reg_num;
    String createdDate;
    String updatedDate;

    public ArrayList<FamilyDetaislModel> getFamliyDiseases() {
        return famliyDiseases;
    }

    public void setFamliyDiseases(ArrayList<FamilyDetaislModel> famliyDiseases) {
        this.famliyDiseases = famliyDiseases;
    }

    public ArrayList<FamilyTrackingInfo> getTracking() {
        return tracking;
    }

    public void setTracking(ArrayList<FamilyTrackingInfo> tracking) {
        this.tracking = tracking;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getMedical_record_id() {
        return medical_record_id;
    }

    public void setMedical_record_id(String medical_record_id) {
        this.medical_record_id = medical_record_id;
    }

    public String getFamily_history_record_id() {
        return family_history_record_id;
    }

    public void setFamily_history_record_id(String family_history_record_id) {
        this.family_history_record_id = family_history_record_id;
    }

    public String getHospital_reg_num() {
        return hospital_reg_num;
    }

    public void setHospital_reg_num(String hospital_reg_num) {
        this.hospital_reg_num = hospital_reg_num;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }
}
