package com.vedas.spectrocare.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vedas.spectrocare.DataBaseModels.MedicinesRecordModel;
import com.vedas.spectrocare.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

public class ViewMedicalAdapter extends RecyclerView.Adapter<ViewMedicalAdapter.ViewMedicalHolder> {
    Context context;
    List<MedicinesRecordModel> viewMedicalList;

    public ViewMedicalAdapter(Context context, List<MedicinesRecordModel> viewMedicalList) {
        this.context = context;
        this.viewMedicalList = viewMedicalList;
    }

    @Override
    public ViewMedicalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewMedical = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medical_view,parent,false);
        return new ViewMedicalHolder(viewMedical);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewMedicalHolder holder, final int position) {
        holder.txtMoreInfo.setText(viewMedicalList.get(position).getMoreDetails());
        holder.txtMedicineName.setText(viewMedicalList.get(position).getName());
        holder.txtPurpose.setText(viewMedicalList.get(position).getPurpose());
        holder.txtFrequency.setText(viewMedicalList.get(position).getFreq());
        holder.txtDuration.setText(viewMedicalList.get(position).getDurationDays());
        holder.txtDosage.setText(viewMedicalList.get(position).getDosage());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent("editModel",holder.getAdapterPosition()));
            }
        });
        holder.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent("showModel",holder.getAdapterPosition()));
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteScreeningData(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
         if(viewMedicalList.size()>0){
            return viewMedicalList.size();
        }else {
             return 0;
         }
    }

    public class ViewMedicalHolder extends RecyclerView.ViewHolder {
        TextView txtPurpose,txtMedicineName,txtFrequency,txtDosage,txtDuration,txtMoreInfo;
        ImageView edit,delete,show;
        public ViewMedicalHolder(@NonNull View itemView) {
            super(itemView);
            txtDosage = itemView.findViewById(R.id.txt_medicine_dosage);
            txtDuration = itemView.findViewById(R.id.duration_txt);
            txtFrequency = itemView.findViewById(R.id.txt_medicine_frequency);
            txtPurpose = itemView.findViewById(R.id.txt_purpose);
            txtMedicineName = itemView.findViewById(R.id.txt_medicine_name);
            txtMoreInfo = itemView.findViewById(R.id.txt_more_info);
            edit = itemView.findViewById(R.id.img_edit);
            show = itemView.findViewById(R.id.img_view);
            delete = itemView.findViewById(R.id.img_delete);

        }
    }
    private void deleteScreeningData(final int model){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        builder.setCancelable(false);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete this record");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EventBus.getDefault().post(new MessageEvent("deleteModel",model));
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.show();
    }
    public static class MessageEvent {
        public  String message;
        public  int position;
        public MessageEvent(String message,int listviewModel) {
            this.message = message;
            this.position =listviewModel;
        }
        public String getMessage() {
            return message;
        }

        public int getSelectPos() {
            return position;
        }
    }
}
