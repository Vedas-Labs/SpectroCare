package com.vedas.spectrocare.ServerApiModel;

public class PhysicalRecordServerObject {

    private String category;
    private String result;
    private String description;
    public PhysicalRecordServerObject(){

    }

    public PhysicalRecordServerObject(String category, String result, String description) {
        this.category = category;
        this.result = result;
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
