package game.client.MainWindow.JoinGamePanel;

import java.awt.*;
import java.util.EventObject;

public class JoinGameEvent extends EventObject{
    private String chosenPlayerName;
    private Color chosenColour;
    private String ipAddress;
    private int port;
    public JoinGameEvent(Object source, String chosenPlayerName, Color chosenColour,
                         String destIP, int destPort){
        super(source);
        this.chosenPlayerName = chosenPlayerName;
        this.chosenColour = chosenColour;
        this.ipAddress = destIP;
        this.port = destPort;
    }

    public JoinGameEvent(Object source, String chosenPlayerName, Color chosenColour){
        this(source,chosenPlayerName,chosenColour,"127.0.0.1",56970);
    }

    public String getChosenPlayerName() {
        return chosenPlayerName;
    }

    public Color getChosenColour() {
        return chosenColour;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }
}
