package com.vedas.spectrocare.patientModuleAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientAppointmentModule.AppointmentArrayModel;
import com.vedas.spectrocare.PatientAppointmentModule.PatientAppointmentsDataController;
import com.vedas.spectrocare.PatientChat.PatientChatActivity;
import com.vedas.spectrocare.PatientChat.SocketIOHelper;
import com.vedas.spectrocare.PatientModule.AppointmentCancelActivity;
import com.vedas.spectrocare.PatientModule.AppointmentDetailsActivity;
import com.vedas.spectrocare.PatientServerApiModel.InboxNotificationModel;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.PatientVideoCallModule.VideoActivity;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.adapter.CalendarAdapter;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import io.socket.emitter.Emitter;

public class PatientNotificationAdapter extends RecyclerView.Adapter<PatientNotificationAdapter.NotificationHolder> {
    Context context;

    public PatientNotificationAdapter(Context context) {
        this.context = context;
    }

    @Override
    public PatientNotificationAdapter.NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View swipeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_recyclerview, parent, false);
        return new NotificationHolder(swipeView);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientNotificationAdapter.NotificationHolder holder, int position) {
        InboxNotificationModel obj = PatientMedicalRecordsController.getInstance().inboxNotificationList.get(position);
        holder.txt_message.setText(obj.getMessageBody().getTitle());
        holder.txt_message1.setText(obj.getMessageBody().getBody());
        if (obj.isRead()) {
            holder.btnView.setBackgroundResource(R.drawable.ic_unread);
        } else {
            holder.btnView.setBackgroundResource(R.drawable.ic_check);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InboxNotificationModel obj = PatientMedicalRecordsController.getInstance().inboxNotificationList.get(holder.getAdapterPosition());
                PatientMedicalRecordsController.getInstance().inboxNotificationModel=obj;
                Log.e("apppintmnetid", "call" + obj.getMessageBody().getData().getAppointmentID());
                if (PatientAppointmentsDataController.getInstance().allappointmentsList.size() > 0) {
                    for (int k = 0; k < PatientAppointmentsDataController.getInstance().allappointmentsList.size(); k++) {
                        AppointmentArrayModel model = PatientAppointmentsDataController.getInstance().allappointmentsList.get(k);
                        if (model.getAppointmentDetails().getAppointmentID().equals(obj.getMessageBody().getData().getAppointmentID())) {
                            Log.e("equalapppintmnetid", "call" + obj.getMessageBody().getData().getAppointmentID());
                            if(obj.getMessageBody().getBody().toLowerCase().contains("cancelled")){
                                accessInterfaceMethods(obj);
                                inboxMessagesUpdateApi(obj);
                                PatientMedicalRecordsController.getInstance().selectedappointmnetModel = model;
                                context.startActivity(new Intent(context, AppointmentDetailsActivity.class));
                            }else{
                                alertDailog(model, obj);
                            }

                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (PatientMedicalRecordsController.getInstance().inboxNotificationList.size() > 0) {
            return PatientMedicalRecordsController.getInstance().inboxNotificationList.size();
        } else {
            return 0;
        }
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {
        Button btnView;
        TextView txt_message, txt_message1;

        // ImageView imgDisease;
        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            btnView = itemView.findViewById(R.id.btn_view);
           /* subTxt = itemView.findViewById(R.id.txt_infection_name);
            txtTime = itemView.findViewById(R.id.txt_doc_time);
            imgDisease = itemView.findViewById(R.id.img_doc_pic);*/
            txt_message = itemView.findViewById(R.id.txt_message);
            txt_message1 = itemView.findViewById(R.id.txt_message1);
        }
    }

    public void alertDailog(AppointmentArrayModel model, InboxNotificationModel inboxNotificationModel) {
        TextView txtName, txtDate, timeTxt;
        CircularImageView imgAlertPic;
        Button btnAbort;
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.addappotment_bottomsheet, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(Objects.requireNonNull(context), R.style.BottomSheetDialogTheme);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();
        btnAbort = (Button) dialog.findViewById(R.id.status);
        txtName = dialog.findViewById(R.id.title);
        txtDate = dialog.findViewById(R.id.txt_date);
        timeTxt = dialog.findViewById(R.id.time);
        imgAlertPic = dialog.findViewById(R.id.icon_play);
        if (!model.getDoctorDetails().getProfile().getUserProfile().getProfilePic().isEmpty())
            Picasso.get().load(ServerApi.img_home_url + model.getDoctorDetails().getProfile().getUserProfile().getProfilePic()).placeholder(R.drawable.image).into(imgAlertPic);
        //
        long ll = Long.parseLong(model.getAppointmentDetails().getAppointmentDate());
        Date currentDate = new Date(ll);
        SimpleDateFormat jdff = new SimpleDateFormat("dd/MM/yyyy");
        jdff.setTimeZone(TimeZone.getDefault());
        String java_date = jdff.format(currentDate);

        txtName.setText(model.getDoctorDetails().getProfile().getUserProfile().getFirstName() + " " +
                model.getDoctorDetails().getProfile().getUserProfile().getLastName());

        txtDate.setText(java_date);
        timeTxt.setText(model.getAppointmentDetails().getAppointmentTime());
        final GradientDrawable drawable = (GradientDrawable) btnAbort.getBackground();
        drawable.setColor(context.getResources().getColor(R.color.colorGreen));
        CardView cancelView = dialog.findViewById(R.id.cancel);
        CardView detailsView = dialog.findViewById(R.id.details);
        CardView messageView = dialog.findViewById(R.id.card_message_view);
        CardView callView = dialog.findViewById(R.id.card_call_view);
        if (model.getAppointmentDetails().getAppointmentStatus().equals("Waiting for confirmation")) {
            callView.setEnabled(false);
            messageView.setEnabled(false);
            callView.setAlpha(0.5f);
            messageView.setAlpha(0.5f);
        }
        callView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("appointmentID", "Id" + model.getAppointmentDetails().getAppointmentID());
                context.startActivity(new Intent(context, VideoActivity.class)
                        .putExtra("appointmentId", model.getAppointmentDetails().getAppointmentID()));
                dialog.dismiss();
            }
        });
        messageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinChat(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId(), model.getAppointmentDetails().getAppointmentID(), model);
                dialog.dismiss();
            }
        });

        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                drawable.setColor(context.getResources().getColor(R.color.colorAccent));
                if (model.getAppointmentDetails().getAppointmentStatus().equals("Cancelled")) {
                    Toast.makeText(context, "Appointment already cancelled", Toast.LENGTH_SHORT).show();
                } else {
                    CalendarAdapter.isFromCalender = true;
                    PatientMedicalRecordsController.getInstance().selectedappointmnetModel = model;
                    context.startActivity(new Intent(context, AppointmentCancelActivity.class));
                }
                dialog.dismiss();
            }
        });
        detailsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                accessInterfaceMethods(inboxNotificationModel);
                inboxMessagesUpdateApi(inboxNotificationModel);
                drawable.setColor(context.getResources().getColor(R.color.colorAccent));
                PatientMedicalRecordsController.getInstance().selectedappointmnetModel = model;
                context.startActivity(new Intent(context, AppointmentDetailsActivity.class));
            }
        });

    }

    private void inboxMessagesUpdateApi(InboxNotificationModel inboxNotificationModel) {
        JSONObject fetchObject = new JSONObject();
        try {
            fetchObject.put("hospital_reg_num", PatientLoginDataController.getInstance().currentPatientlProfile.getHospital_reg_number());
            fetchObject.put("patientID", PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId());
            fetchObject.put("messageID", inboxNotificationModel.getMessageBody().getData().getMessageID());
            fetchObject.put("isRead", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(fetchObject.toString());
        Log.e("inboxMessagesUpdateApi", "length" + fetchObject.toString());

        ApiCallDataController.getInstance().loadServerApiCall(
                ApiCallDataController.getInstance().serverApi.
                        inboxUpdateApi(PatientLoginDataController.getInstance().currentPatientlProfile.getAccessToken(), gsonObject), "invoiceUpdate");
    }
    private void accessInterfaceMethods(InboxNotificationModel obj) {
        ApiCallDataController.getInstance().initializeServerInterface(new ApiCallDataController.ServerResponseInterface() {
            @Override
            public void successCallBack(JSONObject jsonObject, String curdOpetaton) {
                if (curdOpetaton.equals("invoiceUpdate")) {
                    try {
                        Log.e("aaaaaaaaaaaaaaa", "length" + jsonObject.toString());

                        if (jsonObject.getString("response").equals("3")) {
                            Log.e("updateinvocie", "length" + jsonObject.getString("message").toString());
                            if (PatientMedicalRecordsController.getInstance().inboxNotificationList.size() > 0) {
                                for (int k = 0; k < PatientMedicalRecordsController.getInstance().inboxNotificationList.size(); k++) {
                                    InboxNotificationModel model = PatientMedicalRecordsController.getInstance().inboxNotificationList.get(k);
                                    if (model.getMessageBody().getData().getMessageID().equals(obj.getMessageBody().getData().getMessageID())) {
                                        Log.e("equalupdateinvocie", "call" + obj.getMessageBody().getData().getMessageID());
                                        obj.setRead(true);
                                        PatientMedicalRecordsController.getInstance().inboxNotificationList.set(k, obj);
                                        notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void failureCallBack(String failureMsg) {

            }
        });
    }
    private void joinChat(String userID, String roomid, AppointmentArrayModel model) {
        JsonObject feedObj = new JsonObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("roomID", roomid);
            jsonObject.put("userID", userID);
            JsonParser jsonParser = new JsonParser();
            feedObj = (JsonObject) jsonParser.parse(jsonObject.toString());
            //print parameter
            Log.e("ChatJSON:", " " + feedObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("socket11", "message" + SocketIOHelper.getInstance().socket.id());
        SocketIOHelper.getInstance().socket.on("join", onNewMessage);
        SocketIOHelper.getInstance().socket.connect();
        SocketIOHelper.getInstance().socket.emit("join", jsonObject);

        if (!SocketIOHelper.getInstance().socket.id().isEmpty()) {
            context.startActivity(new Intent(context, PatientChatActivity.class)
                    .putExtra("docId", model.getDoctorMedicalPersonnelID())
                    .putExtra("appointmentID", model.getAppointmentDetails().getAppointmentID())
                    .putExtra("docName", model.getDoctorDetails().getProfile().getUserProfile().getFirstName() + " " + model.getDoctorDetails().getProfile().getUserProfile().getLastName())
                    .putExtra("docPic", ServerApi.img_home_url + model.getDoctorDetails().getProfile().getUserProfile().getProfilePic()));
        }

    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Log.e("response for socket", " chat message" + data.toString());
                    String response;
                    String message = null;
                    JSONObject JsonObj;
                    try {
                        JsonObj = new JSONObject(data.toString());
                        response = JsonObj.getString("response");
                        message = JsonObj.getString("message");

                        if (response.equals("3")) {
                            if (JsonObj.getJSONObject("messageData").optString("UserEmail").equals("PatientID") ||
                                    JsonObj.getJSONObject("messageData").optString("UserEmail").equals("PatientID")) {
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Toast.makeText(Chating_activity.this,message,Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
}
