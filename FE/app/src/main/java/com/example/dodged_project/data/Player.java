package com.example.dodged_project.data;

import android.content.Context;

import org.json.JSONObject;

public class Player {

    private String id;
    private String username;
    private String avatar;

    private int likes;
    private int dislikes;
//    private JSONObject stats;

    public Player(String id, String username, String avatar) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
    }

    public Player(String username, int likes, int dislikes) {
        this.username = username;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public int getAvatarResourceId(Context context) {
        return context.getResources().getIdentifier(this.avatar,"drawable", context.getPackageName());
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

//    public JSONObject getStats() {
//        return stats;
//    }
//
//    public void setStats(JSONObject stats) {
//        this.stats = stats;
//    }
}
