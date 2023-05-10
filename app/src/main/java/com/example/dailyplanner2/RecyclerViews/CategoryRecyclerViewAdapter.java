package com.example.dailyplanner2.RecyclerViews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailyplanner2.Category;
import com.example.dailyplanner2.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.MyViewHolder>{

    private final CategoryRecyclerViewInterface mRecyclerViewInterface;
    Context mContext;
    List<Category> mCategories;
    int mSelectedCategoryId;
    public CategoryRecyclerViewAdapter(Context context, List<Category> categories, int selectedCategoryId, CategoryRecyclerViewInterface recyclerViewInterface){
        this.mContext = context;
        this.mCategories = categories;
        this.mRecyclerViewInterface = recyclerViewInterface;
        this.mSelectedCategoryId = selectedCategoryId;
    }

    @NonNull
    @Override
    public CategoryRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.categories_card, parent, false);
        return new CategoryRecyclerViewAdapter.MyViewHolder(view, mRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.mCategoryName.setText(mCategories.get(position).getCategoryName());
        GradientDrawable circleDrawable = (GradientDrawable) holder.mCircle.getBackground();
        int newColor = mCategories.get(position).getCategoryColor();
        circleDrawable.setColor(newColor);
        if(mCategories.get(position).getCategoryId() == mSelectedCategoryId){
            holder.mCircleOutline.setVisibility(View.VISIBLE);
        }else{
            holder.mCircleOutline.setVisibility(View.INVISIBLE);
        }
        holder.mCategoryId = mCategories.get(position).getCategoryId();
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mCategoryName;
        View mCircle;
        View mCircleOutline;
        int mCategoryId;
        public MyViewHolder(@NonNull View itemView, CategoryRecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            mCategoryName = itemView.findViewById(R.id.textViewCategoryName);
            mCircle = itemView.findViewById(R.id.circleView);
            mCircleOutline = itemView.findViewById(R.id.circleOutline);
            mCategoryName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int pos = getAbsoluteAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos, String.valueOf(mCategoryId));
                        }
                    }
                }
            });
        }
    }
}
