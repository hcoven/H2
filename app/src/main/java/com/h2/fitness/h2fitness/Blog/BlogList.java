package com.h2.fitness.h2fitness.Blog;

import java.util.ArrayList;

/**
 * Created by HP on 10/1/2017.
 */

public class BlogList {

    private static BlogList sTaskList;
    private ArrayList<Blog> mTasks;
    private Blog result;

    private BlogList() {
        mTasks = new ArrayList<Blog>();

    }

    public static BlogList getInstance() {
        if (sTaskList == null)
            sTaskList = new BlogList();
        return sTaskList;
    }

    public void addResult(Blog resultList) {
        mTasks.add(resultList);

    }

    public ArrayList<Blog> getTasks() {

        return mTasks;
    }


    public boolean response(Blog result) {

        if (mTasks.equals(result)) {
            return true;
        }
        return false;
    }


    public void removeResultList() {
        mTasks.clear();

    }


}
