package com.vedas.spectrocare.PatientModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicationArrayObject;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicationObject;
import com.vedas.spectrocare.PatinetControllers.PatientFamilyDataController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.MedicalPersonaSignupView;
import com.vedas.spectrocare.activities.MedicalPersonalSignupPresenter;
import com.vedas.spectrocare.adapter.CalendarAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.ButterKnife;
import butterknife.OnClick;
public class PatientEditMedicationsRecordActivity extends AppCompatActivity implements MedicalPersonaSignupView {
     TextView txtDiagnosisView;
     ImageView imgBack;
     EditText ed_date, ed_doctor, ed_condition, ed_instruction;
     RefreshShowingDialog refreshShowingDialog;
     MedicationAdapter recordAdapter;
     RecyclerView recyclerView;
    Button btn_save_changes;
    RelativeLayout rlAdd,rlmedicationView;
     CalendarView calendarView;
     String date,sD,eD;
     long kk;
     boolean cal,ed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_edit_medications_record);
        ButterKnife.bind(this);
        loadIDS();
        accessInterfaceMethods();
        loadCurrentObject();
    }
   @OnClick(R.id.btn_save_changes) void saveAction(){
        Log.e("dasaf","dfdfdfd");
        /*if(PatientMedicalRecordsController.getInstance().medicatioArrayObjectsList.size()>0){
            Log.e("callll","size"+PatientMedicalRecordsController.getInstance().medicatioArrayObjectsList.size());
            for(PatientMedicationArrayObject obj: PatientMedicalRecordsController.getInstance().medicatioArrayObjectsList){
                Log.e("qqqqq","size"+obj.getDosage()+obj.getName()+obj.getFreq());
            }
        }*/
        validations();
   }
    private void loadCurrentObject() {
        if (PatientMedicalRecordsController.getInstance().selectedMedication != null) {
            PatientMedicationObject obj = PatientMedicalRecordsController.getInstance().selectedMedication;
            ed_doctor.setText(obj.getDoctorName());
            if (obj.getMedicatioArrayObjects().size() > 0) {
                ed_instruction.setText(obj.getMedicatioArrayObjects().get(obj.getMedicatioArrayObjects().size() - 1).getMoreDetails());
                ed_condition.setText(obj.getMedicatioArrayObjects().get(obj.getMedicatioArrayObjects().size() - 1).getPurpose());
                PatientMedicalRecordsController.getInstance().medicatioArrayObjectsList = (ArrayList<PatientMedicationArrayObject>) obj.getMedicatioArrayObjects().clone();
//obj.getMedicatioArrayObjects();
                Log.e("sdddsdsds", "dddds" + ed_instruction.getText().toString() + ed_condition.getText().toString());
                rlmedicationView.setVisibility(View.VISIBLE);
                recordAdapter.notifyDataSetChanged();
            }
            btn_save_changes.setVisibility(View.GONE);
        } else {
            btn_save_changes.setVisibility(View.VISIBLE);
        }
    }    private void loadIDS() {
        refreshShowingDialog=new RefreshShowingDialog(PatientEditMedicationsRecordActivity.this);
        txtDiagnosisView = findViewById(R.id.txt_view_diagnosis);
        imgBack = findViewById(R.id.img_back_arrow);
        ed_date = findViewById(R.id.edt_surgery_name);
        ed_doctor = findViewById(R.id.txt_doctor);
        ed_condition = findViewById(R.id.ed_condition);
        ed_instruction = findViewById(R.id.ed_inst);
        btn_save_changes = findViewById(R.id.btn_save_changes);
        rlAdd=findViewById(R.id.rl_add);
        rlmedicationView=findViewById(R.id.rl_medicationView);
        recyclerItems();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ed_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(ed_date);
            }
        });
        rlAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientMedicationArrayObject medicationRecordObj = new PatientMedicationArrayObject();
                medicationRecordObj.setName("");
                medicationRecordObj.setDosage("");
                medicationRecordObj.setDurationDays("");
                medicationRecordObj.setFreq("");
                medicationRecordObj.setMoreDetails(ed_instruction.getText().toString());
                medicationRecordObj.setPurpose(ed_condition.getText().toString());
                PatientMedicalRecordsController.getInstance().medicatioArrayObjectsList.add(medicationRecordObj);
                rlmedicationView.setVisibility(View.VISIBLE);
                recordAdapter.notifyDataSetChanged();
            }
        });
