
package com.vedas.spectrocare.DataBase;

import android.util.Log;

import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.spectrocare.Controllers.PhysicalServerObjectDataController;
import com.vedas.spectrocare.DataBaseModels.MedicationRecordModel;
import com.vedas.spectrocare.DataBaseModels.MedicinesRecordModel;

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;
import java.util.ArrayList;

public class MedicinesRecordDataController {
    public ArrayList<MedicinesRecordModel> allMedicinesList = new ArrayList<>();
    public MedicinesRecordModel currentMedicinesRecordModel;
    public static MedicinesRecordDataController myObj;

    public static MedicinesRecordDataController getInstance() {
        if (myObj == null) {
            myObj = new MedicinesRecordDataController();
        }
        return myObj;
    }

    public boolean insertMedicinesData(MedicinesRecordModel userdata) {
        try {
            MedicalProfileDataController.getInstance().helper.getMedicinesRecordModelsDao().create(userdata);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public ArrayList<MedicinesRecordModel> fetchMedicinesData(MedicationRecordModel medicationRecordModel) {
        allMedicinesList = null;
        allMedicinesList = new ArrayList<>();
        if(medicationRecordModel != null){
            ArrayList<MedicinesRecordModel> urineresultsModels = new ArrayList<MedicinesRecordModel>(medicationRecordModel.getMedicinesRecordModels());
            if(urineresultsModels != null) {
                allMedicinesList = urineresultsModels;
                Log.e("fetchMedicinesData", "Medicines fectched successfully" + allMedicinesList.size());
            }
        }
        return allMedicinesList;
    }

    public void deleteMedicinesData(MedicationRecordModel medicationRecordModel) {
            if (medicationRecordModel != null) {
                ArrayList<MedicinesRecordModel> urineresultsModels = new ArrayList<MedicinesRecordModel>(medicationRecordModel.getMedicinesRecordModels());
                if (urineresultsModels != null) {
                    for (int i = 0; i < urineresultsModels.size(); i++) {
                            MedicinesRecordModel medicinesRecordModel = urineresultsModels.get(i);
                            Log.e("dbchandu", "Medicines" + medicinesRecordModel.getIllnessMedicationID());
                            Log.e("deletefetching", "Medicines fectched successfully" + deleteMemberData(medicinesRecordModel));
                            deleteMemberData(medicinesRecordModel);
                            fetchMedicinesData(MedicationRecordDataController.getInstance().currentMedicationRecordModel);
                    }
                    try {
                        MedicalProfileDataController.getInstance().helper.getMedicinesRecordModelsDao().delete(urineresultsModels);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Log.e("deletefetching", "Medicines fectched successfully" + allMedicinesList.size());
                }
            }

    }
    public void deleteMedicinesRecordModelData(MedicationRecordModel medicationRecordModel, MedicinesRecordModel MedicinesRecordModel) {
        if (medicationRecordModel != null) {
            ArrayList<MedicinesRecordModel> list = new ArrayList<MedicinesRecordModel>(medicationRecordModel.getMedicinesRecordModels());
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getIllnessMedicationID().equals(MedicinesRecordModel.getIllnessMedicationID())) {
                        MedicinesRecordModel medicinesRecordModel = list.get(i);
                        Log.e("dbchandu", "Medicines" + medicationRecordModel.getIllnessID());
                        Log.e("deletefetching", "Medicines fectched successfully" + deleteMemberData(MedicinesRecordModel));
                        deleteMemberData(medicinesRecordModel);
                        fetchMedicinesData(MedicationRecordDataController.getInstance().currentMedicationRecordModel);
                    }
                }
            }
        }
    }
    public boolean deleteMemberData(MedicinesRecordModel member) {
        try {
            MedicalProfileDataController.getInstance().helper.getMedicinesRecordModelsDao().delete(member);
            Log.e("Delete", "delete all Medicines");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
 public boolean updateMedicinesData(MedicinesRecordModel user) {
        try {
            UpdateBuilder<MedicinesRecordModel, Integer> updateBuilder = MedicalProfileDataController.getInstance().helper.getMedicinesRecordModelsDao().updateBuilder();
            updateBuilder.updateColumnValue("dosage",user.getDosage() );
            updateBuilder.updateColumnValue("purpose", user.getPurpose());
            updateBuilder.updateColumnValue("name", user.getName());
            updateBuilder.updateColumnValue("moreDetails", user.getMoreDetails());
            updateBuilder.updateColumnValue("illnessMedicationID",user.getIllnessMedicationID() );
            updateBuilder.updateColumnValue("freq", user.getFreq());
            updateBuilder.updateColumnValue("durationDays", user.getDurationDays());
            updateBuilder.where().eq("Id",user.getId());
            updateBuilder.update();
            Log.e("updateMedicinesData", "updated the Medicines data sucessfully"+user.getId()+"cccccccc"+user.getDurationDays());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return  false;
        }
    }

}
