package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vedas.spectrocare.PatientModule.InvoiceDetailsActivity;
import com.vedas.spectrocare.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BillingAndPaymentAdapter extends RecyclerView.Adapter<BillingAndPaymentAdapter.BillingHolder> {
    Context context;

    public BillingAndPaymentAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public BillingAndPaymentAdapter.BillingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View billingView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_billing_payment, parent, false);
        return new BillingHolder(billingView);
    }

    @Override
    public void onBindViewHolder(@NonNull BillingAndPaymentAdapter.BillingHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, InvoiceDetailsActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class BillingHolder extends RecyclerView.ViewHolder {
        public BillingHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
