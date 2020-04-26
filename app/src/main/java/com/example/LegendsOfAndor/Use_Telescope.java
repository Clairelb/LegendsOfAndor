package com.example.LegendsOfAndor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.LegendsOfAndor.PublicEnums.ActivateFogResponses;
import com.example.LegendsOfAndor.PublicEnums.UseTelescopeResponses;
import com.example.LegendsOfAndor.ReturnClasses.ActivateFogRC;
import com.example.LegendsOfAndor.PublicEnums.FogKind;
import com.example.LegendsOfAndor.ReturnClasses.UseTelescopeRC;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;

public class Use_Telescope extends AppCompatActivity {

    @Override
    public void onBackPressed(){}

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.use_telescope);
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

        Game game = MyPlayer.getInstance().getGame();
        Hero hero = game.getSinglePlayer(MyPlayer.getInstance().getPlayer().getUsername()).getHero();

        final int current_space = hero.getCurrentSpace();
        TextView curReg = (TextView) findViewById(R.id.curReg);
        curReg.setText(String.valueOf(current_space));

        RegionDatabase database = game.getRegionDatabase();
        Region current_region = database.getRegion(current_space);
        ArrayList<Integer> adj_regions = current_region.getAdjacentRegions();
        ArrayList<String> adj_regions_fog = new ArrayList<String>();

        for (Integer region : adj_regions){
            Region reg = database.getRegion(region);
            FogKind fog = reg.getFog();
            ArrayList<RuneStone> runeStones = reg.getRuneStones();
            if (((fog != FogKind.NONE) && (!reg.isFogRevealed())) || (runeStones.size() > 0)){
                adj_regions_fog.add(region.toString());
            }
        }

        final Spinner spinner = findViewById(R.id.regions);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,adj_regions_fog);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button confirmb = (Button) findViewById(R.id.confirmb);
        confirmb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedItem() != null) {
                    Integer targetRegion = Integer.parseInt(String.valueOf(spinner.getSelectedItem()));
                    try {
                        AsyncTask<String, Void, UseTelescopeRC> asyncTask;
                        UseTelescopeSender useTelescopeSender = new UseTelescopeSender();
                        asyncTask = useTelescopeSender.execute(new Gson().toJson(targetRegion));
                        FogKind f = asyncTask.get().getFogKind();
                        ArrayList<RuneStone> runeStones = asyncTask.get().getRuneStones();
                        if (asyncTask.get().getUseTelescopeResponses() == UseTelescopeResponses.FOG_DNE) {
                            Toast.makeText(Use_Telescope.this, "There is no fog token located on region " + targetRegion.toString(), Toast.LENGTH_LONG).show();
                        } else if (asyncTask.get().getUseTelescopeResponses() == UseTelescopeResponses.MUST_END_MOVE_TELESCOPE) {
                            Toast.makeText(Use_Telescope.this, "You must end your move to use the telescope", Toast.LENGTH_LONG).show();
                        } else if (asyncTask.get().getUseTelescopeResponses() == UseTelescopeResponses.DOES_NOT_OWN_TELESCOPE) {
                            Toast.makeText(Use_Telescope.this, "You do not own a telescope", Toast.LENGTH_LONG).show();
                        }
                        else if (f != FogKind.NONE) {
                            if (f == FogKind.GOLD) {
                                Toast.makeText(Use_Telescope.this, "A 'Gold' fog token is located on region " + targetRegion.toString(), Toast.LENGTH_LONG).show();
                            } else if (f == FogKind.MONSTER) {
                                Toast.makeText(Use_Telescope.this, "A 'Monster' fog token is located on region " + targetRegion.toString(), Toast.LENGTH_LONG).show();
                            } else if (f == FogKind.THREE_WP) {
                                Toast.makeText(Use_Telescope.this, "A 'Three WillPower Points' fog token is located on region " + targetRegion.toString(), Toast.LENGTH_LONG).show();
                            } else if (f == FogKind.SP) {
                                Toast.makeText(Use_Telescope.this, "A 'Strength Point' fog token is located on region " + targetRegion.toString(), Toast.LENGTH_LONG).show();
                            } else if (f == FogKind.WITCHBREW) {
                                Toast.makeText(Use_Telescope.this, "A 'Witchbrew' fog token is located on region " + targetRegion.toString(), Toast.LENGTH_LONG).show();
                            } else if (f == FogKind.WINESKIN) {
                                Toast.makeText(Use_Telescope.this, "A 'Wineskin' fog token is located on region " + targetRegion.toString(), Toast.LENGTH_LONG).show();
                            } else if (f == FogKind.TWO_WP) {
                                Toast.makeText(Use_Telescope.this, "A 'Two WillPower Points' fog token is located on region " + targetRegion.toString(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Use_Telescope.this, "A 'Event Card' fog token is located on region " + targetRegion.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                        if (asyncTask.get().getUseTelescopeResponses() == UseTelescopeResponses.RUNE_STONE) {
                            for (RuneStone runeStone: runeStones) {
                                Colour colour = runeStone.getColour();
                                Toast.makeText(Use_Telescope.this, "A " + colour + " rune stone is located on region " + targetRegion.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(Use_Telescope.this, "There are no fog tokens or rune stones near you", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button backb = (Button) findViewById(R.id.backb);
        backb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), UseArticle.class);
                startActivity(myIntent);
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

    private static class UseTelescopeSender extends AsyncTask<String, Void, UseTelescopeRC> {
        @Override
        protected UseTelescopeRC doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() + "/" + myPlayer.getPlayer().getUsername() + "/useTelescope")
                        .header("Content-Type", "application/json")
                        .body(strings[0])
                        .asString();
                String resultAsJsonString = response.getBody();
                return new Gson().fromJson(resultAsJsonString, UseTelescopeRC.class );
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
