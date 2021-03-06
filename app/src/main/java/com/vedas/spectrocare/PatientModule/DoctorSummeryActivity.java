package com.vedas.spectrocare.PatientModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.vedas.spectrocare.PatientDocResponseModel.MedicalPersonnelModel;
import com.vedas.spectrocare.PatientDocResponseModel.ReviewsModel;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.patientModuleAdapter.TabsAdapter;

import java.util.ArrayList;

public class DoctorSummeryActivity extends AppCompatActivity {
    ImageView backImg;
    CircularImageView docPic;
    TextView txtDoc, txtSpecial,txt_ratings;
    RatingBar ratingBar;
    Button book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_summery);
        txtDoc = findViewById(R.id.txt_name_of_doc);
        backImg = findViewById(R.id.img_back_arrow);
        docPic = findViewById(R.id.img_doc_pic);
        txtSpecial = findViewById(R.id.txt_doc_specail);
        book=findViewById(R.id.btn_book);
        ratingBar=findViewById(R.id.ratingBar1);
        txt_ratings=findViewById(R.id.rating_text);

        loadSelectedDoctorData();

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Profile"));
        tabLayout.addTab(tabLayout.newTab().setText("Clinical Summery"));
        tabLayout.addTab(tabLayout.newTab().setText("Availability"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
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

    private void loadSelectedDoctorData() {
        if (PatientMedicalRecordsController.getInstance().medicalPersonnelModel != null) {
            MedicalPersonnelModel obj=PatientMedicalRecordsController.getInstance().medicalPersonnelModel;
            txtSpecial.setText(obj.getProfile()
                    .getUserProfile().getDepartment());

            Picasso.get().load(ServerApi.img_home_url +obj.getProfile()
                    .getUserProfile().getProfilePic()).placeholder(R.drawable.profile_1)
                    .into(docPic);

            txtDoc.setText("DR" + "." + obj.getProfile()
                    .getUserProfile().getFirstName() + " " + obj.getProfile()
                    .getUserProfile().getLastName());

            ArrayList<ReviewsModel> reviewsList=obj.getReviews();
            if(reviewsList.size()>0) {
                ReviewsModel reviewsModel = reviewsList.get(reviewsList.size() - 1);
                ratingBar.setRating(Float.parseFloat(reviewsModel.getRatings()));
                txt_ratings.setText(reviewsModel.getRatings());
            }else{
                ratingBar.setRating(0.0f);
                txt_ratings.setText("0.0");
            }

            book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), PatientBookAppointmentActivity.class)
                            .putExtra("docName", obj.getProfile().getUserProfile().getFirstName()+" "+ obj.getProfile().getUserProfile().getLastName())
                            .putExtra("docProfi", obj.getProfile().getUserProfile().getDepartment())
                            .putExtra("docId", obj.getProfile().getUserProfile().getMedical_personnel_id())
                            .putExtra("docProfile", obj.getProfile().getUserProfile().getProfilePic()));
                }
            });
        }
    }
}
