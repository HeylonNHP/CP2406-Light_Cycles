package com.company.MainWindow.JoinGamePanel;

import java.util.EventObject;

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
