package com.example.LegendsOfAndor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

enum LeaveFightResponses {
    CANNOT_LEAVE_AFTER_ROLLING, CANNOT_LEAVE_WITHOUT_FIGHTING, SUCCESS
}

enum EndBattleRoundResponses {
    WON_ROUND, LOST_ROUND, TIE_ROUND, CREATURE_DEFEATED, BATTLE_LOST, PLAYERS_NO_BATTLE_VALUE, CREATURE_NO_BATTLE_VALUE, WAITING_FOR_PLAYERS_TO_JOIN
}

public class MonsterFight extends AppCompatActivity {

    public static final Random RANDOM = new Random();

    private Button getDice;
    private Button rollDice;
    private Button getEnemyDice;
    private Button rollEnemyDice;
    private Button attack;
    private ImageView imageDice1, imageDice2, imageDice3, imageDice4;
    private TextView playersBattleValue;// = findViewById(R.id.playerBattleValue);
    private TextView monsterBattleValue;

    private TextView player1Profile;
    private TextView player2Profile;
    private TextView player3Profile;
    private TextView player4Profile;
    private TextView player1WP;
    private TextView player2WP;
    private TextView player3WP;
    private TextView player4WP;
    private TextView player1SP;
    private TextView player2SP;
    private TextView player3SP;
    private TextView player4SP;

    private TextView player1BV;
    private TextView player2BV;
    private TextView player3BV;
    private TextView player4BV;

    private TextView enemyProfile;
    private TextView enemyWP;
    private TextView enemySP;

    private ImageView currentCreatureIV;

//    private ImageView player1d1 = findViewById(R.id.player1_d1);
//    private ImageView player1d2 = findViewById(R.id.player1_d2);
//    private ImageView player1d3 = findViewById(R.id.player1_d3);
//    private ImageView player1d4 = findViewById(R.id.player1_d4);
//    private ImageView player1d5 = findViewById(R.id.player1_d5);
//    private ImageView player2d1 = findViewById(R.id.player2_d1);
//    private ImageView player2d2 = findViewById(R.id.player2_d2);
//    private ImageView player2d3 = findViewById(R.id.player2_d3);
//    private ImageView player2d4 = findViewById(R.id.player2_d4);
//    private ImageView player2d5 = findViewById(R.id.player2_d5);
//    private ImageView player3d1 = findViewById(R.id.player3_d1);
//    private ImageView player3d2 = findViewById(R.id.player3_d2);
//    private ImageView player3d3 = findViewById(R.id.player3_d3);
//    private ImageView player3d4 = findViewById(R.id.player3_d4);
//    private ImageView player3d5 = findViewById(R.id.player3_d5);
//    private ImageView player4d1 = findViewById(R.id.player4_d1);
//    private ImageView player4d2 = findViewById(R.id.player4_d2);
//    private ImageView player4d3 = findViewById(R.id.player4_d3);
//    private ImageView player4d4 = findViewById(R.id.player4_d4);
//    private ImageView player4d5 = findViewById(R.id.player4_d5);

    private ImageView player1d1;
    private ImageView player1d2;
    private ImageView player1d3;
    private ImageView player1d4;
    private ImageView player1d5;
    private ImageView player2d1;
    private ImageView player2d2;
    private ImageView player2d3;
    private ImageView player2d4;
    private ImageView player2d5;
    private ImageView player3d1;
    private ImageView player3d2;
    private ImageView player3d3;
    private ImageView player3d4;
    private ImageView player3d5;
    private ImageView player4d1;
    private ImageView player4d2;
    private ImageView player4d3;
    private ImageView player4d4;
    private ImageView player4d5;

    private ImageView enemyd1;
    private ImageView enemyd2;
    private ImageView enemyd3;
    private ImageView enemyd4;
    private ImageView enemyd5;

    private Thread t;
    MyPlayer myPlayer = MyPlayer.getInstance();

    ArrayList<Die> myDice = new ArrayList<>();
    ArrayList<Die> creatureDice = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monster_fight);
