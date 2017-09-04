package com.company.GameStateReceiver;

import java.awt.*;
import java.util.*;

public class GameState {
    HashMap<String, Dimension> playerPositions = new HashMap<>();
    public GameState(){

    }

    public void addPlayer(String playerName, int xPosition, int yPosition){
        Dimension playerPosition = new Dimension(xPosition,yPosition);
        playerPositions.put(playerName, playerPosition);
    }

    public ArrayList<String> getPlayerNames(){
        ArrayList<String> playerNames = new ArrayList<>(playerPositions.keySet());
        return playerNames;
    }

    public Dimension getPlayerCoordinates(String playerName){
        return playerPositions.get(playerName);
    }
}
