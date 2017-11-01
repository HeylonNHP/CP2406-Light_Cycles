package game.client.MainWindow;

import game.client.LightCyclesGame;
import game.client.MainWindow.GamePanel.DetailsDisplayPanel.DetailsDisplayPanel;
import game.client.MainWindow.GamePanel.GamePanel;
import game.client.MainWindow.JoinGamePanel.JoinGamePanel;
import game.client.MainWindow.LeaderBoardViewer.LeaderBoardViewer;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Scanner;

public class Main extends JFrame{
    private JoinGamePanel startScreen = new JoinGamePanel();
    private GamePanel gameScreenPanel;
    private DetailsDisplayPanel displayPanel;
    private LightCyclesGame gameObject;
    public Main(){
        super("Light cycles game");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        startScreen.addJoinGameListener((e) -> {
            String testMessage = String.format("Name: %s Colour: %s", e.getChosenPlayerName(),
                    e.getChosenColour().toString());
            //JOptionPane.showMessageDialog(this,testMessage);

            gameObject = new LightCyclesGame(e.getChosenPlayerName(), e.getChosenColour(),
                    e.getIpAddress(),e.getPort());

            displayPanel = new DetailsDisplayPanel(this,gameObject);

            remove(startScreen);
            add(displayPanel);
            displayPanel.joinServer();
        });

        add(startScreen);
        pack();
        setVisible(true);
    }

    public void switchToStartScreen(){
        getContentPane().removeAll();
        endGame();
        add(startScreen);
        revalidate();
        repaint();
        pack();
    }

    public void switchToLeaderBoard(HashMap<String, Integer> highScores){
        LeaderBoardViewer leaderBoard = new LeaderBoardViewer(highScores);

        leaderBoard.addReturnToStartScreenRequestListener((e) -> switchToStartScreen());

        getContentPane().removeAll();
        add(leaderBoard);

        revalidate();
        repaint();
    }

    private void endGame(){
        /*Will close the current game*/
        displayPanel = null;
        gameObject.close();
        gameObject = null;
    }

    public static void main(String[] args) {
        new Main();
    }
}
