package com.vedas.spectrocare.PatientDocResponseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class ServiceTimeModel implements Serializable {
    ArrayList officeHours;
    ArrayList appointmentHours;

    public ArrayList getOfficeHours() {
        return officeHours;
    }

    public void setOfficeHours(ArrayList officeHours) {
        this.officeHours = officeHours;
    }

    public ArrayList getAppointmentHours() {
        return appointmentHours;
    }

    public void setAppointmentHours(ArrayList appointmentHours) {
        this.appointmentHours = appointmentHours;
    }
}
