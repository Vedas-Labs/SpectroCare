package com.vedas.spectrocare.patientModuleAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.PatientModule.AddPatientAllergyActivity;
import com.vedas.spectrocare.PatientModule.PatientAllergeyActivity;

import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class PatientAllergyAdapter extends RecyclerView.Adapter<PatientAllergyAdapter.AllergyHolder> {
    Context context;
    TextView txtDelete,txtCount;
    public PatientAllergyAdapter(Context context) {
        this.context = context;
    }

    public PatientAllergyAdapter(Context context, TextView txtDelete, TextView txtCount) {
        this.context = context;
        this.txtDelete = txtDelete;
        this.txtCount = txtCount;
    }

    @NonNull
    @Override
    public AllergyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View allergyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_recycle_view, parent, false);
        return new AllergyHolder(allergyView);
    }
    @Override
    public void onBindViewHolder(@NonNull AllergyHolder holder, int position) {
        holder.imgAllergyICon.setImageResource(R.drawable.allergy);
        holder.discription.setText(PatientMedicalRecordsController.getInstance().noteallergyArray.get(position).getNote());
        holder.allergyName.setText(PatientMedicalRecordsController.getInstance().noteallergyArray.get(position).getName());
        long millis = Long.parseLong(PatientMedicalRecordsController.getInstance().allergyObjectArrayList.get(0).getCreatedDate());
        Date d = new Date(millis);
        SimpleDateFormat weekFormatter = new SimpleDateFormat("yyyy/MM/dd hh:mm a", Locale.ENGLISH);
        String weekString = weekFormatter.format(d);
        String time[]=weekString.split(" ");
        Log.e("weeekarray", "" + time[0]+time[1]+time[2]);
        holder.date.setText(time[0]);
        holder.time.setText(time[1]+" "+time[2]);
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((Activity)context).finish();
                PatientMedicalRecordsController.getInstance().selectedPos=holder.getAdapterPosition();
                PatientMedicalRecordsController.getInstance().selectedObject=PatientMedicalRecordsController.getInstance().noteallergyArray.get(position);
                context.startActivity(new Intent(context, AddPatientAllergyActivity.class));
            }
        });
    }
    @Override
    public int getItemCount() {
        if(PatientMedicalRecordsController.getInstance().noteallergyArray.size()>0){
            txtDelete.setVisibility(View.VISIBLE);
            txtCount.setText("("+PatientMedicalRecordsController.getInstance().noteallergyArray.size()+")");
            return PatientMedicalRecordsController.getInstance().noteallergyArray.size();
        }else {
            txtDelete.setVisibility(View.GONE);
            txtCount.setText("("+0+")");
            return 0;
        }

    }
    public class AllergyHolder extends RecyclerView.ViewHolder {
        Button btnView;
        TextView allergyName,discription,date,time;
        ImageView imgAllergyICon;
        public AllergyHolder(@NonNull View itemView) {
            super(itemView);
            btnView = itemView.findViewById(R.id.btn_view);
            imgAllergyICon = itemView.findViewById(R.id.img_doc_pic);
            allergyName = itemView.findViewById(R.id.txt_doc_name);
            discription = itemView.findViewById(R.id.txt_infection_name);//txt_doc_date
            date = itemView.findViewById(R.id.txt_doc_date);//txt_doc_time
            time = itemView.findViewById(R.id.txt_doc_time);//txt_doc_time

        }
    }

}
