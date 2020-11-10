package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vedas.spectrocare.R;
import com.vedas.spectrocare.model.DoctorsItemModel;
import com.vedas.spectrocare.model.RecordModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DoctorsAvailabilityAdapter extends RecyclerView.Adapter<DoctorsAvailabilityAdapter.AvailablityHolder> {
    Context context;
    ArrayList<RecordModel> listOfTimings;

    public DoctorsAvailabilityAdapter(Context context, ArrayList<RecordModel> listOfTimings) {
        this.context = context;
        this.listOfTimings = listOfTimings;
    }

    @Override
    public DoctorsAvailabilityAdapter.AvailablityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View doctorAvailableView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_availability_list, parent, false);
        return new AvailablityHolder(doctorAvailableView);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorsAvailabilityAdapter.AvailablityHolder holder, int position) {
        holder.txtWeekName.setText(listOfTimings.get(position).getsNo());
        holder.txtDayTimings.setText(listOfTimings.get(position).getCondition());
        holder.txtTimings.setText(listOfTimings.get(position).getRelation());

    }

    @Override
    public int getItemCount() {
        return listOfTimings.size();
    }

    public class AvailablityHolder extends RecyclerView.ViewHolder {
        TextView txtWeekName,txtDayTimings,txtTimings;
        public AvailablityHolder(@NonNull View itemView) {
            super(itemView);
            txtTimings = itemView.findViewById(R.id.txt_timings);
            txtDayTimings = itemView.findViewById(R.id.txt_day_time);
            txtWeekName = itemView.findViewById(R.id.txt_week_name);
        }
    }
}
