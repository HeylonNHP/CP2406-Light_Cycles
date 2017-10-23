package game.client;

import java.util.EventListener;

public interface GameOverEvent extends EventListener {
    void gameOverOccurred(GameOverOccurred e);
}
