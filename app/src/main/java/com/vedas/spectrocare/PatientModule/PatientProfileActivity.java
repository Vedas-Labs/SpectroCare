package com.vedas.spectrocare.PatientModule;

import androidx.annotation.NonNull;
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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBaseModels.PatientModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.ServerApiModel.PatientDetailsModel;
import com.vedas.spectrocare.activities.HomeActivity;
import com.vedas.spectrocare.activities.LoginActivity;
import com.vedas.spectrocare.activities.MedicalPersonSignupActivity;
import com.vedas.spectrocare.activities.PatientGeneralProfileActivity;
import com.vedas.spectrocare.activities.SelectUserActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static com.vedas.spectrocare.adapter.MedicalHistoryAdapter.dialog;

public class PatientProfileActivity extends AppCompatActivity {
    RelativeLayout relativeLayout;
    MultipartBody.Part patientProfilePic;
    RequestBody Pmedical_personnel_Id, DofB, PfirstName, PLastName, Pgender, Page, PphoneNumber,
            Paddress, Pcity, Pstate, PCountry, PpatientID, PpostalCod, hospital_reg_num, latitude, longitude,
            PemailID, PphoneNumberCountryCode;
    RequestBody image;
    TextView txtPatientName, txtPatientID, txtMedicalRecordID, txtTitle,genderTxt,ageTxt;
    EditText edtFirstName, edtLastName, edtGender, edtAge, edtDOB, edtCountryCode, edtPhoneNumber, edtEmail,
            editAddress, editCity, edtState, editPostalCode;
    Button btnUpdate;
    File file = null;
    private String mCurrentPhotoPath;
    private Bitmap bitmap;
    ImageView  img_edit;
    CircularImageView profileImg;
    String profileBase64Obj,gender, oldProfielBase64Obj;
    ImageView imgBackArrow;
    RadioButton cb1, cb2, cb3;
    AlertDialog.Builder dialog1;
    Calendar calendar;
    AlertDialog alertDialog;
    String  formattedDate;
    long kk,d;
    int i;
    String date;
    ProgressBar progressBar;
    int year,  month,  dayOfMonth;
    DatePicker picker;
    CalendarView calendarView;
    PopupWindow popUp;
    View mView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);
        calendar = Calendar.getInstance();
        casting();
        getPatientData();
        updateProfile();
        relativeLayout = findViewById(R.id.profile_data_layout);
        imgBackArrow = findViewById(R.id.img_back_arrow);
        imgBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void updateProfile() {
        if (PatientLoginDataController.getInstance().currentPatientlProfile != null) {
            edtState.setFocusable(false);
            editAddress.setFocusable(false);
            editCity.setFocusable(false);
            btnUpdate.setEnabled(false);
            edtAge.setFocusable(false);
            editPostalCode.setFocusable(false);
            edtCountryCode.setFocusable(false);
            edtEmail.setFocusable(false);
            edtPhoneNumber.setFocusable(false);
            edtFirstName.setFocusable(false);
            edtLastName.setFocusable(false);
            cb1.setEnabled(false);
            cb2.setEnabled(false);
            cb3.setEnabled(false);
        }
        cb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtGender.setText("Male");
            }
        });
        cb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtGender.setText("Female");
            }
        });
        cb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtGender.setText("Others");
            }
        });
        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("touch", "ddd");
                cb1.setEnabled(true);
                cb2.setEnabled(true);
                cb3.setEnabled(true);
                btnUpdate.setEnabled(true);
                txtTitle.setText("Edit Patient Profile");
                edtDOB.setClickable(true);
                btnUpdate.setVisibility(View.VISIBLE);
                edtFirstName.setFocusableInTouchMode(true);
                edtFirstName.requestFocus();
                edtFirstName.setSelection(edtFirstName.getText().length());
                edtLastName.setFocusableInTouchMode(true);
                editAddress.setFocusableInTouchMode(true);
                editCity.setFocusableInTouchMode(true);
                edtPhoneNumber.setFocusableInTouchMode(true);
                edtEmail.setFocusableInTouchMode(true);
                edtCountryCode.setFocusableInTouchMode(true);
                editPostalCode.setFocusableInTouchMode(true);
                edtAge.setFocusableInTouchMode(true);
                edtState.setFocusableInTouchMode(true);
                edtDOB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      //  picker.setVisibility(View.VISIBLE);
                      //  mView = LayoutInflater.from(PatientProfileActivity.this).inflate(R.layout.calender_dialog_view, null, false);
                        final Dialog dialog = new Dialog(PatientProfileActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.calender_dialog_view);
                        picker=dialog.findViewById(R.id.datePicker1);
                        picker.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE));
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        Button btnOK = (Button) dialog.findViewById(R.id.btn_ok);
                        dialog.show();

                        btnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.e("dddd",""+picker.getDayOfMonth()+"/"+ (picker.getMonth() + 1)
                                        +"/"+picker.getYear());
                                picker.setMaxDate(Calendar.getInstance().getTimeInMillis());

                               // calendar = Calendar.getInstance();
                                calendar.add(Calendar.DAY_OF_MONTH, 1);
                                calendar.add(Calendar.YEAR, 1);
                                calendar.set(picker.getYear(),(picker.getMonth()),picker.getDayOfMonth());
                                String age = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)-picker.getYear());
                               // long year = picker.getYear();
                                SimpleDateFormat weekFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

                                long kk = (calendar.getTimeInMillis())/1000 ;

                                Log.e("gfjhfj","hgg:"+kk);
                                date = String.valueOf(kk*1000);

                                edtAge.setText(age);
                                Log.e("afdsf","daaf"+age);
                                Date currentDate = new Date(kk*1000);
                                SimpleDateFormat jdff = new SimpleDateFormat("yyyy-MM-dd");
                                jdff.setTimeZone(TimeZone.getDefault());
                                String java_date = jdff.format(currentDate);
                                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                                sdf.setTimeZone(TimeZone.getDefault());
                                Date clickedDate = null;
                                try {
                                    clickedDate = jdff.parse(java_date);
                                    if (i == 0) {
                                        formattedDate = sdf.format(clickedDate);
                                        Log.e("forrr","ff"+formattedDate);

                                    }
                                    edtDOB.setText(formattedDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                dialog.dismiss();
                            }
                        });
                        // popUp.dismiss();
                    }
                });
