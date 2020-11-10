package com.vedas.spectrocare.PatientAppointmentModule;

import java.io.Serializable;

public class CreatorDetailsModel implements Serializable {
    String byWhom;
    String byWhomID;

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
}
