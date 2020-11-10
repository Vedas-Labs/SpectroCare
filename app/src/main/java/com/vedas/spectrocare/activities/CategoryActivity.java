/*
package com.vedas.spectrocare.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.AllergiesServerObjectDataController;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.Controllers.FamilyRecordServerDataController;
import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.Controllers.PhysicalServerObjectDataController;
import com.vedas.spectrocare.DataBase.AllergyDataController;
import com.vedas.spectrocare.DataBase.AllergyTrackInfoDataController;
import com.vedas.spectrocare.DataBase.FamilyHistoryDataController;
import com.vedas.spectrocare.DataBase.FamilyTrackInfoDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBaseModels.AllergieModel;
import com.vedas.spectrocare.DataBaseModels.FamilyHistoryModel;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.DataBaseModels.PatientlProfileModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApiModel.AllergyServerObject;
import com.vedas.spectrocare.ServerApiModel.FamilyRecordServerObject;
import com.vedas.spectrocare.ServerApiModel.TrackingServerObject;
import com.vedas.spectrocare.adapter.CategoryRecyclerAdapter;
import com.vedas.spectrocare.adapter.RecordRecyclerAdapter;
import com.vedas.spectrocare.model.CategoryItemModel;
import com.vedas.spectrocare.model.RecordModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryActivity extends AppCompatActivity {
    ArrayList categoryList;
    ImageView imgRecordAdd,imgDialogCancle;
    TextView condition1, relation1;
    Dialog dialog;
    RecyclerView recordRecyclerView;
    Button btnRecordEdit, btnRecordSave, btnRecordCancel, btnRecordSaveChanges;
    JSONObject params = new JSONObject();
    JSONObject object;
    ArrayList<FamilyHistoryModel> familyHistoryArrayList = new ArrayList<>();
    List<RecordModel> recordList;
    ArrayList<AllergieModel> allergieModels =new ArrayList<>();
    LinearLayout saveBtnLayout, cancelBtnLayout;
    CategoryRecyclerAdapter.RecordRecyclerAdapter adapter;
    CircularImageView circularImageView;
    RefreshShowingDialog showingDialog;
    RecordModel model = new RecordModel();
    TextView txt_name, txtMedicalPersonID, txtPatientId;
    boolean adding = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        categoryList = new ArrayList();


        PhysicalServerObjectDataController.getInstance().fillContent(CategoryActivity.this);
        ApiCallDataController.getInstance().fillContent(getApplicationContext());

        CategoryRecyclerAdapter recyclerAdapter = new CategoryRecyclerAdapter(CategoryActivity.this, categoryList);
        Log.e("cheeeee", "" + categoryList.size());
        categoryList.add(new CategoryItemModel(R.drawable.profile, "Profile"));
        categoryList.add(new CategoryItemModel(R.drawable.physical_exam, "Physical Exam"));
        categoryList.add(new CategoryItemModel(R.drawable.medical_history, "Illness Record"));
        categoryList.add(new CategoryItemModel(R.drawable.allergies, "Allergies"));
        categoryList.add(new CategoryItemModel(R.drawable.family_history, "Family History"));
        categoryList.add(new CategoryItemModel(R.drawable.appointment, "Appointments"));
        categoryList.add(new CategoryItemModel(R.drawable.vaccination, "Immunizations"));
        categoryList.add(new CategoryItemModel(R.drawable.screening_record, "Screening Records"));
        categoryList.add(new CategoryItemModel(R.drawable.export, "Export Medical Records"));

        RecyclerView categoryRecyclerView = findViewById(R.id.category_grid_item_view);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryRecyclerView.setAdapter(recyclerAdapter);
        loadCutterMedicalPersonData();

        ImageView imgButton = findViewById(R.id.img_back);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadCutterMedicalPersonData() {
        PatientProfileDataController.getInstance().fetchPatientlProfileData();
        circularImageView = (CircularImageView) findViewById(R.id.circular_image);
        txt_name = (TextView) findViewById(R.id.mname);
        txtPatientId = findViewById(R.id.txt_patient_id);
        txtMedicalPersonID = findViewById(R.id.txt_medical_id);
        if (PatientProfileDataController.getInstance().currentPatientlProfile != null) {
            final PatientlProfileModel objModel = PatientProfileDataController.getInstance().currentPatientlProfile;
            txt_name.setText(objModel.getFirstName() + " " + objModel.getLastName());
            txtPatientId.setText(objModel.getPatientId());
            txtMedicalPersonID.setText(objModel.getMedicalPerson_id());

            if (objModel.getProfileByteArray() != null && objModel.getProfileByteArray().length > 0) {
                Bitmap bitmap = PersonalInfoController.getInstance().convertByteArrayTOBitmap(objModel.getProfileByteArray());
                circularImageView.setImageBitmap(bitmap);
                Log.e("profileCheckfromdb", "" + bitmap);

            } else {
                Picasso.get().load(objModel.getProfilePic()).into(circularImageView);
                Picasso.get().load(objModel.getProfilePic()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap1, Picasso.LoadedFrom isFromMedicalPerson) {
                        // loaded bitmap is here (bitmap)
                        Log.e("profileCheckfromurl", "" + bitmap1);
                        //  objModel.setProfileByteArray(PersonalInfoController.getInstance().convertBitmapToByteArray(bitmap1));
                        //  MedicalProfileDataController.getInstance().updateMedicalProfileData(objModel);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });
            }

        }
    }


    public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder> {
        Context context;
        ArrayList<CategoryItemModel> categoryItemList;

        public CategoryRecyclerAdapter(Context context, ArrayList<CategoryItemModel> categoryItemList) {
            this.context = context;
            this.categoryItemList = categoryItemList;
        }

        @Override
        public CategoryRecyclerAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View categoryItemView = LayoutInflater.isFromMedicalPerson(parent.getContext()).inflate(R.layout.item_category_grid_view, parent, false);
            return new CategoryRecyclerAdapter.CategoryViewHolder(categoryItemView);
        }

        @Override
        public void onBindViewHolder(final CategoryRecyclerAdapter.CategoryViewHolder holder, int position) {

            Log.e("tyfty", "" + categoryItemList.size());
            holder.categoryIcon.setImageResource(categoryItemList.get(position).getCategoryIcon());
            holder.categoryTitle.setText(categoryItemList.get(position).getCategoryTitle());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.getAdapterPosition() == 0) {
                        Intent bodyIndexIntent = new Intent(context, PatientGeneralProfileActivity.class);
                        context.startActivity(bodyIndexIntent);
                    }
                    if (holder.getAdapterPosition() == 1) {
                        PhysicalServerObjectDataController.getInstance().isFromStaring = true;
                        Intent bodyIndexIntent = new Intent(context, PhysicalExamActivity.class);
                        context.startActivity(bodyIndexIntent);
                    }
                    if (holder.getAdapterPosition() == 4) {
                        alertFamilyRecordDailog();
                       */
