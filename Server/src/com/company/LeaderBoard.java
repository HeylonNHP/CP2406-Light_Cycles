package com.company;

import java.util.HashMap;

public class LeaderBoard {
    private HashMap<String,Integer> highScores = new HashMap<>();
    public LeaderBoard(){

    }

    public void addHighScore(String name, Integer score){
        highScores.put(name,score);
    }
}
