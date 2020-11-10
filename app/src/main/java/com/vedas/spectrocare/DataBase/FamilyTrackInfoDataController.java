package com.vedas.spectrocare.DataBase;

import android.util.Log;

import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.spectrocare.DataBaseModels.FamilyHistoryModel;
import com.vedas.spectrocare.DataBaseModels.PatientlProfileModel;
import com.vedas.spectrocare.DataBaseModels.FamilyTrackInfoModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FamilyTrackInfoDataController {
    public ArrayList<FamilyTrackInfoModel> allTrackList = new ArrayList<>();
    public static FamilyTrackInfoDataController myObj;

    public static FamilyTrackInfoDataController getInstance() {
        if (myObj == null) {
            myObj = new FamilyTrackInfoDataController();
        }
        return myObj;
    }

    public boolean insertTrackData(FamilyTrackInfoModel userdata) {
        try {
            MedicalProfileDataController.getInstance().helper.getFamilyTrackInfoDao().create(userdata);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<FamilyTrackInfoModel> fetchTrackData(FamilyHistoryModel familyHistoryModel) {
        allTrackList = null;
        allTrackList = new ArrayList<>();
        if(familyHistoryModel != null){
            ArrayList<FamilyTrackInfoModel> urineresultsModels = new ArrayList<FamilyTrackInfoModel>(familyHistoryModel.getFamilyTrackInfoModels());
            if(urineresultsModels != null) {
                allTrackList = sortUrineResultsBasedOnTime(urineresultsModels);
                Log.e("chanufetching", "track fectched successfully" + allTrackList.size());
            }
        }
        return allTrackList;
    }
    public ArrayList<FamilyTrackInfoModel> sortUrineResultsBasedOnTime(ArrayList<FamilyTrackInfoModel> allTrackList) {
        Collections.sort(allTrackList, new Comparator<FamilyTrackInfoModel>() {
            @Override
            public int compare(FamilyTrackInfoModel s1, FamilyTrackInfoModel s2) {
                return s1.getDate().compareTo(s2.getDate());
            }
        });
        return allTrackList;

    }
    public ArrayList<FamilyTrackInfoModel> fetchFamilyTrackingBasedOnFamilyRecorsID(FamilyHistoryModel familyHistoryModel) {
        ArrayList<FamilyTrackInfoModel> familyTrackInfoModelArrayList=new ArrayList<>();
        if (familyHistoryModel != null) {
            ArrayList<FamilyTrackInfoModel> list = new ArrayList<FamilyTrackInfoModel>(familyHistoryModel.getFamilyTrackInfoModels());
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getFamilyRecordId().equals(familyHistoryModel.getFamilyRecordId())) {
                        FamilyTrackInfoModel physicalExamModel = list.get(i);
                        Log.e("dbchandu", "bmi" + physicalExamModel.getFamilyRecordId());
                        familyTrackInfoModelArrayList.add(physicalExamModel);
                    }
                }
                Log.e("familyTrackArrayList", "call" + familyTrackInfoModelArrayList.size());
                return familyTrackInfoModelArrayList;
            }
        }
        return null;
    }
    public boolean updateTrackData(FamilyTrackInfoModel user) {
        try {
            UpdateBuilder<FamilyTrackInfoModel, Integer> updateBuilder = MedicalProfileDataController.getInstance().helper.getFamilyTrackInfoDao().updateBuilder();
            updateBuilder.updateColumnValue("byWhom", user.getByWhom());
            updateBuilder.updateColumnValue("byWhomId", user.getByWhomId());
            updateBuilder.updateColumnValue("date", user.getDate());
            updateBuilder.where().eq("familyRecordId", user.getFamilyRecordId());
            updateBuilder.update();
            Log.e("update data", "updated the Track data sucessfully"+user.getFamilyRecordId());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return  false;
        }
    }

    public boolean deleteTrackData(FamilyTrackInfoModel member) {
        try {
            MedicalProfileDataController.getInstance().helper.getFamilyTrackInfoDao().delete(member);
            //fetchTrackData();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteTrackData(ArrayList<FamilyHistoryModel> patientlProfileModel) {
        for(int i=0;i<patientlProfileModel.size();i++) {
            if (patientlProfileModel != null) {
                ArrayList<FamilyTrackInfoModel> urineresultsModels = new ArrayList<FamilyTrackInfoModel>(patientlProfileModel.get(i).getFamilyTrackInfoModels());
                if (urineresultsModels != null) {
                    try {
                        MedicalProfileDataController.getInstance().helper.getFamilyTrackInfoDao().delete(urineresultsModels);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Log.e("deletefetching", "track fectched successfully" + allTrackList.size());
                }
            }
        }
    }
    /*public void deleteTrackData(ArrayList<FamilyTrackInfoModel> FamilyTrackInfoModels) {
        try {
            if(FamilyTrackInfoModels.size()>0){
                for(FamilyTrackInfoModel FamilyTrackInfoModel: FamilyTrackInfoModels) {
                    DeleteBuilder<FamilyTrackInfoModel, Integer> deleteBuilder = MedicalProfileDataController.getInstance().helper.getTrackInfoDao().deleteBuilder();
                    deleteBuilder.where().eq("patientId", FamilyTrackInfoModel.getId());
                    deleteBuilder.delete();
                    Log.e("deleteWifiDetailsData", "sucessfully"+FamilyTrackInfoModels.size());
                    // MedicalProfileDataController.getInstance().helper.getTrackInfoDao().delete(user);
                }
               // fetchTrackData();
        }
        } catch (SQLException e) {
            Log.e("excetpion", "sucessfully"+e.getMessage());
            e.printStackTrace();
        }

    }*/

}
