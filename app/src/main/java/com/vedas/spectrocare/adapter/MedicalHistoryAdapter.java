package com.vedas.spectrocare.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vedas.spectrocare.Controllers.IllnessServerObjectDataController;
import com.vedas.spectrocare.DataBase.IllnessDataController;
import com.vedas.spectrocare.DataBaseModels.IllnessRecordModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.MedicalHistoryActivity;
import com.vedas.spectrocare.activities.MedicationRecordActivity;
import com.vedas.spectrocare.activities.ScreeningRecordForMedicalActivity;
import com.vedas.spectrocare.activities.SurgicalRecordActivity;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class MedicalHistoryAdapter extends RecyclerView.Adapter<MedicalHistoryAdapter.MedicalHistoryHolder> {
    EditText edtSymptoms, edtCurrentStatus, edtDescription;
    String discrip, status, symptoms;
    ImageView imgView;
    public static BottomSheetDialog dialog;
    ArrayList<IllnessRecordModel> medicalHistoryModelList;
    Context context;
    public MedicalHistoryAdapter(Context context, ArrayList<IllnessRecordModel> medicalHistoryModelList) {
        this.context = context;
        this.medicalHistoryModelList = medicalHistoryModelList;
    }
    @Override
    public MedicalHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View medicalHistoryView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medical_hystory, parent, false);
        return new MedicalHistoryHolder(medicalHistoryView);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(@NonNull final MedicalHistoryHolder holder, final int position) {
        holder.txtSymptoms.setText(medicalHistoryModelList.get(position).getSymptoms());
        holder.currentStatus.setText(medicalHistoryModelList.get(position).getCurrentStatus());
        holder.medicalDescription.setText(medicalHistoryModelList.get(position).getMoreInfo());

        holder.btnMedicals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IllnessDataController.getInstance().currentIllnessRecordModel=medicalHistoryModelList.get(position);
                Intent allMedicalRecordsIntent = new Intent(context, MedicationRecordActivity.class);
                context.startActivity(allMedicalRecordsIntent);
            }
        });
        holder.btnSergicals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IllnessDataController.getInstance().currentIllnessRecordModel=medicalHistoryModelList.get(position);
                Intent surgicalRecordIntent = new Intent(context, SurgicalRecordActivity.class);
                context.startActivity(surgicalRecordIntent);
            }
        });
        holder.btnScreening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IllnessDataController.getInstance().currentIllnessRecordModel=medicalHistoryModelList.get(position);
                Intent medicalScreeningRecordIntent = new Intent(context, ScreeningRecordForMedicalActivity.class);
                context.startActivity(medicalScreeningRecordIntent);
            }
        });

        holder.viewImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IllnessDataController.getInstance().currentIllnessRecordModel=medicalHistoryModelList.get(position);
                alertDailog();
            }
        });
        holder.editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IllnessDataController.getInstance().currentIllnessRecordModel=medicalHistoryModelList.get(position);
                alertDailog();
            }
        });
        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                builder.setCancelable(false);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete this record ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        IllnessDataController.getInstance().currentIllnessRecordModel = medicalHistoryModelList.get(position);
                        if (isConn()) {
                            MedicalHistoryActivity.showingDialog.showAlert();
                            IllnessServerObjectDataController.getInstance().deleteIllnessJsonParams(IllnessDataController.getInstance().currentIllnessRecordModel);
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface alertDialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }
    @Override
    public int getItemCount() {
        if (IllnessDataController.getInstance().allIllnesList.size() > 0) {
            return IllnessDataController.getInstance().allIllnesList.size();
        } else {
            return 0;
        }
    }
    public class MedicalHistoryHolder extends RecyclerView.ViewHolder {
        TextView txtSymptoms,currentStatus,medicalDescription,createdBy,createdDate;
        ImageView viewImg,editImg,deleteImg;
        Button btnMedicals,btnScreening,btnSergicals;

        public MedicalHistoryHolder(@NonNull View itemView) {
            super(itemView);
            txtSymptoms = itemView.findViewById(R.id.edt_medical_record_symptoms);
            currentStatus = itemView.findViewById(R.id.txt_current_status);
            medicalDescription = itemView.findViewById(R.id.medical_description);
            btnMedicals = itemView.findViewById(R.id.btn_medicals);
            btnScreening = itemView.findViewById(R.id.btn_screenings);
            btnSergicals = itemView.findViewById(R.id.btn_surgicals);
          /*  createdBy = itemView.findViewById(R.id.txt_name_of_creator);
            createdDate = itemView.findViewById(R.id.txt_date);
*/
            viewImg = itemView.findViewById(R.id.img_view);
            deleteImg = itemView.findViewById(R.id.img_delete);
            editImg = itemView.findViewById(R.id.img_edit);
           }
    }
    public boolean isConn() {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void alertDailog(){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.medical_record_bottpm_sheet, null);
        dialog = new BottomSheetDialog(Objects.requireNonNull(context),R.style.BottomSheetDialogTheme);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        imgView= dialog.findViewById(R.id.img_close);
        edtSymptoms = dialog.findViewById(R.id.edt_symptoms);
        edtCurrentStatus = dialog.findViewById(R.id.edt_current_status);
        edtDescription = dialog.findViewById(R.id.edt_add_medical_description);
        Button addMedicalDetails = dialog.findViewById(R.id.btn_add_medical_record);
        Button updateMedicalDetails = dialog.findViewById(R.id.btn_update_medical_record);
        if (IllnessDataController.getInstance().currentIllnessRecordModel != null){
            IllnessRecordModel objModel=IllnessDataController.getInstance().currentIllnessRecordModel;
            edtCurrentStatus.setText(objModel.getCurrentStatus());
            Log.e("discc",""+objModel.getMoreInfo());
            edtDescription.setText(objModel.getMoreInfo());
            edtSymptoms.setText(objModel.getSymptoms());
            addMedicalDetails.setVisibility(View.GONE);
            updateMedicalDetails.setVisibility(View.VISIBLE);
        }
        updateMedicalDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                symptoms = edtSymptoms.getText().toString();
                discrip = edtDescription.getText().toString();
                status= edtCurrentStatus.getText().toString();
                IllnessRecordModel recordModel=IllnessDataController.getInstance().currentIllnessRecordModel;
                recordModel.setSymptoms(symptoms);
                recordModel.setMoreInfo(discrip);
                recordModel.setCurrentStatus(status);
                recordModel.setIsCurrentIllness("true");
                if (isConn()) {
                    MedicalHistoryActivity.showingDialog.showAlert();
                    IllnessServerObjectDataController.getInstance().updateIllnessJsonParams(recordModel);
                }
            }
        });
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
