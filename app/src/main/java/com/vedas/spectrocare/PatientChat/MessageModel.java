package com.vedas.spectrocare.PatientChat;

import java.util.ArrayList;

public class MessageModel {
    String _id;
    DetailsModel participant;
    ArrayList<MessagesListModel> messages;
    boolean isChatMute;
    String roomID;
    DetailsModel sender;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public DetailsModel getParticipant() {
        return participant;
    }

    public void setParticipant(DetailsModel participant) {
        this.participant = participant;
    }

    public ArrayList<MessagesListModel> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<MessagesListModel> messages) {
        this.messages = messages;
    }

    public boolean isChatMute() {
        return isChatMute;
    }

    public void setChatMute(boolean chatMute) {
        isChatMute = chatMute;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public DetailsModel getSender() {
        return sender;
    }

    public void setSender(DetailsModel sender) {
        this.sender = sender;
    }
}
