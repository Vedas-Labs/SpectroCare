package com.vedas.spectrocare.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.DataBase.AppointmentDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBaseModels.AppointmentModel;
import com.vedas.spectrocare.PatientAppointmentModule.AppointmentArrayModel;
import com.vedas.spectrocare.PatientChat.PatientChatActivity;
import com.vedas.spectrocare.PatientChat.SocketIOHelper;
import com.vedas.spectrocare.PatientModule.AppointmentCancelActivity;
import com.vedas.spectrocare.PatientModule.AppointmentDetailsActivity;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.PatientVideoCallModule.VideoActivity;
import com.vedas.spectrocare.PatinetControllers.PatientAppointmentController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.activities.AppointmentSummaryActivity;
import com.vedas.spectrocare.model.AppointmetModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarHolder> {
    Context context;
    ArrayList<AppointmentArrayModel> calendarItemListarrayList;
    TextView textView;
    public static boolean isFromCalender = false;

    public CalendarAdapter(Context context, ArrayList<AppointmentArrayModel> calendarItemListarrayList, TextView textView) {
        this.context = context;
        this.calendarItemListarrayList = calendarItemListarrayList;
        this.textView = textView;
    }

    public CalendarAdapter(Context context, ArrayList<AppointmentArrayModel> calendarItemListarrayList) {
        this.context = context;
        this.calendarItemListarrayList = calendarItemListarrayList;
    }

    @NonNull
    @Override
    public CalendarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View calendarView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_upcoming_item, parent,
                false);
        return new CalendarHolder(calendarView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CalendarHolder holder, int position) {
        AppointmentArrayModel appointmentModel = calendarItemListarrayList.get(position);

        if (!appointmentModel.getDoctorDetails().getProfile().getUserProfile().getProfilePic().isEmpty()) {
            Picasso.get().load(ServerApi.img_home_url + appointmentModel.getDoctorDetails().getProfile().getUserProfile().getProfilePic()).placeholder(R.drawable.profile_1).into(holder.docProfile);
        } else {
            holder.docProfile.setImageResource(R.drawable.profile_1);
        }
        //
        long ll = Long.parseLong(appointmentModel.getAppointmentDetails().getAppointmentDate());
        Date currentDate = new Date(ll);
        SimpleDateFormat jdff = new SimpleDateFormat("dd/MM/yyyy");
        jdff.setTimeZone(TimeZone.getDefault());
        String java_date = jdff.format(currentDate);
        //  holder.appointmentDate.setText(java_date);
        //load settings date formate to date feild.
        String value = PersonalInfoController.getInstance().loadSettingsDataFormateToEntireApp(context, String.valueOf(ll));
        holder.appointmentDate.setText(value);
        //
        holder.appointmentTime.setText(appointmentModel.getAppointmentDetails().getAppointmentTime());
        holder.docName.setText(appointmentModel.getDoctorDetails().getProfile().getUserProfile().getFirstName() + " " +
                appointmentModel.getDoctorDetails().getProfile().getUserProfile().getLastName());
        holder.txtSpecial.setText(appointmentModel.getDoctorDetails().getProfile().getUserProfile().getDepartment());
        holder.appointmentApproval.setText(appointmentModel.getAppointmentDetails().getAppointmentStatus());
        /*if (appointmentModel.getAppointmentDetails().getAppointmentStatus().contains("Confirmed")) {
            holder.appointmentApproval.setText("Conformed");
            holder.appointmentApproval.setTextColor(Color.parseColor("#615D5E"));
        } else {*/
        if (appointmentModel.getAppointmentDetails().getAppointmentStatus().contains("Completed")) {
            holder.appointmentApproval.setTextColor(Color.GREEN);
            holder.appointmentApproval.setText(appointmentModel.getAppointmentDetails().getAppointmentStatus());
        } else if (appointmentModel.getAppointmentDetails().getAppointmentStatus().contains("Pending")) {
            holder.appointmentApproval.setTextColor(Color.parseColor("#FFA500"));
            holder.appointmentApproval.setText(appointmentModel.getAppointmentDetails().getAppointmentStatus());
        } else if (appointmentModel.getAppointmentDetails().getAppointmentStatus().contains("Cancelled") || appointmentModel.getAppointmentDetails().getAppointmentStatus().contains("Rejected")) {
            holder.appointmentApproval.setTextColor(Color.RED);
            holder.appointmentApproval.setText(appointmentModel.getAppointmentDetails().getAppointmentStatus());
        } else if (appointmentModel.getAppointmentDetails().getAppointmentStatus().contains("Confirmed")) {
            holder.appointmentApproval.setText("Confirmed");
            holder.appointmentApproval.setTextColor(ContextCompat.getColor(context, R.color.colorOrange));
            // holder.appointmentApproval.setTextColor(Color.parseColor("#615D5E"));
        } else if (calendarItemListarrayList.get(position).getAppointmentDetails().getAppointmentStatus().contains("Waiting")) {
            holder.appointmentApproval.setTextColor(ContextCompat.getColor(context, R.color.colorblue));
        }
        //}
        holder.txt_full_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppointmentDataController.getInstance().currentAppointmentModel = AppointmentDataController.getInstance().allAppointmentList.get(holder.getAdapterPosition());
                Intent patientProfileIntent = new Intent(context, AppointmentSummaryActivity.class);
                context.startActivity(patientProfileIntent);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (calendarItemListarrayList.get(holder.getAdapterPosition()).getAppointmentDetails().getAppointmentStatus().contains("Confirmed")) {
                    alertDailog(calendarItemListarrayList.get(holder.getAdapterPosition()));
                } else {
                    AppointmentArrayModel arrayModel = calendarItemListarrayList.get(holder.getAdapterPosition());
                    PatientMedicalRecordsController.getInstance().selectedappointmnetModel = arrayModel;
                    context.startActivity(new Intent(context, AppointmentDetailsActivity.class));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (calendarItemListarrayList.size() > 0) {
            textView.setVisibility(View.GONE);
            return calendarItemListarrayList.size();
        } else {
            textView.setVisibility(View.VISIBLE);
            return 0;
        }
    }

    public class CalendarHolder extends RecyclerView.ViewHolder {
        CircularImageView docProfile;
        TextView docName, appointmentDate, appointmentTime, appointmentApproval;
        TextView txt_full_view, txtSpecial;

        public CalendarHolder(@NonNull View itemView) {
            super(itemView);
            docProfile = itemView.findViewById(R.id.doc_profile);
            docName = itemView.findViewById(R.id.doc_name);
            txtSpecial = itemView.findViewById(R.id.doc_spe);
            appointmentDate = itemView.findViewById(R.id.appointment_date);
            appointmentTime = itemView.findViewById(R.id.appointment_time);
            appointmentApproval = itemView.findViewById(R.id.appointment_approval);
            txt_full_view = itemView.findViewById(R.id.txt_full_view);

        }
    }

    public void alertDailog(AppointmentArrayModel model) {
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
                    isFromCalender = true;
                    PatientMedicalRecordsController.getInstance().selectedappointmnetModel = model;
                    context.startActivity(new Intent(context, AppointmentCancelActivity.class));
                   /* context.startActivity(new Intent(context, AppointmentCancelActivity.class)
                            .putExtra("appointmentDetails",model)
                            .putExtra("docName",model.getDoctorDetails().getProfile().getUserProfile().getFirstName()+" "+
                                    model.getDoctorDetails().getProfile().getUserProfile().getLastName()));
               */
                }
                dialog.dismiss();
            }
        });
        detailsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                drawable.setColor(context.getResources().getColor(R.color.colorAccent));
                PatientMedicalRecordsController.getInstance().selectedappointmnetModel = model;
                context.startActivity(new Intent(context, AppointmentDetailsActivity.class));
                /*context.startActivity(new Intent(context, AppointmentDetailsActivity.class)
                        .putExtra("sampleObject",model)
                        .putExtra("docPic",ServerApi.img_home_url+model.getDoctorDetails().getProfile().getUserProfile().getProfilePic()));
*/
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
