package com.example.dodged_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    private TextView resultsText;
    private TextView resultsDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // get some boolean or win rate from the BE
        // temp results text checker
        boolean tempRemoveLater = true;

        resultsText = findViewById(R.id.resultsText);
        resultsDescription = findViewById(R.id.resultsDescription);

        if(tempRemoveLater) {
            resultsText.setText(R.string.results_play);
            resultsDescription.setText(R.string.high_odds_of_winning_text);
        } else {
            resultsText.setText(R.string.results_dodge);
            resultsDescription.setText(R.string.low_odds_of_winning_text);
        }
    }
}