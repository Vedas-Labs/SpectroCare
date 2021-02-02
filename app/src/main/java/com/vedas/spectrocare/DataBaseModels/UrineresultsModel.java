package com.vedas.spectrocare.DataBaseModels;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

/**
 * Created by dell on 03-10-2017.
 */

public class UrineresultsModel {
    public UrineresultsModel() {

    }

    @DatabaseField(generatedId = true)
    int id;


    @DatabaseField(columnName = "testid")
    String test_id;

    public String getIsFasting() {
        return isFasting;
    }

    public void setIsFasting(String isFasting) {
        this.isFasting = isFasting;
    }

    @DatabaseField(columnName = "isFasting")
    String isFasting;

    @DatabaseField(columnName = "relationType")
    String relationtype;

    @DatabaseField(columnName = "testedTime")
    String testedTime;


    @DatabaseField(columnName = "patientId")
    private String patientId;

    @DatabaseField
    private String testReportNumber;


    @DatabaseField(columnName = "testtype")
    private String testType;


    @DatabaseField(columnName = "longitude")
    private String longitude;

    @DatabaseField(columnName = "latitude")
    private String latitude;

    @DatabaseField(columnName = "patient", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private PatientModel patientModel;

    @ForeignCollectionField
    private ForeignCollection<TestFactors> testFactorses;

    public ForeignCollection<TestFactors> getTestFactorses() {
        return testFactorses;
    }

    public void setTestFactorses(ForeignCollection<TestFactors> testFactorses) {
        this.testFactorses = testFactorses;
    }

    public String getTestReportNumber() {
        return testReportNumber;
    }

    public void setTestReportNumber(String testReportNumber) {
        this.testReportNumber = testReportNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTest_id() {
        return test_id;
    }

    public void setTest_id(String test_id) {
        this.test_id = test_id;
    }

    public String getRelationtype() {
        return relationtype;
    }

    public void setRelationtype(String relationtype) {
        this.relationtype = relationtype;
    }

    public String getTestedTime() {
        return testedTime;
    }

    public void setTestedTime(String testedTime) {
        this.testedTime = testedTime;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public PatientModel getPatientModel() {
        return patientModel;
    }

    public void setPatientModel(PatientModel patientModel) {
        this.patientModel = patientModel;
    }
}
