package com.vedas.spectrocare.PatinetControllers;

import android.util.Log;

import com.vedas.spectrocare.model.CardDetailsModel;

import java.util.ArrayList;

public class CardDetailsController {
    public static CardDetailsController obj;
    ArrayList<CardDetailsModel> cardDetailList= new ArrayList<>();
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

    public ArrayList<CardDetailsModel> getCardDetailList() {
        return cardDetailList;
    }

    public void setCardDetailList(ArrayList<CardDetailsModel> cardDetailList) {
        this.cardDetailList = cardDetailList;
    }

    public CardDetailsModel getCardDetailsModel() {
        return cardDetailsModel;
    }

    public void setCardDetailsModel(CardDetailsModel cardDetailsModel) {
        this.cardDetailsModel = cardDetailsModel;
    }
}
