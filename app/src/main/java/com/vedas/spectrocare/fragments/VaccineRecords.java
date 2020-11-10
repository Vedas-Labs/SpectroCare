/*
package com.vedas.spectrocare.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.ServerApiModel.BodyIndexServerObject;
import com.vedas.spectrocare.ServerApiModel.FamilyHistory;
import com.vedas.spectrocare.ServerApiModel.MedicalRecordResponse;
import com.vedas.spectrocare.ServerApiModel.PhysicalRecordServerObject;
import com.vedas.spectrocare.ServerApiModel.Vaccination;
import com.vedas.spectrocare.activities.AddMedicalRecordActivity;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class VaccineRecords extends Fragment {
    RecyclerView vaccineListView;
    ArrayList<Vaccination> vaccinationArrayList = new ArrayList<>();

    EditText edtVaccineName, edtVaccineDate, edtVaccineDescription;
    String vaccineName, vaccineDate, vaccineDescription, hospRegId, medicalPersonnelId;
    String medicalRecordId;
    Button vaccineAddBtn, submitBn, updateBtn;
    String patientId, accessToken;
    JSONArray vaccineArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View viewVaccine = inflater.inflate(R.layout_boarder.fragment_vaccine_records, container, false);
        vaccineListView = viewVaccine.findViewById(R.id.vaccine_list);
        Button btnAddVaccine = viewVaccine.findViewById(R.id.add_vaccine_record);
        submitBn = viewVaccine.findViewById(R.id.btn_submit);
        updateBtn = viewVaccine.findViewById(R.id.btn_update_record);
        updateBtn.setVisibility(View.GONE);


        if (getActivity() != null && ((AddMedicalRecordActivity) getActivity()).isUpdate) {
            updateBtn.setVisibility(View.VISIBLE);
            submitBn.setVisibility(View.GONE);

            SharedPreferences recordPreferences = getActivity().getSharedPreferences("recordInfo", MODE_PRIVATE);
            String recordInfoString = recordPreferences.getString("record_Info", "");
            try {
                JSONObject jsonObject = new JSONObject(recordInfoString);
                JSONArray medicalRecordArray = jsonObject.getJSONArray("medical_records");
                Log.e("arrayJson", "" + medicalRecordArray);
                JSONObject position = medicalRecordArray.getJSONObject(0);
                vaccineArray = position.getJSONArray("vaccination");
                Log.e("helloArray", "" + vaccineArray);

                for (int i = 0; i < vaccineArray.length(); i++) {

                    try {
                        vaccinationArrayList.add(new Vaccination(vaccineArray.getJSONObject(i).getString("name"),
                                vaccineArray.getJSONObject(i).getString("date"),
                                vaccineArray.getJSONObject(i).getString("notes")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            updateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PersonalInfoController.getInstance().currectProfileData.setVaccination(vaccinationArrayList);

                    BodyIndexServerObject bodyIndex = PersonalInfoController.getInstance().currectProfileData.getBodyIndex();
                    ArrayList<FamilyHistory> familyHistories = PersonalInfoController.getInstance().currectProfileData.getFamilyHistory();
                    ArrayList<PhysicalRecordServerObject> physicalExaminations = PersonalInfoController.getInstance().currectProfileData.getPhysicalExamination();
                    ArrayList<Vaccination> vaccinations = PersonalInfoController.getInstance().currectProfileData.getVaccination();
                    getIds();

                    SharedPreferences recordPreferences = getActivity().getSharedPreferences("recordInfo", MODE_PRIVATE);
                    medicalRecordId = recordPreferences.getString("medicRecordId", "");
                    Log.e("ttttt", "" + medicalRecordId);

                    JsonObject UpdateRecordObject = new JsonObject();
                    JSONObject idObject = new JSONObject();

                    try {
                        idObject.put("medical_personnel_id", medicalPersonnelId);
                        idObject.put("hospital_reg_num", hospRegId);
                        idObject.put("patientID", patientId);
                        idObject.put("medical_record_id",medicalRecordId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONObject bodyIndexObject = new JSONObject();
                    try {
                        bodyIndexObject.put("height", bodyIndex.getHeight());
                        bodyIndexObject.put("weight", bodyIndex.getWeight());
                        bodyIndexObject.put("waistline", bodyIndex.getWaistline());
                        bodyIndexObject.put("bmi", bodyIndex.getBmi());
                        bodyIndexObject.put("bloodPressure", bodyIndex.getBloodPressure());
                        idObject.put("bodyIndex", bodyIndexObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JSONArray familyArryObject = new JSONArray();
                    JSONObject familyObject = new JSONObject();
                    for (int i = 0; i < familyHistories.size(); i++) {


                        try {
                            familyObject.put("condition", familyHistories.get(i).getCondition());
                            familyObject.put("relationship", familyHistories.get(i).getRelationship());
                            familyObject.put("age", familyHistories.get(i).getAge());
                            familyObject.put("moreInfo", familyHistories.get(i).getMoreInfo());
                            familyArryObject.put(familyObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    try {

                        idObject.put("familyHistory", familyArryObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONArray physicalArrayObject = new JSONArray();
                    JSONObject physicalObject = new JSONObject();
                    for (int i = 0; i < physicalExaminations.size(); i++) {

                        try {
                            physicalObject.put("category", physicalExaminations.get(i).getCategory());
                            physicalObject.put("result", physicalExaminations.get(i).getResult());
                            physicalObject.put("description", physicalExaminations.get(i).getDescription());
                            idObject.put("physicalExamination", physicalArrayObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        physicalArrayObject.put(physicalObject);
                    }

                        Log.e("dmkd", "" + physicalArrayObject);



                    JSONArray vaccineArray = new JSONArray();

                    JSONObject vaccineObject = new JSONObject();
                    for (int i = 0; i < vaccinations.size(); i++) {


                        try {
                            vaccineObject.put("name", vaccinations.get(i).getName());
                            vaccineObject.put("date", vaccinations.get(i).getDate());
                            vaccineObject.put("notes", vaccinations.get(i).getNotes());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        vaccineArray.put(vaccineObject);
                    }

                    try {
                        idObject.put("vaccination", vaccineArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonParser jsonParser = new JsonParser();
                    UpdateRecordObject = (JsonObject) jsonParser.parse(idObject.toString());
                    Log.e("checkt",""+UpdateRecordObject);

                    Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                            .addConverterFactory(GsonConverterFactory.create()).build();
                    ServerApi serverApi = retrofit.create(ServerApi.class);

                    Call<MedicalRecordResponse> medicalRecordResponseCall = serverApi.updateMedicalRecordList(accessToken, UpdateRecordObject);
                    medicalRecordResponseCall.enqueue(new Callback<MedicalRecordResponse>() {
                        @Override
                        public void onResponse(Call<MedicalRecordResponse> call, Response<MedicalRecordResponse> response) {
                            Log.e("checkResponse",""+response.code());
                        }

                        @Override
                        public void onFailure(Call<MedicalRecordResponse> call, Throwable t) {

                        }
                    });

                }
            });
        }


        VaccineAdapter vaccineAdapter = new VaccineAdapter(getContext());
        vaccineListView.setHasFixedSize(true);
        vaccineListView.setLayoutManager(new LinearLayoutManager(getContext()));
        vaccineListView.setAdapter(vaccineAdapter);
        getIds();

        submitBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PersonalInfoController.getInstance().currectProfileData.setVaccination(vaccinationArrayList);

                BodyIndexServerObject bodyIndex = PersonalInfoController.getInstance().currectProfileData.getBodyIndex();
                ArrayList<FamilyHistory> familyHistories = PersonalInfoController.getInstance().currectProfileData.getFamilyHistory();
                ArrayList<PhysicalRecordServerObject> physicalExaminations = PersonalInfoController.getInstance().currectProfileData.getPhysicalExamination();
                ArrayList<Vaccination> vaccinations = PersonalInfoController.getInstance().currectProfileData.getVaccination();
                Log.e("fdhjs", "" + bodyIndex.getHeight());
                Log.e("fdhjs", "" + bodyIndex.getWeight());

                SharedPreferences idPatientPrefer = getActivity().getSharedPreferences("patientPrefer",MODE_PRIVATE);
                patientId = idPatientPrefer.getString("idPatient","");
                Log.e("klklkl",""+patientId);

                Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                        .addConverterFactory(GsonConverterFactory.create()).build();
                ServerApi serverApi = retrofit.create(ServerApi.class);

                Log.e("hfjklj", "" + medicalPersonnelId);
                Log.e("hfjklj", "" + hospRegId);
                Log.e("djhjd", "" + patientId);

                JsonObject jsonObject = new JsonObject();
                JSONObject jsonObject1 = new JSONObject();
                try {
                    jsonObject1.put("medical_personnel_id", medicalPersonnelId);
                    jsonObject1.put("hospital_reg_num", hospRegId);
                    jsonObject1.put("patientID", patientId);
                    JSONObject jsonObject2 = new JSONObject();

                    jsonObject2.put("height", bodyIndex.getHeight());
                    jsonObject2.put("weight", bodyIndex.getWeight());
                    jsonObject2.put("waistline", bodyIndex.getWaistline());
                    jsonObject2.put("bmi", bodyIndex.getBmi());
                    jsonObject2.put("bloodPressure", bodyIndex.getBloodPressure());
                    jsonObject1.put("bodyIndex", jsonObject2);
                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject3 = new JSONObject();
                    for (int i = 0; i < familyHistories.size(); i++) {


                        jsonObject3.put("condition", familyHistories.get(i).getCondition());
                        jsonObject3.put("relationship", familyHistories.get(i).getRelationship());
                        jsonObject3.put("age", familyHistories.get(i).getAge());
                        jsonObject3.put("moreInfo", familyHistories.get(i).getMoreInfo());

                        jsonArray.put(jsonObject3);
                    }

                    Log.e("dmkd", "" + jsonArray.get(0).toString());
                    Log.e("dlm", "" + jsonArray.get(1).toString());
                    Log.e("smdd", "" + jsonArray.get(2).toString());
jsonObject3.put("condition",familyHistories.get(0).getCondition());
                    jsonObject3.put("relationship","Father");
                    jsonObject3.put("age","59");
                    jsonObject3.put("moreInfo","had 45 years");
                    JSONObject jsonObject4=new JSONObject();
                    jsonObject4.put("condition","Blood");
                    jsonObject4.put("relationship","Father");
                    jsonObject4.put("age","78");
                    jsonObject4.put("moreInfo","had 78 years");
                    jsonArray.put(jsonObject3);
                    jsonArray.put(jsonObject4);




                    jsonObject1.put("familyHistory", jsonArray);

                    JSONArray jsonArray1 = new JSONArray();
                    JSONObject jsonObject4 = new JSONObject();
                    for (int i = 0; i < physicalExaminations.size(); i++) {


                        jsonObject4.put("category", physicalExaminations.get(i).getCategory());
                        jsonObject4.put("result", physicalExaminations.get(i).getResult());
                        jsonObject4.put("description", physicalExaminations.get(i).getDescription());


                        jsonArray1.put(jsonObject4);
                    }
                    Log.e("physical", "" + jsonArray1.get(0).toString());
 JSONObject jsonObject5=new JSONObject();
                    jsonObject5.put("category","General");
                    jsonObject5.put("result","normol");
                    jsonObject5.put("description","helloo");
                    JSONObject jsonObject6=new JSONObject();

                    jsonObject6.put("category","General");
                    jsonObject6.put("result","sevier");
                    jsonObject6.put("description","hai");
                    jsonArray1.put(jsonObject5);
                    jsonArray1.put(jsonObject6);


                    jsonObject1.put("physicalExamination", jsonArray1);


                    JSONArray jsonArray2 = new JSONArray();

                    JSONObject jsonObject5 = new JSONObject();
                    for (int i = 0; i < vaccinations.size(); i++) {


                        jsonObject5.put("name", vaccinations.get(i).getName());
                        jsonObject5.put("date", vaccinations.get(i).getDate());
                        jsonObject5.put("notes", vaccinations.get(i).getNotes());


                        jsonArray2.put(jsonObject5);
                    }

                    jsonObject1.put("vaccination", jsonArray2);
                    Log.e("testingArray", "" + jsonArray2.get(0).toString());


                    JsonParser jsonParser = new JsonParser();
                    jsonObject = (JsonObject) jsonParser.parse(jsonObject1.toString());
                    Log.e("ifjij", "" + jsonObject.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Call<MedicalRecordResponse> medicalRecordResponseCall = serverApi.AddMedicalRecord(accessToken, jsonObject);
                medicalRecordResponseCall.enqueue(new Callback<MedicalRecordResponse>() {
                    @Override
                    public void onResponse(Call<MedicalRecordResponse> call, Response<MedicalRecordResponse> response) {
                        if (response != null) {
                            Log.e("dnjdj", "" + response.code());
  String  medicRecordId = response.body().getMedicalRecordId();
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                            editor.putString("medicRecordId",medicRecordId);

                            //      Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), AddMedicalRecordActivity.class));
                        }

                    }

                    @Override
                    public void onFailure(Call<MedicalRecordResponse> call, Throwable t) {

                    }
                });

            }
        });
        btnAddVaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout_boarder.vaccine_alert, null);
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(view);
                edtVaccineName = dialog.findViewById(R.id.edt_vaccine_name);
                edtVaccineDate = dialog.findViewById(R.id.edt_vaccine_date);
                edtVaccineDescription = dialog.findViewById(R.id.edt_vaccine_description);
                vaccineAddBtn = dialog.findViewById(R.id.btn_add_vaccine_details);
                vaccineAddBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vaccineName = edtVaccineName.getText().toString();
                        Log.e("vsd", "" + vaccineName);
                        vaccineDate = edtVaccineDate.getText().toString();
                        vaccineDescription = edtVaccineDescription.getText().toString();
                        Vaccination vaccination = new Vaccination(vaccineName, vaccineDate, vaccineDescription);
                        vaccinationArrayList.add(vaccination);
  Log.e("arrayy",""+vaccinationArrayList.get(0).getName());
                        Log.e("arrayy",""+vaccinationArrayList.get(1).getName());
                        Log.e("arrayy",""+vaccinationArrayList.get(2));
                        Log.e("arrayy",""+vaccinationArrayList.get(3));

 VaccineAdapter vaccineAdapter = new VaccineAdapter(getContext(),vaccinationArrayList);
                        vaccineListView.setHasFixedSize(true);
                        vaccineListView.setLayoutManager(new LinearLayoutManager(getContext()));
                        vaccineListView.setAdapter(vaccineAdapter);

                        dialog.dismiss();
                    }
                });
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });
        return viewVaccine;
    }

    public void getIds() {

        SharedPreferences preferences = getActivity().getSharedPreferences("MyPref",0);
        patientId = preferences.getString("patientId",null);
        Log.e("checkIdofpa",""+patientId);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserInfo", MODE_PRIVATE);
        medicalPersonnelId = sharedPreferences.getString("userInfo", "");
        Log.e("paId", "" + medicalPersonnelId);

        try {
            JSONObject jsonObject = new JSONObject(medicalPersonnelId);
            JSONObject medicalPersonnelJsonObject = jsonObject.getJSONObject("medicalPersonnel");
            accessToken = jsonObject.getString("access_token");
            Log.e("token", "" + accessToken);
            medicalPersonnelId = medicalPersonnelJsonObject.getString("medical_personnel_id");
            Log.e("IdOfMedic", "" + medicalPersonnelId);
            hospRegId = medicalPersonnelJsonObject.getString("hospital_reg_num");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
*/
