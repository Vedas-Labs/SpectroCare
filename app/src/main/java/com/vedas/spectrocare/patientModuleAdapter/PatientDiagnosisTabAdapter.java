package com.vedas.spectrocare.patientModuleAdapter;

import com.vedas.spectrocare.PatientModule.PatientDiagnosisActivity;
import com.vedas.spectrocare.patient_fragment.DiagnosticNoteFragment;
import com.vedas.spectrocare.patient_fragment.MedicationsFragment;
import com.vedas.spectrocare.patient_fragment.PatientAllFragment;
import com.vedas.spectrocare.patient_fragment.PatientHospitalFragment;
import com.vedas.spectrocare.patient_fragment.PatientInfoFragment;
import com.vedas.spectrocare.patient_fragment.PatientSelfExamFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PatientDiagnosisTabAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public PatientDiagnosisTabAdapter(@NonNull FragmentManager fm, int NoofTabs) {
        super(fm);
        this.mNumOfTabs = NoofTabs;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                PatientInfoFragment patientInfoFragment =new PatientInfoFragment();
                return patientInfoFragment;
            case 1:
                DiagnosticNoteFragment noteFragment = new DiagnosticNoteFragment();
                return noteFragment;
            case 2:
                MedicationsFragment medicationsFragment = new MedicationsFragment();
                return medicationsFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
