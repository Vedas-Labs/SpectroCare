package com.vedas.spectrocare.PatientDocResponseModel;

public class ProfileModel {
    UserProfileModel userProfile;
    GeneralInformation generalInformation;
    String biography;

    public String getBiography() {
        return biography;
    }

    public GeneralInformation getGeneralInformation() {
        return generalInformation;
    }

    public void setGeneralInformation(GeneralInformation generalInformation) {
        this.generalInformation = generalInformation;
    }
    public UserProfileModel getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfileModel userProfile) {
        this.userProfile = userProfile;
    }
}
