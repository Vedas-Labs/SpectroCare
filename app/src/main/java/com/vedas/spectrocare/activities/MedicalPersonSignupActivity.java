package com.vedas.spectrocare.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.Location.LocationTracker;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApiModel.RetrofitInstance;
import com.vedas.spectrocare.ServerApi;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MedicalPersonSignupActivity extends AppCompatActivity implements MedicalPersonaSignupView {
    Spinner userTypeSpinner, specialzationSpinner, qualificationSpinner, departmentSpinner;
    String selectedUserType="",selectedSpecialization="",selectedQualification="",selectedDepartment="";
    TextView Back,txtLogin;
    String selectedAge = "0";
    String selectedTitle = "y";
    NumberPicker numberPickerAge, agePicker;
    ImageView imageView ,imgClose,imgTick,imgMailClose;

    RetrofitInstance retroInstance;
    TextInputLayout layoutMedicalPersonnelID;
    TextInputEditText txt_dob,ed_age,medicPersonId, ed_conform_p, ed_Password, edtName, ed_phoneNumber,txtGender,edtMail,
            ed_lastName, ed_countyCode, ed_fistname, ed_hospitalRegistrationNum, ed_IdGivenGovt;
    TextView  check_user, check_email, txtAlready,textView1, Star,  Star1;
TextInputEditText spinn,edtSpecialization,edtQulaify,edtDepart;
TextInputLayout layoutHospitalReg,layoutEdtMail;
    Button btn;
    ProgressBar progressBar;
    AlertDialog.Builder dialog2;
    AlertDialog alertDialog;
    AlertDialog.Builder dialog1;
    LayoutInflater inflater;
    AlertDialog dialog;
    CircularImageView profilePic;
    RelativeLayout relativePassword;
    RequestBody userID;
    RequestBody password;
    RequestBody firstName;
    RequestBody LastName;
    RequestBody phoneNumber;
    RequestBody phoneNumberCountryCode;
    RequestBody latitude;
    RequestBody emailID;
    RequestBody qualification;
    RequestBody specialization;
    RequestBody department;
    RequestBody userType;
    RequestBody id_by_govt;
    RequestBody preferLanguage;
    RequestBody gender;
    RequestBody age;
    RequestBody longitude;
    RequestBody hospital_reg_num;
    RequestBody Dob;
    RequestBody MedicPersonId;
    ImageView vision, inVision, visionMp, invisionMp,imgCheckAvailability,imgMailCheckAvailability;
    boolean Mchecked, Uchecked,imgCheck,imageVV;
    RadioButton cb1, cb2, cb3;
    Button Update;
    LinearLayout tileSign, titleUpdate;

    String[] empArray = {"", "Doctor", "Nurse", "Staff"};
    String[] departmentsArray = {"", "Accident and emergency", "Admissions", "Anesthetics", "Breast Screening", "Burn Center", "Cardiology", "Central Sterile Services departmentSpinner (CSSD)", "Chaplaincy", "Coronary Care Unit (CCU)", "Critical Care", "Diagnostic Imaging", "Discharge Lounge", "Elderly services", "Finance departmentSpinner", "Gastroenterology", "General Services", "General Surgery", "Gynecology", "Haematology", "Health & Safety", "Intensive Care Unit (ICU)", "Human Resources", "Infection Control", "Information Management", "Maternity", "Medical Records", "Microbiology", "Neonatal", "Nephrology", "Neurology", "Nutrition and Dietetics", "Obstetrics/Gynecology", "Occupational Therapy", "Oncology", "Ophthalmology", "Orthopaedics", "Otolaryngology (Ear, Nose, and Throat)", "Pain Management", "Patient Accounts", "Patient Services", "Pharmacy", "Physiotherapy", "Purchasing & Supplies", "Radiology", "Radiotherapy", "Renal", "Rheumatology", "Sexual Health", "Social Work", "Urology"};
    String[] specializationArray = {"", "Allergist", "Anasthesiologist", "Andrologist", "Cardiologist",
            "Cardiac Electrophysiologist", "Dermatologist", "Emergency Medicine / Emergency (ER) Doctors",
            "Endocrinologist", "Epidemiologist", "Family Medicine Physician", "Gastroenterologist", "Geriatrician",
            "Hyperbar Physician", "Hematologist", "Hepatologist", "Immunologist", "Infectious Disease Specialist",
            "Intensivist", "Internal Medicine Specialist", "Maxillofacial Surgeon / Oral Surgeon", "Medical Geneticist",
            "Neonatologist", "Nephrologist", "Neurologist", "Neurosurgeon", "Nuclear Medicine Specialist",
            "Obstetrician/Gynecologist (OB/GYN)", "Occupational Medicine Specialist", "Oncologist", "Ophthalmologist",
            "Orthopedic Surgeon / Orthopedist", "Otolaryngologist (also ENT Specialist)", "Parasitologist",
            "Pathologist", "Perinatologist", "Periodontist", "Pediatrician", "Plastic Surgeon", "Physiatrist",
            "Pulmonologist", "Radiologist", "Rheumatologist", "Sleep Doctor / Sleep Disorders Specialist",
            "Spinal Cord Injury Specialist", "Sports Medicine Specialist", "Surgeon", "Thoracic Surgeon",
            "Urologist", "Vascular Surgeon", "Audiologist", "Veterinarian", "Chiropractor", "Diagnostician",
            "Microbiologist", "Palliative care specialist", "Pharmacist", "Physiotherapist", "Podiatrist / Chiropodist"};
    String[] doctorQualificationsArray = {"", "MBBS", "BDS", "BAMS", "BUMS", "BHMS", "MD", "MS", "DNB"};
    String[] nurseQualificationsArray = {"", "LPN/LVN", "RN", "NP", "NLNAC"};
    File file;
    String tempbirthDayObj="";
    private Bitmap bitmap;
    String profileBase64Obj, oldProfielBase64Obj;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_person_signup);
        casting();
        PasswordToggleListners();
        PersonalInfoController.getInstance().fillContent(getApplicationContext());
        PersonalInfoController.getInstance().loadAgeUnitaArray();
        PersonalInfoController.getInstance().loadHeightValuesArray();

       // CheckBox();
        cb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtGender.setText("Male");
            }
        });
        cb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtGender.setText("Female");
            }
        });
        cb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtGender.setText("Others");
            }
        });
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Mchecked = false;
        Uchecked = false;
        imageVV = false;
        imgCheck = false;
        btn = findViewById(R.id.btn_sign_up);

        DatePicker();
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        tempbirthDayObj = dateFormat.format(currentDate);

        dialog2 = new AlertDialog.Builder(MedicalPersonSignupActivity.this);
        LayoutInflater inflater = MedicalPersonSignupActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progress, null);
        progressBar = dialogView.findViewById(R.id.progressBar);
        dialog2.setView(dialogView);
        progressBar.setVisibility(View.VISIBLE);
        alertDialog = dialog2.create();

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


       edtMail.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               imgMailCheckAvailability.setImageResource(R.drawable.find);

               return false;
           }
       });
       edtName.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               imgCheckAvailability.setImageResource(R.drawable.find);
               return false;
           }
       });
     /*  ed_age.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {

               return true;
           }
       });*/

        ed_age.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ed_age.setInputType(InputType.TYPE_NULL);
               loadAgePicker();
           }
       });


        MedicalProfileDataController.getInstance().fetchMedicalProfileData();

        if (MedicalProfileDataController.getInstance().allMedicalProfile.size()>0) {
            relativePassword = findViewById(R.id.password_layout);
            titleUpdate = findViewById(R.id.title_Layout);
            tileSign = findViewById(R.id.main_title);
            tileSign.setVisibility(View.GONE);
            txtAlready.setVisibility(View.GONE);
            txtLogin.setVisibility(View.GONE);
            titleUpdate.setVisibility(View.VISIBLE);
            relativePassword.setVisibility(View.GONE);
            check_user.setVisibility(View.GONE);
            check_email.setVisibility(View.GONE);
            Update.setVisibility(View.VISIBLE);
            imgCheckAvailability.setVisibility(View.GONE);
            imgMailCheckAvailability.setVisibility(View.GONE);
            btn.setVisibility(View.GONE);
            btn.setEnabled(false);
          //  ed_hospitalRegistrationNum.setVisibility(View.GONE);
            layoutHospitalReg.setVisibility(View.GONE);
//            Star1.setVisibility(View.GONE);
         //   TextView hospitalRegNo = findViewById(R.id.txt_hospital_registered_no);
            medicPersonId.setVisibility(View.VISIBLE);
            layoutMedicalPersonnelID.setVisibility(View.VISIBLE);
         //   hospitalRegNo.setVisibility(View.GONE);
            getDataFromSharedPreferences();
            userTypeSpinner.setEnabled(false);
            userTypeSpinner.setClickable(false);
            ed_age.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    ed_age.setInputType(InputType.TYPE_NULL);
                    loadAgePicker();
                    return false;
                }
            });


        }/* else {
            textView1.setVisibility(View.GONE);
            Star.setVisibility(View.GONE);
            medicPersonId.setVisibility(View.GONE);
        }*/

        imgMailCheckAvailability.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                Mchecked = true;
                Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                        .addConverterFactory(GsonConverterFactory.create()).build();
                ServerApi s = retrofit.create(ServerApi.class);
                String email = edtMail.getText().toString();
                if (email.isEmpty())
                    dialogeforCheckavilability("Error", "Please enter your emailID", "Ok");
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                    dialogeforCheckavilability("Error", "Enter valid emailID", "ok");
                else {
                    alertDialog.show();
                    Objects.requireNonNull(alertDialog.getWindow()).setLayout(600, 500);
                    RetrofitInstance retrofitInstance = new RetrofitInstance();
                    retrofitInstance.setUserID(email);
                    Call<RetrofitInstance> call = s.checkAvail(retrofitInstance);
                    call.enqueue(new Callback<RetrofitInstance>() {
                        @Override
                        public void onResponse(Call<RetrofitInstance> call, Response<RetrofitInstance> response) {
                            if (!response.isSuccessful()) {

                                Log.e("raledu", "" + response.code());
                                return;
                            }
                            if (response.body() != null) {
                                String res = response.body().getResponse();
                                String msg = response.body().getMessage();
                                if (res.equals("0")) {
                                    alertDialog.dismiss();
                                    final AlertDialog.Builder alertBuilder1 = new AlertDialog.Builder(MedicalPersonSignupActivity.this);
                                    alertBuilder1.setTitle("UserID already registered by other users");
                                    alertBuilder1.setMessage(msg);

                                    alertBuilder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                           // imgMailClose.setVisibility(View.VISIBLE);
                                            imgMailCheckAvailability.setImageResource(R.drawable.close);
                                            alertBuilder1.create().dismiss();
                                        }
                                    });
                                    alertBuilder1.create().show();
                                   // dialogeforCheckavilability("UserID already registered by other users", msg, "OK");
                                   // edtMail.setTextColor(Color.parseColor("#ED5276"));
                                    //  Toast.makeText(MedicalPersonSignupActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }
                                if (res.equals("3"))
                                    alertDialog.dismiss();
                              //  imgTick.setVisibility(View.VISIBLE);
                                imgMailCheckAvailability.setImageResource(R.drawable.tick);
                                   // edtMail.setTextColor(Color.parseColor("#1b5f2d"));
                                Toast.makeText(MedicalPersonSignupActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                            Log.e("vachindi", "" + response.body());
                        }

                        @Override
                        public void onFailure(Call<RetrofitInstance> call, Throwable t) {
                            // Log.e("dobindi",""+t.getMessage());
                            alertDialog.dismiss();
                            t.printStackTrace();

                        }
                    });
                }
            }
        });
        imgCheckAvailability.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Uchecked = true;
                Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                        .addConverterFactory(GsonConverterFactory.create()).build();
                ServerApi s = retrofit.create(ServerApi.class);
                String name = edtName.getText().toString();
                if (name.isEmpty())
                    dialogeforCheckavilability("Error", "Please enter your username", "Ok");
                else {

                    RetrofitInstance retrofitInstance = new RetrofitInstance();
                    retrofitInstance.setUserID(name);
                    Call<RetrofitInstance> call = s.checkAvail(retrofitInstance);
                    alertDialog.show();
                    Objects.requireNonNull(alertDialog.getWindow()).setLayout(600, 500);

                    call.enqueue(new Callback<RetrofitInstance>() {
                        @Override
                        public void onResponse(Call<RetrofitInstance> call, Response<RetrofitInstance> response) {
                            if (!response.isSuccessful()) {
                                //  Toast.makeText(MedicalPersonSignupActivity.this, "Dobbindi royy", Toast.LENGTH_SHORT).show();
                                Log.e("raledu", "" + response.code());
                                return;
                            }
                            if (response.body() != null) {
                                String res = response.body().getResponse();
                                String msg = response.body().getMessage();
                                Log.e("confusion", "" + res);
                                if (res.equals("0")) {
                                    alertDialog.dismiss();
                                    final AlertDialog.Builder alertBuilder1 = new AlertDialog.Builder(MedicalPersonSignupActivity.this);
                                    alertBuilder1.setTitle("UserID already registered by other users");
                                    alertBuilder1.setMessage(msg);

                                    alertBuilder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // imgClose.setVisibility(View.VISIBLE);
                                            imgCheckAvailability.setImageResource(R.drawable.close);
                                            alertBuilder1.create().dismiss();
                                        }
                                    });

                                    alertBuilder1.create().show();
                                    //  edtName.setTextColor(Color.parseColor("#ED5276"));
                                    //  Toast.makeText(MedicalPersonSignupActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }
                                if (res.equals("3")) {
                                    alertDialog.dismiss();
                                    //  imageView.setVisibility(View.VISIBLE);
                                    imgCheckAvailability.setImageResource(R.drawable.tick);
                                    Toast.makeText(MedicalPersonSignupActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    //  edtName.setTextColor(Color.parseColor("#1b5f2d"));
                                }
                            }
                            Log.e("vachindi", "" + response.body());
                        }

                        @Override
                        public void onFailure(Call<RetrofitInstance> call, Throwable t) {
                            alertDialog.dismiss();
                            Toast.makeText(MedicalPersonSignupActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                            // Log.e("dobindi",""+t.getMessage());
                        }
                    });
                }
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Mchecked || !Uchecked)
                    dialogeforCheckavilability("Verification Failed", "Kindly check the availability for user name and email entered ", "ok");
                else if (ed_Password.getText().toString().isEmpty())
                    dialogeforCheckavilability("Error", "ed_Password can not empty", "ok");
                else if (ed_countyCode.getText().toString().isEmpty())
                    dialogeforCheckavilability("Error", "Country code can not empty", "ok");
                else if (ed_phoneNumber.getText().toString().isEmpty())
                    dialogeforCheckavilability("Error", "ed_phoneNumber can not empty", "ok");
                else if (ed_age.getText().toString().isEmpty())
                    dialogeforCheckavilability("Error", "ed_age can not empty", "ok");
                else if (ed_lastName.getText().toString().isEmpty())
                    dialogeforCheckavilability("Error", "LastName can not empty", "ok");
                else if (ed_fistname.getText().toString().isEmpty())
                    dialogeforCheckavilability("Error", "FirstName can not empty", "ok");
                else if (ed_conform_p.getText().toString().isEmpty())
                    dialogeforCheckavilability("Error", "Conform password can not be empty", "ok");
                else if (!ed_Password.getText().toString().matches(ed_conform_p.getText().toString())) {
                    dialogeforCheckavilability("Error", "ed_Password does not mached", "ok");
                } else if (ed_hospitalRegistrationNum.getText().toString().isEmpty())
                    dialogeforCheckavilability("Error", "Hospital registration number can not empty", "ok");
                else if (ed_IdGivenGovt.getText().toString().isEmpty())
                    dialogeforCheckavilability("Error", "Id given by Govt can not be empty", "ok");
                else if (userTypeSpinner.getSelectedItem().toString().isEmpty()) {
                    dialogeforCheckavilability("Error", "User type can not be empty", "ok");
                } else {
                    if (qualificationSpinner.getSelectedItem().toString().isEmpty())
                        dialogeforCheckavilability("Error", "qualificationSpinner can not be empty", "ok");
                    else if (departmentSpinner.getSelectedItem().toString().isEmpty())
                        dialogeforCheckavilability("Error", "departmentSpinner can not be empty", "ok");
                    else if (specialzationSpinner.getSelectedItem().toString().isEmpty())
                        dialogeforCheckavilability("Error", "specialzationSpinner can not be empty", "ok");
                    else
                        alertDialog.show();
                    Objects.requireNonNull(alertDialog.getWindow()).setLayout(600, 500);
                    RegisterApi();
                }
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                imageVV = true;
              // cameraDailog();
                cameraBottomSheet();
            }
        });
  loadUserTypeSpinner();
    }
    public boolean isConn() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
    }

    public void loadEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        profileBase64Obj = Base64.encodeToString(imageInByte, Base64.NO_WRAP);
        Log.e("base64Image", "call" + profileBase64Obj);

    }
    private void loadUserTypeSpinner(){
       ArrayAdapter adapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,empArray){
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                View v = super.getDropDownView(position, convertView, parent);
                TextView t = new TextView(getContext());
                if (position == 0) {
                    t.setHeight(0);
                    t.setVisibility(View.GONE);
                    v = t;
                } else {
                    v = super.getDropDownView(position, null, parent);
                }
                return v;

            }
        };
        userTypeSpinner.setAdapter(adapter);

        if(!selectedUserType.isEmpty()){

            Log.e("empvalueeeeeeeeee","dd"+ Arrays.asList(empArray).indexOf(selectedUserType));
            userTypeSpinner.setSelection(Arrays.asList(empArray).indexOf(selectedUserType));
        }
        userTypeSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                      /*  InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                      */  selectedUserType=parent.getItemAtPosition(position).toString();
                        ((TextView)view).setText(null);
                        spinn.setText(selectedUserType);
                        if (!selectedUserType.equals("")){
                            loadSpecializationSpinner();
                            loadDepartmentSpinner();
                            loadDoctorQulificationSpinner();
                        }
                        if (selectedUserType.equals("Doctor")){
                            loadDoctorQulificationSpinner();

                        } if (selectedUserType.equals("Nurse")) {
                            loadNurseQulificationSpinner();
                        }
                        Log.e("checking",""+selectedUserType);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Log.e("onNothingSelected",""+selectedUserType);

                    }
                }
        );

    }
    private void loadSpecializationSpinner(){
        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,specializationArray) {
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
               /* InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);*/
                View v = super.getDropDownView(position, convertView, parent);
                TextView t = new TextView(getContext());
                if (position == 0) {
                    t.setHeight(0);
                    t.setVisibility(View.GONE);
                    v = t;
                } else {
                    v = super.getDropDownView(position, null, parent);
                }
                return v;

            }
        };
        specialzationSpinner.setAdapter(adapter);
        /*if(!selectedSpecialization.isEmpty()){
            Log.e("empvalueeeeeeeeee","dd"+ Arrays.asList(specializationArray).indexOf(selectedSpecialization));
            specialzationSpinner.setSelection(Arrays.asList(specializationArray).indexOf(selectedSpecialization));
        }*/
        specialzationSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedSpecialization=parent.getItemAtPosition(position).toString();
                        edtSpecialization.setText(selectedSpecialization);
                        ((TextView)view).setText(null);
                        Log.e("checking",""+selectedSpecialization);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Log.e("selectedSpecialization",""+selectedSpecialization);

                    }
                }
        );

    }
    private void loadDepartmentSpinner(){
    ArrayAdapter adapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,departmentsArray) {
        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            /*InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
*/
            View v = super.getDropDownView(position, convertView, parent);
            TextView t = new TextView(getContext());
            if (position == 0) {
                t.setHeight(0);
                t.setVisibility(View.GONE);
                v = t;
            } else {
                v = super.getDropDownView(position, null, parent);
            }
            return v;

        }
    };
    departmentSpinner.setAdapter(adapter);
    if (!selectedDepartment.isEmpty()){
        departmentSpinner.setSelection(Arrays.asList(departmentsArray).indexOf(selectedDepartment));
    }
    departmentSpinner.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedDepartment = parent.getItemAtPosition(position).toString();
                    ((TextView)view).setText(null);
                    edtDepart.setText(selectedDepartment);

                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Log.e("selectedDepartment",""+selectedDepartment);
                }
            }
    );
    }

    private void loadDoctorQulificationSpinner(){
        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,doctorQualificationsArray) {
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                /*InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
*/
                View v = super.getDropDownView(position, convertView, parent);
                TextView t = new TextView(getContext());
                if (position == 0) {
                    t.setHeight(0);
                    t.setVisibility(View.GONE);
                    v = t;
                } else {
                    v = super.getDropDownView(position, null, parent);
                }
                return v;

            }
        };
        qualificationSpinner.setAdapter(adapter);
       /* if (!selectedQualification.isEmpty()){

            qualificationSpinner.setSelection(Arrays.asList(doctorQualificationsArray).indexOf(selectedQualification));
        }*/
        qualificationSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedQualification =parent.getItemAtPosition(position).toString();
                        ((TextView)view).setText(null);
                        edtQulaify.setText(selectedQualification);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Log.e("selectedQualification",""+selectedQualification);

                    }
                }
        );
    }
    private void loadNurseQulificationSpinner(){
        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,nurseQualificationsArray) {
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
               /* InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
*/
                View v = super.getDropDownView(position, convertView, parent);
                TextView t = new TextView(getContext());
                if (position == 0) {
                    t.setHeight(0);
                    t.setVisibility(View.GONE);
                    v = t;
                } else {
                    v = super.getDropDownView(position, null, parent);
                }
                return v;
            }
        };
        qualificationSpinner.setAdapter(adapter);
        if (!selectedQualification.isEmpty()){
            qualificationSpinner.setSelection(Arrays.asList(nurseQualificationsArray).indexOf(selectedQualification));
        }
        qualificationSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //selectedQualification =parent.getItemAtPosition(position).toString();
                        selectedQualification =parent.getItemAtPosition(position).toString();
                        ((TextView)view).setText(null);
                        edtQulaify.setText(selectedQualification);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Log.e("selectedQualification",""+selectedQualification);

                    }
                }
        );
    }

    private void getDataFromSharedPreferences() {
        final MedicalProfileModel currentMedicalProfile=MedicalProfileDataController.getInstance().currentMedicalProfile;

        ed_IdGivenGovt.setText(currentMedicalProfile.getId_By_govt());
        //ed_IdGivenGovt.setEnabled(false);
        edtMail.setText(currentMedicalProfile.getEmailId());
        edtMail.setEnabled(false);

        edtName.setText(currentMedicalProfile.getUserId());
        edtName.setEnabled(false);
         Log.e("userid","call"+currentMedicalProfile.getUserId());

        txt_dob.setText(currentMedicalProfile.getDob());
        ed_age.setText(currentMedicalProfile.getAge());

        medicPersonId.setText(currentMedicalProfile.getMedical_person_id());
        medicPersonId.setEnabled(false);

        selectedUserType=currentMedicalProfile.getUserType();
        selectedSpecialization=currentMedicalProfile.getSpecialzation();
        selectedDepartment = currentMedicalProfile.getDepartment();
        selectedQualification=currentMedicalProfile.getQualification();
        Log.e("qualifiy","call"+currentMedicalProfile.getProfilePic());

        ed_fistname.setText(currentMedicalProfile.getFirstName());
        ed_lastName.setText(currentMedicalProfile.getLastName());

        edtName.setText(currentMedicalProfile.getUserId());
        edtName.setEnabled(false);
        ed_phoneNumber.setText(currentMedicalProfile.getPhoneNumber());
        ed_countyCode.setText(currentMedicalProfile.getPhone_coutryCode());
      //  Picasso.get().load(currentMedicalProfile.getProfilePic()).into(profilePic);
        Picasso.get().load(currentMedicalProfile.getProfilePic()).resize(350,350).placeholder(R.drawable.profile_1).into(profilePic);


        String genderTxt =currentMedicalProfile.getGender();
        if (genderTxt.equals("Male")) {
            cb1.setChecked(true);
            txtGender.setText("Male");
        }else if(genderTxt.equals("Female")){
            cb2.setChecked(true);
            txtGender.setText("Female");
        }else{
            cb3.setChecked(true);
            txtGender.setText("Other");
        }

        if(currentMedicalProfile.getProfileByteArray() !=null && currentMedicalProfile.getProfileByteArray().length>0){
            bitmap=PersonalInfoController.getInstance().convertByteArrayTOBitmap(currentMedicalProfile.getProfileByteArray());
            profilePic.setImageBitmap(bitmap);
            Log.e("profileCheckfromdb", "" + bitmap);
            loadEncoded64ImageStringFromBitmap(bitmap);
            oldProfielBase64Obj = profileBase64Obj;
        }else {
            Picasso.get().load(currentMedicalProfile.getProfilePic()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap1, Picasso.LoadedFrom from) {
                    // loaded bitmap is here (bitmap)
                    Log.e("profileCheckfromurl", "" + bitmap1);
                    bitmap = bitmap1;
                    file = createImageFileFromBitmap(bitmap1);
                    loadEncoded64ImageStringFromBitmap(bitmap);
                    oldProfielBase64Obj = profileBase64Obj;
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });
        }

        Update.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                  file= createImageFileFromBitmap(bitmap);
                        if (checkProfielDataNotChanged()) {
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            return;
                        }

                    alertDialog.show();
                    Objects.requireNonNull(alertDialog.getWindow()).setLayout(600, 500);
                   /* if (cb1.isChecked()) {
                        txt_gender = cb1.getText().toString();
                    }
                    if (cb2.isChecked()) {
                        txt_gender = cb2.getText().toString();
                    }
                    if (cb3.isChecked()) {
                        txt_gender = cb3.getText().toString();
                    }*/
                    final RequestBody image;
                    String PreferLanguage = "English";
                    OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
                    okhttpClientBuilder.connectTimeout(45, TimeUnit.SECONDS);
                    okhttpClientBuilder.readTimeout(45, TimeUnit.SECONDS);
                    okhttpClientBuilder.writeTimeout(45, TimeUnit.SECONDS);
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                            .client(okhttpClientBuilder.build())
                            .addConverterFactory(GsonConverterFactory.create()).build();
                    ServerApi serverApi = retrofit.create(ServerApi.class);
                    MultipartBody.Part profilePic;
                    image = RequestBody.create(MediaType.parse("image/*"), file);
                    profilePic = MultipartBody.Part.createFormData("profilePic", file.getName(), image);
                    userID = RequestBody.create(MediaType.parse("text/plain"), edtName.getText().toString());
                    firstName = RequestBody.create(MediaType.parse("text/plain"), ed_fistname.getText().toString());
                    LastName = RequestBody.create(MediaType.parse("text/plain"), ed_lastName.getText().toString());
                    age = RequestBody.create(MediaType.parse("text/plain"), ed_age.getText().toString());
                    preferLanguage = RequestBody.create(MediaType.parse("text/plain"), PreferLanguage);
                    userType = RequestBody.create(MediaType.parse("text/plain"), userTypeSpinner.getSelectedItem().toString());
                    gender = RequestBody.create(MediaType.parse("text/plain"), txtGender.getText().toString());
                    MedicPersonId = RequestBody.create(MediaType.parse("text/plain"), medicPersonId.getText().toString());
                    Dob = RequestBody.create(MediaType.parse("text/plain"),txt_dob.getText().toString());
                    phoneNumber = RequestBody.create(MediaType.parse("text/plain"), ed_phoneNumber.getText().toString());
                    phoneNumberCountryCode = RequestBody.create(MediaType.parse("text/plain"), ed_countyCode.getText().toString());
                    id_by_govt = RequestBody.create(MediaType.parse("text/plain"), ed_IdGivenGovt.getText().toString());
                    // phoneNumberExtension =RequestBody.create(MediaType.parse("text/plain"),"3");
                    specialization = RequestBody.create(MediaType.parse("text/plain"), specialzationSpinner.getSelectedItem().toString());
                    qualification = RequestBody.create(MediaType.parse("text/plain"), qualificationSpinner.getSelectedItem().toString());
                    department = RequestBody.create(MediaType.parse("text/plain"), departmentSpinner.getSelectedItem().toString());

                    Call<RetrofitInstance> call = serverApi.update(currentMedicalProfile.getAccessToken(), profilePic, userID, firstName, LastName, phoneNumber
                            , phoneNumberCountryCode
                            , qualification, specialization, department, userType, preferLanguage
                            , MedicPersonId, gender, age, Dob,id_by_govt);
                    Log.e("accesToken1", "is" + currentMedicalProfile.getAccessToken());

                    call.enqueue(new Callback<RetrofitInstance>() {
                        @Override
                        public void onResponse(Call<RetrofitInstance> call, Response<RetrofitInstance> response) {
                            Log.e("Reliance", "response" + response.body());
                            if(response.body()==null){
                                alertDialog.dismiss();
                            }
                            if (response.body() != null) {
                                alertDialog.dismiss();
                                String resp = response.body().getMessage();
                                Log.e("RetrofitInstance", "" + response.body().getResponse());
                                String code = response.body().getResponse();
                                if (code.equals("5")){
                                    LayoutInflater inflater = getLayoutInflater();
                                    View layout = inflater.inflate(R.layout.toast_layout,
                                            (ViewGroup) findViewById(R.id.custom_toast_container));

                                    TextView text = (TextView) layout.findViewById(R.id.text);
                                    text.setText(response.body().getMessage());
                                    Toast toast = new Toast(MedicalPersonSignupActivity.this);
                                    //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                    toast.setDuration(Toast.LENGTH_LONG);
                                    toast.setView(layout);
                                    toast.show();
                                } else if (code.equals("0")){
                                    LayoutInflater inflater = getLayoutInflater();
                                    View layout = inflater.inflate(R.layout.toast_layout,
                                            (ViewGroup) findViewById(R.id.custom_toast_container));

                                    TextView text = (TextView) layout.findViewById(R.id.text);
                                    text.setText(response.body().getMessage());
                                    Toast toast = new Toast(MedicalPersonSignupActivity.this);
                                    //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                    toast.setDuration(Toast.LENGTH_LONG);
                                    toast.setView(layout);
                                    toast.show();
                                }
                                else if(response.body().getResponse().equals("3")){
                                    Log.e("profilepic","call"+response.body().getProfilePic());
                                    final String urlString = ServerApi.img_home_url + response.body().getProfilePic();
                                    Log.e("PersonalURL", urlString);
                                    final MedicalProfileModel objModel=new MedicalProfileModel();
                                    objModel.setPhone_coutryCode(ed_countyCode.getText().toString());
                                    objModel.setPhoneNumber(ed_phoneNumber.getText().toString());
                                    objModel.setPassword(objModel.getPassword());
                                    objModel.setPrefferLanguage("English");
                                    objModel.setQualification(selectedQualification);
                                    objModel.setSpecialzation(selectedSpecialization);
                                    Log.e("testing", ""+currentMedicalProfile.getUserId());
                                    objModel.setUserId(currentMedicalProfile.getUserId());
                                    objModel.setUserType(selectedUserType);
                                    objModel.setAccessToken(currentMedicalProfile.getAccessToken());//load exist token isFromMedicalPerson db
                                    objModel.setAge(ed_age.getText().toString());
                                    objModel.setDepartment(selectedDepartment);
                                    objModel.setDob(txt_dob.getText().toString());
                                    objModel.setEmailId(edtMail.getText().toString());
                                    objModel.setFirstName(ed_fistname.getText().toString());
                                    objModel.setGender(txtGender.getText().toString());
                                    objModel.setHospital_reg_number(objModel.getHospital_reg_number());
                                    objModel.setId_By_govt(ed_IdGivenGovt.getText().toString());
                                    objModel.setLastName(ed_lastName.getText().toString());
                                    objModel.setLotitude(String.valueOf(LocationTracker.getInstance().currentLocation.getLatitude()));
                                    objModel.setLongitude(String.valueOf(LocationTracker.getInstance().currentLocation.getLongitude()));
                                    objModel.setMedical_person_id(medicPersonId.getText().toString());
                                    objModel.setProfilePic(urlString);
                                    objModel.setProfileByteArray(null);

                                    if(MedicalProfileDataController.getInstance().updateMedicalProfileData(objModel)){
                                        Log.e("testing", ""+currentMedicalProfile.getUserId());
                                        Intent intent = new Intent(MedicalPersonSignupActivity.this, HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        LayoutInflater inflater = getLayoutInflater();
                                        View layout = inflater.inflate(R.layout.toast_layout,
                                                (ViewGroup) findViewById(R.id.custom_toast_container));
                                        TextView text = (TextView) layout.findViewById(R.id.text);
                                        text.setText(response.body().getMessage());
                                        Toast toast = new Toast(MedicalPersonSignupActivity.this);
                                        toast.setDuration(Toast.LENGTH_LONG);
                                        toast.setView(layout);
                                        toast.show();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<RetrofitInstance> call, Throwable t) {
                            alertDialog.dismiss();
                            Toast.makeText(MedicalPersonSignupActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            t.printStackTrace();
                        }
                    });
                }
            });

    }
    public String loadEncod1ed64ImageStringFromBitmap(Bitmap bitmap1) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        String profileBase64Obj = Base64.encodeToString(imageInByte, Base64.NO_WRAP);
        Log.e("base64Image", "call" + profileBase64Obj);
        return profileBase64Obj;
    }
    public  Bitmap convertBase64ToBitmap( String base64){
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return  decodedByte;
    }
    public boolean checkProfielDataNotChanged() {
        MedicalProfileModel obj=MedicalProfileDataController.getInstance().currentMedicalProfile;
        if (obj.getLastName().equals(ed_lastName.getText().toString()) && obj.getFirstName().equals(ed_fistname.getText().toString())
                && obj.getDob().equals(txt_dob.getText().toString()) && obj.getAge().equals(ed_age.getText().toString()) &&
                obj.getPhone_coutryCode().equals(ed_countyCode.getText().toString()) && obj.getPhoneNumber().equals(ed_phoneNumber.getText().toString())
                && obj.getId_By_govt().equals(ed_IdGivenGovt.getText().toString()) &&  obj.getSpecialzation().equals(selectedSpecialization)
                && obj.getQualification().equals(selectedQualification) && obj.getDepartment().equals(selectedDepartment)
        && oldProfielBase64Obj.equals(profileBase64Obj) && obj.getGender().equals(txtGender.getText().toString())){
            return true;
        }
        return false;
    }
    public static final int CAMERA_REQUEST_CODE = 1001;
    public static final int GALLERY_REQUEST_CODE = 1002;
    public static final int CAMERA_PERMISSION_CODE = 5001;
    public static final int GALLERY_PERMISSION_CODE = 5002;
    private String mCurrentPhotoPath;

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = false;
        options.inPurgeable = true;

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap bitmap1= BitmapFactory.decodeFile(mCurrentPhotoPath, options);
            Bitmap rotatedBitmap = rotatedImageBitmap(mCurrentPhotoPath, bitmap1);
            bitmap=rotatedBitmap;
            profilePic.setImageBitmap(getResizedBitmap(rotatedBitmap, 500));
            loadEncoded64ImageStringFromBitmap(bitmap);
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            Toast.makeText(MedicalPersonSignupActivity.this, "Image Capturing Cancelled", Toast.LENGTH_SHORT).show();
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri selectedImage = imageReturnedIntent.getData();

            if (selectedImage != null && selectedImage.toString()
                    .startsWith("content://com.google.android.apps.photos.content")) {
                if (selectedImage.toString().contains("video")) {
                    Toast.makeText(this, "Hey ! It's Video Buddy", Toast.LENGTH_SHORT).show();
                    return;
                }
                createImageFromPhotosUri(selectedImage);
            } else {
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mCurrentPhotoPath = cursor.getString(columnIndex);
                cursor.close();
                options.inSampleSize = 2;
                bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, options);
                loadEncoded64ImageStringFromBitmap(bitmap);
            }
            Bitmap rotatedBitmap = rotatedImageBitmap(mCurrentPhotoPath, bitmap);
            profilePic.setImageBitmap(getResizedBitmap(rotatedBitmap, 500));
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            Toast.makeText(MedicalPersonSignupActivity.this, "Image Selection Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void createImageFromPhotosUri(Uri selectedImage) {
        try {
            InputStream is = getContentResolver().openInputStream(selectedImage);
            if (is != null) {
                bitmap = BitmapFactory.decodeStream(is);
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outStream);
                File f = createImageFile();
                try {
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(outStream.toByteArray());
                    fo.flush();
                    fo.close();
                } catch (IOException e) {
                    Log.w("TAG", "Error saving image file: " + e.getMessage());

                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private File createImageFileFromBitmap(Bitmap bitmap) {
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outStream);
            File f = createImageFile();
            Log.e("qqqqqqqq",""+bitmap+f.getName());

            try {
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(outStream.toByteArray());
                fo.flush();
                fo.close();
                return f;
            } catch (IOException e) {
                Log.w("TAG", "Error saving image file: " + e.getMessage());
                return null;
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void LoadImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }


    private void LoadCaptureImageScreen() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(MedicalPersonSignupActivity.this,
                        "com.vedas.spectrocare.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private Bitmap rotatedImageBitmap(String photoPath, Bitmap bitmap) {
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(photoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }
        return rotatedBitmap;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public void cameraDailog() {
        TextView cam, gal, canc;
        dialog1 = new AlertDialog.Builder(MedicalPersonSignupActivity.this);
        inflater = MedicalPersonSignupActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.camera_dailog, null);
        dialog1.setView(dialogView);
        cam = dialogView.findViewById(R.id.camera);
        gal = dialogView.findViewById(R.id.gallery);
        canc = dialogView.findViewById(R.id.cancel);
        canc.setTextColor(Color.parseColor("#ED5276"));

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MedicalPersonSignupActivity.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    dialog.dismiss();
                    LoadCaptureImageScreen();
                } else {
                    requestCameraPermission();
                }
            }
        });
        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MedicalPersonSignupActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    dialog.dismiss();
                    LoadImageFromGallery();
                } else {
                    requestStoragePermission();
                }
            }
        });
        canc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog = dialog1.create();
        dialog.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LoadCaptureImageScreen();
                } else {
                    Toast.makeText(MedicalPersonSignupActivity.this, "Yay! You Denied Permission", Toast.LENGTH_SHORT).show();
                }
                break;
            case GALLERY_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LoadImageFromGallery();
                } else {
                    Toast.makeText(MedicalPersonSignupActivity.this, "Yay! You Denied Permission", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Permission Info")
                    .setMessage("Camera Permission is Needed for Adding your Profile Image")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MedicalPersonSignupActivity.this,
                                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();

        } else {
            ActivityCompat.requestPermissions(MedicalPersonSignupActivity.this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Permission Info")
                    .setMessage("Gallery Permission is needed for adding your Profile Image")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.dismiss();
                            ActivityCompat.requestPermissions(MedicalPersonSignupActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.dismiss();
                            dialogInterface.dismiss();
                        }
                    })
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(MedicalPersonSignupActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
        }
    }

    public void casting() {
        txtLogin = findViewById(R.id.txt_login);
        txtAlready = findViewById(R.id.txt);
        Update = findViewById(R.id.btn_update);
        imageView = findViewById(R.id.img_tick);
        layoutMedicalPersonnelID = findViewById(R.id.layout_medic_person);
        spinn = findViewById(R.id.edt_e);
        layoutEdtMail = findViewById(R.id.layout_edit_mail);
        medicPersonId = findViewById(R.id.edt_medical_person_id);
        edtSpecialization = findViewById(R.id.edt_special);
        edtDepart =findViewById(R.id.edt_depart);
        edtQulaify = findViewById(R.id.edt_qualify);
        imgClose =findViewById(R.id.img_close);
        imgTick = findViewById(R.id.img_mail_tick);
        imgMailClose =findViewById(R.id.img_mail_close);
        imgCheckAvailability = findViewById(R.id.img_user_availability);
        imgMailCheckAvailability = findViewById(R.id.img_email_availability);
     //   textView1 = findViewById(R.id.txt_medcId);
      //  Star1 = findViewById(R.id.txtStar1);
       // Star = findViewById(R.id.medic_star);
        visionMp = findViewById(R.id.visible_mp);
        invisionMp = findViewById(R.id.in_visible_mp);
        vision = findViewById(R.id.visible_mp2);
        inVision = findViewById(R.id.in_visible_mp2);
        userTypeSpinner = findViewById(R.id.edt_user_type);
        Back = findViewById(R.id.back);
        ed_countyCode = findViewById(R.id.edt_county_code);
        specialzationSpinner = findViewById(R.id.edt_specialization);
        qualificationSpinner = findViewById(R.id.edt_qualification);
        departmentSpinner = findViewById(R.id.edt_department);
        ed_Password = findViewById(R.id.edit_mp_password);
        check_user = findViewById(R.id.user_availability);
        check_email = findViewById(R.id.email_availability);
        ed_lastName = findViewById(R.id.edt_last_name);
        ed_fistname = findViewById(R.id.edt_first_name);
        ed_phoneNumber = findViewById(R.id.edt_phone_no);
        ed_hospitalRegistrationNum = findViewById(R.id.edt_hospital_registered_no);
        ed_IdGivenGovt = findViewById(R.id.edt_id_given_by_govt);
        edtName = findViewById(R.id.edt_name);
        edtMail = findViewById(R.id.edt_email);
        txt_dob = findViewById(R.id.date);
        ed_age = findViewById(R.id.edt_age);
        txtGender = findViewById(R.id.txt_gender_check);
        cb1 = findViewById(R.id.male);
        cb2 = findViewById(R.id.female);
        cb3 = findViewById(R.id.other);
        profilePic = findViewById(R.id.imv);
        layoutHospitalReg = findViewById(R.id.layout_medic_reg_no);
        ed_conform_p = findViewById(R.id.edit_conform_password);
    }

    public void toggle(final ImageView Imv, final ImageView ImI, final EditText pa) {
        ImI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pa.post(new Runnable() {
                    @Override
                    public void run() {
                        pa.setSelection(pa.length());
                    }
                });
                ImI.setVisibility(View.GONE);
                Imv.setVisibility(View.VISIBLE);
                pa.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                Imv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pa.post(new Runnable() {
                            @Override
                            public void run() {
                                pa.setSelection(pa.length());
                            }
                        });
                        Imv.setVisibility(View.GONE);
                        ImI.setVisibility(View.VISIBLE);
                        pa.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                });
            }
        });
    }

    public void PasswordToggleListners() {
        toggle(visionMp, invisionMp, ed_Password);
        toggle(vision, inVision, ed_conform_p);

    }

   /* public void check(final RadioButton c, final RadioButton c1, final RadioButton c2) {
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (c.isChecked()) {
                    c1.setChecked(false);
                    c2.setChecked(false);
                }
            }
        });
    }

    public void CheckBox() {
        check(cb1, cb2, cb3);
        check(cb2, cb1, cb3);
        check(cb3, cb1, cb2);
        if (cb1.isChecked())
            txtGender.setText("Male");

    }
*/
    public void AdaptorListners() {
       // DatePicker();
        retroInstance = new RetrofitInstance();
    }

    /*public void RegisterApi() {
        File file ;
        RequestBody image;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ServerApi s = retrofit.create(ServerApi.class);

        String Latitude = "14.6738";
        String Longitude = "15.7873";
        String PreferLanguage = "English";

       *//* if (cb1.isChecked()) {
            txt_gender = cb1.getText().toString();
        }
        if (cb2.isChecked()) {
            txt_gender = cb2.getText().toString();
        }
        if (cb3.isChecked()) {
            txt_gender = cb3.getText().toString();
        }*//*

        if (mCurrentPhotoPath == null) {
            Bitmap bitMap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_human_dummy3x);
            file = createImageFileFromBitmap(bitMap);
        } else {
            file = new File(mCurrentPhotoPath);
        }
        String dt = txt_dob.getText().toString();
        MultipartBody.Part profilePic;
        image = RequestBody.create(MediaType.parse("image/*"), file);
        profilePic = MultipartBody.Part.createFormData("profilePic", file.getName(), image);
        userID = RequestBody.create(MediaType.parse("text/plain"), edtName.getText().toString());
        password = RequestBody.create(MediaType.parse("text/plain"), ed_Password.getText().toString());
        firstName = RequestBody.create(MediaType.parse("text/plain"), ed_fistname.getText().toString());
        LastName = RequestBody.create(MediaType.parse("text/plain"), ed_lastName.getText().toString());
        phoneNumber = RequestBody.create(MediaType.parse("text/plain"), ed_phoneNumber.getText().toString());
        userType = RequestBody.create(MediaType.parse("text/plain"), userTypeSpinner.getSelectedItem().toString());
        specialization = RequestBody.create(MediaType.parse("text/plain"), specialzationSpinner.getSelectedItem().toString());
        qualification = RequestBody.create(MediaType.parse("text/plain"), qualificationSpinner.getSelectedItem().toString());
        department = RequestBody.create(MediaType.parse("text/plain"), departmentSpinner.getSelectedItem().toString());
        phoneNumberCountryCode = RequestBody.create(MediaType.parse("text/plain"), ed_countyCode.getText().toString());
        emailID = RequestBody.create(MediaType.parse("text/plain"), edtMail.getText().toString());
        Dob = RequestBody.create(MediaType.parse("text/plain"), dt);
        Log.e("maii",""+emailID);
        id_by_govt = RequestBody.create(MediaType.parse("text/plain"), ed_IdGivenGovt.getText().toString());
        gender = RequestBody.create(MediaType.parse("text/plain"), txtGender.getText().toString());
        hospital_reg_num = RequestBody.create(MediaType.parse("text/plain"), ed_hospitalRegistrationNum.getText().toString());
        age = RequestBody.create(MediaType.parse("text/plain"), ed_age.getText().toString());
        latitude = RequestBody.create(MediaType.parse("text/plain"), Latitude);
        longitude = RequestBody.create(MediaType.parse("text/plain"), Longitude);
        preferLanguage = RequestBody.create(MediaType.parse("text/plain"), PreferLanguage);

        Call<RetrofitInstance> call = s.register(profilePic, userID, password, firstName, LastName, phoneNumber,
                phoneNumberCountryCode,
                emailID, qualification, specialization, department, userType, id_by_govt, preferLanguage,
                latitude, longitude, hospital_reg_num, gender, age,Dob);
        call.enqueue(new Callback<RetrofitInstance>() {
            @Override
            public void onResponse(Call<RetrofitInstance> call, Response<RetrofitInstance> response) {
                Log.e("Reliance", "response" + response.body());
                if (response.body() != null) {
                    alertDialog.dismiss();
                    String resp = response.body().getMessage();
                    //Toast.makeText(MedicalPersonSignupActivity.this, resp, Toast.LENGTH_SHORT).show();
                    Log.e("RetrofitInstance", "" + resp);
                    String code = response.body().getResponse();
                    if (code.equals("0")){
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast_layout,
                                (ViewGroup) findViewById(R.id.custom_toast_container));

                        TextView text = (TextView) layout.findViewById(R.id.text);
                        text.setText(response.body().getMessage());
                        Toast toast = new Toast(MedicalPersonSignupActivity.this);
                        //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                    }
                    if (response.body().getResponse().equals("3")){
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast_layout,
                                (ViewGroup) findViewById(R.id.custom_toast_container));

                        TextView text = (TextView) layout.findViewById(R.id.text);
                        text.setText(response.body().getMessage());
                       *//* Toast toast = new Toast(MedicalPersonSignupActivity.this);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();*//*
                        final AlertDialog.Builder alertBuilder2 = new AlertDialog.Builder(MedicalPersonSignupActivity.this);
                        alertBuilder2.setTitle("UserID already registered by other users");
                        alertBuilder2.setMessage(response.body().getMessage());

                        alertBuilder2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // imgMailClose.setVisibility(View.VISIBLE);
                                Intent intent = new Intent(MedicalPersonSignupActivity.this,LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                alertBuilder2.create().dismiss();
                            }
                        });
                        alertBuilder2.create().show();
                       *//* Intent intent = new Intent(MedicalPersonSignupActivity.this,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                   *//* }
                }
            }

            @Override
            public void onFailure(Call<RetrofitInstance> call, Throwable t) {
                // Log.e("jio","response"+t.toString());
                alertDialog.dismiss();
                t.printStackTrace();
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toast_layout,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText(t.getMessage());
                Toast toast = new Toast(MedicalPersonSignupActivity.this);
                //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
               // Toast.makeText(MedicalPersonSignupActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    public void RegisterApi() {
        File file ;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ServerApi s = retrofit.create(ServerApi.class);

        String Latitude = "14.6738";
        String Longitude = "15.7873";
        String PreferLanguage = "English";

       /* if (cb1.isChecked()) {
            txt_gender = cb1.getText().toString();
        }
        if (cb2.isChecked()) {
            txt_gender = cb2.getText().toString();
        }
        if (cb3.isChecked()) {
            txt_gender = cb3.getText().toString();
        }*/

        if (mCurrentPhotoPath == null) {
            Bitmap bitMap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_human_dummy3x);
            file = createImageFileFromBitmap(bitMap);
        } else {
            file = new File(mCurrentPhotoPath);
        }
        String dt = txt_dob.getText().toString();
        MultipartBody.Part profilePic;
        RequestBody image = RequestBody.create(MediaType.parse("image/*"), file);
        profilePic = MultipartBody.Part.createFormData("profilePic", file.getName(), image);
        RequestBody userID = RequestBody.create(MediaType.parse("text/plain"), edtName.getText().toString());
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), ed_Password.getText().toString());
        RequestBody firstName = RequestBody.create(MediaType.parse("text/plain"), ed_fistname.getText().toString());
        RequestBody LastName = RequestBody.create(MediaType.parse("text/plain"), ed_lastName.getText().toString());
        RequestBody phoneNumber = RequestBody.create(MediaType.parse("text/plain"), ed_phoneNumber.getText().toString());
        RequestBody userType = RequestBody.create(MediaType.parse("text/plain"), userTypeSpinner.getSelectedItem().toString());
        RequestBody specialization = RequestBody.create(MediaType.parse("text/plain"), specialzationSpinner.getSelectedItem().toString());
        RequestBody qualification = RequestBody.create(MediaType.parse("text/plain"), qualificationSpinner.getSelectedItem().toString());
        RequestBody department = RequestBody.create(MediaType.parse("text/plain"), departmentSpinner.getSelectedItem().toString());
        RequestBody phoneNumberCountryCode = RequestBody.create(MediaType.parse("text/plain"), ed_countyCode.getText().toString());
        RequestBody emailID = RequestBody.create(MediaType.parse("text/plain"), edtMail.getText().toString());
        RequestBody  Dob = RequestBody.create(MediaType.parse("text/plain"), dt);
        Log.e("maii",""+emailID);
        RequestBody id_by_govt = RequestBody.create(MediaType.parse("text/plain"), ed_IdGivenGovt.getText().toString());
        RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), txtGender.getText().toString());
        RequestBody hospital_reg_num = RequestBody.create(MediaType.parse("text/plain"), ed_hospitalRegistrationNum.getText().toString());
        RequestBody age = RequestBody.create(MediaType.parse("text/plain"), ed_age.getText().toString());
        RequestBody  latitude = RequestBody.create(MediaType.parse("text/plain"), Latitude);
        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), Longitude);
        RequestBody preferLanguage = RequestBody.create(MediaType.parse("text/plain"), PreferLanguage);
        RequestBody emergencyPhone = RequestBody.create(MediaType.parse("text/plain"), "8954345634");
        RequestBody emergencyCountryCode = RequestBody.create(MediaType.parse("text/plain"), "91");
        RequestBody extension = RequestBody.create(MediaType.parse("text/plain"), "0");

        Call<RetrofitInstance> call = s.register(profilePic, userID, password, firstName, LastName, phoneNumber,
                phoneNumberCountryCode,
                emailID, /*qualification, specialization,*/ department, userType,/*id_by_govt*/ preferLanguage,
                hospital_reg_num,gender, age,emergencyPhone,emergencyCountryCode,extension,latitude, longitude
               /* Dob,*/);
        call.enqueue(new Callback<RetrofitInstance>() {
            @Override
            public void onResponse(Call<RetrofitInstance> call, Response<RetrofitInstance> response) {
                Log.e("Reliance", "response" + response.body());
                if (response.body() != null) {
                    alertDialog.dismiss();
                    String resp = response.body().getMessage();
                    //Toast.makeText(MedicalPersonSignupActivity.this, resp, Toast.LENGTH_SHORT).show();
                    Log.e("RetrofitInstance", "" + resp);
                    String code = response.body().getResponse();
                    if (code.equals("0")){
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast_layout,
                                (ViewGroup) findViewById(R.id.custom_toast_container));

                        TextView text = (TextView) layout.findViewById(R.id.text);
                        text.setText(response.body().getMessage());
                        Toast toast = new Toast(MedicalPersonSignupActivity.this);
                        //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                    }
                    if (response.body().getResponse().equals("3")){
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast_layout,
                                (ViewGroup) findViewById(R.id.custom_toast_container));

                        TextView text = (TextView) layout.findViewById(R.id.text);
                        text.setText(response.body().getMessage());
                       /* Toast toast = new Toast(MedicalPersonSignupActivity.this);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();*/
                        final AlertDialog.Builder alertBuilder2 = new AlertDialog.Builder(MedicalPersonSignupActivity.this);
                        alertBuilder2.setTitle("UserID already registered by other users");
                        alertBuilder2.setMessage(response.body().getMessage());

                        alertBuilder2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // imgMailClose.setVisibility(View.VISIBLE);
                                Intent intent = new Intent(MedicalPersonSignupActivity.this,LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                alertBuilder2.create().dismiss();
                            }
                        });
                        alertBuilder2.create().show();
                       /* Intent intent = new Intent(MedicalPersonSignupActivity.this,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                   */ }
                }
            }

            @Override
            public void onFailure(Call<RetrofitInstance> call, Throwable t) {
                // Log.e("jio","response"+t.toString());
                alertDialog.dismiss();
                t.printStackTrace();
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toast_layout,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText(t.getMessage());
                Toast toast = new Toast(MedicalPersonSignupActivity.this);
                //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
                // Toast.makeText(MedicalPersonSignupActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void DatePicker(){
        txt_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDatePicker();
            }
        });

    }
    int year, month, day;

    public void loadDatePicker() {
        final DatePickerDialog dialog;

        if (txt_dob.getText().toString().isEmpty()) {
            Log.e("ifcall", "call");
            String[] txtBirthdayArray = tempbirthDayObj.split("-");
            year = Integer.parseInt(txtBirthdayArray[0]);
            month = Integer.parseInt(txtBirthdayArray[1]);
            day = Integer.parseInt(txtBirthdayArray[2]);
            Log.e("textdate", "call" + year + "-" + month + "-" + day);
            dialog = new DatePickerDialog(this, null, year, month - 1, day);
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            dialog.show();
        } else {
            tempbirthDayObj = txt_dob.getText().toString();
            String[] txtBirthdayArray = tempbirthDayObj.split("-");
            year = Integer.parseInt(txtBirthdayArray[0]);
            month = Integer.parseInt(txtBirthdayArray[1]);
            day = Integer.parseInt(txtBirthdayArray[2]);
            Log.e("textdate", "call" + year + "-" + month + "-" + day);
            dialog = new DatePickerDialog(this, null, year, month - 1, day);
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            dialog.show();
        }
        dialog.setButton(DialogInterface.BUTTON_POSITIVE,
                "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog1, int which) {
                        DatePicker objDatePicker = dialog.getDatePicker();
                        year = objDatePicker.getYear();
                        month = objDatePicker.getMonth();
                        day = objDatePicker.getDayOfMonth();
                        txt_dob.setText(year + "-" + (month + 1) + "-" + day);
                        String age = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)-year);
                        ed_age.setText(age);
                        Log.e("ageee",""+age);
                        Log.e("txt_dob", "" + txt_dob.getText().toString());
                    }
                });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void cameraBottomSheet(){
        TextView cam, gal, canc,file;
        View dialogView = getLayoutInflater().inflate(R.layout.file_dailog, null);
        final BottomSheetDialog cameraBottomSheetDialog =new BottomSheetDialog(Objects.requireNonNull(MedicalPersonSignupActivity.this), R.style.BottomSheetDialogTheme);
        cameraBottomSheetDialog.setContentView(dialogView);
        cam = cameraBottomSheetDialog.findViewById(R.id.camera);
        gal = cameraBottomSheetDialog.findViewById(R.id.gallery);
        file = cameraBottomSheetDialog.findViewById(R.id.file);
        canc = cameraBottomSheetDialog.findViewById(R.id.cancel);
        file.setVisibility(View.GONE);
        FrameLayout bottomSheet = (FrameLayout) cameraBottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        bottomSheet.setBackground(null);

        cameraBottomSheetDialog.show();
        canc.setTextColor(Color.parseColor("#53B9c6"));

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MedicalPersonSignupActivity.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    // dialog.dismiss();
                    cameraBottomSheetDialog.cancel();
                    LoadCaptureImageScreen();
                } else {
                    cameraBottomSheetDialog.cancel();
                    // dialog.dismiss();
                 //   requestCameraPermission();
                }
            }
        });
