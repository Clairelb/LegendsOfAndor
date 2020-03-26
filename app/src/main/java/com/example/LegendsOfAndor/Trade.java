package com.example.LegendsOfAndor;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.LegendsOfAndor.PublicEnums.AddDropItemResponses;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;

public class Trade extends AppCompatActivity {

    Button get_items;
    Button add_item;
    Button drop_item;
    TextView selected_item;
    ImageButton add_wineskin;
    ImageButton add_helm;
    ImageButton add_telescope;
    ImageButton add_runestone;
    ImageButton add_witch_brew;
    ImageButton add_medicinal_herb;
    ImageButton add_falcon;
    ImageButton add_shield;
    ImageButton add_bow;
    ImageButton drop_wineskin;
    ImageButton drop_helm;
    ImageButton drop_telescope;
    ImageButton drop_runestone;
    ImageButton drop_witch_brew;
    ImageButton drop_medicinal_herb;
    ImageButton drop_falcon;
    ImageButton drop_shield;
    ImageButton drop_bow;
    Boolean isAdding;
    String selectedItem;
    ImageButton tradeb;

    @Override
        public void onBackPressed(){}

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.trade);

            get_items = findViewById(R.id.get_items);
            selected_item = findViewById(R.id.selected_item);
            add_item = findViewById(R.id.add_item);
            drop_item = findViewById(R.id.drop_item);
            isAdding = false;
            selectedItem = null;
            tradeb = findViewById(R.id.trade_backb);

            add_wineskin = findViewById(R.id.add_wineskin);
            add_helm = findViewById(R.id.add_helm);
            add_telescope = findViewById(R.id.add_telescope);
            add_runestone = findViewById(R.id.add_runestone);
            add_witch_brew = findViewById(R.id.add_witch_brew);
            add_medicinal_herb = findViewById(R.id.add_medicinal_herb);
            add_falcon = findViewById(R.id.add_falcon);
            add_shield = findViewById(R.id.add_shield);
            add_bow = findViewById(R.id.add_bow);

            add_wineskin.setVisibility(View.INVISIBLE);
            add_bow.setVisibility(View.INVISIBLE);
            add_falcon.setVisibility(View.INVISIBLE);
            add_helm.setVisibility(View.INVISIBLE);
            add_medicinal_herb.setVisibility(View.INVISIBLE);
            add_runestone.setVisibility(View.INVISIBLE);
            add_shield.setVisibility(View.INVISIBLE);
            add_telescope.setVisibility(View.INVISIBLE);
            add_witch_brew.setVisibility(View.INVISIBLE);
            add_item.setVisibility(View.INVISIBLE);

            drop_wineskin = findViewById(R.id.drop_wineskin);
            drop_helm = findViewById(R.id.drop_helm);
            drop_telescope = findViewById(R.id.drop_telescope);
            drop_runestone = findViewById(R.id.drop_runestone);
            drop_witch_brew = findViewById(R.id.drop_witch_brew);
            drop_medicinal_herb = findViewById(R.id.drop_medicinal_herb);
            drop_falcon = findViewById(R.id.drop_falcon);
            drop_shield = findViewById(R.id.drop_shield);
            drop_bow = findViewById(R.id.drop_bow);

            drop_wineskin.setVisibility(View.INVISIBLE);
            drop_helm.setVisibility(View.INVISIBLE);
            drop_telescope.setVisibility(View.INVISIBLE);
            drop_runestone.setVisibility(View.INVISIBLE);
            drop_witch_brew.setVisibility(View.INVISIBLE);
            drop_medicinal_herb.setVisibility(View.INVISIBLE);
            drop_falcon.setVisibility(View.INVISIBLE);
            drop_shield.setVisibility(View.INVISIBLE);
            drop_bow.setVisibility(View.INVISIBLE);

            try{
                AsyncTask<String, Void, Game> asyncTaskGame;
                Game gameToSet;
                GetGame getGame = new GetGame();
                asyncTaskGame = getGame.execute();
                gameToSet = asyncTaskGame.get();
                System.out.println(gameToSet);
                MyPlayer.getInstance().setGame(gameToSet);
                for(int i = 0; i < gameToSet.getCurrentNumPlayers(); i++){
                    if(gameToSet.getPlayers()[i].getUsername().equals(MyPlayer.getInstance().getPlayer().getUsername())){
                        MyPlayer.getInstance().setPlayer(gameToSet.getPlayers()[i]);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            for(Item item : MyPlayer.getInstance().getPlayer().getHero().getItems()){
                if(item.getItemType() == ItemType.WINESKIN){
                    drop_wineskin.setVisibility(View.VISIBLE);
                }
                if(item.getItemType() == ItemType.HELM){
                    drop_helm.setVisibility(View.VISIBLE);
                }
                if(item.getItemType() == ItemType.TELESCOPE){
                    drop_telescope.setVisibility(View.VISIBLE);
                }
                if(item.getItemType() == ItemType.WITCH_BREW){
                    drop_witch_brew.setVisibility(View.VISIBLE);
                }
                if(item.getItemType() == ItemType.MEDICINAL_HERB){
                    drop_medicinal_herb.setVisibility(View.VISIBLE);
                }
                if(item.getItemType() == ItemType.FALCON){
                    drop_falcon.setVisibility(View.VISIBLE);
                }
                if(item.getItemType() == ItemType.SHIELD){
                    drop_shield.setVisibility(View.VISIBLE);
                }
                if(item.getItemType() == ItemType.BOW){
                    drop_bow.setVisibility(View.VISIBLE);
                }
            }

            for(RuneStone runeStone : MyPlayer.getInstance().getPlayer().getHero().getRuneStones()){
                drop_runestone.setVisibility(View.VISIBLE);
            }

            tradeb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(Trade.this, Free_Actions.class);
                    startActivity(myIntent);
                    finish();
                }
            });

            get_items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    get_items.setVisibility(View.INVISIBLE);
                    add_item.setVisibility(View.VISIBLE);

                    AsyncTask<String, Void, ArrayList<Item>> asyncTask;
                    ArrayList<Item> items = new ArrayList<>();
                    try {
                        GetItemsSender getItemsSender = new GetItemsSender();
                        asyncTask = getItemsSender.execute();
                        items = asyncTask.get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    for(Item item : items){
                        if(item.getItemType() == ItemType.WINESKIN){
                            add_wineskin.setVisibility(View.VISIBLE);
                        }
                        if(item.getItemType() == ItemType.FALCON){
                            add_falcon.setVisibility(View.VISIBLE);
                        }
                        if(item.getItemType() == ItemType.HELM){
                            add_helm.setVisibility(View.VISIBLE);
                        }
                        if(item.getItemType() == ItemType.MEDICINAL_HERB){
                            add_medicinal_herb.setVisibility(View.VISIBLE);
                        }
                        if(item.getItemType() == ItemType.SHIELD){
                            add_shield.setVisibility(View.VISIBLE);
                        }
                        if(item.getItemType() == ItemType.TELESCOPE){
                            add_telescope.setVisibility(View.VISIBLE);
                        }
                        if(item.getItemType() == ItemType.WITCH_BREW){
                            add_witch_brew.setVisibility(View.VISIBLE);
                        }
                    }
                    AsyncTask<String, Void, ArrayList<RuneStone>> asyncTaskRunestone;
                    ArrayList<RuneStone> runeStones = new ArrayList<>();
                    try {
                        GetRunestonesSender getRunestonesSender = new GetRunestonesSender();
                        asyncTaskRunestone = getRunestonesSender.execute();
                        runeStones = asyncTaskRunestone.get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if(runeStones.size() > 0){
                        add_runestone.setVisibility(View.VISIBLE);
                    }

                }
            });

            add_wineskin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected_item.setText("Adding: Wineskin");
                    selectedItem = "wineskin";
                    isAdding = true;
                }
            });

            add_bow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected_item.setText("Adding: Bow");
                    selectedItem = "bow";
                    isAdding = true;
                }
            });

            add_falcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected_item.setText("Adding: Falcon");
                    selectedItem = "falcon";
                    isAdding = true;
                }
            });

            add_helm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected_item.setText("Adding: helm");
                    selectedItem = "helm";
                    isAdding = true;
                }
            });

            add_telescope.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected_item.setText("Adding: Telescope");
                    selectedItem = "telescope";
                    isAdding = true;
                }
            });

            add_medicinal_herb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected_item.setText("Adding: Medicinal Herb");
                    selectedItem = "herb";
                    isAdding = true;
                }
            });

            add_runestone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected_item.setText("Adding: Runestone");
                    selectedItem = "runestone";
                    isAdding = true;
                }
            });

            add_shield.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected_item.setText("Adding: Shield");
                    selectedItem = "shield";
                    isAdding = true;
                }
            });

            add_witch_brew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected_item.setText("Adding: Witch Brew");
                    selectedItem = "brew";
                    isAdding = true;
                }
            });

            drop_wineskin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected_item.setText("Dropping: Wineskin");
                    selectedItem = "wineskin";
                    isAdding = false;
                }
            });

            drop_bow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected_item.setText("Dropping: Bow");
                    selectedItem = "bow";
                    isAdding = false;
                }
            });

            drop_falcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected_item.setText("Dropping: Falcon");
                    selectedItem = "falcon";
                    isAdding = false;
                }
            });

            drop_helm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected_item.setText("Dropping: helm");
                    selectedItem = "helm";
                    isAdding = false;
                }
            });

            drop_telescope.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected_item.setText("Dropping: Telescope");
                    selectedItem = "telescope";
                    isAdding = false;
                }
            });

            drop_medicinal_herb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected_item.setText("Dropping: Medicinal Herb");
                    selectedItem = "herb";
                    isAdding = false;
                }
            });

            drop_runestone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected_item.setText("Dropping: Runestone");
                    selectedItem = "runestone";
                    isAdding = false;
                }
            });

            drop_shield.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected_item.setText("Dropping: Shield");
                    selectedItem = "shield";
                    isAdding = false;
                }
            });

            drop_witch_brew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected_item.setText("Dropping: Witch Brew");
                    selectedItem = "brew";
                    isAdding = false;
                }
            });

            add_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isAdding && selectedItem!= null){
                        AsyncTask<String, Void, AddDropItemResponses> asyncTask = null;
                        AddDropItemResponses addDropItemResponses = null;


                        if(selectedItem.equals("runestone")){
                            try{
                                AddRunestoneSender addRunestoneSender = new AddRunestoneSender();
                                asyncTask = addRunestoneSender.execute();
                                addDropItemResponses = asyncTask.get();
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }else{
                            try {
                                AddItemSender addItemSender = new AddItemSender();
                                ItemType selectedItemType = null;
                                if(selectedItem.equals("wineskin")){
                                    selectedItemType = ItemType.WINESKIN;
                                }
                                if(selectedItem.equals("helm")){
                                    selectedItemType = ItemType.HELM;
                                }
                                if(selectedItem.equals("falcon")){
                                    selectedItemType = ItemType.FALCON;
                                }
                                if(selectedItem.equals("bow")){
                                    selectedItemType = ItemType.BOW;
                                }
                                if(selectedItem.equals("brew")){
                                    selectedItemType = ItemType.WITCH_BREW;
                                }
                                if(selectedItem.equals("shield")){
                                    selectedItemType = ItemType.SHIELD;
                                }
                                if(selectedItem.equals("telescope")){
                                    selectedItemType = ItemType.TELESCOPE;
                                }
                                if(selectedItem.equals("herb")){
                                    selectedItemType = ItemType.MEDICINAL_HERB;
                                }
                                asyncTask = addItemSender.execute(new Gson().toJson(selectedItemType));
                                addDropItemResponses = asyncTask.get();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if(addDropItemResponses == AddDropItemResponses.ITEM_ADDED){
                            Toast.makeText(Trade.this, "Added item successfully", Toast.LENGTH_LONG).show();
                            Intent myIntent = new Intent(Trade.this, Free_Actions.class);
                            startActivity(myIntent);
                            finish();
                        }else if (addDropItemResponses == AddDropItemResponses.ADD_DROP_FAILURE){
                            Toast.makeText(Trade.this, "Could not successfully add item", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(Trade.this, "Please select an item to add", Toast.LENGTH_LONG).show();
                    }
                }
            });

            drop_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isAdding && selectedItem!= null){
                        AsyncTask<String, Void, AddDropItemResponses> asyncTask = null;
                        AddDropItemResponses addDropItemResponses = null;
                        if(selectedItem.equals("runestone")){
                            try{
                                DropRunestoneSender dropRunestoneSender = new DropRunestoneSender();
                                asyncTask = dropRunestoneSender.execute();
                                addDropItemResponses = asyncTask.get();
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }else{
                            try {
                                DropItemSender dropItemSender = new DropItemSender();
                                ItemType selectedItemType = null;
                                if(selectedItem.equals("wineskin")){
                                    selectedItemType = ItemType.WINESKIN;
                                }
                                if(selectedItem.equals("helm")){
                                    selectedItemType = ItemType.HELM;
                                }
                                if(selectedItem.equals("falcon")){
                                    selectedItemType = ItemType.FALCON;
                                }
                                if(selectedItem.equals("bow")){
                                    selectedItemType = ItemType.BOW;
                                }
                                if(selectedItem.equals("brew")){
                                    selectedItemType = ItemType.WITCH_BREW;
                                }
                                if(selectedItem.equals("shield")){
                                    selectedItemType = ItemType.SHIELD;
                                }
                                if(selectedItem.equals("telescope")){
                                    selectedItemType = ItemType.TELESCOPE;
                                }
                                if(selectedItem.equals("herb")){
                                    selectedItemType = ItemType.MEDICINAL_HERB;
                                }
                                asyncTask = dropItemSender.execute(new Gson().toJson(selectedItemType));
                                addDropItemResponses = asyncTask.get();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if(addDropItemResponses == AddDropItemResponses.ITEM_DROPPED){
                            Toast.makeText(Trade.this, "Item dropped successfully", Toast.LENGTH_LONG).show();
                            Intent myIntent = new Intent(Trade.this, Free_Actions.class);
                            startActivity(myIntent);
                            finish();
                        }else if (addDropItemResponses == AddDropItemResponses.ADD_DROP_FAILURE){
                            Toast.makeText(Trade.this, "Error dropping item", Toast.LENGTH_LONG).show();
                        }

                    }else{
                        Toast.makeText(Trade.this, "Please select an item to drop", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    private static class GetItemsSender extends AsyncTask<String, Void, ArrayList<Item>> {
        @Override
        protected ArrayList<Item> doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.get("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/getItems")
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, new TypeToken<ArrayList<Item>>() {}.getType());
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class GetRunestonesSender extends AsyncTask<String, Void, ArrayList<RuneStone>> {
        @Override
        protected ArrayList<RuneStone> doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.get("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/getRunestones")
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, new TypeToken<ArrayList<RuneStone>>() {}.getType());
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

    private static class AddItemSender extends AsyncTask<String, Void, AddDropItemResponses> {
        @Override
        protected AddDropItemResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/addItem")
                        .header("Content-Type", "application/json")
                        .body(strings[0])
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, AddDropItemResponses.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class AddRunestoneSender extends AsyncTask<String, Void, AddDropItemResponses> {
        @Override
        protected AddDropItemResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.get("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/addRunestone")
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, AddDropItemResponses.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class DropRunestoneSender extends AsyncTask<String, Void, AddDropItemResponses> {
        @Override
        protected AddDropItemResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.get("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/dropRunestone")
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, AddDropItemResponses.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class DropItemSender extends AsyncTask<String, Void, AddDropItemResponses> {
        @Override
        protected AddDropItemResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/dropItem")
                        .header("Content-Type", "application/json")
                        .body(strings[0])
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, AddDropItemResponses.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
