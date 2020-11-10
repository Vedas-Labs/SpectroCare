package com.vedas.spectrocare.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApiModel.MedicalRecordModel;
import com.vedas.spectrocare.adapter.TabAdapter;
import com.google.android.material.tabs.TabLayout;
import com.vedas.spectrocare.model.PatientList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class AddMedicalRecordActivity extends AppCompatActivity {
    EditText edtHeight;
    static ViewPager viewPager;
    String patientId, from;
    public MedicalRecordModel medicalRecordModel;
    public boolean isUpdate;
    TextView txt_patientName,txt_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical_record);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        txt_patientName=findViewById(R.id.patientName);
        txt_back=findViewById(R.id.back);
        tabLayout.addTab(tabLayout.newTab().setText("Body Index"));
        tabLayout.addTab(tabLayout.newTab().setText("Physical Record"));
        tabLayout.addTab(tabLayout.newTab().setText("Family History"));
        tabLayout.addTab(tabLayout.newTab().setText("Vaccine Record"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        TextView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (getIntent().hasExtra("isFromMedicalPerson")) {
            from = getIntent().getStringExtra("isFromMedicalPerson");
            if (from.equalsIgnoreCase("updateRecord")) {
                isUpdate = true;
            }
        }
        patientId = getIntent().getStringExtra("patientID");
        Log.e("ppppp",""+patientId);

        medicalRecordModel = new MedicalRecordModel();

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        TabAdapter tabsAdapter = new TabAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                Log.e("dsfbbdkjf", "" + tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if(PersonalInfoController.getInstance().currentPatient!=null){
            PatientList objModel= PersonalInfoController.getInstance().currentPatient;
            txt_patientName.setText(objModel.getFirstName()+" "+objModel.getLastName());
        }
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }

    public String patientId() {

        return patientId;
    }

    public void changer(int i) {

        viewPager.setCurrentItem(i);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
