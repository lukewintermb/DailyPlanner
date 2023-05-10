package com.example.dailyplanner2.Activities;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dailyplanner2.Category;
import com.example.dailyplanner2.DB.AppDataBase;
import com.example.dailyplanner2.DB.TodoDAO;
import com.example.dailyplanner2.R;

import yuku.ambilwarna.AmbilWarnaDialog;

public class AddCategoryActivity extends AppCompatActivity {

    private static final String USER_ID_EXTRA_KEY = "com.example.dailyplanner2.Activities.USER_ID_EXTRA_KEY";
    private static final String DAY_EXTRA_KEY = "com.example.dailyplanner2.Activities.DAY_EXTRA_KEY";
    TodoDAO mTodoDao;
    EditText mEditText;
    Button mChooseColorButton;
    Button mAddCategory;
    int mDefaultColor;
    ConstraintLayout mLayout;
    String mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        getDatabase();
        wireUpScreen();
    }

    private void wireUpScreen() {
        mLayout = (ConstraintLayout) findViewById(R.id.layout);
        mDefaultColor = ContextCompat.getColor(AddCategoryActivity.this, R.color.black);
        mEditText = findViewById(R.id.editTextTextPersonName);
        mChooseColorButton = findViewById(R.id.buttonChooseColor);
        mAddCategory = findViewById(R.id.buttonAddCategory);

        mChooseColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker();
            }
        });

        mAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategory();
            }
        });
    }

    private void addCategory() {
        String categoryName = mEditText.getText().toString();
        Intent intent = getIntent();
        int userId = intent.getIntExtra(USER_ID_EXTRA_KEY, -1);
        mDay = intent.getStringExtra(DAY_EXTRA_KEY);
        Category category = new Category(categoryName, mDefaultColor, userId);
        mTodoDao.insert(category);
        intent = AddTodoActivity.intentFactory(getApplicationContext(), mDay, userId);
        startActivity(intent);
    }

    public void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor = color;
                mChooseColorButton.setBackgroundColor(mDefaultColor);
            }
        });
        colorPicker.show();
    }

    private void getDatabase() {
        mTodoDao = AppDataBase.getInstance(getApplicationContext()).TodoDAO();
    }

    public static Intent intentFactory(Context context, int userId, String day){
        Intent intent = new Intent(context, AddCategoryActivity.class);
        intent.putExtra(USER_ID_EXTRA_KEY, userId);
        intent.putExtra(DAY_EXTRA_KEY, day);
        return intent;
    }
}