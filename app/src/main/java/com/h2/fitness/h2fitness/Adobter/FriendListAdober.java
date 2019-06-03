package com.h2.fitness.h2fitness.Adobter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h2.fitness.h2fitness.AppMain.Model.Users;
import com.h2.fitness.h2fitness.R;

import java.util.List;

public class FriendListAdober extends RecyclerView.Adapter<FriendListViewHolder> {


    public Context mContex;
    private List<Users> mTasks;


    public FriendListAdober(List<Users> tasks) {
        mTasks = tasks;


    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    @Override
    public void onBindViewHolder(FriendListViewHolder holder, int position) {
        Users task = mTasks.get(position);

        holder.bindTask(task);


    }


    @Override
    public FriendListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater;
        inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.users_single_layout, parent, false);
        return new FriendListViewHolder(view);
    }
}

