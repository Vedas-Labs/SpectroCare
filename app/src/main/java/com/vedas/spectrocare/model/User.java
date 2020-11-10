package com.vedas.spectrocare.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

public class User {
    @DatabaseField (generatedId = true)
    private int id;

    @DatabaseField
    private String password;

    @DatabaseField
    private String latitude;

    @DatabaseField
    private String longitude;



    @DatabaseField
    private String registerLocation;

    @DatabaseField
    private Boolean isVerified;

    @DatabaseField
    String preferedLanguage;



    @DatabaseField
    private String token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getRegisterLocation() {
        return registerLocation;
    }

    public void setRegisterLocation(String registerLocation) {
        this.registerLocation = registerLocation;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public String getPreferedLanguage() {
        return preferedLanguage;
    }

    public void setPreferedLanguage(String preferedLanguage) {
        this.preferedLanguage = preferedLanguage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
