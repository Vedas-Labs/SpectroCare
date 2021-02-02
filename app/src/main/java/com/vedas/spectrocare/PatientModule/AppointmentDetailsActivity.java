package com.vedas.spectrocare.PatientModule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.vedas.spectrocare.PatientAppointmentModule.AppointmentArrayModel;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.PatinetControllers.PatientAppointmentController;
import com.vedas.spectrocare.PatinetControllers.PaymentControll;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.model.AppointmetModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class AppointmentDetailsActivity extends AppCompatActivity {
    ImageView imgBack;
    CircularImageView imgCircle;
    ImageView imgUp, imgDown;
    TextView txtCancelReason,txt_reason;
    RelativeLayout rl_reason;
    int i;
    boolean down;
    String formattedDate;
    TextView txtDocName, txtDocSpec, txtDate, textTimings, txtApprove, txtPaid, txtCardNo, txtConsultent, txtReasonForVisit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);
        casting();
        setData();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void casting() {
        down = true;
        imgCircle = findViewById(R.id.img);
        rl_reason=findViewById(R.id.rl_reason);
        txt_reason=findViewById(R.id.cancel_reason);
     /*   Intent intent = getIntent();
        if (intent.hasExtra("position"))
            position = Integer.parseInt(intent.getStringExtra("position"));
        if (!intent.getStringExtra("docPic").isEmpty())
            Picasso.get().load(intent.getStringExtra("docPic")).placeholder(R.drawable.image).into(imgCircle);
        Log.e("dsfasf", "df" + position);*/
        imgBack = findViewById(R.id.img_back);
        txtDocName = findViewById(R.id.txt_doc_name);
        imgDown = findViewById(R.id.img_down_arrow);
        txtCancelReason = findViewById(R.id.txt_cancel_reason);
        txtConsultent = findViewById(R.id.txt_consultation);
        txtReasonForVisit = findViewById(R.id.txt_reason_for_visit);
        txtDocSpec = findViewById(R.id.txt_profession);
        txtDate = findViewById(R.id.txt_formate);
        textTimings = findViewById(R.id.txt_timings);
        txtApprove = findViewById(R.id.txt_approve);
        txtPaid = findViewById(R.id.txt_status);
        txtCardNo = findViewById(R.id.txt_card_no);

        imgDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (down) {
                    down = false;
                    imgDown.setImageResource(R.drawable.up);
                    rl_reason.setVisibility(View.VISIBLE);

                } else {
                    down = true;
                    imgDown.setImageResource(R.drawable.down_1);
                    rl_reason.setVisibility(View.GONE);

                }
            }
        });
    }
    public void setData() {
       // Intent in = getIntent();
       // if (in.hasExtra("sampleObject")) {
        if (PatientMedicalRecordsController.getInstance().selectedappointmnetModel!=null) {
            // AppointmetModel dene = (AppointmetModel)in.getSerializableExtra("sampleObject");
            //AppointmentArrayModel appointmentArrayModel = (AppointmentArrayModel) in.getSerializableExtra("sampleObject");
            AppointmentArrayModel appointmentArrayModel =PatientMedicalRecordsController.getInstance().selectedappointmnetModel;

            if (appointmentArrayModel.getAppointmentDetails().getAppointmentDate().contains("/")) {
                txtDate.setText(appointmentArrayModel.getAppointmentDetails().getAppointmentDate());

            } else {
                Picasso.get().load(ServerApi.img_home_url+appointmentArrayModel.getDoctorDetails().getProfile().getUserProfile().getProfilePic()).placeholder(R.drawable.image).into(imgCircle);
                long ll = Long.parseLong(appointmentArrayModel.getAppointmentDetails().getAppointmentDate());
                Date currentDate = new Date(ll);
                SimpleDateFormat jdff = new SimpleDateFormat("yyyy-MM-dd");
                jdff.setTimeZone(TimeZone.getDefault());
                String java_date = jdff.format(currentDate);
                Log.e("cccc", "afa" + java_date);
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                SimpleDateFormat parseFormat = new SimpleDateFormat("dd EEEE , MMMM ");
                sdf.setTimeZone(TimeZone.getDefault());
                Date clickedDate = null;
                try {
                    clickedDate = jdff.parse(java_date);

                    if (i == 0) {

                        formattedDate = parseFormat.format(clickedDate);
                        // formattedDate = sdf.format(clickedDate);
                        Log.e("forrr", "ff" + formattedDate);
                        txtDate.setText(formattedDate);

                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            // Log.e("model","reason "+dene.getReason());
            txtReasonForVisit.setText(appointmentArrayModel.getAppointmentDetails().getReasonForVisit());
            txtConsultent.setText(appointmentArrayModel.getAppointmentDetails().getVisitType());
            txtDocName.setText(appointmentArrayModel.getDoctorDetails().getProfile().getUserProfile().getFirstName() + " "
                    + appointmentArrayModel.getDoctorDetails().getProfile().getUserProfile().getLastName());
            txtDocSpec.setText(appointmentArrayModel.getDoctorDetails().getProfile().getUserProfile().getDepartment());
            txtApprove.setText(appointmentArrayModel.getAppointmentDetails().getAppointmentStatus());
            txtCardNo.setText(appointmentArrayModel.getAppointmentDetails().getPaymentDetails().getPaymentMode());
            textTimings.setText(appointmentArrayModel.getAppointmentDetails().getAppointmentTime()
                    + " | " + appointmentArrayModel.getAppointmentDetails().getAppointmentDuration());
            if (appointmentArrayModel.getAppointmentDetails().getAppointmentStatus().equals("Cancelled")) {
                imgDown.setVisibility(View.VISIBLE);
                //rl_reason.setVisibility(View.VISIBLE);
                txt_reason.setText(appointmentArrayModel.getAppointmentDetails().getReasonForCancellation());
            } else {
                imgDown.setVisibility(View.GONE);
               // rl_reason.setVisibility(View.GONE);
            }
        }
       /* txtReasonForVisit.setText(PatientAppointmentController.getInstance().getAppointmentList().get(position).getReason());
        txtConsultent.setText(PatientAppointmentController.getInstance().getAppointmentList().get(position).getAppointmentStatus());
        txtDocName.setText(PatientAppointmentController.getInstance().getAppointmentList().get(position).getDocName());
        txtDocSpec.setText(PatientAppointmentController.getInstance().getAppointmentList().get(position).getSpecialization());
        txtApprove.setText(PatientAppointmentController.getInstance().getAppointmentList().get(position).getApprove());
        txtCardNo.setText(PatientAppointmentController.getInstance().getAppointmentList().get(position).getCardNo());
        textTimings.setText(PatientAppointmentController.getInstance().getAppointmentList().get(position).getTimeSlot()
        +" | "+PatientAppointmentController.getInstance().getAppointmentList().get(position).getTime());*/
    }
}
