package com.vedas.spectrocare.patient_fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBase.TestFactorDataController;
import com.vedas.spectrocare.DataBase.UrineResultsDataController;
import com.vedas.spectrocare.DataBaseModels.UrineresultsModel;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.patientModuleAdapter.AllTestRecordAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllTestRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllTestRecordFragment extends Fragment {
    public  AllTestRecordAdapter allTestRecordAdapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RefreshShowingDialog refreshShowingDialog;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllTestRecordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllTestRecordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllTestRecordFragment newInstance(String param1, String param2) {
        AllTestRecordFragment fragment = new AllTestRecordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewAll = inflater.inflate(R.layout.fragment_all_test_record, container, false);
        RecyclerView allRecycleTests = viewAll.findViewById(R.id.all_recycle_tests);
        refreshShowingDialog=new RefreshShowingDialog(getContext());
       /* accessInterfaceMethods();
        if(PatientMedicalRecordsController.getInstance().isFromLogin){
            if(isConn()){
                PatientMedicalRecordsController.getInstance().isFromLogin=false;
                refreshShowingDialog.showAlert();
                fetchingUrineResultApi();
            }
        }*/
        allRecycleTests.setLayoutManager(new LinearLayoutManager(getContext()));
        allTestRecordAdapter = new AllTestRecordAdapter(getContext(),refreshShowingDialog);
        allRecycleTests.setAdapter(allTestRecordAdapter);
        // allTestRecordAdapter.notifyDataSetChanged();

        return viewAll;
    }
    public void fetchingUrineResultApi() {
        JSONObject fetchObject = new JSONObject();
        try {
            fetchObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
            fetchObject.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            fetchObject.put("byWhomID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            fetchObject.put("byWhom","patient");
            fetchObject.put("medical_record_id", PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(fetchObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.
                fetchingUrineResultApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "fetch");
    }
    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                if (curdOpetaton.equals("fetch")) {
                    try {
                        if (jsonObject.getString("response").equals("3")) {
                            JSONArray testResults = jsonObject.getJSONArray("testResults");//testFactors
                            for(int i=0;i<testResults.length();i++){
                                refreshShowingDialog.hideRefreshDialog();
                                Log.e("sddsddsdsd","call");
                                JSONObject items= (JSONObject) testResults.get(i);

                                UrineresultsModel objResult=new UrineresultsModel();
                                objResult.setTestReportNumber(items.getString("testReportNumber"));
                                objResult.setTestType("Urine");
                                objResult.setLatitude("0.0");
                                objResult.setLongitude("0.0");
                                objResult.setTestedTime(items.getString("testedTime"));
                                objResult.setIsFasting(String.valueOf(items.getBoolean("isFasting")));
                                objResult.setRelationtype("Patient");
                                objResult.setTest_id(items.getString("testID"));
                                objResult.setPatientId(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
                                objResult.setPatientModel(PatientLoginDataController.getInstance().currentPatientlProfile);

                                if (UrineResultsDataController.getInstance().insertUrineResultsForMember(objResult)) {
                                    JSONArray itemsArray=items.getJSONArray("testFactors");
                                    for(int k=0;k<itemsArray.length();k++){
                                        JSONObject obj=itemsArray.getJSONObject(k);
                                        com.vedas.spectrocare.DataBaseModels.TestFactors objTest = new com.vedas.spectrocare.DataBaseModels.TestFactors();
                                        objTest.setFlag(obj.getBoolean("flag"));
                                        objTest.setUnit(obj.getString("unit"));
                                        objTest.setHealthReferenceRanges(obj.getString("healthReferenceRanges"));
                                        objTest.setTestName(obj.getString("testName"));
                                        objTest.setResult(obj.getString("result"));
                                        objTest.setValue(obj.getString("value"));
                                        objTest.setUrineresultsModel(UrineResultsDataController.getInstance().currenturineresultsModel);
                                        if (TestFactorDataController.getInstance().insertTestFactorResults(objTest)) {

                                        }
                                    }
                                }
                            }
                            // refreshShowingDialog.hideRefreshDialog();
                        }else{
                            // refreshShowingDialog.hideRefreshDialog();
                            Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void failureCallBack(String failureMsg) {
                refreshShowingDialog.hideRefreshDialog();
                Toast.makeText(getContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean isConn() {
        ConnectivityManager connectivity = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
    }
}
