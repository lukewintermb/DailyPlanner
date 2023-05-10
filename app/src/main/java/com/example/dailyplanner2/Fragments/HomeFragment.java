package com.example.dailyplanner2.Fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dailyplanner2.Category;
import com.example.dailyplanner2.DB.AppDataBase;
import com.example.dailyplanner2.DB.TodoDAO;
import com.example.dailyplanner2.R;
import com.example.dailyplanner2.Todo;
import com.example.dailyplanner2.User;

import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_USER_ID = "user_id";

    // TODO: Rename and change types of parameters
    private int mUserId;
    private User mUser;
    private TodoDAO mTodoDAO;
    private TextView mWelcomeMessageTextView;
    private TextView mTodoName;
    private TextView mTodoMessage;
    private CardView mTodoCard;
    private TextView mCategoryName;
    private ImageButton mRefresh;
    private List<Todo> mTodos;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userId Parameter 1.
     * @return A new instance of fragment homeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(int userId) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mWelcomeMessageTextView = view.findViewById(R.id.textViewFragmentHomeWelcomeMessage);
        mTodoDAO = AppDataBase.getInstance(getActivity().getApplicationContext()).TodoDAO();
        mUser = mTodoDAO.getUserByUserId(String.valueOf(mUserId));
        mTodos = mTodoDAO.getLogByUserId(mUserId);
        String message;
        if(! (mUser == null)){
            message = "Hello " + mUser.getUserName();
        }else{
            message = "Hello";
        }

        mWelcomeMessageTextView.setText(message);

        //Set up the random todo screen
        mTodoName = view.findViewById(R.id.textViewHomeTodoName);
        mTodoCard = view.findViewById(R.id.cardView);
        mCategoryName = view.findViewById(R.id.textViewHomeTodoCategory);
        mTodoMessage = view.findViewById(R.id.textViewSomethingYouCouldDo);
        mRefresh = view.findViewById(R.id.imageButtonRefresh);
        String somethingTodoMessage = "You have nothing todo. Relax :)";
        if(mTodos.isEmpty()){
            mTodoMessage.setText(somethingTodoMessage);
            mRefresh.setVisibility(View.INVISIBLE);
            mTodoCard.setVisibility(View.INVISIBLE);
        }
        refreshTodo();
        buttonOnClick();
        return view;
    }

    private void buttonOnClick() {
        mRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshTodo();
            }
        });
    }


    private void refreshTodo() {
        if(mTodos.isEmpty()){
            return;
        }

        int size = mTodos.size();
        Random random = new Random();
        int randomNumber = random.nextInt(size);
        Todo todo = mTodos.get(randomNumber);
        mTodoName.setText(todo.getName());
        int categoryId = todo.getCategoryId();
        Category category = mTodoDAO.getCategoryByCategoryId(String.valueOf(categoryId));

        mCategoryName.setText(category.getCategoryName());
        mCategoryName.setTextColor(category.getCategoryColor());
    }
}