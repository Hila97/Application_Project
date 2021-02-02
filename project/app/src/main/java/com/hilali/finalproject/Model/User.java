package com.hilali.finalproject.Model;


public class User {

    //private String id;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String imageUrl;


    public User( String email, String password, String name, String phone) {
        //this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        //this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    /*
    public String getId() {
        return id;
    }
     */

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    /*
    public void setId(String id) {
        this.id = id;
    }

     */

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

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
