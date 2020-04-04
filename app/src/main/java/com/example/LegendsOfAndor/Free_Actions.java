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

enum EmptyWellResponses {
    SUCCESS, WELL_ALREADY_EMPTY, WELL_DNE
}

public class Free_Actions extends AppCompatActivity {

    MyPlayer myPlayer;

    @Override
    public void onBackPressed(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.free_actions_tab);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        myPlayer = MyPlayer.getInstance();

        Button farmer = (Button) findViewById(R.id.farmer);
        farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), FarmerInteract.class);
                startActivity(myIntent);
            }
        });

        Button interactItem = (Button) findViewById(R.id.interactItem);
        interactItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), InteractItem.class);
                startActivity(myIntent);
            }
        });

        Button backb = (Button) findViewById(R.id.backb);
        backb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), OptionsTab.class);
                startActivity(myIntent);
            }
        });

        Button merchantb = (Button) findViewById(R.id.merchantb);
        merchantb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), BuyFromMerchant.class);
                startActivity(myIntent);
            }
        });

        Button tradeb = (Button) findViewById(R.id.tradeb);
        tradeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), Trade.class);
                startActivity(myIntent);
            }
        });

        Button use_article = findViewById(R.id.articleb);
        use_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), UseArticle.class);
                startActivity(myIntent);
            }
        });

        Button wellb = (Button) findViewById(R.id.wellb);
        wellb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmptyWellResponses emptyWellResponses;
                try{
                    AsyncTask<String, Void, EmptyWellResponses> asyncTask;
                    EmptyWellSender emptyWellSender = new EmptyWellSender();
                    asyncTask = emptyWellSender.execute("");
                    emptyWellResponses = asyncTask.get();
                    if(emptyWellResponses == EmptyWellResponses.SUCCESS){
                        Toast.makeText(Free_Actions.this, "Well emptied. Willpower added", Toast.LENGTH_LONG).show();
                    }
                    else if(emptyWellResponses == EmptyWellResponses.WELL_ALREADY_EMPTY){
                        Toast.makeText(Free_Actions.this, "Well already empty", Toast.LENGTH_LONG).show();
                    }
                    else{
                        if (emptyWellResponses == EmptyWellResponses.WELL_DNE){
                            Toast.makeText(Free_Actions.this, "No well located in your region", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private static class EmptyWellSender extends AsyncTask<String, Void, EmptyWellResponses> {
        @Override
        protected EmptyWellResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() + "/" + myPlayer.getPlayer().getUsername() + "/emptyWell")
                        .header("Content-Type", "application/json")
                        .asString();
                String resultAsJsonString = response.getBody();
                return new Gson().fromJson(resultAsJsonString,EmptyWellResponses.class );
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
