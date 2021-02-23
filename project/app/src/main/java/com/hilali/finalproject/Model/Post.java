package com.hilali.finalproject.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;
@Entity
public class Post {
    @PrimaryKey
    @NonNull
    private String pid;
    private String uid;
    private String title;
    private String description;
    private String category;
    private String imageUrl;
    private Long lastUpdated;
    private String isDeleted;

    public Post() { }

    public Post(String uid, String title, String description, String category) {
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.category = category;
        this.imageUrl = null;
        isDeleted="false";
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

    public Map<String, Object> toMap() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("pid",this.getPid());
        data.put("uid",this.getUid());
        data.put("title",this.getTitle());
        data.put("description",this.getDescription());
        data.put("category",this.getCategory());
        data.put("imageUrl",this.getImageUrl());
        data.put("lastUpdated", FieldValue.serverTimestamp());
        data.put("isDeleted",this.getIsDeleted());
        return data;
    }
    public void fromMap(Map<String, Object> map){
        pid = (String)map.get("pid");
        uid = (String)map.get("uid");
        title = (String)map.get("title");
        description = (String)map.get("description");
        category = (String)map.get("category");
        imageUrl = (String)map.get("imageUrl");
        Timestamp ts = (Timestamp)map.get("lastUpdated");
        lastUpdated = ts.getSeconds();
        isDeleted=(String)map.get("isDeleted");
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }
}
