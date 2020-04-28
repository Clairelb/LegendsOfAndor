package com.example.LegendsOfAndor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

enum SelectHeroResponses {
    SELECT_HERO_SUCCESS, ERROR_HERO_ALREADY_SELECTED, ERROR_DUPLICATE_HERO
}

enum IsReadyResponses {
    IS_READY_SUCCESS, ERROR_NO_SELECTED_HERO
}

enum StartGameResponses {
    START_GAME_SUCCESS, ERROR_PLAYER_NOT_READY, ERROR_NOT_HOST, ERROR_NOT_ENOUGH_PLAYERS
}

enum LeavePregameResponses {
    ERROR_GAME_LOADED, LEAVE_SUCCESS
}

enum LoadGameResponses {
    ERROR_NOT_ALL_PLAYERS_SELECTED_HEROES, ERROR_PLAYER_NUM_MISMATCH, ERROR_HERO_MISMATCH, ERROR_DIFFICULTY_MISMATCH, NOT_HOST, NOT_ALL_PLAYERS_READY, NOT_ENOUGH_PLAYERS, LOAD_GAME_SUCCESS
}

public class WaitScreen extends AppCompatActivity {
    private TextView player1NameTV;
    private TextView player2NameTV;
    private TextView player3NameTV;
    private TextView player4NameTV;
    private TextView hero1TV;
    private TextView hero2TV;
    private TextView hero3TV;
    private TextView hero4TV;
    private TextView ready1TV;
    private TextView ready2TV;
    private TextView ready3TV;
    private TextView ready4TV;

    private Button leaveLobbyBTN;
    private Button startGameBTN;
    private Button selectHeroBTN;
    private Button isReadyBTN;

    private Spinner heroSP;

    private Button getSavedGamesBTN;
    private TextView gamenames;
    private TextView game_name;
    private ListView saved_games;
    private Button loadGameBTN;
    ArrayList<Game> savedGames = new ArrayList<>();
    ArrayList<String> gameNames = new ArrayList<>();
    ArrayAdapter<String> adapter;

    private Thread t;
    private boolean threadTerminated = false;

    MyPlayer myPlayer = MyPlayer.getInstance();
    Game updatedGame = null;

