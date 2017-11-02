import game.server.GameGrid;
import game.server.HighScore;
import game.server.LeaderBoard;
import game.server.Player;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class JUnitTests {
    @Test
    public void gameGridTest(){
        Dimension gridSize = new Dimension(500,500);
        GameGrid gameGrid = new GameGrid(gridSize);

        Assert.assertEquals(gridSize,gridSize.getSize());

        Player player = new Player("Heylon");

        gameGrid.addPlayerAtRandomPosition(player);

        try{
            Dimension initialPosition = gameGrid.getLocationOfItemOnGrid(player);

            gameGrid.progressGame();

            Dimension newPosition = gameGrid.getLocationOfItemOnGrid(player);

            //Should be in the same place - player starts out in a stationary state
            boolean playerMoved = false;
            if(initialPosition.width != newPosition.width ||
                    initialPosition.height != newPosition.height){
                playerMoved = true;
            }
            Assert.assertFalse(playerMoved);

            //Test to see if the player can move once its speed has been set
            player.setMovingSpeedFast();
            gameGrid.progressGame();
            newPosition = gameGrid.getLocationOfItemOnGrid(player);
            playerMoved = false;
            if(initialPosition.width != newPosition.width ||
                    initialPosition.height != newPosition.height){
                playerMoved = true;
            }
            Assert.assertTrue(playerMoved);

            //Test if the player can crash into its own jetwall trail

            //This loop should cause the player to make a series of left turns back into its own jet wall
            for(int i = 0; i < 4; ++i){
                for(int j = 0; j < 5; ++j){
                    gameGrid.progressGame();
                }
                player.turnLeft();
            }

            boolean playerIsStillOnGrid;
            try {
                gameGrid.getLocationOfItemOnGrid(player);
                playerIsStillOnGrid = true;
            }catch (Exception e){
                playerIsStillOnGrid = false;
            }
            Assert.assertFalse(playerIsStillOnGrid);


        }catch (Exception e){

        }
    }

    @Test
    public void leaderBoardTest(){
        LeaderBoard leaderBoard = new LeaderBoard();

        HighScore[] highScores = new HighScore[500];
        for(int i = 0; i < highScores.length; ++i){
            highScores[i] = new HighScore("Heylon", new Random().nextInt(10000));
        }

        for(HighScore highScore:highScores){
            leaderBoard.addHighScore(highScore);
        }

        ArrayList<HighScore> highScoresList = leaderBoard.getHighScores();

        Assert.assertEquals(highScoresList.size(), highScores.length);

        Collections.sort(highScoresList);

        boolean sortedByScore = true;
        for (int i = 0; i+1 < highScoresList.size(); ++i){
            if(highScoresList.get(i).getPlayerScore() <
                    highScoresList.get(i+1).getPlayerScore()){
                sortedByScore = false;
            }
        }

        Assert.assertTrue(sortedByScore);
    }

    @Test
    public void lightCyclesGameTest(){

    }

}
