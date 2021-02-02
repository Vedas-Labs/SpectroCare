package com.vedas.spectrocare.PatientModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBaseModels.PatientModel;
import com.vedas.spectrocare.PatientNotificationModule.MyButtonClickListener;
import com.vedas.spectrocare.PatientNotificationModule.MySwipeHelper;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicationObject;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.MedicalPersonaSignupView;
import com.vedas.spectrocare.activities.MedicalPersonalSignupPresenter;
import com.vedas.spectrocare.patientModuleAdapter.PatientMedicationRecordAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PatientMedicationsActivity extends AppCompatActivity implements MedicalPersonaSignupView {
    RecyclerView medicationView;
    ImageView backImg;
    //public static boolean isFromMedication = false;
    PatientMedicationRecordAdapter recordAdapter;
    RefreshShowingDialog refreshShowingDialog;
    int deletePos = -1;
    EditText searchBar;
    ImageView img_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_medications);
        ButterKnife.bind(this);
        backImg = findViewById(R.id.img_back_arrow);
        searchBar = findViewById(R.id.edt_search);
        img_delete = findViewById(R.id.img_delete);

        refreshShowingDialog = new RefreshShowingDialog(PatientMedicationsActivity.this);
        loadClicks();
        loadrecyclerView(PatientMedicalRecordsController.getInstance().medicationArrayList);
        swipeMethod();
        loadSearchtext();
        accessInterfaceMethods();
        loadDeleteButton();
    }

    private void loadDeleteButton() {
        if (PatientMedicalRecordsController.getInstance().medicationArrayList.size() > 0) {
            img_delete.setVisibility(View.VISIBLE);
        } else {
            img_delete.setVisibility(View.GONE);
        }
    }

    private void loadClicks() {
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PatientMedicalRecordsController.getInstance().medicationArrayList.size() > 0) {
                    PatientMedicalRecordsController.getInstance().selectedMedication = PatientMedicalRecordsController.getInstance().medicationArrayList.get(PatientMedicalRecordsController.getInstance().medicationArrayList.size() - 1);
                    deleteAlert();
                }
            }
        });
    }

    private void loadrecyclerView(ArrayList<PatientMedicationObject> list) {
        medicationView = findViewById(R.id.doc_medication_view);
        medicationView.setLayoutManager(new LinearLayoutManager(this));
        recordAdapter = new PatientMedicationRecordAdapter(this, list);
        medicationView.setAdapter(recordAdapter);
    }

    @OnClick(R.id.img_add)
    void addAction() {
        PatientMedicalRecordsController.getInstance().isFromMedication = true;
        PatientMedicalRecordsController.getInstance().medicatioArrayObjectsList.clear();
        // PatientMedicalRecordsController.getInstance().medicationArrayList.clear();
        PatientMedicalRecordsController.getInstance().selectedMedication = null;
        startActivity(new Intent(getApplicationContext(), DiseasesListActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDeleteButton();
        accessInterfaceMethods();
        recordAdapter.notifyDataSetChanged();
    }

    private void loadSearchtext() {
        searchBar.addTextChangedListener(new TextWatcher() {
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
        ArrayList<PatientMedicationObject> sortedArray = new ArrayList<>();
        for (PatientMedicationObject object : PatientMedicalRecordsController.getInstance().medicationArrayList) {
            if (object.getDoctorName().toLowerCase().startsWith(text) || object.getIllnessMedicationID().toLowerCase().startsWith(text)) {
                sortedArray.add(object);
            }
        }
        loadrecyclerView(sortedArray);
        recordAdapter.notifyDataSetChanged();
    }

    private void swipeMethod() {
        MySwipeHelper mySwipeHelper = new MySwipeHelper(PatientMedicationsActivity.this, medicationView, 200) {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buffer) {
                buffer.add(new MyButton(PatientMedicationsActivity.this, "", 0, R.drawable.delete_sweep,
                        Color.parseColor("#FBF8F8"),
                        new MyButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                Log.e("checkk", "check" + pos);
                                deletePos = pos;
                                if (PatientMedicalRecordsController.getInstance().medicationArrayList.size() > 0) {
                                    PatientMedicalRecordsController.getInstance().selectedMedication = PatientMedicalRecordsController.getInstance().medicationArrayList.get(deletePos);
                                    if (isNetworkConnected()) {
                                        refreshShowingDialog.showAlert();
                                        deleteSingleMedicationRecord();
                                    } else {
                                        dialogeforCheckavilability("Error", "Please check internet connection", "ok");
                                    }
                                }
                            }
                        }));
            }
        };
    }

    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                if (curdOpetaton.equals("singleMedicationDelete")) {
                    Log.e("allergyresponse", "call" + jsonObject.toString());
                    try {
                        if (jsonObject.getString("response").equals("3")) {
                            Log.e("singleMedicationDelete", "call" + jsonObject.toString());
                           // Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            PatientMedicalRecordsController.getInstance().medicationArrayList.remove(deletePos);
                            //  PatientMedicalRecordsController.getInstance().medicationArrayList.remove(PatientMedicalRecordsController.getInstance().selectedMedication);
                            recordAdapter.notifyDataSetChanged();
                            refreshShowingDialog.hideRefreshDialog();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (curdOpetaton.equals("allMedicationDelete")) {
                    // Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    PatientMedicalRecordsController.getInstance().medicationArrayList.clear();
                    PatientMedicalRecordsController.getInstance().medicatioArrayObjectsList.clear();
                    loadDeleteButton();
                    recordAdapter.notifyDataSetChanged();
                    refreshShowingDialog.hideRefreshDialog();
                }
            }

            @Override
            public void failureCallBack(String failureMsg) {
                refreshShowingDialog.hideRefreshDialog();
                Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteSingleMedicationRecord() {
        PatientModel currentModel = PatientLoginDataController.getInstance().currentPatientlProfile;
        JSONObject params = new JSONObject();
        try {
            params.put("hospital_reg_num", currentModel.getHospital_reg_number());
            params.put("byWhom", "patient");
            params.put("byWhomID", currentModel.getPatientId());
            params.put("patientID", currentModel.getPatientId());
            params.put("medical_record_id", currentModel.getMedicalRecordId());
            params.put("illnessID", PatientMedicalRecordsController.getInstance().selectedMedication.getIllnessID());
            params.put("illnessMedicationID", PatientMedicalRecordsController.getInstance().selectedMedication.getIllnessMedicationID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.deletePatientSingleMedication(currentModel.getAccessToken(), gsonObject), "singleMedicationDelete");
    }

    public void deleteAllMedicationRecord() {
        PatientModel currentModel = PatientLoginDataController.getInstance().currentPatientlProfile;
        JSONObject params = new JSONObject();
        try {
            params.put("hospital_reg_num", currentModel.getHospital_reg_number());
            params.put("byWhom", "patient");
            params.put("byWhomID", currentModel.getPatientId());
            params.put("patientID", currentModel.getPatientId());
            params.put("medical_record_id", currentModel.getMedicalRecordId());
            params.put("illnessID", PatientMedicalRecordsController.getInstance().selectedMedication.getIllnessID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.deletePatientAllMedication(currentModel.getAccessToken(), gsonObject), "allMedicationDelete");
    }

    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter = new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PatientMedicationsActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);
    }

    private void deleteAlert() {
        final Dialog dialog = new Dialog(PatientMedicationsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_abort);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
        btnNo.setText("Cancel");
        btnYes.setText("Delete");
        dialog.show();

        TextView txt_title = dialog.findViewById(R.id.title);
        TextView txt_msg = dialog.findViewById(R.id.msg);
        TextView txt_msg1 = dialog.findViewById(R.id.msg1);

        txt_title.setText("Delete");
        txt_msg.setText("Are you sure you want to Delete");
        txt_msg1.setText("all records ?");

        RelativeLayout main = (RelativeLayout) dialog.findViewById(R.id.rl_main);
        RelativeLayout main1 = (RelativeLayout) dialog.findViewById(R.id.rl_main1);

        GradientDrawable drawable = (GradientDrawable) main.getBackground();
        drawable.setColor(getResources().getColor(R.color.colorWhite));

        GradientDrawable drawable1 = (GradientDrawable) main1.getBackground();
        drawable1.setColor(getResources().getColor(R.color.colorWhite));

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isNetworkConnected()) {
                    refreshShowingDialog.showAlert();
                    deleteAllMedicationRecord();
                } else {
                    dialogeforCheckavilability("Error", "Please check internet connection", "ok");
                }
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
