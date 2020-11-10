package com.vedas.spectrocare.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.ServerApiModel.RecordDeleteModel;
import com.vedas.spectrocare.activities.AddMedicalRecordActivity;
import com.vedas.spectrocare.model.MedicalRecordModel;
import com.vedas.spectrocare.model.RecordList;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

/*public class MedicalRecordAdapter extends RecyclerView.Adapter<MedicalRecordAdapter.RecordViewHolder> {

    /*Context context;
    List<RecordList> recordList;
    String medicID, hosRegID, IDpatient, accessToken, paId;
    List resposnsList;

    public MedicalRecordAdapter(Context context, List<RecordList> recordList) {
        this.context = context;
        this.recordList = recordList;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.isFromMedicalPerson(parent.getContext()).inflate(R.layout.item_medic_record, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecordViewHolder holder, int position) {
        final RecordList recordList1 = recordList.get(position);
        holder.MedicalRecordId.setText(recordList1.getMedical_record_id());
        Log.e("recordID", "" + recordList1.getMedical_record_id());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("UserInfo", MODE_PRIVATE);
                String medicalPersonnelId = sharedPreferences.getString("userInfo", "");
                Log.e("ckckc",""+medicalPersonnelId);


                try {
                    JSONObject jsonObject = new JSONObject(medicalPersonnelId);
                    JSONObject medicalPersonnelJsonObject = jsonObject.getJSONObject("medicalPersonnel");
                    accessToken = jsonObject.getString("access_token");
                    medicID = medicalPersonnelJsonObject.getString("medical_personnel_id");
                    Log.e("idMedicTest", "" + medicID);
                    hosRegID = medicalPersonnelJsonObject.getString("hospital_reg_num");
                    Log.e("idHosTest", "" + hosRegID);
                    paId = recordList1.getPatientID();
                    SharedPreferences pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("patientId", paId);
                    editor.apply();
                    Log.e("jdnfj", "" + paId);
                    Log.e("idTest", "" + paId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MedicalRecordModel medicalRecordModel = new MedicalRecordModel(medicID, hosRegID, paId);
                Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                        .addConverterFactory(GsonConverterFactory.create()).build();
                ServerApi apiServer = retrofit.create(ServerApi.class);
                Call<ResponseBody> recordResponseModelCall = apiServer.setMedicalRecordList(accessToken, medicalRecordModel);
                recordResponseModelCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.body() != null) {
                            try {
                                String responseString = new String(response.body().bytes());
                                Log.e("testingList", "" + responseString);
                                SharedPreferences recordInfoSP = context.getSharedPreferences("recordInfo", MODE_PRIVATE);
                                SharedPreferences.Editor editor = recordInfoSP.edit();
                                editor.putString("record_Info", responseString);
                                editor.putString("medicRecordId", recordList1.getMedical_record_id());
                                editor.apply();
                                context.startActivity(new Intent(context, AddMedicalRecordActivity.class)
                                        .putExtra("isFromMedicalPerson","updateRecord"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(context, "No Response From Server", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                builder.setCancelable(false);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(holder.getAdapterPosition());
                        SharedPreferences sharedPreferences = context.getSharedPreferences("UserInfo", MODE_PRIVATE);
                        String userInfoString = sharedPreferences.getString("userInfo", "");
                        try {
                            JSONObject jsonObject = new JSONObject(userInfoString);
                            // the inner json object for medical personnel details
                            JSONObject medicalPersonnelJsonObject = jsonObject.getJSONObject("medicalPersonnel");
                            final String paId = recordList1.getPatientID();
                            final String medicRecordID = recordList1.getMedical_record_id();
                            final String acToken = jsonObject.getString("access_token");
                            Log.e("aca", "" + acToken);
                            final String medicalPersonnelId = medicalPersonnelJsonObject.getString("medical_personnel_id");
                            // Log.e("Hosid",""+medicalPersonnelId);
                            final String medicHospitalReg = medicalPersonnelJsonObject.getString("hospital_reg_num");
                            Log.e("Hosid", "" + medicHospitalReg);
                            Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                                    .addConverterFactory(GsonConverterFactory.create()).build();
                            ServerApi delelteMedicRecord = retrofit.create(ServerApi.class);
                           *//* RecordDeleteModel deleteModel = new RecordDeleteModel();
                            deleteModel.setPatientID(paId);
                            deleteModel.setMedical_record_id(medicRecordID);
                            deleteModel.setHospital_reg_num(medicHospitalReg);
                            deleteModel.setMedical_personnel_id(medicalPersonnelId);*//*
                            Map<String, String> deleteModelMap = new HashMap<>();
                            deleteModelMap.put("medical_personnel_id", medicalPersonnelId);
                            deleteModelMap.put("hospital_reg_num", medicHospitalReg);
                            deleteModelMap.put("patientID", paId);
                            deleteModelMap.put("medical_record_id", medicRecordID);
                            // Log.d("deleteReqBody", "" + deleteModelMap.toString());
                            Call<RecordDeleteModel> call = delelteMedicRecord.deleteRecord(acToken, deleteModelMap);
                            call.enqueue(new Callback<RecordDeleteModel>() {
                                @Override
                                public void onResponse(Call<RecordDeleteModel> call, Response<RecordDeleteModel> response) {
                                    String resp = response.message();
                                    Log.e("deleteRes", "" + resp);
                                    Toast.makeText(context, resp, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<RecordDeleteModel> call, Throwable t) {
                                    t.printStackTrace();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
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

    public void delete(int position) {
        recordList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        if (recordList != null)
            return recordList.size();
        else
            return 0;
    }

    public class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView MedicalRecordId;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            MedicalRecordId = itemView.findViewById(R.id.medicRecordId);
        }
    }*/
//}//


