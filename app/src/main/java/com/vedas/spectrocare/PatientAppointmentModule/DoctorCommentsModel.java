package com.vedas.spectrocare.PatientAppointmentModule;

import com.vedas.spectrocare.PatientDocResponseModel.MedicalPersonnelModel;

import java.io.Serializable;

public class DoctorCommentsModel implements Serializable {
    String illnessDiagnosisID;
    String illnessMedicationID;
    String addedDate;

    public String getIllnessDiagnosisID() {
        return illnessDiagnosisID;
    }

    public void setIllnessDiagnosisID(String illnessDiagnosisID) {
        this.illnessDiagnosisID = illnessDiagnosisID;
    }

    public String getIllnessMedicationID() {
        return illnessMedicationID;
    }

    public void setIllnessMedicationID(String illnessMedicationID) {
        this.illnessMedicationID = illnessMedicationID;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }
    // "illnessDiagnosisID":"ID124","illnessMedicationID":"IMD67","addedDate":"1612421994809"}
}
