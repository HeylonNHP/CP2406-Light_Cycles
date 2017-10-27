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
    public Main(){
        super("Light cycles game");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        startScreen.addJoinGameListener((e) -> {
            String testMessage = String.format("Name: %s Colour: %s", e.getChosenPlayerName(),
                    e.getChosenColour().toString());
            //JOptionPane.showMessageDialog(this,testMessage);
            LightCyclesGame newGame = new LightCyclesGame(e.getChosenPlayerName(), e.getChosenColour(),
                    e.getIpAddress(),e.getPort());

            displayPanel = new DetailsDisplayPanel(this,newGame);

            remove(startScreen);
            add(displayPanel);
            displayPanel.joinServer();
            /*
            gameScreenPanel = new GamePanel(newGame);

            gameScreenPanel.addJoinServerFailedListener((e1) ->{
                remove(gameScreenPanel);
                add(startScreen);
                revalidate();
                repaint();
            });
            gameScreenPanel.addRePaintRequestListener((e2) -> {
                revalidate();
                repaint();
                pack();
            });

            //Remove the join game panel from screen
            remove(startScreen);
            //Add game panel to screen
            add(gameScreenPanel);

            gameScreenPanel.joinServer();*/
        });

        add(startScreen);
        pack();
        setVisible(true);
    }

    public void switchToStartScreen(){
        getContentPane().removeAll();
        add(startScreen);
    }

    public void switchToLeaderBoard(HashMap<String, Integer> highScores){
        LeaderBoardViewer leaderBoard = new LeaderBoardViewer(highScores);

        getContentPane().removeAll();
        add(leaderBoard);

        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        new Main();
    }
}
