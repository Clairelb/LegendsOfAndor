package com.example.LegendsOfAndor;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.LegendsOfAndor.PublicEnums.*;
import com.example.LegendsOfAndor.PublicEnums.FogKind;
import com.example.LegendsOfAndor.ReturnClasses.ActivateFogRC;
import com.example.LegendsOfAndor.ReturnClasses.GetPossibleCreaturesToFightRC;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;


import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

enum PassResponses {
    PASS_SUCCESSFUL, MUST_END_DAY, ONLY_PLAYER_LEFT, NOT_CURRENT_TURN, DAY_ENDED, PASS_SUCCESSFUL_WP_DEDUCTED
}

enum EndDayResponses {
    DAY_ALREADY_ENDED, NOT_CURRENT_TURN, NEW_DAY, GAME_OVER, SUCCESS
}

enum MoveResponses {
    PICK_UP_FARMER, FARMERS_DIED, NO_OTHER_ACTIONS
}
enum EndMoveResponses {
    BUY_FROM_MERCHANT, EMPTY_WELL, BUY_WITCH_BREW, ACTIVATE_FOG, MOVE_ALREADY_ENDED, MUST_MOVE_TO_END_MOVE, NONE
}


enum GetAvailableRegionsReponses {
    NOT_CURRENT_TURN, DEDUCT_WILLPOWER, NOT_ENOUGH_WILLPOWER, CURRENT_HOUR_MAXED, CANNOT_MOVE_AFTER_FIGHT, CANNOT_MOVE_AFTER_MOVE_PRINCE, SUCCESS
}

enum GetPrinceThoraldMovesResponses {
    PRINCE_DNE, NOT_CURRENT_TURN, DEDUCT_WILLPOWER, NOT_ENOUGH_WILLPOWER, CURRENT_HOUR_MAXED, CANNOT_MOVE_PRINCE_AFTER_FIGHT, CANNOT_MOVE_PRINCE_AFTER_MOVE, SUCCESS
}

enum EndMovePrinceThoraldResponses {
    MUST_MOVE_PRINCE_TO_END_MOVE, MOVE_PRINCE_ALREADY_ENDED, SUCCESS
}



//import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class Board extends AppCompatActivity {
    private ImageView archer;
    private ImageView wizard;
    private ImageView dwarf;
    private ImageView warrior;
    private Button move;
    private Button fight;
    private Button pass;
    private Button endDay;
    private Button chatb;
    private Button optionsb;
    private Button endMove;
    private int nextMove;
    private Button realMove;

    private Button getDirectionPrince;
    private Button confirmMovePrince;
    private Button endMovePrince;
    private Spinner princeRegions;
    private ArrayAdapter<String> adapterPrince;
    private ArrayList<String> listPrince=new ArrayList<String>();
    private int PrinceNextMove;





    private Thread t;
    private boolean threadTerminated = false;

    private boolean flag;

    //private RegionDatabase regionDatabase;
    private ArrayList<String> list=new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private HashMap<Integer, Integer[]> hourLocation = new HashMap<>();
    private ArrayList<ImageView> farmers = new ArrayList<>();
    private ArrayList<ImageView> gors = new ArrayList<>();
    private ArrayList<ImageView> skrall = new ArrayList<>();
    private ArrayList<ImageView> wells = new ArrayList<>();
    private ArrayList<ImageView> emptyWells = new ArrayList<>();
    private ArrayList<ImageView> theros = new ArrayList<>();
    private ImageView skrall_boss;
    private ArrayList<ImageView> wardraks = new ArrayList<>();
    private ImageView narrator;
    private ImageView prince;
    private ImageView witch;

    private Spinner sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //regionDatabase = new RegionDatabase();
        flag = false;
        setContentView(R.layout.board);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        final MyPlayer myPlayer = MyPlayer.getInstance();
