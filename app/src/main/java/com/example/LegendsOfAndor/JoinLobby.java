package com.example.LegendsOfAndor;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_lobby);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //SET BUTTON TEXT FONT
        Typeface gothicFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "LeagueGothic-Regular.otf");
        Button join_lobby_btn = findViewById(R.id.join_lobby_button);
        Button ready_btn = findViewById(R.id.ready_button);

        join_lobby_btn.setTypeface(gothicFont);
        ready_btn.setTypeface(gothicFont);

        AsyncTask<String, Void, ArrayList<String>> asyncTask;
        ArrayList<String> gameNames = new ArrayList<>();

        try {
            JoinLobby.GameGetter gameGetter = new JoinLobby.GameGetter();
            asyncTask = gameGetter.execute();
            gameNames = asyncTask.get();

            System.out.println("THESE ARE THE GAMES" + gameNames.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView textView = findViewById(R.id.gameList);
        textView.setText(gameNames.get(0));
    }


    private static class GameGetter extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.get("http://" + myPlayer.getServerIP() + ":8080/getAllGames")
                        .asString();
                String resultAsJsonString = response.getBody();

                ArrayList<String> demoResult = new ArrayList<String>();
                demoResult.add(resultAsJsonString);
                return demoResult;
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}