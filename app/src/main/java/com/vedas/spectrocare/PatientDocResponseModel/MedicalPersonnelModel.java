package com.vedas.spectrocare.PatientDocResponseModel;

import com.vedas.spectrocare.PatientAppointmentModule.DoctorProfileModel;

import java.util.ArrayList;

public class MedicalPersonnelModel {

    ProfileModel profile;
    ServiceTimeModel serviceTime;
    ArrayList<ClinicalServices> clinicalServices;
    ArrayList reviews;
    ArrayList<TrackingModel> tracking;

    public ProfileModel getProfile() {
        return profile;
    }

    public void setProfile(ProfileModel profile) {
        this.profile = profile;
    }

    public ServiceTimeModel getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(ServiceTimeModel serviceTime) {
        this.serviceTime = serviceTime;
    }

    public ArrayList<ClinicalServices> getClinicalServices() {
        return clinicalServices;
    }

    public void setClinicalServices(ArrayList<ClinicalServices> clinicalServices) {
        this.clinicalServices = clinicalServices;
    }

    public ArrayList getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList reviews) {
        this.reviews = reviews;
    }

    public ArrayList<TrackingModel> getTracking() {
        return tracking;
    }

    public void setTracking(ArrayList<TrackingModel> tracking) {
        this.tracking = tracking;
    }

}
