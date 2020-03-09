package com.example.LegendsOfAndor;
import java.util.ArrayList;

public class GetAvailableRegionsRC {
    private ArrayList<Integer> adjacentRegions;
    private GetAvailableRegionsReponses getAvailableRegionsResponses;

    public GetAvailableRegionsRC() {
    }

    public ArrayList<Integer> getRegions(){
        return this.adjacentRegions;
    }

    public GetAvailableRegionsReponses getResponse(){
        return this.getAvailableRegionsResponses;
    }

    public GetAvailableRegionsRC(ArrayList<Integer> adjacentRegions, GetAvailableRegionsReponses getAvailableRegionsResponses) {
        this.adjacentRegions = adjacentRegions;
        this.getAvailableRegionsResponses = getAvailableRegionsResponses;
    }
}