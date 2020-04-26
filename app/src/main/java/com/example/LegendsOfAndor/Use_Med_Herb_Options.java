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

enum MedicinalHerbWillPowerResponses {
    ERROR_DOES_NOT_OWN_MEDICINAL_HERB, MEDICINAL_HERB_WILLPOWER_SUCCESS
}

enum MedicinalHerbMoveResponses {
    ERROR_DOES_NOT_OWN_MEDICINAL_HERB, ERROR_NOT_CURRENT_HERO, MEDICINAL_HERB_MOVE_SUCCESS
}

public class Use_Med_Herb_Options extends AppCompatActivity {

    @Override
    public void onBackPressed(){}

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.use_med_herb_options);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Button backb = (Button) findViewById(R.id.backb);
        backb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), UseArticle.class);
                startActivity(myIntent);
            }
        });

        Button willpowerb = (Button) findViewById(R.id.willpowerb);
        willpowerb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedicinalHerbWillPowerResponses medicinalHerbWillPowerResponses;
                try{
                    AsyncTask<String, Void, MedicinalHerbWillPowerResponses> asyncTask;
                    MedHerbWillPowerSender medHerbWillPowerSender = new MedHerbWillPowerSender();
                    asyncTask = medHerbWillPowerSender.execute("");
                    medicinalHerbWillPowerResponses = asyncTask.get();
                    if(medicinalHerbWillPowerResponses == MedicinalHerbWillPowerResponses.ERROR_DOES_NOT_OWN_MEDICINAL_HERB){
                        Toast.makeText(Use_Med_Herb_Options.this, "Error. You do not own a medicinal herb", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(Use_Med_Herb_Options.this, "WillPower points added", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button moveb = (Button) findViewById(R.id.moveb);
        moveb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedicinalHerbMoveResponses medicinalHerbMoveResponses;
                try{
                    AsyncTask<String, Void, MedicinalHerbMoveResponses> asyncTask;
                    MedHerbMoveSender medHerbMoveSender = new MedHerbMoveSender();
                    asyncTask = medHerbMoveSender.execute("");
                    medicinalHerbMoveResponses = asyncTask.get();
                    if(medicinalHerbMoveResponses == MedicinalHerbMoveResponses.ERROR_DOES_NOT_OWN_MEDICINAL_HERB){
                        Toast.makeText(Use_Med_Herb_Options.this, "Error. You do not own a medicinal herb", Toast.LENGTH_LONG).show();
                    }
                    else if (medicinalHerbMoveResponses == MedicinalHerbMoveResponses.ERROR_NOT_CURRENT_HERO){
                        Toast.makeText(Use_Med_Herb_Options.this, "Error. It is not your turn", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(Use_Med_Herb_Options.this, "Medicinal Herb activated", Toast.LENGTH_LONG).show();
                        Intent myIntent = new Intent(v.getContext(), Board.class);
                        startActivity(myIntent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static class MedHerbWillPowerSender extends AsyncTask<String, Void, MedicinalHerbWillPowerResponses> {
        @Override
        protected MedicinalHerbWillPowerResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() + "/" + myPlayer.getPlayer().getUsername() + "/medicinalHerbWillPower")
                        .header("Content-Type", "application/json")
                        .asString();
                String resultAsJsonString = response.getBody();
                return new Gson().fromJson(resultAsJsonString,MedicinalHerbWillPowerResponses.class );
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class MedHerbMoveSender extends AsyncTask<String, Void, MedicinalHerbMoveResponses> {
        @Override
        protected MedicinalHerbMoveResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() + "/" + myPlayer.getPlayer().getUsername() + "/medicinalHerbMove")
                        .header("Content-Type", "application/json")
                        .asString();
                String resultAsJsonString = response.getBody();
                return new Gson().fromJson(resultAsJsonString,MedicinalHerbMoveResponses.class );
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
