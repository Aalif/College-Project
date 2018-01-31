package com.example.ak.savefood;

/**
 * Created by AK on 1/30/2018.
 */

public class blog {
    private String Title;
    private String Discription;
    private String Image;
    private String User_name;

    public blog(){

    }
    public blog(String title, String discription, String image,String user_name) {
        Title = title;
        Discription = discription;
        Image = image;
        User_name = user_name;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDiscription() {
        return Discription;
    }

    public void setDiscription(String discription) {
        Discription = discription;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getUser_name() {
        return User_name;
    }

    public void setUser_name(String user_name) {
        User_name = user_name;
    }
}
