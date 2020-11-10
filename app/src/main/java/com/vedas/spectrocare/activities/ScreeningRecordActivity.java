package com.vedas.spectrocare.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
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
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
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
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.CallBackTask;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.Controllers.PhysicalServerObjectDataController;
import com.vedas.spectrocare.Controllers.ScreeningServerObjectDataController;
import com.vedas.spectrocare.DataBase.FamilyHistoryDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBase.ScreeningRecordDataController;
import com.vedas.spectrocare.DownloadAsyncTask;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApiModel.ScreeningServerObject;
import com.vedas.spectrocare.ServerApiModel.TrackingServerObject;
import com.vedas.spectrocare.SingleTapDetector;
import com.vedas.spectrocare.adapter.ScreeningRecordAdapter;

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
import java.util.List;
import java.util.Objects;

public class ScreeningRecordActivity extends AppCompatActivity implements CallBackTask {
    private static final int FILE_PICKER_REQUEST_CODE = 8001;
    FloatingActionButton btnScreeningRecord;
    ImageView imgFile;
    TextView txtFile,txtScreeningDisc;
    EditText ed_moreInfo;
    ImageView imgView,imgButton,attchmentImg;
    List screeningItemList;
    LayoutInflater inflater;
    AlertDialog dialog;
    private String mCurrentPhotoPath;
    BottomSheetDialog attachmentDialog;
    ScreeningRecordAdapter screeningRecordAdapter;
    RecyclerView screeningRecyclerView;
    public static RefreshShowingDialog showingDialog;
    File file;
    ArrayList<Integer> colorsArray = new ArrayList<>();
    String filenmae;
    int lastAction;
    private float dX, dY;
    GestureDetector gestureDetector;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening_record);

        attachmentDialog = new BottomSheetDialog(this);
        imgButton = findViewById(R.id.img_screen_record_back);
        imgFile = findViewById(R.id.img_screen_record_file);
        txtScreeningDisc = findViewById(R.id.text_screening_disc);
        gestureDetector = new GestureDetector(this, new SingleTapDetector());
        showingDialog=new RefreshShowingDialog(ScreeningRecordActivity.this);

        btnScreeningRecord = findViewById(R.id.btn_screening_record);
        Log.e("screening","ning");

        btnScreeningRecord.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                bottomDialogSheet();
            }
        });
        btnScreeningRecord.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    ScreeningRecordDataController.getInstance().currentScreeningRecordModel = null;
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

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if(isConn()){
            showingDialog.showAlert();
            ScreeningServerObjectDataController.getInstance().fetchScreeningRecord();
        }
        accessInterfaceMethods();
        loadRecyclerView();
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
        //   EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(PhysicalServerObjectDataController.MessageEvent event) {
        Log.e("sidemenuMessageevent", "" + event.message);
        String resultData = event.message.trim();
        if (resultData.equals("addScreening")) {
            loadRecyclerView();
            attachmentDialog.dismiss();
            showingDialog.hideRefreshDialog();
        }/*else if (resultData.equals("deleteScreening")) {
            loadRecyclerView();
            showingDialog.dismiss();
        }*/
    }
    private void loadRecyclerView(){
        screeningItemList = ScreeningRecordDataController.getInstance().fetchScreeningData(PatientProfileDataController.getInstance().currentPatientlProfile);
        if (screeningItemList.size() > 0) {
            for (int i = 0; i < screeningItemList.size(); i++) {
                colorsArray.add(PersonalInfoController.getInstance().getRandomColor());
            }
        }
        if (ScreeningRecordDataController.getInstance().fetchScreeningData(PatientProfileDataController.getInstance().currentPatientlProfile).isEmpty())
           txtScreeningDisc.setVisibility(View.VISIBLE);
       else txtScreeningDisc.setVisibility(View.GONE);
        screeningRecyclerView = findViewById(R.id.screening_recycler_view);
        screeningRecyclerView.setLayoutManager(new LinearLayoutManager(ScreeningRecordActivity.this));
        screeningRecyclerView.setHasFixedSize(true);
        screeningRecordAdapter = new ScreeningRecordAdapter(ScreeningRecordActivity.this, screeningItemList,colorsArray);
        screeningRecyclerView.setAdapter(screeningRecordAdapter);
    }
    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                if (curdOpetaton.equals("fetch")) {
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("illnessScreeningRecords");
                        Log.e("jsonarray","call"+jsonArray.length());
                        ScreeningRecordDataController.getInstance().deleteScreeningData(PatientProfileDataController.getInstance().currentPatientlProfile);
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Gson gson = new Gson();
                                ScreeningServerObject userIdentifier = gson.fromJson(jsonArray.getJSONObject(i).toString(), ScreeningServerObject.class);
                                ArrayList<TrackingServerObject> trackArray = userIdentifier.getTracking();
                                // Log.e("trackArray", "call" + trackArray.size());
                                ScreeningServerObjectDataController.getInstance().processfetchScreenAddData(userIdentifier,trackArray);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (curdOpetaton.equals("delete")) {
                    ScreeningRecordDataController.getInstance().deleteScreeningRecordModelData(PatientProfileDataController.getInstance().currentPatientlProfile, ScreeningRecordDataController.getInstance().currentScreeningRecordModel);
                }
                showingDialog.hideRefreshDialog();
                loadRecyclerView();
            }

            @Override
            public void failureCallBack(String failureMsg) {
                showingDialog.hideRefreshDialog();
                Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
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
    public static final int CAMERA_REQUEST_CODE = 1001;
    public static final int GALLERY_REQUEST_CODE = 1002;
    public static final int CAMERA_PERMISSION_CODE = 5001;
    public static final int GALLERY_PERMISSION_CODE = 5002;

   protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
       super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
       BitmapFactory.Options options = new BitmapFactory.Options();
       options.inJustDecodeBounds = false;
       options.inPurgeable = true;
     /*  Log.d("sample", "onActivityResult: "+requestCode);
       Log.d("sample", "onActivityResult: "+resultCode);
       Log.d("sample", "onActivityResult: "+imageReturnedIntent.toString());
*/
       if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

           Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, options);
           Bitmap rotatedBitmap = rotatedImageBitmap(mCurrentPhotoPath, bitmap);
           Log.e("mpath","call"+mCurrentPhotoPath);
           File f=new File(mCurrentPhotoPath);
           Log.e("mpath","call"+f.getName());
           filenmae= f.getName();
           Log.e("fielnamu","call"+filenmae);
           txtFile.setText(filenmae);
           file=f;
           //loadFile();
       } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_CANCELED) {
           Toast.makeText(ScreeningRecordActivity.this, "Image Capturing Cancelled", Toast.LENGTH_SHORT).show();
       }else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
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
           Log.e("mpath","call"+mCurrentPhotoPath);
           filenmae= getFileName(selectedImage);
           Log.e("fielnamu","call"+filenmae);
           txtFile.setText(filenmae);
          // loadFile();
       } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_CANCELED) {
           Toast.makeText(ScreeningRecordActivity.this, "Image Selection Cancelled", Toast.LENGTH_SHORT).show();
       } else if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK ){
           Uri uri = imageReturnedIntent.getData();
           Log.e("uri","iru"+uri);
           if (uri.toString().startsWith("content://com.google.android.apps.docs.storage")) {
               Log.e("filePicker",""+imageReturnedIntent);
               filenmae = getFileName(uri);
               txtFile.setText(filenmae);
               new DownloadAsyncTask(uri, this, this).execute();
           }else{
               filenmae= getFileName(uri);
               Log.e("fielnamu","call"+filenmae);
               txtFile.setText(filenmae);
               file = new File(uri.toString());
               mCurrentPhotoPath = file.getPath();
               try {
                   mCurrentPhotoPath=getFilePath(getApplicationContext(),uri);
                   Log.e("mpath","call"+getFilePath(getApplicationContext(),uri));
               } catch (URISyntaxException e) {
                   e.printStackTrace();
               }
           }
       }else if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_CANCELED) {
           Toast.makeText(ScreeningRecordActivity.this, "File Selection Cancelled", Toast.LENGTH_SHORT).show();
       }
      /* else if (resultCode == RESULT_OK) {
           Uri uri = imageReturnedIntent.getData();
           Log.e("fielnamu","uri"+uri);
           if (uri.toString().startsWith("content://com.google.android.apps.docs.storage")) {
               Log.e("filePicker",""+imageReturnedIntent);
               filenmae = getFileName(uri);
               txtFile.setText(filenmae);
               new DownloadAsyncTask(uri, this, this).execute();
           }
           filenmae= getFileName(uri);
           Log.e("fielnamu","call"+filenmae);
           txtFile.setText(filenmae);
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
        //  galleryIntent.setType("*/*");
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
                Uri photoURI = FileProvider.getUriForFile(ScreeningRecordActivity.this,
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
        Log.e("currentpath","call"+mCurrentPhotoPath);
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
    private void loadFile(){
        if (mCurrentPhotoPath == null) {
            Bitmap bitMap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_human_dummy3x);
            file = createImageFileFromBitmap(bitMap);
        } else {
            file = new File(mCurrentPhotoPath);
        }
        if(isConn()) {
            showingDialog.showAlert();
            ScreeningServerObjectDataController.getInstance().addScreeningCallForAttchmet(file,filenmae,ed_moreInfo.getText().toString());
        }else {
           // dialogeforCheckavilability("Error", "No Internet connection", "Ok");
        }
    }

