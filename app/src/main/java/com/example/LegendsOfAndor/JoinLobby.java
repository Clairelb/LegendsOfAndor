package com.example.LegendsOfAndor;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class JoinLobby extends AppCompatActivity {

    ArrayList<String> gameNames = new ArrayList<>();
    AsyncTask<String, Void, ArrayList<String>> asyncTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_lobby);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //SET BUTTON TEXT FONT
        Typeface gothicFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "LeagueGothic-Regular.otf");

        Button join_lobby_btn = findViewById(R.id.join_lobby_button);
        final EditText lobbyName = findViewById(R.id.lobby_name);
        ListView listView = findViewById(R.id.available_games);
        Button refreshBtn = findViewById(R.id.refresh_button);

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), JoinLobby.class);
                startActivity(myIntent);
            }
        });

        join_lobby_btn.setTypeface(gothicFont);



        try {
            JoinLobby.GameGetter gameGetter = new JoinLobby.GameGetter();
            asyncTask = gameGetter.execute();
            gameNames = asyncTask.get();

        } catch (Exception e) {
            e.printStackTrace();
        }


        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, gameNames);
        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //position is position of item in list view ie index in gameNames
                lobbyName.setText(gameNames.get(position));
            }
        });
    }


    private static class GameGetter extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;
            ArrayList<String> gameNames = new ArrayList<String>();
            try {
                response = Unirest.get("http://" + myPlayer.getServerIP() + ":8080/getAllGames")
                        .asString();
                String resultAsJsonString = response.getBody();
                resultAsJsonString = resultAsJsonString.substring(1,resultAsJsonString.length()-1);
                String[] gameNamesArray = resultAsJsonString.split(",");
                for(int i = 0; i < gameNamesArray.length; i++){
                    gameNames.add(gameNamesArray[i].substring(1,gameNamesArray[i].length()-1));
                }
                return gameNames;
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}