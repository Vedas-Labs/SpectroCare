package com.vedas.spectrocare.patientModuleAdapter;

import com.vedas.spectrocare.patient_fragment.AllTestRecordFragment;
import com.vedas.spectrocare.patient_fragment.BloodTestFragment;
import com.vedas.spectrocare.patient_fragment.PatientAllFragment;
import com.vedas.spectrocare.patient_fragment.PatientHospitalFragment;
import com.vedas.spectrocare.patient_fragment.PatientSelfExamFragment;
import com.vedas.spectrocare.patient_fragment.UrineTestFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PatientTestRecordAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public PatientTestRecordAdapter(@NonNull FragmentManager fm, int NoofTabs) {
        super(fm);
        this.mNumOfTabs = NoofTabs;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                AllTestRecordFragment allRecords =new AllTestRecordFragment();
                return allRecords;
            case 1:
                UrineTestFragment urineTestFragment = new UrineTestFragment();
                return urineTestFragment;
            case 2:
                BloodTestFragment bloodTestFragment = new BloodTestFragment();
                return bloodTestFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
