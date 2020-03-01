package com.example.LegendsOfAndor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MonsterFight extends AppCompatActivity {

    public static final Random RANDOM = new Random();

    private Button getDice;
    private ImageView imageDice1, imageDice2, imageDice3, imageDice4;
   // private Button attack = findViewById(R.id.attack_btn); //CRASHES APP ON ACTIVITY STARTUP
    private TextView playerBattleValue;// = findViewById(R.id.playerBattleValue);
    private TextView monsterBattleValue;
    private ImageView archerDice1;
    private ImageView archerDice2;
    private ImageView archerDice3;
    private ImageView archerDice4;
    private ImageView archerDice5;
    private ImageView warriorDice1;
    private ImageView warriorDice2;
    private ImageView warriorDice3;
    private ImageView warriorDice4;
    private ImageView wizardDice1;
    private ImageView dwarfDice1;
    private ImageView dwarfDice2;
    private ImageView dwarfDice3;


    MyPlayer myPlayer = MyPlayer.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monster_fight);
        final HeroClass playerClass = myPlayer.getPlayer().getHero().getHeroClass();
        final Hero playerHero = myPlayer.getPlayer().getHero();
        final int playerPosition = myPlayer.getGame().getCurrentFight().getHeroes().indexOf(playerHero);

//        imageDice1 = (ImageView) findViewById(R.id.playerDice1);
//        imageDice2 = (ImageView) findViewById(R.id.playerDice2);
        warriorDice1.setVisibility(View.INVISIBLE);
        warriorDice2.setVisibility(View.INVISIBLE);
        warriorDice3.setVisibility(View.INVISIBLE);
        warriorDice4.setVisibility(View.INVISIBLE);
        archerDice1.setVisibility(View.INVISIBLE);
        archerDice2.setVisibility(View.INVISIBLE);
        archerDice3.setVisibility(View.INVISIBLE);
        archerDice4.setVisibility(View.INVISIBLE);
        archerDice5.setVisibility(View.INVISIBLE);


        getDice = findViewById(R.id.get_dice);

        //Retrieves the amount of dice for the current player class
        getDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int wp = myPlayer.getPlayer().getHero().getWillPower();
                if (playerClass == HeroClass.WARRIOR) {
                    if (wp <= 6) {

                    } else if (wp <= 13) {

                    } else if (wp >= 14) {

                    }
                } else if (playerClass == HeroClass.ARCHER) {
                    if (wp <= 6) {

                    } else if (wp <= 13) {

                    } else if (wp >= 14) {

                    }
                } else if (playerClass == HeroClass.DWARF) {
                    if (wp <= 6) {

                    } else if (wp <= 13) {

                    } else if (wp >= 14) {

                    }
                } else { //if WIZARD

                }
            }
        });

        Button attack = findViewById(R.id.attack_btn);
        attack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int valuePlayer = randomDiceValue();
                int playerDice1 = randomDiceValue();
                int playerDice2 = randomDiceValue();
                int playerDice3 = randomDiceValue();
                int playerDice4 = randomDiceValue();

                int monsterRoll1 = randomDiceValue();
                int monsterRoll2 = randomDiceValue();
                int monsterRoll3 = randomDiceValue();
                int monsterRoll4 = randomDiceValue();

//                int valueMonster = randomDiceValue();

//                int res1 = getResources().getIdentifier("dice_" + valuePlayer, "drawable", "com.example.LegendsOfAndor");
//                int res2 = getResources().getIdentifier("dice_" + valueMonster, "drawable", "com.example.LegendsOfAndor");
                final String dice1 = "dice_" + playerDice1;
                final String dice2 = "dice_" + playerDice2;
                final String dice3 = "dice_" + playerDice3;
                final String dice4 = "dice_" + playerDice4;

//                imageView1.setImageResource(res1);
//                imageView2.setImageResource(res2);

                imageDice1.setImageDrawable(getResources().getDrawable(getResourceID(dice1, "drawable", getApplicationContext())));
                imageDice2.setImageDrawable(getResources().getDrawable(getResourceID(dice2, "drawable", getApplicationContext())));

                playerBattleValue = findViewById(R.id.playerBattleValue);
                monsterBattleValue = findViewById(R.id.monsterBattleValue);

                int battleValueSum = Math.max(Math.max(playerDice1, playerDice2), Math.max(playerDice3, playerDice4));
                //Strength points of hero need to be added to battleValueSum

                int monsterValueSum = monsterRoll1 + monsterRoll2;
                playerBattleValue.setText(Integer.toString(battleValueSum));
                monsterBattleValue.setText(Integer.toString(monsterValueSum));


            }
        });
    }
    public static int randomDiceValue() {
        return RANDOM.nextInt(6) + 1;
    }

    protected final static int getResourceID(final String resName, final String resType, final Context ctx)
    {
        final int ResourceID =
                ctx.getResources().getIdentifier(resName, resType,
                        ctx.getApplicationInfo().packageName);
        if (ResourceID == 0)
        {
            throw new IllegalArgumentException
                    (
                            "No resource string found with name " + resName
                    );
        }
        else
        {
            return ResourceID;
        }
    }
}
