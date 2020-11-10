package com.vedas.spectrocare.PatientModule;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.vedas.spectrocare.MedicalPersonnelController;
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
    ArrayList<String> specificArray;
    ArrayList<String> languageList;
    MedicalPersonnelController departmetResponseController = MedicalPersonnelController.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        specificArray = new ArrayList<>();
        languageList = new ArrayList<>();
        specificArray.add("Dentistry");
        specificArray.add("Surgery");
        specificArray.add("Cardio");
        specificArray.add("Paediatric");
        languageList.add("English");
        languageList.add("Telugu");
        languageList.add("Hindi");
        languageList.add("Kannada");

        chipGroup = view.findViewById(R.id.lang_chip_group);
        specialityView = view.findViewById(R.id.speciality_view);
        languageView = view.findViewById(R.id.language_view);
        specificAdapter = new DoctorSpecificAdapter(getContext(),specificArray);
        languageAdapter = new DoctorLanguageAdapter(getContext(),languageList);
        specialityView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        specialityView.setAdapter(specificAdapter);
        languageView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        languageView.setAdapter(languageAdapter);
       // addChip(specificArray,chipGroup);
        return view;
    }

/*
    private void addChip(ArrayList<String> arrayList, ChipGroup pChipGroup) {


        for(int i=0;i<arrayList.size();i++) {

            Chip lChip = new Chip(getActivity());
            lChip.setText(arrayList.get(i));
            lChip.setTextColor(getResources().getColor(R.color.primary_text));
            lChip.setChipBackgroundColor(getResources().getColorStateList(R.color.gray));
            pChipGroup.addView(lChip, pChipGroup.getChildCount() - 1);

        }
    }
*/
}
