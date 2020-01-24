package com.example.LegendsOfAndor;

import java.util.ArrayList;

public class MessageDatabase {
    private ArrayList<Message> messages;
    private String gameName;

    public MessageDatabase (String gameName) {
        this.gameName = gameName;
        messages = new ArrayList<>();
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public String getGameName() {
        return gameName;
    }

    public void add(Message m) {
        messages.add(m);
    }
}
