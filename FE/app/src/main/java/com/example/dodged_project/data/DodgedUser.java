package com.example.dodged_project.data;

public class DodgedUser {

    private static final String DEFAULT_GOOGLE_ID = "";
    private static final String DEFAULT_RIOT_ID = "";
    private static final String DEFAULT_FIREBASE_TOKEN = "";

    private String googleID;
    private String riotID;
    private String firebaseToken;
    private boolean loggedIn = false;

    public DodgedUser () {
        this.googleID = DEFAULT_GOOGLE_ID;
        this.riotID = DEFAULT_RIOT_ID;
        this.firebaseToken = DEFAULT_FIREBASE_TOKEN;
    }

    public DodgedUser(String googleID, String riotID, String firebaseToken) {
        this.googleID = googleID;
        this.riotID = riotID;
        this.firebaseToken = firebaseToken;
    }

    public String getRiotID() {
        return riotID;
    }

    public void setRiotID(String riotID) {
        this.riotID = riotID;
    }

    public boolean getLoggedInStatus() {
        return loggedIn;
    }

    public void setLoggedInStatus(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getGoogleID() {
        return googleID;
    }

    public void setGoogleID(String googleID) {
        this.googleID = googleID;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }
}
