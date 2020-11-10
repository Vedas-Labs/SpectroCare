package com.vedas.spectrocare.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.AllergiesActivity;
import com.vedas.spectrocare.model.RecordModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecordRecyclerAdapter extends RecyclerView.Adapter<RecordRecyclerAdapter.RecordHolder> {
    ArrayList<RecordModel> recordModels =new ArrayList<>();
    Context context;
    Button btnDelete;
    boolean deleteImg=false;

    public RecordRecyclerAdapter(ArrayList<RecordModel> recordModels, Context context, Button btnDelete) {
        this.recordModels = recordModels;
        this.context = context;
        this.btnDelete = btnDelete;
    }

    @NonNull
    @Override
    public RecordRecyclerAdapter.RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recordView= LayoutInflater.from(parent.getContext()).inflate(R.layout.record_recycler_item,parent,false);
        return new RecordHolder(recordView);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecordRecyclerAdapter.RecordHolder holder, int position) {
        int a = position+1;
        if (deleteImg==true){
            holder.imgDelete.setVisibility(View.VISIBLE);
        }else{
            holder.imgDelete.setVisibility(View.GONE);}
        holder.txtSno.setText( String.valueOf(a));
        holder.txtCondition.setText(recordModels.get(position).getCondition());
        holder.txtRelation.setText(recordModels.get(position).getRelation());
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDelete.setBackground(context.getResources().getDrawable(R.drawable.btn_bck_color));
                btnDelete.setTextColor(Color.parseColor("#ffffff"));

                deleteImg = true;
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return recordModels.size();
    }

    public class RecordHolder extends RecyclerView.ViewHolder {
        TextView txtCondition,txtRelation,txtSno;
        ImageView imgDelete;
        public RecordHolder(@NonNull View itemView) {
            super(itemView);
            txtCondition = itemView.findViewById(R.id.txt_item_condition);
            txtRelation = itemView.findViewById(R.id.txt_item_relation);
            txtSno = itemView.findViewById(R.id.txt_sno);
            imgDelete = itemView.findViewById(R.id.img_record_delete);


        }
    }
}
