package com.company.GameStateReceiver;

import java.util.EventObject;

public class GameStateUpdated extends EventObject {
    public GameStateUpdated(Object source){
        super(source);
    }
}
