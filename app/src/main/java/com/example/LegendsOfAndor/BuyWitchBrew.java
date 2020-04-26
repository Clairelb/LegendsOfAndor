package com.example.LegendsOfAndor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;

enum BuyWitchBrewResponses {
    ERROR_NOT_ENOUGH_GOLD, SUCCESS, MAX_ITEMS
}

public class BuyWitchBrew extends AppCompatActivity {
    private TextView priceTV;
    private Spinner quantitySpinner;
    private Button purchaseBTN;
    private Button toBoardBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_witch_brew);
        MyPlayer myPlayer = MyPlayer.getInstance();

        try{
            AsyncTask<String, Void, Game> asyncTask;
            Game gameToSet;
            GetGame getGame = new GetGame();
            asyncTask = getGame.execute();
            gameToSet = asyncTask.get();
            myPlayer.setGame(gameToSet);
        }catch (Exception e){
            e.printStackTrace();
        }

        Game game = myPlayer.getGame();
        int price;

        priceTV = findViewById(R.id.buy_witch_brew_price);
        quantitySpinner = findViewById(R.id.buy_witch_brew_spinner);
        purchaseBTN = findViewById(R.id.buy_witch_brew_purchase_button);
        toBoardBTN = findViewById(R.id.to_board_legend_card_witch);

        if (game.getSinglePlayer(myPlayer.getPlayer().getUsername()).getHero().getHeroClass() == HeroClass.ARCHER) {
            price = game.getWitch().getCostOfWitchBrewArcher();
        } else {
            price = game.getWitch().getCostOfWitchBrew();
        }
        priceTV.setText("Price: " + price);


        ArrayList<Integer> possibilities = new ArrayList<>();
        for (int i = 1; i <= game.getSinglePlayer(myPlayer.getPlayer().getUsername()).getHero().getGold() / price; i++) {
            possibilities.add(i);
        }

        ArrayAdapter<Integer> adapterDrop = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, possibilities);
        adapterDrop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantitySpinner.setAdapter(adapterDrop);

        purchaseBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AsyncTask<String, Void, BuyWitchBrewResponses> asyncTask;
                    BuyWitchBrewSender buyWitchBrewSender = new BuyWitchBrewSender();
                    asyncTask = buyWitchBrewSender.execute(new Gson().toJson(quantitySpinner.getSelectedItem()));

                    if (asyncTask.get() == BuyWitchBrewResponses.ERROR_NOT_ENOUGH_GOLD) {
                        Toast.makeText(BuyWitchBrew.this, "Error. You do not have enough gold.", Toast.LENGTH_LONG).show();
                    } else if (asyncTask.get() == BuyWitchBrewResponses.MAX_ITEMS) {
                        Toast.makeText(BuyWitchBrew.this, "Error. Your small item inventory is full.", Toast.LENGTH_LONG).show();
                    } else { // success
                        Toast.makeText(BuyWitchBrew.this, "Purchase complete.", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        toBoardBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuyWitchBrew.this, Board.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    private static class BuyWitchBrewSender extends AsyncTask<String, Void, BuyWitchBrewResponses> {
        @Override
        protected BuyWitchBrewResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() + "/" + myPlayer.getPlayer().getUsername() + "/buyWitchBrew")
                        .header("Content-Type", "application/json")
                        .body(strings[0])
                        .asString();
                String resultAsJsonString = response.getBody();
                return new Gson().fromJson(resultAsJsonString, BuyWitchBrewResponses.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class GetGame extends AsyncTask<String, Void, Game > {
        @Override
        protected Game doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.get("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getPlayer().getUsername() + "/getGameByUsername")
                        .asString();

                String resultAsJsonString = response.getBody();
                System.out.println("RESPONSE BODY " + response.getBody());
                return new Gson().fromJson(resultAsJsonString, Game.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
