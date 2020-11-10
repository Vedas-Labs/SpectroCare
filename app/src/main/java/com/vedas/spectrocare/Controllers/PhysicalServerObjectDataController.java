package com.vedas.spectrocare.Controllers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.DataBase.BmiDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBase.PhysicalCategoriesDataController;
import com.vedas.spectrocare.DataBase.PhysicalExamDataController;
import com.vedas.spectrocare.DataBase.PhysicalExamTrackInfoDataController;
import com.vedas.spectrocare.DataBaseModels.BMIModel;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.DataBaseModels.PhysicalCategoriesRecords;
import com.vedas.spectrocare.DataBaseModels.PhysicalExamsDataModel;
import com.vedas.spectrocare.DataBaseModels.PhysicalTrackInfoModel;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.ServerApiModel.BodyIndexServerObject;
import com.vedas.spectrocare.ServerApiModel.PhysicalExamServerObject;
import com.vedas.spectrocare.ServerApiModel.PhysicalRecordServerObject;
import com.vedas.spectrocare.ServerApiModel.TrackingServerObject;
import com.vedas.spectrocare.activities.PhysicalExamRecordActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PhysicalServerObjectDataController {


    public static PhysicalServerObjectDataController myObj;
    Context context;
    public boolean isFromStaring=false;
    public static PhysicalServerObjectDataController getInstance() {
        if (myObj == null) {
            myObj = new PhysicalServerObjectDataController();
        }
        return myObj;
    }

    public void fillContent(Context context1) {
        context = context1;
    }

   public void fetchPhysicalExamServerApi(){

       MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;

       final PhysicalExamServerObject serverObject=new PhysicalExamServerObject();
       serverObject.setHospital_reg_num(currentMedical.getHospital_reg_number());
      // serverObject.setMedical_personnel_id(currentMedical.getMedical_person_id());
       serverObject.setMedical_record_id(PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
       serverObject.setPatientID(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
       serverObject.setByWhom("medical personnel");
       serverObject.setByWhomID(currentMedical.getMedical_person_id());
       Log.e("serverObject","check"+PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
       Log.e("objectH",""+serverObject.getHospital_reg_num());
       Log.e("objectMP",""+currentMedical.getMedical_person_id());
       Log.e("objectMR",""+serverObject.getMedical_record_id());
       Log.e("objectP",""+serverObject.getPatientID());
       Log.e("token",""+currentMedical.getAccessToken());


       Retrofit retrofit = new Retrofit.Builder()
               .baseUrl(ServerApi.home_url)
               .addConverterFactory(GsonConverterFactory.create())
               .build();
       ServerApi serverApi = retrofit.create(ServerApi.class);
       Call<PhysicalExamServerObject> callable=serverApi.fetchPhysicalExamHistory(currentMedical.getAccessToken(),serverObject);

       callable.enqueue(new Callback<PhysicalExamServerObject>() {
           @Override
           public void onResponse(Call<PhysicalExamServerObject> call, Response<PhysicalExamServerObject> response) {
               Log.e("ressss",""+response);
               if (response.body() != null) {
                   String statusCode = response.body().getResponse();
                   String messageStatus = response.body().getMessage();
                   Log.e("codefor3", "call" + response.body().getTrackingServerObjects());
                   if (statusCode.equals("3")) {
                       BmiDataController.getInstance().deleteBmiData(PhysicalExamDataController.getInstance().allPhysicalExamList);
                       PhysicalExamTrackInfoDataController.getInstance().deleteTrackData(PhysicalExamDataController.getInstance().allPhysicalExamList);
                       PhysicalCategoriesDataController.getInstance().deletePhysicalExamData(PhysicalExamDataController.getInstance().allPhysicalExamList);
                       PhysicalExamDataController.getInstance().deletePhysicalExamData(PatientProfileDataController.getInstance().allPatientlProfile);

                       ArrayList<PhysicalExamServerObject> list=response.body().getPhysical_exam_records();
                       Log.e("allcodefor3", "call" + list.size());

                       for(int i=0;i<list.size();i++){
                           PhysicalExamServerObject serverObject1=list.get(i);
                           Log.e("allcodefor3", "call" + serverObject1.getPhysical_exam_id()+serverObject1.getAttachment());

                           Log.e("otherr",""+serverObject1.getOther());
                           Log.e("commentt",""+serverObject1.getPhysicianCommentsOrRecomdations());

                           if(serverObject1.getAttachment() == null) {

                               PhysicalExamsDataModel objPhysical =new PhysicalExamsDataModel();
                               objPhysical.setPatientId(serverObject1.getPatientID());
                               objPhysical.setHospital_reg_number(serverObject1.getHospital_reg_num());
                               objPhysical.setMedicalPersonId(serverObject1.getMedical_personnel_id());
                               objPhysical.setMedicalRecordId(serverObject1.getMedical_record_id());
                               objPhysical.setPhysicalExamId(serverObject1.getPhysical_exam_id());
                               objPhysical.setOther(serverObject1.getOther());
                               objPhysical.setPhysicianComments(serverObject1.getPhysicianCommentsOrRecomdations());
                               objPhysical.setAttachment(serverObject1.getAttachment());
                               objPhysical.setPatientlProfileModel(PatientProfileDataController.getInstance().currentPatientlProfile);

                               if(PhysicalExamDataController.getInstance().insertPhysicalExamData(objPhysical)){
                                   Log.e("fetchPhysicalExam", "PhysicalExam fectched successfully" + PhysicalExamDataController.getInstance().fetchPhysicalExamData(PatientProfileDataController.getInstance().currentPatientlProfile));

                               }

                               BodyIndexServerObject indexServerObject= serverObject1.getBodyIndexArrayList();
                               // Log.e("bmi", "call" + indexServerObject.getHeight());

                               BMIModel bmiModel=new BMIModel();
                               bmiModel.setPhysicalExamId(serverObject1.getPhysical_exam_id());
                               bmiModel.setHeight(indexServerObject.getHeight());
                               bmiModel.setWeight(indexServerObject.getWeight());
                               bmiModel.setBmi(indexServerObject.getBmi());
                               bmiModel.setWaistLine(indexServerObject.getWaistline());
                               bmiModel.setBloodPressure(indexServerObject.getBloodPressure());
                               bmiModel.setPatientId(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
                               bmiModel.setPhysicalExamsDataModel(objPhysical);
                               bmiModel.setMedicalRecordId(serverObject1.getMedical_record_id());

                               Log.e("bmiModel", "call" + bmiModel.getHeight()+bmiModel.getWeight()+bmiModel.getBmi());

                               if(BmiDataController.getInstance().insertBmiData(bmiModel)){
                             /* Log.e("fetchbmidata", "call" + BmiDataController.getInstance().fetchBmiData(
                                      PatientProfileDataController.getInstance().currentPatientlProfile
                              ));*/
                               }
                               ArrayList<PhysicalRecordServerObject> physicalArrayList= serverObject1.getPhysicalExaminations();
                               Log.e("physicalArrayList", "call" + physicalArrayList.size());

                               PhysicalCategoriesRecords physicalExamModel=new PhysicalCategoriesRecords();

                               for(int index=0; index<physicalArrayList.size();index++){
                                   PhysicalRecordServerObject objPhysical1=physicalArrayList.get(index);

                                  // PhysicalCategoriesRecords physicalExamModel=new PhysicalCategoriesRecords();

                                   physicalExamModel.setPhysicalExamId(serverObject1.getPhysical_exam_id());
                                   physicalExamModel.setCategory(objPhysical1.getCategory());
                                   physicalExamModel.setDescription(objPhysical1.getDescription());
                                   physicalExamModel.setResult(objPhysical1.getResult());
                                   physicalExamModel.setPatientId(response.body().getPatientID());
                                   physicalExamModel.setPhysicianComment(response.body().getPhysicianCommentsOrRecomdations());

                                   physicalExamModel.setPhysicalExamsDataModel(objPhysical);
                                   physicalExamModel.setMedicalRecordId(response.body().getMedical_record_id());

                                   Log.e("physicalExamModel", "call" + physicalExamModel.getCategory());
                                   Log.e("physicalExamModel", "call" + physicalExamModel.getResult());

                                   if(PhysicalCategoriesDataController.getInstance().insertPhysicalExamData(physicalExamModel)){
                                 /*  Log.e("fetchbmidata", "call" + PhysicalCategoriesDataController.getInstance().fetchPhysicalExamData(
                                           PatientProfileDataController.getInstance().currentPatientlProfile
                                   ));*/
                                   }
                               }
                               ArrayList<TrackingServerObject> trackingServerObjects= serverObject1.getTrackingServerObjects();
                               Log.e("trackingServerObjects", "call" + trackingServerObjects.size());

                               if (trackingServerObjects.size() > 0) {

                                   for(int index=0;index<trackingServerObjects.size();index++){
                                       TrackingServerObject serverObject=trackingServerObjects.get(index);
                                       PhysicalTrackInfoModel trackInfoModel=new PhysicalTrackInfoModel();
                                       trackInfoModel.setPhysicalExamsDataModel(objPhysical);
                                       trackInfoModel.setPatientId(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
                                       trackInfoModel.setPhysicalExamId(serverObject1.getPhysical_exam_id());
                                       trackInfoModel.setByWhom(serverObject.getByWhom());
                                       trackInfoModel.setByWhomId(serverObject.getByWhomID());
                                       Long l=Long.parseLong(serverObject.getDate())/1000;
                                       trackInfoModel.setDate(String.valueOf(l));
                                       Log.e("trackingServerObjects", "call" + trackInfoModel.getByWhom());

                                       if(PhysicalExamTrackInfoDataController.getInstance().insertTrackData(trackInfoModel)){
                                           PhysicalExamTrackInfoDataController.getInstance().fetchTrackData(PhysicalExamDataController.getInstance().currentPhysicalExamsData);
                                       }
                                   }
                               }
                           }else {
                               PhysicalExamsDataModel objPhysical = new PhysicalExamsDataModel();
                               objPhysical.setPatientId(serverObject1.getPatientID());
                               objPhysical.setHospital_reg_number(serverObject1.getHospital_reg_num());
                               objPhysical.setMedicalPersonId(serverObject1.getMedical_personnel_id());
                               objPhysical.setMedicalRecordId(serverObject1.getMedical_record_id());
                               objPhysical.setPhysicalExamId(serverObject1.getPhysical_exam_id());
                               objPhysical.setOther(serverObject1.getOther());
                               Log.e("dadafs",""+serverObject1.getOther());
                               objPhysical.setPhysicianComments(serverObject1.getPhysicianCommentsOrRecomdations());
                               objPhysical.setAttachment(serverObject1.getAttachment());
                               objPhysical.setPatientlProfileModel(PatientProfileDataController.getInstance().currentPatientlProfile);

                               if (PhysicalExamDataController.getInstance().insertPhysicalExamData(objPhysical)) {
                                   Log.e("fetchPhysicalExam", "PhysicalExam fectched successfully" + PhysicalExamDataController.getInstance().fetchPhysicalExamData(PatientProfileDataController.getInstance().currentPatientlProfile));

                               }

                               ArrayList<TrackingServerObject> trackingServerObjects = serverObject1.getTrackingServerObjects();
                               Log.e("trackingServerObjects", "call" + trackingServerObjects.size());

                               if (trackingServerObjects.size() > 0) {

                                   for (int index = 0; index < trackingServerObjects.size(); index++) {
                                       TrackingServerObject serverObject = trackingServerObjects.get(index);
                                       PhysicalTrackInfoModel trackInfoModel = new PhysicalTrackInfoModel();
                                       trackInfoModel.setPhysicalExamsDataModel(objPhysical);
                                       trackInfoModel.setPatientId(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
                                       trackInfoModel.setPhysicalExamId(serverObject1.getPhysical_exam_id());
                                       trackInfoModel.setByWhom(serverObject.getByWhom());
                                       trackInfoModel.setByWhomId(serverObject.getByWhomID());
                                       Long l = Long.parseLong(serverObject.getDate()) / 1000;
                                       trackInfoModel.setDate(String.valueOf(l));
                                       Log.e("attachtrackingServ", "call" + trackInfoModel.getByWhom());
                                       if (PhysicalExamTrackInfoDataController.getInstance().insertTrackData(trackInfoModel)) {
                                           PhysicalExamTrackInfoDataController.getInstance().fetchTrackData(PhysicalExamDataController.getInstance().currentPhysicalExamsData);
                                       }
                                   }

                               }
                           }
                       }
                       EventBus.getDefault().post(new MessageEvent("fetchPhysicalData"));
                   } else if (statusCode.equals("0")) {
                       Toast.makeText(context,messageStatus,Toast.LENGTH_SHORT).show();
                       EventBus.getDefault().post(new MessageEvent("noPhysicalData"));

                   } else {
                       Toast.makeText(context,messageStatus,Toast.LENGTH_SHORT).show();
                       EventBus.getDefault().post(new MessageEvent("noPhysicalData"));

                   }
               }
           }
           @Override
           public void onFailure(Call<PhysicalExamServerObject> call, Throwable t) {
           }
       });
   }
    public void addPhysicalExamServerApi(final String other, final String comment, final boolean isUpdate , final BMIModel bmiModel,
                                         final ArrayList<PhysicalRecordServerObject> physicalExamList){

        Log.e("isForUpdate","call"+isUpdate+bmiModel.getPhysicalExamId());
        Log.e("checkckk",""+other+","+comment);
        for (PhysicalRecordServerObject p:
             physicalExamList) {
            Log.e("checkckk", p.getCategory()+" and "+p.getResult() );
        }

        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;

        BodyIndexServerObject bodyIndexServerObject=new BodyIndexServerObject();
        bodyIndexServerObject.setBloodPressure(bmiModel.getBloodPressure());
        bodyIndexServerObject.setHeight(bmiModel.getHeight());
        bodyIndexServerObject.setWeight(bmiModel.getWeight());
        bodyIndexServerObject.setBmi(bmiModel.getBmi());
        bodyIndexServerObject.setWaistline(bmiModel.getWaistLine());

        final PhysicalExamServerObject serverObject=new PhysicalExamServerObject();
        serverObject.setBodyIndexArrayList(bodyIndexServerObject);
        serverObject.setPhysicalExaminations(physicalExamList);
        serverObject.setHospital_reg_num(currentMedical.getHospital_reg_number());
        serverObject.setMedical_record_id(PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
       // serverObject.setMedical_personnel_id(currentMedical.getMedical_person_id());
        serverObject.setByWhom("medical personnel");
        serverObject.setByWhomID(currentMedical.getMedical_person_id());

        if(isUpdate){
            serverObject.setPhysical_exam_id(bmiModel.getPhysicalExamId());
        }
        serverObject.setMedical_record_id(PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
        serverObject.setPatientID(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
        serverObject.setOther(other);
        Log.e("other","ooo"+ other);
        serverObject.setPhysicianCommentsOrRecomdations(comment);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerApi serverApi = retrofit.create(ServerApi.class);

        Call<PhysicalExamServerObject> callable;
        if(isUpdate){
            Log.e("abrakadabraa","aaaaadd");
            callable=serverApi.updatePhysicalExamHistory(currentMedical.getAccessToken(),serverObject);
        }else {
            callable=serverApi.addPhysicalExamHistory(currentMedical.getAccessToken(),serverObject);
        }

        callable.enqueue(new Callback<PhysicalExamServerObject>() {
            @Override
            public void onResponse(Call<PhysicalExamServerObject> call, Response<PhysicalExamServerObject> response) {
                Log.e("ponse",""+response);
                if (response.body() != null) {
                    String statusCode = response.body().getResponse();
                    String messageStatus = response.body().getMessage();
                    Log.e("codefor3", "call" + statusCode+messageStatus);
                    if (statusCode.equals("3")) {
                        if(!isUpdate) {
                            String physical_exam_id = response.body().getPhysical_exam_id();
                            Log.e("physical_exam_id", "call" + physical_exam_id);

                            PhysicalExamsDataModel objPhysical =new PhysicalExamsDataModel();
                            objPhysical.setPatientId(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
                            objPhysical.setHospital_reg_number(MedicalProfileDataController.getInstance().currentMedicalProfile.getHospital_reg_number());
                            objPhysical.setMedicalPersonId(MedicalProfileDataController.getInstance().currentMedicalProfile.getMedical_person_id());
                            objPhysical.setMedicalRecordId(PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
                            objPhysical.setPhysicalExamId(physical_exam_id);
                            objPhysical.setOther(other);
                            Log.e("othhhh","aa"+objPhysical.getOther());
                            objPhysical.setAttachment(null);
                            objPhysical.setPhysicianComments(comment);
                            objPhysical.setPatientlProfileModel(PatientProfileDataController.getInstance().currentPatientlProfile);

                            if(PhysicalExamDataController.getInstance().insertPhysicalExamData(objPhysical)){
                                Log.e("fetchexamdata", "PhysicalExam fectched successfully" + PhysicalExamDataController.getInstance().fetchPhysicalExamData(PatientProfileDataController.getInstance().currentPatientlProfile));

                            }

                            bmiModel.setPhysicalExamId(physical_exam_id);
                            bmiModel.setPatientId(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
                            bmiModel.setPhysicalExamsDataModel(objPhysical);
                            bmiModel.setMedicalRecordId(PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
                            if (BmiDataController.getInstance().insertBmiData(bmiModel)) {
                                //  Log.e("fetchbmidata", "call" + BmiDataController.getInstance().fetchBmiData());
                            }

                            for (int i = 0; i < physicalExamList.size(); i++) {
                                PhysicalRecordServerObject serverObject1 = physicalExamList.get(i);

                                PhysicalCategoriesRecords physicalExamModel = new PhysicalCategoriesRecords();

                                physicalExamModel.setPhysicalExamId(physical_exam_id);
                                physicalExamModel.setCategory(serverObject1.getCategory());
                                physicalExamModel.setDescription(serverObject1.getDescription());
                                physicalExamModel.setResult(serverObject1.getResult());
                                physicalExamModel.setPatientId(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
                                physicalExamModel.setPhysicianComment("some thing");

                                physicalExamModel.setPhysicalExamsDataModel(objPhysical);
                                physicalExamModel.setMedicalRecordId(PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());

                                if (PhysicalCategoriesDataController.getInstance().insertPhysicalExamData(physicalExamModel)) {
                                    //  Log.e("fetchbmidata", "call" + PhysicalCategoriesDataController.getInstance().fetchPhysicalExamData());
                                }
                            }

                            PhysicalTrackInfoModel trackInfoModel=new PhysicalTrackInfoModel();
                            trackInfoModel.setPhysicalExamsDataModel(objPhysical);
                            trackInfoModel.setPatientId(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
                            trackInfoModel.setPhysicalExamId(objPhysical.getPhysicalExamId());
                            trackInfoModel.setByWhom(PatientProfileDataController.getInstance().currentPatientlProfile.getFirstName()+" "+ PatientProfileDataController.getInstance().currentPatientlProfile.getLastName());
                            trackInfoModel.setByWhomId(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
                            trackInfoModel.setDate(String.valueOf(System.currentTimeMillis() / 1000L));
                            Log.e("trackingServerObjects", "call" + trackInfoModel.getByWhomId());

                            if(PhysicalExamTrackInfoDataController.getInstance().insertTrackData(trackInfoModel)){
                                PhysicalExamTrackInfoDataController.getInstance().fetchTrackData(PhysicalExamDataController.getInstance().currentPhysicalExamsData);
                            }
                            EventBus.getDefault().post(new MessageEvent("addPhysicalData"));
                        }else {
                            Log.e("eeeee","db"+PhysicalExamDataController.getInstance()
                                    .currentPhysicalExamsData.getOther());
                            bmiModel.setPatientId(PhysicalExamDataController.getInstance().currentPhysicalExamsData.getPatientId());
                            bmiModel.setPhysicalExamsDataModel(PhysicalExamDataController.getInstance().currentPhysicalExamsData);
                            bmiModel.setMedicalRecordId(PhysicalExamDataController.getInstance().currentPhysicalExamsData.getMedicalRecordId());
                            if(BmiDataController.getInstance().updateBmiData(bmiModel))
                            {
                                Log.e("updatebmi","call"+bmiModel.getPhysicalExamId());
                                Log.e("update bmi","call"+ BmiDataController.getInstance().updateBmiData(bmiModel));
                            }

                            for (int i = 0; i < physicalExamList.size(); i++) {
                                PhysicalRecordServerObject serverObject1 = physicalExamList.get(i);
                                PhysicalCategoriesRecords physicalExamModel = new PhysicalCategoriesRecords();
                                physicalExamModel.setPhysicalExamId(bmiModel.getPhysicalExamId());
                                physicalExamModel.setCategory(serverObject1.getCategory());
                                physicalExamModel.setDescription(serverObject1.getDescription());
                                physicalExamModel.setResult(serverObject1.getResult());
                                physicalExamModel.setPatientId(PhysicalExamDataController.getInstance().currentPhysicalExamsData
                                        .getPatientId());
                                physicalExamModel.setPhysicianComment("some thing");
                                physicalExamModel.setPhysicalExamsDataModel(PhysicalExamDataController.getInstance()
                                        .currentPhysicalExamsData);
                                physicalExamModel.setMedicalRecordId(PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());

                                if (PhysicalCategoriesDataController.getInstance().updatePhysicalExamData(physicalExamModel)) {
                                    Log.e("ddddd","db"+PhysicalExamDataController.getInstance()
                                            .currentPhysicalExamsData.getOther());
                                    Log.e("update exam data", "call" + PhysicalCategoriesDataController.getInstance()
                                            .updatePhysicalExamData(physicalExamModel));
                                }
                            }
                            ArrayList<PhysicalTrackInfoModel> trackInfoModels= PhysicalExamTrackInfoDataController.getInstance().fetchPhysicalExamBasedOnPhysicalExamId(PhysicalExamDataController.getInstance().currentPhysicalExamsData);
                            if(trackInfoModels.size()>0){
                                    PhysicalTrackInfoModel trackInfoModel=trackInfoModels.get(trackInfoModels.size()-1);
                                    trackInfoModel.setDate(String.valueOf(System.currentTimeMillis() / 1000L));
                                    if(PhysicalExamTrackInfoDataController.getInstance().updateTrackData(trackInfoModel)){

                                    }
                                }

                               EventBus.getDefault().post(new MessageEvent("updatePhysicalData"));
                        }

                    } else if (statusCode.equals("0")) {
                        Toast.makeText(context,messageStatus,Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context,messageStatus,Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<PhysicalExamServerObject> call, Throwable t) {

                Toast.makeText(context, "Please check your network connection", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void deletePhysicalExamServerApi(){

        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        final PhysicalExamServerObject serverObject=new PhysicalExamServerObject();
      //  serverObject.setMedical_personnel_id(PhysicalExamDataController.getInstance().currentPhysicalExamsData.getMedicalPersonId());
        serverObject.setPatientID(PhysicalExamDataController.getInstance().currentPhysicalExamsData.getPatientId());
        serverObject.setMedical_record_id(PhysicalExamDataController.getInstance().currentPhysicalExamsData.getMedicalRecordId());
        serverObject.setPhysical_exam_id(PhysicalExamDataController.getInstance().currentPhysicalExamsData.getPhysicalExamId());
        serverObject.setHospital_reg_num(PhysicalExamDataController.getInstance().currentPhysicalExamsData.getHospital_reg_number());
        serverObject.setByWhom("medical personnel");
        serverObject.setByWhomID(currentMedical.getMedical_person_id());
        Log.e("alldata","call"+serverObject.getHospital_reg_num()+"zzz"+serverObject.getPhysical_exam_id()+"zzzz"+
        serverObject.getMedical_personnel_id()+"zzz"+serverObject.getMedical_record_id()+"zzzz"+serverObject.getPatientID());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerApi serverApi = retrofit.create(ServerApi.class);
        Call<PhysicalExamServerObject> callable=serverApi.deletePhysicalExamHistory(currentMedical.getAccessToken(),serverObject);

        callable.enqueue(new Callback<PhysicalExamServerObject>() {
            @Override
            public void onResponse(Call<PhysicalExamServerObject> call, Response<PhysicalExamServerObject> response) {
                Log.e("delete","response"+response);
                if (response.body() != null) {
                    String statusCode = response.body().getResponse();
                    String messageStatus = response.body().getMessage();
                    Log.e("codefor3", "call" + statusCode);
                    if (statusCode.equals("3")) {
                        BmiDataController.getInstance().deleteBmiModelData(PhysicalExamDataController.getInstance().currentPhysicalExamsData, PhysicalExamDataController.getInstance().currentPhysicalExamsData.getPhysicalExamId());
                        PhysicalExamTrackInfoDataController.getInstance().deletePhisicalModelData(PhysicalExamDataController.getInstance().currentPhysicalExamsData, PhysicalExamDataController.getInstance().currentPhysicalExamsData.getPhysicalExamId());
                        PhysicalCategoriesDataController.getInstance().deletePhysicalExamModelData(PhysicalExamDataController.getInstance().currentPhysicalExamsData, PhysicalExamDataController.getInstance().currentPhysicalExamsData.getPhysicalExamId());
                        PhysicalExamDataController.getInstance().deleteMemberData(PhysicalExamDataController.getInstance().currentPhysicalExamsData);
                        EventBus.getDefault().post(new MessageEvent("deletePhysicalData"));
                    } else if (statusCode.equals("0")) {
                        Toast.makeText(context,messageStatus,Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context,messageStatus,Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<PhysicalExamServerObject> call, Throwable t) {
            }
        });
    }

    public void apiCallForAttchmet(final File file) {
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        PhysicalExamServerObject serverObject=new PhysicalExamServerObject();
        serverObject.setHospital_reg_num(currentMedical.getHospital_reg_number());
        serverObject.setMedical_personnel_id(currentMedical.getMedical_person_id());
        serverObject.setMedical_record_id(PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
        serverObject.setPatientID(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());


        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ServerApi s = retrofit.create(ServerApi.class);

        MultipartBody.Part physicalExamRecord;

        RequestBody image = RequestBody.create(MediaType.parse("image/*"), file);
        physicalExamRecord = MultipartBody.Part.createFormData("physicalExamRecord", file.getName(), image);
        RequestBody byWhom = RequestBody.create(MediaType.parse("text/plain"),"medical personnel");
        RequestBody byWhomID = RequestBody.create(MediaType.parse("text/plain"),serverObject.getMedical_personnel_id());
       // RequestBody medicalPersonId = RequestBody.create(MediaType.parse("text/plain"),serverObject.getMedical_personnel_id());
        RequestBody medicalRecordId = RequestBody.create(MediaType.parse("text/plain"),serverObject.getMedical_record_id());
        RequestBody hospitalRegNumber = RequestBody.create(MediaType.parse("text/plain"),serverObject.getHospital_reg_num());
        RequestBody patientId = RequestBody.create(MediaType.parse("text/plain"),serverObject.getPatientID());
        Log.e("medicalPers",""+serverObject.getMedical_personnel_id());
        Log.e("medicalR",""+serverObject.getMedical_record_id());
        Log.e("hospitalRegNumber",""+serverObject.getHospital_reg_num());
        Log.e("patientId",""+serverObject.getPatientID());
        Log.e("token",""+currentMedical.getAccessToken());

        Call<PhysicalExamServerObject> call = s.addAttachment(currentMedical.getAccessToken(),byWhom,hospitalRegNumber,patientId,
                medicalRecordId,physicalExamRecord,byWhomID);
        call.enqueue(new Callback<PhysicalExamServerObject>() {
            @Override
            public void onResponse(Call<PhysicalExamServerObject> call, Response<PhysicalExamServerObject> response) {
                Log.e("addd","file"+response);
                String message = response.body().getMessage();
                String respons = response.body().getResponse();
                Log.e("checkMessage", "" + message);
                if (respons.equals("3")) {
                        String physical_exam_id = response.body().getPhysical_exam_id();
                        Log.e("physical_exam_id", "call" + response.body().getAttachment());
                        PhysicalExamsDataModel objPhysical =new PhysicalExamsDataModel();
                        objPhysical.setPatientId(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
                        objPhysical.setHospital_reg_number(MedicalProfileDataController.getInstance().currentMedicalProfile.getHospital_reg_number());
                        objPhysical.setMedicalPersonId(MedicalProfileDataController.getInstance().currentMedicalProfile.getMedical_person_id());
                        objPhysical.setMedicalRecordId(PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
                        objPhysical.setPhysicalExamId(physical_exam_id);
                        objPhysical.setOther("some thing");
                        objPhysical.setPhysicianComments("Some thing");
                        objPhysical.setAttachment(response.body().getAttachment());
                        objPhysical.setPatientlProfileModel(PatientProfileDataController.getInstance().currentPatientlProfile);

                        if(PhysicalExamDataController.getInstance().insertPhysicalExamData(objPhysical)){
                            Log.e("fetchexamdata", "PhysicalExam fectched successfully" + PhysicalExamDataController.getInstance().fetchPhysicalExamData(PatientProfileDataController.getInstance().currentPatientlProfile));
                        }

                        PhysicalTrackInfoModel trackInfoModel=new PhysicalTrackInfoModel();
                        trackInfoModel.setPhysicalExamsDataModel(objPhysical);
                        trackInfoModel.setPatientId(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
                        trackInfoModel.setPhysicalExamId(objPhysical.getPhysicalExamId());
                        trackInfoModel.setByWhom(PatientProfileDataController.getInstance().currentPatientlProfile.getFirstName()+" "+ PatientProfileDataController.getInstance().currentPatientlProfile.getLastName());
                        trackInfoModel.setByWhomId(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
                        trackInfoModel.setDate(String.valueOf(System.currentTimeMillis() / 1000L));
                        Log.e("trackingServerObjects", "call" + trackInfoModel.getByWhomId());

                        if(PhysicalExamTrackInfoDataController.getInstance().insertTrackData(trackInfoModel)){
                            PhysicalExamTrackInfoDataController.getInstance().fetchTrackData(PhysicalExamDataController.getInstance().currentPhysicalExamsData);
                        }
                        EventBus.getDefault().post(new MessageEvent("addPhysicalData"));
                }
            }

            @Override
            public void onFailure(Call<PhysicalExamServerObject> call, Throwable t) {

            }
        });
    }
    public static class MessageEvent {
        public final String message;
        public MessageEvent(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
