package com.vedas.spectrocare.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.Controllers.IllnessServerObjectDataController;
import com.vedas.spectrocare.DataBase.AllergyDataController;
import com.vedas.spectrocare.DataBase.FamilyHistoryDataController;
import com.vedas.spectrocare.DataBase.IllnessDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBaseModels.IllnessRecordModel;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApiModel.IllnessServerObject;
import com.vedas.spectrocare.ServerApiModel.TrackingServerObject;
import com.vedas.spectrocare.SingleTapDetector;
import com.vedas.spectrocare.adapter.MedicalHistoryAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
public class MedicalHistoryActivity extends AppCompatActivity implements MedicalPersonaSignupView{
    ArrayList<IllnessRecordModel> medicalHistoryArrayList = new ArrayList<>();
    ImageView imgBackBtn;
    FloatingActionButton btnAdd;
    ImageView imgView;
    RecyclerView medicalRecordListView;
    TextView dicsIllness;
    String symptoms, currentStatus, currentIllness, description;
    BottomSheetDialog dialog;
    JSONObject params;
    private  float dX,dY;
    int lastAction;
    GestureDetector gestureDetector;
    public static  RefreshShowingDialog showingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history);
        btnAdd = findViewById(R.id.add_medical_record);
        dicsIllness =findViewById(R.id.text_medical_disc);
        gestureDetector = new GestureDetector(this, new SingleTapDetector());

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                bottomSheet();
            }
        });
        btnAdd.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    bottomSheet();
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

        imgBackBtn = findViewById(R.id.img_back);
        showingDialog=new RefreshShowingDialog(MedicalHistoryActivity.this);
        medicalRecordListView = findViewById(R.id.medical_record_recycler_view);

        imgBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if(isConn()){
            showingDialog.showAlert();
            IllnessServerObjectDataController.getInstance().illnessFetchApiCall();

        }
        loadRecyclerview();
        accessInterfaceMethods();

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void bottomSheet(){
        View dialogFamilyView = getLayoutInflater().inflate(R.layout.medical_record_bottpm_sheet, null);
        dialog = new BottomSheetDialog(Objects.requireNonNull(MedicalHistoryActivity.this), R.style.BottomSheetDialogTheme);
        dialog.setContentView(dialogFamilyView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        imgView= dialog.findViewById(R.id.img_close);
        final EditText edtSymptoms = dialog.findViewById(R.id.edt_symptoms);
        final EditText edtCurrentStatus = dialog.findViewById(R.id.edt_current_status);
        final EditText edtDescription = dialog.findViewById(R.id.edt_add_medical_description);
        Button addMedicalRecordDetails = dialog.findViewById(R.id.btn_add_medical_record);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        addMedicalRecordDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                symptoms = edtSymptoms.getText().toString();
                description = edtDescription.getText().toString();
                currentStatus = edtCurrentStatus.getText().toString();
                validationsOfMedicalHistoryRecords();
            }
        });
        dialog.show();
    }
    public void validationsOfMedicalHistoryRecords(){
        if (symptoms.isEmpty())
            dialogeforCheckavilability("Error", "Please select illness symptoms", "Ok");
        else  if (currentStatus.isEmpty())
            dialogeforCheckavilability("Error", "Please enter current status ", "Ok");
/*
        else if (description.isEmpty())
            dialogeforCheckavilability("Error", "Please enter description ", "Ok");
*/
        else{
            if (isConn()) {
                showingDialog.showAlert();
                processJsonParams();
            } else {
                dialogeforCheckavilability("Error", "No Internet connection", "Ok");
            }
        }
    }
    private void loadRecyclerview(){
        medicalHistoryArrayList= IllnessDataController.getInstance().fetchIllnesData(PatientProfileDataController.getInstance().currentPatientlProfile);
        MedicalHistoryAdapter medicalHistoryAdapterRecordAdapter = new MedicalHistoryAdapter(this, medicalHistoryArrayList);
        //medicalRecordListView.setHasFixedSize(true);
        medicalRecordListView.setLayoutManager(new LinearLayoutManager(this));
        if (!medicalHistoryArrayList.isEmpty()){
            dicsIllness.setVisibility(View.GONE);
        }else {
            dicsIllness.setVisibility(View.VISIBLE);
        }

        medicalRecordListView.setAdapter(medicalHistoryAdapterRecordAdapter);

    }
    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                if (curdOpetaton.equals("fetch")) {
                    Log.e("allergies", "call" + jsonObject.toString());
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("illnessRecords");
                        IllnessDataController.getInstance().deleteIllnesData(PatientProfileDataController.getInstance().currentPatientlProfile);
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Gson gson = new Gson();
                                IllnessServerObject userIdentifier = gson.fromJson(jsonArray.getJSONObject(i).toString(), IllnessServerObject.class);
                                ArrayList<TrackingServerObject> trackArray = userIdentifier.getTracking();
                                Log.e("trackArray", "call" + trackArray.size());
                                IllnessServerObjectDataController.getInstance().processAndfetchIllnessData(userIdentifier, trackArray);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (curdOpetaton.equals("insert")) {
                    Gson gson = new Gson();
                    IllnessServerObject userIdentifier = gson.fromJson(params.toString(), IllnessServerObject.class);
                    try {
                        Log.e("familydata", "call" + jsonObject.getString("illnessID"));
                        userIdentifier.setIllnessID(jsonObject.getString("illnessID"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    IllnessServerObjectDataController.getInstance().processAddIllnessData(userIdentifier);
                    dialog.dismiss();
                } else if (curdOpetaton.equals("update")) {
                    Gson gson = new Gson();
                    IllnessServerObject userIdentifier = gson.fromJson(IllnessServerObjectDataController.getInstance().params.toString(), IllnessServerObject.class);
                    IllnessServerObjectDataController.getInstance().processIllnessgyupdateData(userIdentifier);
                    MedicalHistoryAdapter.dialog.dismiss();
                } else if (curdOpetaton.equals("delete")) {
                    IllnessDataController.getInstance().deleteIllnessRecordModelData(PatientProfileDataController.getInstance().currentPatientlProfile, IllnessDataController.getInstance().currentIllnessRecordModel);
                }
                showingDialog.hideRefreshDialog();
                loadRecyclerview();
            }

            @Override
            public void failureCallBack(String failureMsg) {
                showingDialog.hideRefreshDialog();
                Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void processJsonParams(){
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        params = new JSONObject();
        try {
            params.put("hospital_reg_num", currentMedical.getHospital_reg_number());
            params.put("patientID", PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("medical_record_id", PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
            params.put("illnessCondition","fever");
            params.put("symptoms",symptoms );
            params.put("currentStatus", currentStatus);
            params.put("description",description );
            params.put("isCurrentIllness","true");
            params.put("byWhom","medical personnel");
            params.put("byWhomID",currentMedical.getMedical_person_id());
            params.put("startDate","1010570758000");
            params.put("endDate","1010570758000");
           // params.put("medical_personnel_id", currentMedical.getMedical_person_id());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());

        ApiCallDataController.getInstance().loadServerApiCall(
                ApiCallDataController.getInstance().serverApi.addMedicalIllness(currentMedical.getAccessToken(), gsonObject), "insert");

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
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MedicalHistoryActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);

    }

}
