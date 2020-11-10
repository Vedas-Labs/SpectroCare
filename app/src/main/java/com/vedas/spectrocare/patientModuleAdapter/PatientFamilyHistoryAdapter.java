package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vedas.spectrocare.PatientModule.PatientDiseaseActivity;
import com.vedas.spectrocare.PatientServerApiModel.FamilyDetaislModel;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.PatinetControllers.PatientFamilyDataController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.PatientModule.PatientFamilyHistoryActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PatientFamilyHistoryAdapter extends RecyclerView.Adapter<PatientFamilyHistoryAdapter.FamilyHistoryHolder> {

    Context context;
    String clockTime;
    TextView txtDelete,txtCount;
    ArrayList<FamilyDetaislModel> familyDetailsList=new ArrayList<>();

   /* public PatientFamilyHistoryAdapter(Context context, ArrayList<FamilyDetaislModel> familyDetailsList) {
        this.context = context;
        this.familyDetailsList = familyDetailsList;
    }
*/

    public PatientFamilyHistoryAdapter(Context context, ArrayList<FamilyDetaislModel> familyDetailsList, TextView txtDelete, TextView txtCount) {
        this.context = context;
        this.txtDelete = txtDelete;
        this.txtCount = txtCount;
        this.familyDetailsList = familyDetailsList;
    }

    public PatientFamilyHistoryAdapter(Context context) {
        this.context = context;
    }


    @Override
    public PatientFamilyHistoryAdapter.FamilyHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View familyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_recycle_view, parent, false);
        return new FamilyHistoryHolder(familyView);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientFamilyHistoryAdapter.FamilyHistoryHolder holder, int position) {

        Log.e("shghf","ljdfkhgf");

        holder.imgFamilyICon.setImageResource(R.drawable.people);
        holder.txtRelation.setText(familyDetailsList.get(position).getRelationship());
        holder.familyName.setText(familyDetailsList.get(position).getDieseaseName());
        String entredDate =  PatientFamilyDataController.getInstance().getResponseObject().getRecords().getCreatedDate();
        Log.e("string","date"+entredDate);
        long l = Long.parseLong(entredDate);
        Date currentDate = new Date(l);
        SimpleDateFormat jdff = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        jdff.setTimeZone(TimeZone.getDefault());
        String java_date = jdff.format(currentDate);
        Date clickedDate = null;
        try {
            clickedDate = jdff.parse(java_date);

              String  formattedDate = jdff.format(clickedDate);
                Log.e("forrr","ff"+formattedDate);
                String[] two = formattedDate.split(" ");
                holder.txtDate.setText(two[0]);
                String[] timeSplit = two[1].split(":");
                if (12 < Integer.parseInt(timeSplit[0])){
                    int hr = Integer.parseInt(timeSplit[0])-12;
                     clockTime = String.valueOf(hr)+":"+timeSplit[1]+"PM";
                }else{
                    clockTime = two[1]+"AM";
                }
                holder.txtTime.setText(clockTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                familyDetailsList.get(holder.getAdapterPosition());
                String position = String.valueOf(holder.getAdapterPosition());
                context.startActivity(new Intent(context, PatientFamilyHistoryActivity.class).
                        putExtra("position",position));
            }
        });

    }
/*
    public void newList(ArrayList<FamilyDetaislModel> familyDetailsLists){

        Log.e("dbf","djksfh"+familyDetailsList.size());
        Log.e("dbfg","djksfh"+familyDetailsList.get(0).getDieseaseName());


        this.familyDetailsList=familyDetailsLists;
       notifyDataSetChanged();

    }*/

    @Override
    public int getItemCount() {
        if (familyDetailsList.size() > 0) {
            Log.e("chii","fa");
            txtDelete.setVisibility(View.VISIBLE);
            txtCount.setText("("+familyDetailsList.size()+")");
            return familyDetailsList.size();
        } else {
            Log.e("chch","fa");
                txtDelete.setVisibility(View.GONE);
             txtCount.setText("("+0+")");
             return 0;
        }

    }

    public class FamilyHistoryHolder extends RecyclerView.ViewHolder {
        Button btnView;
        TextView familyName,txtRelation,txtDate,txtTime;
        ImageView imgFamilyICon;
        public FamilyHistoryHolder(@NonNull View itemView) {
            super(itemView);
            btnView = itemView.findViewById(R.id.btn_view);
            txtDate = itemView.findViewById(R.id.txt_doc_date);
            txtTime = itemView.findViewById(R.id.txt_doc_time);
            imgFamilyICon = itemView.findViewById(R.id.img_doc_pic);
            familyName = itemView.findViewById(R.id.txt_doc_name);
            txtRelation = itemView.findViewById(R.id.txt_infection_name);

        }
    }
}
