
package com.vedas.spectrocare.DataBase;

import android.util.Log;

import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.spectrocare.DataBaseModels.VaccineModel;
import com.vedas.spectrocare.DataBaseModels.PatientlProfileModel;

import java.sql.SQLException;
import java.util.ArrayList;

public class VaccineDataController {
    public ArrayList<VaccineModel> allVaccineList = new ArrayList<>();
    public VaccineModel currentVaccineModel;
    public static VaccineDataController myObj;

    public static VaccineDataController getInstance() {
        if (myObj == null) {
            myObj = new VaccineDataController();
        }
        return myObj;
    }

    public boolean insertVaccineData(VaccineModel userdata) {
        try {
            MedicalProfileDataController.getInstance().helper.getVaccineModelsDao().create(userdata);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public ArrayList<VaccineModel> fetchVaccineData(PatientlProfileModel physicalExamsDataModel) {
        allVaccineList = null;
        allVaccineList = new ArrayList<>();
        if(physicalExamsDataModel != null){
            ArrayList<VaccineModel> urineresultsModels = new ArrayList<VaccineModel>(physicalExamsDataModel.getVaccineModels());
            if(urineresultsModels != null) {
                allVaccineList = urineresultsModels;
                Log.e("fetchVaccineData", "Vaccine fectched successfully" + allVaccineList.size());
            }
        }
        return allVaccineList;
    }

    public void deleteVaccineData(PatientlProfileModel patientlProfileModels) {
            if (patientlProfileModels != null) {
                ArrayList<VaccineModel> urineresultsModels = new ArrayList<VaccineModel>(patientlProfileModels.getVaccineModels());
                if (urineresultsModels != null) {
                    for (int i = 0; i < urineresultsModels.size(); i++) {
                            VaccineModel VaccineModel = urineresultsModels.get(i);
                            Log.e("dbchandu", "Vaccine" + VaccineModel.getPatientId());
                            Log.e("deletefetching", "Vaccine fectched successfully" + deleteMemberData(VaccineModel));
                            deleteMemberData(VaccineModel);
                            fetchVaccineData(PatientProfileDataController.getInstance().currentPatientlProfile);
                            //EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("deleteVaccineData"));
                    }
                    Log.e("deletefetching", "Vaccine fectched successfully" + allVaccineList.size());
                }
            }

    }
    public void deleteVaccineModelData(PatientlProfileModel patientlProfileModel,VaccineModel VaccineModel) {
        if (patientlProfileModel != null) {
            ArrayList<VaccineModel> list = new ArrayList<VaccineModel>(patientlProfileModel.getVaccineModels());
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getVaccineRecordId().equals(VaccineModel.getVaccineRecordId())) {
                        VaccineModel vaccineModel = list.get(i);
                        Log.e("dbchandu", "Vaccine" + vaccineModel.getPatientId());
                        Log.e("deletefetching", "Vaccine fectched successfully" + deleteMemberData(vaccineModel));
                        deleteMemberData(vaccineModel);
                         fetchVaccineData(PatientProfileDataController.getInstance().currentPatientlProfile);
                       //  EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("deleteVaccineData"));
                    }
                }
            }
        }
    }
    public boolean deleteMemberData(VaccineModel member) {
        try {
            MedicalProfileDataController.getInstance().helper.getVaccineModelsDao().delete(member);
            Log.e("Delete", "delete all Vaccine");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

 public boolean updateVaccineData(VaccineModel user) {
        try {
            UpdateBuilder<VaccineModel, Integer> updateBuilder = MedicalProfileDataController.getInstance().helper.getVaccineModelsDao().updateBuilder();
            updateBuilder.updateColumnValue("patientId", user.getPatientId());
            updateBuilder.updateColumnValue("medicalRecordId", user.getMedicalRecordId());
            updateBuilder.updateColumnValue("medicalPersonId", user.getMedicalPersonId());
            updateBuilder.updateColumnValue("hospitalRegNum", user.getHospitalRegNum());
            updateBuilder.updateColumnValue("vaccineName", user.getVaccineName());
            updateBuilder.updateColumnValue("vaccineDate", user.getVaccineDate());
            updateBuilder.updateColumnValue("note", user.getNote());
            updateBuilder.updateColumnValue("vaccineRecordId", user.getVaccineRecordId());

            updateBuilder.where().eq("vaccineRecordId", user.getVaccineRecordId());
            updateBuilder.update();
            Log.e("update data", "updated the Vaccine data sucessfully"+user.getVaccineRecordId());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return  false;
        }
    }

}
