package com.vedas.spectrocare.PatinetControllers;

import android.util.Log;
import com.vedas.spectrocare.model.AppointmetModel;
import java.util.ArrayList;

public class PatientAppointmentController {
    public static PatientAppointmentController appointmentObj;
    ArrayList<AppointmetModel> appointmentList;

    public static PatientAppointmentController getInstance(){
        if (appointmentObj==null){
            Log.e("shb","dh");
            appointmentObj = new PatientAppointmentController();
        }else {
            Log.e("shdsb","dh");
        }
        return appointmentObj;
    }

    public ArrayList<AppointmetModel> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(ArrayList<AppointmetModel> appointmentList) {
        this.appointmentList = appointmentList;
    }
    public  static PatientAppointmentController setNull(){
        Log.e("daaad","dh");
        appointmentObj=null;
        return appointmentObj;
    }
    public static boolean isNull(){
        if (appointmentObj==null){
            return true;
        }else
            return false;
    }

}
