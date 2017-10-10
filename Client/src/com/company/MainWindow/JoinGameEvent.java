package com.company.MainWindow;

import java.util.EventListener;
import java.util.EventObject;

interface JoinGameEventListener extends EventListener{
    void joinGameRequested(JoinGameEvent e);
}
public class JoinGameEvent extends EventObject{
    private String chosenPlayerName;
    public JoinGameEvent(Object source, String chosenPlayerName){
        super(source);
        this.chosenPlayerName = chosenPlayerName;
    }

    public String getChosenPlayerName() {
        return chosenPlayerName;
    }
}
