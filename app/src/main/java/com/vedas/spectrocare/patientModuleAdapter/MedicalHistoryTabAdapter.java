package com.vedas.spectrocare.patientModuleAdapter;

import com.vedas.spectrocare.PatientModule.AvailabilityFragment;
import com.vedas.spectrocare.PatientModule.ClinicSummeryFragment;
import com.vedas.spectrocare.PatientModule.ProfileFragment;
import com.vedas.spectrocare.patient_fragment.MediacalAllFragment;
import com.vedas.spectrocare.patient_fragment.PatientAllergyFragment;
import com.vedas.spectrocare.patient_fragment.PatientDiseaseFragment;
import com.vedas.spectrocare.patient_fragment.PatientDoctorsFragment;
import com.vedas.spectrocare.patient_fragment.PatientFamilyHistoryFragment;
import com.vedas.spectrocare.patient_fragment.PatientImmunizationFragment;
import com.vedas.spectrocare.patient_fragment.PatientSurgeryFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MedicalHistoryTabAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public MedicalHistoryTabAdapter(@NonNull FragmentManager fm,int NoOfTabs) {
        super(fm);
        this.mNumOfTabs = NoOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                MediacalAllFragment mediacalAllFragment =new MediacalAllFragment();
                return mediacalAllFragment;
            case 1:
                PatientDoctorsFragment docFragment = new PatientDoctorsFragment();
                return docFragment;
            case 2:
                PatientDiseaseFragment diseaseFragment = new PatientDiseaseFragment();
                return diseaseFragment;
            case 3:
                PatientFamilyHistoryFragment familyHistoryFragment = new PatientFamilyHistoryFragment();
                return familyHistoryFragment;
            case 4:
                PatientAllergyFragment allergyFragment = new PatientAllergyFragment();
                return allergyFragment;
            case 5:
                PatientSurgeryFragment surgeryFragment = new PatientSurgeryFragment();
                return surgeryFragment;
            case 6:
                PatientImmunizationFragment immunizationFragment = new PatientImmunizationFragment();
                return immunizationFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {

        return mNumOfTabs;
    }
}
