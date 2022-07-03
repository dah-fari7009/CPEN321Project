package com.example.dodged_project.data;

import android.content.Context;

public class Player {

    private String id;
    private String username;
    private String avatar;

    public Player(String id, String username, String avatar) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
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
}
