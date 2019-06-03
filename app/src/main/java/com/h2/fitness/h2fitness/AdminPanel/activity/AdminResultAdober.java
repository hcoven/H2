package com.h2.fitness.h2fitness.AdminPanel.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h2.fitness.h2fitness.R;

import java.util.List;

/**
 * Created by HP on 11/10/2017.
 */

public class AdminResultAdober extends RecyclerView.Adapter<AdminResultViewHolder> {


    public Context mContex;
    private List<VideoModel> mTasks;


    AdminResultAdober(List<VideoModel> tasks) {
        mTasks = tasks;


    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    @Override
    public void onBindViewHolder(@NonNull AdminResultViewHolder holder, int position) {
        VideoModel task = mTasks.get(position);

        holder.bindTask(task);


    }


    @NonNull
    @Override
    public AdminResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater;
        inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.resultclickview, parent, false);
        return new AdminResultViewHolder(view);

    }


}
