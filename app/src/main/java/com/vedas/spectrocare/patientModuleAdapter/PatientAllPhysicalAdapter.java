package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vedas.spectrocare.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PatientAllPhysicalAdapter extends RecyclerView.Adapter<PatientAllPhysicalAdapter.AllPhysicalHolder> {
    Context context;

    public PatientAllPhysicalAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PatientAllPhysicalAdapter.AllPhysicalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View allPhysicalView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_recycle_view, parent, false);
        return new AllPhysicalHolder(allPhysicalView);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientAllPhysicalAdapter.AllPhysicalHolder holder, int position) {
        holder.hospitalName.setText("Hospital");
        holder.txtInfection.setVisibility(View.GONE);
        holder.imgIcon.setImageResource(R.drawable.user_1);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class AllPhysicalHolder extends RecyclerView.ViewHolder {
        Button btnView;
        TextView hospitalName;
        TextView txtInfection;
        ImageView imgIcon;

        public AllPhysicalHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.img_doc_pic);
            btnView = itemView.findViewById(R.id.btn_view);
            hospitalName = itemView.findViewById(R.id.txt_doc_name);
            txtInfection = itemView.findViewById(R.id.txt_infection_name);

        }
    }
}
