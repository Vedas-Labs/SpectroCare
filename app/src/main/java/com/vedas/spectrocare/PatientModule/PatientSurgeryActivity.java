package com.vedas.spectrocare.PatientModule;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.PhysicalServerObjectDataController;

import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.PatientServerApiModel.PatientSurgicalObject;
import com.vedas.spectrocare.PatientServerApiModel.PatientSurgicalRecordServerObjectDataController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.activities.MedicalPersonaSignupView;
import com.vedas.spectrocare.activities.MedicalPersonalSignupPresenter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class PatientSurgeryActivity extends AppCompatActivity implements MedicalPersonaSignupView {
    ImageView edtImg, imgCal, imgClock, backImg;
    private int mHour, mMinute;
    private String format = "";
    Button btnSaveChanges;
    EditText edtStartDate, edtSurgeryName, edtTime, edtName, edtImmuneNote, edtFile;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    Button btnOk;
    int i, k, j;
    TextView txtHours, txtMin, txtAm, txtPm;
    String hours, minits;
    CalendarView calendarView;
    PopupWindow popUp;
    View mView;
    ImageView imgHrUp, imgHrDown, imgMinUp, imgMinDwn;
    Button btnImmuneChange;
    TextView txt_doctorName, txt_createdDate, txt_CreatedTime;
    RefreshShowingDialog refreshShowingDialog;
    AlertDialog dialog;
    private String mCurrentPhotoPath;
    String filenmae;
    File file;
    TextView txt_attachemnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_surgery);
        casting();
        clickEvents();
    }

    public void casting() {
        refreshShowingDialog = new RefreshShowingDialog(PatientSurgeryActivity.this);
        btnImmuneChange = findViewById(R.id.btn_save_changes);
        edtStartDate = findViewById(R.id.edt_start_date);
        imgClock = findViewById(R.id.img_schedule);
        edtImmuneNote = findViewById(R.id.edt_notes);
        edtSurgeryName = findViewById(R.id.edt_surgery_name);
        edtName = findViewById(R.id.edt_doc_name);
        edtImg = findViewById(R.id.pic_edt);
        edtTime = findViewById(R.id.edt_end_date);
        imgCal = findViewById(R.id.img_calender);
        backImg = findViewById(R.id.img_back_arrow);
        txt_doctorName = findViewById(R.id.txt_name_of_doc);
        txt_createdDate = findViewById(R.id.txt_doc_date);
        txt_CreatedTime = findViewById(R.id.txt_doc_time);
        edtFile = findViewById(R.id.edt_file);
        txt_attachemnt = findViewById(R.id.txt_view);
        btnImmuneChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validations();
            }
        });

        loadCurrentObject();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(PhysicalServerObjectDataController.MessageEvent event) {
        Log.e("sidemenuMessageevent", "" + event.message);
        String resultData = event.message.trim();
        if (resultData.equals("updatePatientSurgical")) {
            refreshShowingDialog.showAlert();
            finish();
            startActivity(
                    new Intent(getApplicationContext(), PatientMedicalHistoryActivity.class).putExtra("surgery","surgery"));
        }
    }
    private void loadCurrentObject() {
        if (PatientMedicalRecordsController.getInstance().selectedSurgeryObject != null) {
            PatientSurgicalObject obj = PatientMedicalRecordsController.getInstance().selectedSurgeryObject;
            edtSurgeryName.setText(obj.getSurgeryProcedure());
            edtStartDate.setText(obj.getSurgeryDate());
            edtName.setText(obj.getDoctorName());
            edtImmuneNote.setText(obj.getMoreDetails());
            long millis = Long.parseLong(obj.getAddedDate());
            Date d = new Date(millis);
            SimpleDateFormat weekFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH);
            String weekString = weekFormatter.format(d);
            String time[] = weekString.split(" ");
            Log.e("weeekarray", "" + time[0] + time[1] + time[2]);
            txt_doctorName.setText("Dr." + obj.getDoctorName());
            txt_createdDate.setText(time[0]);
            txt_CreatedTime.setText(time[1] + " " + time[2]);
// edtFile.setText(obj.getFilePath());
            if(obj.getAttachmentLis().size()>0){
                if (obj.getAttachmentLis().get(0).getFilePath() != null) {
                    txt_attachemnt.setVisibility(View.VISIBLE);
                    edtFile.setText(obj.getAttachmentLis().get(0).getFilePath());
                } else {
                    txt_attachemnt.setVisibility(View.GONE);
                }
            }else {
                txt_attachemnt.setVisibility(View.GONE);
            }
            txt_attachemnt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadAttachUrlImage(obj);
                }
            });
        }
    }
    private void loadAttachUrlImage(PatientSurgicalObject obj) {
        if(obj.getAttachmentLis().size()>0){
            if (obj.getAttachmentLis().get(0).getFilePath() != null) {
                String url = ServerApi.img_home_url + obj.getAttachmentLis().get(0).getFilePath();
                edtImmuneNote.setText(obj.getAttachmentLis().get(0).getMoreDetails());
                Log.e("notdata", "call" + url);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
// intent.setDataAndType(Uri.parse(url), "*/*");
// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
/* if (dataModel.getFilePath() != null) {
String url = ServerApi.img_home_url + dataModel.getFilePath();
Log.e("notdata", "call" + url);
Intent intent = new Intent(Intent.ACTION_VIEW);
intent.setData(Uri.parse(url));
startActivity(intent);
}*/
    }
/*    private void loadCurrentObject() {
        if (PatientMedicalRecordsController.getInstance().selectedSurgeryObject != null) {
            PatientSurgicalObject obj = PatientMedicalRecordsController.getInstance().selectedSurgeryObject;
            edtSurgeryName.setText(obj.getSurgeryProcedure());
            edtStartDate.setText(obj.getSurgeryDate());
            edtName.setText(obj.getDoctorName());
            edtImmuneNote.setText(obj.getMoreDetails());
            long millis = Long.parseLong(obj.getAddedDate());
            Date d = new Date(millis);
            SimpleDateFormat weekFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH);
            String weekString = weekFormatter.format(d);
            String time[] = weekString.split(" ");
            Log.e("weeekarray", "" + time[0] + time[1] + time[2]);
            txt_doctorName.setText("Dr." + obj.getDoctorName());
            txt_createdDate.setText(time[0]);
            txt_CreatedTime.setText(time[1] + " " + time[2]);
            // edtFile.setText(obj.getFilePath());
            if (obj.getFilePath() != null) {
                txt_attachemnt.setVisibility(View.VISIBLE);
            } else {
                txt_attachemnt.setVisibility(View.GONE);
            }
            txt_attachemnt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadAttachUrlImage(obj);
                }
            });
        }
    }
    private void loadAttachUrlImage(PatientSurgicalObject dataModel) {
        if (dataModel.getFilePath() != null) {
            String url = ServerApi.img_home_url + dataModel.getFilePath();
            Log.e("notdata", "call" + url);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));

            startActivity(intent);
        }
    }*/


    private void validations() {
       /* if (edtTime.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please select time", "ok");
        } else */
        if (edtSurgeryName.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please enter surgery info", "ok");
        } else if (edtStartDate.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please enter startDate", "ok");
        } else if (edtName.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please enter doctorName", "ok");
        } else if (edtImmuneNote.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please enter note", "ok");
        } else if (edtImmuneNote.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please enter note", "ok");
        } else {
            if (isNetworkConnected()) {
                refreshShowingDialog.showAlert();
                PatientSurgicalRecordServerObjectDataController.getInstance().addSurgicalCallForAttchmet(true, file, filenmae,
                        edtImmuneNote.getText().toString(), edtName.getText().toString(), edtStartDate.getText().toString(), edtSurgeryName.getText().toString());
            } else {
                refreshShowingDialog.hideRefreshDialog();
                dialogeforCheckavilability("Error", "Please check internet connection", "ok");
            }
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter = new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PatientSurgeryActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);
    }

    private void showDate(int year, int month, int day) {

        edtStartDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    public void clickEvents() {
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });
        imgClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pickerDialogs();
                mView = LayoutInflater.from(PatientSurgeryActivity.this).inflate(R.layout.clock_poup_up_layout, null, false);
                popUp = new PopupWindow(mView, LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, false);
                popUp.setTouchable(true);
                popUp.setFocusable(true);
                popUp.setBackgroundDrawable(new BitmapDrawable());
                popUp.setFocusable(true);
                popUp.setOutsideTouchable(true);
                //Solution
                popUp.showAsDropDown(edtTime);
                imgHrUp = mView.findViewById(R.id.img_arrow_up);
                imgHrDown = mView.findViewById(R.id.img_arrow_down);
                imgMinUp = mView.findViewById(R.id.img_up_arrow);
                imgMinDwn = mView.findViewById(R.id.img_dwn_arrow);
                txtHours = mView.findViewById(R.id.txt_hour);
                txtMin = mView.findViewById(R.id.txt_minit);
                btnOk = mView.findViewById(R.id.btn_ok);
                txtAm = mView.findViewById(R.id.txt_am);
                txtPm = mView.findViewById(R.id.txt_pm);

                clockItemsClickListners();
            }
        });

        edtImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnImmuneChange.setVisibility(View.VISIBLE);
                edtStartDate.setBackgroundResource(R.drawable.edt_txt_background);
                edtStartDate.setFocusable(false);
                edtSurgeryName.setBackgroundResource(R.drawable.edt_txt_background);
                edtSurgeryName.setFocusableInTouchMode(true);
                edtTime.setBackgroundResource(R.drawable.edt_txt_background);
                edtName.setBackgroundResource(R.drawable.edt_txt_background);
                edtName.setFocusableInTouchMode(true);
                edtImmuneNote.setBackgroundResource(R.drawable.edt_txt_background);
                edtImmuneNote.setFocusableInTouchMode(true);
                edtFile.setBackgroundResource(R.drawable.edt_txt_background);
                imgCal.setVisibility(View.VISIBLE);
                imgClock.setVisibility(View.VISIBLE);
                edtSurgeryName.setSelection(edtSurgeryName.length());
                edtName.setSelection(edtName.length());
                edtImmuneNote.setSelection(edtImmuneNote.length());
                edtFile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cameraDailog();
                    }
                });

                edtStartDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //showDialog(999);
                        hideKeyboard(edtStartDate);
                        calendar = Calendar.getInstance();
                        mView = LayoutInflater.from(PatientSurgeryActivity.this).inflate(R.layout.popup_calender_view, null, false);
                        popUp = new PopupWindow(mView, LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT, false);
                        popUp.setTouchable(true);
                        popUp.setFocusable(true);
                        popUp.setBackgroundDrawable(new BitmapDrawable());
                        popUp.setFocusable(true);
                        popUp.setOutsideTouchable(true);
                        //Solution

                        popUp.showAsDropDown(edtStartDate);
                        calendarView = mView.findViewById(R.id.calendar_view);
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        calendar.add(Calendar.YEAR, 1);
                        calendarView.setMaxDate(Calendar.getInstance().getTimeInMillis());
                        String[] parts = edtStartDate.getText().toString().split("/");
                        //Solution
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        try {
                            Date date = (Date)formatter.parse(parts[0]+"-"+parts[1]+"-"+parts[2]);
                            long out = date.getTime();
                            Log.e("tte","af"+out);
                            calendarView.setDate(out);
                        } catch (ParseException e) {
                            Log.e("tte","af"+e);
                            e.printStackTrace();
                        }

                        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                            @Override
                            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                                String msg = dayOfMonth + "/" + (month + 1) + "/" + year;
                                edtStartDate.setText(msg);
                                popUp.dismiss();
                            }
                        });


                    }
                });

            }
        });


    }
    private void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager)PatientSurgeryActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static final int CAMERA_REQUEST_CODE = 1001;
    public static final int GALLERY_REQUEST_CODE = 1002;
    public static final int CAMERA_PERMISSION_CODE = 5001;
    public static final int GALLERY_PERMISSION_CODE = 5002;
    private static final int PICKFILE_RESULT_CODE = 1;


    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, options);
            Bitmap rotatedBitmap = rotatedImageBitmap(mCurrentPhotoPath, bitmap);
            //  imgView.setImageBitmap(getResizedBitmap(rotatedBitmap, 500));
            loadFile();
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            Toast.makeText(PatientSurgeryActivity.this, "Image Capturing Cancelled", Toast.LENGTH_SHORT).show();
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
            }
            Bitmap rotatedBitmap = rotatedImageBitmap(mCurrentPhotoPath, bitmap);
            loadFile();
            // imgView.setImageBitmap(getResizedBitmap(rotatedBitmap, 500));
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            Toast.makeText(PatientSurgeryActivity.this, "Image Selection Cancelled", Toast.LENGTH_SHORT).show();
        } else if (resultCode == RESULT_OK) {
            Uri uri = imageReturnedIntent.getData();
            File myFile = new File(uri.toString());
            mCurrentPhotoPath = myFile.getPath();
            Log.e("noooo", "call" + mCurrentPhotoPath);
            try {
                mCurrentPhotoPath = getFilePath(getApplicationContext(), uri);
                Log.e("mpath", "call" + getFilePath(getApplicationContext(), uri));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            loadFile();
        }
    }

    @SuppressLint("NewApi")
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {

            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }

            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private void loadFile() {
        if (mCurrentPhotoPath == null) {
            Bitmap bitMap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_human_dummy3x);
            file = createImageFileFromBitmap(bitMap);
        } else {
            file = new File(mCurrentPhotoPath);
        }
        filenmae = file.getName();
        edtFile.setText(filenmae);
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
                Uri photoURI = FileProvider.getUriForFile(PatientSurgeryActivity.this,
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

    private Bitmap bitmap;

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
        TextView cam, gal, canc, file;
        AlertDialog.Builder dialog1 = new AlertDialog.Builder(PatientSurgeryActivity.this);
        LayoutInflater inflater = PatientSurgeryActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.file_dailog, null);
        dialog1.setView(dialogView);
        cam = dialogView.findViewById(R.id.camera);
        gal = dialogView.findViewById(R.id.gallery);
        canc = dialogView.findViewById(R.id.cancel);
        canc.setTextColor(Color.parseColor("#ED5276"));
        file = dialogView.findViewById(R.id.file);
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(PatientSurgeryActivity.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    dialog.dismiss();
                    LoadCaptureImageScreen();
                } else {
                    dialog.dismiss();
                    requestCameraPermission();
                }
            }
        });
        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(PatientSurgeryActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    dialog.dismiss();
                    showFileChooser();
                } else {
                    requestStoragePermission();
                }

            }
        });
        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(PatientSurgeryActivity.this,
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

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"), 1);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LoadCaptureImageScreen();
                } else {
                    Toast.makeText(PatientSurgeryActivity.this, "Yay! You Denied Permission", Toast.LENGTH_SHORT).show();
                }
                break;
            case GALLERY_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LoadImageFromGallery();
                } else {
                    Toast.makeText(PatientSurgeryActivity.this, "Yay! You Denied Permission", Toast.LENGTH_SHORT).show();
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
                            ActivityCompat.requestPermissions(PatientSurgeryActivity.this,
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
            ActivityCompat.requestPermissions(PatientSurgeryActivity.this,
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
                            ActivityCompat.requestPermissions(PatientSurgeryActivity.this,
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
            ActivityCompat.requestPermissions(PatientSurgeryActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
        }
    }

    public void clockItemsClickListners() {
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clockView.setVisibility(View.GONE);
                String hour = txtHours.getText().toString();
                String minit = txtMin.getText().toString();
                String time = hour + " : " + minit;
                edtTime.setText(time);
                popUp.dismiss();

            }
        });
        txtAm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAm.setBackgroundResource(R.color.colorpink);
                txtPm.setBackgroundResource(R.color.textBackground);

            }
        });
        txtPm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAm.setBackgroundResource(R.color.colorpink);
                txtPm.setBackgroundResource(R.color.textBackground);
            }
        });
        imgMinUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minits = txtMin.getText().toString();
                k = Integer.parseInt(minits);

                if (k == 55) {
                    k = 0;
                    txtMin.setText(String.valueOf(k));
                } else {
                    i = k + 5;
                    txtMin.setText(String.valueOf(i));
                }

            }
        });
        imgMinDwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minits = txtMin.getText().toString();
                k = Integer.parseInt(hours);

                if (k == 0) {
                    k = 55;
                    txtMin.setText(String.valueOf(k));
                } else {
                    i = k - 5;
                    txtMin.setText(String.valueOf(i));
                }
            }
        });

        imgHrUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minits = txtMin.getText().toString();
                j = Integer.parseInt(minits);

                hours = txtHours.getText().toString();
                k = Integer.parseInt(hours);

                if (k == 12) {
                    k = 0;
                    i = k + 1;
                    txtHours.setText(String.valueOf(i));
                } else {
                    i = k + 1;
                    txtHours.setText(String.valueOf(i));
                }

            }
        });
        imgHrDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hours = txtHours.getText().toString();
                k = Integer.parseInt(hours);

                if (k == 1) {
                    k = 12;
                    txtHours.setText(String.valueOf(k));
                } else {
                    i = k - 1;
                    txtHours.setText(String.valueOf(i));
                }
            }
        });

    }

}
