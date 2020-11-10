package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vedas.spectrocare.PatientModule.PatientHospitalActivity;
import com.vedas.spectrocare.PatientModule.PatientImmunizationActivity;
import com.vedas.spectrocare.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PatientHospitalAdapter extends RecyclerView.Adapter<PatientHospitalAdapter.HospitalHolder> {
    Context context;

    public PatientHospitalAdapter(Context context) {
        this.context = context;
    }

    @Override
    public PatientHospitalAdapter.HospitalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View hospitalView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_recycle_view, parent, false);
        return new HospitalHolder(hospitalView);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientHospitalAdapter.HospitalHolder holder, int position) {
        holder.HospitalICon.setImageResource(R.drawable.hospital);
        holder.infectionName.setVisibility(View.INVISIBLE);
        holder.HospitalName.setText("Hospital");
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, PatientHospitalActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class HospitalHolder extends RecyclerView.ViewHolder {
        Button btnView;
        TextView HospitalName,infectionName;
        ImageView HospitalICon;
        public HospitalHolder(@NonNull View itemView) {
            super(itemView);
            btnView = itemView.findViewById(R.id.btn_view);
            HospitalICon = itemView.findViewById(R.id.img_doc_pic);
            infectionName = itemView.findViewById(R.id.txt_infection_name);
            HospitalName = itemView.findViewById(R.id.txt_doc_name);

        }
    }
}
