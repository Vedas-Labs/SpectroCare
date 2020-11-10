package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vedas.spectrocare.PatientModule.PatientEditMedicationsRecordActivity;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicationArrayObject;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicationObject;
import com.vedas.spectrocare.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PatientMedicationRecordAdapter extends RecyclerView.Adapter<PatientMedicationRecordAdapter.MedicationHolder> {
    Context context;
    ArrayList<PatientMedicationObject> list;
    public PatientMedicationRecordAdapter(Context context,ArrayList<PatientMedicationObject> list1) {
        this.context = context;
        this.list=list1;
    }

    @Override
    public MedicationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View medicationsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient_medication_record, parent, false);
        return new MedicationHolder(medicationsView);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationHolder holder, int position) {
      PatientMedicationObject obj= PatientMedicalRecordsController.getInstance().medicationArrayList.get(position);
        holder.idText.setText(obj.getIllnessMedicationID());
        ///holder.name.setText(obj.get());
        holder.listText.setText(getMedicationListText(obj.getMedicatioArrayObjects()));
        holder.prescribedText.setText("");
        holder.doctorText.setText(obj.getDoctorName());
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientMedicalRecordsController.getInstance().selectedMedication=PatientMedicalRecordsController.getInstance().medicationArrayList.get(position);
                context.startActivity(new Intent(context,PatientEditMedicationsRecordActivity.class));

            }
        });
    }
    @Override
    public int getItemCount() {
        if(list.size()>0){
            return  list.size();
        }else {
            return 0;
        }
    }

    public class MedicationHolder extends RecyclerView.ViewHolder {
        Button btnView;
        TextView idText,listText,prescribedText,doctorText,name;
        public MedicationHolder(@NonNull View itemView) {
            super(itemView);
            btnView = itemView.findViewById(R.id.btn_view);
            name = itemView.findViewById(R.id.name);

            idText = itemView.findViewById(R.id.idtext);
            listText = itemView.findViewById(R.id.listtext);
            prescribedText = itemView.findViewById(R.id.prestext);
            doctorText = itemView.findViewById(R.id.doctortext);
        }
    }
    private String getMedicationListText(ArrayList<PatientMedicationArrayObject> list){
        String text="";
        if (list!=null){
            if(list.size()>0){
                for(int i=0;i<list.size();i++){
                    PatientMedicationArrayObject obj=list.get(i);
                    if(i==0){
                        text=obj.getName();
                    }else{
                        text=text+","+obj.getName();
                    }
                }
            }

        }
/*
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                PatientMedicationArrayObject obj=list.get(i);
               if(i==0){
                   text=obj.getName();
               }else{
                   text=text+","+obj.getName();
               }
            }
        }
*/
        return text;
    }
}
