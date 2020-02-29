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
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


enum JoinGameResponses {
    JOIN_GAME_SUCCESS, ERROR_GAME_FULL, ERROR_GAME_DNE
}


public class JoinLobby extends AppCompatActivity {



    ArrayList<Game> games;
    ArrayList<String> gameNames = new ArrayList<>();
    AsyncTask<String, Void, ArrayList<Game>> asyncTask;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_lobby);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Button back_btn = findViewById(R.id.back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(JoinLobby.this, CreateGame.class));
            }
        });

        //SET BUTTON TEXT FONT
        Typeface gothicFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "LeagueGothic-Regular.otf");

        Button join_lobby_btn = findViewById(R.id.join_lobby_button);
        final TextView join_game_name = findViewById(R.id.join_game_name);
        ListView listView = findViewById(R.id.available_games);
        Button refreshBtn = findViewById(R.id.refresh_button);
        TextView game_names = findViewById(R.id.game_names);

        join_lobby_btn.setTypeface(gothicFont);
        join_game_name.setTypeface(gothicFont);
        game_names.setTypeface(gothicFont);
        refreshBtn.setTypeface(gothicFont);

        try {
            JoinLobby.GameGetter gameGetter = new JoinLobby.GameGetter();
            asyncTask = gameGetter.execute();
            games = asyncTask.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(games != null){
            for (Game g: games) {
                if(g.getGameName()!= null){
                    gameNames.add(g.getGameName());
                }
            }
        }else{
            gameNames.add("THIS IS WEIRD");
        }


        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, gameNames);
        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //position is position of item in list view ie index in gameNames
               join_game_name.setText(gameNames.get(position));
            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        join_lobby_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask<String, Void, JoinGameResponses> asyncTask;
                JoinGameResponses joinGameResponses;

                if (join_game_name.getText() == null || join_game_name.getText().length() == 0) {
                    Toast.makeText(JoinLobby.this, "No game selected error.", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        String gameName = join_game_name.getText().toString();
                        JoinGameSender joinGameSender = new JoinGameSender();
                        asyncTask = joinGameSender.execute(gameName);
                        joinGameResponses = asyncTask.get();

                        if (joinGameResponses == null) {
                            Toast.makeText(JoinLobby.this, "Join game error. No response from server.", Toast.LENGTH_LONG).show();
                        } else if (joinGameResponses == JoinGameResponses.ERROR_GAME_DNE) {
                            Toast.makeText(JoinLobby.this, "Join game error. Game does not exist.", Toast.LENGTH_LONG).show();
                        } else if (joinGameResponses == JoinGameResponses.ERROR_GAME_FULL) {
                            Toast.makeText(JoinLobby.this, "Join game error. Game already full.", Toast.LENGTH_LONG).show();
                        } else if (joinGameResponses == JoinGameResponses.JOIN_GAME_SUCCESS) {
                            Toast.makeText(JoinLobby.this, "Join game success. Added to game " + gameName + ".", Toast.LENGTH_LONG).show();

                            MyPlayer myPlayer = MyPlayer.getInstance();
                            for (Game g : games) {
                                if (g.getGameName().equals(gameName)) {
                                    g.addPlayer(myPlayer.getPlayer());
                                    myPlayer.setGame(g);
                                    break;
                                }
                            }
                            startActivity(new Intent(JoinLobby.this, WaitScreen.class));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        });



    }


    private static class GameGetter extends AsyncTask<String, Void, ArrayList<Game>> {
        @Override
        protected ArrayList<Game> doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.get("http://" + myPlayer.getServerIP() + ":8080/getAllGames")
                        .asString();

                String resultAsJsonString = response.getBody();
                System.out.println("RESPONSE BODY " + response.getBody());
                return new Gson().fromJson(resultAsJsonString, new TypeToken<ArrayList<Game>>() {}.getType());
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class JoinGameSender extends AsyncTask<String, Void, JoinGameResponses> {
        @Override
        protected JoinGameResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://" + myPlayer.getServerIP() +":8080/"+strings[0]+"/"+MyPlayer.getInstance().getPlayer().getUsername()+"/joinGame")
                        .header("Content-Type", "application/json")
                        .asString();
                String resultAsJsonString = response.getBody();
                return new Gson().fromJson(resultAsJsonString, JoinGameResponses.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}