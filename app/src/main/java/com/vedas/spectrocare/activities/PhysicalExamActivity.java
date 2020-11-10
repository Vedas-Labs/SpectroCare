package com.vedas.spectrocare.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
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
import android.view.ContextThemeWrapper;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.Controllers.PhysicalServerObjectDataController;
import com.vedas.spectrocare.DataBase.FamilyHistoryDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBase.PhysicalCategoriesDataController;
import com.vedas.spectrocare.DataBase.PhysicalExamDataController;
import com.vedas.spectrocare.DataBase.PhysicalExamTrackInfoDataController;
import com.vedas.spectrocare.DataBase.VaccineDataController;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.DataBaseModels.PhysicalExamsDataModel;
import com.vedas.spectrocare.DataBaseModels.PhysicalTrackInfoModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.SingleTapDetector;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;


public class PhysicalExamActivity extends AppCompatActivity implements MedicalPersonaSignupView {
    TableRecyclerViewAdapter viewAdapter;
    RelativeLayout imgButton;
    FloatingActionButton btnPhysicalExamRecord;
    BottomSheetDialog attachmentDialog;
    RefreshShowingDialog alertDilogue;
    public ArrayList<PhysicalExamsDataModel> allPhysicalExamList = new ArrayList<>();
    AlertDialog dialog;
    CardView fileLayout;
    TextView txtAttachFile,txtManually;
    private String mCurrentPhotoPath;
    File file = null;
    private  float dX,dY;
    int lastAction;
    RecyclerView creatorDetailView;
    ImageView imgView;
    TextView txtPhysicalDisc;
    ArrayList<Integer> colorsArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical_exam);

        btnPhysicalExamRecord = findViewById(R.id.btn_physical_record);
        imgButton = findViewById(R.id.img_back);
        final GestureDetector gestureDetector = new GestureDetector(this, new SingleTapDetector());
        creatorDetailView = findViewById(R.id.table_recycler_view);
        fileLayout = findViewById(R.id.file_layout);
        txtAttachFile = findViewById(R.id.text_attach_file);
        txtManually = findViewById(R.id.text_enter_manually);
        imgButton = findViewById(R.id.img_back);
        txtPhysicalDisc =findViewById(R.id.text_physical_disc);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        alertDilogue = new RefreshShowingDialog(PhysicalExamActivity.this);
        PatientProfileDataController.getInstance().fetchPatientlProfileData();

        Log.e("jajaj",""+ PhysicalExamDataController.getInstance()
                .fetchPhysicalExamData(PatientProfileDataController.getInstance().currentPatientlProfile));

        loadRecyclerView();

        txtAttachFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fileLayout.setVisibility(View.GONE);
                cameraDailog();
            }
        });

        txtManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fileLayout.setVisibility(View.GONE);
                PhysicalExamDataController.getInstance().currentPhysicalExamsData=null;
                Intent physicalRecordIntent = new Intent(PhysicalExamActivity.this, PhysicalExamRecordActivity.class);
                startActivity(physicalRecordIntent);

            }
        });
        btnPhysicalExamRecord.setOnTouchListener(new View.OnTouchListener() {
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

/*
        btnPhysicalExamRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileLayout.setVisibility(View.VISIBLE);
            }
        });
*/

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResume() {
        super.onResume();
        Log.e("onResume", "call");
        EventBus.getDefault().register(this);
        if(PhysicalServerObjectDataController.getInstance().isFromStaring) {
            alertDilogue.showAlert();
            PhysicalServerObjectDataController.getInstance().fetchPhysicalExamServerApi();
        }
       /* if (!allPhysicalExamList.isEmpty()){fetchPhysicalData

        }*/
        loadRecyclerView();
        /*allPhysicalExamList= PhysicalExamDataController.getInstance().fetchPhysicalExamData(PatientProfileDataController.getInstance().currentPatientlProfile);
        if (allPhysicalExamList.size() > 0) {
            for (int i = 0; i < allPhysicalExamList.size(); i++) {
                colorsArray.add(PersonalInfoController.getInstance().getRandomColor());
            }
        }*/
        viewAdapter.notifyDataSetChanged();

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
        Log.e("physicalevent", "" + event.message);
        String resultData = event.message.trim();
        alertDilogue.hideRefreshDialog();
        allPhysicalExamList= PhysicalExamDataController.getInstance().fetchPhysicalExamData(PatientProfileDataController.getInstance().currentPatientlProfile);
        if (resultData.equals("fetchPhysicalData")) {
            PhysicalServerObjectDataController.getInstance().isFromStaring=false;
            loadRecyclerView();
            viewAdapter.notifyDataSetChanged();
        }else if (resultData.equals("noPhysicalData")) {
            alertDilogue.hideRefreshDialog();
        }else if (resultData.equals("deletePhysicalData")) {
            alertDilogue.hideRefreshDialog();
            if (allPhysicalExamList.isEmpty()){
                txtPhysicalDisc.setVisibility(View.VISIBLE);
            }
            viewAdapter.notifyDataSetChanged();
        }else if (resultData.equals("addPhysicalData")) { ///for attach fiel adding
            alertDilogue.hideRefreshDialog();
            loadRecyclerView();
            viewAdapter.notifyDataSetChanged();
        }
    }

    public void loadRecyclerView(){
       // alertDilogue.hideRefreshDialog();
        PatientProfileDataController.getInstance().fetchPatientlProfileData();
        allPhysicalExamList= PhysicalExamDataController.getInstance()
                .fetchPhysicalExamData(PatientProfileDataController.getInstance().currentPatientlProfile);
        Log.e("onResume", "call"+allPhysicalExamList.size());

        if (allPhysicalExamList.size() > 0) {
            for (int i = 0; i < allPhysicalExamList.size(); i++) {
                Log.e("dfyuu","fgu");
                colorsArray.add(PersonalInfoController.getInstance().getRandomColor());
            }
        }else{
            colorsArray.add(PersonalInfoController.getInstance().getRandomColor());
        }
        if (allPhysicalExamList.isEmpty())
            txtPhysicalDisc.setVisibility(View.VISIBLE);
        else
            txtPhysicalDisc.setVisibility(View.GONE);

        creatorDetailView.setLayoutManager(new LinearLayoutManager(PhysicalExamActivity.this));
        creatorDetailView.setHasFixedSize(true);
        Log.e("tsfjgj","gygku"+colorsArray.size());
         viewAdapter = new TableRecyclerViewAdapter(PhysicalExamActivity.this,colorsArray);
        creatorDetailView.setAdapter(viewAdapter);
    }

    public class TableRecyclerViewAdapter extends RecyclerView.Adapter<TableRecyclerViewAdapter.TableRecyclerViewHolder> {
        Context context;
        ArrayList<Integer> colorsArray;
        TableRecyclerViewAdapter(Context context, ArrayList<Integer> colorsArray1) {
            this.context = context;
            this.colorsArray=colorsArray1;
            Log.e("tfjgsj","gygku"+colorsArray1.size());
        }
        @NonNull
        @Override
        public TableRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_physical_exam,parent,false);
            Log.e("tfjgj","gygku"+allPhysicalExamList.size());
            return new TableRecyclerViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull final TableRecyclerViewHolder holder, final int position) {
            final PhysicalExamsDataModel physicalExamsDataModel=allPhysicalExamList.get(position);
            if(physicalExamsDataModel.getAttachment() != null){
                Log.e("attachment","call"+physicalExamsDataModel.getAttachment());
                holder.editImg.setVisibility(View.GONE);
                holder.attachImg.setVisibility(View.VISIBLE);
            }else {
                holder.editImg.setVisibility(View.VISIBLE);
                holder.attachImg.setVisibility(View.GONE);
            }

            holder.colorLayout.setBackgroundColor(colorsArray.get(position));
            final MedicalProfileModel objModel= MedicalProfileDataController.getInstance().currentMedicalProfile;
            ArrayList<PhysicalTrackInfoModel> trackInfoModels= PhysicalExamTrackInfoDataController.getInstance().fetchPhysicalExamBasedOnPhysicalExamId(physicalExamsDataModel);
            if(trackInfoModels.size()>0){
                try {
                    PhysicalTrackInfoModel trackInfoModel=trackInfoModels.get(trackInfoModels.size()-1);
                    String date[]= PersonalInfoController.getInstance().convertTimestampToslashFormate(trackInfoModel.getDate());
                    holder.txt_first_name.setText(objModel.getFirstName()+" "+objModel.getLastName());
                    Log.e("trackingdate","call"+date[0]+date[1]+date[2]);
                    holder.txt_on.setText(" on");
                    holder.txt_date.setText(date[0]);
                    holder.txt_time.setText(date[1]+" "+date[2]);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            holder.viewImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhysicalExamDataController.getInstance().currentPhysicalExamsData=allPhysicalExamList.get(position);
                    if(PhysicalExamDataController.getInstance().currentPhysicalExamsData.getAttachment()!=null){
                        PhysicalExamDataController.getInstance().currentPhysicalExamsData=allPhysicalExamList.get(position);
                        loadAttachUrlImage(allPhysicalExamList.get(position));
                    }else {
                        Intent patientProfileIntent = new Intent(context, PhysicalExamRecordActivity.class);
                        startActivity(patientProfileIntent);
                    }
                }
            });
            holder.editImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhysicalExamDataController.getInstance().currentPhysicalExamsData=allPhysicalExamList.get(position);
                    Intent patientProfileIntent = new Intent(context, PhysicalExamRecordActivity.class);
                    startActivity(patientProfileIntent);
                }
            });
            holder.deleteImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhysicalExamDataController.getInstance().currentPhysicalExamsData=allPhysicalExamList.get(position);
                    deletePhysicalExam();
                }
            });
            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context wrapper = new ContextThemeWrapper(PhysicalExamActivity.this, R.style.NoPopupAnimation);
                    PopupMenu popup = new PopupMenu(wrapper, holder.more);


                    // PopupMenu popup = new PopupMenu(wrapper, holder.more);
                    popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                    // Context wrapper = new ContextThemeWrapper(context, R.style.MyPopupOtherStyle);

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getTitle().equals("View")) {
                                PhysicalExamDataController.getInstance().currentPhysicalExamsData = allPhysicalExamList.get(position);
                                if (PhysicalExamDataController.getInstance().currentPhysicalExamsData.getAttachment() != null) {
                                    PhysicalExamDataController.getInstance().currentPhysicalExamsData = allPhysicalExamList.get(position);
                                    loadAttachUrlImage(allPhysicalExamList.get(position));
                                } else {
                                    Intent patientProfileIntent = new Intent(context, PhysicalExamRecordActivity.class);
                                    startActivity(patientProfileIntent);
                                }
                            }else {
                                PhysicalExamDataController.getInstance().currentPhysicalExamsData=allPhysicalExamList.get(position);
                                deletePhysicalExam();
                            }
                            return true;
                        }

                    });
                    popup.show();
                   /* Context wrapper = new ContextThemeWrapper(PhysicalExamActivity.this, R.style.MyPopupOtherStyle);
                    PopupMenu popup = new PopupMenu(wrapper, holder.more);
                    popup.getMenuInflater().inflate(R.menu.popup_layout, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getTitle().equals("View")) {
                                PhysicalCategoriesDataController.getInstance().currentPhysicalExamModel = PhysicalCategoriesDataController.getInstance().allPhysicalExamList.get(position);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                    // bottomDialogSheet();
                                }
                            }*/

                }
            });
        }


        @Override
        public int getItemCount() {
            if(allPhysicalExamList.size()>0){
                return allPhysicalExamList.size();
            }else {
                return 0;
            }
        }
        public class TableRecyclerViewHolder extends RecyclerView.ViewHolder {
            TextView txt_first_name,txt_on,txt_date,txt_time;
            ImageView viewImg,editImg,deleteImg,attachImg;
            RelativeLayout colorLayout,more;
            public TableRecyclerViewHolder(@NonNull View itemView) {
                super(itemView);
                txt_first_name = itemView.findViewById(R.id.txt_first_name);
                txt_on = itemView.findViewById(R.id.txt_on);
                colorLayout = itemView.findViewById(R.id.color);
                txt_date = itemView.findViewById(R.id.txt_date);
                txt_time = itemView.findViewById(R.id.txt_time);
                viewImg = itemView.findViewById(R.id.img_view);
                deleteImg = itemView.findViewById(R.id.img_delete);
                editImg = itemView.findViewById(R.id.img_edit);
                attachImg = itemView.findViewById(R.id.img_attach);
                more = itemView.findViewById(R.id.more_layout);

            }
        }
    }
    private void  loadAttachUrlImage(PhysicalExamsDataModel dataModel){
        String url= ServerApi.img_home_url+dataModel.getAttachment();
        Log.e("notdata","call"+url);
        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.setData(Uri.parse(url));
        // intent.setDataAndType(Uri.parse(url), "*/*");
        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    private void deletePhysicalExam(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(PhysicalExamActivity.this, R.style.MyAlertDialogStyle);
        builder.setCancelable(false);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete this record");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDilogue.showAlert();
                PhysicalServerObjectDataController.getInstance().deletePhysicalExamServerApi();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.show();
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
            Toast.makeText(PhysicalExamActivity.this, "Image Capturing Cancelled", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(PhysicalExamActivity.this, "Image Selection Cancelled", Toast.LENGTH_SHORT).show();
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
            PhysicalServerObjectDataController.getInstance().apiCallForAttchmet(file);
        }else {
            alertDilogue.dismiss();
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
                Uri photoURI = FileProvider.getUriForFile(PhysicalExamActivity.this,
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

    /*public void cameraDailog() {
        TextView cam, gal, canc,file;
        AlertDialog.Builder   dialog1 = new AlertDialog.Builder(PhysicalExamActivity.this);
        LayoutInflater   inflater = PhysicalExamActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout_boarder.file_dailog, null);
        dialog1.setView(dialogView);
        cam = dialogView.findViewById(R.id.camera);
        gal = dialogView.findViewById(R.id.gallery);
        canc = dialogView.findViewById(R.id.cancel);
        canc.setTextColor(Color.parseColor("#ED5276"));
        file = dialogView.findViewById(R.id.file);
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(PhysicalExamActivity.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    dialog.dismiss();
                    LoadCaptureImageScreen();
                } else {
                    requestCameraPermission();
                }
            }
        });
        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              *//*  if (ActivityCompat.checkSelfPermission(PhysicalExamActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    dialog.dismiss();
                    loadFileFromFileManneger();
                } else {
                    requestStoragePermission();
                }*//*

            }
        });
        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(PhysicalExamActivity.this,
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
    private void loadFileFromFileManneger() {
        Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        fileIntent.setType("file/*");
        startActivityForResult(fileIntent, PICKFILE_RESULT_CODE);
    }*/
    public void cameraDailog() {
        TextView cam, gal, canc,file;
        AlertDialog.Builder   dialog1 = new AlertDialog.Builder(PhysicalExamActivity.this);
        LayoutInflater   inflater = PhysicalExamActivity.this.getLayoutInflater();
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
                if (ActivityCompat.checkSelfPermission(PhysicalExamActivity.this,
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
                if (ActivityCompat.checkSelfPermission(PhysicalExamActivity.this,
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
                if (ActivityCompat.checkSelfPermission(PhysicalExamActivity.this,
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
                    Toast.makeText(PhysicalExamActivity.this, "Yay! You Denied Permission", Toast.LENGTH_SHORT).show();
                }
                break;
            case GALLERY_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LoadImageFromGallery();
                } else {
                    Toast.makeText(PhysicalExamActivity.this, "Yay! You Denied Permission", Toast.LENGTH_SHORT).show();
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
                            ActivityCompat.requestPermissions(PhysicalExamActivity.this,
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
            ActivityCompat.requestPermissions(PhysicalExamActivity.this,
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
                            ActivityCompat.requestPermissions(PhysicalExamActivity.this,
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
            ActivityCompat.requestPermissions(PhysicalExamActivity.this,
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
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PhysicalExamActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);
    }
    @Override
    public  void onBackPressed(){
        finish();
    }

    // this method is used for drag the floating button

/*
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onTouch(View view, MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                view.setY(event.getRawY() + dY);
                view.setX(event.getRawX() + dX);
                lastAction = MotionEvent.ACTION_MOVE;
                break;

            case MotionEvent.ACTION_UP:
                if (lastAction == MotionEvent.ACTION_DOWN)
                    // Toast.makeText(DraggableView.this, "Clicked!", Toast.LENGTH_SHORT).show();
                    break;
            case MotionEvent.ACTION_BUTTON_PRESS:
                bottomDialogSheet();
               // fileLayout.setVisibility(View.VISIBLE);
                break;

            default:
                return false;
        }
        return true;
    }
*/


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void bottomDialogSheet() {
        View dialogFamilyView = getLayoutInflater().inflate(R.layout.file_bottom_sheet, null);
        attachmentDialog = new BottomSheetDialog(Objects.requireNonNull(PhysicalExamActivity.this), R.style.BottomSheetDialogTheme);
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
                PhysicalExamDataController.getInstance().currentPhysicalExamsData=null;
                fileLayout.setVisibility(View.GONE);
                Intent physicalRecordIntent = new Intent(PhysicalExamActivity.this, PhysicalExamRecordActivity.class);
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
        final BottomSheetDialog cameraBottomSheetDialog =new BottomSheetDialog(Objects.requireNonNull(PhysicalExamActivity.this), R.style.BottomSheetDialogTheme);
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
                if (ActivityCompat.checkSelfPermission(PhysicalExamActivity.this,
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
                if (ActivityCompat.checkSelfPermission(PhysicalExamActivity.this,
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
                if (ActivityCompat.checkSelfPermission(PhysicalExamActivity.this,
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
}
