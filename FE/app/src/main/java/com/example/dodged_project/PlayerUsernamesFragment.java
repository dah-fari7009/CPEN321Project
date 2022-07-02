package com.example.dodged_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.dodged_project.data.Player;
import com.example.dodged_project.data.PlayerArrayAdapter;
import com.example.dodged_project.data.PlayerData;

import java.util.List;

public class PlayerUsernamesFragment extends ListFragment {
    List<Player> players = new PlayerData().playerList();

    public PlayerUsernamesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PlayerArrayAdapter adapter = new PlayerArrayAdapter(getActivity(), R.layout.player_username_item, players);
        setListAdapter(adapter);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.player_usernames_fragment, container, false);
        return view;
    }
}
