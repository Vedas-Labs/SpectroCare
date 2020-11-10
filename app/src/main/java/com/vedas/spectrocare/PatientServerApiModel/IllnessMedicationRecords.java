package com.vedas.spectrocare.PatientServerApiModel;

public class IllnessMedicationRecords{
    private String illnessID;
    private String illnessMedicationID;
    private String patientID;
    private String hospital_reg_num;
    private String medical_record_id;
    private MannualPrescriptions mannualPrescriptions;

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

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getHospital_reg_num() {
        return hospital_reg_num;
    }

    public void setHospital_reg_num(String hospital_reg_num) {
        this.hospital_reg_num = hospital_reg_num;
    }

    public String getMedical_record_id() {
        return medical_record_id;
    }

    public void setMedical_record_id(String medical_record_id) {
        this.medical_record_id = medical_record_id;
    }

    public MannualPrescriptions getMannualPrescriptions() {
        return mannualPrescriptions;
    }

    public void setMannualPrescriptions(MannualPrescriptions mannualPrescriptions) {
        this.mannualPrescriptions = mannualPrescriptions;
    }
}
