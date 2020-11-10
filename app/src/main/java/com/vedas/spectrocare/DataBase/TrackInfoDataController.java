package com.vedas.spectrocare.DataBase;

import android.util.Log;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.spectrocare.DataBaseModels.PatientlProfileModel;
import com.vedas.spectrocare.DataBaseModels.TrackInfoModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TrackInfoDataController {
    public ArrayList<TrackInfoModel> allTrackList = new ArrayList<>();
    public static TrackInfoDataController myObj;

    public static TrackInfoDataController getInstance() {
        if (myObj == null) {
            myObj = new TrackInfoDataController();
        }
        return myObj;
    }

    public boolean insertTrackData(TrackInfoModel userdata) {
        try {
            MedicalProfileDataController.getInstance().helper.getTrackInfoDao().create(userdata);
           // fetchTrackData();
           // Log.e("fetc", "" + allTrackList.size());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<TrackInfoModel> fetchTrackData(PatientlProfileModel patientlProfileModel) {
        allTrackList = null;
        allTrackList = new ArrayList<>();
        if(patientlProfileModel != null){
            ArrayList<TrackInfoModel> urineresultsModels = new ArrayList<TrackInfoModel>(patientlProfileModel.getTrackInfos());
            if(urineresultsModels != null) {
                allTrackList = sortUrineResultsBasedOnTime(urineresultsModels);
                Log.e("chanufetching", "track fectched successfully" + allTrackList.size());
            }
        }
        return allTrackList;
    }
    public ArrayList<TrackInfoModel> sortUrineResultsBasedOnTime(ArrayList<TrackInfoModel> allTrackList) {
        Collections.sort(allTrackList, new Comparator<TrackInfoModel>() {
            @Override
            public int compare(TrackInfoModel s1, TrackInfoModel s2) {
                return s1.getDate().compareTo(s2.getDate());
            }
        });
        return allTrackList;

    }
  /*  public ArrayList<TrackInfoModel> fetchTrackData() {
        allTrackList = null;
        allTrackList = new ArrayList<>();
        try {
            allTrackList = (ArrayList<TrackInfoModel>) MedicalProfileDataController.getInstance().helper.getTrackInfoDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.e("trackfetching", "track fectched successfully" + allTrackList.size());
        return allTrackList;
    }*/
    public boolean updateTrackData(TrackInfoModel user) {
        try {
            UpdateBuilder<TrackInfoModel, Integer> updateBuilder = MedicalProfileDataController.getInstance().helper.getTrackInfoDao().updateBuilder();
            updateBuilder.updateColumnValue("patientId", user.getPatientId());
            updateBuilder.updateColumnValue("byWhom", user.getByWhom());
            updateBuilder.updateColumnValue("byWhomId", user.getByWhomId());
            updateBuilder.updateColumnValue("date", user.getDate());
            updateBuilder.where().eq("patientId", user.getPatientId());
            updateBuilder.update();
            Log.e("update data", "updated the Track data sucessfully"+user.getPatientId());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return  false;
        }
    }

    public boolean deleteTrackData(TrackInfoModel member) {
        try {
            Log.e("Delete", "delete patient"+member.getPatientId());
            MedicalProfileDataController.getInstance().helper.getTrackInfoDao().delete(member);
            //fetchTrackData();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteTrackData(ArrayList<PatientlProfileModel> patientlProfileModel) {
        for(int i=0;i<patientlProfileModel.size();i++) {
            if (patientlProfileModel != null) {
                ArrayList<TrackInfoModel> urineresultsModels = new ArrayList<TrackInfoModel>(patientlProfileModel.get(i).getTrackInfos());
                if (urineresultsModels != null) {
                    try {
                        MedicalProfileDataController.getInstance().helper.getTrackInfoDao().delete(urineresultsModels);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Log.e("deletefetching", "track fectched successfully" + allTrackList.size());
                }
            }
        }
    }
    /*public void deleteTrackData(ArrayList<TrackInfoModel> trackInfoModels) {
        try {
            if(trackInfoModels.size()>0){
                for(TrackInfoModel trackInfoModel: trackInfoModels) {
                    DeleteBuilder<TrackInfoModel, Integer> deleteBuilder = MedicalProfileDataController.getInstance().helper.getTrackInfoDao().deleteBuilder();
                    deleteBuilder.where().eq("patientId", trackInfoModel.getId());
                    deleteBuilder.delete();
                    Log.e("deleteWifiDetailsData", "sucessfully"+trackInfoModels.size());
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
