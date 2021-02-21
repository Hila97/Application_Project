package com.hilali.finalproject.Model;



public class Post {
    private String pid;
    private String uid;
    private String title;
    private String description;
    private PostCategory category;
    private String imageUrl;

    public Post() { }

    public Post(String uid, String title, String description, PostCategory category) {
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.category = category;
        this.imageUrl = null;
    }
    public Post(Post post)
    {
        this.pid=post.getPid();
        this.uid=post.getUid();
        this.title=post.getTitle();
        this.description=post.getDescription();
        this.category=post.getCategory();
        this.imageUrl=post.getImageUrl();
    }


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
