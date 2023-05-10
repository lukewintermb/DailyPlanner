package com.example.dailyplanner2.RecyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailyplanner2.Category;
import com.example.dailyplanner2.DB.AppDataBase;
import com.example.dailyplanner2.DB.TodoDAO;
import com.example.dailyplanner2.R;
import com.example.dailyplanner2.Todo;

import java.util.List;

public class TodosRecycleViewAdapter extends RecyclerView.Adapter<TodosRecycleViewAdapter.MyViewHolder> {
    private final TodoRecyclerViewInterface mRecyclerViewInterface;
    Context mContext;
    List<Todo> mTodos;
    TodoDAO mTodoDAO;


    public TodosRecycleViewAdapter(Context context, List<Todo> todos, TodoRecyclerViewInterface recyclerViewInterface) {
        mContext = context;
        mTodos = todos;
        mRecyclerViewInterface = recyclerViewInterface;
        mTodoDAO = AppDataBase.getInstance(context).TodoDAO();
    }

    @NonNull
    @Override
    public TodosRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.todo_card, parent, false);
        return new TodosRecycleViewAdapter.MyViewHolder(view, mRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull TodosRecycleViewAdapter.MyViewHolder holder, int position) {
        holder.mName.setText(mTodos.get(position).getName());
        holder.mTodoId = mTodos.get(position).getTodoId();

        int categoryId = mTodos.get(position).getCategoryId();
        Category category = mTodoDAO.getCategoryByCategoryId(String.valueOf(categoryId));
        holder.mCategory.setText(category.getCategoryName());
        holder.mCategory.setTextColor(category.getCategoryColor());
    }

    @Override
    public int getItemCount() {
        return mTodos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox mName;
        TextView mCategory;
        int mTodoId;
        public MyViewHolder(@NonNull View itemView, TodoRecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            mName = itemView.findViewById(R.id.checkBoxTodos);
            mCategory = itemView.findViewById(R.id.textViewTodosCategoryName);
            mName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int pos = getAbsoluteAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos, String.valueOf(mTodoId));
                        }
                    }
                }
            });
        }
    }
}
