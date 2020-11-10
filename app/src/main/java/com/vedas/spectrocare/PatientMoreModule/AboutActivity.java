package com.vedas.spectrocare.PatientMoreModule;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.j256.ormlite.stmt.query.In;
import com.vedas.spectrocare.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends AppCompatActivity {
    RelativeLayout rl_back,rl_feedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingsabout);
        ButterKnife.bind(this);
        loadIDS();
    }
    private void loadIDS(){
        rl_back=findViewById(R.id.back);
        rl_feedback=findViewById(R.id.feedback);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        rl_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),FeedbackActivity.class));
            }
        });
    }
    @OnClick(R.id.rlterms) void action() {
        // TODO call server...
        loadUrl();
    }
    @OnClick(R.id.rlprivacy) void dateAction() {
        // TODO call server...
        loadUrl();
    }
    private void loadUrl(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://spectrochips.com"));
        startActivity(browserIntent);
    }
}
