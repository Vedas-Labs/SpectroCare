package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vedas.spectrocare.PatinetControllers.CardDetailsController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.model.CardDetailsModel;
import com.vedas.spectrocare.model.DoctorsItemModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PatientPaymentAdapter extends RecyclerView.Adapter<PatientPaymentAdapter.PatientPaymentHolder> {
    Context context;
    String gg;
    ArrayList<DoctorsItemModel> patientPaymentList;
    int p=-1;

    public PatientPaymentAdapter(Context context) {
        this.context = context;
    }

    @Override
    public PatientPaymentAdapter.PatientPaymentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View paymentItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_payment_item_view, parent, false);
        return new PatientPaymentHolder(paymentItemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final PatientPaymentAdapter.PatientPaymentHolder holder, int position) {
        CardDetailsModel cardDetailsModel=CardDetailsController.getInstance().cardDetailList.get(position);
        if (cardDetailsModel.getUpiID()==null){
            holder.txtCardNum.setText( cardDetailsModel.getCardNo());
            holder.txtCardDate.setText(cardDetailsModel.getExpairDate());

        }else{
            holder.imgCardView.setImageResource(R.drawable.paypal);
            holder.txtCardNum.setText( cardDetailsModel.getUpiID());
            holder.txtCardDate.setText("");

        }
       // holder.imgCardView.setImageResource(patientPaymentList.get(position).getCategoryIcon());

        if(p==position){

            holder.btnSelect.setChecked(true);
            if (CardDetailsController.getInstance().cardDetailList.get(p).getUpiID()==null){
                 gg =CardDetailsController.getInstance().cardDetailList.get(p).getCardNo();
            }else{
                gg =CardDetailsController.getInstance().cardDetailList.get(p).getUpiID();

            }

            Log.e("selectedcard","adf"+gg);


        }else{
            holder.btnSelect.setChecked(false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(p != position){
                    p=position;
                    notifyDataSetChanged();
                }else{
                    p=-1;
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (CardDetailsController.getInstance().cardDetailList.size()>0){
            return  CardDetailsController.getInstance().cardDetailList.size();
        }else{
            return 0;
        }
    }
    public String getSelectrdPosition() {
        if (p == -1) {
            return "";
        } else {

          //  Log.e("fafd","adf"+CardDetailsController.getInstance().getCardDetailList().get(p).getCardNo());
            return gg;

        }
    }


    public class PatientPaymentHolder extends RecyclerView.ViewHolder {
        CheckBox btnSelect;
        ImageView imgCardView;
        TextView txtCardNum,txtCardDate;
        public PatientPaymentHolder(@NonNull View itemView) {
            super(itemView);
            btnSelect = itemView.findViewById(R.id.btn_check_payment);
            imgCardView = itemView.findViewById(R.id.img_card);
            txtCardDate = itemView.findViewById(R.id.txt_card_date);
            txtCardNum = itemView.findViewById(R.id.txt_card_no);
        }
    }
}
