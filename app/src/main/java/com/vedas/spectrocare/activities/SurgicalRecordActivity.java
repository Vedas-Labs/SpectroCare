package com.vedas.spectrocare.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.BuildConfig;
import com.vedas.spectrocare.CallBackTask;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.Controllers.PhysicalServerObjectDataController;
import com.vedas.spectrocare.Controllers.SurgicalRecordServerObjectDataController;
import com.vedas.spectrocare.DataBase.IllnessDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBase.SurgricalRecordDataControll;
import com.vedas.spectrocare.DataBaseModels.SurgicalRecordModel;
import com.vedas.spectrocare.DownloadAsyncTask;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApiModel.AttachmentServerObjects;
import com.vedas.spectrocare.ServerApiModel.SurgicalServerObject;
import com.vedas.spectrocare.SingleTapDetector;
import com.vedas.spectrocare.adapter.SurgeryRecordAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.vedas.spectrocare.activities.PhysicalExamActivity.CAMERA_PERMISSION_CODE;
import static com.vedas.spectrocare.activities.PhysicalExamActivity.CAMERA_REQUEST_CODE;
import static com.vedas.spectrocare.activities.PhysicalExamActivity.GALLERY_PERMISSION_CODE;
import static com.vedas.spectrocare.activities.PhysicalExamActivity.GALLERY_REQUEST_CODE;
import static com.vedas.spectrocare.activities.ScreeningRecordActivity.getFilePath;
import static com.vedas.spectrocare.activities.ScreeningRecordActivity.isDownloadsDocument;
import static com.vedas.spectrocare.activities.ScreeningRecordActivity.isExternalStorageDocument;
import static com.vedas.spectrocare.activities.ScreeningRecordActivity.isGooglePhotosUri;
import static com.vedas.spectrocare.activities.ScreeningRecordActivity.isMediaDocument;
import static com.vedas.spectrocare.activities.ScreeningRecordActivity.showingDialog;

