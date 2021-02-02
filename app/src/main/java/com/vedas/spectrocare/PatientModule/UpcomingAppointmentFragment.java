package com.vedas.spectrocare.PatientModule;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.DataBase.AppointmentDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBaseModels.UrineresultsModel;
import com.vedas.spectrocare.PatientAppointmentModule.AppointmentArrayModel;
import com.vedas.spectrocare.PatientAppointmentModule.PatientAppointmentsDataController;
import com.vedas.spectrocare.PatinetControllers.PatientAppointmentController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.model.AppointmetModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class UpcomingAppointmentFragment extends Fragment {
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    RelativeLayout mainLayout;
    UpComingAppointmentsAdapter upComingAdapter;
    AppointmetModel model;
    RefreshShowingDialog refreshShowingDialog;
    Calendar calendar;
    RecyclerView recyclerView;
    EditText ed_search;
    ArrayList<AppointmentArrayModel> sortedUpcomingList = new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;

    public UpcomingAppointmentFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static UpcomingAppointmentFragment newInstance(int columnCount) {
        UpcomingAppointmentFragment fragment = new UpcomingAppointmentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);

        Log.e("appointments", "Update");
        mainLayout = (RelativeLayout) view.findViewById(R.id.mainLayout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorOrange);
        //​mSwipeRefreshLayout.setColorSchemeResources(R.color.colorOrange);
        recyclerView = view.findViewById(R.id.list);
        ed_search = view.findViewById(R.id.search);
        refreshShowingDialog = new RefreshShowingDialog(getContext());
        loadRecyclerView();

        calendar = Calendar.getInstance();
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.WHITE);
        gd.setCornerRadius(10);
        gd.setStroke(2, Color.parseColor("#615D5E"));
        Log.e("alskd", "ss" + calendar.getTimeInMillis());
        loadEdittext();
        mainLayout.setBackgroundDrawable(gd);
        accessInterfaceMethod();

        if(PatientAppointmentsDataController.getInstance().allappointmentsList.size()>0){
           // PatientAppointmentsDataController.getInstance().allappointmentsList.clear();
            PatientAppointmentsDataController.getInstance().upcomingAppointmentsList.clear();
            PatientAppointmentsDataController.getInstance().pastAppointmentsList.clear();
            sortedUpcomingList.clear();
            sortResultsBasedOnTime(PatientAppointmentsDataController.getInstance().allappointmentsList);
            sortedUpcomingList = PatientAppointmentsDataController.getInstance().upcomingAppointmentsList;
            loadRecyclerView();
        }
        fetchAppointmentDetails();
        /*else{
            if (isNetworkConnected()) {
                refreshShowingDialog.showAlert();
                fetchAppointmentDetails();
            } else {
                refreshShowingDialog.hideRefreshDialog();
            }
        }*/

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchAppointmentDetails();
            }
        });

        return view;
    }

    private void loadRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        upComingAdapter = new UpComingAppointmentsAdapter(getContext(),sortedUpcomingList);
        recyclerView.setAdapter(upComingAdapter);
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(/*DummyItem item*/);
    }

    private void loadEdittext() {

        ed_search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    Log.e("dssssssss", "call" + s.toString());
                  //  filterArray(s.toString().toLowerCase());
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.toString().length() > 0) {
                    Log.e("dssssssss", "call" + s.toString());
                    filterArray(s.toString().toLowerCase());
                }else {
                    upComingAdapter.filterList(sortedUpcomingList);
                }

            }
        });
    }

    private void filterArray(String val) {
        ArrayList<AppointmentArrayModel> tempdAppointmentsList = new ArrayList<>();

        for (int i = 0; i < PatientAppointmentsDataController.getInstance().upcomingAppointmentsList.size(); i++) {
            AppointmentArrayModel obj = PatientAppointmentsDataController.getInstance().upcomingAppointmentsList.get(i);
            String name=obj.getDoctorDetails().getProfile().getUserProfile().getFirstName()+" "+obj.getDoctorDetails().getProfile().getUserProfile().getLastName();
            if (name.toLowerCase().startsWith(val)) {
                tempdAppointmentsList.add(obj);
            }
        }
        Log.e("filterArray", "call" +tempdAppointmentsList.size());
        upComingAdapter.filterList(tempdAppointmentsList);
    }
    private void fetchAppointmentDetails() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
            jsonObject.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject body = (JsonObject) jsonParser.parse(jsonObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.fetchAppointmentDetaisl(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken()
                , body), "fetchAppointment");
    }

    @Override
    public void onStart() {
        super.onStart();
        upComingAdapter.notifyDataSetChanged();
    }

    public void accessInterfaceMethod() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String opetation) {
                refreshShowingDialog.hideRefreshDialog();
                if (opetation.equals("fetchAppointment")) {
                    try {
                        if (jsonObject.getString("response").equals("3")) {
                            PatientAppointmentsDataController.getInstance().allappointmentsList.clear();
                            PatientAppointmentsDataController.getInstance().upcomingAppointmentsList.clear();
                            PatientAppointmentsDataController.getInstance().pastAppointmentsList.clear();
                            sortedUpcomingList.clear();
                            mSwipeRefreshLayout.setRefreshing(false);
                            JSONArray appointmentArray = jsonObject.getJSONArray("appointments");
                            Log.e("appontment", "length" + appointmentArray.length());

                            for (int l = 0; l < appointmentArray.length(); l++) {
                                Gson gson = new Gson();
                                String jsonString = jsonObject.getJSONArray("appointments").getJSONObject(l).toString();
                                AppointmentArrayModel appointmentList = gson.fromJson(jsonString, AppointmentArrayModel.class);
                                PatientAppointmentsDataController.getInstance().allappointmentsList.add(appointmentList);
                            }
                            PatientAppointmentsDataController.getInstance().setAppointmentsList(PatientAppointmentsDataController.getInstance().allappointmentsList);
                            Log.e("appointmentsize", "dd" +  PatientAppointmentsDataController.getInstance().allappointmentsList.size());

                            if (PatientAppointmentsDataController.getInstance().allappointmentsList.size() > 0) {
                                sortResultsBasedOnTime(PatientAppointmentsDataController.getInstance().allappointmentsList);
                                sortedUpcomingList = PatientAppointmentsDataController.getInstance().upcomingAppointmentsList;
                                loadRecyclerView();
                               // upComingAdapter.filterList(sortedUpcomingList);
                            }
                        }
                    } catch (Exception e) {
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

    public void sortResultsBasedOnTime(ArrayList<AppointmentArrayModel> list) {
        Date currentDate = new Date();
        for (int i = 0; i < list.size(); i++) {
            Date date = new Date(Long.parseLong(list.get(i).getAppointmentDetails().getAppointmentDate()));
            Log.e("xxxx", "daffff" + date.getTime());
            if (currentDate.compareTo(date) <= 0) {
                Log.e("upcominglist", "daffff" + date.getTime());
                PatientAppointmentsDataController.getInstance().upcomingAppointmentsList.add(list.get(i));
            }
            if (currentDate.compareTo(date) > 0) {
                Log.e("pastlist", "daffff" + date.getTime());
                PatientAppointmentsDataController.getInstance().pastAppointmentsList.add(list.get(i));
            }
        }
        Log.e("upcominglist", "daffff" + PatientAppointmentsDataController.getInstance().upcomingAppointmentsList.size());
        Log.e("qqqqqqqqqqq", "daffff" + PatientAppointmentsDataController.getInstance().pastAppointmentsList.size());

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
