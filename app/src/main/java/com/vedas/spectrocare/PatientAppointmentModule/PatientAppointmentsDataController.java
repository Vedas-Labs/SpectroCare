package com.vedas.spectrocare.PatientAppointmentModule;

import java.util.ArrayList;

public class PatientAppointmentsDataController {

    public  static PatientAppointmentsDataController controller;
    ArrayList<AppointmentArrayModel> appointmentsList;

    public static PatientAppointmentsDataController getInstance(){
        if (controller==null){
            controller = new PatientAppointmentsDataController();
        }
        return controller;
    }
    public static boolean isNull(){
        if (controller==null){
            return true;
        }else
            return false;
    }
    public static PatientAppointmentsDataController setNull(){
        controller =null;
        return controller;
    }

    public ArrayList<AppointmentArrayModel> getAppointmentsList() {
        return appointmentsList;
    }

    public void setAppointmentsList(ArrayList<AppointmentArrayModel> appointmentsList) {
        this.appointmentsList = appointmentsList;
    }
}
