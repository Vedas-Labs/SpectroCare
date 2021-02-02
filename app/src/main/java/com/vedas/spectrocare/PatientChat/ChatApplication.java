package com.vedas.spectrocare.PatientChat;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.vedas.spectrocare.DataBase.PatientLoginDataController;
import com.vedas.spectrocare.ServerApi;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;


public class ChatApplication extends Application {
    private Socket mSocket;

    public io.socket.client.Socket getSocket() {
        try {

            String name;
            SharedPreferences preferences=getApplicationContext().getSharedPreferences("temp", 0);
            name=preferences.getString("strPatientID",null);
          //  name = PatientLoginDataController.getInstance().currentPatientlProfile.getPatientId();
            Log.e("patienttt","Id:: "+name);

            IO.Options mOptions = new IO.Options();
            mOptions.query = "userID=" + name+"&userType="+"Patient";
           // mOptions.query = "userType="+"Patient";
            mSocket = IO.socket(ServerApi.img_home_url, mOptions);

            // mSocket = IO.socket(ServerApi.img_home_url);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return mSocket;
    }
}
