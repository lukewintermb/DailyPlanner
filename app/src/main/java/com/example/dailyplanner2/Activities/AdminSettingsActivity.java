package com.example.dailyplanner2.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.dailyplanner2.DB.AppDataBase;
import com.example.dailyplanner2.DB.TodoDAO;
import com.example.dailyplanner2.R;
import com.example.dailyplanner2.RecyclerViews.RecyclerViewInterface;
import com.example.dailyplanner2.User;
import com.example.dailyplanner2.RecyclerViews.UsersRecycleViewAdapter;

import java.util.List;
import java.util.Objects;

public class AdminSettingsActivity extends AppCompatActivity implements RecyclerViewInterface {
    private static final String USER_ID = "com.example.dailyplanner2.userId";
    TodoDAO mTodoDAO;
    List<User> mUsers;
    private int mUserId;
    RecyclerView mRecyclerView;
    UsersRecycleViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getDatabase();
        Intent intent = getIntent();
        mUserId = intent.getIntExtra(USER_ID, -1);
        setUpRecycler();
    }

    private void setUpRecycler(){
        mUsers = mTodoDAO.getAllUsers();
        mRecyclerView = findViewById(R.id.recycleViewAdminSettings);
        adapter = new UsersRecycleViewAdapter(this, mUsers, this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, AdminSettingsActivity.class);
        intent.putExtra(USER_ID, userId);
        return intent;
    }

    private void getDatabase(){
        mTodoDAO = AppDataBase.getInstance(getApplicationContext()).TodoDAO();;
    }

    @Override
    public void onItemClick(int position, String userId, boolean clickedDelete) {
        if(!clickedDelete){
            if(userId.equals(String.valueOf(mUserId))){
                Toast.makeText(this, "Cannot change your own admin Status", Toast.LENGTH_LONG).show();
                return;
            }
            changeAdminStatus(userId);
            return;
        }
        User user = mTodoDAO.getUserByUserId(String.valueOf(userId));
        if(!userId.equals(String.valueOf(mUserId))){
            deleteUser(user);
        }else{
            Toast.makeText(this, "Cannot delete yourself...", Toast.LENGTH_LONG).show();
        }
    }

    private void changeAdminStatus(String userId) {
        String message;
        User user = mTodoDAO.getUserByUserId(userId);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
//        final AlertDialog alertDialog = alertBuilder.create();

        if(user.isAdmin()) {
            message = "Take away admin rights of " + user.getUserName();
        }else {
            message = "Make " + user.getUserName() + " an admin";
        }

        alertBuilder.setMessage(message);

        alertBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        user.setAdmin(!user.isAdmin());
                        mTodoDAO.update(user);
                        setUpRecycler();
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


    private void deleteUser(User user){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
//        final AlertDialog alertDialog = alertBuilder.create();

        String message = "Delete " + user.getUserName();
        alertBuilder.setMessage(message);

        alertBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        mTodoDAO.delete(user);
                        setUpRecycler();
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
}