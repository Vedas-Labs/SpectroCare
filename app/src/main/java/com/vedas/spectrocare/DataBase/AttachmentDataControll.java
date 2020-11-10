package com.vedas.spectrocare.DataBase;

import android.util.Log;

import com.vedas.spectrocare.DataBaseModels.SurgeryAttachModel;
import com.vedas.spectrocare.DataBaseModels.SurgicalRecordModel;
import com.vedas.spectrocare.DataBaseModels.VaccineModel;
import com.vedas.spectrocare.DataBaseModels.VaccineTrackInfoModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AttachmentDataControll {

   /* public ArrayList<SurgeryAttachModel> attachModelArrayList = new ArrayList<>();
    public static SurgricalRecordDataControll myObj;

    public static SurgricalRecordDataControll getInstance() {
        if (myObj == null) {
            myObj = new SurgricalRecordDataControll();
        }
        return myObj;
    }

    public boolean insertTrackData(SurgeryAttachModel userdata) {
        try {
            MedicalProfileDataController.getInstance().helper.getSurgicalAttachDao().create(userdata);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<SurgeryAttachModel> fetchTrackData(SurgicalRecordModel surgicalRecordModel) {
        attachModelArrayList = null;
        attachModelArrayList = new ArrayList<>();
        if(surgicalRecordModel != null){
            ArrayList<SurgeryAttachModel> urineresultsModels = new ArrayList<SurgeryAttachModel>(surgicalRecordModel.getAttachModels());
            if(urineresultsModels != null) {
              //  attachModelArrayList = sortUrineResultsBasedOnTime(urineresultsModels);
                Log.e("chanufetching","track fectched successfully" + attachModelArrayList.size());
            }
        }
        return attachModelArrayList;
    }

    public ArrayList<SurgeryAttachModel> fetchVaccineTrackingBasedOnVaccineRecorsID(SurgicalRecordModel surgicalRecordModel) {
        ArrayList<SurgeryAttachModel> surgicalAttachModelArrayList=new ArrayList<>();
        if (surgicalRecordModel != null) {
            ArrayList<SurgeryAttachModel> list = new ArrayList<SurgeryAttachModel>(surgicalRecordModel.getAttachModels());
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getFilePath().equals(surgicalRecordModel.getAttachment())) {
                        SurgeryAttachModel physicalExamModel = list.get(i);
                        surgicalAttachModelArrayList.add(physicalExamModel);
                    }
                }
                Log.e("familyTrackArrayList", "call" + surgicalAttachModelArrayList.size());
                return surgicalAttachModelArrayList;
            }
        }
        return null;
    }

    public boolean deleteTrackData(SurgeryAttachModel member) {
        try {
            MedicalProfileDataController.getInstance().helper.getSurgicalAttachDao().delete(member);
            //fetchTrackData();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteTrackData(ArrayList<SurgicalRecordModel> patientlProfileModel) {
        for(int i=0;i<patientlProfileModel.size();i++) {
            if (patientlProfileModel != null) {
                ArrayList<SurgeryAttachModel> urineresultsModels = new ArrayList<SurgeryAttachModel>(patientlProfileModel.get(i).getAttachModels());
                if (urineresultsModels != null) {
                    try {
                        MedicalProfileDataController.getInstance().helper.getSurgicalAttachDao().delete(urineresultsModels);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Log.e("deletefetching", "track fectched successfully" + attachModelArrayList.size());
                }
            }
        }
    }

*//*
    public ArrayList<SurgeryAttachModel> sortUrineResultsBasedOnTime(ArrayList<SurgeryAttachModel> allTrackList) {
        Collections.sort(allTrackList, new Comparator<SurgeryAttachModel>() {
            @Override
            public int compare(SurgeryAttachModel s1, SurgeryAttachModel s2) {
                return s1.getDate().compareTo(s2.getDate());
            }
        });
        return allTrackList;

    }
*//*
*/
}
