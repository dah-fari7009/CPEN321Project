package com.example.dodged_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.dodged_project.data.Comment;
import com.example.dodged_project.data.CommentRecyclerViewAdapter;
import com.example.dodged_project.databinding.ActivityPlayerProfileBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class PlayerProfileActivity extends AppCompatActivity {

    private ActivityPlayerProfileBinding binding;
    RequestQueue queue;
    private CommentRecyclerViewAdapter commentRecyclerViewAdapter;
    private ArrayList<Comment> commentsArrayList;
    private JSONArray commentsArray;

    private View champExpPopupView;
    private MaterialAlertDialogBuilder builder;
    private String region = "NA1";
    private String selectedChamp = "Aatrox";

    private String playerProfileURL = "http://ec2-52-32-39-246.us-west-2.compute.amazonaws.com:8080/playerdb/getPlayer/";
    private String postCommentURL = "http://ec2-52-32-39-246.us-west-2.compute.amazonaws.com:8080/playerdb/comment";
    private String champMasteryURL = "http://ec2-52-32-39-246.us-west-2.compute.amazonaws.com:8080/playerdb/getMastery";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player_profile);

        queue = Volley.newRequestQueue(this);

        commentsArrayList = new ArrayList<>();

        String playerUsername = "";
        Double kps = 0.0;
        Double aps = 0.0;
        Double dps = 0.0;
        Double gps = 0.0;
        Double vps = 0.0;

        Bundle playerProfileExtra = getIntent().getExtras();
        if (playerProfileExtra != null) {
            playerUsername = playerProfileExtra.getString("player_username");
            region = playerProfileExtra.getString("region");
            kps = playerProfileExtra.getDouble("kps");
            aps = playerProfileExtra.getDouble("aps");
            dps = playerProfileExtra.getDouble("dps");
            gps = playerProfileExtra.getDouble("gps");
            vps = playerProfileExtra.getDouble("vps");
        }

        binding.playerProfileUsername.setText(playerUsername);
        binding.statsKillsTextview.setText(String.format("%.2f", kps * 60.0));
        binding.statsDeathsTextview.setText(String.format("%.2f", dps * 60.0));
        binding.statsAssistsTextview.setText(String.format("%.2f", aps * 60.0));
        binding.statsGoldTextview.setText(String.format("%.2f", gps * 60.0));
        binding.statsVisionTextview.setText(String.format("%.2f", vps * 60.0));

        playerProfileURL = playerProfileURL + playerUsername;

        String finalPlayerUsername = playerUsername;

        getComments(finalPlayerUsername,PlayerProfileActivity.this);

        String[] champions = new String[] {"Aatrox", "Ahri", "Akali", "Akshan", "Alistar", "Amumu", "Anivia", "Annie", "Aphelios", "Ashe", "AurelionSol", "Azir", "Bard", "Belveth", "Blitzcrank", "Brand", "Braum", "Caitlyn", "Camille", "Cassiopeia", "Chogath", "Corki", "Darius", "Diana", "Draven", "DrMundo", "Ekko", "Elise", "Evelynn", "Ezreal", "Fiddlesticks", "Fiora", "Fizz", "Galio", "Gangplank", "Garen", "Gnar", "Gragas", "Graves", "Gwen", "Hecarim", "Heimerdinger", "Illaoi", "Irelia", "Ivern", "Janna", "JarvanIV", "Jax", "Jayce", "Jhin", "Jinx", "Kaisa", "Kalista", "Karma", "Karthus", "Kassadin", "Katarina", "Kayle", "Kayn", "Kennen", "Khazix", "Kindred", "Kled", "KogMaw", "Leblanc", "LeeSin", "Leona", "Lillia", "Lissandra", "Lucian", "Lulu", "Lux", "Malphite", "Malzahar", "Maokai", "MasterYi", "MissFortune", "MonkeyKing", "Mordekaiser", "Morgana", "Nami", "Nasus", "Nautilus", "Neeko", "Nidalee", "Nocturne", "Nunu", "Olaf", "Orianna", "Ornn", "Pantheon", "Poppy", "Pyke", "Qiyana", "Quinn", "Rakan", "Rammus", "RekSai", "Rell", "Renata", "Renekton", "Rengar", "Riven", "Rumble", "Ryze", "Samira", "Sejuani", "Senna", "Seraphine", "Sett", "Shaco", "Shen", "Shyvana", "Singed", "Sion", "Sivir", "Skarner", "Sona", "Soraka", "Swain", "Sylas", "Syndra", "TahmKench", "Taliyah", "Talon", "Taric", "Teemo", "Thresh", "Tristana", "Trundle", "Tryndamere", "TwistedFate", "Twitch", "Udyr", "Urgot", "Varus", "Vayne", "Veigar", "Velkoz", "Vex", "Vi", "Viego", "Viktor", "Vladimir", "Volibear", "Warwick", "Xayah", "Xerath", "XinZhao", "Yasuo", "Yone", "Yorick", "Yuumi", "Zac", "Zed", "Zeri", "Ziggs", "Zilean", "Zoe", "Zyra"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(PlayerProfileActivity.this, R.layout.champion_dropdown_item, champions);
        binding.championDropdownItem.setAdapter(adapter);

        binding.championDropdownItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedChamp = (String) parent.getItemAtPosition(position);
            }
        });

        binding.championExpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getMastery(finalPlayerUsername, region, selectedChamp, PlayerProfileActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                builder = new MaterialAlertDialogBuilder(PlayerProfileActivity.this);
                champExpPopupView = LayoutInflater.from(PlayerProfileActivity.this).inflate(R.layout.champ_exp_popup, null, false);
            }
        });

        binding.commentsRecyclerView.setHasFixedSize(true);
        binding.commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        populateRecyclerView();
        if(MainActivity.googleAccountName == null) {
            binding.logInToCommentHint.setVisibility(View.VISIBLE);
            binding.commentTextviewAndAddButton.setVisibility(View.GONE);
        }
        else {
            binding.logInToCommentHint.setVisibility(View.GONE);
            binding.commentTextviewAndAddButton.setVisibility(View.VISIBLE);
            binding.addCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        postComment(binding.addCommentTextinput.getText().toString(), finalPlayerUsername, PlayerProfileActivity.this);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    binding.addCommentTextinput.setText("");
                    populateRecyclerView();
                }
            });
        }
    }

    private void populateRecyclerView() {
        commentRecyclerViewAdapter = new CommentRecyclerViewAdapter(commentsArrayList);
        binding.commentsRecyclerView.setAdapter(commentRecyclerViewAdapter);
        commentRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void getComments(String finalPlayerUsername, Context context){
        commentsArrayList.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, playerProfileURL, null,
                response ->
                {
                    try {
                        commentsArray = response.getJSONArray("comments");
                        //Log.d("JSON", response.getJSONArray("comments").toString());
                        for (int i = 0; i < commentsArray.length(); i++) {
                            if (commentsArray.getJSONObject(i) != null) {
//                                Log.d("JSON", "" + commentsArray.getJSONObject(i).length());
//                                Log.d("JSON", "" + commentsArray.getJSONObject(i).get("poster"));
//                                Log.d("JSON", "" + commentsArray.getJSONObject(i).getString("poster"));
                                String poster =commentsArray.getJSONObject(i).getString("poster");
                                String date = commentsArray.getJSONObject(i).getString("date");
                                String comment = commentsArray.getJSONObject(i).getString("comment");

                                Comment commentItem = new Comment(poster, date, comment, finalPlayerUsername);
                                commentsArrayList.add(commentItem);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Collections.reverse(commentsArrayList);
                    //Log.d("JSON","" + commentsArrayList.size());
                    commentRecyclerViewAdapter = new CommentRecyclerViewAdapter(commentsArrayList);
                    binding.commentsRecyclerView.setAdapter(commentRecyclerViewAdapter);
                    commentRecyclerViewAdapter.notifyDataSetChanged();
                },
                error -> {
                    Log.d("JSON", error.toString());
                });
        queue = Volley.newRequestQueue(context);
        queue.add(jsonObjectRequest);
    }

    private void postComment(String comment, String playerUsername, Context context) throws JSONException {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("name", playerUsername);
        jsonBody.put("poster", MainActivity.googleAccountName);
        jsonBody.put("date", new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
        jsonBody.put("comment", comment);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postCommentURL, jsonBody,
                response ->
                {
                    Toast.makeText(getApplicationContext(), "Comment Posted", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Log.d("JSON", error.toString());
                });
        queue = Volley.newRequestQueue(context);
        queue.add(jsonObjectRequest);
        Comment commentItem = new Comment(MainActivity.googleAccountName, new SimpleDateFormat("MM/dd/yyyy").format(new Date()), comment, playerUsername);
        commentsArrayList.add(0, commentItem);
    }

    private void getMastery(String playerUsername, String region, String champ, Context context) throws JSONException {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("name", playerUsername);
        jsonBody.put("region", region);
        jsonBody.put("champ", champ);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, champMasteryURL, jsonBody,
                response ->
                {
                    TextView champPopupTitle = (TextView) champExpPopupView.findViewById(R.id.champ_popup_title_textview);
                    champPopupTitle.setText(playerUsername + "'s Exp Level on " + selectedChamp);
                    TextView champExpLvl = (TextView) champExpPopupView.findViewById(R.id.champ_exp_level_textview);
                    TextView masteryPoints = (TextView) champExpPopupView.findViewById(R.id.mastery_points_textview);
                    TextView topChamp1 = (TextView) champExpPopupView.findViewById(R.id.top_champ_1_textview);
                    TextView topChamp2 = (TextView) champExpPopupView.findViewById(R.id.top_champ_2_textview);
                    TextView topChamp3 = (TextView) champExpPopupView.findViewById(R.id.top_champ_3_textview);
                    try {
                        if (response.has("playTime")) {
                            champExpLvl.setText(response.getString("playTime"));
                        }
                        else {
                            champExpLvl.setText("No Experience");
                        }

                        if (response.has("mastery")) {
                            masteryPoints.setText(response.getString("mastery"));
                        }
                        else {
                            masteryPoints.setText("0");
                        }

                        if (response.has("top1")) {
                            topChamp1.setText(response.getString("top1"));
                        }
                        else {
                            topChamp1.setText("--");
                        }

                        if (response.has("top2")) {
                            topChamp2.setText(response.getString("top2"));
                        }
                        else {
                            topChamp2.setText("--");
                        }

                        if (response.has("top3")) {
                            topChamp3.setText(response.getString("top3"));
                        }
                        else {
                            topChamp3.setText("--");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showChampExpPopUp();
                },
                error -> {
                    Log.d("JSON", error.toString());
                });
        queue = Volley.newRequestQueue(context);
        queue.add(jsonObjectRequest);
    }

    private void showChampExpPopUp() {
        builder.setView(champExpPopupView);
        builder.show();
    }
}