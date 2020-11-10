package com.vedas.spectrocare.DataBase;

import android.util.Log;

import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.spectrocare.DataBaseModels.PatientlProfileModel;
import com.vedas.spectrocare.DataBaseModels.PhysicalCategoriesRecords;
import com.vedas.spectrocare.DataBaseModels.PhysicalExamsDataModel;

import java.sql.SQLException;
import java.util.ArrayList;

public class PhysicalCategoriesDataController {
    public ArrayList<PhysicalCategoriesRecords> allPhysicalExamList = new ArrayList<>();
    public PhysicalCategoriesRecords currentPhysicalExamModel;
    public static PhysicalCategoriesDataController myObj;

    public static PhysicalCategoriesDataController getInstance() {
        if (myObj == null) {
            myObj = new PhysicalCategoriesDataController();
        }
        return myObj;
    }

    public boolean insertPhysicalExamData(PhysicalCategoriesRecords userdata) {
        try {
           MedicalProfileDataController.getInstance(). helper.getPhysicalCategoriesDao().create(userdata);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public ArrayList<PhysicalCategoriesRecords> fetchPhysicalExamData(PhysicalExamsDataModel PhysicalExamsDataModel) {
        allPhysicalExamList = null;
        allPhysicalExamList = new ArrayList<>();
        if(PhysicalExamsDataModel != null){
            ArrayList<PhysicalCategoriesRecords> urineresultsModels = new ArrayList<PhysicalCategoriesRecords>(PhysicalExamsDataModel
                    .getPhysicalCategoriesRecords());
            if(urineresultsModels != null) {
                allPhysicalExamList = urineresultsModels;
                Log.e("fetchPhysicalExamData", "physical fectched successfully" + allPhysicalExamList.size());
            }
        }
        return allPhysicalExamList;
    }

    public boolean updatePhysicalExamData(PhysicalCategoriesRecords user) {
        try {
            UpdateBuilder<PhysicalCategoriesRecords, Integer> updateBuilder =MedicalProfileDataController.getInstance().helper.getPhysicalCategoriesDao().updateBuilder();
            updateBuilder.updateColumnValue("patientId", user.getPatientId());
            updateBuilder.updateColumnValue("result", user.getResult());
            updateBuilder.updateColumnValue("category", user.getCategory());
            updateBuilder.updateColumnValue("medicalRecordId", user.getMedicalRecordId());
            updateBuilder.updateColumnValue("description", user.getDescription());
            updateBuilder.updateColumnValue("physicalExamId", user.getPhysicalExamId());
            updateBuilder.updateColumnValue("physicianComment", user.getPhysicianComment());
            updateBuilder.where().eq("Id", user.getId());
            updateBuilder.update();
            Log.e("update data", "updated the PhysicalExam data sucessfully"+user.getPhysicalExamId());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return  false;
        }
    }

    public boolean deletePhysicalExamData(PhysicalCategoriesRecords member) {
        try {
            Log.e("Delete", "delete patient"+member.getPatientId());
            MedicalProfileDataController.getInstance().helper.getPhysicalCategoriesDao().delete(member);return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void deletePhysicalExamData(ArrayList<PhysicalExamsDataModel> patientlProfileModel) {
        for(int i=0;i<patientlProfileModel.size();i++) {
            if (patientlProfileModel != null) {
                ArrayList<PhysicalCategoriesRecords> urineresultsModels = new ArrayList<PhysicalCategoriesRecords>(patientlProfileModel.get(i).getPhysicalCategoriesRecords());
                if (urineresultsModels != null) {
                    try {
                        MedicalProfileDataController.getInstance().helper.getPhysicalCategoriesDao().delete(urineresultsModels);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Log.e("deletefetching", "physical fectched successfully" + allPhysicalExamList.size());
                }
            }
        }
    }

    public ArrayList<PhysicalCategoriesRecords> fetchPhysicalExamBasedOnPhysicalExamId(PhysicalExamsDataModel patientlProfileModel, String physicalExamID) {
        ArrayList<PhysicalCategoriesRecords> physicalExamModelArrayList=new ArrayList<>();
        if (patientlProfileModel != null) {
            ArrayList<PhysicalCategoriesRecords> list = new ArrayList<PhysicalCategoriesRecords>(patientlProfileModel.getPhysicalCategoriesRecords());
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getPhysicalExamId().equals(physicalExamID)) {
                        PhysicalCategoriesRecords physicalExamModel = list.get(i);
                        Log.e("dbchandu", "bmi" + physicalExamModel.getPhysicalExamId());
                        physicalExamModelArrayList.add(physicalExamModel);
                    }
                }
                Log.e("physicalExamModellist", "call" + physicalExamModelArrayList.size());
                return physicalExamModelArrayList;
            }
        }
        return null;
    }
    public void deletePhysicalExamModelData(PhysicalExamsDataModel patientlProfileModel,String physicalExamID) {
        if (patientlProfileModel != null) {
            ArrayList<PhysicalCategoriesRecords> list = new ArrayList<PhysicalCategoriesRecords>(patientlProfileModel.getPhysicalCategoriesRecords());
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getPhysicalExamId().equals(physicalExamID)) {
                        PhysicalCategoriesRecords bmiModel = list.get(i);
                        Log.e("dbchandu", "bmi" + bmiModel.getPhysicalExamId());
                        Log.e("deleteexamModelData", "bmi fectched successfully" + deleteMemberData(bmiModel));
                        deleteMemberData(bmiModel);
                        fetchPhysicalExamData(PhysicalExamDataController.getInstance().currentPhysicalExamsData);
                        //EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("deletePhysicalData"));
                    }
                }
            }
        }
    }
    public boolean deleteMemberData(PhysicalCategoriesRecords member) {
        try {
            MedicalProfileDataController.getInstance().helper.getPhysicalCategoriesDao().delete(member);
            Log.e("Delete", "delete all members");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void deletePhysicalData(ArrayList<PhysicalCategoriesRecords> user) {
        try {
            MedicalProfileDataController.getInstance().helper.getPhysicalCategoriesDao().delete(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    
}
