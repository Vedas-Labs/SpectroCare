package com.vedas.spectrocare.PatientModule;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.chip.ChipGroup;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.patientModuleAdapter.DoctorLanguageAdapter;
import com.vedas.spectrocare.patientModuleAdapter.DoctorSpecificAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    RecyclerView specialityView,languageView;
    DoctorSpecificAdapter specificAdapter;
    DoctorLanguageAdapter languageAdapter;
    ChipGroup chipGroup;
    ArrayList<String> specificArray= new ArrayList<>();
    ArrayList<String> languageList = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView txt_expirence,txt_summary;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        txt_expirence=view.findViewById(R.id.text_experience);
        txt_summary=view.findViewById(R.id.txt_summery);
        loadSelectedDoctorData();
        chipGroup = view.findViewById(R.id.lang_chip_group);

        languageView = view.findViewById(R.id.language_view);
        languageAdapter = new DoctorLanguageAdapter(getContext(),languageList);
        languageView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        languageView.setAdapter(languageAdapter);

        specialityView = view.findViewById(R.id.speciality_view);
        specificAdapter = new DoctorSpecificAdapter(getContext(),specificArray);
        specialityView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        specialityView.setAdapter(specificAdapter);

        return view;
    }
    private void loadSelectedDoctorData() {
        if (PatientMedicalRecordsController.getInstance().medicalPersonnelModel != null) {
            if(PatientMedicalRecordsController.getInstance().medicalPersonnelModel.getProfile().getGeneralInformation() != null){
                txt_expirence.setText(PatientMedicalRecordsController.getInstance().medicalPersonnelModel.getProfile().getGeneralInformation().getExperience());
                txt_summary.setText(PatientMedicalRecordsController.getInstance().medicalPersonnelModel.getProfile().getBiography());

                String spokenLang=PatientMedicalRecordsController.getInstance().medicalPersonnelModel.getProfile().getGeneralInformation().getSpokenLanguages();
                String langArray[]=spokenLang.split(",");
                for(int i=0;i<langArray.length;i++){
                    languageList.add(langArray[i]);
                }

                String speciality=PatientMedicalRecordsController.getInstance().medicalPersonnelModel.getProfile().getGeneralInformation().getSpeciality();
                if(speciality.contains(",")){
                    String specialityArray[]=speciality.split(",");
                    for(int i=0;i<specialityArray.length;i++){
                        specificArray.add(specialityArray[i]);
                    }
                }else{
                    specificArray.add(speciality);
                }

            }
        }
    }
}
