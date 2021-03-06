package com.example.LegendsOfAndor;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.LegendsOfAndor.ReturnClasses.ActivateFogRC;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;

//import com.example.androidchat.R;

public class EventCard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_card);

        ImageView ec1 = findViewById(R.id.e1);
        ec1.setVisibility(View.INVISIBLE);

        ImageView ec2 = findViewById(R.id.e2);
        ec2.setVisibility(View.INVISIBLE);

        ImageView ec3 = findViewById(R.id.e3);
        ec3.setVisibility(View.INVISIBLE);

        ImageView ec4 = findViewById(R.id.e4);
        ec4.setVisibility(View.INVISIBLE);

        ImageView ec5 = findViewById(R.id.e5);
        ec5.setVisibility(View.INVISIBLE);

        ImageView ec6 = findViewById(R.id.e6);
        ec6.setVisibility(View.INVISIBLE);

        ImageView ec7 = findViewById(R.id.e7);
        ec7.setVisibility(View.INVISIBLE);

        ImageView ec8 = findViewById(R.id.e8);
        ec8.setVisibility(View.INVISIBLE);

        ImageView[] EventCards = {ec1, ec2, ec3, ec4, ec5, ec6, ec7, ec8};

        Intent myIntent = getIntent();
        final int r = myIntent.getIntExtra("EventID",-1);

        EventCards[r].setVisibility(View.VISIBLE);


        Button backAndAccept = (Button) findViewById(R.id.backAndAccept);
        Button backToBoard = (Button) findViewById(R.id.backToBoard);
        Button useShield = (Button) findViewById(R.id.useShield);

        backAndAccept.setVisibility(View.INVISIBLE);
        backToBoard.setVisibility(View.INVISIBLE);
        useShield.setVisibility(View.INVISIBLE);

        boolean checkGroupShield = false;
        MyPlayer myPlayer = MyPlayer.getInstance();
        Player[] players = myPlayer.getGame().getPlayers();
        for(int i=0; i<players.length; i++)
        {
            if (players[i].getHero() != null) {
                ArrayList<Item> heroItems = players[i].getHero().getItems();
                for(int j=0; j<heroItems.size(); j++)
                {
                    if(heroItems.get(j).getItemType()==ItemType.SHIELD)
                    {
                        checkGroupShield = true;
                        break;
                    }
                }
            }
            if(checkGroupShield){ break;}
        }

       if(r==0||r==3||r==7||checkGroupShield==false)
       {
           backAndAccept.setVisibility(View.VISIBLE);
       }
       else{
           ArrayList<Item> myItems = myPlayer.getGame().getSinglePlayer(myPlayer.getPlayer().getUsername()).getHero().getItems();
           boolean checkShield = false;
           for(int i=0; i<myItems.size(); i++)
           {
               if(myItems.get(i).getItemType()==ItemType.SHIELD)
               {
                   backAndAccept.setVisibility(View.VISIBLE);
                   useShield.setVisibility(View.VISIBLE);
                   checkShield = true;
                   break;
               }
           }

           if(!checkShield)
           {
               backToBoard.setVisibility(View.VISIBLE);
           }

       }

        backAndAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ActivateEventSender activateEventSender = new ActivateEventSender();
                    activateEventSender.execute(new Gson().toJson(r));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(EventCard.this, Board.class);
                startActivity(intent);
                finish();
            }
        });

        backToBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EventCard.this, Board.class);
                startActivity(intent);
                finish();
            }
        });

        //need go to server change foundEvent to -1
        //need to mark the shield as used
        useShield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    RejectEventSender rejectEventSender = new RejectEventSender();
                    rejectEventSender.execute("");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(EventCard.this, Board.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private static class ActivateEventSender extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();

            try {
                Unirest.post("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() + "/" + myPlayer.getPlayer().getUsername() + "/activateEvent")
                        .header("Content-Type", "application/json")
                        .body(strings[0])
                        .asString();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class RejectEventSender extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();

            try {
                Unirest.post("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() + "/" + myPlayer.getPlayer().getUsername() + "/rejectEvent")
                        .asString();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
