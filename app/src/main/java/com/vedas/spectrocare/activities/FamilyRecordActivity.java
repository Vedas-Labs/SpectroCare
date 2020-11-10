package com.vedas.spectrocare.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
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
import android.widget.NumberPicker;
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
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.Controllers.FamilyRecordServerDataController;
import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.Controllers.PhysicalServerObjectDataController;
import com.vedas.spectrocare.DataBase.FamilyHistoryDataController;
import com.vedas.spectrocare.DataBase.FamilyTrackInfoDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBaseModels.FamilyHistoryModel;
import com.vedas.spectrocare.DataBaseModels.FamilyTrackInfoModel;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.ServerApiModel.FamilyRecordServerObject;
import com.vedas.spectrocare.ServerApiModel.TrackingServerObject;
import com.vedas.spectrocare.SingleTapDetector;
import com.vedas.spectrocare.model.RecordModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FamilyRecordActivity extends AppCompatActivity implements MedicalPersonaSignupView {
    EditText edtCondition, edtRelation, edtDescription;
    TextView edtAge;
    ArrayList<FamilyHistoryModel> familyHistoryArrayList = new ArrayList<>();
    ArrayList<Integer> colorsArray = new ArrayList<>();
    RelativeLayout imgBackBtn;
    FloatingActionButton btnAdd;
    ImageView imgEdt;
    NumberPicker numberPickerAge, agePicker;
    RecyclerView familyListView;
    String selectedAge = "0";
    String selectedTitle = "y";
    Button nextBn, btnSaveRecords;
    BottomSheetDialog dialog;
    FamilyRecordAdapter familyRecordAdapter;
    RefreshShowingDialog showingDialog;
    JSONObject params = new JSONObject();
    JSONObject object;
    String ff, gg;
    List<RecordModel> recordList;
    TextView txtEmptyFamily;
    int lastAction;
    private float dX, dY;
    GestureDetector gestureDetector;
    List<RecordModel> recordModels;
    Button addFamilyDetails;
    RecordModel model;
    JSONArray array;
    JSONObject jsonObject;
    int poss;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_record);
        imgBackBtn = findViewById(R.id.img_back);
        txtEmptyFamily = findViewById(R.id.text_family_disc);
        btnSaveRecords = findViewById(R.id.btn_save_records);
        gestureDetector = new GestureDetector(this, new SingleTapDetector());
        FamilyRecordServerDataController.getInstance().fillContent(getApplicationContext());
        showingDialog = new RefreshShowingDialog(FamilyRecordActivity.this);
        imgBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        PersonalInfoController.getInstance().fillContent(getApplicationContext());
        PersonalInfoController.getInstance().loadAgeUnitaArray();
        PersonalInfoController.getInstance().loadHeightValuesArray();

        btnAdd = findViewById(R.id.add_family_record);
        nextBn = findViewById(R.id.btn_next);
        familyListView = findViewById(R.id.family_recycler_view);
        loadRecyclerView();

        if (isConn()) {
            showingDialog.showAlert();
            Log.e("vcvcv", "duck");
            FamilyRecordServerDataController.getInstance().newFamilyFetchApi();
            //  FamilyRecordServerDataController.getInstance().familyFetchApiCall();
        }
        accessInterfaceMethods();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                FamilyHistoryDataController.getInstance().currentFamilyHistoryModel = null;
                alertDailog();
            }
        });
        btnSaveRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 array = new JSONArray();
                // array.put(recordModel);
                showingDialog.showAlert();
                for (int i = 0; i < recordList.size(); i++) {
                     jsonObject = new JSONObject();
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
            }
        });

        // this code is for drag and drop ,click events of floating button

        btnAdd.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    FamilyHistoryDataController.getInstance().currentFamilyHistoryModel = null;
                    alertDailog();
                } else {
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

    /*
        private void accessInterfaceMethods() {

            ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
                @Override
                public void successCallBack(JSONObject jsonObject, String curdOpetaton) {

                    if (curdOpetaton.equals("fetch")) {
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("family_history_records");
                            Log.e("jsonArray","array"+jsonArray);
                            PatientProfileDataController.getInstance().fetchPatientlProfileData();
                            FamilyTrackInfoDataController.getInstance().deleteTrackData(FamilyHistoryDataController.getInstance().allFamilyList);
                            FamilyHistoryDataController.getInstance().deleteFamilyData(PatientProfileDataController.getInstance().currentPatientlProfile);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Gson gson = new Gson();
                                    FamilyRecordServerObject userIdentifier = gson.fromJson(jsonArray.getJSONObject(i).toString(), FamilyRecordServerObject.class);
                                    ArrayList<TrackingServerObject> trackArray = userIdentifier.getTracking();
                                    Log.e("trackArray", "call" + trackArray.size());
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
    */
    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                Log.e("teee", "jfjf");
                if (curdOpetaton.equals("fetch")) {
                    Log.e("dddd","ffff"+jsonObject.toString());

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
                        Log.e("eeerrroooo","rrrrr"+e.getMessage());
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
                    // dialog.dismiss();
                } else if (curdOpetaton.equals("update")) {
                    Gson gson = new Gson();
                    FamilyRecordServerObject userIdentifier = gson.fromJson(params.toString(), FamilyRecordServerObject.class);
                    Log.e("updatefamilydata", "call" + userIdentifier.getAge() + userIdentifier.getMoreInfo());
                    FamilyRecordServerDataController.getInstance().processFamilyupdateData(userIdentifier);
                    //  dialog.dismiss();
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
                    familyRecordAdapter.notifyDataSetChanged();

                } else {
                    recordList.clear();
                    Log.e("ggg", "" + FamilyHistoryDataController.getInstance().allFamilyList);
                    for (int i = 0; i < familyHistoryArrayList.size(); i++) {
                        recordList.add(new RecordModel(familyHistoryArrayList.get(i).getDieseaseName(), familyHistoryArrayList.get(i).getRelation()));
                    }
                    familyRecordAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failureCallBack(String failureMsg) {
                showingDialog.hideRefreshDialog();
                Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadRecyclerView() {
        PatientProfileDataController.getInstance().fetchPatientlProfileData();
        FamilyHistoryDataController.getInstance().fetchFamilyData(PatientProfileDataController.getInstance().currentPatientlProfile);
       /* if (FamilyHistoryDataController.getInstance().fetchFamilyData(PatientProfileDataController.getInstance().currentPatientlProfile).isEmpty())
            txtEmptyFamily.setVisibility(View.VISIBLE);
        else
            txtEmptyFamily.setVisibility(View.GONE);
*/
        familyHistoryArrayList = FamilyHistoryDataController.getInstance().allFamilyList;
        if (familyHistoryArrayList.size() > 0) {
            for (int i = 0; i < familyHistoryArrayList.size(); i++) {
                colorsArray.add(PersonalInfoController.getInstance().getRandomColor());
            }
        }
        recordList = new ArrayList<>();

        //  familyRecordAdapter = new FamilyRecordAdapter(this, familyHistoryArrayList);
        familyRecordAdapter = new FamilyRecordAdapter(recordList, this);
        familyListView.setHasFixedSize(true);
        familyListView.setLayoutManager(new LinearLayoutManager(this));
        familyListView.setAdapter(familyRecordAdapter);
        familyRecordAdapter.notifyDataSetChanged();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void alertDailog() {

        View view = getLayoutInflater().inflate(R.layout.family_record_alert, null);
        dialog = new BottomSheetDialog(Objects.requireNonNull(FamilyRecordActivity.this), R.style.BottomSheetDialogTheme);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();
       /* LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.family_record_alert, null);
        dialog.setContentView(view);*/
               /* dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);*/
        imgEdt = dialog.findViewById(R.id.edit);
        edtCondition = dialog.findViewById(R.id.edt_medical_condition);
        edtRelation = dialog.findViewById(R.id.edt_relation);
        edtAge = dialog.findViewById(R.id.edt_spinner_age);
        edtDescription = dialog.findViewById(R.id.edt_add_description);
        final TextView title = dialog.findViewById(R.id.title);
        ImageView imageView = dialog.findViewById(R.id.cancel);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        addFamilyDetails = dialog.findViewById(R.id.btn_add_family_details);


        if (FamilyHistoryDataController.getInstance().currentFamilyHistoryModel != null) {
            FamilyHistoryModel objModel = FamilyHistoryDataController.getInstance().currentFamilyHistoryModel;
            imgEdt.setVisibility(View.VISIBLE);
            addFamilyDetails.setVisibility(View.GONE);
            title.setText("Family record");
            edtCondition.setText(objModel.getMedicalCondition());
            edtRelation.setText(objModel.getRelation());
            edtAge.setText(objModel.getAge());
            edtDescription.setText(objModel.getDiscription());
            edtAge.setEnabled(false);
            edtRelation.setEnabled(false);
            edtCondition.setEnabled(false);
            edtDescription.setEnabled(false);
            edtCondition.setTextColor(Color.parseColor("#a4b3b7"));
            edtRelation.setTextColor(Color.parseColor("#a4b3b7"));
            edtAge.setTextColor(Color.parseColor("#a4b3b7"));
            edtDescription.setTextColor(Color.parseColor("#a4b3b7"));
        }
        imgEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtAge.setEnabled(true);
                edtRelation.setEnabled(true);
                edtCondition.setEnabled(true);
                edtDescription.setEnabled(true);
                edtCondition.setTextColor(Color.parseColor("#3E454C"));
                edtRelation.setTextColor(Color.parseColor("#3E454C"));
                edtAge.setTextColor(Color.parseColor("#3E454C"));
                edtDescription.setTextColor(Color.parseColor("#3E454C"));

                addFamilyDetails.setVisibility(View.VISIBLE);
                imgEdt.setVisibility(View.GONE);
                addFamilyDetails.setText("Update");
                title.setText("Update family record");
            }
        });
        edtAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtAge.setInputType(InputType.TYPE_NULL);
                loadAgePicker();
            }
        });

        addFamilyDetails.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                validationsOfFamilyRecords();
            }
        });

        /*dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);*/
    }


    private void loadAgePicker() {
        final Dialog mod = new Dialog(FamilyRecordActivity.this);
        mod.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mod.setContentView(R.layout.alert_dailog);
        mod.show();
        TextView txtTitle = (TextView) mod.findViewById(R.id.title);
        txtTitle.setText("Select Age");
        numberPickerAge = mod.findViewById(R.id.value);


        if (edtAge.getText().toString().length() > 0 && edtAge.getText().toString().contains("y")) {
            String weightArray[] = edtAge.getText().toString().split(" ");
            selectedAge = weightArray[0];
            selectedTitle = weightArray[1];
        } else {
            selectedAge = edtAge.getText().toString();
        }
        Log.e("selectAge", "call" + selectedAge);
        ////for value array
        numberPickerAge.setDisplayedValues(null);
        int index = PersonalInfoController.getInstance().ageValuesArray.indexOf(selectedAge);
        numberPickerAge.setMinValue(0);
        numberPickerAge.setWrapSelectorWheel(false);
        numberPickerAge.setMaxValue(PersonalInfoController.getInstance().ageValuesArray.size() - 1);
        String[] mStringArray = new String[PersonalInfoController.getInstance().ageValuesArray.size()];
        mStringArray = PersonalInfoController.getInstance().ageValuesArray.toArray(mStringArray);
        numberPickerAge.setDisplayedValues(mStringArray);
        numberPickerAge.setValue(index);
        //for measure
        agePicker = (NumberPicker) mod.findViewById(R.id.name);
        agePicker.setWrapSelectorWheel(false);
        agePicker.setMaxValue(0);
        agePicker.setMinValue(0);
        agePicker.setDisplayedValues(PersonalInfoController.getInstance().ageUnitsArray);

        numberPickerAge.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedAge = PersonalInfoController.getInstance().ageValuesArray.get(newVal);
            }
        });


        Button btnOk = mod.findViewById(R.id.ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mod.dismiss();
                edtAge.setText(selectedAge + " " + selectedTitle);
            }
        });
        Button btnCancel = mod.findViewById(R.id.cancle);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mod.dismiss();
            }
        });
    }

    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter = new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(FamilyRecordActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void validationsOfFamilyRecords() {
        if (edtCondition.getText().toString().isEmpty())
            dialogeforCheckavilability("Error", "Please select medical condition", "Ok");
        else if (edtRelation.getText().toString().isEmpty())
            dialogeforCheckavilability("Error", "Please select relation ", "Ok");
        else {

            model = new RecordModel();
            model.setCondition(edtCondition.getText().toString());
            model.setRelation(edtRelation.getText().toString());
            Log.e("addddddufghyjfj", "dddd" + addFamilyDetails.getText().toString());

            if (addFamilyDetails.getText().toString().equals("Add")) {
                Log.e("daa","ddaa");
                recordList.add(model);
            } else {
                recordModels.set(poss, model);

            }
            familyRecordAdapter.notifyDataSetChanged();
            dialog.dismiss();
            /*if (isConn()) {
                showingDialog.showAlert();
                JSONArray array = new JSONArray();
                MedicalProfileDataController.getInstance().fetchMedicalProfileData();
                MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
                params = new JSONObject();
                try {
                    if (FamilyHistoryDataController.getInstance().currentFamilyHistoryModel != null) {
                        FamilyHistoryModel objModel = FamilyHistoryDataController.getInstance().currentFamilyHistoryModel;
                        params.put("family_history_record_id", objModel.getFamilyRecordId());
                    }

                    params.put("hospital_reg_num", currentMedical.getHospital_reg_number());
                    params.put("medical_personnel_id", currentMedical.getMedical_person_id());
                    params.put("medical_record_id", PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
                    params.put("patientID", PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
                    params.put("healthCondition", edtCondition.getText().toString());
                    params.put("relationship", edtRelation.getText().toString());
                    params.put("age", selectedAge);
                    params.put("moreInfo", edtDescription.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonParser jsonParser = new JsonParser();
                JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
                Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
                if (FamilyHistoryDataController.getInstance().currentFamilyHistoryModel != null) {
                    Log.e("notThis","Update");

                    ApiCallDataController.getInstance().loadServerApiCall(
                            ApiCallDataController.getInstance().serverApi.updateFamilyHistory(currentMedical.getAccessToken(), gsonObject), "update");
                } else {
                    Log.e("notThis","Add");

                                      ApiCallDataController.getInstance().loadServerApiCall(
                            ApiCallDataController.getInstance().serverApi.addFamilyHistory1(currentMedical.getAccessToken(), gsonObject), "insert");
                }
            } else {
                dialogeforCheckavilability("Error", "No Internet connection", "Ok");
            }*/
        }
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


        if (!(list.length() == 0)) {
            Log.e("asdfaad", "adf" + ff);
            showingDialog.hideRefreshDialog();
            ApiCallDataController.getInstance().loadServerApiCall(
                    ApiCallDataController.getInstance().serverApi.addFamilyHistoryMultiple(currentMedical.getAccessToken(), gsonObject), "insert");

            /*if (ff.isEmpty()) {

                dialogeforCheckavilability("Error", "Please enter disease ", "ok");
            } else if (gg.isEmpty()) {
                showingDialog.dismiss();
                dialogeforCheckavilability("Error", "Please enter relation ", "ok");
            } else
                ApiCallDataController.getInstance().loadServerApiCall(
                        ApiCallDataController.getInstance().serverApi.addFamilyHistoryMultiple(currentMedical.getAccessToken(), gsonObject), "insert");
       */
        } else {
            Log.e("resulteee", "ddaaaa");

            showingDialog.hideRefreshDialog();
            ApiCallDataController.getInstance().loadServerApiCall(
                    ApiCallDataController.getInstance().serverApi.addFamilyHistoryMultiple(currentMedical.getAccessToken(), gsonObject), "insert");

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
    public void onResume() {
        super.onResume();
        Log.e("onResume", "call");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(PhysicalServerObjectDataController.MessageEvent event) {
        Log.e("addFamilyData", "" + event.message);
        String resultData = event.message.trim();
        FamilyHistoryDataController.getInstance().fetchFamilyData(PatientProfileDataController.getInstance().currentPatientlProfile);
        familyHistoryArrayList = FamilyHistoryDataController.getInstance().allFamilyList;
        if (resultData.equals("deleteFamilyData")) {
            loadRecyclerView();
        }
    }

    public class FamilyRecordAdapter extends RecyclerView.Adapter<FamilyRecordAdapter.FamilyRecordHolder> {

        Context context;
        // List<RecordModel> recordModels;
        RecordModel model = new RecordModel();
        ArrayList<FamilyHistoryModel> familyRecordModelList;

        /* public FamilyRecordAdapter(Context context, ArrayList<FamilyHistoryModel> familyRecordModelList) {
             this.context = context;
             this.familyRecordModelList = familyRecordModelList;
         }*/
        public FamilyRecordAdapter(List<RecordModel> mainRecordModels, Context context) {

            Log.e("fjbjk", "kfnjnjf");
            recordModels = mainRecordModels;
            this.context = context;
        }


        @Override
        public FamilyRecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View familyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_recycler_item, parent, false);
            return new FamilyRecordHolder(familyView);
        }

        @Override
        public void onBindViewHolder(@NonNull final FamilyRecordHolder holder, final int position) {
            Log.e("kuchNahi", "haiii");
            Log.e("tfy", "" + recordModels.size());
            int a = position + 1;
            // final FamilyHistoryModel familyRecordModel = familyRecordModelList.get(position);
           /* holder.medicalIssue.setText(familyRecordModel.getMedicalCondition());
            holder.memberAge.setText(familyRecordModel.getAge()+" Y");
            holder.familyDisc.setText(familyRecordModel.getDiscription());
            holder.relationshipTxt.setText(familyRecordModel.getRelation());
           */// holder.colorLayout.setBackgroundColor(colorsArray.get(position));

            holder.txtCondition.setText(recordModels.get(position).getCondition());
            Log.e("texting", "empty" + recordModels.get(position).getCondition());
            holder.txtRelation.setText(recordModels.get(position).getRelation());
            holder.txtSno.setText(String.valueOf(a));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("isClicked", "true");

                    poss = holder.getAdapterPosition();

                    //  dialog.show();
                    alertDailog();
                    addFamilyDetails.setText("Update");
                    btnSaveRecords.setText("Save changes");
                    edtCondition.setText(recordModels.get(position).getCondition());
                    edtRelation.setText(recordModels.get(position).getRelation());
                }
            });
            holder.imgRecordDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeAt(position);
                    //  Toast.makeText(context, "click ayyaindi royyy", Toast.LENGTH_SHORT).show();
                }
            });


          /*  ArrayList<FamilyTrackInfoModel> trackInfoModels = FamilyTrackInfoDataController.getInstance().fetchTrackData(reco);
            if (trackInfoModels.size() > 0) {
                try {
                    FamilyTrackInfoModel trackInfoModel = trackInfoModels.get(trackInfoModels.size() - 1);
                    String date = PersonalInfoController.getInstance().convertTimestampToMinutes(trackInfoModel.getDate());
                    //  String sourceString = "<b>" + date + "</b> " + " by " +"<b>"+ trackInfoModel.getByWhom()+"</b>" +"\n"+"(Id: "+trackInfoModel.getByWhomId()+")";
                    String sourceString = "<b>" + trackInfoModel.getByWhom() + "</b> " + "on " + "<b>" + date + "</b>" *//*+"\n"+"(Id: "+trackInfoModel.getByWhomId()+")"*//*;

                    holder.medicalRecord.setText(Html.fromHtml(sourceString));
                    //holder.medicalRecord.setText("Last Update :"+" "+date+" by"+" "+trackInfoModel.getByWhom()+" "+"(Id: "+trackInfoModel.getByWhomId()+")");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
          */
            holder.viewImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FamilyHistoryDataController.getInstance().currentFamilyHistoryModel = familyRecordModelList.get(position);
                    alertDailog();
                }
            });
            holder.editImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FamilyHistoryDataController.getInstance().currentFamilyHistoryModel = familyRecordModelList.get(position);
                    alertDailog();
                }
            });
            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context wrapper = new ContextThemeWrapper(FamilyRecordActivity.this, R.style.MyPopupOtherStyle);
                    PopupMenu popup = new PopupMenu(wrapper, holder.more);
                    popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getTitle().equals("View")) {
                                FamilyHistoryDataController.getInstance().currentFamilyHistoryModel = familyRecordModelList.get(position);
                                alertDailog();
                            } else {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                                builder.setCancelable(false);
                                builder.setTitle("Delete");
                                builder.setMessage("Do you want to delete this Record ?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FamilyHistoryDataController.getInstance().currentFamilyHistoryModel = familyRecordModelList.get(position);
                                        FamilyHistoryModel objModel = familyRecordModelList.get(position);
                                        JSONObject params = new JSONObject();
                                        try {
                                            params.put("hospital_reg_num", MedicalProfileDataController.getInstance().currentMedicalProfile.getHospital_reg_number());
                                            params.put("medical_personnel_id", objModel.getMedicalPersonId());
                                            params.put("patientID", objModel.getPatientId());
                                            params.put("medical_record_id", objModel.getMedicalRecordId());
                                            params.put("family_history_record_id", objModel.getFamilyRecordId());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        JsonParser jsonParser = new JsonParser();
                                        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
                                        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
                                        if (isConn()) {
                                            showingDialog.showAlert();
                                            ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.deleteFamilyHistory1(
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
                }
            });
            holder.deleteImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            /*holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
*/
           /* holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });*/
        }

        @Override
        public int getItemCount() {
            if (recordList.size() > 0) {
                return recordList.size();
            } else {
                Log.e("kuchNahi", "haiii");
                return 0;
            }
        }

        public void removeAt(int position) {
            recordModels.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, recordModels.size());
        }

        public class FamilyRecordHolder extends RecyclerView.ViewHolder {
            TextView medicalIssue, relationshipTxt, memberAge, familyDisc, medicalRecord;
            ImageView viewImg, editImg, deleteImg, attachImg, imgRecordDelete;
            RelativeLayout colorLayout, more;
            TextView txtSno;
            ImageView imgDelete;
            EditText txtCondition, txtRelation;
            CardView cardView;


            public FamilyRecordHolder(@NonNull View itemView) {
                super(itemView);
                txtCondition = itemView.findViewById(R.id.txt_item_condition);
                txtRelation = itemView.findViewById(R.id.txt_item_relation);
                txtSno = itemView.findViewById(R.id.txt_sno);
                //  imgDelete = itemView.findViewById(R.id.img_item_delete);
                cardView = itemView.findViewById(R.id.cardView);
                imgRecordDelete = itemView.findViewById(R.id.img_record_delete);
                txtCondition.setEnabled(false);
                txtRelation.setEnabled(false);

                medicalIssue = itemView.findViewById(R.id.medical_issue);
                relationshipTxt = itemView.findViewById(R.id.relationship);
                familyDisc = itemView.findViewById(R.id.family_description);
                memberAge = itemView.findViewById(R.id.family_member_age);
                medicalRecord = itemView.findViewById(R.id.medical_record);
                colorLayout = itemView.findViewById(R.id.color);
                viewImg = itemView.findViewById(R.id.img_view);
                deleteImg = itemView.findViewById(R.id.img_delete);
                editImg = itemView.findViewById(R.id.img_edit);
                attachImg = itemView.findViewById(R.id.img_attach);
                more = itemView.findViewById(R.id.layout_more);
            }
        }

    }
}
