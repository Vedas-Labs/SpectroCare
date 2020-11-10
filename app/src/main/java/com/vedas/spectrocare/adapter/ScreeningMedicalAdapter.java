package com.vedas.spectrocare.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vedas.spectrocare.Controllers.MedicalScreeningServerObjectDataController;
import com.vedas.spectrocare.DataBase.MedicalScreeningRecordDataController;
import com.vedas.spectrocare.DataBaseModels.MedicalScreeningRecordModel;
import com.vedas.spectrocare.DataBaseModels.ScreeningRecordModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.activities.ScreeningRecordForMedicalActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class ScreeningMedicalAdapter extends RecyclerView.Adapter<ScreeningMedicalAdapter.ScreeningMedicalViewHolder> {
    Context context;
    List<ScreeningRecordModel> screeningMedicalRecordList;

    public ScreeningMedicalAdapter(Context context, List<ScreeningRecordModel> screeningMedicalRecordList) {
        this.context = context;
        this.screeningMedicalRecordList = screeningMedicalRecordList;
    }

    @Override
    public ScreeningMedicalAdapter.ScreeningMedicalViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.screening_record_item,parent,false);
        return new ScreeningMedicalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ScreeningMedicalAdapter.ScreeningMedicalViewHolder holder, final int position) {
        MedicalScreeningRecordModel objModel=MedicalScreeningRecordDataController.getInstance().allMedicalScreeningList.get(position);
        holder.dateOfCreationId.setText(objModel.getScreeningID());
        String array[]=objModel.getAttachment().split("/");
        holder.SceeningDisc.setText(array[array.length-1]);
        holder.imgLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context wrapper = new ContextThemeWrapper(context, R.style.MyPopupOtherStyle);
                PopupMenu popup = new PopupMenu(wrapper,  holder.imgLayout);
                popup.getMenuInflater().inflate(R.menu.popup_menu,popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("View")){
                            MedicalScreeningRecordDataController.getInstance().currentMedicalScreenigRecordModel=MedicalScreeningRecordDataController.getInstance().allMedicalScreeningList.get(position);
                            if(MedicalScreeningRecordDataController.getInstance().currentMedicalScreenigRecordModel.getAttachment()!=null){
                                // loadAttachUrlImage(ScreeningRecordDataController.getInstance().allScreeningList.get(position));
                                loadAttachUrlImage(MedicalScreeningRecordDataController.getInstance().allMedicalScreeningList.get(position));
                            }
                        }else{
                            MedicalScreeningRecordDataController.getInstance().currentMedicalScreenigRecordModel=MedicalScreeningRecordDataController.getInstance().allMedicalScreeningList.get(position);
                            deleteMedicalScreeningData();

                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
       // holder.SceeningDisc.setText(objModel.getMoreInfo());

      /*  holder.deleteImgScreening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedicalScreeningRecordDataController.getInstance().currentMedicalScreenigRecordModel=MedicalScreeningRecordDataController.getInstance().allMedicalScreeningList.get(position);
                deleteMedicalScreeningData();
            }
        });
        holder.viewImgScreening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedicalScreeningRecordDataController.getInstance().currentMedicalScreenigRecordModel=MedicalScreeningRecordDataController.getInstance().allMedicalScreeningList.get(position);
                if(MedicalScreeningRecordDataController.getInstance().currentMedicalScreenigRecordModel.getAttachment()!=null){
                   // loadAttachUrlImage(ScreeningRecordDataController.getInstance().allScreeningList.get(position));
                    loadAttachUrlImage(MedicalScreeningRecordDataController.getInstance().allMedicalScreeningList.get(position));
                }
            }
        });*/

    }

    @Override
    public int getItemCount() {

        if(MedicalScreeningRecordDataController.getInstance().allMedicalScreeningList.size()>0){
            return MedicalScreeningRecordDataController.getInstance().allMedicalScreeningList.size();
        }else {
            return 0;
        }
    }

    public class ScreeningMedicalViewHolder extends RecyclerView.ViewHolder {
        TextView dateOfCreationId,SceeningDisc;
        RelativeLayout popUpLayout,imgLayout,colorLayout;
        ImageView viewImgScreening,attachImgScreening,deleteImgScreening;
        public ScreeningMedicalViewHolder(@NonNull View itemView) {
            super(itemView);
            dateOfCreationId = itemView.findViewById(R.id.txtid);
            SceeningDisc = itemView.findViewById(R.id.txtdis);
            imgLayout = itemView.findViewById(R.id.img_layout);
            colorLayout = itemView.findViewById(R.id.color);
          /*  viewImgScreening = itemView.findViewById(R.id.screening_img_view);
            deleteImgScreening = itemView.findViewById(R.id.screening_img_delete);
            attachImgScreening = itemView.findViewById(R.id.screening_img_attach);
*/
        }
    }
    private void  loadAttachUrlImage(MedicalScreeningRecordModel dataModel){
        String url= ServerApi.img_home_url+dataModel.getAttachment();
        Log.e("notdata","call"+url);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        // intent.setDataAndType(Uri.parse(url), "*/*");
        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    private void deleteMedicalScreeningData(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        builder.setCancelable(false);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete this record");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ScreeningRecordForMedicalActivity.showingDialog.showAlert();
                MedicalScreeningServerObjectDataController.getInstance().deleteMedicalScreeningRecord(MedicalScreeningRecordDataController.getInstance().currentMedicalScreenigRecordModel);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.show();
    }

}
