package com.vedas.spectrocare.DataBaseModels;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "medicalProfile")
public class MedicalProfileModel {

    @DatabaseField(generatedId = true)
    private int profileId;

    @DatabaseField
    private String accessToken;

    @DatabaseField
    private String age;

    @DatabaseField
    private String department;

    @DatabaseField
    private String dob;

    @DatabaseField
    private String emailId;

    @DatabaseField
    private String firstName;

    @DatabaseField
    private String gender;

    @DatabaseField
    private String hospital_reg_number;

    @DatabaseField
    private String id_By_govt;

    @DatabaseField
    private String lastName;

    @DatabaseField
    private String lotitude;

    @DatabaseField
    private String longitude;

    @DatabaseField
    private String medical_person_id;

    @DatabaseField
    private String password;

    @DatabaseField
    private String phoneNumber;

    @DatabaseField
    private String phone_coutryCode;

    @DatabaseField
    private String PrefferLanguage;

    @DatabaseField
    private String qualification;

    @DatabaseField
    private String specialzation;

    @DatabaseField
    private String userId;

    @DatabaseField
    private String userType;


    public byte[] getProfileByteArray() {
        return profileByteArray;
    }

    public void setProfileByteArray(byte[] profileByteArray) {
        this.profileByteArray = profileByteArray;
    }

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    private byte[] profileByteArray;

    @DatabaseField
    private String profilePic;

    @ForeignCollectionField
    private ForeignCollection<PatientlProfileModel> patientlProfileModels;

    public ForeignCollection<PatientlProfileModel> getPatientlProfileModels() {
        return patientlProfileModels;
    }

    public void setPatientlProfileModels(ForeignCollection<PatientlProfileModel> patientlProfileModels) {
        this.patientlProfileModels = patientlProfileModels;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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

    public String getId_By_govt() {
        return id_By_govt;
    }

    public void setId_By_govt(String id_By_govt) {
        this.id_By_govt = id_By_govt;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLotitude() {
        return lotitude;
    }

    public void setLotitude(String lotitude) {
        this.lotitude = lotitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMedical_person_id() {
        return medical_person_id;
    }

    public void setMedical_person_id(String medical_person_id) {
        this.medical_person_id = medical_person_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getPrefferLanguage() {
        return PrefferLanguage;
    }

    public void setPrefferLanguage(String prefferLanguage) {
        PrefferLanguage = prefferLanguage;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getSpecialzation() {
        return specialzation;
    }

    public void setSpecialzation(String specialzation) {
        this.specialzation = specialzation;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }


}
