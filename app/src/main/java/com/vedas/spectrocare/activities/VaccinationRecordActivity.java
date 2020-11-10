package com.vedas.spectrocare.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.Controllers.VaccineServerObjectDataController;
import com.vedas.spectrocare.DataBase.IllnessDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBase.VaccineDataController;
import com.vedas.spectrocare.DataBase.VaccineTrackInfoDataController;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.DataBaseModels.VaccineModel;
import com.vedas.spectrocare.DataBaseModels.VaccineTrackInfoModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApiModel.TrackingServerObject;
import com.vedas.spectrocare.ServerApiModel.Vaccination;
import com.vedas.spectrocare.ServerApiModel.VaccineServerObject;
import com.vedas.spectrocare.SingleTapDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class VaccinationRecordActivity extends AppCompatActivity implements MedicalPersonaSignupView {
    FloatingActionButton addVaccineRecordBtn;
    RecyclerView vaccineRecordRecyclerView;
    String vaccineName, vaccineDate, vaccineDescription;
    BottomSheetDialog dialog;
    RelativeLayout backImgBtn;
    RefreshShowingDialog showingDialog;
    ImageView imgEdt;
    VaccineAdapter vaccineAdapter;
    JSONObject params;
    String  tempbirthDayObj="";
    private  float dX,dY;
    int lastAction;
    TextView txt_date,txtdsc;
    GestureDetector gestureDetector;
    long timestamp;
    String m,d;
    ArrayList<Integer> colorsArray = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccination_record);
        addVaccineRecordBtn = findViewById(R.id.add_vaccine_record);
        gestureDetector = new GestureDetector(this, new SingleTapDetector());
        vaccineRecordRecyclerView = findViewById(R.id.vaccine_recycler_view);
        txtdsc  = findViewById(R.id.text_vd_disc);
        backImgBtn = findViewById(R.id.img_back);

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        tempbirthDayObj = dateFormat.format(currentDate);
        Log.e("dateCheking",""+tempbirthDayObj);
        backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        addVaccinerecord();
        VaccineDataController.getInstance().fetchVaccineData(PatientProfileDataController.getInstance().currentPatientlProfile);
        showingDialog = new RefreshShowingDialog(VaccinationRecordActivity.this);

        if (isConn()) {
            showingDialog.showAlert();
            VaccineServerObjectDataController.getInstance().vaccineFetchApiCall();
        }
        loadRecyclerview();
        accessInterfaceMethods();

        addVaccineRecordBtn.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    VaccineDataController.getInstance().currentVaccineModel=null;
                    bottomDialogSheet();
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
    private void loadRecyclerview(){
      ArrayList<VaccineModel> arrayList=  VaccineDataController.getInstance().fetchVaccineData(PatientProfileDataController.getInstance().currentPatientlProfile);
        if (arrayList.isEmpty())
            txtdsc.setVisibility(View.VISIBLE);
        else
            txtdsc.setVisibility(View.GONE);

        if (arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                colorsArray.add(PersonalInfoController.getInstance().getRandomColor());
            }
        }
        vaccineAdapter = new VaccineAdapter(this);
        vaccineRecordRecyclerView.setHasFixedSize(true);
        vaccineRecordRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        vaccineRecordRecyclerView.setAdapter(vaccineAdapter);
    }
    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                Log.e("vaccine", "call" + jsonObject.toString());
                if (curdOpetaton.equals("fetch")) {

                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("immunization_records");
                        VaccineTrackInfoDataController.getInstance().deleteTrackData(VaccineDataController.getInstance().allVaccineList);
                            VaccineDataController.getInstance().deleteVaccineData(PatientProfileDataController.getInstance().currentPatientlProfile);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Gson gson = new Gson();
                                    VaccineServerObject userIdentifier = gson.fromJson(jsonArray.getJSONObject(i).toString(), VaccineServerObject.class);
                                    ArrayList<TrackingServerObject> trackArray = userIdentifier.getTracking();
                                    Log.e("trackArray", "call" + trackArray.size());
                                    VaccineServerObjectDataController.getInstance().processAndfetchVaccineData(userIdentifier, trackArray);
                                }
                            }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (curdOpetaton.equals("insert")) {
                    Gson gson = new Gson();
                    VaccineServerObject userIdentifier = gson.fromJson(params.toString(), VaccineServerObject.class);
                    try {
                        Log.e("familydata", "call" + jsonObject.getString("immunization_record_id"));
                        userIdentifier.setImmunization_record_id(jsonObject.getString("immunization_record_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    VaccineServerObjectDataController.getInstance().processAddVaccineData(userIdentifier);
                    dialog.dismiss();
                } else if (curdOpetaton.equals("update")) {
                    Gson gson = new Gson();
                    VaccineServerObject userIdentifier = gson.fromJson(params.toString(), VaccineServerObject.class);
                    VaccineServerObjectDataController.getInstance().processVaccineupdateData(userIdentifier);
                     dialog.dismiss();
                } else if (curdOpetaton.equals("delete")) {
                    VaccineDataController.getInstance().deleteVaccineModelData(PatientProfileDataController.getInstance().currentPatientlProfile, VaccineDataController.getInstance().currentVaccineModel);
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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void validations(){
        if (vaccineName.isEmpty())
            dialogeforCheckavilability("Error", "Please enter name of vaccine", "Ok");
        else if (vaccineDate.isEmpty())
            dialogeforCheckavilability("Error", "Please enter date of vaccine", "Ok");
/*
        else if (vaccineDescription.isEmpty())
            dialogeforCheckavilability("Error", "Please fill note about vaccine", "Ok");
*/
        else{
            if(isConn()){
                showingDialog.showAlert();
                processJsonParams();
            }
        }
    }


    public class VaccineAdapter extends RecyclerView.Adapter<VaccineAdapter.VaccineViewHolde> {
        Context context;
        Dialog dialog;

        public VaccineAdapter(Context context) {
            this.context = context;
        }

        @Override
        public VaccineViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View vaccineView= LayoutInflater.from(parent.getContext()).inflate(R.layout.vaccine_recycler_item,parent,false);
            return new VaccineViewHolde(vaccineView);
        }

        @Override
        public void onBindViewHolder(@NonNull final VaccineViewHolde holder, final int position) {
            VaccineModel vaccineModel=VaccineDataController.getInstance().allVaccineList.get(position);
            holder.vaccineID.setText(vaccineModel.getVaccineRecordId());
            holder.vaccineName.setText(vaccineModel.getVaccineName());
            holder.vaccineDate.setText(vaccineModel.getVaccineDate());
            holder.vaccineDescription.setText(vaccineModel.getNote());
            holder.colorLayout.setBackgroundColor(colorsArray.get(position));

            ArrayList<VaccineTrackInfoModel> trackInfoModels = VaccineTrackInfoDataController.getInstance().fetchTrackData(vaccineModel);
            if (trackInfoModels.size() > 0) {
                try {
                    VaccineTrackInfoModel trackInfoModel = trackInfoModels.get(trackInfoModels.size() - 1);
                    String date[] = PersonalInfoController.getInstance().convertTimestampToslashFormate(trackInfoModel.getDate());
                    String sourceString = "<b>" + trackInfoModel.getByWhom() + "</b> " + "on " + "<b>" + date[0] +" "+date[1] + " " + date[2]+ "</b>" /*+"\n"+"(Id: "+trackInfoModel.getByWhomId()+")"*/;

                    holder.createdDate.setText(Html.fromHtml(sourceString));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            holder.viewImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VaccineDataController.getInstance().currentVaccineModel=VaccineDataController.getInstance().allVaccineList.get(position);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        bottomDialogSheet();
                    }

                }
            });
            holder.editImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VaccineDataController.getInstance().currentVaccineModel=VaccineDataController.getInstance().allVaccineList.get(position);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        bottomDialogSheet();
                    }
                }
            });
            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context wrapper = new ContextThemeWrapper(VaccinationRecordActivity.this, R.style.MyPopupOtherStyle);
                    PopupMenu popup = new PopupMenu(wrapper,  holder.more);
                    popup.getMenuInflater().inflate(R.menu.popup_menu,popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getTitle().equals("View")) {
                                VaccineDataController.getInstance().currentVaccineModel=VaccineDataController.getInstance().allVaccineList.get(position);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                    bottomDialogSheet();
                                }
                            }else {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                                builder.setCancelable(false);
                                builder.setTitle("Delete");
                                builder.setMessage("Are you sure you want to delete");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        VaccineDataController.getInstance().currentVaccineModel=VaccineDataController.getInstance().allVaccineList.get(position);
                                        VaccineModel objModel=VaccineDataController.getInstance().allVaccineList.get(position);
                                        JSONObject params = new JSONObject();
                                        try {
                                          //  params.put("medical_personnel_id", objModel.getMedicalPersonId());
                                            params.put("patientID", objModel.getPatientId());
                                            params.put("medical_record_id", objModel.getMedicalRecordId());
                                            params.put("immunization_record_id", objModel.getVaccineRecordId());
                                            params.put("hospital_reg_num", objModel.getHospitalRegNum());
                                            params.put("byWhom","medical personnel");
                                            params.put("byWhomID",MedicalProfileDataController.getInstance().currentMedicalProfile.getMedical_person_id());

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        JsonParser jsonParser = new JsonParser();
                                        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
                                        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
                                        Log.e("midicID",""+MedicalProfileDataController.getInstance().currentMedicalProfile.getMedical_person_id());
                                        Log.e("token",""+MedicalProfileDataController.getInstance().currentMedicalProfile.getAccessToken());
                                        if (isConn()) {
                                            showingDialog.showAlert();
                                            ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.deleteVaccine(
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
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });
        }
        @Override
        public int getItemCount() {
            if(VaccineDataController.getInstance().allVaccineList.size()>0){
                return VaccineDataController.getInstance().allVaccineList.size();
            }else {
                return 0;
            }
        }

        public class VaccineViewHolde extends RecyclerView.ViewHolder {
            TextView vaccineName,vaccineDate,vaccineDescription,createdDate,vaccineID;
            ImageView viewImg,editImg,deleteImg;
            RelativeLayout colorLayout,more;
            public VaccineViewHolde(@NonNull View itemView) {
                super(itemView);
                vaccineName=itemView.findViewById(R.id.vaccinName);
                vaccineDate=itemView.findViewById(R.id.vaccine_date);
                vaccineID = itemView.findViewById(R.id.id);
                vaccineDescription=itemView.findViewById(R.id.vaccine_description);
                viewImg = itemView.findViewById(R.id.img_view);
                deleteImg = itemView.findViewById(R.id.img_delete);
                editImg = itemView.findViewById(R.id.img_edit);
                createdDate = itemView.findViewById(R.id.medical_record);
                colorLayout = itemView.findViewById(R.id.color);
                more = itemView.findViewById(R.id.layout_more_img);
            }

        }
    }
    public void addVaccinerecord(){
        addVaccineRecordBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                VaccineDataController.getInstance().currentVaccineModel=null;
            bottomDialogSheet();
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void bottomDialogSheet() {
        View dialogView = getLayoutInflater().inflate(R.layout.vaccine_alert, null);
        dialog = new BottomSheetDialog(Objects.requireNonNull(VaccinationRecordActivity.this),R.style.BottomSheetDialogTheme);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        ImageView imgView= dialog.findViewById(R.id.img_close);
        imgEdt = dialog.findViewById(R.id.edit);
        final EditText edtVaccineName = dialog.findViewById(R.id.edt_vaccine_name);
        txt_date  = dialog.findViewById(R.id.edt_vaccine_date);
        final EditText edtVaccineDescription = dialog.findViewById(R.id.edt_vaccine_description);
        final Button vaccineAddBtn = dialog.findViewById(R.id.btn_add_vaccine_details);
        imgEdt.setVisibility(View.GONE);
        if( VaccineDataController.getInstance().currentVaccineModel !=null){
            VaccineModel objModel=VaccineDataController.getInstance().currentVaccineModel;
          //  vaccineAddBtn.setText("UPDATE");
            vaccineAddBtn.setVisibility(View.GONE);
            txt_date.setText(objModel.getVaccineDate());
            edtVaccineName.setText(objModel.getVaccineName());
            edtVaccineDescription.setText(objModel.getNote());
            edtVaccineDescription.setTextColor(Color.parseColor("#a4b3b7"));
            edtVaccineName.setTextColor(Color.parseColor("#a4b3b7"));
            txt_date.setTextColor(Color.parseColor("#a4b3b7"));
            imgEdt.setVisibility(View.VISIBLE);
            txt_date.setEnabled(false);
            edtVaccineDescription.setEnabled(false);
            edtVaccineName.setEnabled(false);

        }
        txt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDatePicker();
            }
        });
        imgEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtVaccineDescription.setTextColor(Color.parseColor("#3E454C"));
                edtVaccineName.setTextColor(Color.parseColor("#3E454C"));
                txt_date.setTextColor(Color.parseColor("#3E454C"));
                txt_date.setEnabled(true);
                edtVaccineDescription.setEnabled(true);
                edtVaccineName.setEnabled(true);
                imgEdt.setVisibility(View.GONE);
                vaccineAddBtn.setText("UPDATE");
                vaccineAddBtn.setVisibility(View.VISIBLE);

            }
        });
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        vaccineAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaccineName = edtVaccineName.getText().toString();
                Log.e("vsd", "" + vaccineName);
                vaccineDescription = edtVaccineDescription.getText().toString();
              /*  vaccineDate = String.valueOf(timestamp);
                Log.e("dcdc",""+vaccineDate);*/
                vaccineDate = txt_date.getText().toString();
                validations();

            }
        });
        dialog.show();

    }

    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter = new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(VaccinationRecordActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);

    }
    int year, month, day;

    public void loadDatePicker() {
        final DatePickerDialog dialog;

        if (txt_date.getText().toString().isEmpty()) {
            Log.e("ifcall", "call");
            String[] txtBirthdayArray = tempbirthDayObj.split("-");
            year = Integer.parseInt(txtBirthdayArray[0]);
            month = Integer.parseInt(txtBirthdayArray[1]);
            day = Integer.parseInt(txtBirthdayArray[2]);
            Log.e("textdate", "call" + year + "-" + month + "-" + day);
            dialog = new DatePickerDialog(this, null, year, month - 1, day);
            dialog.getDatePicker().setMaxDate(new Date().getTime());

            dialog.show();
        } else {
            tempbirthDayObj = txt_date.getText().toString();
            String[] txtBirthdayArray = tempbirthDayObj.split("-");
            year = Integer.parseInt(txtBirthdayArray[0]);

            month = Integer.parseInt(txtBirthdayArray[1]);
            day = Integer.parseInt(txtBirthdayArray[2]);
            Log.e("textdate", "call" + year + "-" + month + "-" + day);

            dialog = new DatePickerDialog(this, null, year, month - 1, day);
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            dialog.show();
        }
        dialog.setButton(DialogInterface.BUTTON_POSITIVE,
                "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog1, int which) {
                        DatePicker objDatePicker = dialog.getDatePicker();
                        year = objDatePicker.getYear();
                        month = objDatePicker.getMonth();
                        day = objDatePicker.getDayOfMonth();


                        month=month+1;

                        if(month<10){
                             m="0"+month;
                        }
                        else{
                            m=""+month;
                        }


                        if(day<10){
                            d="0"+day;
                        }else {
                            d=""+day;
                        }
                        txt_date.setText(year + "-" + m + "-" + d);
                        Log.e("txt_dob", "" + txt_date.getText().toString());

                        String str_date=m+"-"+d+"-"+year;
                        DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
                        Date date = null;
                        try {
                            date = (Date)formatter.parse(str_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long output=date.getTime()/1000L;
                        String str=Long.toString(output);
                        timestamp = Long.parseLong(str);
                        Log.e("afdafafsa",""+timestamp);

                    }
                });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                    }
                });
    }
    public void processJsonParams(){
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        params = new JSONObject();
        try {
            params.put("hospital_reg_num", currentMedical.getHospital_reg_number());
            params.put("immunizationName",vaccineName);
            params.put("immunizationDate",vaccineDate);
            params.put("notes",vaccineDescription);
            params.put("patientID", PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
            //params.put("medical_personnel_id", currentMedical.getMedical_person_id());
            params.put("medical_record_id", PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
            params.put("byWhom","medical personnel");
            params.put("byWhomID",currentMedical.getMedical_person_id());
            params.put("doctorMPID","dathu7728@gmail.com");
            params.put("doctorName","Datheswara reddy");
            if( VaccineDataController.getInstance().currentVaccineModel !=null){
                VaccineModel objModel=VaccineDataController.getInstance().currentVaccineModel;
                params.put("immunization_record_id",objModel.getVaccineRecordId());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        if( VaccineDataController.getInstance().currentVaccineModel !=null){
            ApiCallDataController.getInstance().loadServerApiCall(
                    ApiCallDataController.getInstance().serverApi.updateVaccine(currentMedical.getAccessToken(), gsonObject), "update");

        }else {
            ApiCallDataController.getInstance().loadServerApiCall(
                    ApiCallDataController.getInstance().serverApi.addVaccine(currentMedical.getAccessToken(), gsonObject), "insert");

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
}
