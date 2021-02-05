package com.vedas.spectrocare.PatientModule;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBaseModels.PatientModel;
import com.vedas.spectrocare.PatientAppointmentModule.AppointmentArrayModel;
import com.vedas.spectrocare.PatientAppointmentModule.PatientAppointmentsDataController;
import com.vedas.spectrocare.PatientMoreModule.FeedbackActivity;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.activities.MedicalPersonaSignupView;
import com.vedas.spectrocare.activities.MedicalPersonalSignupPresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DoctorRatingActivity extends AppCompatActivity implements MedicalPersonaSignupView, RatingBar.OnRatingBarChangeListener {
    RefreshShowingDialog showingDialog;
    EditText ed_comment;
    TextView doctorName, doctorDept;
    ImageView profilePic;
    Button submit;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_rating);
        showingDialog = new RefreshShowingDialog(DoctorRatingActivity.this);
        loadValidations();
        accessInterfaceMethod();
    }

    private void loadValidations() {
        ed_comment = findViewById(R.id.edittext);
        doctorName = findViewById(R.id.txt_doc_name);
        doctorDept = findViewById(R.id.txt_doc_prof);
        profilePic = findViewById(R.id.img_doc);
        submit = findViewById(R.id.btn_submit);
        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("rating", "call" + ratingBar.getRating());
                submitAction();
            }
        });
        loadDoctorData();
    }

    // implement abstract method onRatingChanged
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        ratingBar.setRating(rating);
        Log.e("rating", "call" + rating);
    }

    private void loadDoctorData() {
        if (PatientMedicalRecordsController.getInstance().selectedappointmnetModel != null) {
            AppointmentArrayModel model = PatientMedicalRecordsController.getInstance().selectedappointmnetModel;
            doctorName.setText("DR. " + model.getDoctorDetails().getProfile().getUserProfile().getFirstName() + " " +
                    model.getDoctorDetails().getProfile().getUserProfile().getLastName());
            doctorDept.setText(model.getDoctorDetails().getProfile().getUserProfile().getDepartment());
            if (!model.getDoctorDetails().getProfile().getUserProfile().getProfilePic().isEmpty())
                Picasso.get().load(ServerApi.img_home_url + model.getDoctorDetails().getProfile().getUserProfile().getProfilePic()).placeholder(R.drawable.image).into(profilePic);

        }
    }

    private void submitAction() {
        if (ed_comment.getText().toString().length() > 0) {
            if(ratingBar.getRating()>0.0) {
                if (isNetworkAvailable()) {
                    showingDialog.showAlert();
                    addDoctorReviewRatingsByPAtient();
                } else {
                    dialogeforCheckavilability("Alert", "Please check your connection ", "Ok");
                }
            }else {
                dialogeforCheckavilability("Alert", "Please select the rating", "Ok");
            }
        } else {
            dialogeforCheckavilability("Alert", "Please enter comment message ", "Ok");
        }
    }

    public void addDoctorReviewRatingsByPAtient() {
        PatientModel currentModel = PatientLoginDataController.getInstance().currentPatientlProfile;
        AppointmentArrayModel arrayModel = PatientMedicalRecordsController.getInstance().selectedappointmnetModel;
        JSONObject params = new JSONObject();
        JSONObject reviewparams = new JSONObject();
        try {
            params.put("hospital_reg_num", currentModel.getHospital_reg_number());
            params.put("patientID", currentModel.getPatientId());
            params.put("medical_personnel_id", arrayModel.getDoctorMedicalPersonnelID());

            reviewparams.put("reviewername", currentModel.getFirstName() + " " + currentModel.getLastName());
            reviewparams.put("reviewerID", currentModel.getPatientId());
            reviewparams.put("ratings", ratingBar.getRating());
            reviewparams.put("comment", ed_comment.getText().toString());
            reviewparams.put("appointmentID", arrayModel.getAppointmentDetails().getAppointmentID());

            params.put("customerReview", reviewparams);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.addDoctorReview(currentModel.getAccessToken(), gsonObject), "addDoctorReview");
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(DoctorRatingActivity.this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter = new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(DoctorRatingActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);
    }

    public void accessInterfaceMethod() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String opetation) {
                showingDialog.hideRefreshDialog();
                if (opetation.equals("addDoctorReview")) {
                    try {
                        if (jsonObject.getString("response").equals("3")) {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void failureCallBack(String failureMsg) {
                showingDialog.hideRefreshDialog();
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}