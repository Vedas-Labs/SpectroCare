package com.vedas.spectrocare.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.AllergyDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBaseModels.AllergieModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.AllergiesActivity;

import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

/*
public class AllergyRecordAdapter extends RecyclerView.Adapter<AllergyRecordAdapter.AllergyRecordHolder> {
    Context context;

    public AllergyRecordAdapter(Context context) {
        this.context = context;
    }

    @Override
    public AllergyRecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View allergyView = LayoutInflater.isFromMedicalPerson(parent.getContext()).inflate(R.layout_boarder.item_allergy_record, parent, false);
        return new AllergyRecordHolder(allergyView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AllergyRecordHolder holder, final int position) {

        AllergieModel allergieModel=AllergyDataController.getInstance().allAllergyList.get(position);

        holder.allergyDescription.setText(allergieModel.getAllergyInfo());
        //holder.createdBy.setText(allergieModel.get());
        holder.allergyDescription.setText(allergieModel.getAllergyInfo());
        holder.medicalPersonId.setText(allergieModel.getMedicalPersonId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllergyDataController.getInstance().currentAllergieModel = AllergyDataController.getInstance().allAllergyList.get(position);
               */
/* LayoutInflater inflater = LayoutInflater.isFromMedicalPerson(context);
                View view = inflater.inflate(R.layout_boarder.allergy_record_alert, null);
                Dialog dialog = new Dialog(context);
                dialog.setContentView(view);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                Button btnAdd = dialog.findViewById(R.id.btn_add_allergy_details);
                btnAdd.setVisibility(View.GONE);
                Button  btnUpdate = dialog.findViewById(R.id.btn_update_allergy_details);
                btnUpdate.setVisibility(View.VISIBLE);
                EditText aboutAllergy = dialog.findViewById(R.id.edt_add_description);*//*


            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                builder.setCancelable(false);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete");

                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AllergyDataController.getInstance().currentAllergieModel = AllergyDataController.getInstance().allAllergyList.get(position);
                        AllergieModel objModel = AllergyDataController.getInstance().allAllergyList.get(position);
                        JSONObject params = new JSONObject();
                        try {
                            params.put("hospital_reg_num", objModel.getHospitalRegNum());
                            params.put("medical_personnel_id", objModel.getMedicalPersonId());
                            params.put("patientID", objModel.getPatientId());
                            params.put("medical_record_id", objModel.getMedicalRecordId());
                            params.put("allergy_record_id", objModel.getAllergyRecordId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JsonParser jsonParser = new JsonParser();
                        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
                        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
                        if (isConn()) {
                            AllergiesActivity.showingDialog.showAlert();
                            ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.deleteAllergyHistory(
                                    MedicalProfileDataController.getInstance().currentMedicalProfile.getAccessToken(), gsonObject), "delete");

                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        if(AllergyDataController.getInstance().allAllergyList.size()>0){
            return AllergyDataController.getInstance().allAllergyList.size();
        }else {
            return 0;
        }
    }

    public class AllergyRecordHolder extends RecyclerView.ViewHolder {
        TextView allergyDescription,createdBy,medicalPersonId,createdDate;
        public AllergyRecordHolder(@NonNull View itemView) {
            super(itemView);
            allergyDescription = itemView.findViewById(R.id.txt_allergy_description);
            createdBy = itemView.findViewById(R.id.txt_name_of_creator);
            createdDate = itemView.findViewById(R.id.txt_date);
            medicalPersonId = itemView.findViewById(R.id.txt_id_of_creator);
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
}
*/
