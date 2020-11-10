package com.vedas.spectrocare.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vedas.spectrocare.Controllers.SurgicalRecordServerObjectDataController;
import com.vedas.spectrocare.DataBase.SurgricalRecordDataControll;
import com.vedas.spectrocare.DataBaseModels.ScreeningRecordModel;
import com.vedas.spectrocare.DataBaseModels.SurgicalRecordModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.activities.SurgicalRecordActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SurgeryRecordAdapter extends RecyclerView.Adapter<SurgeryRecordAdapter.SurgeryRecordHolder> {
    Context context;
    List<SurgicalRecordModel> surgicalRecordList;

    public SurgeryRecordAdapter(Context context, ArrayList<SurgicalRecordModel> surgicalRecordList) {
        this.context = context;
        this.surgicalRecordList = surgicalRecordList;
    }

    @NonNull
    @Override
    public SurgeryRecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_surgery_record,parent,false);
        return new SurgeryRecordHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SurgeryRecordHolder holder, final int position) {
        SurgicalRecordModel surgicalRecordModel = SurgricalRecordDataControll.getInstance().allSurgicalList.get(position);
        Log.e("zzzzzzzzz", "call" +SurgricalRecordDataControll.getInstance().allSurgicalList.size());
        holder.txtIllnessSurgicalId.setText(surgicalRecordModel.getIllnessSurgicalId());
        holder.txtDocName.setText(surgicalRecordModel.getDoctorName());
        holder.txtSurgeryDisc.setText(surgicalRecordModel.getMoreInfo());
        String disc = surgicalRecordModel.getAttachment();

        String surgicalFileName = disc.substring(41,disc.length()-1);
        Log.e("surgeryDisc",""+disc.length());
        holder.fileName.setText(surgicalFileName);

        holder.viewImgSurgery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SurgricalRecordDataControll.getInstance().currentSurgicalmodel=SurgricalRecordDataControll.getInstance().allSurgicalList.get(position);
                if(SurgricalRecordDataControll.getInstance().currentSurgicalmodel.getAttachment()!=null) {
                    loadAttachUrlImage(SurgricalRecordDataControll.getInstance().currentSurgicalmodel);
                }
            }
        });
        holder.attachImgSurgery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SurgricalRecordDataControll.getInstance().currentSurgicalmodel=SurgricalRecordDataControll.getInstance().allSurgicalList.get(position);
                if(SurgricalRecordDataControll.getInstance().currentSurgicalmodel.getAttachment()!=null) {
                    loadAttachUrlImage(SurgricalRecordDataControll.getInstance().currentSurgicalmodel);
                }
            }
        });
        holder.deleteImgSurgery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SurgricalRecordDataControll.getInstance().currentSurgicalmodel=SurgricalRecordDataControll.getInstance().allSurgicalList.get(position);
                deleteScreeningData();
            }
        });

    }

    @Override
    public int getItemCount() {
        if(SurgricalRecordDataControll.getInstance().allSurgicalList.size()>0) {
            Log.e("listcount", "callddd" + SurgricalRecordDataControll.getInstance().allSurgicalList.size());
            return SurgricalRecordDataControll.getInstance().allSurgicalList.size();
        }else {
            return 0;
        }
    }
    private void loadAttachUrlImage(SurgicalRecordModel dataModel){
        String url= ServerApi.img_home_url+dataModel.getAttachment();
        Log.e("notdata","call"+url);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
// intent.setDataAndType(Uri.parse(url), "*/*");
// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
    public class SurgeryRecordHolder extends RecyclerView.ViewHolder {
        TextView txtIllnessSurgicalId,txtDocName,txtSurgeryDisc,fileName;
        ImageView viewImgSurgery,attachImgSurgery,deleteImgSurgery;
        public SurgeryRecordHolder(@NonNull View itemView) {
            super(itemView);
            txtDocName = itemView.findViewById(R.id.txt_doctor_name);
            txtIllnessSurgicalId = itemView.findViewById(R.id.txt_surgical_record_illness);
            txtSurgeryDisc = itemView.findViewById(R.id.txt_surgery_disc);
            viewImgSurgery = itemView.findViewById(R.id.surgery_img_view);
            deleteImgSurgery = itemView.findViewById(R.id.surgery_img_delete);
            attachImgSurgery = itemView.findViewById(R.id.surgery_img_attach);
            fileName = itemView.findViewById(R.id.txt_f_name);
        }
    }
    private void deleteScreeningData(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        builder.setCancelable(false);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete this record");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(isConn()){
                    SurgicalRecordActivity.showingDialog.showAlert();
                    SurgicalRecordServerObjectDataController.getInstance().deleteSurgicalRecord( SurgricalRecordDataControll.getInstance().currentSurgicalmodel);
                }
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
    public boolean isConn() {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
    }
}