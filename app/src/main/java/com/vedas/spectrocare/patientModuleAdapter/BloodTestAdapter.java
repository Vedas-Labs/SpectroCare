package com.vedas.spectrocare.patientModuleAdapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vedas.spectrocare.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BloodTestAdapter extends RecyclerView.Adapter<BloodTestAdapter.BloodTestHold> {
    Context context;
    public boolean  testboolesn = false;
    Button deleteBtn;

    public BloodTestAdapter(Context context, Button deleteBtn) {
        this.context = context;
        this.deleteBtn = deleteBtn;
    }

    @Override
    public BloodTestAdapter.BloodTestHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View bloodTestView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_recycle_view, parent, false);
        return new BloodTestHold(bloodTestView);
    }

    @Override
    public void onBindViewHolder(@NonNull BloodTestAdapter.BloodTestHold holder, int position) {
        holder.txtNormal.setText("Normal");
        holder.txtTest.setText("Lipid Panel");
        holder.imgIcon.setBackgroundResource(R.drawable.new_test);
        holder.txtNormal.setTextColor(Color.parseColor("#2DAF22"));
        if (testboolesn){
            holder.checkBox.setVisibility(View.VISIBLE);
        }else{
            holder.checkBox.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
        }
       // holder.txtAbnormal.setVisibility(View.VISIBLE);
    }
    public void showDeleteFileDialog(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_abort);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
        btnNo.setText("No");
        btnYes.setText("yes");

        TextView txt_title=dialog.findViewById(R.id.title);
        TextView txt_msg=dialog.findViewById(R.id.msg);
        TextView txt_msg1=dialog.findViewById(R.id.msg1);

        txt_title.setText("Delete Record");
        txt_msg.setText("Are you sure you want to");
        txt_msg1.setText(" delete file ?");

        RelativeLayout main=(RelativeLayout)dialog.findViewById(R.id.rl_main);
        RelativeLayout main1=(RelativeLayout)dialog.findViewById(R.id.rl_main1);

        GradientDrawable drawable = (GradientDrawable) main.getBackground();
        drawable.setColor(context.getResources().getColor(R.color.colorWhite));

        GradientDrawable drawable1 = (GradientDrawable) main1.getBackground();
        drawable1.setColor(context.getResources().getColor(R.color.colorWhite));

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testboolesn=false;
                //  holder1.checkBox.setVisibility(View.GONE);
                notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public int getItemCount() {
        return 4;
    }
    public void testMethod(){
        deleteBtn.setVisibility(View.VISIBLE);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDeleteFileDialog();
            }
        });
        testboolesn=true;
        notifyDataSetChanged();
    }
    public class BloodTestHold extends RecyclerView.ViewHolder {
        TextView txtNormal,txtAbnormal,txtTest;
        ImageView imgIcon;
        CheckBox checkBox;
        public BloodTestHold(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.img_doc_pic);
            checkBox = itemView.findViewById(R.id.checkbox);
            txtTest = itemView.findViewById(R.id.txt_doc_name);
            txtAbnormal = itemView.findViewById(R.id.txt_Abnormal);
            txtNormal = itemView.findViewById(R.id.txt_infection_name);
        }
    }
}
