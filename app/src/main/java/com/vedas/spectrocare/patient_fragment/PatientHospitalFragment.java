package com.vedas.spectrocare.patient_fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vedas.spectrocare.PatientModule.PatientSelfExamEditActivity;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.SingleTapDetector;
import com.vedas.spectrocare.patientModuleAdapter.PatientHospitalAdapter;
import com.vedas.spectrocare.patientModuleAdapter.PatientSelfExamAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientHospitalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientHospitalFragment extends Fragment {
    RecyclerView patientSelfExamView;
    FloatingActionButton btnAddRecord;
    GestureDetector gestureDetector;
    int lastAction;
    private float dX, dY;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PatientHospitalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientHospitalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientHospitalFragment newInstance(String param1, String param2) {
        PatientHospitalFragment fragment = new PatientHospitalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.fragment_patient_hospital, container, false);
        patientSelfExamView = v.findViewById(R.id.hospital_recycle_view);
        PatientHospitalAdapter hospitalAdapter = new PatientHospitalAdapter(getContext());
        patientSelfExamView.setLayoutManager(new LinearLayoutManager(getContext()));
        patientSelfExamView.setAdapter(hospitalAdapter);
        btnAddRecord = v.findViewById(R.id.btn_add_record);
        gestureDetector = new GestureDetector(getContext(), new SingleTapDetector());
        btnAddRecord.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    getActivity().startActivity(new Intent(getContext(), PatientSelfExamEditActivity.class));
                    //   Toast.makeText(getContext(), "immunization clicked", Toast.LENGTH_SHORT).show();
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

        // Inflate the layout for this fragment
        return v;
    }
}
