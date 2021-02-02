package com.vedas.spectrocare.PatientServerApiModel;
import android.content.Context;

import com.vedas.spectrocare.PatientAppointmentModule.AppointmentArrayModel;
import com.vedas.spectrocare.PatientDocResponseModel.MedicalPersonnelModel;
import com.vedas.spectrocare.model.AppointmetModel;
import com.vedas.spectrocare.model.CategoryItemModel;
import com.vedas.spectrocare.model.ServicesModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import kotlin.time.AbstractDoubleTimeSource;

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
    public boolean isFromLogin = false;

    // public boolean isFromDiagnosisRecordPage = false;

    public ArrayList<PatientMedicationRecordModel> allMedicationList = new ArrayList<>();
    public PatientMedicationRecordModel currentMedicationRecordModel;

    public  ArrayList<PatientMedicinesRecordModel> viewMedicalModelList = new ArrayList<>();
    public PatientMedicinesRecordModel currentListviewModel=null;

    public ArrayList<ServicesModel> servicesModelArrayList;
    public ServicesModel currentServicesModel=null;

    public ArrayList<PatientMyDevicesObject> myDevicesArrayList;
    public ArrayList<PatientMyDevicesObject> otherDevicesArrayList;
    public PatientMyDevicesObject selectedDevice = null;

    //for doctors module
    // MedicalPersonnelModel medicalPersonnelModel=null;
    public MedicalPersonnelModel medicalPersonnelModel=null;
    public CategoryItemModel selectedCategoryItemModel=null;
    public TestItemsModel selectedTestItem=null;

    public ArrayList<MedicalPersonnelModel> doctorProfleList;
    public AppointmentArrayModel selectedappointmnetModel=null;
    /*public ArrayList<DoctorDetailsModel> latestSearchDoctorsList;
    public DoctorDetailsModel selectedDoctorDetailsModel=null;
*/

    public ArrayList<InvoiceModel> invoiceList;
    public InvoiceModel invoiceObject=null;

    public ArrayList<InboxNotificationModel> inboxNotificationList;
    public InboxNotificationModel inboxNotificationModel=null;

    public ArrayList<ArrayList<ChatRoomMessageModel>> chatRoomMessageList;
    public ChatRoomMessageModel chatRoomMessageModel=null;

    public static PatientMedicalRecordsController getInstance() {
        if (myObj == null) {
            myObj = new PatientMedicalRecordsController();
        }
        return myObj;
    }

    public void fillContent(Context context1) {
        context = context1;
        servicesModelArrayList=new ArrayList<>();
        allergyObjectArrayList = new ArrayList<>();
        noteallergyArray = new ArrayList<>();
        immunizationArrayList = new ArrayList<>();
        surgeryObjectArrayList = new ArrayList<>();
        diagnosisObjectArrayList = new ArrayList<>();
        medicationArrayList = new ArrayList<>();
        medicatioArrayObjectsList = new ArrayList<>();
        viewMedicalModelList=new ArrayList<>();
        myDevicesArrayList=new ArrayList<>();
        otherDevicesArrayList=new ArrayList<>();
        doctorProfleList=new ArrayList<>();
        invoiceList=new ArrayList<>();
        inboxNotificationList=new ArrayList<>();
        chatRoomMessageList=new ArrayList<>();
    }

}
