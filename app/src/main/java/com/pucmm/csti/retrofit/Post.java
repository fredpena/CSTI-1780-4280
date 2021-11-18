package com.pucmm.csti.retrofit;

import com.google.gson.annotations.SerializedName;

public class Post {

    private int userId;
    private int id;
    private String title;
    @SerializedName("body")
    private String text;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
