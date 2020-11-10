package com.vedas.spectrocare.PatientModule;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.Controllers.PhysicalServerObjectDataController;
import com.vedas.spectrocare.DataBase.BmiDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBase.PhysicalCategoriesDataController;
import com.vedas.spectrocare.DataBase.PhysicalExamDataController;
import com.vedas.spectrocare.DataBase.PhysicalExamTrackInfoDataController;
import com.vedas.spectrocare.DataBaseModels.BMIModel;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.DataBaseModels.PatientModel;
import com.vedas.spectrocare.DataBaseModels.PhysicalCategoriesRecords;
import com.vedas.spectrocare.DataBaseModels.PhysicalExamsDataModel;
import com.vedas.spectrocare.DataBaseModels.PhysicalTrackInfoModel;
import com.vedas.spectrocare.PatientServerApiModel.PatientPhysicalModel;
import com.vedas.spectrocare.PatientServerApiModel.PatientPhysicalRecordModel;
import com.vedas.spectrocare.PatinetControllers.PatientFamilyDataController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.ServerApiModel.BodyIndexServerObject;
import com.vedas.spectrocare.ServerApiModel.PhysicalExamServerObject;
import com.vedas.spectrocare.ServerApiModel.PhysicalRecordServerObject;
import com.vedas.spectrocare.ServerApiModel.TrackingServerObject;
import com.vedas.spectrocare.SingleTapDetector;
import com.vedas.spectrocare.activities.LoginActivity;
import com.vedas.spectrocare.activities.MedicalPersonaSignupView;
import com.vedas.spectrocare.activities.MedicalPersonalSignupPresenter;
import com.vedas.spectrocare.activities.PhysicalExamActivity;
import com.vedas.spectrocare.activities.PhysicalExamRecordActivity;
import com.vedas.spectrocare.patientModuleAdapter.PatientPhysicalAdapter;

import org.greenrobot.eventbus.EventBus;
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

