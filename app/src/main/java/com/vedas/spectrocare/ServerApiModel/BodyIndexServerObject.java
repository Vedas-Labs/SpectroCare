package com.vedas.spectrocare.ServerApiModel;

public class BodyIndexServerObject {
    String height;
    String weight;
    String waistline;
    String bmi;
    String bloodPressure;
    public BodyIndexServerObject(){

    }

    public BodyIndexServerObject(String height, String weight, String waistline, String bmi, String bloodPressure) {
        this.height = height;
        this.weight = weight;
        this.waistline = waistline;
        this.bmi = bmi;
        this.bloodPressure = bloodPressure;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWaistline() {
        return waistline;
    }

    public void setWaistline(String waistline) {
        this.waistline = waistline;
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }
}
