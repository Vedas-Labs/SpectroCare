package com.vedas.spectrocare.PatientChat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by wave on 10/6/2018.
 */

public class WifiReceiver extends BroadcastReceiver {
    String ssid = "";
    boolean isoNLINE = false;
    @Override
    public void onReceive(Context context, Intent intent) {

        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (info != null && info.isConnected()) {

            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            ssid = wifiInfo.getSSID();
            Boolean isConnectedStatus = info != null && info.isConnected();
            Log.e("ssidforwifi", "call" + ssid + isConnectedStatus);
            Log.e("ssidforwifi1", "call" + ssid +ChatDataController.isConnected);
            if(ChatDataController.isConnected != isConnectedStatus){
                if (isOnline()) {
                    ChatDataController.isConnected = isConnectedStatus;
                    Log.e("isConnectedCalled", "" + ChatDataController.isConnected);
                   // ChatDataController.getInstance().sycnOfflineData();
                    ChatDataController.isConnected=false;
                }
            }
           /* Log.e("ssidforwifi1", "call" + ssid +OfflineDataController.isConnected);
            if(OfflineDataController.isConnected != isConnectedStatus){
                if (isOnline()) {
                    OfflineDataController.isConnected = isConnectedStatus;
                    Log.e("isConnectedCalled", "" + OfflineDataController.isConnected);
                    OfflineDataController.getInstance().sycnOfflineData();
                    OfflineDataController.isConnected=false;
                }
            }*/
        }


       /* if (isConnected) {
            Log.e("ssidequals", "call" + isConnected + ssid.substring(1, ssid.length() - 1));
            String SSID = ssid.substring(1, ssid.length() - 1);
            ClientController.getInstance().checkDeviceConnection(SSID, context);
        }*/

      /*  ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isoNLINE = activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();

       if(isoNLINE){

       }

        if (isoNLINE) {
            Log.e("ConnectionReceiver", "call" + isoNLINE);

            OfflineDataController.getInstance().sycnOfflineData();
        } else {
            *//*Toast toast = Toast.makeText(context, "Check Internet Connection", Toast.LENGTH_SHORT);
            View view1 = toast.getView();
            view1.setBackgroundColor(Color.parseColor("#FF0012"));
            view1.setMinimumWidth(650);
            view1.setBackgroundResource(R.drawable.layout_toastbg);
            toast.show();*//*
        }*/
        /*HandlerThread handlerThread = new HandlerThread("data");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (isOnline()){
                    Log.e("wifireceiver", "call");
                    OfflineDataController.getInstance().sycnOfflineData();
                }
            }
        });*/
    }
    public boolean isOnline() {
        try {
            Process p1 = Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal == 0);
            Log.e("checkreachabilty", "call" + reachable);
            return reachable;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.e("checkreachabilty", "call" + "false");
        return false;
    }
}
