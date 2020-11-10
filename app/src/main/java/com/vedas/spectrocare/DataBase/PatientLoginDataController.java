package com.vedas.spectrocare.DataBase;

import android.util.Log;

import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.spectrocare.DataBaseModels.PatientModel;

import java.sql.SQLException;
import java.util.ArrayList;

public class PatientLoginDataController {
    public ArrayList<PatientModel> allPatientlProfile = new ArrayList<>();
    public PatientModel currentPatientlProfile;
    public static PatientLoginDataController myObj;

    public static PatientLoginDataController getInstance() {
        if (myObj == null) {
            myObj = new PatientLoginDataController();
        }
        return myObj;
    }

    public boolean insertPatientData(PatientModel userdata) {
        try {
            MedicalProfileDataController.getInstance().helper.getpatientlModelDao().create(userdata);
            fetchPatientlProfileData();
            Log.e("fetc", "" + allPatientlProfile);
            return  true;
        } catch (SQLException e) {
            e.printStackTrace();
            return  false;
        }
    }

    public void getCurrentPatientlProfile() {
        if (allPatientlProfile.size() > 0) {
            for (int i = 0; i < allPatientlProfile.size(); i++) {
                PatientModel userobj = allPatientlProfile.get(i);
                Log.e("curur", "" + userobj);
                Log.e("ActiveUser", userobj.getFirstName());
            }
        }
    }
    public ArrayList<PatientModel> fetchPatientlProfileData() {
        allPatientlProfile = null;
        allPatientlProfile = new ArrayList<>();
        try {
            allPatientlProfile = (ArrayList<PatientModel>) MedicalProfileDataController.getInstance().helper.getpatientlModelDao().queryForAll();
            if(allPatientlProfile.size()>0){
                currentPatientlProfile=allPatientlProfile.get(allPatientlProfile.size()-1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.e("fetching", "patient profile data fectched successfully" + allPatientlProfile.size());
        return allPatientlProfile;
    }
    public boolean updateMedicalProfileData(PatientModel user) {
        try {
            UpdateBuilder<PatientModel, Integer> updateBuilder =  MedicalProfileDataController.getInstance().helper.getpatientlModelDao().updateBuilder();

            updateBuilder.updateColumnValue("address", user.getAddress());
            updateBuilder.updateColumnValue("age", user.getAge());
            updateBuilder.updateColumnValue("city", user.getCity());
            updateBuilder.updateColumnValue("country", user.getCountry());
            updateBuilder.updateColumnValue("dob", user.getDob());
            updateBuilder.updateColumnValue("emailId", user.getEmailId());
            Log.e("mailID",""+ user.getEmailId());
            updateBuilder.updateColumnValue("firstName", user.getFirstName());
            updateBuilder.updateColumnValue("gender", user.getGender());
            updateBuilder.updateColumnValue("hospital_reg_number", user.getHospital_reg_number());
            updateBuilder.updateColumnValue("lastName", user.getLastName());
            updateBuilder.updateColumnValue("latitude", user.getLatitude());
            updateBuilder.updateColumnValue("longitude", user.getLongitude());
            updateBuilder.updateColumnValue("medicalPerson_id", user.getMedicalPerson_id());
            updateBuilder.updateColumnValue("patientId", user.getPatientId());
            updateBuilder.updateColumnValue("phoneNumber", user.getPhoneNumber());
            updateBuilder.updateColumnValue("profilePic", user.getProfileByteArray());
            updateBuilder.updateColumnValue("phone_coutryCode", user.getPhone_coutryCode());
            updateBuilder.updateColumnValue("postalCode", user.getPostalCode());
            updateBuilder.updateColumnValue("state", user.getState());
            updateBuilder.updateColumnValue("profilePic", user.getProfilePic());
            updateBuilder.updateColumnValue("medicalRecordId", user.getMedicalRecordId());

            updateBuilder.where().eq("patientId",user.getPatientId());
            updateBuilder.update();

            Log.e("update data", "updated the data sucessfully");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void deletePatientModelData(ArrayList<PatientModel> user) {
        try {
            MedicalProfileDataController.getInstance().helper.getpatientlModelDao().delete(user);
            fetchPatientlProfileData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public boolean deletePatientData(PatientModel member) {
        try {
            Log.e("Delete", "delete patient"+member.getPatientId());
            MedicalProfileDataController.getInstance().helper.getpatientlModelDao().delete(member);
            fetchPatientlProfileData();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public PatientModel getPatientModelForEmail(String emailId) {
        if (allPatientlProfile.size() > 0) {
            for (int l = 0; l < allPatientlProfile.size(); l++) {
                PatientModel obUser = allPatientlProfile.get(l);
                if (obUser.getEmailId().equals(emailId)) {
                    return obUser;
                }
            }
        }
        return null;
    }
}
