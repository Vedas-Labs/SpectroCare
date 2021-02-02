package com.vedas.spectrocare.PatientModule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.vedas.spectrocare.PatientDocResponseModel.MedicalPersonnelModel;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.model.CategoryItemModel;
import com.vedas.spectrocare.model.DoctorsItemModel;
import com.vedas.spectrocare.patientModuleAdapter.DoctorLatestSearchAdapter;
import com.vedas.spectrocare.patientModuleAdapter.DoctorsAdapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
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
    RecyclerView doctorsRecyclerView,latestDocSearchView,deptRecyclerView;
    DoctorsAdapter doctorsAdapter;
    TextView txtByCategory,txtByDepartment;
    ImageView imgBack;
    RelativeLayout layoutElderly;
    DoctorLatestSearchAdapter latestSearchAdapter;
    ArrayList doctorsList;
    DepartmentResponseModel responseModel;
    RefreshShowingDialog showingDialog;
    DoctorDeptAdapter doctorDeptAdapter;
    ArrayList<CategoryItemModel> departmentList = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);
        responseModel = new DepartmentResponseModel();
        PatientLoginDataController.getInstance().fetchPatientlProfileData();
        imgBack = findViewById(R.id.img_back_arrow);
        accessInterfaceMethods();
        fetchLatestDoctorsApi();
        fetchDepartmentApiFromHospital();

        doctorsRecyclerView = findViewById(R.id.doctors_grid_view);
        latestDocSearchView = findViewById(R.id.latest_doc_list);
        txtByCategory = findViewById(R.id.txt_view_all);
        txtByDepartment = findViewById(R.id.txt_all_view);
        layoutElderly = findViewById(R.id.elderly_layout);
        deptRecyclerView = findViewById(R.id.dept_grid_view);

        doctorsList = new ArrayList();
        doctorsRecyclerView.setFocusable(false);
        showingDialog=new RefreshShowingDialog(DoctorsActivity.this);
        doctorsList.add(new CategoryItemModel(R.drawable.diabetescare_1, "Diabetic Care"));
        doctorsList.add(new CategoryItemModel(R.drawable.cardiovascular_1, "cardiovascular Care"));
        doctorsList.add(new CategoryItemModel(R.drawable.kidney_1, "Kidney Care"));
        doctorsList.add(new CategoryItemModel(R.drawable.dentalcare_1, "Dental Care"));
        doctorsList.add(new CategoryItemModel(R.drawable.cough_cold_1, "Cough & cold"));
        doctorsList.add(new CategoryItemModel(R.drawable.stomochcre_1, "Stomach Care "));


        latestSearchAdapter = new DoctorLatestSearchAdapter(DoctorsActivity.this);
        doctorsAdapter = new DoctorsAdapter(DoctorsActivity.this,doctorsList);
        doctorsRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        doctorsRecyclerView.setNestedScrollingEnabled(false);
        doctorsRecyclerView.setAdapter(doctorsAdapter);

        /*latestDocList.add(new DoctorsItemModel("k", "Dr Muneeswar","Cardiology"));
        latestDocList.add(new DoctorsItemModel("k", "Dr Durga prasd","ENT"));*/
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
    private void fetchDepartmentApiFromHospital() {
        JSONObject params = new JSONObject();
        try {
            params.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.fetchDepartmentsFromHospitalApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "fetch");
    }

    private void fetchLatestDoctorsApi(){
        JSONObject params = new JSONObject();
        try {
            params.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number() );
            params.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId() );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
          ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.fetchLatestSearchApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(),gsonObject), "fetchdoctors");
    }
    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                if (curdOpetaton.equals("fetchdoctors")) {
                    try {
                        JSONArray appointmentArray = jsonObject.getJSONArray("doctors");
                        for (int l = 0; l < appointmentArray.length(); l++) {
                            Gson gson = new Gson();
                            String jsonString = appointmentArray.getJSONObject(l).toString();
                          /*  DoctorDetailsModel doctorDetailsModel = gson.fromJson(jsonString, DoctorDetailsModel.class);
                            PatientMedicalRecordsController.getInstance().latestSearchDoctorsList.add(doctorDetailsModel);
                            Log.e("adddoctor", "call" + PatientMedicalRecordsController.getInstance().latestSearchDoctorsList.size());
                        */
                            MedicalPersonnelModel doctorDetailsModel = gson.fromJson(jsonString, MedicalPersonnelModel.class);
                            PatientMedicalRecordsController.getInstance().doctorProfleList.add(doctorDetailsModel);
                            Log.e("adddoctor", "call" + PatientMedicalRecordsController.getInstance().doctorProfleList.size());

                        }
                        latestSearchAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(curdOpetaton.equals("fetch")){
                    try {
                        if (jsonObject.getString("response").equals("3")) {
                            JSONObject recordsObj = jsonObject.getJSONObject("records");
                            JSONArray departmentsArray=recordsObj.getJSONArray("departments");
                            for(int i=0;i<departmentsArray.length();i++){
                                JSONObject obj=departmentsArray.getJSONObject(i);
                                departmentList.add(new CategoryItemModel(R.drawable.sample_image, obj.getString("department")));
                            }
                            doctorDeptAdapter = new DoctorDeptAdapter(DoctorsActivity.this,departmentList);
                            // deptRecyclerView.setLayoutManager(new LinearLayoutManager(DoctorsActivity.this));
                            deptRecyclerView.setLayoutManager(new GridLayoutManager(DoctorsActivity.this, 3));
                            deptRecyclerView.setNestedScrollingEnabled(false);
                            deptRecyclerView.setAdapter(doctorDeptAdapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
    public class DoctorDeptAdapter extends RecyclerView.Adapter<DoctorDeptAdapter.DoctorSearchHolder> {
        Context context;
        ArrayList<CategoryItemModel> doctorsLatestList;

        public DoctorDeptAdapter(Context context, ArrayList<CategoryItemModel> doctorsLatestList) {
            this.context = context;
            this.doctorsLatestList = doctorsLatestList;
        }

        @NonNull
        @Override
        public DoctorDeptAdapter.DoctorSearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View doctoresLatestSearchView = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_items_layout, parent, false);
            return new DoctorSearchHolder(doctoresLatestSearchView);
        }

        @Override
        public void onBindViewHolder(@NonNull DoctorSearchHolder holder, int position) {
            holder.doctorsItemIcon.setImageResource(doctorsLatestList.get(position).getCategoryIcon());
            holder.doctorsItemName.setText(doctorsLatestList.get(position).getCategoryTitle());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i =new Intent(context, SearchResultsActivity.class);
                    i.putExtra("deptname",doctorsLatestList.get(holder.getAdapterPosition()).getCategoryTitle());
                    context.startActivity(i);
                    // context.startActivity(new Intent(context, SearchResultsActivity.class));
                }
            });
        }

        @Override
        public int getItemCount() {
            if(doctorsLatestList.size()>0){
                if(doctorsLatestList.size()>=3){
                    return 3;
                }else{
                    return doctorsLatestList.size();
                }
            }else {
                return 0;
            }
        }

        public class DoctorSearchHolder extends RecyclerView.ViewHolder {
            ImageView doctorsItemIcon;
            TextView doctorsItemName;
            public DoctorSearchHolder(@NonNull View itemView) {
                super(itemView);
                doctorsItemIcon = itemView.findViewById(R.id.img_item);
                doctorsItemName = itemView.findViewById(R.id.txt_item);

            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DoctorsActivity.this, PatientHomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
