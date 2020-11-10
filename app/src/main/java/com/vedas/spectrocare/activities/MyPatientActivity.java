package com.vedas.spectrocare.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.text.SimpleDateFormat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonElement;

import com.google.gson.JsonObject;
import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.DataBase.AllergyDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBase.PhysicalExamDataController;
import com.vedas.spectrocare.DataBase.TrackInfoDataController;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.DataBaseModels.PatientlProfileModel;
import com.vedas.spectrocare.DataBaseModels.TrackInfoModel;
import com.vedas.spectrocare.Location.LocationTracker;
import com.vedas.spectrocare.ServerApiModel.TrackingServerObject;
import com.vedas.spectrocare.SingleTapDetector;
import com.vedas.spectrocare.adapter.PatientAdapter;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.model.GetPatientsResponseModel;
import com.vedas.spectrocare.model.PatientList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vedas.spectrocare.model.PhoneNumber;

import org.json.JSONException;
import org.json.JSONObject;
import java.sql.Timestamp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

public class MyPatientActivity extends AppCompatActivity implements MedicalPersonaSignupView{
    FloatingActionButton btnAddPatientDetails;
    ProgressBar progressBar;
    //AlertDialog.Builder dialog2;
    public static AlertDialog alertDialog;
    PatientAdapter patientAdapter;
    RecyclerView recyclerView;
    SearchView searchView;
    private  float dX,dY;
    int lastAction;
    TextView txtPatientDisc;
    EditText searchbar;
    public static boolean isFromStarting=false;
    GestureDetector gestureDetector;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_patient);
        txtPatientDisc = findViewById(R.id.text_patients_disc);
        gestureDetector = new GestureDetector(this, new SingleTapDetector());
        PersonalInfoController.getInstance().fillContent(getApplicationContext());
        PatientProfileDataController.getInstance().fetchPatientlProfileData();
        init();

        if (isFromStarting) {
            if (isConn()) {
                alertDialog.show();
                Objects.requireNonNull(alertDialog.getWindow()).setLayout(600, 500);
                fetchServerApi();
            } else {
                alertDialog.dismiss();
                dialogeforCheckavilability("Error", "No Internet connection", "Ok");
            }
        }

    }

    public void init() {
        LayoutInflater inflater = MyPatientActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alertbox_layout, null);
        AlertDialog.Builder dialog1 = new AlertDialog.Builder(MyPatientActivity.this);
        progressBar = dialogView.findViewById(R.id.progressBar);
        dialog1.setView(dialogView);
        alertDialog = dialog1.create();

        searchbar = (EditText) findViewById(R.id.search);
        loadSearchtext();

        btnAddPatientDetails = findViewById(R.id.btn_add_detail);
