package com.vedas.spectrocare.patientModuleAdapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import io.socket.client.Socket;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.vedas.spectrocare.Alert.TimeAgo;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientAppointmentModule.AppointmentArrayModel;
import com.vedas.spectrocare.PatientAppointmentModule.PatientAppointmentsDataController;
import com.vedas.spectrocare.PatientChat.PatientChatActivity;
import com.vedas.spectrocare.PatientChat.SocketIOHelper;
import com.vedas.spectrocare.PatientServerApiModel.ChatRoomMessageModel;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DoctorMessageAdapter extends RecyclerView.Adapter<DoctorMessageAdapter.MessageHolder> {
    Context context;
    Socket mSocket;
    ArrayList<ArrayList<ChatRoomMessageModel>> chatRoomMessageList;
    public DoctorMessageAdapter(Context context,ArrayList<ArrayList<ChatRoomMessageModel>> chatRoomMessageList1) {
        this.context = context;
        this.chatRoomMessageList=chatRoomMessageList1;
        mSocket = SocketIOHelper.getInstance().socket;
    }
    @Override
    public DoctorMessageAdapter.MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View doctorItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor_message, parent, false);
        return new MessageHolder(doctorItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorMessageAdapter.MessageHolder holder, int position) {
        ArrayList<ChatRoomMessageModel> object = chatRoomMessageList.get(position);
        holder.txt_msg.setText(object.get(object.size() - 1).getMessage());
        String string = object.get(object.size() - 1).getTimeStamp();
        long s = Long.parseLong(string);
        holder.txt_date.setText("Chated on " + TimeAgo.getTimeAgo(s / 1000));

        //AppointmentArrayModel model = loadDoctorsDetails(object.get(position).getRoomID());
        if (object.get(position).getDoctorName() != null && object.get(position).getProfile() != null) {
            holder.txt_name.setText(object.get(position).getDoctorName() );
            Picasso.get().load(object.get(position).getProfile() ).placeholder(R.drawable.image).into(holder.circularImageView);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinChat(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId(),
                        object.get(holder.getAdapterPosition()).getRoomID(),loadDoctorsDetails(object.get(position).getRoomID()),holder.txt_name.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (chatRoomMessageList.size() > 0) {
            return chatRoomMessageList.size();
        } else {
            return 0;
        }
    }

    public class MessageHolder extends RecyclerView.ViewHolder {
        ImageView imgMessage;
        CircularImageView circularImageView;
        TextView txt_msg, txt_date, txt_name;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            imgMessage = itemView.findViewById(R.id.btn_view);
            circularImageView = itemView.findViewById(R.id.img_doc_pic);
            txt_msg = itemView.findViewById(R.id.txt_infection_name);
            txt_name = itemView.findViewById(R.id.txt_doc_name);
            txt_date = itemView.findViewById(R.id.txt_last_seen);
        }
    }

    private AppointmentArrayModel loadDoctorsDetails(String roomID) {
        Log.e("zzzzz","cll"+roomID);
        if (PatientAppointmentsDataController.getInstance().allappointmentsList.size() > 0) {
            for (int i = 0; i < PatientAppointmentsDataController.getInstance().allappointmentsList.size(); i++) {
                AppointmentArrayModel model = PatientAppointmentsDataController.getInstance().allappointmentsList.get(i);
                Log.e("model","cll"+model.getAppointmentDetails().getAppointmentID());
                if (roomID.equals(model.getAppointmentDetails().getAppointmentID())) {
                    return model;
                }
            }
        }
        return null;
    }

    private void joinChat(String userID, String roomid,AppointmentArrayModel model,String name) {
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
        if (mSocket.id() != null) {
            Intent i = new Intent(context, PatientChatActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("docId",model.getDoctorMedicalPersonnelID());
            i.putExtra("appointmentID", model.getAppointmentDetails().getAppointmentID());
            i.putExtra("docName", name);
            i.putExtra("isOnline",model.getDoctorDetails().getProfile().getUserProfile().isOnline());
            i.putExtra("docPic", ServerApi.img_home_url + model.getDoctorDetails().getProfile().getUserProfile().getProfilePic());
            context.startActivity(i);
        }

    }
}
