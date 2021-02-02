package com.vedas.spectrocare.PatinetControllers;

import android.content.Context;
import android.util.Log;

import com.vedas.spectrocare.model.CardDetailsModel;

import java.util.ArrayList;

public class CardDetailsController {
    public Context context;
    public static CardDetailsController obj;
    public ArrayList<CardDetailsModel> cardDetailList= new ArrayList<>();
    CardDetailsModel cardDetailsModel;
    public static CardDetailsController getInstance(){
        if (obj==null){
            Log.e("shb","dh");
            obj = new CardDetailsController();
        }else {
            Log.e("shdsb","dh");
        }
        return obj;
    }
    public void fillContent(Context context1) {
        context = context1;
        cardDetailList=new ArrayList<>();
    }
  /*  public ArrayList<CardDetailsModel> getCardDetailList() {
        return cardDetailList;
    }

    public void setCardDetailList(ArrayList<CardDetailsModel> cardDetailList) {
        this.cardDetailList = cardDetailList;
    }*/

   /* public CardDetailsModel getCardDetailsModel() {
        return cardDetailsModel;
    }*/

   /* public void setCardDetailsModel(CardDetailsModel cardDetailsModel) {
        this.cardDetailsModel = cardDetailsModel;
    }*/
}
