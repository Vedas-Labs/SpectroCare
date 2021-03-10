package com.vedas.spectrocare.PatientModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientDocResponseModel.TrackingModel;
import com.vedas.spectrocare.PatientServerApiModel.AllergyListObject;
import com.vedas.spectrocare.PatientServerApiModel.AllergyObject;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.MedicalPersonaSignupView;
import com.vedas.spectrocare.activities.MedicalPersonalSignupPresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddPatientAllergyActivity extends AppCompatActivity implements MedicalPersonaSignupView {
    String[] allergyArrayNames;
    TextView txt_allergyType;
    Spinner allergySpinner;
    ArrayAdapter<String> dataAdapter;
    CardView rl_recyclerView;
    PatientAllergyAdapter allergyAdapter;
    RecyclerView recyclerView;
    ArrayList<String> typeList = new ArrayList<>();
    ArrayList<TrackingModel> trackingModelList = new ArrayList<>();
    Button btn_save;
    int selectedPos = -1;
    EditText ed_allegyName, ed_allegyNote;
    RefreshShowingDialog refreshShowingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient_allergy);
        ButterKnife.bind(this);
        Casting();
        ed_allegyName.setSelection(ed_allegyName.length());
        ed_allegyNote.setSelection(ed_allegyNote.length());
        accessInterfaceMethods();
        //fetchAllergiesApi();
    }
    @OnClick(R.id.btn_save_changes)
    void saveaction() {
        Log.e("adaasfds", "adfaf");
        validations();
    }
    public void Casting() {
        refreshShowingDialog = new RefreshShowingDialog(AddPatientAllergyActivity.this);
        rl_recyclerView = findViewById(R.id.rl);
        recyclerView = findViewById(R.id.recycler_view);
        btn_save = findViewById(R.id.btn_save_changes);
        ed_allegyName = findViewById(R.id.edt_allergy_name);
        ed_allegyNote = findViewById(R.id.edt_allergy_note);

        typeList.add("Food");
        typeList.add("Medicine");
        typeList.add("Others");

        rl_recyclerView = findViewById(R.id.rl);
        recyclerView = findViewById(R.id.recycler_view);
        allergyAdapter = new PatientAllergyAdapter(AddPatientAllergyActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(AddPatientAllergyActivity.this));
        recyclerView.setAdapter(allergyAdapter);

        txt_allergyType = findViewById(R.id.edt_type_of_allergy);
        allergyArrayNames = new String[]{"Food", "Medicine", "Others"};
        allergySpinner = findViewById(R.id.spinner_allergy);
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allergyArrayNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        allergySpinner.setAdapter(dataAdapter);

        txt_allergyType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_recyclerView.setVisibility(View.VISIBLE);
            }
        });
        ImageView imgBack = findViewById(R.id.img_back_arrow);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loadCurrentObject();
    }
    private void loadCurrentObject(){
        if(PatientMedicalRecordsController.getInstance().selectedObject!=null){
            AllergyListObject allergyListObject=PatientMedicalRecordsController.getInstance().selectedObject;
            ed_allegyName.setText(allergyListObject.getName());
            ed_allegyNote.setText(allergyListObject.getNote());
            txt_allergyType.setText("");
        }
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                if (curdOpetaton.equals("add")) {
                    try {
                        if (jsonObject.getString("response").equals("3")) {
                            Log.e("allergyresponse", "call" + jsonObject.toString());
                            refreshShowingDialog.hideRefreshDialog();
                            Log.e("sdmasdadasd","call");
                            if(PatientMedicalRecordsController.getInstance().selectedPos !=-1 && PatientMedicalRecordsController.getInstance().selectedObject != null){
                                AllergyListObject noteObj= PatientMedicalRecordsController.getInstance().noteallergyArray.get(PatientMedicalRecordsController.getInstance().selectedPos);
                                noteObj.setName(ed_allegyName.getText().toString());
                                noteObj.setNote(ed_allegyNote.getText().toString());
                                PatientMedicalRecordsController.getInstance().noteallergyArray.set(PatientMedicalRecordsController.getInstance().selectedPos,noteObj);
                            }else {
                                AllergyListObject listObject = new AllergyListObject();// for list name note array
                                listObject.setName(ed_allegyName.getText().toString());
                                listObject.setNote(ed_allegyNote.getText().toString());
                                PatientMedicalRecordsController.getInstance().noteallergyArray.add(listObject);

                                AllergyObject allergieModel = new AllergyObject();
                                allergieModel.setHospital_reg_num(PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
                                allergieModel.setMedical_record_id(PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
                                allergieModel.setByWhom("Patient");
                                allergieModel.setByWhomID(PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
                                allergieModel.setPatientID(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
                                allergieModel.setAllergies(PatientMedicalRecordsController.getInstance().noteallergyArray);
                                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                allergieModel.setCreatedDate(String.valueOf(timestamp.getTime()));
                                TrackingModel trackingModel = new TrackingModel();
                                trackingModel.setInfo("Allergy recorded added");
                                trackingModel.setDate(String.valueOf(timestamp.getTime()));
                                trackingModel.setByWhomID(PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
                                trackingModel.setByWhom("Patient");
                                PatientMedicalRecordsController.getInstance().allergyObjectArrayList.get(0).getTrackingList().add(trackingModel);

                                PatientMedicalRecordsController.getInstance().allergyObjectArrayList.add(allergieModel);

                                if (PatientMedicalRecordsController.getInstance().allergyObjectArrayList.size() == 0) {
                                    allergieModel.setAllergy_record_id(jsonObject.getString("allergy_record_id"));
                                }
                            }
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                          //  finish();
                            startActivity(new Intent(getApplicationContext(),PatientMedicalHistoryActivity.class)
                                    .putExtra("allergy","allergy"));

                        }else{
                           Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    refreshShowingDialog.hideRefreshDialog();
                }
            }
            @Override
            public void failureCallBack(String failureMsg) {
                refreshShowingDialog.hideRefreshDialog();
                //Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void validations() {
        /*if (txt_allergyType.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please select allergy type", "ok");
        } else*/ if (ed_allegyName.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please enter allergy name", "ok");
        } else if (ed_allegyNote.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please enter note", "ok");
        } else {
            if (isNetworkConnected()) {
                refreshShowingDialog.showAlert();
                loadAddAllergiesApi();
            } else {
                dialogeforCheckavilability("Error", "Please check internet connection", "ok");
            }
        }
    }

    private void loadAddAllergiesApi() {
        JSONObject params = new JSONObject();
        JSONObject notecurrentObject = new JSONObject();
        JSONArray noteArray = new JSONArray();
        try {
            if(PatientMedicalRecordsController.getInstance().selectedPos !=-1 && PatientMedicalRecordsController.getInstance().selectedObject != null){
                AllergyListObject noteObj= PatientMedicalRecordsController.getInstance().noteallergyArray.get(PatientMedicalRecordsController.getInstance().selectedPos);
                noteObj.setName(ed_allegyName.getText().toString());
                noteObj.setNote(ed_allegyNote.getText().toString());
                PatientMedicalRecordsController.getInstance().noteallergyArray.set(PatientMedicalRecordsController.getInstance().selectedPos,noteObj);
            }else {
                notecurrentObject.put("name", ed_allegyName.getText().toString());
                notecurrentObject.put("note", ed_allegyNote.getText().toString());
                noteArray.put(notecurrentObject);
            }
            if(PatientMedicalRecordsController.getInstance().allergyObjectArrayList.size()>0){
                if(PatientMedicalRecordsController.getInstance().noteallergyArray.size()>0){
                    for(int i = 0; i<PatientMedicalRecordsController.getInstance().noteallergyArray.size(); i++){
                        AllergyListObject noteList=PatientMedicalRecordsController.getInstance().noteallergyArray.get(i);
                        JSONObject noteSingleObject = new JSONObject();
                        noteSingleObject.put("name", noteList.getName());
                        noteSingleObject.put("note", noteList.getNote());
                        noteArray.put(noteSingleObject);
                    }
                }
                params.put("allergy_record_id", PatientMedicalRecordsController.getInstance().allergyObjectArrayList.get(0).getAllergy_record_id());
            }
            Log.e("noteObjectentire", "onResponse: " + noteArray.toString());

            params.put("byWhom", "patient");
            params.put("byWhomID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("medical_record_id", PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
            params.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
            params.put("allergies", noteArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.addAlergiesApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "add");
    }
    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter = new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AddPatientAllergyActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);
    }
    /* public void clickListener() {
         allergySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 String item = parent.getItemAtPosition(position).toString();
                 allergyType.setText(item);
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });

     }*/
    public class PatientAllergyAdapter extends RecyclerView.Adapter<PatientAllergyAdapter.AllergyHolder> {
        Context context;

        public PatientAllergyAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public AllergyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View allergyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_text, parent, false);
            return new AllergyHolder(allergyView);
        }

        @Override
        public void onBindViewHolder(@NonNull AllergyHolder holder, int position) {
            holder.allergyName.setText(typeList.get(position));
            if (selectedPos == position) {
                holder.relativeLayout.setBackgroundColor(Color.parseColor("#E9F9FB"));
                txt_allergyType.setText(typeList.get(selectedPos));
                rl_recyclerView.setVisibility(View.GONE);
            } else {
                holder.relativeLayout.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedPos != position) {
                        selectedPos = position;
                        notifyDataSetChanged();
                    } else {
                        selectedPos = -1;
                        notifyDataSetChanged();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return typeList.size();
        }

        public class AllergyHolder extends RecyclerView.ViewHolder {
            TextView allergyName;
            RelativeLayout relativeLayout;

            public AllergyHolder(@NonNull View itemView) {
                super(itemView);
                relativeLayout = itemView.findViewById(R.id.layout_status);
                allergyName = itemView.findViewById(R.id.item_spinner);
            }
        }
    }

}
