package com.vedas.spectrocare.adapter;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vedas.spectrocare.R;
import com.vedas.spectrocare.model.FilterItemModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static android.graphics.Color.parseColor;

public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.FilterHolder> {
    Context context;
    ArrayList<FilterItemModel> filterItemsArray;

    public FiltersAdapter(Context context, ArrayList<FilterItemModel> filterItemsArray) {
        this.context = context;
        this.filterItemsArray = filterItemsArray;
    }

    @NonNull
    @Override
    public FiltersAdapter.FilterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View filterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.filters_recycle_item,parent,false);
        return new FilterHolder(filterView);
    }

    @Override
    public void onBindViewHolder(@NonNull final FiltersAdapter.FilterHolder holder, int position) {
        holder.txtFilterItem.setText(filterItemsArray.get(position).getFilterItem());
        holder.txtFilterItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  holder.txtFilterItem.setBackground(parseColor("#3E454C"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterItemsArray.size();
    }

    public class FilterHolder extends RecyclerView.ViewHolder {
        TextView txtFilterItem;
        public FilterHolder(@NonNull View itemView) {
            super(itemView);
            txtFilterItem = itemView.findViewById(R.id.item_filter);
        }
    }
}
