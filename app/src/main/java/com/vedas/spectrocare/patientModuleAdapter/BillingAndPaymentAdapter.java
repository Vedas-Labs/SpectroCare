package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientModule.InvoiceDetailsActivity;
import com.vedas.spectrocare.PatientServerApiModel.InvoiceModel;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        InvoiceModel invoiceModel = PatientMedicalRecordsController.getInstance().invoiceList.get(position);
        holder.txt_invoice.setText(invoiceModel.getInvoiceNumber());
        holder.txt_complete.setText(invoiceModel.getPaymentStatus());
        holder.txt_money.setText(invoiceModel.getTotalAmount()+" $");
        loadTimeDateToLabel(holder,invoiceModel);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientMedicalRecordsController.getInstance().invoiceObject = PatientMedicalRecordsController.getInstance().invoiceList.get(holder.getAdapterPosition());
                context.startActivity(new Intent(context, InvoiceDetailsActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (PatientMedicalRecordsController.getInstance().invoiceList.size() > 0) {
            return PatientMedicalRecordsController.getInstance().invoiceList.size();
        } else {
            return 0;
        }
    }

    public class BillingHolder extends RecyclerView.ViewHolder {
        public final View mView;
        TextView txt_invoice, txt_complete, txt_money, txt_month, txt_timing;

        public BillingHolder(View view) {
            super(view);
            mView = view;
            txt_invoice = view.findViewById(R.id.txt_invoice);
            txt_complete = view.findViewById(R.id.txt_complete);
            txt_money = view.findViewById(R.id.txt_money);
            txt_month = view.findViewById(R.id.txt_month);
            txt_timing = view.findViewById(R.id.txt_timing);
        }
    }

    private void loadTimeDateToLabel(BillingHolder holder,InvoiceModel obj) {
        try {
            String array[] = PersonalInfoController.getInstance().invoiceTimestampToslashFormate(obj.getInvoicePaymentDueDate());
            holder.txt_month.setText(array[0] + " " + array[1]);
            holder.txt_timing.setText(array[2] + " " + array[3]);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
    private void updateInvoiceApi(){
        Log.e("updateinvoiceapi", "onResponse: call");

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ServerApi s = retrofit.create(ServerApi.class);
        PatientMedicalRecordsController.getInstance().invoiceObject.setByWhom("admin");
        PatientMedicalRecordsController.getInstance().invoiceObject.setByWhomID("viswanath3344");
        PatientMedicalRecordsController.getInstance().invoiceObject.getClientDetails().setClientID("viswanath3344");

        Call<InvoiceModel> call = s.invoiceUpdateApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(),PatientMedicalRecordsController.getInstance().invoiceObject);
        call.enqueue(new Callback<InvoiceModel>() {
            @Override
            public void onResponse(Call<InvoiceModel> call, Response<InvoiceModel> responseModel) {
                if (responseModel.body() != null) {
                        Log.e("updateinvoiceapi", "onResponse: " + responseModel.message());
                        if (responseModel.body().getResponse().equals("3")) {
                            Log.e("updateinvoiceapi", "onResponse: " + responseModel.body().getMessage());
                            PatientMedicalRecordsController.getInstance().invoiceObject.setPaymentStatus("paid");
                        } else if (responseModel.body().getResponse().equals("0")) {
                            String message =responseModel.body().getMessage();
                            Log.e("updateinvoicemessage", "onResponse: " + message);
                        }
                }
            }

            @Override
            public void onFailure(Call<InvoiceModel> call, Throwable t) {
                //  Toast.makeText(LoginActivity.this,"Please check your internet connection", Toast.LENGTH_SHORT).show();
                Log.e("dobindi", "" + t.getMessage());
                t.printStackTrace();
            }
        });
    }
}
