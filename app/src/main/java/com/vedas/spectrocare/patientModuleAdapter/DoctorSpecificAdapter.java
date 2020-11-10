package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vedas.spectrocare.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DoctorSpecificAdapter extends RecyclerView.Adapter<DoctorSpecificAdapter.DcotorSpecificHolder> {
    Context context;
    ArrayList specialDepart;

    public DoctorSpecificAdapter(Context context, ArrayList specialDepart) {
        this.context = context;
        this.specialDepart = specialDepart;
    }

    public DoctorSpecificAdapter(Context context) {
        this.context = context;
    }

    @Override
    public DoctorSpecificAdapter.DcotorSpecificHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View specificView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_button, parent, false);
        return new DcotorSpecificHolder(specificView);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorSpecificAdapter.DcotorSpecificHolder holder, int position) {
        holder.setText.setText(specialDepart.get(position).toString());

    }

    @Override
    public int getItemCount() {
        return specialDepart.size();
    }

    public class DcotorSpecificHolder extends RecyclerView.ViewHolder {
        TextView setText;

        public DcotorSpecificHolder(@NonNull View itemView) {
            super(itemView);
            setText = itemView.findViewById(R.id.set_text);

        }
    }
}
