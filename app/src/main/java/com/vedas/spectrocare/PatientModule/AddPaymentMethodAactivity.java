package com.vedas.spectrocare.PatientModule;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vedas.spectrocare.PatinetControllers.CardDetailsController;
import com.vedas.spectrocare.R;
import com.vedas.spectrocare.model.CardDetailsModel;

public class AddPaymentMethodAactivity extends AppCompatActivity {
    ToggleButton creditBtn, paypalBtn;
    LinearLayout layout_creditCard, layout_payPal;
    RelativeLayout rl_back;
    EditText edtEmail;
    TextView txtCardNo, txtDate, txtCvv;
    String strCardNo, strDate, strCvv, strEmail;
    Button btnAdd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpayment);
        loadIds();
    }

    private void loadIds() {
        creditBtn = findViewById(R.id.toggle_1);
        paypalBtn = findViewById(R.id.toggle_2);
        layout_creditCard = findViewById(R.id.linear_creditcared);
        layout_payPal = findViewById(R.id.linear_paypal);
        rl_back = findViewById(R.id.back);
        btnAdd = findViewById(R.id.btn_add);
        edtEmail = findViewById(R.id.emailaddress);
        txtCardNo = findViewById(R.id.cardnum);
        txtCvv = findViewById(R.id.txt_cvv);
        txtDate = findViewById(R.id.date);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        creditBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    paypalBtn.setChecked(true);
                    layout_payPal.setVisibility(View.VISIBLE);
                    layout_creditCard.setVisibility(View.GONE);
                } else {
                    paypalBtn.setChecked(false);
                    layout_creditCard.setVisibility(View.VISIBLE);
                    layout_payPal.setVisibility(View.GONE);
                }
            }
        });

        paypalBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    creditBtn.setChecked(true);
                    layout_creditCard.setVisibility(View.GONE);
                    layout_payPal.setVisibility(View.VISIBLE);
                } else {
                    creditBtn.setChecked(false);
                    layout_payPal.setVisibility(View.GONE);
                    layout_creditCard.setVisibility(View.VISIBLE);
                }
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strCardNo = txtCardNo.getText().toString();
                strCvv = txtCvv.getText().toString();
                strDate = txtDate.getText().toString();
                CardDetailsModel cardDetailsModel=new CardDetailsModel();

                if (!edtEmail.getText().toString().isEmpty()) {

                    strEmail = edtEmail.getText().toString();
                    cardDetailsModel.setUpiID(strEmail);
                    CardDetailsController.getInstance().cardDetailList.add(cardDetailsModel);
                    startActivity(new Intent(AddPaymentMethodAactivity.this, PatientPaymentActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }else{

                    cardDetailsModel.setCardNo(strCardNo);
                    cardDetailsModel.setCvv(strCvv);
                    cardDetailsModel.setExpairDate(strDate);
                    CardDetailsController.getInstance().cardDetailList.add(cardDetailsModel);
                    Log.e("dfaaf","adsf"+strCardNo+strCvv+strDate);

                    Log.e("dfaaf","adsf"+CardDetailsController.getInstance().cardDetailList.size());
                    startActivity(new Intent(AddPaymentMethodAactivity.this, PatientPaymentActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    );
                }

            }
        });

    }
}
