package com.vedas.spectrocare.DataBase;

import android.util.Log;

import com.vedas.spectrocare.Controllers.PhysicalServerObjectDataController;
import com.vedas.spectrocare.DataBaseModels.IllnessRecordModel;
import com.vedas.spectrocare.DataBaseModels.MedicalScreeningRecordModel;
import com.vedas.spectrocare.DataBaseModels.PatientlProfileModel;
import com.vedas.spectrocare.DataBaseModels.ScreeningRecordModel;

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;
import java.util.ArrayList;

public class MedicalScreeningRecordDataController {
    public ArrayList<MedicalScreeningRecordModel> allMedicalScreeningList = new ArrayList<>();
    public MedicalScreeningRecordModel currentMedicalScreenigRecordModel;
    public static MedicalScreeningRecordDataController myObj;

    public static MedicalScreeningRecordDataController getInstance() {
        if (myObj == null) {
            myObj = new MedicalScreeningRecordDataController();
        }
        return myObj;
    }
    public boolean insertMedicalScreeningData(MedicalScreeningRecordModel userMedicaldata) {
        try {
            MedicalProfileDataController.getInstance().helper.getMedicalScreeningRecordModels().create(userMedicaldata);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public ArrayList<MedicalScreeningRecordModel> fetchMedicalScreeningData(IllnessRecordModel illnessRecordModel) {
        allMedicalScreeningList = null;
        allMedicalScreeningList = new ArrayList<>();
        if(illnessRecordModel != null){
            ArrayList<MedicalScreeningRecordModel> urineresultsModels = new ArrayList<MedicalScreeningRecordModel>(illnessRecordModel.getMedicalScreeningRecordModels());
            if(urineresultsModels != null) {
                allMedicalScreeningList = urineresultsModels;
                Log.e("medicalfetchScreening", "Screening fectched successfully" + allMedicalScreeningList.size());
            }
        }
        return allMedicalScreeningList;
    }
    public void deleteMedicalScreeningData(IllnessRecordModel patientlProfileModels) {
        if (patientlProfileModels != null) {
            ArrayList<MedicalScreeningRecordModel> urineresultsModels = new ArrayList<MedicalScreeningRecordModel>(patientlProfileModels.getMedicalScreeningRecordModels());
            if (urineresultsModels != null) {
                for (int i = 0; i < urineresultsModels.size(); i++) {
                    MedicalScreeningRecordModel medicalScreeningRecordModel = urineresultsModels.get(i);
                    Log.e("dbchandu", "Screening" + medicalScreeningRecordModel.getPatientID());
                    Log.e("deletefetching", "Screening fectched successfully" + deleteMemberData(medicalScreeningRecordModel));
                    deleteMemberData(medicalScreeningRecordModel);
                    fetchMedicalScreeningData(IllnessDataController.getInstance().currentIllnessRecordModel);
                    EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("deleteScreeningData"));
                }
                try {
                    MedicalProfileDataController.getInstance().helper.getMedicalScreeningRecordModels().delete(urineresultsModels);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Log.e("deletefetching", "Screening fectched successfully" + allMedicalScreeningList.size());
            }
        }


    }
    public void deleteScreeningRecordModelData(IllnessRecordModel patientlProfileModel,MedicalScreeningRecordModel screeningRecordModel) {
        if (patientlProfileModel != null) {
            ArrayList<MedicalScreeningRecordModel> list = new ArrayList<MedicalScreeningRecordModel>(patientlProfileModel.getMedicalScreeningRecordModels());
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getScreeningID().equals(screeningRecordModel.getScreeningID())) {
                        MedicalScreeningRecordModel screeningRecordModel1 = list.get(i);
                        Log.e("dbchandu", "Screening" + screeningRecordModel1.getPatientID());
                        Log.e("deletefetching", "Screening fectched successfully" + deleteMemberData(screeningRecordModel1));
                        deleteMemberData(screeningRecordModel1);
                        fetchMedicalScreeningData(IllnessDataController.getInstance().currentIllnessRecordModel);
                        EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("deleteScreeningData"));
                    }
                }
            }
        }
    }

    public boolean deleteMemberData(MedicalScreeningRecordModel member) {
        try {
            MedicalProfileDataController.getInstance().helper.getMedicalScreeningRecordModels().delete(member);
            Log.e("Delete", "delete all Screening");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