//        move = findViewById(R.id.move);
//        move.setVisibility(View.INVISIBLE);
        realMove = findViewById(R.id.realMove);
        realMove.setVisibility(View.INVISIBLE);
        fight = findViewById(R.id.fight);
        fight.setVisibility(View.INVISIBLE);
        pass = findViewById(R.id.pass);
        pass.setVisibility(View.INVISIBLE);
        endDay = findViewById(R.id.endDay);
        endDay.setVisibility(View.INVISIBLE);
        endMove = findViewById(R.id.endMove);
        endMove.setVisibility(View.INVISIBLE);

        chatb= findViewById(R.id.chatb);
        optionsb = findViewById(R.id.optionsb);

        if (myPlayer.isFoundWitch()) {
            myPlayer.setFoundWitch(false);

            Intent intent = new Intent(Board.this, BuyWitchBrewOptions.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            interruptThreadAndStartActivity(intent);
        }


        farmers.add((ImageView)findViewById(R.id.farmer0));
        farmers.add((ImageView)findViewById(R.id.farmer1));
        farmers.add((ImageView)findViewById(R.id.farmer2));
        farmers.add((ImageView)findViewById(R.id.farmer3));

        gors.add((ImageView) findViewById(R.id.gor0));
        gors.add((ImageView) findViewById(R.id.gor1));
        gors.add((ImageView) findViewById(R.id.gor2));
        gors.add((ImageView) findViewById(R.id.gor3));
        gors.add((ImageView) findViewById(R.id.gor4));
        gors.add((ImageView) findViewById(R.id.gor5));
        gors.add((ImageView) findViewById(R.id.gor6));
        gors.add((ImageView) findViewById(R.id.gor7));
        gors.add((ImageView) findViewById(R.id.gor8));
        gors.add((ImageView) findViewById(R.id.gor9));
        gors.add((ImageView) findViewById(R.id.gor10));
        gors.add((ImageView) findViewById(R.id.gor11));
        gors.add((ImageView) findViewById(R.id.gor12));
        gors.add((ImageView) findViewById(R.id.gor13));
        gors.add((ImageView) findViewById(R.id.gor14));
        gors.add((ImageView) findViewById(R.id.gor15));
        for(int i = 0; i < gors.size();i++){
            gors.get(i).setVisibility(View.INVISIBLE);
        }





        skrall.add((ImageView) findViewById(R.id.skrall0));
        skrall.add((ImageView) findViewById(R.id.skrall1));
        skrall.add((ImageView) findViewById(R.id.skrall2));
        skrall.add((ImageView) findViewById(R.id.skrall3));
        for(int i = 0 ; i < skrall.size();i++){
            skrall.get(i).setVisibility(View.INVISIBLE);
        }

        skrall_boss = findViewById(R.id.skrall_boss);
        skrall_boss.setVisibility(View.INVISIBLE);

        wardraks.add((ImageView) findViewById(R.id.wardrak0));
        wardraks.add((ImageView) findViewById(R.id.wardrak1));
        for(int i = 0 ; i < wardraks.size();i++){
            wardraks.get(i).setVisibility(View.INVISIBLE);
        }


        //narrator
        narrator = (ImageView)findViewById(R.id.narrator);

        //prince
        prince = (ImageView)findViewById((R.id.prince));
        prince.setVisibility(View.INVISIBLE);

        //witch
        witch = (ImageView)findViewById(R.id.witch);

        wells.add((ImageView)findViewById(R.id.well5));
        wells.add((ImageView)findViewById(R.id.well35));
        wells.add((ImageView)findViewById(R.id.well45));
        wells.add((ImageView)findViewById(R.id.well55));
        emptyWells.add((ImageView)findViewById(R.id.emptywell5));
        emptyWells.add((ImageView)findViewById(R.id.emptywell35));
        emptyWells.add((ImageView)findViewById(R.id.emptywell45));
        emptyWells.add((ImageView)findViewById(R.id.emptywell55));

        wells.get(0).setX(59);
        wells.get(0).setY(345);
        wells.get(1).setX(232);
        wells.get(1).setY(753);
        wells.get(2).setX(1271);
        wells.get(2).setY(536);
        wells.get(3).setX(1106);
        wells.get(3).setY(103);
        emptyWells.get(0).setX(59);
        emptyWells.get(0).setY(345);
        emptyWells.get(1).setX(232);
        emptyWells.get(1).setY(753);
        emptyWells.get(2).setX(1271);
        emptyWells.get(2).setY(536);
        emptyWells.get(3).setX(1106);
        emptyWells.get(3).setY(103);

        theros.add((ImageView)findViewById(R.id.twarrior));
        theros.get(0).setVisibility(View.INVISIBLE);
        theros.add((ImageView)findViewById(R.id.tarcher_male));
        theros.get(1).setVisibility(View.INVISIBLE);
        theros.add((ImageView)findViewById(R.id.tdwarf_male));
        theros.get(2).setVisibility(View.INVISIBLE);
        theros.add((ImageView)findViewById(R.id.tmage_male));
        theros.get(3).setVisibility(View.INVISIBLE);

        for(int i = 0; i < myPlayer.getGame().getAllHeroes().size();i++){
            if(myPlayer.getGame().getAllHeroes().get(i) == HeroClass.WIZARD){
                theros.get(3).setVisibility(View.VISIBLE);
            }
            if(myPlayer.getGame().getAllHeroes().get(i) == HeroClass.DWARF){
                theros.get(2).setVisibility(View.VISIBLE);
            }
            if(myPlayer.getGame().getAllHeroes().get(i) == HeroClass.ARCHER){
                theros.get(1).setVisibility(View.VISIBLE);
            }
            if(myPlayer.getGame().getAllHeroes().get(i) == HeroClass.WARRIOR){
                theros.get(0).setVisibility(View.VISIBLE);
            }
        }


        final TextView spText = findViewById(R.id.spText);
        spText.setVisibility(View.INVISIBLE);

        sp=(Spinner)findViewById(R.id.sp);
        sp.bringToFront();
        String[]ls=getResources().getStringArray(R.array.action);

        for(int i=0;i<ls.length;i++){
            list.add(ls[i]);
        }

        endMovePrince = findViewById(R.id.endMovePrince);
        endMovePrince.setVisibility(View.INVISIBLE);

        confirmMovePrince =findViewById(R.id.movePrinceConfirm);
        confirmMovePrince.setVisibility(View.INVISIBLE);

        getDirectionPrince = findViewById(R.id.getRegionPrince);
        getDirectionPrince.setVisibility(View.INVISIBLE);

        princeRegions = findViewById(R.id.spPrinceRegion);
        princeRegions.setVisibility(View.INVISIBLE);



        confirmMovePrince.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                try {
                    AsyncTask<String, Void, Void> asyncTask;
                    MovePrinceSender movePrinceSender = new MovePrinceSender();
                    asyncTask = movePrinceSender.execute(new Gson().toJson(PrinceNextMove));
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        realMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    AsyncTask<String, Void, MoveRC> asyncTask;
                    MoveRC moveRC;

                    MoveSender moveSender = new MoveSender();
                    asyncTask = moveSender.execute(new Gson().toJson(nextMove));
                    Log.d("CHECK", "here");
                    moveRC = asyncTask.get();
                    if (moveRC.getMoveResponses() == MoveResponses.PICK_UP_FARMER){
                        Toast.makeText(Board.this, "You can pick up a farmer at this region", Toast.LENGTH_LONG).show();
                    } else if (moveRC.getMoveResponses() == MoveResponses.FARMERS_DIED){
                        Toast.makeText(Board.this, "The farmer you were previous carrying died", Toast.LENGTH_LONG).show();
                    }

                    AsyncTask<String, Void, GetAvailableRegionsRC> asyncTask1;
                    GetRegionsSender getRegionsSender = new GetRegionsSender();
                    GetAvailableRegionsReponses getAvailableRegionsReponses;

                    asyncTask1 = getRegionsSender.execute();
                    GetAvailableRegionsRC availableRegions = asyncTask1.get();
                    Log.d("REGION", availableRegions.getResponse().toString());

                    adapter.clear();

                    ArrayList<Integer> available = availableRegions.getRegions();
                    for(Integer i: available){
                        adapter.add(i.toString());
                    }
                    adapter.notifyDataSetChanged();
                    sp.setAdapter(adapter);

                }catch(Exception e){
                    e.printStackTrace();
                }
                moveHero(myPlayer.getPlayer().getHero(),nextMove);

            }
        });
        //Prince spinner

        adapterPrince = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        adapterPrince.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        princeRegions.setAdapter((adapterPrince));
        princeRegions.getSelectedItem();

        princeRegions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer space = Integer.parseInt(adapter.getItem(position));
                PrinceNextMove = space;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp.setAdapter(adapter);
        sp.getSelectedItem();
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                Integer space = Integer.parseInt(adapter.getItem(position));

