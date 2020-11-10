package com.vedas.spectrocare.PatientModule;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
//import com.vedas.spectrocare.PatientModule.SettingsActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientAppointmentModule.AppointmentArrayModel;
import com.vedas.spectrocare.PatientAppointmentModule.PatientAppointmentsDataController;
import com.vedas.spectrocare.PatinetControllers.PatientAppointmentController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.ChangePasswordActivity;
import com.vedas.spectrocare.model.AppointmetModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;
public class PatientAppointmentsTabsActivity extends AppCompatActivity {
   ImageView backImg,addImg;
   ArrayList<AppointmetModel> modelList;
    RefreshShowingDialog refreshShowingDialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientappointment);
        modelList = new ArrayList<>();
        ButterKnife.bind(this);
       // PatientAppointmentsDataController.setNull();
        refreshShowingDialog = new RefreshShowingDialog(PatientAppointmentsTabsActivity.this);

        backImg=findViewById(R.id.back);
      //  addImg = findViewById(R.id.img_add);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
       /* modelList.add(new AppointmetModel(1,"Dr.Angaj"
                ,"05/09/2020","10:00 AM","Conformed","10:00","","paid","PayPal","Cardiology"));
        modelList.add(new AppointmetModel(1,"Dr.Sugreev"
                ,"07/05/2020","10:00 AM","Conformed","10:00","","paid","PayPal","Dentist"));
        modelList.add(new AppointmetModel(1,"Dr.Vaali"
                ,"10/06/2020","10:00 AM","Completed","10:00","","paid","PayPal","Mentalist"));
        modelList.add(new AppointmetModel(1,"Dr.Jambavanth"
                ,"15/08/2020","10:00 AM","Cancel","10:00","","paid","PayPal","Antrology"));
        PatientAppointmentController.getInstance().setAppointmentList(modelList);
                Log.e("sssiizz","dad"+PatientAppointmentController.getInstance().getAppointmentList().size());
        */
        addImg=findViewById(R.id.add);
        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientAppointmentsTabsActivity.this, DoctorsActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
               /* startActivity(new Intent(PatientAppointmentsTabsActivity.this,DoctorsActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));*/
            }
        });
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("UpComing"));
        tabLayout.addTab(tabLayout.newTab().setText("  Past  "));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager =(ViewPager)findViewById(R.id.view_pager);
        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
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
    Button btnAbort;
    @OnClick(R.id.add) void action() {
     alertDailog();
    }
    public void alertDailog() {
        View view = getLayoutInflater().inflate(R.layout.addappotment_bottomsheet, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(Objects.requireNonNull(PatientAppointmentsTabsActivity.this),R.style.BottomSheetDialogTheme);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();
        btnAbort = (Button) dialog.findViewById(R.id.status);
        final GradientDrawable drawable = (GradientDrawable) btnAbort.getBackground();
        drawable.setColor(getResources().getColor(R.color.colorGreen));
        CardView cancelView=dialog.findViewById(R.id.cancel);
        CardView detailsView=dialog.findViewById(R.id.details);

        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                drawable.setColor(getResources().getColor(R.color.colorAccent));
                startActivity(new Intent(getApplicationContext(),AppointmentCancelActivity.class));
            }
        });
        detailsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                drawable.setColor(getResources().getColor(R.color.colorAccent));
                startActivity(new Intent(getApplicationContext(),AppointmentDetailsActivity.class));
            }
        });
       /* RelativeLayout rlPSW=dialog.findViewById(R.id.psw);
        RelativeLayout rlProfile=dialog.findViewById(R.id.profile);
        RelativeLayout rlLogout=dialog.findViewById(R.id.logout);
        RelativeLayout rlsettings=dialog.findViewById(R.id.settings);
      */
    }

    public class TabsAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;
        public TabsAdapter(FragmentManager fm, int NoofTabs){
            super(fm);
            this.mNumOfTabs = NoofTabs;
        }
        @Override
        public int getCount() {
            return mNumOfTabs;
        }
        @Override
        public Fragment getItem(int position){
            switch (position){
                case 0:
                    UpcomingAppointmentFragment home = new UpcomingAppointmentFragment();
                    return home;
                case 1:
                    PastAppointmentFragment home1 = new PastAppointmentFragment();
                    return home1;
                default:
                    return null;
            }
        }
    }
}
