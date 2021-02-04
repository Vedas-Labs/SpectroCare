package com.vedas.spectrocare.patientModuleAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.PatientModule.AddPatientDiagnosisActivity;
import com.vedas.spectrocare.PatientModule.PatientDiagnosisActivity;
import com.vedas.spectrocare.PatientModule.PatientSurgeryActivity;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DiagnosisAdapter extends RecyclerView.Adapter<DiagnosisAdapter.DiagnosisHolder> {
    Context context;
    TextView txtDelete,txtCount;
    String from="";

    public DiagnosisAdapter(Context context, TextView txtDelete, TextView txtCount) {
        this.context = context;
        this.txtDelete = txtDelete;
        this.txtCount = txtCount;
    }

    public DiagnosisAdapter(Context context, String from) {
        this.context = context;
        this.from = from;
    }

    @NonNull
    @Override
    public DiagnosisHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View diagnosisView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_recycle_view, parent, false);
        return new DiagnosisHolder(diagnosisView);
    }

    @Override
    public void onBindViewHolder(@NonNull DiagnosisHolder holder, int position) {
        holder.imgImmunizationICon.setImageResource(R.drawable.doctors);
        holder.discription.setText(PatientMedicalRecordsController.getInstance().diagnosisObjectArrayList.get(position).getDiagnosis());
        holder.immunizationName.setText("DR. "+PatientMedicalRecordsController.getInstance().diagnosisObjectArrayList.get(position).getDoctorName());

        String entredDate = PatientMedicalRecordsController.getInstance().diagnosisObjectArrayList.get(position).getAddedDate();
        Log.e("string","date"+entredDate);
        long l = Long.parseLong(entredDate);
        Date currentDate = new Date(l);
        SimpleDateFormat jdff = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        jdff.setTimeZone(TimeZone.getDefault());
        String java_date = jdff.format(currentDate);
        Date clickedDate = null;
        try {
            clickedDate = jdff.parse(java_date);

            String formattedDate = jdff.format(clickedDate);
            Log.e("forrr", "ff" + formattedDate);
            String[] two = formattedDate.split(" ");
            //load settings date formate to date feild.
            String value = PersonalInfoController.getInstance().loadSettingsDataFormateToEntireApp(context, entredDate);
            holder.date.setText(value);
            String clockTime;
            //holder.txtDate.setText(two[0]);
            String[] timeSplit = two[1].split(":");
            if (12 < Integer.parseInt(timeSplit[0])) {
                int hr = Integer.parseInt(timeSplit[0]) - 12;
                clockTime = String.valueOf(hr) + ":" + timeSplit[1] + "PM";
            } else {
                clockTime = two[1] + "AM";
            }
            holder.time.setText(clockTime);
        }catch (ParseException e) {
            e.printStackTrace();
        }

        /*long millis = Long.parseLong(PatientMedicalRecordsController.getInstance().diagnosisObjectArrayList.get(position).getAddedDate());
        Date d = new Date(millis);
        SimpleDateFormat weekFormatter = new SimpleDateFormat("yyyy/MM/dd hh:mm a", Locale.ENGLISH);
        String weekString = weekFormatter.format(d);
        String time[]=weekString.split(" ");
        Log.e("weeekarray", "" + time[0]+time[1]+time[2]);
        holder.date.setText(time[0]);
        holder.time.setText(time[1]+" "+time[2]);*/

        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).finish();
                PatientMedicalRecordsController.getInstance().selectedPos=holder.getAdapterPosition();
                PatientMedicalRecordsController.getInstance().selectedDiagnosisObject=PatientMedicalRecordsController.getInstance().diagnosisObjectArrayList.get(position);
                context.startActivity(new Intent(context, AddPatientDiagnosisActivity.class));
            }
        });
        if (PatientMedicalRecordsController.getInstance().diagnosisObjectArrayList.size() > 0) {
            txtDelete.setVisibility(View.VISIBLE);
            txtCount.setText("("+PatientMedicalRecordsController.getInstance().diagnosisObjectArrayList.size()+")");
        } else {
            txtDelete.setVisibility(View.GONE);
            txtCount.setText("("+0+")");

        }
    }

    @Override
    public int getItemCount() {
        Log.e("dianosisadapter","call"+PatientMedicalRecordsController.getInstance().diagnosisObjectArrayList.size() );
        if (PatientMedicalRecordsController.getInstance().diagnosisObjectArrayList.size() > 0) {
            return PatientMedicalRecordsController.getInstance().diagnosisObjectArrayList.size();
            /*if(!from.equals("from")) {
                txtDelete.setVisibility(View.VISIBLE);
                txtCount.setText("(" + PatientMedicalRecordsController.getInstance().diagnosisObjectArrayList.size() + ")");
            }  return  PatientMedicalRecordsController.getInstance().diagnosisObjectArrayList.size();*/
        } else {
            return 0;
           /* if(!from.equals("from")) {
                txtDelete.setVisibility(View.GONE);
                txtCount.setText("(" + 0 + ")");
            }return 0;*/
        }
    }

    public class DiagnosisHolder extends RecyclerView.ViewHolder {
        Button btnView;
        TextView immunizationName,discription,date,time;
        ImageView imgImmunizationICon;
        public DiagnosisHolder(@NonNull View itemView) {
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
