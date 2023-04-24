package com.example.dailyplanner2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.dailyplanner2.DB.AppDataBase;

@Entity(tableName = AppDataBase.TODO_TABLE)
public class Todo {
    @PrimaryKey(autoGenerate = true)
    private int mTodoId;
    private String mName;

    private int mUserId;

    public Todo(String name, int userId) {
        mName = name;
        mUserId = userId;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "mTodoId=" + mTodoId +
                ", mName='" + mName + '\'' +
                '}';
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
