package com.vedas.spectrocare.patientModuleAdapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Alert.RefreshShowingDialog;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBase.TestFactorDataController;
import com.vedas.spectrocare.DataBase.UrineResultsDataController;
import com.vedas.spectrocare.DataBaseModels.UrineresultsModel;
import com.vedas.spectrocare.PatientModule.PatientTestRecordActivity;
import com.vedas.spectrocare.PatientModule.ResultPageViewActivity;
import com.vedas.spectrocare.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class UrineTestAdapter extends RecyclerView.Adapter<UrineTestAdapter.UrineTestHolder> {
    Context context;
    Button btnEdit;
    int selectedPosition = -1;
    RefreshShowingDialog refreshShowingDialog;

    public UrineTestAdapter(Context context1, Button btnEdit) {
        this.context = context1;
        this.btnEdit = btnEdit;
    }

    public UrineTestAdapter(Context context1, RefreshShowingDialog refreshShowingDialog1) {
        this.context = context1;
        this.refreshShowingDialog = refreshShowingDialog1;
    }


    @Override
    public UrineTestAdapter.UrineTestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View urineTestView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_recycle_view, parent, false);
        return new UrineTestHolder(urineTestView);
    }

    @Override
    public void onBindViewHolder(@NonNull UrineTestAdapter.UrineTestHolder holder, int position) {
        UrineresultsModel model = UrineResultsDataController.getInstance().allUrineResults.get(position);
        holder.txtTest.setText("Urine Routine");
        holder.imgIcon.setBackgroundResource(R.drawable.testtube);
        holder.txtNormal.setTextColor(Color.parseColor("#2DAF22"));
        holder.txtAbnormal.setVisibility(View.VISIBLE);
        holder.txtAbnormal.setTextColor(Color.parseColor("#FF0012"));

        try {
            String dateTime = convertTimestampTodate(model.getTestedTime());
            String array[] = dateTime.split(" ");
            holder.date.setText(array[0]);
            holder.time.setText(array[1] + " " + array[2]);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (TestFactorDataController.getInstance().fetchTestFactorresults(model).size() > 0) {
            String val[] = calculateNormalAndAbnormalValues(model).split(" ");
            holder.txtNormal.setText("Normal " + val[0]);
            holder.txtAbnormal.setText("AbNormal " + val[1]);
        }
        if (selectedPosition == position) {
            accessInterfaceMethods(UrineResultsDataController.getInstance().allUrineResults.get(selectedPosition));
            showDeleteDialog(UrineResultsDataController.getInstance().allUrineResults.get(selectedPosition));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition != position) {
                    selectedPosition = position;
                } else {
                    selectedPosition = -1;
                }
                notifyDataSetChanged();
            }
        });
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientTestRecordActivity.isFromTestRecord = true;
                UrineResultsDataController.getInstance().currenturineresultsModel = UrineResultsDataController.getInstance().allUrineResults.get(holder.getAdapterPosition());
                context.startActivity(new Intent(context, ResultPageViewActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (UrineResultsDataController.getInstance().allUrineResults.size() > 0) {
            return UrineResultsDataController.getInstance().allUrineResults.size();
        } else {
            return 0;
        }
    }

    public class UrineTestHolder extends RecyclerView.ViewHolder {
        TextView txtNormal, txtAbnormal, txtTest, date, time;
        ImageView imgIcon;
        CheckBox checkBox;
        Button btnView;

        public UrineTestHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.txt_doc_date);
            imgIcon = itemView.findViewById(R.id.img_doc_pic);
            time = itemView.findViewById(R.id.txt_doc_time);
            txtTest = itemView.findViewById(R.id.txt_doc_name);
            checkBox = itemView.findViewById(R.id.checkbox);
            btnView = itemView.findViewById(R.id.btn_view);
            txtAbnormal = itemView.findViewById(R.id.txt_Abnormal);
            txtNormal = itemView.findViewById(R.id.txt_infection_name);
        }
    }

    private String calculateNormalAndAbnormalValues(UrineresultsModel model) {
        int normal = 0, abnormal = 0;
        if (TestFactorDataController.getInstance().fetchTestFactorresults(model).size() > 0) {
            for (int i = 0; i < TestFactorDataController.getInstance().fetchTestFactorresults(model).size(); i++) {
                if (TestFactorDataController.getInstance().fetchTestFactorresults(model).get(i).isFlag()) {
                    normal = normal + 1;
                } else {
                    abnormal = abnormal + 1;
                }
            }
        }
        return String.valueOf(normal) + " " + String.valueOf(abnormal);
    }

    public String convertTimestampTodate(String stringData)
            throws ParseException {
        long yourmilliseconds = Long.parseLong(stringData);
        SimpleDateFormat weekFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH);
        Date resultdate = new Date(yourmilliseconds * 1000);
        String weekString = weekFormatter.format(resultdate);
        return weekString;
    }

    public void showDeleteDialog(UrineresultsModel model) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_abort);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
        btnNo.setText("No");
        btnYes.setText("yes");

        TextView txt_title = dialog.findViewById(R.id.title);
        TextView txt_msg = dialog.findViewById(R.id.msg);
        TextView txt_msg1 = dialog.findViewById(R.id.msg1);

        txt_title.setText("Delete Record");
        txt_msg.setText("Are you sure you want to");
        txt_msg1.setText(" delete this record ?");

        RelativeLayout main = (RelativeLayout) dialog.findViewById(R.id.rl_main);
        RelativeLayout main1 = (RelativeLayout) dialog.findViewById(R.id.rl_main1);

        GradientDrawable drawable = (GradientDrawable) main.getBackground();
        drawable.setColor(context.getResources().getColor(R.color.colorWhite));

        GradientDrawable drawable1 = (GradientDrawable) main1.getBackground();
        drawable1.setColor(context.getResources().getColor(R.color.colorWhite));

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                refreshShowingDialog.showAlert();
                deleteUrineResultApi(model);
            }
        });
        dialog.show();

    }

    public void deleteUrineResultApi(UrineresultsModel model) {
        JSONObject fetchObject = new JSONObject();
        try {
            fetchObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
            fetchObject.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            fetchObject.put("byWhomID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            fetchObject.put("byWhom", "patient");
            fetchObject.put("medical_record_id", PatientLoginDataController.getInstance().currentPatientlProfile.getMedicalRecordId());
            fetchObject.put("testReportNumber", model.getTestReportNumber());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(fetchObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverJsonApi.
                deleteUrineResultApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "delete");
    }

    private void accessInterfaceMethods(UrineresultsModel model) {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                if (curdOpetaton.equals("delete")) {
                    try {
                        if (jsonObject.getString("response").equals("3")) {
                            refreshShowingDialog.hideRefreshDialog();
                            if (UrineResultsDataController.getInstance().deleteurineresultsData(model)) {
                                selectedPosition = -1;
                                notifyDataSetChanged();
                            }
                        } else {
                            refreshShowingDialog.hideRefreshDialog();
                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void failureCallBack(String failureMsg) {
                refreshShowingDialog.hideRefreshDialog();
                Toast.makeText(context, failureMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
