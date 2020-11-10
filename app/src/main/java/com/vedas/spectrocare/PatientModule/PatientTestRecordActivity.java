package com.vedas.spectrocare.PatientModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.patientModuleAdapter.PatientTestRecordAdapter;
import com.vedas.spectrocare.patient_fragment.AllTestRecordFragment;
import com.vedas.spectrocare.patient_fragment.BloodTestFragment;
import com.vedas.spectrocare.patient_fragment.UrineTestFragment;

public class PatientTestRecordActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ImageView edtImage, backImg;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_test_record);
        edtImage = findViewById(R.id.img_edit);
        backImg = findViewById(R.id.img_back_arrow);
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.addTab(tabLayout.newTab().setText("Urine Text"));
        tabLayout.addTab(tabLayout.newTab().setText("Blood Text"));
        final ViewPager viewPager = (ViewPager) findViewById(R.id.patient_view_pager);
        PatientTestRecordAdapter patienTestTabAdapter = new PatientTestRecordAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(patienTestTabAdapter);
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
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        edtImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewPager.getCurrentItem()==0){
                    AllTestRecordFragment allTestRecordFragment = new AllTestRecordFragment();
                    allTestRecordFragment.betterTest();
                }
                if(viewPager.getCurrentItem()==1){
                    UrineTestFragment urineTestFragment = new UrineTestFragment();
                    urineTestFragment.betterTest();
                }
                if(viewPager.getCurrentItem()==2){
                    BloodTestFragment bloodTestFragment = new BloodTestFragment();
                    bloodTestFragment.betterTest();
                }

            }
        });
    }

    public String sendData() {
        return data;
    }
}
