package com.vedas.spectrocare.Controllers;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.DataBase.DoctorInfoDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBaseModels.DoctorInfoModel;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class DoctorInfoServerObjectDataController {
    public static DoctorInfoServerObjectDataController myObj;
    Context context;
    public ArrayList<String> visiteTypeArray = new ArrayList<>();
    public ArrayList<String> statusTypeArray = new ArrayList<>();
    public ArrayList<String> departmentArray = new ArrayList<>();

    public ArrayList<String> selectedVisiteTypeArray = new ArrayList<>();
    public ArrayList<String> selectedStatusTypeArray = new ArrayList<>();
    public ArrayList<String> selectedDepartmentArray = new ArrayList<>();

   public String[] specializationArray = {"Allergist", "Anasthesiologist", "Andrologist", "Cardiology",
            "Cardiac Electrophysiologist", "Dermatologist", "Emergency Medicine / Emergency (ER) Doctors",
            "Endocrinologist", "Epidemiologist", "Family Medicine Physician", "Gastroenterologist", "Geriatrician",
            "Hyperbar Physician", "Hematologist", "Hepatologist", "Immunologist", "Infectious Disease Specialist",
            "Intensivist", "Internal Medicine Specialist", "Maxillofacial Surgeon / Oral Surgeon", "Medical Geneticist",
            "Neonatologist", "Nephrologist", "Neurologist", "Neurosurgeon", "Nuclear Medicine Specialist",
            "Obstetrician/Gynecologist (OB/GYN)", "Occupational Medicine Specialist", "Oncologist", "Ophthalmologist",
            "Orthopedic Surgeon / Orthopedist", "Otolaryngologist (also ENT Specialist)", "Parasitologist",
            "Pathologist", "Perinatologist", "Periodontist", "Pediatrician", "Plastic Surgeon", "Physiatrist",
            "Pulmonologist", "Radiologist", "Rheumatologist", "Sleep Doctor / Sleep Disorders Specialist",
            "Spinal Cord Injury Specialist", "Sports Medicine Specialist", "Surgeon", "Thoracic Surgeon",
            "Urologist", "Vascular Surgeon", "Audiologist", "Veterinarian", "Chiropractor", "Diagnostician",
            "Microbiologist", "Palliative care specialist", "Pharmacist", "Physiotherapist", "Podiatrist / Chiropodist"};

    public static DoctorInfoServerObjectDataController getInstance() {
        if (myObj == null) {
            myObj = new DoctorInfoServerObjectDataController();
        }
        return myObj;
    }
    public void clearFilterData(){
        selectedDepartmentArray=new ArrayList<>();
        selectedVisiteTypeArray=new ArrayList<>();
        selectedStatusTypeArray=new ArrayList<>();
    }
    public void processFilterArrays() {

        visiteTypeArray = new ArrayList<>();
        statusTypeArray = new ArrayList<>();
        departmentArray = new ArrayList<>();

        visiteTypeArray.add("online");
        visiteTypeArray.add("onsite");

        statusTypeArray.add("Waiting");
        statusTypeArray.add("Reschedule");
        statusTypeArray.add("Completed");
        statusTypeArray.add("Cancelled");
        statusTypeArray.add("Accepted");

        for(int i=0;i<specializationArray.length;i++){
            departmentArray.add(specializationArray[i]);
        }
        Log.e("departmentArray", "sizecall: " + departmentArray.size());
    }

    public void fetchHospitaldoctors() {
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        JSONObject params = new JSONObject();
        try {
            params.put("hospital_reg_num", currentMedical.getHospital_reg_number());
            params.put("userID", "viswanath3344@gmail.com");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.fetchDoctorInfo(currentMedical.getAccessToken(), gsonObject), "fetchDoctor");
    }

    public void processDoctorsData(JSONArray jsonArray) {
        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String name = jsonObject.getString("firstName");
                    String lastName = jsonObject.getString("lastName");
                    Log.e("doctorsname", "call" + name + " " + lastName);
                    String dept = jsonObject.getString("department");
                    String email = jsonObject.getString("emailID");
                    String specialization = jsonObject.getString("specialization");
                    String medicalPersonnelId = jsonObject.getString("medical_personnel_id");
                    String hospitalRegNum = jsonObject.getString("hospital_reg_num");
                    DoctorInfoModel doctorInfoModel = new DoctorInfoModel();
                    doctorInfoModel.setHospitalRegNum(hospitalRegNum);
                    doctorInfoModel.setMedicalPersonId(medicalPersonnelId);
                    doctorInfoModel.setSpecialization(specialization);
                    doctorInfoModel.setDepartment(dept);
                    doctorInfoModel.setEmailID(email);
                    doctorInfoModel.setDoctorName(name + " " + lastName);
                    if (DoctorInfoDataController.getInstance().insertDoctorInfoData(doctorInfoModel)) {
                        Log.e("doctorsArray", "call" + DoctorInfoDataController.getInstance().fetchDoctorInfoData());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    public void loadJson() {
        JSONObject params = new JSONObject();
        try {
            params.put("username", "viswanath3344@gmail.com");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.loadjsons(gsonObject), "fetchDoctor");
    }
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
    {
        Set<T> set = new LinkedHashSet<>();
        set.addAll(list);
        list.clear();
        list.addAll(set);
        return list;
    }
}
