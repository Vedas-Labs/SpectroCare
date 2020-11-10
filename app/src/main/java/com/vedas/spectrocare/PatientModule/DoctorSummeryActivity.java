package com.vedas.spectrocare.PatientModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.vedas.spectrocare.MedicalPersonnelController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.patientModuleAdapter.TabsAdapter;

public class DoctorSummeryActivity extends AppCompatActivity {
ImageView backImg;
CircularImageView docPic;
TextView txtDoc,txtSpecial;
    MedicalPersonnelController medicalPersonnelController = MedicalPersonnelController.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_summery);
        txtDoc = findViewById(R.id.txt_name_of_doc);
        backImg = findViewById(R.id.img_back_arrow);
        docPic = findViewById(R.id.img_doc_pic);
        txtSpecial = findViewById(R.id.txt_doc_specail);
        txtSpecial.setText(medicalPersonnelController.getMedicalPersonnelModel().getProfile()
                .getUserProfile().getDepartment());
        Log.e("dkdkd","ad"+medicalPersonnelController.getMedicalPersonnelModel().getProfile().getUserProfile().getProfilePic());
        String imagePath = medicalPersonnelController.getMedicalPersonnelModel().getProfile().getUserProfile().getProfilePic();
        Log.e("gkb","jh:"+imagePath);


        Picasso.get().load(ServerApi.img_home_url+medicalPersonnelController.getMedicalPersonnelModel().getProfile()
                .getUserProfile().getProfilePic()).placeholder(R.drawable.profile_1)
                .into(docPic);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Profile"));
        tabLayout.addTab(tabLayout.newTab().setText("Clinical Summery"));
        tabLayout.addTab(tabLayout.newTab().setText("Availability"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        txtDoc.setText("DR"+"."+medicalPersonnelController.getMedicalPersonnelModel().getProfile()
        .getUserProfile().getFirstName()+" "+medicalPersonnelController.getMedicalPersonnelModel().getProfile()
                .getUserProfile().getLastName());
        final ViewPager viewPager =(ViewPager)findViewById(R.id.view_pager);
        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
