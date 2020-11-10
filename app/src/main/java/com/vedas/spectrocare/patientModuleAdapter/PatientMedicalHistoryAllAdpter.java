package com.vedas.spectrocare.patientModuleAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vedas.spectrocare.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PatientMedicalHistoryAllAdpter extends RecyclerView.Adapter<PatientMedicalHistoryAllAdpter.AllMedicHolder> {
    @NonNull
    @Override
    public PatientMedicalHistoryAllAdpter.AllMedicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View allMedical = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_recycle_view, parent, false);
        return new AllMedicHolder(allMedical);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientMedicalHistoryAllAdpter.AllMedicHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class AllMedicHolder extends RecyclerView.ViewHolder {
        public AllMedicHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
