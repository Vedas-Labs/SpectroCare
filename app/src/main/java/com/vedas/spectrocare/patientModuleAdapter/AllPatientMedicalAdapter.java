package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vedas.spectrocare.PatientModule.PatientDoctorRecordActivity;
import com.vedas.spectrocare.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AllPatientMedicalAdapter extends RecyclerView.Adapter<AllPatientMedicalAdapter.AllMedicalHolder> {
    Context context;

    public AllPatientMedicalAdapter(Context context) {
        this.context = context;
    }

    @Override
    public AllPatientMedicalAdapter.AllMedicalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View allMedical = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_recycle_view, parent, false);
        return new AllMedicalHolder(allMedical);    }

    @Override
    public void onBindViewHolder(@NonNull final AllPatientMedicalAdapter.AllMedicalHolder holder, int position) {
    holder.btnView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        context.startActivity(new Intent(context, PatientDoctorRecordActivity.class));
    }
});
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class AllMedicalHolder extends RecyclerView.ViewHolder {
        Button btnView;
        public AllMedicalHolder(@NonNull View itemView) {
            super(itemView);
            btnView = itemView.findViewById(R.id.btn_view);
        }
    }
}
