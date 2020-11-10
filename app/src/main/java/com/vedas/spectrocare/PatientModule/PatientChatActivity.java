package com.vedas.spectrocare.PatientModule;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.vedas.spectrocare.CallBackTask;
import com.vedas.spectrocare.DownloadAsyncTask;
import com.vedas.spectrocare.PatientChat.ChatArrayModel;
import com.vedas.spectrocare.PatientChat.ChatDataController;
import com.vedas.spectrocare.PatientChat.ChatModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.activities.ScreeningRecordActivity;
import com.vedas.spectrocare.patientModuleAdapter.InboxChatAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static com.vedas.spectrocare.adapter.MedicalHistoryAdapter.dialog;

public class PatientChatActivity extends AppCompatActivity {
    RecyclerView chatRecyclerView;
    InboxChatAdapter chatAdapter;
    private String mCurrentPhotoPath;
    ImageView imgBack,imgSend,imgAttach,imgSelected;
    public static final int CAMERA_REQUEST_CODE = 1001;
    public static final int GALLERY_REQUEST_CODE = 1002;
    public static final int CAMERA_PERMISSION_CODE = 5001;
    public static final int GALLERY_PERMISSION_CODE = 5002;
    private static final int FILE_PICKER_REQUEST_CODE = 8001;
    String filenmae,profileBase64Obj;
    File file;
    Intent picIntent ;
    CircularImageView imgPic;
    TextView txtUri,txtDocName;
    EditText edtSendingTxt;
    ImageView imgPlay;
    private Bitmap bitmap;
    String urlStr="";
    AlertDialog dialog;
    VideoView video;
    ChatModel chatModel;
    ArrayList<ChatModel> chatList;
    RelativeLayout relaVideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_chat);
       castingAndClicks();
    }
    public void castingAndClicks(){
        imgBack = findViewById(R.id.img_back_arrow);
        imgAttach = findViewById(R.id.img_attach);
        imgSelected = findViewById(R.id.img_selected);
        video=findViewById(R.id.vv1);
        imgPic = findViewById(R.id.circular_image);
        chatList = new ArrayList<>();
        txtDocName = findViewById(R.id.txt_doc_name);
        relaVideo = findViewById(R.id.rl_video);
        imgSend = findViewById(R.id.img_send);
        edtSendingTxt = findViewById(R.id.edt_sending_txt);
        txtUri = findViewById(R.id.txt_url);
        chatRecyclerView = findViewById(R.id.chat_recycleView);
        chatAdapter = new InboxChatAdapter(this);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);
        Intent docIntent = getIntent();
        if (!docIntent.getStringExtra("docPic").isEmpty())
            Picasso.get().load(docIntent.getStringExtra("docPic")).placeholder(R.drawable.image).into(imgPic);
        if (docIntent.hasExtra("docName")){
            txtDocName.setText(docIntent.getStringExtra("docName"));
        }

        if (ChatDataController.isNull()){
            chatList.add(new ChatModel("NonLocal","Hello","","",""));
            chatList.add(new ChatModel("Local","Hai there","","",""));
            chatList.add(new ChatModel("NonLocal","send me anything from your device","","",""));
            ChatDataController.getInstance().setChatModelArrayList(chatList);
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgPlay = findViewById(R.id.img_play);
        edtSendingTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                imgSelected.setVisibility(View.GONE);
                video.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSendingTxt.setVisibility(View.VISIBLE);
                imgSelected.setVisibility(View.GONE);
                imgAttach.setVisibility(View.VISIBLE);
                relaVideo.setVisibility(View.GONE);
               // Log.e("idd","userID"+ChatDataController.getInstance().getChatModelArrayList().get(ChatDataController.getInstance().getChatModelArrayList().size()-1).getiD());

                    if (!edtSendingTxt.getText().toString().isEmpty()){
                        if (ChatDataController.isNull()){
                            chatList.add(new ChatModel("Local",edtSendingTxt.getText().toString(),
                                    "","",""));
                            edtSendingTxt.setText("");
                            Log.e("fdafsf","dff");
                            ChatDataController.getInstance().setChatModelArrayList(chatList);
                            chatAdapter.notifyDataSetChanged();
                        }else{
                            Log.e("fdafsf","dfff");

                            ChatDataController.getInstance().getChatModelArrayList().add(new ChatModel("Local",edtSendingTxt.getText().toString(),
                                    "","",""));
                            edtSendingTxt.setText("");

                            // Log.e("idd","userI"+ChatDataController.getInstance().getChatModelArrayList().get(0).getiD());

                        }
                    }else
                    if (!urlStr.isEmpty()){
                        if (urlStr.contains("image")){
                            if (ChatDataController.isNull()){
                                chatList.add(new ChatModel("Local","",
                                        urlStr,"",""));
                                urlStr="";
                                ChatDataController.getInstance().setChatModelArrayList(chatList);
                            }else{
                                ChatDataController.getInstance().getChatModelArrayList().add(new ChatModel("Local","",
                                        urlStr,"",""));
                                urlStr="";
                            }
                        }else if (urlStr.contains("video")){
                            if (ChatDataController.isNull()){
                                chatList.add(new ChatModel("Local","",
                                        "",urlStr,""));
                                ChatDataController.getInstance().setChatModelArrayList(chatList);
                                urlStr="";
                            }else{
                                ChatDataController.getInstance().getChatModelArrayList().add(new ChatModel("Local","",
                                        "",urlStr,""));
                                urlStr="";
                            }
                        }
                        else if (urlStr.contains("jpg")){
                            if (ChatDataController.isNull()){
                                chatList.add(new ChatModel("Local","", urlStr,"",""));
                                ChatDataController.getInstance().setChatModelArrayList(chatList);
                                urlStr="";
                            }else{
                                ChatDataController.getInstance().getChatModelArrayList().add(new ChatModel("Local","", urlStr,"",""));
                                urlStr="";
                            }
                        }else{
                            if (ChatDataController.isNull()){
                                chatList.add(new ChatModel("Local","",
                                        "","",mCurrentPhotoPath));
                                urlStr="";
                                ChatDataController.getInstance().setChatModelArrayList(chatList);
                            }else{
                                ChatDataController.getInstance().getChatModelArrayList().add(new ChatModel("Local","",
                                        "","",mCurrentPhotoPath));
                                urlStr="";
                            }
                        }

                    }
                chatRecyclerView.smoothScrollToPosition(ChatDataController.getInstance().getChatModelArrayList().size());

                Gson gson = new Gson();
                String json = gson.toJson(ChatDataController.getInstance().getChatModelArrayList());
                Log.e("yaandi","d"+ChatDataController.getInstance().getChatModelArrayList().size());
                chatAdapter.notifyDataSetChanged();

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtSendingTxt.getWindowToken(), 0);

            }
        });
        imgAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraBottomSheet();
            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void cameraBottomSheet(){
        edtSendingTxt.setText("");
        TextView cam, gal, canc,file;
        View dialogView = getLayoutInflater().inflate(R.layout.file_dailog, null);
        final BottomSheetDialog cameraBottomSheetDialog =new BottomSheetDialog(Objects.requireNonNull(PatientChatActivity.this), R.style.BottomSheetDialogTheme);
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
                if (ActivityCompat.checkSelfPermission(PatientChatActivity.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
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
                if (ActivityCompat.checkSelfPermission(PatientChatActivity.this,
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
                if (ActivityCompat.checkSelfPermission(PatientChatActivity.this,
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


    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Permission Info")
                    .setMessage("Camera Permission is Needed for Adding your Profile Image")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(PatientChatActivity.this,
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
            ActivityCompat.requestPermissions(PatientChatActivity.this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    public void loadEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        profileBase64Obj = Base64.encodeToString(imageInByte, Base64.NO_WRAP);
        Log.e("base64Image", "call" + profileBase64Obj);

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
            urlStr = mCurrentPhotoPath;
            Log.e("uuuuuf","afad"+mCurrentPhotoPath);
            imgSelected.setVisibility(View.VISIBLE);
            imgAttach.setVisibility(View.GONE);
            edtSendingTxt.setVisibility(View.GONE);
            imgSelected.setImageBitmap(getResizedBitmap(rotatedBitmap, 500));
            loadEncoded64ImageStringFromBitmap(bitmap);

        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            Toast.makeText(PatientChatActivity.this, "Image Capturing Cancelled", Toast.LENGTH_SHORT).show();
        }
       else if ( requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK){

            Uri uri = imageReturnedIntent.getData();
            Log.e("checkURI","uri "+uri);
            urlStr = String.valueOf(uri);
            if (urlStr.contains("image")){
                Log.e("urii","image "+uri);
                imgSelected.setVisibility(View.VISIBLE);
                imgAttach.setVisibility(View.GONE);
                edtSendingTxt.setVisibility(View.GONE);
                imgSelected.setImageURI(uri);
            }else if (urlStr.contains("video")){
                relaVideo.setVisibility(View.VISIBLE);
                imgSelected.setVisibility(View.GONE);
                imgAttach.setVisibility(View.GONE);
                edtSendingTxt.setVisibility(View.GONE);
                Log.e("urii","video "+uri);
                video.setVisibility(View.VISIBLE);
                video.setVideoURI(uri);
                imgPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imgPlay.setVisibility(View.GONE);
                        video.start();
                    }
                });
                // video.start();
            }

        }else if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK ){
            Uri fileUri = imageReturnedIntent.getData();
            urlStr = String.valueOf(fileUri);
            Log.e("fileName","path"+urlStr);

            imgSelected.setImageResource(R.drawable.pdf);
            imgSelected.setVisibility(View.VISIBLE);
            edtSendingTxt.setVisibility(View.GONE);
            imgAttach.setVisibility(View.GONE);


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



            Log.e("uri","iru"+fileUri);
        }else if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            Toast.makeText(PatientChatActivity.this, "File Selection Cancelled", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Attachment selection cancelled", Toast.LENGTH_SHORT).show();
        }

    }

    private void showFileChooser() {
        Intent intent;
        if (android.os.Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
            Log.e("callSam","hello samsung");
            intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
            intent.putExtra("CONTENT_TYPE", "*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        } else {

            intent = new Intent(Intent.ACTION_GET_CONTENT); // or ACTION_OPEN_DOCUMENT
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        }

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"), FILE_PICKER_REQUEST_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
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



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LoadCaptureImageScreen();
                } else {
                    Toast.makeText(PatientChatActivity.this, "Yay! You Denied Permission", Toast.LENGTH_SHORT).show();
                }
                break;
            case GALLERY_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LoadImageFromGallery();
                } else {
                    Toast.makeText(PatientChatActivity.this, "Yay! You Denied Permission", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
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
                Uri photoURI = FileProvider.getUriForFile(PatientChatActivity.this,
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
      //  galleryIntent.setType("*/*"); in /* LoadImageFromGallery() */

/*
    private void LoadImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }
*/
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
                            ActivityCompat.requestPermissions(PatientChatActivity.this,
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
            ActivityCompat.requestPermissions(PatientChatActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
        }
    }

}
