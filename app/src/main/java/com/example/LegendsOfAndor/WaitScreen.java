package com.example.LegendsOfAndor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
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

    private Thread t;
    MyPlayer myPlayer = MyPlayer.getInstance();
    Game updatedGame = null;

    @Override
    public void onBackPressed() {
    }

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
                while (!Thread.currentThread().isInterrupted()) {
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
                                        interruptThreadAndStartActivity();
                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                        if (e instanceof InterruptedException) {
                            Thread.currentThread().interrupt();
                        }
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
                    leavePregameSender.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                t.interrupt();
                //myPlayer.setGame(null);

                Intent intent = new Intent(WaitScreen.this, CreateGame.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent); //EXIT LOBBY AND HEAD TO CREATE GAME
                finish();
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
                        interruptThreadAndStartActivity();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void interruptThreadAndStartActivity() {
//        startActivity(new Intent(WaitScreen.this, MonsterFight.class));
        if(myPlayer.getPlayer().getUsername().equals(myPlayer.getGame().getPlayers()[0].getUsername())){
            startActivity(new Intent(WaitScreen.this, DistributeItems.class));
        }else{
            startActivity(new Intent(WaitScreen.this, DistributeItemsWaitPage.class));
        }
        t.interrupt();
    }


    private static class LeavePregameSender extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();

            try {
                Unirest.delete("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() + "/" + myPlayer.getPlayer().getUsername() + "/leavePregame") // here game1 is a test, the gameName goes here
                        .asString();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return "";
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
}
