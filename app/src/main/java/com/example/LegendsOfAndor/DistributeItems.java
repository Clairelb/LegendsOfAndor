package com.example.LegendsOfAndor;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DistributeItems extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distributeitemspage);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        final Spinner warriorGold = findViewById(R.id.warriorGold);
        final Spinner archerGold = findViewById(R.id.archerGold);
        final Spinner wizardGold = findViewById(R.id.wizardGold);
        final Spinner dwarfGold = findViewById(R.id.dwarfGold);

        final Spinner warriorWineskin = findViewById(R.id.warriorWineskin);
        final Spinner archerWineskin = findViewById(R.id.archerWineskin);
        final Spinner wizardWineskin = findViewById(R.id.wizardWineskin);
        final Spinner dwarfWineskin = findViewById(R.id.dwarfWineskin);

        Button confirmDistribution = findViewById(R.id.finalizeDistribution);

        confirmDistribution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int war_gold = Integer.parseInt(warriorGold.getSelectedItem().toString());
                int arch_gold = Integer.parseInt(archerGold.getSelectedItem().toString());
                int wiz_gold = Integer.parseInt(wizardGold.getSelectedItem().toString());
                int dwarf_gold = Integer.parseInt(dwarfGold.getSelectedItem().toString());

                int wiz_wineskin = Integer.parseInt(wizardWineskin.getSelectedItem().toString());
                int war_wineskin = Integer.parseInt(warriorWineskin.getSelectedItem().toString());
                int arch_wineskin = Integer.parseInt(archerWineskin.getSelectedItem().toString());
                int dwarf_wineskin = Integer.parseInt(dwarfWineskin.getSelectedItem().toString());

                if((war_gold + arch_gold + wiz_gold + dwarf_gold == 5) && (wiz_wineskin + war_wineskin + arch_wineskin + dwarf_wineskin == 2)){
                    Toast.makeText(DistributeItems.this, "Valid item distribution. Welcome to the game", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(DistributeItems.this, Board.class));
                }else{
                    Toast.makeText(DistributeItems.this, "Error. Invalid item distribution", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

}
