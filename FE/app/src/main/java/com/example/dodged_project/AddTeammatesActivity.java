package com.example.dodged_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
                Intent finalizeTeammatesIntent = new Intent(AddTeammatesActivity.this, FinalizeTeammatesActivity.class);
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