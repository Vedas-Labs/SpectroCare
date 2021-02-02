package com.vedas.spectrocare.model;

public class ServicesModel {
    String isAvailable;
    String serviceID;
    String adminUserID;
    String hospital_reg_num;
    String serviceName;
    String serviceCost;
    String currency;
    String serviceVATPercent;
    String createdDate;
    String updatedDate;

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getAdminUserID() {
        return adminUserID;
    }

    public void setAdminUserID(String adminUserID) {
        this.adminUserID = adminUserID;
    }

    public String getHospital_reg_num() {
        return hospital_reg_num;
    }

    public void setHospital_reg_num(String hospital_reg_num) {
        this.hospital_reg_num = hospital_reg_num;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(String serviceCost) {
        this.serviceCost = serviceCost;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getServiceVATPercent() {
        return serviceVATPercent;
    }

    public void setServiceVATPercent(String serviceVATPercent) {
        this.serviceVATPercent = serviceVATPercent;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }
}
