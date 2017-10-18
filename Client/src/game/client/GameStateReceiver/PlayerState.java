package game.client.GameStateReceiver;

import java.awt.*;

public class PlayerState {
    Dimension position;
    String name;
    boolean jetwallEnabled;

    public PlayerState(String playerName, Dimension playerPosition, boolean jetwallEnabled){
        setName(playerName);
        setPosition(playerPosition);
        setJetwallEnabled(jetwallEnabled);
    }

    public Dimension getPosition() {
        return position;
    }

    public void setPosition(Dimension position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isJetwallEnabled() {
        return jetwallEnabled;
    }

    public void setJetwallEnabled(boolean jetwallEnabled) {
        this.jetwallEnabled = jetwallEnabled;
    }
}
