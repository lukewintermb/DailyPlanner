package com.example.dailyplanner2.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dailyplanner2.Activities.AddTodoActivity;
import com.example.dailyplanner2.DB.AppDataBase;
import com.example.dailyplanner2.DB.TodoDAO;
import com.example.dailyplanner2.R;
import com.example.dailyplanner2.Todo;
import com.example.dailyplanner2.RecyclerViews.TodoRecyclerViewInterface;
import com.example.dailyplanner2.RecyclerViews.TodosRecycleViewAdapter;
import com.example.dailyplanner2.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodosFragment extends Fragment  implements TodoRecyclerViewInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_USER_ID = "user_id";

    // TODO: Rename and change types of parameters
    private int mUserId;
    private TodoDAO mTodoDAO;
    private BottomNavigationView mNavigationView;
    private TextView mDayText;
    private Button mAddTodoButton;
    private Button mUpdateDayButton;
    private String mDay;
    RecyclerView mRecyclerView;
    TodosRecycleViewAdapter adapter;
    List<Todo> mTodos;
    public TodosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userId Parameter 1.
     * @return A new instance of fragment TodosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodosFragment newInstance(int userId) {
        TodosFragment fragment = new TodosFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getInt(ARG_USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todos, container, false);

        //Setting mDay to a default "Today" because it will start on the "Today" navigation
        mDay = "Today";
        mTodoDAO = AppDataBase.getInstance(getActivity().getApplicationContext()).TodoDAO();
        mNavigationView = view.findViewById(R.id.bottomNavigationTodos);
        mAddTodoButton = view.findViewById(R.id.buttonAddTodo);
        mUpdateDayButton = view.findViewById(R.id.buttonUpdateDay);
        mDayText = view.findViewById(R.id.textViewTodosDay);
        mDayText.setText(mDay);
        addTodoOnClick();
        navigationOnClick();
        mRecyclerView = view.findViewById(R.id.recyclerViewTodosFragment);

        setUpRecycler();
        return view;
    }

    private void setUpRecycler(){
        mTodos = mTodoDAO.getLogByDayAndUserId(mUserId, mDay);

        adapter = new TodosRecycleViewAdapter(getActivity().getApplicationContext(), mTodos, this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
    }

    private void addTodoOnClick() {
        mAddTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddTodoActivity.intentFactory(getActivity().getApplicationContext(), mDay, mUserId);
                startActivity(intent);
            }
        });

        mUpdateDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDays();
            }
        });
    }

    private void updateDays() {
        String message = "Are you sure you want to update the days?";
        List<Todo> todays = mTodoDAO.getLogByDayAndUserId(mUserId, "Today");
        List<Todo> tomorrows = mTodoDAO.getLogByDayAndUserId(mUserId, "Tomorrow");
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());

        alertBuilder.setMessage(message);

        alertBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        //Delete all todos for today
                        for(Todo todo: todays){
                            mTodoDAO.delete(todo);
                        }

                        for(Todo todo: tomorrows){
                            mTodoDAO.delete(todo);
                            todo.setDay("Today");
                            mTodoDAO.insert(todo);
                        }
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


    private void navigationOnClick() {
        mNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.navToday:
                        mDay = "Today";
                        mDayText.setText(mDay);
                        //Update RecyclerView
                        setUpRecycler();
                        return true;
                    case R.id.navTomorrow:
                        mDay = "Tomorrow";
                        mDayText.setText(mDay);
                        //Update RecyclerView
                        setUpRecycler();
                        return true;
                    case R.id.navGeneral:
                        mDay = "General";
                        mDayText.setText(mDay);
                        //Update RecyclerView
                        setUpRecycler();
                        return true;
                }

                return false;
            }
        });
    }

    @Override
    public void onItemClick(int position, String todoId) {
        try {
            // Wait for 1 second (1000 milliseconds)
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // This block is executed when the sleep is interrupted
            e.printStackTrace();
        }

        List<Todo> todos = mTodoDAO.getLogById(Integer.parseInt(todoId));
        if(todos.size() == 1){
            mTodoDAO.delete(todos.get(0));
        }else{
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        setUpRecycler();
    }
}

