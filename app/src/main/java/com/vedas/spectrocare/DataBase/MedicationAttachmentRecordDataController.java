
package com.vedas.spectrocare.DataBase;

import android.util.Log;

import com.vedas.spectrocare.Controllers.PhysicalServerObjectDataController;
import com.vedas.spectrocare.DataBaseModels.MedicationRecordModel;
import com.vedas.spectrocare.DataBaseModels.MedicationAttachmentModel;

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;
import java.util.ArrayList;

public class MedicationAttachmentRecordDataController {
    public ArrayList<MedicationAttachmentModel> allAttachmentList = new ArrayList<>();
    public MedicationAttachmentModel currentMedicationAttachmentModel;
    public static MedicationAttachmentRecordDataController myObj;

    public static MedicationAttachmentRecordDataController getInstance() {
        if (myObj == null) {
            myObj = new MedicationAttachmentRecordDataController();
        }
        return myObj;
    }

    public boolean insertAttachmentData(MedicationAttachmentModel userdata) {
        try {
            MedicalProfileDataController.getInstance().helper.getMedicationAttachmentModelsDao().create(userdata);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("insertattach", "call" + e.getMessage());
            return false;
        }
    }
    public ArrayList<MedicationAttachmentModel> fetchAttachmentData(MedicationRecordModel medicationRecordModel) {
        allAttachmentList = null;
        allAttachmentList = new ArrayList<>();
        if(medicationRecordModel != null){
            ArrayList<MedicationAttachmentModel> urineresultsModels = new ArrayList<MedicationAttachmentModel>(medicationRecordModel.getMedicationAttachmentModels());
            if(urineresultsModels != null) {
                allAttachmentList = urineresultsModels;
                Log.e("fetchAttachmentData", "Attachment fectched successfully" + allAttachmentList.size());
            }
        }
        return allAttachmentList;
    }

    public void deleteAttachmentData(MedicationRecordModel medicationRecordModel) {
            if (medicationRecordModel != null) {
                ArrayList<MedicationAttachmentModel> urineresultsModels = new ArrayList<MedicationAttachmentModel>(medicationRecordModel.getMedicationAttachmentModels());
                if (urineresultsModels != null) {
                    for (int i = 0; i < urineresultsModels.size(); i++) {
                            MedicationAttachmentModel MedicationAttachmentModel = urineresultsModels.get(i);
                            Log.e("dbchandu", "Attachment" + MedicationAttachmentModel.getIllnessMedicationID());
                            Log.e("deletefetching", "Attachment fectched successfully" + deleteMemberData(MedicationAttachmentModel));
                            deleteMemberData(MedicationAttachmentModel);
                            fetchAttachmentData(MedicationRecordDataController.getInstance().currentMedicationRecordModel);
                            EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("deleteAttachmentData"));
                    }
                    try {
                        MedicalProfileDataController.getInstance().helper.getMedicationAttachmentModelsDao().delete(urineresultsModels);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Log.e("deletefetching", "Attachment fectched successfully" + allAttachmentList.size());
                }
            }

    }
    public void deleteMedicationAttachmentModelData(MedicationRecordModel medicationRecordModel, MedicationAttachmentModel MedicationAttachmentModel) {
        if (medicationRecordModel != null) {
            ArrayList<MedicationAttachmentModel> list = new ArrayList<MedicationAttachmentModel>(medicationRecordModel.getMedicationAttachmentModels());
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getIllnessMedicationID().equals(MedicationAttachmentModel.getIllnessMedicationID())) {
                        MedicationAttachmentModel medicationAttachmentModel = list.get(i);
                        Log.e("dbchandu", "Attachment" + medicationAttachmentModel.getIllnessMedicationID());
                        Log.e("deletefetching", "Attachment fectched successfully" + deleteMemberData(MedicationAttachmentModel));
                        deleteMemberData(MedicationAttachmentModel);
                        fetchAttachmentData(MedicationRecordDataController.getInstance().currentMedicationRecordModel);
                    }
                }
            }
        }
    }
    public boolean deleteMemberData(MedicationAttachmentModel member) {
        try {
            MedicalProfileDataController.getInstance().helper.getMedicationAttachmentModelsDao().delete(member);
            Log.e("Delete", "delete all Attachment");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

 /*public boolean updateAttachmentData(MedicationAttachmentModel user) {
        try {
            UpdateBuilder<MedicationAttachmentModel, Integer> updateBuilder = MedicalProfileDataController.getInstance().helper.getMedicationAttachmentModelDao().updateBuilder();
            updateBuilder.updateColumnValue("medicalCondition", user.getMedicalCondition());
            updateBuilder.updateColumnValue("discription", user.getDiscription());
            updateBuilder.updateColumnValue("age", user.getAge());
            updateBuilder.updateColumnValue("Relation", user.getRelation());
            updateBuilder.updateColumnValue("medicalPersonId", user.getMedicalPersonId());
            updateBuilder.updateColumnValue("medicalRecordId", user.getMedicalRecordId());
            updateBuilder.updateColumnValue("AttachmentRecordId", user.getAttachmentRecordId());
            updateBuilder.updateColumnValue("patientId", user.getPatientId());
            updateBuilder.where().eq("AttachmentRecordId", user.getAttachmentRecordId());
            updateBuilder.update();
            Log.e("update data", "updated the Attachment data sucessfully"+user.getAttachmentRecordId());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return  false;
        }
    }
*/
}
