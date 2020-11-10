
package com.vedas.spectrocare.DataBase;

import android.util.Log;

import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.spectrocare.Controllers.PhysicalServerObjectDataController;
import com.vedas.spectrocare.DataBaseModels.AllergieModel;
import com.vedas.spectrocare.DataBaseModels.PatientlProfileModel;

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;
import java.util.ArrayList;

public class AllergyDataController {
    public ArrayList<AllergieModel> allAllergyList = new ArrayList<>();
    public AllergieModel currentAllergieModel;
    public static AllergyDataController myObj;

    public static AllergyDataController getInstance() {
        if (myObj == null) {
            myObj = new AllergyDataController();
        }
        return myObj;
    }

    public boolean insertAllergyData(AllergieModel userdata) {
        try {
            MedicalProfileDataController.getInstance().helper.getAllergieModelsDao().create(userdata);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public ArrayList<AllergieModel> fetchAllergyData(PatientlProfileModel physicalExamsDataModel) {
        allAllergyList = null;
        allAllergyList = new ArrayList<>();
        if(physicalExamsDataModel != null){
            ArrayList<AllergieModel> urineresultsModels = new ArrayList<AllergieModel>(physicalExamsDataModel.getAllergieModels());
            if(urineresultsModels != null) {
                allAllergyList = urineresultsModels;
                Log.e("fetchAllergyData", "Allergy fectched successfully" + allAllergyList.size());
            }
        }
        return allAllergyList;
    }

    public void deleteAllergyData(PatientlProfileModel patientlProfileModels) {
            if (patientlProfileModels != null) {
                ArrayList<AllergieModel> urineresultsModels = new ArrayList<AllergieModel>(patientlProfileModels.getAllergieModels());
                if (urineresultsModels != null) {
                    for (int i = 0; i < urineresultsModels.size(); i++) {
                            AllergieModel AllergieModel = urineresultsModels.get(i);
                            Log.e("dbchandu", "Allergy" + AllergieModel.getPatientId());
                            Log.e("deletefetching", "Allergy fectched successfully" + deleteMemberData(AllergieModel));
                            deleteMemberData(AllergieModel);
                            fetchAllergyData(PatientProfileDataController.getInstance().currentPatientlProfile);
                            //EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("deleteAllergyData"));
                    }
                    /*try {
                        MedicalProfileDataController.getInstance().helper.getAllergieModelsDao().delete(urineresultsModels);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }*/
                    Log.e("deletefetching", "Allergy fectched successfully" + allAllergyList.size());
                }
            }

    }
    public void deleteAllergieModelData(PatientlProfileModel patientlProfileModel,AllergieModel AllergieModel) {
        if (patientlProfileModel != null) {
            ArrayList<AllergieModel> list = new ArrayList<AllergieModel>(patientlProfileModel.getAllergieModels());
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getAllergyRecordId().equals(AllergieModel.getAllergyRecordId())) {
                        AllergieModel allergieModel = list.get(i);
                        Log.e("dbchandu", "Allergy" + allergieModel.getPatientId());
                        Log.e("deletefetching", "Allergy fectched successfully" + deleteMemberData(allergieModel));
                        deleteMemberData(allergieModel);
                         fetchAllergyData(PatientProfileDataController.getInstance().currentPatientlProfile);
                       //  EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("deleteAllergyData"));
                    }
                }
            }
        }
    }
    public boolean deleteMemberData(AllergieModel member) {
        try {
            MedicalProfileDataController.getInstance().helper.getAllergieModelsDao().delete(member);
            Log.e("Delete", "delete all Allergy");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

 public boolean updateAllergyData(AllergieModel user) {
        try {
            UpdateBuilder<AllergieModel, Integer> updateBuilder = MedicalProfileDataController.getInstance().helper.getAllergieModelsDao().updateBuilder();
            updateBuilder.updateColumnValue("patientId", user.getPatientId());
            updateBuilder.updateColumnValue("medicalRecordId", user.getMedicalRecordId());
            updateBuilder.updateColumnValue("medicalPersonId", user.getMedicalPersonId());
            updateBuilder.updateColumnValue("hospitalRegNum", user.getHospitalRegNum());
            updateBuilder.updateColumnValue("allergyInfo", user.getAllergyInfo());
            updateBuilder.updateColumnValue("allergyRecordId", user.getAllergyRecordId());
            updateBuilder.where().eq("allergyRecordId", user.getAllergyRecordId());
            updateBuilder.update();
            Log.e("update data", "updated the Allergy data sucessfully"+user.getAllergyRecordId());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return  false;
        }
    }

}
