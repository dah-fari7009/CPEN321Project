package com.example.dodged_project.data;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerData {
    private String[] playerUsernames = {"Player 01", "", "Player 03", "Player 04", "Player 05"};

    public ArrayList<Player> playerList(){
        ArrayList<Player> list = new ArrayList<>();
        for (int i = 0; i < playerUsernames.length; i++) {
            Player player = new Player(UUID.randomUUID().toString(), playerUsernames[i], playerUsernames[i].equals("") ? "no_user_added_icon" : "username_added");
            list.add(player);
        }
        return list;
    }
}
