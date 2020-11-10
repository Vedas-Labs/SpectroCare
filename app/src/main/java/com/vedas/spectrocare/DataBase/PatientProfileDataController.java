package com.vedas.spectrocare.DataBase;

import android.util.Log;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.spectrocare.DataBaseModels.PatientlProfileModel;
import java.sql.SQLException;
import java.util.ArrayList;

public class PatientProfileDataController {
    public ArrayList<PatientlProfileModel> allPatientlProfile = new ArrayList<>();
    public PatientlProfileModel currentPatientlProfile;
    public static PatientProfileDataController myObj;

    public static PatientProfileDataController getInstance() {
        if (myObj == null) {
            myObj = new PatientProfileDataController();
        }
        return myObj;
    }

    public boolean insertPatientlProfileData(PatientlProfileModel userdata) {
        try {
            MedicalProfileDataController.getInstance().helper.getpatientlProfileDao().create(userdata);
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
                PatientlProfileModel userobj = allPatientlProfile.get(i);
                Log.e("curur", "" + userobj);
                Log.e("ActiveUser", userobj.getFirstName());
            }
        }
    }
    public ArrayList<PatientlProfileModel> fetchPatientlProfileData() {
        allPatientlProfile = null;
        allPatientlProfile = new ArrayList<>();
        try {
            allPatientlProfile = (ArrayList<PatientlProfileModel>)
                    MedicalProfileDataController.getInstance().helper.getpatientlProfileDao().queryForAll();
            /*if (allPatientlProfile.size() > 0) {
                currentPatientlProfile = allPatientlProfile.get()
                Log.e("currentUser", "call" + currentPatientlProfile.getEmailId());
            }*/
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.e("fetching", "patient profile data fectched successfully" + allPatientlProfile.size());
        return allPatientlProfile;
    }
    public boolean updateMedicalProfileData(PatientlProfileModel user) {
        try {
            UpdateBuilder<PatientlProfileModel, Integer> updateBuilder =  MedicalProfileDataController.getInstance().helper.getpatientlProfileDao().updateBuilder();

            updateBuilder.updateColumnValue("address", user.getAddress());
            updateBuilder.updateColumnValue("age", user.getAge());
            updateBuilder.updateColumnValue("city", user.getCity());
            updateBuilder.updateColumnValue("country", user.getCountry());
            updateBuilder.updateColumnValue("dob", user.getDob());
            updateBuilder.updateColumnValue("emailId", user.getEmailId());
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

            updateBuilder.where().eq("emailId", user.getEmailId());
            updateBuilder.update();

            Log.e("update data", "updated the data sucessfully");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void deletePatientlProfileModelData(ArrayList<PatientlProfileModel> user) {
        try {
            MedicalProfileDataController.getInstance().helper.getpatientlProfileDao().delete(user);
            fetchPatientlProfileData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public boolean deletePatientData(PatientlProfileModel member) {
        try {
            Log.e("Delete", "delete patient"+member.getPatientId());
            MedicalProfileDataController.getInstance().helper.getpatientlProfileDao().delete(member);
            fetchPatientlProfileData();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public PatientlProfileModel getPatientlProfileModelForEmail(String emailId) {
        if (allPatientlProfile.size() > 0) {
            for (int l = 0; l < allPatientlProfile.size(); l++) {
                PatientlProfileModel obUser = allPatientlProfile.get(l);
                if (obUser.getEmailId().equals(emailId)) {
                    return obUser;
                }
            }
        }
        return null;
    }
}
