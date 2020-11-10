package com.vedas.spectrocare.ServerApiModel;

import java.util.ArrayList;

public class AllmedicalRecordsServerObject {
    String illnessId;
    String doctorMedicalPersonnellId;
    String doctorName;
    ArrayList<MedicationRecordServerObject> medicalRecordModelArrayList;

    public AllmedicalRecordsServerObject(){

    }

    public AllmedicalRecordsServerObject(String illnessId, String doctorMedicalPersonnellId, String doctorName,
                                         ArrayList<MedicationRecordServerObject> medicalRecordModelArrayList) {
        this.illnessId = illnessId;
        this.doctorMedicalPersonnellId = doctorMedicalPersonnellId;
        this.doctorName = doctorName;
        this.medicalRecordModelArrayList = medicalRecordModelArrayList;
    }

    public String getIllnessId() {
        return illnessId;
    }

    public void setIllnessId(String illnessId) {
        this.illnessId = illnessId;
    }

    public String getDoctorMedicalPersonnellId() {
        return doctorMedicalPersonnellId;
    }

    public void setDoctorMedicalPersonnellId(String doctorMedicalPersonnellId) {
        this.doctorMedicalPersonnellId = doctorMedicalPersonnellId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public ArrayList<MedicationRecordServerObject> getMedicalRecordModelArrayList() {
        return medicalRecordModelArrayList;
    }

    public void setMedicalRecordModelArrayList(ArrayList<MedicationRecordServerObject> medicalRecordModelArrayList) {
        this.medicalRecordModelArrayList = medicalRecordModelArrayList;
    }
}
