package com.vedas.spectrocare.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBase.TrackInfoDataController;
import com.vedas.spectrocare.DataBaseModels.PatientlProfileModel;
import com.vedas.spectrocare.DataBaseModels.TrackInfoModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.ServerApiModel.TrackingServerObject;
import com.vedas.spectrocare.activities.CategoryActivity;
import com.vedas.spectrocare.activities.MedicalRecordActivity;
import com.vedas.spectrocare.activities.MyPatientActivity;
import com.vedas.spectrocare.activities.PatientGeneralProfileActivity;
import com.vedas.spectrocare.model.PatientList;
import com.vedas.spectrocare.ServerApiModel.PatientsDeleteModel;
import com.google.gson.JsonElement;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> implements Filterable {

    Context context;
    List<PatientlProfileModel> patientList;
    List<PatientList> patientListFull;
    MyPatientActivity myPatientActivity = new MyPatientActivity();

    public PatientAdapter(Context context, ArrayList<PatientlProfileModel> patientList) {
        this.context = context;
        this.patientList = patientList;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient, parent, false);

        return new PatientViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(final PatientViewHolder holder, final int position) {

        final PatientlProfileModel patientList1 = patientList.get(position);
        String finstName = patientList1.getFirstName();
        String lastName = patientList1.getLastName();
        String name = finstName+" "+lastName;
        holder.PatientName.setText(name);
        holder.PatientAge.setText(patientList1.getAge());
       // holder.PatientLastName.setText(patientList1.getLastName());
        holder.PatientId.setText(patientList1.getPatientId());
        holder.PatientSex.setText(patientList1.getGender());
        holder.txtMedicalId.setText(patientList1.getMedicalPerson_id());

        ArrayList<TrackInfoModel> trackInfoModels=TrackInfoDataController.getInstance().fetchTrackData(patientList1);
       if(trackInfoModels.size()>0){
           try {
               TrackInfoModel trackInfoModel=trackInfoModels.get(trackInfoModels.size()-1);
            //   String date=PersonalInfoController.getInstance().convertTimestampToMinutes(trackInfoModel.getDate());
               String date[]= PersonalInfoController.getInstance().convertTimestampToslashFormate(trackInfoModel.getDate());
               String sourceString = "<b>" + trackInfoModel.getByWhom() + "</b> " + "on " +"<b>"+ date[0]+"</b>";
               holder.txt_first_name.setText(Html.fromHtml(sourceString));
               holder.txt_time.setText(date[1]+" "+date[2]);
           } catch (ParseException e) {
               e.printStackTrace();
           }
       }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientProfileDataController.getInstance().currentPatientlProfile=patientList.get(position);
                Log.e("deleteRes", "" + PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
                Log.e("deleteRes", "" + PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
                Log.e("deleteRes", "" + PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
                Log.e("deleteRes", "" + PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalPerson_id());

                Intent patientProfileIntent = new Intent(context, CategoryActivity.class);
                context.startActivity(patientProfileIntent);
            }
        });

        holder.viewImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientProfileDataController.getInstance().currentPatientlProfile=patientList.get(position);
                Intent patientProfileIntent = new Intent(context, CategoryActivity.class);
                context.startActivity(patientProfileIntent);
            }
        });
        holder.editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientProfileDataController.getInstance().currentPatientlProfile=patientList.get(position);
                Intent patientProfileIntent = new Intent(context, CategoryActivity.class);
                context.startActivity(patientProfileIntent);
            }
        });
        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientProfileDataController.getInstance().currentPatientlProfile=patientList.get(position);
                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                builder.setCancelable(false);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isConn()) {
                            Objects.requireNonNull(MyPatientActivity.alertDialog.getWindow()).setLayout(600, 500);
                            MyPatientActivity.alertDialog.show();
                            Log.d("deleteReqBody", "" + PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
                            Log.d("deleteReqBody", "" + MedicalProfileDataController.getInstance().currentMedicalProfile.getHospital_reg_number());
                            Log.d("deleteReqBody", "" + MedicalProfileDataController.getInstance().currentMedicalProfile.getMedical_person_id());
                            try {
                                Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                                        .addConverterFactory(GsonConverterFactory.create()).build();
                                ServerApi delateApi = retrofit.create(ServerApi.class);
                                Map<String, String> deleteModelMap = new HashMap<>();
                                deleteModelMap.put("medical_personnel_id", MedicalProfileDataController.getInstance().currentMedicalProfile
                                        .getMedical_person_id());
                                deleteModelMap.put("hospital_reg_num", MedicalProfileDataController.getInstance().currentMedicalProfile
                                        .getHospital_reg_number());
                                deleteModelMap.put("patientID", PatientProfileDataController.getInstance().currentPatientlProfile
                                        .getPatientId());
                                Log.d("deleteReqBody", "" + deleteModelMap.toString());
                                Call<PatientsDeleteModel> call = delateApi.delete(MedicalProfileDataController.getInstance().currentMedicalProfile.getAccessToken(), deleteModelMap);
                                call.enqueue(new Callback<PatientsDeleteModel>() {
                                    @Override
                                    public void onResponse(Call<PatientsDeleteModel> call, Response<PatientsDeleteModel> response) {
                                        String resp = response.message();
                                        Log.e("deleteRes", "" + resp);
                                        if (response.body().getResponse() == "3") {
                                            MyPatientActivity.alertDialog.dismiss();
                                            Toast.makeText(context, resp, Toast.LENGTH_SHORT).show();
                                            PatientProfileDataController.getInstance().deletePatientData(patientList.get(position));
                                          /*  ((Activity) (context)).finish();
                                            context.startActivity(new Intent(context, MyPatientActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));*/
                                            myPatientActivity.fetchServerApi();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<PatientsDeleteModel> call, Throwable t) {
                                        t.printStackTrace();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PatientProfileDataController.getInstance().currentPatientlProfile=patientList.get(position);
                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                builder.setCancelable(false);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isConn()) {
                            MyPatientActivity.alertDialog.show();
                            Log.d("deleteReqBody", "" + PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
                            Log.d("deleteReqBody", "" + MedicalProfileDataController.getInstance().currentMedicalProfile.getHospital_reg_number());
                            Log.d("deleteReqBody", "" + MedicalProfileDataController.getInstance().currentMedicalProfile.getMedical_person_id());
                            try {
                                Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApi.home_url)
                                        .addConverterFactory(GsonConverterFactory.create()).build();
                                ServerApi delateApi = retrofit.create(ServerApi.class);
                                Map<String, String> deleteModelMap = new HashMap<>();
                                deleteModelMap.put("medical_personnel_id", MedicalProfileDataController.getInstance().currentMedicalProfile
                                        .getMedical_person_id());
                                deleteModelMap.put("hospital_reg_num", MedicalProfileDataController.getInstance().currentMedicalProfile
                                        .getHospital_reg_number());
                                deleteModelMap.put("patientID", PatientProfileDataController.getInstance().currentPatientlProfile
                                        .getPatientId());
                                Log.d("deleteReqBody", "" + deleteModelMap.toString());
                                Call<PatientsDeleteModel> call = delateApi.delete(MedicalProfileDataController.getInstance().currentMedicalProfile.getAccessToken(), deleteModelMap);
                                call.enqueue(new Callback<PatientsDeleteModel>() {
                                    @Override
                                    public void onResponse(Call<PatientsDeleteModel> call, Response<PatientsDeleteModel> response) {
                                        Log.e("deleteRes", "" + response.body().getResponse());
                                        String resp = response.body().getMessage();
                                        if (response.body().getResponse().equals("3")) {
                                            MyPatientActivity.alertDialog.dismiss();
                                            Log.e("adfssa","afadf");
                                            Toast.makeText(context, resp, Toast.LENGTH_SHORT).show();
                                            PatientProfileDataController.getInstance().deletePatientData(patientList.get(position));
                                            ((Activity) (context)).finish();
                                            context.startActivity(new Intent(context, MyPatientActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<PatientsDeleteModel> call, Throwable t) {
                                        MyPatientActivity.alertDialog.dismiss();
                                        Toast.makeText(context, "Please check your network connection", Toast.LENGTH_SHORT).show();
                                        t.printStackTrace();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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
       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent patientProfileIntent = new Intent(context, CategoryActivity.class);
                PatientProfileDataController.getInstance().currentPatientlProfile=patientList.get(position);
                context.startActivity(patientProfileIntent);
            }
        });*/

        String url = patientList1.getProfilePic();
        Picasso.get().load(patientList1.getProfilePic()).placeholder(R.drawable.ic_human_dummy3x).into(holder.PatientProfile);

    }

    public void delete(int position) {
        patientList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {

        if(patientList.size()>0){
            return patientList.size();
        }else{
            return 0;
        }
    }

    public class PatientViewHolder extends RecyclerView.ViewHolder {
        TextView PatientName, PatientId, PatientAge, PatientSex, MedicalRecord, PatientLastName;
        public LinearLayout frontLayout;
        CircularImageView PatientProfile;
        TextView txt_first_name,txt_on,txt_date,txt_time,txtPatientId,txtMedicalId;
        ImageView viewImg,editImg,deleteImg,attachImg;
        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            PatientName = itemView.findViewById(R.id.patient_name);
          //  PatientLastName = itemView.findViewById(R.id.patient_last_name);
            PatientAge = itemView.findViewById(R.id.patient_age);
            PatientId = itemView.findViewById(R.id.patient_id);
            MedicalRecord = itemView.findViewById(R.id.medical_record);
            PatientSex = itemView.findViewById(R.id.sex);
            txtMedicalId = itemView.findViewById(R.id.txt_medical_personnel_id);
            frontLayout = itemView.findViewById(R.id.image_layout);
            PatientProfile = itemView.findViewById(R.id.patient_imv);

            txt_first_name = itemView.findViewById(R.id.txt_first_name);
            txt_on = itemView.findViewById(R.id.txt_on);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_time = itemView.findViewById(R.id.txt_time);

            viewImg = itemView.findViewById(R.id.img_view);
            deleteImg = itemView.findViewById(R.id.img_delete);
            editImg = itemView.findViewById(R.id.img_edit);
            attachImg = itemView.findViewById(R.id.img_attach);
        }
    }

    @Override
    public Filter getFilter() {
        return patientFilter;
    }

    private Filter patientFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<PatientList> filtratedList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filtratedList.addAll(patientListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (PatientList item : patientListFull) {
                    if (item.getFirstName().toLowerCase().contains(filterPattern)) {
                        filtratedList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtratedList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            patientList.clear();
            patientList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
    public boolean isConn() {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
    }
}
