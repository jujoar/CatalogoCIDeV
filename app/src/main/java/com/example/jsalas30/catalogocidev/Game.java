package com.example.jsalas30.catalogocidev;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

public class Game {

    private int id;
    private String title;
    private int user_id;
    private String description;
    private String miniature;
    private String video;
    private String status;

    public Game(int id, String title, int user_id, String description, String miniature, String video, String status) {
        this.id = id;
        this.title = title;
        this.user_id = user_id;
        this.description = description;
        this.miniature = miniature;
        this.video = video;
        this.status = status;


    }

    public Game(JSONObject object) {
        try{
            this.id = object.getInt("id");
            this.title = object.getString("title");
            this.user_id = object.getInt("user_id");
            this.description = object.getString("description");
            this.miniature = object.getString("miniature");
            this.video = object.getString("video");
            this.status = object.getString("hidden");
        }
        catch(Exception ex){

        }


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMiniature() {
        return miniature;
    }

    public void setMiniature(String miniature) {
        this.miniature = miniature;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
