package com.example.LegendsOfAndor;

public class Region {
    private Region [] myNeighbors;
    private RegionType mytype;
    //Item [] myItem;
    //int myGoldAndGems;
    Region(RegionType type, Region[]neighbors){
        myNeighbors = neighbors;
        mytype = type;

    }
}
