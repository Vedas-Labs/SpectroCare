package com.vedas.spectrocare.DataBase;

import com.vedas.spectrocare.DataBaseModels.TestFactors;
import com.vedas.spectrocare.DataBaseModels.UrineresultsModel;

import java.sql.SQLException;
import java.util.ArrayList;


public class TestFactorDataController {

    public ArrayList<TestFactors> testfactorlist ;
    public static TestFactorDataController myObj;

    public static TestFactorDataController getInstance() {
        if (myObj == null) {
            myObj = new TestFactorDataController();
        }
        return myObj;
    }

    //Inserting tests data
    public boolean insertTestFactorResults(TestFactors testFactors) {
        try {
            MedicalProfileDataController.getInstance().helper.getTestFactorsesDao().create(testFactors);
            fetchTestFactorresults(UrineResultsDataController.getInstance().currenturineresultsModel);
            return  true;
        } catch (SQLException e) {
            e.printStackTrace();
            return  false;
        }
    }

    /*public ArrayList<TestFactors> fetchTestFactorresults() {
        testfactorlist=new ArrayList<>();
        if (UrineResultsDataController.getInstance().currenturineresultsModel != null) {
            ArrayList<TestFactors> test = new ArrayList<TestFactors>(UrineResultsDataController.getInstance().currenturineresultsModel.getTestFactorses());
            if (test != null) {
                testfactorlist = test;
            }
        }
        return testfactorlist;
    }*/

    public ArrayList<TestFactors> fetchTestFactorresults(UrineresultsModel urineObjectModel) {
        testfactorlist=new ArrayList<>();
        if (urineObjectModel != null) {
            ArrayList<TestFactors> test = new ArrayList<TestFactors>(urineObjectModel.getTestFactorses());
            if (test != null) {
                testfactorlist = test;
            }
        }
        return testfactorlist;
    }
}
