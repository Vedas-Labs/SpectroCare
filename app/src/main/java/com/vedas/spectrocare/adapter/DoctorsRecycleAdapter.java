package com.vedas.spectrocare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vedas.spectrocare.DataBase.DoctorInfoDataController;
import com.vedas.spectrocare.DataBaseModels.DoctorInfoModel;
import com.vedas.spectrocare.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

public class DoctorsRecycleAdapter extends RecyclerView.Adapter<DoctorsRecycleAdapter.DoctorsHolder> {
    Context context;
    ArrayList<DoctorInfoModel> doctorsArrayList ;

    public DoctorsRecycleAdapter(Context context, ArrayList<DoctorInfoModel> doctorsArrayList) {
        this.context = context;
        this.doctorsArrayList = doctorsArrayList;
    }

    @Override
    public DoctorsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View doctorsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctors_recycle_item,null);
        return new DoctorsHolder(doctorsView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DoctorsHolder holder, final int position) {

       DoctorInfoModel doctorInfoModel= doctorsArrayList.get(position);
        holder.docName.setText("Dr. "+doctorInfoModel.getDoctorName());
        holder.doctorSpe.setText(doctorInfoModel.getSpecialization());
        holder.doctorDepart.setText(doctorInfoModel.getDepartment());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoctorInfoDataController.getInstance().currentDoctorInfo=doctorsArrayList.get(holder.getAdapterPosition());
                EventBus.getDefault().post(new ViewMedicalAdapter.MessageEvent("loadDoctor",holder.getAdapterPosition()));

            }
        });
    }

    @Override
    public int getItemCount() {
         if(doctorsArrayList.size()>0){
            return doctorsArrayList.size();
        }else {
             return 0;
         }
    }

    public class DoctorsHolder extends RecyclerView.ViewHolder {
        TextView docName,doctorSpe,doctorDepart;
        public DoctorsHolder(@NonNull View itemView) {
            super(itemView);
            docName = itemView.findViewById(R.id.txt_doctor_name);
            doctorSpe = itemView.findViewById(R.id.txt_spe);
            doctorDepart = itemView.findViewById(R.id.txt_depart);
        }
    }
}
