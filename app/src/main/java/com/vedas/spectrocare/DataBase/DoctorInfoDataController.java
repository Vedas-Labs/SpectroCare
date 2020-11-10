package com.vedas.spectrocare.DataBase;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.spectrocare.DataBaseModels.DoctorInfoModel;

import java.sql.SQLException;
import java.util.ArrayList;

public class DoctorInfoDataController {
    public ArrayList<DoctorInfoModel> allDoctorInfo = new ArrayList<>();
    public DoctorInfoModel currentDoctorInfo;
    public static DoctorInfoDataController myObj;
    public static DoctorInfoDataController getInstance() {
        if (myObj == null) {
            myObj = new DoctorInfoDataController();
        }
        return myObj;
    }
   
    public boolean insertDoctorInfoData(DoctorInfoModel userdata) {
        try {
           MedicalProfileDataController.getInstance(). helper.getDoctorInfoModelsDao().create(userdata);
            fetchDoctorInfoData();
            Log.e("fetc", "" + allDoctorInfo);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public ArrayList<DoctorInfoModel> fetchDoctorInfoData() {
        allDoctorInfo = null;
        allDoctorInfo = new ArrayList<>();
        try {
            allDoctorInfo = (ArrayList<DoctorInfoModel>) MedicalProfileDataController.getInstance(). helper.getDoctorInfoModelsDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.e("fetching", "doctor fectched successfully" + allDoctorInfo.size());
        return allDoctorInfo;
    }
    public void deleteDoctorInfoData(ArrayList<DoctorInfoModel> user) {
        try {
            MedicalProfileDataController.getInstance(). helper.getDoctorInfoModelsDao().delete(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
