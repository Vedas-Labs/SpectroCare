package com.vedas.spectrocare.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.HomeActivity;
import com.vedas.spectrocare.activities.MyPatientActivity;
import com.vedas.spectrocare.model.CategoryItemModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {
    Context context;
    ArrayList<CategoryItemModel> homeItemList;

    public HomeAdapter(Context context, ArrayList<CategoryItemModel> homeItemList) {
        this.context = context;
        this.homeItemList = homeItemList;
    }

    @NonNull
    @Override
    public HomeAdapter.HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View homeItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_grid_view,parent,false);
        return new HomeHolder(homeItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeAdapter.HomeHolder holder, int position) {
        holder.homeItemIcon.setImageResource(homeItemList.get(position).getCategoryIcon());
        holder.homeItemTitle.setText(homeItemList.get(position).getCategoryTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getAdapterPosition()==0){
                    MyPatientActivity.isFromStarting=true;
                    Intent myPatientIntent = new Intent(context, MyPatientActivity.class);
                    context.startActivity(myPatientIntent);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeItemList.size();
    }

    public class HomeHolder extends RecyclerView.ViewHolder {

        ImageView homeItemIcon;
        TextView homeItemTitle;

        public HomeHolder( View itemView) {
            super(itemView);
            homeItemIcon = itemView.findViewById(R.id.img_home_icon);
            homeItemTitle = itemView.findViewById(R.id.txt_home_title);
        }
    }
}
