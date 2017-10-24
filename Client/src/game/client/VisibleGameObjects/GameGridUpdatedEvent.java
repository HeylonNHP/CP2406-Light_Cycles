package game.client.VisibleGameObjects;

import java.util.EventListener;
import java.util.EventObject;

public interface GameGridUpdatedEvent extends EventListener {
    void gameGridUpdated(EventObject e);
}
