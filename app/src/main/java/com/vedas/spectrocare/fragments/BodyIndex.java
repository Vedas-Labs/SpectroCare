package com.vedas.spectrocare.fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import java.text.DecimalFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApiModel.BodyIndexServerObject;
import com.vedas.spectrocare.activities.AddMedicalRecordActivity;
import com.vedas.spectrocare.model.PatientList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class BodyIndex extends Fragment {
    EditText edtHeight,edtWeight,edt_waistline,edt_blood_pressure,edt_bmi;
    NumberPicker numberPickerWeight, measurePickerWeight;
    Button nxtBn;
    String selectedWeightMeasure="Kgs", selectedWeightValue="70";
    NumberPicker numberPickerHeight, measurePickerHeight;
    String selectedHeightMeasure="CM", selectedHeightValue="168";
    String selectedWaistLine="25";
    String selectedWaistMeasure="CM";
    NumberPicker numberPickerWaist, measurePickerwaist;
    RelativeLayout rl_bmicondition;
    Button btncircle;
    TextView txt_bmiconditon;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_body_index, container, false);
        PersonalInfoController.getInstance().loadAllUnitsArrays();
        PersonalInfoController.getInstance().loadHeightValuesArray();
        PersonalInfoController.getInstance().loadWeightValuesArray();

        edtHeight = view.findViewById(R.id.edt_height);
        edtWeight = view.findViewById(R.id.edt_weight);
        edt_waistline=view.findViewById(R.id.edt_waistline);
        edt_bmi=view.findViewById(R.id.edt_bmi);
        edt_blood_pressure=view.findViewById(R.id.edt_blood_pressure);
        nxtBn=view.findViewById(R.id.btn_sign_up);
        rl_bmicondition=view.findViewById(R.id.conditon);
        btncircle=view.findViewById(R.id.image_con);
        txt_bmiconditon=view.findViewById(R.id.txt_condition);

        nxtBn.setOnClickListener(new View.OnClickListener() {@Override
            public void onClick(View v) {
                BodyIndexServerObject bodyIndex=new BodyIndexServerObject(edtHeight.getText().toString(),
                        edtWeight.getText().toString(),edt_waistline.getText().toString(),edt_bmi.getText().toString(),edt_blood_pressure.getText().toString());
                PersonalInfoController.getInstance().currectProfileData.setBodyIndex(bodyIndex);
                AddMedicalRecordActivity addMedicalRecordActivity=new AddMedicalRecordActivity();
                addMedicalRecordActivity.changer(1);
            }
        });

        if (getActivity()!=null && ((AddMedicalRecordActivity)getActivity()).isUpdate){
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("recordInfo", MODE_PRIVATE);
            String recordInfoString = sharedPreferences.getString("record_Info", "");
          //  Log.e("usersInfo",""+recordInfoString);

            try {
                JSONObject jsonObject = new JSONObject(recordInfoString);
                JSONArray medicalRecordArray = jsonObject.getJSONArray("medical_records");
                Log.e("arrayJson",""+medicalRecordArray);
                JSONObject bodyIndex = medicalRecordArray.getJSONObject(0);
                Log.e("bodyIndex",""+bodyIndex);
                JSONObject bodyIndexElements = bodyIndex.getJSONObject("bodyIndex");
                Log.e("indexBody",""+bodyIndexElements);
                String heightOfPatient = bodyIndexElements.getString("height");
                String weightOfPatient = bodyIndexElements.getString("weight");
                String waistlineOfPatient = bodyIndexElements.getString("waistline");
                String bmiOfPatient = bodyIndexElements.getString("bmi");
                String bloodPressureOfPatient = bodyIndexElements.getString("bloodPressure");

                Log.e("heightnssnsnsa","call"+heightOfPatient);

                edtHeight.setText(heightOfPatient);
                edtWeight.setText(weightOfPatient);
                edt_blood_pressure.setText(bloodPressureOfPatient);
                edt_waistline.setText(waistlineOfPatient);
                edt_bmi.setText(bmiOfPatient);

                if(edtHeight.getText().toString().length()>0 && edtHeight.getText().toString().contains("CM")){
                    String array[]=edtHeight.getText().toString().split(" ");
                    selectedHeightValue=array[0];
                    selectedHeightMeasure="CM";
                    Log.e("heightnssnsnsa","call"+selectedWeightValue+selectedWeightMeasure);

                }else {

                }

                if(edtWeight.getText().toString().length()>0 && edtWeight.getText().toString().contains("Kgs")){
                    String weightarray[]=edtWeight.getText().toString().split(" ");
                    selectedWeightValue=weightarray[0];
                    selectedWeightMeasure="Kgs";
                    Log.e("weightexists","call"+selectedWeightValue+selectedWeightMeasure);

                }else {
                    String weightarray[]=edtWeight.getText().toString().split(" ");
                    selectedWeightValue=weightarray[0];
                    selectedWeightMeasure="Lbs";
                    Log.e("weightexists","call"+selectedWeightValue+selectedWeightMeasure);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        conversionOfHeightAndWeightForBmi();
    }

    private void loadWeightSpinner(){
        final Dialog mod = new Dialog(getContext());
        mod.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mod.setContentView(R.layout.alert_dailog);
        mod.show();
        numberPickerWeight = mod.findViewById(R.id.value);
        if(edtWeight.getText().toString().length()>0 && edtWeight.getText().toString().contains("Kgs")){
            String weightarray[]=edtWeight.getText().toString().split(" ");
            selectedWeightValue=weightarray[0];
            selectedWeightMeasure="Kgs";
            Log.e("zzzzzzzzzzzz","call"+selectedWeightValue+selectedWeightMeasure);

        }else {
            String weightarray[]=edtWeight.getText().toString().split(" ");
            selectedWeightValue=weightarray[0];
            selectedWeightMeasure="Lbs";
            Log.e("xxxxxxxxxxx","call"+selectedWeightValue+selectedWeightMeasure);

        }
      /*  if(edtWeight.getText().toString().length()>0){
            String weightarray[]=edtWeight.getText().toString().split(" ");
            selectedWeightValue=weightarray[0];
            selectedWeightMeasure=weightarray[1];
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
        final Dialog mod = new Dialog(getContext());
        mod.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mod.setContentView(R.layout.alert_dailog);
        TextView txtTitle = (TextView) mod.findViewById(R.id.title);
        txtTitle.setText("Select Height");
        mod.show();
        numberPickerHeight(mod);


        if(edtHeight.getText().toString().length()>0 && edtHeight.getText().toString().equalsIgnoreCase("CM")){
            String array[]=edtHeight.getText().toString().split(" ");
            selectedHeightValue=array[0];
            selectedHeightMeasure="CM";
            Log.e("heightnssnsnsa","call"+selectedWeightValue+selectedWeightMeasure);

        }
       /* if(edtHeight.getText().toString().length()>0){
            if(edtHeight.getText().toString().contains("Cm")) {
                String array[] = edtHeight.getText().toString().split(" ");
                selectedHeightValue = array[0];
                selectedHeightMeasure = array[1];
            }else {
                Log.e("existed","call"+edtHeight.getText().toString());
                selectedHeightValue = edtHeight.getText().toString();
                selectedHeightMeasure = "Feet";
            }
        }*/
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
    private void loadWaistLinePicker(){
        final Dialog mod = new Dialog(getContext());
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
                edt_waistline.setText(selectedWaistLine+" "+selectedWaistMeasure);
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
    private void conversionOfHeightAndWeightForBmi() {
        double KgValue = 0.0, cmValue = 0.0;
        String mGender="Male";
        String mAge="27";
        if(PersonalInfoController.getInstance().currentPatient!=null){
            PatientList objModel= PersonalInfoController.getInstance().currentPatient;
             mGender=objModel.getGender();
             mAge=objModel.getAge();
            Log.e("currentPatient", "" + mGender+mAge);

        }
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
    private void calculateBMI(double KgValue, double cmValue) {
        ///
        double bmiVal = 0.0;
        double age = Double.parseDouble("27");
        Log.e("callage", "call" + age);
        String bmrValue = "";
        String mGender="Male";

        if(PersonalInfoController.getInstance().currentPatient!=null){
            PatientList objModel= PersonalInfoController.getInstance().currentPatient;
            mGender=objModel.getGender();
            //mAge=objModel.getAge();
            Log.e("currentPatient", "" + mGender+objModel.getAge());
        }

        if (mGender.equalsIgnoreCase("Female")) {
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
}



/*
package com.vedas.spectrocare.fragments;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.AddMedicalRecordActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class BodyIndexServerObject extends Fragment {
    final String[] Units = {"CM", "Feets"};
    final String[] Unames = {"Kgs","Lbs"};
    List<String> height = new ArrayList<>();
    List<String> centiMeters = new ArrayList<>();
    List<String> kg = new ArrayList<>();
    List<String> lbs = new ArrayList<>();
    String[] data, numValue;
    EditText edtHeight,edtWeight,edt_waistline,edt_blood_pressure,edt_bmi;
    int value;
    NumberPicker valuePicker, namePicker;
    private boolean isValueChanged;
    Button oK,canclE;
    Button nxtBn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout_boarder.fragment_body_index, container, false);
        edtHeight = view.findViewById(R.id.edt_height);
        edtWeight = view.findViewById(R.id.edt_weight);
        edt_waistline=view.findViewById(R.id.edt_waistline);
        edt_bmi=view.findViewById(R.id.edt_bmi);
        edt_blood_pressure=view.findViewById(R.id.edt_blood_pressure);
        nxtBn=view.findViewById(R.id.btn_sign_up);
        addDataToFeetsArray();
        addDataToCmsArray();

        if (getActivity()!=null && ((AddMedicalRecordActivity)getActivity()).isUpdate){
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("recordInfo", MODE_PRIVATE);
            String recordInfoString = sharedPreferences.getString("record_Info", "");
            Log.e("usersInfo",""+recordInfoString);

            try {
                JSONObject jsonObject = new JSONObject(recordInfoString);
                JSONArray medicalRecordArray = jsonObject.getJSONArray("medical_records");
                Log.e("arrayJson",""+medicalRecordArray);
                JSONObject bodyIndex = medicalRecordArray.getJSONObject(0);
                Log.e("bodyIndex",""+bodyIndex);
                JSONObject bodyIndexElements = bodyIndex.getJSONObject("bodyIndex");
                Log.e("indexBody",""+bodyIndexElements);
                String heightOfPatient = bodyIndexElements.getString("height");
                String weightOfPatient = bodyIndexElements.getString("weight");
                String waistlineOfPatient = bodyIndexElements.getString("waistline");
                String bmiOfPatient = bodyIndexElements.getString("bmi");
                String bloodPressureOfPatient = bodyIndexElements.getString("bloodPressure");

                edtHeight.setText(heightOfPatient);
                edtWeight.setText(weightOfPatient);
                edt_blood_pressure.setText(bloodPressureOfPatient);
                edt_waistline.setText(waistlineOfPatient);
                edt_bmi.setText(bmiOfPatient);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }




        nxtBn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               com.vedas.spectrocare.ServerApiModel.BodyIndexServerObject bodyIndex=new com.vedas.spectrocare.ServerApiModel.BodyIndexServerObject(edtHeight.getText().toString(),
                       edtWeight.getText().toString(),edt_waistline.getText().toString(),edt_bmi.getText().toString(),edt_blood_pressure.getText().toString());

               PersonalInfoController.getInstance().currectProfileData.setBodyIndex(bodyIndex);

               AddMedicalRecordActivity addMedicalRecordActivity=new AddMedicalRecordActivity();

               addMedicalRecordActivity.changer(1);


           }
       });
        */
/* PersonalInfoController.getInstance().currectProfileData.setWeight(edtWeight.getText().toString());
        PersonalInfoController.getInstance().currectProfileData.setBmi(edt_bmi.getText().toString());
        PersonalInfoController.getInstance().currectProfileData.setBloodPressure(edt_blood_pressure.getText().toString());
        PersonalInfoController.getInstance().currectProfileData.setWaistline(edt_waistline.getText().toString());
        PersonalInfoController.getInstance().currectProfileData.setHeight(edtHeight.getText().toString());

        edtWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout_boarder.alert_dailog, null);

                valuePicker = view.findViewById(R.id.value);
                oK = view.findViewById(R.id.ok);
                canclE = view.findViewById(R.id.cancle);
                namePicker = view.findViewById(R.id.name);
                namePicker.setMinValue(0);
                namePicker.setMaxValue(Unames.length - 1);
                namePicker.setWrapSelectorWheel(true);
                namePicker.setDisplayedValues(Unames);
                valuePicker.invalidate();
                valuePicker.setMinValue(3);
                valuePicker.setMaxValue(350);
                //valuePicker.setDisplayedValues(numValue);

                oK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                namePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        if (newVal==1){
                            if (isValueChanged){
                                    Log.e("adff","nfhlf");
                            }
                        }
                    }
                });

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setView(view);
                AlertDialog dialog = alertDialog.create();
                dialog.show();
            }
        });
*//*

        edtWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout_boarder.alert_dailog, null);
                valuePicker = view.findViewById(R.id.value);
                namePicker = view.findViewById(R.id.name);
                oK = view.findViewById(R.id.ok);
                namePicker.setMinValue(0);
                namePicker.setMaxValue(Unames.length - 1);
                namePicker.setWrapSelectorWheel(true);
                namePicker.setDisplayedValues(Unames);
                valuePicker.invalidate();
                valuePicker.setMinValue(3);
                valuePicker.setMaxValue(350);
                //valuePicker.setDisplayedValues(numValue);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setView(view);
                final AlertDialog dialog = alertDialog.create();
                dialog.show();


                oK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String value = String.valueOf(valuePicker.getValue());
                        edtWeight.setText(value);
                        dialog.dismiss();

                    }
                });
                namePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        // feets -> 1 , cms ->0
                        Log.e("akak", "value is " + valuePicker.getValue());
                        Log.e("akak", "" + picker.getValue());
                        if (newVal == 1) {

                            if(!isValueChanged){
                                valuePicker.setMinValue(7);
                                valuePicker.setMaxValue(720);
                                valuePicker.setValue(7);
                                return;

                            }
                            if (isValueChanged) {
                                int selectedValue = valuePicker.getValue();
                                Log.e("klklj", "" + selectedValue);
                                int dd = (int) (selectedValue * 2.20462262185);

                                Log.e("duhfjf",""+dd);
                                int selectedFeetPosition = getValuePosFromKgToLbs(dd);
                                Log.e("sssss ", "" + selectedFeetPosition);
                                valuePicker.invalidate();
                                valuePicker.setMinValue(7);
                                valuePicker.setMaxValue(720);
                                valuePicker.setValue(selectedFeetPosition);
                                return;
                            }
                            Log.e("ddkf", "" + data.toString());
                            Log.e("fklf", "" + data.length);
                            valuePicker.invalidate();
                            valuePicker.setMinValue(7);
                            valuePicker.setMaxValue(720);
                            //valuePicker.setDisplayedValues(data);
                            valuePicker.setValue(0);
                            valuePicker.setWrapSelectorWheel(false);

                        }

                        else if (newVal == 0) {
                            valuePicker.invalidate();
                            valuePicker.setMinValue(3);
                            valuePicker.setMaxValue(350);
                            //valuePicker.setDisplayedValues(numValue);
                            valuePicker.setValue(3);
                            valuePicker.setWrapSelectorWheel(true);






                            if (isValueChanged) {
                                int selectedValue = valuePicker.getValue();
                                Log.e("klklj", "" + selectedValue);
                                int dd = (int) (selectedValue / 2.20462262185);

                                Log.e("duhf",""+dd);
                                int selectedFeetPosition = getValuePosFromLbsToKg(dd);
                                Log.e("sssssaa ", "" + selectedFeetPosition);
                                valuePicker.invalidate();
                                valuePicker.setMinValue(3);
                                valuePicker.setMaxValue(350);
                                valuePicker.setValue(selectedFeetPosition);
                                return;
                            }














                        }
                    }
                });
                valuePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        isValueChanged = true;
                    }
                });


            }
        });






























        edtHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout_boarder.alert_dailog, null);
                valuePicker = view.findViewById(R.id.value);
                namePicker = view.findViewById(R.id.name);
                oK = view.findViewById(R.id.ok);
                namePicker.setMinValue(0);
                namePicker.setMaxValue(Units.length - 1);
                namePicker.setWrapSelectorWheel(true);
                namePicker.setDisplayedValues(Units);
                valuePicker.invalidate();
                valuePicker.setMinValue(0);
                valuePicker.setMaxValue(numValue.length - 1);
                valuePicker.setDisplayedValues(numValue);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setView(view);
                final AlertDialog dialog = alertDialog.create();
                dialog.show();


                oK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      String value = String.valueOf(valuePicker.getValue());
                      edtHeight.setText(value);
                      dialog.dismiss();

                    }
                });
                namePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // feets -> 1 , cms ->0
                        Log.e("akak", "value is " + valuePicker.getValue());
                        Log.e("akak", "" + picker.getValue());
                        if (newVal == 1) {
                            if (isValueChanged) {
                                int selectedValue = valuePicker.getValue();
                                Log.e("klklj", "" + selectedValue);
                                double dd = selectedValue * 0.0328;
                                Log.e("vsvvv", "" + dd);
                                String splitValue = String.valueOf(dd);
                                String[] parts = splitValue.split("\\.");
                                String part1 = parts[0] + " Feet";
                                Log.e("vvvvv", part1);
                                String part2 = parts[1];
                                String subS = part2.substring(0,3);
                                Log.e("subStrin", subS);
                                double subInt = Integer.parseInt(subS)*12;
                                String conc = String.valueOf(subInt);
                                String subStr = conc.substring(0, 1);
                                Log.e("subString", subStr);
                                String selectedFeetValue = part1 + " " + subStr + " In";
                                int selectedFeetPosition = getValuePosFromFeetsArray(selectedFeetValue);
                                Log.e("sssss ", "" + selectedFeetPosition);
                                valuePicker.invalidate();
                                valuePicker.setMinValue(0);
                                valuePicker.setMaxValue(data.length - 1);
                                valuePicker.setDisplayedValues(data);
                                valuePicker.setValue(selectedFeetPosition);

                                return;
                            }
                            Log.e("ddkf", "" + data.toString());
                            Log.e("fklf", "" + data.length);
                            valuePicker.invalidate();
                            valuePicker.setMinValue(0);
                            valuePicker.setMaxValue(data.length - 1);
                            valuePicker.setDisplayedValues(data);
                            valuePicker.setValue(0);
                            valuePicker.setWrapSelectorWheel(false);

                        } else if (newVal == 0) {
                            valuePicker.invalidate();
                            valuePicker.setMinValue(0);
                            valuePicker.setMaxValue(numValue.length - 1);
                            valuePicker.setDisplayedValues(numValue);
                            valuePicker.setValue(0);
                            valuePicker.setWrapSelectorWheel(true);
                        }
                    }
                });
                valuePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        isValueChanged = true;
                    }
                });


            }
        });
        return view;
    }

    private void addDataToCmsArray() {
        for (int b = 0; b < 251; b++) {
            String cm = String.valueOf(b);
            centiMeters.add(cm);
            kg.add(cm);
            lbs.add(cm);
        }
        numValue = new String[height.size()];
        numValue = centiMeters.toArray(numValue);
        numValue = kg.toArray(numValue);
        numValue = lbs.toArray(numValue);
    }

    private void addDataToFeetsArray() {
        for (int i = 0; i <= 8; i++) {
            String feet = i + " Feet";
            for (int j = 0; j <= 11; j++) {
                String inch = " " + j;
                final String feetinch = feet.concat(inch) + " In";
                height.add(feetinch);
            }
        }
        data = new String[height.size()];
        data = height.toArray(data);
    }

    private int getValuePosFromFeetsArray(String value) {
        for (int i = 0; i < data.length; i++) {
            if (data[i].equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0;
    }


    int getValuePosFromKgToLbs(int value){

        int j = 0;

        if(value==6){

            j=7;
        }
        else{

        for (int i = 7; i < 772; i++) {

            if (i==value) {
                j=i;
            }
        }
        }

        return j;
    }


    int getValuePosFromLbsToKg(int value){

        int j = 0;


            for (int i = 3; i < 350; i++) {

                if (i==value) {
                    j=i;
                }
            }

        return j;
    }

}



*/
