package com.vedas.spectrocare.Alert;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.vedas.spectrocare.R;

import java.util.Objects;

/**
 * Created by .
 */
public class RefreshShowingDialog extends Dialog {
    Context context;
    AlertDialog alertDialog;
    Dialog dialog;
    public RefreshShowingDialog(Context context1) {
        super(context1);
        Log.e("hideRefreshDialog", "call");
        context = context1;
        AlertDialog.Builder dialog1 = new AlertDialog.Builder(context);
        alertDialog=dialog1.create();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void showAlert() {
        if (!((Activity) context).isFinishing()) {
            if (alertDialog.isShowing()){
                alertDialog.dismiss();
            }
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alertbox_layout, null);
            AlertDialog.Builder dialog1 = new AlertDialog.Builder(context);
            // ProgressBar progressBar = dialogView.findViewById(R.id.progressBar);
            dialog1.setView(dialogView);
            alertDialog = dialog1.create();
            alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.transparent_bg);
           // alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.layout_cornerbg);
         //   alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alertDialog.show();
           Objects.requireNonNull(alertDialog.getWindow()).setLayout(450, 400);
        }
    }
    public void dialogue() {
        if (!((Activity) context).isFinishing()) {
           /* RotateAnimation rotate = new RotateAnimation(0, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);

            rotate.setDuration(2000);
            rotate.setRepeatCount(Animation.INFINITE);
            imageView.setAnimation(rotate);*/
            dialog.show();

        }
    }
    public RefreshShowingDialog(Context context1, String msg) {
        super(context1);
        context = context1;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alertbox_layout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        TextView textView = (TextView) dialog.findViewById(R.id.connecting);
        textView.setText(msg);
     /*   dialog.getWindow().setBackgroundDrawableResource(R.drawable.layout_cornerbg);
        imageView = (ImageView) dialog.findViewById(R.id.image_rottate);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.layout_cornerbg);*/
    }
    public void hideRefreshDialog() {
        Log.e("hideRefreshDialog", "call");
        alertDialog.dismiss();
    }
    public void dismissDialogue() {
        Log.e("hideRefreshDialog", "call");
        dialog.dismiss();
    }
}


/*
package com.vedas.spectrocare.Alert;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.vedas.spectrocare.R;


*/
/**
 * Created by .
 *//*

public class RefreshShowingDialog extends Dialog {
    Dialog dialog;
    ImageView imageView;
    Context context;


    public RefreshShowingDialog(Context context1) {
        super(context1);
        context = context1;
        dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_animate);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        TextView textView=(TextView)dialog.findViewById(R.id.connecting);
        textView.setText("Connecting...");
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.layout_cornerbg);
        imageView=(ImageView)dialog.findViewById(R.id.image_rottate) ;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.layout_cornerbg);
      */
/*  TextView text=(TextView)dialog.findViewById(R.id.connecting);
        text.setText(LanguageTextController.getInstance().currentLanguageDictionary.get(LanguagesKeys.CONNECTING_KEY));
*//*

    }
    public RefreshShowingDialog(Context context1, String msg) {
        super(context1);
        context = context1;
        dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_animate);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        TextView textView=(TextView)dialog.findViewById(R.id.connecting);
        textView.setText(msg);
        //textView.setText(LanguageTextController.getInstance().currentLanguageDictionary.get(LanguagesKeys.CONNECTING_KEY));
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.layout_cornerbg);
        imageView=(ImageView)dialog.findViewById(R.id.image_rottate) ;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.layout_cornerbg);
      */
/*  TextView text=(TextView)dialog.findViewById(R.id.connecting);
        text.setText(LanguageTextController.getInstance().currentLanguageDictionary.get(LanguagesKeys.CONNECTING_KEY));
*//*

    }

    public  void  showAlert(){

        if(!((Activity) context).isFinishing()) {

            RotateAnimation rotate = new RotateAnimation(0, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);

            rotate.setDuration(600);
            rotate.setRepeatCount(Animation.INFINITE);
            imageView.setAnimation(rotate);
            dialog.show();

        }

    }

    public void hideRefreshDialog(){
        Log.e("hideRefreshDialog", "call");
        dialog.dismiss();
    }
}
*/
