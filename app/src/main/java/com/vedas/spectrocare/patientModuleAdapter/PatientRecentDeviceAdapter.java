package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vedas.spectrocare.PatientMyDeviceModule.MyDevicesActivity;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.PatientServerApiModel.PatientMyDevicesObject;
import com.vedas.spectrocare.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PatientRecentDeviceAdapter extends RecyclerView.Adapter<PatientRecentDeviceAdapter.RecentDeviceHolder> {
    Context context;

    public PatientRecentDeviceAdapter(Context context) {
        this.context = context;
    }

    @Override
    public PatientRecentDeviceAdapter.RecentDeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recentDevicesView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_devices_recyclerview, parent, false);
        return new RecentDeviceHolder(recentDevicesView);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientRecentDeviceAdapter.RecentDeviceHolder holder, int position) {
        PatientMyDevicesObject obj = PatientMedicalRecordsController.getInstance().myDevicesArrayList.get(position);
        holder.txt_id.setText(obj.getDeviceID());
        holder.txt_name.setText(obj.getDeviceName());
        holder.imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientMedicalRecordsController.getInstance().selectedDevice=PatientMedicalRecordsController.getInstance().myDevicesArrayList.get(holder.getAdapterPosition());
                context.startActivity(new Intent(context, MyDevicesActivity.class));
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientMedicalRecordsController.getInstance().selectedDevice=PatientMedicalRecordsController.getInstance().myDevicesArrayList.get(holder.getAdapterPosition());
                context.startActivity(new Intent(context, MyDevicesActivity.class));
            }
        });
    }
    @Override
    public int getItemCount() {
        if (PatientMedicalRecordsController.getInstance().myDevicesArrayList.size() > 0) {
            return PatientMedicalRecordsController.getInstance().myDevicesArrayList.size();
        } else {
            return 0;
        }
    }

    public class RecentDeviceHolder extends RecyclerView.ViewHolder {
        ImageView imgInfo;
        RelativeLayout layout;
        TextView txt_id, txt_name;
        ProgressBar progressBar;

        public RecentDeviceHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout_main);
            txt_id = itemView.findViewById(R.id.txt_id);
            txt_name = itemView.findViewById(R.id.txt_name);
            progressBar = itemView.findViewById(R.id.progressBar);
            imgInfo = itemView.findViewById(R.id.img_info);

        }

    }
}
