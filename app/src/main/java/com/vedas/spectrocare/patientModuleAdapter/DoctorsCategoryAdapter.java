package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vedas.spectrocare.PatientModule.SearchResultsActivity;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.model.CategoryItemModel;
import com.vedas.spectrocare.model.DoctorsItemModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DoctorsCategoryAdapter extends RecyclerView.Adapter<DoctorsCategoryAdapter.DoctoresCategiryHolder> {
    Context context;
    ArrayList<CategoryItemModel> categoryItemList;

    public DoctorsCategoryAdapter(Context context, ArrayList<CategoryItemModel> categoryItemList) {
        this.context = context;
        this.categoryItemList = categoryItemList;
    }

    @NonNull
    @Override
    public DoctorsCategoryAdapter.DoctoresCategiryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View categoryView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_item_view, parent, false);
        return new DoctoresCategiryHolder(categoryView);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorsCategoryAdapter.DoctoresCategiryHolder holder, int position) {
        Picasso.get().load("http://34.231.177.197:3000"+ categoryItemList.get(position).getImage()).placeholder(R.drawable.sample_image).into(holder.imgIcon);

     //   holder.imgIcon.setImageResource(categoryItemList.get(position).getCategoryIcon());
        holder.txtTitle.setText(categoryItemList.get(position).getCategoryTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(context, SearchResultsActivity.class);
                i.putExtra("category",categoryItemList.get(holder.getAdapterPosition()).getCategoryTitle());
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryItemList.size();
    }

    public class DoctoresCategiryHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView txtTitle;
        public DoctoresCategiryHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.img_doc_pic);
            txtTitle =itemView.findViewById(R.id.txt_doc_name);
        }
    }
    public void filterList(ArrayList<CategoryItemModel> filterdNames) {
        this.categoryItemList = filterdNames;
        notifyDataSetChanged();
    }
}
