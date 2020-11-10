package com.vedas.spectrocare.PatientDocResponseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class UserProfileModel implements Serializable {
    PhoneNumberModel phoneNumber;
    EmergencyNumberModel emergencyPhoneNumber;
    boolean verificationStatus;
    String preferLanguage;
    boolean hospitalApprove;
    ArrayList loc;
    String hospital_reg_num;
    String userID;
    String password;
    String lastName;
    String firstName;
    String gender;
    String age;
    String emailID;
    String medical_personnel_id;
    String profilePic;
    String userType;
    String department;
    String registerTime;

    public PhoneNumberModel getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumberModel phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public EmergencyNumberModel getEmergencyPhoneNumber() {
        return emergencyPhoneNumber;
    }

    public void setEmergencyPhoneNumber(EmergencyNumberModel emergencyPhoneNumber) {
        this.emergencyPhoneNumber = emergencyPhoneNumber;
    }

    public boolean isVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(boolean verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public String getPreferLanguage() {
        return preferLanguage;
    }

    public void setPreferLanguage(String preferLanguage) {
        this.preferLanguage = preferLanguage;
    }

    public boolean isHospitalApprove() {
        return hospitalApprove;
    }

    public void setHospitalApprove(boolean hospitalApprove) {
        this.hospitalApprove = hospitalApprove;
    }

    public ArrayList getLoc() {
        return loc;
    }

    public void setLoc(ArrayList loc) {
        this.loc = loc;
    }

    public String getHospital_reg_num() {
        return hospital_reg_num;
    }

    public void setHospital_reg_num(String hospital_reg_num) {
        this.hospital_reg_num = hospital_reg_num;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getMedical_personnel_id() {
        return medical_personnel_id;
    }

    public void setMedical_personnel_id(String medical_personnel_id) {
        this.medical_personnel_id = medical_personnel_id;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }
}
