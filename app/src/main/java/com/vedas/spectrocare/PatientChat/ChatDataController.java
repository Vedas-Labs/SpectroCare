package com.vedas.spectrocare.PatientChat;

import java.util.ArrayList;

public class ChatDataController {
    public static ChatDataController chatObj;
    ArrayList<ChatModel> chatModelArrayList;
    public static ChatDataController getInstance(){
        if (chatObj==null){
            chatObj = new ChatDataController();
            return chatObj;
        }
        return chatObj;
    }

    public static boolean isNull(){
        if (chatObj==null){
            return true;
        }else
            return false;
    }

    public ArrayList<ChatModel> getChatModelArrayList() {
        return chatModelArrayList;
    }

    public void setChatModelArrayList(ArrayList<ChatModel> chatModelArrayList) {
        this.chatModelArrayList = chatModelArrayList;
    }
}
