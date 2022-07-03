package com.example.dodged_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.dodged_project.data.Player;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddTeammatesActivity extends AppCompatActivity {

    private Button confirmButton;
    private Button cancelButton;
    private ImageView xButton;

    private String[] usernames = new String[5];
    private TextInputEditText usernameTextInput01;
    private TextInputEditText usernameTextInput02;
    private TextInputEditText usernameTextInput03;
    private TextInputEditText usernameTextInput04;
    private TextInputEditText usernameTextInput05;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teammates);

        Bundle addTeammatesActivityExtra = getIntent().getExtras();

        confirmButton = findViewById(R.id.confirm_button);
        cancelButton = findViewById(R.id.cancel_button);
        xButton = findViewById(R.id.x_button_close);

        // there is 100% a better/cleaner way to do this
        // i'll clean it later, my brain has stopped working <>_<>
        usernameTextInput01 = findViewById(R.id.username_01_textinput);
        usernameTextInput02 = findViewById(R.id.username_02_textinput);
        usernameTextInput03 = findViewById(R.id.username_03_textinput);
        usernameTextInput04 = findViewById(R.id.username_04_textinput);
        usernameTextInput05 = findViewById(R.id.username_05_textinput);

        if (addTeammatesActivityExtra != null) {
            usernameTextInput01.setText(addTeammatesActivityExtra.getStringArray("user_input_player_names")[0]);
            usernameTextInput02.setText(addTeammatesActivityExtra.getStringArray("user_input_player_names")[1]);
            usernameTextInput03.setText(addTeammatesActivityExtra.getStringArray("user_input_player_names")[2]);
            usernameTextInput04.setText(addTeammatesActivityExtra.getStringArray("user_input_player_names")[3]);
            usernameTextInput05.setText(addTeammatesActivityExtra.getStringArray("user_input_player_names")[4]);
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // there is 100% a better way to do this
                // i'll clean it later, my brain has stopped working <>_<>
                usernames[0] = usernameTextInput01.getText().toString();
                usernames[1] = usernameTextInput02.getText().toString();
                usernames[2] = usernameTextInput03.getText().toString();
                usernames[3] = usernameTextInput04.getText().toString();
                usernames[4] = usernameTextInput05.getText().toString();

                Intent finalizeTeammatesIntent = new Intent(AddTeammatesActivity.this, FinalizeTeammatesActivity.class);
                finalizeTeammatesIntent.putExtra("user_input_player_names", usernames);
                startActivity(finalizeTeammatesIntent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancelFromAddTeammatesActivityIntent = new Intent(AddTeammatesActivity.this, ChooseTeammatesActivity.class);
                startActivity(cancelFromAddTeammatesActivityIntent);
            }
        });

        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancelFromAddTeammatesActivityIntent = new Intent(AddTeammatesActivity.this, ChooseTeammatesActivity.class);
                startActivity(cancelFromAddTeammatesActivityIntent);
            }
        });
    }
}