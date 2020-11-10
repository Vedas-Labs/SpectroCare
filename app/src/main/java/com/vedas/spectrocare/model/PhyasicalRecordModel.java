package com.vedas.spectrocare.model;

public class PhyasicalRecordModel {
    String AddDiscription;
    String Normal;
    String Abnormal;
    String NotExammained;

    public PhyasicalRecordModel(String addDiscription, String normal, String abnormal, String notExammained) {
        AddDiscription = addDiscription;
        Normal = normal;
        Abnormal = abnormal;
        NotExammained = notExammained;
    }

    public String getAddDiscription() {
        return AddDiscription;
    }

    public void setAddDiscription(String addDiscription) {
        AddDiscription = addDiscription;
    }

    public String getNormal() {
        return Normal;
    }

    public void setNormal(String normal) {
        Normal = normal;
    }

    public String getAbnormal() {
        return Abnormal;
    }

    public void setAbnormal(String abnormal) {
        Abnormal = abnormal;
    }

    public String getNotExammained() {
        return NotExammained;
    }

    public void setNotExammained(String notExammained) {
        NotExammained = notExammained;
    }
}
