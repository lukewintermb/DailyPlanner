package com.example.dailyplanner2.RecyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailyplanner2.R;
import com.example.dailyplanner2.User;

import java.util.List;

public class UsersRecycleViewAdapter extends RecyclerView.Adapter<UsersRecycleViewAdapter.MyViewHolder> {
    private final RecyclerViewInterface mRecyclerViewInterface;
    Context mContext;
    List<User> mUsers;
    public UsersRecycleViewAdapter(Context context, List<User> users, RecyclerViewInterface recyclerViewInterface){
        this.mContext = context;
        this.mUsers = users;
        this.mRecyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public UsersRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.user_card, parent, false);
        return new UsersRecycleViewAdapter.MyViewHolder(view, mRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersRecycleViewAdapter.MyViewHolder holder, int position) {
        holder.mUsername.setText(mUsers.get(position).getUserName());
        holder.mUserId.setText(String.valueOf(mUsers.get(position).getUserId()));
        if(mUsers.get(position).isAdmin()){
            holder.mIsAdmin.setImageResource(R.drawable.baseline_admin_panel_settings_24);
        }else{
            holder.mIsAdmin.setImageResource(R.drawable.outline_admin_panel_settings_24);
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView mUsername, mUserId;
        ImageView mDeleteUser, mIsAdmin;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            mUsername = itemView.findViewById(R.id.textViewUserCardName);
            mUserId = itemView.findViewById(R.id.textViewUserCardiD);
            mDeleteUser = itemView.findViewById(R.id.imageViewUserCard);
            mIsAdmin = itemView.findViewById(R.id.imageViewIsAdmin);
            mDeleteUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int pos = getAbsoluteAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos, mUserId.getText().toString(), true);
                        }
                    }
                }
            });
            mIsAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int pos = getAbsoluteAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos, mUserId.getText().toString(), false);
                        }
                    }
                }
            });
        }
    }
}
