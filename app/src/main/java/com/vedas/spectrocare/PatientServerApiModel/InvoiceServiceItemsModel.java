package com.vedas.spectrocare.PatientServerApiModel;

public class InvoiceServiceItemsModel {
    private String  serviceID;
    private String  serviceName;
    private String  serviceNetCost;
    private String  serviceUnit;
    private String  serviceVAT;
    private String  serviceGrossCost;
    private String  category;

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceNetCost() {
        return serviceNetCost;
    }

    public void setServiceNetCost(String serviceNetCost) {
        this.serviceNetCost = serviceNetCost;
    }

    public String getServiceUnit() {
        return serviceUnit;
    }

    public void setServiceUnit(String serviceUnit) {
        this.serviceUnit = serviceUnit;
    }

    public String getServiceVAT() {
        return serviceVAT;
    }

    public void setServiceVAT(String serviceVAT) {
        this.serviceVAT = serviceVAT;
    }

    public String getServiceGrossCost() {
        return serviceGrossCost;
    }

    public void setServiceGrossCost(String serviceGrossCost) {
        this.serviceGrossCost = serviceGrossCost;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
