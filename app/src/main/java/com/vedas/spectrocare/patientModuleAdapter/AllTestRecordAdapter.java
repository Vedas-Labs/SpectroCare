package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.vedas.spectrocare.PatientModule.ResultPageViewActivity;
import com.vedas.spectrocare.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AllTestRecordAdapter extends RecyclerView.Adapter<AllTestRecordAdapter.AllRecordHolder> {
    Context context;
    public boolean  testboolesn = false;
    public AllTestRecordAdapter(Context context) {
        this.context = context;
    }

    @Override
    public AllTestRecordAdapter.AllRecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View allTestView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_recycle_view, parent, false);
        return new AllRecordHolder(allTestView);
    }

    @Override
    public void onBindViewHolder(@NonNull AllTestRecordAdapter.AllRecordHolder holder, int position) {
        holder.txtNormal.setText("Normal");
        holder.txtTest.setText("Urine Routine");
        holder.imgIcon.setBackgroundResource(R.drawable.testtube);
        holder.txtNormal.setTextColor(Color.parseColor("#2DAF22"));
        holder.txtAbnormal.setVisibility(View.VISIBLE);
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ResultPageViewActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }
    public void testMethod(){

        testboolesn=true;
        notifyDataSetChanged();
    }

    public class AllRecordHolder extends RecyclerView.ViewHolder {
        TextView txtNormal,txtAbnormal,txtTest;
        ImageView imgIcon;
        CheckBox checkBox;
        Button btnView;
        public AllRecordHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.img_doc_pic);
            txtTest = itemView.findViewById(R.id.txt_doc_name);
            checkBox = itemView.findViewById(R.id.checkbox);
            btnView = itemView.findViewById(R.id.btn_view);
            txtAbnormal = itemView.findViewById(R.id.txt_Abnormal);
            txtNormal = itemView.findViewById(R.id.txt_infection_name);
        }
    }
}
