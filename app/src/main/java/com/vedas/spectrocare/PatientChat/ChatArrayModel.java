package com.vedas.spectrocare.PatientChat;

import java.util.ArrayList;

public class ChatArrayModel {
    ArrayList<ChatModel> chatList;

    public ChatArrayModel(ArrayList<ChatModel> chatList) {
        this.chatList = chatList;
    }

    public ArrayList<ChatModel> getChatList() {
        return chatList;
    }

    public void setChatList(ArrayList<ChatModel> chatList) {
        this.chatList = chatList;
    }
}
