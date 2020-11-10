package com.vedas.spectrocare.PatientChat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vedas.spectrocare.R;


import java.io.IOException;

import uk.co.senab.photoview.PhotoView;


public class FullImageActivity extends AppCompatActivity {


    PhotoView photoView;
    ImageView backAttachPreview;
    TextView toolName;
    String strPic;
    ImageView imgSet;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        toolName=findViewById(R.id.toolName);
        imgSet = findViewById(R.id.img_set);

        if(getIntent().hasExtra("name")){

            toolName.setText(getIntent().getStringExtra("name"));
        }

        photoView = findViewById(R.id.photozooom);
        photoView.setMaximumScale(10);
        backAttachPreview = findViewById(R.id.backAttachPreview);


        backAttachPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (getIntent().getStringExtra("proPic").contains("jpg")){
            Log.e("nnnniiill","fad"+getIntent().getStringExtra("proPic"));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inPurgeable = true;

            Bitmap bitmap1 = BitmapFactory.decodeFile(getIntent().getStringExtra("proPic"), options);
            Bitmap rotatedBitmap = rotatedImageBitmap(getIntent().getStringExtra("proPic"), bitmap1);
            bitmap = rotatedBitmap;
            Log.e("uuuuuf","afad"+strPic);
            imgSet.setImageBitmap(getResizedBitmap(rotatedBitmap, 500));
        }else{
            Glide.with(this)
                    .load(Uri.parse(getIntent().getStringExtra("proPic")))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).placeholder(R.drawable.profile)
                    .into(imgSet);
           // imgSet.setImageURI(Uri.parse(strPic));
        }
        Log.e("gfgh","kjhjk: "+getIntent().getStringExtra("proPic"));

/*
        Glide.with(this)
                .load(Uri.parse(getIntent().getStringExtra("proPic")))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).placeholder(R.drawable.profile)
                .into(photoView);
*/

               /* Glide.with(this).load()
                .apply(new RequestOptions().placeholder(R.drawable.default_pro_img))
                .into(photoView);
*/

    }
    private Bitmap rotatedImageBitmap(String photoPath, Bitmap bitmap) {
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(photoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }
        return rotatedBitmap;
    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}