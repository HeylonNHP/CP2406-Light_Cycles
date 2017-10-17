package com.company.MainWindow.JoinGamePanel;

import java.awt.*;
import java.util.EventObject;

public class JoinGameEvent extends EventObject{
    private String chosenPlayerName;
    private Color chosenColour;
    public JoinGameEvent(Object source, String chosenPlayerName, Color chosenColour){
        super(source);
        this.chosenPlayerName = chosenPlayerName;
        this.chosenColour = chosenColour;
    }

    public String getChosenPlayerName() {
        return chosenPlayerName;
    }

    public Color getChosenColour() {
        return chosenColour;
    }
}