/*
                edtDOB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calendar = Calendar.getInstance();
                        mView = LayoutInflater.from(PatientProfileActivity.this).inflate(R.layout.calenderview_item, null, false);
                        popUp = new PopupWindow(mView, LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT, false);
                        popUp.setTouchable(true);
                        popUp.setFocusable(true);
                        popUp.setBackgroundDrawable(new BitmapDrawable());
                        popUp.setFocusable(true);
                        popUp.setOutsideTouchable(true);
                        //Solution
                        popUp.showAsDropDown(edtDOB);
                        calendarView = mView.findViewById(R.id.calendar_view);
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        calendar.add(Calendar.YEAR, 1);
                        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                            @Override
                            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                                String msg = dayOfMonth + "-" + (month + 1) + "-" + year;
                                calendar.set(year,month,dayOfMonth);
                                String age = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)-year);
                                edtAge.setText(age);

                                SimpleDateFormat weekFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

                                long kk = (calendar.getTimeInMillis())/1000 ;

                                Log.e("gfjhfj","hgg:"+kk);
                                date = String.valueOf(kk*1000);

                                popUp.dismiss();
                                Date currentDate = new Date(kk*1000);
                                SimpleDateFormat jdff = new SimpleDateFormat("yyyy-MM-dd");
                                jdff.setTimeZone(TimeZone.getDefault());
                                String java_date = jdff.format(currentDate);
                                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                                sdf.setTimeZone(TimeZone.getDefault());
                                Date clickedDate = null;
                                try {

                                    clickedDate = jdff.parse(java_date);

                                    if (i == 0) {
                                        formattedDate = sdf.format(clickedDate);
                                        Log.e("forrr","ff"+formattedDate);

                                    }
                                    edtDOB.setText(formattedDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                });
*/

                profileImg.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View v) {
                        cameraBottomSheet();

                       // dateToStramp();

                    }
                });
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("hhhhhh","kkk");
                alertDialog.show();
                Objects.requireNonNull(alertDialog.getWindow()).setLayout(600, 500);
                api();
            }
        });
    }

    private File createImageFileFromBitmap(Bitmap bitmap) {
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outStream);
            File f = createImageFile();
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

    public void api() {

        if (mCurrentPhotoPath == null) {
            Log.e("profileCheck", "re" );

            if (PatientLoginDataController.getInstance().currentPatientlProfile.getProfilePic().isEmpty()){
                Log.e("profileCheck", "ri" );
                Bitmap bitMap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_human_dummy3x);
                file = createImageFileFromBitmap(bitMap);
                bitmap = bitMap;
            }else{
                Log.e("profileCheck", "ry" );
                Log.e("url","img"+PatientLoginDataController.getInstance().currentPatientlProfile.getProfilePic());
                Picasso.get().load(Uri.parse(PatientLoginDataController.getInstance().currentPatientlProfile.getProfilePic())).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap1, Picasso.LoadedFrom from) {

                        Log.e("profileCheckfromurl", "rerrr" + bitmap1);
                        Log.e("profileCheckfromurl", "rerrr" );
                        Bitmap bittu = null;
                        bittu = bitmap1;
                        file = createImageFileFromBitmap(bittu);
                        Log.e("profile", "rerrr" + file);

                    }
                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    }
                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });
            }

            Log.e("filenull", "d" + mCurrentPhotoPath);

        } else {
            file = new File(mCurrentPhotoPath);
            Log.e("fileupload", "kk" + mCurrentPhotoPath);
        }
       // file = createImageFileFromBitmap(bitmap);
        if (date==null){
            date =PatientLoginDataController.getInstance().
                    currentPatientlProfile.getDob();
            Log.e("date","dddd"+date);
        }
       // date = String.valueOf(kk);
        Log.e("kkk",""+date);
        String Latitude = "14.6738";
        String Longitude = "15.7873";
        MultipartBody.Part patientProfilePic;
        image = RequestBody.create(MediaType.parse("image/*"), file);
        Log.e("ghghghgh", "jj" + image);
        patientProfilePic = MultipartBody.Part.createFormData("profilePic", file.getName(), image);
        Pmedical_personnel_Id = RequestBody.create(MediaType.parse("text/plain"), PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalPerson_id());
        PfirstName = RequestBody.create(MediaType.parse("text/plain"), edtFirstName.getText().toString());
        PLastName = RequestBody.create(MediaType.parse("text/plain"), edtLastName.getText().toString());
        PphoneNumber = RequestBody.create(MediaType.parse("text/plain"), edtPhoneNumber.getText().toString());
        Paddress = RequestBody.create(MediaType.parse("text/plain"), editAddress.getText().toString());
        Pcity = RequestBody.create(MediaType.parse("text/plain"), editCity.getText().toString());
        Pstate = RequestBody.create(MediaType.parse("text/plain"), edtState.getText().toString());
        PCountry = RequestBody.create(MediaType.parse("text/plain"), "India");
        PpostalCod = RequestBody.create(MediaType.parse("text/plain"), editPostalCode.getText().toString());
        PphoneNumberCountryCode = RequestBody.create(MediaType.parse("text/plain"), edtCountryCode.getText().toString());
        PemailID = RequestBody.create(MediaType.parse("text/plain"), edtEmail.getText().toString().trim());
        Log.e("email",""+ edtEmail.getText().toString());
        PpatientID = RequestBody.create(MediaType.parse("text/plain"), PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
        Pgender = RequestBody.create(MediaType.parse("text/plain"), edtGender.getText().toString());
        hospital_reg_num = RequestBody.create(MediaType.parse("text/plain"), PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
        Page = RequestBody.create(MediaType.parse("text/plain"), edtAge.getText().toString());
        latitude = RequestBody.create(MediaType.parse("text/plain"), Latitude);
        longitude = RequestBody.create(MediaType.parse("text/plain"), Longitude);
        DofB = RequestBody.create(MediaType.parse("text/plain"), date);
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        okhttpClientBuilder.connectTimeout(45, TimeUnit.SECONDS);
        okhttpClientBuilder.readTimeout(45, TimeUnit.SECONDS);
        okhttpClientBuilder.writeTimeout(45, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                .client(okhttpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create()).build();

        ServerApi serverApi = retrofit.create(ServerApi.class);
        Log.e("body", "api" +edtEmail.getText() +" ,"+editAddress.getText() + ", " + edtState.getText() + ", " + PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalPerson_id() +
                ", " + PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number() + ", " + edtAge.getText()
                + " ," + edtFirstName.getText() + ", " + edtLastName.getText() + ", " + edtGender.getText() + "," + edtPhoneNumber.getText() + " ,"
                + edtCountryCode.getText() + ", " + editPostalCode.getText()+","+date+","+PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId()+","
                +PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number()+","+PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());

        Call<PatientDetailsModel> call = serverApi.PatientProfileUpdate(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), PfirstName, PLastName, Pgender, Page,
                PphoneNumberCountryCode, PphoneNumber, Paddress, Pstate, PCountry, PpostalCod, Pmedical_personnel_Id, hospital_reg_num,
                latitude, longitude, patientProfilePic, PemailID, PpatientID, Pcity, DofB);

        call.enqueue(new Callback<PatientDetailsModel>() {
            @Override
            public void onResponse(Call<PatientDetailsModel> call, Response<PatientDetailsModel> response) {
                alertDialog.dismiss();
                Log.e("responsee", "response" + response.body());

                Log.e("responsee", "message" + response.message());
                Toast.makeText(PatientProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                if (response.body().getResponse().equals("3")){
                    PatientModel objModel = new PatientModel();
                    final String urlString = ServerApi.img_home_url + response.body().getProfilePic();
                    objModel.setMedicalPerson_id(PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalPerson_id());
                    objModel.setPatientId(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
                    objModel.setFirstName(edtFirstName.getText().toString());
                    objModel.setAddress(editAddress.getText().toString());
                    objModel.setAge(edtAge.getText().toString());
                    objModel.setCity(editCity.getText().toString());
                    objModel.setHospital_reg_number(PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
                    objModel.setLastName(edtLastName.getText().toString());
                    objModel.setCountry("India");
                    objModel.setGender(edtGender.getText().toString());
                    objModel.setDob(date);
                    objModel.setState(edtState.getText().toString());
                    objModel.setEmailId(edtEmail.getText().toString());
                    objModel.setMedicalRecordId(PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
                    Log.e("eeeeee","nnnnn"+edtEmail.getText().toString());
                    objModel.setLatitude("14.6738");
                    objModel.setLongitude("15.7873");
                    objModel.setPhone_coutryCode(edtCountryCode.getText().toString());
                    objModel.setPhoneNumber(edtPhoneNumber.getText().toString());
                    objModel.setPostalCode(editPostalCode.getText().toString());
                    objModel.setProfilePic(urlString);
                    if(PatientLoginDataController.getInstance().updateMedicalProfileData(objModel)){
                        Intent intent = new Intent(PatientProfileActivity.this, PatientHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                       /* LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast_layout,
                                (ViewGroup) findViewById(R.id.custom_toast_container));
                        TextView text = (TextView) layout.findViewById(R.id.text);
                        text.setText(response.body().getMessage());
                        Toast toast = new Toast(MedicalPersonSignupActivity.this);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();*/
                    }

                }
            }

            @Override
            public void onFailure(Call<PatientDetailsModel> call, Throwable t) {
                alertDialog.dismiss();
            }
        });

    }

    public void getPatientData() {
        if (PatientLoginDataController.getInstance().currentPatientlProfile != null) {
            PatientModel objModel = PatientLoginDataController.getInstance().currentPatientlProfile;
           //  Picasso.get().load(objModel.getProfilePic()).into(profileImage);
            Log.e("caammm","dfd "+objModel.getProfilePic());
            Log.e("object", "name" + objModel.getMedicalPerson_id());
            editAddress.setText(objModel.getAddress());
            editCity.setText(objModel.getCity());
            dateToStramp();
           // edtDOB.setText(objModel.getDob());
            Log.e("date","d"+objModel.getDob());
            edtAge.setText(objModel.getAge());
            genderTxt.setText(objModel.getGender()+" ");
            ageTxt.setText(objModel.getAge()+" yrs");
            editPostalCode.setText(objModel.getPostalCode());
            edtCountryCode.setText(objModel.getPhone_coutryCode());
            edtEmail.setText(objModel.getEmailId());
            Log.e("adaffdfa","nothing"+edtEmail.getText());
            edtPhoneNumber.setText(objModel.getPhoneNumber());
           // edtGender.setText(objModel.getGender());
            edtFirstName.setText(objModel.getFirstName());
            edtLastName.setText(objModel.getLastName());
            edtState.setText(objModel.getState());
            txtPatientID.setText(objModel.getPatientId());
            gender=objModel.getGender();
            txtMedicalRecordID.setText(objModel.getMedicalPerson_id());
            txtPatientName.setText(objModel.getFirstName().trim()+" "+objModel.getLastName().trim());
            Picasso.get().load(objModel.getProfilePic()).placeholder(R.drawable.image).into(profileImg);
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat weekFormatter = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
            // edtDOB.setText("Today ,"+weekFormatter.format(currentTime));
            if (gender.equals("Male")) {
                cb1.setChecked(true);
                edtGender.setText("Male");
            }else if (gender.equals("Female")){
                cb2.setChecked(true);
                edtGender.setText("Female");
            }else{
                cb3.setChecked(true);
                edtGender.setText("Other");
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void cameraBottomSheet() {
        TextView cam, gal, canc, file;
        View dialogView = getLayoutInflater().inflate(R.layout.file_dailog, null);
        final BottomSheetDialog cameraBottomSheetDialog = new BottomSheetDialog(Objects.requireNonNull(PatientProfileActivity.this), R.style.BottomSheetDialogTheme);
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
                if (ActivityCompat.checkSelfPermission(PatientProfileActivity.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    // dialog.dismiss();
                    cameraBottomSheetDialog.cancel();
                    LoadCaptureImageScreen();
                } else {
                    cameraBottomSheetDialog.cancel();
                    // dialog.dismiss();
                    requestCameraPermission();
                }
            }
        });

        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(PatientProfileActivity.this,
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

    public static final int CAMERA_REQUEST_CODE = 1001;
    public static final int GALLERY_REQUEST_CODE = 1002;
    public static final int CAMERA_PERMISSION_CODE = 5001;
    public static final int GALLERY_PERMISSION_CODE = 5002;

    private void LoadImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        galleryIntent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
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
                            ActivityCompat.requestPermissions(PatientProfileActivity.this,
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
            ActivityCompat.requestPermissions(PatientProfileActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
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
                            ActivityCompat.requestPermissions(PatientProfileActivity.this,
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
            ActivityCompat.requestPermissions(PatientProfileActivity.this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
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
                Uri photoURI = FileProvider.getUriForFile(PatientProfileActivity.this,
                        "com.vedas.spectrocare.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = false;
        options.inPurgeable = true;

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap bitmap1 = BitmapFactory.decodeFile(mCurrentPhotoPath, options);
            Bitmap rotatedBitmap = rotatedImageBitmap(mCurrentPhotoPath, bitmap1);
            bitmap = rotatedBitmap;
            profileImg.setImageBitmap(getResizedBitmap(rotatedBitmap, 500));
            loadEncoded64ImageStringFromBitmap(bitmap);

        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            Toast.makeText(PatientProfileActivity.this, "Image Capturing Cancelled", Toast.LENGTH_SHORT).show();
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri selectedImage = imageReturnedIntent.getData();
            Log.e("cccccccc","df"+selectedImage);

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
                Log.e("gallery photo", "call" + mCurrentPhotoPath);

            }
            Bitmap rotatedBitmap = rotatedImageBitmap(mCurrentPhotoPath, bitmap);
            profileImg.setImageBitmap(getResizedBitmap(rotatedBitmap, 500));
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            Toast.makeText(PatientProfileActivity.this, "Image Selection Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        profileBase64Obj = Base64.encodeToString(imageInByte, Base64.NO_WRAP);
        Log.e("base64Image", "call" + profileBase64Obj);

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

    public void casting() {
        txtPatientName = findViewById(R.id.txt_patient_name);
        profileImg = findViewById(R.id.img_patient_profile);
        btnUpdate = findViewById(R.id.update_btn);
        edtCountryCode = findViewById(R.id.edt_county_code);
        txtMedicalRecordID = findViewById(R.id.txt_id_medical_record);
        editPostalCode = findViewById(R.id.edt_postal_code);
        edtState = findViewById(R.id.edt_province);
        editCity = findViewById(R.id.edt_city);
        img_edit = findViewById(R.id.img_edit);
        cb1 = findViewById(R.id.male);
        cb2 = findViewById(R.id.female);
        cb3 = findViewById(R.id.other);
        ageTxt = findViewById(R.id.age_txt);
        genderTxt = findViewById(R.id.gender_txt);
        editAddress = findViewById(R.id.edt_address);
        txtTitle = findViewById(R.id.txt_title);
        edtLastName = findViewById(R.id.edt_last_name);
        edtFirstName = findViewById(R.id.edt_firsr_name);
        edtPhoneNumber = findViewById(R.id.edt_phone_no);
        txtPatientID = findViewById(R.id.txt_id_patient);
        edtEmail = findViewById(R.id.edt_email);
        edtDOB = findViewById(R.id.edt_dob);
        edtAge = findViewById(R.id.edt_age);
        edtGender = findViewById(R.id.edt_gender);
        final LayoutInflater inflater = PatientProfileActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alertbox_layout, null);
        dialog1 = new AlertDialog.Builder(PatientProfileActivity.this);
        progressBar = dialogView.findViewById(R.id.progressBar);
        dialog1.setView(dialogView);
        alertDialog = dialog1.create();
        progressBar.setVisibility(View.VISIBLE);
    }
    public void dateToStramp(){
        Log.e("string","d"+PatientLoginDataController.getInstance().
                currentPatientlProfile.getDob());
        if (PatientLoginDataController.getInstance().
                currentPatientlProfile.getDob().contains("-")){
            edtDOB.setText(PatientLoginDataController.getInstance().
                    currentPatientlProfile.getDob());
        }else{
            d = Long.valueOf(PatientLoginDataController.getInstance().
                    currentPatientlProfile.getDob());
           // calendar.setTimeInMillis(d);

        }

        Date date1 = null;
        date1 = new Date(d);
        calendar.setTime(date1);
// format of the date
        SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd");
        jdf.setTimeZone(TimeZone.getDefault());
        String java_date = jdf.format(date1);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        sdf.setTimeZone(TimeZone.getDefault());
        Date date = null;
        try {

            date = jdf.parse(java_date);

            if (i == 0) {
                formattedDate = sdf.format(date);
                Log.e("forrr","ff"+formattedDate);

            } else {

                formattedDate = formattedDate + ", " + sdf.format(date);
                Log.e("forrr","ff"+formattedDate);

            }
            edtDOB.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
