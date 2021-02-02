package com.vedas.spectrocare.PatientModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.PatientServerApiModel.InvoiceModel;
import com.vedas.spectrocare.PatientServerApiModel.InvoiceServiceItemsModel;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.PatinetControllers.PaymentControll;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.model.PaymentModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class InvoiceDetailsActivity extends AppCompatActivity {
    TextView txt_invoiceno, txt_invoiceDate, txt_dueDate, txt_totalAmt, txt_totaltopamt, txt_paidon;
    ImageView imgBack;
    Button btn_pay;
    BillingAndPaymentAdapter billingAdapter;
    androidx.recyclerview.widget.RecyclerView billingRecyclerview;
    RelativeLayout layout_total_paid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_details);
        imgBack = findViewById(R.id.back_img);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loadRecyclerView();
        loadIds();
    }

    private void loadRecyclerView() {
        billingRecyclerview = findViewById(R.id.billing_recycle);
        billingAdapter = new BillingAndPaymentAdapter(getApplicationContext());
        billingRecyclerview.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(getApplicationContext()));
        billingRecyclerview.setAdapter(billingAdapter);
    }

    private void loadIds() {
        txt_invoiceno = findViewById(R.id.invno);
        txt_invoiceDate = findViewById(R.id.invoicedate);
        txt_dueDate = findViewById(R.id.txt_due_date);
        txt_totalAmt = findViewById(R.id.txt_totla_fee);
        layout_total_paid = findViewById(R.id.layout_total_paid);
        btn_pay = findViewById(R.id.btn_pay);
        txt_totaltopamt = findViewById(R.id.total_amt);
        txt_paidon = findViewById(R.id.paid_on);

        if (PatientMedicalRecordsController.getInstance().invoiceObject != null) {
            InvoiceModel obj = PatientMedicalRecordsController.getInstance().invoiceObject;
            txt_invoiceno.setText(obj.getInvoiceNumber());
            txt_totalAmt.setText(obj.getTotalAmount());
            try {
                txt_invoiceDate.setText(PersonalInfoController.getInstance().invoiceTimestampToDate(obj.getInvoiceIssueDate()));
                txt_dueDate.setText(PersonalInfoController.getInstance().invoiceTimestampToDate(obj.getInvoicePaymentDueDate()));

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (obj.getPaymentStatus().toLowerCase().contains("unpaid")) {
                layout_total_paid.setVisibility(View.GONE);
                btn_pay.setVisibility(View.VISIBLE);
            } else {
                layout_total_paid.setVisibility(View.VISIBLE);
                btn_pay.setVisibility(View.GONE);
                txt_totaltopamt.setText(obj.getTotalAmount());
                try {
                    String date = PersonalInfoController.getInstance().invoiceTimestampToDate(obj.getInvoicePaymentDueDate());
                    txt_paidon.setText("Paid on "+date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            loadServiceItemObject(obj.getServiceItems().get(0));

            btn_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), PatientPaymentActivity.class));
                }
            });
        }

    }

    private void loadServiceItemObject(InvoiceServiceItemsModel model) {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat jdff = new SimpleDateFormat("yyyy/MM/dd");
        jdff.setTimeZone(TimeZone.getDefault());
        //String date = jdff.format(currentDate);

        PaymentModel paymentModel = new PaymentModel();
        paymentModel.setAppointmentType("");
        paymentModel.setDuration("");
        paymentModel.setTimeSlot("");
        paymentModel.setDocName("");
        paymentModel.setDocID("");
        paymentModel.setReasonForVisit("Invoice Payment");
        paymentModel.setDocProfession("");
        paymentModel.setFormattedDate(String.valueOf(currentDate.getTime()));
        if (model != null) {
            paymentModel.setServiceCost(model.getServiceGrossCost());
            paymentModel.setCurrency("USD");
            paymentModel.setServiceVATPercent(model.getServiceVAT());
            paymentModel.setServiceID(model.getServiceID());

        }
        PaymentControll.getInstance().currentPaymentModel = paymentModel;

    }

    public class BillingAndPaymentAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<BillingAndPaymentAdapter.BillingHolder> {
        Context context;

        public BillingAndPaymentAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public BillingAndPaymentAdapter.BillingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View billingView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_servises_invoice, parent, false);
            return new BillingHolder(billingView);
        }

        @Override
        public void onBindViewHolder(@NonNull BillingAndPaymentAdapter.BillingHolder holder, int position) {
            InvoiceServiceItemsModel invoiceModel = PatientMedicalRecordsController.getInstance().invoiceObject.getServiceItems().get(position);
            holder.txt_servicename.setText(invoiceModel.getServiceName());
            holder.txt_amount.setText(invoiceModel.getServiceGrossCost());
            //   loadTimeDateToLabel(holder,invoiceModel);
        }

        @Override
        public int getItemCount() {
            if (PatientMedicalRecordsController.getInstance().invoiceObject.getServiceItems().size() > 0) {
                return PatientMedicalRecordsController.getInstance().invoiceObject.getServiceItems().size();
            } else {
                return 0;
            }
        }

        public class BillingHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            public final View mView;
            TextView txt_servicename, txt_amount;

            public BillingHolder(View view) {
                super(view);
                mView = view;
                txt_servicename = view.findViewById(R.id.txt_servicename);
                txt_amount = view.findViewById(R.id.txt_amount);

            }
        }
       /* private void loadTimeDateToLabel(BillingHolder holder,InvoiceModel obj) {
            try {
                String array[] = PersonalInfoController.getInstance().invoiceTimestampToslashFormate(obj.getInvoicePaymentDueDate());
                holder.txt_month.setText(array[0] + " " + array[1]);
                holder.txt_timing.setText(array[2] + " " + array[3]);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }*/
    }

}
