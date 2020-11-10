package com.vedas.spectrocare.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.DataBase.BmiDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBase.PhysicalCategoriesDataController;
import com.vedas.spectrocare.DataBase.PhysicalExamDataController;
import com.vedas.spectrocare.DataBase.PhysicalExamTrackInfoDataController;
import com.vedas.spectrocare.DataBase.TrackInfoDataController;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.R;
import com.google.android.material.navigation.NavigationView;
import com.vedas.spectrocare.adapter.HomeAdapter;
import com.vedas.spectrocare.model.CategoryItemModel;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {
    DrawerLayout menuLayout;
    NavigationView navigationView;
    String mailID,passworD;
    Button btnPatientProfile;
    ActionBarDrawerToggle actionBarDrawerToggle;
    TextView text_name;
    ImageView imageHeader;
    ArrayList homeItemList;
    FrameLayout content_frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menuLayout = findViewById(R.id.navigation_layout);
        content_frame=findViewById(R.id.content_frame);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        homeItemList = new ArrayList();
        navigationView = findViewById(R.id.navigation_view);
        btnPatientProfile = findViewById(R.id.patient_profile);
        View hView =  navigationView.getHeaderView(0);
         imageHeader = hView.findViewById(R.id.header_pro_pic);
        text_name= hView.findViewById(R.id.header_txt);

        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        loadCutterMedicalPersonData();

        homeItemList.add(new CategoryItemModel(R.drawable.search_patients,"Search Patients"));
        homeItemList.add(new CategoryItemModel(R.drawable.statistics,"Statistics Reports"));
        homeItemList.add(new CategoryItemModel(R.drawable.add,"Add Medical Record"));
        homeItemList.add(new CategoryItemModel(R.drawable.consult_talk,"Consulting Room"));
        homeItemList.add(new CategoryItemModel(R.drawable.screening_center,"Screening Center"));

        RecyclerView homeItemRecyclerView = findViewById(R.id.home_grid_item_view);
        HomeAdapter homeAdapter = new HomeAdapter(this,homeItemList);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        homeItemRecyclerView.setLayoutManager(layoutManager);
        homeItemRecyclerView.setAdapter(homeAdapter);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, menuLayout, toolbar, R.string.open, R.string.close);
        actionBarDrawerToggle.syncState();
        /* actionBarDrawerToggle = new ActionBarDrawerToggle(this, menuLayout, R.string.open, R.string.close)
        {
            @SuppressLint("NewApi")
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                super.onDrawerSlide(drawerView, slideOffset);
                float moveFactor = (menuLayout.getWidth() * slideOffset);
                content_frame.setTranslationX(moveFactor);
            }
        };*/
        menuLayout.setScrimColor(Color.TRANSPARENT);
        menuLayout.addDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu3x);
        menuLayout.addDrawerListener(actionBarDrawerToggle);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
               // menuLayout.closeDrawer(GravityCompat.START);
                int id =menuItem.getItemId();
                if(id==R.id.Logout){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
                    alertDialog.setTitle("Logout");
                    alertDialog.setMessage("Are you sure you want to logout ?");
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences prefs = getSharedPreferences("UserPref's", 0);
                            SharedPreferences.Editor edit = prefs.edit();
                            edit.clear();
                            edit.commit();
                            finish();
                            MedicalProfileDataController.getInstance().deleteMedicalProfileData(MedicalProfileDataController.getInstance().allMedicalProfile);
                            PhysicalCategoriesDataController.getInstance().deletePhysicalExamData(PhysicalExamDataController.getInstance().allPhysicalExamList);
                            BmiDataController.getInstance().deleteBmiData(PhysicalExamDataController.getInstance().allPhysicalExamList);
                            PhysicalExamTrackInfoDataController.getInstance().deleteTrackData(PhysicalExamDataController.getInstance().allPhysicalExamList);

                            TrackInfoDataController.getInstance().deleteTrackData(PatientProfileDataController.getInstance().allPatientlProfile);
                            TrackInfoDataController.getInstance().allTrackList=null;
                            PatientProfileDataController.getInstance().deletePatientlProfileModelData(PatientProfileDataController.getInstance().allPatientlProfile);
                            startActivity(new Intent(getApplicationContext(),SelectUserActivity.class));// Write your code here to invoke YES event
                        }
                    });
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                }
                if (id==R.id.about){
                    Intent openURL = new Intent(android.content.Intent.ACTION_VIEW);
                    openURL.setData(Uri.parse("http://www.spectrochips.com"));
                    startActivity(openURL);
                }

                if (id == R.id.changePassword) {
                   // Toast.makeText(HomeActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                    mailID = getIntent().getStringExtra("mailId");
                    Log.e("change", "" + mailID);
                    passworD = getIntent().getStringExtra("password");
                    Intent intent = new Intent(HomeActivity.this, ChangePasswordActivity.class);
                    intent.putExtra("mail", mailID);
                    intent.putExtra("password", passworD);
                    startActivity(intent);
                }
                return false;
            }
        });
        btnPatientProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPatientActivity.isFromStarting=true;
                Intent myPatientIntent = new Intent(HomeActivity.this, MyPatientActivity.class);
               // myPatientIntent.putExtra("medicalPId",medicalPersonnelId);
                //myPatientIntent.putExtra("hospRegId",hospRegId);
                startActivity(myPatientIntent);

            }
        });

        imageHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent UpdateMedicPerson = new Intent(HomeActivity.this, MedicalPersonSignupActivity.class);
                UpdateMedicPerson.putExtra("mail", mailID);
                UpdateMedicPerson.putExtra("password", passworD);
                UpdateMedicPerson.putExtra("isFromMedicalPerson","UpdateMedic");
                startActivity(UpdateMedicPerson);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCutterMedicalPersonData();
    }

    private  void loadCutterMedicalPersonData(){
        if(MedicalProfileDataController.getInstance().currentMedicalProfile!=null){
            final MedicalProfileModel objModel=MedicalProfileDataController.getInstance().currentMedicalProfile;
            text_name.setText(objModel.getFirstName()+" "+objModel.getLastName());
            Log.e("nanan",""+text_name.getText().toString());
            Log.e("profilePic", "" + objModel.getProfilePic());

            if(objModel.getProfileByteArray() !=null && objModel.getProfileByteArray().length>0){
               Bitmap bitmap=PersonalInfoController.getInstance().convertByteArrayTOBitmap(objModel.getProfileByteArray());
                imageHeader.setImageBitmap(bitmap);
                Log.e("profileCheckfromdb", "" + bitmap);

            }else {
              //  Picasso.get().load(objModel.getProfilePic()).into(imageHeader);
                Picasso.get().load(objModel.getProfilePic()).placeholder(R.drawable.profile_1).into(imageHeader);
                /*Picasso.get().load(objModel.getProfilePic()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap1, Picasso.LoadedFrom isFromMedicalPerson) {
                        // loaded bitmap is here (bitmap)
                        Log.e("profileCheckfromurl", "" + bitmap1);
                        objModel.setProfileByteArray(PersonalInfoController.getInstance().convertBitmapToByteArray(bitmap1));
                        MedicalProfileDataController.getInstance().updateMedicalProfileData(objModel);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });*/
            }

        }
    }
    @Override
    public void onBackPressed() {    //when click on phone backbutton
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
