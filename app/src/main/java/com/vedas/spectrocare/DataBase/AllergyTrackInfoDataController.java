package com.vedas.spectrocare.DataBase;

import android.util.Log;

import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.spectrocare.DataBaseModels.AllergieModel;
import com.vedas.spectrocare.DataBaseModels.FamilyHistoryModel;
import com.vedas.spectrocare.DataBaseModels.AllergyTrackInfoModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AllergyTrackInfoDataController {
    public ArrayList<AllergyTrackInfoModel> allTrackList = new ArrayList<>();
    public static AllergyTrackInfoDataController myObj;

    public static AllergyTrackInfoDataController getInstance() {
        if (myObj == null) {
            myObj = new AllergyTrackInfoDataController();
        }
        return myObj;
    }

    public boolean insertTrackData(AllergyTrackInfoModel userdata) {
        try {
            MedicalProfileDataController.getInstance().helper.getAllergyTrackInfoDao().create(userdata);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<AllergyTrackInfoModel> fetchTrackData(AllergieModel allergieModel) {
        allTrackList = null;
        allTrackList = new ArrayList<>();
        if(allergieModel != null){
            ArrayList<AllergyTrackInfoModel> urineresultsModels = new ArrayList<AllergyTrackInfoModel>(allergieModel.getAllergyTrackInfoModels());
            if(urineresultsModels != null) {
                allTrackList = sortUrineResultsBasedOnTime(urineresultsModels);
                Log.e("chanufetching", "track fectched successfully" + allTrackList.size());
            }
        }
        return allTrackList;
    }
    public ArrayList<AllergyTrackInfoModel> sortUrineResultsBasedOnTime(ArrayList<AllergyTrackInfoModel> allTrackList) {
        Collections.sort(allTrackList, new Comparator<AllergyTrackInfoModel>() {
            @Override
            public int compare(AllergyTrackInfoModel s1, AllergyTrackInfoModel s2) {
                return s1.getDate().compareTo(s2.getDate());
            }
        });
        return allTrackList;

    }
    public ArrayList<AllergyTrackInfoModel> fetchFamilyTrackingBasedOnAllergyRecorsID(AllergieModel allergieModel) {
        ArrayList<AllergyTrackInfoModel> AllergyTrackInfoModelArrayList=new ArrayList<>();
        if (allergieModel != null) {
            ArrayList<AllergyTrackInfoModel> list = new ArrayList<AllergyTrackInfoModel>(allergieModel.getAllergyTrackInfoModels());
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getAllergyRecordId().equals(allergieModel.getAllergyRecordId())) {
                        AllergyTrackInfoModel physicalExamModel = list.get(i);
                        Log.e("dbchandu", "bmi" + physicalExamModel.getAllergyRecordId());
                        AllergyTrackInfoModelArrayList.add(physicalExamModel);
                    }
                }
                Log.e("familyTrackArrayList", "call" + AllergyTrackInfoModelArrayList.size());
                return AllergyTrackInfoModelArrayList;
            }
        }
        return null;
    }
    public boolean updateTrackData(AllergyTrackInfoModel user) {
        try {
            UpdateBuilder<AllergyTrackInfoModel, Integer> updateBuilder = MedicalProfileDataController.getInstance().helper.getAllergyTrackInfoDao().updateBuilder();
            updateBuilder.updateColumnValue("byWhom", user.getByWhom());
            updateBuilder.updateColumnValue("byWhomId", user.getByWhomId());
            updateBuilder.updateColumnValue("date", user.getDate());
            updateBuilder.where().eq("allergyRecordId", user.getAllergyRecordId());
            updateBuilder.update();
            Log.e("update data", "updated the Track data sucessfully"+user.getAllergyRecordId());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return  false;
        }
    }

    public boolean deleteTrackData(AllergyTrackInfoModel member) {
        try {
            MedicalProfileDataController.getInstance().helper.getAllergyTrackInfoDao().delete(member);
            //fetchTrackData();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteTrackData(ArrayList<AllergieModel> patientlProfileModel) {
        for(int i=0;i<patientlProfileModel.size();i++) {
            if (patientlProfileModel != null) {
                ArrayList<AllergyTrackInfoModel> urineresultsModels = new ArrayList<AllergyTrackInfoModel>(patientlProfileModel.get(i).getAllergyTrackInfoModels());
                if (urineresultsModels != null) {
                    try {
                        MedicalProfileDataController.getInstance().helper.getAllergyTrackInfoDao().delete(urineresultsModels);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Log.e("deletefetching", "track fectched successfully" + allTrackList.size());
                }
            }
        }
    }
    /*public void deleteTrackData(ArrayList<AllergyTrackInfoModel> AllergyTrackInfoModels) {
        try {
            if(AllergyTrackInfoModels.size()>0){
                for(AllergyTrackInfoModel AllergyTrackInfoModel: AllergyTrackInfoModels) {
                    DeleteBuilder<AllergyTrackInfoModel, Integer> deleteBuilder = MedicalProfileDataController.getInstance().helper.getTrackInfoDao().deleteBuilder();
                    deleteBuilder.where().eq("patientId", AllergyTrackInfoModel.getId());
                    deleteBuilder.delete();
                    Log.e("deleteWifiDetailsData", "sucessfully"+AllergyTrackInfoModels.size());
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
