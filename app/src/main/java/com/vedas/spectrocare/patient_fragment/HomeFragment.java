package com.vedas.spectrocare.patient_fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBaseModels.PatientModel;
import com.vedas.spectrocare.PatientMyDeviceModule.PatientMyDeviceActivity;
import com.vedas.spectrocare.PatientModule.DoctorsActivity;
import com.vedas.spectrocare.PatientModule.PatientAppointmentsTabsActivity;
import com.vedas.spectrocare.PatientModule.PatientMessageActivity;
import com.vedas.spectrocare.PatientModule.PatientTestRecordActivity;
import com.vedas.spectrocare.PatientModule.TestMenuStartactivity;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.adapter.PatientItemAdapter;
import com.vedas.spectrocare.model.CategoryItemModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    ArrayList patientsList;
    PatientItemAdapter patientItemAdapter;
    RecyclerView patientItemRecycler;
    TextView wishText,dateText;
    TextView nameText;
    CircularImageView profileImage;
    CardView cv_appintments;
    public HomeFragment() {
    }
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_home, container, false);
        patientsList = new ArrayList();
        RelativeLayout appointmentLayout = v.findViewById(R.id.appointment_layout);
        RelativeLayout docLayout = v.findViewById(R.id.doc_layout);
        RelativeLayout messageLayout = v.findViewById(R.id.layout_message);
        RelativeLayout cv_test = v.findViewById(R.id.layout_test);
        RelativeLayout deviceLayout = v.findViewById(R.id.layout_device);
        RelativeLayout testRecordLayout = v.findViewById(R.id.layout_test_record);
        patientItemRecycler = v.findViewById(R.id.patient_grid_view);
        patientItemRecycler.setFocusable(false);
        patientsList.add(new CategoryItemModel(R.drawable.ic_user, "Profile"));
        patientsList.add(new CategoryItemModel(R.drawable.ic_medical_history, "Medical History"));
        patientsList.add(new CategoryItemModel(R.drawable.ic_physical_exam, "Physical Exam"));
        patientsList.add(new CategoryItemModel(R.drawable.drug, "Medications"));
      //  patientsList.add(new CategoryItemModel(R.drawable.hospital, "Diagnosis"));
        patientsList.add(new CategoryItemModel(R.drawable.test_record, "Screening Record"));
       // patientsList.add(new CategoryItemModel(R.drawable.test_record, "Test Report"));
     //   patientsList.add(new CategoryItemModel(R.drawable.ic_user, "Change Password"));
        patientItemAdapter = new PatientItemAdapter(getContext(),patientsList);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        patientItemRecycler.setLayoutManager(layoutManager);
        patientItemRecycler.setNestedScrollingEnabled(false);
        patientItemRecycler.setAdapter(patientItemAdapter);

        testRecordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getContext(), PatientTestRecordActivity.class));
            }
        });
        cv_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), TestMenuStartactivity.class));
            }
        });
        deviceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PatientMyDeviceActivity.class));
            }
        });
        docLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), DoctorsActivity.class));
            }
        });
        loadIds(v);
        loadCurrentMemberData(v);
        messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getContext(), PatientMessageActivity.class));
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
    private void loadIds(View view){
        cv_appintments=view.findViewById(R.id.appointments);
        cv_appintments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PatientAppointmentsTabsActivity.class));
            }
        });
    }
    private void loadCurrentMemberData(View view){
        wishText=view.findViewById(R.id.txt_morning);
        dateText=view.findViewById(R.id.txt_date);
        nameText=view.findViewById(R.id.txt_name);
        profileImage=view.findViewById(R.id.profileImg);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(getContext(), PatientProfileActivity.class));
            }
        });
        if(PatientLoginDataController.getInstance().currentPatientlProfile!=null){
            PatientModel objModel=PatientLoginDataController.getInstance().currentPatientlProfile;
            nameText.setText(objModel.getFirstName()+" "+objModel.getLastName());
           // Picasso.get().load(objModel.getProfilePic()).into(profileImage);
            Log.e("meraaPic","df"+objModel.getProfilePic());
           Picasso.get().load(objModel.getProfilePic()).placeholder(R.drawable.image).into(profileImage);
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat weekFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
            dateText.setText("Today ,"+weekFormatter.format(currentTime));
            wishText.setText(getWishTextString());
        }
    }
    private String getWishTextString(){
        String text="";
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            text="Good Morning";
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            text="Good Afternoon";
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            text="Good Evening";
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            text="Good Night";
        }
        return text;
    }
}
