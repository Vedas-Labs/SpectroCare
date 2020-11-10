package com.vedas.spectrocare.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vedas.spectrocare.PatientModule.PatientAppointmentsTabsActivity;
import com.vedas.spectrocare.PatientModule.PatientDiagnosisRecordActivity;
import com.vedas.spectrocare.PatientModule.PatientFileActivity;
import com.vedas.spectrocare.PatientModule.PatientMedicalHistoryActivity;
import com.vedas.spectrocare.PatientModule.PatientMedicationRecordActivity;
import com.vedas.spectrocare.PatientModule.PatientMedicationsActivity;
import com.vedas.spectrocare.PatientModule.PatientMedicinesRecordActivity;
import com.vedas.spectrocare.PatientModule.PatientPhysicalActivity;
import com.vedas.spectrocare.PatientModule.PatientPhysicalExamActivity;
import com.vedas.spectrocare.PatientModule.PatientProfileActivity;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.CategoryActivity;
import com.vedas.spectrocare.activities.ChangePasswordActivity;
import com.vedas.spectrocare.activities.PasswordChangedActivity;
import com.vedas.spectrocare.model.CategoryItemModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PatientItemAdapter extends RecyclerView.Adapter<PatientItemAdapter.PatientItemHolder> {
    Context context;
    ArrayList<CategoryItemModel> categoryItemList;

    public PatientItemAdapter(Context context, ArrayList<CategoryItemModel> categoryItemList) {
        this.context = context;
        this.categoryItemList = categoryItemList;
    }

    @NonNull
    @Override
    public PatientItemAdapter.PatientItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View patientItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_items_layout, parent, false);
        return new PatientItemHolder(patientItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PatientItemAdapter.PatientItemHolder holder, final int position) {
        holder.patientItemIcon.setImageResource(categoryItemList.get(position).getCategoryIcon());
        holder.patientItemName.setText(categoryItemList.get(position).getCategoryTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (holder.getAdapterPosition()){
                    case 0:
                        context.startActivity(new Intent(context, PatientProfileActivity.class));
                        break;
                    case 1:
                        context.startActivity(new Intent(context, PatientMedicalHistoryActivity.class));
                        break;
                    case 2:
                        context.startActivity(new Intent(context, PatientPhysicalActivity.class));
                        break;
                    case 3:
                        context.startActivity(new Intent(context, PatientMedicationRecordActivity.class));
                        break;
                   /* case 4:
                        context.startActivity(new Intent(context, PatientDiagnosisRecordActivity.class));
                        break;*/
                    case 4:
                        context.startActivity(new Intent(context, PatientFileActivity.class));
                        break;
                  /*  case 6 :
                        context.startActivity(new Intent(context, ChangePasswordActivity.class));
                        break;*/

                }
              /*  if(position==7){
                  context.startActivity(new Intent(context, ChangePasswordActivity.class));
                }*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return  categoryItemList.size();
    }

    public class PatientItemHolder extends RecyclerView.ViewHolder {
        ImageView patientItemIcon;
        TextView patientItemName;

        public PatientItemHolder(@NonNull View itemView) {
            super(itemView);
            patientItemIcon = itemView.findViewById(R.id.img_item);
            patientItemName = itemView.findViewById(R.id.txt_item);

        }
    }
}
