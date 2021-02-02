package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.SelectUserActivity;
import com.vedas.spectrocare.model.CategoryItemModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookPatientAppointmentAdapted extends RecyclerView.Adapter<BookPatientAppointmentAdapted.PatientAppointmentHolder> {
    Context context;
    int positionItem = -1;
    int i = -1;
    ArrayList<String> timesArray;
    TextView txtDisc;

    public BookPatientAppointmentAdapted(Context context, ArrayList<String> timesArray, TextView txtDisc) {
        this.context = context;
        this.timesArray = timesArray;
        this.txtDisc = txtDisc;
    }

    public BookPatientAppointmentAdapted(Context context, ArrayList<String> timesArray) {
        this.context = context;
        this.timesArray = timesArray;
    }

    @Override
    public PatientAppointmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View doctorItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient_book_appointment, parent, false);
        return new PatientAppointmentHolder(doctorItemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final PatientAppointmentHolder holder, final int position) {
        holder.btnTime.setText(timesArray.get(position).toString());

        if (i == holder.getAdapterPosition()) {
            holder.btnTime.setBackground(context.getResources().getDrawable(R.drawable.btn_yellow_backgroound));
            holder.btnTime.setTextColor(Color.parseColor("#ffffff"));
        } else {
            holder.btnTime.setBackground(context.getResources().getDrawable(R.drawable.new_login_boarder));
            holder.btnTime.setTextColor(Color.parseColor("#3E454C"));
        }

        holder.btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positionItem = holder.getAdapterPosition();
                i = holder.getAdapterPosition();
                notifyDataSetChanged();

                //   holder.btnTime.setBackgroundColor(Color.parseColor("#FFA500"));

            }
        });


    }

    @Override
    public int getItemCount()
    {
        if (timesArray.size()>0){
            txtDisc.setVisibility(View.GONE);
        }else{
            txtDisc.setVisibility(View.VISIBLE);
        }
        return timesArray.size();
    }

    public String getSelectrdPosition() {
        if (positionItem == -1) {
           // Toast.makeText(context, "select time slot", Toast.LENGTH_SHORT).show();
            return "";
        } else {
            return timesArray.get(positionItem);

        }
    }

    public class PatientAppointmentHolder extends RecyclerView.ViewHolder {
        Button btnTime;

        public PatientAppointmentHolder(@NonNull View itemView) {
            super(itemView);
            btnTime = itemView.findViewById(R.id.btn_time);
        }
    }
}
