/*
package com.vedas.spectrocare.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApiModel.PhysicalRecordServerObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PhysicalRecordAdapter extends RecyclerView.Adapter<PhysicalRecordAdapter.PhysicalRecordHolder> {

    Context context;
    ArrayList<PhysicalRecordServerObject> arryList;

    public PhysicalRecordAdapter(Context context, ArrayList<PhysicalRecordServerObject> arryList) {
        this.context = context;
        this.arryList = arryList;
    }

    @Override
    public PhysicalRecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.isFromMedicalPerson(parent.getContext()).inflate(R.layout_boarder.physical_record_item, parent, false);
        return new PhysicalRecordHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PhysicalRecordHolder holder, final int position) {

        holder.txtTitle.setText(arryList.get(position).getCategory());
        holder.edtDescription.setText(arryList.get(position).getDescription());
        holder.btnNotExamined.setChecked(true);

        if (arryList.get(position).getResult().equals("Normal")){
               holder.btnNormal.setChecked(true);
               holder.btnAbnormal.setChecked(false);
               holder.btnNotExamined.setChecked(false);}
        if (arryList.get(position).getResult().equals("Abnormal")){
            holder.btnNormal.setChecked(false);
            holder.btnAbnormal.setChecked(true);
            holder.btnNotExamined.setChecked(false);}
        if (arryList.get(position).getResult().equals("Not examined")){
            holder.btnNormal.setChecked(false);
            holder.btnAbnormal.setChecked(false);
            holder.btnNotExamined.setChecked(true);}

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhysicalRecordServerObject selectedObject=arryList.get(position);
                Log.e("selecedObj","dsdsds"+selectedObject.get());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                builder.setCancelable(false);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(holder.getAdapterPosition());
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                builder.create().show();
                return false;
            }
        });
    }
    public void delete(int position) {
        arryList.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public int getItemCount() {
        return arryList.size();
    }

    public class PhysicalRecordHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        RadioGroup radioGroup;
        RadioButton btnNormal,btnAbnormal,btnNotExamined;
        EditText edtDescription;
        public PhysicalRecordHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.item_title);
            radioGroup = itemView.findViewById(R.id.radio_group);
            btnNormal = itemView.findViewById(R.id.normal);
            btnAbnormal = itemView.findViewById(R.id.abnormal);
            btnNotExamined = itemView.findViewById(R.id.not_examined);
            edtDescription = itemView.findViewById(R.id.edt_description);
            btnNotExamined.isChecked();
        }
    }
}
*/
