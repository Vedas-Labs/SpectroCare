package com.vedas.spectrocare.fireBaseNotifications;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import io.socket.client.Socket;

import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vedas.spectrocare.Controllers.ApiCallDataController;
import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.PatientAppointmentModule.AppointmentArrayModel;
import com.vedas.spectrocare.PatientAppointmentModule.PatientAppointmentsDataController;
import com.vedas.spectrocare.PatientChat.PatientChatActivity;
import com.vedas.spectrocare.PatientChat.SocketIOHelper;
import com.vedas.spectrocare.PatientDocResponseModel.MedicalPersonnelModel;
import com.vedas.spectrocare.PatientServerApiModel.InboxData;
import com.vedas.spectrocare.PatientServerApiModel.InboxNotificationModel;
import com.vedas.spectrocare.PatientServerApiModel.PatientMedicalRecordsController;
import com.vedas.spectrocare.PatientVideoCallModule.VideoActivity;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.activities.SplashActivity;
import com.vedas.spectrocare.PatientModule.PatientHomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyService extends FirebaseMessagingService {
    // Uri sound;
    Socket mSocket;
    MedicalPersonnelModel docProfilemodel;
    ArrayList<InboxData> messageDataList = new ArrayList<>();
    public MyService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
       /* Log.e("onMessagerecived", "Called");
        Log.e("remoteMessagecaaaa", "asfnj" + remoteMessage.getData());
        showNotification(remoteMessage);*/
        Log.e("onMessagerecived", "Called");
        Log.e("remoteMessagecaaaa", "asfnj1 " + remoteMessage.getData().get("appointmentID"));
        Gson gson = new Gson();
        String kk = gson.toJson(remoteMessage.getData());
        Log.e("messageData", "is:: " + kk);
        for (int m = 0; m< PatientAppointmentsDataController.getInstance().allappointmentsList.size(); m++){
            Log.e("remoteMessagecaaaa", "asfnj2 " + remoteMessage.getData().get("appointmentID"));
            if ( PatientAppointmentsDataController.getInstance().allappointmentsList.get(m).getAppointmentDetails().getAppointmentID().equals(remoteMessage.getData().get("appointmentID"))){
                mSocket = SocketIOHelper.getInstance().socket;
                Log.e("remoteMessagecaaaa", "asfnj3 " + remoteMessage.getData().get("appointmentID"));
                docProfilemodel =PatientAppointmentsDataController.getInstance().allappointmentsList.get(m).getDoctorDetails();
                Log.e("Docdataaa",":: "+gson.toJson(docProfilemodel));
            }
        }
        if (!remoteMessage.getData().get("appointmentID").equals("")){
            showNotification(remoteMessage,remoteMessage.getData().get("appointmentID"));
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("tokenfirebase", s);
        SplashActivity.sharedPreferencesTOkenEditor.putString("tokenid", s);
        SplashActivity.sharedPreferencesTOkenEditor.commit();
    }

    public void showNotification(RemoteMessage remoteMessage,String channelId) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "com.vedas.spectrocare";

        Log.e("showNotification", "called");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "notification",
                    NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription("hello");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(R.color.colorpink);
            notificationChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(notificationChannel);
        } else {
            Log.e("elsefirebasecall", "asfnj" + remoteMessage.getData());
        }

        if (!remoteMessage.getData().isEmpty()) { // for background handlings we perfome same login on splashscreen page.
            Log.e("remoteMessage", "asfnj" + remoteMessage.getData());
            Intent intent=null;
            if(remoteMessage.getData().get("messageType").equals("Appointment")) {
                Log.e("remotemsgAppointment", "asfnj" + remoteMessage.getData());

                 intent = new Intent(this, PatientHomeActivity.class)
                        .putExtra("isFromNotificaton","Appointment")
                        .putExtra("appointmentID",remoteMessage.getData().get("appointmentID"));

            }else if(remoteMessage.getData().get("messageType").equals("Calling")){
                Log.e("remotemsgwithcalling", "asfnj" + remoteMessage.getData().get("roomID"));

                 intent = new Intent(this, VideoActivity.class)
                        . putExtra("roomID", remoteMessage.getData().get("roomID"))
                        .putExtra("isFromNotificaton","Calling")
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            }else if(remoteMessage.getData().get("messageType").contains("Invoice")){
                Log.e("remotemsgwithinvoice", "asfnj" + remoteMessage.getData());

                 intent = new Intent(this, PatientHomeActivity.class)
                        .putExtra("isFromNotificaton","Invoice");
            }else if (remoteMessage.getData().get("messageType").equals("ChatMessage")){
              //  joinChat(PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId(),remoteMessage.getData().get("appointmentID"));
                intent = new Intent(this, PatientChatActivity.class)
                        .putExtra("docName",docProfilemodel.getProfile().getUserProfile().getFirstName()+" "+
                                docProfilemodel.getProfile().getUserProfile().getLastName())
                        .putExtra("isOnline",true)
                        .putExtra("appointmentID",remoteMessage.getData().get("appointmentID"))
                        .putExtra("docPic",docProfilemodel.getProfile().getUserProfile().getProfilePic())
                        .putExtra("docId",docProfilemodel.getProfile().getUserProfile().getMedical_personnel_id());
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(this, (int)System.currentTimeMillis(), intent, PendingIntent.FLAG_ONE_SHOT);
            notificationHandle(remoteMessage,pendingIntent,notificationManager);
        }
    }
    public void notificationHandle(RemoteMessage remoteMessage,PendingIntent pendingIntent,NotificationManager notificationManager){
        NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(this,"com.vedas.spectrocare");
        NotificationCompat.InboxStyle iStyle =  new NotificationCompat.InboxStyle()
                .setBigContentTitle(remoteMessage.getNotification().getTitle());
       // iStyle.addLine(remoteMessage.getNotification().getBody());

        notificationManager.cancel(123);

        Log.e("callInBck","back");
        InboxData inboxData = new InboxData();
       inboxData.setAppointmentID(remoteMessage.getData().get("appointmentID"));
       inboxData.setMessageID(remoteMessage.getNotification().getBody());
       messageDataList.add(inboxData);
       for (int bm=0; bm<messageDataList.size();bm++){
           if (messageDataList.get(bm).getAppointmentID().equals(remoteMessage.getData().get("appointmentID"))){

               iStyle.addLine(messageDataList.get(bm).getMessageID());
           }
       }
        Gson gson = new Gson();
        String ffd = gson.toJson(messageDataList);
        Log.e("messsgg","data: "+ffd);
        notificationBuilder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL).setWhen(System.currentTimeMillis()).
                setContentTitle(remoteMessage.getNotification().getTitle()).setContentText(remoteMessage.getNotification().getBody())
                .setContentInfo("Info").setStyle(iStyle)
                .setPriority(NotificationManager.IMPORTANCE_HIGH).setContentIntent(pendingIntent);

        notificationBuilder.setDefaults(Notification.DEFAULT_LIGHTS);// i got struggled with out these 2 lines to get custom notification sound in background.
        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);//and set channel id in manifest
        notificationBuilder.setSmallIcon(R.mipmap.ic_app_icon);
        notificationManager.notify(123,notificationBuilder.build());
    }
}

