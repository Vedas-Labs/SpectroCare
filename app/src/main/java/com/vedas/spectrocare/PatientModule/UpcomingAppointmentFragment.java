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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.AppointmentDataController;
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

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class UpcomingAppointmentFragment extends Fragment {
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    RelativeLayout mainLayout;
    UpComingAppointmentsAdapter upComingAdapter;
    AppointmetModel model;
    ArrayList<AppointmentArrayModel> modelArrayList;
    RefreshShowingDialog refreshShowingDialog;
    Calendar calendar;
    RecyclerView recyclerView;
    AppointmentArrayModel appointmentModel;
    ArrayList<AppointmentArrayModel> upcomingList= new ArrayList<>();
    ArrayList<AppointmetModel> appointmentList= new ArrayList<>();
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
        mainLayout= (RelativeLayout)view. findViewById(R.id.mainLayout);
        modelArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        //PatientAppointmentsDataController.setNull();
        Log.e("nulll","dddd"+PatientAppointmentsDataController.isNull());
        refreshShowingDialog = new RefreshShowingDialog(getContext());

        calendar = Calendar.getInstance();
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.WHITE);
        gd.setCornerRadius(10);
        gd.setStroke(2, Color.parseColor("#615D5E"));
        Log.e("alskd","ss"+calendar.getTimeInMillis());
        mainLayout.setBackgroundDrawable(gd);
        if (isNetworkConnected()){
            refreshShowingDialog.showAlert();
            fetchAppointmentDetails();
            accessInterfaceMethod();

        }
        else{
            refreshShowingDialog.hideRefreshDialog();
            Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
        }

        if (!PatientAppointmentsDataController.isNull()){
            Log.e("adsfafdffa","dafsfasfdsa"+PatientAppointmentsDataController.getInstance().getAppointmentsList().size());

           /* for (int i=0;i<PatientAppointmentsDataController.getInstance().getAppointmentsList().size();i++){
              *//*  if ((calendar.getTimeInMillis())/1000<Long.parseLong(PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i).
                getAppointmentDetails().getAppointmentDate())){

                }*//*


           if (!PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i).
                   getAppointmentDetails().getAppointmentStatus().equals("Cancelled") && !PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i).
                   getAppointmentDetails().getAppointmentStatus().equals("Completed")){
               Log.e("lalalalalalalal","daafd"+PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i).
                       getAppointmentDetails().getAppointmentStatus());
               appointmentModel = new AppointmentArrayModel();
               appointmentModel = PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i);
               upcomingList.add(appointmentModel);

           }
            }
            Log.e("jjdkdl","aa"+upcomingList.size());
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            upComingAdapter = new UpComingAppointmentsAdapter(getContext(),upcomingList);
            recyclerView.setAdapter(upComingAdapter);
*/
        }
        // Set the adapter
        if(!PatientAppointmentController.isNull()){
            Log.e("dassaf","fda"+PatientAppointmentController.getInstance().getAppointmentList().get(0).getDate());
            Log.e("uuusus","dadu"+ PatientAppointmentController.getInstance().getAppointmentList().size());
            for (int i=0;i<PatientAppointmentController.getInstance().getAppointmentList().size();i++){
                if (calendar.getTimeInMillis()<Long.parseLong(PatientAppointmentController.getInstance().getAppointmentList().get(i).getDate())){
                    model = new AppointmetModel();
                    Log.e("dfafadf","dfa"+PatientAppointmentController.getInstance()
                            .getAppointmentList().get(i).getApprove());
                    model = PatientAppointmentController.getInstance()
                            .getAppointmentList().get(i);
                   /* model.setDate(PatientAppointmentController.getInstance()
                            .getAppointmentList().get(i).getDate());
                    model.setTime(
                            PatientAppointmentController.getInstance()
                                    .getAppointmentList().get(i).getTime());
                    model.setSpecialization(
                            PatientAppointmentController.getInstance()
                                    .getAppointmentList().get(i).getSpecialization());
                    model.setDocName(
                            PatientAppointmentController.getInstance()
                                    .getAppointmentList().get(i).getDocName());
                    model.setApprove(PatientAppointmentController.getInstance().getAppointmentList()
                    .get(i).getApprove());
                    model.setReason(PatientAppointmentController.getInstance()
                            .getAppointmentList().get(i).getReason());
*/
                    appointmentList.add(model);
                }

            }
        }
        Log.e("sicfdf","adf"+appointmentList.size());

        return view;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(/*DummyItem item*/);
    }
    private void fetchAppointmentDetails(){
       // PatientAppointmentsDataController.setNull();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
            jsonObject.put("patientID",PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject body = (JsonObject) jsonParser.parse(jsonObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(ApiCallDataController.getInstance().serverJsonApi.fetchAppointmentDetaisl(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken()
                ,body),"fetchAppointment");
    }

    public void accessInterfaceMethod(){
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String opetation) {
                refreshShowingDialog.hideRefreshDialog();
                try {
                    Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (opetation.equals("fetchAppointment")){

                    try {
                        Log.e("kkkkkk","dd"+jsonObject.getString("response"));

                        if (jsonObject.getString("response").equals("3")){
                            JSONArray appointmentArray = jsonObject.getJSONArray("appointments");
                            Log.e("appontment","length"+appointmentArray.length());

                            for (int l =0;l<appointmentArray.length();l++){
                                Gson gson = new Gson();
                                String jsonString= jsonObject.getJSONArray("appointments").getJSONObject(l).toString();
                                AppointmentArrayModel appointmentList = gson.fromJson(jsonString, AppointmentArrayModel.class);
                                String kk = gson.toJson(appointmentList);
                                Log.e("logaaaa","daffff"+kk);
                                modelArrayList.add(appointmentList);
                            }
                            PatientAppointmentsDataController.getInstance().setAppointmentsList(modelArrayList);

                            if (!PatientAppointmentsDataController.isNull()){
                                Log.e("adsfafdffa","dafsfasfdsa"+PatientAppointmentsDataController.getInstance().getAppointmentsList().size());

                                for (int i=0;i<PatientAppointmentsDataController.getInstance().getAppointmentsList().size();i++){
                                    if (!PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i).
                                            getAppointmentDetails().getAppointmentStatus().equals("Cancelled") && !PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i).
                                            getAppointmentDetails().getAppointmentStatus().equals("Completed")){
                                        Log.e("lalalalalalalal","daafd"+PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i).
                                                getAppointmentDetails().getAppointmentStatus());
                                        appointmentModel = new AppointmentArrayModel();
                                        appointmentModel = PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i);
                                        upcomingList.add(appointmentModel);

                                    }
                                }
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                upComingAdapter = new UpComingAppointmentsAdapter(getContext(),upcomingList);
                                recyclerView.setAdapter(upComingAdapter);
                               // upComingAdapter.notifyDataSetChanged();
                                Log.e("jjdkdl","aa"+upcomingList.size());


                            }

                            Log.e("ffffaaaaa","dgga"+PatientAppointmentsDataController.getInstance().getAppointmentsList().size());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void failureCallBack(String failureMsg) {
                refreshShowingDialog.hideRefreshDialog();

                Toast.makeText(getActivity(), "Please check your connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
