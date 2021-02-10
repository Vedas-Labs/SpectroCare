package com.vedas.spectrocare;

//import com.vedas.spectrocare.PatientServerObjects.FeedbackObject;
import com.vedas.spectrocare.PatientAppointmentModule.AppointmentDataModel;
import com.vedas.spectrocare.PatientDocResponseModel.DepartmentResponseModel;
import com.vedas.spectrocare.PatientServerApiModel.InvoiceModel;
import com.vedas.spectrocare.PatientServerApiModel.PatientPhysicalRecordModel;
import com.vedas.spectrocare.PatientServerApiModel.PatientSurgicalObject;
import com.vedas.spectrocare.ServerApiModel.AppointmentServerObjects;
import com.vedas.spectrocare.ServerApiModel.ChangePasswordInstance;
import com.vedas.spectrocare.ServerApiModel.MedicalRecordResponse;
import com.vedas.spectrocare.ServerApiModel.MedicationAttachServerObject;
import com.vedas.spectrocare.ServerApiModel.MedicationManullayServerObject;
import com.vedas.spectrocare.ServerApiModel.PhysicalExamServerObject;
import com.vedas.spectrocare.ServerApiModel.RetrofitInstance;
import com.vedas.spectrocare.ServerApiModel.ScreeningServerObject;
import com.vedas.spectrocare.ServerApiModel.SurgicalServerObject;
import com.vedas.spectrocare.activities.MyPatientActivity;
import com.vedas.spectrocare.model.GetMedicalRecordResponse;
import com.vedas.spectrocare.model.GetPatientsResponseModel;
import com.vedas.spectrocare.ServerApiModel.PatientDetailsModel;
import com.vedas.spectrocare.ServerApiModel.PatientsDeleteModel;
import com.vedas.spectrocare.ServerApiModel.RecordDeleteModel;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface ServerApi {
  String home_url = "http://34.231.177.197:3000/api/";
  String img_home_url = "http://34.231.177.197:3000";
  String appointment_home_url = "http://34.231.177.197:3000/api/timeslots";
    /*String home_url = "http://3.92.226.247:3000/api/";
    String img_home_url = "http://3.92.226.247:3000";*/
  // String url="http://52.1.227.73:8096/spectrocare/";//"http://52.1.227.73:8096/spectrocare/";

  //@Headers("Content-Type: application/json")
  @POST("sdkjsonfilesforengg/getallstripes")
  Call<JsonObject> loadjsons(@Body JsonObject body);

  //http://52.1.227.73:8096/spectrocare/sdkjsonfiles/fileread
  @POST("sdkjsonfiles/fileread")
  Call<JsonObject> readjsons(@Body JsonObject body);


  @Headers("Content-Type: application/json")
  @POST("medicalpersonnel/login")
  Call<ResponseBody> login(@Body RetrofitInstance body);

  @Headers("Content-Type: application/json")
  @POST("medicalpersonnel/changepassword")
  Call<ChangePasswordInstance> changePassword(@Body ChangePasswordInstance body);

  @POST(" patient/generalinfo/fetch ")
  Call<ResponseBody> fetchPatientDetails(@Header("x-access-token") String AccessToken,
                                         @Body PatientDetailsModel body);

  @Multipart
  @POST("patient/generalinfo")
  Call<PatientDetailsModel> addPatient(@Header("x-access-token") String AccessToken,
                                       @Part("firstName") RequestBody firstName,
                                       @Part("lastName") RequestBody lastName,
                                       @Part("gender") RequestBody gender,
                                       @Part("age") RequestBody age,
                                       @Part("phoneNumber") RequestBody phoneNumber,
                                       @Part("address") RequestBody address,
                                       @Part("city") RequestBody city,
                                       @Part("state") RequestBody state,
                                       @Part("country") RequestBody country,
                                       @Part("postalCode") RequestBody postalCode,
                                       @Part("medical_personnel_id") RequestBody medical_personnel_id,
                                       @Part("hospital_reg_num") RequestBody hospital_reg_num,
                                       @Part("latitude") RequestBody latitude,
                                       @Part("longitude") RequestBody longitude,
                                       @Part MultipartBody.Part profilePic,
                                       @Part("emailID") RequestBody emailID,
                                       @Part("phoneNumberCountryCode") RequestBody phoneNumberCountryCode,
                                       @Part("dob") RequestBody dob
  );

  @Multipart
  @PUT("patient/generalinfo")
  Call<PatientDetailsModel> PatientUpdate(@Header("x-access-token") String AccessToken,
                                          @Part("firstName") RequestBody firstName,
                                          @Part("lastName") RequestBody lastName,
                                          @Part("gender") RequestBody gender,
                                          @Part("age") RequestBody age,
                                          @Part("phoneNumber") RequestBody phoneNumber,
                                          @Part("address") RequestBody address,
                                          @Part("city") RequestBody city,
                                          @Part("state") RequestBody state,
                                          @Part("country") RequestBody country,
                                          @Part("postalCode") RequestBody postalCode,
                                          @Part("medical_personnel_id") RequestBody medical_personnel_id,
                                          @Part("hospital_reg_num") RequestBody hospital_reg_num,
                                          @Part("latitude") RequestBody latitude,
                                          @Part("longitude") RequestBody longitude,
                                          @Part MultipartBody.Part profilePic,
                                          @Part("emailID") RequestBody emailID,
                                          @Part("patientID") RequestBody patientID,
                                          @Part("phoneNumberCountryCode") RequestBody phoneNumberCountryCode,
                                          @Part("dob") RequestBody dob
  );
  @Multipart
  @PUT("patient/generalinfo")
  Call<PatientDetailsModel> PatientProfileUpdate(@Header("x-access-token") String AccessToken,
                                                 @Part("firstName") RequestBody firstName,
                                                 @Part("lastName") RequestBody lastName,
                                                 @Part("gender") RequestBody gender,
                                                 @Part("age") RequestBody age,
                                                 @Part("phoneNumberCountryCode") RequestBody phoneNumberCountryCode,
                                                 @Part("phoneNumber") RequestBody phoneNumber,
                                                 @Part("address") RequestBody address,
                                                 @Part("state") RequestBody state,
                                                 @Part("country") RequestBody country,
                                                 @Part("postalCode") RequestBody postalCode,
                                                 @Part("medical_personnel_id") RequestBody medical_personnel_id,
                                                 @Part("hospital_reg_num") RequestBody hospital_reg_num,
                                                 @Part("latitude") RequestBody latitude,
                                                 @Part("longitude") RequestBody longitude,
                                                 @Part MultipartBody.Part profilePic,
                                                 @Part("emailID") RequestBody emailID,
                                                 @Part("patientID") RequestBody patientID,
                                                 @Part("city") RequestBody city,
                                                 @Part("dob") RequestBody dob
  );
  @Multipart
  @POST("medicalpersonnel/update")
  Call<RetrofitInstance> update(@Header("x-access-token") String AccessToken,
                                @Part MultipartBody.Part profilePic,
                                @Part("userID") RequestBody userID,
                                @Part("firstName") RequestBody firstName,
                                @Part("lastName") RequestBody lastName,
                                @Part("phoneNumber") RequestBody phoneNumber,
                                @Part("phoneNumberCountryCode") RequestBody phoneNumberCountryCode,
                                //   @Part("phoneNumberExtension") RequestBody phoneNumberExtension,
                                @Part("qualification") RequestBody qualification,
                                @Part("specialization") RequestBody specialization,
                                @Part("department") RequestBody department,
                                @Part("userType") RequestBody userType,
                                @Part("preferLanguage") RequestBody preferLanguage,
                                @Part("medical_personnel_id") RequestBody medical_personnel_id,
                                @Part("gender") RequestBody gender,
                                @Part("age") RequestBody age,
                                @Part("dob") RequestBody dob,
                                @Part("id_by_govt") RequestBody id_by_govt
  );


  @Multipart
  @POST("medicalpersonnel/register")
  Call<RetrofitInstance> register(@Part MultipartBody.Part profilePic,
                                  @Part("userID") RequestBody userID,
                                  @Part("password") RequestBody password,
                                  @Part("firstName") RequestBody firstName,
                                  @Part("lastName") RequestBody lastName,
                                  @Part("phoneNumber") RequestBody phoneNumber,
                                  @Part("phoneNumberCountryCode") RequestBody phoneNumberCountryCode,
                                  @Part("emailID") RequestBody emailID,
                                    /* @Part("qualification") RequestBody qualification,
                                     @Part("specialization") RequestBody specialization,*/
                                  @Part("department") RequestBody department,
                                  @Part("userType") RequestBody userType,
                                  //   @Part("id_by_govt") RequestBody id_by_govt,
                                  @Part("preferLanguage") RequestBody preferLanguage,
                                  @Part("hospital_reg_num") RequestBody hospital_reg_num,
                                  @Part("gender") RequestBody gender,
                                  @Part("age") RequestBody age,
                                  @Part("emergencyPhoneNumber") RequestBody emergenctPhone,
                                  @Part("emergencyPhoneNumberCountryCode") RequestBody emergencyPhoneCountryCode,
                                  @Part("emergencyPhoneNumberExtension") RequestBody emergencyPhoneExtension,
                                  @Part("latitude") RequestBody latitude,
                                  @Part("longitude") RequestBody longitude
                                  // @Part("dob") RequestBody dob
  );



  @POST("medicalpersonnel/forgetpassword")
  Call<RetrofitInstance> forgot(@Body RetrofitInstance body);

  @FormUrlEncoded
  @HTTP(method = "DELETE", path = "patient/generalinfo", hasBody = true)
  Call<PatientsDeleteModel> delete(@Header("x-access-token") String accessToken,
                                   @FieldMap Map<String, String> patientDeleteJObject);

  @FormUrlEncoded
  @HTTP(method = "DELETE", path = "patient/medicalrecord", hasBody = true)
  Call<RecordDeleteModel> deleteRecord(@Header("x-access-token") String accessToken
          , @FieldMap Map<String, String> patientDeleteJObject);

  @Headers("Content-Type: application/json")
  @POST("medicalpersonnel/checkavailability")
  Call<RetrofitInstance> checkAvail(@Body RetrofitInstance body);

  @POST("patient/generalinfo/fetchall")
  Call<GetPatientsResponseModel> getPatientsList(@Header("x-access-token") String accessToken,
                                                 @Body MyPatientActivity.GetPatientsRequestModel patientJsonObject);


  //created by chandrika
  @POST("patient/physicalexamrecord")
  Call<PhysicalExamServerObject> addPhysicalExamHistory(@Header("x-access-token") String accessToken,
                                                        @Body PhysicalExamServerObject body);

  @POST("patient/physicalexamrecord/fetch")
  Call<PhysicalExamServerObject> fetchPhysicalExamHistory(@Header("x-access-token") String accessToken,
                                                          @Body PhysicalExamServerObject body);


  @POST("patient/physicalexamrecord/fetch")
  Call<PatientPhysicalRecordModel> patientPhysicalFetchRecord(@Header("x-access-token") String accessToken,
                                                              @Body PhysicalExamServerObject body);


  @PUT("patient/physicalexamrecord")
  Call<PhysicalExamServerObject> updatePhysicalExamHistory(@Header("x-access-token") String accessToken,
                                                           @Body PhysicalExamServerObject body);


  @HTTP(method = "DELETE", path = "patient/physicalexamrecord", hasBody = true)
  Call<PhysicalExamServerObject> deletePhysicalExamHistory(@Header("x-access-token") String accessToken,
                                                           @Body PhysicalExamServerObject body);

  @HTTP(method = "DELETE", path = "patient/physicalexamrecord/all", hasBody = true)
  Call<PhysicalExamServerObject> deleteAllPhysicalExamHistory(@Header("x-access-token") String accessToken,
                                                              @Body PhysicalExamServerObject body);


  /*   @Multipart
     @POST("patient/physicalexamrecord/attachment")
     Call<PhysicalExamServerObject> addAttachment(@Header("x-access-token") String AccessToken,
                                                  @Part("medical_personnel_id") RequestBody medical_personnel_id,
                                                  @Part("hospital_reg_num") RequestBody hospital_reg_num,
                                                  @Part("patientID") RequestBody patientID,
                                                  @Part("medical_record_id") RequestBody medical_record_id,
                                                  @Part MultipartBody.Part physicalExamRecord);
  */   @Multipart
  @POST("patient/physicalexamrecord/attachment")
  Call<PhysicalExamServerObject> addAttachment(@Header("x-access-token") String AccessToken,
                                               @Part("byWhom") RequestBody byWhom,
                                               //  @Part("medical_personnel_id") RequestBody medical_personnel_id,
                                               @Part("hospital_reg_num") RequestBody hospital_reg_num,
                                               @Part("patientID") RequestBody patientID,
                                               @Part("medical_record_id") RequestBody medical_record_id,
                                               @Part MultipartBody.Part physicalExamRecord,
                                               @Part("byWhomID") RequestBody byWhomID);

  /*  @POST("patient/familyhistory")
    Call<FamilyRecordServerObject> addFamilyHistory(
            @Header("x-access-token") String accessToken, @Body FamilyRecordServerObject body);

    @POST("patient/familyhistory/fetch")
    Call<FamilyRecordServerObject> fetchFamilyHistory(
            @Header("x-access-token") String accessToken, @Body FamilyRecordServerObject body);

    @HTTP(method = "DELETE", path = "patient/familyhistory", hasBody = true)
    Call<FamilyRecordServerObject> deleteFamilyHistory(@Header("x-access-token") String accessToken,
                                                       @Body FamilyRecordServerObject body);
*/

  //using api call interface
  @POST("patient/familyhistory/fetch")
  Call<JsonObject> fetchFamilyHistory1(@Header("x-access-token") String accessToken,
                                       @Body JsonObject body);

  @POST("patient/familyhistory")
  Call<JsonObject> addFamilyHistory1(
          @Header("x-access-token") String accessToken, @Body JsonObject body);


  @POST("patient/familyhistory/multiple")
  Call<JsonObject> addFamilyHistoryMultiple(
          @Header("x-access-token") String accessToken, @Body JsonObject body);

  @HTTP(method = "DELETE", path = "patient/familyhistory/all", hasBody = true)
  Call<JsonObject> deleteFamilyHistoryMultiple(@Header("x-access-token") String accessToken,
                                               @Body JsonObject body);

  @HTTP(method = "DELETE", path = "patient/familyhistory", hasBody = true)
  Call<JsonObject> deleteFamilyHistory1(@Header("x-access-token") String accessToken,
                                        @Body JsonObject body);

  @PUT("patient/familyhistory")
  Call<JsonObject> updateFamilyHistory(@Header("x-access-token") String accessToken,
                                       @Body JsonObject body);

  @POST("patient/allergies")
  Call<JsonObject> addAllergy(
          @Header("x-access-token") String accessToken, @Body JsonObject body);
  //add multiple allergy records
  @POST("patient/allergies/multiple")
  Call<JsonObject> addMultipleAllergyRecords(
          @Header("x-access-token") String accessToken, @Body JsonObject body);

  @POST("patient/allergies/fetch")
  Call<JsonObject> fetchAllergy(@Header("x-access-token") String accessToken,
                                @Body JsonObject body);

  @HTTP(method = "DELETE", path = "patient/allergies", hasBody = true)
  Call<JsonObject> deleteAllergyHistory(@Header("x-access-token") String accessToken,
                                        @Body JsonObject body);

  @PUT("patient/allergies")
  Call<JsonObject> updateAllergyHistory(@Header("x-access-token") String accessToken,
                                        @Body JsonObject body);

  @POST("patient/immunization/fetch")
  Call<JsonObject> fetchVaccine(@Header("x-access-token") String accessToken,
                                @Body JsonObject body);

  @POST("patient/immunization")
  Call<JsonObject> addVaccine(@Header("x-access-token") String accessToken,
                              @Body JsonObject body);

  @HTTP(method = "DELETE", path = "patient/immunization", hasBody = true)
  Call<JsonObject> deleteVaccine(@Header("x-access-token") String accessToken,
                                 @Body JsonObject body);

  @PUT("patient/immunization")
  Call<JsonObject> updateVaccine(@Header("x-access-token") String accessToken,
                                 @Body JsonObject body);

  @POST("patient/medicalrecord/illness/fetchall")
  Call<JsonObject> fetchMedicalRecordIllness(@Header("x-access-token") String accessToken,
                                             @Body JsonObject body);

  @POST("patient/medicalrecord/illness")
  Call<JsonObject> addMedicalIllness(@Header("x-access-token") String accessToken,
                                     @Body JsonObject body);

  @HTTP(method = "DELETE", path = "patient/medicalrecord/illness", hasBody = true)
  Call<JsonObject> deleteMedicalIllness(@Header("x-access-token") String accessToken,
                                        @Body JsonObject body);

  @PUT("patient/medicalrecord/illness")
  Call<JsonObject> updateMedicalIllness(@Header("x-access-token") String accessToken,
                                        @Body JsonObject body);

  /*
      @Multipart
      @POST("patient/medicalrecord/screeningrecord")
      Call<ScreeningServerObject> addScreeninRecord(@Header("x-access-token") String AccessToken,
                                                    @Part("patientID") RequestBody patientid,
                                                    @Part("recordMoreDetails") RequestBody moreDetails,
                                                    @Part("recordName") RequestBody recorName,
                                                    @Part("byWhomName") RequestBody whomname,
                                                    @Part("byWhomType") RequestBody whomtype,
                                                    @Part("byWhomID") RequestBody whomid,
                                                    @Part("medical_record_id") RequestBody medical_record_id,
                                                    @Part MultipartBody.Part screeningRecord);
  */
  @Multipart
  @POST("patient/medicalrecord/screeningrecord")
  Call<ScreeningServerObject> addScreeninRecord(@Header("x-access-token") String AccessToken,
                                                @Part("patientID") RequestBody patientid,
                                                @Part MultipartBody.Part screeningRecord,
                                                @Part("recordMoreDetails") RequestBody moreDetails,
                                                @Part("recordName") RequestBody recorName,
                                                @Part("byWhom") RequestBody whomtype,
                                                @Part("byWhomID") RequestBody whomid,
                                                @Part("medical_record_id") RequestBody medical_record_id,
                                                @Part("hospital_reg_num") RequestBody hospital_reg_num
  );

  @POST("patient/medicalrecord/screeningrecord/fetchall")
  Call<JsonObject> fetchScreenRecord(@Header("x-access-token") String accessToken,
                                     @Body JsonObject body);

  @HTTP(method = "DELETE", path = "patient/medicalrecord/screeningrecord", hasBody = true)
  Call<JsonObject> deleteScreenRecord(@Header("x-access-token") String accessToken,
                                      @Body JsonObject body);
  @HTTP(method = "DELETE", path = "patient/medicalrecord/screeningrecord/all", hasBody = true)
  Call<JsonObject> deleteAllScreenRecord(@Header("x-access-token") String accessToken,
                                         @Body JsonObject body);


  //illness screening record API's
  /*  @Multipart
    @POST("patient/medicalrecord/screeningrecord")
    Call<ScreeningServerObject> addIllnessScreeninRecord(@Header("x-access-token") String AccessToken,
                                                         @Part("illnessID") RequestBody illnessID,
                                                         @Part("patientID") RequestBody patientid,
                                                         @Part("recordMoreDetails") RequestBody moreDetails,
                                                         @Part("recordName") RequestBody recorName,
                                                         @Part("byWhomName") RequestBody whomname,
                                                         @Part("byWhomType") RequestBody whomtype,
                                                         @Part("byWhomID") RequestBody whomid,
                                                         @Part("medical_record_id") RequestBody medical_record_id,
                                                         @Part MultipartBody.Part screeningRecord);
  */  @Multipart
  @POST("patient/medicalrecord/screeningrecord")
  Call<ScreeningServerObject> addIllnessScreeninRecord(@Header("x-access-token") String AccessToken,
                                                       @Part("patientID") RequestBody patientid,
                                                       @Part MultipartBody.Part screeningRecord,
                                                       @Part("recordMoreDetails") RequestBody moreDetails,
                                                       @Part("recordName") RequestBody recorName,
                                                       @Part("byWhom") RequestBody whomtype,
                                                       @Part("byWhomID") RequestBody whomid,
                                                       @Part("medical_record_id") RequestBody medical_record_id,
                                                       @Part("hospital_reg_num") RequestBody hospital_reg_num,
                                                       @Part("illnessID") RequestBody illnessID
  );

  @POST("patient/medicalrecord/screeningrecord/fetchall")
  Call<JsonObject> fetchMedicalScreenRecord(@Header("x-access-token") String accessToken,
                                            @Body JsonObject body);

  @HTTP(method = "DELETE", path = "patient/medicalrecord/screeningrecord", hasBody = true)
  Call<JsonObject> deleteMedicalScreenRecord(@Header("x-access-token") String accessToken,
                                             @Body JsonObject body);


  //Surgical Record API's
  @Multipart
  @POST("patient/medicalrecord/illness/surgicalrecord")
  Call<SurgicalServerObject> addIllnessSurgicalRecord(@Header("x-access-token") String AccessToken,
                                                      @Part("illnessID") RequestBody illnessID,
                                                      @Part MultipartBody.Part surgicalRecord,
                                                      @Part("moreDetails") RequestBody moreDetails,
                                                      @Part("doctorMedicalPersonnelID") RequestBody doctorMedicalpersondId,
                                                      @Part("doctorName") RequestBody doctorName,
                                                      @Part("surgeryProcedure") RequestBody surgeryProcedure,
                                                      @Part("surgeryDate") RequestBody surgeryDate,
                                                      @Part("surgeryInformation") RequestBody surgeryInformation,
                                                      @Part("byWhomID") RequestBody whomid,
                                                      @Part("medical_record_id") RequestBody medical_record_id,
                                                      @Part("byWhom") RequestBody whomtype,
                                                      @Part("hospital_reg_num") RequestBody hospital_reg_num,
                                                      @Part("patientID") RequestBody patientid


  );

/*
    @Multipart
    @POST("patient/medicalrecord/illness/surgicalrecord")
    Call<SurgicalServerObject> addIllnessSurgicalRecord(@Header("x-access-token") String AccessToken,
                                                        @Part("illnessID") RequestBody illnessID,
                                                        @Part MultipartBody.Part surgicalRecord,
                                                        @Part("moreDetails") RequestBody moreDetails,
                                                        @Part("doctorMedicalPersonnelID") RequestBody doctorMedicalpersondId,
                                                        @Part("doctorName") RequestBody doctorName,
                                                        @Part("surgeryProcedure") RequestBody surgeryProcedure,
                                                        @Part("surgeryDate") RequestBody surgeryDate,
                                                        @Part("surgeryInformation") RequestBody surgeryInformation
    );
*/

  @POST("patient/medicalrecord/illness/surgicalrecord/fetchall")
  Call<JsonObject> fetchSurgicalRecord(@Header("x-access-token") String accessToken,
                                       @Body JsonObject body);


  @HTTP(method = "DELETE", path = "patient/medicalrecord/illness/surgicalrecord", hasBody = true)
  Call<JsonObject> deleteSurgicalRecord(@Header("x-access-token") String accessToken, @Body JsonObject body);


  //Medication server api's
  @POST("patient/medicalrecord/illness/medication/manually")
  Call<JsonObject> addMedicationRecord(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @POST("patient/medicalrecord/illness/medication/manually")
  Call<JsonObject> updateMedicationRecord(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @POST("patient/medicalrecord/illness/medication/fetchall")
  Call<JsonObject> fetchMedicinesRecord(@Header("x-access-token") String accessToken, @Body JsonObject body);

  //newely added
  @POST("patient/medicalrecord/illness/medication/fetch_all_by_patientID")
  Call<JsonObject> fetchMedicinesByPatientID(@Header("x-access-token") String accessToken, @Body JsonObject body);


  @HTTP(method = "DELETE", path = "patient/medicalrecord/illness/medication", hasBody = true)
  Call<JsonObject> deleteMEdicationRecord(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @Multipart
  @POST("patient/medicalrecord/illness/medication/attachment")
  Call<MedicationAttachServerObject> addMedicationFirstAttachmentRecord(@Header("x-access-token") String AccessToken,
                                                                        @Part("illnessID") RequestBody illnessID,
                                                                        @Part MultipartBody.Part prescription1,
                                                                        // @Part MultipartBody.Part prescription2,
                                                                        @Part("prescription1MoreDetails") RequestBody prescription1MoreDetails,
                                                                        // @Part("prescription2MoreDetails") RequestBody prescription2MoreDetails,
                                                                        @Part("doctorMedicalPersonnelID") RequestBody doctorMedicalpersondId,
                                                                        @Part("doctorName") RequestBody doctorName,
                                                                        @Part("patientID") RequestBody patientID,
                                                                        @Part("medical_record_id") RequestBody medical_record_ID,
                                                                        @Part("byWhom") RequestBody byWhom,
                                                                        @Part("byWhomID") RequestBody byWhomID,
                                                                        @Part("hospital_reg_num") RequestBody hospital_reg_num);


  @Multipart
  @POST("patient/medicalrecord/illness/medication/attachment")
  Call<MedicationAttachServerObject> addMedicationSecondAttachmentRecord(@Header("x-access-token") String AccessToken,
                                                                         @Part("illnessID") RequestBody illnessID,
                                                                         @Part MultipartBody.Part prescription2,
                                                                         @Part("prescription2MoreDetails") RequestBody prescription2MoreDetails,
                                                                         @Part("doctorMedicalPersonnelID") RequestBody doctorMedicalpersondId,
                                                                         @Part("doctorName") RequestBody doctorName,
                                                                         @Part("patientID") RequestBody patientID,
                                                                         @Part("medical_record_id") RequestBody medical_record_ID,
                                                                         @Part("byWhom") RequestBody byWhom,
                                                                         @Part("byWhomID") RequestBody byWhomID,
                                                                         @Part("hospital_reg_num") RequestBody hospital_reg_num);


  @Multipart
  @POST("patient/medicalrecord/illness/medication/attachment")
  Call<MedicationAttachServerObject> addMedicationAllAttachmentRecord(@Header("x-access-token") String AccessToken,
                                                                      @Part("illnessID") RequestBody illnessID,
                                                                      @Part MultipartBody.Part prescription1,
                                                                      @Part MultipartBody.Part prescription2,
                                                                      @Part("prescription1MoreDetails") RequestBody prescription1MoreDetails,
                                                                      @Part("prescription2MoreDetails") RequestBody prescription2MoreDetails,
                                                                      @Part("doctorMedicalPersonnelID") RequestBody doctorMedicalpersondId,
                                                                      @Part("doctorName") RequestBody doctorName,
                                                                      @Part("patientID") RequestBody patientID,
                                                                      @Part("medical_record_id") RequestBody medical_record_ID,
                                                                      @Part("byWhom") RequestBody byWhom,
                                                                      @Part("byWhomID") RequestBody byWhomID,
                                                                      @Part("hospital_reg_num") RequestBody hospital_reg_num);

  @POST("appointments/bookappointment")
  Call<JsonObject> addAppointment(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @POST("appointments/cancelappointment")
  Call<JsonObject> cancelAppointment(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @POST("appointments/appointmentreschedule")
  Call<JsonObject> reschuduleAppointment(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @POST("appointments/allappointmentsforPatient")
  Call<JsonObject> fetcchAppointment(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @POST("medicalpersonnel/getdoctors")
  Call<JsonObject> fetchDoctorInfo(@Header("x-access-token") String accessToken, @Body JsonObject body);

  //Patients login module
  @POST("patient/generalinfo/login")
  Call<GetPatientsResponseModel> loginAsPatient(/*@Header("x-access-token") String accessToken,*/ @Body GetPatientsResponseModel body);

  @POST("patient/generalinfo/forgetPassword")
  Call<RetrofitInstance> patientForgotApi(/*@Header("x-access-token") String accessToken,*/@Body RetrofitInstance body);


  //////Patient Module

  @POST("patient/generalinfo/changepassword")
  Call<ChangePasswordInstance> patientChangePswApi(@Header("x-access-token") String accessToken, @Body ChangePasswordInstance body);

  @POST("feedback")
  Call<JsonObject> feedbackApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @PUT("patient/generalinfo/appSettings")
  Call<JsonObject> settingsApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  /* @POST("hospital/getDoctorsByDept")
   Call<DepartmentResponseModel> getDoctorsByDept(@Header("x-access-token") String accessToken, @Body JsonObject body);
*/
  //Patient module API's
  //patient family API's
  @POST("patient/familyhistory/multiple")
  Call<JsonObject> addPatientFamilyHistory(@Header("x-access-token") String accessToken, @Body JsonObject body);
  @POST("patient/familyhistory/fetch")
  Call<JsonObject> fetchPatientFamilyHistory(@Header("x-access-token")String accessToken,@Body JsonObject familyBody);

  @HTTP(method = "DELETE", path = "patient/familyhistory/all", hasBody = true)
  Call<JsonObject> deleteAllPatientFamilyHistory(@Header("x-access-token")String accessToken,@Body JsonObject body);

  //Patient Disease API'S
  @POST("patient/medicalrecord/illness/fetchall")
  Call<JsonObject> fetchAllPatientIllness(@Header("x-access-token") String accessToken, @Body JsonObject body);
  @POST("patient/medicalrecord/illness")
  Call<JsonObject> addPatientIllness(@Header("x-access-token") String accessToken, @Body JsonObject body);
  @HTTP(method = "DELETE", path = "patient/medicalrecord/illness/", hasBody = true)
  Call<JsonObject> deletePatientIllnessHistory(@Header("x-access-token")String accessToken,@Body JsonObject body);
  @HTTP(method = "DELETE", path = "patient/medicalrecord/illness/all", hasBody = true)
  Call<JsonObject> deleteAllPatientIllnessHistory(@Header("x-access-token")String accessToken,@Body JsonObject body);
  @PUT("patient/medicalrecord/illness")
  Call<JsonObject> updatePatientIllness(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @POST("patient/allergies/multiple")
  Call<JsonObject> addAlergiesApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @POST("patient/allergies/fetch")
  Call<JsonObject> fetchAlergiesApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  //@DELETE("patient/allergies/all")
  @HTTP(method = "DELETE", path = "patient/allergies/all", hasBody = true)
  Call<JsonObject> deleteAlergiesApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @POST("patient/immunization")
  Call<JsonObject> addImmunizationApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @PUT("patient/immunization")
  Call<JsonObject> updateImmunizationApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @POST("patient/immunization/fetch")
  Call<JsonObject> fetchImmunizationApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @HTTP(method = "DELETE", path = "patient/immunization", hasBody = true)
  Call<JsonObject> deleteSingleImmuApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @HTTP(method = "DELETE", path = "patient/immunization/all", hasBody = true)
  Call<JsonObject> deleteAllImmuApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @POST("hospital/getDoctorsByDept")
  Call<DepartmentResponseModel> getDoctorsByDept(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @Multipart
  @POST("patient/medicalrecord/illness/surgicalrecord/")
  Call<PatientSurgicalObject> addPatientSurgicalRecord(@Header("x-access-token") String AccessToken,
                                                       @Part("illnessID") RequestBody illnessID,
                                                       @Part MultipartBody.Part surgicalRecord,
                                                       @Part("moreDetails") RequestBody moreDetails,
                                                       @Part("doctorMedicalPersonnelID") RequestBody doctorMedicalpersondId,
                                                       @Part("doctorName") RequestBody doctorName,
                                                       @Part("surgeryProcedure") RequestBody surgeryProcedure,
                                                       @Part("surgeryDate") RequestBody surgeryDate,
                                                       @Part("surgeryInformation") RequestBody surgeryInformation,
                                                       @Part("byWhomID") RequestBody whomid,
                                                       @Part("medical_record_id") RequestBody medical_record_id,
                                                       @Part("byWhom") RequestBody whomtype,
                                                       @Part("hospital_reg_num") RequestBody hospital_reg_num,
                                                       @Part("patientID") RequestBody patientid
  );

  // PatientSurgical API's

  @Multipart
  @POST("patient/medicalrecord/illness/surgicalrecord/")
  Call<PatientSurgicalObject> addPatientSurgicalRecor(@Header("x-access-token") String AccessToken,
                                                      @Part("illnessID") RequestBody illnessID,
                                                      @Part MultipartBody.Part surgicalRecord,
                                                      @Part("moreDetails") RequestBody moreDetails,
                                                      @Part("doctorMedicalPersonnelID") RequestBody doctorMedicalpersondId,
                                                      @Part("doctorName") RequestBody doctorName,
                                                      @Part("surgeryProcedure") RequestBody surgeryProcedure,
                                                      @Part("surgeryDate") RequestBody surgeryDate,
                                                      @Part("surgeryInformation") RequestBody surgeryInformation,
                                                      @Part("byWhomID") RequestBody whomid,
                                                      @Part("medical_record_id") RequestBody medical_record_id,
                                                      @Part("byWhom") RequestBody whomtype,
                                                      @Part("hospital_reg_num") RequestBody hospital_reg_num,
                                                      @Part("patientID") RequestBody patientid
  );

  @Multipart
  @POST("patient/medicalrecord/illness/surgicalrecord/")
  Call<PatientSurgicalObject> updatePatientSurgicalRecord(@Header("x-access-token") String AccessToken,
                                                          @Part("illnessID") RequestBody illnessID,
                                                          @Part MultipartBody.Part surgicalRecord,
                                                          @Part("moreDetails") RequestBody moreDetails,
                                                          @Part("doctorMedicalPersonnelID") RequestBody doctorMedicalpersondId,
                                                          @Part("doctorName") RequestBody doctorName,
                                                          @Part("surgeryProcedure") RequestBody surgeryProcedure,
                                                          @Part("surgeryDate") RequestBody surgeryDate,
                                                          @Part("surgeryInformation") RequestBody surgeryInformation,
                                                          @Part("byWhomID") RequestBody whomid,
                                                          @Part("medical_record_id") RequestBody medical_record_id,
                                                          @Part("byWhom") RequestBody whomtype,
                                                          @Part("hospital_reg_num") RequestBody hospital_reg_num,
                                                          @Part("patientID") RequestBody patientid,
                                                          @Part("illnessSurgicalID") RequestBody illnessSurgicalID
  );

  @POST("patient/medicalrecord/illness/surgicalrecord/fetchall")
  Call<JsonObject> patientfetchSurgeryApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @HTTP(method = "DELETE", path = "patient/medicalrecord/illness/surgicalrecord/", hasBody = true)
  Call<JsonObject> deleteSingleSurgeryApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @HTTP(method = "DELETE", path = "patient/medicalrecord/illness/surgicalrecord//all", hasBody = true)
  Call<JsonObject> deleteAllSurgeryApi(@Header("x-access-token") String accessToken, @Body JsonObject body);


  @POST("patient/medicalrecord/illness/diagnosisNotes/")
  Call<JsonObject> addDiagnosisApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @PUT("patient/medicalrecord/illness/diagnosisNotes/")
  Call<JsonObject> updateDiagnosisApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @POST("patient/medicalrecord/illness/diagnosisNotes/fetchall")
  Call<JsonObject> fetchDiagnosisApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @HTTP(method = "DELETE", path = "patient/medicalrecord/illness/diagnosisNotes/", hasBody = true)
  Call<JsonObject> deleteSingleDiagnosis(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @HTTP(method = "DELETE", path = "patient/medicalrecord/illness/diagnosisNotes//all", hasBody = true)
  Call<JsonObject> deleteAllDiagnosis(@Header("x-access-token") String accessToken, @Body JsonObject body);


  @POST("patient/medicalrecord/illness/medication/manually")
  Call<JsonObject> addPatientMedicationApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @POST("patient/medicalrecord/illness/medication/fetchall")
  Call<JsonObject> fetchPatientMedicationApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @PUT("patient/medicalrecord/illness/medication/manually")
  Call<JsonObject> updatePatientMedicationApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @HTTP(method = "DELETE", path = "patient/medicalrecord/illness/medication", hasBody = true)
  Call<JsonObject> deletePatientSingleMedication(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @HTTP(method = "DELETE", path = "patient/medicalrecord/illness/medication/all", hasBody = true)
  Call<JsonObject> deletePatientAllMedication(@Header("x-access-token") String accessToken, @Body JsonObject body);


  //Patient appointment api's
  @POST("appointments/allappointmentsforPatient")
  Call<JsonObject> fetchAppointmentDetaisl(@Header("x-access-token") String accessToken, @Body JsonObject jsonObject);

  @POST("appointments/bookappointment")
  Call<JsonObject> addPatientAppointment(@Header("x-access-token") String accessToken,@Body JsonObject jsonObject);
  @POST("appointments/cancelappointment")
  Call<JsonObject> cancelPatientAppointment(@Header("x-access-token") String accessToken,@Body JsonObject jsonObject);

  @POST("services/fetch")
  Call<JsonObject> fetchServices(@Header("x-access-token") String accessToken,@Body JsonObject jsonObject);


  //PatientChatApi's
  @POST("chat/createchat")
  Call<JsonObject> createRoomID(@Header("x-access-token") String accessToken,@Body JsonObject jsonObject);

  @POST("patientdevices/fetchall")
  Call<JsonObject> fetchPatientDevicesApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @POST("patientdevices/")
  Call<JsonObject> addPatientDevicesApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @HTTP(method = "DELETE", path = "patientdevices", hasBody = true)
  Call<JsonObject> deleteDeviceApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @POST("patient/testresult/add")
  Call<JsonObject> addUriineResultApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @HTTP(method = "DELETE", path = "patient/testresult/one", hasBody = true)
  Call<JsonObject> deleteUrineResultApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @POST("patient/testresult/fetch")
  Call<JsonObject> fetchingUrineResultApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @POST("patient/latestsearchdoctors/add")
  Call<JsonObject> addLatestSearchApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @POST("patient/latestsearchdoctors/fetch")
  Call<JsonObject> fetchLatestSearchApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @POST("testitems/fetch")
  Call<JsonObject> fetchTestItemsApi(@Header("x-access-token") String accessToken, @Body JsonObject body);


  @POST("hospital/departments/fetch")
  Call<JsonObject> fetchDepartmentsFromHospitalApi(@Header("x-access-token") String accessToken, @Body JsonObject body);


  @POST("patient/logindevices/add")
  Call<JsonObject> addLoginDeviceApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @HTTP(method = "DELETE", path = "patient/logindevices/delete", hasBody = true)
  Call<JsonObject> deleteLoginDeviceApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @POST("invoice/byhospitalid")
  Call<JsonObject> getInvoiceByHospitalId(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @PUT("invoice")
  Call<InvoiceModel> invoiceUpdateApi(@Header("x-access-token") String accessToken,
                                      @Body InvoiceModel body);

  @POST("patient/notification/get")
  Call<JsonObject> getInboxFetchApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @POST("patient/notification/update")
  Call<JsonObject> inboxUpdateApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @HTTP(method = "DELETE", path = "patient/notification/deleteone", hasBody = true)
  Call<JsonObject> inboxDeleteApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @HTTP(method = "DELETE", path = "patient/notification/deleteall", hasBody = true)
  Call<JsonObject> inboxDeleteAllApi(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @POST("chatroom/getRooms")
  Call<JsonObject> getChatRooms(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @POST("patient/generalinfo/doctorreview")
  Call<JsonObject> addDoctorReview(@Header("x-access-token") String accessToken, @Body JsonObject body);

  //hospital/categories/fetch
  @POST("hospital/categories/fetch")
  Call<JsonObject> categoriesFetch(@Header("x-access-token") String accessToken, @Body JsonObject body);

  @POST("hospital/getDoctorsByCategory")
  Call<JsonObject> getDoctorsByCategory(@Header("x-access-token") String accessToken, @Body JsonObject body);
}
