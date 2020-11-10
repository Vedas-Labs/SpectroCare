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

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.DoctorsHolder> {
    Context context;
    ArrayList<CategoryItemModel> categoryItemList;

    public DoctorsAdapter(Context context, ArrayList<CategoryItemModel> categoryItemList) {
        this.context = context;
        this.categoryItemList = categoryItemList;
    }

    @NonNull
    @Override
    public DoctorsAdapter.DoctorsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View doctorItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_items_layout, parent, false);
        return new DoctorsHolder(doctorItemView);    }

    @Override
    public void onBindViewHolder(@NonNull DoctorsAdapter.DoctorsHolder holder, int position) {
        holder.doctorsItemIcon.setImageResource(categoryItemList.get(position).getCategoryIcon());
        holder.doctorsItemName.setText(categoryItemList.get(position).getCategoryTitle());

    }

    @Override
    public int getItemCount() {
        return  categoryItemList.size();
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
