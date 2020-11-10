package com.vedas.spectrocare.DataBase;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;

import java.sql.SQLException;
import java.util.ArrayList;

public class MedicalProfileDataController {
    public DataBaseHelper helper;
    public ArrayList<MedicalProfileModel> allMedicalProfile = new ArrayList<>();
    public MedicalProfileModel currentMedicalProfile;
    public static MedicalProfileDataController myObj;

    public static MedicalProfileDataController getInstance() {
        if (myObj == null) {
            myObj = new MedicalProfileDataController();
        }
        return myObj;
    }
    public void fillContext(Context context1) {
        Log.e("DBStatus", "Fill Context Called");
        helper = new DataBaseHelper(context1);
    }
    public boolean insertMedicalProfileData(MedicalProfileModel userdata) {
        try {
            helper.getMedicalProfileDao().create(userdata);
            fetchMedicalProfileData();
            Log.e("fetc", "" + allMedicalProfile);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void getCurrentMedicalProfile() {
        if (allMedicalProfile.size() > 0) {
            for (int i = 0; i < allMedicalProfile.size(); i++) {
                MedicalProfileModel userobj = allMedicalProfile.get(i);
                Log.e("curur", "" + userobj);
                Log.e("ActiveUser", userobj.getFirstName());
            }
        }
    }
    public ArrayList<MedicalProfileModel> fetchMedicalProfileData() {
        allMedicalProfile = null;
        allMedicalProfile = new ArrayList<>();

        try {
            allMedicalProfile = (ArrayList<MedicalProfileModel>) helper.getMedicalProfileDao().queryForAll();
            if (allMedicalProfile.size() > 0) {
                currentMedicalProfile = allMedicalProfile.get(0);
                Log.e("currentUser", "call" + currentMedicalProfile.getEmailId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.e("fetching", "medical profile data fectched successfully" + allMedicalProfile.size());
        return allMedicalProfile;
    }
    public boolean updateMedicalProfileData(MedicalProfileModel user) {
        try {
            UpdateBuilder<MedicalProfileModel, Integer> updateBuilder = helper.getMedicalProfileDao().updateBuilder();
            updateBuilder.updateColumnValue("accessToken", user.getAccessToken());
            updateBuilder.updateColumnValue("age", user.getAge());
            updateBuilder.updateColumnValue("department", user.getDepartment());
            updateBuilder.updateColumnValue("dob", user.getDob());
            updateBuilder.updateColumnValue("emailId", user.getEmailId());
            updateBuilder.updateColumnValue("firstName", user.getFirstName());
            updateBuilder.updateColumnValue("gender", user.getGender());
            updateBuilder.updateColumnValue("hospital_reg_number", user.getHospital_reg_number());
            updateBuilder.updateColumnValue("id_By_govt", user.getId_By_govt());
            updateBuilder.updateColumnValue("lastName", user.getLastName());
            updateBuilder.updateColumnValue("lotitude", user.getLotitude());
            updateBuilder.updateColumnValue("longitude", user.getLongitude());
            updateBuilder.updateColumnValue("medical_person_id", user.getMedical_person_id());
            updateBuilder.updateColumnValue("password", user.getPassword());
            updateBuilder.updateColumnValue("phoneNumber", user.getPhoneNumber());
            updateBuilder.updateColumnValue("phone_coutryCode", user.getPhone_coutryCode());
            updateBuilder.updateColumnValue("PrefferLanguage", user.getPrefferLanguage());
            updateBuilder.updateColumnValue("qualification", user.getQualification());
            updateBuilder.updateColumnValue("specialzation", user.getSpecialzation());
            updateBuilder.updateColumnValue("userId", user.getUserId());
            updateBuilder.updateColumnValue("userType", user.getUserType());
            updateBuilder.updateColumnValue("profilePic", user.getProfilePic());
            updateBuilder.updateColumnValue("profileByteArray", user.getProfileByteArray());
            updateBuilder.where().eq("emailId", user.getEmailId());
            updateBuilder.update();
            Log.e("update data", "updated the data sucessfully"+user.getUserId());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return  false;
        }
    }
    public void deleteMedicalProfileData(ArrayList<MedicalProfileModel> user) {
        try {
            helper.getMedicalProfileDao().delete(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public MedicalProfileModel getUserObjectForEmail(String emailId) {
        if (allMedicalProfile.size() > 0) {
            for (int l = 0; l < allMedicalProfile.size(); l++) {
                MedicalProfileModel obUser = allMedicalProfile.get(l);
                if (obUser.getEmailId().equals(emailId)) {
                    return obUser;
                }
            }
        }
        return null;
    }
}
