/*
package com.vedas.spectrocare.BLEConnectionModule;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skyfishjy.library.RippleBackground;
import com.spectrochips.spectrumsdk.FRAMEWORK.SCConnectionHelper;
import com.spectrochips.spectrumsdk.FRAMEWORK.SCTestAnalysis;
import com.spectrochips.spectrumsdk.FRAMEWORK.SpectroCareSDK;
import com.vedas.spectrocare.R;

import java.util.ArrayList;

public class BleScanningActivity extends AppCompatActivity {
    RelativeLayout rl_back,rl_ripplebg,rl_list;
    Button cancelBtn,nextBtn;
    BluetoothDevice bluetoothDevice;
    public static final int ACTION_REQUEST_ENABLE_BT = 1;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private ArrayList<BluetoothDevice> devicesArray = new ArrayList<BluetoothDevice>();
    int selectedPosition = -1;
    devicesAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_scanning);
        loadIDs();
        activateNotification();
        SpectroCareSDK.getInstance().fillContext(getApplicationContext());
        SCConnectionHelper.getInstance().initializeAdapterAndServcie();
        SCConnectionHelper.getInstance().startScan(true);
        loadRecyclerView();
        checkLocationPermission();
        checkBluetoothIsEnable();
    }
    private void loadIDs(){
        rl_back=findViewById(R.id.back);
        cancelBtn=findViewById(R.id.cancel);
        rl_ripplebg=findViewById(R.id.ripplebg);
        rl_list=findViewById(R.id.rl_list);
        nextBtn=findViewById(R.id.next);

        rl_ripplebg.setVisibility(View.VISIBLE);
        rl_list.setVisibility(View.GONE);

        final RippleBackground rippleBackground=(RippleBackground) findViewById(R.id.content);
        ImageView imageView=(ImageView)findViewById(R.id.centerImage);
        rippleBackground.startRippleAnimation();

        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SCConnectionHelper.getInstance().startScan(false);
                finish();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rippleBackground.stopRippleAnimation();
                finish();
            }
        });

        nextBtn.setVisibility(View.GONE);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(getApplicationContext(),CollectSampleActivity.class));
            }
        });
    }
    private void loadHandler(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rl_ripplebg.setVisibility(View.GONE);
                rl_list.setVisibility(View.VISIBLE);
            }
        }, 3000);
    }
    public void activateNotification() {
        SCConnectionHelper.getInstance().activateScanNotification(new SCConnectionHelper.ScanDeviceInterface() {
            @Override
            public void onSuccessForConnection(String msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("onSuccessForConnection", "call");
                        Toast.makeText(getApplicationContext(),"connected to"+bluetoothDevice.getName(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), FileViewControllerActivity.class));
                    }
                });
            }
            @Override
            public void onSuccessForScanning(final ArrayList<BluetoothDevice> devcies, boolean msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("onSuccessForScanning", "call"+devcies.size());
                        devicesArray =devcies;
                        if(devicesArray.size()>0){
                            rl_ripplebg.setVisibility(View.GONE);
                            rl_list.setVisibility(View.VISIBLE);
                            nextBtn.setVisibility(View.VISIBLE);
                        }else{
                            nextBtn.setVisibility(View.GONE);
                        }
                        loadRecyclerView();
                        adapter.notifyDataSetChanged();
                    }
                });
            }
            @Override
            public void onFailureForConnection(String error) {
                Log.e("onFailureForConnection", "call");
                //adapter.notifyDataSetChanged();
                // showMessage("Device not connected.");
            }

            @Override
            public void uartServiceClose(String error) {
                //  showMessage("Not support Ble Service");
                adapter.notifyDataSetChanged();

            }
        });
    }
    private void loadRecyclerView() {
        adapter = new devicesAdapter(this);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        */
/* RelativeLayout back =  findViewById(R.id.home);
        ImageView refresh = (ImageView) findViewById(R.id.refresh);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SCConnectionHelper.getInstance().startScan(true);
                adapter.notifyDataSetChanged();
            }
        });*//*


       */
/*back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SCConnectionHelper.getInstance().startScan(false);
                finish();
            }
        });*//*

    }
    public void checkBluetoothIsEnable() {
        if (SCConnectionHelper.getInstance().mBluetoothAdapter == null) {
            finish();
            return;
        }
        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!SCConnectionHelper.getInstance().mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, ACTION_REQUEST_ENABLE_BT);
        } else {
            loadHandler();
            SCConnectionHelper.getInstance().startScan(true);
        }
    }
    public void checkLocationPermission() {
       */
/* if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Location Permission1");
            builder.setMessage("The app needs location permissions. Please grant this permission to continue using the features of the app.");
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    requestPermissions(new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },MY_PERMISSIONS_REQUEST_LOCATION);
                }
            });
            builder.setNegativeButton(android.R.string.no, null);
            builder.show();

        }*//*

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Location Permission1");
                builder.setMessage("The app needs location permissions. Please grant this permission to continue using the features of the app.");
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermissions(new String[]{
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        },MY_PERMISSIONS_REQUEST_LOCATION);
                    }
                });
                builder.setNegativeButton(android.R.string.no, null);
                builder.show();

            }
        }else{
            Log.e("elsecall","call");
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean isGpsProviderEnabled, isNetworkProviderEnabled;
            isGpsProviderEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkProviderEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGpsProviderEnabled && !isNetworkProviderEnabled) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Location Permission");
                builder.setMessage("The app needs location permissions. Please grant this permission to continue using the features of the app.");
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton(android.R.string.no, null);
                builder.show();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("location permission", "coarse location permission granted");
                }
                else {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            }
        }
    }
    */
/* public void checkLocationPermission() {
       if (ContextCompat.checkSelfPermission(BleScanningActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(BleScanningActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(BleScanningActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(BleScanningActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }*//*

    public class devicesAdapter extends RecyclerView.Adapter<devicesAdapter.ViewHolder> {
        Context ctx;
        public devicesAdapter(Context ctx) {
            this.ctx = ctx;
        }
        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView name,ipaddress,status;
            ImageView image;

            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.testName);
                ipaddress = (TextView) itemView.findViewById(R.id.id);
                status = (TextView) itemView.findViewById(R.id.status);
                image = (ImageView) itemView.findViewById(R.id.image);
                itemView.setOnClickListener(this);
            }
            @Override
            public void onClick(View v) {

            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.downloadjsonlist, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            BluetoothDevice device= devicesArray.get(position);
            holder.name.setText(device.getName());
            holder.ipaddress.setText(device.getAddress());
            if (selectedPosition == position) {
                holder.image.setVisibility(View.VISIBLE);
                SCConnectionHelper.getInstance().startScan(false);
                bluetoothDevice = devicesArray.get(position);
                holder.status.setText("Connected");
                SCTestAnalysis.getInstance().mService.connect(bluetoothDevice.getAddress());
            } else {
                holder.image.setVisibility(View.GONE);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedPosition != position) {
                        selectedPosition = position;
                        notifyDataSetChanged();
                    } else {
                        selectedPosition = -1;
                        notifyDataSetChanged();
                    }
                }
            });
        }
        @Override
        public int getItemCount() {
            if (devicesArray.size()>0) {
                return devicesArray.size();
            }else {
                return 0;
            }
        }
    }
    */
/*@Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(BleScanningActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }
*//*

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == ACTION_REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        } else {
           */
/* rl_ripplebg.setVisibility(View.VISIBLE);
            loadHandler();*//*

            SCConnectionHelper.getInstance().startScan(true);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        SCConnectionHelper.getInstance().startScan(false);
    }
}
*/
