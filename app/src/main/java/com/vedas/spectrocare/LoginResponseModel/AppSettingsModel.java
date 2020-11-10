package com.vedas.spectrocare.LoginResponseModel;

import com.vedas.spectrocare.LoginResponseModel.NotificationItemModel;

import java.util.ArrayList;

public class AppSettingsModel {
    String dateFormat;
    String timeFormatType;
    String timeFormat;
    String region;
    String language;
    String unit;
    boolean notification;
    ArrayList<NotificationItemModel> notificationItems;

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getTimeFormatType() {
        return timeFormatType;
    }

    public void setTimeFormatType(String timeFormatType) {
        this.timeFormatType = timeFormatType;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public ArrayList<NotificationItemModel> getNotificationItems() {
        return notificationItems;
    }

    public void setNotificationItems(ArrayList<NotificationItemModel> notificationItems) {
        this.notificationItems = notificationItems;
    }
}
