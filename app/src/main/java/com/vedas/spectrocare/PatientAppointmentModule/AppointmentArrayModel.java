package com.vedas.spectrocare.PatientAppointmentModule;

import com.vedas.spectrocare.PatientDocResponseModel.MedicalPersonnelModel;

import java.io.Serializable;

public class AppointmentArrayModel implements Serializable {
    AppointmentDetailsModel appointmentDetails;
    String hospital_reg_num;
    String doctorMedicalPersonnelID;
    String patientID;
    CreatorDetailsModel creatorDetails;
    PatientAppointDetailsModel patientDetails;
   // DoctorDetailsModel doctorDetails;
     MedicalPersonnelModel doctorDetails;

    public AppointmentDetailsModel getAppointmentDetails() {
        return appointmentDetails;
    }

    public void setAppointmentDetails(AppointmentDetailsModel appointmentDetails) {
        this.appointmentDetails = appointmentDetails;
    }

    public String getHospital_reg_num() {
        return hospital_reg_num;
    }

    public void setHospital_reg_num(String hospital_reg_num) {
        this.hospital_reg_num = hospital_reg_num;
    }

    public String getDoctorMedicalPersonnelID() {
        return doctorMedicalPersonnelID;
    }

    public void setDoctorMedicalPersonnelID(String doctorMedicalPersonnelID) {
        this.doctorMedicalPersonnelID = doctorMedicalPersonnelID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public CreatorDetailsModel getCreatorDetails() {
        return creatorDetails;
    }

    public void setCreatorDetails(CreatorDetailsModel creatorDetails) {
        this.creatorDetails = creatorDetails;
    }

    public PatientAppointDetailsModel getPatientDetails() {
        return patientDetails;
    }

    public void setPatientDetails(PatientAppointDetailsModel patientDetails) {
        this.patientDetails = patientDetails;
    }

    public MedicalPersonnelModel getDoctorDetails() {
        return doctorDetails;
    }

    public void setDoctorDetails(MedicalPersonnelModel doctorDetails) {
        this.doctorDetails = doctorDetails;
    }
}
