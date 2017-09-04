package com.company;

import com.company.GameStateReceiver.GameState;
import com.company.GameStateReceiver.GameStateReceiver;
import com.company.GameStateReceiver.GameStateUpdated;

import java.awt.*;
import java.util.HashMap;

public class LightCyclesGame {
    String multicastAddress = "239.69.69.69";
    int multicastPort = 56969;
    public LightCyclesGame(){
        GameStateReceiver receiver = new GameStateReceiver();
        receiver.addGameStateUpdateListener(e -> {receivedNewGameState(e);});
        receiver.start();
    }

    public void receivedNewGameState(GameStateUpdated e){
        GameState gameState = e.getGameState();

        for (String playerName: gameState.getPlayerNames()){
            Dimension playerCoords = gameState.getPlayerCoordinates(playerName);
            System.out.println(String.format("Player: %s x: %s y:%s", playerName, playerCoords.width, playerCoords.height));
        }

        System.out.println("The event listener worked.");
    }
}
