package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vedas.spectrocare.PatientModule.PatientImmunizationActivity;
import com.vedas.spectrocare.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PatientSelfExamAdapter extends RecyclerView.Adapter<PatientSelfExamAdapter.SelfExamHolder> {
    Context context;

    public PatientSelfExamAdapter(Context context) {
        this.context = context;
    }

    @Override
    public PatientSelfExamAdapter.SelfExamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View selfExamView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_recycle_view, parent, false);
        return new SelfExamHolder(selfExamView);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientSelfExamAdapter.SelfExamHolder holder, int position) {
        holder.selfExamICon.setImageResource(R.drawable.selftest);
        holder.infectionName.setVisibility(View.INVISIBLE);
        holder.selfExamName.setText("Self-Exam");
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  context.startActivity(new Intent(context, PatientImmunizationActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class SelfExamHolder extends RecyclerView.ViewHolder {
        Button btnView;
        TextView selfExamName,infectionName;
        ImageView selfExamICon;
        public SelfExamHolder(@NonNull View itemView) {
            super(itemView);
            btnView = itemView.findViewById(R.id.btn_view);
            selfExamICon = itemView.findViewById(R.id.img_doc_pic);
            infectionName = itemView.findViewById(R.id.txt_infection_name);
            selfExamName = itemView.findViewById(R.id.txt_doc_name);

        }
    }
}
