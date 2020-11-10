package com.vedas.spectrocare.DataBase;

import android.util.Log;

import com.vedas.spectrocare.Controllers.PhysicalServerObjectDataController;
import com.vedas.spectrocare.DataBaseModels.IllnessRecordModel;
import com.vedas.spectrocare.DataBaseModels.PatientlProfileModel;
import com.vedas.spectrocare.DataBaseModels.ScreeningRecordModel;
import com.vedas.spectrocare.DataBaseModels.SurgicalRecordModel;

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;
import java.util.ArrayList;

public class SurgricalRecordDataControll {

    public ArrayList<SurgicalRecordModel> allSurgicalList = new ArrayList<>();
    public SurgicalRecordModel currentSurgicalmodel;
    public static  SurgricalRecordDataControll myObj;

    public static SurgricalRecordDataControll getInstance(){
        if (myObj==null)
            myObj = new SurgricalRecordDataControll();
        return myObj;
    }

    public boolean insertSurgicalData(SurgicalRecordModel userdata) {
        try {
            MedicalProfileDataController.getInstance().helper.getSurgicalRecordModels().create(userdata);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public ArrayList<SurgicalRecordModel> fetchingSurgicalData(IllnessRecordModel illnessRecordModel){
        allSurgicalList = null;
        allSurgicalList = new ArrayList<>();
        if(illnessRecordModel != null){
            ArrayList<SurgicalRecordModel> urineresultsModels = new ArrayList<SurgicalRecordModel>(illnessRecordModel.getSurgicalRecordModels());
            Log.e("illnessList",""+illnessRecordModel.getSurgicalRecordModels());
            if(urineresultsModels != null) {
                allSurgicalList = urineresultsModels;
                Log.e("fetchSurgicalData", "Surgical records fectched successfully" + allSurgicalList.size());
            }
        }
        return allSurgicalList;
    }

    public void deleteSurgicalData(IllnessRecordModel illnessRecordModel) {
        if (illnessRecordModel != null) {
            ArrayList<SurgicalRecordModel> urineresultsModels = new ArrayList<SurgicalRecordModel>(illnessRecordModel.getSurgicalRecordModels());
            if (urineresultsModels != null) {
                for (int i = 0; i < urineresultsModels.size(); i++) {
                    SurgicalRecordModel SurgicalRecord = urineresultsModels.get(i);
                    Log.e("deletefetching", "Screening fectched successfully" + deleteMemberData(SurgicalRecord));
                    deleteMemberData(SurgicalRecord);
                    fetchingSurgicalData(IllnessDataController.getInstance().currentIllnessRecordModel);
                   // EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("deleteScreeningData"));
                }
                try {
                    MedicalProfileDataController.getInstance().helper.getSurgicalRecordModels().delete(urineresultsModels);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Log.e("deletefetching", "Screening fectched successfully" + allSurgicalList.size());
            }
        }

    }
    public void deleteSurgicalRecordModelData(IllnessRecordModel patientlProfileModel,SurgicalRecordModel screeningRecordModel) {
        if (patientlProfileModel != null) {
            ArrayList<SurgicalRecordModel> list = new ArrayList<SurgicalRecordModel>(patientlProfileModel.getSurgicalRecordModels());
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getIllnessSurgicalId().equals(screeningRecordModel.getIllnessSurgicalId())) {
                        SurgicalRecordModel screeningRecordModel1 = list.get(i);
                      //  Log.e("dbchandu", "Screening" + screeningRecordModel1.getPatientId());
                        Log.e("deletefetching", "Screening fectched successfully" + deleteMemberData(screeningRecordModel1));
                        deleteMemberData(screeningRecordModel1);
                        fetchingSurgicalData(IllnessDataController.getInstance().currentIllnessRecordModel);
                       // EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("deleteScreeningData"));
                    }
                }
            }
        }
    }

    public boolean deleteMemberData(SurgicalRecordModel member) {
        try {
            MedicalProfileDataController.getInstance().helper.getSurgicalRecordModels().delete(member);
            Log.e("Delete", "delete all Screening");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


  /*  public boolean insertTrackData(SurgicalRecordModel userdata) {
        try {
            MedicalProfileDataController.getInstance().helper.getSurgicalRecordModels().create(userdata);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }*/
}
