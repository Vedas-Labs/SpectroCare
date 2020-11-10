
package com.vedas.spectrocare.DataBase;

import android.util.Log;

import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.spectrocare.Controllers.PhysicalServerObjectDataController;
import com.vedas.spectrocare.DataBaseModels.IllnessRecordModel;
import com.vedas.spectrocare.DataBaseModels.PatientlProfileModel;

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;
import java.util.ArrayList;

public class IllnessDataController {
    public ArrayList<IllnessRecordModel> allIllnesList = new ArrayList<>();
    public IllnessRecordModel currentIllnessRecordModel;
    public static IllnessDataController myObj;

    public static IllnessDataController getInstance() {
        if (myObj == null) {
            myObj = new IllnessDataController();
        }
        return myObj;
    }

    public boolean insertIllnesData(IllnessRecordModel userdata) {
        try {
            MedicalProfileDataController.getInstance().helper.getIllnessRecordDao().create(userdata);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public ArrayList<IllnessRecordModel> fetchIllnesData(PatientlProfileModel physicalExamsDataModel) {
        allIllnesList = null;
        allIllnesList = new ArrayList<>();
        if(physicalExamsDataModel != null){
            ArrayList<IllnessRecordModel> urineresultsModels = new ArrayList<IllnessRecordModel>(physicalExamsDataModel.getIllnessRecordModels());
            if(urineresultsModels != null) {
                allIllnesList = urineresultsModels;
                Log.e("fetchIllnesData", "Illnes fectched successfully" + allIllnesList.size());
            }
        }
        return allIllnesList;
    }

    public void deleteIllnesData(PatientlProfileModel patientlProfileModels) {
            if (patientlProfileModels != null) {
                ArrayList<IllnessRecordModel> urineresultsModels = new ArrayList<IllnessRecordModel>(patientlProfileModels.getIllnessRecordModels());
                if (urineresultsModels != null) {
                    for (int i = 0; i < urineresultsModels.size(); i++) {
                            IllnessRecordModel IllnessRecordModel = urineresultsModels.get(i);
                            Log.e("dbchandu", "Illnes" + IllnessRecordModel.getPatientId());
                            Log.e("deletefetching", "Illnes fectched successfully" + deleteMemberData(IllnessRecordModel));
                            deleteMemberData(IllnessRecordModel);
                            fetchIllnesData(PatientProfileDataController.getInstance().currentPatientlProfile);
                            EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("deleteIllnesData"));
                    }
                    try {
                        MedicalProfileDataController.getInstance().helper.getIllnessRecordDao().delete(urineresultsModels);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Log.e("deletefetching", "Illnes fectched successfully" + allIllnesList.size());
                }
            }

    }
    public void deleteIllnessRecordModelData(PatientlProfileModel patientlProfileModel,IllnessRecordModel illnessRecordModel) {
        if (patientlProfileModel != null) {
            ArrayList<IllnessRecordModel> list = new ArrayList<IllnessRecordModel>(patientlProfileModel.getIllnessRecordModels());
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getIllnessRecordId().equals(illnessRecordModel.getIllnessRecordId())) {
                        IllnessRecordModel IllnessRecordModel = list.get(i);
                        Log.e("dbchandu", "Illnes" + IllnessRecordModel.getPatientId());
                        Log.e("deletefetching", "Illnes fectched successfully" + deleteMemberData(IllnessRecordModel));
                        deleteMemberData(IllnessRecordModel);
                        fetchIllnesData(PatientProfileDataController.getInstance().currentPatientlProfile);
                    }
                }
            }
        }
    }
    public boolean deleteMemberData(IllnessRecordModel member) {
        try {
            MedicalProfileDataController.getInstance().helper.getIllnessRecordDao().delete(member);
            Log.e("Delete", "delete all Illnes");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

 public boolean updateIllnesData(IllnessRecordModel user) {
        try {
            UpdateBuilder<IllnessRecordModel, Integer> updateBuilder = MedicalProfileDataController.getInstance().helper.getIllnessRecordDao().updateBuilder();
            updateBuilder.updateColumnValue("IllnessRecordId", user.getIllnessRecordId());
            updateBuilder.updateColumnValue("isCurrentIllness", user.getIsCurrentIllness());
            updateBuilder.updateColumnValue("symptoms", user.getSymptoms());
            updateBuilder.updateColumnValue("currentStatus", user.getCurrentStatus());
            updateBuilder.updateColumnValue("moreInfo", user.getMoreInfo());
            updateBuilder.updateColumnValue("medicalPersonId", user.getMedicalPersonId());
            updateBuilder.updateColumnValue("medicalRecordId", user.getMedicalRecordId());
            updateBuilder.updateColumnValue("hospitalRegNum", user.getHospitalRegNum());
            updateBuilder.updateColumnValue("patientId", user.getPatientId());
            updateBuilder.where().eq("IllnessRecordId", user.getIllnessRecordId());
            updateBuilder.update();
            Log.e("update data", "updated the Illnes data sucessfully"+user.getIllnessRecordId());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return  false;
        }
    }

}
