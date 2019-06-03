package com.h2.fitness.h2fitness.AppMain.Model;


public class Users {

    public String user_email;
    public String user_image;
    public String status;
    public String thumb_image;
    public String user_id;

    public Users() {

    }

    public Users(String name, String user_image, String status, String thumb_image) {
        this.user_email = name;
        this.user_image = user_image;
        this.status = status;
        this.thumb_image = thumb_image;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return user_email;
    }

    public void setName(String name) {
        this.user_email = name;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

}
