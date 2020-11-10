package com.vedas.spectrocare.patientModuleAdapter;

import com.vedas.spectrocare.PatientModule.AvailabilityFragment;
import com.vedas.spectrocare.PatientModule.ClinicSummeryFragment;
import com.vedas.spectrocare.PatientModule.ProfileFragment;
import com.vedas.spectrocare.patient_fragment.PatientAllFragment;
import com.vedas.spectrocare.patient_fragment.PatientHospitalFragment;
import com.vedas.spectrocare.patient_fragment.PatientSelfExamFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PatientPhysicalExamTabAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public PatientPhysicalExamTabAdapter(@NonNull FragmentManager fm, int NoofTabs) {
        super(fm);
        this.mNumOfTabs = NoofTabs;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                PatientAllFragment profileFragment =new PatientAllFragment();
                return profileFragment;
            case 1:
                PatientHospitalFragment hospitalFragment = new PatientHospitalFragment();
                return hospitalFragment;
            case 2:
                PatientSelfExamFragment selfExamFragment = new PatientSelfExamFragment();
                return selfExamFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
