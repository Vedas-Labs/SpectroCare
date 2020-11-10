package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vedas.spectrocare.PatientModule.SearchResultsActivity;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.model.CategoryItemModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DoctorsDepartmentAdapter extends RecyclerView.Adapter<DoctorsDepartmentAdapter.DepartmentHolder> {
    Context context;
    ArrayList<CategoryItemModel> departmentItemList;

    public DoctorsDepartmentAdapter(Context context, ArrayList<CategoryItemModel> departmentItemList) {
        this.context = context;
        this.departmentItemList = departmentItemList;
    }

    @Override
    public DoctorsDepartmentAdapter.DepartmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View departmentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_item_view, parent, false);
        return new DepartmentHolder(departmentView);    }

    @Override
    public void onBindViewHolder(@NonNull final DoctorsDepartmentAdapter.DepartmentHolder holder, int position) {
        holder.imgDepartmentIcon.setImageResource(departmentItemList.get(position).getCategoryIcon());
        holder.txtDepartmentTitle.setText(departmentItemList.get(position).getCategoryTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, SearchResultsActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return departmentItemList.size();
    }

    public class DepartmentHolder extends RecyclerView.ViewHolder {
        ImageView imgDepartmentIcon;
        TextView txtDepartmentTitle;
        public DepartmentHolder(@NonNull View itemView) {
            super(itemView);
            imgDepartmentIcon = itemView.findViewById(R.id.img_doc_pic);
            txtDepartmentTitle =itemView.findViewById(R.id.txt_doc_name);
        }
    }
    public void filterList(ArrayList<CategoryItemModel> filterdNames) {
        this.departmentItemList = filterdNames;
        notifyDataSetChanged();
    }
}
