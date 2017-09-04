package com.company;

import com.company.GameStateReceiver.GameStateReceiver;
import com.company.GameStateReceiver.GameStateUpdated;

public class LightCyclesGame {
    String multicastAddress = "239.69.69.69";
    int multicastPort = 56969;
    public LightCyclesGame(){
        GameStateReceiver receiver = new GameStateReceiver();
        receiver.addGameStateUpdateListener(e -> {receivedNewGameState(e);});
        receiver.start();
    }

    public void receivedNewGameState(GameStateUpdated e){
        System.out.println("The event listener worked.");
    }
}