/*
        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ScreeningRecordActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    cameraBottomSheetDialog.cancel();
                    showFileChooser();
                } else {
                    cameraBottomSheetDialog.cancel();
                    requestStoragePermission();
                }

            }
        });
*/
        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MedicalPersonSignupActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    cameraBottomSheetDialog.cancel();
                    LoadImageFromGallery();
                } else {
                    requestStoragePermission();
                }
            }
        });
        canc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraBottomSheetDialog.cancel();
            }
        });

    }
    private void loadAgePicker() {
        final Dialog mod = new Dialog(MedicalPersonSignupActivity.this);
        mod.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mod.setContentView(R.layout.alert_dailog);
        mod.show();
        TextView txtTitle = (TextView) mod.findViewById(R.id.title);
        txtTitle.setText("Select Age");
        numberPickerAge = mod.findViewById(R.id.value);


        if (ed_age.getText().toString().length() > 0 && ed_age.getText().toString().contains("y")) {
            String weightArray[] = ed_age.getText().toString().split(" ");
            selectedAge = weightArray[0];
            selectedTitle = weightArray[1];
        } else {
            selectedAge = ed_age.getText().toString();
        }
        Log.e("selectAge", "call" + selectedAge);
        ////for value array
        numberPickerAge.setDisplayedValues(null);
        int index = PersonalInfoController.getInstance().ageValuesArray.indexOf(selectedAge);
        numberPickerAge.setMinValue(0);
        numberPickerAge.setWrapSelectorWheel(false);
        numberPickerAge.setMaxValue(PersonalInfoController.getInstance().ageValuesArray.size() - 1);
        String[] mStringArray = new String[PersonalInfoController.getInstance().ageValuesArray.size()];
        mStringArray = PersonalInfoController.getInstance().ageValuesArray.toArray(mStringArray);
        numberPickerAge.setDisplayedValues(mStringArray);
        numberPickerAge.setValue(index);
        //for measure
        agePicker = (NumberPicker) mod.findViewById(R.id.name);
        agePicker.setWrapSelectorWheel(false);
        agePicker.setMaxValue(0);
        agePicker.setMinValue(0);
        agePicker.setDisplayedValues(PersonalInfoController.getInstance().ageUnitsArray);

        numberPickerAge.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedAge = PersonalInfoController.getInstance().ageValuesArray.get(newVal);
            }
        });


        Button btnOk = mod.findViewById(R.id.ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mod.cancel();
                mod.dismiss();
                ed_age.setText(selectedAge);
                txt_dob.setText("");
            }
        });
        Button btnCancel = mod.findViewById(R.id.cancle);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mod.dismiss();
            }
        });
    }



    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter = new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MedicalPersonSignupActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);
    }
}

