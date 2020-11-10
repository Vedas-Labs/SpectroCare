package com.vedas.spectrocare.PatientServerApiModel;
import android.content.Context;
import android.widget.ArrayAdapter;

import com.vedas.spectrocare.DataBaseModels.MedicationRecordModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
public class PatientMedicalRecordsController {
    public static PatientMedicalRecordsController myObj;
    Context context;
    public ArrayList<AllergyObject> allergyObjectArrayList;
    public ArrayList<AllergyListObject> noteallergyArray;

    public AllergyListObject selectedObject;
    public int selectedPos = -1;
    public ArrayList<PatientSurgicalObject> surgeryObjectArrayList;
    public PatientSurgicalObject selectedSurgeryObject;

    public ArrayList<DiagnosisObject> diagnosisObjectArrayList;
    public DiagnosisObject selectedDiagnosisObject;


    public ArrayList<ImmunizationObject> immunizationArrayList;
    public ImmunizationObject selectedImmunizationObject;

    public ArrayList<PatientMedicationObject> medicationArrayList;
    public PatientMedicationObject selectedMedication;

    public ArrayList<PatientMedicationArrayObject> medicatioArrayObjectsList;
    public PatientMedicationArrayObject selectedMedicationArrayObj;

    public boolean isFromDiagnosis = false;
    public boolean isFromMedication = false;

    // public boolean isFromDiagnosisRecordPage = false;

    public ArrayList<PatientMedicationRecordModel> allMedicationList = new ArrayList<>();
    public PatientMedicationRecordModel currentMedicationRecordModel;

    public  ArrayList<PatientMedicinesRecordModel> viewMedicalModelList = new ArrayList<>();
    public PatientMedicinesRecordModel currentListviewModel=null;

    public static PatientMedicalRecordsController getInstance() {
        if (myObj == null) {
            myObj = new PatientMedicalRecordsController();
        }
        return myObj;
    }

    public void fillContent(Context context1) {
        context = context1;
        allergyObjectArrayList = new ArrayList<>();
        noteallergyArray = new ArrayList<>();
        immunizationArrayList = new ArrayList<>();
        surgeryObjectArrayList = new ArrayList<>();
        diagnosisObjectArrayList = new ArrayList<>();
        medicationArrayList = new ArrayList<>();
        medicatioArrayObjectsList = new ArrayList<>();
        viewMedicalModelList=new ArrayList<>();
    }

}
