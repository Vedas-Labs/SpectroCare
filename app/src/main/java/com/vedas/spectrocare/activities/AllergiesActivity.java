package com.vedas.spectrocare.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.AllergiesServerObjectDataController;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.DataBase.AllergyDataController;
import com.vedas.spectrocare.DataBase.AllergyTrackInfoDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBaseModels.AllergieModel;
import com.vedas.spectrocare.DataBaseModels.AllergyTrackInfoModel;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApiModel.AllergyServerObject;
import com.vedas.spectrocare.ServerApiModel.TrackingServerObject;
import com.vedas.spectrocare.SingleTapDetector;
import com.vedas.spectrocare.adapter.RecordRecyclerAdapter;
import com.vedas.spectrocare.model.RecordModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AllergiesActivity extends AppCompatActivity implements MedicalPersonaSignupView {
    FloatingActionButton allergyBtn;
    RecyclerView allergyRecyclerView,recordRecyclerView;
    LinearLayout saveBtnLayout,cancelBtnLayout;
    EditText aboutAllergy,allergyName;
    Button btnRecordEdit,btnRecordSave,btnRecordCancel,btnRecordSaveChanges;
    Dialog dialog;
    Button btnAddAllergy,btnSaveRecords;
    TextView ttile;
    EditText edtAllergyName,edtAllergyDisc;
    ImageView imgEdt,imgRecordAdd;
    RefreshShowingDialog showingDialog;
    AllergyRecordAdapter recordAdapter;
    JSONObject params;
    JSONObject object;
    private  float dX,dY;
    TextView tt;
    int lastAction;
    ArrayList<AllergieModel> allergieModels = new ArrayList<>();
    List<RecordModel> recordModels;
    List<RecordModel> recordList;
    ArrayList<Integer> colorsArray = new ArrayList<>();
    int poss;
    int selectdPos=-1;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergies);
        dialog = new Dialog(AllergiesActivity.this);
        allergyBtn = findViewById(R.id.add_allergy_record);
        btnSaveRecords = findViewById(R.id.btn_save_records);
        final GestureDetector gestureDetector = new GestureDetector(this, new SingleTapDetector());
        allergyRecyclerView = findViewById(R.id.allergy_recycler_view);
        RelativeLayout imgBackBtn = findViewById(R.id.img_back);
        tt=findViewById(R.id.text_medical_disc);
        showingDialog = new RefreshShowingDialog(AllergiesActivity.this);
        imgBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        PatientProfileDataController.getInstance().fetchPatientlProfileData();
       // accessInterfaceMethods();
        accessAllargyInterfaceMethods();
        if (isConn()) {
            showingDialog.showAlert();
           // AllergiesServerObjectDataController.getInstance().allergyFetchApiCall();
            AllergiesServerObjectDataController.getInstance().allergyFetchApiCall();

        }
        loadRecyclerview();

        allergyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllergyDataController.getInstance().currentAllergieModel = null;
               // alertDailog();
                alertRecordDailog();
            }
        });
        btnSaveRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray array = new JSONArray();
                // array.put(recordModel);
                Log.e("adfasfs","adfasffaf");
                Log.e("recordList", "" + recordList.size());
                for (int i = 0; i < recordList.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("name", recordList.get(i).getCondition());
                        jsonObject.put("note", recordList.get(i).getRelation());
                        array.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } addAllergyRecord(array);

            }
        });
        allergyBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    AllergyDataController.getInstance().currentAllergieModel = null;
                  //  alertDailog();
                    alertRecordDailog();

                }
                else{
                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            dX = v.getX() - event.getRawX();
                            dY = v.getY() - event.getRawY();
                            lastAction = MotionEvent.ACTION_DOWN;
                            break;

                        case MotionEvent.ACTION_MOVE:
                            v.setY(event.getRawY() + dY);
                            v.setX(event.getRawX() + dX);
                            lastAction = MotionEvent.ACTION_MOVE;
                            break;

                        case MotionEvent.ACTION_UP:
                            if (lastAction == MotionEvent.ACTION_DOWN)
                                // Toast.makeText(DraggableView.this, "Clicked!", Toast.LENGTH_SHORT).show();
                                break;

                        default:
                            return false;
                    }

                }



                return true;
            }
        });

    }

    private void loadRecyclerview() {
        recordList = new ArrayList<>();
        recordAdapter = new AllergyRecordAdapter(recordList,AllergiesActivity.this);
        allergyRecyclerView.setHasFixedSize(true);
        allergyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        allergyRecyclerView.setAdapter(recordAdapter);
        recordAdapter.notifyDataSetChanged();
    }

