package com.vedas.spectrocare.activities;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import androidx.appcompat.app.AppCompatActivity;

import com.vedas.spectrocare.Controllers.PhysicalServerObjectDataController;
import com.vedas.spectrocare.DataBase.PhysicalExamDataController;
import com.vedas.spectrocare.DataBaseModels.PhysicalExamsDataModel;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;
import android.os.Environment;


public class AttachmentActivity extends AppCompatActivity {
    ImageView imgButton;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_attachment);
        imgButton = findViewById(R.id.img_back);
        webView=findViewById(R.id.webview);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhysicalServerObjectDataController.getInstance().isFromStaring=false;
                finish();
            }
        });
        loadAttachment();
    }
    private void loadAttachment() {
        if (PhysicalExamDataController.getInstance().currentPhysicalExamsData != null) {
            PhysicalExamsDataModel dataModel = PhysicalExamDataController.getInstance().currentPhysicalExamsData;
            String url = ServerApi.img_home_url + dataModel.getAttachment();
            Log.e("loadUll", "call" +url);

            startWebView(url);
        }
    }
    private void startWebView(String url) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            public void onLoadResource (WebView view, String url) {
                if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(AttachmentActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }
            }
            public void onPageFinished(WebView view, String url) {
                try{
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }

        });

        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);

        // Other webview options
        /*
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);
        */

        /*
         String summary = "<html><body>You scored <b>192</b> points.</body></html>";
         webview.loadData(summary, "text/html", null);
         */

        //Load url in webview
        webView.loadUrl(url);


    }

    // Open previous opened link isFromMedicalPerson history on webview when back button pressed

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        PhysicalServerObjectDataController.getInstance().isFromStaring=false;
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            // Let the system handle the back button
            super.onBackPressed();
        }
    }

}