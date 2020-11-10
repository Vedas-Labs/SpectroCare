package com.vedas.spectrocare.patient_fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vedas.spectrocare.PatientModule.AddPatientAllergyActivity;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.SingleTapDetector;
import com.vedas.spectrocare.patientModuleAdapter.PatientAllergyAdapter;
import com.vedas.spectrocare.patientModuleAdapter.PatientDiseaseAdapter;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientAllergyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientAllergyFragment extends Fragment {
    RecyclerView patientAllergyRecycleView;
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

    public PatientAllergyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientAllergyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientAllergyFragment newInstance(String param1, String param2) {
        PatientAllergyFragment fragment = new PatientAllergyFragment();
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
        View v =inflater.inflate(R.layout.fragment_patient_allergy, container, false);
        patientAllergyRecycleView = v.findViewById(R.id.patient_allergy_recycle_view);
        PatientAllergyAdapter allergyAdapter = new PatientAllergyAdapter(getContext());
      //  PatientAllergyAdapter diseaseAdapter = new PatientAllergyAdapter(getContext());
        patientAllergyRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        patientAllergyRecycleView.setAdapter(allergyAdapter);
        btnAddRecord = v.findViewById(R.id.btn_add_record);
        gestureDetector = new GestureDetector(getContext(), new SingleTapDetector());
        btnAddRecord.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                   // Toast.makeText(getContext(), "allergy clicked", Toast.LENGTH_SHORT).show();
                    getActivity().startActivity(new Intent(getContext(), AddPatientAllergyActivity.class));
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

        return v;
    }
}
