package com.vedas.spectrocare.PatientModule;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientAppointmentModule.AppointmentArrayModel;
import com.vedas.spectrocare.PatientAppointmentModule.PatientAppointmentsDataController;
import com.vedas.spectrocare.PatinetControllers.PatientAppointmentController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.model.AppointmetModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class PastAppointmentFragment extends Fragment {
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    Calendar calendar;
    UpComingAppointmentsAdapter pastAdapter;
    ArrayList<AppointmentArrayModel> modelArrayList;
    RefreshShowingDialog refreshShowingDialog;
    RecyclerView recyclerView;
    RelativeLayout mainLayout;
    ArrayList<AppointmentArrayModel> sortedPastList = new ArrayList<>();
    EditText ed_search;

    public PastAppointmentFragment() {
    }
    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PastAppointmentFragment newInstance(int columnCount) {
        PastAppointmentFragment fragment = new PastAppointmentFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pastappointment, container, false);
        Log.e("appointments","past");
        mainLayout= (RelativeLayout)view. findViewById(R.id.mainLayout);
        calendar = Calendar.getInstance();
        ed_search = view.findViewById(R.id.search);
        loadEdittext();
        modelArrayList = new ArrayList<>();
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.WHITE);
        gd.setCornerRadius(10);
        gd.setStroke(2, Color.parseColor("#615D5E"));
        mainLayout.setBackgroundDrawable(gd);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        refreshShowingDialog = new RefreshShowingDialog(getContext());
        return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
           // pastList.clear();
            if (!PatientAppointmentsDataController.isNull()){
               /* for (int i=0;i<PatientAppointmentsDataController.getInstance().getAppointmentsList().size();i++){
                    if (PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i).
                            getAppointmentDetails().getAppointmentStatus().equals("Cancelled") && PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i).
                                            getAppointmentDetails().getAppointmentStatus().equals("Completed")){
                        Log.e("pastApppa","daafd"+PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i).
                                getAppointmentDetails().getAppointmentStatus());
                        appointmentModel = new AppointmentArrayModel();
                        appointmentModel = PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i);
                        pastList.add(appointmentModel);

                    }
                }*/
                Log.e("jjdkdl","aa"+PatientAppointmentsDataController.getInstance().pastAppointmentsList.size());
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                sortedPastList=PatientAppointmentsDataController.getInstance().pastAppointmentsList;
                pastAdapter = new UpComingAppointmentsAdapter(getContext(),sortedPastList);
                recyclerView.setAdapter(pastAdapter);

            }else
                Log.e("reeee","yyyyy");
        }
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
                    pastAdapter.filterList(sortedPastList);
                }

            }
        });
    }

    private void filterArray(String val) {
        ArrayList<AppointmentArrayModel> tempdAppointmentsList = new ArrayList<>();

        for (int i = 0; i <sortedPastList.size(); i++) {
            AppointmentArrayModel obj = sortedPastList.get(i);
            String name=obj.getDoctorDetails().getProfile().getUserProfile().getFirstName()+" "+obj.getDoctorDetails().getProfile().getUserProfile().getLastName();
            if (name.toLowerCase().startsWith(val)) {
                tempdAppointmentsList.add(obj);
            }
        }
        Log.e("filterArray", "call" +tempdAppointmentsList.size());
        pastAdapter.filterList(tempdAppointmentsList);
    }
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(/*DummyItem item*/);
    }
}
