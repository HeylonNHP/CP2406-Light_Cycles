import game.client.LightCyclesGame;
import game.client.MainWindow.GamePanel.GamePanel;
import game.client.VisibleGameObjects.*;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

public class LightCyclesGameGUItests {
    boolean eventFired = false;
    @Test
    public void testJoinServerFailedEvent(){
        LightCyclesGame game = new LightCyclesGame("Test", Color.red,"127.0.0.1",
                1);
        GamePanel panel = new GamePanel(game);

        panel.addJoinServerFailedListener((e) ->{
            eventFired = true;
        });

        panel.joinServer();
        Assert.assertTrue(eventFired);
    }

    boolean gridUpdatedEventFired = false;
    @Test
    public void testGameGrid(){
        Dimension gridSize = new Dimension(500,500);
        GameGrid gameGrid = new GameGrid(gridSize);

        Assert.assertTrue(gridSize == gameGrid.getGridDimensions());

        Player[] testPlayers = new Player[10];
        for(int i =0; i < testPlayers.length;++i){
            Dimension position = new Dimension(i,i);
            testPlayers[i] = new Player("Player " + i, position, PlayerDirection.UP);
        }

        for(Player player:testPlayers){
            gameGrid.addPlayerToGrid(player);
        }
        /*
        JetWall[] testJetWalls = new JetWall[testPlayers.length*10];
        int count = 0;
        for(int i = 0; i < testPlayers.length; ++i){
            for (int j = 0; j < testJetWalls.length/testPlayers.length; ++j){
                System.out.println(count);
                Player player = testPlayers[i];
                testJetWalls[count] = new JetWall(
                        player,new Dimension(i,j), JetWallDirection.VERTICAL
                );
                count++;
            }
        }

        for(JetWall jetWall:testJetWalls){
            gameGrid.addJetWallToGrid(jetWall);
        }
        */
        //Remove all players from the grid. Check if this succeeded
        for(Player player:testPlayers){
            gameGrid.removePlayerFromGrid(player);
        }
        Assert.assertEquals(0,gameGrid.getPlayerList().size());

        //Test the event handler
        gameGrid.addGridUpdatedListener((e) ->{
            gridUpdatedEventFired = true;
        });

        gameGrid.raiseGridUpdatedListener();

        Assert.assertTrue(gridUpdatedEventFired);
    }

    @Test
    public void jetWallTest(){
        Player player = new Player("Player", new Dimension(1,0),PlayerDirection.LEFT);

        Dimension position = new Dimension(100,100);
        JetWall jetWall = new JetWall(player,position,JetWallDirection.VERTICAL);

        Assert.assertEquals(player,jetWall.getParentPlayer());
        Assert.assertEquals(position,jetWall.getPosition());
    }

    @Test
    public void playerTest(){

    }

}
