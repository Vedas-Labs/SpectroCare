package com.vedas.spectrocare.PatientModule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientDocResponseModel.DepartmentResponseModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.model.CategoryItemModel;
import com.vedas.spectrocare.model.DoctorsItemModel;
import com.vedas.spectrocare.patientModuleAdapter.DoctorLatestSearchAdapter;
import com.vedas.spectrocare.patientModuleAdapter.DoctorsAdapter;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DoctorsActivity extends AppCompatActivity {
    RecyclerView doctorsRecyclerView,latestDocSearchView;
    DoctorsAdapter doctorsAdapter;
    TextView txtByCategory,txtByDepartment;
    ImageView imgBack;
    RelativeLayout layoutElderly;
    DoctorLatestSearchAdapter latestSearchAdapter;
    ArrayList doctorsList,latestDocList;
    DepartmentResponseModel responseModel;
    RefreshShowingDialog showingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);
        responseModel = new DepartmentResponseModel();
        PatientLoginDataController.getInstance().fetchPatientlProfileData();
        imgBack = findViewById(R.id.img_back_arrow);
        accessInterfaceMethods();
        doctorsRecyclerView = findViewById(R.id.doctors_grid_view);
        latestDocSearchView = findViewById(R.id.latest_doc_list);
        txtByCategory = findViewById(R.id.txt_view_all);
        txtByDepartment = findViewById(R.id.txt_all_view);
        layoutElderly = findViewById(R.id.elderly_layout);
        doctorsList = new ArrayList();
        latestDocList = new ArrayList();
        doctorsRecyclerView.setFocusable(false);
        showingDialog=new RefreshShowingDialog(DoctorsActivity.this);
        doctorsList.add(new CategoryItemModel(R.drawable.diabetescare_1, "Diabetic Care"));
        doctorsList.add(new CategoryItemModel(R.drawable.cardiovascular_1, "cardiovascular Care"));
        doctorsList.add(new CategoryItemModel(R.drawable.kidney_1, "Kidney Care"));
        doctorsList.add(new CategoryItemModel(R.drawable.dentalcare_1, "Dental Care"));
        doctorsList.add(new CategoryItemModel(R.drawable.cough_cold_1, "Cough & cold"));
        doctorsList.add(new CategoryItemModel(R.drawable.stomochcre_1, "Stomach Care "));

        latestSearchAdapter = new DoctorLatestSearchAdapter(DoctorsActivity.this,latestDocList);
        doctorsAdapter = new DoctorsAdapter(DoctorsActivity.this,doctorsList);
        doctorsRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        doctorsRecyclerView.setNestedScrollingEnabled(false);
        doctorsRecyclerView.setAdapter(doctorsAdapter);

        latestDocList.add(new DoctorsItemModel("k", "Dr Muneeswar","Cardiology"));
        latestDocList.add(new DoctorsItemModel("k", "Dr Durga prasd","ENT"));
        latestDocSearchView.setLayoutManager(new LinearLayoutManager(DoctorsActivity.this));
        latestDocSearchView.setNestedScrollingEnabled(false);
        latestDocSearchView.setAdapter(latestSearchAdapter);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtByCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoctorsActivity.this, CategoryInPatientModuleActivity.class));
            }
        });
        layoutElderly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoctorsActivity.this,SearchResultsActivity.class));
            }
        });

        txtByDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // showingDialog.showAlert();
                //DocDepartmentAPI();
               //loadgetDoctorsByDeptApi();
               startActivity(new Intent(DoctorsActivity.this,DoctorsDepartmentActivity.class));
            }
        });
    }
    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                if (curdOpetaton.equals("add")) {
                    try {
                        Log.e("feeedbackresponse","call"+jsonObject.toString());
                        JSONArray medicalArray=jsonObject.getJSONArray("medicalPersonnels");
                        Log.e("feeedbackresponse","call"+medicalArray.length());
                        for(int i=0; i<medicalArray.length(); i++) {
                            JSONObject profile=medicalArray.getJSONObject(i);
                            Log.e("feeedbackresponse","call"+profile.toString());
                            JSONObject profileObj=profile.getJSONObject("profile");
                            JSONObject userProfile=profileObj.getJSONObject("userProfile");
                            Log.e("userProfile","call"+userProfile.toString());
                            String firstName=userProfile.getString("firstName");
                            String lastName=userProfile.getString("lastName");
                            String emailID=userProfile.getString("emailID");
                            Log.e("medicalArray",""+firstName +lastName+emailID );
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showingDialog.hideRefreshDialog();
                    // Toast.makeText(getApplicationContext(), "Patient App settings has been updated", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void failureCallBack(String failureMsg) {
                showingDialog.hideRefreshDialog();
                Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadgetDoctorsByDeptApi(){
        JSONObject params = new JSONObject();
        try {
            params.put("byWhomID","viswanath3344@gmail.com");
            params.put("byWhom","patient");
            params.put("department","Elderly services");
            params.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number() );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
      //  ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.getDoctorsByDept(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(),gsonObject), "add");
    }
    public void DocDepartmentAPI(){
        Retrofit rFit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ServerApi serverDepartmentApi = rFit.create(ServerApi.class);
        JSONObject params = new JSONObject();
        try {
            params.put("byWhomID","viswanath3344@gmail.com");
            params.put("byWhom","patient");
            params.put("department","Elderly services");
            params.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number() );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        Call<DepartmentResponseModel> callDepartmentAPI = serverDepartmentApi.getDoctorsByDept(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(),gsonObject);
        callDepartmentAPI.enqueue(new Callback<DepartmentResponseModel>() {
            @Override
            public void onResponse(Call<DepartmentResponseModel> call, Response<DepartmentResponseModel> response) {
                Log.e("response","res"+response.body());
                showingDialog.hideRefreshDialog();
                responseModel = response.body();

                Gson gson = new Gson();
                String json = gson.toJson(responseModel);
                Log.e("list","size"+responseModel.getMessage()) ;
                Log.e("jso","size"+json) ;
            }

            @Override
            public void onFailure(Call<DepartmentResponseModel> call, Throwable t) {
                Log.e("fail","res"+t.getMessage());
                showingDialog.hideRefreshDialog();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DoctorsActivity.this, PatientHomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
