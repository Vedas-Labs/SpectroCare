package com.vedas.spectrocare.PatientChat;

import java.util.ArrayList;

public class ChatMembersListModel {
    ArrayList<ChatArrayModel> chatMembersList;

    public ChatMembersListModel(ArrayList<ChatArrayModel> chatMembersList) {
        this.chatMembersList = chatMembersList;
    }

    public ArrayList<ChatArrayModel> getChatMembersList() {
        return chatMembersList;
    }

    public void setChatMembersList(ArrayList<ChatArrayModel> chatMembersList) {
        this.chatMembersList = chatMembersList;
    }
}