/* Intent bodyIndexIntent = new Intent(context, FamilyRecordActivity.class);
                        context.startActivity(bodyIndexIntent);
                   *//*

                    }
                    if (holder.getAdapterPosition() == 5) {
                        Intent appointmentIntent = new Intent(context, AppointmentsActivity.class);
                        context.startActivity(appointmentIntent);
                    }
                    if (holder.getAdapterPosition() == 3) {

                        alertAllergyRecordDailog();

                    }
                    if (holder.getAdapterPosition() == 6) {
                        Intent bodyIndexIntent = new Intent(context, VaccinationRecordActivity.class);
                        context.startActivity(bodyIndexIntent);
                    }

                    if (holder.getAdapterPosition() == 7) {
                        Intent bodyIndexIntent = new Intent(context, ScreeningRecordActivity.class);
                        context.startActivity(bodyIndexIntent);
                    }

                    if (holder.getAdapterPosition() == 2) {
                        Intent bodyIndexIntent = new Intent(context, MedicalHistoryActivity.class);
                        context.startActivity(bodyIndexIntent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return categoryItemList.size();
        }

        public class CategoryViewHolder extends RecyclerView.ViewHolder {
            ImageView categoryIcon;
            TextView categoryTitle;

            public CategoryViewHolder(View itemView) {
                super(itemView);
                categoryIcon = itemView.findViewById(R.id.img_category_icon);
                categoryTitle = itemView.findViewById(R.id.txt_category_title);
            }
        }


        public void alertAllergyRecordDailog() {
            showingDialog = new RefreshShowingDialog(CategoryActivity.this);
            if (isConn()) {
               // showingDialog.showAlert();
                AllergiesServerObjectDataController.getInstance().allergyFetchApiCall();
            }
            accessAllargyInterfaceMethods();

            View view = getLayoutInflater().inflate(R.layout.records_alert_dialog, null);
            RelativeLayout addListLayout = view.findViewById(R.id.add_list_layout);
            imgRecordAdd = view.findViewById(R.id.img_add);
            recordRecyclerView = view.findViewById(R.id.record_recyclerview);
            btnRecordEdit = view.findViewById(R.id.btn_record_edit);
            btnRecordSave = view.findViewById(R.id.btn_record_save);
            saveBtnLayout = view.findViewById(R.id.btn_save_layout);
            condition1 =view.findViewById(R.id.txt_co_title);
            btnRecordSaveChanges = view.findViewById(R.id.btn_record_save_changes);
            imgDialogCancle =view.findViewById(R.id.img_cancel);
            imgDialogCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            condition1.setText("Name");
            relation1 = view.findViewById(R.id.txt_rele);
            relation1.setText("Note");
            cancelBtnLayout = view.findViewById(R.id.btn_cancel_layout);
            btnRecordCancel = view.findViewById(R.id.btn_record_cancel);
            cancelBtnLayout.setVisibility(View.GONE);
            AllergyDataController.getInstance().fetchAllergyData(PatientProfileDataController.getInstance().
                            currentPatientlProfile);
            recyclerView();



           allergieModels = AllergyDataController.getInstance().allAllergyList;
                for (int i = 0; i < allergieModels.size(); i++) {
                    Log.e("recordOfAllergies",""+allergieModels.get(i).getName());
                    recordList.add(new RecordModel(allergieModels.get(i).getName(), allergieModels.get(i).getNote()));
                }
            dialog = new BottomSheetDialog(Objects.requireNonNull(CategoryActivity.this), R.style.BottomSheetDialogTheme);
            recordRecyclerView = view.findViewById(R.id.record_recyclerview);
            dialog.setContentView(view);
            dialog.setCancelable(true);
            dialog.show();

            adapter.notifyDataSetChanged();


            addListLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // alertRecordDialog();
                    recordList.add(new RecordModel("", ""));
                    adding = true;
                    adapter.notifyDataSetChanged();

                }
            });
            btnRecordSaveChanges.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONArray array = new JSONArray();
                    // array.put(recordModel);

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


                    }

                    addAllergyRecord(array);
                    btnRecordSaveChanges.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.btn_bck_color));
                    btnRecordSaveChanges.setTextColor(Color.parseColor("#ffffff"));
                    btnRecordCancel.setTextColor(Color.parseColor("#3E454C"));
                    btnRecordCancel.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));

                }
            });
            btnRecordSave.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    JSONArray array = new JSONArray();
                    // array.put(recordModel);

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


                    }

                    addAllergyRecord(array);
                    btnRecordSave.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.btn_bck_color));
                    btnRecordSave.setTextColor(Color.parseColor("#ffffff"));
                    btnRecordEdit.setTextColor(Color.parseColor("#3E454C"));
                    btnRecordEdit.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));

                }
            });

        }

        public boolean isConn() {
            ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity.getActiveNetworkInfo() != null) {
                if (connectivity.getActiveNetworkInfo().isConnected())
                    return true;
            }
            return false;
        }


        // family record list

        public void alertFamilyRecordDailog() {
            TextView titleHedaing, txtNoRecord;
            final Button btnSaveChanges;
            showingDialog = new RefreshShowingDialog(CategoryActivity.this);

            View view = getLayoutInflater().inflate(R.layout.records_alert_dialog, null);
            RelativeLayout addListLayout = view.findViewById(R.id.add_list_layout);
            recordRecyclerView = view.findViewById(R.id.record_recyclerview);
            imgRecordAdd = view.findViewById(R.id.img_add);
            btnSaveChanges = view.findViewById(R.id.btn_record_save_changes);
            btnRecordEdit = view.findViewById(R.id.btn_record_edit);
            btnRecordSave = view.findViewById(R.id.btn_record_save);
            txtNoRecord = view.findViewById(R.id.txt_no_records);
            txtNoRecord.setVisibility(View.GONE);
            saveBtnLayout = view.findViewById(R.id.btn_save_layout);
            cancelBtnLayout = view.findViewById(R.id.btn_cancel_layout);
            btnRecordCancel = view.findViewById(R.id.btn_record_cancel);
            titleHedaing = view.findViewById(R.id.record_title);
            titleHedaing.setText("Family Record");
            imgDialogCancle =view.findViewById(R.id.img_cancel);

            imgDialogCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            if (isConn()) {

              //  showingDialog.showAlert();
                FamilyRecordServerDataController.getInstance().newFamilyFetchApi();
            }
            accessInterfaceMethods();

            recyclerView();

            cancelBtnLayout.setVisibility(View.GONE);
            PatientProfileDataController.getInstance().fetchPatientlProfileData();
            FamilyHistoryDataController.getInstance().fetchFamilyData(PatientProfileDataController.getInstance().currentPatientlProfile);
            if (FamilyHistoryDataController.getInstance().fetchFamilyData(PatientProfileDataController.getInstance()
                    .currentPatientlProfile).isEmpty()){
                dialog = new BottomSheetDialog(Objects.requireNonNull(CategoryActivity.this), R.style.BottomSheetDialogTheme);
                dialog.setContentView(view);
                dialog.setCancelable(true);
                dialog.show();
                Toast.makeText(context, "Family records not found", Toast.LENGTH_SHORT).show();
            }
            else {
                familyHistoryArrayList = FamilyHistoryDataController.getInstance().allFamilyList;

                for (int i = 0; i < familyHistoryArrayList.size(); i++) {

                    Log.e("khiladi",""+familyHistoryArrayList.get(i).getDieseaseName());
                    recordList.add(new RecordModel(familyHistoryArrayList.get(i).getDieseaseName(), familyHistoryArrayList.get(i).getRelation()));

                }
                adapter.notifyDataSetChanged();
                dialog = new BottomSheetDialog(Objects.requireNonNull(CategoryActivity.this), R.style.BottomSheetDialogTheme);
                dialog.setContentView(view);
                dialog.setCancelable(true);
                dialog.show();
            }


            addListLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  alertRecordDialog();
                    recordList.add(new RecordModel("", ""));
                    adding = true;
                    adapter.notifyDataSetChanged();

                }
            });
            btnSaveChanges.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    JSONArray array = new JSONArray();
                    // array.put(recordModel);

                    for (int i = 0; i < recordList.size(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("dieseaseName", recordList.get(i).getCondition());
                            jsonObject.put("relationship", recordList.get(i).getRelation());
                            jsonObject.put("age", "");
                            jsonObject.put("note", "");
                            array.put(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    addFamilyRecord(array);
                    btnSaveChanges.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.btn_bck_color));
                    btnSaveChanges.setTextColor(Color.parseColor("#ffffff"));
                    btnRecordCancel.setTextColor(Color.parseColor("#3E454C"));
                    btnRecordCancel.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));

                }
            });

            btnRecordSave.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    JSONArray array = new JSONArray();
                    // array.put(recordModel);

                    for (int i = 0; i < recordList.size(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("dieseaseName", recordList.get(i).getCondition());
                            jsonObject.put("relationship", recordList.get(i).getRelation());
                            jsonObject.put("age", "");
                            jsonObject.put("note", "");
                            array.put(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    addFamilyRecord(array);
                    btnRecordSave.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.btn_bck_color));
                    btnRecordSave.setTextColor(Color.parseColor("#ffffff"));
                    btnRecordEdit.setTextColor(Color.parseColor("#3E454C"));
                    btnRecordEdit.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));

                }
            });

        }

        public void addFamilyRecord(JSONArray list) {
            MedicalProfileDataController.getInstance().fetchMedicalProfileData();
            MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
            object = new JSONObject();
            String regNo = currentMedical.getHospital_reg_number();
            Log.e("dfasdf", regNo);

            try {
                object.put("hospital_reg_num", regNo);
                object.put("patientID", PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
                object.put("medical_record_id", PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
                object.put("byWhom", "medical personnel");
                object.put("byWhomID", PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalPerson_id());
                object.put("famliyDiseases", list);
                Log.e("list", "" + list);
                Log.e("accessToken", "" + currentMedical.getAccessToken());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("objecttt", "" + object);
            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject) jsonParser.parse(object.toString());

            ApiCallDataController.getInstance().loadServerApiCall(
                    ApiCallDataController.getInstance().serverApi.addFamilyHistoryMultiple(currentMedical.getAccessToken(), gsonObject), "insert");


        }


        // Adding allergy Records
        public void addAllergyRecord(JSONArray list) {
            MedicalProfileDataController.getInstance().fetchMedicalProfileData();
            MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
            object = new JSONObject();
            String regNo = currentMedical.getHospital_reg_number();
            Log.e("dfasdf", regNo);

            try {
                object.put("hospital_reg_num", regNo);
                object.put("patientID", PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
                object.put("medical_record_id", PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
                object.put("byWhom", "medical personnel");
                object.put("byWhomID", PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalPerson_id());
                object.put("allergies", list);
                Log.e("list", "" + list);
                Log.e("accessToken", "" + currentMedical.getAccessToken());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("objecttt", "" + object);
            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject) jsonParser.parse(object.toString());

            ApiCallDataController.getInstance().loadServerApiCall(
                    ApiCallDataController.getInstance().serverApi.addMultipleAllergyRecords(currentMedical.getAccessToken(), gsonObject), "insert");


        }

        private void accessAllargyInterfaceMethods() {
            ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
                @Override
                public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                    if (curdOpetaton.equals("fetch")) {
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

                    } else if (curdOpetaton.equals("insert")) {
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
                    } else if (curdOpetaton.equals("update")) {
                        Gson gson = new Gson();
                        AllergyServerObject userIdentifier = gson.fromJson(params.toString(), AllergyServerObject.class);
                        AllergiesServerObjectDataController.getInstance().processAllergyupdateData(userIdentifier);
                        dialog.dismiss();
                    } else if (curdOpetaton.equals("delete")) {
                        AllergyDataController.getInstance().deleteAllergieModelData(PatientProfileDataController.getInstance().currentPatientlProfile, AllergyDataController.getInstance().currentAllergieModel);
                    }
                    showingDialog.hideRefreshDialog();
                }

                @Override
                public void failureCallBack(String failureMsg) {
                    showingDialog.hideRefreshDialog();
                    Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void recyclerView() {
            recordList = new ArrayList<>();
            adapter = new RecordRecyclerAdapter(recordList, CategoryActivity.this);
            recordRecyclerView.setLayoutManager(new LinearLayoutManager(CategoryActivity.this));
            recordRecyclerView.setAdapter(adapter);
            recordRecyclerView.setHasFixedSize(false);

        }


        public class RecordRecyclerAdapter extends RecyclerView.Adapter<RecordRecyclerAdapter.RecordHolder> {
            List<RecordModel> recordModels;
            RecordModel model = new RecordModel();
            Context context;
            boolean deleteImg = false;

            public RecordRecyclerAdapter(List<RecordModel> recordModels, Context context) {

                Log.e("fjbjk", "kfnjnjf");
                this.recordModels = recordModels;
                this.context = context;
            }

            @Override
            public RecordRecyclerAdapter.RecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View recordView = LayoutInflater.isFromMedicalPerson(parent.getContext()).inflate(R.layout.record_recycler_item, parent, false);
                return new RecordRecyclerAdapter.RecordHolder(recordView);

            }

            @Override
            public void onBindViewHolder(final RecordRecyclerAdapter.RecordHolder holder, final int position) {

                Log.e("tfy",""+recordModels.size());

                holder.txtRelation.setEnabled(false);
                holder.txtCondition.setEnabled(false);

                if (adding) {

                    Log.e("fdjhfjurjfbh", "fdjfn   " + position);

                    if (position == recordModels.size() - 1) {
                        //notifyDataSetChanged();

                        Log.e("fdjhjfbh", "fdjdfddfn   " + position);
                        adding = false;
                        holder.txtRelation.setEnabled(true);
                        holder.txtCondition.setEnabled(true);
                    }
                }
                Log.e("fdjhjfbh", "fdjfn   " + position);

                int a = position + 1;
                if (deleteImg) {
                    holder.txtCondition.setEnabled(true);
                    holder.txtRelation.setEnabled(true);
                    holder.imgDelete.setVisibility(View.VISIBLE);
                } else {

                  if (adding) {
                        if (position == recordModels.size() - 1) {
                            //notifyDataSetChanged();
                            adding = false;
                            holder.txtRelation.setEnabled(true);
                            holder.txtCondition.setEnabled(true);
                        }
                    }
                    holder.imgDelete.setVisibility(View.GONE);
                }


                holder.txtSno.setText(String.valueOf(a));
                holder.txtCondition.setText(recordModels.get(position).getCondition());
                holder.txtRelation.setText(recordModels.get(position).getRelation());
                // if(model.isTxtBoolean()){}
                holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeAt(position);
                       */
