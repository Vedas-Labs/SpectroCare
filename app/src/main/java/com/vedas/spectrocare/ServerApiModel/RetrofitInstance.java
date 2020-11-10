package com.vedas.spectrocare.ServerApiModel;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class RetrofitInstance {
    @SerializedName("userID")
    private String userID;
    @SerializedName("access_token")
    private String access_token;
    @SerializedName("response")
    private String response;
    @SerializedName("password")
    private String password;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("phoneNumberCountryCode")
    private String phoneNumberCountryCode;
    @SerializedName("emailID")
    private String emailID;
    @SerializedName("userType")
    private String userType;
    @SerializedName("specialization")
    private String specialization;
    @SerializedName("qualification")
    private String qualification;
    @SerializedName("department")
    private String department;
    @SerializedName("id_by_govt")
    private String id_by_govt;
    @SerializedName("preferLanguage")
    private String preferLanguage;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("hospital_reg_num")
    private String hospital_reg_num;
    @SerializedName("gender")
    private String gender;
    @SerializedName("age")
    private String age;
    @SerializedName("message")
    private String message;
    @SerializedName("profilePic")
    private String profilePic;
    @SerializedName("medicalPersonnel")
    private JsonObject medicalPersonnel;

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }



    public String getUserType() {
        return userType;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumberCountryCode() {
        return phoneNumberCountryCode;
    }

    public void setPhoneNumberCountryCode(String phoneNumberCountryCode) {
        this.phoneNumberCountryCode = phoneNumberCountryCode;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getId_by_govt() {
        return id_by_govt;
    }

    public void setId_by_govt(String id_by_govt) {
        this.id_by_govt = id_by_govt;
    }

    public String getPreferLanguage() {
        return preferLanguage;
    }

    public void setPreferLanguage(String preferLanguage) {
        this.preferLanguage = preferLanguage;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getHospital_reg_num() {
        return hospital_reg_num;
    }

    public void setHospital_reg_num(String hospital_reg_num) {
        this.hospital_reg_num = hospital_reg_num;
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

    public String getUserID() {
        return userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String Password) {
        password = Password;
    }

    public void setUserID(String UserID) { userID = UserID;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
