package com.example.dodged_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.dodged_project.data.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class FinalizeTeammatesActivity extends AppCompatActivity implements PlayerUsernamesFragment.Callbacks {

    private Button addTeammatesButton;
    private String[] usernames = {"", "", "", "", ""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalize_teammates);

        Bundle finalizeTeammatesActivityBundle = getIntent().getExtras();
        usernames = finalizeTeammatesActivityBundle.getStringArray("user_input_player_names");

        addTeammatesButton = findViewById(R.id.add_teammates_button);

        addTeammatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addTeammatesIntent = new Intent(FinalizeTeammatesActivity.this, AddTeammatesActivity.class);
                addTeammatesIntent.putExtra("user_input_player_names", usernames);
                startActivity(addTeammatesIntent);
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.player_usernames_fragment);

        if (fragment == null) {
            fragment = new PlayerUsernamesFragment(this);
            fragment.setArguments(finalizeTeammatesActivityBundle);
            fm.beginTransaction().add(R.id.player_usernames_fragment, fragment).commit();
        }
    }

    @Override
    public void getPlayersFromPlayerUsernamesFragment(String[] playerUsernames) {
        this.usernames = Arrays.copyOf(playerUsernames,playerUsernames.length);
    }
}