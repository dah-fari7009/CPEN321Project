package com.example.dodged_project;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.dodged_project.data.Player;
import com.example.dodged_project.data.PlayerArrayAdapter;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerUsernamesFragment extends ListFragment implements PlayerArrayAdapter.Callbacks {
    private ArrayList<Player> players = new ArrayList<>();
    private Callbacks callback;
    public PlayerUsernamesFragment(Callbacks callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            createPlayerArrayList(getArguments().getStringArray("user_input_player_names"));
        }
        PlayerArrayAdapter adapter = new PlayerArrayAdapter(getActivity(), R.layout.player_username_item, players, this);
        setListAdapter(adapter);
    }

    private void createPlayerArrayList(String[] playerUsernames) {
        for (int i = 0; i < playerUsernames.length; i++) {
            Player player = new Player(UUID.randomUUID().toString(), playerUsernames[i], playerUsernames[i].equals("") ? "no_user_added_icon" : "username_added");
            players.add(player);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.player_usernames_fragment, container, false);
        Button button = (Button) view.findViewById(R.id.confirm_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("fragment button", "CLICKED ");
            }
        });
        return view;
    }

    @Override
    public void getPlayersFromPlayerArrayAdapter(ArrayList<Player> players) {
        this.players = players;
        String[] usernames = {"", "", "", "", ""};
        if (!players.isEmpty()) {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i) != null)
                usernames[i] = players.get(i).getUsername() != null ? players.get(i).getUsername() : "";
            }
        }
        callback.getPlayersFromPlayerUsernamesFragment(usernames);
    }

    public interface Callbacks {
        void getPlayersFromPlayerUsernamesFragment(String[] playerUserNames);
    }

}
