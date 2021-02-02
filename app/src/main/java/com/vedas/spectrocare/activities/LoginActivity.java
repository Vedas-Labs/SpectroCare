package com.vedas.spectrocare.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.DataBaseModels.PatientModel;
import com.vedas.spectrocare.Location.LocationTracker;
import com.vedas.spectrocare.LoginResponseModel.AppSettingsModel;
import com.vedas.spectrocare.PatientAppointmentModule.AppointmentArrayModel;
import com.vedas.spectrocare.PatientAppointmentModule.PatientAppointmentsDataController;
import com.vedas.spectrocare.PatientMoreModule.SettingsActivity;
import com.vedas.spectrocare.PatientModule.PatientHomeActivity;
import com.vedas.spectrocare.PatientMoreModule.SettingsActivity;
//import com.vedas.spectrocare.PatientServerObjects.AppSettingsModel;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApiModel.RetrofitInstance;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.model.GetPatientsResponseModel;
import com.vedas.spectrocare.model.PatientList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;

import static com.vedas.spectrocare.activities.SelectUserActivity.isFromMedicalPerson;

public class LoginActivity extends AppCompatActivity implements MedicalPersonaSignupView {
    ImageView visible, invisible, iconBack;
    TextInputEditText edtPassword, edtEMail;
    TextInputLayout textInputLayout, txtPassLayout;
    String mailId, password;
    TextView Forgot, btn, txtDont, txtError;
    String mail, Password;
    ProgressBar progressBar;
    AlertDialog.Builder dialog1;
    Handler handler;
    AlertDialog alertDialog;
    RelativeLayout txtLayout, forgotLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Get User Location
        LocationTracker.getInstance().fillContext(getApplicationContext());
        LocationTracker.getInstance().startLocation();
        ApiCallDataController.getInstance().fillContent(getApplicationContext());
        accessInterfaceMethods();
        MedicalProfileDataController.getInstance().fillContext(getApplicationContext());
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        Log.e("Size", "" + MedicalProfileDataController.getInstance().allMedicalProfile.size());

