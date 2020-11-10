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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientModule.PatientForgotPassordActivity;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApiModel.RetrofitInstance;
import com.vedas.spectrocare.ServerApi;

import java.util.Objects;

import static com.vedas.spectrocare.activities.SelectUserActivity.isFromMedicalPerson;

public class ForgotActivity extends AppCompatActivity implements MedicalPersonaSignupView {
    Button Login, Next;
    ImageView iconBack;
    ProgressBar progressBar;
    AlertDialog.Builder dialog1;
    TextInputEditText F_mail;
    AlertDialog alertDialog;
    Handler handler;
    String fMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        F_mail = findViewById(R.id.edt_forgot_email);
        dialog1 = new AlertDialog.Builder(ForgotActivity.this);
        LayoutInflater inflater = ForgotActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alertbox_layout, null);
        progressBar = dialogView.findViewById(R.id.progressBar);
        dialog1.setView(dialogView);
        handler = new Handler();
        iconBack = findViewById(R.id.icon_back);
        progressBar.setVisibility(View.VISIBLE);
        alertDialog = dialog1.create();
        Next = findViewById(R.id.btn_next);
        //  Login = findViewById(R.id.btn_flogin);
        final ProgressDialog dialog = new ProgressDialog(this);
        //    MedicalProfileModel obj= MedicalProfileDataController.getInstance().currentMedicalProfile;
        Log.e("usus",""+getIntent().getStringExtra("userID"));
        F_mail.setText(getIntent().getStringExtra("userID"));

        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if (!isFromMedicalPerson){
            final TextView errorTxt = findViewById(R.id.error_txt);
            TextView forgotDisc = findViewById(R.id.title_forgot_description);
            forgotDisc.setText("Please enter your email and we will send a password reset link to your email.");
            final TextInputLayout textInputLayout = findViewById(R.id.layout_edit_text);
            textInputLayout.setHint("Email");
            F_mail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    errorTxt.setVisibility(View.GONE);
                    textInputLayout.setBackground(ForgotActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            Next.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    fMail=F_mail.getText().toString();
                    Log.e("fMail","call"+fMail);
                    F_mail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email,0,0,0);
                    if (F_mail.getText().toString().isEmpty()){
                        errorTxt.setText(" Email is empty");
                        errorTxt.setVisibility(View.VISIBLE);
                           textInputLayout.setBackground(ForgotActivity.this.getResources().getDrawable(R.drawable.btn_red_background));
                    }else if (!F_mail.getText().toString().contains("@gmail.com")){
                        errorTxt.setText(" Invalid Email Id");
                        errorTxt.setVisibility(View.VISIBLE);
                         textInputLayout.setBackground(ForgotActivity.this.getResources().getDrawable(R.drawable.btn_red_background));
                    }else{
                        if (isConn()) {
                            alertDialog.show();
                            Objects.requireNonNull(alertDialog.getWindow()).setLayout(600, 500);
                            patientForgotApi();
                        } else {
                            alertDialog.dismiss();
                            dialogeforCheckavilability("Error", "No Internet connection", "Ok");
                        }
                       /* errorTxt.setText(" Under Development");
                        errorTxt.setVisibility(View.VISIBLE);
                        textInputLayout.setBackground(ForgotActivity.this.getResources().getDrawable(R.drawable.btn_red_background));
*/
                    }
                }
            });

        }else{
            Next.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                     fMail = F_mail.getText().toString();
                    if (fMail.isEmpty()){
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ForgotActivity.this);
                        alertBuilder.setTitle("Error");
                        alertBuilder.setMessage("Please enter your Email");
                        alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertBuilder.create().show();
                    }else {
                        if (isConn()) {
                            alertDialog.show();
                            Objects.requireNonNull(alertDialog.getWindow()).setLayout(600, 500);
                            medicalPersonForgotApi();
                        } else {
                            alertDialog.dismiss();
                            dialogeforCheckavilability("Error", "No Internet connection", "Ok");
                        }
                    }
                }
            });

        }
    }
    private void medicalPersonForgotApi(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitInstance instance = new RetrofitInstance();
        ServerApi api = retrofit.create(ServerApi.class);
        instance.setUserID(fMail);
        Call<RetrofitInstance> call = api.forgot(instance);
        call.enqueue(new Callback<RetrofitInstance>() {
            @Override
            public void onResponse(Call<RetrofitInstance> call, Response<RetrofitInstance> response) {
                if (response.body() != null) {
                    if (response.body().getResponse().equals("0")) {
                        alertDialog.dismiss();
                        Toast.makeText(ForgotActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    if (response.body().getResponse().equals("3")) {
                        alertDialog.dismiss();
                        AlertDialog.Builder alertBuilder1 = new AlertDialog.Builder(ForgotActivity.this);
                        alertBuilder1.setTitle("Success");
                        alertBuilder1.setMessage(response.body().getMessage());
                        alertBuilder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();

                            }
                        });
                        alertBuilder1.create().show();
                        // Toast.makeText(ForgotActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RetrofitInstance> call, Throwable t) {
                alertDialog.dismiss();
            }
        });
    }
    private void patientForgotApi(){
        PatientLoginDataController.getInstance().fetchPatientlProfileData();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitInstance instance = new RetrofitInstance();
        ServerApi api = retrofit.create(ServerApi.class);
        instance.setUserID(fMail);
        Call<RetrofitInstance> call = api.patientForgotApi(/*PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(),*/
                instance);
        call.enqueue(new Callback<RetrofitInstance>() {
            @Override
            public void onResponse(Call<RetrofitInstance> call, Response<RetrofitInstance> response) {
                Log.e("responseBody","response"+response);
                if (response.body() != null) {
                    if (response.body().getResponse().equals("0")) {
                        alertDialog.dismiss();
                        Toast.makeText(ForgotActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    if (response.body().getResponse().equals("3")) {
                        alertDialog.dismiss();
                        startActivity(new Intent(ForgotActivity.this, PatientForgotPassordActivity.class));
                       /* AlertDialog.Builder alertBuilder1 = new AlertDialog.Builder(ForgotActivity.this);
                        alertBuilder1.setTitle("Success");
                        alertBuilder1.setMessage(response.body().getMessage());
                        alertBuilder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();

                            }
                        });
                        alertBuilder1.create().show();*/
                        // Toast.makeText(ForgotActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RetrofitInstance> call, Throwable t) {
                alertDialog.dismiss();
            }
        });
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
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ForgotActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);
    }
}
