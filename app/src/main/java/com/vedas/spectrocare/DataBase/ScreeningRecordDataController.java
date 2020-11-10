
package com.vedas.spectrocare.DataBase;

import android.util.Log;
import com.vedas.spectrocare.Controllers.PhysicalServerObjectDataController;
import com.vedas.spectrocare.DataBaseModels.ScreeningRecordModel;
import com.vedas.spectrocare.DataBaseModels.PatientlProfileModel;
import com.vedas.spectrocare.DataBaseModels.SurgicalRecordModel;
import com.vedas.spectrocare.ServerApiModel.ScreeningServerObject;
import com.vedas.spectrocare.ServerApiModel.SurgicalServerObject;
import com.vedas.spectrocare.ServerApiModel.TrackingServerObject;

import org.greenrobot.eventbus.EventBus;
import java.sql.SQLException;
import java.util.ArrayList;

public class ScreeningRecordDataController {
    public ArrayList<ScreeningRecordModel> allScreeningList = new ArrayList<>();
    public ScreeningRecordModel currentScreeningRecordModel;
    public static ScreeningRecordDataController myObj;

    public static ScreeningRecordDataController getInstance() {
        if (myObj == null) {
            myObj = new ScreeningRecordDataController();
        }
        return myObj;
    }

    public boolean insertScreeningData(ScreeningRecordModel userdata) {
        try {
            MedicalProfileDataController.getInstance().helper.getScreeningRecordModels().create(userdata);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public ArrayList<ScreeningRecordModel> fetchScreeningData(PatientlProfileModel physicalExamsDataModel) {
        allScreeningList = null;
        allScreeningList = new ArrayList<>();
        if(physicalExamsDataModel != null){
            ArrayList<ScreeningRecordModel> urineresultsModels = new ArrayList<ScreeningRecordModel>(physicalExamsDataModel.getScreeningRecordModels());
            if(urineresultsModels != null) {
                allScreeningList = urineresultsModels;
                Log.e("fetchScreeningData", "Screening fectched successfully" + allScreeningList.size());
            }
        }
        return allScreeningList;
    }


    public void deleteScreeningData(PatientlProfileModel patientlProfileModels) {
            if (patientlProfileModels != null) {
                ArrayList<ScreeningRecordModel> urineresultsModels = new ArrayList<ScreeningRecordModel>(patientlProfileModels.getScreeningRecordModels());
                if (urineresultsModels != null) {
                    for (int i = 0; i < urineresultsModels.size(); i++) {
                            ScreeningRecordModel ScreeningRecordModel = urineresultsModels.get(i);
                            Log.e("dbchandu", "Screening" + ScreeningRecordModel.getPatientId());
                            Log.e("deletefetching", "Screening fectched successfully" + deleteMemberData(ScreeningRecordModel));
                            deleteMemberData(ScreeningRecordModel);
                            fetchScreeningData(PatientProfileDataController.getInstance().currentPatientlProfile);
                            EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("deleteScreeningData"));
                    }
                    try {
                        MedicalProfileDataController.getInstance().helper.getScreeningRecordModels().delete(urineresultsModels);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Log.e("deletefetching", "Screening fectched successfully" + allScreeningList.size());
                }
            }

    }
    public void deleteScreeningRecordModelData(PatientlProfileModel patientlProfileModel,ScreeningRecordModel screeningRecordModel) {
        if (patientlProfileModel != null) {
            ArrayList<ScreeningRecordModel> list = new ArrayList<ScreeningRecordModel>(patientlProfileModel.getScreeningRecordModels());
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getScreeningID().equals(screeningRecordModel.getScreeningID())) {
                        ScreeningRecordModel screeningRecordModel1 = list.get(i);
                        Log.e("dbchandu", "Screening" + screeningRecordModel1.getPatientId());
                        Log.e("deletefetching", "Screening fectched successfully" + deleteMemberData(screeningRecordModel1));
                         deleteMemberData(screeningRecordModel1);
                         fetchScreeningData(PatientProfileDataController.getInstance().currentPatientlProfile);
                         EventBus.getDefault().post(new PhysicalServerObjectDataController.MessageEvent("deleteScreeningData"));
                    }
                }
            }
        }
    }
    public boolean deleteMemberData(ScreeningRecordModel member) {
        try {
            MedicalProfileDataController.getInstance().helper.getScreeningRecordModels().delete(member);
            Log.e("Delete", "delete all Screening");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void processfetchSurgeryAddData(SurgicalServerObject userIdentifier) {

        SurgicalRecordModel historyModel = new SurgicalRecordModel();
        historyModel.setIllnessSurgicalId(userIdentifier.getIllnessSurgicalID());
        historyModel.setDoctorName(userIdentifier.getDoctorName());
        historyModel.setDoctorMedicalPersonnelId(userIdentifier.getDoctorMedicalPersonnelId());
        historyModel.setMoreInfo(userIdentifier.getMoreDetails());
       /* ScreeningRecordModel historyModel = new ScreeningRecordModel();
        historyModel.setPatientId(userIdentifier.getPatientID());
        //historyModel.setMedicalPersonId(userIdentifier.get());
        historyModel.setMedicalRecordId(userIdentifier.getMedical_record_id());
        historyModel.setPatientlProfileModel(PatientProfileDataController.getInstance().currentPatientlProfile);
        historyModel.setScreeningID(userIdentifier.getIllnessScreeningID());
        historyModel.setMoreInfo(userIdentifier.getRecordMoreDetails());
        historyModel.setAttachment(userIdentifier.getRecordFilePath());
*/

        if (SurgricalRecordDataControll.getInstance().insertSurgicalData(historyModel)) {
            SurgricalRecordDataControll.getInstance().fetchingSurgicalData(IllnessDataController.getInstance().currentIllnessRecordModel);
        }
        /*if (trackArray.size() > 0) {
            for (int index = 0; index < trackArray.size(); index++) {
                processFamilytrackData(trackArray.get(index),historyModel);
            }
        }*/
    }


 /*public boolean updateScreeningData(ScreeningRecordModel user) {
        try {
            UpdateBuilder<ScreeningRecordModel, Integer> updateBuilder = MedicalProfileDataController.getInstance().helper.getScreeningRecordModelDao().updateBuilder();
            updateBuilder.updateColumnValue("medicalCondition", user.getMedicalCondition());
            updateBuilder.updateColumnValue("discription", user.getDiscription());
            updateBuilder.updateColumnValue("age", user.getAge());
            updateBuilder.updateColumnValue("Relation", user.getRelation());
            updateBuilder.updateColumnValue("medicalPersonId", user.getMedicalPersonId());
            updateBuilder.updateColumnValue("medicalRecordId", user.getMedicalRecordId());
            updateBuilder.updateColumnValue("ScreeningRecordId", user.getScreeningRecordId());
            updateBuilder.updateColumnValue("patientId", user.getPatientId());
            updateBuilder.where().eq("ScreeningRecordId", user.getScreeningRecordId());
            updateBuilder.update();
            Log.e("update data", "updated the Screening data sucessfully"+user.getScreeningRecordId());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return  false;
        }
    }*/

}
