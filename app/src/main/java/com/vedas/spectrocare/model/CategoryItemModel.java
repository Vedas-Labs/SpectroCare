package com.vedas.spectrocare.model;

import android.widget.ImageView;

public class CategoryItemModel {
    int categoryIcon;
    String categoryTitle;

    public CategoryItemModel(int categoryIcon, String categoryTitle) {
        this.categoryIcon = categoryIcon;
        this.categoryTitle = categoryTitle;
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
