
package com.vedas.spectrocare.DataBase;

import android.util.Log;

import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.spectrocare.Controllers.PhysicalServerObjectDataController;
import com.vedas.spectrocare.DataBaseModels.FamilyHistoryModel;
import com.vedas.spectrocare.DataBaseModels.PatientlProfileModel;

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;
import java.util.ArrayList;

public class FamilyHistoryDataController {
    public ArrayList<FamilyHistoryModel> allFamilyList = new ArrayList<>();
    public FamilyHistoryModel currentFamilyHistoryModel;
    public static FamilyHistoryDataController myObj;

    public static FamilyHistoryDataController getInstance() {
        if (myObj == null) {
            myObj = new FamilyHistoryDataController();
        }
        return myObj;
    }

    public boolean insertFamilyData(FamilyHistoryModel userdata) {
        try {
            MedicalProfileDataController.getInstance().helper.getFamilyHistoryModelDao().create(userdata);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public ArrayList<FamilyHistoryModel> fetchFamilyData(PatientlProfileModel physicalExamsDataModel) {
        allFamilyList = null;
        allFamilyList = new ArrayList<>();
        if(physicalExamsDataModel != null){
            ArrayList<FamilyHistoryModel> urineresultsModels = new ArrayList<FamilyHistoryModel>(physicalExamsDataModel.getFamilyHistoryModels());
            if(urineresultsModels != null) {
                allFamilyList = urineresultsModels;
                Log.e("fetchFamilyData", "Family fectched successfully" + allFamilyList.size());
            }
        }
        return allFamilyList;
    }

    public void deleteFamilyData(PatientlProfileModel patientlProfileModels) {
            if (patientlProfileModels != null) {
                ArrayList<FamilyHistoryModel> urineresultsModels = new ArrayList<FamilyHistoryModel>(patientlProfileModels.getFamilyHistoryModels());
                if (urineresultsModels != null) {
                    for (int i = 0; i < urineresultsModels.size(); i++) {
                            FamilyHistoryModel FamilyHistoryModel = urineresultsModels.get(i);
                            Log.e("dbchandu", "Family" + FamilyHistoryModel.getPatientId());
                            Log.e("deletefetching", "Family fectched successfully" + deleteMemberData(FamilyHistoryModel));
                            deleteMemberData(FamilyHistoryModel);
                            fetchFamilyData(PatientProfileDataController.getInstance().currentPatientlProfile);
                            EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("deleteFamilyData"));
                    }
                    try {
                        MedicalProfileDataController.getInstance().helper.getFamilyHistoryModelDao().delete(urineresultsModels);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Log.e("deletefetching", "Family fectched successfully" + allFamilyList.size());
                }
            }

    }
    public void deleteFamilyHistoryModelData(PatientlProfileModel patientlProfileModel,FamilyHistoryModel familyHistoryModel) {
        if (patientlProfileModel != null) {
            ArrayList<FamilyHistoryModel> list = new ArrayList<FamilyHistoryModel>(patientlProfileModel.getFamilyHistoryModels());
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getFamilyRecordId().equals(familyHistoryModel.getFamilyRecordId())) {
                        FamilyHistoryModel FamilyHistoryModel = list.get(i);
                        Log.e("dbchandu", "Family" + FamilyHistoryModel.getPatientId());
                        Log.e("deletefetching", "Family fectched successfully" + deleteMemberData(FamilyHistoryModel));
                        deleteMemberData(FamilyHistoryModel);
                         fetchFamilyData(PatientProfileDataController.getInstance().currentPatientlProfile);
                         EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("deleteFamilyData"));
                    }
                }
            }
        }
    }
    public boolean deleteMemberData(FamilyHistoryModel member) {
        try {
            MedicalProfileDataController.getInstance().helper.getFamilyHistoryModelDao().delete(member);
            Log.e("Delete", "delete all family");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

 public boolean updateFamilyData(FamilyHistoryModel user) {
        try {
            UpdateBuilder<FamilyHistoryModel, Integer> updateBuilder = MedicalProfileDataController.getInstance().helper.getFamilyHistoryModelDao().updateBuilder();
            updateBuilder.updateColumnValue("medicalCondition", user.getMedicalCondition());
            updateBuilder.updateColumnValue("discription", user.getDiscription());
            updateBuilder.updateColumnValue("age", user.getAge());
            updateBuilder.updateColumnValue("Relation", user.getRelation());
            updateBuilder.updateColumnValue("medicalPersonId", user.getMedicalPersonId());
            updateBuilder.updateColumnValue("medicalRecordId", user.getMedicalRecordId());
            updateBuilder.updateColumnValue("familyRecordId", user.getFamilyRecordId());
            updateBuilder.updateColumnValue("patientId", user.getPatientId());
            updateBuilder.where().eq("familyRecordId", user.getFamilyRecordId());
            updateBuilder.update();
            Log.e("update data", "updated the Family data sucessfully"+user.getFamilyRecordId());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return  false;
        }
    }

}
