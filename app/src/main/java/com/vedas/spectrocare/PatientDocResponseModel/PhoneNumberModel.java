package com.vedas.spectrocare.PatientDocResponseModel;

import java.io.Serializable;

public class PhoneNumberModel implements Serializable {
    String countryCode;
    String phoneNumber;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
