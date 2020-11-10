/*
package com.vedas.spectrocare.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApiModel.FamilyHistory;
import com.vedas.spectrocare.activities.AddMedicalRecordActivity;
import com.vedas.spectrocare.adapter.FamilyRecordAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FamilyRecords extends Fragment {
    EditText edtCondition, edtRelation, edtAge, edtDescription;
    ArrayList<FamilyHistory> familyHistoryArrayList = new ArrayList<>();
    List<String> family;
    RecyclerView familyListView;
    String discrip, ag, relation, condition;
    Button nextBn;
    JSONArray familyArray;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View familyView = inflater.inflate(R.layout_boarder.fragment_family_records, container, false);
        Button btnAdd = familyView.findViewById(R.id.add_family_record);
        nextBn = familyView.findViewById(R.id.btn_next);
        familyListView = familyView.findViewById(R.id.family_recycler_view);

        final FamilyRecordAdapter familyRecordAdapter = new FamilyRecordAdapter(getActivity(), familyHistoryArrayList);
        familyListView.setHasFixedSize(true);
        familyListView.setLayoutManager(new LinearLayoutManager(getContext()));
        familyListView.setAdapter(familyRecordAdapter);


       if (getActivity()!=null && ((AddMedicalRecordActivity)getActivity()).isUpdate){
           SharedPreferences sharedPreferences = getActivity().getSharedPreferences("recordInfo", MODE_PRIVATE);
           String recordInfoString = sharedPreferences.getString("record_Info", "");
           Log.e("usersInfo",""+recordInfoString);

           try {
               JSONObject jsonObject = new JSONObject(recordInfoString);
               JSONArray medicalRecordArray = jsonObject.getJSONArray("medical_records");
               Log.e("arrayJson",""+medicalRecordArray);
               JSONObject position = medicalRecordArray.getJSONObject(0);
               Log.e("bodyIndex",""+position);
               familyArray = position.getJSONArray("familyHistory");
               Log.e("arrayFamily",""+familyArray);
               family = new ArrayList<>();


               for (int i=0;i<familyArray.length();i++){

                   try {
                       familyHistoryArrayList.add(new FamilyHistory(familyArray.getJSONObject(i).getString("condition"),
                               familyArray.getJSONObject(i).getString("relationship"),
                               familyArray.getJSONObject(i).getString("age"),
                               familyArray.getJSONObject(i).getString("moreInfo")));

                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               }
               Log.e("chekingArray",""+familyHistoryArrayList);
           */
/* for (int i=0;i<familyArray.length();i++){
                String familyArrayDetails = familyArray.get(i).toString();
                family.add(familyArrayDetails);
            }
            familyHistoryArrayList.add((FamilyHistory) family);
            Log.e("testArray",""+familyHistoryArrayList.add((FamilyHistory) family));*//*


           } catch (JSONException e) {
               e.printStackTrace();
           }

       }



        nextBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PersonalInfoController.getInstance().currectProfileData.setFamilyHistory(familyHistoryArrayList);
                Log.e("familyListArray",""+familyHistoryArrayList);


               */
/* if (getActivity() != null)
                     ((AddMedicalRecordActivity) getActivity()).medicalRecordModel.setFamilyHistory(familyHistoryArrayList);*//*


                AddMedicalRecordActivity addMedicalRecordActivity = new AddMedicalRecordActivity();

                addMedicalRecordActivity.changer(3);


            }
        });*/
/*
        PersonalInfoController.getInstance().currectProfileData.setCondition("normal");
        PersonalInfoController.getInstance().currectProfileData.setRelationship("friend");
        PersonalInfoController.getInstance().currectProfileData.setAge("20");
        PersonalInfoController.getInstance().currectProfileData.setMoreInfo("qqqqqqqqqqqqqq");*//*



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout_boarder.family_record_alert, null);
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(view);
                edtCondition = dialog.findViewById(R.id.edt_medical_condition);
                edtRelation = dialog.findViewById(R.id.edt_relation);
                edtAge = dialog.findViewById(R.id.edt_spinner_age);
                edtDescription = dialog.findViewById(R.id.edt_add_description);
                Button addFamilyDetails = dialog.findViewById(R.id.btn_add_family_details);


                addFamilyDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        condition = edtCondition.getText().toString();
                        discrip = edtDescription.getText().toString();
                        ag = edtAge.getText().toString();
                        relation = edtRelation.getText().toString();
                        Log.e("ddjij", "" + condition);
                        Log.e("jij", "" + discrip);

                        FamilyHistory familyHistory = new FamilyHistory(condition, relation, ag, discrip);
                        familyHistoryArrayList.add(familyHistory);
                        dialog.dismiss();
                    }
                });

                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });
        // Inflate the layout_boarder for this fragment
        return familyView;


    }
}
*/
