package com.vedas.spectrocare.PatientModule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.AppointmentDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientAppointmentModule.PatientAppointmentsDataController;
import com.vedas.spectrocare.PatientAppointmentModule.PaymentDetailsModel;
import com.vedas.spectrocare.PatinetControllers.CardDetailsController;
import com.vedas.spectrocare.PatinetControllers.PatientAppointmentController;
import com.vedas.spectrocare.PatinetControllers.PaymentControll;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.LoginActivity;
import com.vedas.spectrocare.model.CardDetailsModel;
import com.vedas.spectrocare.model.PaymentModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class PaymentStatusActivity extends AppCompatActivity {
Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status);
        loginBtn=findViewById(R.id.btn_login_now);
        Log.e("linkakaaa","gfff");
        appointmentAddApi();
        accessInterface();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  appointmentAddApi();
                startActivity(new Intent(PaymentStatusActivity.this,PatientAppointmentsTabsActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }
    public void appointmentAddApi(){

        Log.e("linkaka","dkkk1");
        JSONObject addObject = new JSONObject();
        JSONObject paymentDetaildObject = new JSONObject();
        PaymentDetailsModel paymentDetails = new PaymentDetailsModel();

        try {
            addObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
            addObject.put("appointmentDate",PatientAppointmentController.getInstance().getAppointmentList().get(0).getDate());
            addObject.put("appointmentTime",PatientAppointmentController.getInstance().getAppointmentList().get(0).getTimeSlot());
            addObject.put("appointmentDuration",PatientAppointmentController.getInstance().getAppointmentList().get(0).getTime());
            addObject.put("visitType",PatientAppointmentController.getInstance().getAppointmentList().get(0).getAppointmentStatus());
            addObject.put("doctorName",PatientAppointmentController.getInstance().getAppointmentList().get(0).getDocName());
            addObject.put("doctorMedicalPersonnelID", PaymentControll.getInstance().getPaymentModel().getDocID());
            addObject.put("patientName",PatientLoginDataController.getInstance().currentPatientlProfile.getFirstName()+" "+PatientLoginDataController.getInstance().currentPatientlProfile.getLastName());
            addObject.put("patientID",PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            addObject.put("department",PatientAppointmentController.getInstance().getAppointmentList().get(0).getSpecialization());
            addObject.put("reasonForVisit",PatientAppointmentController.getInstance().getAppointmentList().get(0).getReason());
            addObject.put("medicalRecordID",PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalPerson_id());
            addObject.put("byWhom","Patient");
            addObject.put("byWhomID",PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            paymentDetaildObject.put("paymentCard",PatientAppointmentController.getInstance().getAppointmentList().get(0).getCardNo());
            paymentDetaildObject.put("paymentDate",String.valueOf(Calendar.getInstance().getTimeInMillis()));
            paymentDetaildObject.put("txnStatus","Paypal");
            paymentDetaildObject.put("txnAmount","3.00");
            paymentDetaildObject.put("txnCurrency","USD");
            paymentDetaildObject.put("paymentMode","Paypal");
            paymentDetaildObject.put("txnStatus","success");
            /*JsonParser paymentParse = new JsonParser();
            JsonObject paymentObject = (JsonObject) paymentParse.parse(paymentDetaildObject.toString());*/
            addObject.put("paymentDetails",paymentDetaildObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(addObject.toString());
        Log.e("sendJJJ","data"+gsonObject.toString());
        ApiCallDataController.getInstance().loadjsonApiCall(
                ApiCallDataController.getInstance().serverJsonApi.
                        addPatientAppointment(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(),gsonObject), "addAppointment");

    }
    public void accessInterface(){
        Log.e("linkaka","dkkk2");

        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String opetation) {
                    if (opetation.equals("addAppointment")){
                        Log.e("addAppointment","status");

                    }
            }

            @Override
            public void failureCallBack(String failureMsg) {
                Log.e("addAppointment","error");
            }
        });
    }
}
