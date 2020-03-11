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

public class EndMove_Well extends AppCompatActivity {

    @Override
    public void onBackPressed(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.endmove_well);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Button no_btn = (Button) findViewById(R.id.no_btn);
        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), Board.class);
                startActivity(myIntent);
            }
        });

        Button yes_btn = (Button) findViewById(R.id.yes_btn);
        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmptyWellResponses emptyWellResponses;
                try{
                    AsyncTask<String, Void, EmptyWellResponses> asyncTask;
                    EmptyWellSender emptyWellSender = new EmptyWellSender();
                    asyncTask = emptyWellSender.execute("");
                    emptyWellResponses = asyncTask.get();
                    if(emptyWellResponses == EmptyWellResponses.SUCCESS){
                        Toast.makeText(EndMove_Well.this, "Well emptied. Willpower added", Toast.LENGTH_LONG).show();
                    }
                    else if(emptyWellResponses == EmptyWellResponses.WELL_ALREADY_EMPTY){
                        Toast.makeText(EndMove_Well.this, "Well already empty", Toast.LENGTH_LONG).show();
                    }
                    else{
                        if (emptyWellResponses == EmptyWellResponses.WELL_DNE){
                            Toast.makeText(EndMove_Well.this, "No well located in your region", Toast.LENGTH_LONG).show();
                        }
                    }
                    Intent myIntent = new Intent(v.getContext(), Board.class);
                    startActivity(myIntent);
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
