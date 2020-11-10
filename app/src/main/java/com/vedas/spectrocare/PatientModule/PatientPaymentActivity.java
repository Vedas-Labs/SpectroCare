package com.vedas.spectrocare.PatientModule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.vedas.spectrocare.PatinetControllers.CardDetailsController;
import com.vedas.spectrocare.PatinetControllers.PatientAppointmentController;
import com.vedas.spectrocare.PatinetControllers.PaymentControll;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.model.AppointmetModel;
import com.vedas.spectrocare.model.Config;
import com.vedas.spectrocare.model.DoctorsItemModel;

import com.vedas.spectrocare.patientModuleAdapter.PatientPaymentAdapter;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PatientPaymentActivity extends AppCompatActivity {
    TextView txtAddPayment,txtAppointmentType,txtDate,txtAmount,txtTime;
    RecyclerView paymentRecycleView;
    ArrayList patientPaymentList;
    Button btnPayNow;
    private static final int PAYPAL_REQUEST_CODE = 7777;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);
    PatientPaymentAdapter patientPaymentAdapter;
    ImageView imgBack, imgDown;
    ArrayList<AppointmetModel> appointmentList;
    AppointmetModel appointmetModel;
    String  strEmail, strCardNo,formattedDate;
    TextView dcoName,docSpecial,txtReason;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_payment);

        appointmentList = new ArrayList<>();
        intial();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentRecycleView.setVisibility(View.VISIBLE);
                txtAddPayment.setVisibility(View.VISIBLE);
            }
        });

    }

    public void intial() {
        strEmail ="";
        patientPaymentList = new ArrayList();
        txtReason = findViewById(R.id.txt_consultation);
        imgBack = findViewById(R.id.img_back_arrow);
        dcoName = findViewById(R.id.txt_doc_name);
        docSpecial = findViewById(R.id.txt_doc_profession);
        imgDown = findViewById(R.id.img_arrow_down);
        btnPayNow = findViewById(R.id.btn_pay_now);
        txtDate = findViewById(R.id.txt_date);
        txtAmount = findViewById(R.id.txt_amount);
        txtTime = findViewById(R.id.txt_time);
        txtAppointmentType = findViewById(R.id.txt_online);
        txtAddPayment = findViewById(R.id.txt_add_payment);
        recylerview();
        long ll = Long.parseLong(PaymentControll.getInstance().getPaymentModel().getFormattedDate());
        Date currentDate = new Date(ll);
        SimpleDateFormat jdff = new SimpleDateFormat("yyyy-MM-dd");
        jdff.setTimeZone(TimeZone.getDefault());
        String java_date = jdff.format(currentDate);
        Log.e("cccc","afa"+java_date);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat parseFormat = new SimpleDateFormat("dd EEEE , MMMM "); sdf.setTimeZone(TimeZone.getDefault());
        Date clickedDate = null;
        try {
            clickedDate = jdff.parse(java_date);

            if (i == 0) {

                formattedDate = parseFormat.format(clickedDate);
                // formattedDate = sdf.format(clickedDate);
                Log.e("forrr","ff"+formattedDate);

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        txtAppointmentType.setText(PaymentControll.getInstance().getPaymentModel().getAppointmentType() + " Consultation");
        txtDate.setText(formattedDate);
        txtTime.setText(PaymentControll.getInstance().getPaymentModel().getTimeSlot() + " | " +
                PaymentControll.getInstance().getPaymentModel().getDuration());
        txtReason.setText(PaymentControll.getInstance().getPaymentModel().getReasonForVisit());
        txtAddPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientPaymentActivity.this, AddPaymentMethodAactivity.class));
            }
        });
        if (PaymentControll.getInstance()!=null){
            dcoName.setText(PaymentControll.getInstance().getPaymentModel().getDocName());
            docSpecial.setText(PaymentControll.getInstance().getPaymentModel().getDocProfession());
        }
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strCardNo = patientPaymentAdapter.getSelectrdPosition();
                appointmetModel = new AppointmetModel();
                Log.e("strgg","sdf"+strCardNo);
                appointmetModel.setCardNo(strCardNo);
                appointmetModel.setAppointmentStatus(PaymentControll.getInstance().getPaymentModel().getAppointmentType() + " Consultation");
                appointmetModel.setDocName(PaymentControll.getInstance().getPaymentModel().getDocName());
                appointmetModel.setSpecialization(PaymentControll.getInstance().getPaymentModel().getDocProfession());
                appointmetModel.setTimeSlot(PaymentControll.getInstance().getPaymentModel().getTimeSlot());
                appointmetModel.setReason(PaymentControll.getInstance().getPaymentModel().getReasonForVisit());
                appointmetModel.setTime(PaymentControll.getInstance().getPaymentModel().getDuration());
                appointmetModel.setDate(PaymentControll.getInstance().getPaymentModel().getFormattedDate());
                appointmetModel.setApprove("Confirmed");
                appointmentList.add(appointmetModel);
                if(PatientAppointmentController.isNull()){
                    PatientAppointmentController.getInstance().setAppointmentList(appointmentList);
                }else {
                    PatientAppointmentController.getInstance().getAppointmentList().add(appointmetModel);
                }
                processPayment();
               // startActivity(new Intent(PatientPaymentActivity.this, PaymentStatusActivity.class));
            }
        });
    }
    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
    private void processPayment() {
        /*amount = edtAmount.getText().toString();
        Log.e("amountcalling","call"+amount);*/
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(10)),"USD",
                "Purchase Goods",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }

    public void recylerview(){
        paymentRecycleView = findViewById(R.id.payment_recyclerView);
        patientPaymentAdapter = new PatientPaymentAdapter(PatientPaymentActivity.this);
        paymentRecycleView.setLayoutManager(new LinearLayoutManager(PatientPaymentActivity.this));
        paymentRecycleView.setNestedScrollingEnabled(false);
        paymentRecycleView.setAdapter(patientPaymentAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode  == RESULT_OK) {
                Log.e("adfasf","dfa");
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        Log.e("paymentDetails","call"+paymentDetails.toString());
                        startActivity(new Intent(this, PaymentStatusActivity.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", "10"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
    }
}
