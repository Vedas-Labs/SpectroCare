package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PatientCalenderAdapter extends RecyclerView.Adapter<PatientCalenderAdapter.PatientCalendarHolder> {
    Context context;

    public PatientCalenderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public PatientCalenderAdapter.PatientCalendarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PatientCalenderAdapter.PatientCalendarHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class PatientCalendarHolder extends RecyclerView.ViewHolder {
        public PatientCalendarHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
