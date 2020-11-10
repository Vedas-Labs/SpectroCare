package com.vedas.spectrocare.PatientMyDeviceModule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.vedas.spectrocare.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutDeviceActiivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_device_actiivty);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.back) void backAction() {
        // TODO call server...
        onBackPressed();
    }
}
