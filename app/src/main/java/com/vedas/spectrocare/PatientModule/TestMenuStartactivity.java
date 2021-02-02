
package com.vedas.spectrocare.PatientModule;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.PatientServerApiModel.TestItemsModel;
import com.vedas.spectrocare.PatientServerApiModel.TestItemsResponseModel;
import com.vedas.spectrocare.PatientServerApiModel.TestParametersResponseModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.MedicalPersonaSignupView;
import com.vedas.spectrocare.activities.MedicalPersonalSignupPresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TestMenuStartactivity extends AppCompatActivity implements MedicalPersonaSignupView {
    ToggleButton creditBtn, paypalBtn;
    RelativeLayout rl_back;
    RecyclerView urineView;
    TextView txt_testType;
    UrineAllAdpter urineAllAdpter;
    int selectedPos = -1;
    // int selectedItemPos = -1;
    ArrayList<TestItemsModel> urineList = new ArrayList<>();
    ArrayList<TestItemsModel> bloodList = new ArrayList<>();
    ArrayList<TestItemsModel> arrayList = new ArrayList<>();
    Button btn_next;
    RefreshShowingDialog refreshShowingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_menu_startactivity);
        refreshShowingDialog = new RefreshShowingDialog(TestMenuStartactivity.this);
        loadIds();
        accessResponseFromInterface();
        if (isNetworkConnected()) {
            refreshShowingDialog.showAlert();
            fetchTestItemsApi();
        } else {
            dialogeforCheckavilability("Error", "Please check internet connection", "ok");
        }
    }
    private void loadIds() {
        creditBtn = findViewById(R.id.toggle_1);
        paypalBtn = findViewById(R.id.toggle_2);
        urineView = findViewById(R.id.recycler_view);
        txt_testType = findViewById(R.id.txt_type);
        btn_next = findViewById(R.id.btn_next);

        rl_back = findViewById(R.id.back);
        urineView.setVisibility(View.VISIBLE);
        loadRecyclerview();
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedPos>-1) {
                    startActivity(new Intent(getApplicationContext(), BleScanningActivity.class));
                }else{
                    dialogeforCheckavilability("Error", "Please select testItem", "ok");

                }
            }
        });
        creditBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                txt_testType.setVisibility(View.GONE);
                urineView.setVisibility(View.VISIBLE);
                arrayList = bloodList;
                if (isChecked) {
                    paypalBtn.setChecked(false);
                    arrayList = urineList;
                } else {
                    paypalBtn.setChecked(true);
                    //  arrayList=urineList;
                }
                urineAllAdpter.notifyDataSetChanged();
            }
        });
        paypalBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                txt_testType.setVisibility(View.GONE);
                urineView.setVisibility(View.VISIBLE);
                arrayList = urineList;
                if (isChecked) {
                    creditBtn.setChecked(false);
                    arrayList = bloodList;
                } else {
                    creditBtn.setChecked(true);
                    // arrayList=bloodList;
                }
                urineAllAdpter.notifyDataSetChanged();
            }
        });
    }
    private void loadRecyclerview() {
        urineAllAdpter = new UrineAllAdpter();
        urineView.setHasFixedSize(true);
        urineView.setLayoutManager(new LinearLayoutManager(this));
        urineView.setAdapter(urineAllAdpter);
        urineAllAdpter.notifyDataSetChanged();
    }
    private void fetchTestItemsApi() {
        JSONObject params = new JSONObject();
        try {
            params.put("byWhom", "patient");
            params.put("byWhomID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.fetchTestItemsApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "fetch");
    }
    private void accessResponseFromInterface() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                if (curdOpetaton.equals("fetch")) {
                    try {
                        if (jsonObject.getString("response").equals(String.valueOf("3"))) {
                            JSONArray recordsObj = jsonObject.getJSONArray("records");
                            if(recordsObj.length()>0) {
                                Gson gson = new Gson();
                                TestItemsResponseModel testItemsResponseModel = gson.fromJson(recordsObj.get(0).toString(), TestItemsResponseModel.class);
                                Log.e("testResponse", "call" + testItemsResponseModel.getTestItems().size());
                                ArrayList<TestItemsModel> list=testItemsResponseModel.getTestItems();
                                for(int i=0;i<list.size();i++){
                                    TestItemsModel itemsModel=list.get(i);
                                    if(itemsModel.getSpecimenType().equals("Urine")){
                                        urineList.add(itemsModel);
                                    }else{
                                        bloodList.add(itemsModel);
                                    }
                                }
                                /*txt_testType.setVisibility(View.GONE);
                                urineView.setVisibility(View.VISIBLE);*/
                                loadRecyclerview();
                            }
                            refreshShowingDialog.hideRefreshDialog();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void failureCallBack(String failureMsg) {
                refreshShowingDialog.hideRefreshDialog();
            }
        });
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter = new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(TestMenuStartactivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);
    }

    public class UrineAllAdpter extends RecyclerView.Adapter<UrineAllAdpter.Holder> {
        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View allMedical = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_testmenu_items, parent, false);
            return new Holder(allMedical);
        }
        @Override
        public void onBindViewHolder(@NonNull Holder holder, final int position) {
            TestItemsModel itemsModel = arrayList.get(position);
            holder.txt_name.setText(itemsModel.getTestName());
            holder.txt_strip_no.setText(itemsModel.getStripNo());
            holder.txt_params.setText((itemsModel.getParameters().size())+" Parameters");
            holder.specimentype.setText(itemsModel.getSpecimenType());
            holder.qty.setText(itemsModel.getSpecimenQuantity());
            holder.notes.setText(itemsModel.getCollectionNotes());
            loadTestData(holder);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedPos != position) {
                        selectedPos = position;
                        notifyDataSetChanged();
                    } else {
                        selectedPos = -1;
                        notifyDataSetChanged();
                    }
                }
            });
            if (selectedPos == position) {
                holder.rl_hiddenView.setVisibility(View.VISIBLE);
                holder.downBtn.setBackgroundResource(R.drawable.up);
                holder.relativeLayout.setBackgroundColor(Color.parseColor("#E9F9FB"));
                PatientMedicalRecordsController.getInstance().selectedTestItem=arrayList.get(selectedPos);
            } else {
                holder.rl_hiddenView.setVisibility(View.GONE);
                holder.downBtn.setBackgroundResource(R.drawable.down_1);
                holder.relativeLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        }
        @Override
        public int getItemCount() {
            if(arrayList.size()>0){
                return arrayList.size();
            }else{
                return 0;
            }
        }
        private void loadTestData(UrineAllAdpter.Holder holder){
            String testNames="";
            TestParametersResponseModel testItemsModel=null;
            if(arrayList.get(holder.getAdapterPosition()).getParameters().size()>0){
                for(int i=0;i<arrayList.get(holder.getAdapterPosition()).getParameters().size();i++){
                    testItemsModel=arrayList.get(holder.getAdapterPosition()).getParameters().get(i);
                    if(i==0){
                        testNames=testNames+""+testItemsModel.getName();
                    }else{
                        testNames=testNames+","+testItemsModel.getName();
                    }
                }
                holder.testitems.setText(testNames);
            }
        }
        public class Holder extends RecyclerView.ViewHolder {
            public RelativeLayout downrl;
            Button  downBtn;
            public TextView paramsTxt, txt_name,txt_strip_no,txt_params,testitems, specimentype ,qty ,notes;
            public CardView relativeLayout;
            RelativeLayout rl_hiddenView;

            public Holder(View itemView) {
                super(itemView);
                this.downBtn =itemView.findViewById(R.id.down);
                this.downrl =itemView.findViewById(R.id.downrl);
                this.paramsTxt = (TextView) itemView.findViewById(R.id.params);
                this.txt_name = (TextView) itemView.findViewById(R.id.txt_name);
                this.txt_strip_no = (TextView) itemView.findViewById(R.id.txt_strip_no);
                this.txt_params=itemView.findViewById(R.id.params);
                this.testitems=itemView.findViewById(R.id.testitems);
                this.specimentype=itemView.findViewById(R.id.specimentype);
                this.qty=itemView.findViewById(R.id.qty);
                this.notes=itemView.findViewById(R.id.notes);
                this.relativeLayout = (CardView) itemView.findViewById(R.id.relativeLayout);
                this.rl_hiddenView = (RelativeLayout) itemView.findViewById(R.id.rl_hiddenview);
            }
        }
    }
}
