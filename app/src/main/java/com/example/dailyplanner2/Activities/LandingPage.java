package com.example.dailyplanner2.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.dailyplanner2.DB.AppDataBase;
import com.example.dailyplanner2.DB.TodoDAO;
import com.example.dailyplanner2.Fragments.HomeFragment;
import com.example.dailyplanner2.Fragments.TodosFragment;
import com.example.dailyplanner2.R;
import com.example.dailyplanner2.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class LandingPage extends AppCompatActivity {
    private static final String USER_ID_EXTRA_KEY = "com.example.dailyplanner2.user_id";
    private static final String DEFAULT_FRAGMENT_EXTRA_KEY = "com.example.dailyplanner2.default_fragment";


    //TODO: Public for now, so that i can access in the HomeFragment

    public TodoDAO mTodoDAO;
    private int mUserId;
    private User mUser;
    private BottomNavigationView mNavigationView;
    private Intent mIntent;
    private FragmentManager fragmentManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
//        mTodoDAO = AppDataBase.getInstance(getApplicationContext()).TodoDAO();
        getDatabase();
        getUserInfo();
        fragmentManager = getSupportFragmentManager();
        setUpFirstFragment(savedInstanceState);
        wireUpScreen();
        addUserToPreference();
    }

    private void setUpFirstFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            int defaultFragment = getIntent().getIntExtra(DEFAULT_FRAGMENT_EXTRA_KEY, R.id.navHome);
            Bundle bundle = new Bundle();
            bundle.putInt(HomeFragment.ARG_USER_ID, mUserId);

            if (defaultFragment == R.id.navHome) {
                HomeFragment homeFragment = HomeFragment.newInstance(mUserId);
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainerView, homeFragment)
                        .commit();
            } else if (defaultFragment == R.id.navCalender) {
                TodosFragment todosFragment = TodosFragment.newInstance(mUserId);
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainerView, todosFragment)
                        .commit();
            }
        }
    }


    private void getUserInfo() {
        mIntent = getIntent();
        mUserId = mIntent.getIntExtra(USER_ID_EXTRA_KEY, -1);
        mUser = mTodoDAO.getUserByUserId(String.valueOf(mUserId));
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem adminSettings = menu.findItem(R.id.adminSettings);
        adminSettings.setVisible(mUser.isAdmin());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout:
                logoutUser();
                return true;
            case R.id.adminSettings:
                startActivity(AdminSettingsActivity.intentFactory(getApplicationContext(), mUserId));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void wireUpScreen(){
        mNavigationView = findViewById(R.id.navigationViewLanding);
        mNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.navHome:
                        HomeFragment homeFragment = HomeFragment.newInstance(mUserId);
                        getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.fragmentContainerView, homeFragment)
                                .commit();
                        return true;
                    case R.id.navCalender:
                        Bundle bundle = new Bundle();
                        bundle.putInt(HomeFragment.ARG_USER_ID, mUserId);
                        TodosFragment todosFragment = TodosFragment.newInstance(mUserId);
                        getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.fragmentContainerView, todosFragment)
                                .commit();
                        return true;
                }
                return false;
            }
        });
    }

    private void addUserToPreference() {
        if (mUserId == -1) {
            return;
        }

        SharedPreferences preferences = getSharedPreferences(MainActivity.PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(MainActivity.USER_ID_KEY, mUserId);
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
        getIntent().putExtra(USER_ID_EXTRA_KEY, -1);
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

    public static Intent intentFactory(Context context, int userId, int defaultFragment){
        Intent intent = new Intent(context, LandingPage.class);
        intent.putExtra(USER_ID_EXTRA_KEY, userId);
        intent.putExtra(DEFAULT_FRAGMENT_EXTRA_KEY, defaultFragment);
        return intent;
    }


    private void getDatabase(){
        mTodoDAO = AppDataBase.getInstance(getApplicationContext()).TodoDAO();;
    }
}