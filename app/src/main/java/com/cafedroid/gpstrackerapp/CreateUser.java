package com.cafedroid.gpstrackerapp;

public class CreateUser {


    public CreateUser()
    {}
    public String name;

    public CreateUser(String name, String email, String password, String code, String isSharing, String lat, String lng, String imageUrl,String userId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.code = code;
        this.isSharing = isSharing;
        this.lat = lat;
        this.lng = lng;
        this.imageUrl = imageUrl;
        this.userId = userId;
    }

    public String email;
    public String password;
    public String code;
    public String isSharing;
    public String lat;
    public String lng;
    public String imageUrl;
    public String userId;

}

