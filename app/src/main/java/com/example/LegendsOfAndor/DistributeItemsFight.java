package com.example.LegendsOfAndor;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

public class DistributeItemsFight extends AppCompatActivity {

    @Override
    public void onBackPressed(){}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distribute_items_fight);

        final Spinner warriorGold = findViewById(R.id.warrior_fight_gold);
        warriorGold.setVisibility(View.INVISIBLE);
        final Spinner archerGold = findViewById(R.id.archer_fight_gold);
        archerGold.setVisibility(View.INVISIBLE);
        final Spinner wizardGold = findViewById(R.id.wizard_fight_gold);
        wizardGold.setVisibility(View.INVISIBLE);
        final Spinner dwarfGold = findViewById(R.id.dwarf_fight_gold);
        dwarfGold.setVisibility(View.INVISIBLE);

        final Spinner warriorWillpower = findViewById(R.id.warrior_fight_willpower);
        warriorWillpower.setVisibility(View.INVISIBLE);
        final Spinner archerWillpower = findViewById(R.id.archer_fight_willpower);
        archerWillpower.setVisibility(View.INVISIBLE);
        final Spinner wizardWillpower = findViewById(R.id.wizard_fight_willpower);
        wizardWillpower.setVisibility(View.INVISIBLE);
        final Spinner dwarfWillpower = findViewById(R.id.dwarf_fight_willpower);
        dwarfWillpower.setVisibility(View.INVISIBLE);

        final TextView warrior_text = findViewById(R.id.warrior_fight);
        warrior_text.setVisibility(View.INVISIBLE);
        final TextView archer_text = findViewById(R.id.archer_fight);
        archer_text.setVisibility(View.INVISIBLE);
        final TextView dwarf_text = findViewById(R.id.dwarf_fight);
        dwarf_text.setVisibility(View.INVISIBLE);
        final TextView wizard_text = findViewById(R.id.wizard_fight);
        wizard_text.setVisibility(View.INVISIBLE);

        final Game currentGame = MyPlayer.getInstance().getGame();

        TextView description = findViewById(R.id.distribute_fight_items_description);
        description.setText(new StringBuilder().append("CONGRATULATIONS. You have won the fight. You get ").append(currentGame.getCurrentFight().getCreature().getGoldReward()).append(" gold and willpower to distribute amongst your team").toString());

        MyPlayer myPlayer = MyPlayer.getInstance();
        for(Hero heroInFight : myPlayer.getFightDistributionHeroes()){
            if(heroInFight.getHeroClass() == HeroClass.ARCHER){
                archerGold.setVisibility(View.VISIBLE);
                archerWillpower.setVisibility(View.VISIBLE);
                archer_text.setVisibility(View.VISIBLE);
            }else if(heroInFight.getHeroClass() == HeroClass.WARRIOR){
                warriorGold.setVisibility(View.VISIBLE);
                warriorWillpower.setVisibility(View.VISIBLE);
                warrior_text.setVisibility(View.VISIBLE);
            }else if(heroInFight.getHeroClass() == HeroClass.WIZARD){
                wizardGold.setVisibility(View.VISIBLE);
                wizardWillpower.setVisibility(View.VISIBLE);
                wizard_text.setVisibility(View.VISIBLE);
            }else {
                dwarfGold.setVisibility(View.VISIBLE);
                dwarfWillpower.setVisibility(View.VISIBLE);
                dwarf_text.setVisibility(View.VISIBLE);
            }
        }

        Button confirmDistribution = findViewById(R.id.finalize_fight_distribution);

        confirmDistribution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int war_gold = Integer.parseInt(warriorGold.getSelectedItem().toString());
                int arch_gold = Integer.parseInt(archerGold.getSelectedItem().toString());
                int wiz_gold = Integer.parseInt(wizardGold.getSelectedItem().toString());
                int dwarf_gold = Integer.parseInt(dwarfGold.getSelectedItem().toString());

                int wiz_willpower = Integer.parseInt(wizardWillpower.getSelectedItem().toString());
                int war_willpower = Integer.parseInt(warriorWillpower.getSelectedItem().toString());
                int arch_willpower = Integer.parseInt(archerWillpower.getSelectedItem().toString());
                int dwarf_willpower = Integer.parseInt(dwarfWillpower.getSelectedItem().toString());

                if((war_gold + arch_gold + wiz_gold + dwarf_gold + wiz_willpower + war_willpower + arch_willpower + dwarf_willpower) == currentGame.getCurrentFight().getCreature().getGoldReward()){
                    FightDistribution fightDistribution = new FightDistribution();
                    fightDistribution.setArcherGold(arch_gold);
                    fightDistribution.setArcherWillpower(arch_willpower);
                    fightDistribution.setDwarfGold(dwarf_gold);
                    fightDistribution.setDwarfWillpower(dwarf_willpower);
                    fightDistribution.setWarriorGold(war_gold);
                    fightDistribution.setWarriorWillpower(war_willpower);
                    fightDistribution.setWizardGold(wiz_gold);
                    fightDistribution.setWizardWillpower(wiz_willpower);
                    //SEND DISTRIBUTION TO SERVER

                    try {
                        DistributeAfterFightSender distributeAfterFightSender = new DistributeAfterFightSender();
                        distributeAfterFightSender.execute(new Gson().toJson(fightDistribution));

                        Toast.makeText(DistributeItemsFight.this, "Distribution successful. Going back to the board...", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(DistributeItemsFight.this, Board.class));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //IF DISTRIBUTION OK, GO BACK TO BOARD

                }else{
                    Toast.makeText(DistributeItemsFight.this, "Invalid distribution. Please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button chatb = findViewById(R.id.distribute_items_fight_chatb);
        chatb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key1", new Gson().toJson(PreviousPage.DISTRIBUTE_ITEMS_FIGHT));
                Intent intent = new Intent(DistributeItemsFight.this, ChatScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }

    private static class DistributeAfterFightSender extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();

            try {
                Unirest.post("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getPlayer().getUsername() + "/distributeAfterFight")
                        .header("Content-Type", "application/json")
                        .body(strings[0])
                        .asString();

            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
