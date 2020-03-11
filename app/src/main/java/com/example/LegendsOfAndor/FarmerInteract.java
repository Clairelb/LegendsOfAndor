package com.example.LegendsOfAndor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;

public class FarmerInteract extends AppCompatActivity {

    MyPlayer myPlayer = MyPlayer.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farmer_interact);

        final int currentSpace = myPlayer.getGame().getCurrentHero().getCurrentSpace();
        final Region currentRegion = myPlayer.getGame().getRegionDatabase().getRegion(currentSpace);
        final ArrayList<Farmer> currentFarmers = currentRegion.getFarmers();

//        ArrayList<Farmer> currentFarmers = myPlayer.getGame().getCurrentHero().getFar

        Button backb = findViewById(R.id.backb);
        backb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), Free_Actions.class);
                startActivity(myIntent);
            }
        });


        //Need to remove the farmer from the current space, update boolean status of farmer
        Button pickUp = findViewById(R.id.pickUpFarmer);
        pickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    PickUpFarmer pickUpGoldSender = new PickUpFarmer();
                    Toast.makeText(FarmerInteract.this, "Successfully picked up farmer.", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //if Boolean isBeingCarried is false and farmer exists on current,. Else output toast message
//                if (currentFarmers.get(0) != null) {//exists a farmer on space
//                    //player is not already carrying farmer
//                    if (myPlayer.getGame().getSinglePlayer(myPlayer.getPlayer().getUsername()).getHero().getFarmers().isEmpty()) {
//                        ArrayList<Farmer> carriedFarmer = new ArrayList<Farmer>();
//                        carriedFarmer.add(currentFarmers.get(0));
//                        //****HOW TO CHECK FARMER ISBEINGCARRIED BOOLEAN****
//                        currentFarmers.remove(0);//remove farmer from current space
//                        //set current player with arraylist containing removed farmer from current space
//                        myPlayer.getGame().getSinglePlayer(myPlayer.getPlayer().getUsername()).getHero().setFarmers(carriedFarmer);
//                        Toast.makeText(FarmerInteract.this, "Picked up farmer", Toast.LENGTH_LONG).show();
//                    }
//                } else {
//                    Toast.makeText(FarmerInteract.this, "There are no farmers to pick up.", Toast.LENGTH_LONG).show();
//                }
            }
        });

        //Need to add farmer to the current space, update boolean status of farmer
        Button drop = findViewById(R.id.dropFarmer);
        drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DropFarmerSender dropGoldSender = new DropFarmerSender();
//                    dropGoldSender.execute(dropSpinner.getSelectedItem().toString());
                    Toast.makeText(FarmerInteract.this, "Successfully dropped farmer.", Toast.LENGTH_LONG).show();
//                    int amountDropped = Integer.parseInt(dropSpinner.getSelectedItem().toString());
//                    myPlayer.getGame().getSinglePlayer(myPlayer.getPlayer().getUsername()).getHero().setGold(playerGold - amountDropped);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //if Boolean isBeingCarried is true, drop farmer on current space
//                if (myPlayer.getGame().getSinglePlayer(myPlayer.getPlayer().getUsername()).getHero().getFarmers().size() == 1) {
//                    //Get the farmer being carried by the current hero, and place it on the current region farmer arraylist
//                    currentRegion.getFarmers().add(myPlayer.getGame().getSinglePlayer(myPlayer.getPlayer().getUsername()).getHero().getFarmers().get(0));
//                    //Removes the farmer being carried by the current player from their farmer arraylist
//                    myPlayer.getGame().getSinglePlayer(myPlayer.getPlayer().getUsername()).getHero().getFarmers().remove(0);
//                } else {
//                    Toast.makeText(FarmerInteract.this, "No farmer being carried.", Toast.LENGTH_LONG).show();
//                }
            }
        });
    }

    private static class DropFarmerSender extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();

            try {
                Unirest.post("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/dropFarmer")
                        .header("Content-Type", "application/json")
                        .body(strings[0])
                        .asString();

            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class PickUpFarmer extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();

            try {
                Unirest.post("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/pickUpFarmer")
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
