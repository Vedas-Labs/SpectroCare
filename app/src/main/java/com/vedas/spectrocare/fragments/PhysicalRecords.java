package com.vedas.spectrocare.fragments;

import android.app.Dialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApiModel.PhysicalRecordServerObject;
import com.vedas.spectrocare.activities.AddMedicalRecordActivity;
import com.vedas.spectrocare.activities.PhysicalExamRecordActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class PhysicalRecords extends Fragment {
    RecyclerView physicalRecordView;
    PhysicalExamRecordActivity.PhysicalRecordAdapter recordAdapter;
    ArrayList<PhysicalRecordServerObject> recordList=new ArrayList<>();
    Button addTitle, nextBtn;
    EditText edtTitle;
    String title = null;
    View recordView;
    PhysicalRecordServerObject physicalRecordModel1,physicalRecordModel2,physicalRecordModel3;
    JSONArray physicalArray;
    TextView txt;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recordView = inflater.inflate(R.layout.fragment_physical_records, container, false);
        addTitle = recordView.findViewById(R.id.add_physical_record);
        txt = recordView.findViewById(R.id.text_description);
        nextBtn = recordView.findViewById(R.id.btn_next);

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
                physicalArray = position.getJSONArray("physicalExamination");
                Log.e("arrayPhysical",""+physicalArray);

                for (int i=0;i<physicalArray.length();i++){

                    try {
                        recordList.add(new PhysicalRecordServerObject(physicalArray.getJSONObject(i).getString("category"),
                                physicalArray.getJSONObject(i).getString("result"),
                                physicalArray.getJSONObject(i).getString("description")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



      /*  physicalRecordModel1 = new PhysicalRecordServerObject("Category1","Normal","No need to worry");
        physicalRecordModel2 = new PhysicalRecordServerObject("Category2","Abnormal","follow diet");
        physicalRecordModel3 = new PhysicalRecordServerObject("Category3","Not examined","Need to examine");



        recordList = new ArrayList();
        recordList.add(physicalRecordModel1);
        recordList.add(physicalRecordModel2);
        recordList.add(physicalRecordModel3);*/
        recyclerItems();


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*




                physicalRecordModel1 = new PhysicalRecordServerObject("Category1","Normal","No need to worry");
                physicalRecordModel2 = new PhysicalRecordServerObject("Category2","Abnormal","follow diet");
                physicalRecordModel3 = new PhysicalRecordServerObject("Category3","Not examined","Need to examine");



                recordList = new ArrayList();
                recordList.add(physicalRecordModel1);
                recordList.add(physicalRecordModel2);
                recordList.add(physicalRecordModel3);

*/


                PersonalInfoController.getInstance().currectProfileData.setPhysicalExamination(recordList);
                Log.e("physicalRecordList",""+recordList.toString());
                AddMedicalRecordActivity addMedicalRecordActivity=new AddMedicalRecordActivity();

                addMedicalRecordActivity.changer(2);



             /*   PersonalInfoController.getInstance().currectProfileData.setDescription("aasdnsajasj");
                PersonalInfoController.getInstance().currectProfileData.setCategory("sjdnasjsaj");
                PersonalInfoController.getInstance().currectProfileData.setResult("asdsasassa");
*/
            }
        });




        addTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setCancelable(true);

                View viewAlert = getActivity().getLayoutInflater().inflate(R.layout.physical_record_alert, null);
                dialog.setContentView(viewAlert);
                edtTitle = dialog.findViewById(R.id.edt_title);
                Button btnAdd = dialog.findViewById(R.id.add_title);
                Button btnnnn = viewAlert.findViewById(R.id.btn_no_thanks);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        title = edtTitle.getText().toString();
                        if (!title.isEmpty()) {
                            txt.setVisibility(View.GONE);
                            PhysicalRecordServerObject physicalExamination = new PhysicalRecordServerObject(title,"","");
                            recordList.add(physicalExamination);

                        }
                        else{
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.toast_layout,
                                    (ViewGroup) recordView.findViewById(R.id.custom_toast_container));

                            TextView text = (TextView) layout.findViewById(R.id.text);
                            text.setText("Category is empty");
                            Toast toast = new Toast(getContext());
                            //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                            dialog.dismiss();
                        }
                            //Toast.makeText(getActivity(), "Category name is empty", Toast.LENGTH_SHORT).show();
                        //  Log.e("stringTest",""+title);
                        dialog.dismiss();
                    }
                });
                btnnnn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });
        Log.e("stringTest", "" + title);
        if (title != null) {
            recyclerItems();
        } else {

            txt.setVisibility(View.VISIBLE);
        }

        return recordView;

    }

    public void recyclerItems() {

     //   recordAdapter = PhysicalExamRecordActivity.PhysicalRecordAdapter(getContext());
        physicalRecordView = recordView.findViewById(R.id.recycler_view);
        physicalRecordView.setHasFixedSize(true);
        physicalRecordView.setLayoutManager(new LinearLayoutManager(getContext()));
        physicalRecordView.setAdapter(recordAdapter);
        // return recordView;
    }
}
