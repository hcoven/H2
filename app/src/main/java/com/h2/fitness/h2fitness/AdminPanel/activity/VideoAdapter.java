package com.h2.fitness.h2fitness.AdminPanel.activity;

import java.util.ArrayList;

/**
 * Created by HP on 10/1/2017.
 */

public class VideoAdapter {

    private static VideoAdapter sTaskList;
    private ArrayList<VideoModel> mTasks;
    private VideoModel videoModel;

    private VideoAdapter() {
        mTasks = new ArrayList<VideoModel>();

    }

    public static VideoAdapter getInstance() {
        if (sTaskList == null)
            sTaskList = new VideoAdapter();
        return sTaskList;
    }

    public void addResult(VideoModel videoModelList) {
        mTasks.add(videoModelList);

    }

    public ArrayList<VideoModel> getTasks() {

        return mTasks;
    }


    public boolean response(VideoModel videoModel) {

        if (mTasks.equals(videoModel)) {
            return true;
        }
        return false;
    }


    public void removeResultList() {
        mTasks.clear();

    }


}
