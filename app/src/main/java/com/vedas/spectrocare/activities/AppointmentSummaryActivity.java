package com.vedas.spectrocare.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
//import com.vedas.spectrocare.BLEConnectionModule.Constants;
import com.vedas.spectrocare.Constants;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.Controllers.AppointmentsServerObjectDataController;
import com.vedas.spectrocare.DataBase.AppointmentDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBaseModels.AppointmentModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApiModel.AppointmentServerObjects;

import org.json.JSONObject;

import java.util.Objects;

public class AppointmentSummaryActivity extends AppCompatActivity implements MedicalPersonaSignupView {
    TextView txtDate,txtTime,txtPatientId,txtPatientName,txtDoctorName,txtDivison,txtVisitType,
            txtResonToVisit,txtOtherNote;
    RelativeLayout back;
    Button btnReSchedule,btnCancel;
    CircularImageView imageView;
    BottomSheetDialog dialog;
    RefreshShowingDialog showingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_summary);
        castingViews();
        accessInterfaceMethods();
    }
    public void castingViews(){
        back = findViewById(R.id.back);
        imageView=findViewById(R.id.imv);
        btnCancel = findViewById(R.id.btn_cancel);
        btnReSchedule = findViewById(R.id.btn_re_schedule);
        txtDate = findViewById(R.id.txt_appointment_date);
        txtTime = findViewById(R.id.txt_appointment_time);
        txtPatientId = findViewById(R.id.txt_patient_id);
        txtPatientName = findViewById(R.id.txt_patient_name);
        txtDoctorName = findViewById(R.id.txt_doc_name);
        txtVisitType = findViewById(R.id.txt_visit_type);
        txtResonToVisit = findViewById(R.id.txt_reason);
        txtDivison = findViewById(R.id.txt_dprt_divison);
        txtOtherNote = findViewById(R.id.txt_status);
        showingDialog= new RefreshShowingDialog(AppointmentSummaryActivity.this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnReSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(),BookAppointmentsActivity.class));
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                alertDailog();
               /* edtReason.setVisibility(View.VISIBLE);
                titleReason.setVisibility(View.VISIBLE);*/
            }
        });
        loadCurrentAppointmentData();
    }
    private void loadCurrentAppointmentData(){
        if(AppointmentDataController.getInstance().currentAppointmentModel!=null){
            AppointmentModel obj=AppointmentDataController.getInstance().currentAppointmentModel;
            txtDate.setText(obj.getAppointmentDate());
            txtTime.setText(obj.getAppointmentTimeFrom()+" - "+obj.getAppointmentTimeTo());
            txtPatientId.setText(obj.getPatientID());
            txtPatientName.setText(obj.getPatientName());
            txtDivison.setText(obj.getDepartment());
            txtDoctorName.setText(obj.getDoctorName());
            txtVisitType.setText(obj.getVisitType());
            txtResonToVisit.setText(obj.getReasonForVisit());
            if(obj.getAppointmentStatus().contains("Waiting")){
                txtOtherNote.setText("Waiting");
            }else {
                txtOtherNote.setText(obj.getAppointmentStatus());
            }
            Picasso.get().load(obj.getDoctorProfilePic()).into(imageView);
            Log.e("status", "call"+obj.getAppointmentStatus()+obj.getVisitType());

            if(obj.getAppointmentStatus().contains(Constants.StatusTypes.Accepted.toString())){
                btnReSchedule.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.GONE);
            }else if(obj.getAppointmentStatus().contains(Constants.StatusTypes.Rejected.toString())){
                btnReSchedule.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.GONE);
            }else if(obj.getAppointmentStatus().contains(Constants.StatusTypes.Cancelled.toString())){
                btnReSchedule.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.GONE);
            }
        }
    }
     EditText aboutAllergy;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void alertDailog() {
        View view = getLayoutInflater().inflate(R.layout.allergy_record_alert, null);
        dialog = new BottomSheetDialog(Objects.requireNonNull(AppointmentSummaryActivity.this),R.style.BottomSheetDialogTheme);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();
         aboutAllergy = dialog.findViewById(R.id.edt_add_description);
        aboutAllergy.setHint("");//cancelReason
        TextView title = dialog.findViewById(R.id.title);
        title.setText("Cancel Appointment");
        TextView cancelReason = dialog.findViewById(R.id.cancelReason);
        cancelReason.setVisibility(View.VISIBLE);
        ImageView imageView=dialog.findViewById(R.id.cancel);
        ImageView edit=dialog.findViewById(R.id.img_edit);
        edit.setVisibility(View.GONE);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
       Button btnAddAllergy = dialog.findViewById(R.id.btn_add_allergy_details);
       btnAddAllergy.setText("Save");
        btnAddAllergy.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (aboutAllergy.getText().toString().isEmpty()) {
                    dialogeforCheckavilability("Error", "Please fill reason", "Ok");
                } else {
                    if (isConn()) {
                        showingDialog.showAlert();
                        AppointmentModel currentModel= AppointmentDataController.getInstance().currentAppointmentModel;
                        AppointmentServerObjects serverObjects=new AppointmentServerObjects();
                        serverObjects.setHospital_reg_num(currentModel.getHospital_reg_num());
                        serverObjects.setAppointmentID(currentModel.getAppointmentID());
                        serverObjects.setCancelledByWhom("Patient");
                        serverObjects.setCancelledPersonID(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
                        serverObjects.setCancelReason(aboutAllergy.getText().toString());
                        AppointmentsServerObjectDataController.getInstance().cancelAppointment(serverObjects);

                    } else {
                        dialogeforCheckavilability("Error", "No Internet connection", "Ok");
                    }

                }
            }
        });
    }
    private void accessInterfaceMethods() {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                 if (curdOpetaton.equals("cancel")) {
                     AppointmentDataController.getInstance().currentAppointmentModel.setCancelledByWhom("Patient");
                     AppointmentDataController.getInstance().currentAppointmentModel.setCancelledPersonID(PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
                     AppointmentDataController.getInstance().currentAppointmentModel.setCancelReason(aboutAllergy.getText().toString());
                     AppointmentDataController.getInstance().currentAppointmentModel.setAppointmentStatus("Cancelled");
                     AppointmentDataController.getInstance().updateAppointmentData( AppointmentDataController.getInstance().currentAppointmentModel);
                }
                showingDialog.hideRefreshDialog();
                dialog.dismiss();
                finish();
            }

            @Override
            public void failureCallBack(String failureMsg) {
                showingDialog.hideRefreshDialog();
                Toast.makeText(getApplicationContext(), failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void dialogeforCheckavilability(String title, String message, String ok) {
        MedicalPersonalSignupPresenter presenter = new MedicalPersonalSignupPresenter(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AppointmentSummaryActivity.this);
        presenter.dialogebox(alertBuilder, title, message, ok);

    }
    public boolean isConn() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
    }
}
