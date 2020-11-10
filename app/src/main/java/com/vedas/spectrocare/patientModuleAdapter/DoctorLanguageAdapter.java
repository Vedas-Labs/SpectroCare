package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vedas.spectrocare.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DoctorLanguageAdapter extends RecyclerView.Adapter<DoctorLanguageAdapter.LanguageHolder> {
   Context context;
   ArrayList languageList;

    public DoctorLanguageAdapter(Context context, ArrayList languageList) {
        this.context = context;
        this.languageList = languageList;
    }

    @Override
    public DoctorLanguageAdapter.LanguageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View lanView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_button, parent, false);
        return new LanguageHolder(lanView);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorLanguageAdapter.LanguageHolder holder, int position) {
        holder.setText.setText(languageList.get(position).toString());
      //  holder.setText.setText("English");

    }

    @Override
    public int getItemCount() {
        return languageList.size();
    }

    public class LanguageHolder extends RecyclerView.ViewHolder {
        TextView setText;
        public LanguageHolder(@NonNull View itemView) {
            super(itemView);
            setText = itemView.findViewById(R.id.set_text);
        }
    }
}
