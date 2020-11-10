package com.vedas.spectrocare.adapter;

import com.vedas.spectrocare.fragments.BodyIndex;
import com.vedas.spectrocare.fragments.PhysicalRecords;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TabAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public TabAdapter(FragmentManager fm, int NoofTabs){
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
                BodyIndex bodyIndex = new BodyIndex();
                return bodyIndex;
            case 1:
                PhysicalRecords physicalRecords = new PhysicalRecords();
                return physicalRecords;
            case 2:
              /*  FamilyRecords familyRecords = new FamilyRecords();
                return familyRecords;
*/
            case 3 :
              /*  VaccineRecords vaccineRecords = new VaccineRecords();
                return vaccineRecords;*/
            default:
                return null;
        }
    }
}
