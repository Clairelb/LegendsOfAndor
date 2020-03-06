package com.example.LegendsOfAndor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;

public class InteractItem extends AppCompatActivity {

    private Spinner dropSpinner;
    private Spinner pickUpSpinner;

    private Button pickUpItemBTN;
    private Button dropItemBTN;
    private Button getItemBTN;

    MyPlayer myPlayer = MyPlayer.getInstance();
    int playerGold = myPlayer.getGame().getSinglePlayer(myPlayer.getPlayer().getUsername()).getHero().getGold();
    int goldOnGround = 4; //NEED TO CHANGE FOR SERVER VALUE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interact_item);

        pickUpItemBTN = findViewById(R.id.pickUpItem);
        dropItemBTN = findViewById(R.id.dropItem);
        getItemBTN = findViewById(R.id.getItems);

        //Updates corresponding spinner for drop gold
        ArrayList<Integer> spinnerGoldDrop = new ArrayList<Integer>();
        for (int i = 0; i < playerGold; i++) {
            spinnerGoldDrop.add(i+1);
        }
        dropSpinner = findViewById(R.id.dropSpinner);
        ArrayAdapter<Integer> adapterDrop = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerGoldDrop);
        adapterDrop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropSpinner.setAdapter(adapterDrop);

        //Updates corresponding spinner for pick up gold
        ArrayList<Integer> spinnerPickUpGold = new ArrayList<Integer>();
        for (int i = 0; i < goldOnGround; i++) {
            spinnerPickUpGold.add(i+1);
        }
        pickUpSpinner = findViewById(R.id.pickUpSpinner);
        ArrayAdapter<Integer> adapterPick = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerPickUpGold);
        adapterPick.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropSpinner.setAdapter(adapterPick);

        pickUpItemBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PickUpGoldSender pickUpGoldSender = new PickUpGoldSender();
                    pickUpGoldSender.execute(pickUpSpinner.getSelectedItem().toString());
                    Toast.makeText(InteractItem.this, "Successfully picked up " + pickUpSpinner.getSelectedItem().toString() + "gold.", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dropItemBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DropGoldSender dropGoldSender = new DropGoldSender();
                    dropGoldSender.execute(dropSpinner.getSelectedItem().toString());
                    Toast.makeText(InteractItem.this, "Successfully dropped " + dropSpinner.getSelectedItem().toString() + "gold.", Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        getItemBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AsyncTask<String, Void, Integer> asyncTask;

                    GetGoldSender getGoldSender = new GetGoldSender();
                    asyncTask = getGoldSender.execute("");

                    goldOnGround = asyncTask.get();

                    // change the spinner here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private static class DropGoldSender extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();

            try {
                Unirest.post("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/dropGold")
                        .header("Content-Type", "application/json")
                        .body(strings[0])
                        .asString();

            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class GetGoldSender extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.get("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/getGold")
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, Integer.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class PickUpGoldSender extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();

            try {
                Unirest.post("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/pickUpGold")
                        .header("Content-Type", "application/json")
                        .body(strings[0])
                        .asString();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
