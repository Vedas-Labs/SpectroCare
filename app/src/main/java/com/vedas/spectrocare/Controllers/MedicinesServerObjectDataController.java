package com.vedas.spectrocare.Controllers;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.DataBase.IllnessDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.MedicationAttachmentRecordDataController;
import com.vedas.spectrocare.DataBase.MedicationRecordDataController;
import com.vedas.spectrocare.DataBase.MedicinesRecordDataController;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.DataBaseModels.MedicationAttachmentModel;
import com.vedas.spectrocare.DataBaseModels.MedicinesRecordModel;
import com.vedas.spectrocare.ServerApiModel.MedicationManullayServerObject;
import com.vedas.spectrocare.ServerApiModel.MedicinesRecordServerObject;

import org.json.JSONException;
import org.json.JSONObject;

public class MedicinesServerObjectDataController {

    public static MedicinesServerObjectDataController myObj;
    Context context;
    public static MedicinesServerObjectDataController getInstance() {
        if (myObj == null) {
            myObj = new MedicinesServerObjectDataController();
        }

        return myObj;
    }
    public void medicineAddData(MedicationManullayServerObject userIdentifier) {
        Log.e("getMannualPrescriptions", "call" + userIdentifier.getIllnessMedicationID());
        JsonObject jsonObject = userIdentifier.getMannualPrescriptions();
        JsonArray jsonArray = jsonObject.get("medicines").getAsJsonArray();
        Log.e("getMannualPrescriptions", "call" + jsonArray.size());
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject singleObject = jsonArray.get(i).getAsJsonObject();
            String purpose = singleObject.get("purpose").getAsString();
            String more = singleObject.get("moreDetails").getAsString();
            String name = singleObject.get("name").getAsString();
            String days = singleObject.get("durationDays").getAsString();
            String dose = singleObject.get("dosage").getAsString();
            String freq = singleObject.get("freq").getAsString();
            Log.e("getMannualPrescriptions", "call" + purpose);
            MedicinesRecordModel historyModel = new MedicinesRecordModel();
            historyModel.setPurpose(purpose);
            historyModel.setName(name);
            historyModel.setMoreDetails(more);
            historyModel.setIllnessMedicationID(userIdentifier.getIllnessMedicationID());
            historyModel.setFreq(freq);
            historyModel.setDurationDays(days);
            historyModel.setDosage(dose);
            historyModel.setUniqueId(String.valueOf(i));
            if (MedicationRecordDataController.getInstance().getMedicationForMedicationId(userIdentifier.getIllnessMedicationID()) != null) {
                historyModel.setMedicationRecordModel(MedicationRecordDataController.getInstance().getMedicationForMedicationId(userIdentifier.getIllnessMedicationID()));
            }
            if (MedicinesRecordDataController.getInstance().insertMedicinesData(historyModel)) {
                MedicinesRecordDataController.getInstance().fetchMedicinesData(MedicationRecordDataController.getInstance().currentMedicationRecordModel);
            }
        }
    }
    public void attachmentsAddData(MedicationManullayServerObject userIdentifier) {
        Log.e("attachmentsAddData", "call" + userIdentifier.getIllnessMedicationID());
        JsonObject jsonObject = userIdentifier.getAttachedPrescriptions();
        JsonArray jsonArray = jsonObject.get("attachments").getAsJsonArray();
        Log.e("attachmentsAddData", "call" + jsonArray.size());
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject singleObject = jsonArray.get(i).getAsJsonObject();
            String filePath = singleObject.get("filePath").getAsString();
            Log.e("filepath", "call" + filePath);
            MedicationAttachmentModel historyModel = new MedicationAttachmentModel();
            historyModel.setIllnessMedicationID(userIdentifier.getIllnessMedicationID());
            historyModel.setFilePath(filePath);
            historyModel.setFilePath2(filePath);
            if (MedicationRecordDataController.getInstance().getMedicationForMedicationId(userIdentifier.getIllnessMedicationID()) != null) {
                historyModel.setMedicationRecordModel(MedicationRecordDataController.getInstance().getMedicationForMedicationId(userIdentifier.getIllnessMedicationID()));
                Log.e("testmodel", "call" + historyModel.getIllnessMedicationID());
            }
            if (MedicationAttachmentRecordDataController.getInstance().insertAttachmentData(historyModel)) {
                MedicationAttachmentRecordDataController.getInstance().fetchAttachmentData(MedicationRecordDataController.getInstance().currentMedicationRecordModel);
            }
        }
    }
}
