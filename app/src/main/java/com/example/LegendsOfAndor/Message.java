package com.example.LegendsOfAndor;

public class Message {
    private  String text;
    private Player player;
    private boolean belongsToCurrentUser;

    public Message(String text, Player player, boolean belongsToCurrentUser){
        this.text = text;
        this.player = player;
        this.belongsToCurrentUser = belongsToCurrentUser;
    }

    public String getText(){
        return this.text;
    }

    public Player getPlayer(){
        return this.player;
    }

    public boolean isBelongsToCurrentUser(){
        return this.belongsToCurrentUser;
    }
}
