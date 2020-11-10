package com.vedas.spectrocare.PatientMyDeviceModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.vedas.spectrocare.R;
import com.vedas.spectrocare.patientModuleAdapter.PatientRecentDeviceAdapter;

public class PatientMyDeviceActivity extends AppCompatActivity {
RecyclerView recentDevicesView,otherDevicesView;
ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_my_device);
        recentDevicesView = findViewById(R.id.recent_devices_view);
        otherDevicesView = findViewById(R.id.other_devices_view);
        imgBack = findViewById(R.id.img_back_arrow);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        PatientRecentDeviceAdapter deviceAdapter = new PatientRecentDeviceAdapter(this);
        recentDevicesView.setLayoutManager(new LinearLayoutManager(this));
        recentDevicesView.setAdapter(deviceAdapter);
        otherDevicesView.setLayoutManager(new LinearLayoutManager(this));
        OtherDevicesAdapter otherDevicesAdapter = new OtherDevicesAdapter();
        otherDevicesView.setAdapter(otherDevicesAdapter);

    }
    public class OtherDevicesAdapter extends RecyclerView.Adapter<OtherDevicesAdapter.OtherDeviceHolder>{

        @NonNull
        @Override
        public OtherDevicesAdapter.OtherDeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View otherDevicesView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_devices_recyclerview, parent, false);
            return new OtherDeviceHolder(otherDevicesView);
        }

        @Override
        public void onBindViewHolder(@NonNull OtherDevicesAdapter.OtherDeviceHolder holder, int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.layout.setVisibility(View.GONE);
                }
            });

        }

        @Override
        public int getItemCount() {
            return 1;
        }

        public class OtherDeviceHolder extends RecyclerView.ViewHolder {
            RelativeLayout layout;
            public OtherDeviceHolder(@NonNull View itemView) {
                super(itemView);
                layout = itemView.findViewById(R.id.layout_main);
            }
        }
    }
}
