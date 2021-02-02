package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBase.PhysicalExamDataController;
import com.vedas.spectrocare.DataBase.PhysicalExamTrackInfoDataController;
import com.vedas.spectrocare.DataBaseModels.PhysicalExamsDataModel;
import com.vedas.spectrocare.DataBaseModels.PhysicalTrackInfoModel;
import com.vedas.spectrocare.PatientModule.PatientPhysicalRecordActivity;
import com.vedas.spectrocare.PatientServerApiModel.FamilyTrackingInfo;
import com.vedas.spectrocare.PatinetControllers.PatientFamilyDataController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.ServerApiModel.PhysicalExamServerObject;
import com.vedas.spectrocare.activities.PhysicalExamActivity;
import com.vedas.spectrocare.activities.PhysicalExamRecordActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PatientPhysicalAdapter extends RecyclerView.Adapter<PatientPhysicalAdapter.PatientPhysicalHolder> {
    Context context;
    String clockTime="";
    ImageView imgDelete;
    ArrayList<Integer> colorsArray;
    ArrayList<PhysicalExamDataController> patientPhysicalList;

    public PatientPhysicalAdapter(Context context, ArrayList<Integer> colorsArray, ImageView imgDelete) {
        this.context = context;
        this.colorsArray = colorsArray;
        this.imgDelete = imgDelete;

    }
/*
    public PatientPhysicalAdapter(Context context, ArrayList<Integer> colorsArray) {
        this.context = context;
        this.colorsArray = colorsArray;
    }*/


    @NonNull
    @Override
    public PatientPhysicalAdapter.PatientPhysicalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_physical_exam,parent,false);
        return new PatientPhysicalHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientPhysicalAdapter.PatientPhysicalHolder holder, int position) {
        ArrayList<FamilyTrackingInfo> trackingInfos = PatientFamilyDataController.getInstance().getPhysicalRecordModel()
                .getPhysical_exam_records().get(position).getTracking();
       /* ArrayList<PhysicalTrackInfoModel> trackInfoModels=
                PhysicalExamTrackInfoDataController.getInstance().fetchPhysicalExamBasedOnPhysicalExamId(physicalExamsDataModel);*/
        if(trackingInfos.size()>0){
            try {
                FamilyTrackingInfo trackInfoModel=trackingInfos.get(trackingInfos.size()-1);
                String date[]= PersonalInfoController.getInstance().convertTimestampToslashFormate(trackInfoModel.getDate());
                String entredDate = trackInfoModel.getDate();
                Log.e("string","date"+entredDate);
                long l = Long.parseLong(entredDate);
                Date currentDate = new Date(l);
                SimpleDateFormat jdff = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                jdff.setTimeZone(TimeZone.getDefault());
                String java_date = jdff.format(currentDate);
                Date clickedDate = null;
                try {
                    clickedDate = jdff.parse(java_date);

                    String  formattedDate = jdff.format(clickedDate);
                    Log.e("forrr","ff"+formattedDate);
                    String[] two = formattedDate.split(" ");
                    //load settings date formate to date feild.
                    String value = PersonalInfoController.getInstance().loadSettingsDataFormateToEntireApp(context,entredDate);
                    holder.txt_date.setText(value);
                    //
                    String[] timeSplit = two[1].split(":");
                    if (12 < Integer.parseInt(timeSplit[0])){
                        int hr = Integer.parseInt(timeSplit[0])-12;
                        clockTime = String.valueOf(hr)+":"+timeSplit[1]+"PM";
                    }else{
                        clockTime = two[1]+"AM";
                    }
                    holder.txt_time.setText(clockTime);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                    holder.txt_first_name.setText(trackInfoModel.getByWhomName());
                Log.e("trackingdate","call"+date[0]+date[1]+date[2]);
                holder.txt_on.setText(" on");
                /*holder.txt_date.setText(date[0]);
                holder.txt_time.setText(date[1]+" "+date[2]);
*/
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        holder.colorLayout.setBackgroundColor(colorsArray.get(position));
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context wrapper = new ContextThemeWrapper(context, R.style.NoPopupAnimation);
                PopupMenu popup = new PopupMenu(wrapper, holder.more);
                // PopupMenu popup = new PopupMenu(wrapper, holder.more);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                // Context wrapper = new ContextThemeWrapper(context, R.style.MyPopupOtherStyle);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getTitle().equals("View")) {
                            if(PatientFamilyDataController.getInstance().getPhysicalRecordModel()!=null){
                               if(PatientFamilyDataController.getInstance().getPhysicalRecordModel().getPhysical_exam_records()
                                       .get(position).getAttachment()!=null){
                                   String url= ServerApi.img_home_url+PatientFamilyDataController.getInstance().getPhysicalRecordModel().getPhysical_exam_records()
                                           .get(position).getAttachment();
                                   Log.e("notdata","call"+url);
                                   Intent intent = new Intent(Intent.ACTION_VIEW);

                                   intent.setData(Uri.parse(url));
                                   // intent.setDataAndType(Uri.parse(url), "*/*");
                                   // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                   context.startActivity(intent);
                               }else{
                                   int k = holder.getAdapterPosition();
                                   String position = String.valueOf(k);
                                   context.startActivity(new Intent(context, PatientPhysicalRecordActivity.class)
                                           .putExtra("position",position));
                               }
                            }
                           /* PhysicalExamDataController.getInstance().currentPhysicalExamsData = allPhysicalExamList.get(position);
                            if (PhysicalExamDataController.getInstance().currentPhysicalExamsData.getAttachment() != null) {
                                PhysicalExamDataController.getInstance().currentPhysicalExamsData = allPhysicalExamList.get(position);
                                loadAttachUrlImage(allPhysicalExamList.get(position));
                            } else {
                                Intent patientProfileIntent = new Intent(context, PhysicalExamRecordActivity.class);
                                startActivity(patientProfileIntent);
                            }*/
                        }else {
                            //PhysicalExamDataController.getInstance().currentPhysicalExamsData=allPhysicalExamList.get(position);
                            deleteRecord(holder.getAdapterPosition());
                        }
                        return true;
                    }

                });
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (PatientFamilyDataController.getInstance().getPhysicalRecordModel()!=null){
            imgDelete.setVisibility(View.VISIBLE);
          return PatientFamilyDataController.getInstance().getPhysicalRecordModel().getPhysical_exam_records().size();
        }else{
            imgDelete.setVisibility(View.GONE);
            return 0;
        }

    }

    public class PatientPhysicalHolder extends RecyclerView.ViewHolder {
        TextView txt_first_name,txt_on,txt_date,txt_time;
        ImageView viewImg,editImg,deleteImg,attachImg;
        RelativeLayout colorLayout,more;
        public PatientPhysicalHolder(@NonNull View itemView) {
            super(itemView);
            txt_first_name = itemView.findViewById(R.id.txt_first_name);
            txt_on = itemView.findViewById(R.id.txt_on);
            colorLayout = itemView.findViewById(R.id.color);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_time = itemView.findViewById(R.id.txt_time);
            viewImg = itemView.findViewById(R.id.img_view);
            deleteImg = itemView.findViewById(R.id.img_delete);
            editImg = itemView.findViewById(R.id.img_edit);
            attachImg = itemView.findViewById(R.id.img_attach);
            more = itemView.findViewById(R.id.more_layout);

        }
    }
    public void deleteRecord(int p){
        RefreshShowingDialog alertDilogue =new RefreshShowingDialog(context);
        alertDilogue.showAlert();
        PhysicalExamServerObject examServerObject = new PhysicalExamServerObject();
        examServerObject.setPatientID(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
        examServerObject.setMedical_record_id(PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
        examServerObject.setHospital_reg_num(PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
        examServerObject.setByWhomID(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
        examServerObject.setByWhom("patient");
        examServerObject.setPhysical_exam_id(PatientFamilyDataController.getInstance().getPhysicalRecordModel()
                .getPhysical_exam_records().get(p).getPhysical_exam_id());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerApi.home_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerApi serverApi = retrofit.create(ServerApi.class);
        Call<PhysicalExamServerObject> callDelete=serverApi.deletePhysicalExamHistory(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(),examServerObject);
        callDelete.enqueue(new Callback<PhysicalExamServerObject>() {
            @Override
            public void onResponse(Call<PhysicalExamServerObject> call, Response<PhysicalExamServerObject> response) {
               Log.e("deleeee","te"+response.body());
               alertDilogue.hideRefreshDialog();
                if (response.body().getResponse().equals("3")){
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    PatientFamilyDataController.getInstance().getPhysicalRecordModel().getPhysical_exam_records().remove(p);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<PhysicalExamServerObject> call, Throwable t) {
                alertDilogue.hideRefreshDialog();
                Log.e("eree","fa"+t.getMessage());
            }
        });
    }
}
