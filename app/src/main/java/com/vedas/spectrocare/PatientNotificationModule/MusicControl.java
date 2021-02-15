package com.vedas.spectrocare.PatientNotificationModule;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.vedas.spectrocare.R;

import java.util.Objects;
import java.util.Random;

import androidx.core.app.NotificationCompat;

import static tvi.webrtc.ContextUtils.getApplicationContext;

public class MusicControl {
    private static MusicControl sInstance;
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    PendingIntent pendingIntent,closingIntent;
    NotificationManager notificationManager;
    NotificationCompat.Builder notificationBuilder;
    boolean stopNotify;

    public MusicControl(Context context) {
        mContext = context;
    }

    public MusicControl(Context mContext, PendingIntent pendingIntent, PendingIntent closingIntent, NotificationManager notificationManager, NotificationCompat.Builder notificationBuilder) {
        this.mContext = mContext;
        this.pendingIntent = pendingIntent;
        this.closingIntent = closingIntent;
        this.notificationManager = notificationManager;
        this.notificationBuilder = notificationBuilder;
    }

    public static MusicControl getInstance(Context context, PendingIntent pendingIntent, PendingIntent closingIntent, NotificationManager notificationManager, NotificationCompat.Builder notificationBuilder) {
        if (sInstance == null) {
            sInstance = new MusicControl( context, pendingIntent,  closingIntent, notificationManager, notificationBuilder);
        }
        return sInstance;
    }

    public static MusicControl getStopInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MusicControl( context);
        }
        return sInstance;
    }

    public void playMusic() {
        Uri alarmSound =
                RingtoneManager. getDefaultUri (RingtoneManager.TYPE_RINGTONE );
        mMediaPlayer = MediaPlayer. create (mContext, alarmSound);
        mMediaPlayer.start();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.e("onCompletion","complete:: "+stopNotify);
                if (!stopNotify){
                    notificationBuilder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL).setWhen(System.currentTimeMillis()).
                            setChannelId("com.twilio.video.qickstart").
                            setVisibility(NotificationCompat.VISIBILITY_PUBLIC).
                            setSmallIcon(R.mipmap.ic_app_icon).setContentTitle("Missed Call")
                            .setContentText("Hey...! You missed a call buddy")
                            .setContentInfo("Info").setPriority(NotificationManager.IMPORTANCE_HIGH).setContentIntent(pendingIntent)
                            .setContentIntent(closingIntent)
                            .setAutoCancel(true)
                            .setWhen(0);

                    notificationManager.notify(new Random().nextInt(), notificationBuilder.build());

                }else{
                    notificationBuilder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL).setWhen(System.currentTimeMillis()).
                            setChannelId("com.twilio.video.qickstart").
                            setVisibility(NotificationCompat.VISIBILITY_PUBLIC).
                            setSmallIcon(R.mipmap.ic_app_icon).setContentTitle("Received Call")
                            .setContentText("Hey...! You Received a call buddy")
                            .setContentInfo("Info").setPriority(NotificationManager.IMPORTANCE_HIGH).setContentIntent(pendingIntent)
                            .setContentIntent(closingIntent)
                            .setAutoCancel(true)
                            .setWhen(0);

                    notificationManager.notify(new Random().nextInt(), notificationBuilder.build());

                }
            }
        });

    }


    public void stopMusic() {
        if(mMediaPlayer != null) {
            stopNotify = true;
            mMediaPlayer.stop();
            mMediaPlayer.seekTo(0);
        }
    }
}
