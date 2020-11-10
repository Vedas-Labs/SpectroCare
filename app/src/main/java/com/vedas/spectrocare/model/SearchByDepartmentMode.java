package com.vedas.spectrocare.model;

public class SearchByDepartmentMode {
    String byWhomID;
    String byWhom;
    String category;
    String department;
    String hospitalNo;

    public SearchByDepartmentMode(String byWhomID, String byWhom,
                                  String category, String department, String hospitalNo) {
        this.byWhomID = byWhomID;
        this.byWhom = byWhom;
        this.category = category;
        this.department = department;
        this.hospitalNo = hospitalNo;
    }

    public String getByWhomID() {
        return byWhomID;
    }

    public void setByWhomID(String byWhomID) {
        this.byWhomID = byWhomID;
    }

    public String getByWhom() {
        return byWhom;
    }

    public void setByWhom(String byWhom) {
        this.byWhom = byWhom;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getHospitalNo() {
        return hospitalNo;
    }

    public void setHospitalNo(String hospitalNo) {
        this.hospitalNo = hospitalNo;
    }
}
