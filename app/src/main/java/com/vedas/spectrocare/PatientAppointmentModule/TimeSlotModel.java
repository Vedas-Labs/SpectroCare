package com.vedas.spectrocare.PatientAppointmentModule;

public class TimeSlotModel {
    int timeInMins;
    String timeInFormat;

    public int getTimeInMins() {
        return timeInMins;
    }

    public void setTimeInMins(int timeInMins) {
        this.timeInMins = timeInMins;
    }

    public String getTimeInFormat() {
        return timeInFormat;
    }

    public void setTimeInFormat(String timeInFormat) {
        this.timeInFormat = timeInFormat;
    }
}