public class PatientPhysicalActivity extends AppCompatActivity implements MedicalPersonaSignupView {
    private  float dX,dY;
    int lastAction;
    FloatingActionButton btnPhysicalRecord;
    AlertDialog dialog;
    ImageView imgView;
    File file = null;
    CardView fileLayout;
    RelativeLayout back;
    RefreshShowingDialog alertDilogue;
    private String mCurrentPhotoPath;
    TextView txtAttachFile,txtManually;
    public ArrayList<PatientPhysicalModel> patientPhysicalExamList = new ArrayList<>();
    BottomSheetDialog attachmentDialog;
    ArrayList<Integer> colorsArray = new ArrayList<>();
    TextView txtPhysicalDisc;
    ImageView imgDeleteAll;
    GestureDetector gestureDetector;
    PatientPhysicalAdapter viewAdapter;
    RecyclerView patientPhysicaRecycleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_physical);

         gestureDetector = new GestureDetector(this, new SingleTapDetector());
         castingViews();
         clickEvents();
         if (isConn()){
             alertDilogue.showAlert();
             fetchPhysicalExamServerApi();
         }else {
             alertDilogue.hideRefreshDialog();
             dialogeforCheckavilability("Error", "No Internet connection", "Ok");
         }

    }
    public void castingViews(){
        alertDilogue = new RefreshShowingDialog(PatientPhysicalActivity.this);
        btnPhysicalRecord = findViewById(R.id.btn_physical_record);
        back = findViewById(R.id.img_back);
        imgDeleteAll = findViewById(R.id.img_delete_all);
        txtPhysicalDisc =findViewById(R.id.text_physical_disc);
        patientPhysicaRecycleView = findViewById(R.id.table_recycler_view);
        viewAdapter = new PatientPhysicalAdapter(PatientPhysicalActivity.this,colorsArray,imgDeleteAll);
       // loadRecyclerView();
    }
    public void clickEvents(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteAll();
            }
        });
        btnPhysicalRecord.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    bottomDialogSheet();
                }
                else{
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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void bottomDialogSheet() {
        View dialogFamilyView = getLayoutInflater().inflate(R.layout.file_bottom_sheet, null);
        attachmentDialog = new BottomSheetDialog(Objects.requireNonNull(PatientPhysicalActivity.this), R.style.BottomSheetDialogTheme);
        attachmentDialog.setContentView(dialogFamilyView);
        attachmentDialog.setCanceledOnTouchOutside(true);
        attachmentDialog.setCancelable(true);
        imgView = attachmentDialog.findViewById(R.id.img_close);
        txtAttachFile = attachmentDialog.findViewById(R.id.text_attach_file);
        txtManually = attachmentDialog.findViewById(R.id.text_enter_manually);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachmentDialog.cancel();
            }
        });

        txtAttachFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // cameraDailog();
                cameraBottomSheet();
                attachmentDialog.cancel();

            }
        });
        txtManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  fileLayout.setVisibility(View.GONE);
                Intent physicalRecordIntent = new Intent(PatientPhysicalActivity.this, PatientPhysicalRecordActivity.class);
                attachmentDialog.cancel();
                startActivity(physicalRecordIntent);

            }
        });

        attachmentDialog.show();

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void cameraBottomSheet(){
        TextView cam, gal, canc,file;
        View dialogView = getLayoutInflater().inflate(R.layout.file_dailog, null);
        final BottomSheetDialog cameraBottomSheetDialog =new BottomSheetDialog(Objects.requireNonNull(PatientPhysicalActivity.this), R.style.BottomSheetDialogTheme);
        cameraBottomSheetDialog.setContentView(dialogView);
        cam = cameraBottomSheetDialog.findViewById(R.id.camera);
        gal = cameraBottomSheetDialog.findViewById(R.id.gallery);
        file = cameraBottomSheetDialog.findViewById(R.id.file);
        canc = cameraBottomSheetDialog.findViewById(R.id.cancel);
        FrameLayout bottomSheet = (FrameLayout) cameraBottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        bottomSheet.setBackground(null);

        cameraBottomSheetDialog.show();

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(PatientPhysicalActivity.this,
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
                if (ActivityCompat.checkSelfPermission(PatientPhysicalActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
                if (ActivityCompat.checkSelfPermission(PatientPhysicalActivity.this,
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PatientPhysicalActivity.this,PatientHomeActivity.class));
        finish();
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
            Toast.makeText(PatientPhysicalActivity.this, "Image Capturing Cancelled", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(PatientPhysicalActivity.this, "Image Selection Cancelled", Toast.LENGTH_SHORT).show();
        }  else  if (resultCode == RESULT_OK) {
            Uri uri = imageReturnedIntent.getData();
            File myFile = new File(uri.toString());
            mCurrentPhotoPath = myFile.getPath();
            Log.e("noooo","call"+mCurrentPhotoPath);
            try {
                mCurrentPhotoPath=getFilePath(getApplicationContext(),uri);
                Log.e("mpath","call"+getFilePath(getApplicationContext(),uri));
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
    /* private void loadFile(){
         if (mCurrentPhotoPath == null) {
             Bitmap bitMap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_human_dummy3x);
             file = createImageFileFromBitmap(bitMap);
         } else {
             file = new File(mCurrentPhotoPath);
         }
         if(isConn()) {
             alertDilogue.showAlert();
             PhysicalServerObjectDataController.getInstance().apiCallForAttchmet(file);
         }else {
             dialogeforCheckavilability("Error", "No Internet connection", "Ok");
         }
     }*/
    private void loadFile(){
        if (mCurrentPhotoPath == null) {
            Bitmap bitMap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_human_dummy3x);
            file = createImageFileFromBitmap(bitMap);
        } else {
            file = new File(mCurrentPhotoPath);
        }
        if(isConn()) {
            alertDilogue.showAlert();
            apiCallForAttchmet(file);
          //  PhysicalServerObjectDataController.getInstance().apiCallForAttchmet(file);
        }else {
            alertDilogue.hideRefreshDialog();
            dialogeforCheckavilability("Error", "No Internet connection", "Ok");
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
                Uri photoURI = FileProvider.getUriForFile(PatientPhysicalActivity.this,
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
                    Toast.makeText(PatientPhysicalActivity.this, "Yay! You Denied Permission", Toast.LENGTH_SHORT).show();
                }
                break;
            case GALLERY_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LoadImageFromGallery();
                } else {
                    Toast.makeText(PatientPhysicalActivity.this, "Yay! You Denied Permission", Toast.LENGTH_SHORT).show();
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
                            ActivityCompat.requestPermissions(PatientPhysicalActivity.this,
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
            ActivityCompat.requestPermissions(PatientPhysicalActivity.this,
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
                            ActivityCompat.requestPermissions(PatientPhysicalActivity.this,
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
            ActivityCompat.requestPermissions(PatientPhysicalActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
        }
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
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PatientPhysicalActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);
    }

    public void apiCallForAttchmet(final File file) {
        PatientModel patientModel = PatientLoginDataController.getInstance().currentPatientlProfile;
      //  MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        PhysicalExamServerObject serverObject=new PhysicalExamServerObject();
        serverObject.setHospital_reg_num(patientModel.getHospital_reg_number());
       // serverObject.setMedical_personnel_id(patientModel.getMedical_person_id());
        serverObject.setMedical_record_id(patientModel.getMedicalRecordId());
        serverObject.setPatientID(patientModel.getPatientId());

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ServerApi s = retrofit.create(ServerApi.class);

        MultipartBody.Part physicalExamRecord;

        RequestBody image = RequestBody.create(MediaType.parse("image/*"), file);
        physicalExamRecord = MultipartBody.Part.createFormData("physicalExamRecord", file.getName(), image);
        RequestBody byWhom = RequestBody.create(MediaType.parse("text/plain"),"patient");
        RequestBody byWhomID = RequestBody.create(MediaType.parse("text/plain"),serverObject.getPatientID());
        // RequestBody medicalPersonId = RequestBody.create(MediaType.parse("text/plain"),serverObject.getMedical_personnel_id());
        RequestBody medicalRecordId = RequestBody.create(MediaType.parse("text/plain"),serverObject.getMedical_record_id());
        RequestBody hospitalRegNumber = RequestBody.create(MediaType.parse("text/plain"),serverObject.getHospital_reg_num());
        RequestBody patientId = RequestBody.create(MediaType.parse("text/plain"),serverObject.getPatientID());
        Log.e("medicalPers",""+serverObject.getMedical_personnel_id()+","+serverObject.getMedical_record_id()
                +","+serverObject.getHospital_reg_num()+","+serverObject.getPatientID()+","+patientModel.getAccessToken());

        Call<PhysicalExamServerObject> call = s.addAttachment(patientModel.getAccessToken(),byWhom,hospitalRegNumber,patientId,
                medicalRecordId,physicalExamRecord,byWhomID);
        call.enqueue(new Callback<PhysicalExamServerObject>() {
            @Override
            public void onResponse(Call<PhysicalExamServerObject> call, Response<PhysicalExamServerObject> response) {
               alertDilogue.hideRefreshDialog();
                Gson gson = new Gson();
                String json = gson.toJson(response.body());
                Log.e("dadd","d"+json);
                Log.e("addd","file"+response.body());
                String message = response.body().getMessage();
                String respons = response.body().getResponse();
                Log.e("checkMessage", "" + message);
                if (respons.equals("3")){
                    fetchPhysicalExamServerApi();
                    Toast.makeText(PatientPhysicalActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PhysicalExamServerObject> call, Throwable t) {
                alertDilogue.hideRefreshDialog();
                Log.e("error","message is :"+t.getMessage());

            }
        });
    }
    public void deleteAllRecords(){
        RefreshShowingDialog alertDilogue =new RefreshShowingDialog(PatientPhysicalActivity.this);
        alertDilogue.showAlert();
        PhysicalExamServerObject examServerObject = new PhysicalExamServerObject();
        examServerObject.setPatientID(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
        examServerObject.setMedical_record_id(PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
        examServerObject.setHospital_reg_num(PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
        examServerObject.setByWhomID(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
        examServerObject.setByWhom("patient");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerApi serverApi = retrofit.create(ServerApi.class);
        Call<PhysicalExamServerObject> callDelete=serverApi.deleteAllPhysicalExamHistory(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(),examServerObject);
        callDelete.enqueue(new Callback<PhysicalExamServerObject>() {
            @Override
            public void onResponse(Call<PhysicalExamServerObject> call, Response<PhysicalExamServerObject> response) {
                Log.e("deleeee","te"+response.body());
                alertDilogue.hideRefreshDialog();
                if (response.body().getResponse().equals("3")){
                    fetchPhysicalExamServerApi();
                    Toast.makeText(PatientPhysicalActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PhysicalExamServerObject> call, Throwable t) {
                alertDilogue.hideRefreshDialog();
                Log.e("eree","fa"+t.getMessage());
            }
        });
    }

    public void loadRecyclerView(){
        // alertDilogue.hideRefreshDialog();

       // PatientProfileDataController.getInstance().fetchPatientlProfileData();
        if (PatientFamilyDataController.getInstance().getPhysicalRecordModel()!=null) {

            patientPhysicalExamList = PatientFamilyDataController.getInstance().getPhysicalRecordModel().getPhysical_exam_records();
       /* patientPhysicalExamList= PhysicalExamDataController.getInstance()
                .fetchPhysicalExamData(PatientProfileDataController.getInstance().currentPatientlProfile);*/
            Log.e("dfyuu","fgu"+patientPhysicalExamList.size());
            if (patientPhysicalExamList.size() > 0) {
                for (int i = 0; i < patientPhysicalExamList.size(); i++) {
                    colorsArray.add(PersonalInfoController.getInstance().getRandomColor());
                }
            }else{
                colorsArray.add(PersonalInfoController.getInstance().getRandomColor());
            }
                txtPhysicalDisc.setVisibility(View.GONE);

        }else
            txtPhysicalDisc.setVisibility(View.VISIBLE);

        // Log.e("onResume", "call"+patientPhysicalExamList.size());



        patientPhysicaRecycleView.setLayoutManager(new LinearLayoutManager(PatientPhysicalActivity.this));
        patientPhysicaRecycleView.setHasFixedSize(true);
        Log.e("tsfjgj","gygku"+colorsArray.size());
       // viewAdapter = new PhysicalExamActivity.TableRecyclerViewAdapter(PatientPhysicalActivity.this,colorsArray);
        patientPhysicaRecycleView.setAdapter(viewAdapter);
    }

    public void fetchPhysicalExamServerApi(){
        final PhysicalExamServerObject serverObject=new PhysicalExamServerObject();
        serverObject.setHospital_reg_num(PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
        // serverObject.setMedical_personnel_id(currentMedical.getMedical_person_id());
        serverObject.setMedical_record_id(PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
        serverObject.setPatientID(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
        serverObject.setByWhom("patient");
        serverObject.setByWhomID(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerApi serverApi = retrofit.create(ServerApi.class);

        Call<PatientPhysicalRecordModel> callFetch = serverApi.patientPhysicalFetchRecord(
                PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(),serverObject);

        callFetch.enqueue(new Callback<PatientPhysicalRecordModel>() {
            @Override
            public void onResponse(Call<PatientPhysicalRecordModel> call, Response<PatientPhysicalRecordModel> response) {
                alertDilogue.hideRefreshDialog();
                if (response.body().getResponse()==0){
                    PatientFamilyDataController.getInstance().setPhysicalRecordModel(null);
                    loadRecyclerView();
                }else if (response.body().getResponse()==3){
                    PatientFamilyDataController.getInstance().setPhysicalRecordModel(response.body());
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    Log.e("mmmmmm","response"+json);
                    if (PatientFamilyDataController.getInstance().getPhysicalRecordModel()!=null){
                        imgDeleteAll.setVisibility(View.VISIBLE);
                        Log.e("ddddddfsdfa","dsafsdaf");
                       // PatientFamilyDataController.getInstance().getPhysicalRecordModel().getPhysical_exam_records().clear();
                        Log.e("sizuu","is 1 :  "+PatientFamilyDataController.getInstance().getPhysicalRecordModel().getPhysical_exam_records().size());
                        loadRecyclerView();
                    }else{
                        imgDeleteAll.setVisibility(View.GONE);
                    }

                }

                Log.e("ressss",""+response);

              //  Log.e("sizuu","is :  "+PatientFamilyDataController.getInstance().getPhysicalRecordModel().getPhysical_exam_records().size());

               //   viewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PatientPhysicalRecordModel> call, Throwable t) {
                alertDilogue.hideRefreshDialog();

            }
        });

     }
    public void showDeleteAll() {
        Log.e("logggg","out");
        final Dialog dialog = new Dialog(PatientPhysicalActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_abort);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
        dialog.show();
        btnNo.setText("Cancel");
        btnYes.setText("Delete");

        TextView txt_title = dialog.findViewById(R.id.title);
        TextView txt_msg = dialog.findViewById(R.id.msg);
        TextView txt_msg1 = dialog.findViewById(R.id.msg1);

        txt_title.setText("Delete");
        txt_msg.setText("Are you sure you");
        txt_msg1.setText("want to delete all records ?");

        RelativeLayout main = (RelativeLayout) dialog.findViewById(R.id.rl_main);
        RelativeLayout main1 = (RelativeLayout) dialog.findViewById(R.id.rl_main1);

        GradientDrawable drawable = (GradientDrawable) main.getBackground();
        drawable.setColor(getResources().getColor(R.color.colorWhite));

        GradientDrawable drawable1 = (GradientDrawable) main1.getBackground();
        drawable1.setColor(getResources().getColor(R.color.colorWhite));

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (isConn()){
                  dialog.dismiss();
                  deleteAllRecords();
              }else{
                  dialogeforCheckavilability("Error", "No Internet connection", "Ok");

              }
            }
        });
        dialog.show();

    }

}
