package com.example.LegendsOfAndor;

import java.util.ArrayList;

public class MyPlayer {
   private static MyPlayer myPlayer = null;
   private Player player;
   private String serverIP;
   private Game game;
   private boolean runeStoneToastDisplayed;
   private boolean legendCardCDisplayed;
   private boolean legendCardRuneStonesDisplayed;
   private boolean legendCardGDisplayed;
   private boolean legendCardNDisplayed;
   private boolean legendCardTheWitchDisplayed;
   private ArrayList<Integer> possibleCreaturesToFight;
   private boolean foundWitch;

   public void setPlayer(Player p){
       player = p;
   }

   public Player getPlayer(){
       return this.player;
   }
   private MyPlayer(){}

   public String getServerIP() {
        return serverIP;
   }
   public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

   public static MyPlayer getInstance(){
       if(myPlayer == null){
           myPlayer = new MyPlayer();
       }
       return myPlayer;
   }

   public Game getGame() {
       return game;
   }

   public void setGame(Game game) {
       this.game = game;
   }

    public boolean isRuneStoneToastDisplayed() {
        return runeStoneToastDisplayed;
    }

    public void setRuneStoneToastDisplayed(boolean runeStoneToastDisplayed) {
        this.runeStoneToastDisplayed = runeStoneToastDisplayed;
    }

    public boolean isLegendCardCDisplayed() {
        return legendCardCDisplayed;
    }

    public void setLegendCardCDisplayed(boolean legendCardCDisplayed) {
        this.legendCardCDisplayed = legendCardCDisplayed;
    }

    public boolean isLegendCardRuneStonesDisplayed() {
        return legendCardRuneStonesDisplayed;
    }

    public void setLegendCardRuneStonesDisplayed(boolean legendCardRuneStonesDisplayed) {
        this.legendCardRuneStonesDisplayed = legendCardRuneStonesDisplayed;
    }

    public boolean isLegendCardGDisplayed() {
        return legendCardGDisplayed;
    }

    public void setLegendCardGDisplayed(boolean legendCardGDisplayed) {
        this.legendCardGDisplayed = legendCardGDisplayed;
    }

    public boolean isLegendCardNDisplayed() {
        return legendCardNDisplayed;
    }

    public void setLegendCardNDisplayed(boolean legendCardNDisplayed) {
        this.legendCardNDisplayed = legendCardNDisplayed;
    }

    public ArrayList<Integer> getPossibleCreaturesToFight() {
        return possibleCreaturesToFight;
    }

    public void setPossibleCreaturesToFight(ArrayList<Integer> possibleCreaturesToFight) {
        this.possibleCreaturesToFight = possibleCreaturesToFight;
    }

    public boolean isLegendCardTheWitchDisplayed() {
        return legendCardTheWitchDisplayed;
    }

    public void setLegendCardTheWitchDisplayed(boolean legendCardTheWitchDisplayed) {
        this.legendCardTheWitchDisplayed = legendCardTheWitchDisplayed;
    }

    public boolean isFoundWitch() {
        return foundWitch;
    }

    public void setFoundWitch(boolean foundWitch) {
        this.foundWitch = foundWitch;
    }
}
