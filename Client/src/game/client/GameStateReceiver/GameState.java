package game.client.GameStateReceiver;

import java.awt.*;
import java.util.*;

public class GameState {
    private ArrayList<PlayerState> playerPositions = new ArrayList<>();

    public GameState(){

    }

    public void addPlayer(String playerName, int xPosition, int yPosition, boolean jetwallEnabled){
        Dimension playerPosition = new Dimension(xPosition,yPosition);
        PlayerState playerState = new PlayerState(playerName, playerPosition, jetwallEnabled);
        playerPositions.add(playerState);
    }

    public ArrayList<PlayerState> getPlayerStates(){
        return playerPositions;
    }
}
