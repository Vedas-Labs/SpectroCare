package com.vedas.spectrocare.model;

public class DoctorsItemModel {
    String categoryIcon;
    String doctorName;
    String docProfession;

    public DoctorsItemModel(String categoryIcon, String doctorName, String docProfession) {
        this.categoryIcon = categoryIcon;
        this.doctorName = doctorName;
        this.docProfession = docProfession;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDocProfession() {
        return docProfession;
    }

    public void setDocProfession(String docProfession) {
        this.docProfession = docProfession;
    }
}
