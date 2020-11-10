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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vedas.spectrocare.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class UrineTestAdapter extends RecyclerView.Adapter<UrineTestAdapter.UrineTestHolder> {
    UrineTestAdapter.UrineTestHolder holder1;
    Context context;
   public boolean  testboolesn = false;
    Button btnEdit;

    public UrineTestAdapter(Context context, Button btnEdit) {
        this.context = context;
        this.btnEdit = btnEdit;
    }

    public UrineTestAdapter(Context context) {
        this.context = context;
    }


    @Override
    public UrineTestAdapter.UrineTestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View urineTestView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_recycle_view, parent, false);
        return new UrineTestHolder(urineTestView);
    }

    @Override
    public void onBindViewHolder( UrineTestAdapter.UrineTestHolder holder, int position) {
        holder1=holder;
        holder.txtNormal.setText("Normal");
        holder.txtTest.setText("Urine Routine");
        holder.imgIcon.setBackgroundResource(R.drawable.testtube);
        holder.txtNormal.setTextColor(Color.parseColor("#2DAF22"));
        holder.txtAbnormal.setVisibility(View.VISIBLE);
        if (testboolesn){
            holder.checkBox.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.VISIBLE);
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteFileDialog();
                }
            });
        }else{
            holder.checkBox.setVisibility(View.GONE);
btnEdit.setVisibility(View.GONE);
        }

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

        testboolesn=true;
        notifyDataSetChanged();
    }

    public static class UrineTestHolder extends RecyclerView.ViewHolder {
        TextView txtNormal,txtAbnormal,txtTest;
        CheckBox checkBox;
        ImageView imgIcon;
        Button viewBtn;
        public UrineTestHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.img_doc_pic);
            checkBox = itemView.findViewById(R.id.checkbox);
            txtTest = itemView.findViewById(R.id.txt_doc_name);
            txtAbnormal = itemView.findViewById(R.id.txt_Abnormal);
            txtNormal = itemView.findViewById(R.id.txt_infection_name);
        }
    }
}