/*
        btnAddPatientDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientProfileDataController.getInstance().currentPatientlProfile=null;
                Intent intent = new Intent(MyPatientActivity.this, PatientGeneralProfileActivity.class);
                startActivity(intent);
            }
        });
*/

        btnAddPatientDetails.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {

                    PatientProfileDataController.getInstance().currentPatientlProfile=null;
                    Intent intent = new Intent(MyPatientActivity.this, PatientGeneralProfileActivity.class);
                    startActivity(intent);
                }
                else{
                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            dX = v.getX() - event.getRawX();
                            dY = v.getY() - event.getRawY();
                            lastAction = MotionEvent.ACTION_DOWN;
                            break;

                        case MotionEvent.ACTION_MOVE:
                            v.setY(event.getRawY() + dY);
                            v.setX(event.getRawX() + dX);
                            lastAction = MotionEvent.ACTION_MOVE;
                            break;

                        case MotionEvent.ACTION_UP:
                            if (lastAction == MotionEvent.ACTION_DOWN)
                                // Toast.makeText(DraggableView.this, "Clicked!", Toast.LENGTH_SHORT).show();
                                break;

                        default:
                            return false;
                    }

                }


                return true;
            }
        });


        TextView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getRecyclerView(PatientProfileDataController.getInstance().allPatientlProfile);
    }

    public void fetchServerApi() {
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        MedicalProfileModel medicalProfileModel= MedicalProfileDataController.getInstance().currentMedicalProfile;
        GetPatientsRequestModel patientsRequestModel = new GetPatientsRequestModel(medicalProfileModel.getMedical_person_id(),
                medicalProfileModel.getHospital_reg_number());
Log.e("medicalID","d"+medicalProfileModel.getMedical_person_id()+","+medicalProfileModel.getAccessToken()+
        ","+medicalProfileModel.getHospital_reg_number());
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ServerApi s = retrofit.create(ServerApi.class);
        Call<GetPatientsResponseModel> patientsListCall = s.getPatientsList(medicalProfileModel.getAccessToken(),
                patientsRequestModel);
        Log.e("ServiceResponse", "onResponse: " + patientsRequestModel.toString());
        patientsListCall.enqueue(new Callback<GetPatientsResponseModel>() {
            @Override
            public void onResponse(Call<GetPatientsResponseModel> call, Response<GetPatientsResponseModel> response) {
                alertDialog.dismiss();
                Log.e("response","ff"+response);
                if (response.body() != null) {
                    TrackInfoDataController.getInstance().deleteTrackData(PatientProfileDataController.getInstance().allPatientlProfile);
                    PatientProfileDataController.getInstance().deletePatientlProfileModelData(PatientProfileDataController.getInstance().allPatientlProfile);

                    List<PatientList> ListOfPatients = response.body().getPatientsInfo();
                    Log.e("patientinfo", "" + response.body().getPatientsInfo().size());

                    if (ListOfPatients == null) {
                        TextView medicDisc = findViewById(R.id.text_patientDisc);
                        medicDisc.setVisibility(View.VISIBLE);
                    }
                    for (int i = 0; i < ListOfPatients.size(); i++) {
                        Log.e("medicalrecoedid", "" + ListOfPatients.get(i).getMedical_record_id());
                        final JsonElement number = ListOfPatients.get(i).getPhoneNumber().get("phoneNumber");
                        final JsonElement numberCode = ListOfPatients.get(i).getPhoneNumber().get("countryCode");
                        PatientlProfileModel profileModel = new PatientlProfileModel();
                        profileModel.setMedicalProfileModel(MedicalProfileDataController.getInstance().currentMedicalProfile);
                        profileModel.setPatientId(ListOfPatients.get(i).getPatientID());
                        profileModel.setPhone_coutryCode(numberCode.getAsString());
                        profileModel.setPhoneNumber(number.getAsString());
                        profileModel.setAddress(ListOfPatients.get(i).getAddress());
                        profileModel.setMedicalRecordId(ListOfPatients.get(i).getMedical_record_id());

                        profileModel.setAge(ListOfPatients.get(i).getAge());
                        profileModel.setCity(ListOfPatients.get(i).getCity());
                        profileModel.setCountry(ListOfPatients.get(i).getCountry());
                        profileModel.setDob(ListOfPatients.get(i).getDob());
                        profileModel.setEmailId(ListOfPatients.get(i).getEmailID());
                        profileModel.setFirstName(ListOfPatients.get(i).getFirstName());
                        profileModel.setGender(ListOfPatients.get(i).getGender());
                        profileModel.setHospital_reg_number(MedicalProfileDataController.getInstance().currentMedicalProfile.getHospital_reg_number());
                        profileModel.setLastName(ListOfPatients.get(i).getLastName());
                        profileModel.setLatitude(String.valueOf(LocationTracker.getInstance().currentLocation.getLatitude()));
                        profileModel.setLongitude(String.valueOf(LocationTracker.getInstance().currentLocation.getLongitude()));
                        profileModel.setMedicalPerson_id(MedicalProfileDataController.getInstance().currentMedicalProfile.getMedical_person_id());
                        profileModel.setPostalCode(ListOfPatients.get(i).getPostalCode());
                        profileModel.setProfileByteArray(null);
                        profileModel.setState(ListOfPatients.get(i).getState());
                        profileModel.setProfilePic(ServerApi.img_home_url+ListOfPatients.get(i).getProfilePic());
                        Log.e("dob", "" + profileModel.getDob());

                        if(!ListOfPatients.get(i).getDob().isEmpty() && ListOfPatients.get(i).getDob() != null) {
                            if(ListOfPatients.get(i).getDob().contains("-")){
                                profileModel.setDob(ListOfPatients.get(i).getDob());
                            }else if (ListOfPatients.get(i).getDob().contains("/")){
                                profileModel.setDob(ListOfPatients.get(i).getDob());
                            }else{
                                try {
                                    Log.e("timestamp", "" + ListOfPatients.get(i).getDob());
                                    String date = PersonalInfoController.getInstance().convertTimestampTodate(ListOfPatients.get(i).getDob());
                                    profileModel.setDob(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if(PatientProfileDataController.getInstance().insertPatientlProfileData(profileModel)){
                            PatientProfileDataController.getInstance().fetchPatientlProfileData();
                            Log.e("listOfname", "" + PatientProfileDataController.getInstance().allPatientlProfile.size());
                        }

                        if (ListOfPatients.get(i).getTracking().size() > 0) {
                            List<TrackingServerObject> trackingList = ListOfPatients.get(i).getTracking();
                            Log.e("trackpatienid", "" + profileModel.getPatientId());
                            for(int index=0;index<trackingList.size();index++){
                                TrackingServerObject serverObject=trackingList.get(index);
                                TrackInfoModel trackInfoModel=new TrackInfoModel();
                                trackInfoModel.setPatientlProfileModel(profileModel);
                                trackInfoModel.setPatientId(profileModel.getPatientId());
                                trackInfoModel.setByWhom(serverObject.getByWhom());
                                trackInfoModel.setByWhomId(serverObject.getByWhomID());
                                Long l=Long.parseLong(serverObject.getDate())/1000;
                                trackInfoModel.setDate(String.valueOf(l));
                                if(TrackInfoDataController.getInstance().insertTrackData(trackInfoModel)){
                                   // TrackInfoDataController.getInstance().fetchTrackData(profileModel);
                                }
                            }
                        }
                    }
                    isFromStarting=false;
                    getRecyclerView(PatientProfileDataController.getInstance().allPatientlProfile);
                }
            }

            @Override
            public void onFailure(Call<GetPatientsResponseModel> call, Throwable t) {
                t.printStackTrace();
                alertDialog.dismiss();
                Toast.makeText(MyPatientActivity.this,"Please check your internet connection", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void loadSearchtext() {
        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateSearchResults(s.toString().toLowerCase());

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateSearchResults(s.toString().toLowerCase());

            }
        });
    }

    private void updateSearchResults(String text) {
        ArrayList<PatientlProfileModel> sortedArray = new ArrayList<>();
        for (PatientlProfileModel object : PatientProfileDataController.getInstance().allPatientlProfile) {
            if (object.getFirstName().toLowerCase().startsWith(text) || object.getLastName().toLowerCase().startsWith(text) || object.getPatientId().toLowerCase().startsWith(text) ) {
                sortedArray.add(object);
            }
        }
        getRecyclerView(sortedArray);
        patientAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        patientAdapter.notifyDataSetChanged();
    }

    public void getRecyclerView(ArrayList<PatientlProfileModel> lists) {
        if ( lists.isEmpty())
            txtPatientDisc.setVisibility(View.VISIBLE);
        else
            txtPatientDisc.setVisibility(View.GONE);

        patientAdapter = new PatientAdapter(MyPatientActivity.this, lists);
        recyclerView = findViewById(R.id.patient_view);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
       // recyclerView.setLayoutManager(new LinearLayoutManager(MyPatientActivity.this));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(patientAdapter);
        alertDialog.dismiss();
    }


    public class GetPatientsRequestModel {
        private String medical_personnel_id;
        private String hospital_reg_num;

        public GetPatientsRequestModel(String medical_personnel_id, String hospital_reg_num) {
            this.medical_personnel_id = medical_personnel_id;
            this.hospital_reg_num = hospital_reg_num;
        }

        public String getMedical_personnel_id() {
            return medical_personnel_id;
        }

        public void setMedical_personnel_id(String medical_personnel_id) {
            this.medical_personnel_id = medical_personnel_id;
        }

        public String getHospital_reg_num() {
            return hospital_reg_num;
        }

        public void setHospital_reg_num(String hospital_reg_num) {
            this.hospital_reg_num = hospital_reg_num;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.example_menu, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.e("fdfdf", "" + newText);
                    patientAdapter.getFilter().filter(newText);
                    return false;
                }
            });
        }
        return true;
    }
    public boolean isConn() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
    }
    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter = new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MyPatientActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);
    }
}
