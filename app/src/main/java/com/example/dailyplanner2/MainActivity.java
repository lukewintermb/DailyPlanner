package com.example.dailyplanner2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dailyplanner2.DB.AppDataBase;
import com.example.dailyplanner2.DB.TodoDAO;
import com.example.dailyplanner2.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String PREFERENCES_KEY = "com.example.dailyplanner2.preferencesKey";
    public static final String USER_ID_KEY = "com.example.dailyplanner2.user_key2";
    ActivityMainBinding binding;

    Button mLogin;

    Button mCreateAccount;

    TodoDAO mTodoDAO;

    int mUserId = -1;

    private SharedPreferences mPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDatabase();

        checkForUser();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mLogin = binding.loginButton;
        mCreateAccount = binding.createAccountButton;

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivity(LoginActivity.class);
            }
        });
    }

    private void getDatabase() {
        mTodoDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .TodoDAO();
    }

    private void checkForUser() {
        //do we have a user in the intent?
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        if(mUserId != -1){
            Intent intent = LandingPage.intentFactory(getApplicationContext(), mUserId);
            startActivity(intent);
        }

        //SharedPreferences preferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);

        if(mPreferences == null){
            getPrefs();
        }

        redirectToLandingPageIfValidUser();

        mUserId = mPreferences.getInt(USER_ID_KEY, -1);

        if(mUserId != -1){
            return;
        }

        List<User> users = mTodoDAO.getAllUsers();
        if(users.size() <= 0){
            User defaultUser = new User("testuser1", "testuser1", false);
            User defaultUser2 = new User("admin2", "admin2", true);
            mTodoDAO.insert(defaultUser);
            mTodoDAO.insert(defaultUser2);
        }
        //do we have a user in the preferences?
        //do we have any users at all?

        //TODO: I don't think I want this here
//        Intent intent = LoginActivity.intentFactory(this);
//        startActivity(intent);
    }

    private void redirectToLandingPageIfValidUser() {
        mUserId = mPreferences.getInt(USER_ID_KEY, -1);

        if (mUserId != -1) {
            Intent intent = LandingPage.intentFactory(getApplicationContext(), mUserId);
            startActivity(intent);
        }
    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }


    private void goToActivity(Class<?> targetActivity){
        Intent intent = new Intent(MainActivity.this, targetActivity);
        startActivity(intent);
    }

    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, MainActivity.class);

        return intent;
    }
}