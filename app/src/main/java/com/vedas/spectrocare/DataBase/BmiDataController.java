package com.vedas.spectrocare.DataBase;

import android.util.Log;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.spectrocare.DataBaseModels.BMIModel;
import com.vedas.spectrocare.DataBaseModels.PatientlProfileModel;
import com.vedas.spectrocare.DataBaseModels.PhysicalExamsDataModel;

import java.sql.SQLException;
import java.util.ArrayList;

public class BmiDataController {
    public ArrayList<BMIModel> allBmiList = new ArrayList<>();
    public BMIModel currentBmiModel;
    public static BmiDataController myObj;

    public static BmiDataController getInstance() {
        if (myObj == null) {
            myObj = new BmiDataController();
        }
        return myObj;
    }

    public boolean insertBmiData(BMIModel userdata) {
        try {
            MedicalProfileDataController.getInstance().helper.getBmiDao().create(userdata);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public ArrayList<BMIModel> fetchBmiData(PhysicalExamsDataModel physicalExamsDataModel) {
        allBmiList = null;
        allBmiList = new ArrayList<>();
        if(physicalExamsDataModel != null){
            ArrayList<BMIModel> urineresultsModels = new ArrayList<BMIModel>(physicalExamsDataModel.getBmiModels());
            if(urineresultsModels != null) {
                allBmiList = urineresultsModels;
               // currentBmiModel=allBmiList.get(0);
                Log.e("fetchBmiData", "bmi fectched successfully" + allBmiList.size());
            }
        }
        return allBmiList;
    }
    /*public ArrayList<BMIModel> fetchBmiData() {
        allBmiList = null;
        allBmiList = new ArrayList<>();
        try {
            allBmiList = (ArrayList<BMIModel>) MedicalProfileDataController.getInstance().helper.getBmiDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.e("fetching", "bmi profile data fectched successfully" + allBmiList.size());
        return allBmiList;
    }*/
    public boolean updateBmiData(BMIModel user) {
        try {
            UpdateBuilder<BMIModel, Integer> updateBuilder = MedicalProfileDataController.getInstance().helper.getBmiDao().updateBuilder();
            updateBuilder.updateColumnValue("patientId", user.getPatientId());
            updateBuilder.updateColumnValue("medicalRecordId", user.getMedicalRecordId());
            updateBuilder.updateColumnValue("height", user.getHeight());
            updateBuilder.updateColumnValue("weight", user.getWeight());
            updateBuilder.updateColumnValue("bmi", user.getBmi());
            updateBuilder.updateColumnValue("bloodPressure", user.getBloodPressure());
            updateBuilder.updateColumnValue("physicalExamId", user.getPhysicalExamId());
            updateBuilder.updateColumnValue("waistLine", user.getWaistLine());
            updateBuilder.where().eq("physicalExamId", user.getPhysicalExamId());
            updateBuilder.update();
            Log.e("update data", "updated the bmi data sucessfully"+user.getPhysicalExamId()+user.getHeight());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return  false;
        }
    }
    public void deleteBmiData(ArrayList<PhysicalExamsDataModel> physicalExamsDataModels) {
        for(int i=0;i<physicalExamsDataModels.size();i++) {
            if (physicalExamsDataModels != null) {
                ArrayList<BMIModel> urineresultsModels = new ArrayList<BMIModel>(physicalExamsDataModels.get(i).getBmiModels());
                if (urineresultsModels != null) {
                    try {
                        MedicalProfileDataController.getInstance().helper.getBmiDao().delete(urineresultsModels);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Log.e("deletefetching", "bmi fectched successfully" + allBmiList.size());
                }
            }
        }
    }

    public void deleteBmiModelData(PhysicalExamsDataModel physicalExamsDataModel,String physicalExamID) {
            if (physicalExamsDataModel != null) {
                ArrayList<BMIModel> list = new ArrayList<BMIModel>(physicalExamsDataModel.getBmiModels());
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getPhysicalExamId().equals(physicalExamID)) {
                            BMIModel bmiModel = list.get(i);
                            Log.e("dbchandu", "bmi" + bmiModel.getPhysicalExamId());
                            Log.e("deletefetching", "bmi fectched successfully" + deleteMemberData(bmiModel));
                            deleteMemberData(bmiModel);
                           // fetchBmiData(PhysicalExamDataController.getInstance().currentPhysicalExamsData);
                          //  EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("deletePhysicalData"));
                        }
                    }
                }
            }
    }
    public boolean deleteMemberData(BMIModel member) {
        try {
            MedicalProfileDataController.getInstance().helper.getBmiDao().delete(member);
            Log.e("Delete", "delete all members");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
   /* public boolean deleteBmiData(BMIModel member) {
        try {
            Log.e("Delete", "delete patient"+member.getPatientId());
            MedicalProfileDataController.getInstance().helper.getBmiDao().delete(member);
            fetchBmiData();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void deleteBmiData(ArrayList<BMIModel> user) {
        try {
            MedicalProfileDataController.getInstance().helper.getBmiDao().delete(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }*/

}
