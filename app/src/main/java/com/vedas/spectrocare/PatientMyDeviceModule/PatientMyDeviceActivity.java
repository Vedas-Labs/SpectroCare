package com.vedas.spectrocare.PatientMyDeviceModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.PatientServerApiModel.PatientMyDevicesObject;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.MedicalPersonaSignupView;
import com.vedas.spectrocare.activities.MedicalPersonalSignupPresenter;
import com.vedas.spectrocare.patientModuleAdapter.PatientRecentDeviceAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PatientMyDeviceActivity extends AppCompatActivity implements MedicalPersonaSignupView {
    RecyclerView recentDevicesView, otherDevicesView;
    ImageView imgBack;
    RefreshShowingDialog refreshShowingDialog;
    PatientRecentDeviceAdapter deviceAdapter;
    OtherDevicesAdapter otherDevicesAdapter;
    int selectedPos=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_my_device);
        recentDevicesView = findViewById(R.id.recent_devices_view);
        otherDevicesView = findViewById(R.id.other_devices_view);
        imgBack = findViewById(R.id.img_back_arrow);
        refreshShowingDialog = new RefreshShowingDialog(PatientMyDeviceActivity.this);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        deviceAdapter = new PatientRecentDeviceAdapter(this);
        recentDevicesView.setLayoutManager(new LinearLayoutManager(this));
        recentDevicesView.setAdapter(deviceAdapter);

        otherDevicesView.setLayoutManager(new LinearLayoutManager(this));
        otherDevicesAdapter = new OtherDevicesAdapter();
        otherDevicesView.setAdapter(otherDevicesAdapter);

        accessInterfaceMethods();
        if (isNetworkConnected()) {
            refreshShowingDialog.showAlert();
            fetchDevicesApi();
        } else {
            dialogeforCheckavilability("Error", "Please check internet connection", "ok");
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        accessInterfaceMethods();
        if(PatientMedicalRecordsController.getInstance().selectedDevice!=null){
            if (isNetworkConnected()) {
                fetchDevicesApi();
            } else {
                dialogeforCheckavilability("Error", "Please check internet connection", "ok");
            }
            deviceAdapter.notifyDataSetChanged();
            otherDevicesAdapter.notifyDataSetChanged();
            PatientMedicalRecordsController.getInstance().selectedDevice=null;
        }
    }
    public class OtherDevicesAdapter extends RecyclerView.Adapter<OtherDevicesAdapter.OtherDeviceHolder> {
        @NonNull
        @Override
        public OtherDevicesAdapter.OtherDeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View otherDevicesView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_devices_recyclerview, parent, false);
            return new OtherDeviceHolder(otherDevicesView);
        }
        @Override
        public void onBindViewHolder(@NonNull OtherDevicesAdapter.OtherDeviceHolder holder, int position) {
            PatientMyDevicesObject obj = PatientMedicalRecordsController.getInstance().otherDevicesArrayList.get(position);
            holder.txt_id.setText(obj.getDeviceID());
            holder.txt_name.setText(obj.getDeviceName());

            if(selectedPos==position){
                if (isNetworkConnected()) {
                    holder.progressBar.setVisibility(View.VISIBLE);
                    PatientMedicalRecordsController.getInstance().selectedDevice = PatientMedicalRecordsController.getInstance().otherDevicesArrayList.get(selectedPos);
                    selectedPos=-1;
                    addDevicesApi(PatientMedicalRecordsController.getInstance().selectedDevice);
                }
            }else {
                holder.progressBar.setVisibility(View.GONE);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedPos != position){
                        selectedPos=position;
                        notifyDataSetChanged();
                    }else{
                        selectedPos=-1;
                        notifyDataSetChanged();
                    }
                }
            });
        }
        @Override
        public int getItemCount() {
            if (PatientMedicalRecordsController.getInstance().otherDevicesArrayList.size() > 0) {
                return PatientMedicalRecordsController.getInstance().otherDevicesArrayList.size();
            } else {
                return 0;
            }
        }
        public class OtherDeviceHolder extends RecyclerView.ViewHolder {
            RelativeLayout layout;
            TextView txt_id, txt_name;
            ProgressBar progressBar;
            public OtherDeviceHolder(@NonNull View itemView) {
                super(itemView);
                layout = itemView.findViewById(R.id.layout_main);
                txt_id = itemView.findViewById(R.id.txt_id);
                txt_name = itemView.findViewById(R.id.txt_name);
                progressBar = itemView.findViewById(R.id.progressBar);
            }
        }
    }
    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.
                ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                refreshShowingDialog.hideRefreshDialog();
                try {
                    if (jsonObject.getString("response").equals("3")) {
                        Log.e("deviceid", "call" +curdOpetaton.toString());
                        if (curdOpetaton.equals("devicesFetch")) {
                            PatientMedicalRecordsController.getInstance().myDevicesArrayList.clear();
                            PatientMedicalRecordsController.getInstance().otherDevicesArrayList.clear();
                            JSONArray myDevicesArray = jsonObject.getJSONArray("myDevices");
                            JSONArray otherDevicesArray = jsonObject.getJSONArray("otherDevices");
                            if (myDevicesArray.length() > 0) {
                                for (int i = 0; i < myDevicesArray.length(); i++) {
                                    JSONObject obj = myDevicesArray.getJSONObject(i);
                                    Gson gson = new Gson();
                                    PatientMyDevicesObject patientMyDevicesObject = gson.fromJson(obj.toString(), PatientMyDevicesObject.class);
                                    PatientMedicalRecordsController.getInstance().myDevicesArrayList.add(patientMyDevicesObject);
                                }
                            }
                            if (otherDevicesArray.length() > 0) {
                                for (int i = 0; i < otherDevicesArray.length(); i++) {
                                    JSONObject obj = otherDevicesArray.getJSONObject(i);
                                    Gson gson = new Gson();
                                    PatientMyDevicesObject patientMyDevicesObject = gson.fromJson(obj.toString(), PatientMyDevicesObject.class);
                                    Log.e("deviceid", "call" + patientMyDevicesObject.getDeviceHardwareVersion());
                                    PatientMedicalRecordsController.getInstance().otherDevicesArrayList.add(patientMyDevicesObject);
                                }
                            }
                            deviceAdapter.notifyDataSetChanged();
                            otherDevicesAdapter.notifyDataSetChanged();
                        }if (curdOpetaton.equals("addDevice")) {
                            PatientMedicalRecordsController.getInstance().myDevicesArrayList.add(PatientMedicalRecordsController.getInstance().selectedDevice);
                            deviceAdapter.notifyDataSetChanged();

                            int index = PatientMedicalRecordsController.getInstance().otherDevicesArrayList.indexOf(PatientMedicalRecordsController.getInstance().selectedDevice);
                            PatientMedicalRecordsController.getInstance().otherDevicesArrayList.remove(index);
                            otherDevicesAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void failureCallBack(String failureMsg) {
                refreshShowingDialog.hideRefreshDialog();
                Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public void fetchDevicesApi() {
        JSONObject fetchObject = new JSONObject();
        try {
            fetchObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile
                    .getHospital_reg_number());
            fetchObject.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(fetchObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(
                ApiCallDataController.getInstance().serverJsonApi.
                        fetchPatientDevicesApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "devicesFetch");
    }
    public void addDevicesApi(PatientMyDevicesObject object) {
        JSONObject fetchObject = new JSONObject();
        try {
            fetchObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
            fetchObject.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            fetchObject.put("deviceID", object.getDeviceID());
            fetchObject.put("deviceModelNumber", object.getDeviceModelNumber());
            fetchObject.put("deviceName", object.getDeviceName());
            fetchObject.put("deviceSerialNumber", object.getDeviceSerialNumber());
            fetchObject.put("deviceHardwareVersion", object.getDeviceHardwareVersion());
            fetchObject.put("deviceSoftwareVersion", object.getDeviceSoftwareVersion());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(fetchObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(
                ApiCallDataController.getInstance().serverJsonApi.
                        addPatientDevicesApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "addDevice");

    }
    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter = new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PatientMyDeviceActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);
    }
}