        final LayoutInflater inflater = LoginActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alertbox_layout, null);
        dialog1 = new AlertDialog.Builder(LoginActivity.this);
        progressBar = dialogView.findViewById(R.id.progressBar);
        iconBack = findViewById(R.id.icon_back);
        txtDont = findViewById(R.id.txt);
        dialog1.setView(dialogView);
        forgotLayout = findViewById(R.id.layout_forgot);
        alertDialog = dialog1.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.transparent_bg);
        handler = new Handler();
        progressBar.setVisibility(View.VISIBLE);
        btn = findViewById(R.id.create);
        visible = findViewById(R.id.visible);
        invisible = findViewById(R.id.in_visible);
        Forgot = findViewById(R.id.txt_forgot);
        edtEMail = findViewById(R.id.edit_email);
        edtPassword = findViewById(R.id.edit_password);
        Button btn_b = findViewById(R.id.btn_login);
        edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        txtLayout = findViewById(R.id.txt_sign_up_layout);

        if (!isFromMedicalPerson) {
            forgotLayout.setVisibility(View.VISIBLE);
            Forgot.setVisibility(View.GONE);
            TextView txtUser = findViewById(R.id.txt_user);
            final ImageView imgInfo = findViewById(R.id.img_info);
            txtError = findViewById(R.id.txt_error);
            textInputLayout = findViewById(R.id.layout_edit_text);
            txtPassLayout = findViewById(R.id.layout_password);
            textInputLayout.setHint("Email/PatientID");
            edtEMail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email, 0, 0, 0);
            txtUser.setText(" Patient");
            imgInfo.setVisibility(View.VISIBLE);
            txtLayout.setVisibility(View.GONE);
            edtEMail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    txtError.setVisibility(View.GONE);
                    textInputLayout.setBackground(LoginActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    txtError.setVisibility(View.GONE);
                    textInputLayout.setBackground(LoginActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));
                    txtPassLayout.setBackground(LoginActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));
                }
            });
            edtPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    txtPassLayout.setBackground(LoginActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));
                    txtError.setVisibility(View.GONE);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            forgotLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(LoginActivity.this, ForgotActivity.class);
                    intent.putExtra("userID", edtEMail.getText().toString());
                    startActivity(intent);
                }
            });

            imgInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LayoutInflater mInflater = (LayoutInflater) getApplicationContext().
                            getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layoutView = mInflater.inflate(R.layout.popup_layout, null);
                    PopupWindow mDropdown = new PopupWindow(layoutView, FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT, true);
                    TextView textView = layoutView.findViewById(R.id.txt_info);
                    Typeface face = ResourcesCompat.getFont(LoginActivity.this, R.font.montserrat_regular);
                    textView.setTypeface(face);
                    textView.setTextColor(Color.parseColor("#707070"));
                    Drawable background = getResources().getDrawable(android.R.drawable.editbox_background);
                    mDropdown.setBackgroundDrawable(background);
                    mDropdown.showAsDropDown(imgInfo, 5, 5);

                   /* LayoutInflater mInflater = (LayoutInflater) getApplicationContext()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = mInflater.inflate(R.layout.popup_layout, null);

                              *//*  layout.measure(View.MeasureSpec.UNSPECIFIED,
                                        View.MeasureSpec.UNSPECIFIED);*//*
                    PopupWindow mDropdown = new PopupWindow(layout, RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT,true);
                    Drawable background = getResources().getDrawable(android.R.drawable.editbox_background);
                    mDropdown.setBackgroundDrawable(background);
                    mDropdown.showAsDropDown(imgInfo,5,5);
*/

                }
            });

            btn_b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (edtEMail.getText().toString().isEmpty()) {
                        txtError.setText(" Email is empty");
                        txtError.setVisibility(View.VISIBLE);
                        textInputLayout.setBackground(LoginActivity.this.getResources().getDrawable(R.drawable.btn_red_background));
                    } else if (!edtEMail.getText().toString().contains("@gmail.com")) {
                        txtError.setText(" Invalid Email Id");
                        txtError.setVisibility(View.VISIBLE);
                        textInputLayout.setBackground(LoginActivity.this.getResources().getDrawable(R.drawable.btn_red_background));
                    } else if (edtPassword.getText().toString().isEmpty()) {
                        txtError.setVisibility(View.VISIBLE);
                        txtPassLayout.setBackground(LoginActivity.this.getResources().getDrawable(R.drawable.btn_red_background));
                        txtError.setText(" Password is empty");
                    }/*else if ((edtPassword.getText().toString().length() <8)){
                        txtError.setText("Password length must be 8 characters");
                        txtError.setVisibility(View.VISIBLE);
                        txtPassLayout.setBackground(LoginActivity.this.getResources().getDrawable(R.drawable.btn_red_background));
                    }*/ else {
                        alertDialog.show();
                        Objects.requireNonNull(alertDialog.getWindow()).setLayout(450, 400);
                        Validation();
                    }
                }
            });
        } else {
            btn_b.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    alertDialog.show();
                    Objects.requireNonNull(alertDialog.getWindow()).setLayout(450, 400);
                    Validation();

                }
            });
        }
        // Button btn = findViewById(R.id.create);


        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, ForgotActivity.class);
                intent.putExtra("userID", edtEMail.getText().toString());
                startActivity(intent);
            }
        });

        invisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPassword.post(new Runnable() {
                    @Override
                    public void run() {
                        edtPassword.setSelection(edtPassword.length());
                    }
                });
                invisible.setVisibility(View.GONE);
                visible.setVisibility(View.VISIBLE);
                edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });
        visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPassword.post(new Runnable() {
                    @Override
                    public void run() {
                        edtPassword.setSelection(edtPassword.length());
                    }
                });
                visible.setVisibility(View.GONE);
                invisible.setVisibility(View.VISIBLE);
                edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MedicalPersonSignupActivity.class);
                startActivity(intent);
            }
        });

        if (MedicalProfileDataController.getInstance().allMedicalProfile.size() > 0) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(LoginActivity.this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private boolean Validation() {
        mailId = edtEMail.getText().toString();
        password = edtPassword.getText().toString();
        if (mailId.isEmpty()) {
            alertDialog.dismiss();
            dialogeforCheckavilability("Alert", "please enter email address", "Ok");
        }
        if (!mailId.isEmpty()) {
            if (password.isEmpty()) {
                alertDialog.dismiss();
                dialogeforCheckavilability("Alert", "please enter password ", "Ok");
            } else {
                if (isNetworkAvailable()) {
                    if (isFromMedicalPerson) {
                        alertDialog.show();
                        Api();
                    } else {
                        alertDialog.show();
                        executePatientLoginApi();
                    }
                } else {
                    alertDialog.dismiss();
                    dialogeforCheckavilability("Alert", "please check your connection ", "Ok");

                }
            }
        }
        return true;
    }

    private void executePatientLoginApi() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ServerApi s = retrofit.create(ServerApi.class);
        final GetPatientsResponseModel retrofitInstance = new GetPatientsResponseModel();
        final String mail = edtEMail.getText().toString();
        String Password = edtPassword.getText().toString();
        retrofitInstance.setUserID(mail);
        retrofitInstance.setPassword(Password);
        Call<GetPatientsResponseModel> call = s.loginAsPatient(retrofitInstance);
        call.enqueue(new Callback<GetPatientsResponseModel>() {
            @Override
            public void onResponse(Call<GetPatientsResponseModel> call, Response<GetPatientsResponseModel> response) {
                Log.e("response", "check" + response.body().getResponse());
                if (response.body() != null) {
                    if (response.body().getResponse().equals("0")) {
                        txtError.setText(" Incorrect userID or password");
                        txtError.setVisibility(View.VISIBLE);
                        alertDialog.dismiss();
                    }
                    if (response.body() != null) {
                        //  TrackInfoDataController.getInstance().deleteTrackData(PatientLoginDataController.getInstance().allPatientlProfile);
                        PatientLoginDataController.getInstance().deletePatientModelData(PatientLoginDataController.getInstance().allPatientlProfile);
                        PatientList ObjPatient = response.body().getPatientInfo();
                        if (ObjPatient != null) {
                            final JsonElement number = ObjPatient.getPhoneNumber().get("phoneNumber");
                            final JsonElement numberCode = ObjPatient.getPhoneNumber().get("countryCode");
                            PatientModel profileModel = new PatientModel();
                            profileModel.setAccessToken(response.body().getAccessToken());
                            profileModel.setPatientId(ObjPatient.getPatientID());
                            profileModel.setPhone_coutryCode(numberCode.getAsString());
                            profileModel.setPhoneNumber(number.getAsString());
                            profileModel.setAddress(ObjPatient.getAddress());
                            profileModel.setMedicalRecordId(ObjPatient.getMedical_record_id());
                            profileModel.setAge(ObjPatient.getAge());
                            profileModel.setCity(ObjPatient.getCity());
                            profileModel.setCountry(ObjPatient.getCountry());
                            profileModel.setDob(ObjPatient.getDob());
                            profileModel.setEmailId(ObjPatient.getEmailID());
                            profileModel.setFirstName(ObjPatient.getFirstName());
                            profileModel.setGender(ObjPatient.getGender());
                            profileModel.setHospital_reg_number(ObjPatient.getHospital_reg_num());
                            profileModel.setLastName(ObjPatient.getLastName());
                            profileModel.setLatitude(String.valueOf(LocationTracker.getInstance().currentLocation.getLatitude()));
                            profileModel.setLongitude(String.valueOf(LocationTracker.getInstance().currentLocation.getLongitude()));
                            profileModel.setMedicalPerson_id(ObjPatient.getMedical_personnel_id());
                            profileModel.setPostalCode(ObjPatient.getPostalCode());
                            profileModel.setProfileByteArray(null);
                            profileModel.setState(ObjPatient.getState());
                            profileModel.setProfilePic(ServerApi.img_home_url + ObjPatient.getProfilePic());
                            Log.e("dob", "" + profileModel.getDob() + profileModel.getProfilePic());
                            /*Gson gson = new Gson();
                            String json = gson.toJson(profileModel);
                            Log.e("dadd","d"+json);*/
                            //accessToken
                            if (!ObjPatient.getDob().isEmpty() && ObjPatient.getDob() != null) {
                                if (ObjPatient.getDob().contains("-")) {
                                    profileModel.setDob(ObjPatient.getDob());
                                } else if (ObjPatient.getDob().contains("/")) {
                                    profileModel.setDob(ObjPatient.getDob());
                                } else {
                                    Log.e("timestamp", "" + ObjPatient.getDob());
                                    profileModel.setDob(ObjPatient.getDob());
/*
                                    try {
                                        String date = PersonalInfoController.getInstance().convertTimestampTodate(ObjPatient.getDob());
                                        profileModel.setDob(date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
*/
                                }
                            }
                            PersonalInfoController.getInstance().appSettingsModel = ObjPatient.getAppSettings();
                            //  PersonalInfoController.getInstance().appSettingsModel = ObjPatient.getAppSettings();
                            if (PersonalInfoController.getInstance().appSettingsModel != null) {
                                SharedPreferences sharedpreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
                                SettingsActivity.editor = sharedpreferences.edit();
                                Log.e("appSettingObj", "" + PersonalInfoController.getInstance().appSettingsModel.getLanguage());
                                AppSettingsModel appSettingsModel = PersonalInfoController.getInstance().appSettingsModel;
                                Log.e("AppSettingsModel", "call" + appSettingsModel.getDateFormat());
                                SettingsActivity.editor.putString("units", appSettingsModel.getUnit());
                                SettingsActivity.editor.putString("dateFormat", appSettingsModel.getDateFormat());
                                if (appSettingsModel.getTimeFormatType().equals("24-Hour")) {
                                    SettingsActivity.editor.putBoolean("is24Hour", true);
                                } else {
                                    SettingsActivity.editor.putBoolean("is24Hour", false);
                                }
                                SettingsActivity.editor.putString("region", appSettingsModel.getRegion());
                                SettingsActivity.editor.putString("language", appSettingsModel.getLanguage());
                                SettingsActivity.editor.putBoolean("allnotification", appSettingsModel.isNotification());
                                if (appSettingsModel.getNotificationItems().size() > 0) {
                                    SettingsActivity.editor.putBoolean("appointment", appSettingsModel.getNotificationItems().get(0).isEnabled());
                                    SettingsActivity.editor.putBoolean("chat", appSettingsModel.getNotificationItems().get(1).isEnabled());
                                    SettingsActivity.editor.putBoolean("bill", appSettingsModel.getNotificationItems().get(2).isEnabled());
                                }
                                SettingsActivity.editor.commit();
                            }
                            String strPatientId = ObjPatient.getPatientID();
                            SharedPreferences preferences = getSharedPreferences("temp", 0);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("strPatientID", strPatientId);
                            editor.apply();
                            if (PatientLoginDataController.getInstance().insertPatientData(profileModel)) {
                                Log.e("listOfname", "" + profileModel.getHospital_reg_number());
                                PatientLoginDataController.getInstance().fetchPatientlProfileData();
                                addLoginDevice();
                                /*PatientMedicalRecordsController.getInstance().isFromLogin=true;
                                startActivity(new Intent(getApplicationContext(), PatientHomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                             */
                            }

                       /* if (ObjPatient.getTracking().size() > 0) {
                            List<TrackingServerObject> trackingList = ObjPatient.getTracking();
                            Log.e("trackpatienid", "" + profileModel.getPatientId());
                            for(int index=0;index<trackingList.size();index++){
                                TrackingServerObject serverObject=trackingList.get(index);
                                TrackInfoModel trackInfoModel=new TrackInfoModel();
                                trackInfoModel.setPatientlProfileModel(profileModel);
                                trackInfoModel.setPatientId(profileModel.getPatientId());
                                trackInfoModel.setByWhom(serverObject.getByWhom());
                                trackInfoModel.setByWhomId(serverObject.getByWhomID());
                                Long l=Long.parseLong(serverObject.getDate())/1000;
                                trackInfoModel.setDate(String.valueOf(l));
                                if(TrackInfoDataController.getInstance().insertTrackData(trackInfoModel)){
                                    // TrackInfoDataController.getInstance().fetchTrackData(profileModel);
                                }
                            }
                        }*/
                        }
                    }
                } else {
                    alertDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<GetPatientsResponseModel> call, Throwable t) {
                alertDialog.dismiss();
                Log.e("dobindi", "" + t.getMessage());
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    public void Api() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ServerApi s = retrofit.create(ServerApi.class);
        final RetrofitInstance retrofitInstance = new RetrofitInstance();
        mail = edtEMail.getText().toString();
        mail = mail.trim();
        Password = edtPassword.getText().toString();
        Password = Password.trim();
        retrofitInstance.setUserID(mail);
        retrofitInstance.setPassword(Password);
        Call<ResponseBody> call = s.login(retrofitInstance);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    alertDialog.dismiss();
                    try {
                        String responseString = new String(response.body().bytes());
                        Log.e("ServiceResponse", "onResponse: " + responseString);
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("response").equals("3")) {
                            String accessToken = jsonObject.getString("access_token");
                            Log.e("token", "onResponse: " + accessToken);
                            final MedicalProfileModel objModel = new MedicalProfileModel();

                            JSONObject allJsonObj = jsonObject.getJSONObject("medicalPersonnel");
                            JSONObject profileObj = allJsonObj.getJSONObject("profile");
                            JSONObject medicalPersonnelJsonObject = profileObj.getJSONObject("userProfile");

                            JSONObject phoneObject = medicalPersonnelJsonObject.getJSONObject("phoneNumber");
                            Log.e("PersonnelJsonObject", "onResponse: " + phoneObject.getString("phoneNumber"));
                            objModel.setPhone_coutryCode(phoneObject.getString("countryCode"));
                            objModel.setPhoneNumber(phoneObject.getString("phoneNumber"));
                            objModel.setAccessToken(accessToken);
                            objModel.setHospital_reg_number(medicalPersonnelJsonObject.getString("hospital_reg_num"));
                            objModel.setPassword(medicalPersonnelJsonObject.getString("password"));
                          /*  objModel.setQualification(medicalPersonnelJsonObject.getString("qualification"));
                            objModel.setSpecialzation(medicalPersonnelJsonObject.getString("specialization"));
                          */
                            objModel.setUserId(medicalPersonnelJsonObject.getString("userID"));
                            objModel.setUserType(medicalPersonnelJsonObject.getString("userType"));
                            // objModel.setAccessToken(jsonObject.getString("access_token"));
                            objModel.setAge(medicalPersonnelJsonObject.getString("age"));
                            objModel.setDepartment(medicalPersonnelJsonObject.getString("department"));
                            //  objModel.setDob(medicalPersonnelJsonObject.getString("dob"));
                            objModel.setEmailId(medicalPersonnelJsonObject.getString("emailID"));
                            objModel.setFirstName(medicalPersonnelJsonObject.getString("firstName"));
                            objModel.setGender(medicalPersonnelJsonObject.getString("gender"));
                            // objModel.setId_By_govt(medicalPersonnelJsonObject.getString("id_by_govt"));
                            objModel.setLastName(medicalPersonnelJsonObject.getString("lastName"));
                            objModel.setLotitude(String.valueOf(LocationTracker.getInstance().currentLocation.getLatitude()));
                            objModel.setLongitude(String.valueOf(LocationTracker.getInstance().currentLocation.getLongitude()));
                            objModel.setMedical_person_id(medicalPersonnelJsonObject.getString("medical_personnel_id"));

                            Log.e("testing", "" + objModel.getUserId() + objModel.getPhone_coutryCode());
                            Log.e("accesstoken", "call" + ServerApi.img_home_url + medicalPersonnelJsonObject.getString("profilePic"));
                            final String urlString = ServerApi.img_home_url + medicalPersonnelJsonObject.getString("profilePic");
                            Log.e("PersonalURL", urlString);
                            objModel.setProfilePic(urlString);
                            objModel.setPrefferLanguage("English");


                            if (MedicalProfileDataController.getInstance().insertMedicalProfileData(objModel)) {
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                MedicalProfileDataController.getInstance().fetchMedicalProfileData();
                            }

                        } else if (jsonObject.getString("response").equals("0")) {
                            String message = jsonObject.getString("message");
                            dialogeforCheckavilability("Login Failed", message, "Ok");
                            alertDialog.dismiss();
                            // Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    alertDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                alertDialog.dismiss();
                //  Toast.makeText(LoginActivity.this,"Please check your internet connection", Toast.LENGTH_SHORT).show();
                Log.e("dobindi", "" + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                if (curdOpetaton.equals("addDevice")) {
                    try {
                        Log.e("accessInterfaceMethods", "dd" + jsonObject.toString());
                        if (jsonObject.getString("response").equals("3")) {
                            Log.e("kkkkkk", "dd" + jsonObject.getString("message"));
                            alertDialog.dismiss();
                            PatientMedicalRecordsController.getInstance().isFromLogin = true;
                            finish();
                            startActivity(new Intent(getApplicationContext(), PatientHomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void failureCallBack(String failureMsg) {

            }
        });
    }

    private void addLoginDevice() {
       /* String token = FirebaseInstanceId.getInstance().getToken();
        String ID = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);*/
        SharedPreferences sharedPreferencesTOken = getApplicationContext().getSharedPreferences("tokendeviceids", 0);
        String deviceID = sharedPreferencesTOken.getString("deviceId", null);
        String deviceToken = sharedPreferencesTOken.getString("tokenid", null);

        Log.e("addLoginDevice", deviceID+" token :: " + deviceToken);
        JSONObject fetchObject = new JSONObject();
        try {
            fetchObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile
                    .getHospital_reg_number());
            fetchObject.put("deviceType", "mobile");
            fetchObject.put("deviceID", deviceID);
            fetchObject.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            fetchObject.put("deviceToken", deviceToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(fetchObject.toString());
        Log.e("send", "data" + PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken());
        ApiCallDataController.getInstance().loadjsonApiCall(
                ApiCallDataController.getInstance().serverApi.
                        addLoginDeviceApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "addDevice");

    }

    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter = new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LoginActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);
    }

    @Override
    public void onBackPressed() {
       /* Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
        startActivity(new Intent(LoginActivity.this, SelectUserActivity.class));
        finish();
    }
}



