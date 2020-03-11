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
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;

enum PickUpFarmersResponses {
    FARMERS_DIED, NO_FARMERS, FARMERS_PICKED_UP
}

public class FarmerInteract extends AppCompatActivity {

    private Spinner pickUpFarmerSpinner;
    private Spinner dropFarmerSpinner;

    MyPlayer myPlayer = MyPlayer.getInstance();

    ArrayList<Farmer> farmersOnSpace = new ArrayList<Farmer>();
    ArrayList<Farmer> farmersCarried = new ArrayList<Farmer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farmer_interact);



        Button backb = findViewById(R.id.backb);
        backb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), Free_Actions.class);
                startActivity(myIntent);
            }
        });

        Button pickUp = findViewById(R.id.pickUpFarmer);
        pickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask<String, Void, PickUpFarmersResponses> asyncTask;

                try {
                    PickUpFarmersSender pickUpFarmersSender = new PickUpFarmersSender();
                    int amountFarmers = Integer.parseInt(pickUpFarmerSpinner.getSelectedItem().toString());
                    ArrayList<Farmer> farmersToSend = new ArrayList<Farmer>();
                    for (int i = 0; i < amountFarmers; i++) {
                        farmersToSend.add(new Farmer(false));
                    }
                    asyncTask = pickUpFarmersSender.execute(new Gson().toJson(farmersToSend));


                    if (asyncTask.get() == PickUpFarmersResponses.FARMERS_DIED) {
                        Toast.makeText(FarmerInteract.this, "The " + pickUpFarmerSpinner.getSelectedItem().toString() + " you just picked up died.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(FarmerInteract.this, "Picked up " + pickUpFarmerSpinner.getSelectedItem().toString() + " farmer.", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button drop = findViewById(R.id.dropFarmer);
        dropFarmerSpinner = findViewById(R.id.dropFarmerSpinner);
        drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DropFarmersSender dropFarmersSender = new DropFarmersSender();
                    int amountFarmers = Integer.parseInt(dropFarmerSpinner.getSelectedItem().toString());
                    ArrayList<Farmer> farmersToSend = new ArrayList<Farmer>();
                    for (int i = 0; i < amountFarmers; i++) {
                        farmersToSend.add(new Farmer(false));
                    }
                    dropFarmersSender.execute(new Gson().toJson(farmersToSend));
                    Toast.makeText(FarmerInteract.this, "Dropped " + dropFarmerSpinner.getSelectedItem().toString() + " farmer.", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button getFarmers = findViewById(R.id.getFarmers);
        getFarmers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AsyncTask<String, Void, ArrayList<Farmer>> asyncTask;

                    FarmerInteract.GetFarmersSender getFarmersSender = new FarmerInteract.GetFarmersSender();
                    asyncTask = getFarmersSender.execute("");

                    farmersOnSpace = asyncTask.get();
                    Toast.makeText(FarmerInteract.this, "There are " + farmersOnSpace.size() + " farmers on the region.", Toast.LENGTH_LONG).show();

                    ArrayList<Integer> spinnerNumFarmers = new ArrayList<Integer>();
                    for (int i = 0; i <= farmersOnSpace.size(); i++) {
                        spinnerNumFarmers.add(i);
                    }
                    pickUpFarmerSpinner = findViewById(R.id.pickUpFarmerSpinner);
                    ArrayAdapter<Integer> adapterPick = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerNumFarmers);
                    adapterPick.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    pickUpFarmerSpinner.setAdapter(adapterPick);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button getMyFarmers = findViewById(R.id.getMyFarmer);
        getMyFarmers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AsyncTask<String, Void, ArrayList<Farmer>> asyncTask;

                    FarmerInteract.GetMyFarmersSender getMyFarmersSender = new FarmerInteract.GetMyFarmersSender();
                    asyncTask = getMyFarmersSender.execute("");

                    farmersCarried = asyncTask.get();
                    Toast.makeText(FarmerInteract.this, "You are carrying " + farmersCarried.size() + " farmers.", Toast.LENGTH_LONG).show();


                    ArrayList<Integer> spinnerNumFarmers = new ArrayList<Integer>();
                    for (int i = 0; i <= farmersCarried.size(); i++) {
                        spinnerNumFarmers.add(i);
                    }
                    dropFarmerSpinner = findViewById(R.id.dropFarmerSpinner);
                    ArrayAdapter<Integer> adapterPick = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerNumFarmers);
                    adapterPick.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dropFarmerSpinner.setAdapter(adapterPick);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private static class DropFarmersSender extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();

            try {
                Unirest.post("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/dropFarmers")
                        .header("Content-Type", "application/json")
                        .body(strings[0])
                        .asString();

            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class PickUpFarmersSender extends AsyncTask<String, Void, PickUpFarmersResponses> {
        @Override
        protected PickUpFarmersResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/pickUpFarmers")
                        .header("Content-Type", "application/json")
                        .body(strings[0])
                        .asString();

                String resultAsJsonString = response.getBody();
                return new Gson().fromJson(resultAsJsonString, PickUpFarmersResponses.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class GetMyFarmersSender extends AsyncTask<String, Void, ArrayList<Farmer>> {
        @Override
        protected ArrayList<Farmer> doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.get("http://"+ myPlayer.getServerIP()+":8080/"+ myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/getMyFarmers")
                        .asString();

                String resultAsJsonString = response.getBody();
                return new Gson().fromJson(resultAsJsonString, new TypeToken<ArrayList<Farmer>>() {}.getType());
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class GetFarmersSender extends AsyncTask<String, Void, ArrayList<Farmer>> {
        @Override
        protected ArrayList<Farmer> doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.get("http://"+ myPlayer.getServerIP()+":8080/"+ myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/getFarmers")
                        .asString();

                String resultAsJsonString = response.getBody();
                return new Gson().fromJson(resultAsJsonString, new TypeToken<ArrayList<Farmer>>() {}.getType());
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
