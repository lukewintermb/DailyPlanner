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
import android.widget.TextView;
import android.widget.Toast;

import com.example.dailyplanner2.DB.AppDataBase;
import com.example.dailyplanner2.DB.TodoDAO;

public class LandingPage extends AppCompatActivity {


    private static final String USER_ID_KEY = "com.example.dailyplanner2.user_id";

    private TodoDAO mTodoDAO;

    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        getDatabase();

        TextView welcomeMessage = findViewById(R.id.welcomeMessage);
        Button logOutButton = findViewById(R.id.button_logout2);
        Button adminButton = findViewById(R.id.button_admin);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });

        Intent intent = getIntent();
        int userId = intent.getIntExtra(USER_ID_KEY, 0);
        mUser = mTodoDAO.getUserByUserId(String.valueOf(userId));
        String welcomeMessageString = "Hello, " + mUser.getUserName();
        if(mUser.isAdmin()){
            adminButton.setVisibility(View.VISIBLE);
        }else{
            adminButton.setVisibility(View.INVISIBLE);
        }

        welcomeMessage.setText(welcomeMessageString);

        addUserToPreference();
    }

    private void addUserToPreference() {
        int userId = getIntent().getIntExtra(USER_ID_KEY, -1);

        if (userId == -1) {
            // If there's no valid user ID, don't proceed with storing it
            return;
        }

        SharedPreferences preferences = getSharedPreferences(MainActivity.PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(MainActivity.USER_ID_KEY, userId);
        editor.apply();
    }


    private void logoutUser(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage(R.string.logout);

        alertBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        clearUserFromIntent();
                        clearUserFromPref();
                    }
                });
        alertBuilder.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                    }
                });

        alertBuilder.create().show();
    }

    private void clearUserFromIntent(){
        getIntent().putExtra(USER_ID_KEY, -1);
    }

    private void clearUserFromPref() {
        SharedPreferences preferences = getSharedPreferences(MainActivity.PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(MainActivity.USER_ID_KEY);
        editor.apply();

        // Redirect user to the MainActivity (login page)
        Intent intent = MainActivity.intentFactory(this);
        startActivity(intent);
        finish();
    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, LandingPage.class);
        intent.putExtra(USER_ID_KEY, userId);

        return intent;
    }

    private void getDatabase(){
        mTodoDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .TodoDAO();
    }
}