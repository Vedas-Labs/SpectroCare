package com.vedas.spectrocare.PatientModule;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vedas.spectrocare.PatientDocResponseModel.OfficeHours;
import com.vedas.spectrocare.PatientDocResponseModel.ServiceTimeModel;
import com.vedas.spectrocare.PatientDocResponseModel.Sessions;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.model.RecordModel;
import com.vedas.spectrocare.patientModuleAdapter.DoctorsAvailabilityAdapter;

import java.util.ArrayList;

public class AvailabilityFragment extends Fragment {
    RecyclerView availableTimeView;
    DoctorsAvailabilityAdapter availabilityAdapter;
    ArrayList<OfficeHours> sessionsArrayList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_availibility, container, false);
        availableTimeView = view.findViewById(R.id.available_view);
        loadOfficeHours();
        availabilityAdapter = new DoctorsAvailabilityAdapter(container.getContext(),sessionsArrayList);
        availableTimeView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        availableTimeView.setAdapter(availabilityAdapter);
        return view;
    }
    private void loadOfficeHours() {
        if (PatientMedicalRecordsController.getInstance().medicalPersonnelModel != null) {
            if(PatientMedicalRecordsController.getInstance().medicalPersonnelModel.getProfile().getGeneralInformation() != null){
                ServiceTimeModel obj=PatientMedicalRecordsController.getInstance().medicalPersonnelModel.getServiceTime();
                for(int i=0;i<obj.getOfficeHours().size();i++){
                    OfficeHours sessions=obj.getOfficeHours().get(i);
                    sessionsArrayList.add(sessions);
                   /*getSessions();

                    for(int k=0;k<sessions.size();k++){
                        sessionsArrayList.add(sessions.get(k));
                    }*/
                }
            }
        }
    }
}
