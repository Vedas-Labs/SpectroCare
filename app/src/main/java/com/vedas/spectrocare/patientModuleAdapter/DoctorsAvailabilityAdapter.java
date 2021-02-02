package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vedas.spectrocare.PatientDocResponseModel.OfficeHours;
import com.vedas.spectrocare.PatientDocResponseModel.Sessions;
import com.vedas.spectrocare.R;

import java.util.ArrayList;

public class DoctorsAvailabilityAdapter extends RecyclerView.Adapter<DoctorsAvailabilityAdapter.AvailablityHolder> {
    Context context;
    ArrayList<OfficeHours> listOfTimings;

    public DoctorsAvailabilityAdapter(Context context, ArrayList<OfficeHours> listOfTimings) {
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
        OfficeHours obj = listOfTimings.get(position);
        holder.txtWeekName.setText(obj.getDayName());
        loadTimings(holder);
    }

    @Override
    public int getItemCount() {
        if (listOfTimings.size() > 0) {
            return listOfTimings.size();
        } else {
            return 0;
        }
    }

    public class AvailablityHolder extends RecyclerView.ViewHolder {
        TextView txtWeekName, txtMorning, txtAfternoon, txt_night;

        public AvailablityHolder(@NonNull View itemView) {
            super(itemView);
            txtAfternoon = itemView.findViewById(R.id.txt_timings);
            txtMorning = itemView.findViewById(R.id.txt_day_time);
            txt_night = itemView.findViewById(R.id.txt_night);
            txtWeekName = itemView.findViewById(R.id.txt_week_name);
        }
    }

    private void loadTimings(DoctorsAvailabilityAdapter.AvailablityHolder holder) {
        for (int i = 0; i < listOfTimings.size(); i++) {
            ArrayList<Sessions> sessionsArrayList = listOfTimings.get(i).getSessions();
            holder.txtMorning.setText(sessionsArrayList.get(0).getStartTime()/*.replace("AM","-")*/+"-" + sessionsArrayList.get(0).getEndTime()/*.replace("PM","")*/);
            holder.txtAfternoon.setText(sessionsArrayList.get(1).getStartTime().replace("PM","-") + sessionsArrayList.get(1).getEndTime()/*.replace("PM","")*/);
            holder.txt_night.setText(sessionsArrayList.get(2).getStartTime().replace("PM","-") + sessionsArrayList.get(2).getEndTime()/*.replace("PM","")*/);
        }
    }
}
