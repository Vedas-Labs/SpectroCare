package com.vedas.spectrocare.PatinetControllers;

import android.util.Log;

import com.vedas.spectrocare.model.PaymentModel;

public class PaymentControll {

    public static PaymentControll myObj;
    PaymentModel paymentModel;

    public static PaymentControll getInstance(){
        if (myObj==null){
            Log.e("shb","dh");
            myObj = new PaymentControll();
        }else {
            Log.e("shdsb","dh");
        }
        return myObj;
    }

    public PaymentModel getPaymentModel() {
        return paymentModel;
    }

    public void setPaymentModel(PaymentModel paymentModel) {
        this.paymentModel = paymentModel;
    }

    public  static PaymentControll setNull(){

        myObj=null;
        return myObj;
    }

}
