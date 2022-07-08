package com.example.dodged_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.dodged_project.databinding.ActivityFinalizeTeammatesBinding;

import java.util.Arrays;

public class FinalizeTeammatesActivity extends AppCompatActivity implements PlayerUsernamesFragment.Callbacks {

    private ActivityFinalizeTeammatesBinding binding;
    private String[] usernames = {"", "", "", "", ""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_finalize_teammates);

        Bundle finalizeTeammatesActivityBundle = getIntent().getExtras();
        usernames = finalizeTeammatesActivityBundle.getStringArray("user_input_player_names");

        String[] regions = new String[] {"BR1", "EUN1", "EUW1", "JP1", "KR", "LA1", "LA2", "NA1", "OC1", "RU", "TR1"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(FinalizeTeammatesActivity.this, R.layout.region_dropdown_item, regions);
        binding.regionDropdownItem.setAdapter(adapter);


        binding.addTeammatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addTeammatesIntent = new Intent(FinalizeTeammatesActivity.this, AddTeammatesActivity.class);
                addTeammatesIntent.putExtra("user_input_player_names", usernames);
                addTeammatesIntent.putExtra("confirmedTeammatesBefore", true);
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