public class SurgicalRecordActivity extends AppCompatActivity implements CallBackTask {
    private static final int FILE_PICKER_REQUEST_CODE = 8001;
    FloatingActionButton btnAddSurgicalRecord;
    private float dX, dY;
    private String mCurrentPhotoPath;
    String  tempbirthDayObj="";
    EditText ed_moreInfo,edtDocName,edtSurgeryPro;
    File file;
    JSONObject params;
    String filenmae;
    TextView edtSurgeryDate,txtSurgDisc;
    EditText attachment;
    AlertDialog dialog;
    int lastAction;
    GestureDetector gestureDetector;
    RecyclerView surgicalRecycleView;
    public static RefreshShowingDialog showingDialog;
    SurgeryRecordAdapter surgeryRecordAdapter;
    BottomSheetDialog attachmentDialog;
    int year, month, day;
    private Bitmap bitmap;
    String m,d;
    ImageView imgButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surgical_record);
        btnAddSurgicalRecord = findViewById(R.id.btn_add_surgical_record);
        surgicalRecycleView = findViewById(R.id.surgical_recycler_view);
        gestureDetector = new GestureDetector(this, new SingleTapDetector());
        showingDialog=new RefreshShowingDialog(SurgicalRecordActivity.this);
        imgButton = findViewById(R.id.img_surgical_record_back);
        txtSurgDisc = findViewById(R.id.text_surge_disc);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        tempbirthDayObj = dateFormat.format(currentDate);

        if (isConn()) {
            showingDialog.showAlert();
            SurgicalRecordServerObjectDataController.getInstance().fetchSurgicalRecoreds();
        }
        getRecyclerView();
        accessInterfaceMethods();
        loadPlusButton();
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.e("onResume", "call");
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
        if (resultData.equals("addSurgical")) {
            getRecyclerView();
            attachmentDialog.dismiss();
            showingDialog.hideRefreshDialog();
        }
    }
    private void getRecyclerView(){
        ArrayList<SurgicalRecordModel>  surgicalItemList = SurgricalRecordDataControll.getInstance().fetchingSurgicalData(IllnessDataController.getInstance().currentIllnessRecordModel);
        if (!surgicalItemList.isEmpty()){
            txtSurgDisc.setVisibility(View.GONE);
        }else {
            txtSurgDisc.setVisibility(View.VISIBLE);
        }
        surgicalRecycleView.setLayoutManager(new LinearLayoutManager(SurgicalRecordActivity.this));
        surgicalRecycleView.setHasFixedSize(true);
        surgeryRecordAdapter = new SurgeryRecordAdapter(SurgicalRecordActivity.this, surgicalItemList);
        surgicalRecycleView.setAdapter(surgeryRecordAdapter);
        surgeryRecordAdapter.notifyDataSetChanged();
    }

    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                if (curdOpetaton.equals("fetch")) {
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("illnessSurgicalRecords");
                        Log.e("jsonarray","call"+jsonArray.length());
                        SurgricalRecordDataControll.getInstance().deleteSurgicalData(IllnessDataController.getInstance().currentIllnessRecordModel);
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Gson gson = new Gson();
                                SurgicalServerObject userIdentifier = gson.fromJson(jsonArray.getJSONObject(i).toString(), SurgicalServerObject.class);
                                ArrayList<AttachmentServerObjects> attachArray = userIdentifier.getAttachmentLis();
                                Log.e("kaka",""+userIdentifier.getDoctorName()+userIdentifier.getIllnessSurgicalID());
                                SurgicalRecordServerObjectDataController.getInstance().processfetchSurgeryAddData(userIdentifier,attachArray);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (curdOpetaton.equals("delete")) {
                    SurgricalRecordDataControll.getInstance().deleteSurgicalRecordModelData(IllnessDataController.getInstance().currentIllnessRecordModel, SurgricalRecordDataControll.getInstance().currentSurgicalmodel);
                }
                showingDialog.hideRefreshDialog();
                getRecyclerView();
            }

            @Override
            public void failureCallBack(String failureMsg) {
                showingDialog.hideRefreshDialog();
                Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Permission Info")
                    .setMessage("Camera Permission is Needed for Adding your Profile Image")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(SurgicalRecordActivity.this,
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
            ActivityCompat.requestPermissions(SurgicalRecordActivity.this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void bottomDialogSheet() {
        View dialogFamilyView = getLayoutInflater().inflate(R.layout.surgical_bottom_sheet, null);
        attachmentDialog = new BottomSheetDialog(Objects.requireNonNull(SurgicalRecordActivity.this), R.style.BottomSheetDialogTheme);
        attachmentDialog.setContentView(dialogFamilyView);
        attachmentDialog.setCanceledOnTouchOutside(true);
        attachmentDialog.setCancelable(true);
        Button add_btn = attachmentDialog.findViewById(R.id.btn_add_surgery_record);
        attachment = attachmentDialog.findViewById(R.id.edt_attachment_file);
        ed_moreInfo =attachmentDialog.findViewById(R.id.edt_surgical_description);
        edtDocName = attachmentDialog.findViewById(R.id.edt_doctor_name);
        edtSurgeryPro = attachmentDialog.findViewById(R.id.edt_surgery_procedure);
        edtSurgeryDate = attachmentDialog.findViewById(R.id.edt_surgery_date);
        ImageView imgView = attachmentDialog.findViewById(R.id.img_close);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachmentDialog.cancel();
            }
        });
        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraBottomSheet();
            }
        });
        edtSurgeryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDatePicker();
            }
        });
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFile();
            }
        });

        attachmentDialog.show();
    }
    public void loadDatePicker() {
        final DatePickerDialog dialog;
      if (edtSurgeryDate.getText().toString().isEmpty()) {
            Log.e("ifcall", "call");
            String[] txtBirthdayArray = tempbirthDayObj.split("-");
            year = Integer.parseInt(txtBirthdayArray[0]);
            month = Integer.parseInt(txtBirthdayArray[1]);
            day = Integer.parseInt(txtBirthdayArray[2]);
            Log.e("textdate", "call" + year + "-" + month + "-" + day);
            dialog = new DatePickerDialog(this, null, year, month - 1, day);
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            dialog.show();
        }else {
            tempbirthDayObj = edtSurgeryDate.getText().toString();
            String[] txtBirthdayArray = tempbirthDayObj.split("-");
            year = Integer.parseInt(txtBirthdayArray[0]);
            month = Integer.parseInt(txtBirthdayArray[1]);
            day = Integer.parseInt(txtBirthdayArray[2]);
            Log.e("textdate", "call" + year + "-" + month + "-" + day);
            dialog = new DatePickerDialog(this, null, year, month - 1, day);
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            dialog.show();
        }
        Log.e("textdate", "call" + year + "-" + month + "-" + day);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE,
                "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog1, int which) {
                        DatePicker objDatePicker = dialog.getDatePicker();
                        year = objDatePicker.getYear();
                        month = objDatePicker.getMonth();
                        day = objDatePicker.getDayOfMonth();
                        month=month+1;
                        if(month<10){
                            m="0"+month;
                        }
                        else{
                            m=""+month;
                        }
                        if(day<10){
                            d="0"+day;
                        }else {
                            d=""+day;
                        }
                        edtSurgeryDate.setText(year + "-" + m + "-" + d);
                        Log.e("txt_dob", "" + edtSurgeryDate.getText().toString());
                    }
                });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                    }
                });
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
    public boolean isConn() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
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
                Uri photoURI = FileProvider.getUriForFile(SurgicalRecordActivity.this,
                        "com.vedas.spectrocare.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    public static final int CAMERA_REQUEST_CODE = 1001;
    public static final int GALLERY_REQUEST_CODE = 1002;
    public static final int CAMERA_PERMISSION_CODE = 5001;
    public static final int GALLERY_PERMISSION_CODE = 5002;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        Log.d("sample", "onActivityResult: "+requestCode);
        Log.d("sample", "onActivityResult: "+resultCode);
      //  Log.d("sample", "onActivityResult: "+imageReturnedIntent.toString());
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, options);
            // Bitmap rotatedBitmap = rotatedImageBitmap(mCurrentPhotoPath, bitmap);
            Log.e("mpath", "call" + mCurrentPhotoPath);
            File f = new File(mCurrentPhotoPath);
            Log.e("mpath", "call" + f.getName());
            filenmae = f.getName();

            attachment.setText(filenmae);
            file = f;
            //loadFile();
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            Toast.makeText(SurgicalRecordActivity.this, "Image Capturing Cancelled", Toast.LENGTH_SHORT).show();
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
            Log.e("mpath", "call" + mCurrentPhotoPath);
            filenmae = getFileName(selectedImage);
            attachment.setText(filenmae);
            // loadFile();
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            Toast.makeText(SurgicalRecordActivity.this, "Image Selection Cancelled", Toast.LENGTH_SHORT).show();
        } else if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK ){

            Uri uri = imageReturnedIntent.getData();

            if (uri.toString().startsWith("content://com.google.android.apps.docs.storage")) {
                Log.e("filePicker",""+imageReturnedIntent);
                filenmae = getFileName(uri);
                attachment.setText(filenmae);
               new DownloadAsyncTask(uri, this, this).execute();
            }else{
                filenmae= getFileName(uri);
                Log.e("fielnamu","call"+filenmae);
                attachment.setText(filenmae);
                file = new File(uri.toString());
                mCurrentPhotoPath = file.getPath();
                try {
                    mCurrentPhotoPath=getFilePath(getApplicationContext(),uri);
                    Log.e("mpath","call"+getFilePath(getApplicationContext(),uri));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
       /* else if (resultCode == RESULT_OK) {
            Uri uri = imageReturnedIntent.getData();
            Log.e("ajajaj",""+uri);
            filenmae = getFileName(uri);

            attachment.setText(filenmae);
            file = new File(uri.toString());
            mCurrentPhotoPath = file.getPath();
            try {
                mCurrentPhotoPath=getFilePath(getApplicationContext(),uri);
                Log.e("mpath","call"+getFilePath(getApplicationContext(),uri));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            // loadFile();
        }*/
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

    private void loadFile(){
        if (mCurrentPhotoPath == null) {
            Bitmap bitMap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_human_dummy3x);
            file = createImageFileFromBitmap(bitMap);
        } else {
            file = new File(mCurrentPhotoPath);
            Log.e("takeFi",""+file);
        }
        if(isConn()) {
            showingDialog.showAlert();
            SurgicalRecordServerObjectDataController.getInstance().addSurgicalCallForAttchmet(file,filenmae,
                    ed_moreInfo.getText().toString(),edtDocName.getText().toString(),edtSurgeryDate.getText().toString(),edtSurgeryPro.getText().toString());
        }else {
            showingDialog.dismiss();
            Toast.makeText(this, "please check your connection", Toast.LENGTH_SHORT).show();
            // dialogeforCheckavilability("Error", "No Internet connection", "Ok");
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
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

            e.printStackTrace();
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void CameraBottomSheet() {
        TextView cam, gal, canc, file;
        View cameraView = getLayoutInflater().inflate(R.layout.file_dailog, null);
        final BottomSheetDialog cameraBottomSheet = new BottomSheetDialog(Objects.requireNonNull(SurgicalRecordActivity.this), R.style.BottomSheetDialogTheme);
        cameraBottomSheet.setContentView(cameraView);
        FrameLayout bottomSheet = (FrameLayout) cameraBottomSheet.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        bottomSheet.setBackground(null);
        cameraBottomSheet.show();
        cam = cameraBottomSheet.findViewById(R.id.camera);
        gal = cameraBottomSheet.findViewById(R.id.gallery);
        file = cameraBottomSheet.findViewById(R.id.file);
        canc = cameraBottomSheet.findViewById(R.id.cancel);
        canc.setTextColor(Color.parseColor("#53B9c6"));
        canc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraBottomSheet.cancel();
            }
        });
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(SurgicalRecordActivity.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    cameraBottomSheet.cancel();
                    LoadCaptureImageScreen();
                } else {
                    cameraBottomSheet.cancel();
                    requestCameraPermission();
                }
            }

        });
        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(SurgicalRecordActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    cameraBottomSheet.cancel();
                    LoadImageFromGallery();
                } else {
                    cameraBottomSheet.cancel();
                    requestStoragePermission();
                }

            }
        });
        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(SurgicalRecordActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    cameraBottomSheet.cancel();
                    showFileChooser();
                } else {
                    cameraBottomSheet.cancel();
                    requestStoragePermission();
                }

            }
        });


    }
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
     //   intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"), FILE_PICKER_REQUEST_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void LoadImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Permission Info")
                    .setMessage("Gallery Permission is needed for adding your Profile Image")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.dismiss();
                            ActivityCompat.requestPermissions(SurgicalRecordActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
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
            ActivityCompat.requestPermissions(SurgicalRecordActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
        }
    }
    private void loadPlusButton(){
        btnAddSurgicalRecord.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    bottomDialogSheet();
                } else {
                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            dX = v.getX() - event.getRawX();
                            dY = v.getY() - event.getRawY();
                            lastAction = MotionEvent.ACTION_DOWN;
                            break;

                        case MotionEvent.ACTION_MOVE:
                            v.setY(event.getRawY() + dY);
                            v.setX(event.getRawX() + dX);
                            lastAction = MotionEvent.ACTION_MOVE;
                            break;

                        case MotionEvent.ACTION_UP:
                            if (lastAction == MotionEvent.ACTION_DOWN)
                                // Toast.makeText(DraggableView.this, "Clicked!", Toast.LENGTH_SHORT).show();
                                break;

                        default:
                            return false;
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void getResultFromAsynTask(String result) {
        mCurrentPhotoPath = result;
        Log.d("smaple", "getResultFromAsynTask: "+result);
        Log.d("smaple", "mCurrentPhotoPath: "+mCurrentPhotoPath);
    }
}
