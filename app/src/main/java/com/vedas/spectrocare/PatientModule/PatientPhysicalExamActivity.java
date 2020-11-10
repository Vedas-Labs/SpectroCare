package com.vedas.spectrocare.PatientModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.patientModuleAdapter.MedicalHistoryTabAdapter;
import com.vedas.spectrocare.patientModuleAdapter.PatientPhysicalExamTabAdapter;

public class PatientPhysicalExamActivity extends AppCompatActivity {
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_physical_exam);
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.addTab(tabLayout.newTab().setText("Hospital"));
        tabLayout.addTab(tabLayout.newTab().setText("Self-Exam"));
        final ViewPager viewPager =(ViewPager)findViewById(R.id.patient_view_pager);
        PatientPhysicalExamTabAdapter patientPhysicalTabAdapter = new PatientPhysicalExamTabAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(patientPhysicalTabAdapter);
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
