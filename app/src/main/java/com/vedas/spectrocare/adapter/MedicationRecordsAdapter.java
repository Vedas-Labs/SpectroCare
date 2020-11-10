package com.vedas.spectrocare.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vedas.spectrocare.Controllers.MedicationServerObjectDataController;
import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.Controllers.PhysicalServerObjectDataController;
import com.vedas.spectrocare.DataBase.MedicationAttachmentRecordDataController;
import com.vedas.spectrocare.DataBase.MedicationRecordDataController;
import com.vedas.spectrocare.DataBase.MedicinesRecordDataController;
import com.vedas.spectrocare.DataBase.VaccineDataController;
import com.vedas.spectrocare.DataBaseModels.MedicationAttachmentModel;
import com.vedas.spectrocare.DataBaseModels.MedicationRecordModel;
import com.vedas.spectrocare.DataBaseModels.MedicinesRecordModel;
import com.vedas.spectrocare.DataBaseModels.PhysicalExamsDataModel;
import com.vedas.spectrocare.DataBaseModels.VaccineModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.activities.MedicationRecordActivity;
import com.vedas.spectrocare.activities.MedicinesRecordActivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;

public class MedicationRecordsAdapter extends RecyclerView.Adapter<MedicationRecordsAdapter.VaccineViewHolde> {
    Context context;
    Dialog dialog;

    public MedicationRecordsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public VaccineViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vaccineView= LayoutInflater.from(parent.getContext()).inflate(R.layout.medication_recycler_item,parent,false);
        return new VaccineViewHolde(vaccineView);
    }

    @Override
    public void onBindViewHolder(@NonNull final VaccineViewHolde holder, final int position) {
        MedicationRecordModel model=MedicationRecordDataController.getInstance().allMedicationList.get(position);
        holder.mid.setText(model.getIllnessMedicationID());
        holder.name.setText(model.getDoctorName());
        try {
            String array[]= PersonalInfoController.getInstance().convertTimestampToslashFormate(model.getDate());
            holder.date.setText(array[0]+" "+array[1]+" "+array[2]);
        } catch (ParseException e) {
            e.printStackTrace();
        }

      final  ArrayList<MedicationAttachmentModel> attachmentModelArrayList= MedicationAttachmentRecordDataController.getInstance().fetchAttachmentData(model);
        ArrayList<MedicinesRecordModel> viewMedicalModelList=MedicinesRecordDataController.getInstance().fetchMedicinesData( MedicationRecordDataController.getInstance().currentMedicationRecordModel);

        if(viewMedicalModelList.size()>0){
            holder.attachlayout.setVisibility(View.GONE);
            holder.viewBtnLayout.setVisibility(View.VISIBLE);
        }else {
           /* holder.viewBtnLayout.setVisibility(View.GONE);
            holder.attachlayout.setVisibility(View.VISIBLE);*/
        }
        if(attachmentModelArrayList.size()>0){
            holder.viewBtnLayout.setVisibility(View.GONE);
            holder.attachlayout.setVisibility(View.VISIBLE);
            for(int i=0;i<attachmentModelArrayList.size();i++){
                String array[]=attachmentModelArrayList.get(0).getFilePath().split("/");
                String array1[]=attachmentModelArrayList.get(1).getFilePath2().split("/");
                holder.file1.setText(array[array.length-1]);
                holder.file2.setText(array1[array1.length-1]);
            }
            holder.file1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadAttachUrlImage(attachmentModelArrayList.get(0));
                }
            });
            holder.file2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadAttachUrlImage(attachmentModelArrayList.get(1));
                }
            });
        }/*else{
            holder.attachlayout.setVisibility(View.GONE);
            holder.viewBtnLayout.setVisibility(View.VISIBLE);
        }*/

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              MedicationRecordDataController.getInstance().currentMedicationRecordModel=MedicationRecordDataController.getInstance().allMedicationList.get(position);
                Intent medicalScreeningRecordIntent = new Intent(context, MedicinesRecordActivity.class);
                context.startActivity(medicalScreeningRecordIntent);
            }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  MedicationRecordDataController.getInstance().currentMedicationRecordModel=MedicationRecordDataController.getInstance().allMedicationList.get(position);
               // MedicationAttachmentRecordDataController.getInstance().fetchAttachmentData( MedicationRecordDataController.getInstance().currentMedicationRecordModel);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                builder.setCancelable(false);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete manual record");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*if (isConn()) {
                            MedicationRecordDataController.getInstance().currentMedicationRecordModel=MedicationRecordDataController.getInstance().allMedicationList.get(holder.getAdapterPosition());
                            MedicationRecordActivity.alertDialog.show();
                            Objects.requireNonNull(MedicationRecordActivity.alertDialog.getWindow()).setLayout(600, 500);
                            MedicationServerObjectDataController.getInstance().medicationDeleteApiCall(MedicationRecordDataController.getInstance().currentMedicationRecordModel);
                        }*/
                       MedicationRecordDataController.getInstance().currentMedicationRecordModel=MedicationRecordDataController.getInstance().allMedicationList.get(position);
                       EventBus.getDefault().post(new ViewMedicalAdapter.MessageEvent("deletem",holder.getAdapterPosition()));
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                builder.create().show();
            }
        });
        holder.attach_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                builder.setCancelable(false);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete this record");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            if (isConn()) {
                                MedicationRecordDataController.getInstance().currentMedicationRecordModel=MedicationRecordDataController.getInstance().allMedicationList.get(holder.getAdapterPosition());
                                MedicationRecordActivity.alertDialog.show();
                                Objects.requireNonNull(MedicationRecordActivity.alertDialog.getWindow()).setLayout(600, 500);
                                MedicationServerObjectDataController.getInstance().medicationDeleteApiCall(MedicationRecordDataController.getInstance().currentMedicationRecordModel);
                            }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                builder.create().show();
            }
        });
    }
    @Override
    public int getItemCount() {
        if(MedicationRecordDataController.getInstance().allMedicationList.size()>0){
            return MedicationRecordDataController.getInstance().allMedicationList.size();
        }else {
            return 0;
        }
    }
    public class VaccineViewHolde extends RecyclerView.ViewHolder {
        TextView mid,name,date,file1,file2;
        ImageView edit,delete,view,attach,attach_delete;
        LinearLayout viewBtnLayout;
        RelativeLayout attachlayout;
        public VaccineViewHolde(@NonNull View itemView) {
            super(itemView);
            mid=itemView.findViewById(R.id.mid);
            name=itemView.findViewById(R.id.name);
            date=itemView.findViewById(R.id.date);
            edit=itemView.findViewById(R.id.img_edit);
            delete=itemView.findViewById(R.id.img_delete);
            view=itemView.findViewById(R.id.img_view);
            attach=itemView.findViewById(R.id.img_attach);
            viewBtnLayout=itemView.findViewById(R.id.l1);
            attachlayout=itemView.findViewById(R.id.attachlayout);
            attach_delete=itemView.findViewById(R.id.attach_delete);
            file1=itemView.findViewById(R.id.file1);
            file2=itemView.findViewById(R.id.file2);
        }

    }
    private void  loadAttachUrlImage(MedicationAttachmentModel dataModel){
        String url= ServerApi.img_home_url+dataModel.getFilePath();
        Log.e("notdata","call"+url);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        // intent.setDataAndType(Uri.parse(url), "*/*");
        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
    public boolean isConn() {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
    }
}
