package com.h2.fitness.h2fitness.AppMain.Model;

import java.util.ArrayList;
import java.util.List;

public class UserList {

    private static UserList sTaskList;
    private ArrayList<Users> mTasks;
    private Users users;

    private UserList() {
        mTasks = new ArrayList<Users>();

    }

    public static UserList getInstance() {
        if (sTaskList == null)
            sTaskList = new UserList();
        return sTaskList;
    }

    public List<Users> getTasks() {

        return mTasks;
    }


    public void addResult(Users usersList) {
        mTasks.add(usersList);

    }


    public boolean response(Users result) {

        if (mTasks.equals(result)) {
            return true;
        }
        return false;
    }


    public void removeResultList() {
        mTasks.clear();

    }
}