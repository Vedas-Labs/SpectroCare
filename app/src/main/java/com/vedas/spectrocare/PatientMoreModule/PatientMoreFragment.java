package com.vedas.spectrocare.PatientMoreModule;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientModule.PatientHomeActivity;
import com.vedas.spectrocare.PatientModule.PatientProfileActivity;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.ChangePasswordActivity;
import com.vedas.spectrocare.activities.LoginActivity;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientMoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientMoreFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PatientMoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientMoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientMoreFragment newInstance(String param1, String param2) {
        PatientMoreFragment fragment = new PatientMoreFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View moreView =inflater.inflate(R.layout.fragment_patient_more, container, false);
        alertDailog();
        return moreView;
    }
    public void alertDailog() {
        View view = getLayoutInflater().inflate(R.layout.more_alert, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(Objects.requireNonNull(getActivity()),R.style.BottomSheetDialogTheme);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();
        RelativeLayout rlPSW=dialog.findViewById(R.id.psw);
        RelativeLayout rlProfile=dialog.findViewById(R.id.profile);
        RelativeLayout rlLogout=dialog.findViewById(R.id.logout);
        RelativeLayout rlsettings=dialog.findViewById(R.id.settings);
        RelativeLayout rlHospital =dialog.findViewById(R.id.hospital);

        rlsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(getContext(), SettingsActivity.class));
            }
        });
        rlHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                loadUrl();            }
        });
        rlPSW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(getContext(), ChangePasswordActivity.class));
            }
        });
        rlProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(getContext(), PatientProfileActivity.class));
            }
        });
        rlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showLogoutDialog();
            }
        });
    }
    private void loadUrl(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://spectrochips.com"));
        startActivity(browserIntent);
    }
    public void showLogoutDialog(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_abort);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
        btnNo.setText("Cancel");
        btnYes.setText("LogOut");

        TextView txt_title=dialog.findViewById(R.id.title);
        TextView txt_msg=dialog.findViewById(R.id.msg);
        TextView txt_msg1=dialog.findViewById(R.id.msg1);

        txt_title.setText("LogOut");
        txt_msg.setText("Are you sure you");
        txt_msg1.setText("want to logout ?");

        RelativeLayout main=(RelativeLayout)dialog.findViewById(R.id.rl_main);
        RelativeLayout main1=(RelativeLayout)dialog.findViewById(R.id.rl_main1);

        GradientDrawable drawable = (GradientDrawable) main.getBackground();
        drawable.setColor(getResources().getColor(R.color.colorWhite));

        GradientDrawable drawable1 = (GradientDrawable) main1.getBackground();
        drawable1.setColor(getResources().getColor(R.color.colorWhite));

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(PatientLoginDataController.getInstance().deletePatientData(PatientLoginDataController.getInstance().currentPatientlProfile))
                        {
                            Log.e("ddd","ss");
                            startActivity(new Intent(getContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            dialog.dismiss();
                        }
                    }
                });
                //  dialog.dismiss();

            }
        });
        dialog.show();

    }

}
