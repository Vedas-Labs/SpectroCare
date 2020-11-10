package com.vedas.spectrocare.PatientModule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientAppointmentModule.AppointmentArrayModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.SelectUserActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import androidx.cardview.widget.CardView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppointmentCancelActivity extends AppCompatActivity {
    String docName, formattedDate,appointmentID;
    TextView txtNoLonger, txtUnable, txtUnsatisfied, txtOther, txtError;
    String reason;
    TextView txtDetails;
    RelativeLayout btnSubmit;
    int i;
    RefreshShowingDialog refreshShowingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_cancel);
        ButterKnife.bind(this);
        getData();
    }

    @OnClick(R.id.back)
    void backAction() {
        // TODO call server...
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AppointmentCancelActivity.this,PatientAppointmentsTabsActivity.class)
        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public void getData() {
        txtNoLonger = findViewById(R.id.txt_no_longer);
        txtUnable = findViewById(R.id.txt_unable);
        txtError = findViewById(R.id.txt_error);
        txtUnsatisfied = findViewById(R.id.txt_unsatisfied);
        txtOther = findViewById(R.id.txt_other);
        txtDetails = findViewById(R.id.txt_details);
        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkConnected()){
                    refreshShowingDialog.showAlert();
                    cancelAppointmentApi();
                    accessInterFace();

                }
                else{
                    refreshShowingDialog.hideRefreshDialog();
                    Toast.makeText(AppointmentCancelActivity.this, "Please check your connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        refreshShowingDialog = new RefreshShowingDialog(
                AppointmentCancelActivity.this);
        txtUnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                background(txtUnable, txtNoLonger, txtError, txtOther, txtUnsatisfied);
                reason = txtUnable.getText().toString();
            }
        });
        txtOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reason = txtOther.getText().toString();
                background(txtOther, txtNoLonger, txtError, txtUnable, txtUnsatisfied);
            }
        });
        txtUnsatisfied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reason = txtUnsatisfied.getText().toString();
                background(txtUnsatisfied, txtNoLonger, txtError, txtOther, txtUnable);
            }
        });
        txtError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reason = txtError.getText().toString();
                background(txtError, txtNoLonger, txtUnable, txtOther, txtUnsatisfied);
            }
        });
        txtNoLonger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reason = txtNoLonger.getText().toString();
                background(txtNoLonger, txtUnable, txtError, txtOther, txtUnsatisfied);
            }
        });

        Intent getDataIntent = getIntent();
        docName = getDataIntent.getStringExtra("docName");
        AppointmentArrayModel appointmentArrayModel = (AppointmentArrayModel) getDataIntent.getSerializableExtra("appointmentDetails");
        appointmentID = appointmentArrayModel.getAppointmentDetails().getAppointmentID();
        long ll = Long.parseLong(appointmentArrayModel.getAppointmentDetails().getAppointmentDate());
        Date currentDate = new Date(ll);
        SimpleDateFormat jdff = new SimpleDateFormat("yyyy-MM-dd");
        jdff.setTimeZone(TimeZone.getDefault());
        String java_date = jdff.format(currentDate);
        Log.e("cccc", "afa" + java_date);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat parseFormat = new SimpleDateFormat("dd  MMMM ");
        sdf.setTimeZone(TimeZone.getDefault());
        Date clickedDate = null;
        try {
            clickedDate = jdff.parse(java_date);

            if (i == 0) {

                formattedDate = parseFormat.format(clickedDate);
                // formattedDate = sdf.format(clickedDate);
                Log.e("forrr", "ff" + formattedDate);

            }

            txtDetails.setText("Your appointment with" + " " + docName + " on " + formattedDate + " "
                    + appointmentArrayModel.getAppointmentDetails().getAppointmentTime() + " will be cancelled ");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) AppointmentCancelActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public void background(TextView r1, TextView r2, TextView r3, TextView r4, TextView r5) {
        r1.setBackground(AppointmentCancelActivity.this.getResources().getDrawable(R.drawable.btn_bck_color));
        r1.setTextColor(Color.parseColor("#ffffff"));
        r2.setBackground(AppointmentCancelActivity.this.getResources().getDrawable(R.drawable.background_white));
        r2.setTextColor(Color.parseColor("#3E454C"));
        r3.setBackground(AppointmentCancelActivity.this.getResources().getDrawable(R.drawable.background_white));
        r3.setTextColor(Color.parseColor("#3E454C"));
        r4.setBackground(AppointmentCancelActivity.this.getResources().getDrawable(R.drawable.background_white));
        r4.setTextColor(Color.parseColor("#3E454C"));
        r5.setBackground(AppointmentCancelActivity.this.getResources().getDrawable(R.drawable.background_white));
        r5.setTextColor(Color.parseColor("#3E454C"));
    }
    public void cancelAppointmentApi(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
            jsonObject.put("appointmentID", appointmentID);
            jsonObject.put("byWhom", "Patient");
            jsonObject.put("byWhomID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            jsonObject.put("reason", reason);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject body = (JsonObject) jsonParser.parse(jsonObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.cancelPatientAppointment(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken()
                ,body),"cancelAppointment");

    }
    public void accessInterFace(){
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String opetation) {
                refreshShowingDialog.hideRefreshDialog();
                try {
                    Log.e("success","response"+jsonObject.getString("response"));
                    try {
                        Toast.makeText(AppointmentCancelActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failureCallBack(String failureMsg) {
                refreshShowingDialog.hideRefreshDialog();
                Log.e("error","response"+failureMsg);
            }
        });
    }
}
