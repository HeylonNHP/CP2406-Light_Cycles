package com.company.GameStateReceiver;

import java.awt.*;
import java.util.*;

public class GameState {
    private HashMap<String, Dimension> playerPositions = new HashMap<>();
    public GameState(){

    }

    public void addPlayer(String playerName, int xPosition, int yPosition){
        Dimension playerPosition = new Dimension(xPosition,yPosition);
        playerPositions.put(playerName, playerPosition);
    }

    public ArrayList<String> getPlayerNames(){
        return new ArrayList<>(playerPositions.keySet());
    }

    public Dimension getPlayerCoordinates(String playerName){
        return playerPositions.get(playerName);
    }
}
