
package com.vedas.spectrocare.DataBase;

import android.util.Log;

import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.spectrocare.Controllers.PhysicalServerObjectDataController;
import com.vedas.spectrocare.DataBaseModels.IllnessRecordModel;
import com.vedas.spectrocare.DataBaseModels.MedicationRecordModel;
import com.vedas.spectrocare.DataBaseModels.PatientlProfileModel;

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;
import java.util.ArrayList;

public class MedicationRecordDataController {
    public ArrayList<MedicationRecordModel> allMedicationList = new ArrayList<>();
    public MedicationRecordModel currentMedicationRecordModel;
    public static MedicationRecordDataController myObj;

    public static MedicationRecordDataController getInstance() {
        if (myObj == null) {
            myObj = new MedicationRecordDataController();
        }
        return myObj;
    }

    public boolean insertMedicationData(MedicationRecordModel userdata) {
        try {
            MedicalProfileDataController.getInstance().helper.getMedicationRecordDao().create(userdata);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public MedicationRecordModel getMedicationForMedicationId(String medicationId) {
        if (allMedicationList.size() > 0) {
            for (int i = 0; i < allMedicationList.size(); i++) {
                MedicationRecordModel memberObj = allMedicationList.get(i);
                if (memberObj.getIllnessMedicationID().equals(medicationId)) {
                    Log.e("meberadmin", "call" + memberObj.getIllnessMedicationID());
                    return memberObj;
                }
            }
        }
        return null;
    }
    public ArrayList<MedicationRecordModel> fetchMedicationData(IllnessRecordModel physicalExamsDataModel) {
        allMedicationList = null;
        allMedicationList = new ArrayList<>();
        if(physicalExamsDataModel != null){
            ArrayList<MedicationRecordModel> urineresultsModels = new ArrayList<MedicationRecordModel>(physicalExamsDataModel.getMedicationRecordModels());
            if(urineresultsModels != null) {
                allMedicationList = urineresultsModels;
                Log.e("fetchMedicationData", "Medication fectched successfully" + allMedicationList.size());
            }
        }
        return allMedicationList;
    }

    public void deleteMedicationData(IllnessRecordModel illnessRecordModel) {
            if (illnessRecordModel != null) {
                ArrayList<MedicationRecordModel> urineresultsModels = new ArrayList<MedicationRecordModel>(illnessRecordModel.getMedicationRecordModels());
                if (urineresultsModels != null) {
                    for (int i = 0; i < urineresultsModels.size(); i++) {
                            MedicationRecordModel MedicationRecordModel = urineresultsModels.get(i);
                            Log.e("dbchandu", "Medication" + MedicationRecordModel.getIllnessID());
                            Log.e("deletefetching", "Medication fectched successfully" + deleteMemberData(MedicationRecordModel));
                            deleteMemberData(MedicationRecordModel);
                            fetchMedicationData(IllnessDataController.getInstance().currentIllnessRecordModel);
                          //  EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("deleteMedicationData"));
                    }
                    try {
                        MedicalProfileDataController.getInstance().helper.getMedicationRecordDao().delete(urineresultsModels);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Log.e("deletefetching", "Medication fectched successfully" + allMedicationList.size());
                }
            }

    }
    public void deleteMedicationRecordModelData(IllnessRecordModel illnessRecordModel,MedicationRecordModel MedicationRecordModel) {
        if (illnessRecordModel != null) {
            ArrayList<MedicationRecordModel> list = new ArrayList<MedicationRecordModel>(illnessRecordModel.getMedicationRecordModels());
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getIllnessID().equals(MedicationRecordModel.getIllnessID())) {
                        MedicationRecordModel medicationRecordModel = list.get(i);
                        Log.e("dbchandu", "Medication" + medicationRecordModel.getIllnessID());
                        Log.e("deletefetching", "Medication fectched successfully" + deleteMemberData(MedicationRecordModel));
                        deleteMemberData(MedicationRecordModel);
                         fetchMedicationData(IllnessDataController.getInstance().currentIllnessRecordModel);
                    }
                }
            }
        }
    }
    public boolean deleteMemberData(MedicationRecordModel member) {
        try {
            MedicalProfileDataController.getInstance().helper.getMedicationRecordDao().delete(member);
            Log.e("Delete", "delete all Medication");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

 /*public boolean updateMedicationData(MedicationRecordModel user) {
        try {
            UpdateBuilder<MedicationRecordModel, Integer> updateBuilder = MedicalProfileDataController.getInstance().helper.getMedicationRecordModelDao().updateBuilder();
            updateBuilder.updateColumnValue("medicalCondition", user.getMedicalCondition());
            updateBuilder.updateColumnValue("discription", user.getDiscription());
            updateBuilder.updateColumnValue("age", user.getAge());
            updateBuilder.updateColumnValue("Relation", user.getRelation());
            updateBuilder.updateColumnValue("medicalPersonId", user.getMedicalPersonId());
            updateBuilder.updateColumnValue("medicalRecordId", user.getMedicalRecordId());
            updateBuilder.updateColumnValue("MedicationRecordId", user.getMedicationRecordId());
            updateBuilder.updateColumnValue("patientId", user.getPatientId());
            updateBuilder.where().eq("MedicationRecordId", user.getMedicationRecordId());
            updateBuilder.update();
            Log.e("update data", "updated the Medication data sucessfully"+user.getMedicationRecordId());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return  false;
        }
    }
*/
}
