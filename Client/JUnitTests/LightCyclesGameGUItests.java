import game.client.LightCyclesGame;
import game.client.MainWindow.GamePanel.GamePanel;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

public class LightCyclesGameGUItests {
    boolean eventFired = false;
    @Test
    public void testJoinServerFailedEvent(){
        LightCyclesGame game = new LightCyclesGame("Test", Color.red);
        GamePanel panel = new GamePanel(game);

        panel.addJoinServerFailedListener((e) ->{
            eventFired = true;
        });

        panel.joinServer();
        Assert.assertTrue(eventFired);
    }

}
