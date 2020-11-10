package com.vedas.spectrocare.PatientModule;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.vedas.spectrocare.PatientAppointmentModule.AppointmentArrayModel;
import com.vedas.spectrocare.PatientAppointmentModule.PatientAppointmentsDataController;
import com.vedas.spectrocare.PatinetControllers.PatientAppointmentController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.model.AppointmetModel;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PastAppointmentFragment extends Fragment {
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    Calendar calendar;
    AppointmetModel model;
    AppointmentArrayModel appointmentModel;
    ArrayList<AppointmentArrayModel> pastList = new ArrayList<>();
    ArrayList<AppointmetModel> appointmentList= new ArrayList<>();
    RelativeLayout mainLayout;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
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
        mainLayout= (RelativeLayout)view. findViewById(R.id.mainLayout);
        calendar = Calendar.getInstance();
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.WHITE);
        gd.setCornerRadius(10);
        gd.setStroke(2, Color.parseColor("#615D5E"));
        mainLayout.setBackgroundDrawable(gd);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);

        if (!PatientAppointmentsDataController.isNull()){
            Log.e("pastAppointment","past" );

            for (int i=0;i<PatientAppointmentsDataController.getInstance().getAppointmentsList().size();i++){
/*
                if ((calendar.getTimeInMillis())/1000<Long.parseLong(PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i).
                getAppointmentDetails().getAppointmentDate())){

                }
*/
                if (PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i).
                        getAppointmentDetails().getAppointmentStatus().equals("Cancelled")/* || PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i).
                        getAppointmentDetails().getAppointmentStatus().equals("Completed")*/){
                    Log.e("Paaaassttt","daafd"+PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i).
                            getAppointmentDetails().getAppointmentStatus());
                    appointmentModel = new AppointmentArrayModel();
                    appointmentModel = PatientAppointmentsDataController.getInstance().getAppointmentsList().get(i);
                    pastList.add(appointmentModel);

                }
            }
            Log.e("jjdkdl","aa"+pastList.size());

        }else{
            Log.e("nullAppointments","null");
        }

/*
        if(!PatientAppointmentController.isNull()){
            Log.e("dassaf","fda"+PatientAppointmentController.getInstance().getAppointmentList().get(0).getDate());
            Log.e("uuusus","dadu"+ PatientAppointmentController.getInstance().getAppointmentList().size());
            for (int i=0;i<PatientAppointmentController.getInstance().getAppointmentList().size();i++){
                if (calendar.getTimeInMillis()>Long.parseLong(PatientAppointmentController.getInstance().getAppointmentList().get(i).getDate())){
                    model = new AppointmetModel();

                    Log.e("dfafadf","dfa"+PatientAppointmentController.getInstance()
                            .getAppointmentList().get(i).getApprove());
                    model = PatientAppointmentController.getInstance()
                            .getAppointmentList().get(i);
                   */
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
                    model.setReason(PatientAppointmentController.getInstance().getAppointmentList()
                            .get(i).getReason());
*//*

                    appointmentList.add(model);
                }

            }
        }
*/
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new UpComingAppointmentsAdapter(getActivity(),pastList));
        return view;
    }

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(/*DummyItem item*/);
    }
}
