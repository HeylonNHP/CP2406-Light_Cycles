import com.company.LightCyclesGame;
import com.company.MainWindow.GamePanel.GamePanel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runners.JUnit4;

public class LightCyclesGameGUItests {
    boolean eventFired = false;
    @Test
    public void testJoinServerFailedEvent(){
        LightCyclesGame game = new LightCyclesGame("Test");
        GamePanel panel = new GamePanel(game);

        panel.addJoinServerFailedListener((e) ->{
            eventFired = true;
        });

        panel.joinServer();
        Assert.assertTrue(eventFired);
    }

}
