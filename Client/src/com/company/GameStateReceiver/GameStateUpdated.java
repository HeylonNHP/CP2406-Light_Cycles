package com.company.GameStateReceiver;

import java.util.EventObject;

public class GameStateUpdated extends EventObject {
    GameState gameState;
    public GameStateUpdated(Object source, GameState state){
        super(source);
        this.gameState = state;
    }

    public GameState getGameState(){
        return gameState;
    }
}
