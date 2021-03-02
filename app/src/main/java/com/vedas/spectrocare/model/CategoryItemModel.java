package com.vedas.spectrocare.model;

import android.widget.ImageView;

public class CategoryItemModel {
    int categoryIcon;
    String categoryTitle;
    String Image;

    public CategoryItemModel(int categoryIcon, String categoryTitle) {
        this.categoryIcon = categoryIcon;
        this.categoryTitle = categoryTitle;
    }
    public CategoryItemModel(int categoryIcon, String categoryTitle,String image) {
        this.categoryIcon = categoryIcon;
        this.categoryTitle = categoryTitle;
        this.Image=image;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(int categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }
}
