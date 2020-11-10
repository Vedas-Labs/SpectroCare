package com.vedas.spectrocare.ServerApiModel;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordInstance {
    @SerializedName("userID")
    private String userID;

    @SerializedName("oldPassword")
    private String oldPassword;
    @SerializedName("newPassword")
    private String newPassword;

    @SerializedName("message")
    private String message;
    @SerializedName("response")
    private String response;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
