package com.vedas.spectrocare.patientModuleAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.PatientServerApiModel.IllnessScreenigRecord;
import com.vedas.spectrocare.PatinetControllers.PatientFamilyDataController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PatientFileAdapter extends RecyclerView.Adapter<PatientFileAdapter.FileHolder> {
    Context context;
    ImageView btnVisible,imgBack;
    String clockTime;
    TextView txtCancel,txtTitle;
    Button btnDelete;
    FloatingActionButton btnAddFile;
    boolean isClicked =false;
    ArrayList<IllnessScreenigRecord> recordArrayList=new ArrayList<>();

    public PatientFileAdapter(Context context, ArrayList<IllnessScreenigRecord> recordArrayList) {
        this.context = context;
        this.recordArrayList = recordArrayList;
    }

    public PatientFileAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PatientFileAdapter.FileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View fileView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file_recycle_view, parent, false);
        return new FileHolder(fileView);
    }

/*
    public PatientFileAdapter(Context context, ImageView btnVisible, ImageView imgBack, TextView txtCancel, TextView txtTitle, Button btnDelete, FloatingActionButton btnAddFile) {
        this.context = context;
        this.btnVisible = btnVisible;
        this.imgBack = imgBack;
        this.txtCancel = txtCancel;
        this.txtTitle = txtTitle;
        this.btnDelete = btnDelete;
        this.btnAddFile = btnAddFile;
    }
*/


    @Override
    public void onBindViewHolder(@NonNull final PatientFileAdapter.FileHolder holder, int position) {
        Log.e("gggdaa","adsf"+position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recordArrayList
                        .get(position).getRecordFilePath()!=null){
                    String url= ServerApi.img_home_url+recordArrayList
                            .get(position).getRecordFilePath();
                    Log.e("notdata","call"+url);
                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    intent.setData(Uri.parse(url));
                    // intent.setDataAndType(Uri.parse(url), "*/*");
                    // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            }
        });
        holder.txtByWhome.setText(recordArrayList
                .get(position).getTracking().get(0).getByWhomName());
        holder.txtFileName.setText(recordArrayList
        .get(position).getRecordName());
        try {
            String date[]= PersonalInfoController.getInstance().convertTimestampToslashFormate(PatientFamilyDataController.getInstance().getIllnessScreeningRecords()
                    .get(position).getTracking().get(0).getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String entredDate = recordArrayList
                .get(position).getTracking().get(0).getDate();
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
          //  holder.txt_date.setText(two[0]);
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

/*        if (isClicked){
            holder.btnRadio.setVisibility(View.VISIBLE);
            txtCancel.setVisibility(View.VISIBLE);
            btnVisible.setVisibility(View.GONE);
            txtTitle.setText("Delete Files");
            btnAddFile.setVisibility(View.GONE);
            holder.imgDownload.setVisibility(View.INVISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
            txtCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtTitle.setText("Files");
                    btnAddFile.setVisibility(View.VISIBLE);
                    txtCancel.setVisibility(View.INVISIBLE);
                    holder.imgDownload.setVisibility(View.VISIBLE);
                    btnVisible.setVisibility(View.VISIBLE);
                    btnDelete.setVisibility(View.INVISIBLE);
                    isClicked=false;
                    notifyDataSetChanged();
                }
            });
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtTitle.setText("Files");
                    txtCancel.setVisibility(View.INVISIBLE);
                    btnAddFile.setVisibility(View.VISIBLE);
                    btnVisible.setVisibility(View.VISIBLE);
                    holder.imgDownload.setVisibility(View.VISIBLE);
                    btnDelete.setVisibility(View.INVISIBLE);
                    isClicked=false;
                    notifyDataSetChanged();
                }
            });
        }else{
            holder.btnRadio.setVisibility(View.INVISIBLE);
            holder.imgDownload.setVisibility(View.VISIBLE);
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity)context).onBackPressed();
                }
            });
        }*/

/*
        btnVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked=true;
                notifyDataSetChanged();
            }
        });
*/

    }

    @Override
    public int getItemCount() {
        if (recordArrayList.size()>0){
            Log.e("size","array"+recordArrayList.size());
            return recordArrayList.size();
        }else{
            return 0;
        }
    }

    public class FileHolder extends RecyclerView.ViewHolder {
        TextView txtByWhome,txt_date,txt_time,txtFileName;
        ImageView imgDownload;
        public FileHolder(@NonNull View itemView) {
            super(itemView);
            txtByWhome = itemView.findViewById(R.id.txt_doc_name);
            txtFileName = itemView.findViewById(R.id.txt_file_name);
            txt_date = itemView.findViewById(R.id.txt_doc_date);
            txt_time = itemView.findViewById(R.id.txt_doc_time);
            imgDownload = itemView.findViewById(R.id.img_download);
        }
    }
}
