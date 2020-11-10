package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vedas.spectrocare.PatientMyDeviceModule.MyDevicesActivity;
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
        holder.imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MyDevicesActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class RecentDeviceHolder extends RecyclerView.ViewHolder {
        ImageView imgInfo;
        public RecentDeviceHolder(@NonNull View itemView) {
            super(itemView);
            imgInfo = itemView.findViewById(R.id.img_info);
        }
    }
}
