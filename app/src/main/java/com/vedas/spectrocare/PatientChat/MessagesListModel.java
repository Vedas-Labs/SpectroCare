package com.vedas.spectrocare.PatientChat;

import java.util.ArrayList;

public class MessagesListModel {
    String messageID;
    String message;
    String userID;
    String timeStamp;
    boolean isRead;
    boolean isLiked;
    String type;
    boolean isEdited;
    String message1;
    String repliedMessageID;
    String recipientRepliedName;
    ArrayList<ChatAttachmentModel> attachments;

    public ArrayList<ChatAttachmentModel> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<ChatAttachmentModel> attachments) {
        this.attachments = attachments;
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

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
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
}
