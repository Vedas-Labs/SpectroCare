package com.vedas.spectrocare.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.vedas.spectrocare.DataBase.AppointmentDataController;
import com.vedas.spectrocare.DataBaseModels.AppointmentModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.AppointmentSummaryActivity;
import com.vedas.spectrocare.model.AppointmetModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentHolder> {
    Context context;
    ArrayList<AppointmentModel> arrayList;

    public AppointmentAdapter(Context context,ArrayList<AppointmentModel> appointmentModelArrayList) {
        this.context = context;
        this.arrayList=appointmentModelArrayList;
    }

    @NonNull
    @Override
    public AppointmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View appointmentsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_recycle_item, parent,
                false);
        return new AppointmentHolder(appointmentsView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AppointmentHolder holder, int position) {
        AppointmentModel appointmentModel = arrayList.get(position);
        if(!appointmentModel.getDoctorProfilePic().contains("null")) {
            Picasso.get().load(appointmentModel.getDoctorProfilePic()).into(holder.docProfile);
        }else {
            holder.docProfile.setImageResource(R.drawable.ic_human_dummy3x);
        }
        String drName = "Dr. "+appointmentModel.getDoctorName();
        holder.appointmentDate.setText(appointmentModel.getAppointmentDate());
        holder.appointmentTime.setText(appointmentModel.getAppointmentTimeFrom() + " to " + appointmentModel.getAppointmentTimeTo());
       // holder.docName.setText(appointmentModel.getDoctorName());
        holder.docName.setText(drName);

        if(appointmentModel.getAppointmentStatus().contains("Waiting")){
            holder.appointmentApproval.setText("Waiting");
            holder.appointmentApproval.setTextColor(Color.parseColor("#615D5E"));
        }else {
            if(appointmentModel.getAppointmentStatus().contains("Accepted")){
                holder.appointmentApproval.setTextColor(Color.GREEN);
                holder.appointmentApproval.setText(appointmentModel.getAppointmentStatus());
            }else if(appointmentModel.getAppointmentStatus().contains("Pending")){
                holder.appointmentApproval.setTextColor(Color.parseColor("#FFA500"));
                holder.appointmentApproval.setText(appointmentModel.getAppointmentStatus());
            }else if(appointmentModel.getAppointmentStatus().contains("Cancelled") || appointmentModel.getAppointmentStatus().contains("Rejected") ){
                holder.appointmentApproval.setTextColor(Color.RED);
                holder.appointmentApproval.setText(appointmentModel.getAppointmentStatus());
            }
        }
        holder.txt_full_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppointmentDataController.getInstance().currentAppointmentModel=arrayList.get(holder.getAdapterPosition());
                Intent patientProfileIntent = new Intent(context, AppointmentSummaryActivity.class);
                context.startActivity(patientProfileIntent);
 }
        });
    }

    @Override
    public int getItemCount() {
        if (arrayList.size() > 0) {
            return arrayList.size();
        } else {
            return 0;
        }
    }

    public class AppointmentHolder extends RecyclerView.ViewHolder {
        CircularImageView docProfile;
        TextView docName, appointmentDate, appointmentTime, appointmentApproval;
        TextView txt_full_view;

        public AppointmentHolder(@NonNull View itemView) {
            super(itemView);
            docProfile = itemView.findViewById(R.id.doc_profile);
            docName = itemView.findViewById(R.id.doc_name);
            appointmentDate = itemView.findViewById(R.id.appointment_date);
            appointmentTime = itemView.findViewById(R.id.appointment_time);
            appointmentApproval = itemView.findViewById(R.id.appointment_approval);
            txt_full_view = itemView.findViewById(R.id.txt_full_view);
        }
    }
}
