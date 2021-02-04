package com.vedas.spectrocare.patientModuleAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vedas.spectrocare.PatientModule.AddPatientAllergyActivity;
import com.vedas.spectrocare.PatientModule.AddPatientDiseaseActivity;
import com.vedas.spectrocare.PatientModule.AddPatientFamilyHistoryActivity;
import com.vedas.spectrocare.PatientModule.AddPatientImmunizationActivity;
import com.vedas.spectrocare.PatientModule.AddPatientSurgeryActivity;
import com.vedas.spectrocare.PatientModule.DiseasesListActivity;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.model.CategoryItemModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PatientMedicalHistoryAddAdapter extends RecyclerView.Adapter<PatientMedicalHistoryAddAdapter.addHistoryModel> {
    Context context;
    ArrayList<CategoryItemModel> categoryItemList;

    public PatientMedicalHistoryAddAdapter(Context context, ArrayList<CategoryItemModel> categoryItemList) {
        this.context = context;
        this.categoryItemList = categoryItemList;
    }

    @NonNull
    @Override
    public addHistoryModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View medicalItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient_medical_add, parent, false);
        return new addHistoryModel(medicalItemView);     }

    @Override
    public void onBindViewHolder(@NonNull final addHistoryModel holder, final int position) {
        holder.medicalItemIcon.setImageResource(categoryItemList.get(position).getCategoryIcon());
        holder.medicalItemName.setText(categoryItemList.get(position).getCategoryTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientMedicalRecordsController.getInstance().isFromMedication = false;
                PatientMedicalRecordsController.getInstance().isFromDiagnosis=false;
                Log.e("checktoggle","call"+PatientMedicalRecordsController.getInstance().isFromMedication);
                switch (holder.getAdapterPosition()){
                    case 0:
                        //PatientMedicalRecordsController.getInstance().isFromDiagnosis=false;
                        context.startActivity(new Intent(context, AddPatientDiseaseActivity.class));
                        break;
                    case 1:
                       // PatientMedicalRecordsController.getInstance().isFromDiagnosis=false;
                        context.startActivity(new Intent(context, AddPatientFamilyHistoryActivity.class));
                        break;
                    case 2:
                        //PatientMedicalRecordsController.getInstance().isFromDiagnosis=false;
                        context.startActivity(new Intent(context, AddPatientAllergyActivity.class));
                        break;
                    case 3:
                       // context.startActivity(new Intent(context, AddPatientSurgeryActivity.class));
                       //((Activity)context).finish();
                       // PatientMedicalRecordsController.getInstance().isFromDiagnosis=false;
                        context.startActivity(new Intent(context, DiseasesListActivity.class));
                        break;
                    case 4:
                        //PatientMedicalRecordsController.getInstance().isFromDiagnosis=false;
                        context.startActivity(new Intent(context, AddPatientImmunizationActivity.class));
                        break;
                    case 5:
                       // ((Activity)context).finish();
                       // Toast.makeText(context, "Diagnosis not available", Toast.LENGTH_SHORT).show();
                        PatientMedicalRecordsController.getInstance().isFromDiagnosis=true;
                        context.startActivity(new Intent(context, DiseasesListActivity.class));
                        break;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
       return categoryItemList.size();
    }

    public class addHistoryModel extends RecyclerView.ViewHolder {
        ImageView medicalItemIcon;
        TextView medicalItemName;
        public addHistoryModel(@NonNull View itemView) {
            super(itemView);
            medicalItemIcon = itemView.findViewById(R.id.img_item);
            medicalItemName = itemView.findViewById(R.id.txt_item);

        }
    }
}
