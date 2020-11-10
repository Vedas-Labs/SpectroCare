package com.vedas.spectrocare.PatientChat;

public class ChatModel {
    String iD;
    String text;
    String image;
    String video;
    String file;

    public ChatModel(String iD, String text, String image, String video, String file) {
        this.iD = iD;
        this.text = text;
        this.image = image;
        this.video = video;
        this.file = file;
    }

    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
