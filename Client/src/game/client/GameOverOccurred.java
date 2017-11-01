package game.client;

import java.util.EventObject;

public class GameOverOccurred extends EventObject {
    private String winningPlayerName;
    public GameOverOccurred(Object source, String winningPlayer){
        super(source);
        winningPlayerName = winningPlayer;
    }

    public String getWinningPlayerName() {
        return winningPlayerName;
    }
}
