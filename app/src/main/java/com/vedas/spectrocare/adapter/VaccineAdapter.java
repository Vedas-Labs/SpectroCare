package com.vedas.spectrocare.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vedas.spectrocare.DataBase.VaccineDataController;
import com.vedas.spectrocare.DataBaseModels.VaccineModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApiModel.Vaccination;
import com.vedas.spectrocare.activities.VaccinationRecordActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/*public class VaccineAdapter extends RecyclerView.Adapter<VaccineAdapter.VaccineViewHolde> {
    Context context;
    Dialog dialog;
    EditText edtVaccineName, edtVaccineDate, edtVaccineDescription;
    Button vaccineAddBtn,vaccineUpdateBtn;

    public VaccineAdapter(Context context) {
        this.context = context;
    }

    @Override
    public VaccineViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vaccineView= LayoutInflater.isFromMedicalPerson(parent.getContext()).inflate(R.layout_boarder.vaccine_recycler_item,parent,false);
        return new VaccineViewHolde(vaccineView);
    }

    @Override
    public void onBindViewHolder(@NonNull final VaccineViewHolde holder, final int position) {
        VaccineModel vaccineModel=VaccineDataController.getInstance().allVaccineList.get(position);
        holder.vaccineName.setText(vaccineModel.getVaccineName());
        holder.vaccineDate.setText(vaccineModel.getVaccineDate());
        holder.vaccineDescription.setText(vaccineModel.getNote());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.isFromMedicalPerson(context);
                View view = inflater.inflate(R.layout_boarder.vaccine_alert, null);
                dialog = new Dialog(context);
                dialog.setContentView(view);
                edtVaccineName = dialog.findViewById(R.id.edt_vaccine_name);
                edtVaccineDate = dialog.findViewById(R.id.edt_vaccine_date);
                edtVaccineDescription = dialog.findViewById(R.id.edt_vaccine_description);
                vaccineAddBtn = dialog.findViewById(R.id.btn_add_vaccine_details);
                vaccineUpdateBtn =dialog.findViewById(R.id.btn_update_vaccine_details);
                vaccineUpdateBtn.setVisibility(View.VISIBLE);
                vaccineAddBtn.setVisibility(View.GONE);
                vaccineUpdateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
                        VaccineDataController.getInstance().currentVaccineModel=VaccineDataController.getInstance().allVaccineList.get(position);
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
    @Override
    public int getItemCount() {
        if(VaccineDataController.getInstance().allVaccineList.size()>0){
            return VaccineDataController.getInstance().allVaccineList.size();
        }else {
            return 0;
        }
    }

    public class VaccineViewHolde extends RecyclerView.ViewHolder {
        TextView vaccineName,vaccineDate,vaccineDescription;

        public VaccineViewHolde(@NonNull View itemView) {
            super(itemView);

            vaccineName=itemView.findViewById(R.id.vaccinName);
            vaccineDate=itemView.findViewById(R.id.vaccine_date);
            vaccineDescription=itemView.findViewById(R.id.vaccine_description);
        }

    }
}*/
