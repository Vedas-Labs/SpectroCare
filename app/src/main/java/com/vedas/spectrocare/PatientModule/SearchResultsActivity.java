package com.vedas.spectrocare.PatientModule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientDocResponseModel.DepartmentResponseModel;
import com.vedas.spectrocare.PatientDocResponseModel.MedicalPersonnelModel;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.model.DoctorsItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchResultsActivity extends AppCompatActivity {
    //ArrayList<DoctorsItemModel> searchedList=new ArrayList<>();
    RelativeLayout btnShowMore;
    RecyclerView searchResultView;
    Button btnMore;
    ImageView imgBack;
    SearchResultAdapter resultAdapter;
   // DepartmentResponseModel responseModel;
    EditText searchEdit;
    ArrayList<MedicalPersonnelModel> medicalPersonnelModelArrayList=new ArrayList<>();
    RefreshShowingDialog showingDialog;
    TextView txt_selectedDept;
    String deptName = "";
    String category = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        btnShowMore = findViewById(R.id.layout_more);
        imgBack = findViewById(R.id.img_back_arrow);
        btnMore = findViewById(R.id.btn_show_more);
       // responseModel = new DepartmentResponseModel();
        showingDialog = new RefreshShowingDialog(SearchResultsActivity.this);
        showingDialog.showAlert();
        searchEdit = findViewById(R.id.edt_search);


        searchResultView = findViewById(R.id.serch_result_view);
        searchResultView.setLayoutManager(new LinearLayoutManager(SearchResultsActivity.this));
        searchResultView.setNestedScrollingEnabled(false);
        resultAdapter = new SearchResultAdapter(SearchResultsActivity.this, medicalPersonnelModelArrayList);
        searchResultView.setAdapter(resultAdapter);

        txt_selectedDept = findViewById(R.id.txt_search_by);
        accessInterfaceMethods();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                deptName = null;
            } else {
                deptName = extras.getString("deptname");
                category = extras.getString("category");
                Log.e("extras", "onResponse: " + deptName + category);
                if (deptName != null) {
                    txt_selectedDept.setText(deptName);
                    DocDepartmentAPI();
                } else {
                    txt_selectedDept.setText(category);
                    DocCategoryAPI();
                }

            }
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchResultsActivity.this, PatientPaymentActivity.class));
            }
        });
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });
    }

    private void filter(String text) {
        ArrayList<MedicalPersonnelModel> filterdNames = new ArrayList<>();
        for (MedicalPersonnelModel s : medicalPersonnelModelArrayList) {
            if (s.getProfile().getUserProfile().getFirstName().toLowerCase().contains(text.toLowerCase())) {
                filterdNames.add(s);
            }
        }
        resultAdapter.filterList(filterdNames);
    }

    public void DocCategoryAPI() {
        JSONObject params = new JSONObject();
        try {
            params.put("byWhomID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("byWhom", "patient");
            params.put("category", txt_selectedDept.getText().toString()/*"Dermatology"*/);
            params.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.getDoctorsByCategory(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "fetchcategoriesdoctors");

    }

    public void DocDepartmentAPI() {
        JSONObject params = new JSONObject();
        try {
            params.put("byWhomID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("byWhom", "patient");
            params.put("department", txt_selectedDept.getText().toString());
            params.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("DocDepartmentAPI", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.getDoctorsByDept(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "fetchdoctorsdept");
    }

    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                try {
                    if (jsonObject.getString("response").equals("3")) {
                       // searchedList.clear();
                        showingDialog.hideRefreshDialog();
                        if (curdOpetaton.equals("fetchcategoriesdoctors") || curdOpetaton.equals("fetchdoctorsdept")) {
                            try {
                                JSONArray jsonArray = jsonObject.getJSONArray("medicalPersonnels");
                                if (jsonArray.length() > 0) {
                                    Log.e("medicalPersonnels", "call" + jsonArray);
                                    for (int l = 0; l < jsonArray.length(); l++) {
                                        Gson gson = new Gson();
                                        String jsonString = jsonArray.getJSONObject(l).toString();
                                        MedicalPersonnelModel medicalPersonnelModel = gson.fromJson(jsonString, MedicalPersonnelModel.class);
                                        medicalPersonnelModelArrayList.add(medicalPersonnelModel);
                                    }
                                    resultAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(getApplicationContext(), "No Doctors Found.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void failureCallBack(String failureMsg) {
                showingDialog.hideRefreshDialog();
                Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResulHolder> {
        Context context;
        ArrayList<MedicalPersonnelModel> doctorsLatestList;

        public SearchResultAdapter(Context context, ArrayList<MedicalPersonnelModel> doctorsLatestList) {
            this.context = context;
            this.doctorsLatestList = doctorsLatestList;
        }

        @NonNull
        @Override
        public SearchResultAdapter.SearchResulHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View doctorSearchView = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctors_latest_search_item, parent, false);
            return new SearchResulHolder(doctorSearchView);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchResultAdapter.SearchResulHolder holder, int position) {
            Picasso.get().load(ServerApi.img_home_url + doctorsLatestList.get(position).getProfile().getUserProfile().getProfilePic()).placeholder(R.drawable.profile_1)
                    .into(holder.docPic);
            holder.txtDocName.setText(doctorsLatestList.get(position).getProfile().getUserProfile().getFirstName());
            holder.txtDocProfession.setText(doctorsLatestList.get(position).getProfile().getUserProfile().getDepartment());
            holder.btnBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PatientMedicalRecordsController.getInstance().medicalPersonnelModel = doctorsLatestList.get(holder.getAdapterPosition());
                    //context.startActivity(new Intent(context, PatientBookAppointmentActivity.class));
                    MedicalPersonnelModel obj = PatientMedicalRecordsController.getInstance().medicalPersonnelModel;
                    context.startActivity(new Intent(context, PatientBookAppointmentActivity.class)
                            .putExtra("docName", obj.getProfile().getUserProfile().getFirstName() + " " + obj.getProfile().getUserProfile().getLastName())
                            .putExtra("docProfi", obj.getProfile().getUserProfile().getDepartment())
                            .putExtra("docId", obj.getProfile().getUserProfile().getMedical_personnel_id())
                            .putExtra("docProfile", obj.getProfile().getUserProfile().getProfilePic()));
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PatientMedicalRecordsController.getInstance().medicalPersonnelModel = doctorsLatestList.get(holder.getAdapterPosition());
                    context.startActivity(new Intent(context, DoctorSummeryActivity.class));
                }
            });
        }

        @Override
        public int getItemCount() {
            if(doctorsLatestList.size()>0) {
                return doctorsLatestList.size();
            }else{
                return 0;
            }
        }

        public class SearchResulHolder extends RecyclerView.ViewHolder {
            ImageView docPic;
            Button btnBook;
            TextView txtDocName, txtDocProfession;

            public SearchResulHolder(@NonNull View itemView) {
                super(itemView);
                docPic = itemView.findViewById(R.id.img_doc_pic);
                txtDocName = itemView.findViewById(R.id.txt_doc_name);
                btnBook = itemView.findViewById(R.id.btn_book);
                txtDocProfession = itemView.findViewById(R.id.txt_doc_profession);
            }
        }

        public void filterList(ArrayList<MedicalPersonnelModel> filterdNames) {
            this.doctorsLatestList = filterdNames;
            notifyDataSetChanged();
        }
    }
}