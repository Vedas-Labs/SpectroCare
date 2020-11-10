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
import com.vedas.spectrocare.PatientAppointmentModule.AppointmentArrayModel;
import com.vedas.spectrocare.PatinetControllers.PatientAppointmentController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.AppointmentSummaryActivity;
import com.vedas.spectrocare.model.AppointmetModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarHolder> {
    Context context;
    ArrayList<AppointmentArrayModel> calendarItemListarrayList;
    TextView textView;

    public CalendarAdapter(Context context, ArrayList<AppointmentArrayModel> calendarItemListarrayList, TextView textView) {
        this.context = context;
        this.calendarItemListarrayList = calendarItemListarrayList;
        this.textView = textView;
    }
 public CalendarAdapter(Context context,  ArrayList<AppointmentArrayModel> calendarItemListarrayList) {
        this.context = context;
        this.calendarItemListarrayList = calendarItemListarrayList;
    }

    @NonNull
    @Override
    public CalendarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View calendarView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_upcoming_item,parent,
                false);
        return new CalendarHolder(calendarView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CalendarHolder holder, int position) {
        AppointmentArrayModel appointmentModel = calendarItemListarrayList.get(position);

       /* if(!appointmentModel.getDoctorProfilePic().contains("null")) {
            Log.e("sdlfsdlfskdlf","call"+appointmentModel.getDoctorProfilePic());
            Picasso.get().load(appointmentModel.getDoctorProfilePic()).into(holder.docProfile);
        }else {
            holder.docProfile.setImageResource(R.drawable.ic_human_dummy3x);
        }*/
        //
        long ll = Long.parseLong(appointmentModel.getAppointmentDetails().getAppointmentDate());
        Date currentDate = new Date(ll);
        SimpleDateFormat jdff = new SimpleDateFormat("dd/MM/yyyy");
        jdff.setTimeZone(TimeZone.getDefault());
        String java_date = jdff.format(currentDate);
        holder.appointmentDate.setText(java_date);
        holder.appointmentTime.setText(appointmentModel.getAppointmentDetails().getAppointmentTime());
        holder.docName.setText(appointmentModel.getDoctorDetails().getProfile().getUserProfile().getFirstName()+" "+
                appointmentModel.getDoctorDetails().getProfile().getUserProfile().getLastName() );
        holder.txtSpecial.setText(appointmentModel.getDoctorDetails().getProfile().getUserProfile().getDepartment());
        holder.appointmentApproval.setText(appointmentModel.getAppointmentDetails().getAppointmentStatus());
        if(appointmentModel.getAppointmentDetails().getAppointmentStatus().contains("Conformed")){
            holder.appointmentApproval.setText("Conformed");
            holder.appointmentApproval.setTextColor(Color.parseColor("#615D5E"));
        }else {
            if(appointmentModel.getAppointmentDetails().getAppointmentStatus().contains("Completed")){
                holder.appointmentApproval.setTextColor(Color.GREEN);
                holder.appointmentApproval.setText(appointmentModel.getAppointmentDetails().getAppointmentStatus());
            }else if(appointmentModel.getAppointmentDetails().getAppointmentStatus().contains("Pending")){
                holder.appointmentApproval.setTextColor(Color.parseColor("#FFA500"));
                holder.appointmentApproval.setText(appointmentModel.getAppointmentDetails().getAppointmentStatus());
            }else if(appointmentModel.getAppointmentDetails().getAppointmentStatus().contains("Cancelled") || appointmentModel.getAppointmentDetails().getAppointmentStatus().contains("Rejected") ){
                holder.appointmentApproval.setTextColor(Color.RED);
                holder.appointmentApproval.setText(appointmentModel.getAppointmentDetails().getAppointmentStatus());
            }
        }
        holder.txt_full_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppointmentDataController.getInstance().currentAppointmentModel=AppointmentDataController.getInstance().allAppointmentList.get(holder.getAdapterPosition());
                Intent patientProfileIntent = new Intent(context, AppointmentSummaryActivity.class);
                context.startActivity(patientProfileIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (calendarItemListarrayList.size() > 0) {
            textView.setVisibility(View.GONE);
            return calendarItemListarrayList.size();
        } else {
            textView.setVisibility(View.VISIBLE);
            return 0;
        }
    }
    public class CalendarHolder extends RecyclerView.ViewHolder {
        CircularImageView docProfile;
        TextView docName,appointmentDate,appointmentTime,appointmentApproval;
        TextView txt_full_view,txtSpecial;

        public CalendarHolder(@NonNull View itemView) {
            super(itemView);
            docProfile = itemView.findViewById(R.id.doc_profile);
            docName = itemView.findViewById(R.id.doc_name);
            txtSpecial = itemView.findViewById(R.id.doc_spe);
            appointmentDate = itemView.findViewById(R.id.appointment_date);
            appointmentTime = itemView.findViewById(R.id.appointment_time);
            appointmentApproval =itemView.findViewById(R.id.appointment_approval);
            txt_full_view = itemView.findViewById(R.id.txt_full_view);

        }
    }
}
