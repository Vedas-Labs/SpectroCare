package com.vedas.spectrocare.PatientModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skyfishjy.library.RippleBackground;
import com.spectrochips.spectrumsdk.FRAMEWORK.SCConnectionHelper;
import com.spectrochips.spectrumsdk.FRAMEWORK.SCTestAnalysis;
import com.spectrochips.spectrumsdk.FRAMEWORK.SpectroCareSDK;
import com.vedas.spectrocare.BLEConnectionModule.FileViewControllerActivity;
import com.vedas.spectrocare.R;

import java.util.ArrayList;
public class BleScanningActivity extends AppCompatActivity {
    RelativeLayout rl_back,rl_ripplebg,rl_list;
    Button cancelBtn,nextBtn;
    BluetoothDevice bluetoothDevice;
    public static final int ACTION_REQUEST_ENABLE_BT = 1;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private ArrayList<BluetoothDevice> devciesArray = new ArrayList<BluetoothDevice>();
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
        loadRecyclerView();
        /*checkLocationPermission();
        checkBluetoothIsEnable();*/
    }
    private void loadIDs(){
        rl_back=findViewById(R.id.back);
        cancelBtn=findViewById(R.id.cancel);
        rl_ripplebg=findViewById(R.id.ripplebg);
        rl_list=findViewById(R.id.rl_list);
        nextBtn=findViewById(R.id.next);

        final RippleBackground rippleBackground=(RippleBackground) findViewById(R.id.content);
        ImageView imageView=(ImageView)findViewById(R.id.centerImage);
        rippleBackground.startRippleAnimation();

        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SCConnectionHelper.getInstance().startScan(false);
                onBackPressed();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rippleBackground.stopRippleAnimation();
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FileViewControllerActivity.class));
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               rl_ripplebg.setVisibility(View.GONE);
               rl_list.setVisibility(View.VISIBLE);
                checkLocationPermission();
                checkBluetoothIsEnable();
            }
        }, 5000);
    }
    public void activateNotification() {
        SCConnectionHelper.getInstance().activateScanNotification(new SCConnectionHelper.ScanDeviceInterface() {
            @Override
            public void onSuccessForConnection(String msg) {
                Log.e("onSuccessForConnection", "call");
                Toast.makeText(getApplicationContext(),"connected to"+bluetoothDevice.getName(), Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(getApplicationContext(), FileViewControllerActivity.class));
            }
            @Override
            public void onSuccessForScanning(final ArrayList<BluetoothDevice> devcies, boolean msg) {
                Log.e("onSuccessForScanning", "call"+devcies.size());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        devciesArray=devcies;
                        adapter.notifyDataSetChanged();
                    }
                });
            }
            @Override
            public void onFailureForConnection(String error) {
                Log.e("onFailureForConnection", "call");
                adapter.notifyDataSetChanged();
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
       /* RelativeLayout back =  findViewById(R.id.home);
        ImageView refresh = (ImageView) findViewById(R.id.refresh);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SCConnectionHelper.getInstance().startScan(true);
                adapter.notifyDataSetChanged();
            }
        });*/

     /*   back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SCConnectionHelper.getInstance().startScan(false);
                finish();
            }
        });*/
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
            SCConnectionHelper.getInstance().startScan(true);
        }
    }

    public boolean checkLocationPermission() {
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
    }
    public class devicesAdapter extends RecyclerView.Adapter<devicesAdapter.ViewHolder> {
        Context ctx;
        public devicesAdapter(Context ctx) {
            this.ctx = ctx;
        }
        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView name;
            ImageView image;

            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.testName);
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
            BluetoothDevice device=devciesArray.get(position);
            holder.name.setText(device.getName());

            if (selectedPosition == position) {
                  holder.image.setVisibility(View.VISIBLE);
                SCConnectionHelper.getInstance().startScan(false);
                bluetoothDevice = devciesArray.get(position);
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
            if (devciesArray.size()>0) {
                return devciesArray.size();
            }else {
                return 0;
            }
        }

    }
    @Override
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == ACTION_REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        } else {
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
