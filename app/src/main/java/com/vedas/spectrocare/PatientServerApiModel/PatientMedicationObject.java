package com.vedas.spectrocare.PatientServerApiModel;

import com.google.gson.annotations.SerializedName;
import com.vedas.spectrocare.ServerApiModel.AttachmentServerObjects;
import com.vedas.spectrocare.ServerApiModel.VaccineServerObject;

import java.util.ArrayList;

public class PatientMedicationObject {
    private String hospital_reg_num;
    private String byWhom;
    private String byWhomID;
    private String patientID;
    private String medical_record_id;
    private String illnessID;
    private String doctorMedicalPersonnelID;
    private String doctorName;
    private String response;
    private String illnessMedicationID;
    private String message;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getIllnessMedicationID() {
        return illnessMedicationID;
    }

    public void setIllnessMedicationID(String illnessMedicationID) {
        this.illnessMedicationID = illnessMedicationID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHospital_reg_num() {
        return hospital_reg_num;
    }

    public void setHospital_reg_num(String hospital_reg_num) {
        this.hospital_reg_num = hospital_reg_num;
    }

    public String getByWhom() {
        return byWhom;
    }

    public void setByWhom(String byWhom) {
        this.byWhom = byWhom;
    }

    public String getByWhomID() {
        return byWhomID;
    }

    public void setByWhomID(String byWhomID) {
        this.byWhomID = byWhomID;
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

    public ArrayList<PatientMedicationArrayObject> getMedicatioArrayObjects() {
        return medicatioArrayObjects;
    }

    public void setMedicatioArrayObjects(ArrayList<PatientMedicationArrayObject> medicatioArrayObjects) {
        this.medicatioArrayObjects = medicatioArrayObjects;
    }

    @SerializedName("medications")
    ArrayList<PatientMedicationArrayObject> medicatioArrayObjects;


}
