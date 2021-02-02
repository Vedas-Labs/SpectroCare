package com.vedas.spectrocare.DataBaseModels;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "patientLogin")
public class PatientModel {

    @DatabaseField(generatedId = true)
    private int Id;

    @DatabaseField
    private String address;

    @DatabaseField
    private String age;

    @DatabaseField
    private String city;

    @DatabaseField
    private String country;

    @DatabaseField
    private String dob;

    @DatabaseField
    private String emailId;

    @DatabaseField
    private String firstName;

    @DatabaseField
    private String medicalRecordId;

    @DatabaseField
    private String gender;

    @DatabaseField
    private String hospital_reg_number;

    @DatabaseField
    private String lastName;

    @DatabaseField
    private String latitude;

    @DatabaseField
    private String longitude;

    @DatabaseField
    private String medicalPerson_id;

    @DatabaseField
    private String patientId;

    @DatabaseField
    private String phoneNumber;

    @DatabaseField
    private String phone_coutryCode;

    @DatabaseField
    private String postalCode;

    @DatabaseField
    private String accessToken;

    @DatabaseField
    private String profilePic;

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    private byte[] profileByteArray;

    @DatabaseField
    private String state;

    @ForeignCollectionField
    private ForeignCollection<UrineresultsModel> urineresultsModels;

    public ForeignCollection<UrineresultsModel> getUrineresultsModels() {
        return urineresultsModels;
    }

    public void setUrineresultsModels(ForeignCollection<UrineresultsModel> urineresultsModels) {
        this.urineresultsModels = urineresultsModels;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(String medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHospital_reg_number() {
        return hospital_reg_number;
    }

    public void setHospital_reg_number(String hospital_reg_number) {
        this.hospital_reg_number = hospital_reg_number;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getMedicalPerson_id() {
        return medicalPerson_id;
    }

    public void setMedicalPerson_id(String medicalPerson_id) {
        this.medicalPerson_id = medicalPerson_id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhone_coutryCode() {
        return phone_coutryCode;
    }

    public void setPhone_coutryCode(String phone_coutryCode) {
        this.phone_coutryCode = phone_coutryCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public byte[] getProfileByteArray() {
        return profileByteArray;
    }

    public void setProfileByteArray(byte[] profileByteArray) {
        this.profileByteArray = profileByteArray;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}


