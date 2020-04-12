package com.example.LegendsOfAndor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.sql.Savepoint;

public class OptionsTab extends AppCompatActivity {
    private Button backb;
    private Button characterb;
    private Button actionsb;
    private Button savegameb;
    private Button leavegameb;
    private MyPlayer myPlayer;

    @Override
    public void onBackPressed(){}

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_tab);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        myPlayer = MyPlayer.getInstance();

        try{
            AsyncTask<String, Void, Game> asyncTaskGame;
            Game gameToSet;
            GetGame getGame = new GetGame();
            asyncTaskGame = getGame.execute();
            gameToSet = asyncTaskGame.get();
            System.out.println(gameToSet);
            myPlayer.setGame(gameToSet);
            for(int i = 0; i < gameToSet.getCurrentNumPlayers(); i++){
                if(gameToSet.getPlayers()[i].getUsername().equals(myPlayer.getPlayer().getUsername())){
                    myPlayer.setPlayer(gameToSet.getPlayers()[i]);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        actionsb = findViewById(R.id.actionsb);

        if(!myPlayer.getPlayer().getHero().isHasEndedDay()){
            actionsb.setVisibility(View.VISIBLE);
        }else{
            actionsb.setVisibility(View.INVISIBLE);
        }

        actionsb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), Free_Actions.class);
                startActivity(myIntent);
            }
        });

        backb = findViewById(R.id.backb);
        backb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), Board.class);
                startActivity(myIntent);
            }
        });

        characterb = findViewById(R.id.characterb);
        characterb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), Character_Tab.class);
                startActivity(myIntent);
            }
        });


        savegameb = findViewById(R.id.savegameb);
        savegameb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    AsyncTask<String, Void, Void> asyncTask;
                    SaveGameSender saveGameSender = new SaveGameSender();
                    asyncTask = saveGameSender.execute("");
                    Toast.makeText(OptionsTab.this, "Game Saved", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        leavegameb = findViewById(R.id.leavegameb);
        leavegameb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LeaveGameSender leaveGameSender = new LeaveGameSender();
                leaveGameSender.execute("");

                Toast.makeText(OptionsTab.this, "Leaving game...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(OptionsTab.this, CreateGame.class);
                startActivity(intent);
            }
        });

    }

    private static class SaveGameSender extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();

            try {
                Unirest.post("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/saveGame")
                        .asString();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class LeaveGameSender extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();

            try {
                Unirest.delete("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/leaveGame")
                        .asString();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class GetGame extends AsyncTask<String, Void, Game > {
        @Override
        protected Game doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.get("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getPlayer().getUsername() + "/getGameByUsername")
                        .asString();

                String resultAsJsonString = response.getBody();
                System.out.println("RESPONSE BODY " + response.getBody());
                return new Gson().fromJson(resultAsJsonString, Game.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
