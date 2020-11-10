package com.vedas.spectrocare.PatientMoreModule;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.Controllers.AppointmentsServerObjectDataController;
import com.vedas.spectrocare.Controllers.DoctorInfoServerObjectDataController;
import com.vedas.spectrocare.DataBase.AppointmentDataController;
import com.vedas.spectrocare.DataBase.DoctorInfoDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.Location.LocationTracker;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.ServerApiModel.AppointmentServerObjects;
import com.vedas.spectrocare.ServerApiModel.TrackingServerObject;
import com.vedas.spectrocare.activities.HomeActivity;
import com.vedas.spectrocare.activities.LoginActivity;
import com.vedas.spectrocare.activities.MedicalPersonaSignupView;
import com.vedas.spectrocare.activities.MedicalPersonalSignupPresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FeedbackActivity extends AppCompatActivity implements MedicalPersonaSignupView {
    RelativeLayout rl_back, rl_feedback;
    EditText ed_text;
    Button btn_submit;
    RefreshShowingDialog showingDialog;
    JsonObject gsonObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        loadIDS();
        accessInterfaceMethods();
        PatientLoginDataController.getInstance().fetchPatientlProfileData();
    }

    private void loadIDS() {
        showingDialog=new RefreshShowingDialog(FeedbackActivity.this);
        rl_back = findViewById(R.id.back);
        btn_submit = findViewById(R.id.btn_next);
        ed_text = findViewById(R.id.edittext);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadValidations();
            }
        });
    }

    private void loadValidations() {
        if (ed_text.getText().toString().length() > 0) {
            if (isNetworkAvailable()) {
                showingDialog.showAlert();
                loadJson();
            } else {
                dialogeforCheckavilability("Alert", "Please check your connection ", "Ok");
            }
        } else {
            dialogeforCheckavilability("Alert", "Please enter feedback message ", "Ok");
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(FeedbackActivity.this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter = new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(FeedbackActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);
    }
    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                if (curdOpetaton.equals("add")) {
                    Log.e("feeedbackresponse","call"+jsonObject.toString());
                    showingDialog.hideRefreshDialog();
                    ed_text.getText().clear();
                    Toast.makeText(getApplicationContext(), "Feedback has been received", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void failureCallBack(String failureMsg) {
                showingDialog.hideRefreshDialog();
                Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void loadJson() {
        JSONObject userObj = new JSONObject();
        try {
            userObj.put("userType", "Patient");
            userObj.put("userID",PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId() );
            userObj.put("userName", PatientLoginDataController.getInstance().currentPatientlProfile.getFirstName() );
            userObj.put("userEmailID",PatientLoginDataController.getInstance().currentPatientlProfile.getEmailId() );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject params = new JSONObject();
        try {
            params.put("source", "Mobile App");
            params.put("feedbackMessage", ed_text.getText().toString());
            params.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number() );
            params.put("userInfo", userObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
         gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.feedbackApi("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE1OTY0NTk2MjV9.PkoAZmMex8VVNaIf9WeiMfyKtm6VvDkOiT3vCAEbiPo",gsonObject), "add");
    }

}
