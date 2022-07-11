package com.example.dodged_project.data;

import android.content.Context;

import org.json.JSONObject;

public class Player {

    private String id;
    private String username;
    private String avatar;
    private String region;

    private int likes;
    private int dislikes;
    private double kps;
    private double aps;
    private double dps;
    private double gps;
    private double vps;
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

    public Player(String username, String region, int likes, int dislikes, double kps, double aps, double dps, double gps, double vps) {
        this.username = username;
        this.region = region;
        this.likes = likes;
        this.dislikes = dislikes;
        this.kps = kps;
        this.aps = aps;
        this.dps = dps;
        this.gps = gps;
        this.vps = vps;
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

    public double getKps() {
        return kps;
    }

    public void setKps(double kps) {
        this.kps = kps;
    }

    public double getAps() {
        return aps;
    }

    public void setAps(double aps) {
        this.aps = aps;
    }

    public double getDps() {
        return dps;
    }

    public void setDps(double dps) {
        this.dps = dps;
    }

    public double getGps() {
        return gps;
    }

    public void setGps(double gps) {
        this.gps = gps;
    }

    public double getVps() {
        return vps;
    }

    public void setVps(double vps) {
        this.vps = vps;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    //    public JSONObject getStats() {
//        return stats;
//    }
//
//    public void setStats(JSONObject stats) {
//        this.stats = stats;
//    }
}
