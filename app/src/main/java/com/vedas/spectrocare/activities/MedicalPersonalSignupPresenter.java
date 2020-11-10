package com.vedas.spectrocare.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;

public class MedicalPersonalSignupPresenter {
    MedicalPersonaSignupView view;

    public MedicalPersonalSignupPresenter(MedicalPersonaSignupView view) {
        this.view = view;
    }

    public void dialogebox(AlertDialog.Builder builder, String title, String message, String ok){

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


}
