package com.example.LegendsOfAndor;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;

enum BuyFromMerchantResponses {
    SUCCESS, NOT_ENOUGH_GOLD, MERCHANT_DNE
}

public class BuyFromMerchant extends AppCompatActivity {

    @Override
    public void onBackPressed(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyfrommerchant);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        try{
            AsyncTask<String, Void, Game> asyncTask1;
            Game gameToSet;
            GetGame getGame = new GetGame();
            asyncTask1 = getGame.execute();
            gameToSet = asyncTask1.get();
            System.out.println(gameToSet);
            MyPlayer.getInstance().setGame(gameToSet);
        }catch (Exception e){
            e.printStackTrace();
        }

        final Game game = MyPlayer.getInstance().getGame();
        final Player player = game.getSinglePlayer(MyPlayer.getInstance().getPlayer().getUsername());
        final int gold = player.getHero().getGold();

        final TextView gold_av = (TextView) findViewById(R.id.Gold_Av);
        gold_av.setText(String.valueOf(gold));

        final ArrayList<Item> items = new ArrayList<>();
        final MerchantPurchase merchantPurchase = new MerchantPurchase(0, items);

        ImageButton SP = (ImageButton) findViewById(R.id.SP);
        SP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int strength = merchantPurchase.getStrength();
                merchantPurchase.setStrength(strength + 1);
            }
        });

        ImageButton helm = (ImageButton) findViewById(R.id.helm);
        helm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.add(new Item(ItemType.HELM));
                merchantPurchase.setItems(items);
            }
        });

        ImageButton falcon = (ImageButton) findViewById(R.id.falcon);
        falcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.add(new Item(ItemType.FALCON));
                merchantPurchase.setItems(items);
            }
        });

        ImageButton bow = (ImageButton) findViewById(R.id.bow);
        bow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.add(new Item(ItemType.BOW));
                merchantPurchase.setItems(items);
            }
        });

        ImageButton shield = (ImageButton) findViewById(R.id.shield);
        shield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.add(new Item(ItemType.SHIELD));
                merchantPurchase.setItems(items);
            }
        });

        ImageButton wineskin = (ImageButton) findViewById(R.id.wineskin);
        wineskin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.add(new Item(ItemType.WINESKIN));
                merchantPurchase.setItems(items);
            }
        });

        ImageButton telescope = (ImageButton) findViewById(R.id.telescope);
        telescope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.add(new Item(ItemType.TELESCOPE));
                merchantPurchase.setItems(items);
            }
        });

        ImageButton backb = (ImageButton) findViewById(R.id.backb);
        backb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), Free_Actions.class);
                startActivity(myIntent);
            }
        });

        Button confirmb = (Button) findViewById(R.id.confirmb);
        confirmb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask<String, Void, BuyFromMerchantResponses> asyncTask;
                BuyFromMerchantResponses buyFromMerchantResponses;
                try {
                    SendPurchase sendPurchase = new SendPurchase();
                    asyncTask = sendPurchase.execute(new Gson().toJson(merchantPurchase));
                    buyFromMerchantResponses = asyncTask.get();
                    if (buyFromMerchantResponses == BuyFromMerchantResponses.SUCCESS) {
                        Toast.makeText(BuyFromMerchant.this, "Purchase Successful", Toast.LENGTH_LONG).show();
                        //gold_av.setText(String.valueOf(player.getHero().getGold()));
                    }
                    else if (buyFromMerchantResponses == BuyFromMerchantResponses.NOT_ENOUGH_GOLD){
                        Toast.makeText(BuyFromMerchant.this, "Purchase Failed. Not Enough Gold", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(BuyFromMerchant.this, "No merchant located in your region", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private static class SendPurchase extends AsyncTask<String, Void, BuyFromMerchantResponses> {
        @Override
        protected BuyFromMerchantResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/buyFromMerchant")
                        .header("Content-Type", "application/json")
                        .body(strings[0])
                        .asString();
                String resultAsJsonString = response.getBody();
                return new Gson().fromJson(resultAsJsonString,BuyFromMerchantResponses.class );
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
