package game.client.MainWindow.LeaderBoardViewer;

import java.util.EventListener;
import java.util.EventObject;

public interface ReturnToStartScreenListener extends EventListener {
    void returnToStartScreenRequested(EventObject e);
}
