package com.example.LegendsOfAndor;

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

    @Override
    public void onBackPressed(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_tab);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Button backb = (Button) findViewById(R.id.backb);
        backb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), Board.class);
                startActivity(myIntent);
            }
        });

        Button characterb = (Button) findViewById(R.id.characterb);
        characterb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), Character_Tab.class);
                startActivity(myIntent);
            }
        });

        Button actionsb = (Button) findViewById(R.id.actionsb);
        actionsb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), Free_Actions.class);
                startActivity(myIntent);
            }
        });

        Button savegameb = (Button) findViewById(R.id.savegameb);
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

    }

    private static class SaveGameSender extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();

            try {
                Unirest.post("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() + "/saveGame")
                        .header("Content-Type", "application/json")
                        .asString();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
