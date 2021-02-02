package com.vedas.spectrocare.PatientServerApiModel;

import com.vedas.spectrocare.ServerApiModel.AttachmentServerObjects;

import java.util.ArrayList;

public class ChatRoomMessageModel {
    //for load doctor details in messages page.
    private String roomID;
    private String doctorName;
    private String profile;
    //
    private String messageID;
    private String message;
    private String userID;
    private String timeStamp;
    private String isRead;
    private String isLiked;
    private String type;
    private String isEdited;
    private String message1;
    private String repliedMessageID;
    private String recipientRepliedName;
    private ArrayList<AttachmentServerObjects> attachmentObjects;

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsEdited() {
        return isEdited;
    }

    public void setIsEdited(String isEdited) {
        this.isEdited = isEdited;
    }

    public String getMessage1() {
        return message1;
    }

    public void setMessage1(String message1) {
        this.message1 = message1;
    }

    public String getRepliedMessageID() {
        return repliedMessageID;
    }

    public void setRepliedMessageID(String repliedMessageID) {
        this.repliedMessageID = repliedMessageID;
    }

    public String getRecipientRepliedName() {
        return recipientRepliedName;
    }

    public void setRecipientRepliedName(String recipientRepliedName) {
        this.recipientRepliedName = recipientRepliedName;
    }

    public ArrayList<AttachmentServerObjects> getAttachmentObjects() {
        return attachmentObjects;
    }

    public void setAttachmentObjects(ArrayList<AttachmentServerObjects> attachmentObjects) {
        this.attachmentObjects = attachmentObjects;
    }
}
