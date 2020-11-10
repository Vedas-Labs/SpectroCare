package com.vedas.spectrocare.model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.vedas.spectrocare.LoginResponseModel.AppSettingsModel;
//import com.vedas.spectrocare.PatientServerObjects.AppSettingsModel;
import com.vedas.spectrocare.ServerApiModel.TrackingServerObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PatientList {
    String firstName;
    String lastName;
    String age;
    String patientID;
    String gender;
    String profilePic;
    String medicId;
    String hospReg;
    String emailID;
    String address;
    String city;
    String adres;
    String state;
    String country;
    String postalCode;
    String dob;
    String medical_record_id;
    String medical_personnel_id;
    String hospital_reg_num;
    private JsonObject phoneNumber;

    private AppSettingsModel appSettings;

    public AppSettingsModel getAppSettings() {
        return appSettings;
    }

    public void setAppSettings(AppSettingsModel appSettings) {
        this.appSettings = appSettings;
    }

    public String getMedical_personnel_id() {
        return medical_personnel_id;
    }

    public void setMedical_personnel_id(String medical_personnel_id) {
        this.medical_personnel_id = medical_personnel_id;
    }

    public String getHospital_reg_num() {
        return hospital_reg_num;
    }

    public void setHospital_reg_num(String hospital_reg_num) {
        this.hospital_reg_num = hospital_reg_num;
    }

    public List<TrackingServerObject> getTracking() {
        return tracking;
    }

    public String getMedical_record_id() {
        return medical_record_id;
    }

    public void setMedical_record_id(String medical_record_id) {
        this.medical_record_id = medical_record_id;
    }

    public void setTracking(List<TrackingServerObject> tracking) {
        this.tracking = tracking;
    }

    private List<TrackingServerObject> tracking;

    public JsonObject getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(JsonObject phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getMedicId() {
        return medicId;
    }

    public void setMedicId(String medicId) {
        this.medicId = medicId;
    }

    public String getHospReg() {
        return hospReg;
    }

    public void setHospReg(String hospReg) {
        this.hospReg = hospReg;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
