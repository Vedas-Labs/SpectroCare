package com.vedas.spectrocare.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vedas.spectrocare.PatientModule.PatientHomeActivity;
import com.vedas.spectrocare.R;

public class PasswordChangedActivity extends AppCompatActivity {
    ImageView backIcon;
Button loginBtn;
TextView txtDisc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_changed);
        backIcon = findViewById(R.id.back_icon);
        loginBtn=findViewById(R.id.btn_login_now);
        txtDisc = findViewById(R.id.title_change_description);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onBackPressed();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
               /* Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
                startActivity(intent);*/
                startActivity(new Intent(getApplicationContext(), PatientHomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        });
    }
}
