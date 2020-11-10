package com.vedas.spectrocare.PatientServerApiModel;

import java.util.ArrayList;

public class MannualPrescriptions{
    private String addedDate;
    private String updateDate;
    private String doctorMedicalPersonnelID;
    private String doctorName;
    private ArrayList<Medicines> medicines;

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
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

    public ArrayList<Medicines> getMedicines() {
        return medicines;
    }

    public void setMedicines(ArrayList<Medicines> medicines) {
        this.medicines = medicines;
    }
}
