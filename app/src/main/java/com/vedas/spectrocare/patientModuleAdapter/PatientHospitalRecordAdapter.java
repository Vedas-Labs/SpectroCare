package com.vedas.spectrocare.patientModuleAdapter;

import com.vedas.spectrocare.PatientModule.ClinicSummeryFragment;
import com.vedas.spectrocare.patient_fragment.PatientDoctorRecordFragment;
import com.vedas.spectrocare.patient_fragment.PatientFileFragment;
import com.vedas.spectrocare.patient_fragment.PatientHospitalRecordFragment;
import com.vedas.spectrocare.patient_fragment.PatientHospitalSystemFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PatientHospitalRecordAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public PatientHospitalRecordAdapter(@NonNull FragmentManager fm,int NoofTabs) {
        super(fm);
        this.mNumOfTabs = NoofTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                PatientHospitalRecordFragment  hospitalRecordFragment =new PatientHospitalRecordFragment();
                return hospitalRecordFragment;
            case 1:
                PatientHospitalSystemFragment summeryFragment = new PatientHospitalSystemFragment();
                return summeryFragment;
            case 2:
                PatientFileFragment fileFragment = new PatientFileFragment();
                return fileFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
