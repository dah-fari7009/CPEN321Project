package com.example.dodged_project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.dodged_project.databinding.ActivityAddTeammatesBinding;

public class AddTeammatesActivity extends AppCompatActivity {

    private ActivityAddTeammatesBinding binding;
    private String[] usernames = new String[5];

    private TextView userLoggedInStatusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_teammates);

//        userLoggedInStatusText = findViewById(R.id.userLoggedInStatus);
//
//        String userLoggedInStatus = getIntent().getStringExtra("USER_ACCOUNT_INFO");
//
//        if(userLoggedInStatus == null) {
//            userLoggedInStatusText.setText("Not logged in");
//        } else {
//            userLoggedInStatusText.setText("Logged in as: " + userLoggedInStatus);
//        }

        Bundle addTeammatesActivityExtra = getIntent().getExtras();

        if (addTeammatesActivityExtra != null) {
            binding.username01Textinput.setText(addTeammatesActivityExtra.getStringArray("user_input_player_names")[0]);
            binding.username02Textinput.setText(addTeammatesActivityExtra.getStringArray("user_input_player_names")[1]);
            binding.username03Textinput.setText(addTeammatesActivityExtra.getStringArray("user_input_player_names")[2]);
            binding.username04Textinput.setText(addTeammatesActivityExtra.getStringArray("user_input_player_names")[3]);
            binding.username05Textinput.setText(addTeammatesActivityExtra.getStringArray("user_input_player_names")[4]);
        }

        binding.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToFinalizeTeammates();
            }
        });

        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addTeammatesActivityExtra != null && addTeammatesActivityExtra.getBoolean("confirmedTeammatesBefore")) {
                    switchToFinalizeTeammates();
                }
                else {
                    Intent cancelFromAddTeammatesActivityIntent = new Intent(AddTeammatesActivity.this, ChooseTeammatesActivity.class);
//                    cancelFromAddTeammatesActivityIntent.putExtra("USER_ACCOUNT_INFO", userLoggedInStatus);
                    startActivity(cancelFromAddTeammatesActivityIntent);
                }
            }
        });

        binding.xButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addTeammatesActivityExtra != null && addTeammatesActivityExtra.getBoolean("confirmedTeammatesBefore")) {
                    switchToFinalizeTeammates();
                }
                else {
                    Intent cancelFromAddTeammatesActivityIntent = new Intent(AddTeammatesActivity.this, ChooseTeammatesActivity.class);
//                    cancelFromAddTeammatesActivityIntent.putExtra("USER_ACCOUNT_INFO", userLoggedInStatus);
                    startActivity(cancelFromAddTeammatesActivityIntent);
                }
            }
        });
    }

    private void switchToFinalizeTeammates() {
        // there is prolly a better way to do this
        // i'll clean it later, my brain has stopped working <>_<>
        usernames[0] = binding.username01Textinput.getText().toString();
        usernames[1] = binding.username02Textinput.getText().toString();
        usernames[2] = binding.username03Textinput.getText().toString();
        usernames[3] = binding.username04Textinput.getText().toString();
        usernames[4] = binding.username05Textinput.getText().toString();

        Intent finalizeTeammatesIntent = new Intent(AddTeammatesActivity.this, FinalizeTeammatesActivity.class);
        finalizeTeammatesIntent.putExtra("user_input_player_names", usernames);
        startActivity(finalizeTeammatesIntent);
    }
}