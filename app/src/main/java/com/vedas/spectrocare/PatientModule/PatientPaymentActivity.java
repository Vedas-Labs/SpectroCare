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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.Location.LocationTracker;
import com.vedas.spectrocare.PatientAppointmentModule.PaymentDetailsModel;
import com.vedas.spectrocare.PatientServerApiModel.InvoiceModel;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.PatinetControllers.CardDetailsController;
import com.vedas.spectrocare.PatinetControllers.PatientAppointmentController;
import com.vedas.spectrocare.PatinetControllers.PaymentControll;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.ServerApiModel.RetrofitInstance;
import com.vedas.spectrocare.activities.HomeActivity;
import com.vedas.spectrocare.model.AppointmetModel;
import com.vedas.spectrocare.model.Config;
import com.vedas.spectrocare.model.DoctorsItemModel;

import com.vedas.spectrocare.patientModuleAdapter.PatientPaymentAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PatientPaymentActivity extends AppCompatActivity {
    TextView txtAddPayment, txtAppointmentType, txtDate, txtAmount, txtTime;
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
    String strEmail, strCardNo, formattedDate;
    TextView dcoName, docSpecial, txtReason;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_payment);

        appointmentList = new ArrayList<>();
        intial();
        loadPaymentData();
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
        strEmail = "";
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

        txtAddPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientPaymentActivity.this, AddPaymentMethodAactivity.class));
            }
        });
        strCardNo = patientPaymentAdapter.getSelectrdPosition();
        Log.e("strCardNo", "afa" + strCardNo);

        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strCardNo = patientPaymentAdapter.getSelectrdPosition();
                appointmetModel = new AppointmetModel();
                Log.e("strgg", "sdf" + strCardNo);
                appointmetModel.setCardNo(strCardNo);
                appointmetModel.setAppointmentStatus(PaymentControll.getInstance().currentPaymentModel.getAppointmentType() + " Consultation");
                appointmetModel.setDocName(PaymentControll.getInstance().currentPaymentModel.getDocName());
                appointmetModel.setSpecialization(PaymentControll.getInstance().currentPaymentModel.getDocProfession());
                appointmetModel.setTimeSlot(PaymentControll.getInstance().currentPaymentModel.getTimeSlot());
                appointmetModel.setReason(PaymentControll.getInstance().currentPaymentModel.getReasonForVisit());
                appointmetModel.setTime(PaymentControll.getInstance().currentPaymentModel.getDuration());
                appointmetModel.setDate(PaymentControll.getInstance().currentPaymentModel.getFormattedDate());
                appointmetModel.setApprove("Confirmed");
                appointmentList.add(appointmetModel);
                if (PatientAppointmentController.isNull()) {
                    PatientAppointmentController.getInstance().setAppointmentList(appointmentList);
                } else {
                    PatientAppointmentController.getInstance().getAppointmentList().add(appointmetModel);
                }
                // if(!strCardNo.isEmpty())
                processPayment();
                // startActivity(new Intent(PatientPaymentActivity.this, PaymentStatusActivity.class));
            }
        });
    }

    private void loadPaymentData() {
        if (PaymentControll.getInstance().currentPaymentModel != null) {
            txtAppointmentType.setText(PaymentControll.getInstance().currentPaymentModel.getAppointmentType() + " Consultation");
            txtTime.setText(PaymentControll.getInstance().currentPaymentModel.getTimeSlot() + " | " +
                    PaymentControll.getInstance().currentPaymentModel.getDuration());
            txtReason.setText(PaymentControll.getInstance().currentPaymentModel.getReasonForVisit());
            txtAmount.setText("Total Amount " + PaymentControll.getInstance().currentPaymentModel.getCurrency() + " " + PaymentControll.getInstance().currentPaymentModel.getServiceCost());
            dcoName.setText(PaymentControll.getInstance().currentPaymentModel.getDocName());
            docSpecial.setText(PaymentControll.getInstance().currentPaymentModel.getDocProfession());
            long ll = Long.parseLong(PaymentControll.getInstance().currentPaymentModel.getFormattedDate());
            Date currentDate = new Date(ll);
            SimpleDateFormat jdff = new SimpleDateFormat("yyyy-MM-dd");
            jdff.setTimeZone(TimeZone.getDefault());
            String java_date = jdff.format(currentDate);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            SimpleDateFormat parseFormat = new SimpleDateFormat("dd EEEE , MMMM ");
            sdf.setTimeZone(TimeZone.getDefault());
            Date clickedDate = null;
            try {
                clickedDate = jdff.parse(java_date);

                if (i == 0) {

                    formattedDate = parseFormat.format(clickedDate);
                    // formattedDate = sdf.format(clickedDate);
                    Log.e("forrr", "ff" + PaymentControll.getInstance().currentPaymentModel.getCurrency());

                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            txtDate.setText(formattedDate);
        }
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    private void processPayment() {
        if (PaymentControll.getInstance().currentPaymentModel != null) {
            Log.e("zzzzzzz", "ff" + PaymentControll.getInstance().currentPaymentModel.getCurrency()+PaymentControll.getInstance().currentPaymentModel.getServiceCost());

            PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal("40.00"),
                 //   new BigDecimal(String.valueOf(PaymentControll.getInstance().currentPaymentModel.getServiceCost())),
                 "USD"  /* PaymentControll.getInstance().currentPaymentModel.getCurrency()*/,
                    "Appointment Payment", PayPalPayment.PAYMENT_INTENT_SALE);
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
            startActivityForResult(intent, PAYPAL_REQUEST_CODE);
        }
    }

    public void recylerview() {
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
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        Log.e("paymentDetails", "call" + paymentDetails.toString());
                        if (PatientMedicalRecordsController.getInstance().invoiceObject != null) {
                            Log.e("updateInvoiceApi", "call");
                            updateInvoiceApi();
                        } else {
                            Log.e("appointmentAddApi", "call");
                            appointmentAddApi();
                        }
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

    private void updateInvoiceApi() {
        Log.e("updateinvoiceapi", "onResponse: call");
        Date currentDate = Calendar.getInstance().getTime();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ServerApi s = retrofit.create(ServerApi.class);
        PatientMedicalRecordsController.getInstance().invoiceObject.setByWhom("admin");
        PatientMedicalRecordsController.getInstance().invoiceObject.setByWhomID("viswanath3344");
        PatientMedicalRecordsController.getInstance().invoiceObject.getClientDetails().setClientID("viswanath3344");
        PatientMedicalRecordsController.getInstance().invoiceObject.setPaymentStatus("paid");
        PatientMedicalRecordsController.getInstance().invoiceObject.setInvoicePaymentDueDate(String.valueOf(currentDate.getTime()));

        Call<InvoiceModel> call = s.invoiceUpdateApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), PatientMedicalRecordsController.getInstance().invoiceObject);
        call.enqueue(new Callback<InvoiceModel>() {
            @Override
            public void onResponse(Call<InvoiceModel> call, Response<InvoiceModel> responseModel) {
                if (responseModel.body() != null) {
                    Log.e("updateinvoiceapi", "onResponse: " + responseModel.message());
                    if (responseModel.body().getResponse().equals("3")) {
                        Log.e("updateinvoiceapi", "onResponse: " + responseModel.body().getMessage());
                        PatientMedicalRecordsController.getInstance().invoiceObject.setPaymentStatus("paid");
                    } else if (responseModel.body().getResponse().equals("0")) {
                        String message = responseModel.body().getMessage();
                        Log.e("updateinvoicemessage", "onResponse: " + message);
                    }
                }
            }

            @Override
            public void onFailure(Call<InvoiceModel> call, Throwable t) {
                Log.e("dobindi", "" + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    public void appointmentAddApi() {
        Log.e("linkaka", "dkkk1");
        JSONObject addObject = new JSONObject();
        JSONObject paymentDetaildObject = new JSONObject();
        //PaymentDetailsModel paymentDetails = new PaymentDetailsModel();
        try {
            addObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
            addObject.put("appointmentDate", PatientAppointmentController.getInstance().getAppointmentList().get(0).getDate());
            addObject.put("appointmentTime", PatientAppointmentController.getInstance().getAppointmentList().get(0).getTimeSlot());
            addObject.put("appointmentDuration", PatientAppointmentController.getInstance().getAppointmentList().get(0).getTime());
            addObject.put("appointmentFee",PaymentControll.getInstance().currentPaymentModel.getServiceCost());
            addObject.put("appointmentFeeUnit",PaymentControll.getInstance().currentPaymentModel.getCurrency());
            addObject.put("visitType", PatientAppointmentController.getInstance().getAppointmentList().get(0).getAppointmentStatus());
            addObject.put("doctorName", PatientAppointmentController.getInstance().getAppointmentList().get(0).getDocName());
            addObject.put("doctorMedicalPersonnelID", PaymentControll.getInstance().currentPaymentModel.getDocID());
            addObject.put("patientName", PatientLoginDataController.getInstance().currentPatientlProfile.getFirstName() + " " + PatientLoginDataController.getInstance().currentPatientlProfile.getLastName());
            addObject.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            addObject.put("department", PatientAppointmentController.getInstance().getAppointmentList().get(0).getSpecialization());
            addObject.put("reasonForVisit", PatientAppointmentController.getInstance().getAppointmentList().get(0).getReason());
            addObject.put("medicalRecordID", PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalPerson_id());
            addObject.put("byWhom", "Patient");
            addObject.put("byWhomID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            if (!strCardNo.isEmpty()) {
                paymentDetaildObject.put("paymentCard", strCardNo/*PatientAppointmentController.getInstance().getAppointmentList().get(0).getCardNo()*/);
            } else {
                paymentDetaildObject.put("paymentCard", "4213242602642202"/*PatientAppointmentController.getInstance().getAppointmentList().get(0).getCardNo()*/);
            }
            paymentDetaildObject.put("paymentDate", String.valueOf(System.currentTimeMillis()/*String.valueOf(Calendar.getInstance().getTimeInMillis()*/));
            paymentDetaildObject.put("txnStatus", "Paypal");
            paymentDetaildObject.put("txnAmount", "3.00");
            paymentDetaildObject.put("txnCurrency", "USD");
            paymentDetaildObject.put("paymentMode", "Paypal");
            paymentDetaildObject.put("txnStatus", "success");
            /*JsonParser paymentParse = new JsonParser();
            JsonObject paymentObject = (JsonObject) paymentParse.parse(paymentDetaildObject.toString());*/
            addObject.put("paymentDetails", paymentDetaildObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(addObject.toString());
        Log.e("sendJJJ", "data" + gsonObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(
                ApiCallDataController.getInstance().serverJsonApi.
                        addPatientAppointment(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "addAppointment");
    }
    public void accessInterface() {
        Log.e("linkaka", "dkkk2");

        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String opetation) {
                if (opetation.equals("addAppointment")) {
                    Log.e("addAppointment", "status");

                }
            }
            @Override
            public void failureCallBack(String failureMsg) {
                Log.e("addAppointment", "error");
            }
        });
    }
}
