package com.example.dailyplanner2.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.dailyplanner2.Category;
import com.example.dailyplanner2.Todo;
import com.example.dailyplanner2.User;

@Database(entities = {Todo.class, User.class, Category.class}, version = 5)
public abstract class AppDataBase extends RoomDatabase {
    public static final String DATABASE_NAME = "Todo.db";
    public static final String TODO_TABLE = "todo_table";
    public static final String USER_TABLE = "user_table";
    public static final String CATEGORY_TABLE = "category_table";
    private static volatile AppDataBase instance;
    private static final Object LOCK = new Object();

    public abstract TodoDAO TodoDAO();

    public static AppDataBase getInstance(Context context){
        if(instance == null) {
            synchronized (LOCK) {
                if(instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDataBase.class,
                            DATABASE_NAME).allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }


}
