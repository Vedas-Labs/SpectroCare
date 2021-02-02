package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vedas.spectrocare.PatientDocResponseModel.ClinicalServices;
import com.vedas.spectrocare.R;

import java.util.ArrayList;

public class DoctorsClinicalSummaryAdapter extends RecyclerView.Adapter<DoctorsClinicalSummaryAdapter.AvailablityHolder> {
    Context context;
    ArrayList<ClinicalServices> listOfTimings;

    public DoctorsClinicalSummaryAdapter(Context context, ArrayList<ClinicalServices> listOfTimings) {
        this.context = context;
        this.listOfTimings = listOfTimings;
    }

    @Override
    public DoctorsClinicalSummaryAdapter.AvailablityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View doctorAvailableView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clinicalsummary_list, parent, false);
        return new AvailablityHolder(doctorAvailableView);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorsClinicalSummaryAdapter.AvailablityHolder holder, int position) {
        ClinicalServices obj = listOfTimings.get(position);
        holder.txtServiceName.setText(obj.getServiceName());
        holder.txtSummery.setText(obj.getServiceDescription());
    }

    @Override
    public int getItemCount() {
        if (listOfTimings.size() > 0) {
            return listOfTimings.size();
        } else {
            return 0;
        }
    }

    public class AvailablityHolder extends RecyclerView.ViewHolder {
        TextView txtServiceName, txtSummery;

        public AvailablityHolder(@NonNull View itemView) {
            super(itemView);
            txtServiceName = itemView.findViewById(R.id.txt_servicename);
            txtSummery = itemView.findViewById(R.id.txt_summery);

        }
    }
}
