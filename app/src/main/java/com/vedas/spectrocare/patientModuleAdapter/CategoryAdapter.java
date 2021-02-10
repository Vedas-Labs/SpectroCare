package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vedas.spectrocare.PatientModule.SearchResultsActivity;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.model.CategoryItemModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.DoctorsHolder> {
    Context context;
   // ArrayList<CategoryItemModel> categoryItemList;

    public CategoryAdapter(Context context/*, ArrayList<CategoryItemModel> categoryItemList*/) {
        this.context = context;
      //  this.categoryItemList = categoryItemList;
    }

    @NonNull
    @Override
    public CategoryAdapter.DoctorsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View doctorItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_items_layout, parent, false);
        return new DoctorsHolder(doctorItemView);    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.DoctorsHolder holder, int position) {
        holder.doctorsItemIcon.setImageResource(PatientMedicalRecordsController.getInstance().doctorsCategoryList.get(position).getCategoryIcon());
        holder.doctorsItemName.setText(PatientMedicalRecordsController.getInstance().doctorsCategoryList.get(position).getCategoryTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(context, SearchResultsActivity.class);
                i.putExtra("category",PatientMedicalRecordsController.getInstance().doctorsCategoryList.get(holder.getAdapterPosition()).getCategoryTitle());
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        if(PatientMedicalRecordsController.getInstance().doctorsCategoryList.size()>0) {
            if(PatientMedicalRecordsController.getInstance().doctorsCategoryList.size()>3){
                return 3;
            }else{
                return PatientMedicalRecordsController.getInstance().doctorsCategoryList.size();
            }
        }else{
            return 0;
        }
    }

    public class DoctorsHolder extends RecyclerView.ViewHolder {
        ImageView doctorsItemIcon;
        TextView doctorsItemName;

        public DoctorsHolder(@NonNull View itemView) {
            super(itemView);
            doctorsItemIcon = itemView.findViewById(R.id.img_item);
            doctorsItemName = itemView.findViewById(R.id.txt_item);

        }
    }
}
