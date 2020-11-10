package com.vedas.spectrocare.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.Controllers.PhysicalServerObjectDataController;
import com.vedas.spectrocare.DataBase.MedicalProfileDataController;
import com.vedas.spectrocare.DataBase.PatientProfileDataController;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.AllergiesActivity;
import com.vedas.spectrocare.activities.AppointmentsActivity;
import com.vedas.spectrocare.activities.CategoryActivity;
import com.vedas.spectrocare.activities.FamilyRecordActivity;
import com.vedas.spectrocare.activities.MedicalHistoryActivity;
import com.vedas.spectrocare.activities.PatientGeneralProfileActivity;
import com.vedas.spectrocare.activities.PhysicalExamActivity;
import com.vedas.spectrocare.activities.ScreeningRecordActivity;
import com.vedas.spectrocare.activities.VaccinationRecordActivity;
import com.vedas.spectrocare.model.CategoryItemModel;
import com.vedas.spectrocare.model.RecordModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder> {
    Context context;
    ArrayList<CategoryItemModel> categoryItemList;

    public CategoryRecyclerAdapter(Context context, ArrayList<CategoryItemModel> categoryItemList) {
        this.context = context;
        this.categoryItemList = categoryItemList;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View categoryItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_grid_view,parent,false);
        return new CategoryViewHolder(categoryItemView);
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder holder, int position) {

        Log.e("tyfty",""+categoryItemList.size());
        holder.categoryIcon.setImageResource(categoryItemList.get(position).getCategoryIcon());
        holder.categoryTitle.setText(categoryItemList.get(position).getCategoryTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getAdapterPosition()==0){
                    Intent bodyIndexIntent = new Intent(context, PatientGeneralProfileActivity.class);
                    context.startActivity(bodyIndexIntent);
                }
                if (holder.getAdapterPosition()==1){
                    PhysicalServerObjectDataController.getInstance().isFromStaring=true;
                    Intent bodyIndexIntent = new Intent(context, PhysicalExamActivity.class);
                    context.startActivity(bodyIndexIntent);
                }
                if (holder.getAdapterPosition()==4){
                    Intent bodyIndexIntent = new Intent(context, FamilyRecordActivity.class);
                    context.startActivity(bodyIndexIntent);
                }
                if (holder.getAdapterPosition()==5){
                    Intent appointmentIntent = new Intent(context, AppointmentsActivity.class);
                    context.startActivity(appointmentIntent);
                }
                if (holder.getAdapterPosition()==3){

                    /* Intent bodyIndexIntent = new Intent(context, AllergiesActivity.class);
                    context.startActivity(bodyIndexIntent);
               */ }
                if (holder.getAdapterPosition()==6){
                    Intent bodyIndexIntent = new Intent(context, VaccinationRecordActivity.class);
                    context.startActivity(bodyIndexIntent);
                }

                if (holder.getAdapterPosition()==7){
                    Intent bodyIndexIntent = new Intent(context, ScreeningRecordActivity.class);
                    context.startActivity(bodyIndexIntent);
                }

                if (holder.getAdapterPosition()==2){
                    Intent bodyIndexIntent = new Intent(context, MedicalHistoryActivity.class);
                    context.startActivity(bodyIndexIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryItemList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryIcon;
        TextView categoryTitle;
        public CategoryViewHolder( View itemView) {
            super(itemView);
            categoryIcon = itemView.findViewById(R.id.img_category_icon);
            categoryTitle = itemView.findViewById(R.id.txt_category_title);
        }
    }

/*
    public void alertRecordDailog() {

        recordList.clear();
        View view = getLayoutInflater().inflate(R.layout.records_alert_dialog, null);
        RelativeLayout addListLayout = view.findViewById(R.id.add_list_layout);
        imgRecordAdd = view.findViewById(R.id.img_add);
        btnRecordEdit = view.findViewById(R.id.btn_record_edit);
        btnRecordSave = view.findViewById(R.id.btn_record_save);
        saveBtnLayout =view.findViewById(R.id.btn_save_layout);
        cancelBtnLayout = view.findViewById(R.id.btn_cancel_layout);
        btnRecordCancel = view.findViewById(R.id.btn_record_cancel);
        cancelBtnLayout.setVisibility(View.GONE);

        final RecordModel recordModel = new RecordModel();
        RecordModel recordModel1 = new RecordModel();
        recordModel.setsNo("1");
        recordModel.setCondition("Carona");
        recordModel.setRelation("Wife");
        recordModel1.setsNo("2");
        recordModel1.setRelation("Husbend");
        recordModel1.setCondition("carona");
        recordList.add(recordModel);
        recordList.add(recordModel1);
        dialog = new BottomSheetDialog(Objects.requireNonNull(CategoryActivity.this),R.style.BottomSheetDialogTheme);
        recordRecyclerView = view.findViewById(R.id.record_recyclerview);
        recyclerView(recordList);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();

        addListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertRecordDialog();

            }
        });
        btnRecordSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                JSONArray array = new JSONArray();
                // array.put(recordModel);

                Log.e("recordList",""+recordList.size());
                for (int i=0;i<recordList.size();i++){
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("name",recordList.get(i).getCondition());
                        jsonObject.put("note",recordList.get(i).getRelation());
                        array.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }

                addAllergyRecord(array);
                btnRecordSave.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.btn_bck_color));
                btnRecordSave.setTextColor(Color.parseColor("#ffffff"));
                btnRecordEdit.setTextColor(Color.parseColor("#3E454C"));
                btnRecordEdit.setBackground(CategoryActivity.this.getResources().getDrawable(R.drawable.new_login_boarder));

            }
        });

    }
*/
   /* public void addAllergyRecord(JSONArray list){
        MedicalProfileDataController.getInstance().fetchMedicalProfileData();
        MedicalProfileModel currentMedical = MedicalProfileDataController.getInstance().currentMedicalProfile;
        object = new JSONObject();
        JsonObject finalObject = new JsonObject();
        String regNo = currentMedical.getHospital_reg_number();
        Log.e("dfasdf",regNo);

        try {
            object.put("hospital_reg_num",regNo);
            object.put("patientID", PatientProfileDataController.getInstance().currentPatientlProfile.getPatientId());
            object.put("medical_record_id", PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
            object.put("byWhom","medical personnel");
            object.put("byWhomID",PatientProfileDataController.getInstance().currentPatientlProfile.getMedicalPerson_id());
            object.put("allergies",list);
            Log.e("list",""+list);
            Log.e("accessToken",""+currentMedical.getAccessToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("objecttt",""+object);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(object.toString());

        ApiCallDataController.getInstance().loadServerApiCall(
                ApiCallDataController.getInstance().serverApi.addMultipleAllergyRecords(currentMedical.getAccessToken(), gsonObject), "insert");


    }

    public void recyclerView(ArrayList arrayList){

        AllergiesActivity.RecordRecyclerAdapter adapter = new AllergiesActivity.RecordRecyclerAdapter(arrayList,AllergiesActivity.this);
        recordRecyclerView.setAdapter(adapter);
        recordRecyclerView.setHasFixedSize(true);
        recordRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }*/
/*
    public void alertRecordDialog(){
        final EditText edtCondition,edtRelation;
        ImageView imgAlertCancel;
        Button btnSave;
        View view = getLayoutInflater().inflate(R.layout.add_new_record_list, null);
        AlertDialog.Builder addRecordBuilder = new AlertDialog.Builder(this);
        final AlertDialog dialog;
        addRecordBuilder.setView(view);
        dialog = addRecordBuilder.create();
        edtCondition =view.findViewById(R.id.edt_record_condition);
        edtRelation = view.findViewById(R.id.edt_record_relation);
        imgAlertCancel = view.findViewById(R.id.img_alert_cancel);
        btnSave = view.findViewById(R.id.btn_list_save);
        imgAlertCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String condition =edtCondition.getText().toString();
                String relation =edtRelation.getText().toString();
                RecordModel modelRecord = new RecordModel();
                modelRecord.setCondition(condition);
                modelRecord.setRelation(relation);
                recordList.add(modelRecord);
                recyclerView(recordList);
                dialog.cancel();
            }
        });
        dialog.show();

    }
*/

}
