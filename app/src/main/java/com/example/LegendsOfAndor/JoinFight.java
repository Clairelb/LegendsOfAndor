package com.example.LegendsOfAndor;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class JoinFight extends AppCompatActivity {
    Button joinFight;
    Button declineFight;
    MyPlayer myPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_fight_options);

        myPlayer = MyPlayer.getInstance();

        joinFight = findViewById(R.id.joinFight);
        joinFight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JoinFightSender joinFightSender = new JoinFightSender();
                    joinFightSender.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                startActivity(new Intent(JoinFight.this, MonsterFight.class));
                finish();
            }
        });

        declineFight = findViewById(R.id.declineFight);
        declineFight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DeclineFightInvitationSender declineFightInvitationSender = new DeclineFightInvitationSender();
                    declineFightInvitationSender.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                startActivity(new Intent(JoinFight.this, Board.class));
                finish();
            }
        });
    }

    private static class JoinFightSender extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();

            try {
                Unirest.post("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/joinFight")
                        .asString();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class DeclineFightInvitationSender extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();

            try {
                Unirest.post("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/declineFightInvitation")
                        .asString();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
