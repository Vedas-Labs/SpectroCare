package com.vedas.spectrocare.patient_fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vedas.spectrocare.DataBase.BmiDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBase.PhysicalCategoriesDataController;
import com.vedas.spectrocare.DataBase.PhysicalExamDataController;
import com.vedas.spectrocare.DataBase.PhysicalExamTrackInfoDataController;
import com.vedas.spectrocare.DataBase.TrackInfoDataController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.HomeActivity;
import com.vedas.spectrocare.activities.SelectUserActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoreFragment newInstance(String param1, String param2) {
        MoreFragment fragment = new MoreFragment();
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
        final View moreView = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_more, container, false);
        TextView txtLogout = moreView.findViewById(R.id.txt_logout);
        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(container.getContext());
                alertDialog.setTitle("Logout");
                alertDialog.setMessage("Are you sure you want to logout ?");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        PatientLoginDataController.getInstance().deletePatientModelData(PatientLoginDataController.getInstance().allPatientlProfile);
                       getActivity().finish();
                        /* MedicalProfileDataController.getInstance().deleteMedicalProfileData(MedicalProfileDataController.getInstance().allMedicalProfile);
                        PhysicalCategoriesDataController.getInstance().deletePhysicalExamData(PhysicalExamDataController.getInstance().allPhysicalExamList);
                        BmiDataController.getInstance().deleteBmiData(PhysicalExamDataController.getInstance().allPhysicalExamList);
                        PhysicalExamTrackInfoDataController.getInstance().deleteTrackData(PhysicalExamDataController.getInstance().allPhysicalExamList);

                        TrackInfoDataController.getInstance().deleteTrackData(PatientProfileDataController.getInstance().allPatientlProfile);
                        TrackInfoDataController.getInstance().allTrackList=null;
                        PatientProfileDataController.getInstance().deletePatientlProfileModelData(PatientProfileDataController.getInstance().allPatientlProfile);*/

                       startActivity(new Intent(container.getContext(), SelectUserActivity.class));// Write your code here to invoke YES event
                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });
        return moreView;
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_more, container, false);
    }
}
