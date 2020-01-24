package com.example.LegendsOfAndor;

public class MyPlayer {
    private static Player myPlayer;

    private MyPlayer(Player p){
        myPlayer = p;
    }

    public static Player getMyPlayer(){
        return myPlayer;
    }

    public static void setMyPlayer(Player p){
        myPlayer = p;
    }
}
