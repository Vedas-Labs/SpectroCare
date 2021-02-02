package com.vedas.spectrocare.PatientDocResponseModel;
import java.util.ArrayList;

public class OfficeHours {
    String dayName;
    ArrayList<Sessions> sessions;

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public ArrayList<Sessions> getSessions() {
        return sessions;
    }

    public void setSessions(ArrayList<Sessions> sessions) {
        this.sessions = sessions;
    }
}
