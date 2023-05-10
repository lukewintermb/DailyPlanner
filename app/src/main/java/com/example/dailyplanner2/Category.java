package com.example.dailyplanner2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.dailyplanner2.DB.AppDataBase;

@Entity(tableName = AppDataBase.CATEGORY_TABLE)
public class Category {

    @PrimaryKey(autoGenerate = true)
    private int mCategoryId;

    private int mUserId;

    private String mCategoryName;

    private int mCategoryColor;

    public Category(String categoryName, int categoryColor, int userId) {
        mCategoryName = categoryName;
        mCategoryColor = categoryColor;
        mUserId = userId;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(int categoryId) {
        mCategoryId = categoryId;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String categoryName) {
        mCategoryName = categoryName;
    }

    public int getCategoryColor() {
        return mCategoryColor;
    }

    public void setCategoryColor(int categoryColor) {
        mCategoryColor = categoryColor;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }
}
