package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vedas.spectrocare.R;
import com.vedas.spectrocare.model.CategoryItemModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PatientItemAdapter extends RecyclerView.Adapter<PatientItemAdapter.PatientItemHolder> {
    Context context;
    ArrayList<CategoryItemModel> categoryItemList;

    public PatientItemAdapter(Context context, ArrayList<CategoryItemModel> categoryItemList) {
        this.context = context;
        this.categoryItemList = categoryItemList;
    }

    @NonNull
    @Override
    public PatientItemAdapter.PatientItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View patientItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_items_layout, parent, false);
        return new PatientItemHolder(patientItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientItemAdapter.PatientItemHolder holder, int position) {
        holder.patientItemIcon.setImageResource(categoryItemList.get(position).getCategoryIcon());
        holder.patientItemName.setText(categoryItemList.get(position).getCategoryTitle());

    }

    @Override
    public int getItemCount() {
        return  categoryItemList.size();
    }

    public class PatientItemHolder extends RecyclerView.ViewHolder {
        ImageView patientItemIcon;
        TextView patientItemName;

        public PatientItemHolder(@NonNull View itemView) {
            super(itemView);
            patientItemIcon = itemView.findViewById(R.id.img_item);
            patientItemName = itemView.findViewById(R.id.txt_item);

        }
    }
}
