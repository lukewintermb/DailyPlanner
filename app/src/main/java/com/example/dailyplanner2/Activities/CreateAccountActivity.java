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

import java.util.List;
import java.util.Objects;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText mUsername;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private Button mCreateAccount;
    private TextView mLogIn;
    private TodoDAO mTodoDAO;
    private List<User> mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        
        wireUpDisplay();
    }

    private void createAccount(){
        // Username already exist
        String userName = mUsername.getText().toString();
        String password = mPassword.getText().toString();
        String confirmPassword = mConfirmPassword.getText().toString();
        for(User u: mUsers){
            if(userName.equals(u.getUserName())){
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //Passwords not the same
        if(!password.equals(confirmPassword)){
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            return;
        }

        //add User to database
        User user = new User(userName, password, false);

        //Maybe have to insert a todo object instead of user object
        mTodoDAO.insert(user);

        //Update the user to get the userId
        user = mTodoDAO.getUserByUsername(user.getUserName());

        //Start login activity
        Intent intent2 = LandingPage.intentFactory(getApplicationContext(), user.getUserId(), R.id.navHome);
        startActivity(intent2);
    }
    
    private void wireUpDisplay() {
        getDatabase();
        mUsername = findViewById(R.id.editTextCreateAccountUsername);
        mPassword = findViewById(R.id.editTextCreateAccountPassword);
        mConfirmPassword = findViewById(R.id.editTextCreateAccountConfirmPassword);
        mCreateAccount = findViewById(R.id.buttonCreateAccount);
        mLogIn = findViewById(R.id.textViewCreateAccountLogin);
        mUsers = mTodoDAO.getAllUsers();
        mCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
        
        mLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(LoginActivity.intentFactory(getApplicationContext()));
            }
        });
    }

    private void getDatabase(){
        mTodoDAO = AppDataBase.getInstance(getApplicationContext()).TodoDAO();;
    }

    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, CreateAccountActivity.class);
        return intent;
    }
}