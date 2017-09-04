package com.company;

import java.util.HashMap;

public class LeaderBoard {
    HashMap<String,Integer> highScores = new HashMap<String, Integer>();
    public LeaderBoard(){

    }

    public void addHighScore(String name, Integer score){
        highScores.put(name,score);
    }
}
