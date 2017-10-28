package game.server;

import java.util.EventListener;
import java.util.EventObject;

public interface GameOverListener extends EventListener {
    void gameOverOccurred(EventObject e);
}
