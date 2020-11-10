package com.vedas.spectrocare.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.Controllers.MedicationServerObjectDataController;
import com.vedas.spectrocare.Controllers.MedicinesServerObjectDataController;
import com.vedas.spectrocare.Controllers.PhysicalServerObjectDataController;
import com.vedas.spectrocare.DataBase.IllnessDataController;
import com.vedas.spectrocare.DataBase.MedicationRecordDataController;
import com.vedas.spectrocare.DataBase.MedicinesRecordDataController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApiModel.MedicationAttachServerObject;
import com.vedas.spectrocare.ServerApiModel.MedicationManullayServerObject;
import com.vedas.spectrocare.SingleTapDetector;
import com.vedas.spectrocare.adapter.MedicationRecordsAdapter;
import com.vedas.spectrocare.adapter.ViewMedicalAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class MedicationRecordActivity extends AppCompatActivity {
    FloatingActionButton addAllMedicalRecords;
    private float dX, dY;
    int lastAction;
    GestureDetector gestureDetector;
    TextView txt_nodata;
    BottomSheetDialog bottomSheetDialog, fileBottomSheet;
    ProgressBar progressBar;
    public static AlertDialog alertDialog;
    RecyclerView recyclerView;
    MedicationRecordsAdapter viewMedicalAdapter;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_medical_record);
        addAllMedicalRecords = findViewById(R.id.add_all_medical_record);
        txt_nodata = findViewById(R.id.text_medical_disc);
        recyclerView = findViewById(R.id.all_medical_record_recycler_view);
        gestureDetector = new GestureDetector(this, new SingleTapDetector());
        ImageView imgButton = findViewById(R.id.img_back);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loadSpinner();
        recyclerView();

        addAllMedicalRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet();
            }
        });
        if (isConn()) {
            alertDialog.show();
            Objects.requireNonNull(alertDialog.getWindow()).setLayout(600, 500);
            MedicationServerObjectDataController.getInstance().medicinesFetchApiCall();

        } else {
            // dialogeforCheckavilability("Error", "No Internet connection", "Ok");
        }
        loadPlusButton();
        requestStoragePermission();
        accessInterfaceMethods();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResume() {
        super.onResume();
        Log.e("onResume", "call");
        MedicationRecordDataController.getInstance().fetchMedicationData(IllnessDataController.getInstance().currentIllnessRecordModel);
        if (!MedicationRecordDataController.getInstance().allMedicationList.isEmpty()) {
            txt_nodata.setVisibility(View.GONE);
        } else {
            txt_nodata.setVisibility(View.VISIBLE);
        }
        viewMedicalAdapter.notifyDataSetChanged();
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
    public void onEventMainThread(ViewMedicalAdapter.MessageEvent event) {
        String resultData = event.message.trim();
        if (resultData.equals("addattach")) {
            fileBottomSheet.dismiss();
            alertDialog.dismiss();
            recyclerView();
        } else if(resultData.equals("deletem")) {
            if (isConn()) {
                MedicationRecordDataController.getInstance().currentMedicationRecordModel=MedicationRecordDataController.getInstance().allMedicationList.get(event.position);
                alertDialog.show();
                accessInterfaceMethods();
                //  Objects.requireNonNull(MedicationRecordActivity.alertDialog.getWindow()).setLayout(600, 500);
                MedicationServerObjectDataController.getInstance().medicationDeleteApiCall(MedicationRecordDataController.getInstance().currentMedicationRecordModel);
            }
        } else {
            alertDialog.dismiss();
            Toast.makeText(getApplicationContext(), resultData, Toast.LENGTH_SHORT).show();
        }

    }

    public void recyclerView() {
        if (!MedicationRecordDataController.getInstance().allMedicationList.isEmpty()) {
            txt_nodata.setVisibility(View.GONE);
        } else {
            txt_nodata.setVisibility(View.VISIBLE);
        }
        /*MedicationRecordDataController.getInstance().fetchMedicationData(IllnessDataController.getInstance().currentIllnessRecordModel);
        if (MedicationRecordDataController.getInstance().allMedicationList.size() > 0) {
            txt_nodata.setVisibility(View.GONE);
        } else {
            txt_nodata.setVisibility(View.VISIBLE);
        }*/
        viewMedicalAdapter = new MedicationRecordsAdapter(MedicationRecordActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(viewMedicalAdapter);
        viewMedicalAdapter.notifyDataSetChanged();
    }

    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                Log.e("success", "call" + curdOpetaton);
                if (curdOpetaton.equals("fetch")) {
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("illnessMedicationRecords");
                        Log.e("medicationArray", "call" + jsonArray.length());
                        MedicationRecordDataController.getInstance().deleteMedicationData(IllnessDataController.getInstance().currentIllnessRecordModel);
                        MedicinesRecordDataController.getInstance().deleteMedicinesData(MedicationRecordDataController.getInstance().currentMedicationRecordModel);
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Gson gson = new Gson();
                                MedicationManullayServerObject userIdentifier = gson.fromJson(jsonArray.getJSONObject(i).toString(), MedicationManullayServerObject.class);
                                if (userIdentifier.getMannualPrescriptions() != null) {
                                    Log.e("medicines", "call" + userIdentifier.getMannualPrescriptions().get("medicines"));
                                    MedicationServerObjectDataController.getInstance().processAddMedicationAddData(userIdentifier);
                                    MedicinesServerObjectDataController.getInstance().medicineAddData(userIdentifier);
                                }
                                if (userIdentifier.getAttachedPrescriptions() != null) {
                                    MedicationServerObjectDataController.getInstance().processAttachmentsMedicationAddData(userIdentifier);
                                    Log.e("attachments", "call" + userIdentifier.getAttachedPrescriptions().get("attachments"));
                                    MedicinesServerObjectDataController.getInstance().attachmentsAddData(userIdentifier);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (curdOpetaton.equals("delete")) {
                    MedicationRecordDataController.getInstance().deleteMedicationRecordModelData(IllnessDataController.getInstance().currentIllnessRecordModel, MedicationRecordDataController.getInstance().currentMedicationRecordModel);
                    MedicationRecordDataController.getInstance().fetchMedicationData(IllnessDataController.getInstance().currentIllnessRecordModel);
                    if (MedicationRecordDataController.getInstance().allMedicationList.size() > 0) {
                        txt_nodata.setVisibility(View.GONE);
                    } else {
                        txt_nodata.setVisibility(View.VISIBLE);
                    }
                }
                alertDialog.dismiss();
                recyclerView();
            }

            @Override
            public void failureCallBack(String failureMsg) {
                alertDialog.dismiss();
                Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSpinner() {
        LayoutInflater inflater = MedicationRecordActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alertbox_layout, null);
        AlertDialog.Builder dialog1 = new AlertDialog.Builder(MedicationRecordActivity.this);
        progressBar = dialogView.findViewById(R.id.progressBar);
        dialog1.setView(dialogView);
        alertDialog = dialog1.create();
    }

    private void showBottomSheet() {
        try {
            View sheetView = getLayoutInflater().inflate(R.layout.dialogue_medication_sheet, null);
            bottomSheetDialog = new BottomSheetDialog(MedicationRecordActivity.this,R.style.BottomSheetDialogTheme);
            bottomSheetDialog.setCanceledOnTouchOutside(true);
            bottomSheetDialog.setCancelable(true);
            bottomSheetDialog.setContentView(sheetView);
            bottomSheetDialog.show();
            // Remove default white color background
            FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            bottomSheet.setBackground(null);
            LinearLayout l_addManual = bottomSheetDialog.findViewById(R.id.l_add);
            LinearLayout l_attach = bottomSheetDialog.findViewById(R.id.l_attach);
            LinearLayout l_dismiss = bottomSheetDialog.findViewById(R.id.l_dismiss);
            l_addManual.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                    MedicationRecordDataController.getInstance().currentMedicationRecordModel = null;
                    Intent viewMedicalIntent = new Intent(MedicationRecordActivity.this, MedicinesRecordActivity.class);
                    startActivity(viewMedicalIntent);
                }
            });
            l_attach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                    showFileBottomSheet();
                }
            });
            l_dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fileSelectionBottomSheet() {
        try {
            View sheetView = getLayoutInflater().inflate(R.layout.dialogue_medication_sheet, null);
            final BottomSheetDialog fileSelectionSheetDialog = new BottomSheetDialog(MedicationRecordActivity.this);
            fileSelectionSheetDialog.setContentView(sheetView);
            fileSelectionSheetDialog.show();
            // Remove default white color background
            FrameLayout bottomSheet = (FrameLayout) fileSelectionSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            bottomSheet.setBackground(null);
            LinearLayout l_gallery = fileSelectionSheetDialog.findViewById(R.id.l_add);
            TextView txt_gallery = fileSelectionSheetDialog.findViewById(R.id.text1);
            txt_gallery.setText("Gallery");
            TextView txt_file = fileSelectionSheetDialog.findViewById(R.id.text2);
            txt_file.setText("My Files");
            LinearLayout l_file = fileSelectionSheetDialog.findViewById(R.id.l_attach);
            LinearLayout l_dismiss = fileSelectionSheetDialog.findViewById(R.id.l_dismiss);
            l_gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fileSelectionSheetDialog.dismiss();
                    LoadImageFromGallery();
                }
            });
            l_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fileSelectionSheetDialog.dismiss();
                    showFileChooser();
                }
            });
            l_dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fileSelectionSheetDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String mCurrentPhotoPath;
    File firstFile, secondFile;
    TextView fielPathOne, fielPathtwo;
    boolean isFirstSelect;
    MedicationAttachServerObject serverObject = new MedicationAttachServerObject();

    private void showFileBottomSheet() {
        try {
            View sheetView = getLayoutInflater().inflate(R.layout.dialogue_fileattachmedication_sheet, null);
            fileBottomSheet = new BottomSheetDialog(MedicationRecordActivity.this, R.style.BottomSheetDialogTheme);
            fileBottomSheet.setContentView(sheetView);
            fileBottomSheet.show();
            // Remove default white color background
            FrameLayout bottomSheet = (FrameLayout) fileBottomSheet.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            bottomSheet.setBackground(null);
            ImageView close = fileBottomSheet.findViewById(R.id.img_close);
            fielPathOne = fileBottomSheet.findViewById(R.id.edt_file_path);
            fielPathtwo = fileBottomSheet.findViewById(R.id.edt_file_path1);
            final EditText disOne = fileBottomSheet.findViewById(R.id.edt_screen_medic_record_description);
            final EditText disTwo = fileBottomSheet.findViewById(R.id.edt_screen_record_description1);
            Button addBtn = fileBottomSheet.findViewById(R.id.btn_add_screening_record_details);

            fielPathOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isFirstSelect = true;
                    fileSelectionBottomSheet();
                   /*
                    if (ActivityCompat.checkSelfPermission(MedicationRecordActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        showFileChooser();
                    } *//*else {
                        requestStoragePermission();
                    }*/
                }
            });
            fielPathtwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isFirstSelect = false;
                    fileSelectionBottomSheet();
                   /*
                    if (ActivityCompat.checkSelfPermission(MedicationRecordActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        showFileChooser();
                    } *//*else {
                        requestStoragePermission();
                    }*/
                }
            });
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fileBottomSheet.dismiss();
                }
            });
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    serverObject.setPrescription1MoreDetails(disOne.getText().toString());
                    Log.e("tectt","hfh"+disOne.getText().toString());
                    serverObject.setPrescription2MoreDetails1(disTwo.getText().toString());

                    loadFile();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
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
                            ActivityCompat.requestPermissions(MedicationRecordActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(MedicationRecordActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
        }
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

    public static final int GALLERY_PERMISSION_CODE = 5002;
    public static final int GALLERY_REQUEST_CODE = 1002;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case GALLERY_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LoadImageFromGallery();
                } else {
                    Toast.makeText(MedicationRecordActivity.this, "Yay! You Denied Permission", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }

    private void LoadImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        //  galleryIntent.setType("*/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        if (resultCode == RESULT_OK) {
            Uri uri = imageReturnedIntent.getData();
            String filenmae = getFileName(uri);
            Log.e("fielnamu", "call" + filenmae);
            loadFileNmaes(uri, filenmae);
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri uri = imageReturnedIntent.getData();
            if (uri != null && uri.toString()
                    .startsWith("content://com.google.android.apps.photos.content")) {
                if (uri.toString().contains("video")) {
                    Toast.makeText(this, "Hey ! It's Video Buddy", Toast.LENGTH_SHORT).show();
                    return;
                }
                createImageFromPhotosUri(uri);
            } else {
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mCurrentPhotoPath = cursor.getString(columnIndex);
                cursor.close();
                options.inSampleSize = 2;
                // bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, options);
            }
            Log.e("mpath", "call" + mCurrentPhotoPath);
            String filenmae = getFileName(uri);
            Log.e("fielnamu", "call" + filenmae);
            loadFileNmaes(uri, filenmae);
        }
    }

    private void loadFileNmaes(Uri uri, String filenmae) {
        if (isFirstSelect) {
            fielPathOne.setText(filenmae);
            firstFile = new File(uri.toString());
            mCurrentPhotoPath = firstFile.getPath();
            try {
                mCurrentPhotoPath = getFilePath(getApplicationContext(), uri);
                Log.e("mpathone", "call" + getFilePath(getApplicationContext(), uri));
                serverObject.setPrescription1(new File(mCurrentPhotoPath));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            fielPathtwo.setText(filenmae);
            secondFile = new File(uri.toString());
            mCurrentPhotoPath = secondFile.getPath();
            try {
                mCurrentPhotoPath = getFilePath(getApplicationContext(), uri);
                serverObject.setPrescription2(new File(mCurrentPhotoPath));
                Log.e("mpathtwo", "call" + getFilePath(getApplicationContext(), uri));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadFile() {
        serverObject.setDoctorName("Chandu");
        serverObject.setDoctorMedicalPersonnelID("DcM2345");
        serverObject.setIllnessID(IllnessDataController.getInstance().currentIllnessRecordModel.getIllnessRecordId());
        if (isConn()) {
            alertDialog.show();
            MedicationServerObjectDataController.getInstance().addApiCallForAttchmet(serverObject);
        } else {
            // dialogeforCheckavilability("Error", "No Internet connection", "Ok");
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

    public boolean isConn() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
    }

    private void createImageFromPhotosUri(Uri selectedImage) {
        try {
            InputStream is = getContentResolver().openInputStream(selectedImage);
            if (is != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(is);
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
        Log.e("currentpath", "call" + mCurrentPhotoPath);
        return image;
    }

    private void loadPlusButton() {
        addAllMedicalRecords.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    showBottomSheet();
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
}
