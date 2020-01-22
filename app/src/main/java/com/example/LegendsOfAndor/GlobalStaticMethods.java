package com.example.LegendsOfAndor;

import java.util.Random;

public class GlobalStaticMethods {

    //get random color for user
    //update
    public static String getRandomColor() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer("#");
        while(sb.length() < 7){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, 7);
    }
}
