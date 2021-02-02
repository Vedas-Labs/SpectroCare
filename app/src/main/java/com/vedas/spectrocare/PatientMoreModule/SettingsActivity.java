package com.vedas.spectrocare.PatientMoreModule;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.PatientModule.DateTimeActivity;
import com.vedas.spectrocare.PatientMoreModule.PatientLanguageActivity;
/*import com.vedas.spectrocare.PatientServerObjects.AppSettingsModel;
import com.vedas.spectrocare.PatientServerObjects.NotificationItemModel;*/
import com.vedas.spectrocare.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {
    RefreshShowingDialog showingDialog;
    public static SharedPreferences.Editor editor;
    TextView txt_notification,txt_timeFormate,txt_save,txt_language;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        showingDialog = new RefreshShowingDialog(SettingsActivity.this);
        accessInterfaceMethods();
        loadDataIntoPreferences();
        loadIds();

    }
    @Override
    protected void onResume() {
        super.onResume();
        readData();
    }
    private void loadIds(){
        txt_notification=findViewById(R.id.notify_text);
        txt_timeFormate=findViewById(R.id.txt_formate);
        txt_save=findViewById(R.id.save);
        txt_language=findViewById(R.id.lang_text);

         spinner = (Spinner) findViewById(R.id.spinner);
        List<String> categories = new ArrayList<>();

        categories.add("Metric");
        categories.add("Imperial");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                editor.putString("units",item);
                editor.commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    @OnClick(R.id.save) void saveaction() {
        // TODO call server...
        showingDialog.showAlert();
        loadSettingsApi();
    }
    @OnClick(R.id.languages) void languagesaction() {
        // TODO call server...
        startActivity(new Intent(getApplicationContext(), PatientLanguageActivity.class));
    }
    @OnClick(R.id.notification) void action() {
        // TODO call server...
        startActivity(new Intent(getApplicationContext(),NotificationActivity.class));
    }
    @OnClick(R.id.rl_datetime) void dateAction() {
        // TODO call server...
        startActivity(new Intent(getApplicationContext(), DateTimeActivity.class));
    }
    @OnClick(R.id.back) void backAction() {
        //clearSharedPreferenceData();
        // TODO call server...
        onBackPressed();
    }
    @OnClick(R.id.rlabout) void aboutAction() {
        // TODO call server...
        startActivity(new Intent(getApplicationContext(),AboutActivity.class));
    }
    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                if (curdOpetaton.equals("add")) {
                    Log.e("settings response","call"+jsonObject.toString());
                    showingDialog.hideRefreshDialog();
                    Toast.makeText(getApplicationContext(), "Patient App settings has been updated", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void failureCallBack(String failureMsg) {
                showingDialog.hideRefreshDialog();
                Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadSettingsApi(){
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean notification = sharedPreferences.getBoolean("allnotification", false);
        boolean appointment = sharedPreferences.getBoolean("appointment", false);
        boolean chat = sharedPreferences.getBoolean("chat", false);
        boolean bill = sharedPreferences.getBoolean("bill", false);
        String region = sharedPreferences.getString("region", "India");
        String dateFormate = sharedPreferences.getString("dateFormat", "YYYY/MM/dd");
        String language = sharedPreferences.getString("language", "English");
        /*  boolean formate = sharedPreferences.getBoolean("is24Hour", false);
            String region = sharedPreferences.getString("region", "India");
        */
        JSONObject params = new JSONObject();
        try {
            JSONObject jo = new JSONObject();
            jo.put("name", "Appointment");
            jo.put("isEnabled", appointment);

            JSONObject jo1 = new JSONObject();
            jo1.put("name", "Chat & Message");
            jo1.put("isEnabled", chat);

            JSONObject jo2 = new JSONObject();
            jo2.put("name", "Bill & Payment");
            jo2.put("isEnabled", bill);

            JSONArray ja = new JSONArray();
            ja.put(jo);
            ja.put(jo1);
            ja.put(jo2);

            JSONObject appSettingsObj = new JSONObject();
            appSettingsObj.put("dateFormat", dateFormate);
            appSettingsObj.put("timeFormatType", txt_timeFormate.getText().toString());
            appSettingsObj.put("timeFormat", "HH:MM a");
            appSettingsObj.put("region", region);
            appSettingsObj.put("language", language);
            appSettingsObj.put("unit", spinner.getSelectedItem().toString());
            appSettingsObj.put("notification", notification);
            appSettingsObj.put("notificationItems", ja);

            params.put("userID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number() );
            params.put("appSettings", appSettingsObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.settingsApi("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE1OTY0NTk2MjV9.PkoAZmMex8VVNaIf9WeiMfyKtm6VvDkOiT3vCAEbiPo",gsonObject), "add");
    }

    SharedPreferences sharedpreferences;
    private void loadDataIntoPreferences(){
        sharedpreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
    }
    public   void clearSettingsSharedPreferenceData(){
        SharedPreferences preferences =getSharedPreferences("settings",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
    private void readData(){
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean notification = sharedPreferences.getBoolean("allnotification", false);
        boolean formate = sharedPreferences.getBoolean("is24Hour", false);
        String units = sharedPreferences.getString("units", " ");
        String language = sharedPreferences.getString("language", "English");

        txt_language.setText(""+language);

        if(!units.equals(" ")){
            if(units.contains("Metric")){
                spinner.setSelection(0);
            }else{
                spinner.setSelection(1);
            }
        }
        if(notification){
           txt_notification.setText("On");
       }else {
           txt_notification.setText("Off");
       }

        if(formate){
            txt_timeFormate.setText("24-Hour");
        }else {
            txt_timeFormate.setText("12-Hour");
        }
    }
}
