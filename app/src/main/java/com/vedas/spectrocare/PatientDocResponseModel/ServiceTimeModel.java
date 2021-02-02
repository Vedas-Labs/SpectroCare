package com.vedas.spectrocare.PatientDocResponseModel;

import java.util.ArrayList;

public class ServiceTimeModel {
    ArrayList<OfficeHours> officeHours;
    ArrayList appointmentHours;

    public ArrayList<OfficeHours> getOfficeHours() {
        return officeHours;
    }

    public void setOfficeHours(ArrayList<OfficeHours> officeHours) {
        this.officeHours = officeHours;
    }

    public ArrayList getAppointmentHours() {
        return appointmentHours;
    }

    public void setAppointmentHours(ArrayList appointmentHours) {
        this.appointmentHours = appointmentHours;
    }
}
