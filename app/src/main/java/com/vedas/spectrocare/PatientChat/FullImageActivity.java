package com.vedas.spectrocare.PatientChat;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.ServerApi;


import android.transition.TransitionInflater;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


import uk.co.senab.photoview.PhotoView;


public class FullImageActivity extends AppCompatActivity {


    PhotoView photoView;
    ImageView backAttachPreview;
    TextView toolName;
    String strPic;
    ImageView imgSet,imgDownload,imgShare;
    Bitmap bitmap;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        toolName = findViewById(R.id.toolName);
        imgSet = findViewById(R.id.img_set);
        photoView = findViewById(R.id.photo_view);
        imgDownload = findViewById(R.id.img_download);
        imgShare = findViewById(R.id.img_share);
       /* if(getIntent().hasExtra("name")){

            toolName.setText(getIntent().getStringExtra("name"));
        }*/
        Log.e("gfgh", "kjhjk: " + getIntent().getStringExtra("imgUrl"));
      //  Picasso.get().load(Uri.parse(ServerApi.img_home_url+getIntent().getStringExtra("imgUrl"))).placeholder(R.drawable.profile_1).into(photoView);
        Glide.with(FullImageActivity.this)
                .load(Uri.parse(ServerApi.img_home_url+getIntent().getStringExtra("imgUrl")))
                .placeholder(R.drawable.profile_1)
                .into(photoView);
        if (getIntent().getStringExtra("type").equals("loadId")){
            Glide.with(FullImageActivity.this)
                    .load(Uri.parse(getIntent().getStringExtra("imgUrl")))
                    .placeholder(R.drawable.profile_1)
                    .into(photoView);
        }else{
            Glide.with(FullImageActivity.this)
                    .load(Uri.parse(ServerApi.img_home_url+getIntent().getStringExtra("imgUrl")))
                    .placeholder(R.drawable.profile_1)
                    .into(photoView);
        }


        getWindow().setSharedElementEnterTransition(TransitionInflater.from(FullImageActivity.this).inflateTransition(R.transition.img_transition));
        photoView.setTransitionName("photoTransition");

        photoView = findViewById(R.id.photozooom);
        photoView.setMaximumScale(10);
        backAttachPreview = findViewById(R.id.backAttachPreview);
        imgDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadImage(ServerApi.img_home_url+getIntent().getStringExtra("imgUrl"));
            }
        });
        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareItem(ServerApi.img_home_url+getIntent().getStringExtra("imgUrl"));
            }
        });
        backAttachPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
    public void shareItem(String url) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Picasso.get().load(url).into(new Target() {
            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                startActivity(Intent.createChooser(i, "Share Image"));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }
            @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
        });
    }
    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
    public void downloadImage(String urlStr){
        Toast.makeText(this, "File is downloading...", Toast.LENGTH_SHORT).show();
        String filename = "filename.jpg";
        String downloadUrlOfImage = urlStr;
        File direct =
                new File(Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        .getAbsolutePath() + "/" + "SpectroCare" + "/");


        if (!direct.exists()) {
            direct.mkdir();
        }

        DownloadManager dm = (DownloadManager) FullImageActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(downloadUrlOfImage);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(filename)
                .setMimeType("image/jpeg")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                        File.separator + "SpectroCare" + File.separator + filename);

        dm.enqueue(request);

    }
    public void shareImage(String ingUrl){
        Uri imageUri = Uri.parse("android.resource://your.package/drawable/fileName");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");

        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(intent , "Share"));

    }

    @Override
    public void onBackPressed() {
        //To support reverse transitions when user clicks the device back button
        supportFinishAfterTransition();
    }
    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });
    }
}