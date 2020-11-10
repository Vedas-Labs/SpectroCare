package com.vedas.spectrocare.model;

public class PhoneNumber {
    private String phoneNumber;
    private String countryCode;

    public PhoneNumber(String phoneNumber, String countryCode) {
        this.phoneNumber = phoneNumber;
        this.countryCode = countryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
