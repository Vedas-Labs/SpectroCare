package com.vedas.spectrocare.PatientServerApiModel;

import java.util.ArrayList;

public class PatientPhysicalRecordModel {
    int response;
    ArrayList<PatientPhysicalModel> physical_exam_records;

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public ArrayList<PatientPhysicalModel> getPhysical_exam_records() {
        return physical_exam_records;
    }

    public void setPhysical_exam_records(ArrayList<PatientPhysicalModel> physical_exam_records) {
        this.physical_exam_records = physical_exam_records;
    }
}