/*
        rlAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientMedicationArrayObject medicationRecordObj=new PatientMedicationArrayObject();
                medicationRecordObj.setName("");
                medicationRecordObj.setDosage("");
                medicationRecordObj.setDurationDays("");
                medicationRecordObj.setFreq("");
                medicationRecordObj.setMoreDetails("");
                medicationRecordObj.setPurpose("");
                PatientMedicalRecordsController.getInstance().medicatioArrayObjectsList.add(medicationRecordObj);
                rlmedicationView.setVisibility(View.VISIBLE);
                recordAdapter.notifyDataSetChanged();
            }
        });
*/
    }
    public void recyclerItems() {
        recordAdapter = new MedicationAdapter(getApplicationContext());
        recyclerView = findViewById(R.id.rl_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(PatientEditMedicationsRecordActivity.this));
        recyclerView.setAdapter(recordAdapter);
    }
    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                if (curdOpetaton.equals("add")) {
                    try {
                        if (jsonObject.getString("response").equals("3")) {
                            Log.e("allergyresponse", "call" + jsonObject.toString());
                            refreshShowingDialog.hideRefreshDialog();
                            Gson gson = new Gson();
                            PatientMedicationObject patientMedicationObject = gson.fromJson(jsonObject.toString(), PatientMedicationObject.class);
                            /*Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                            immunizationObject.setCreatedDate(String.valueOf(timestamp.getTime()));*/

                            patientMedicationObject.setByWhom("Patient");
                            patientMedicationObject.setByWhomID(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
                            patientMedicationObject.setPatientID(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
                            patientMedicationObject.setMedical_record_id(PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
                            patientMedicationObject.setHospital_reg_num(PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
                            patientMedicationObject.setDoctorName(ed_doctor.getText().toString());
                            patientMedicationObject.setIllnessID(PatientFamilyDataController.getInstance().selectedIllnessRecord.getIllnessID());
                            patientMedicationObject.setDoctorMedicalPersonnelID("xyz123");
                            patientMedicationObject.setIllnessMedicationID(jsonObject.getString("illnessMedicationID"));
                            patientMedicationObject.setMedicatioArrayObjects(PatientMedicalRecordsController.getInstance().medicatioArrayObjectsList);
                            PatientMedicalRecordsController.getInstance().medicationArrayList.add(patientMedicationObject);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    refreshShowingDialog.hideRefreshDialog();
                }
            }
            @Override
            public void failureCallBack(String failureMsg) {
                refreshShowingDialog.hideRefreshDialog();
                //Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void validations() {
        if (ed_date.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please select date", "ok");
        } else if (ed_doctor.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please enter doctor name", "ok");
        } else if (ed_condition.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please enter condition", "ok");
        } else if (ed_instruction.getText().toString().isEmpty()) {
            dialogeforCheckavilability("Error", "Please enter instruction", "ok");
        }else {
            if (isNetworkConnected()) {
                if(PatientMedicalRecordsController.getInstance().selectedMedication !=null){
                   // loadAddMedicationApi(true);
                }else{
                    refreshShowingDialog.showAlert();
                    loadAddMedicationApi(false);
                }
            } else {
                refreshShowingDialog.hideRefreshDialog();
                dialogeforCheckavilability("Error", "Please check internet connection", "ok");
            }
        }
    }

    private void loadAddMedicationApi(boolean isUpdate) {
        JSONObject params = new JSONObject();
        JSONArray medicationsArray = new JSONArray();
        try {
            if (PatientMedicalRecordsController.getInstance().medicatioArrayObjectsList.size() > 0) {
                Log.e("callll", "size" + PatientMedicalRecordsController.getInstance().medicatioArrayObjectsList.size());
                for (PatientMedicationArrayObject obj : PatientMedicalRecordsController.getInstance().medicatioArrayObjectsList) {
                    Log.e("qqqqq", "size" + obj.getDosage() + obj.getName() + obj.getFreq());
                    JSONObject medicationObj = new JSONObject();
                    medicationObj.put("name", obj.getName());
                    medicationObj.put("dosage", obj.getDosage());
                    medicationObj.put("freq", obj.getFreq());
                    medicationObj.put("purpose", ed_condition.getText().toString());
                    medicationObj.put("durationDays", "10");
                    medicationObj.put("moreDetails", ed_instruction.getText().toString());
                    medicationsArray.put(medicationObj);
                }
            }
            if (isUpdate) {
                params.put("illnessID", PatientMedicalRecordsController.getInstance().selectedMedication.getIllnessID());
                params.put("illnessMedicationID", PatientMedicalRecordsController.getInstance().selectedMedication.getIllnessMedicationID());
            }else {
                params.put("illnessID", PatientFamilyDataController.getInstance().selectedIllnessRecord.getIllnessID());
            }
            params.put("byWhom", "patient");
            params.put("byWhomID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            params.put("medical_record_id", PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
            params.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
            params.put("doctorMedicalPersonnelID", "xyz1234");
            params.put("doctorName", ed_doctor.getText().toString());
            params.put("medications", medicationsArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        if (isUpdate) {
            ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.updatePatientMedicationApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "update");
        } else {
            ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.addPatientMedicationApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "add");
        }
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter = new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PatientEditMedicationsRecordActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);
    }


    public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.PatientItemHolder> {
        Context context;
       // ArrayList<CategoryItemModel> categoryItemList;

        public MedicationAdapter(Context context /*ArrayList<CategoryItemModel> categoryItemList*/) {
            this.context = context;
          //  this.categoryItemList = categoryItemList;
        }

        @NonNull
        @Override
        public PatientItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View patientItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_medication_items, parent, false);
            return new PatientItemHolder(patientItemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final PatientItemHolder holder, final int position) {
           PatientMedicationArrayObject obj= PatientMedicalRecordsController.getInstance().medicatioArrayObjectsList.get(position);
            holder.sno.setText(""+(position+1)+".");
            holder.name.setText(obj.getName());
            holder.dosage.setText(obj.getDosage());
            holder.frequency.setText(obj.getFreq());
            holder.name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                   PatientMedicalRecordsController.getInstance().medicatioArrayObjectsList.get(holder.getAdapterPosition()).setName(s.toString());
                }
                @Override
                public void afterTextChanged(Editable s) {
                    PatientMedicalRecordsController.getInstance().medicatioArrayObjectsList.get(holder.getAdapterPosition()).setName(s.toString());
                }
            });
            holder.dosage.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    PatientMedicalRecordsController.getInstance().medicatioArrayObjectsList.get(holder.getAdapterPosition()).setDosage(s.toString());
                }
                @Override
                public void afterTextChanged(Editable s) {
                    PatientMedicalRecordsController.getInstance().medicatioArrayObjectsList.get(holder.getAdapterPosition()).setDosage(s.toString());
                }
            });
           holder.frequency.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   final Dialog dialog = new Dialog(PatientEditMedicationsRecordActivity.this);
                   dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                   dialog.setCancelable(true);
                   dialog.setContentView(R.layout.spinner_frequency_alert);
                   dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                   NumberPicker picker1 = dialog.findViewById(R.id.numbr1);
                   NumberPicker picker2 = dialog.findViewById(R.id.numbr2);
                   NumberPicker picker3 = dialog.findViewById(R.id.numbr3);
                   TextView ok=dialog.findViewById(R.id.txtok);
                   picker1.setMaxValue(1);
                   picker2.setMaxValue(1);
                   picker3.setMaxValue(1);
                   dialog.show();
                   if(holder.frequency.getText().toString().length()>0){
                       String val=holder.frequency.getText().toString();
                       String a[]=val.split("-");
                       Log.e("saaaaa","call"+a[0]+a[1]+a[2]);
                       picker1.setValue(Integer.parseInt(a[0]));
                       picker2.setValue(Integer.parseInt(a[1]));
                       picker3.setValue(Integer.parseInt(a[2]));
                   }
                   picker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                       @Override
                       public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                           val=String.valueOf(picker1.getValue())+"-"+String.valueOf(picker2.getValue())+"-"+String.valueOf(picker3.getValue());
                       }
                   });
                   picker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                       @Override
                       public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                           val=String.valueOf(picker1.getValue())+"-"+String.valueOf(picker2.getValue())+"-"+String.valueOf(picker3.getValue());
                       }
                   });
                   picker3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                       @Override
                       public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                           val=String.valueOf(picker1.getValue())+"-"+String.valueOf(picker2.getValue())+"-"+String.valueOf(picker3.getValue());
                       }
                   });
                   ok.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           holder.frequency.setText(val);
                           PatientMedicalRecordsController.getInstance().medicatioArrayObjectsList.get(holder.getAdapterPosition()).setFreq(val);
                           dialog.dismiss();
                       }
                   });
               }
           });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

        @Override
        public int getItemCount() {
            if(PatientMedicalRecordsController.getInstance().medicatioArrayObjectsList.size()>0){
                return PatientMedicalRecordsController.getInstance().medicatioArrayObjectsList.size();
            }else{
                return  0;
            }
        }

        public class PatientItemHolder extends RecyclerView.ViewHolder {
            EditText name,dosage;
            TextView sno,frequency;
            public PatientItemHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.edt_name);
                dosage = itemView.findViewById(R.id.dosage);
                frequency = itemView.findViewById(R.id.freq);
                sno = itemView.findViewById(R.id.sno);

            }
        }
    }
    String val="0-0-0";
    public void frequencyAlert(MedicationAdapter.PatientItemHolder holder){
        final Dialog dialog = new Dialog(PatientEditMedicationsRecordActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.spinner_frequency_alert);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        NumberPicker picker1 = dialog.findViewById(R.id.numbr1);
        NumberPicker picker2 = dialog.findViewById(R.id.numbr2);
        NumberPicker picker3 = dialog.findViewById(R.id.numbr3);
        picker1.setMaxValue(1);
        picker2.setMaxValue(1);
        picker3.setMaxValue(1);
        dialog.show();
        picker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                val=String.valueOf(picker1.getValue())+"-"+String.valueOf(picker2.getValue())+"-"+String.valueOf(picker3.getValue());
                Log.e("saaaaa","call"+val);
            }
        });
        picker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                val=String.valueOf(picker1.getValue())+"-"+String.valueOf(picker2.getValue())+"-"+String.valueOf(picker3.getValue());
                Log.e("saaaaa","call"+val);
            }
        });
        picker3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                val=String.valueOf(picker1.getValue())+"-"+String.valueOf(picker2.getValue())+"-"+String.valueOf(picker3.getValue());
                Log.e("saaaaa","call"+val);
            }
        });
        PatientMedicalRecordsController.getInstance().medicatioArrayObjectsList.get(holder.getAdapterPosition()).setFreq(val);
    }
    public void selectDate(EditText editText){
        Calendar calendar = Calendar.getInstance();
        View mView = LayoutInflater.from(PatientEditMedicationsRecordActivity.this).inflate(R.layout.popup_calender_view, null, false);
        PopupWindow  popUp = new PopupWindow(mView, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, false);
        popUp.setTouchable(true);
        popUp.setFocusable(true);
        popUp.setBackgroundDrawable(new BitmapDrawable());
        popUp.setFocusable(true);
        popUp.setOutsideTouchable(true);
        //Solution
        popUp.showAsDropDown(editText);
        calendarView = mView.findViewById(R.id.calendar_view);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.YEAR, 1);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String msg = dayOfMonth + "/" + (month + 1) + "/" + year;
                //  editText.setText(msg);
                calendar.set(year,month,dayOfMonth);
                editText.setText(msg);

                kk = (calendar.getTimeInMillis())/1000 ;

                Log.e("gfjhfj","hgg:"+kk);
                date = String.valueOf(kk*1000);
                if(ed){
                    sD = date;
                }else{
                    eD = date;
                }
                Log.e("dd","ddd"+sD);
                Log.e("dd","d"+eD);

                popUp.dismiss();
                Date currentDate = new Date(kk*1000);
                SimpleDateFormat jdff = new SimpleDateFormat("dd/MM/yyyy");
                jdff.setTimeZone(TimeZone.getDefault());
                String java_date = jdff.format(currentDate);
                editText.setText(java_date);

                //  popUp.dismiss();
            }
        });
    }
}
