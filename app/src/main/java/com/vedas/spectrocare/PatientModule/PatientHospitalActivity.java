package com.vedas.spectrocare.PatientModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.patientModuleAdapter.PatientDoctorRecordAdapter;
import com.vedas.spectrocare.patientModuleAdapter.PatientHospitalAdapter;
import com.vedas.spectrocare.patientModuleAdapter.PatientHospitalRecordAdapter;

public class PatientHospitalActivity extends AppCompatActivity {
    ImageView backImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_hospital);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Basic"));
        tabLayout.addTab(tabLayout.newTab().setText("System"));
        tabLayout.addTab(tabLayout.newTab().setText("Files"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        backImage = findViewById(R.id.img_back_arrow);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        final ViewPager viewPager =(ViewPager)findViewById(R.id.view_pager);
        PatientHospitalRecordAdapter tabsAdapter = new PatientHospitalRecordAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
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
