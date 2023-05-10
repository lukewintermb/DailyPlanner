package com.example.dailyplanner2.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dailyplanner2.DB.AppDataBase;
import com.example.dailyplanner2.DB.TodoDAO;
import com.example.dailyplanner2.R;
import com.example.dailyplanner2.User;

public class LoginActivity extends AppCompatActivity {
    private EditText mUsernameField;
    private EditText mPasswordField;
    private Button mButton;
    private TodoDAO mTodoDAO;
    private String mUsername;
    private String mPassword;
    private User mUser;
    private TextView mCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        wireupDisplay();

        getDatabase();
    }

    private void wireupDisplay(){
        mUsernameField = findViewById(R.id.editTextCreateAccountUsername);
        mPasswordField = findViewById(R.id.editTextCreateAccountPassword);
        mButton = findViewById(R.id.buttonLoginLogin);
        mCreateAccount = findViewById(R.id.textViewLoginCreateAccount);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValuesFromDisplay();
                if(checkForUserInDatabase()){
                    if(!validatePassword()){
                        Toast.makeText(LoginActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = LandingPage.intentFactory(getApplicationContext(), mUser.getUserId(), R.id.navHome);
                        startActivity(intent);
                    }
                }
            }
        });

        mCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CreateAccountActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
    }

    private boolean validatePassword(){
        return mUser.getPassword().equals(mPassword);
    }
    private void getValuesFromDisplay(){
        mUsername = mUsernameField.getText().toString();
        mPassword = mPasswordField.getText().toString();
    }

    private boolean checkForUserInDatabase(){
        mUser = mTodoDAO.getUserByUsername(mUsername);
        if(mUser == null){
            Toast.makeText(this, "no user " + mUsername + " found", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void getDatabase(){
        mTodoDAO = AppDataBase.getInstance(getApplicationContext()).TodoDAO();;
    }

    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, LoginActivity.class);

        return intent;
    }

}