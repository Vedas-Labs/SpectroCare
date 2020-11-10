package com.vedas.spectrocare.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.Controllers.AppointmentsServerObjectDataController;
import com.vedas.spectrocare.Controllers.DoctorInfoServerObjectDataController;
import com.vedas.spectrocare.DataBase.AppointmentDataController;
import com.vedas.spectrocare.DataBase.DoctorInfoDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBaseModels.AppointmentModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApiModel.AppointmentServerObjects;
import com.vedas.spectrocare.ServerApiModel.TrackingServerObject;
import com.vedas.spectrocare.adapter.AppointmentAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
public class AppointmentsActivity extends AppCompatActivity {
    ImageView popUpImgBtn;
    TextView txtUpAppointment;
    ArrayList<AppointmentModel> allAppointmentsArray = new ArrayList<>();
    ArrayList<AppointmentModel> filterArray = new ArrayList<>();
    RecyclerView appointmentRecycleView;
    AlertDialog.Builder alertDialog;
    AlertDialog dialog;
    RelativeLayout imgAlertButton;
    ImageView  imgBack;
    EditText edtSearch;
    RefreshShowingDialog showingDialog;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
   // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
    boolean isPastClick = false, isUpcoming = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);
        clickListners();
        refreshRecyclerView();
        accessInterfaceMethods();
        if (isConn()) {
            showingDialog.showAlert();
            DoctorInfoServerObjectDataController.getInstance().clearFilterData();
            AppointmentsServerObjectDataController.getInstance().fetchAppointment();
        }
        DoctorInfoDataController.getInstance().fetchDoctorInfoData();
        if (DoctorInfoDataController.getInstance().allDoctorInfo.size() == 0) {
          //showingDialog.showAlert();
            DoctorInfoServerObjectDataController.getInstance().fetchHospitaldoctors();
        }
    }
    public void onResume() {
        super.onResume();
        if(FiltersActivity.isSelectFilter){
            /*Log.e("testArrays","call"+DoctorInfoServerObjectDataController.getInstance().selectedVisiteTypeArray.size());
            Log.e("testArrays","call"+DoctorInfoServerObjectDataController.getInstance().selectedStatusTypeArray.size());
            Log.e("testArrays","call"+DoctorInfoServerObjectDataController.getInstance().selectedDepartmentArray.size());
            */ArrayList<String> visiteList=DoctorInfoServerObjectDataController.getInstance().selectedVisiteTypeArray;
            ArrayList<String> statusTypeArray=DoctorInfoServerObjectDataController.getInstance().selectedStatusTypeArray;
            ArrayList<String> departmentArray=DoctorInfoServerObjectDataController.getInstance().selectedDepartmentArray;
            if(visiteList.size()==0 && statusTypeArray.size()==0 && departmentArray.size()==0){// filter is empty load all appointments
                txtUpAppointment.setText("All Appointments");
                refreshRecyclerView();
            }else {
                filterAppointments(visiteList, statusTypeArray, departmentArray);
            }
        }else if(isPastClick || isUpcoming){
            appointmentRecycleView(filterArray);
        }
        else {
            refreshRecyclerView();
        }
    }
    public void appointmentRecycleView(ArrayList<AppointmentModel> list) {
        AppointmentAdapter appointmentAdapter = new AppointmentAdapter(AppointmentsActivity.this, list);
        appointmentRecycleView.setLayoutManager(new LinearLayoutManager(this));
        appointmentRecycleView.setAdapter(appointmentAdapter);
        appointmentAdapter.notifyDataSetChanged();
    }
    private void refreshRecyclerView() {
        PatientProfileDataController.getInstance().fetchPatientlProfileData();
        AppointmentDataController.getInstance().fetchAppointmentData(PatientProfileDataController.getInstance().currentPatientlProfile);
        allAppointmentsArray = AppointmentDataController.getInstance().allAppointmentList;
        if(allAppointmentsArray.size()==0){
            edtSearch.setVisibility(View.GONE);
        }else {
            edtSearch.setVisibility(View.VISIBLE);
        }
        filterArray = allAppointmentsArray;
        appointmentRecycleView(allAppointmentsArray);
    }
    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                if (curdOpetaton.equals("fetch")) {
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("appointments");
                        PatientProfileDataController.getInstance().fetchPatientlProfileData();
                        AppointmentDataController.getInstance().deleteAppointmentData(PatientProfileDataController.getInstance().currentPatientlProfile);
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Gson gson = new Gson();
                                AppointmentServerObjects userIdentifier = gson.fromJson(jsonArray.getJSONObject(i).toString(), AppointmentServerObjects.class);
                                ArrayList<TrackingServerObject> trackArray = userIdentifier.getTracking();
                                Log.e("trackArray", "call" + trackArray.size());
                                String profilePic = userIdentifier.getPatientName();
                                String profilePic1 = userIdentifier.getAppointmentStatus();
                                Log.e("doctorProfilePic", "call" + profilePic + profilePic1);
                                AppointmentsServerObjectDataController.getInstance().processAppointmentdata(userIdentifier, trackArray);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    refreshRecyclerView();
                    showingDialog.hideRefreshDialog();
                } /*else if (curdOpetaton.equals("delete")) {
                    AppointmentDataController.getInstance().deleteAppointmentModelData(PatientProfileDataController.getInstance().currentPatientlProfile, AppointmentDataController.getInstance().currentAppointmentModel);
                }*/else if (curdOpetaton.equals("fetchDoctor")) {
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("doctors");
                        Log.e("fetchDoctor", "call" + jsonArray.length());
                        DoctorInfoDataController.getInstance().deleteDoctorInfoData(DoctorInfoDataController.getInstance().allDoctorInfo);
                        DoctorInfoServerObjectDataController.getInstance().processDoctorsData(jsonArray);
                    } catch (JSONException e) {

                    }
                }

            }

            @Override
            public void failureCallBack(String failureMsg) {
                showingDialog.hideRefreshDialog();
                Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void clickListners() {
        showingDialog = new RefreshShowingDialog(AppointmentsActivity.this);
        appointmentRecycleView = findViewById(R.id.appointments_recycle_view);
        LayoutInflater inflater = AppointmentsActivity.this.getLayoutInflater();
        edtSearch = findViewById(R.id.search);
        final View dialogView = inflater.inflate(R.layout.appointment_dailog, null);
        txtUpAppointment = findViewById(R.id.txt_up_appointment);
        imgBack = findViewById(R.id.image_back);
        popUpImgBtn = findViewById(R.id.pop_up_img_btn);
        imgAlertButton = findViewById(R.id.img_alert_btn);
        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(dialogView);
        dialog = alertDialog.create();
        imgAlertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showappointmentBottomSheet();
            }
        });
        popUpImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoctorInfoServerObjectDataController.getInstance().processFilterArrays();
                startActivity(new Intent(getApplicationContext(),FiltersActivity.class));
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateSearchResults(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void updateSearchResults(String text) {
        Log.e("updateSearchResults", "call" + filterArray.size());
        ArrayList<AppointmentModel> filterArray1 = new ArrayList<>();
        if (filterArray.size() > 0) {
            for (AppointmentModel object : filterArray) {
                if (object.getDoctorName().toLowerCase().startsWith(text) ||
                        object.getDepartment().toLowerCase().startsWith(text)
                        || object.getAppointmentStatus().toLowerCase().startsWith(text)
                        || object.getVisitType().toLowerCase().startsWith(text)) {
                    filterArray1.add(object);
                }
            }
            appointmentRecycleView(filterArray1);
        }
    }


    public void showappointmentBottomSheet() {
        View sheetView = getLayoutInflater().inflate(R.layout.appointment_dailog, null);
        final BottomSheetDialog appointmentBottomSheetDialog = new BottomSheetDialog(AppointmentsActivity.this, R.style.BottomSheetDialogTheme);
        appointmentBottomSheetDialog.setContentView(sheetView);
        appointmentBottomSheetDialog.show();
        FrameLayout bottomSheet = (FrameLayout) appointmentBottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        bottomSheet.setBackground(null);

        LinearLayout txtallAppointment = appointmentBottomSheetDialog.findViewById(R.id.txt_all_appointments);
        LinearLayout txtUpdateAppointment = appointmentBottomSheetDialog.findViewById(R.id.txt_upcmng_appointments);
        LinearLayout txtBookAppointment = appointmentBottomSheetDialog.findViewById(R.id.txt_book_appointments);
        LinearLayout txtPastAppointment = appointmentBottomSheetDialog.findViewById(R.id.txt_past_appointments);
        LinearLayout txtCalender = appointmentBottomSheetDialog.findViewById(R.id.layout_cal);
        LinearLayout dismissLayout = appointmentBottomSheetDialog.findViewById(R.id.layout_dismiss);

        dismissLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentBottomSheetDialog.dismiss();
            }
        });
        txtCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentBottomSheetDialog.dismiss();
                DoctorInfoServerObjectDataController.getInstance().clearFilterData();
                AppointmentDataController.getInstance().currentAppointmentModel = null;
                Intent i = new Intent(AppointmentsActivity.this, CalendarActivity.class);
                startActivity(i);
            }
        });
        txtBookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentBottomSheetDialog.dismiss();
                DoctorInfoServerObjectDataController.getInstance().clearFilterData();
                AppointmentDataController.getInstance().currentAppointmentModel = null;
                Intent i = new Intent(AppointmentsActivity.this, BookAppointmentsActivity.class);
                startActivity(i);
            }
        });
        txtPastAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentBottomSheetDialog.dismiss();
                DoctorInfoServerObjectDataController.getInstance().clearFilterData();
                FiltersActivity.isSelectFilter=false;
                txtUpAppointment.setText("Past Appointments");
                isPastClick = true;
                isUpcoming = false;
                loadPastAppointments();
            }
        });
        txtallAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentBottomSheetDialog.dismiss();
                DoctorInfoServerObjectDataController.getInstance().clearFilterData();
                FiltersActivity.isSelectFilter=false;
                txtUpAppointment.setText("All Appointments");
                refreshRecyclerView();
            }
        });
        txtUpdateAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentBottomSheetDialog.dismiss();
                FiltersActivity.isSelectFilter=false;
                DoctorInfoServerObjectDataController.getInstance().clearFilterData();
                txtUpAppointment.setText("Upcoming Appointments");
                isPastClick = false;
                isUpcoming = true;
                loadUpcomingAppointments();
            }
        });
    }
    private void loadUpcomingAppointments() {
        filterArray = new ArrayList<>();
        if (allAppointmentsArray.size() > 0) {
            for (int i = 0; i < allAppointmentsArray.size(); i++) {
                AppointmentModel model = allAppointmentsArray.get(i);
                String date = model.getAppointmentDate() + " " + model.getAppointmentTimeTo();
                Log.e("date", "call" + date);
                try {
                    Date d1 = dateFormat.parse(date);
                    Date d2 = new Date();
                    System.out.println("The date 1 is: " + dateFormat.format(d1));
                    System.out.println("The date 2 is: " + dateFormat.format(d2));
                    if (d1.compareTo(d2) > 0) {
                        System.out.println("Date 1 occurs after Date 2");
                        filterArray.add(model);
                    } /*else if (d1.compareTo(d2) < 0) {
                        System.out.println("Date 1 occurs before Date 2");
                        upcomingAppointmetArrayList.add(model);
                    } else if (d1.compareTo(d2) == 0) {
                        System.out.println("Both dates are equal");
                    }*/
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            appointmentRecycleView(filterArray);
        }
    }

    private void loadPastAppointments() {
        filterArray = new ArrayList<>();
        if (allAppointmentsArray.size() > 0) {
            for (int i = 0; i < allAppointmentsArray.size(); i++) {
                AppointmentModel model = allAppointmentsArray.get(i);
                String date = model.getAppointmentDate() + " " + model.getAppointmentTimeTo();
                Log.e("date", "call" + date);
                try {
                    Date d1 = dateFormat.parse(date);
                    Date d2 = new Date();
                    System.out.println("The date 1 is: " + dateFormat.format(d1));
                    System.out.println("The date 2 is: " + dateFormat.format(d2));
                    if (d1.compareTo(d2) < 0) {
                        System.out.println("Date 1 occurs before Date 2");
                        filterArray.add(model);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            appointmentRecycleView(filterArray);
        }
    }

    private void filterAppointments(ArrayList<String> visitype, ArrayList<String> status, ArrayList<String> department) {
       // FiltersActivity.isSelectFilter=false;
        ArrayList<AppointmentModel> filterArray1 = new ArrayList<>();
        if (filterArray.size() > 0) {
            for (AppointmentModel object : filterArray) {
                if(visitype.size()>0){
                    for (int s = 0; s < visitype.size(); s++) {
                        if (object.getVisitType().toLowerCase().contains(visitype.get(s))) {
                            filterArray1.add(object);
                        }
                    }
                }
                if(status.size()>0){
                    for (int s = 0; s < status.size(); s++) {
                        if (object.getAppointmentStatus().toLowerCase().contains(status.get(s).toLowerCase())) {
                            filterArray1.add(object);
                        }
                    }
                }
                if(department.size()>0){
                    for (int d = 0; d < department.size(); d++) {
                        if (object.getDepartment().toLowerCase().contains(department.get(d).toLowerCase())) {
                            filterArray1.add(object);
                        }
                    }
                }
            }
            Set<AppointmentModel> primesWithoutDuplicates = new LinkedHashSet<>(filterArray1);//remove dulicate elements
            filterArray1.clear();
            filterArray1.addAll(primesWithoutDuplicates);
            Log.e("date", "call" + filterArray1.size());
           // filterArray = filterArray1;
            if(filterArray1.size()==0){
                edtSearch.setVisibility(View.GONE);
            }else {
                edtSearch.setVisibility(View.VISIBLE);
            }
            appointmentRecycleView(filterArray1);
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