/*
    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                if (curdOpetaton.equals("fetch")) {
                    Log.e("allergies", "call" + jsonObject.toString());
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("allergy_records");
                        AllergyTrackInfoDataController.getInstance().deleteTrackData(AllergyDataController.getInstance().allAllergyList);
                        AllergyDataController.getInstance().deleteAllergyData(PatientProfileDataController.getInstance().currentPatientlProfile);
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Gson gson = new Gson();
                                AllergyServerObject userIdentifier = gson.fromJson(jsonArray.getJSONObject(i).toString(), AllergyServerObject.class);
                                ArrayList<TrackingServerObject> trackArray = userIdentifier.getTracking();
                                Log.e("trackArray", "call" + trackArray.size());
                                AllergiesServerObjectDataController.getInstance().processAndfetchAllegryData(userIdentifier, trackArray);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (curdOpetaton.equals("insert")) {
                    Gson gson = new Gson();
                 //   AllergyServerObject userIdentifier = gson.fromJson(params.toString(), AllergyServerObject.class);
                    AllergyServerObject userIdentifier = gson.fromJson(object.toString(), AllergyServerObject.class);

                    try {
                        userIdentifier.setAllergy_record_id(jsonObject.getString("allergy_record_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AllergiesServerObjectDataController.getInstance().processAddAllergyData(userIdentifier);
                    dialog.dismiss();
                } else if (curdOpetaton.equals("update")) {
                    Gson gson = new Gson();
                    AllergyServerObject userIdentifier = gson.fromJson(params.toString(), AllergyServerObject.class);
                    AllergiesServerObjectDataController.getInstance().processAllergyupdateData(userIdentifier);
                    dialog.dismiss();
                } else if (curdOpetaton.equals("delete")) {
                    AllergyDataController.getInstance().deleteAllergieModelData(PatientProfileDataController.getInstance().currentPatientlProfile, AllergyDataController.getInstance().currentAllergieModel);
                }
                showingDialog.hideRefreshDialog();
                loadRecyclerview();
            }

            @Override
            public void failureCallBack(String failureMsg) {
                showingDialog.hideRefreshDialog();
                Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
*/
private void accessAllargyInterfaceMethods() {
    ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
        @Override
        public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
            Log.e("chchc", "ERE");
            if (curdOpetaton.equals("fetchAllergy")) {
                Log.e("allergies", "call" + jsonObject.toString());
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("allergy_records");
                    Log.e("afdasa", "" + jsonArray.toString());
                    AllergyTrackInfoDataController.getInstance().deleteTrackData(AllergyDataController.getInstance().allAllergyList);
                    AllergyDataController.getInstance().deleteAllergyData(PatientProfileDataController.getInstance().currentPatientlProfile);
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Gson gson = new Gson();
                            AllergyServerObject userIdentifier = gson.fromJson(jsonArray.getJSONObject(i).toString(), AllergyServerObject.class);
                            ArrayList<TrackingServerObject> trackArray = userIdentifier.getTracking();
                            Log.e("trackArray", "call" + trackArray.size());
                            AllergiesServerObjectDataController.getInstance().processAndfetchAllegryData(userIdentifier, trackArray);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (curdOpetaton.equals("insertAllergy")) {
                Gson gson = new Gson();
                AllergyServerObject userIdentifier = gson.fromJson(params.toString(), AllergyServerObject.class);
                // AllergyServerObject userIdentifier = gson.fromJson(object.toString(), AllergyServerObject.class);
                try {
                    Log.e("familydata", "call" + jsonObject.getString("allergy_record_id"));
                    userIdentifier.setAllergy_record_id(jsonObject.getString("allergy_record_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AllergiesServerObjectDataController.getInstance().processAddAllergyData(userIdentifier);
                dialog.dismiss();
            } else if (curdOpetaton.equals("updateAllergy")) {
                Gson gson = new Gson();
                AllergyServerObject userIdentifier = gson.fromJson(params.toString(), AllergyServerObject.class);
                AllergiesServerObjectDataController.getInstance().processAllergyupdateData(userIdentifier);
                dialog.dismiss();
            } else if (curdOpetaton.equals("deleteAllergy")) {
                AllergyDataController.getInstance().deleteAllergieModelData(PatientProfileDataController.getInstance().currentPatientlProfile, AllergyDataController.getInstance().currentAllergieModel);
            }
            showingDialog.hideRefreshDialog();

            recordList.clear();
            AllergyDataController.getInstance().fetchAllergyData(PatientProfileDataController.getInstance().currentPatientlProfile);
            allergieModels = AllergyDataController.getInstance().allAllergyList;
            for (int i = 0; i < allergieModels.size(); i++) {
                Log.e("recordOfAllergies", "" + allergieModels.get(i).getName());
                recordList.add(new RecordModel(allergieModels.get(i).getName(), allergieModels.get(i).getNote()));
            }
            recordAdapter.notifyDataSetChanged();
        }

        @Override
        public void failureCallBack(String failureMsg) {
            showingDialog.hideRefreshDialog();
            Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
        }
    });
}


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void alertDailog() {
       /* LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.allergy_record_alert, null);
        dialog.setContentView(view);
        dialog.show();*/

        View view = getLayoutInflater().inflate(R.layout.allergy_record_alert, null);
        dialog = new BottomSheetDialog(Objects.requireNonNull(AllergiesActivity.this),R.style.BottomSheetDialogTheme);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();

       /* Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);*/
        aboutAllergy = dialog.findViewById(R.id.edt_add_description);

        ImageView imageView=dialog.findViewById(R.id.cancel);
        imgEdt = dialog.findViewById(R.id.img_edit);
        allergyName = dialog.findViewById(R.id.edt_allergy_name);
        ttile=dialog.findViewById(R.id.title);
        imgEdt.setVisibility(View.GONE);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnAddAllergy = dialog.findViewById(R.id.btn_add_allergy_details);
        if (AllergyDataController.getInstance().currentAllergieModel != null) {
           aboutAllergy.setText(AllergyDataController.getInstance().currentAllergieModel.getAllergyInfo());
            aboutAllergy.setSelection(aboutAllergy.getText().toString().length());
            btnAddAllergy.setVisibility(View.GONE);
            imgEdt.setVisibility(View.VISIBLE);
            ttile.setText("Allergy record");
            aboutAllergy.setEnabled(false);
            aboutAllergy.setTextColor(Color.parseColor("#a4b3b7"));
           /* btnAddAllergy.setText("Update");
            ttile.setText("Update allergy record");
       */ }
        imgEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddAllergy.setVisibility(View.VISIBLE);
                aboutAllergy.setEnabled(true);
                imgEdt.setVisibility(View.GONE);
                aboutAllergy.setTextColor(Color.parseColor("#3E454C"));
                btnAddAllergy.setText("Update");
                ttile.setText("Update allergy record");

            }
        });
        btnAddAllergy.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
               validation();
            }
        });
    }



    public void alertRecordDailog() {

       // recordList.clear();
        View view = getLayoutInflater().inflate(R.layout.allergy_record_alert, null);
      //  RelativeLayout addListLayout = view.findViewById(R.id.add_list_layout);
       /* imgRecordAdd = view.findViewById(R.id.img_add);
        btnRecordEdit = view.findViewById(R.id.btn_record_edit);*/
        btnRecordSave = view.findViewById(R.id.btn_add_allergy_details);
       /* saveBtnLayout =view.findViewById(R.id.btn_save_layout);
        cancelBtnLayout = view.findViewById(R.id.btn_cancel_layout);
        btnRecordCancel = view.findViewById(R.id.btn_record_cancel);
        cancelBtnLayout.setVisibility(View.GONE);
*/        edtAllergyName = view.findViewById(R.id.edt_allergy_name);
        edtAllergyDisc = view.findViewById(R.id.edt_add_description);


       /* final RecordModel recordModel = new RecordModel();
        RecordModel recordModel1 = new RecordModel();
        recordModel.setsNo("1");
        recordModel.setCondition("Carona");
        recordModel.setRelation("Wife");
        recordModel1.setsNo("2");
        recordModel1.setRelation("Husbend");
        recordModel1.setCondition("carona");
        recordList.add(recordModel);
        recordList.add(recordModel1);
       */ dialog = new BottomSheetDialog(Objects.requireNonNull(AllergiesActivity.this),R.style.BottomSheetDialogTheme);
       // recordRecyclerView = view.findViewById(R.id.record_recyclerview);
        //recyclerView(recordList);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();

       /* addListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertRecordDialog();

            }
        });*/
        btnRecordSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                Log.e("leaver","asdf");
            validation();
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void validation() {
        if (edtAllergyName.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please fill allergy name", "Ok");
        } else if(edtAllergyDisc.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please fill allergy details", "Ok");

        }else {
            RecordModel model = new RecordModel();
            model.setCondition(edtAllergyName.getText().toString());
            model.setRelation(edtAllergyDisc.getText().toString());
            if (btnRecordSave.getText().toString().equals("Add")){
                Log.e("check","chck");
                recordList.add(model);
            }else{
                recordModels.set(poss,model);
            }
            recordAdapter.notifyDataSetChanged();
            dialog.dismiss();
        }
    }
    //fetch allergy record API
    public void fetchAllergyRecordData(){
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        object = new JSONObject();
        JsonObject finalObject = new JsonObject();
        try {
            object.put("hospital_reg_num",currentMedical.getHospital_reg_number());
            object.put("byWhom","medical personnel");
            object.put("byWhomID",PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalPerson_id());
            object.put("patientID", PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
            object.put("medical_record_id", PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(object.toString());

    }
    //add allergy record API
    public void addAllergyRecord(JSONArray list){
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        object = new JSONObject();
        JsonObject finalObject = new JsonObject();

        Log.e("dfasdf","notcoming");

        try {
            object.put("hospital_reg_num",currentMedical.getHospital_reg_number());
            object.put("patientID", PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
            object.put("medical_record_id", PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
            object.put("byWhom","medical personnel");
            object.put("byWhomID",PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalPerson_id());
            object.put("allergies",list);
            Log.e("list",""+list);
            Log.e("accessToken",""+currentMedical.getAccessToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("objecttt",""+object);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(object.toString());

        if (!(list.length() == 0)) {
            showingDialog.hideRefreshDialog();
            ApiCallDataController.getInstance().loadServerApiCall(
                    ApiCallDataController.getInstance().serverApi.addMultipleAllergyRecords(currentMedical.getAccessToken(), gsonObject), "insert");
        }else{
            Log.e("resulteee","ddaaaa");
            showingDialog.hideRefreshDialog();
            ApiCallDataController.getInstance().loadServerApiCall(
                    ApiCallDataController.getInstance().serverApi.addMultipleAllergyRecords(currentMedical.getAccessToken(), gsonObject), "insert");
        }
        }


    public class AllergyRecordAdapter extends RecyclerView.Adapter<AllergyRecordAdapter.AllergyRecordHolder> {
        Context context;

       /* public AllergyRecordAdapter(Context context) {
            this.context = context;
        }*/
        public AllergyRecordAdapter(List<RecordModel> mainRecordModels, Context context) {

            Log.e("fjbjk", "kfnjnjf");
            recordModels = mainRecordModels;
            this.context = context;
        }

        @Override
        public AllergyRecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View allergyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_allergy_record, parent, false);
            return new AllergyRecordHolder(allergyView);
        }

        @Override
        public void onBindViewHolder(@NonNull final AllergyRecordHolder holder, final int position) {

           // AllergieModel allergieModel = AllergyDataController.getInstance().allAllergyList.get(position);
            int a = position + 1;

           // holder.allergyDescription.setText(allergieModel.getAllergyInfo());
            holder.txtCondition.setText(recordModels.get(position).getCondition());
            Log.e("texting","empty"+recordModels.get(position).getCondition());
            holder.txtRelation.setText(recordModels.get(position).getRelation());
            holder.txtSno.setText(String.valueOf(a));
            holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeAt(position);
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    poss=holder.getAdapterPosition();
                    alertRecordDailog();
                    btnRecordSave.setText("Update");
                    btnSaveRecords.setText("Save changes");
                    edtAllergyName.setText(recordModels.get(position).getCondition());
                    edtAllergyDisc.setText(recordModels.get(position).getRelation());
                }
            });
         //   holder.colorLayout.setBackgroundColor(colorsArray.get(position));

          /*  ArrayList<AllergyTrackInfoModel> trackInfoModels = AllergyTrackInfoDataController.getInstance().fetchTrackData(allergieModel);
            if (trackInfoModels.size() > 0) {
                try {
                    AllergyTrackInfoModel trackInfoModel = trackInfoModels.get(trackInfoModels.size() - 1);
                    String date[] = PersonalInfoController.getInstance().convertTimestampToslashFormate(trackInfoModel.getDate());
                    String sourceString = "Created by " + "<b>" + trackInfoModel.getByWhom() + "</b> " + "on " *//*+"\n"+"(Id: "+trackInfoModel.getByWhomId()+")"*//*;
                    holder.createdDate.setText(Html.fromHtml(sourceString));
                    holder.txt_time.setText( date[0] +" "+date[1] + " " + date[2]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
*/            holder.viewImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AllergyDataController.getInstance().currentAllergieModel = AllergyDataController.getInstance().allAllergyList.get(position);
                    alertDailog();
                }
            });
            holder.editImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AllergyDataController.getInstance().currentAllergieModel = AllergyDataController.getInstance().allAllergyList.get(position);
                    alertDailog();
                }
            });
            holder.deleteImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context wrapper = new ContextThemeWrapper(AllergiesActivity.this, R.style.MyPopupOtherStyle);
                    PopupMenu popup = new PopupMenu(wrapper,  holder.more);
                    popup.getMenuInflater().inflate(R.menu.popup_menu,popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getTitle().equals("View")){
                                AllergyDataController.getInstance().currentAllergieModel = AllergyDataController.getInstance().allAllergyList.get(position);
                                alertDailog();
                            }else{
                                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                                builder.setCancelable(false);
                                builder.setTitle("Delete");
                                builder.setMessage("Are you sure you want to delete");

                                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AllergyDataController.getInstance().currentAllergieModel = AllergyDataController.getInstance().allAllergyList.get(position);
                                        AllergieModel objModel = AllergyDataController.getInstance().allAllergyList.get(position);
                                        JSONObject params = new JSONObject();
                                        try {
                                            params.put("hospital_reg_num", objModel.getHospitalRegNum());
                                            params.put("medical_personnel_id", objModel.getMedicalPersonId());
                                            params.put("patientID", objModel.getPatientId());
                                            params.put("medical_record_id", objModel.getMedicalRecordId());
                                            params.put("allergy_record_id", objModel.getAllergyRecordId());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        JsonParser jsonParser = new JsonParser();
                                        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
                                        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
                                        if (isConn()) {
                                            showingDialog.showAlert();
                                            ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.deleteAllergyHistory(
                                                    MedicalProfileDataController.getInstance().currentMedicalProfile.getAccessToken(), gsonObject), "delete");

                                        }
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.create().show();

                            }
                                return true;
                        }
                    });
                    popup.show();
                  /*  if (selectdPos != position) {
                        selectdPos = position;
                    } else {
                        selectdPos = -1;
                    }*/
                    notifyDataSetChanged();
                }
            });
          }

        @Override
        public int getItemCount() {
            if (recordList.size() > 0) {
                return recordList.size();
            } else {
                Log.e("kuchNahi","haiii");
                return 0;
            }
        }
        public void removeAt(int position) {
            recordModels.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, recordModels.size());
        }

        public class AllergyRecordHolder extends RecyclerView.ViewHolder {
            TextView allergyDescription, createdDate,txt_time,txtSno;
            ImageView viewImg,editImg,deleteImg,imgDelete;
            RelativeLayout colorLayout,pop_up_layout,more;
            EditText txtCondition, txtRelation;

            TextView txtDelete,txtView;

            public AllergyRecordHolder(@NonNull View itemView) {
                super(itemView);

                    txtCondition = itemView.findViewById(R.id.txt_item_condition);
                    txtRelation = itemView.findViewById(R.id.txt_item_relation);
                    txtSno = itemView.findViewById(R.id.txt_sno);
                    imgDelete = itemView.findViewById(R.id.img_record_delete);

                    allergyDescription = itemView.findViewById(R.id.txt_allergy_description);
                createdDate = itemView.findViewById(R.id.txt_created_date);
                txt_time = itemView.findViewById(R.id.txt_date);
                colorLayout = itemView.findViewById(R.id.color);
                more = itemView.findViewById(R.id.more_img);
                viewImg = itemView.findViewById(R.id.img_view);
                deleteImg = itemView.findViewById(R.id.img_delete);
                editImg = itemView.findViewById(R.id.img_edit);
                pop_up_layout = itemView.findViewById(R.id.pop_up_layout);
                txtView = itemView.findViewById(R.id.txt_view);
                txtDelete = itemView.findViewById(R.id.txt_delete);

            }
        }
    }

    public boolean isConn() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
    }

    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter = new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AllergiesActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);

    }
    public void processJsonParams(String aboutAllergy){
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        params = new JSONObject();
        try {
            params.put("hospital_reg_num", currentMedical.getHospital_reg_number());
            params.put("medical_personnel_id", currentMedical.getMedical_person_id());
            params.put("medical_record_id", PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
            params.put("patientID", PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("allergyInformation", aboutAllergy);
            if (AllergyDataController.getInstance().currentAllergieModel != null) {
                params.put("allergy_record_id", AllergyDataController.getInstance().currentAllergieModel.getAllergyRecordId());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        if (AllergyDataController.getInstance().currentAllergieModel != null) {

            ApiCallDataController.getInstance().loadServerApiCall(
                    ApiCallDataController.getInstance().serverApi.updateAllergyHistory(currentMedical.getAccessToken(), gsonObject), "update");

        } else {
            ApiCallDataController.getInstance().loadServerApiCall(
                    ApiCallDataController.getInstance().serverApi.addAllergy(currentMedical.getAccessToken(), gsonObject), "insert");

        }
    }












/*
    public class RecordRecyclerAdapter extends RecyclerView.Adapter<RecordRecyclerAdapter.RecordHolder> {
       // ArrayList<RecordModel> recordModels =new ArrayList<>();
        Context context;
        boolean deleteImg=false;

        public RecordRecyclerAdapter(ArrayList<RecordModel> recordModels, Context context) {
            this.recordModels = recordModels;
            this.context = context;
        }

        @NonNull
        @Override
        public RecordRecyclerAdapter.RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View recordView= LayoutInflater.isFromMedicalPerson(parent.getContext()).inflate(R.layout.record_recycler_item,parent,false);
            return new RecordRecyclerAdapter.RecordHolder(recordView);

        }

        @Override
        public void onBindViewHolder(@NonNull final RecordRecyclerAdapter.RecordHolder holder, int position) {
            int a = position+1;
            if (deleteImg==true){
                holder.imgDelete.setVisibility(View.VISIBLE);
            }else{
                holder.imgDelete.setVisibility(View.GONE);}
            holder.txtSno.setText( String.valueOf(a));
            holder.txtCondition.setText(recordModels.get(position).getCondition());
            holder.txtRelation.setText(recordModels.get(position).getRelation());

           */
/* btnRecordEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveBtnLayout.setVisibility(View.GONE);
                    cancelBtnLayout.setVisibility(View.VISIBLE);
                    btnRecordEdit.setBackground(AllergiesActivity.this.getResources().getDrawable(R.drawable.btn_bck_color));
                    btnRecordEdit.setTextColor(Color.parseColor("#ffffff"));
                    btnRecordSave.setTextColor(Color.parseColor("#3E454C"));
                    btnRecordSave.setBackground(AllergiesActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));

                    deleteImg = true;
                    notifyDataSetChanged();
                }
            });
            btnRecordCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    deleteImg = false;
                    notifyDataSetChanged();

                    holder.imgDelete.setVisibility(View.GONE);
                    cancelBtnLayout.setVisibility(View.GONE);
                    saveBtnLayout.setVisibility(View.VISIBLE);
                    btnRecordCancel.setBackground(AllergiesActivity.this.getResources().getDrawable(R.drawable.btn_bck_color));
                    btnRecordCancel.setTextColor(Color.parseColor("#ffffff"));

                }
            });
*//*


        }

        @Override
        public int getItemCount() {
            return recordModels.size();
        }

        public class RecordHolder extends RecyclerView.ViewHolder {
            TextView txtSno;
            EditText txtCondition, txtRelation;
            ImageView imgDelete;
            public RecordHolder(@NonNull View itemView) {
                super(itemView);
                txtCondition = itemView.findViewById(R.id.txt_item_condition);
                txtRelation = itemView.findViewById(R.id.txt_item_relation);
                txtSno = itemView.findViewById(R.id.txt_sno);
                imgDelete = itemView.findViewById(R.id.img_record_delete);


            }
        }
    }
*/












}
