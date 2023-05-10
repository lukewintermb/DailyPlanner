package com.example.dailyplanner2.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.dailyplanner2.Category;
import com.example.dailyplanner2.DB.AppDataBase;
import com.example.dailyplanner2.DB.TodoDAO;
import com.example.dailyplanner2.R;
import com.example.dailyplanner2.RecyclerViews.CategoryRecyclerViewAdapter;
import com.example.dailyplanner2.RecyclerViews.CategoryRecyclerViewInterface;
import com.example.dailyplanner2.RecyclerViews.UsersRecycleViewAdapter;
import com.example.dailyplanner2.Todo;

import java.util.ArrayList;
import java.util.List;

public class AddTodoActivity extends AppCompatActivity implements CategoryRecyclerViewInterface {
    private static final String DAY_KEY = "androidx.appcompat.app.AppCompatActivity.DAY_KEY";
    private static final String USERID_KEY = "androidx.appcompat.app.AppCompatActivity.USERID_KEY";

    private EditText mTodoName;
    private Button mAddTodo;
    private ImageButton mAddCategory;
    private TodoDAO mTodoDAO;
    private static int mUserId;
    private static String mDay;
    private int mCategoryId;
    private RecyclerView mRecyclerView;
    private CategoryRecyclerViewAdapter mAdapter;
    private List<Category> mCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        setUpDataBase();
        getUserInfo();
        wireUpScreen();
        setUpRecycler(mCategoryId);
    }

    private void getUserInfo() {
        Intent intent = getIntent();
        mUserId = intent.getIntExtra(USERID_KEY, -1);
    }
    private void setUpRecycler(int selectedCategoryId){
        mCategories = mTodoDAO.getCategoriesByUserId(mUserId);
//        if(mCategories.isEmpty()) {
//            mTodoDAO.insert(new Category("Something", "black", mUserId));
//            mTodoDAO.insert(new Category("Something Else", "black", mUserId));
//            setUpRecycler(mCategoryId);
//        }
        mRecyclerView = findViewById(R.id.recyclerViewCategories);
        mAdapter = new CategoryRecyclerViewAdapter(this, mCategories, selectedCategoryId,  this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }



    private void setUpDataBase() {
        mTodoDAO = AppDataBase.getInstance(getApplicationContext()).TodoDAO();
    }

    private void wireUpScreen() {
        mTodoName = findViewById(R.id.editTextTodoName);
        mAddTodo = findViewById(R.id.buttonAddTodoAddTodo);
        mAddCategory = findViewById(R.id.imageButtonAddCategory);

        mAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTodo();
            }
        });

        mAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddCategoryActivity.intentFactory(getApplicationContext(), mUserId, mDay);
                startActivity(intent);
            }
        });
    }

    private void createTodo() {
        String todoName = mTodoName.getText().toString();
        int userId = getIntent().getIntExtra(USERID_KEY, -1);
        String day = getIntent().getStringExtra(DAY_KEY);

        //TODO get the Category the user selected
        if(mCategoryId < 1){
            return;
        }
        Todo todo = new Todo(todoName, userId, day, mCategoryId);
        mTodoDAO.insert(todo);
        int targetFragment;

        //TODO: figure out the correct value so that it navigates back to Todos Fragment
        Intent intent = LandingPage.intentFactory(getApplicationContext(), mUserId, R.id.navCalender);
        startActivity(intent);
    }

    public static Intent intentFactory(Context context, String day, int userId){
        mUserId = userId;
        mDay = day;
        Intent intent = new Intent(context, AddTodoActivity.class);
        intent.putExtra(DAY_KEY, day);
        intent.putExtra(USERID_KEY, userId);

        return intent;
    }

    @Override
    public void onItemClick(int position, String categoryId) {
        mCategoryId = Integer.parseInt(categoryId);
        setUpRecycler(mCategoryId);
    }
}