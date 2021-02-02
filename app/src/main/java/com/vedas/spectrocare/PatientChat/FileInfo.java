package com.vedas.spectrocare.PatientChat;

public class FileInfo {

    public FileInfo() {
    }

    public FileInfo(FileInfo fileInfo) {
        this.id = fileInfo.id;
        this.name = fileInfo.name;
        this.offset = fileInfo.offset;
        this.size = fileInfo.size;
        this.data = fileInfo.data;
        this.type = fileInfo.type;
        this.roomID = fileInfo.roomID;
        this.userID = fileInfo.userID;
        this.messageID = fileInfo.messageID;
        this.messageType = fileInfo.messageType;
    }

    private String id;
    private String name;
    private int offset;
    private int size;
    private byte[] data;
    private String type;
    private String roomID;
    private String userID;
    private String messageID;
    private String messageType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
