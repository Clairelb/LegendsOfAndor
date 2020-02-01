package com.example.LegendsOfAndor;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HostLobby extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_lobby);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //SET BUTTON TEXT FONT
        Typeface gothicFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "LeagueGothic-Regular.otf");
        final EditText lobby_name = findViewById(R.id.lobby_name);
        Button start_game_btn = findViewById(R.id.host_start_game);
        Button create_lobby_btn = findViewById(R.id.create_lobby);
        start_game_btn.setTypeface(gothicFont);
        create_lobby_btn.setTypeface(gothicFont);
        lobby_name.setTypeface(gothicFont);

        //get number of players in a variable
        Spinner s = findViewById(R.id.spinner);
        String text = s.getSelectedItem().toString();
        final int maxNumPlayers = Integer.parseInt(text);

        Spinner s2 = (Spinner) findViewById(R.id.spinner2);
        String hero_type = s2.getSelectedItem().toString();
        final MyPlayer myPlayer = MyPlayer.getInstance();


        start_game_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gameName = lobby_name.getText().toString();
                Game game = new Game(myPlayer.getPlayer(), maxNumPlayers, gameName);
                System.out.println("CREATED NEW GAME WITH " + maxNumPlayers + " PLAYERS AND GAME NAME " + gameName);

                //GO STRAIGHT TO GAME BOARD
                startActivity(new Intent(HostLobby.this, Board.class));
            }
        });

        create_lobby_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GO STRAIGHT TO GAME BOARD
                startActivity(new Intent(HostLobby.this, WaitScreen.class));
            }
        });




    }
}