/* final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                        builder.setCancelable(false);
                        builder.setTitle("Delete");
                        builder.setMessage("Do you want to delete this Record ?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            //    FamilyHistoryDataController.getInstance().currentFamilyHistoryModel = recordModels.get(position);
                                JSONObject objectDelete = new JSONObject();
                                try {
                                    objectDelete.put("hospital_reg_num", MedicalProfileDataController.getInstance().currentMedicalProfile.getHospital_reg_number());
                                    objectDelete.put("byWhom", "medical personnel");
                                    objectDelete.put("byWhomID", MedicalProfileDataController.getInstance().currentMedicalProfile.getMedical_person_id());
                                    objectDelete.put("patientID", PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
                                    objectDelete.put("medical_record_id", PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
                                    // params.put("family_history_record_id", objModel.getFamilyRecordId());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                JsonParser jsonParser = new JsonParser();
                                JsonObject gsonObject = (JsonObject) jsonParser.parse(objectDelete.toString());
                                Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
                                Log.e("accessToken", "" + MedicalProfileDataController.getInstance().currentMedicalProfile.getAccessToken());
                                if (isConn()) {
                                    showingDialog.showAlert();
                                    ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.deleteFamilyHistoryMultiple(
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
                        builder.create().show();*//*

                    }
                });

                btnRecordEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveBtnLayout.setVisibility(View.GONE);
                        cancelBtnLayout.setVisibility(View.VISIBLE);
                        btnRecordEdit.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.btn_bck_color));
                        btnRecordEdit.setTextColor(Color.parseColor("#ffffff"));
                        btnRecordSave.setTextColor(Color.parseColor("#3E454C"));
                        btnRecordCancel.setTextColor(Color.parseColor("#3E454C"));
                        btnRecordCancel.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));
                        btnRecordSave.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));
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
                        btnRecordEdit.setTextColor(Color.parseColor("#3E454C"));
                        btnRecordEdit.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));
                        btnRecordCancel.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.btn_bck_color));
                        btnRecordCancel.setTextColor(Color.parseColor("#ffffff"));

                    }
                });


            }

            @Override
            public int getItemCount() {
                return recordModels.size();
            }

            public void removeAt(int position) {
                recordModels.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, recordModels.size());
            }

            public class RecordHolder extends RecyclerView.ViewHolder {
                TextView txtSno;
                ImageView imgDelete;
                EditText txtCondition, txtRelation;

                public RecordHolder(@NonNull View itemView) {
                    super(itemView);
                    txtCondition = itemView.findViewById(R.id.txt_item_condition);
                    txtRelation = itemView.findViewById(R.id.txt_item_relation);
                    txtSno = itemView.findViewById(R.id.txt_sno);
                    imgDelete = itemView.findViewById(R.id.img_item_delete);
                    txtCondition.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (txtCondition.hasFocus()) {
                                if (!s.toString().isEmpty()) {
                                    recordList.get(getAdapterPosition()).setCondition(s.toString());
                                }
                            }

                        }
                    });
                    txtRelation.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (txtRelation.hasFocus()) {
                                if (!s.toString().isEmpty()) {
                                    recordList.get(getAdapterPosition()).setRelation(s.toString());
                                }
                            }

                        }
                    });
                }
            }
        }


    }

    private void accessInterfaceMethods() {

        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {

                if (curdOpetaton.equals("fetch")) {
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("famliyDiseases");
                        Log.e("afdasa", "" + jsonArray.toString());
                        PatientProfileDataController.getInstance().fetchPatientlProfileData();
                        FamilyTrackInfoDataController.getInstance().deleteTrackData(FamilyHistoryDataController.getInstance().allFamilyList);
                        FamilyHistoryDataController.getInstance().deleteFamilyData(PatientProfileDataController.getInstance().currentPatientlProfile);
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Gson gson = new Gson();
                                FamilyRecordServerObject userIdentifier = gson.fromJson(jsonArray.getJSONObject(i).toString(), FamilyRecordServerObject.class);
                                ArrayList<TrackingServerObject> trackArray = userIdentifier.getTracking();
                                Log.e("trackArray", "call" + userIdentifier.getRelationship());
                                FamilyRecordServerDataController.getInstance().processfetchFamilyAddData(userIdentifier, trackArray);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (curdOpetaton.equals("insert")) {
                    Gson gson = new Gson();
                    FamilyRecordServerObject userIdentifier = gson.fromJson(params.toString(), FamilyRecordServerObject.class);
                    try {
                        Log.e("familydata", "call" + jsonObject.getString("family_history_record_id"));
                        userIdentifier.setFamily_history_record_id(jsonObject.getString("family_history_record_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("familydata", "call" + userIdentifier.getAge() + userIdentifier.getMoreInfo());
                    FamilyRecordServerDataController.getInstance().processAddFamilyAddData(userIdentifier);
                    dialog.dismiss();
                } else if (curdOpetaton.equals("update")) {
                    Gson gson = new Gson();
                    FamilyRecordServerObject userIdentifier = gson.fromJson(params.toString(), FamilyRecordServerObject.class);
                    Log.e("updatefamilydata", "call" + userIdentifier.getAge() + userIdentifier.getMoreInfo());
                    FamilyRecordServerDataController.getInstance().processFamilyupdateData(userIdentifier);
                    dialog.dismiss();
                } else if (curdOpetaton.equals("delete")) {
                    FamilyHistoryDataController.getInstance().deleteFamilyHistoryModelData(PatientProfileDataController.getInstance().currentPatientlProfile, FamilyHistoryDataController.getInstance().currentFamilyHistoryModel);
                }
                showingDialog.hideRefreshDialog();

            }

            @Override
            public void failureCallBack(String failureMsg) {
                showingDialog.hideRefreshDialog();
                Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}*/


