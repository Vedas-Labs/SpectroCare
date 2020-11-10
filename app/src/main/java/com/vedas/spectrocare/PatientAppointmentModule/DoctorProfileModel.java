package com.vedas.spectrocare.PatientAppointmentModule;

import com.vedas.spectrocare.PatientDocResponseModel.UserProfileModel;

import java.io.Serializable;

public class DoctorProfileModel implements Serializable {
    UserProfileModel userProfile;

    public UserProfileModel getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfileModel userProfile) {
        this.userProfile = userProfile;
    }
}
