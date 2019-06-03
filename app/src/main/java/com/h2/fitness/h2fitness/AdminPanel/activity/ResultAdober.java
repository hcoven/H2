package com.h2.fitness.h2fitness.AdminPanel.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h2.fitness.h2fitness.R;

import java.util.List;

/**
 * Created by HP on 10/2/2017.
 */

public class ResultAdober extends RecyclerView.Adapter<VideoViewHolder> {


    public Context mContex;
    private List<VideoModel> mTasks;


    public ResultAdober(List<VideoModel> tasks) {
        mTasks = tasks;


    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        VideoModel task = mTasks.get(position);


        holder.bindTask(task);


    }


    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater;
        inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.resultclickview, parent, false);
        return new VideoViewHolder(view);
    }


}
