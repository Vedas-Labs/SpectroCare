package com.vedas.spectrocare.PatientServerApiModel;

import java.util.ArrayList;

public class PatientFamilyAddServerObject {
int response;
RecordModel records;

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public RecordModel getRecords() {
        return records;
    }

    public void setRecords(RecordModel records) {
        this.records = records;
    }
}
