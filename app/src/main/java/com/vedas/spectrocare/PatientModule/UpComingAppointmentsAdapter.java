package com.vedas.spectrocare.PatientModule;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.Controllers.PersonalInfoController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.DataBaseModels.PatientModel;
import com.vedas.spectrocare.PatientAppointmentModule.AppointmentArrayModel;
import com.vedas.spectrocare.PatientAppointmentModule.PatientAppointmentsDataController;
import com.vedas.spectrocare.PatientChat.PatientChatActivity;
import com.vedas.spectrocare.PatientChat.SocketIOHelper;
import com.vedas.spectrocare.PatientModule.UpcomingAppointmentFragment.OnListFragmentInteractionListener;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.PatientVideoCallModule.VideoActivity;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import com.vedas.spectrocare.activities.HomeActivity;
import com.vedas.spectrocare.model.AppointmetModel;
import com.vedas.spectrocare.model.DoctorsItemModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

/**
 * {@link RecyclerView.Adapter} that can display a {@link} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class UpComingAppointmentsAdapter extends RecyclerView.Adapter<UpComingAppointmentsAdapter.ViewHolder> {
    public Context context1;
    TextView txtName, txtDate, timeTxt;
    CircularImageView imgAlertPic;
    AppointmetModel appointmetModel;
    Socket mSocket;
    AppointmentArrayModel arrayModel;
    Button btnAbort;
    int pos,modelPosition;
    ArrayList<AppointmentArrayModel> appointmentDataList;

    public UpComingAppointmentsAdapter(Context context1, ArrayList<AppointmentArrayModel> appointmentDataList) {
        this.context1 = context1;
        this.appointmentDataList = appointmentDataList;
        mSocket = SocketIOHelper.getInstance().socket;
        // mSocket.on("join", onNewMessage);

        // mSocket.connect();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_upcoming_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (!appointmentDataList.isEmpty()) {
            holder.txtName.setText(appointmentDataList.get(position).getDoctorDetails().getProfile().getUserProfile().getFirstName() + " " +
                    appointmentDataList.get(position).getDoctorDetails().getProfile().getUserProfile().getLastName());
            holder.txtSpecial.setText(appointmentDataList.get(position).getDoctorDetails().getProfile().getUserProfile().getDepartment());

            if (appointmentDataList.get(position).getAppointmentDetails().getAppointmentDate().contains("/")) {
                holder.txtAppointmentDate.setText(appointmentDataList.get(position).getAppointmentDetails().getAppointmentDate());
            } else {
                long ll = Long.parseLong(appointmentDataList.get(position).getAppointmentDetails().getAppointmentDate());

                //load settings date formate to date feild.
                String value = PersonalInfoController.getInstance().loadSettingsDataFormateToEntireApp(context1,String.valueOf(ll));
                holder.txtAppointmentDate.setText(value);
                //
                Date currentDate = new Date(ll);
                SimpleDateFormat jdff = new SimpleDateFormat("dd/MM/yyyy");
                jdff.setTimeZone(TimeZone.getDefault());
                String java_date = jdff.format(currentDate);
               // holder.txtAppointmentDate.setText(java_date);
            }
            holder.txtTime.setText(appointmentDataList.get(position).getAppointmentDetails().getAppointmentTime());
            holder.txtStatus.setText(appointmentDataList.get(position).getAppointmentDetails().getAppointmentStatus());
        }


        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.WHITE);
        gd.setCornerRadius(10);
        gd.setStroke(2, Color.parseColor("#615D5E"));
        if (appointmentDataList.get(position).getAppointmentDetails().getAppointmentStatus().equals("Cancelled")) {
            holder.txtStatus.setTextColor(ContextCompat.getColor(context1, R.color.colorRed));
        } else if (appointmentDataList.get(position).getAppointmentDetails().getAppointmentStatus().equals("Confirmed")) {
            holder.txtStatus.setTextColor(ContextCompat.getColor(context1, R.color.colorOrange));
        } else if (appointmentDataList.get(position).getAppointmentDetails().getAppointmentStatus().contains("Waiting")) {
            holder.txtStatus.setTextColor(ContextCompat.getColor(context1, R.color.colorblue));
        }
        if (!appointmentDataList.get(position).getDoctorDetails().getProfile().getUserProfile().getProfilePic().isEmpty())
            Picasso.get().load(ServerApi.img_home_url + appointmentDataList.get(position).getDoctorDetails().getProfile().getUserProfile().getProfilePic()).placeholder(R.drawable.image).into(holder.imgPic);
        holder.mainLayout.setBackgroundDrawable(gd);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = holder.getAdapterPosition();
              //  mSocket.on("join", onNewMessage);
            //    mSocket.on("subscribe"/*, onNewMessage*/);
                Log.e("dadd", "d" + appointmentDataList.get(pos).getAppointmentDetails().getAppointmentID());
                if (appointmentDataList.get(holder.getAdapterPosition()).getAppointmentDetails().getAppointmentStatus().equals("Confirmed")
                        || appointmentDataList.get(holder.getAdapterPosition()).getAppointmentDetails().getAppointmentStatus().equals("Waiting for confirmation"))
                    alertDailog();
                else {
                    arrayModel = appointmentDataList.get(pos);
                    PatientMedicalRecordsController.getInstance().selectedappointmnetModel=arrayModel;
                    // addDoctorReviewRatingsByPAtient();
                    context1.startActivity(new Intent(context1, AppointmentDetailsActivity.class));
                           // .putExtra("sampleObject", arrayModel)
                           // .putExtra("docPic", ServerApi.img_home_url + appointmentDataList.get(pos).getDoctorDetails().getProfile().getUserProfile().getProfilePic()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (appointmentDataList.size() > 0) {
            return appointmentDataList.size();
        } else
            return 0;/*mValues.size();*/
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        TextView txtName, txtSpecial, txtAppointmentDate, txtStatus, txtTime;
        /*public final TextView mIdView;
        public final TextView mContentView;*/
        // public DummyItem mItem;
        CircularImageView imgPic;
        RelativeLayout mainLayout;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtName = view.findViewById(R.id.doc_name);
            txtStatus = view.findViewById(R.id.appointment_approval);
            txtSpecial = view.findViewById(R.id.doc_spe);
            imgPic = view.findViewById(R.id.doc_profile);
            txtTime = view.findViewById(R.id.appointment_time);
            txtAppointmentDate = view.findViewById(R.id.appointment_date);
            mainLayout = (RelativeLayout) view.findViewById(R.id.mainLayout);
            /*mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);*/
        }
    }

    public void filterList(ArrayList<AppointmentArrayModel> filterdNames) {
        this.appointmentDataList = filterdNames;
        notifyDataSetChanged();
    }

    public void alertDailog() {
        LayoutInflater li = (LayoutInflater) context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.addappotment_bottomsheet, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(Objects.requireNonNull(context1), R.style.BottomSheetDialogTheme);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();
        btnAbort = (Button) dialog.findViewById(R.id.status);
        txtName = dialog.findViewById(R.id.title);
        txtDate = dialog.findViewById(R.id.txt_date);
        timeTxt = dialog.findViewById(R.id.time);
        TextView txtOnline = dialog.findViewById(R.id.title1);

        final GradientDrawable drawable = (GradientDrawable) btnAbort.getBackground();
        drawable.setColor(context1.getResources().getColor(R.color.colorGreen));

        imgAlertPic = dialog.findViewById(R.id.icon_play);
        if (!appointmentDataList.get(pos).getDoctorDetails().getProfile().getUserProfile().getProfilePic().isEmpty())
            Picasso.get().load(ServerApi.img_home_url + appointmentDataList.get(pos).getDoctorDetails().getProfile().getUserProfile().getProfilePic()).placeholder(R.drawable.image).into(imgAlertPic);
        long ll = Long.parseLong(appointmentDataList.get(pos).getDoctorDetails().getProfile().getUserProfile().getRegisterTime());
        if (appointmentDataList.get(pos).getDoctorDetails().getProfile().getUserProfile().isOnline()) {
            txtOnline.setText("Online");
        } else {
            txtOnline.setText("Offline");
            btnAbort.setBackgroundResource(R.drawable.btn_yellow_backgroound);
        }
        Date currentDate = new Date(ll);
        SimpleDateFormat jdff = new SimpleDateFormat("dd/MM/yyyy");
        jdff.setTimeZone(TimeZone.getDefault());
        String java_date = jdff.format(currentDate);
        txtName.setText(appointmentDataList.get(pos).getDoctorDetails().getProfile().getUserProfile().getFirstName() + " " +
                appointmentDataList.get(pos).getDoctorDetails().getProfile().getUserProfile().getLastName());
        txtDate.setText(java_date);
        timeTxt.setText(appointmentDataList.get(pos).getAppointmentDetails().getAppointmentTime());

        CardView cancelView = dialog.findViewById(R.id.cancel);
        CardView detailsView = dialog.findViewById(R.id.details);
        CardView messageView = dialog.findViewById(R.id.card_message_view);
        CardView callView = dialog.findViewById(R.id.card_call_view);
        if (appointmentDataList.get(pos).getAppointmentDetails().getAppointmentStatus().equals("Waiting for confirmation")) {
            callView.setEnabled(false);
            messageView.setEnabled(false);
            callView.setAlpha(0.5f);
            messageView.setAlpha(0.5f);
        }
        callView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("appointmentID", "Id" + appointmentDataList.get(pos).getAppointmentDetails().getAppointmentID());
                context1.startActivity(new Intent(context1, VideoActivity.class)
                        .putExtra("appointmentId", appointmentDataList.get(pos).getAppointmentDetails().getAppointmentID()));
                dialog.dismiss();
            }
        });
        messageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                joinChat(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId(), appointmentDataList.get(pos).getAppointmentDetails().getAppointmentID());
                dialog.dismiss();
            }
        });

        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                drawable.setColor(context1.getResources().getColor(R.color.colorAccent));
                if (appointmentDataList.get(pos).getAppointmentDetails().getAppointmentStatus().equals("Cancelled")) {
                    Toast.makeText(context1, "Appointment already cancelled", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("mmmmmmmmmm", "call" + appointmentDataList.get(pos).getAppointmentDetails().getAppointmentStatus());
                    PatientMedicalRecordsController.getInstance().selectedappointmnetModel = appointmentDataList.get(pos);
                    context1.startActivity(new Intent(context1, AppointmentCancelActivity.class));
                  /*  context1.startActivity(new Intent(context1, AppointmentCancelActivity.class)
                            .putExtra("appointmentDetails", appointmentDataList.get(pos))
                            .putExtra("docName", appointmentDataList.get(pos).getDoctorDetails().getProfile().getUserProfile().getFirstName() + " " +
                                    appointmentDataList.get(pos).getDoctorDetails().getProfile().getUserProfile().getLastName()));
              */
                }
            }
        });
        detailsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                arrayModel = appointmentDataList.get(pos);
                drawable.setColor(context1.getResources().getColor(R.color.colorAccent));
                PatientMedicalRecordsController.getInstance().selectedappointmnetModel = appointmentDataList.get(pos);
                context1.startActivity(new Intent(context1, AppointmentDetailsActivity.class));
               /* arrayModel = appointmentDataList.get(pos);
                drawable.setColor(context1.getResources().getColor(R.color.colorAccent));
                context1.startActivity(new Intent(context1, AppointmentDetailsActivity.class)
                        .putExtra("sampleObject", arrayModel)
                        .putExtra("docPic", ServerApi.img_home_url + appointmentDataList.get(pos).getDoctorDetails().getProfile().getUserProfile().getProfilePic()));
                Gson gson = new Gson();
                String json = gson.toJson(arrayModel);
                Log.e("dadd", "d" + json);*/

            }
        });

    }
    private void joinChat(String userID, String roomid) {
        JsonObject feedObj = new JsonObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("roomID", roomid);
            jsonObject.put("userID", userID);
            JsonParser jsonParser = new JsonParser();
            feedObj = (JsonObject) jsonParser.parse(jsonObject.toString());
            Log.e("ChatJSON:", " " + feedObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("socket11", "message" + mSocket.id());
        mSocket.emit("subscribe", jsonObject);
        if (mSocket.id()!=null){
            Intent i = new Intent(context1, PatientChatActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("docId", appointmentDataList.get(pos).getDoctorMedicalPersonnelID());
            i.putExtra("appointmentID", appointmentDataList.get(pos).getAppointmentDetails().getAppointmentID());
            i.putExtra("docName", txtName.getText().toString());
            i.putExtra("isOnline",appointmentDataList.get(pos).getDoctorDetails().getProfile().getUserProfile().isOnline());
            i.putExtra("docPic", ServerApi.img_home_url + appointmentDataList.get(pos).getDoctorDetails().getProfile().getUserProfile().getProfilePic());
            context1.startActivity(i);
        }

    }
    public void addDoctorReviewRatingsByPAtient() {
        PatientModel currentModel = PatientLoginDataController.getInstance().currentPatientlProfile;
        AppointmentArrayModel arrayModel= PatientMedicalRecordsController.getInstance().selectedappointmnetModel;
        JSONObject params = new JSONObject();
        JSONObject reviewparams = new JSONObject();
        try {
            params.put("hospital_reg_num", currentModel.getHospital_reg_number());
            params.put("patientID", currentModel.getPatientId());
            params.put("medical_personnel_id", arrayModel.getDoctorMedicalPersonnelID());

            reviewparams.put("reviewername",currentModel.getFirstName()+" "+currentModel.getLastName());
            reviewparams.put("reviewerID", currentModel.getPatientId());
            reviewparams.put("ratings", "4.0");
            reviewparams.put("comment","Good Doctor");
            reviewparams.put("appointmentID",arrayModel.getAppointmentDetails().getAppointmentID());

            params.put("customerReview",reviewparams);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(params.toString());
        Log.e("ServiceResponse", "onResponse: " + gsonObject.toString());
        ApiCallDataController.getInstance().loadServerApiCall(ApiCallDataController.getInstance().serverApi.addDoctorReview(currentModel.getAccessToken(), gsonObject), "addDoctorReview");
    }
   /* private void joinChat(String userID, String roomid) {
        JsonObject feedObj = new JsonObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("roomID", roomid);
            jsonObject.put("userID", userID);
            JsonParser jsonParser = new JsonParser();
            feedObj = (JsonObject) jsonParser.parse(jsonObject.toString());
            Log.e("ChatJSON:", " " + feedObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("socket11", "message" + mSocket.id());
        mSocket.emit("subscribe", jsonObject);
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ((Activity) context1).runOnUiThread(new Runnable() {
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
                            Intent i = new Intent(context1, PatientChatActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.putExtra("docId", appointmentDataList.get(pos).getDoctorMedicalPersonnelID());
                            i.putExtra("appointmentID", appointmentDataList.get(pos).getAppointmentDetails().getAppointmentID());
                            i.putExtra("docName", txtName.getText().toString());
                            i.putExtra("isOnline",appointmentDataList.get(pos).getDoctorDetails().getProfile().getUserProfile().isOnline());
                            i.putExtra("docPic", ServerApi.img_home_url + appointmentDataList.get(pos).getDoctorDetails().getProfile().getUserProfile().getProfilePic());
                            context1.startActivity(i);
                            *//*
                            context1.startActivity(new Intent(context1, PatientChatActivity.class)
                                     //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    .putExtra("docId", appointmentDataList.get(pos).getDoctorMedicalPersonnelID())
                                    .putExtra("appointmentID", appointmentDataList.get(pos).getAppointmentDetails().getAppointmentID())
                                    .putExtra("docName", txtName.getText().toString())
                                    .putExtra("docPic", ServerApi.img_home_url + appointmentDataList.get(pos).getDoctorDetails().getProfile().getUserProfile().getProfilePic()));
*//*

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };*/

   /* private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ((Activity) context1).runOnUiThread(new Runnable() {
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
    };*/

}
