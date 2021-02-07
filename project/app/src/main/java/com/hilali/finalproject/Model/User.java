package com.hilali.finalproject.Model;


import org.jetbrains.annotations.NotNull;

public class User {

    private String uid;
    private String email;
    private String password;
    private String name;
    private String phone;
    //private String imageUrl;



    public User() { }

    public User(String id, String email, String password, String name, String phone) {
        this.uid = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        //this.imageUrl = imageUrl;
    }
    public User(@NotNull User user)
    {
        this.uid = user.getUid();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.name = user.getName();
        this.phone = user.getPhone();
    }



    public String getEmail() {
        return email;
    }


    public String getUid() {
        return uid;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

  /*  public String getImageUrl() {
        return imageUrl;
    }


   */

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
/*
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

 */

}
