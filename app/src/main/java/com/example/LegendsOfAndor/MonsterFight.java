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
    private Button rollDices;
    private ImageView imageDice1, imageDice2, imageDice3, imageDice4;
   // private Button attack = findViewById(R.id.attack_btn); //CRASHES APP ON ACTIVITY STARTUP
    private TextView playerBattleValue;// = findViewById(R.id.playerBattleValue);
    private TextView monsterBattleValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monster_fight);

        imageDice1 = (ImageView) findViewById(R.id.playerDice1);
        imageDice2 = (ImageView) findViewById(R.id.playerDice2);


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
