package com.vedas.spectrocare.DataBase;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBaseModels.UrineresultsModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by dell on 03-10-2017.
 */
public class UrineResultsDataController {
    public ArrayList<UrineresultsModel> allUrineResults = new ArrayList<>();
    public static UrineResultsDataController myObj;
    public UrineresultsModel currenturineresultsModel;


    public static UrineResultsDataController getInstance() {
        if (myObj == null) {
            myObj = new UrineResultsDataController();
        }
        return myObj;
    }
    public boolean insertUrineResultsForMember(UrineresultsModel urineresultsModel) {
        try {
            MedicalProfileDataController.getInstance().helper.getUrineresultsDao().create(urineresultsModel);
            fetchAllUrineResults();
            return  true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public ArrayList<UrineresultsModel> fetchAllUrineResults() {
        allUrineResults = new ArrayList<UrineresultsModel>();
        if (PatientLoginDataController.getInstance().currentPatientlProfile != null) {
            ArrayList<UrineresultsModel> urineresultsModels = new ArrayList<UrineresultsModel>(PatientLoginDataController.getInstance().currentPatientlProfile.getUrineresultsModels());
            Log.e("call", "" + urineresultsModels);
            if (urineresultsModels != null) {
                allUrineResults = urineresultsModels;
                if (allUrineResults.size() > 0) {
                    currenturineresultsModel = allUrineResults.get(allUrineResults.size() - 1);
                    allUrineResults = sortUrineResultsBasedOnTime(allUrineResults);
                }
            }
        }
        return allUrineResults;
    }


    public ArrayList<UrineresultsModel> sortUrineResultsBasedOnTime(ArrayList<UrineresultsModel> urineResults) {
        Collections.sort(urineResults, new Comparator<UrineresultsModel>() {
            @Override
            public int compare(UrineresultsModel s1, UrineresultsModel s2) {
                return s1.getTestedTime().compareTo(s2.getTestedTime());
            }
        });
        return urineResults;

    }

    public boolean deleteurineresultsData(UrineresultsModel urineresultsModel) {
        try {
            MedicalProfileDataController.getInstance().helper.getUrineresultsDao().delete(urineresultsModel);
            Log.e("Delete", "delete all urineresultsModel");
            fetchAllUrineResults();
            Log.e("deleteUrineRecord", "call" + UrineResultsDataController.getInstance().allUrineResults.size());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void deleteUrineResults(List<UrineresultsModel> results_list) {
        try {
            MedicalProfileDataController.getInstance().helper.getUrineresultsDao().delete(results_list);
            Log.e("deleted", "deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*public void deleteUrineResultApi() {
        JSONObject fetchObject = new JSONObject();
        try {
            fetchObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
            fetchObject.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            fetchObject.put("byWhomID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            fetchObject.put("byWhom","patient");
            fetchObject.put("medical_record_id", PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
            fetchObject.put("testReportNumber", UrineResultsDataController.getInstance().currenturineresultsModel.getTestReportNumber());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(fetchObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.
                        deleteUrineResultApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "delete");
    }*/

}
