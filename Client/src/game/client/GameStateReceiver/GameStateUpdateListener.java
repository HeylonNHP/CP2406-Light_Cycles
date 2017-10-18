package game.client.GameStateReceiver;

import java.util.EventListener;

public interface GameStateUpdateListener extends EventListener {
    void GameStateUpdate(GameStateUpdated e);
}
