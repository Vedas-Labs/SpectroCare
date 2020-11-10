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
import com.vedas.spectrocare.Controllers.FamilyRecordServerDataController;
import com.vedas.spectrocare.DataBase.FamilyHistoryDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBaseModels.FamilyHistoryModel;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApiModel.FamilyHistory;
import com.vedas.spectrocare.activities.FamilyRecordActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

/*
public class FamilyRecordAdapter extends RecyclerView.Adapter<FamilyRecordAdapter.FamilyRecordHolder>   {

  Context context;
  ArrayList<FamilyHistoryModel> familyRecordModelList;

    public FamilyRecordAdapter(Context context, ArrayList<FamilyHistoryModel> familyRecordModelList) {
        this.context = context;
        this.familyRecordModelList = familyRecordModelList;
    }
    @Override
    public FamilyRecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View familyView = LayoutInflater.isFromMedicalPerson(parent.getContext()).inflate(R.layout_boarder.family_recycler_item,parent,false);
        return new FamilyRecordHolder(familyView);
    }

    @Override
    public void onBindViewHolder(@NonNull final FamilyRecordHolder holder, final int position) {
        final FamilyHistoryModel familyRecordModel = familyRecordModelList.get(position);
        holder.medicalIssue.setText(familyRecordModel.getMedicalCondition());
        holder.memberAge.setText(familyRecordModel.getAge());
        holder.familyDisc.setText(familyRecordModel.getDiscription());
        holder.relationshipTxt.setText(familyRecordModel.getRelation());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FamilyHistoryDataController.getInstance().currentFamilyHistoryModel=familyRecordModelList.get(position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                builder.setCancelable(false);
                builder.setTitle("Delete");
                builder.setMessage("Do you want to delete this Record ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FamilyHistoryDataController.getInstance().currentFamilyHistoryModel=familyRecordModelList.get(position);
                        FamilyHistoryModel objModel=familyRecordModelList.get(position);
                       JSONObject params = new JSONObject();
                        try {
                            params.put("hospital_reg_num", MedicalProfileDataController.getInstance().currentMedicalProfile.getHospital_reg_number());
                            params.put("medical_personnel_id", objModel.getMedicalPersonId());
                            params.put("patientID", objModel.getPatientId());
                            params.put("medical_record_id",objModel.getMedicalRecordId());
                            params.put("family_history_record_id",objModel.getFamilyRecordId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JsonParser jsonParser = new JsonParser();
                        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
                        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
                        if(isConn()) {
                            FamilyRecordActivity.showingDialog.showAlert();
                            ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.deleteFamilyHistory1(
                                    MedicalProfileDataController.getInstance().currentMedicalProfile.getAccessToken(), gsonObject),"delete");

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
   */
/* public void delete(int position) {
        familyRecordModelList.remove(position);
        notifyItemRemoved(position);
    }*//*


    @Override
    public int getItemCount() {
        return familyRecordModelList.size();
    }

    public class FamilyRecordHolder extends RecyclerView.ViewHolder {
        TextView medicalIssue,relationshipTxt,memberAge,familyDisc;
        public FamilyRecordHolder(@NonNull View itemView) {
            super(itemView);
            medicalIssue = itemView.findViewById(R.id.medical_issue);
            relationshipTxt = itemView.findViewById(R.id.relationship);
            familyDisc = itemView.findViewById(R.id.family_description);
            memberAge = itemView.findViewById(R.id.family_member_age);

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
