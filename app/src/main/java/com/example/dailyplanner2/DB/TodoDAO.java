package com.example.dailyplanner2.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dailyplanner2.Category;
import com.example.dailyplanner2.Todo;
import com.example.dailyplanner2.User;

import java.util.List;

@Dao
public interface TodoDAO {
    @Insert
    void insert(Todo... todos);

    @Update
    void update(Todo... todos);

    @Delete
    void delete(Todo todo);

    @Query("SELECT * FROM " + AppDataBase.TODO_TABLE)
    List<Todo> getTodos();

    @Query("SELECT * FROM " + AppDataBase.TODO_TABLE + " WHERE mTodoId = :todoId")
    List<Todo> getLogById(int todoId);

    @Query("SELECT * FROM " + AppDataBase.TODO_TABLE + " WHERE mUserId = :userId AND mDay = :day")
    List<Todo> getLogByDayAndUserId(int userId, String day);

    @Query("SELECT * FROM " + AppDataBase.TODO_TABLE + " WHERE mUserId = :userId")
    List<Todo> getLogByUserId(int userId);

    @Insert
    void insert(User...users);

    @Update
    void update(User...users);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE)
    List<User> getAllUsers();

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " WHERE mUserName = :username")
    User getUserByUsername(String username);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " WHERE mUserId = :userId")
    User getUserByUserId(String userId);


    @Insert
    void insert(Category...categories);

    @Update
    void update(Category...categories);

    @Delete
    void delete(Category...categories);

    @Query("SELECT * FROM " + AppDataBase.CATEGORY_TABLE + " WHERE mUserId = :userId")
    List<Category> getCategoriesByUserId(int userId);

    @Query("SELECT * FROM " + AppDataBase.CATEGORY_TABLE + " WHERE mCategoryId = :categoryId")
    Category getCategoryByCategoryId(String categoryId);
}
