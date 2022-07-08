package com.example.dodged_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.dodged_project.data.Comment;
import com.example.dodged_project.data.CommentRecyclerViewAdapter;
import com.example.dodged_project.databinding.ActivityPlayerProfileBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PlayerProfileActivity extends AppCompatActivity {

    private ActivityPlayerProfileBinding binding;
    RequestQueue queue;
    private CommentRecyclerViewAdapter commentRecyclerViewAdapter;
    private ArrayList<Comment> commentsArrayList;
    private JSONArray commentsArray;

    private String playerProfileURL = "http://ec2-52-32-39-246.us-west-2.compute.amazonaws.com:8080/playerdb/getPlayer/";
    private String postCommentURL = "http://ec2-52-32-39-246.us-west-2.compute.amazonaws.com:8080/playerdb/comment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player_profile);

        queue = Volley.newRequestQueue(this);

        commentsArrayList = new ArrayList<>();

//        Bundle playerProfileExtra = getIntent().getExtras();
//        if (playerProfileExtra != null) {
//            String playerUsername = playerProfileExtra.getString("player_username");
//            playerProfileURL = playerProfileURL + "?name=" + playerUsername;
//        }

        String playerUsername = "APAP";
        playerProfileURL = playerProfileURL + playerUsername;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, playerProfileURL, null,
                response ->
                {
                    try {
                        commentsArray = response.getJSONArray("comments");
                        //Log.d("JSON", response.getJSONArray("comments").toString());
                        for (int i = 5; i < commentsArray.length(); i++) {
                            if (commentsArray.getJSONObject(i) != null) {
//                                Log.d("JSON", "" + commentsArray.getJSONObject(i).length());
//                                Log.d("JSON", "" + commentsArray.getJSONObject(i).get("poster"));
//                                Log.d("JSON", "" + commentsArray.getJSONObject(i).getString("poster"));
                                String poster =commentsArray.getJSONObject(i).getString("poster");
                                String date = commentsArray.getJSONObject(i).getString("date");
                                String comment = commentsArray.getJSONObject(i).getString("comment");

                                Comment commentItem = new Comment(poster, date, comment, playerUsername);
                                commentsArrayList.add(commentItem);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Log.d("JSON","" + commentsArrayList.size());
                    commentRecyclerViewAdapter = new CommentRecyclerViewAdapter(commentsArrayList);
                    binding.commentsRecyclerView.setAdapter(commentRecyclerViewAdapter);
                    commentRecyclerViewAdapter.notifyDataSetChanged();
                },
                error -> {
                    Log.d("JSON", error.toString());
                });

        queue.add(jsonObjectRequest);

        binding.commentsRecyclerView.setHasFixedSize(true);
        binding.commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        commentRecyclerViewAdapter = new CommentRecyclerViewAdapter(commentsArrayList);
        binding.commentsRecyclerView.setAdapter(commentRecyclerViewAdapter);
        commentRecyclerViewAdapter.notifyDataSetChanged();

        binding.addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    postComment(binding.addCommentTextinput.getText().toString(), playerUsername, PlayerProfileActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                binding.addCommentTextinput.setText("");
            }
        });
    }

    private void postComment(String comment, String playerUsername, Context context) throws JSONException {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("name", playerUsername);
        jsonBody.put("poster", playerUsername);
        jsonBody.put("date", new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
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
    }
}