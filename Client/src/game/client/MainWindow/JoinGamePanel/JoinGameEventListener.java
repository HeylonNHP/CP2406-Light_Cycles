package game.client.MainWindow.JoinGamePanel;

import java.util.EventListener;

public interface JoinGameEventListener extends EventListener {
    void joinGameRequested(JoinGameEvent e);
}