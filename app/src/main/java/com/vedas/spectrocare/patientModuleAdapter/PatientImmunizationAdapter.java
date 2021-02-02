package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.PatientModule.AddPatientImmunizationActivity;
import com.vedas.spectrocare.PatientModule.PatientImmunizationActivity;

import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PatientImmunizationAdapter extends RecyclerView.Adapter<PatientImmunizationAdapter.immunizationHolder> {
   Context context;
   TextView txtDelete,txtCount;

    public PatientImmunizationAdapter(Context context) {
        this.context = context;
    }

    public PatientImmunizationAdapter(Context context, TextView txtDelete, TextView txtCount) {
        this.context = context;
        this.txtDelete = txtDelete;
        this.txtCount = txtCount;
    }

    @NonNull
    @Override
    public immunizationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View immunizationView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_recycle_view, parent, false);
        return new immunizationHolder(immunizationView);
    }

    @Override
    public void onBindViewHolder(@NonNull immunizationHolder holder, int position) {
        holder.imgImmunizationICon.setImageResource(R.drawable.immunaization);
       // holder.immunizationName.setText("Immunizations");

        holder.discription.setText(PatientMedicalRecordsController.getInstance().immunizationArrayList.get(position).getNotes());
        holder.immunizationName.setText(PatientMedicalRecordsController.getInstance().immunizationArrayList.get(position).getImmunizationName());
        long millis = Long.parseLong(PatientMedicalRecordsController.getInstance().immunizationArrayList.get(position).getCreatedDate());
        Date d = new Date(millis);
        SimpleDateFormat weekFormatter = new SimpleDateFormat("yyyy/MM/dd hh:mm a", Locale.ENGLISH);
        String weekString = weekFormatter.format(d);
        String time[]=weekString.split(" ");
        Log.e("weeekarray", "" + time[0]+time[1]+time[2]);
        //load settings date formate to date feild.
        String value = PersonalInfoController.getInstance().loadSettingsDataFormateToEntireApp(context,String.valueOf(millis));
        holder.date.setText(value);
        //
       // holder.date.setText(time[0]);
        holder.time.setText(time[1]+" "+time[2]);
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientMedicalRecordsController.getInstance().selectedPos=holder.getAdapterPosition();
                PatientMedicalRecordsController.getInstance().selectedImmunizationObject=PatientMedicalRecordsController.getInstance().immunizationArrayList.get(position);
                context.startActivity(new Intent(context, PatientImmunizationActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(PatientMedicalRecordsController.getInstance().immunizationArrayList.size()>0){
            txtDelete.setVisibility(View.VISIBLE);
            txtCount.setText("("+PatientMedicalRecordsController.getInstance().immunizationArrayList.size()+")");
            return  PatientMedicalRecordsController.getInstance().immunizationArrayList.size();
        }else {
            txtDelete.setVisibility(View.GONE);
            txtCount.setText("("+0+")");
            return 0;
        }
    }

    public class immunizationHolder extends RecyclerView.ViewHolder {
        Button btnView;
        TextView immunizationName,discription,date,time;
        ImageView imgImmunizationICon;
        public immunizationHolder(@NonNull View itemView) {
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