package com.vedas.spectrocare.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.AllergiesServerObjectDataController;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.Controllers.FamilyRecordServerDataController;
import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.Controllers.PhysicalServerObjectDataController;
import com.vedas.spectrocare.DataBase.AllergyDataController;
import com.vedas.spectrocare.DataBase.AllergyTrackInfoDataController;
import com.vedas.spectrocare.DataBase.FamilyHistoryDataController;
import com.vedas.spectrocare.DataBase.FamilyTrackInfoDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBaseModels.AllergieModel;
import com.vedas.spectrocare.DataBaseModels.FamilyHistoryModel;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.DataBaseModels.PatientlProfileModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApiModel.AllergyServerObject;
import com.vedas.spectrocare.ServerApiModel.FamilyRecordServerObject;
import com.vedas.spectrocare.ServerApiModel.TrackingServerObject;
import com.vedas.spectrocare.adapter.CategoryRecyclerAdapter;
import com.vedas.spectrocare.adapter.RecordRecyclerAdapter;
import com.vedas.spectrocare.model.CategoryItemModel;
import com.vedas.spectrocare.model.RecordModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryActivity extends AppCompatActivity implements MedicalPersonaSignupView {
    ArrayList categoryList;
    ImageView imgRecordAdd, imgDialogCancle;
    String gg, ff;
    TextView condition1, relation1;
    Dialog dialog;
    RelativeLayout layoutAllergy;
    RecyclerView recordRecyclerView;
    Button btnRecordEdit, btnRecordSave, btnRecordCancel, btnRecordSaveChanges,btnCancel;
    JSONObject params = new JSONObject();
    JSONObject object;
    ArrayList<FamilyHistoryModel> familyHistoryArrayList = new ArrayList<>();
    List<RecordModel> recordList;
    ArrayList<AllergieModel> allergieModels = new ArrayList<>();
    LinearLayout saveBtnLayout, cancelBtnLayout;
    CategoryRecyclerAdapter.RecordRecyclerAdapter adapter;
    CircularImageView circularImageView;
    RefreshShowingDialog showingDialog;
    RecordModel model = new RecordModel();
    TextView txt_name, txtMedicalPersonID, txtPatientId;
    boolean adding = false;
    boolean record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        categoryList = new ArrayList();

        PhysicalServerObjectDataController.getInstance().fillContent(CategoryActivity.this);
        ApiCallDataController.getInstance().fillContent(getApplicationContext());

        CategoryRecyclerAdapter recyclerAdapter = new CategoryRecyclerAdapter(CategoryActivity.this, categoryList);
        Log.e("cheeeee", "" + categoryList.size());
        categoryList.add(new CategoryItemModel(R.drawable.profile, "Profile"));
        categoryList.add(new CategoryItemModel(R.drawable.physical_exam, "Physical Exam"));
        categoryList.add(new CategoryItemModel(R.drawable.medical_history, "Illness Record"));
        categoryList.add(new CategoryItemModel(R.drawable.allergies, "Allergies"));
        categoryList.add(new CategoryItemModel(R.drawable.family_history, "Family History"));
        categoryList.add(new CategoryItemModel(R.drawable.appointment, "Appointments"));
        categoryList.add(new CategoryItemModel(R.drawable.vaccination, "Immunizations"));
        categoryList.add(new CategoryItemModel(R.drawable.screening_record, "Screening Records"));
        categoryList.add(new CategoryItemModel(R.drawable.export, "Export Medical Records"));

        RecyclerView categoryRecyclerView = findViewById(R.id.category_grid_item_view);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryRecyclerView.setAdapter(recyclerAdapter);
        loadCutterMedicalPersonData();

        ImageView imgButton = findViewById(R.id.img_back);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadCutterMedicalPersonData() {
        PatientProfileDataController.getInstance().fetchPatientlProfileData();
        circularImageView = (CircularImageView) findViewById(R.id.circular_image);
        txt_name = (TextView) findViewById(R.id.mname);
        txtPatientId = findViewById(R.id.txt_patient_id);
        txtMedicalPersonID = findViewById(R.id.txt_medical_id);
        if (PatientProfileDataController.getInstance().currentPatientlProfile != null) {
            final PatientlProfileModel objModel = PatientProfileDataController.getInstance().currentPatientlProfile;
            txt_name.setText(objModel.getFirstName() + " " + objModel.getLastName());
            txtPatientId.setText(objModel.getPatientId());
            txtMedicalPersonID.setText(objModel.getMedicalPerson_id());

            if (objModel.getProfileByteArray() != null && objModel.getProfileByteArray().length > 0) {
                Bitmap bitmap = PersonalInfoController.getInstance().convertByteArrayTOBitmap(objModel.getProfileByteArray());
                circularImageView.setImageBitmap(bitmap);
                Log.e("profileCheckfromdb", "" + bitmap);

            } else {
                Picasso.get().load(objModel.getProfilePic()).into(circularImageView);
                Picasso.get().load(objModel.getProfilePic()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap1, Picasso.LoadedFrom from) {
                        // loaded bitmap is here (bitmap)
                        Log.e("profileCheckfromurl", "" + bitmap1);
                        //  objModel.setProfileByteArray(PersonalInfoController.getInstance().convertBitmapToByteArray(bitmap1));
                        //  MedicalProfileDataController.getInstance().updateMedicalProfileData(objModel);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });
            }

        }
    }

    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter = new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CategoryActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);
    }

    public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder> {
        Context context;
        ArrayList<CategoryItemModel> categoryItemList;

        public CategoryRecyclerAdapter(Context context, ArrayList<CategoryItemModel> categoryItemList) {
            this.context = context;
            this.categoryItemList = categoryItemList;
        }

        @Override
        public CategoryRecyclerAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View categoryItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_grid_view, parent, false);
            return new CategoryRecyclerAdapter.CategoryViewHolder(categoryItemView);
        }

        @Override
        public void onBindViewHolder(final CategoryRecyclerAdapter.CategoryViewHolder holder, int position) {

            Log.e("tyfty", "" + categoryItemList.size());
            holder.categoryIcon.setImageResource(categoryItemList.get(position).getCategoryIcon());
            holder.categoryTitle.setText(categoryItemList.get(position).getCategoryTitle());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.getAdapterPosition() == 0) {
                        Intent bodyIndexIntent = new Intent(context, PatientGeneralProfileActivity.class);
                        context.startActivity(bodyIndexIntent);
                    }
                    if (holder.getAdapterPosition() == 1) {
                        PhysicalServerObjectDataController.getInstance().isFromStaring = true;
                        Intent bodyIndexIntent = new Intent(context, PhysicalExamActivity.class);
                        context.startActivity(bodyIndexIntent);
                    }
                    if (holder.getAdapterPosition() == 4) {
                       // alertFamilyRecordDailog();
                        Intent familyIntent = new Intent(context, FamilyRecordActivity.class);
                        context.startActivity(familyIntent);
                    }
                    if (holder.getAdapterPosition() == 5) {
                        Intent appointmentIntent = new Intent(context, AppointmentsActivity.class);
                        context.startActivity(appointmentIntent);
                    }
                    if (holder.getAdapterPosition() == 3) {

                        //alertAllergyRecordDailog();
                        Intent allergyIntent = new Intent(context, AllergiesActivity.class);
                        context.startActivity(allergyIntent);


                    }
                    if (holder.getAdapterPosition() == 6) {
                        Intent bodyIndexIntent = new Intent(context, VaccinationRecordActivity.class);
                        context.startActivity(bodyIndexIntent);
                    }

                    if (holder.getAdapterPosition() == 7) {
                        Intent bodyIndexIntent = new Intent(context, ScreeningRecordActivity.class);
                        context.startActivity(bodyIndexIntent);
                    }

                    if (holder.getAdapterPosition() == 2) {
                        Intent bodyIndexIntent = new Intent(context, MedicalHistoryActivity.class);
                        context.startActivity(bodyIndexIntent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return categoryItemList.size();
        }

        public class CategoryViewHolder extends RecyclerView.ViewHolder {
            ImageView categoryIcon;
            TextView categoryTitle;

            public CategoryViewHolder(View itemView) {
                super(itemView);
                categoryIcon = itemView.findViewById(R.id.img_category_icon);
                categoryTitle = itemView.findViewById(R.id.txt_category_title);
            }
        }

        public void alertAllergyRecordDailog() {
            record = true;
            showingDialog = new RefreshShowingDialog(CategoryActivity.this);
            Log.e("boolean", "" + record);
            if (isConn()) {
                Log.e("dffa", "dfdf");
                // showingDialog.showAlert();
                AllergiesServerObjectDataController.getInstance().allergyFetchApiCall();
            } else {
                showingDialog.dismiss();

            }
            accessAllargyInterfaceMethods();

            View view = getLayoutInflater().inflate(R.layout.records_alert_dialog, null);
            RelativeLayout addListLayout = view.findViewById(R.id.add_list_layout);
            imgRecordAdd = view.findViewById(R.id.img_add);
            recordRecyclerView = view.findViewById(R.id.record_recyclerview);
            btnRecordEdit = view.findViewById(R.id.btn_record_edit);
            layoutAllergy = view.findViewById(R.id.faily_record_layout);
            btnRecordSave = view.findViewById(R.id.btn_record_save);
            saveBtnLayout = view.findViewById(R.id.btn_save_layout);
            condition1 = view.findViewById(R.id.txt_co_title);
            btnCancel = view.findViewById(R.id.btn_cancel_record);
            btnRecordSaveChanges = view.findViewById(R.id.btn_record_save_changes);
            imgDialogCancle = view.findViewById(R.id.img_cancel);
            imgDialogCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            condition1.setText("Allergy");
            relation1 = view.findViewById(R.id.txt_rele);
            relation1.setText("Note");
            cancelBtnLayout = view.findViewById(R.id.btn_cancel_layout);
            btnRecordCancel = view.findViewById(R.id.btn_record_cancel);
            cancelBtnLayout.setVisibility(View.GONE);
            PatientProfileDataController.getInstance().fetchPatientlProfileData();
            AllergyDataController.getInstance().fetchAllergyData(PatientProfileDataController.getInstance().currentPatientlProfile);
            recyclerView();


            //  recordList.clear();
            if (!AllergyDataController.getInstance().allAllergyList.isEmpty()) {
                condition1.setVisibility(View.VISIBLE);
                relation1.setVisibility(View.VISIBLE);
                dialog = new BottomSheetDialog(Objects.requireNonNull(CategoryActivity.this), R.style.BottomSheetDialogTheme);
                dialog.setContentView(view);
                dialog.setCancelable(true);
                dialog.show();
                showingDialog.showAlert();
                allergieModels = AllergyDataController.getInstance().allAllergyList;
                for (int i = 0; i < allergieModels.size(); i++) {
                    Log.e("redf", "" + allergieModels.get(i).getName());
                    recordList.add(new RecordModel(allergieModels.get(i).getName(), allergieModels.get(i).getNote()));
                }
                adapter.notifyDataSetChanged();
            } else {
                condition1.setVisibility(View.GONE);
                relation1.setVisibility(View.GONE);
                dialog = new BottomSheetDialog(Objects.requireNonNull(CategoryActivity.this), R.style.BottomSheetDialogTheme);
                dialog.setContentView(view);
                dialog.setCancelable(true);
                dialog.show();
                showingDialog.showAlert();
               /* recordList.clear();
                adapter.notifyDataSetChanged();
               */
                // Toast.makeText(context, "Allergy records not found", Toast.LENGTH_SHORT).show();
            }
            addListLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnCancel.setVisibility(View.VISIBLE);
                    layoutAllergy.setVisibility(View.VISIBLE);
                    if (!recordList.isEmpty()){
                        for (int i=0;i<=recordList.size()-1;i++){
                            Log.e("redf", "hbjj" +  recordList.get(i).getCondition() );
                            if (recordList.get(recordList.size()-1).getCondition().isEmpty() || recordList.get(recordList.size()-1).getRelation().isEmpty()){
                                dialogeforCheckavilability("Alert","Fields can't be empty","Ok");
                                break;
                            }else {

                                recordList.add(new RecordModel("", ""));
                                adding = true;
                                adapter.notifyDataSetChanged();
                                break;
                            }
                        }

                    }else{
                        condition1.setVisibility(View.VISIBLE);
                        relation1.setVisibility(View.VISIBLE);
                        recordList.add(new RecordModel("", ""));
                        adding = true;
                        adapter.notifyDataSetChanged();

                    }

                }
            });
            btnRecordSaveChanges.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showingDialog.showAlert();
                    JSONArray array = new JSONArray();
                    // array.put(recordModel);

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


                    }

                    addAllergyRecord(array);
                    btnRecordSaveChanges.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.btn_bck_color));
                    btnRecordSaveChanges.setTextColor(Color.parseColor("#ffffff"));
                    btnRecordCancel.setTextColor(Color.parseColor("#3E454C"));
                    btnRecordCancel.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));

                }
            });
            btnRecordSave.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    showingDialog.showAlert();
                    JSONArray array = new JSONArray();
                    // array.put(recordModel);

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


                    }

                    addAllergyRecord(array);
                    btnRecordSave.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.btn_bck_color));
                    btnRecordSave.setTextColor(Color.parseColor("#ffffff"));
                    btnRecordEdit.setTextColor(Color.parseColor("#3E454C"));
                    btnRecordEdit.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));

                }
            });

        }

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
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void failureCallBack(String failureMsg) {
                    showingDialog.hideRefreshDialog();
                    Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
                }
            });
        }

        public boolean isConn() {
            ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity.getActiveNetworkInfo() != null) {
                if (connectivity.getActiveNetworkInfo().isConnected())
                    return true;
            }
            return false;
        }

        // family record list
        public void alertFamilyRecordDailog() {
            record = false;
            final TextView titleHedaing, txtNoRecord,txtCondition,txtRelation;
            final RelativeLayout layoutFamily;
            final Button btnSaveChanges;
            showingDialog = new RefreshShowingDialog(CategoryActivity.this);
            View view = getLayoutInflater().inflate(R.layout.records_alert_dialog, null);
            RelativeLayout addListLayout = view.findViewById(R.id.add_list_layout);
            recordRecyclerView = view.findViewById(R.id.record_recyclerview);
            imgRecordAdd = view.findViewById(R.id.img_add);
            layoutFamily = view.findViewById(R.id.faily_record_layout);
            btnSaveChanges = view.findViewById(R.id.btn_record_save_changes);
            btnRecordEdit = view.findViewById(R.id.btn_record_edit);
            btnRecordSave = view.findViewById(R.id.btn_record_save);
            txtNoRecord = view.findViewById(R.id.txt_no_records);
            txtCondition = view.findViewById(R.id.txt_co_title);
            txtRelation = view.findViewById(R.id.txt_rele);
            txtNoRecord.setVisibility(View.GONE);
            btnCancel = view.findViewById(R.id.btn_cancel_record);
            saveBtnLayout = view.findViewById(R.id.btn_save_layout);
            cancelBtnLayout = view.findViewById(R.id.btn_cancel_layout);
            btnRecordCancel = view.findViewById(R.id.btn_record_cancel);
            titleHedaing = view.findViewById(R.id.record_title);
            titleHedaing.setText("Family Record");
            imgDialogCancle = view.findViewById(R.id.img_cancel);

            imgDialogCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            if (isConn()) {

                //  showingDialog.showAlert();
                FamilyRecordServerDataController.getInstance().newFamilyFetchApi();
            } else {
                showingDialog.dismiss();

            }
            accessInterfaceMethods();
            if (FamilyHistoryDataController.getInstance().allFamilyList.isEmpty()){
                layoutFamily.setVisibility(View.GONE);
            }else{
                layoutFamily.setVisibility(View.VISIBLE);
            }
            recyclerView();
            cancelBtnLayout.setVisibility(View.GONE);
            PatientProfileDataController.getInstance().fetchPatientlProfileData();
            FamilyHistoryDataController.getInstance().fetchFamilyData(PatientProfileDataController.getInstance().currentPatientlProfile);
            if (!FamilyHistoryDataController.getInstance().allFamilyList.isEmpty()/*.fetchFamilyData(PatientProfileDataController.getInstance()
                    .currentPatientlProfile).isEmpty()*/) {
                dialog = new BottomSheetDialog(Objects.requireNonNull(CategoryActivity.this), R.style.BottomSheetDialogTheme);
                dialog.setContentView(view);
                dialog.setCancelable(true);
                dialog.show();
                showingDialog.showAlert();
                familyHistoryArrayList = FamilyHistoryDataController.getInstance().allFamilyList;

                for (int i = 0; i < familyHistoryArrayList.size(); i++) {
                    recordList.add(new RecordModel(familyHistoryArrayList.get(i).getDieseaseName(), familyHistoryArrayList.get(i).getRelation()));
                }
                adapter.notifyDataSetChanged();
            } else {
                txtCondition.setVisibility(View.GONE);
                txtRelation.setVisibility(View.GONE);
                dialog = new BottomSheetDialog(Objects.requireNonNull(CategoryActivity.this), R.style.BottomSheetDialogTheme);
                dialog.setContentView(view);
                dialog.setCancelable(true);
                dialog.show();
                showingDialog.showAlert();
                // Toast.makeText(context, "Family records not found", Toast.LENGTH_SHORT).show();
            }

            addListLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnCancel.setVisibility(View.VISIBLE);
                    layoutFamily.setVisibility(View.VISIBLE);
                    //   Log.e("reccc",""+recordList.get(recordList.size()-1));
                    if (!recordList.isEmpty()){
                        for (int i=0;i<=recordList.size()-1;i++){
                            Log.e("redf", "hbjj" +  recordList.get(i).getCondition() );
                            if (recordList.get(recordList.size()-1).getCondition().isEmpty() || recordList.get(recordList.size()-1).getRelation().isEmpty()){
                                dialogeforCheckavilability("Alert","Fields can't be empty","Ok");
                                break;
                            }else {

                                recordList.add(new RecordModel("", ""));
                                adding = true;
                                adapter.notifyDataSetChanged();
                                break;
                            }
                        }

                    }else{
                        txtCondition.setVisibility(View.VISIBLE);
                        txtRelation.setVisibility(View.VISIBLE);
                        recordList.add(new RecordModel("", ""));
                        adding = true;
                        adapter.notifyDataSetChanged();

                    }

                }
            });
            btnSaveChanges.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    JSONArray array = new JSONArray();
                    // array.put(recordModel);
                    showingDialog.showAlert();
                    for (int i = 0; i < recordList.size(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("dieseaseName", recordList.get(i).getCondition());
                            jsonObject.put("relationship", recordList.get(i).getRelation());
                            jsonObject.put("age", "");
                            jsonObject.put("note", "");
                            array.put(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    addFamilyRecord(array);
                    btnSaveChanges.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.btn_bck_color));
                    btnSaveChanges.setTextColor(Color.parseColor("#ffffff"));
                    btnRecordCancel.setTextColor(Color.parseColor("#3E454C"));
                    btnRecordCancel.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));

                }
            });

            btnRecordSave.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    showingDialog.showAlert();
                    JSONArray array = new JSONArray();
                    // array.put(recordModel);
                    for (int i = 0; i < recordList.size(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("dieseaseName", recordList.get(i).getCondition());
                            jsonObject.put("relationship", recordList.get(i).getRelation());
                            jsonObject.put("age", "");
                            jsonObject.put("note", "");
                            array.put(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    addFamilyRecord(array);
                    btnRecordSave.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.btn_bck_color));
                    btnRecordSave.setTextColor(Color.parseColor("#ffffff"));
                    btnRecordEdit.setTextColor(Color.parseColor("#3E454C"));
                    btnRecordEdit.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));
                }
            });
        }

        public void addFamilyRecord(JSONArray list) {

            MedicalProfileDataController.getInstance().fetchMedicalProfileData();
            MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
            object = new JSONObject();
            String regNo = currentMedical.getHospital_reg_number();
            Log.e("dfasdf", regNo);
            try {
                object.put("hospital_reg_num", regNo);
                object.put("patientID", PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
                object.put("medical_record_id", PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
                object.put("byWhom", "medical personnel");
                object.put("byWhomID", PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalPerson_id());
                object.put("famliyDiseases", list);
                Log.e("list", "" + list.getJSONObject(list.length() - 1));
                ff = list.getJSONObject(list.length() - 1).getString("dieseaseName");
                gg = list.getJSONObject(list.length() - 1).getString("relationship");


                Log.e("accessToken", "" + currentMedical.getAccessToken());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("objecttt", "" + object);
            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject) jsonParser.parse(object.toString());
            Log.e("asdfaad", "adf" + ff);

            if (!(list.length() == 0)) {
                showingDialog.hideRefreshDialog();
                if (ff.isEmpty()) {

                    dialogeforCheckavilability("Error", "Please enter disease ", "ok");
                } else if (gg.isEmpty()) {
                    showingDialog.dismiss();
                    dialogeforCheckavilability("Error", "Please enter relation ", "ok");
                } else
                    ApiCallDataController.getInstance().loadServerApiCall(
                            ApiCallDataController.getInstance().serverApi.addFamilyHistoryMultiple(currentMedical.getAccessToken(), gsonObject), "insert");
            } else {
                ApiCallDataController.getInstance().loadServerApiCall(
                        ApiCallDataController.getInstance().serverApi.addFamilyHistoryMultiple(currentMedical.getAccessToken(), gsonObject), "insert");

            }
        }


        // Adding allergy Records
        public void addAllergyRecord(JSONArray list) {
            MedicalProfileDataController.getInstance().fetchMedicalProfileData();
            MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
            object = new JSONObject();
            String regNo = currentMedical.getHospital_reg_number();
            Log.e("dfasdf", regNo);

            try {
                object.put("hospital_reg_num", regNo);
                object.put("patientID", PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
                object.put("medical_record_id", PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
                object.put("byWhom", "medical personnel");
                object.put("byWhomID", PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalPerson_id());
                object.put("allergies", list);
                Log.e("list", "" + list);
                ff = list.getJSONObject(list.length() - 1).getString("name");
                gg = list.getJSONObject(list.length() - 1).getString("note");

                Log.e("accessToken", "" + currentMedical.getAccessToken());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("objecttt", "" + object);
            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject) jsonParser.parse(object.toString());
            if (!(list.length() == 0)) {
                showingDialog.hideRefreshDialog();
                if (ff.isEmpty()) {
                    showingDialog.dismiss();
                    dialogeforCheckavilability("Error", "Please enter allergy name ", "ok");
                } else if (gg.isEmpty()) {
                    showingDialog.dismiss();
                    dialogeforCheckavilability("Error", "Please enter note ", "ok");
                } else
                    ApiCallDataController.getInstance().loadServerApiCall(
                            ApiCallDataController.getInstance().serverApi.addMultipleAllergyRecords(currentMedical.getAccessToken(), gsonObject), "insertAllergy");

            } else {
                ApiCallDataController.getInstance().loadServerApiCall(
                        ApiCallDataController.getInstance().serverApi.addMultipleAllergyRecords(currentMedical.getAccessToken(), gsonObject), "insertAllergy");

            }

        }


        public void recyclerView() {
            recordList = new ArrayList<>();

            adapter = new RecordRecyclerAdapter(recordList, CategoryActivity.this);
            recordRecyclerView.setLayoutManager(new LinearLayoutManager(CategoryActivity.this));
            recordRecyclerView.setAdapter(adapter);
            recordRecyclerView.setHasFixedSize(false);
        }

        private void accessInterfaceMethods() {
            ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
                @Override
                public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                    Log.e("teee", "jfjf");
                    if (curdOpetaton.equals("fetch")) {
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("famliyDiseases");
                            Log.e("afdasa", "" + jsonArray.toString());
                            PatientProfileDataController.getInstance().fetchPatientlProfileData();
                            FamilyTrackInfoDataController.getInstance().deleteTrackData(FamilyHistoryDataController.getInstance().allFamilyList);
                            FamilyHistoryDataController.getInstance().deleteFamilyData(PatientProfileDataController.getInstance().currentPatientlProfile);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Gson gson = new Gson();
                                    FamilyRecordServerObject userIdentifier = gson.fromJson(jsonArray.getJSONObject(i).toString(), FamilyRecordServerObject.class);
                                    ArrayList<TrackingServerObject> trackArray = userIdentifier.getTracking();
                                    Log.e("trackArray", "call" + userIdentifier.getRelationship());
                                    FamilyRecordServerDataController.getInstance().processfetchFamilyAddData(userIdentifier, trackArray);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (curdOpetaton.equals("insert")) {
                        Gson gson = new Gson();
                        FamilyRecordServerObject userIdentifier = gson.fromJson(params.toString(), FamilyRecordServerObject.class);
                        try {
                            Log.e("familydata", "call" + jsonObject.getString("family_history_record_id"));
                            userIdentifier.setFamily_history_record_id(jsonObject.getString("family_history_record_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("familydata", "call" + userIdentifier.getAge() + userIdentifier.getMoreInfo());
                        FamilyRecordServerDataController.getInstance().processAddFamilyAddData(userIdentifier);
                        dialog.dismiss();
                    } else if (curdOpetaton.equals("update")) {
                        Gson gson = new Gson();
                        FamilyRecordServerObject userIdentifier = gson.fromJson(params.toString(), FamilyRecordServerObject.class);
                        Log.e("updatefamilydata", "call" + userIdentifier.getAge() + userIdentifier.getMoreInfo());
                        FamilyRecordServerDataController.getInstance().processFamilyupdateData(userIdentifier);
                        dialog.dismiss();
                    } else if (curdOpetaton.equals("delete")) {
                        FamilyHistoryDataController.getInstance().deleteFamilyHistoryModelData(PatientProfileDataController.getInstance().currentPatientlProfile, FamilyHistoryDataController.getInstance().currentFamilyHistoryModel);
                    }
                    showingDialog.hideRefreshDialog();

                    familyHistoryArrayList = FamilyHistoryDataController.getInstance().allFamilyList;
                    if (!familyHistoryArrayList.isEmpty()) {
                        recordList.clear();
                        Log.e("ggg", "" + FamilyHistoryDataController.getInstance().allFamilyList);
                        for (int i = 0; i < familyHistoryArrayList.size(); i++) {
                            recordList.add(new RecordModel(familyHistoryArrayList.get(i).getDieseaseName(), familyHistoryArrayList.get(i).getRelation()));
                        }
                        adapter.notifyDataSetChanged();

                    } else {
                        recordList.clear();
                        Log.e("ggg", "" + FamilyHistoryDataController.getInstance().allFamilyList);
                        for (int i = 0; i < familyHistoryArrayList.size(); i++) {
                            recordList.add(new RecordModel(familyHistoryArrayList.get(i).getDieseaseName(), familyHistoryArrayList.get(i).getRelation()));
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void failureCallBack(String failureMsg) {
                    showingDialog.hideRefreshDialog();
                    Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
                }
            });
        }

        public class RecordRecyclerAdapter extends RecyclerView.Adapter<RecordRecyclerAdapter.RecordHolder> {
            List<RecordModel> recordModels;
            RecordModel model = new RecordModel();
            Context context;
            boolean deleteImg = false;

            public RecordRecyclerAdapter(List<RecordModel> recordModels, Context context) {

                Log.e("fjbjk", "kfnjnjf");
                this.recordModels = recordModels;
                this.context = context;
            }

            @Override
            public RecordRecyclerAdapter.RecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View recordView = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_recycler_item, parent, false);
                return new RecordRecyclerAdapter.RecordHolder(recordView);
            }

            @Override
            public void onBindViewHolder(final RecordRecyclerAdapter.RecordHolder holder, final int position) {

                Log.e("tfy", "" + recordModels.size());

                holder.txtRelation.setEnabled(false);
                holder.txtCondition.setEnabled(false);


                if (adding) {

                    Log.e("fdjhfjurjfbh", "fdjfn   " + position);

                    if (position == recordModels.size() - 1) {
                        //notifyDataSetChanged();


                        Log.e("fdjhjfbh", "fdjdfddfn   " + position);
                        adding = false;
                        holder.txtRelation.setEnabled(true);
                        holder.txtCondition.setEnabled(true);
                    }
                }
                Log.e("fdjhjfbh", "fdjfn   " + position);
                int a = position + 1;
                if (deleteImg) {
                    holder.txtCondition.setEnabled(true);
                    holder.txtRelation.setEnabled(true);
                    holder.imgDelete.setVisibility(View.VISIBLE);
                } else {
                  /*  holder.txtCondition.setEnabled(false);
                    holder.txtRelation.setEnabled(false);
                  */
                    if (adding) {
                        if (position == recordModels.size() - 1) {
                            //notifyDataSetChanged();
                            adding = false;
                            holder.txtRelation.setEnabled(true);
                            holder.txtCondition.setEnabled(true);
                        }
                    }
                    holder.imgDelete.setVisibility(View.GONE);
                }


                holder.txtSno.setText(String.valueOf(a));
                holder.txtCondition.setText(recordModels.get(position).getCondition());
                holder.txtRelation.setText(recordModels.get(position).getRelation());
                // if(model.isTxtBoolean()){}
                holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeAt(position);
                    }
                });

                btnRecordEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveBtnLayout.setVisibility(View.GONE);
                        cancelBtnLayout.setVisibility(View.VISIBLE);
                        btnRecordEdit.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.btn_bck_color));
                        btnRecordEdit.setTextColor(Color.parseColor("#ffffff"));
                        btnRecordSave.setTextColor(Color.parseColor("#3E454C"));
                        btnRecordSave.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));
                        btnRecordCancel.setTextColor(Color.parseColor("#3E454C"));
                        btnRecordCancel.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));

                        deleteImg = true;
                        notifyDataSetChanged();
                    }
                });
                btnRecordCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteImg = false;
                        Log.e("booleanm", "" + record);
                        notifyDataSetChanged();
                        holder.imgDelete.setVisibility(View.GONE);
                        cancelBtnLayout.setVisibility(View.GONE);
                        saveBtnLayout.setVisibility(View.VISIBLE);
                        btnRecordEdit.setTextColor(Color.parseColor("#3E454C"));
                        btnRecordEdit.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));
                        btnRecordCancel.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.btn_bck_color));
                        btnRecordCancel.setTextColor(Color.parseColor("#ffffff"));
                        showingDialog.dismiss();
                        if (!record) {
                            record = false;
                            Log.e("jdfsdjfd", "RRR");
                            recordList.clear();
                            familyHistoryArrayList = FamilyHistoryDataController.getInstance().allFamilyList;
                            Log.e("ggg", "" + FamilyHistoryDataController.getInstance().allFamilyList);
                            for (int i = 0; i < familyHistoryArrayList.size(); i++) {
                                recordList.add(new RecordModel(familyHistoryArrayList.get(i).getDieseaseName(), familyHistoryArrayList.get(i).getRelation()));
                            }
                            adapter.notifyDataSetChanged();

                        } else {
                            Log.e("jdfsdjfd", "JJJ");
                            recordList.clear();
                            AllergyDataController.getInstance().fetchAllergyData(PatientProfileDataController.getInstance().currentPatientlProfile);
                            allergieModels = AllergyDataController.getInstance().allAllergyList;
                            for (int i = 0; i < allergieModels.size(); i++) {
                                Log.e("recordOfAllergies", "" + allergieModels.get(i).getName());
                                recordList.add(new RecordModel(allergieModels.get(i).getName(), allergieModels.get(i).getNote()));
                            }
                            adapter.notifyDataSetChanged();
                        }

                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!record) {
                            record = false;
                            Log.e("jdfsdjfd", "RRR");
                            recordList.clear();
                            familyHistoryArrayList = FamilyHistoryDataController.getInstance().allFamilyList;
                            Log.e("ggg", "" + FamilyHistoryDataController.getInstance().allFamilyList);
                            for (int i = 0; i < familyHistoryArrayList.size(); i++) {
                                recordList.add(new RecordModel(familyHistoryArrayList.get(i).getDieseaseName(), familyHistoryArrayList.get(i).getRelation()));
                            }
                            adapter.notifyDataSetChanged();

                        } else {
                            Log.e("jdfsdjfd", "JJJ");
                            recordList.clear();
                            AllergyDataController.getInstance().fetchAllergyData(PatientProfileDataController.getInstance().currentPatientlProfile);
                            allergieModels = AllergyDataController.getInstance().allAllergyList;
                            for (int i = 0; i < allergieModels.size(); i++) {
                                Log.e("recordOfAllergies", "" + allergieModels.get(i).getName());
                                recordList.add(new RecordModel(allergieModels.get(i).getName(), allergieModels.get(i).getNote()));
                            }
                            adapter.notifyDataSetChanged();
                        }
                        btnCancel.setVisibility(View.GONE);
                        btnRecordEdit.setTextColor(Color.parseColor("#3E454C"));
                        btnRecordEdit.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));

                    }
                });
            }

            @Override
            public int getItemCount() {
                if (recordList.size() > 0) {
                    return recordList.size();
                } else {
                    return 0;
                }
            }

            public void removeAt(int position) {
                recordModels.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, recordModels.size());
            }

            public class RecordHolder extends RecyclerView.ViewHolder {
                TextView txtSno;
                ImageView imgDelete;
                EditText txtCondition, txtRelation;

                public RecordHolder(@NonNull View itemView) {
                    super(itemView);
                    txtCondition = itemView.findViewById(R.id.txt_item_condition);
                    txtRelation = itemView.findViewById(R.id.txt_item_relation);
                    txtSno = itemView.findViewById(R.id.txt_sno);
                 //   imgDelete = itemView.findViewById(R.id.img_item_delete);
                    txtCondition.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (txtCondition.hasFocus()) {
                                if (!s.toString().isEmpty()) {
                                    recordList.get(getAdapterPosition()).setCondition(s.toString());
                                }
                            }

                        }
                    });
                    txtRelation.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (txtRelation.hasFocus()) {
                                if (!s.toString().isEmpty()) {
                                    recordList.get(getAdapterPosition()).setRelation(s.toString());
                                }
                            }

                        }
                    });
                }
            }
        }


    }

}

