package com.vedas.spectrocare.PatientModule;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vedas.spectrocare.R;
import com.vedas.spectrocare.model.RecordModel;
import com.vedas.spectrocare.patientModuleAdapter.DoctorsAvailabilityAdapter;

import java.util.ArrayList;

public class AvailabilityFragment extends Fragment {
    RecyclerView availableTimeView;
  ArrayList<RecordModel> timingsList;
  DoctorsAvailabilityAdapter availabilityAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_availibility, container, false);
        availableTimeView = view.findViewById(R.id.available_view);
        timingsList = new ArrayList<>();
        timingsList.add(new RecordModel("Mon","09:00-12:00","14:00-18:00"));
        timingsList.add(new RecordModel("Tue","09:00-12:00","14:00-18:00"));
        timingsList.add(new RecordModel("Wed","09:00-12:00","14:00-18:00"));
        timingsList.add(new RecordModel("Thu","09:00-12:00","14:00-18:00"));
        timingsList.add(new RecordModel("Fri","09:00-12:00","14:00-18:00"));
        timingsList.add(new RecordModel("Sat","09:00-12:00","14:00-18:00"));
        timingsList.add(new RecordModel("Sun","09:00-12:00","14:00-18:00"));

        availabilityAdapter = new DoctorsAvailabilityAdapter(container.getContext(),timingsList);
        availableTimeView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        availableTimeView.setAdapter(availabilityAdapter);
        return view;
    }
}
