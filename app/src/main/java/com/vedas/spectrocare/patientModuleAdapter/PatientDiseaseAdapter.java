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

import com.vedas.spectrocare.PatientModule.PatientDiseaseActivity;
import com.vedas.spectrocare.PatientServerApiModel.FamilyDetaislModel;
import com.vedas.spectrocare.PatientServerApiModel.IllnessPatientRecord;
import com.vedas.spectrocare.PatinetControllers.PatientFamilyDataController;
import com.vedas.spectrocare.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PatientDiseaseAdapter extends RecyclerView.Adapter<PatientDiseaseAdapter.PatientDiseasHolder> {
    ArrayList<IllnessPatientRecord> diseaseList;
    Context context;
    String clockTime;
    TextView txtDelete,txtCount;

    public PatientDiseaseAdapter(Context context) {
        this.context = context;
    }

    public PatientDiseaseAdapter( Context context,ArrayList<IllnessPatientRecord> diseaseList, TextView txtDelete, TextView txtCount) {
        this.diseaseList = diseaseList;
        this.context = context;
        this.txtDelete = txtDelete;
        this.txtCount = txtCount;
    }

    public PatientDiseaseAdapter(Context context, ArrayList<IllnessPatientRecord> diseaseList) {
        this.diseaseList = diseaseList;
        this.context = context;
    }

    @NonNull
    @Override
    public PatientDiseaseAdapter.PatientDiseasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View allMedical = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_recycle_view, parent, false);
        return new PatientDiseasHolder(allMedical);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientDiseaseAdapter.PatientDiseasHolder holder, int position) {

        holder.imgDisease.setImageResource(R.drawable.disease);
        holder.diseaseName.setText(diseaseList.get(position).getCurrentStatus());
        holder.subTxt.setText(diseaseList.get(position).getDescription());
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String position =  String.valueOf(holder.getAdapterPosition());
                context.startActivity(new Intent(context, PatientDiseaseActivity.class).putExtra("position",position));
            }
        });
        String entredDate = diseaseList.get(position).getAddedDate();
        Log.e("string","date"+entredDate);
        long l = Long.parseLong(entredDate);
        Date currentDate = new Date(l);
        SimpleDateFormat jdff = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        jdff.setTimeZone(TimeZone.getDefault());
        String java_date = jdff.format(currentDate);
        Date clickedDate = null;
        try {
            clickedDate = jdff.parse(java_date);

            String  formattedDate = jdff.format(clickedDate);
            Log.e("forrr","ff"+formattedDate);
            String[] two = formattedDate.split(" ");
            holder.txtDate.setText(two[0]);
            String[] timeSplit = two[1].split(":");
            if (12 < Integer.parseInt(timeSplit[0])){
                int hr = Integer.parseInt(timeSplit[0])-12;
                clockTime = String.valueOf(hr)+":"+timeSplit[1]+"PM";
            }else{
                clockTime = two[1]+"AM";
            }
            holder.txtTime.setText(clockTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {

        if (!diseaseList.isEmpty()){
            Log.e("disese",""+diseaseList.size());
            txtDelete.setVisibility(View.VISIBLE);
            txtCount.setText("("+diseaseList.size()+")");
            return diseaseList.size();
        }else {
            Log.e("chch","fa");
            txtDelete.setVisibility(View.GONE);
            txtCount.setText("("+0+")");
            return 0;
        }
    }

    public class PatientDiseasHolder extends RecyclerView.ViewHolder {
        Button btnView;
        TextView diseaseName,txtDate,txtTime,subTxt;
        ImageView imgDisease;
        public PatientDiseasHolder(@NonNull View itemView) {
            super(itemView);
            btnView = itemView.findViewById(R.id.btn_view);
            subTxt = itemView.findViewById(R.id.txt_infection_name);
            txtDate = itemView.findViewById(R.id.txt_doc_date);
            txtTime = itemView.findViewById(R.id.txt_doc_time);
            imgDisease = itemView.findViewById(R.id.img_doc_pic);
            diseaseName = itemView.findViewById(R.id.txt_doc_name);

        }
    }
}