//                try{
//                    AsyncTask<String, Void, MoveRC> asyncTask;
//                    MoveRC moveRC;
//
//                    MoveSender moveSender = new MoveSender();
//                    asyncTask = moveSender.execute(new Gson().toJson(space));
//                    Log.d("CHECK", "here");
//                    moveRC = asyncTask.get();
//                    if (moveRC.getMoveResponses() == MoveResponses.PICK_UP_FARMER){
//                        Toast.makeText(Board.this, "You can pick up a farmer at this region", Toast.LENGTH_LONG).show();
//                    }
//
//                    if(moveRC.getMoveResponses() == MoveResponses.FARMERS_DIED){
//                        Toast.makeText(Board.this, "The farmer you were previous carrying died", Toast.LENGTH_LONG);
//                    }
//
//                    if(moveRC.getMoveResponses() == MoveResponses.NO_OTHER_ACTIONS){
//                        Toast.makeText(Board.this, "Please make your next move or end your move", Toast.LENGTH_LONG);
//                    }
//                }catch(Exception e){
//                    e.printStackTrace();
//                }
                nextMove = space;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp.setVisibility(View.VISIBLE);
        //final Toolbar toolbar2 = findViewById(R.id.toolbar2);
        //toolbar2.setVisibility(View.INVISIBLE);

        hourLocation.put(0,new Integer[]{644,15});
        hourLocation.put(1,new Integer[]{892,15});
        hourLocation.put(2,new Integer[]{971,15});
        hourLocation.put(3,new Integer[]{1042,15});
        hourLocation.put(4,new Integer[]{1122,15});
        hourLocation.put(5,new Integer[]{1198,15});
        hourLocation.put(6,new Integer[]{1269,15});
        hourLocation.put(7,new Integer[]{1345,15});
        hourLocation.put(8,new Integer[]{1447,15});
        hourLocation.put(9,new Integer[]{1527,15});
        hourLocation.put(10,new Integer[]{1603,15});

        this.archer = findViewById(R.id.archer_male);
        this.archer.setVisibility(View.INVISIBLE);
        this.wizard = findViewById(R.id.mage_male);
        this.wizard.setVisibility(View.INVISIBLE);
        this.dwarf  = findViewById(R.id.dwarf_male);
        this.dwarf.setVisibility(View.INVISIBLE);
        this.warrior = findViewById(R.id.warrior);
        this.warrior.setVisibility(View.INVISIBLE);
        for(int i = 0; i < myPlayer.getGame().getAllHeroes().size();i++){
            if(myPlayer.getGame().getAllHeroes().get(i) == HeroClass.WIZARD){
                this.wizard.setVisibility(View.VISIBLE);
            }
            if(myPlayer.getGame().getAllHeroes().get(i) == HeroClass.DWARF){
                this.dwarf.setVisibility(View.VISIBLE);
            }
            if(myPlayer.getGame().getAllHeroes().get(i) == HeroClass.ARCHER){
                this.archer.setVisibility(View.VISIBLE);
            }
            if(myPlayer.getGame().getAllHeroes().get(i) == HeroClass.WARRIOR){
                this.warrior.setVisibility(View.VISIBLE);
            }
        }



        try{
            AsyncTask<String, Void, Game> asyncTask;
            Game gameToSet;
            GetGame getGame = new GetGame();
            asyncTask = getGame.execute();
            gameToSet = asyncTask.get();
            System.out.println(gameToSet);
            myPlayer.setGame(gameToSet);
            for(int i = 0; i < gameToSet.getCurrentNumPlayers(); i++){
                if(gameToSet.getPlayers()[i].getUsername().equals(myPlayer.getPlayer().getUsername())){
                    myPlayer.setPlayer(gameToSet.getPlayers()[i]);
                    System.out.println("SET PLAYER");
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Game currentGame = myPlayer.getGame();
        if(currentGame.getCurrentHero().getHeroClass() == myPlayer.getPlayer().getHero().getHeroClass()){
            Toast.makeText(Board.this,"It is your turn to go first", Toast.LENGTH_LONG).show();
            realMove.setVisibility(View.VISIBLE);
//            move.setVisibility(View.VISIBLE);
            fight.setVisibility(View.VISIBLE);
            pass.setVisibility(View.VISIBLE);
            endDay.setVisibility(View.VISIBLE);
            endMove.setVisibility(View.VISIBLE);

        }

        if (!myPlayer.isRuneStoneToastDisplayed()) {
            myPlayer.setRuneStoneToastDisplayed(true);
            Toast.makeText(Board.this, "The rune stones legend card appears at " + currentGame.getRuneStoneLegendCard().toString() + " space.", Toast.LENGTH_LONG).show();
        }

        for(int i = 0; i < currentGame.getCurrentNumPlayers(); i++) {
            //DRAW PLAYERS HERE
            Hero h = currentGame.getPlayers()[i].getHero();
            Integer s = h.getCurrentSpace();
            moveHero(h, s);

            //DRAW TIME MARKERS HERE
            if(h.isHasEndedDay())
            {
                moveHeroTime(h, hourLocation.get(0)[0],hourLocation.get(0)[1]);
            }else{
                int time = h.getCurrentHour();
                moveHeroTime(h, hourLocation.get(time)[0],hourLocation.get(time)[1]);
            }
        }


        ArrayList<Region> mRegion = currentGame.getRegionDatabase().getAllRegionsWithCreatures();
        ArrayList<Integer> gorRegion = new ArrayList<>();
        ArrayList<Integer> skralRegion = new ArrayList<>();

        for(Region r: mRegion){
            if(r.getCurrentCreatures().get(0).getCreatureType() == CreatureType.GOR){
                gorRegion.add(r.getNumber());
            }
            if(r.getCurrentCreatures().get(0).getCreatureType() == CreatureType.SKRAL){
                skralRegion.add(r.getNumber());
            }
        }
        System.out.println("total"+ mRegion.size());
        System.out.println("sieze" + gorRegion.size());
        for(int i = 0; i <gorRegion.size();i++){
            gors.get(i).setVisibility(View.VISIBLE);
            moveMonster(gors.get(i),gorRegion.get(i));
        }
        for(int i = 0; i < skralRegion.size();i++){
            skrall.get(i).setVisibility((View.VISIBLE));
            moveMonster(skrall.get(i), skralRegion.get(i));
        }

        //Update Wells
        if(currentGame.getRegionDatabase().getRegion(5).isFountainStatus())
        {
            wells.get(0).setVisibility(View.VISIBLE);
            emptyWells.get(0).setVisibility(View.INVISIBLE);
        }else{
            wells.get(0).setVisibility(View.INVISIBLE);
            emptyWells.get(0).setVisibility(View.VISIBLE);
        }
        if(currentGame.getRegionDatabase().getRegion(35).isFountainStatus())
        {
            wells.get(1).setVisibility(View.VISIBLE);
            emptyWells.get(1).setVisibility(View.INVISIBLE);
        }else{
            wells.get(1).setVisibility(View.INVISIBLE);
            emptyWells.get(1).setVisibility(View.VISIBLE);
        }
        if(currentGame.getRegionDatabase().getRegion(45).isFountainStatus())
        {
            wells.get(2).setVisibility(View.VISIBLE);
            emptyWells.get(2).setVisibility(View.INVISIBLE);
        }else{
            wells.get(2).setVisibility(View.INVISIBLE);
            emptyWells.get(2).setVisibility(View.VISIBLE);
        }
        if(currentGame.getRegionDatabase().getRegion(55).isFountainStatus())
        {
            wells.get(3).setVisibility(View.VISIBLE);
            emptyWells.get(3).setVisibility(View.INVISIBLE);
        }else{
            wells.get(3).setVisibility(View.INVISIBLE);
            emptyWells.get(3).setVisibility(View.VISIBLE);
        }

        //narrators
        NarratorSpace narratorSpace = myPlayer.getGame().getNarrator().getSlot();
        HashMap<NarratorSpace,Integer[]> narratorSpaceHashMap = myPlayer.getGame().getNarrator().getMap();
        Integer[] narratorCoor = narratorSpaceHashMap.get(narratorSpace);
        movePic(narrator, narratorCoor[0], narratorCoor[1]);

        ArrayList<Integer> toDrawFarmer1 = new ArrayList<>();
        ArrayList<Region> regionList1 = myPlayer.getGame().getRegionDatabase().getRegionDatabase();
        for(int i = 0; i < regionList1.size();i++){
            int num = regionList1.get(i).getFarmers().size();
            for(int j = 0; j < num; j++){
                if(!regionList1.get(i).getFarmers().get(j).isBeingCarried){
                    toDrawFarmer1.add(regionList1.get(i).getNumber());
                }
            }
        }
        for(int i = 0 ; i < farmers.size();i++){
            farmers.get(i).setVisibility(View.INVISIBLE);
        }

        for(int i = 0; i < toDrawFarmer1.size();i ++){
            farmers.get(i).setVisibility(View.VISIBLE)  ;
            displayFarmer(farmers.get(i), toDrawFarmer1.get(i));
        }
        Witch temp = myPlayer.getGame().getWitch();
        if(temp!= null){
            witch.setVisibility(View.VISIBLE);
            displayFarmer(witch, temp.getCurrentPosition());
        }else{
            witch.setVisibility(View.INVISIBLE);
        }

        AsyncTask<String, Void, GetAvailableRegionsRC> asyncTask1;
        GetRegionsSender getRegionsSender = new GetRegionsSender();
        GetAvailableRegionsReponses getAvailableRegionsReponses;
        try {
            asyncTask1 = getRegionsSender.execute();
            GetAvailableRegionsRC availableRegions = asyncTask1.get();
            Log.d("REGION", availableRegions.getResponse().toString());

            adapter.clear();

            ArrayList<Integer> available = availableRegions.getRegions();
            for (Integer i : available) {
                adapter.add(i.toString());
            }
            adapter.notifyDataSetChanged();
            sp.setAdapter(adapter);
        }catch(Exception e){
            e.printStackTrace();
        }




        t = new Thread(new Runnable() { // add logic that if game is active go to game board and end the thread
            @Override
            public void run() {
                final MyPlayer myPlayer = MyPlayer.getInstance();
                while (!threadTerminated) {
                    try {
                        final HttpResponse<String> response = Unirest.get("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getPlayer().getUsername() + "/getGameUpdate")
                                .asString();

                        if(response.getCode() == 200){
                            final Game game = new Gson().fromJson(response.getBody(), Game.class);
                            if (game != null) {
                                MyPlayer.getInstance().setGame(game);
                            }
                            runOnUiThread(new Runnable() { // cannot run this part on seperate thread, so this forces the following to run on UiThread
                                @Override
                                public void run() {
                                    if (game.isLeftGame()) {
                                        Toast.makeText(Board.this,"Someone left the game. Going back to the join/create game screen...", Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(Board.this, CreateGame.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        interruptThreadAndStartActivity(intent);
                                    }

                                    if (game.getGameStatus() == GameStatus.GAME_WON || game.getGameStatus() == GameStatus.GAME_LOST) {
                                        Intent intent = new Intent(Board.this, LegendCardN.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        interruptThreadAndStartActivity(intent);
                                    }

                                    if(game.getGoldenShields() <= 0){
                                        myPlayer.getGame().setGameStatus(GameStatus.GAME_LOST);
                                        Intent gameOverIntent = new Intent(Board.this, GameOver.class);
                                        gameOverIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                        interruptThreadAndStartActivity(gameOverIntent);
                                    }else{
                                        if (game.isFoundWitch()) {
                                            if (!myPlayer.isLegendCardTheWitchDisplayed()) {
                                                myPlayer.setLegendCardTheWitchDisplayed(true);

                                                Intent intent = new Intent(Board.this, LegendCardTheWitch.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                interruptThreadAndStartActivity(intent);
                                            }
                                        }

                                        if (game.getNarrator().getSlot() == NarratorSpace.C) {
                                            if (!myPlayer.isLegendCardCDisplayed()) {
                                                myPlayer.setLegendCardCDisplayed(true);
                                                Intent intent;
                                                if (game.getDifficultMode()) {
                                                    intent = new Intent(Board.this, LegendCardC1Hard.class);
                                                } else {
                                                    intent = new Intent(Board.this, LegendCardC1Easy.class);
                                                }
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                interruptThreadAndStartActivity(intent);
                                            }
                                        } else if (game.getNarrator().getSlot() == game.getRuneStoneLegendCard()) {
                                            if (!myPlayer.isLegendCardRuneStonesDisplayed()) {
                                                myPlayer.setLegendCardRuneStonesDisplayed(true);
                                                Intent intent;
                                                if (game.getDifficultMode()) {
                                                    intent = new Intent(Board.this, LegendCardRuneStonesHard.class);
                                                } else {
                                                    intent = new Intent(Board.this, LegendCardRuneStonesEasy.class);
                                                }
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                interruptThreadAndStartActivity(intent);
                                            }
                                        } else if (game.getNarrator().getSlot() == NarratorSpace.G) {
                                            if (!myPlayer.isLegendCardGDisplayed()) {
                                                myPlayer.setLegendCardGDisplayed(true);
                                                Intent intent = new Intent(Board.this, LegendCardG.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                interruptThreadAndStartActivity(intent);
                                            }
                                        } else if (game.getNarrator().getSlot() == NarratorSpace.N) {
                                            if (!myPlayer.isLegendCardNDisplayed()) {
                                                myPlayer.setLegendCardNDisplayed(true);
                                                if (myPlayer.getPlayer().getUsername().equals(myPlayer.getGame().getPlayers()[0].getUsername())) {
                                                    try {
                                                        ActivateLegendCardN activateLegendCardN = new ActivateLegendCardN();
                                                        activateLegendCardN.execute("");
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        }

                                        for(int i = 0; i < game.getCurrentNumPlayers(); i++) {
                                            //DRAW PLAYERS HERE
                                            Hero h = game.getPlayers()[i].getHero();
                                            Integer s = h.getCurrentSpace();
                                            moveHero(h, s);

                                            //DRAW TIME MARKERS HERE
                                            if(h.isHasEndedDay())
                                            {
                                                moveHeroTime(h, hourLocation.get(0)[0],hourLocation.get(0)[1]);
                                            }else{
                                                int time = h.getCurrentHour();
                                                moveHeroTime(h, hourLocation.get(time)[0],hourLocation.get(time)[1]);
                                            }
                                        }

                                        ArrayList<Integer> toDrawFarmer = new ArrayList<>();
                                        ArrayList<Region> regionList = myPlayer.getGame().getRegionDatabase().getRegionDatabase();
                                        for(int i = 0; i < regionList.size();i++){
                                            int num = regionList.get(i).getFarmers().size();
                                            for(int j = 0; j < num; j++){
                                                if(!regionList.get(i).getFarmers().get(j).isBeingCarried){
                                                    toDrawFarmer.add(regionList.get(i).getNumber());
                                                }
                                            }
                                        }
                                        for(int i = 0 ; i < farmers.size();i++){
                                            farmers.get(i).setVisibility(View.INVISIBLE);
                                        }

                                        for(int i = 0; i < toDrawFarmer.size();i ++){
                                            farmers.get(i).setVisibility(View.VISIBLE)  ;
                                            displayFarmer(farmers.get(i), toDrawFarmer.get(i));
                                        }


                                        ArrayList<Region> mRegion = game.getRegionDatabase().getAllRegionsWithCreatures();
                                        ArrayList<Integer> gorRegion = new ArrayList<>();
                                        ArrayList<Integer> skralRegion = new ArrayList<>();
                                        ArrayList<Integer> wardrakRegion = new ArrayList<>();

                                        for(Region r: mRegion){
                                            if(r.getCurrentCreatures().get(0).getCreatureType() == CreatureType.GOR){
                                                gorRegion.add(r.getNumber());
                                            }
                                            if(r.getCurrentCreatures().get(0).getCreatureType() == CreatureType.SKRAL){
                                                skralRegion.add(r.getNumber());
                                            }
                                            if(r.getCurrentCreatures().get(0).getCreatureType() == CreatureType.SKRAL_BOSS){
                                                skrall_boss.setVisibility(View.VISIBLE);
                                                moveMonster(skrall_boss,r.getNumber());
                                            }
                                            if(r.getCurrentCreatures().get(0).getCreatureType() == CreatureType.WARDRAKS){
                                                wardrakRegion.add(r.getNumber());
                                            }
                                        }
                                        System.out.println("total"+ mRegion.size());
                                        System.out.println("size" + gorRegion.size());
                                        for(int i = 0; i <gorRegion.size();i++){
                                            gors.get(i).setVisibility(View.VISIBLE);
                                            moveMonster(gors.get(i),gorRegion.get(i));
                                        }
                                        for(int i = 0; i < skralRegion.size();i++){
                                            skrall.get(i).setVisibility((View.VISIBLE));
                                            moveMonster(skrall.get(i), skralRegion.get(i));
                                        }
                                        for(int i = 0; i < wardrakRegion.size();i++){
                                            wardraks.get(i).setVisibility(View.VISIBLE);
                                            moveMonster(wardraks.get(i),wardrakRegion.get(i));
                                        }
                                        //prince
                                        if(myPlayer.getGame().getPrinceThorald() != null){
                                            displayFarmer(prince, myPlayer.getGame().getPrinceThorald().currentPosition);
                                        }
                                        //witch

                                        Witch temp = myPlayer.getGame().getWitch();
                                        if(temp!= null){
                                            witch.setVisibility(View.VISIBLE);
                                            displayFarmer(witch, temp.getCurrentPosition());
                                        }else{
                                            witch.setVisibility(View.INVISIBLE);
                                        }

                                        //narrators
                                        NarratorSpace narratorSpace = myPlayer.getGame().getNarrator().getSlot();
                                        HashMap<NarratorSpace,Integer[]> narratorSpaceHashMap = myPlayer.getGame().getNarrator().getMap();
                                        Integer[] narratorCoor = narratorSpaceHashMap.get(narratorSpace);
                                        movePic(narrator, narratorCoor[0], narratorCoor[1]);
                                        //Update Wells
                                        if(game.getRegionDatabase().getRegion(5).isFountainStatus())
                                        {
                                            wells.get(0).setVisibility(View.VISIBLE);
                                            emptyWells.get(0).setVisibility(View.INVISIBLE);
                                        }else{
                                            wells.get(0).setVisibility(View.INVISIBLE);
                                            emptyWells.get(0).setVisibility(View.VISIBLE);
                                        }
                                        if(game.getRegionDatabase().getRegion(35).isFountainStatus())
                                        {
                                            wells.get(1).setVisibility(View.VISIBLE);
                                            emptyWells.get(1).setVisibility(View.INVISIBLE);
                                        }else{
                                            wells.get(1).setVisibility(View.INVISIBLE);
                                            emptyWells.get(1).setVisibility(View.VISIBLE);
                                        }
                                        if(game.getRegionDatabase().getRegion(45).isFountainStatus())
                                        {
                                            wells.get(2).setVisibility(View.VISIBLE);
                                            emptyWells.get(2).setVisibility(View.INVISIBLE);
                                        }else{
                                            wells.get(2).setVisibility(View.INVISIBLE);
                                            emptyWells.get(2).setVisibility(View.VISIBLE);
                                        }
                                        if(game.getRegionDatabase().getRegion(55).isFountainStatus())
                                        {
                                            wells.get(3).setVisibility(View.VISIBLE);
                                            emptyWells.get(3).setVisibility(View.INVISIBLE);
                                        }else{
                                            wells.get(3).setVisibility(View.INVISIBLE);
                                            emptyWells.get(3).setVisibility(View.VISIBLE);
                                        }

                                        if(game.getCurrentFight() != null){
                                            for(Hero h : game.getCurrentFight().getPendingInvitedHeroes()){
                                                if(h.getHeroClass() == myPlayer.getPlayer().getHero().getHeroClass()){
                                                    realMove.setVisibility(View.INVISIBLE);
//                                                    move.setVisibility(View.INVISIBLE);
                                                    fight.setVisibility(View.INVISIBLE);
                                                    pass.setVisibility(View.INVISIBLE);
                                                    endDay.setVisibility(View.INVISIBLE);
                                                    endMove.setVisibility(View.INVISIBLE);
                                                    confirmMovePrince.setVisibility(View.INVISIBLE);
                                                    getDirectionPrince.setVisibility(View.INVISIBLE);
                                                    endMovePrince.setVisibility(View.INVISIBLE);
                                                    princeRegions.setVisibility(View.INVISIBLE);
                                                    Intent joinFightIntent = new Intent(Board.this, JoinFight.class);
                                                    joinFightIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    interruptThreadAndStartActivity(joinFightIntent);
                                                }
                                            }
                                        }
                                        if (game.getCurrentHero().getHeroClass() == myPlayer.getPlayer().getHero().getHeroClass()) {
                                            Toast.makeText(Board.this,"It is your turn", Toast.LENGTH_LONG).show();
                                            realMove.setVisibility(View.VISIBLE);
//                                            move.setVisibility(View.VISIBLE);
                                            fight.setVisibility(View.VISIBLE);
                                            pass.setVisibility(View.VISIBLE);
                                            endDay.setVisibility(View.VISIBLE);
                                            endMove.setVisibility(View.VISIBLE);
                                            if(game.getPrinceThorald()!= null){
                                                confirmMovePrince.setVisibility(View.VISIBLE);
                                                getDirectionPrince.setVisibility(View.VISIBLE);
                                                endMovePrince.setVisibility(View.VISIBLE);
                                                princeRegions.setVisibility(View.VISIBLE);
                                            }else{
                                                confirmMovePrince.setVisibility(View.INVISIBLE);
                                                getDirectionPrince.setVisibility(View.INVISIBLE);
                                                endMovePrince.setVisibility(View.INVISIBLE);
                                                princeRegions.setVisibility(View.INVISIBLE);
                                            }
                                        }else{
                                            realMove.setVisibility(View.INVISIBLE);
//                                            move.setVisibility(View.INVISIBLE);
                                            fight.setVisibility(View.INVISIBLE);
                                            pass.setVisibility(View.INVISIBLE);
                                            endDay.setVisibility(View.INVISIBLE);
                                            endMove.setVisibility(View.INVISIBLE);
                                            confirmMovePrince.setVisibility(View.INVISIBLE);
                                            getDirectionPrince.setVisibility(View.INVISIBLE);
                                            endMovePrince.setVisibility(View.INVISIBLE);
                                            princeRegions.setVisibility(View.INVISIBLE);

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


        getDirectionPrince.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                try{
                    AsyncTask<String, Void, GetPrinceThoraldMovesRC> asyncTask;
                    GetPrinceSender getPrinceSender = new GetPrinceSender();
                   GetPrinceThoraldMovesResponses getPrinceThoraldMovesResponses;

                    asyncTask = getPrinceSender.execute();
                    GetPrinceThoraldMovesRC princeAvailableRegion =asyncTask.get();
                    getPrinceThoraldMovesResponses = princeAvailableRegion.getGetPrinceThoraldMovesResponses();

                    if(getPrinceThoraldMovesResponses == GetPrinceThoraldMovesResponses.CANNOT_MOVE_PRINCE_AFTER_FIGHT){
                        Toast.makeText(Board.this, "Error. You cannot move the prince after fighting.", Toast.LENGTH_LONG).show();
                    }else if(getPrinceThoraldMovesResponses == GetPrinceThoraldMovesResponses.CANNOT_MOVE_PRINCE_AFTER_MOVE){
                        Toast.makeText(Board.this, "Error. You cannot move the prince after moving.", Toast.LENGTH_LONG).show();
                    }else if(getPrinceThoraldMovesResponses == GetPrinceThoraldMovesResponses.PRINCE_DNE){
                        Toast.makeText(Board.this, "Error. The Prince DNE.", Toast.LENGTH_LONG).show();
                    }else if(getPrinceThoraldMovesResponses == GetPrinceThoraldMovesResponses.CURRENT_HOUR_MAXED){
                        Toast.makeText(Board.this, "You have already reached the maximum hour and can't move futher.", Toast.LENGTH_LONG).show();
                    }else if(getPrinceThoraldMovesResponses == GetPrinceThoraldMovesResponses.DEDUCT_WILLPOWER){
                        Toast.makeText(Board.this, "You have already used up all normal hour, will power is deducted.", Toast.LENGTH_LONG).show();
                    }else if(getPrinceThoraldMovesResponses == GetPrinceThoraldMovesResponses.NOT_CURRENT_TURN) {
                        Toast.makeText(Board.this, "You are not in the current turn.", Toast.LENGTH_LONG).show();
                    }else if(getPrinceThoraldMovesResponses == GetPrinceThoraldMovesResponses.NOT_ENOUGH_WILLPOWER){
                        Toast.makeText(Board.this, "You don't have enought Will power", Toast.LENGTH_LONG).show();
                    }else if(getPrinceThoraldMovesResponses == GetPrinceThoraldMovesResponses.SUCCESS){
                        Toast.makeText(Board.this, "Move successfully", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
//        move.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//
//                try {
//                    AsyncTask<String, Void, GetAvailableRegionsRC> asyncTask;
//                    GetRegionsSender getRegionsSender = new GetRegionsSender();
//                    GetAvailableRegionsReponses getAvailableRegionsReponses;
//
//                    asyncTask = getRegionsSender.execute();
//                    GetAvailableRegionsRC availableRegions = asyncTask.get();
//                    Log.d("REGION", availableRegions.getResponse().toString());
//
//                    getAvailableRegionsReponses = availableRegions.getResponse();
//                    if (getAvailableRegionsReponses == GetAvailableRegionsReponses.CANNOT_MOVE_AFTER_FIGHT) {
//                        Toast.makeText(Board.this, "Error. You cannot move after fighting.", Toast.LENGTH_LONG).show();
//                    } else if (getAvailableRegionsReponses == GetAvailableRegionsReponses.CANNOT_MOVE_AFTER_MOVE_PRINCE) {
//                        Toast.makeText(Board.this, "Error. You cannot move after moving prince.", Toast.LENGTH_LONG).show();
//                    } else if (getAvailableRegionsReponses == GetAvailableRegionsReponses.CURRENT_HOUR_MAXED) {
//                        Toast.makeText(Board.this,"Error. Your hours are maxed. You must end your day.", Toast.LENGTH_LONG).show();
//                    } else if (getAvailableRegionsReponses == GetAvailableRegionsReponses.DEDUCT_WILLPOWER) {
//                        Toast.makeText(Board.this,"Moving will result in losing 2 willpower due to overtime.", Toast.LENGTH_LONG).show();
//                    } else if (getAvailableRegionsReponses == GetAvailableRegionsReponses.NOT_CURRENT_TURN) {
//                        Toast.makeText(Board.this,"Error. It is not your turn.", Toast.LENGTH_LONG).show();
//                    } else if (getAvailableRegionsReponses == GetAvailableRegionsReponses.NOT_ENOUGH_WILLPOWER) {
//                        Toast.makeText(Board.this,"Error. You do not have enough willpower.", Toast.LENGTH_LONG).show();
//                    } else { // SuccESS
//
//                    }
//
//                    adapter.clear();
//
//                    ArrayList<Integer> available = availableRegions.getRegions();
//                    for(Integer i: available){
//                        adapter.add(i.toString());
//                    }
//                    adapter.notifyDataSetChanged();
//                    sp.setAdapter(adapter);
//                    sp.setVisibility(View.VISIBLE);
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }
//
////                adapter.clear();
////                int region = myPlayer.getPlayer().getHero().getCurrentSpace();
////                ArrayList<Integer> adjacentRegions = MyPlayer.getInstance().getGame().getRegionDatabase().getRegion(region).getAdjacentRegions();
////                //adapter.add("Not selected");
////                for(Integer e: adjacentRegions) {
////                    adapter.add(e.toString());
////                }
////                sp.setVisibility(View.VISIBLE);
////                spText.setVisibility(View.VISIBLE);
////                //toolbar2.setVisibility(View.VISIBLE);
////                flag = true;
//
//            }
//        });

        chatb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key1", new Gson().toJson(PreviousPage.BOARD));
                Intent intent = new Intent(Board.this, ChatScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtras(bundle);
                interruptThreadAndStartActivity(intent);
            }
        });

        fight.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    AsyncTask<String, Void, GetPossibleCreaturesToFightRC> asyncTask;

                    FightSender messageSender = new FightSender();
                    asyncTask = messageSender.execute("");

                    System.out.println("!!!!!!!" + new Gson().toJson(asyncTask.get().getGetPossibleCreaturesToFightResponses()));

                    if (asyncTask.get().getGetPossibleCreaturesToFightResponses() == GetPossibleCreaturesToFightResponses.SUCCESS) {
                        Toast.makeText(Board.this, "Joining choose monster fight...", Toast.LENGTH_LONG).show();
                        myPlayer.setPossibleCreaturesToFight(asyncTask.get().getPossibleCreaturesToFight());

                        Intent fightIntent = new Intent(Board.this, ChooseMonsterFight.class);
                        fightIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        interruptThreadAndStartActivity(fightIntent);
                    } else if (asyncTask.get().getGetPossibleCreaturesToFightResponses() == GetPossibleCreaturesToFightResponses.NO_CREATURE_FOUND) {
                        Toast.makeText(Board.this, "Fight error. No creature found.", Toast.LENGTH_LONG).show();
                    } else if (asyncTask.get().getGetPossibleCreaturesToFightResponses() == GetPossibleCreaturesToFightResponses.DAY_ENDED) {
                        Toast.makeText(Board.this, "Fight error. Cannot fight after day ended.", Toast.LENGTH_LONG).show();
                    } else if (asyncTask.get().getGetPossibleCreaturesToFightResponses() == GetPossibleCreaturesToFightResponses.NOT_CURRENT_TURN){
                        Toast.makeText(Board.this, "Fight error. It is not your turn yet.", Toast.LENGTH_LONG).show();
                    } else if (asyncTask.get().getGetPossibleCreaturesToFightResponses() == GetPossibleCreaturesToFightResponses.CANNOT_FIGHT_AFTER_MOVE) {
                        Toast.makeText(Board.this, "Fight error. Cannot fight after moving.", Toast.LENGTH_LONG).show();
                    } else { // CANNOT_FIGHT_MOVE_PRINCE
                        Toast.makeText(Board.this, "Fight error. Cannot fight after moving prince.", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AsyncTask<String, Void, PassResponses> asyncTask;

                    PassSender passSender = new PassSender();
                    asyncTask = passSender.execute("");

                    if (asyncTask.get() == PassResponses.PASS_SUCCESSFUL) {
                        Toast.makeText(Board.this, "Successfully passed turn.", Toast.LENGTH_LONG).show();
                    } else if (asyncTask.get() == PassResponses.DAY_ENDED) {
                        Toast.makeText(Board.this, "Pass error. Your day already ended.", Toast.LENGTH_LONG).show();
                    } else if (asyncTask.get() == PassResponses.MUST_END_DAY) {
                        Toast.makeText(Board.this, "Pass error. You must end your day now.", Toast.LENGTH_LONG).show();
                    } else if (asyncTask.get() == PassResponses.NOT_CURRENT_TURN){
                        Toast.makeText(Board.this, "Pass error. It is not your turn yet.", Toast.LENGTH_LONG).show();
                    } else if (asyncTask.get() == PassResponses.PASS_SUCCESSFUL_WP_DEDUCTED) {
                        Toast.makeText(Board.this, "Successfully passed turn. 2 Willpower deducted for overtime.", Toast.LENGTH_LONG).show();
                    } else { // ONLY_PLAYER_LEFT
                        Toast.makeText(Board.this, "Pass error. You are the only player left.", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        endDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AsyncTask<String, Void, EndDayResponses> asyncTask;

                    EndDaySender endDaySender = new EndDaySender();
                    asyncTask = endDaySender.execute("");

                    if (asyncTask.get() == EndDayResponses.DAY_ALREADY_ENDED) {
                        Toast.makeText(Board.this, "End day error. Your day already ended.", Toast.LENGTH_LONG).show();
                    } else if (asyncTask.get() == EndDayResponses.NEW_DAY) {
                        Toast.makeText(Board.this, "New day!", Toast.LENGTH_LONG).show();
                    } else if (asyncTask.get() == EndDayResponses.GAME_OVER) {
                        Toast.makeText(Board.this, "Game over!", Toast.LENGTH_LONG).show();
                    } else if (asyncTask.get() == EndDayResponses.NOT_CURRENT_TURN){
                        Toast.makeText(Board.this, "End day. It is not your turn yet.", Toast.LENGTH_LONG).show();
                    } else { // ONLY_PLAYER_LEFT
                        Toast.makeText(Board.this, "Success. You ended your day.", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        endMovePrince.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                EndMovePrinceThoraldResponses endMovePrinceThoraldResponses;
                try{
                    AsyncTask<String,Void,EndMovePrinceThoraldResponses> asyncTask;
                    EndMovePrinceSender endMovePrinceSender = new EndMovePrinceSender();
                    asyncTask = endMovePrinceSender.execute("");
                    endMovePrinceThoraldResponses = asyncTask.get();

                    if(endMovePrinceThoraldResponses == EndMovePrinceThoraldResponses.SUCCESS){
                        Toast.makeText(Board.this, "Move prince successfully", Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        endMove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EndMoveResponses endMoveResponses;
                try {
                    AsyncTask<String, Void, EndMoveResponses> asyncTask;
                    EndMoveSender endMoveSender = new EndMoveSender();
                    asyncTask = endMoveSender.execute("");
                    endMoveResponses = asyncTask.get();
                    if (endMoveResponses == EndMoveResponses.ACTIVATE_FOG) {
                        Toast.makeText(Board.this, "A fog token will be activated", Toast.LENGTH_LONG).show();

                        try {
                            AsyncTask<String, Void, ActivateFogRC> asyncTaskFog;
                            ActivateFogSender activateFogSender = new ActivateFogSender();
                            asyncTaskFog = activateFogSender.execute("");
                            FogKind f = asyncTaskFog.get().getFogKind();
                            if (asyncTaskFog.get().getActivateFogResponses() == ActivateFogResponses.SUCCESS) {
                                if (f == FogKind.GOLD) {
                                    Toast.makeText(Board.this, "Gold added.", Toast.LENGTH_LONG).show();
                                } else if (f == FogKind.MONSTER) {
                                    Toast.makeText(Board.this, "A monster appeared in your region.", Toast.LENGTH_LONG).show();
                                } else if (f == FogKind.THREE_WP) {
                                    Toast.makeText(Board.this, "Three willpower points added.", Toast.LENGTH_LONG).show();
                                } else if (f == FogKind.SP) {
                                    Toast.makeText(Board.this, "Strength points added.", Toast.LENGTH_LONG).show();
                                } else if (f == FogKind.WITCHBREW) {
                                    Toast.makeText(Board.this, "A witchbrew was added to your inventory. Now proceeding to The Witch legend card...", Toast.LENGTH_LONG).show();

                                    myPlayer.setFoundWitch(true);
                                    try {
                                        FoundWitchSender foundWitchSender = new FoundWitchSender();
                                        foundWitchSender.execute("");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else if (f == FogKind.WINESKIN) {
                                    Toast.makeText(Board.this, "A wineskin was added to your inventory.", Toast.LENGTH_LONG).show();
                                } else if (f == FogKind.TWO_WP) {
                                    Toast.makeText(Board.this, "Two willpower points added.", Toast.LENGTH_LONG).show();
                                } else if (f == FogKind.EVENT) {
                                    //Event card
                                    Intent intent = new Intent(Board.this, EventCard.class);
                                    interruptThreadAndStartActivity(intent);
                                    finish();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (endMoveResponses == EndMoveResponses.BUY_FROM_MERCHANT) {
                        //Toast.makeText(Board.this,"You can buy items from a merchant",Toast.LENGTH_LONG).show();
                        threadTerminated = true;
                        Intent myIntent = new Intent(v.getContext(), EndMove_Merchant.class);
                        startActivity(myIntent);
                    } else if (endMoveResponses == EndMoveResponses.EMPTY_WELL) {
                        //Toast.makeText(Board.this,"You are in an area with a well",Toast.LENGTH_LONG).show();
                        threadTerminated = true;
                        Intent myIntent = new Intent(v.getContext(), EndMove_Well.class);
                        startActivity(myIntent);
                    } else if (endMoveResponses == EndMoveResponses.MOVE_ALREADY_ENDED) {
                        Toast.makeText(Board.this, "You have already ended your move", Toast.LENGTH_LONG).show();
                    } else if (endMoveResponses == EndMoveResponses.MUST_MOVE_TO_END_MOVE) {
                        Toast.makeText(Board.this, "You must first move", Toast.LENGTH_LONG).show();
                    } else if (endMoveResponses == EndMoveResponses.BUY_WITCH_BREW) {
                        threadTerminated = true;
                        Intent myIntent = new Intent(v.getContext(), BuyWitchBrewOptions.class);
                        startActivity(myIntent);
                    } else {
                        Toast.makeText(Board.this,"Successfully ended your move",Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        optionsb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Board.this, OptionsTab.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                interruptThreadAndStartActivity(intent);
            }
        });

    }


    public void interruptThreadAndStartActivity(Intent myIntent){
        threadTerminated = true;
        startActivity(myIntent);
        //if(t!= null || !t.isInterrupted()){
        //t.interrupt();
        //}
        finish();
    }
    //    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
////        if(flag){
////            this.flag = false;
////            Bitmap layout = BitmapFactory.decodeResource(getResources(),R.drawable.overlay);
////
////            int newColor = layout.getPixel((int)(event.getX()),(int)(event.getY()));
////            int newRegionNumber = Color.blue(newColor);
////            //Region newRegion = RegionDatabase.getInstance().getRegionDatabase().get(newRegionNumber);
////
////            int oldColor = layout.getPixel((int)(this.warrior.getX()),(int)(this.warrior.getY()));
////            int oldRegionNumber = Color.blue(oldColor);
////            //Region currentRegion = RegionDatabase.getInstance().getRegionDatabase().get(oldRegionNumber);
////
////            //if(currentRegion.getAdjacentRegions().contains(newRegion))
////            //{
////                //this.warrior.setX(event.getX());
////                //this.warrior.setY(event.getY());
////                //return true;
////            //}
////            //else
////                //{
////                    //Toast.makeText(Board.this, "The clicked region is not a neighbor to your current region. Please choose your destination again.", Toast.LENGTH_LONG).show();
////                    //return super.dispatchTouchEvent(event);
////                //}
////            //check if the region is a neighbor to the current region;
////            //set the player's current region to new region;
////            //add the player to the new region and remove the player from the previous region
////        }
//
//        if(flag){
//            float X = event.getX();
//            float Y = event.getY();
//            Bitmap layout = BitmapFactory.decodeResource(getResources(),R.drawable.overlay);
//
//
//            int newColor = layout.getPixel((int)(event.getX()),(int)(event.getY()));
//            int newRegionNumber = Color.blue(newColor);
//            String log = " "+"x:"+X+", y:"+ Y;
//            warrior.setY((float)Y-65);
//            warrior.setX((float)X);
//            String color = " "+newRegionNumber;
//            Log.d("TEST",log);
//            Log.d("Test2",color);
//        }
//        return super.dispatchTouchEvent(event);
//    }

    private void displayFarmer(ImageView imageView, int space){
        final MyPlayer myPlayer = MyPlayer.getInstance();

        float[] coor = myPlayer.getGame().getRegionDatabase().getRegion(space).getCoordinates();
        imageView.setX(coor[0]);
        imageView.setY(coor[1]+10);
    }
    private void moveHero(Hero hero, int space){
        if(hero.getHeroClass() == HeroClass.WARRIOR){
            movePic(this.warrior, space);
        }
        if(hero.getHeroClass() == HeroClass.ARCHER){
            movePic(this.archer, space);
        }
        if(hero.getHeroClass() == HeroClass.DWARF){
            movePic(this.dwarf, space);
        }
        if(hero.getHeroClass() == HeroClass.WIZARD){
            movePic((this.wizard),space);
        }
        hero.setCurrentSpace(space);

    }

    private void moveHeroTime(Hero hero, float  x, float y){
        if(hero.getHeroClass() == HeroClass.WARRIOR){
            movePic(this.theros.get(0), x-5, y);
        }
        if(hero.getHeroClass() == HeroClass.ARCHER){
            movePic(this.theros.get(1), x+10, y);
        }
        if(hero.getHeroClass() == HeroClass.DWARF){
            movePic(this.theros.get(2), x-5, y+15);
        }
        if(hero.getHeroClass() == HeroClass.WIZARD){
            movePic(this.theros.get(3), x+10, y+15);
        }
        //hero.setCurrentSpace(space);

    }

    private void moveMonster(ImageView imageView, int space){
        final MyPlayer myPlayer = MyPlayer.getInstance();

        float[] coor = myPlayer.getGame().getRegionDatabase().getRegion(space).getCoordinates();
        imageView.setX(coor[0]+10);
        imageView.setY(coor[1]+10);
    }


    //Used to move anything on on the board
    public void movePic(ImageView imageView, int space){

        final MyPlayer myPlayer = MyPlayer.getInstance();

        float[] coor = myPlayer.getGame().getRegionDatabase().getRegion(space).getCoordinates();
        Log.d("ERR","trd");

        //try if there is already a movable
        imageView.setX(coor[0]);
        imageView.setY(coor[1]);
        Log.d("ERR","4th");

    }

    //used to move narrator and hours.
    public void movePic(ImageView imageView, float x, float y){
        imageView.setX(x);
        imageView.setY(y);
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

    private static class FightSender extends AsyncTask<String, Void, GetPossibleCreaturesToFightRC> {
        @Override
        protected GetPossibleCreaturesToFightRC doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.get("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/getPossibleCreaturesToFight")
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, GetPossibleCreaturesToFightRC.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class PassSender extends AsyncTask<String, Void, PassResponses> {
        @Override
        protected PassResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/pass")
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, PassResponses.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class EndDaySender extends AsyncTask<String, Void, EndDayResponses> {
        @Override
        protected EndDayResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/endDay")
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, EndDayResponses.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class MoveSender extends AsyncTask<String, Void, MoveRC> {
        @Override
        protected MoveRC doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/move")
                        .header("Content-Type", "application/json")
                        .body(strings[0])
                        .asString();
                String resultAsJsonString = response.getBody();
                Log.d("BODY","RESPONSE BODY " + response.getBody());
                return new Gson().fromJson(resultAsJsonString, MoveRC.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class MovePrinceSender extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/movePrinceThorald")
                        .header("Content-Type", "application/json")
                        .body(strings[0])
                        .asString();
                String resultAsJsonString = response.getBody();
                Log.d("BODY","RESPONSE BODY " + response.getBody());
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class EndMoveSender extends AsyncTask<String, Void, EndMoveResponses>{
        @Override
        protected EndMoveResponses doInBackground(String... strings){
            HttpResponse<String> response;
            MyPlayer myPlayer = MyPlayer.getInstance();
            try{
                response = Unirest.post("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/endMove")
                        .asString();
                String resultAsJsonString = response.getBody();
                return new Gson().fromJson(resultAsJsonString, EndMoveResponses.class);
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class EndMovePrinceSender extends AsyncTask<String, Void, EndMovePrinceThoraldResponses>{
        @Override
        protected EndMovePrinceThoraldResponses doInBackground(String... strings){
            HttpResponse<String> response;
            MyPlayer myPlayer = MyPlayer.getInstance();
            try{
                response = Unirest.post("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/endMovePrinceThorald")
                        .asString();
                String resultAsJsonString = response.getBody();
                return new Gson().fromJson(resultAsJsonString, EndMovePrinceThoraldResponses.class);
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class GetRegionsSender extends AsyncTask<String, Void, GetAvailableRegionsRC>{
        @Override
        protected GetAvailableRegionsRC doInBackground(String... strings){
            HttpResponse<String> response;
            MyPlayer myPlayer = MyPlayer.getInstance();
            try{
                response = Unirest.get("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/getAvailableRegions")
                        .asString();
                String resultAsJsonString = response.getBody();
                return new Gson().fromJson(resultAsJsonString, GetAvailableRegionsRC.class);
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class GetPrinceSender extends AsyncTask<String, Void, GetPrinceThoraldMovesRC>{
        @Override
        protected GetPrinceThoraldMovesRC doInBackground(String... strings){
            HttpResponse<String> response;
            MyPlayer myPlayer = MyPlayer.getInstance();
            try{
                response = Unirest.get("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/getPrinceThoraldMoves")
                        .asString();
                String resultAsJsonString = response.getBody();
                return new Gson().fromJson(resultAsJsonString, GetPrinceThoraldMovesRC.class);
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class ActivateFogSender extends AsyncTask<String, Void, ActivateFogRC> {
        @Override
        protected ActivateFogRC doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() + "/" + myPlayer.getPlayer().getUsername() + "/activateFog")
                        .header("Content-Type", "application/json")
                        .asString();
                String resultAsJsonString = response.getBody();
                return new Gson().fromJson(resultAsJsonString,ActivateFogRC.class );
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class FoundWitchSender extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();

            try {
                Unirest.post("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() + "/" + myPlayer.getPlayer().getUsername() + "/foundWitch")
                        .asString();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class ActivateLegendCardN extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();

            try {
                Unirest.post("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() + "/" + myPlayer.getPlayer().getUsername() + "/activateLegendCardN")
                        .asString();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}