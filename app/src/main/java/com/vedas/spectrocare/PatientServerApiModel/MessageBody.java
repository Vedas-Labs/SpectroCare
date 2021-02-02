package com.vedas.spectrocare.PatientServerApiModel;

public class MessageBody {
    private String title;
    private String body;
    private String messageType;
    InboxData data;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public InboxData getData() {
        return data;
    }

    public void setData(InboxData data) {
        this.data = data;
    }
}
