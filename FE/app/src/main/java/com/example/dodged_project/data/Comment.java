package com.example.dodged_project.data;

import java.util.Date;

public class Comment {
    private String poster;
    private String date;
    private String comment;
    private String username;

    public Comment(String poster, String date, String comment, String username) {
        this.poster = poster != null ? poster : "";
        this.date = date != null ? date : "";
        this.comment = comment != null ? comment : "";
        this.username = username != null ? username : "";
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
