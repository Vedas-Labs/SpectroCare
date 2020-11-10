package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vedas.spectrocare.PatientModule.PatientChatActivity;
import com.vedas.spectrocare.PatientModule.PatientMessageActivity;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.adapter.DoctorsRecycleAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DoctorMessageAdapter extends RecyclerView.Adapter<DoctorMessageAdapter.MessageHolder> {
Context context;

    public DoctorMessageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public DoctorMessageAdapter.MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View doctorItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor_message, parent, false);
        return new MessageHolder(doctorItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorMessageAdapter.MessageHolder holder, int position) {
        holder.btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, PatientChatActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class MessageHolder extends RecyclerView.ViewHolder {
        Button btnMessage;
        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            btnMessage = itemView.findViewById(R.id.btn_view);
        }
    }
}
