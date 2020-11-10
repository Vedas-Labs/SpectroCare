package com.vedas.spectrocare.model;

public class Medicprofile {

    String firstname;
    String lastname;
    String hosipital_regno;
    String medicalperson_id;

    public Medicprofile(String firstname, String lastname, String hosipital_regno, String medicalperson_id) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.hosipital_regno = hosipital_regno;
        this.medicalperson_id = medicalperson_id;
    }


    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getHosipital_regno() {
        return hosipital_regno;
    }

    public void setHosipital_regno(String hosipital_regno) {
        this.hosipital_regno = hosipital_regno;
    }

    public String getMedicalperson_id() {
        return medicalperson_id;
    }

    public void setMedicalperson_id(String medicalperson_id) {
        this.medicalperson_id = medicalperson_id;
    }
}
