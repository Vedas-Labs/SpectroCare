package com.vedas.spectrocare.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

import com.vedas.spectrocare.Controllers.ScreeningServerObjectDataController;
import com.vedas.spectrocare.DataBase.ScreeningRecordDataController;
import com.vedas.spectrocare.DataBaseModels.PhysicalExamsDataModel;
import com.vedas.spectrocare.DataBaseModels.ScreeningRecordModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.activities.ScreeningRecordActivity;
import com.vedas.spectrocare.activities.VaccinationRecordActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ScreeningRecordAdapter extends RecyclerView.Adapter<ScreeningRecordAdapter.ScreeningRecordHolder> {
    Context context;
    ArrayList<Integer> colorsArray = new ArrayList<>();
    List<ScreeningRecordModel> screeningRecordList;

    public ScreeningRecordAdapter(Context context, List<ScreeningRecordModel> screeningRecordList, ArrayList<Integer> colorsArray1) {
        this.context = context;
        this.screeningRecordList = screeningRecordList;
        this.colorsArray=colorsArray1;
    }
    @NonNull
    @Override
    public ScreeningRecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.screening_record_item,parent,false);
        return new ScreeningRecordHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ScreeningRecordHolder holder, final int position) {
         ScreeningRecordModel objModel=ScreeningRecordDataController.getInstance().allScreeningList.get(position);
         holder.txtID.setText(objModel.getScreeningID());
        String array[]=objModel.getAttachment().split("/");
        holder.txtDis.setText(array[array.length-1]);
         //holder.txtDis.setText(objModel.getMoreInfo());
        holder.colorLayout.setBackgroundColor(colorsArray.get(position));
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
                             ScreeningRecordDataController.getInstance().currentScreeningRecordModel=ScreeningRecordDataController.getInstance().allScreeningList.get(position);
                             if(ScreeningRecordDataController.getInstance().currentScreeningRecordModel.getAttachment()!=null){
                                 loadAttachUrlImage(ScreeningRecordDataController.getInstance().allScreeningList.get(position));
                             }
                         }else{
                             ScreeningRecordDataController.getInstance().currentScreeningRecordModel=ScreeningRecordDataController.getInstance().allScreeningList.get(position);
                             deleteScreeningData();

                         }
                             return true;
                     }
                 });
                 popup.show();
             }
         });
       /*  holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
             }
         });*/
         holder.txtView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ScreeningRecordDataController.getInstance().currentScreeningRecordModel=ScreeningRecordDataController.getInstance().allScreeningList.get(position);
                 if(ScreeningRecordDataController.getInstance().currentScreeningRecordModel.getAttachment()!=null){
                     loadAttachUrlImage(ScreeningRecordDataController.getInstance().allScreeningList.get(position));
                 }
             }
         });
         holder.txtDelete.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ScreeningRecordDataController.getInstance().currentScreeningRecordModel=ScreeningRecordDataController.getInstance().allScreeningList.get(position);
                 deleteScreeningData();

             }
         });
/*
         holder.deleteImgScreening.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ScreeningRecordDataController.getInstance().currentScreeningRecordModel=ScreeningRecordDataController.getInstance().allScreeningList.get(position);
                 deleteScreeningData();
             }
         });
*/

/*
        holder.viewImgScreening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreeningRecordDataController.getInstance().currentScreeningRecordModel=ScreeningRecordDataController.getInstance().allScreeningList.get(position);
                if(ScreeningRecordDataController.getInstance().currentScreeningRecordModel.getAttachment()!=null){
                    loadAttachUrlImage(ScreeningRecordDataController.getInstance().allScreeningList.get(position));
                }
            }
        });
*/
    }
    private void  loadAttachUrlImage(ScreeningRecordModel dataModel){
        String url= ServerApi.img_home_url+dataModel.getAttachment();
        Log.e("notdata","call"+url);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        // intent.setDataAndType(Uri.parse(url), "*/*");
        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        if(ScreeningRecordDataController.getInstance().allScreeningList.size()>0){
            return ScreeningRecordDataController.getInstance().allScreeningList.size();
        }else {
            return 0;
        }
    }
    public class ScreeningRecordHolder extends RecyclerView.ViewHolder {
        TextView txtID,txtDis,txtDelete,txtView;
        ImageView viewImgScreening,attachImgScreening,deleteImgScreening;
        RelativeLayout popUpLayout,imgLayout,colorLayout;
        public ScreeningRecordHolder(@NonNull View itemView) {
            super(itemView);
            txtID = itemView.findViewById(R.id.txtid);
            txtDis = itemView.findViewById(R.id.txtdis);
            txtView = itemView.findViewById(R.id.txt_view);
            txtDelete = itemView.findViewById(R.id.txt_delete);
            popUpLayout = itemView.findViewById(R.id.pop_up_layout);
            imgLayout = itemView.findViewById(R.id.img_layout);
            colorLayout = itemView.findViewById(R.id.color);

           /* viewImgScreening = itemView.findViewById(R.id.screening_img_view);
            deleteImgScreening = itemView.findViewById(R.id.screening_img_delete);
            attachImgScreening = itemView.findViewById(R.id.screening_img_attach);
*/
        }
    }
    private void deleteScreeningData(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        builder.setCancelable(false);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete this record");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ScreeningRecordActivity.showingDialog.showAlert();
                ScreeningServerObjectDataController.getInstance().deleteScreeningRecord( ScreeningRecordDataController.getInstance().currentScreeningRecordModel);
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
