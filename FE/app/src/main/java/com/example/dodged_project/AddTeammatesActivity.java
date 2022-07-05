package com.example.dodged_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AddTeammatesActivity extends AppCompatActivity {

    private Button confirmButton;
    private Button cancelButton;
    private ImageView xButton;

    private TextView userLoggedInStatusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teammates);

        userLoggedInStatusText = findViewById(R.id.userLoggedInStatus);

        String userLoggedInStatus = getIntent().getStringExtra("USER_ACCOUNT_INFO");

        if(userLoggedInStatus == null) {
            userLoggedInStatusText.setText("Not logged in");
        } else {
            userLoggedInStatusText.setText("Logged in as: " + userLoggedInStatus);
        }

        confirmButton = findViewById(R.id.confirm_button);
        cancelButton = findViewById(R.id.cancel_button);
        xButton = findViewById(R.id.x_button_close);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent finalizeTeammatesIntent = new Intent(AddTeammatesActivity.this, FinalizeTeammatesActivity.class);
                startActivity(finalizeTeammatesIntent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancelFromAddTeammatesActivityIntent = new Intent(AddTeammatesActivity.this, ChooseTeammatesActivity.class);
                cancelFromAddTeammatesActivityIntent.putExtra("USER_ACCOUNT_INFO", userLoggedInStatus);
                startActivity(cancelFromAddTeammatesActivityIntent);
            }
        });

        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancelFromAddTeammatesActivityIntent = new Intent(AddTeammatesActivity.this, ChooseTeammatesActivity.class);
                cancelFromAddTeammatesActivityIntent.putExtra("USER_ACCOUNT_INFO", userLoggedInStatus);
                startActivity(cancelFromAddTeammatesActivityIntent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent backToChooseTeammates = new Intent(AddTeammatesActivity.this, ChooseTeammatesActivity.class);
        startActivity(backToChooseTeammates);
    }
}