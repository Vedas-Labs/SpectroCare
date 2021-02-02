package com.vedas.spectrocare.PatientAppointmentModule;

import android.content.Context;

import java.util.ArrayList;

public class PatientAppointmentsDataController {
    Context context;

    public  static PatientAppointmentsDataController controller;
    ArrayList<AppointmentArrayModel> appointmentsList;

    public ArrayList<AppointmentArrayModel> allappointmentsList;
    public ArrayList<AppointmentArrayModel> upcomingAppointmentsList;
    public ArrayList<AppointmentArrayModel> pastAppointmentsList;

    public static PatientAppointmentsDataController getInstance(){
        if (controller==null){
            controller = new PatientAppointmentsDataController();
        }
        return controller;
    }
    public void fillContent(Context context1) {
        context = context1;
        allappointmentsList=new ArrayList<>();
        upcomingAppointmentsList = new ArrayList<>();
        pastAppointmentsList = new ArrayList<>();

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
