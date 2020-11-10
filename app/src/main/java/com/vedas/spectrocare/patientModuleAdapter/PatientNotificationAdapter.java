package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vedas.spectrocare.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PatientNotificationAdapter extends RecyclerView.Adapter<PatientNotificationAdapter.NotificationHolder> {
   Context context;

    public PatientNotificationAdapter(Context context) {
        this.context = context;
    }

    @Override
    public PatientNotificationAdapter.NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View swipeView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_recyclerview, parent, false);
        return new NotificationHolder(swipeView);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientNotificationAdapter.NotificationHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {
        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
