package com.vedas.spectrocare.ServerApiModel;

import android.text.Editable;

public class MedicationRecordServerObject {
    String name;
    String dosage;
    String freq;
    String purpose;
    String durationDays;
    String moreDetails;

   /* public MedicationRecordServerObject(String name, String dosage, String freq, String purpose, String durationDays, String moreDetails) {
        this.name = name;
        this.dosage = dosage;
        this.freq = freq;
        this.purpose = purpose;
        this.durationDays = durationDays;
        this.moreDetails = moreDetails;
    }*/

    public String getName(String text) {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDosage(String s) {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFreq(String s) {
        return freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getDurationDays(String s) {
        return durationDays;
    }

    public void setDurationDays(String durationDays) {
        this.durationDays = durationDays;
    }

    public String getMoreDetails(String s) {
        return moreDetails;
    }

    public void setMoreDetails(String moreDetails) {
        this.moreDetails = moreDetails;
    }
}
