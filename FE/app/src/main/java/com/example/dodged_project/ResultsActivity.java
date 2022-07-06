package com.example.dodged_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Arrays;

public class ResultsActivity extends AppCompatActivity {

    private TextView resultsText;
    private TextView resultsDescription;

//    private String[] usernames = {"", "", "", "", ""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            String userLoggedInStatus = bundle.getString("USER_ACCOUNT_INFO");
            String predictionData = bundle.getString("response");
        }

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

//        FragmentManager fm = getSupportFragmentManager();
//        Fragment fragment = fm.findFragmentById(R.id.player_usernames_fragment);
//
//
//        String[] tempList = new String[5];
//        tempList[0] = "a";
//        tempList[1] = "a";
//        tempList[2] = "a";
//        tempList[3] = "a";
//        tempList[4] = "a";
//
//        Bundle bundle = new Bundle();
//        bundle.putStringArray("list", tempList);
//
//        usernames = bundle.getStringArray("list");
//
//        if (fragment == null) {
//            fragment = new PlayerUsernamesFragment(ResultsActivity.this);
//            fragment.setArguments(bundle);
//            fm.beginTransaction().add(R.id.player_usernames_fragment, fragment).commit();
//        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent backToChooseTeammates = new Intent(ResultsActivity.this, ChooseTeammatesActivity.class);
        startActivity(backToChooseTeammates);
    }

//    @Override
//    public void getPlayersFromPlayerUsernamesFragment(String[] playerUsernames) {
//        this.usernames = Arrays.copyOf(playerUsernames,playerUsernames.length);
//    }
}