package com.example.dodged_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;

public class AddTeammatesActivity extends AppCompatActivity {

    private Button confirmButton;
    private Button cancelButton;
    private ImageView xButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teammates);

        confirmButton = findViewById(R.id.confirm_button);
        cancelButton = findViewById(R.id.cancel_button);
        xButton = findViewById(R.id.x_button_close);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // there has to be a better way to do this
                // i'll clean it later, my brain has stopped working <>_<>
                String[] usernames = new String[5];
                TextInputEditText usernameTextInput;

                usernameTextInput = findViewById(R.id.username_01_textinput);
                usernames[0] = usernameTextInput.getText().toString();

                usernameTextInput = findViewById(R.id.username_02_textinput);
                usernames[1] = usernameTextInput.getText().toString();

                usernameTextInput = findViewById(R.id.username_03_textinput);
                usernames[2] = usernameTextInput.getText().toString();

                usernameTextInput = findViewById(R.id.username_04_textinput);
                usernames[3] = usernameTextInput.getText().toString();

                usernameTextInput = findViewById(R.id.username_05_textinput);
                usernames[4] = usernameTextInput.getText().toString();

                Intent finalizeTeammatesIntent = new Intent(AddTeammatesActivity.this, FinalizeTeammatesActivity.class);
                finalizeTeammatesIntent.putExtra("user_input_playernames", usernames);
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