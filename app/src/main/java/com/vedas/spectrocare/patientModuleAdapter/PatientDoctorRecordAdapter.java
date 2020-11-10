package com.vedas.spectrocare.patientModuleAdapter;

import com.vedas.spectrocare.PatientModule.AvailabilityFragment;
import com.vedas.spectrocare.PatientModule.ClinicSummeryFragment;
import com.vedas.spectrocare.PatientModule.ProfileFragment;
import com.vedas.spectrocare.patient_fragment.PatientDoctorRecordFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PatientDoctorRecordAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public PatientDoctorRecordAdapter(FragmentManager fm, int NoofTabs){
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
                PatientDoctorRecordFragment doctorRecordFragment =new PatientDoctorRecordFragment();
                return doctorRecordFragment;
            case 1:
                ClinicSummeryFragment about = new ClinicSummeryFragment();
                return about;
            default:
                return null;
        }
    }
}
