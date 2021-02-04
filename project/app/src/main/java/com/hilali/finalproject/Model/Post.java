package com.hilali.finalproject.Model;


import java.time.LocalDate;

public class Post {
    private String yoo;
    private String pid;
    private String userID;
    private String title;
    private String description;
    private PostCategory category;
    private LocalDate postDate;

    public Post() { }

    public Post(String userID, String title, String description, PostCategory category) {
        //this.pid = pid;
        this.userID = userID;
        this.title = title;
        this.description = description;
        this.category = category;
        //this.postDate=LocalDate.now();
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PostCategory getCategory() {
        return category;
    }

    public void setCategory(PostCategory category) {
        this.category = category;
    }
}
