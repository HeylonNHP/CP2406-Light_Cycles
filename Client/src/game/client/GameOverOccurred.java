package game.client;

import java.util.EventObject;

public class GameOverOccurred extends EventObject {
    String winningPlayerName;
    public GameOverOccurred(Object source, String winningPlayer){
        super(source);
        winningPlayerName = winningPlayer;
    }

    public String getWinningPlayerName() {
        return winningPlayerName;
    }
}
