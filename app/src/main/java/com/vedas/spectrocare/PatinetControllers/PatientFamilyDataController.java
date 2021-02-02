package com.vedas.spectrocare.PatinetControllers;

import android.util.Log;

import com.vedas.spectrocare.Controllers.AllergiesServerObjectDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBaseModels.PhysicalExamsDataModel;
import com.vedas.spectrocare.PatientServerApiModel.FamilyDetaislModel;
import com.vedas.spectrocare.PatientServerApiModel.IllnessPatientRecord;
import com.vedas.spectrocare.PatientServerApiModel.IllnessScreenigRecord;
import com.vedas.spectrocare.PatientServerApiModel.PatientFamilyAddServerObject;
import com.vedas.spectrocare.PatientServerApiModel.PatientIllnessServerResponse;
import com.vedas.spectrocare.PatientServerApiModel.PatientPhysicalRecordModel;
import com.vedas.spectrocare.ServerApiModel.PhysicalExamServerObject;
import com.vedas.spectrocare.ServerApiModel.ScreeningServerObject;

import java.util.ArrayList;

public class PatientFamilyDataController {
    public static PatientFamilyDataController myObj;
    public PatientFamilyAddServerObject responseObject;
    public PatientIllnessServerResponse illnessServerResponse;
    ArrayList<IllnessPatientRecord> illnessPatientList;
    ArrayList<FamilyDetaislModel> famliyDiseases;
    public IllnessPatientRecord selectedIllnessRecord;
    PatientPhysicalRecordModel physicalRecordModel;
    ArrayList<IllnessScreenigRecord> illnessScreeningRecords;

    public static PatientFamilyDataController getInstance(){
        if (myObj==null){

            Log.e("shb","dh");
            myObj = new PatientFamilyDataController();
        }else {
            Log.e("shdsb","dh");
        }

        return myObj;
    }

    public ArrayList<IllnessScreenigRecord> getIllnessScreeningRecords() {
        return illnessScreeningRecords;
    }

    public void setIllnessScreeningRecords(ArrayList<IllnessScreenigRecord> illnessScreeningRecords) {
        this.illnessScreeningRecords = illnessScreeningRecords;
    }

    public PatientPhysicalRecordModel getPhysicalRecordModel() {
        return physicalRecordModel;
    }

    public void setPhysicalRecordModel(PatientPhysicalRecordModel physicalRecordModel) {
        this.physicalRecordModel = physicalRecordModel;
    }

    public PatientFamilyAddServerObject getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(PatientFamilyAddServerObject responseObject) {
        this.responseObject = responseObject;
    }

    public PatientIllnessServerResponse getIllnessServerResponse() {
        return illnessServerResponse;
    }

    public void setIllnessServerResponse(PatientIllnessServerResponse illnessServerResponse) {
        this.illnessServerResponse = illnessServerResponse;
    }

    public ArrayList<IllnessPatientRecord> getIllnessPatientList() {
        return illnessPatientList;
    }

    public void setIllnessPatientList(ArrayList<IllnessPatientRecord> illnessPatientList) {
        this.illnessPatientList = illnessPatientList;
    }

    public ArrayList<FamilyDetaislModel> getFamliyDiseases() {
        return famliyDiseases;
    }

    public void setFamliyDiseases(ArrayList<FamilyDetaislModel> famliyDiseases) {
        this.famliyDiseases = famliyDiseases;
    }

    public  static PatientFamilyDataController setNull(){

        myObj=null;
        return myObj;
    }
}
