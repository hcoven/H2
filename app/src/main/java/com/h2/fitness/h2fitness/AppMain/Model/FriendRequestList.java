package com.h2.fitness.h2fitness.AppMain.Model;

public class FriendRequestList {

    String user_email, request_type;

    public FriendRequestList() {
    }

    public FriendRequestList(String user_email, String request_type) {
        this.user_email = user_email;
        this.request_type = request_type;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_id) {
        this.user_email = user_id;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }
}