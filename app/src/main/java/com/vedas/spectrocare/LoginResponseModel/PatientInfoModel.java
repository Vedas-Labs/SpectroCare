package com.vedas.spectrocare.LoginResponseModel;

import com.vedas.spectrocare.PatientMoreModule.LoginPhoneNumber;

import java.util.ArrayList;

public class PatientInfoModel {
    LoginPhoneNumber phoneNumber;
    ArrayList<LoginTrackingModel> tracking;
    String patientID;
    String medical_record_id;
    String firstName;
    String lastName;
    String password;
    String gender;
    String dob;
    int age;
    String emailID;
    String address;
    String city;
    String state;
    String country;
    String postalCode;
    String medical_personnel_id;
    String hospital_reg_num;
    String profilePic;
    ArrayList loc;
    AppSettingsModel appSettings;
    String createdDate;
    String updatedDate;

    public LoginPhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(LoginPhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ArrayList<LoginTrackingModel> getTracking() {
        return tracking;
    }

    public void setTracking(ArrayList<LoginTrackingModel> tracking) {
        this.tracking = tracking;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getMedical_record_id() {
        return medical_record_id;
    }

    public void setMedical_record_id(String medical_record_id) {
        this.medical_record_id = medical_record_id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public ArrayList getLoc() {
        return loc;
    }

    public void setLoc(ArrayList loc) {
        this.loc = loc;
    }

    public AppSettingsModel getAppSettings() {
        return appSettings;
    }

    public void setAppSettings(AppSettingsModel appSettings) {
        this.appSettings = appSettings;
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
