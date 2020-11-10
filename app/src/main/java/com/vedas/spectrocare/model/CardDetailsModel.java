package com.vedas.spectrocare.model;

public class CardDetailsModel {
    String cardNo;
    String cvv;
    String upiID;
    String expairDate;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getUpiID() {
        return upiID;
    }

    public void setUpiID(String upiID) {
        this.upiID = upiID;
    }

    public String getExpairDate() {
        return expairDate;
    }

    public void setExpairDate(String expairDate) {
        this.expairDate = expairDate;
    }
}
