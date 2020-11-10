package com.vedas.spectrocare.DataBase;

import android.util.Log;

import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.spectrocare.DataBaseModels.PatientlProfileModel;
import com.vedas.spectrocare.DataBaseModels.PhysicalCategoriesRecords;
import com.vedas.spectrocare.DataBaseModels.PhysicalExamsDataModel;
import com.vedas.spectrocare.DataBaseModels.PhysicalTrackInfoModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PhysicalExamTrackInfoDataController {
    public ArrayList<PhysicalTrackInfoModel> allTrackList = new ArrayList<>();
    public static PhysicalExamTrackInfoDataController myObj;

    public static PhysicalExamTrackInfoDataController getInstance() {
        if (myObj == null) {
            myObj = new PhysicalExamTrackInfoDataController();
        }
        return myObj;
    }

    public boolean insertTrackData(PhysicalTrackInfoModel userdata) {
        try {
            MedicalProfileDataController.getInstance().helper.getPhisicalTrackDao().create(userdata);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<PhysicalTrackInfoModel> fetchTrackData(PhysicalExamsDataModel patientlProfileModel) {
        allTrackList = null;
        allTrackList = new ArrayList<>();
        if(patientlProfileModel != null){
            ArrayList<PhysicalTrackInfoModel> urineresultsModels = new ArrayList<PhysicalTrackInfoModel>(patientlProfileModel.getPhysicalTrackInfoModels());
            if(urineresultsModels != null) {
                allTrackList = sortUrineResultsBasedOnTime(urineresultsModels);
                Log.e("chanufetching", "physicaltrack fectched successfully" + allTrackList.size());
            }
        }
        return allTrackList;
    }
    public ArrayList<PhysicalTrackInfoModel> sortUrineResultsBasedOnTime(ArrayList<PhysicalTrackInfoModel> allTrackList) {
        Collections.sort(allTrackList, new Comparator<PhysicalTrackInfoModel>() {
            @Override
            public int compare(PhysicalTrackInfoModel s1, PhysicalTrackInfoModel s2) {
                return s1.getDate().compareTo(s2.getDate());
            }
        });
        return allTrackList;
    }
    public ArrayList<PhysicalTrackInfoModel> fetchPhysicalExamBasedOnPhysicalExamId(PhysicalExamsDataModel patientlProfileModel) {
        ArrayList<PhysicalTrackInfoModel> physicalExamModelArrayList=new ArrayList<>();
        if (patientlProfileModel != null) {
            ArrayList<PhysicalTrackInfoModel> list = new ArrayList<PhysicalTrackInfoModel>(patientlProfileModel.getPhysicalTrackInfoModels());
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getPhysicalExamId().equals(patientlProfileModel.getPhysicalExamId())) {
                        PhysicalTrackInfoModel physicalExamModel = list.get(i);
                        Log.e("dbchandu", "bmi" + physicalExamModel.getPhysicalExamId());
                        physicalExamModelArrayList.add(physicalExamModel);
                    }
                }
                Log.e("PhysicalTrackInfoarray", "call" + physicalExamModelArrayList.size());
                return physicalExamModelArrayList;
            }
        }
        return null;
    }
    public boolean updateTrackData(PhysicalTrackInfoModel user) {
        try {
            UpdateBuilder<PhysicalTrackInfoModel, Integer> updateBuilder = MedicalProfileDataController.getInstance().helper.getPhisicalTrackDao().updateBuilder();
            updateBuilder.updateColumnValue("patientId", user.getPatientId());
            updateBuilder.updateColumnValue("byWhom", user.getByWhom());
            updateBuilder.updateColumnValue("byWhomId", user.getByWhomId());
            updateBuilder.updateColumnValue("physicalExamId", user.getPhysicalExamId());
            updateBuilder.updateColumnValue("date", user.getDate());

            updateBuilder.where().eq("physicalExamId", user.getPhysicalExamId());
            updateBuilder.update();
            Log.e("update data", "updated the Track data sucessfully"+user.getPhysicalExamId());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return  false;
        }
    }

    public boolean deleteTrackData(PhysicalTrackInfoModel member) {
        try {
            Log.e("Delete", "delete patient"+member.getPatientId());
            MedicalProfileDataController.getInstance().helper.getPhisicalTrackDao().delete(member);
            //fetchTrackData();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteTrackData(ArrayList<PhysicalExamsDataModel> patientlProfileModel) {
        for(int i=0;i<patientlProfileModel.size();i++) {
            if (patientlProfileModel != null) {
                ArrayList<PhysicalTrackInfoModel> urineresultsModels = new ArrayList<PhysicalTrackInfoModel>(patientlProfileModel.get(i).getPhysicalTrackInfoModels());
                if (urineresultsModels != null) {
                    try {
                        MedicalProfileDataController.getInstance().helper.getPhisicalTrackDao().delete(urineresultsModels);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Log.e("deletefetching", "track fectched successfully" + allTrackList.size());
                }
            }
        }
    }

    public void deletePhisicalModelData(PhysicalExamsDataModel patientlProfileModel, String physicalExamID) {
        if (patientlProfileModel != null) {
            ArrayList<PhysicalTrackInfoModel> list = new ArrayList<PhysicalTrackInfoModel>(patientlProfileModel.getPhysicalTrackInfoModels());
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getPhysicalExamId().equals(physicalExamID)) {
                        PhysicalTrackInfoModel bmiModel = list.get(i);
                        Log.e("dbchandu", "bmi" + bmiModel.getPhysicalExamId());
                        Log.e("deletePhisicalModelData", "bmi fectched successfully" + deleteMemberData(bmiModel));
                        deleteMemberData(bmiModel);
                        fetchTrackData(PhysicalExamDataController.getInstance().currentPhysicalExamsData);
                      //  EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("deletePhysicalData"));
                    }
                }
            }
        }
    }
    public boolean deleteMemberData(PhysicalTrackInfoModel member) {
        try {
            MedicalProfileDataController.getInstance().helper.getPhisicalTrackDao().delete(member);
            Log.e("Delete", "delete all members");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    /*public void deleteTrackData(ArrayList<PhysicalTrackInfoModel> PhisicalTrackInfoModels) {
        try {
            if(PhisicalTrackInfoModels.size()>0){
                for(PhysicalTrackInfoModel PhysicalTrackInfoModel: PhisicalTrackInfoModels) {
                    DeleteBuilder<PhysicalTrackInfoModel, Integer> deleteBuilder = MedicalProfileDataController.getInstance().helper.getTrackInfoDao().deleteBuilder();
                    deleteBuilder.where().eq("patientId", PhysicalTrackInfoModel.getId());
                    deleteBuilder.delete();
                    Log.e("deleteWifiDetailsData", "sucessfully"+PhisicalTrackInfoModels.size());
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