    @Override
    public void onBackPressed() {
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wait_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        player1NameTV = findViewById(R.id.player1);
        player2NameTV = findViewById(R.id.player2);
        player3NameTV = findViewById(R.id.player3);
        player4NameTV = findViewById(R.id.player4);

        hero1TV = findViewById(R.id.hero1);
        hero2TV = findViewById(R.id.hero2);
        hero3TV = findViewById(R.id.hero3);
        hero4TV = findViewById(R.id.hero4);

        ready1TV = findViewById(R.id.ready1);
        ready2TV = findViewById(R.id.ready2);
        ready3TV = findViewById(R.id.ready3);
        ready4TV = findViewById(R.id.ready4);

        startGameBTN = findViewById(R.id.start_game);
        leaveLobbyBTN = findViewById(R.id.leave_lobby);
        selectHeroBTN = findViewById(R.id.confirm);
        isReadyBTN = findViewById(R.id.ready_button);

        heroSP = findViewById(R.id.spinner3);

        getSavedGamesBTN = findViewById(R.id.get_saved_games);
        gamenames = findViewById(R.id.gamenames);
        game_name = findViewById(R.id.game_name);
        saved_games = findViewById(R.id.saved_games);
        loadGameBTN = findViewById(R.id.load_game);
        gamenames.setVisibility(View.INVISIBLE);
        game_name.setVisibility(View.INVISIBLE);
        saved_games.setVisibility(View.INVISIBLE);
        loadGameBTN.setVisibility(View.INVISIBLE);

        myPlayer.setLegendCardCDisplayed(false);
        myPlayer.setLegendCardGDisplayed(false);
        myPlayer.setLegendCardNDisplayed(false);
        myPlayer.setLegendCardRuneStonesDisplayed(false);
        myPlayer.setLegendCardTheWitchDisplayed(false);

        for (int i = 0; i < myPlayer.getGame().getMaxNumPlayers(); i++) {
            if (i == 0) {
                player1NameTV.setText("Player 1: " + myPlayer.getGame().getPlayers()[0].getUsername());
                if (myPlayer.getGame().getPlayers()[0].getHero() != null) {
                    hero1TV.setText("Hero: " + myPlayer.getGame().getPlayers()[0].getHero().getHeroClass());
                } else {
                    hero1TV.setText("Hero: ");
                }
                if (myPlayer.getGame().getPlayers()[0].isReady()) {
                    ready1TV.setText("READY");
                    ready1TV.setBackgroundColor(Color.GREEN);
                }
            } else if (i == 1) {
                if (myPlayer.getGame().getPlayers()[1] != null) {
                    player2NameTV.setText("Player 2: " + myPlayer.getGame().getPlayers()[1].getUsername());
                    if (myPlayer.getGame().getPlayers()[1].getHero() != null) {
                        hero2TV.setText("Hero: " + myPlayer.getGame().getPlayers()[1].getHero().getHeroClass());
                    } else {
                        hero2TV.setText("Hero: ");
                    }
                    if (myPlayer.getGame().getPlayers()[1].isReady()) {
                        ready2TV.setText("READY");
                        ready2TV.setBackgroundColor(Color.GREEN);
                    }
                }
            } else if (i == 2) {
                if (myPlayer.getGame().getPlayers()[2] != null) {
                    player3NameTV.setText("Player 3: " + myPlayer.getGame().getPlayers()[2].getUsername());
                    if (myPlayer.getGame().getPlayers()[2].getHero() != null) {
                        hero3TV.setText("Hero: " + myPlayer.getGame().getPlayers()[2].getHero().getHeroClass());
                    } else {
                        hero3TV.setText("Hero: ");
                    }
                    if (myPlayer.getGame().getPlayers()[2].isReady()) {
                        ready3TV.setText("READY");
                        ready3TV.setBackgroundColor(Color.GREEN);
                    }
                }
            } else if (i == 3) {
                if (myPlayer.getGame().getPlayers()[3] != null) {
                    player4NameTV.setText("Player 4: " + myPlayer.getGame().getPlayers()[3].getUsername());
                    if (myPlayer.getGame().getPlayers()[3].getHero() != null) {
                        hero4TV.setText("Hero: " + myPlayer.getGame().getPlayers()[3].getHero().getHeroClass());
                    } else {
                        hero4TV.setText("Hero: ");
                    }
                    if (myPlayer.getGame().getPlayers()[3].isReady()) {
                        ready4TV.setText("READY");
                        ready4TV.setBackgroundColor(Color.GREEN);
                    }
                }
            }
        }

        if (myPlayer.getGame().getMaxNumPlayers() == 2) {
            player3NameTV.setVisibility(View.INVISIBLE);
            hero3TV.setVisibility(View.INVISIBLE);
            ready3TV.setVisibility(View.INVISIBLE);
            player4NameTV.setVisibility(View.INVISIBLE);
            hero4TV.setVisibility(View.INVISIBLE);
            ready4TV.setVisibility(View.INVISIBLE);
        } else if (myPlayer.getGame().getMaxNumPlayers() == 3) {
            player4NameTV.setVisibility(View.INVISIBLE);
            hero4TV.setVisibility(View.INVISIBLE);
            ready4TV.setVisibility(View.INVISIBLE);
        }

        t = new Thread(new Runnable() { // add logic that if game is active go to game board and end the thread
            @Override
            public void run() {
                while (!threadTerminated) {
                    try {
                        final HttpResponse<String> response = Unirest.get("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getPlayer().getUsername() + "/getPregameUpdate")
                                .asString();

                        if (response.getCode() == 200) {
                            final Game game = new Gson().fromJson(response.getBody(), Game.class);

                            runOnUiThread(new Runnable() { // cannot run this part on seperate thread, so this forces the following to run on UiThread
                                @Override
                                public void run() {
                                    if (!game.isActive()) {
                                        for (int i = 0; i < game.getMaxNumPlayers(); i++) {
                                            if (i == 0) {
                                                if (game.getPlayers()[i] != null) {
                                                    player1NameTV.setText("Player 1: " + game.getPlayers()[i].getUsername());
                                                    if (game.getPlayers()[i].getHero() != null) {
                                                        hero1TV.setText("Hero: " + game.getPlayers()[i].getHero().getHeroClass());
                                                    } else {
                                                        hero1TV.setText("Hero: ");
                                                    }
                                                    if (game.getPlayers()[i].isReady()) {
                                                        ready1TV.setText("READY");
                                                        ready1TV.setBackgroundColor(Color.GREEN);
                                                    } else {
                                                        ready1TV.setText("NOT READY");
                                                        ready1TV.setBackgroundColor(Color.RED);
                                                    }
                                                } else {
                                                    player1NameTV.setText("Player 1: ");
                                                    hero1TV.setText("Hero: ");
                                                    ready1TV.setText("NOT READY");
                                                    ready1TV.setBackgroundColor(Color.RED);
                                                }
                                            } else if (i == 1) {
                                                if (game.getPlayers()[i] != null) {
                                                    player2NameTV.setText("Player 2: " + game.getPlayers()[i].getUsername());
                                                    if (game.getPlayers()[i].getHero() != null) {
                                                        hero2TV.setText("Hero: " + game.getPlayers()[i].getHero().getHeroClass());
                                                    } else {
                                                        hero2TV.setText("Hero: ");
                                                    }
                                                    if (game.getPlayers()[i].isReady()) {
                                                        ready2TV.setText("READY");
                                                        ready2TV.setBackgroundColor(Color.GREEN);
                                                    } else {
                                                        ready2TV.setText("NOT READY");
                                                        ready2TV.setBackgroundColor(Color.RED);
                                                    }
                                                } else {
                                                    player2NameTV.setText("Player 2: ");
                                                    hero2TV.setText("Hero: ");
                                                    ready2TV.setText("NOT READY");
                                                    ready2TV.setBackgroundColor(Color.RED);
                                                }
                                            } else if (i == 2) {
                                                if (game.getPlayers()[i] != null) {
                                                    player3NameTV.setText("Player 3: " + game.getPlayers()[i].getUsername());
                                                    if (game.getPlayers()[i].getHero() != null) {
                                                        hero3TV.setText("Hero: " + game.getPlayers()[i].getHero().getHeroClass());
                                                    } else {
                                                        hero3TV.setText("Hero: ");
                                                    }
                                                    if (game.getPlayers()[i].isReady()) {
                                                        ready3TV.setText("READY");
                                                        ready3TV.setBackgroundColor(Color.GREEN);
                                                    } else {
                                                        ready3TV.setText("NOT READY");
                                                        ready3TV.setBackgroundColor(Color.RED);
                                                    }
                                                } else {
                                                    player3NameTV.setText("Player 3: ");
                                                    hero3TV.setText("Hero: ");
                                                    ready3TV.setText("NOT READY");
                                                    ready3TV.setBackgroundColor(Color.RED);
                                                }
                                            } else if (i == 3) {
                                                if (game.getPlayers()[i] != null) {
                                                    player4NameTV.setText("Player 4: " + game.getPlayers()[i].getUsername());
                                                    if (game.getPlayers()[i].getHero() != null) {
                                                        hero4TV.setText("Hero: " + game.getPlayers()[i].getHero().getHeroClass());
                                                    } else {
                                                        hero4TV.setText("Hero: ");
                                                    }
                                                    if (game.getPlayers()[i].isReady()) {
                                                        ready4TV.setText("READY");
                                                        ready4TV.setBackgroundColor(Color.GREEN);
                                                    } else {
                                                        ready4TV.setText("NOT READY");
                                                        ready4TV.setBackgroundColor(Color.RED);
                                                    }
                                                } else {
                                                    player4NameTV.setText("Player 4: ");
                                                    hero4TV.setText("Hero: ");
                                                    ready4TV.setText("NOT READY");
                                                    ready4TV.setBackgroundColor(Color.RED);
                                                }
                                            }
                                        }
                                    } else {
                                        myPlayer.setGame(game);
                                        updatedGame = game;
                                        if(updatedGame.isGameLoaded()) {
                                            interruptThreadAndStartActivityLoad();
                                        } else {
                                            interruptThreadAndStartActivity();
                                        }
                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();

        leaveLobbyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPlayer.getInstance().getPlayer().setHero(null);
                try {
                    LeavePregameSender leavePregameSender = new LeavePregameSender();
                    AsyncTask<String, Void, LeavePregameResponses> asyncTask = leavePregameSender.execute();

                    if (asyncTask.get() == LeavePregameResponses.ERROR_GAME_LOADED) {
                        Toast.makeText(WaitScreen.this, "Leave error. Cannot leave after loading game.", Toast.LENGTH_LONG).show();
                    } else {
                        threadTerminated = true;
                        //myPlayer.setGame(null);

                        Intent intent = new Intent(WaitScreen.this, CreateGame.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent); //EXIT LOBBY AND HEAD TO CREATE GAME
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        selectHeroBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask<String, Void, SelectHeroResponses> asyncTask;
                try {
                    SelectHeroSender selectHeroSender = new SelectHeroSender();
                    asyncTask = selectHeroSender.execute(new Gson().toJson(new Gson().fromJson(heroSP.getSelectedItem().toString(), HeroClass.class)));

                    if (asyncTask.get() == null) {
                        Toast.makeText(WaitScreen.this, "Select hero error. No response from server.", Toast.LENGTH_LONG).show();
                    } else if (asyncTask.get() == SelectHeroResponses.ERROR_HERO_ALREADY_SELECTED) {
                        Toast.makeText(WaitScreen.this, "Select hero error. Hero already selected.", Toast.LENGTH_LONG).show();
                    } else if (asyncTask.get() == SelectHeroResponses.ERROR_DUPLICATE_HERO) {
                        Toast.makeText(WaitScreen.this, "Select hero error. Hero already exists in the game.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(WaitScreen.this, "Select hero success.", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        isReadyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask<String, Void, IsReadyResponses> asyncTask;
                try {
                    IsReadySender isReadySender = new IsReadySender();
                    asyncTask = isReadySender.execute();

                    if (asyncTask.get() == null) {
                        Toast.makeText(WaitScreen.this, "Ready error. No response from server.", Toast.LENGTH_LONG).show();
                    } else if (asyncTask.get() == IsReadyResponses.ERROR_NO_SELECTED_HERO) {
                        Toast.makeText(WaitScreen.this, "Ready error. No hero selected.", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        startGameBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask<String, Void, StartGameResponses> asyncTask;
                try {
                    StartGameSender startGameSender = new StartGameSender();
                    asyncTask = startGameSender.execute();

                    if (asyncTask.get() == null) {
                        Toast.makeText(WaitScreen.this, "Start game error. No response from server.", Toast.LENGTH_LONG).show();
                    } else if (asyncTask.get() == StartGameResponses.ERROR_NOT_HOST) {
                        Toast.makeText(WaitScreen.this, "Start game error. Only the host can start the game.", Toast.LENGTH_LONG).show();
                    } else if (asyncTask.get() == StartGameResponses.ERROR_NOT_ENOUGH_PLAYERS) {
                        Toast.makeText(WaitScreen.this, "Start game error. Must have at least 2 players in lobby.", Toast.LENGTH_LONG).show();
                    } else if (asyncTask.get() == StartGameResponses.ERROR_PLAYER_NOT_READY) {
                        Toast.makeText(WaitScreen.this, "Start game error. Not every player in lobby is ready.", Toast.LENGTH_LONG).show();
                    } else {
                        myPlayer.setGame(updatedGame);
                        if(updatedGame.isGameLoaded()) {
                            interruptThreadAndStartActivityLoad();
                        } else {
                            interruptThreadAndStartActivity();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        adapter = new ArrayAdapter<>(WaitScreen.this, android.R.layout.simple_list_item_1, gameNames);
        saved_games.setAdapter(adapter);

        getSavedGamesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gamenames.setVisibility(View.VISIBLE);
                game_name.setVisibility(View.VISIBLE);
                saved_games.setVisibility(View.VISIBLE);
                loadGameBTN.setVisibility(View.VISIBLE);

                AsyncTask<String, Void, ArrayList<Game>> asyncTaskSaved;
                try {
                    GetSavedGames getSavedGames = new GetSavedGames();
                    asyncTaskSaved = getSavedGames.execute();
                    savedGames = asyncTaskSaved.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (Game g : savedGames) {
                    if (g.getGameName() != null) {
                        gameNames.add(g.getGameName());
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        saved_games.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                game_name.setText(gameNames.get(position));
            }
        });

        loadGameBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask<String, Void, LoadGameResponses> asyncTaskLoad;
                LoadGameResponses loadGameResponses;

                if (game_name.getText() == null || game_name.getText().length() == 0) {
                    Toast.makeText(WaitScreen.this, "No game selected", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        LoadGameSender loadGameSender = new LoadGameSender();
                        String name = game_name.getText().toString();
                        asyncTaskLoad = loadGameSender.execute(name);
                        loadGameResponses = asyncTaskLoad.get();

                        if (loadGameResponses == null) {
                            Toast.makeText(WaitScreen.this, "Load game error. No response from server.", Toast.LENGTH_LONG).show();
                        } else if (loadGameResponses == LoadGameResponses.NOT_HOST) {
                            Toast.makeText(WaitScreen.this, "Load game error. Only the host can load the game.", Toast.LENGTH_LONG).show();
                        } else if (loadGameResponses == LoadGameResponses.ERROR_NOT_ALL_PLAYERS_SELECTED_HEROES) {
                            Toast.makeText(WaitScreen.this, "Load game error. All players must select a hero", Toast.LENGTH_LONG).show();
                        } else if (loadGameResponses == LoadGameResponses.ERROR_DIFFICULTY_MISMATCH) {
                            Toast.makeText(WaitScreen.this, "Load game error. Difficulty does not match the saved game's difficulty", Toast.LENGTH_LONG).show();
                        } else if (loadGameResponses == LoadGameResponses.ERROR_HERO_MISMATCH) {
                            Toast.makeText(WaitScreen.this, "Load game error. Heroes selected do not match the saved game's heroes", Toast.LENGTH_LONG).show();
                        } else if (loadGameResponses == LoadGameResponses.ERROR_PLAYER_NUM_MISMATCH) {
                            Toast.makeText(WaitScreen.this, "Load game error. Number of players does match the saved game's", Toast.LENGTH_LONG).show();
                        } else if (loadGameResponses == LoadGameResponses.NOT_ALL_PLAYERS_READY) {
                            Toast.makeText(WaitScreen.this, "Load game error. All the players must be ready", Toast.LENGTH_LONG).show();
                        } else if (loadGameResponses == LoadGameResponses.NOT_ENOUGH_PLAYERS) {
                            Toast.makeText(WaitScreen.this, "Load game error. Must have at least 2 players in the lobby.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(WaitScreen.this, "Load game success", Toast.LENGTH_LONG).show();
                            startGameBTN.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void interruptThreadAndStartActivity() {
        threadTerminated = true;

        Intent myIntent;
        myIntent = new Intent(WaitScreen.this, LegendCardA1.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
        finish();
    }

    public void interruptThreadAndStartActivityLoad() {
        threadTerminated = true;

        Intent myIntent;
        myIntent = new Intent(WaitScreen.this, Board.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
        finish();
    }
        /*Game g = myPlayer.getGame();
        Fight f = g.getCurrentFight();

        if (f != null) {
            ArrayList<Hero> heroes = f.getHeroes();
            for (int i = 0; i < g.getCurrentNumPlayers(); i++) {
                if (myPlayer.getPlayer().getUsername().equals(g.getPlayers()[i].getUsername())) {
                    for (Hero h : heroes) {
                        if (h.getHeroClass() == g.getPlayers()[i].getHero().getHeroClass()) {
                            Intent myIntent;
                            myIntent = new Intent(WaitScreen.this, MonsterFight.class);
                            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(myIntent);
                            finish();
                        }
                    }
                    Intent myIntent;
                    myIntent = new Intent(WaitScreen.this, Board.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(myIntent);
                    finish();
                }
            }*/

//    public void interruptThreadAndStartActivity() {
//        threadTerminated = true;
//
//        Intent myIntent;
//        if(myPlayer.getPlayer().getUsername().equals(myPlayer.getGame().getPlayers()[0].getUsername())){
//            myIntent = new Intent(WaitScreen.this, DistributeItems.class);
//        }else{
//            myIntent = new Intent(WaitScreen.this, DistributeItemsWaitPage.class);
//        }
//        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(myIntent);
//        finish();
//    }


    private static class LeavePregameSender extends AsyncTask<String, Void, LeavePregameResponses> {
        @Override
        protected LeavePregameResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.delete("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() + "/" + myPlayer.getPlayer().getUsername() + "/leavePregame") // here game1 is a test, the gameName goes here
                        .asString();

                String resultAsJsonString = response.getBody();
                return new Gson().fromJson(resultAsJsonString, LeavePregameResponses.class);

            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class SelectHeroSender extends AsyncTask<String, Void, SelectHeroResponses> {
        @Override
        protected SelectHeroResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() + "/" + myPlayer.getPlayer().getUsername() + "/selectHero")
                        .header("Content-Type", "application/json")
                        .body(strings[0])
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, SelectHeroResponses.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class IsReadySender extends AsyncTask<String, Void, IsReadyResponses> {
        @Override
        protected IsReadyResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() + "/" + myPlayer.getPlayer().getUsername() + "/isReady")
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, IsReadyResponses.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class StartGameSender extends AsyncTask<String, Void, StartGameResponses> {
        @Override
        protected StartGameResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() + "/" + myPlayer.getPlayer().getUsername() + "/startGame")
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, StartGameResponses.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class GetSavedGames extends AsyncTask<String, Void, ArrayList<Game>> {
        @Override
        protected ArrayList<Game> doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.get("http://" + myPlayer.getServerIP() + ":8080/getSavedGames")
                        .asString();

                String resultAsJsonString = response.getBody();
                System.out.println("RESPONSE BODY " + response.getBody());
                return new Gson().fromJson(resultAsJsonString, new TypeToken<ArrayList<Game>>() {}.getType());
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class LoadGameSender extends AsyncTask<String, Void, LoadGameResponses> {
        @Override
        protected LoadGameResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() + "/" + myPlayer.getPlayer().getUsername() + "/loadGame")
                        .header("Content-Type", "application/json")
                        .body(strings[0])
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, LoadGameResponses.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
