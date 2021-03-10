package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.PatientModule.PatientSurgeryActivity;

import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PatientSurgeryAdapter extends RecyclerView.Adapter<PatientSurgeryAdapter.SurgeryHolder> {

    Context context;
    TextView txtDelete,txtCount;
    String clockTime;
    boolean isHourFormat;
    public PatientSurgeryAdapter(Context context) {
        this.context = context;
    }

/*
    public PatientSurgeryAdapter(Context context, TextView txtDelete, TextView txtCount) {
        this.context = context;
        this.txtDelete = txtDelete;
        this.txtCount = txtCount;
    }
*/

    public PatientSurgeryAdapter(Context context, TextView txtDelete, TextView txtCount, boolean isHourFormat) {
        this.context = context;
        this.txtDelete = txtDelete;
        this.txtCount = txtCount;
        this.isHourFormat = isHourFormat;
    }

    @NonNull
    @Override
    public SurgeryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View surgeryView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_recycle_view, parent, false);
        return new SurgeryHolder(surgeryView);
    }

    @Override
    public void onBindViewHolder(@NonNull SurgeryHolder holder, int position) {
        holder.imgImmunizationICon.setImageResource(R.drawable.ic_surgery);
        holder.discription.setText(PatientMedicalRecordsController.getInstance().surgeryObjectArrayList.get(position).getSurgeryProcedure());
        holder.immunizationName.setText("DR. "+PatientMedicalRecordsController.getInstance().surgeryObjectArrayList.get(position).getDoctorName());
        long millis = Long.parseLong(PatientMedicalRecordsController.getInstance().surgeryObjectArrayList.get(position).getAddedDate());
        Date d = new Date(millis);
        SimpleDateFormat weekFormatter = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
        String weekString = weekFormatter.format(d);
        String time[]=weekString.split(" ");
        Log.e("weeekarray", "" + time[0]+time[1]+" "+time[2]);
        //load settings date formate to date feild.
        String value = PersonalInfoController.getInstance().loadSettingsDataFormateToEntireApp(context,String.valueOf(millis));
        holder.date.setText(value);
        //
        //holder.date.setText(time[0]);
        String[] timeSplit = time[1].split(":");

        if (isHourFormat){
            clockTime = time[1];
        }else{
            if (12 < Integer.parseInt(timeSplit[0])){
                int hr = Integer.parseInt(timeSplit[0])-12;
                clockTime = String.valueOf(hr)+":"+timeSplit[1]+time[2];
            }else{
                clockTime = time[1]+" "+time[2];
            }
        }

        holder.time.setText(clockTime);
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientMedicalRecordsController.getInstance().selectedPos=holder.getAdapterPosition();
                PatientMedicalRecordsController.getInstance().selectedSurgeryObject=PatientMedicalRecordsController.getInstance().surgeryObjectArrayList.get(position);
                context.startActivity(new Intent(context, PatientSurgeryActivity.class)
                        .putExtra("isHourFormat",isHourFormat));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(PatientMedicalRecordsController.getInstance().surgeryObjectArrayList.size()>0){
            txtDelete.setVisibility(View.VISIBLE);
            txtCount.setText("("+PatientMedicalRecordsController.getInstance().surgeryObjectArrayList.size()+")");
            return PatientMedicalRecordsController.getInstance().surgeryObjectArrayList.size();
        }else {
            txtDelete.setVisibility(View.GONE);
            txtCount.setText("("+0+")");
            return 0;
        }
    }

    public class SurgeryHolder extends RecyclerView.ViewHolder {
        Button btnView;
        TextView immunizationName,discription,date,time;
        ImageView imgImmunizationICon;
        public SurgeryHolder(@NonNull View itemView) {
            super(itemView);
            btnView = itemView.findViewById(R.id.btn_view);
            imgImmunizationICon = itemView.findViewById(R.id.img_doc_pic);
            immunizationName = itemView.findViewById(R.id.txt_doc_name);
            discription = itemView.findViewById(R.id.txt_infection_name);//txt_doc_date
            date = itemView.findViewById(R.id.txt_doc_date);//txt_doc_time
            time = itemView.findViewById(R.id.txt_doc_time);//txt_doc_time
        }
    }
}
