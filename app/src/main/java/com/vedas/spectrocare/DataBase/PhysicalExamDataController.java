package com.vedas.spectrocare.DataBase;

import android.util.Log;

import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.spectrocare.Controllers.PhysicalServerObjectDataController;
import com.vedas.spectrocare.DataBaseModels.PhysicalExamsDataModel;
import com.vedas.spectrocare.DataBaseModels.PatientlProfileModel;

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;
import java.util.ArrayList;

public class PhysicalExamDataController {
    public ArrayList<PhysicalExamsDataModel> allPhysicalExamList = new ArrayList<>();
    public PhysicalExamsDataModel currentPhysicalExamsData;
    public static PhysicalExamDataController myObj;

    public static PhysicalExamDataController getInstance() {
        if (myObj == null) {
            myObj = new PhysicalExamDataController();
        }
        return myObj;
    }

    public boolean insertPhysicalExamData(PhysicalExamsDataModel userdata) {
        try {
            MedicalProfileDataController.getInstance().helper.getPhysicalExamsDataModelsDao().create(userdata);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public ArrayList<PhysicalExamsDataModel> fetchPhysicalExamData(PatientlProfileModel patientlProfileModel) {
        allPhysicalExamList = null;
        allPhysicalExamList = new ArrayList<>();
        if(patientlProfileModel != null){
            ArrayList<PhysicalExamsDataModel> urineresultsModels = new ArrayList<PhysicalExamsDataModel>(patientlProfileModel.getPhysicalExamsDataModels());
            if(urineresultsModels != null) {
                allPhysicalExamList = urineresultsModels;
                Log.e("fetchPhysicalExamData", "PhysicalExam fectched successfully" + allPhysicalExamList.size());
            }
        }
        return allPhysicalExamList;
    }
    public boolean updatePhysicalExamData(PhysicalExamsDataModel user) {
        try {
            UpdateBuilder<PhysicalExamsDataModel, Integer> updateBuilder = MedicalProfileDataController.getInstance().helper.getPhysicalExamsDataModelsDao().updateBuilder();
            updateBuilder.updateColumnValue("patientId", user.getPatientId());
            updateBuilder.updateColumnValue("hospital_reg_number", user.getHospital_reg_number());
            updateBuilder.updateColumnValue("medicalPersonId", user.getMedicalPersonId());
            updateBuilder.updateColumnValue("medicalRecordId", user.getMedicalRecordId());
            updateBuilder.updateColumnValue("physicalExamId", user.getPhysicalExamId());
            updateBuilder.updateColumnValue("other", user.getOther());
            updateBuilder.updateColumnValue("physicianComments", user.getPhysicianComments());
            updateBuilder.where().eq("physicalExamId", user.getId());
            updateBuilder.update();
            Log.e("update data", "updated the Physical data sucessfully"+user.getPhysicalExamId());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return  false;
        }
    }
    public void deletePhysicalExamData(ArrayList<PatientlProfileModel> patientlProfileModel) {
        for(int i=0;i<patientlProfileModel.size();i++) {
            if (patientlProfileModel != null) {
                ArrayList<PhysicalExamsDataModel> urineresultsModels = new ArrayList<PhysicalExamsDataModel>(patientlProfileModel.get(i).getPhysicalExamsDataModels());
                if (urineresultsModels != null) {
                    try {
                        MedicalProfileDataController.getInstance().helper.getPhysicalExamsDataModelsDao().delete(urineresultsModels);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Log.e("deletefetching", "PhysicalExam fectched successfully" + allPhysicalExamList.size());
                }
            }
        }
    }

    public void deletePhysicalExamsDataModelData(PatientlProfileModel patientlProfileModel,String physicalExamID) {
            if (patientlProfileModel != null) {
                ArrayList<PhysicalExamsDataModel> list = new ArrayList<PhysicalExamsDataModel>(patientlProfileModel.getPhysicalExamsDataModels());
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getPhysicalExamId().equals(physicalExamID)) {
                            PhysicalExamsDataModel PhysicalExamsDataModel = list.get(i);
                            Log.e("dbchandu", "PhysicalExam" + PhysicalExamsDataModel.getPhysicalExamId());
                            Log.e("deletefetching", "PhysicalExam fectched successfully" + deleteMemberData(PhysicalExamsDataModel));
                            deleteMemberData(PhysicalExamsDataModel);
                            fetchPhysicalExamData(PatientProfileDataController.getInstance().currentPatientlProfile);
                            EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("deletePhysicalData"));
                        }
                    }
                }
            }
    }
    public boolean deleteMemberData(PhysicalExamsDataModel member) {
        try {
            MedicalProfileDataController.getInstance().helper.getPhysicalExamsDataModelsDao().delete(member);
            Log.e("Delete", "delete all members");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
