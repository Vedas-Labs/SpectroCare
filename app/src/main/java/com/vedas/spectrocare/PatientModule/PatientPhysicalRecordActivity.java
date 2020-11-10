package com.vedas.spectrocare.PatientModule;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.Controllers.PhysicalServerObjectDataController;
import com.vedas.spectrocare.DataBase.BmiDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBase.PhysicalCategoriesDataController;
import com.vedas.spectrocare.DataBase.PhysicalExamDataController;
import com.vedas.spectrocare.DataBase.PhysicalExamTrackInfoDataController;
import com.vedas.spectrocare.DataBaseModels.BMIModel;
import com.vedas.spectrocare.DataBaseModels.PhysicalCategoriesRecords;
import com.vedas.spectrocare.DataBaseModels.PhysicalTrackInfoModel;
import com.vedas.spectrocare.PatientServerApiModel.FamilyTrackingInfo;
import com.vedas.spectrocare.PatinetControllers.PatientFamilyDataController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.ServerApiModel.BodyIndexServerObject;
import com.vedas.spectrocare.ServerApiModel.PhysicalExamServerObject;
import com.vedas.spectrocare.ServerApiModel.PhysicalRecordServerObject;
import com.vedas.spectrocare.activities.MedicalPersonaSignupView;
import com.vedas.spectrocare.activities.MedicalPersonalSignupPresenter;
import com.vedas.spectrocare.activities.PhysicalExamRecordActivity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PatientPhysicalRecordActivity extends AppCompatActivity implements MedicalPersonaSignupView {
    EditText edtHeight,edtWeight,edt_waistline,edt_blood_pressure,edt_bmi,edt_comment,edtOther;
    Button btncircle;
    Button btnBodyIndex, btnPhysicalRecord;
    PatientPhysicalRecordAdapter recordAdapter;
    ImageView imageView,imgBtn;
    AlertDialog.Builder dialog1;
    Boolean imageVV;
    RecyclerView physicalRecordView;
    NumberPicker numberPickerWaist, measurePickerwaist;
    String selectedHeightMeasure="CM", selectedHeightValue="168";
    String selectedWeightMeasure="Kgs", selectedWeightValue="70";
    NumberPicker numberPickerHeight, measurePickerHeight;
    // FloatingActionButton btnAddTitle;
    EditText edtTitle;
    String selectedWaistLine="25";
    String selectedWaistMeasure="CM";
    String title,patientExamId;
    ArrayList<PhysicalRecordServerObject> recordList = new ArrayList<>();
    RelativeLayout bodyIndexLayout, physicalRecordLayout;
    NumberPicker numberPickerWeight, measurePickerWeight;
    RelativeLayout rl_bmicondition;
    private String mCurrentPhotoPath;
    TextView txt_bmiconditon;
    AlertDialog dialog;
    ArrayList<PhysicalRecordServerObject> serverObjectArrayList;
    BodyIndexServerObject bodyIndex;
    ImageView imgButton,imgEdtBtn;
    RelativeLayout layoutSugession;
    Button btn_done;
    List<String> categoriesArray = Arrays.asList(new String[]{"General Appearance", "Vital Signs", "Head", "Heent", "Neck", "Chest and Lungs",
            "Cardiovascular", "Abdomen", "Genitourinary", "Rectal", "Musculoskeletal", "Lymph Nodes",
            "Extremities/Skin", "Neurological"});

    RefreshShowingDialog showingDialog;
    boolean isUpdateApi=false;
    TextView txt_updatetxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_physical_record);
        PersonalInfoController.getInstance().fillContent(getApplicationContext());
        PersonalInfoController.getInstance().loadAllUnitsArrays();
        PersonalInfoController.getInstance().loadHeightValuesArray();
        PersonalInfoController.getInstance().loadWeightValuesArray();
        castingViews();
        clickEvents();
    }
    public void castingViews(){
        showingDialog = new RefreshShowingDialog(PatientPhysicalRecordActivity.this);
        bodyIndexLayout = findViewById(R.id.body_index_layout);
        physicalRecordLayout = findViewById(R.id.physical_record_layout);
        btnBodyIndex = findViewById(R.id.btn_body_index);
        btnPhysicalRecord = findViewById(R.id.btn_physical_record);
        layoutSugession = findViewById(R.id.sugession_layout);
        imgEdtBtn = findViewById(R.id.img_edt_btn);
        imgButton = findViewById(R.id.img_back_btn);
        edtHeight = findViewById(R.id.edt_height);
        edtWeight = findViewById(R.id.edt_weight);
        edt_waistline= findViewById(R.id.edt_waistline);
        edt_bmi= findViewById(R.id.edt_bmi);//ed_comment
        edt_comment= findViewById(R.id.ed_comment);//ed_comment
        edtOther =findViewById(R.id.edt_others);
        edt_blood_pressure= findViewById(R.id.edt_blood_pressure);
        rl_bmicondition= findViewById(R.id.conditon);
        btncircle= findViewById(R.id.image_con);
        txt_bmiconditon= findViewById(R.id.txt_condition);
        imageView = findViewById(R.id.circular_image);
        recordAdapter = new PatientPhysicalRecordAdapter(PatientPhysicalRecordActivity.this);
        btn_done = findViewById(R.id.btn_done);
        txt_updatetxt=findViewById(R.id.updatetxt);
        bodyIndex = new BodyIndexServerObject();
        for (int i=0; i<categoriesArray.size();i++){
            title = categoriesArray.get(i);
            PhysicalRecordServerObject physicalExamination = new PhysicalRecordServerObject(title, "Not Examined", "");
            recordList.add(physicalExamination);
        }

    }
    public void clickEvents(){
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhysicalServerObjectDataController.getInstance().isFromStaring=false;
                finish();
            }
        });

        edtWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadWeightSpinner();
            }
        });

        edtHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHeightPickerValue();
            }
        });

        edt_waistline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadWaistLinePicker();
            }
        });



        btn_done.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                validations();
            }
        });
        loadData();
        bodyIndexRecord();
        physicalRecord();
    }
    public void bodyIndexRecord(){
        btnBodyIndex.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                btnBodyIndex.bringToFront();
                btnBodyIndex.setTextColor(Color.parseColor("#ffffff"));
                btnPhysicalRecord.setTextColor(Color.parseColor("#3E454C"));
                btnBodyIndex.setBackground(PatientPhysicalRecordActivity.this.getResources().getDrawable(R.drawable.btn_bck_color));
                btnPhysicalRecord.setBackground(PatientPhysicalRecordActivity.this.getResources().getDrawable(R.drawable.btn_bck));
                bodyIndexLayout.setVisibility(View.VISIBLE);
                physicalRecordLayout.setVisibility(View.GONE);
                layoutSugession.setVisibility(View.GONE);

            }
        });
    }
    public void physicalRecord(){
        btnPhysicalRecord.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                recyclerItems();
                btnPhysicalRecord.bringToFront();
                btnPhysicalRecord.setTextColor(Color.parseColor("#ffffff"));
                btnBodyIndex.setTextColor(Color.parseColor("#3E454C"));
                btnPhysicalRecord.setBackground(PatientPhysicalRecordActivity.this.getResources().getDrawable(R.drawable.btn_bck_color));
                btnBodyIndex.setBackground(PatientPhysicalRecordActivity.this.getResources().getDrawable(R.drawable.btn_bck));
                physicalRecordLayout.setVisibility(View.VISIBLE);
                bodyIndexLayout.setVisibility(View.GONE);
                FloatingActionButton btnAddTitle = findViewById(R.id.btn_add_physical_record);
                layoutSugession.setVisibility(View.VISIBLE);


                btnAddTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog = new Dialog(PatientPhysicalRecordActivity.this);
                        dialog.setCancelable(true);
                        View viewAlert = getLayoutInflater().inflate(R.layout.physical_record_alert, null);
                        dialog.setContentView(viewAlert);
                        edtTitle = dialog.findViewById(R.id.edt_title);
                        Button btnAdd = dialog.findViewById(R.id.add_title);
                        Button btnnnn = viewAlert.findViewById(R.id.btn_no_thanks);

                        Log.e("array",""+title);


                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                title = edtTitle.getText().toString();
                                if (!title.isEmpty()) {
                                    //     txt.setVisibility(View.GONE);
                                    PhysicalRecordServerObject physicalExamination = new PhysicalRecordServerObject(title, "Not Examined", "Add Disc");
                                    recordList.add(physicalExamination);
                                    recordAdapter.notifyDataSetChanged();
                                } else {
                                    LayoutInflater inflater = getLayoutInflater();
                                    View layout = inflater.inflate(R.layout.toast_layout,
                                            (ViewGroup) findViewById(R.id.custom_toast_container));

                                    TextView text = (TextView) layout.findViewById(R.id.text);
                                    text.setText("Category is empty");
                                    Toast toast = new Toast(PatientPhysicalRecordActivity.this);
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
              //  recyclerItems();

            }
        });

    }
    public void loadData(){
        Intent intent = getIntent();
        if (intent.hasExtra("position")){
            isUpdateApi = true;
            int jj = Integer.parseInt(intent.getStringExtra("position"));
           bodyIndex = PatientFamilyDataController.getInstance().getPhysicalRecordModel().getPhysical_exam_records()
                   .get(jj).getBodyIndex();
           patientExamId = PatientFamilyDataController.getInstance().getPhysicalRecordModel().getPhysical_exam_records()
                   .get(jj).getPhysical_exam_id();
           edtHeight.setText(bodyIndex.getHeight());
           edt_waistline.setText(bodyIndex.getWaistline());
           edtWeight.setText(bodyIndex.getWeight());
           edt_bmi.setText(bodyIndex.getBmi());
           edt_blood_pressure.setText(bodyIndex.getBloodPressure());
           edtOther.setText(PatientFamilyDataController.getInstance().getPhysicalRecordModel()
                   .getPhysical_exam_records().get(jj).getOther());
           edt_comment.setText(PatientFamilyDataController.getInstance().getPhysicalRecordModel()
                   .getPhysical_exam_records().get(jj).getPhysicianCommentsOrRecomdations());
            ArrayList<PhysicalRecordServerObject> serverObjectArrayList =PatientFamilyDataController.getInstance().getPhysicalRecordModel().getPhysical_exam_records()
                    .get(jj).getPhysicalExamination();
            Log.e("lalal","dfd"+serverObjectArrayList.size());
            recordList.clear();
            recordList=serverObjectArrayList;
            recordAdapter.notifyDataSetChanged();
            conversionOfHeightAndWeightForBmi();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void  validations(){
        if (edtHeight.getText().toString().isEmpty())
            dialogeforCheckavilability("Error", "Please select patient height", "Ok");
        else if (edtWeight.getText().toString().isEmpty())
            dialogeforCheckavilability("Error", "Please select patient weight", "Ok");
        else if (edt_waistline.getText().toString().isEmpty())
            dialogeforCheckavilability("Error", "Please select patient waistline", "Ok");
        else if (edt_bmi.getText().toString().isEmpty())
            dialogeforCheckavilability("Error", "Please select patient BMI", "Ok");
        else {
            if (isConn()){
                showingDialog.showAlert();
                loadAddApi();
            }else {
                showingDialog.hideRefreshDialog();
                dialogeforCheckavilability("Error", "No Internet connection", "Ok");
            }

/*            if(isConn()) {
                showingDialog.showAlert();
                currentBmiModel.setHeight(edtHeight.getText().toString());
                currentBmiModel.setWeight(edtWeight.getText().toString());
                currentBmiModel.setBmi(edt_bmi.getText().toString());
                currentBmiModel.setWaistLine(edt_waistline.getText().toString());
                currentBmiModel.setBloodPressure(edt_blood_pressure.getText().toString());
                if(isUpdateApi) {
                    PhysicalExamDataController.getInstance().currentPhysicalExamsData.setOther(edtOther.getText().toString());
                    PhysicalExamDataController.getInstance().currentPhysicalExamsData.setPhysicianComments(edt_comment.getText().toString());
                    currentBmiModel.setPhysicalExamId(PhysicalExamDataController.getInstance().currentPhysicalExamsData.getPhysicalExamId());
                }
                PhysicalServerObjectDataController.getInstance().addPhysicalExamServerApi(edtOther.getText().toString(),
                        edt_comment.getText().toString(),isUpdateApi,currentBmiModel, recordList);
                //Log.e("comment",""+ PhysicalExamDataController.getInstance().currentPhysicalExamsData.getOther());

            }else {
                dialogeforCheckavilability("Error", "No Internet connection", "Ok");
            }*/
        }
    }
    private void loadWeightSpinner(){
        final Dialog mod = new Dialog(PatientPhysicalRecordActivity.this);
        mod.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mod.setContentView(R.layout.alert_dailog);
        mod.show();
        numberPickerWeight = mod.findViewById(R.id.value);

        if (edtWeight.getText().toString().length() > 0 && edtWeight.getText().toString().contains("Kgs")) {
            String weightarray[] = edtWeight.getText().toString().split(" ");
            selectedWeightValue = weightarray[0];
            selectedWeightMeasure = "Kgs";
            Log.e("zzzzzzzzzzzz", "call" + selectedWeightValue + selectedWeightMeasure);

        } /*else {
                String weightarray[] = edtWeight.getText().toString().split(" ");
                selectedWeightValue = weightarray[0];
                selectedWeightMeasure = "Lbs";
                Log.e("xxxxxxxxxxx", "call" + selectedWeightValue + selectedWeightMeasure);
            }*/
        weightmeasurePicker(mod);
        weightnumberPicker(mod);
        Button btnOk = mod.findViewById(R.id.ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mod.dismiss();
                Log.e("aaaaaaaaaaaaa","call"+selectedWeightValue+" "+selectedWeightMeasure);

                edtWeight.setText(selectedWeightValue+" "+selectedWeightMeasure);
                conversionOfHeightAndWeightForBmi();
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
    public void loadAddApi(){
        PhysicalExamServerObject examServerObject = new PhysicalExamServerObject();
        examServerObject.setHospital_reg_num(PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
        examServerObject.setByWhom("patient");
        examServerObject.setByWhomID(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
        examServerObject.setMedical_record_id(PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
        examServerObject.setPatientID(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
        bodyIndex.setBloodPressure(edt_blood_pressure.getText().toString());
        bodyIndex.setBmi(edt_bmi.getText().toString());
        bodyIndex.setHeight(edtHeight.getText().toString());
        bodyIndex.setWaistline(edt_waistline.getText().toString());
        bodyIndex.setWeight(edtWeight.getText().toString());
        examServerObject.setBodyIndexArrayList(bodyIndex);
        examServerObject.setPhysicalExaminations(recordList);
        examServerObject.setOther(edtOther.getText().toString());
        examServerObject.setPhysicianCommentsOrRecomdations(edt_comment.getText().toString());
        Gson gson = new Gson();
        Log.e("lalala","dd"+gson.toJson(examServerObject));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerApi serverApi = retrofit.create(ServerApi.class);
        if (isUpdateApi){
            Log.e("iddd","phys"+patientExamId);
            examServerObject.setPhysical_exam_id(patientExamId);
            Call<PhysicalExamServerObject> updateCall=serverApi.updatePhysicalExamHistory(PatientLoginDataController.getInstance()
                    .currentPatientlProfile.getAccessToken(),examServerObject);
           updateCall.enqueue(new Callback<PhysicalExamServerObject>() {
               @Override
               public void onResponse(Call<PhysicalExamServerObject> call, Response<PhysicalExamServerObject> response) {
                   showingDialog.hideRefreshDialog();
                   Log.e("jfjfd","fd");
                   if (response.body().getResponse().equals("3"))
                   Toast.makeText(PatientPhysicalRecordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                   startActivity(new Intent(PatientPhysicalRecordActivity.this,PatientPhysicalActivity.class));
                   finish();
               }

               @Override
               public void onFailure(Call<PhysicalExamServerObject> call, Throwable t) {
                   showingDialog.hideRefreshDialog();
                   Toast.makeText(PatientPhysicalRecordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

               }
           });
        }else{
            Call<PhysicalExamServerObject> callable=serverApi.addPhysicalExamHistory(PatientLoginDataController.getInstance()
                    .currentPatientlProfile.getAccessToken(),examServerObject);
            callable.enqueue(new Callback<PhysicalExamServerObject>() {
                @Override
                public void onResponse(Call<PhysicalExamServerObject> call, Response<PhysicalExamServerObject> response) {
                    showingDialog.hideRefreshDialog();
                    if (response.body().getResponse().equals("3"))
                      Toast.makeText(PatientPhysicalRecordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PatientPhysicalRecordActivity.this,PatientPhysicalActivity.class));
                    finish();
                    Log.e("rekk","ll"+response.message());
                }

                @Override
                public void onFailure(Call<PhysicalExamServerObject> call, Throwable t) {
                    showingDialog.hideRefreshDialog();
                    Toast.makeText(PatientPhysicalRecordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }


    }
    private void weightmeasurePicker(Dialog mod) {
        measurePickerWeight = (NumberPicker) mod.findViewById(R.id.name);
        measurePickerWeight.setWrapSelectorWheel(true);
        measurePickerWeight.setMaxValue( PersonalInfoController.getInstance().weightunitsArray.length - 1);
        measurePickerWeight.setMinValue(0);
        measurePickerWeight.setDisplayedValues(PersonalInfoController.getInstance().weightunitsArray);
        final List<String> objWeightUnitsList = Arrays.asList(PersonalInfoController.getInstance().weightunitsArray);
        int index = objWeightUnitsList.indexOf(selectedWeightMeasure);
        measurePickerWeight.setValue(index);
        measurePickerWeight.setWrapSelectorWheel(false);
        measurePickerWeight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String newMeasureString = objWeightUnitsList.get(newVal);
                Log.e("SELECT_HEIGHT", "" + newMeasureString);
                if (!selectedWeightMeasure.equalsIgnoreCase(newMeasureString)) {
                    if (newMeasureString.equalsIgnoreCase("Kgs")) {
                        selectedWeightMeasure = newMeasureString;
                        selectedWeightValue = PersonalInfoController.getInstance().convertLbsToKg(selectedWeightValue);
                        Log.e("convertedCMValue", "" + selectedWeightValue);
                        Log.e("convertedCMArray", "" + PersonalInfoController.getInstance().kgArray);
                        loadKgArray();

                    } else if (newMeasureString.equalsIgnoreCase("Lbs")) {
                        selectedWeightMeasure = newMeasureString;
                        selectedWeightValue = PersonalInfoController.getInstance().convertKgToLbs(selectedWeightValue);
                        loadlbsArray();
                    }
                }
            }
        });

    }


    private void weightnumberPicker(Dialog mod) {
        numberPickerWeight = (NumberPicker) mod.findViewById(R.id.value);
        if (selectedWeightMeasure.equalsIgnoreCase("Kgs")) {
            Log.e("weightnumberPicker", "call");
            loadKgArray();//kg array
        } else {
            loadlbsArray(); // lbs array
        }
        numberPickerWeight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                if (selectedWeightMeasure.equalsIgnoreCase("Kgs")) {
                    selectedWeightValue = PersonalInfoController.getInstance().kgArray.get(newVal);
                } else {
                    selectedWeightValue = PersonalInfoController.getInstance().lbsArray.get(newVal);
                }

            }
        });
    }


    public void loadKgArray() {
        numberPickerWeight.setDisplayedValues(null);
        int index = PersonalInfoController.getInstance().kgArray.indexOf(selectedWeightValue);
        numberPickerWeight.setMinValue(0);
        numberPickerWeight.setMaxValue( PersonalInfoController.getInstance().kgArray.size() - 1);
        String[] mStringArray = new String[ PersonalInfoController.getInstance().kgArray.size()];
        mStringArray =  PersonalInfoController.getInstance().kgArray.toArray(mStringArray);
        numberPickerWeight.setDisplayedValues(mStringArray);
        numberPickerWeight.setValue(index);
    }

    public void loadlbsArray() {

        numberPickerWeight.setDisplayedValues(null);
        Log.e("weightValueInFeet", "" + selectedWeightValue+selectedWeightMeasure);

        int index =  PersonalInfoController.getInstance().lbsArray.indexOf(selectedWeightValue);

        numberPickerWeight.setMinValue(0);
        numberPickerWeight.setMaxValue( PersonalInfoController.getInstance().lbsArray.size() - 1);

        String[] mStringArray = new String[ PersonalInfoController.getInstance().lbsArray.size()];
        mStringArray =  PersonalInfoController.getInstance().lbsArray.toArray(mStringArray);

        numberPickerWeight.setDisplayedValues(mStringArray);
        numberPickerWeight.setValue(index);
    }


    public void loadHeightPickerValue() {
        final Dialog mod = new Dialog(PatientPhysicalRecordActivity.this);
        mod.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mod.setContentView(R.layout.alert_dailog);
        TextView txtTitle = (TextView) mod.findViewById(R.id.title);
        txtTitle.setText("Select Height");
        mod.show();
        numberPickerHeight(mod);


        if(edtHeight.getText().toString().length()>0 && edtHeight.getText().toString().contains("CM")){
            String array[]=edtHeight.getText().toString().split(" ");
            selectedHeightValue=array[0];
            selectedHeightMeasure="CM";
            Log.e("heightnssnsnsa","call"+selectedWeightValue+selectedWeightMeasure);

        }
        if (selectedHeightMeasure.equalsIgnoreCase("CM")) {
            loadCmArray();
        } else {
            Log.e("feetcall", "call");
            loadfeetArray();
        }

        measurePickerHeight(mod);

        Button btnOk = mod.findViewById(R.id.ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mod.dismiss();
                if(selectedHeightMeasure.equalsIgnoreCase("CM")) {
                    edtHeight.setText(selectedHeightValue+" "+"CM");
                }else {
                    edtHeight.setText(selectedHeightValue);
                }
                conversionOfHeightAndWeightForBmi();
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


    private void numberPickerHeight(Dialog mod) {
        numberPickerHeight = (NumberPicker) mod.findViewById(R.id.value);
        if (selectedHeightMeasure.equalsIgnoreCase("CM")) {
            loadCmArray();//cm array
        } else {
            loadfeetArray(); // Feet array
        }
        numberPickerHeight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (selectedHeightMeasure.equalsIgnoreCase("CM")) {
                    selectedHeightValue = PersonalInfoController.getInstance().cmArray.get(newVal);
                } else {
                    selectedHeightValue = PersonalInfoController.getInstance().feetArray.get(newVal);
                }

            }
        });
    }

    private void measurePickerHeight(Dialog mod) {
        ///mearsurePicker
        measurePickerHeight = (NumberPicker) mod.findViewById(R.id.name);
        measurePickerHeight.setWrapSelectorWheel(true);
        measurePickerHeight.setMaxValue(PersonalInfoController.getInstance().heightUnitsArray.length - 1);
        measurePickerHeight.setMinValue(0);
        measurePickerHeight.setDisplayedValues(PersonalInfoController.getInstance().heightUnitsArray);

        final List<String> objHeightUnitsList = Arrays.asList(PersonalInfoController.getInstance().heightUnitsArray);

        int index = objHeightUnitsList.indexOf(selectedHeightMeasure);
        measurePickerHeight.setValue(index);
        measurePickerHeight.setWrapSelectorWheel(true);
        measurePickerHeight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                String newMeasureString = objHeightUnitsList.get(newVal);
                Log.e("SELECT_HEIGHT", "" + newMeasureString);

                if (!selectedHeightMeasure.equalsIgnoreCase(newMeasureString)) {
                    if (newMeasureString.equalsIgnoreCase("CM")) {

                        selectedHeightMeasure = newMeasureString;
                        selectedHeightValue = PersonalInfoController.getInstance().convertFeetToCm(selectedHeightValue);
                        Log.e("convertedCMValue", "" + selectedHeightValue);

                        loadCmArray();

                    } else if (newMeasureString.equalsIgnoreCase("feet")) {

                        selectedHeightMeasure = newMeasureString;
                        selectedHeightValue = PersonalInfoController.getInstance().convertCmToFeet(selectedHeightValue);
                        loadfeetArray();

                    }
                }
            }
        });

    }

    public void loadCmArray() {
        numberPickerHeight.setDisplayedValues(null);
        int index = PersonalInfoController.getInstance().cmArray.indexOf(selectedHeightValue);
        numberPickerHeight.setMinValue(0);
        numberPickerHeight.setMaxValue(PersonalInfoController.getInstance().cmArray.size() - 1);
        String[] mStringArray = new String[PersonalInfoController.getInstance().cmArray.size()];
        mStringArray = PersonalInfoController.getInstance().cmArray.toArray(mStringArray);
        numberPickerHeight.setDisplayedValues(mStringArray);
        numberPickerHeight.setValue(index);
    }

    private void loadfeetArray() {
        numberPickerHeight.setDisplayedValues(null);
        int index = PersonalInfoController.getInstance().feetArray.indexOf(selectedHeightValue);
        numberPickerHeight.setMinValue(0);
        numberPickerHeight.setMaxValue(PersonalInfoController.getInstance().feetArray.size() - 1);
        String[] mStringArray = new String[PersonalInfoController.getInstance().feetArray.size()];
        Log.e("FeetValueindex", "" + index);
        mStringArray = PersonalInfoController.getInstance().feetArray.toArray(mStringArray);
        numberPickerHeight.setDisplayedValues(mStringArray);
        numberPickerHeight.setValue(index);
    }

    //this method for wrestLine

    private void loadWaistLinePicker(){
        final Dialog mod = new Dialog(PatientPhysicalRecordActivity.this);
        mod.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mod.setContentView(R.layout.alert_dailog);
        mod.show();
        TextView txtTitle = (TextView) mod.findViewById(R.id.title);
        txtTitle.setText("Select WaistLine");
        numberPickerWaist = mod.findViewById(R.id.value);
        if(edt_waistline.getText().toString().length()>0){
            String weightarray[]=edt_waistline.getText().toString().split(" ");
            selectedWaistLine=weightarray[0];
            selectedWaistMeasure=weightarray[1];
        }
        ////for value array
        numberPickerWaist.setDisplayedValues(null);
        int index = PersonalInfoController.getInstance().waistArray.indexOf(selectedWaistLine);
        numberPickerWaist.setMinValue(0);
        numberPickerWaist.setWrapSelectorWheel(false);
        numberPickerWaist.setMaxValue( PersonalInfoController.getInstance().waistArray.size() - 1);
        String[] mStringArray = new String[ PersonalInfoController.getInstance().waistArray.size()];
        mStringArray =  PersonalInfoController.getInstance().waistArray.toArray(mStringArray);
        numberPickerWaist.setDisplayedValues(mStringArray);
        numberPickerWaist.setValue(index);
        //for measure
        measurePickerwaist = (NumberPicker) mod.findViewById(R.id.name);
        measurePickerwaist.setWrapSelectorWheel(false);
        measurePickerwaist.setMaxValue(0);
        measurePickerwaist.setMinValue(0);
        measurePickerwaist.setDisplayedValues(PersonalInfoController.getInstance().waistUnitsArray);

        numberPickerWaist.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedWaistLine = PersonalInfoController.getInstance().waistArray.get(newVal);
            }
        });


        Button btnOk = mod.findViewById(R.id.ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mod.dismiss();
                edt_waistline.setText(selectedWaistLine+" "+"CM");
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

    // this method for calculation of height

    private void conversionOfHeightAndWeightForBmi() {
        double KgValue = 0.0, cmValue = 0.0;
        String mGender="Male";
        String mAge="27";
        if (mGender != null && mAge != null && edtHeight.getText().toString() != null && edtWeight.getText().toString() != null) {
            if (edtWeight.getText().toString().contains("Lbs")) {
                String a[] = edtWeight.getText().toString().split(" ");
                double kgValue = new Double(a[0]) * 0.453592;
                String selectedValue1 = String.valueOf(Math.round(kgValue));
                KgValue = Double.parseDouble(selectedValue1);
                Log.e("bmiWeigth", "call" + KgValue);
            } else {
                if (edtWeight.getText().toString().length() > 0) {
                    String a[] = edtWeight.getText().toString().split(" ");
                    KgValue = Double.parseDouble(a[0]);
                    Log.e("bmiWeigth1", "call" + KgValue);
                }
            }

            if (edtHeight.getText().toString().contains("feet")) {
                String feetVal[] = edtHeight.getText().toString().split(" ");
                String selectedValue = PersonalInfoController.getInstance().convertFeetToCm(feetVal[0] + " " + feetVal[2]);
                cmValue = Double.parseDouble(selectedValue);
                Log.e("bmiheigth", "call" + cmValue);
            } else {
                if (edtHeight.getText().toString().length() > 0) {
                    String feetVal[] = edtHeight.getText().toString().split(" ");
                    cmValue = Double.parseDouble(feetVal[0]);
                    Log.e("bmiHeigth1", "call" + cmValue);
                }
            }

            if (KgValue > 0.0 && cmValue > 0.0) {
                Log.e("kgandcmvalues", "call" + KgValue + cmValue);
                calculateBMI(KgValue, cmValue);
            }
        }
    }


    // this method for calculating the bmi

    private void calculateBMI(double KgValue, double cmValue) {
        ///
        double bmiVal = 0.0;
        double age = Double.parseDouble("27");
        Log.e("callage", "call" + age);
        String bmrValue = "";
        if(PersonalInfoController.getInstance().currentPatient!=null){
            //  txt_patientName.setText(PersonalInfoController.getInstance().currentPatient.getFirstName());
        }
        String mGender="Male";

        if (mGender.equals("Female")) {
            bmiVal = 10 * KgValue + 6.25 * cmValue - 5 * age - 161;
            Log.e("Femaledbmi", "call" + bmiVal);
            bmrValue = String.valueOf(bmiVal);
        } else {
            bmiVal = 10 * KgValue + 6.25 * cmValue - 5 * age + 5;
            Log.e("Maledbmi", "call" + bmiVal);
            bmrValue = String.valueOf(bmiVal);
        }
        final double bmi = (KgValue * 10000f) / (cmValue * cmValue);
        Log.e("bmical", "call" + bmi);
        final DecimalFormat df = new DecimalFormat("#.#");
        edt_bmi.setText(""+df.format(bmi));
        rl_bmicondition.setVisibility(View.VISIBLE);
        loadBmiText(Double.parseDouble(df.format(bmi)));
    }

    private void  loadBmiText(double bmiValue) {

        Drawable background = btncircle.getBackground();
        if (bmiValue >= 40.0)
        {
            background.setColorFilter(Color.parseColor("#8449b0"), PorterDuff.Mode.SRC_OVER);
            txt_bmiconditon.setText("Morbidly Obese");
        }
        else if (bmiValue >= 35.0 && bmiValue < 40.0)
        {
            background.setColorFilter(Color.parseColor("#ff2600"), PorterDuff.Mode.SRC_OVER);
            txt_bmiconditon.setText("Severely Obese");

        }
        else if (bmiValue >= 30.0 && bmiValue < 35.0)
        {
            background.setColorFilter(Color.parseColor("#ffd300"), PorterDuff.Mode.SRC_OVER);
            txt_bmiconditon.setText("Obese");
        }
        else if (bmiValue >= 25.0 && bmiValue < 30.0)
        {
            background.setColorFilter(Color.parseColor("#fffb00"), PorterDuff.Mode.SRC_OVER);
            txt_bmiconditon.setText("Overweight");

        }
        else if (bmiValue >= 18.0 && bmiValue < 25.0)
        {
            background.setColorFilter(Color.parseColor("#a1d663"), PorterDuff.Mode.SRC_OVER);
            txt_bmiconditon.setText("Healthy Weight");

        }
        else
        {
            background.setColorFilter(Color.parseColor("#f9f9f9"), PorterDuff.Mode.SRC_OVER);
            txt_bmiconditon.setText("Under Weight");
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
public class PatientPhysicalRecordAdapter extends RecyclerView.Adapter<PatientPhysicalRecordAdapter.PhysicHolder>{
    Context context;

    public PatientPhysicalRecordAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PatientPhysicalRecordAdapter.PhysicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.physical_record_item, parent, false);
        return new PhysicHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientPhysicalRecordAdapter.PhysicHolder holder, int position) {
        PhysicalRecordServerObject serverObject=recordList.get(position);
        holder.txtTitle.setText(serverObject.getCategory());
        holder.edtDescription.setText(serverObject.getDescription());
        Log.e("reeere",serverObject.getCategory()+" and "+serverObject.getResult());
        switch (serverObject.getResult()) {
            case "Normal":
                holder.btnNormal.setChecked(true);
                break;
            case "Abnormal":
                holder.btnAbnormal.setChecked(true);
                break;
            case "Not Examined":
                holder.btnNotExamined.setChecked(true);
                break;
            default:
                holder.radioGroup.clearCheck();
        }
        holder.edtDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
              //  Log.e("nooo","de");
                PhysicalRecordServerObject selectedObject=recordList.get(position);
                selectedObject.setDescription(holder.edtDescription.getText().toString());
                recordList.set(position,selectedObject);
            }
        });
        holder.edtDescription.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    Log.e("selecedObj","radiogroup"+holder.edtDescription.getText().toString());
                    PhysicalRecordServerObject selectedObject=recordList.get(position);
                    selectedObject.setDescription(holder.edtDescription.getText().toString());
                    recordList.set(position,selectedObject);
                    return true;
                }
                return false;
            }
        });

        holder.btnNormal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    PhysicalRecordServerObject selectedObject = recordList.get(position);
                    selectedObject.setResult("Normal");
                    Log.e("selecedObj", "radiogroup" + selectedObject.getResult());
                    recordList.set(position, selectedObject);
                }

            }
        });
        holder.btnAbnormal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    PhysicalRecordServerObject selectedObject = recordList.get(position);
                    selectedObject.setResult("Abnormal");
                    Log.e("selecedObj", "radiogroup" + selectedObject.getResult());
                    recordList.set(position, selectedObject);
                }

            }
        });

        holder.btnNotExamined.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    PhysicalRecordServerObject selectedObject = recordList.get(position);
                    selectedObject.setResult("Not Examined");
                    Log.e("selecedObj", "radiogroup" + selectedObject.getResult());
                    recordList.set(position, selectedObject);
                }

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhysicalRecordServerObject selectedObject=recordList.get(position);
                Log.e("selecedObj","dsdsds"+position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                builder.setCancelable(false);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(holder.getAdapterPosition());
                        recordAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                builder.create().show();
                return false;
            }
        });
    }
    public void delete(int position) {
        recordList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        if(recordList.size()>0) {
            return recordList.size();
        }else {
            return 0;
        }    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class PhysicHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        RadioGroup radioGroup;
        RadioButton btnNormal,btnAbnormal,btnNotExamined;
        EditText edtDescription;
        public PhysicHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.item_title);
            radioGroup = itemView.findViewById(R.id.radio_group);
            btnNormal = itemView.findViewById(R.id.normal);
            btnAbnormal = itemView.findViewById(R.id.abnormal);
            btnNotExamined = itemView.findViewById(R.id.not_examined);
            edtDescription = itemView.findViewById(R.id.edt_description);
            btnNotExamined.isChecked();

        }
    }
}
    public void recyclerItems() {
        Log.e("affa","ddf");
        physicalRecordView = findViewById(R.id.recycler_view);
        physicalRecordView.setHasFixedSize(true);
        physicalRecordView.setLayoutManager(new LinearLayoutManager(PatientPhysicalRecordActivity.this));
        physicalRecordView.setAdapter(recordAdapter);

    }    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter = new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PatientPhysicalRecordActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);
    }
}
