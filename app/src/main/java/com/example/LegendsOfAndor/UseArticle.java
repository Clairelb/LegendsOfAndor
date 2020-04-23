package com.example.LegendsOfAndor;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.LegendsOfAndor.PublicEnums.AddDropItemResponses;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
enum AcceptFalconTradeResponses{
    FALCON_TRADE_ACCEPTED, FALCON_TRADE_ACCEPT_FAILURE
}
public class UseArticle extends AppCompatActivity {
    MyPlayer myPlayer;
    Button startFalconTrade;
    Button joinFalconTrade;
    Button backbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.use_article);

        myPlayer = MyPlayer.getInstance();
        startFalconTrade = findViewById(R.id.start_falcon_trade);
        joinFalconTrade = findViewById(R.id.join_falcon_trade);
        backbutton = findViewById(R.id.back_to_free_actions);
        joinFalconTrade.setVisibility(View.INVISIBLE);
        startFalconTrade.setVisibility(View.INVISIBLE);

        try{
            AsyncTask<String, Void, Game> asyncTaskGame;
            Game gameToSet;
            GetGame getGame = new GetGame();
            asyncTaskGame = getGame.execute();
            gameToSet = asyncTaskGame.get();
            System.out.println(gameToSet);
            myPlayer.setGame(gameToSet);
            for(int i = 0; i < gameToSet.getCurrentNumPlayers(); i++){
                if(gameToSet.getPlayers()[i].getUsername().equals(myPlayer.getPlayer().getUsername())){
                    myPlayer.setPlayer(gameToSet.getPlayers()[i]);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //IF MY PLAYER HAS NOT ENDED THEIR DAY
        if(!myPlayer.getPlayer().getHero().isHasEndedDay()){
            for(Item item : myPlayer.getPlayer().getHero().getItems()){
                if(item.getItemType() == ItemType.FALCON){
                    if(item.getNumUses() >= 1){
                        startFalconTrade.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        if(myPlayer.getPlayer().getHero().getFalconTradeStatus() == FalconTradeStatus.TRADE_PENDING){
            joinFalconTrade.setVisibility(View.VISIBLE);
        }

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UseArticle.this, Free_Actions.class));
                finish();
            }
        });

        startFalconTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UseArticle.this, FalconTradeSelectHero.class));
                finish();
            }
        });

        joinFalconTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    HeroClass tradingWith = myPlayer.getPlayer().getHero().getFalconTradingWith();
                    AsyncTask<String, Void, AcceptFalconTradeResponses> asyncTask;
                    JoinFalconTradeSender joinFalconTradeSender = new JoinFalconTradeSender();
                    asyncTask = joinFalconTradeSender.execute(new Gson().toJson(tradingWith));
                    AcceptFalconTradeResponses acceptFalconTradeResponses = asyncTask.get();
                    if(acceptFalconTradeResponses == AcceptFalconTradeResponses.FALCON_TRADE_ACCEPT_FAILURE){
                        Toast.makeText(UseArticle.this, "Falcon Trade Accept Failure. Please try again", Toast.LENGTH_LONG).show();
                    }else if(acceptFalconTradeResponses == AcceptFalconTradeResponses.FALCON_TRADE_ACCEPTED){
                        Toast.makeText(UseArticle.this, "Joining Falcon Trade", Toast.LENGTH_LONG).show();
                        Intent myIntent = new Intent(UseArticle.this, FalconTrade.class);
                        Bundle extras = new Bundle();
                        if(myPlayer.getPlayer().getHero().getFalconTradingWith() == HeroClass.DWARF){
                            extras.putString("p1","dwarf");
                        }else if(myPlayer.getPlayer().getHero().getFalconTradingWith() == HeroClass.ARCHER){
                            extras.putString("p1","archer");
                        } else if(myPlayer.getPlayer().getHero().getFalconTradingWith() == HeroClass.WARRIOR){
                            extras.putString("p1","warrior");
                        } else if(myPlayer.getPlayer().getHero().getFalconTradingWith() == HeroClass.WIZARD){
                            extras.putString("p1","wizard");
                        }
                        if(myPlayer.getPlayer().getHero().getHeroClass() == HeroClass.WIZARD){
                            extras.putString("p2","wizard");
                        }else if(myPlayer.getPlayer().getHero().getHeroClass() == HeroClass.DWARF){
                            extras.putString("p2","dwarf");
                        }else if(myPlayer.getPlayer().getHero().getHeroClass() == HeroClass.WARRIOR){
                            extras.putString("p2","warrior");
                        }else if(myPlayer.getPlayer().getHero().getHeroClass() == HeroClass.ARCHER){
                            extras.putString("p2","archer");
                        }
                        myIntent.putExtras(extras);
                        startActivity(myIntent);
                        finish();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

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

    private static class JoinFalconTradeSender extends AsyncTask<String, Void, AcceptFalconTradeResponses> {
        @Override
        protected AcceptFalconTradeResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() + "/" + myPlayer.getPlayer().getUsername() + "/joinFalconTrade")
                        .header("Content-Type", "application/json")
                        .body(strings[0])
                        .asString();

                String resultAsJsonString = response.getBody();
                return new Gson().fromJson(resultAsJsonString, AcceptFalconTradeResponses.class );
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
