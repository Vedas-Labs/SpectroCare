package com.vedas.spectrocare.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.DataBaseModels.PatientModel;
import com.vedas.spectrocare.ServerApiModel.ChangePasswordInstance;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;

import java.util.Objects;

public class ChangePasswordActivity extends AppCompatActivity implements MedicalPersonaSignupView {
    TextView Back;
    ImageView imgVisible,imgInvisible,imgNewVisible,imgNewInvisible,imgCnfVisible,imgCnfInvisible;
    TextInputEditText edtOldPassword,edtNewPassword,edtConformNewpassword;
    Button btnChange;
    ProgressBar progressBar;
    AlertDialog alertDialog;
    Handler handler;
    AlertDialog.Builder dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        edtOldPassword = findViewById(R.id.edit_old_password);
        edtNewPassword = findViewById(R.id.edit_new_password);
        imgCnfInvisible = findViewById(R.id.conform_pass_in_visible);
        imgCnfVisible = findViewById(R.id.conform_pass_visible);
        imgNewInvisible = findViewById(R.id.new_pass_in_visible);
        imgNewVisible = findViewById(R.id.new_pass_visible);
        imgInvisible = findViewById(R.id.in_visible);
        imgVisible  = findViewById(R.id.visible);
        edtConformNewpassword = findViewById(R.id.edit_conform_new_password);
        btnChange = findViewById(R.id.btn_cahange_password);
        Back = findViewById(R.id.back);
        dialog1 = new AlertDialog.Builder(ChangePasswordActivity.this);
        LayoutInflater inflater = ChangePasswordActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alertbox_layout, null);
        handler = new Handler();
        progressBar = dialogView.findViewById(R.id.progressBar);
        dialog1.setView(dialogView);
        progressBar.setVisibility(View.VISIBLE);
        alertDialog = dialog1.create();
        final ProgressDialog dialog = new ProgressDialog(this);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        passwordToggle(imgCnfInvisible,imgCnfVisible,edtConformNewpassword);
        passwordToggle(imgNewInvisible,imgNewVisible,edtNewPassword);
        passwordToggle(imgInvisible,imgVisible,edtOldPassword);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                ChangePasswordInstance instu = new ChangePasswordInstance();
                Retrofit rFit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                        .addConverterFactory(GsonConverterFactory.create()).build();
                ServerApi SApi = rFit.create(ServerApi.class);
                String user = edtOldPassword.getText().toString();
                String pass = edtNewPassword.getText().toString();
                String conform = edtConformNewpassword.getText().toString();
                String password="";
                if(SelectUserActivity.isFromMedicalPerson){
                    final MedicalProfileModel currentMedicalProfile= MedicalProfileDataController.getInstance().currentMedicalProfile;
                    instu.setUserID(currentMedicalProfile.getUserId());
                     password = currentMedicalProfile.getPassword();
                    Log.e("papap",""+password);
                    instu.setOldPassword(user);
                    instu.setNewPassword(pass);
                }else {

                 PatientModel patientModel= PatientLoginDataController.getInstance().currentPatientlProfile;
                    instu.setUserID(patientModel.getPatientId());
                    Log.e("string","id"+instu.getUserID()); // password = patientModel.getP();
                    instu.setOldPassword(user);
                    instu.setNewPassword(pass);
                }

                if (user.isEmpty())
                    dialogeforCheckavilability("Error","Password should not be empty  ","Ok");
                /*else if (!user.equals(password))
                    dialogeforCheckavilability("Error","Incorrect old password  ","Ok");*/
                else if(pass.isEmpty())
                    dialogeforCheckavilability("Error","Please enter  password ","Ok");
                else if(edtNewPassword.length()<=7)
                    dialogeforCheckavilability("Error","password should have atleast 8 charecters  ","Ok");
                else if(conform.isEmpty())
                    dialogeforCheckavilability("Error","Please enter  password ","Ok");
               /* else if (edtOldPassword.length()<=7)
                    dialogeforCheckavilability("Error","password should have atleast 8 charecters  ","Ok");*/
                else if (!pass.matches(conform))
                    dialogeforCheckavilability("Error"," Password does not matched","Ok");
                else if(SelectUserActivity.isFromMedicalPerson) {
                    if (!user.equals(password))
                        dialogeforCheckavilability("Error","Incorrect old password  ","Ok");
                }
                else {
                    alertDialog.show();
                    Objects.requireNonNull(alertDialog.getWindow()).setLayout(600, 500);
                    Call<ChangePasswordInstance> call = null;
                    if(SelectUserActivity.isFromMedicalPerson){
                        call = SApi.changePassword(instu);
                    }else {
                        PatientLoginDataController.getInstance().fetchPatientlProfileData();
                        call = SApi.patientChangePswApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(),instu);
                    }
                    call.enqueue(new Callback<ChangePasswordInstance>() {
                        @Override
                        public void onResponse(Call<ChangePasswordInstance> call, Response<ChangePasswordInstance> response) {
                            String grrrr = response.message();
                            Log.e("change", "check body response " + grrrr);
                            if (response.body() != null) {
                                String statusCoe = response.body().getResponse();
                                String msg = response.body().getMessage();
                                if (statusCoe.equals("0")) {
                                    alertDialog.dismiss();
                                    LayoutInflater inflater = getLayoutInflater();
                                    View layout = inflater.inflate(R.layout.toast_layout,
                                            (ViewGroup) findViewById(R.id.custom_toast_container));

                                    TextView text = (TextView) layout.findViewById(R.id.text);
                                    text.setText(response.body().getMessage());
                                    Toast toast = new Toast(ChangePasswordActivity.this);
                                    //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                    toast.setDuration(Toast.LENGTH_LONG);
                                    toast.setView(layout);
                                    toast.show();
                                    //  Toast.makeText(ChangePasswordActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }
                                if (statusCoe.equals("3")) {
                                    alertDialog.dismiss();
                                    if(SelectUserActivity.isFromMedicalPerson) {
                                        //currentMedicalProfile.setPassword(edtNewPassword.getText().toString());
                                        // Toast.makeText(ChangePasswordActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    }else {
                                        finish();
                                        startActivity(new Intent(getApplicationContext(),PasswordChangedActivity.class));
                                    }
                                    LayoutInflater inflater = getLayoutInflater();
                                    View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.custom_toast_container));
                                    TextView text = (TextView) layout.findViewById(R.id.text);
                                    text.setText(response.body().getMessage());
                                    Toast toast = new Toast(ChangePasswordActivity.this);
                                    //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                    toast.setDuration(Toast.LENGTH_LONG);
                                    toast.setView(layout);
                                    toast.show();
                                }
                            } else {
                                alertDialog.dismiss();
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.custom_toast_container));
                                TextView text = (TextView) layout.findViewById(R.id.text);
                                text.setText("not found");
                                Toast toast = new Toast(ChangePasswordActivity.this);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setView(layout);
                                toast.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ChangePasswordInstance> call, Throwable t) {
                            alertDialog.dismiss();
                        }

                    });
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog.dismiss();
                    }
                }, 3000);

            }
        });
    }

    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter=new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ChangePasswordActivity.this);
        presenter.dialogebox(alertBuilder,title,message,ok);
    }

    public void passwordToggle( final ImageView inVis,final ImageView vis, final TextInputEditText edtPassword){
        inVis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPassword.post(new Runnable() {
                    @Override
                    public void run() {
                        edtPassword.setSelection(edtPassword.length());
                    }
                });
                inVis.setVisibility(View.GONE);
                vis.setVisibility(View.VISIBLE);
                edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });
        vis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPassword.post(new Runnable() {
                    @Override
                    public void run() {
                        edtPassword.setSelection(edtPassword.length());
                    }
                });
                vis.setVisibility(View.GONE);
                inVis.setVisibility(View.VISIBLE);
                edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
    }
}
