package com.vedas.spectrocare.PatientChat;

import java.util.ArrayList;

public class ChatDataController {
    public static ChatDataController chatObj;
    ArrayList<ChatModel> chatModelArrayList;
    ArrayList<MessageModel> messageModelArrayList;
    public static boolean isConnected;
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
    public static ChatDataController setNull(){
        chatObj=null;
        return chatObj;
    }


    public ArrayList<MessageModel> getMessageModelArrayList() {
        return messageModelArrayList;
    }

    public void setMessageModelArrayList(ArrayList<MessageModel> messageModelArrayList) {
        this.messageModelArrayList = messageModelArrayList;
    }

    public ArrayList<ChatModel> getChatModelArrayList() {
        return chatModelArrayList;
    }

    public void setChatModelArrayList(ArrayList<ChatModel> chatModelArrayList) {
        this.chatModelArrayList = chatModelArrayList;
    }
}
