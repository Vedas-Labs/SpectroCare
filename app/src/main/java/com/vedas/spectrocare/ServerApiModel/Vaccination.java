package com.vedas.spectrocare.ServerApiModel;

public class Vaccination {

    String name;
    String date;
    String notes;

    public Vaccination(String name, String date, String notes) {
        this.name = name;
        this.date = date;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
