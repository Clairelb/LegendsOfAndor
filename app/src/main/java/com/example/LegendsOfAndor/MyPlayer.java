package com.example.LegendsOfAndor;

public class MyPlayer {
   private static MyPlayer myPlayer = null;
   private Player player;

   public void setPlayer(Player p){
       player = p;
   }

   public Player getPlayer(){
       return this.player;
   }
   private MyPlayer(){}

   public static MyPlayer getInstance(){
        if(myPlayer == null){
            myPlayer = new MyPlayer();

        }
        return myPlayer;
   }






}
