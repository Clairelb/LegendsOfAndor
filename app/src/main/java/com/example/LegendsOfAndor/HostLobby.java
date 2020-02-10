package com.example.LegendsOfAndor;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

enum HostGameResponses {
    HOST_GAME_SUCCESS, ERROR_GAME_ALREADY_EXISTS
}

public class HostLobby extends AppCompatActivity {

    @Override
    public void onBackPressed() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_lobby);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        //SET BUTTON TEXT FONT
        Typeface gothicFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "LeagueGothic-Regular.otf");
        final EditText lobby_name = findViewById(R.id.lobby_name);
        Button back_btn = findViewById(R.id.back_btn);
        Button create_lobby_btn = findViewById(R.id.create_lobby);
        back_btn.setTypeface(gothicFont);
        create_lobby_btn.setTypeface(gothicFont);
        lobby_name.setTypeface(gothicFont);

        //go back to create game screen
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HostLobby.this, CreateGame.class));
            }
        });

        //get hero type in a variable S
        final Spinner s2 = (Spinner) findViewById(R.id.warrior_type);
        String hero_type = s2.getSelectedItem().toString();
        final MyPlayer myPlayer = MyPlayer.getInstance();

        create_lobby_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get number of players in a variable
                Spinner s = findViewById(R.id.num_players);
                String text = s.getSelectedItem().toString();
                int spinner_position = s.getSelectedItemPosition();
                String[] spinner_values = getResources().getStringArray(R.array.number_players_values);
                final int maxNumPlayers = Integer.valueOf(spinner_values[spinner_position]);
                System.out.println("THIS IS THE NUMBER OF PLAYERS" + maxNumPlayers);

                //get hero type in a variable S
                final Spinner s2 = (Spinner) findViewById(R.id.warrior_type);
                String hero_type = s2.getSelectedItem().toString();
                final MyPlayer myPlayer = MyPlayer.getInstance();

                AsyncTask<String, Void, HostGameResponses> asyncTask;
                HostGameResponses hostGameResponses;

                String gameName = lobby_name.getText().toString();

                myPlayer.getPlayer().setHero(new Gson().fromJson(s2.getSelectedItem().toString(), Hero.class));
                Game game = new Game(myPlayer.getPlayer(), maxNumPlayers, gameName);
                myPlayer.setGame(game);

                try {
                    HostGameSender hostGameSenderSender = new HostGameSender();
                    asyncTask = hostGameSenderSender.execute(new Gson().toJson(game));
                    hostGameResponses = asyncTask.get();

                    if (hostGameResponses == null) {
                        Toast.makeText(HostLobby.this, "Host game error. No response from server.", Toast.LENGTH_LONG).show();
                    } else if (hostGameResponses == HostGameResponses.ERROR_GAME_ALREADY_EXISTS) {
                        Toast.makeText(HostLobby.this, "Host game error. Game already exists in server", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(HostLobby.this, "Host game success. Game created.", Toast.LENGTH_LONG).show();
                        //GO STRAIGHT TO GAME BOARD
                        startActivity(new Intent(HostLobby.this, WaitScreen.class));
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static class HostGameSender extends AsyncTask<String, Void, HostGameResponses> {
        @Override
        protected HostGameResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://" + myPlayer.getServerIP() +":8080/hostGame")
                        .header("Content-Type", "application/json")
                        .body(strings[0])
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, HostGameResponses.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
