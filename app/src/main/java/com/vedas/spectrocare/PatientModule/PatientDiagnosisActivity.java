package com.vedas.spectrocare.PatientModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.material.tabs.TabLayout;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.patientModuleAdapter.PatientDiagnosisTabAdapter;
import com.vedas.spectrocare.patientModuleAdapter.PatientDoctorRecordAdapter;

public class PatientDiagnosisActivity extends AppCompatActivity {
    ImageView backImage;
    RelativeLayout docLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paatient_diagnosis);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Patient Info"));
        tabLayout.addTab(tabLayout.newTab().setText("Diagnostic Note"));
        tabLayout.addTab(tabLayout.newTab().setText("Medication"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        backImage = findViewById(R.id.img_back_arrow);
        docLayout = findViewById(R.id.doc_layout);
        docLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(PatientDiagnosisActivity.this,DoctorSummeryActivity.class));
            }
        });
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        final ViewPager viewPager =(ViewPager)findViewById(R.id.view_pager);
        PatientDiagnosisTabAdapter diagnosisTabAdapter = new PatientDiagnosisTabAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(diagnosisTabAdapter);
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
