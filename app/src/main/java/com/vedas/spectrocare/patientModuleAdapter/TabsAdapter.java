package com.vedas.spectrocare.patientModuleAdapter;

import com.vedas.spectrocare.PatientModule.AvailabilityFragment;
import com.vedas.spectrocare.PatientModule.ClinicSummeryFragment;
import com.vedas.spectrocare.PatientModule.ProfileFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TabsAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public TabsAdapter(FragmentManager fm, int NoofTabs){
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
                ProfileFragment profileFragment =new ProfileFragment();
                return profileFragment;
            case 1:
                ClinicSummeryFragment about = new ClinicSummeryFragment();
                return about;
            case 2:
                AvailabilityFragment availabilityFragment = new AvailabilityFragment();
                return availabilityFragment;
            default:
                return null;
        }
    }
}
