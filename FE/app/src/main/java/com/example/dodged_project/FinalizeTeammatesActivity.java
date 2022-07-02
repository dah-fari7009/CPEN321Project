package com.example.dodged_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FinalizeTeammatesActivity extends AppCompatActivity {

    private Button addTeammatesButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalize_teammates);

        addTeammatesButton = findViewById(R.id.add_teammates_button);

        addTeammatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addTeammatesIntent = new Intent(FinalizeTeammatesActivity.this, AddTeammatesActivity.class);
                startActivity(addTeammatesIntent);
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.player_usernames_fragment);

        if (fragment == null) {
            fragment = new PlayerUsernamesFragment();
            fm.beginTransaction().add(R.id.player_usernames_fragment, fragment).commit();
        }
    }
}