//        final HeroClass myPlayer.getGame().getCurrentFight().getHeroes().get(i).getHeroClass() = myPlayer.getPlayer().getHero().getHeroClass();
        final Hero playerHero = myPlayer.getPlayer().getHero();

//        final int currentIndex = myPlayer.getGame().getCurrentFight().getHeroes().indexOf(playerHero);
        final ArrayList<Hero> playerIndex = myPlayer.getGame().getCurrentFight().getHeroes();

        player1BV = findViewById(R.id.player1_bv);
        player2BV = findViewById(R.id.player2_bv);
        player3BV = findViewById(R.id.player3_bv);
        player4BV = findViewById(R.id.player4_bv);
        playersBattleValue = findViewById(R.id.playerBattleValue);
        monsterBattleValue = findViewById(R.id.monsterBattleValue);

        enemyd1 = findViewById(R.id.enemy_d1);
        enemyd2 = findViewById(R.id.enemy_d2);
        enemyd3 = findViewById(R.id.enemy_d3);
        enemyd4 = findViewById(R.id.enemy_d4);
        enemyd5 = findViewById(R.id.enemy_d5);

//        int playersInFight = playerIndex.size();
        List<TextView> profileList = new ArrayList<TextView>();
        List<TextView> playerWPList = new ArrayList<TextView>();
        List<TextView> playerSPList = new ArrayList<TextView>();
        profileList.add(player1Profile);
        profileList.add(player2Profile);
        profileList.add(player3Profile);
        profileList.add(player4Profile);
        playerWPList.add(player1WP);
        playerWPList.add(player2WP);
        playerWPList.add(player3WP);
        playerWPList.add(player4WP);
        playerSPList.add(player1SP);
        playerSPList.add(player2SP);
        playerSPList.add(player3SP);
        playerSPList.add(player4SP);

        final List<ImageView> player1Dice = new ArrayList<ImageView>();
        final List<ImageView> player2Dice = new ArrayList<ImageView>();
        final List<ImageView> player3Dice = new ArrayList<ImageView>();
        final List<ImageView> player4Dice = new ArrayList<ImageView>();
        player1Dice.add(player1d1);
        player1Dice.add(player1d2);
        player1Dice.add(player1d3);
        player1Dice.add(player1d4);
        player1Dice.add(player1d5);
        player2Dice.add(player2d1);
        player2Dice.add(player2d2);
        player2Dice.add(player2d3);
        player2Dice.add(player2d4);
        player2Dice.add(player2d5);
        player3Dice.add(player3d1);
        player3Dice.add(player3d2);
        player3Dice.add(player3d3);
        player3Dice.add(player3d4);
        player3Dice.add(player3d5);
        player4Dice.add(player4d1);
        player4Dice.add(player4d2);
        player4Dice.add(player4d3);
        player4Dice.add(player4d4);
        player4Dice.add(player4d5);


        //Changes and makes visible the picture of the monster currently being faced
        //!!!!!!!!!!!![NOT FINISHED: Need to find and add wardrak resources]!!!!!!!!!!!!!!!!!!!!!!!!
        currentCreatureIV = findViewById(R.id.creatureImage);
        CreatureType creatureType = myPlayer.getGame().getCurrentFight().getCreature().getCreatureType();
        if (creatureType == CreatureType.GOR) {
            currentCreatureIV.setImageDrawable(getResources().getDrawable(getResourceID("gor", "drawable", getApplicationContext())));
        } else if (creatureType == CreatureType.SKRAL) {
            currentCreatureIV.setImageDrawable(getResources().getDrawable(getResourceID("skral", "drawable", getApplicationContext())));
        } else if (creatureType == CreatureType.WARDRAKS) { //if creatureType == CreatureType.TROLL
            currentCreatureIV.setImageDrawable(getResources().getDrawable(getResourceID("wardrak", "drawable", getApplicationContext())));
        } else { // if creatureType == CreatureType.TROLL
            currentCreatureIV.setImageDrawable(getResources().getDrawable(getResourceID("troll", "drawable", getApplicationContext())));
        }





        //Adds information for participating players, making their profiles visible
        for (int i = 0; i < playerIndex.size(); i++) {
            Hero currentPlayer = playerIndex.get(i);
            int playerNumber = i + 1;
            String playerPosition = "player" + playerNumber;
            String playerWPText = "player" + playerNumber + "_wp";
            String playerSPText = "player" + playerNumber + "_sp";
            String playerBVText = "player" + playerNumber + "_bv";
            int playerProfileID = getResources().getIdentifier(playerPosition, "id", getPackageName());
            int playerWPID = getResources().getIdentifier(playerWPText, "id", getPackageName());
            int playerSPID = getResources().getIdentifier(playerSPText, "id", getPackageName());
            int playerBVID = getResources().getIdentifier(playerBVText, "id", getPackageName());
            TextView userProfile = findViewById(playerProfileID);
            TextView userWP = findViewById(playerWPID);
            TextView userSP = findViewById(playerSPID);
            TextView userBV = findViewById(playerBVID);
            String currentWP = "WP: " + currentPlayer.getWillPower();
            String currentSP = "SP: " + currentPlayer.getStrength();
            userProfile.setText(currentPlayer.getHeroClass().toString());
            userWP.setText(currentWP);
            userSP.setText(currentSP);
            userProfile.setVisibility(View.VISIBLE);
            userWP.setVisibility(View.VISIBLE);
            userSP.setVisibility(View.VISIBLE);
            userBV.setVisibility(View.VISIBLE);
        }

        //Add information for current creature, makes attributes visible
        Creature currentMonster = myPlayer.getGame().getCurrentFight().getCreature();
        enemyProfile = findViewById(R.id.enemy);
        enemyWP = findViewById(R.id.enemy_wp);
        enemySP = findViewById(R.id.enemy_sp);

        enemyProfile.setText(currentMonster.getCreatureType().toString());
        String enemyCurrentWp = "WP:" + currentMonster.getWillpower();
        String enemyCurrentSp = "SP:" + currentMonster.getStrength();
        enemyWP.setText(enemyCurrentWp);
        enemySP.setText(enemyCurrentSp);
        enemyProfile.setVisibility(View.VISIBLE);
        enemyWP.setVisibility(View.VISIBLE);
        enemySP.setVisibility(View.VISIBLE);


        t = new Thread(new Runnable() { // add logic that if game is active go to game board and end the thread
            @Override
            public void run() {
                final MyPlayer myPlayer = MyPlayer.getInstance();
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        final HttpResponse<String> response = Unirest.get("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getPlayer().getUsername() + "/getGameUpdate")
                                .asString();

                        if(response.getCode() == 200){
                            final Game game = new Gson().fromJson(response.getBody(), Game.class);
                            final Fight fight = game.getCurrentFight();
                            MyPlayer.getInstance().setGame(game);

                            runOnUiThread(new Runnable() { // cannot run this part on separate thread, so this forces the following to run on UiThread
                                @Override
                                public void run() {
                                    if (fight.getHeroes().size() == 0) {
                                        interruptThreadAndGoToBoard();
                                    }

                                    boolean heroFound = false;
                                    for (Hero h : fight.getHeroes()) {
                                        if (h.getHeroClass() == myPlayer.getGame().getSinglePlayer(myPlayer.getPlayer().getUsername()).getHero().getHeroClass()) {
                                            heroFound = true;
                                        }
                                    }

                                    if (!heroFound) {
                                        interruptThreadAndGoToBoard();
                                    }

                                    for (int i = 0; i < fight.getHeroes().size(); i++) {
                                        Hero currentPlayer = fight.getHeroes().get(i);
                                        int playerNumber = i + 1;
                                        String playerPosition = "player" + playerNumber;
                                        String playerWPText = "player" + playerNumber + "_wp";
                                        String playerSPText = "player" + playerNumber + "_sp";
                                        String playerBVText = "player" + playerNumber + "_bv";
                                        int playerProfileID = getResources().getIdentifier(playerPosition, "id", getPackageName());
                                        int playerWPID = getResources().getIdentifier(playerWPText, "id", getPackageName());
                                        int playerSPID = getResources().getIdentifier(playerSPText, "id", getPackageName());
                                        int playerBVID = getResources().getIdentifier(playerBVText, "id", getPackageName());
                                        TextView userProfile = findViewById(playerProfileID);
                                        TextView userWP = findViewById(playerWPID);
                                        TextView userSP = findViewById(playerSPID);
                                        TextView userBV = findViewById(playerBVID);
                                        String currentWP = "WP: " + currentPlayer.getWillPower();
                                        String currentSP = "SP: " + currentPlayer.getStrength();
                                        userProfile.setText(currentPlayer.getHeroClass().toString());
                                        userWP.setText(currentWP);
                                        userSP.setText(currentSP);
                                        userProfile.setVisibility(View.VISIBLE);
                                        userWP.setVisibility(View.VISIBLE);
                                        userSP.setVisibility(View.VISIBLE);
                                        userBV.setVisibility(View.VISIBLE);
                                    }

                                    int i = 0;

                                    ImageView currentD1;
                                    ImageView currentD2;
                                    ImageView currentD3;
                                    ImageView currentD4;
                                    ImageView currentD5;

                                    int totalBV = 0;
                                    for (Integer bv : fight.getHeroesBattleScores()) {
                                        totalBV += bv;
                                    }
                                    playersBattleValue.setText(Integer.toString(totalBV));

                                    for (Hero h : fight.getHeroes()) {
                                        if (fight.getHeroesBattleScores().get(i) > 0) {
                                            if (i == 0) {
                                                player1BV.setText("BV: " + fight.getHeroesBattleScores().get(i));
                                            } else if (i == 1) {
                                                player2BV.setText("BV: " + fight.getHeroesBattleScores().get(i));
                                            } else if (i == 2) {
                                                player3BV.setText("BV: " + fight.getHeroesBattleScores().get(i));
                                            } else { // i = 3
                                                player4BV.setText("BV: " + fight.getHeroesBattleScores().get(i));
                                            }
                                        }

                                        int playerNum = i + 1;
                                        String diceNum1 = "player" + playerNum + "_d" + 1;
                                        String diceNum2 = "player" + playerNum + "_d" + 2;
                                        String diceNum3 = "player" + playerNum + "_d" + 3;
                                        String diceNum4 = "player" + playerNum + "_d" + 4;
                                        String diceNum5 = "player" + playerNum + "_d" + 5;
                                        int d1IV = getResources().getIdentifier(diceNum1, "id", getPackageName());
                                        int d2IV = getResources().getIdentifier(diceNum2, "id", getPackageName());
                                        int d3IV = getResources().getIdentifier(diceNum3, "id", getPackageName());
                                        int d4IV = getResources().getIdentifier(diceNum4, "id", getPackageName());
                                        int d5IV = getResources().getIdentifier(diceNum5, "id", getPackageName());

                                        currentD1 = findViewById(d1IV);
                                        currentD2 = findViewById(d2IV);
                                        currentD3 = findViewById(d3IV);
                                        currentD4 = findViewById(d4IV);
                                        currentD5 = findViewById(d5IV);

                                        currentD1.setVisibility(View.INVISIBLE);
                                        currentD2.setVisibility(View.INVISIBLE);
                                        currentD3.setVisibility(View.INVISIBLE);
                                        currentD4.setVisibility(View.INVISIBLE);
                                        currentD5.setVisibility(View.INVISIBLE);

//                                        imageDice1.setImageDrawable(getResources().getDrawable(getResourceID(dice1, "drawable", getApplicationContext())));
                                        if (h.getHeroClass() == HeroClass.WARRIOR) {
                                            String class_id;
                                                for (int j = 0; j < fight.getWarriorDice().size(); j++) {
                                                    Integer dieValue = fight.getWarriorDice().get(j);

                                                    if (dieValue == 0) {
                                                        class_id = "warrior_dice";
                                                    } else {
                                                        class_id = "warrior_dice_" + dieValue;
                                                    }
                                                    if (j == 0) {
                                                        currentD1.setImageDrawable(getResources().getDrawable(getResourceID(class_id, "drawable", getApplicationContext())));
                                                        currentD1.setVisibility(View.VISIBLE);
                                                    } else if (j == 1) {
                                                        currentD2.setImageDrawable(getResources().getDrawable(getResourceID(class_id, "drawable", getApplicationContext())));
                                                        currentD2.setVisibility(View.VISIBLE);
                                                    } else if (j == 2) {
                                                        currentD3.setImageDrawable(getResources().getDrawable(getResourceID(class_id, "drawable", getApplicationContext())));
                                                        currentD3.setVisibility(View.VISIBLE);
                                                    } else if (j == 3) {
                                                        currentD4.setImageDrawable(getResources().getDrawable(getResourceID(class_id, "drawable", getApplicationContext())));
                                                        currentD4.setVisibility(View.VISIBLE);
                                                    } else {
                                                        currentD5.setImageDrawable(getResources().getDrawable(getResourceID(class_id, "drawable", getApplicationContext())));
                                                        currentD5.setVisibility(View.VISIBLE);
                                                    }
                                            }
                                        } else if (h.getHeroClass() == HeroClass.ARCHER) {
                                            String class_id;
                                            for (int j = 0; j < fight.getArcherDice().size(); j++) {
                                                Integer dieValue = fight.getArcherDice().get(j);
                                                if (dieValue == 0) {
                                                    class_id = "archer_dice";
                                                } else {
                                                    class_id = "archer_dice_" + dieValue;
                                                }
                                                if (j == 0) {
                                                    currentD1.setImageDrawable(getResources().getDrawable(getResourceID(class_id, "drawable", getApplicationContext())));
                                                    currentD1.setVisibility(View.VISIBLE);
                                                } else if (j == 1) {
                                                    currentD2.setImageDrawable(getResources().getDrawable(getResourceID(class_id, "drawable", getApplicationContext())));
                                                    currentD2.setVisibility(View.VISIBLE);
                                                } else if (j == 2) {
                                                    currentD3.setImageDrawable(getResources().getDrawable(getResourceID(class_id, "drawable", getApplicationContext())));
                                                    currentD3.setVisibility(View.VISIBLE);
                                                } else if (j == 3) {
                                                    currentD4.setImageDrawable(getResources().getDrawable(getResourceID(class_id, "drawable", getApplicationContext())));
                                                    currentD4.setVisibility(View.VISIBLE);
                                                } else {
                                                    currentD5.setImageDrawable(getResources().getDrawable(getResourceID(class_id, "drawable", getApplicationContext())));
                                                    currentD5.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        } else if (h.getHeroClass() == HeroClass.DWARF) {
                                            String class_id;
                                            for (int j = 0; j < fight.getDwarfDice().size(); j++) {
                                                Integer dieValue = fight.getDwarfDice().get(j);

                                                if (dieValue == 0) {
                                                    class_id = "dwarf_dice";
                                                } else {
                                                    class_id = "dwarf_dice_" + dieValue;
                                                }
                                                if (j == 0) {
                                                    currentD1.setImageDrawable(getResources().getDrawable(getResourceID(class_id, "drawable", getApplicationContext())));
                                                    currentD1.setVisibility(View.VISIBLE);
                                                } else if (j == 1) {
                                                    currentD2.setImageDrawable(getResources().getDrawable(getResourceID(class_id, "drawable", getApplicationContext())));
                                                    currentD2.setVisibility(View.VISIBLE);
                                                } else if (j == 2) {
                                                    currentD3.setImageDrawable(getResources().getDrawable(getResourceID(class_id, "drawable", getApplicationContext())));
                                                    currentD3.setVisibility(View.VISIBLE);
                                                } else if (j == 3) {
                                                    currentD4.setImageDrawable(getResources().getDrawable(getResourceID(class_id, "drawable", getApplicationContext())));
                                                    currentD4.setVisibility(View.VISIBLE);
                                                } else {
                                                    currentD5.setImageDrawable(getResources().getDrawable(getResourceID(class_id, "drawable", getApplicationContext())));
                                                    currentD5.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        } else { // wizard
                                            String class_id;
                                            for (int j = 0; j < fight.getWizardDice().size(); j++) {
                                                Integer dieValue = fight.getWizardDice().get(j);

                                                if (dieValue == 0) {
                                                    class_id = "wizard_dice";
                                                } else {
                                                    class_id = "wizard_dice_" + dieValue;
                                                }
                                                if (j == 0) {
                                                    currentD1.setImageDrawable(getResources().getDrawable(getResourceID(class_id, "drawable", getApplicationContext())));
                                                    currentD1.setVisibility(View.VISIBLE);
                                                } else if (j == 1) {
                                                    currentD2.setImageDrawable(getResources().getDrawable(getResourceID(class_id, "drawable", getApplicationContext())));
                                                    currentD2.setVisibility(View.VISIBLE);
                                                } else if (j == 2) {
                                                    currentD3.setImageDrawable(getResources().getDrawable(getResourceID(class_id, "drawable", getApplicationContext())));
                                                    currentD3.setVisibility(View.VISIBLE);
                                                } else if (j == 3) {
                                                    currentD4.setImageDrawable(getResources().getDrawable(getResourceID(class_id, "drawable", getApplicationContext())));
                                                    currentD4.setVisibility(View.VISIBLE);
                                                } else {
                                                    currentD5.setImageDrawable(getResources().getDrawable(getResourceID(class_id, "drawable", getApplicationContext())));
                                                    currentD5.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        }
                                        i++;
                                    }

                                    String class_id;

                                    enemyd1.setVisibility(View.INVISIBLE);
                                    enemyd2.setVisibility(View.INVISIBLE);
                                    enemyd3.setVisibility(View.INVISIBLE);
                                    enemyd4.setVisibility(View.INVISIBLE);
                                    enemyd5.setVisibility(View.INVISIBLE);


                                    for (int k = 0; k < fight.getCreatureDice().size(); k++) {
                                        Integer dieValue = fight.getCreatureDice().get(k);

                                        if (dieValue == -1) { // empty black die
                                            class_id = "black_dice";
                                        } else if (dieValue >= 8) { // black die
                                            class_id = "black_dice_" + dieValue;
                                        } else if (dieValue == 0) {
                                            class_id = "enemy_dice";
                                        } else { // dieValue 1 to 6
                                            class_id = "enemy_dice_" + dieValue;
                                        }

                                        if (k == 0) {
                                            enemyd1.setImageDrawable(getResources().getDrawable(getResourceID(class_id, "drawable", getApplicationContext())));
                                            enemyd1.setVisibility(View.VISIBLE);
                                        } else if (k == 1) {
                                            enemyd2.setImageDrawable(getResources().getDrawable(getResourceID(class_id, "drawable", getApplicationContext())));
                                            enemyd2.setVisibility(View.VISIBLE);
                                        } else if (k == 2) {
                                            enemyd3.setImageDrawable(getResources().getDrawable(getResourceID(class_id, "drawable", getApplicationContext())));
                                            enemyd3.setVisibility(View.VISIBLE);
                                        } else if (k == 3) {
                                            enemyd4.setImageDrawable(getResources().getDrawable(getResourceID(class_id, "drawable", getApplicationContext())));
                                            enemyd4.setVisibility(View.VISIBLE);
                                        } else {
                                            enemyd5.setImageDrawable(getResources().getDrawable(getResourceID(class_id, "drawable", getApplicationContext())));
                                            enemyd5.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    monsterBattleValue.setText(Integer.toString(fight.getCreatureBattleScore()));

                                    enemyWP.setText("WP:"+fight.getCreature().getWillpower());
                                    enemySP.setText("SP:"+fight.getCreature().getStrength());
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



        //Retrieves the amount of dice for the current player class
        getDice = findViewById(R.id.get_dice);
        getDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask<String, Void, ArrayList<Die>> asyncTask;

                try {
                    GetDiceSender getDiceSender = new GetDiceSender();
                    asyncTask = getDiceSender.execute();
                    myDice = asyncTask.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        rollDice = findViewById(R.id.roll_dice);
        rollDice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ArrayList<Integer> myDiceRolls = new ArrayList<>();

                for (Die die : myDice) {
                    myDiceRolls.add(die.rollDie());
                }

                try {
                    CalculateBattleValueSender calculateBattleValueSender = new CalculateBattleValueSender();
                    calculateBattleValueSender.execute(new Gson().toJson(myDiceRolls));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //player1BV.setText("BV: " + battleValue);
            }
        });

        getEnemyDice = findViewById(R.id.get_enemy_dice);
        getEnemyDice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AsyncTask<String, Void, ArrayList<Die>> asyncTask;

                try {
                    GetCreatureDiceSender getCreatureDiceSender = new GetCreatureDiceSender();
                    asyncTask = getCreatureDiceSender.execute();
                    creatureDice = asyncTask.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        rollEnemyDice = findViewById(R.id.roll_enemy_dice);
        rollEnemyDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Integer> creatureRolls = new ArrayList<>();

                for (Die die : creatureDice) {
                    creatureRolls.add(die.rollDie());
                }

                try {
                    CalculateCreatureBattleValueSender calculateCreatureBattleValueSender = new CalculateCreatureBattleValueSender();
                    calculateCreatureBattleValueSender.execute(new Gson().toJson(creatureRolls));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        attack = findViewById(R.id.attack_btn);
        attack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AsyncTask<String, Void, EndBattleRoundResponses> asyncTask;

                    EndBattleRoundSender endBattleRoundSender = new EndBattleRoundSender();
                    asyncTask = endBattleRoundSender.execute("");

                    if (asyncTask.get() == EndBattleRoundResponses.WON_ROUND) {
                        Toast.makeText(MonsterFight.this, "Heroes won this round!", Toast.LENGTH_LONG).show();
                    } else if (asyncTask.get() == EndBattleRoundResponses.LOST_ROUND) {
                        Toast.makeText(MonsterFight.this, "Heroes lost this round!", Toast.LENGTH_LONG).show();
                    } else if (asyncTask.get() == EndBattleRoundResponses.TIE_ROUND) {
                        Toast.makeText(MonsterFight.this, "This round resulted in a tie!", Toast.LENGTH_LONG).show();
                    } else if (asyncTask.get() == EndBattleRoundResponses.CREATURE_DEFEATED) {
                        t.interrupt();
                        Toast.makeText(MonsterFight.this, "Heroes win! Creature defeated!", Toast.LENGTH_LONG).show();
                    } else if (asyncTask.get() == EndBattleRoundResponses.BATTLE_LOST) {
                        Toast.makeText(MonsterFight.this, "Creature wins! Heroes defeated! Going back to the board...", Toast.LENGTH_LONG).show();
                    } else if (asyncTask.get() == EndBattleRoundResponses.PLAYERS_NO_BATTLE_VALUE) {
                        Toast.makeText(MonsterFight.this, "ERROR. Cannot attack! Player(s) do not have battle values!", Toast.LENGTH_LONG).show();
                    } else if (asyncTask.get() == EndBattleRoundResponses.CREATURE_NO_BATTLE_VALUE) {
                        Toast.makeText(MonsterFight.this, "ERROR. Cannot attack! Creature does not have battle value!", Toast.LENGTH_LONG).show();
                    } else { // WAITING_FOR_PLAYERS_TO_JOIN
                        Toast.makeText(MonsterFight.this, "ERROR. Cannot attack! Still waiting for player(s) to join the fight", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });




//        Button attack = findViewById(R.id.attack_btn);
//        attack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                int valuePlayer = randomDiceValue();
//                int playerDice1 = randomDiceValue();
//                int playerDice2 = randomDiceValue();
//                int playerDice3 = randomDiceValue();
//                int playerDice4 = randomDiceValue();
//
//                int monsterRoll1 = randomDiceValue();
//                int monsterRoll2 = randomDiceValue();
//                int monsterRoll3 = randomDiceValue();
//                int monsterRoll4 = randomDiceValue();
//
////                int valueMonster = randomDiceValue();
//
////                int res1 = getResources().getIdentifier("dice_" + valuePlayer, "drawable", "com.example.LegendsOfAndor");
////                int res2 = getResources().getIdentifier("dice_" + valueMonster, "drawable", "com.example.LegendsOfAndor");
//                final String dice1 = "dice_" + playerDice1;
//                final String dice2 = "dice_" + playerDice2;
//                final String dice3 = "dice_" + playerDice3;
//                final String dice4 = "dice_" + playerDice4;
//
////                imageView1.setImageResource(res1);
////                imageView2.setImageResource(res2);
//
//                imageDice1.setImageDrawable(getResources().getDrawable(getResourceID(dice1, "drawable", getApplicationContext())));
//                imageDice2.setImageDrawable(getResources().getDrawable(getResourceID(dice2, "drawable", getApplicationContext())));
//
//                playerBattleValue = findViewById(R.id.playerBattleValue);
//                monsterBattleValue = findViewById(R.id.monsterBattleValue);
//
//                int battleValueSum = Math.max(Math.max(playerDice1, playerDice2), Math.max(playerDice3, playerDice4));
//                //Strength points of hero need to be added to battleValueSum
//
//                int monsterValueSum = monsterRoll1 + monsterRoll2;
//                playerBattleValue.setText(Integer.toString(battleValueSum));
//                monsterBattleValue.setText(Integer.toString(monsterValueSum));
//
//
//            }
//        });
    }

    public void interruptThreadAndGoToBoard() {
        t.interrupt();

        try {
            LeaveFightSender leaveFightSender = new LeaveFightSender();
            leaveFightSender.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent myIntent = new Intent(MonsterFight.this, Board.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
        finish();
    }


    protected final static int getResourceID(final String resName, final String resType, final Context ctx) {
        final int ResourceID =
                ctx.getResources().getIdentifier(resName, resType,
                        ctx.getApplicationInfo().packageName);
        if (ResourceID == 0) {
            throw new IllegalArgumentException("No resource string found with name " + resName);
        }
        else {
            return ResourceID;
        }
    }

    private static class LeaveFightSender extends AsyncTask<String, Void, LeaveFightResponses> {
        @Override
        protected LeaveFightResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/leaveFight")
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, LeaveFightResponses.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class GetDiceSender extends AsyncTask<String, Void, ArrayList<Die>> {
        @Override
        protected ArrayList<Die> doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.get("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/getDice")
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, new TypeToken<ArrayList<Die>>() {}.getType());
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class CalculateBattleValueSender extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/calculateBattleValue")
                        .header("Content-Type", "application/json")
                        .body(strings[0])
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, Integer.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class GetCreatureDiceSender extends AsyncTask<String, Void, ArrayList<Die>> {
        @Override
        protected ArrayList<Die> doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.get("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() + "/" + myPlayer.getPlayer().getUsername() + "/getCreatureDice")
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, new TypeToken<ArrayList<Die>>() {}.getType());
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class CalculateCreatureBattleValueSender extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/calculateCreatureBattleValue")
                        .header("Content-Type", "application/json")
                        .body(strings[0])
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, Integer.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class EndBattleRoundSender extends AsyncTask<String, Void, EndBattleRoundResponses> {
        @Override
        protected EndBattleRoundResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/endBattleRound")
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, EndBattleRoundResponses.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
