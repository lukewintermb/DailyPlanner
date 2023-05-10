package com.example.dailyplanner2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.dailyplanner2.DB.AppDataBase;

@Entity(tableName = AppDataBase.TODO_TABLE)
public class Todo {
    @PrimaryKey(autoGenerate = true)
    private int mTodoId;
    private String mName;
    private String mDay;

    private int mUserId;
    private int mCategoryId;

    public Todo(String name, int userId, String day, int categoryId) {
        mName = name;
        mUserId = userId;
        mDay = day;
        mCategoryId = categoryId;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "mTodoId=" + mTodoId +
                ", mName='" + mName + '\'' +
                '}';
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(int categoryId) {
        mCategoryId = categoryId;
    }

    public String getDay() {
        return mDay;
    }

    public void setDay(String day) {
        mDay = day;
    }

    public int getTodoId() {
        return mTodoId;
    }

    public void setTodoId(int todoId) {
        mTodoId = todoId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }
}
