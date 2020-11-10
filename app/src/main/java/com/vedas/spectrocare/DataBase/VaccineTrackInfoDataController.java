package com.vedas.spectrocare.DataBase;

import android.util.Log;

import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.spectrocare.DataBaseModels.VaccineModel;
import com.vedas.spectrocare.DataBaseModels.VaccineTrackInfoModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class VaccineTrackInfoDataController {
    public ArrayList<VaccineTrackInfoModel> allTrackList = new ArrayList<>();
    public static VaccineTrackInfoDataController myObj;

    public static VaccineTrackInfoDataController getInstance() {
        if (myObj == null) {
            myObj = new VaccineTrackInfoDataController();
        }
        return myObj;
    }

    public boolean insertTrackData(VaccineTrackInfoModel userdata) {
        try {
            MedicalProfileDataController.getInstance().helper.getVaccineTrackInfoDao().create(userdata);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<VaccineTrackInfoModel> fetchTrackData(VaccineModel vaccineModel) {
        allTrackList = null;
        allTrackList = new ArrayList<>();
        if(vaccineModel != null){
            ArrayList<VaccineTrackInfoModel> urineresultsModels = new ArrayList<VaccineTrackInfoModel>(vaccineModel.getVaccineTrackInfoModels());
            if(urineresultsModels != null) {
                allTrackList = sortUrineResultsBasedOnTime(urineresultsModels);
                Log.e("chanufetching", "track fectched successfully" + allTrackList.size());
            }
        }
        return allTrackList;
    }
    public ArrayList<VaccineTrackInfoModel> sortUrineResultsBasedOnTime(ArrayList<VaccineTrackInfoModel> allTrackList) {
        Collections.sort(allTrackList, new Comparator<VaccineTrackInfoModel>() {
            @Override
            public int compare(VaccineTrackInfoModel s1, VaccineTrackInfoModel s2) {
                return s1.getDate().compareTo(s2.getDate());
            }
        });
        return allTrackList;

    }
    public ArrayList<VaccineTrackInfoModel> fetchVaccineTrackingBasedOnVaccineRecorsID(VaccineModel vaccineModel) {
        ArrayList<VaccineTrackInfoModel> vaccineTrackInfoModelArrayList=new ArrayList<>();
        if (vaccineModel != null) {
            ArrayList<VaccineTrackInfoModel> list = new ArrayList<VaccineTrackInfoModel>(vaccineModel.getVaccineTrackInfoModels());
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getVaccineRecordId().equals(vaccineModel.getVaccineRecordId())) {
                        VaccineTrackInfoModel physicalExamModel = list.get(i);
                        vaccineTrackInfoModelArrayList.add(physicalExamModel);
                    }
                }
                Log.e("familyTrackArrayList", "call" + vaccineTrackInfoModelArrayList.size());
                return vaccineTrackInfoModelArrayList;
            }
        }
        return null;
    }
    public boolean updateTrackData(VaccineTrackInfoModel user) {
        try {
            UpdateBuilder<VaccineTrackInfoModel, Integer> updateBuilder = MedicalProfileDataController.getInstance().helper.getVaccineTrackInfoDao().updateBuilder();
            updateBuilder.updateColumnValue("byWhom", user.getByWhom());
            updateBuilder.updateColumnValue("byWhomId", user.getByWhomId());
            updateBuilder.updateColumnValue("date", user.getDate());
            updateBuilder.where().eq("vaccineRecordId", user.getVaccineRecordId());
            updateBuilder.update();
            Log.e("update data", "updated the Track data sucessfully"+user.getVaccineRecordId());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return  false;
        }
    }

    public boolean deleteTrackData(VaccineTrackInfoModel member) {
        try {
            MedicalProfileDataController.getInstance().helper.getVaccineTrackInfoDao().delete(member);
            //fetchTrackData();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteTrackData(ArrayList<VaccineModel> patientlProfileModel) {
        for(int i=0;i<patientlProfileModel.size();i++) {
            if (patientlProfileModel != null) {
                ArrayList<VaccineTrackInfoModel> urineresultsModels = new ArrayList<VaccineTrackInfoModel>(patientlProfileModel.get(i).getVaccineTrackInfoModels());
                if (urineresultsModels != null) {
                    try {
                        MedicalProfileDataController.getInstance().helper.getVaccineTrackInfoDao().delete(urineresultsModels);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Log.e("deletefetching", "track fectched successfully" + allTrackList.size());
                }
            }
        }
    }

}