/*
    public void cameraDailog() {
        TextView cam, gal, file, canc;
        AlertDialog.Builder dialog1 = new AlertDialog.Builder(ScreeningRecordActivity.this);
        inflater = ScreeningRecordActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.file_dailog, null);
        dialog1.setView(dialogView);
        cam = dialogView.findViewById(R.id.camera);
        gal = dialogView.findViewById(R.id.gallery);
        canc = dialogView.findViewById(R.id.cancel);
        file = dialogView.findViewById(R.id.file);
        canc.setTextColor(Color.parseColor("#ED5276"));

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ScreeningRecordActivity.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    dialog.dismiss();
                    LoadCaptureImageScreen();
                } else {
                    dialog.dismiss();
                    requestCameraPermission();
                }
            }
        });

        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ScreeningRecordActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    dialog.dismiss();
                    LoadImageFromGallery();
                } else {
                    requestStoragePermission();
                }
            }
        });
        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ScreeningRecordActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    dialog.dismiss();
                    showFileChooser();
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
*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LoadCaptureImageScreen();
                } else {
                    Toast.makeText(ScreeningRecordActivity.this, "Yay! You Denied Permission", Toast.LENGTH_SHORT).show();
                }
                break;
            case GALLERY_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LoadImageFromGallery();
                } else {
                    Toast.makeText(ScreeningRecordActivity.this, "Yay! You Denied Permission", Toast.LENGTH_SHORT).show();
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
                            ActivityCompat.requestPermissions(ScreeningRecordActivity.this,
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
            ActivityCompat.requestPermissions(ScreeningRecordActivity.this,
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
                            ActivityCompat.requestPermissions(ScreeningRecordActivity.this,
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
            ActivityCompat.requestPermissions(ScreeningRecordActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void bottomDialogSheet() {
        View dialogFamilyView = getLayoutInflater().inflate(R.layout.attachment_bottom_sheet, null);
        attachmentDialog = new BottomSheetDialog(Objects.requireNonNull(ScreeningRecordActivity.this), R.style.BottomSheetDialogTheme);
        attachmentDialog.setContentView(dialogFamilyView);
        attachmentDialog.setCanceledOnTouchOutside(true);
        attachmentDialog.setCancelable(true);
        Button add_btn=attachmentDialog.findViewById(R.id.btn_add_screening_record_details);
        attchmentImg = attachmentDialog.findViewById(R.id.img_attachment);
        imgView = attachmentDialog.findViewById(R.id.img_close);
        ed_moreInfo = attachmentDialog.findViewById(R.id.edt_screen_record_description);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               loadFile();
            }
        });

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachmentDialog.cancel();
            }
        });

        attchmentImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cameraDailog();
                cameraBottomSheet();

            }
        });

        txtFile = attachmentDialog.findViewById(R.id.edt_file_path);

        txtFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraBottomSheet();
            }
        });
        attachmentDialog.show();
    }
    private void showFileChooser() {
        Intent intent;
        if (android.os.Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
            Log.e("callSam","hello samsung");
            intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
            intent.putExtra("CONTENT_TYPE", "*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        } else {

          /*  String[] mimeTypes =
                    {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                            "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                            "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                            "text/plain",
                            "application/pdf",
                            "application/zip", "application/vnd.android.package-archive"};
*/
            intent = new Intent(Intent.ACTION_GET_CONTENT); // or ACTION_OPEN_DOCUMENT
            intent.setType("*/*");
           // intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        }
        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
       //intent.setType("*/*");
      //  intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"), FILE_PICKER_REQUEST_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
// Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void cameraBottomSheet(){
        TextView cam, gal, canc,file;
        View dialogView = getLayoutInflater().inflate(R.layout.file_dailog, null);
        final BottomSheetDialog cameraBottomSheetDialog =new BottomSheetDialog(Objects.requireNonNull(ScreeningRecordActivity.this), R.style.BottomSheetDialogTheme);
        cameraBottomSheetDialog.setContentView(dialogView);
        cam = cameraBottomSheetDialog.findViewById(R.id.camera);
        gal = cameraBottomSheetDialog.findViewById(R.id.gallery);
        file = cameraBottomSheetDialog.findViewById(R.id.file);
        canc = cameraBottomSheetDialog.findViewById(R.id.cancel);
        FrameLayout bottomSheet = (FrameLayout) cameraBottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        bottomSheet.setBackground(null);

        cameraBottomSheetDialog.show();
        canc.setTextColor(Color.parseColor("#53B9c6"));

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ScreeningRecordActivity.this,
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
        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ScreeningRecordActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    cameraBottomSheetDialog.cancel();
                    showFileChooser();
                } else {
                    cameraBottomSheetDialog.cancel();
                    requestStoragePermission();
                }

            }
        });
        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ScreeningRecordActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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

    @Override
    public void getResultFromAsynTask(String result) {
        mCurrentPhotoPath = result;
        Log.d("smaple", "getResultFromAsynTask: "+result);
        Log.d("smaple", "mCurrentPhotoPath: "+mCurrentPhotoPath);

    }
}
