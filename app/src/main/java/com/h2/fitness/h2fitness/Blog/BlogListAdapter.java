package com.h2.fitness.h2fitness.Blog;

/*
  Created by ravi on 18/01/18.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h2.fitness.h2fitness.R;

import java.util.List;

public class BlogListAdapter extends RecyclerView.Adapter<BlogViewHolder> {

    public Context mContex;
    private List<Blog> mTasks;


    public BlogListAdapter(List<Blog> tasks) {
        mTasks = tasks;


    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
        Blog task = mTasks.get(position);

        holder.bindTask(task);


    }


    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

           //ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(mContex), R.layout.recipe_list_item, parent, false);//was commented
        // return new BlogViewHolder(viewDataBinding);
        LayoutInflater inflater;
        inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_list_item, parent, false);
        return new BlogViewHolder(view);
    }


}
