package game.client.MainWindow.GamePanel.DetailsDisplayPanel;

import game.client.LightCyclesGame;
import game.client.MainWindow.GamePanel.GamePanel;
import game.client.MainWindow.Main;
import game.client.VisibleGameObjects.GameGrid;
import game.client.VisibleGameObjects.Player;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class DetailsDisplayPanel extends JPanel {
    JLabel scoreLabel = new JLabel("Score");
    JLabel userName = new JLabel("Name");
    JButton leaveGameButton = new JButton("Leave game");
    JButton viewScoreboardButton = new JButton("View scoreboard");
    Main mainWindow;
    GamePanel gamePanel;

    private String usersName;
    private GameGrid gameGrid;
    private LightCyclesGame gameObject;
    public DetailsDisplayPanel(Main mainWindow, LightCyclesGame gameObject){
        //super(new GridLayout(2,3));
        super(new FlowLayout());
        this.mainWindow = mainWindow;
        this.gamePanel = new GamePanel(gameObject);
        this.gameObject = gameObject;
        this.usersName = gameObject.getUsersName();

        userName.setText("Name: " + gameObject.getUsersName());

        add(scoreLabel);
        add(userName);
        add(leaveGameButton);
    }

    public void joinServer(){
        gameObject.addGameOverListener((e) ->{
            JOptionPane.showMessageDialog(null,
                    String.format(
                            "My name: %s Winners name: %s \nI %s",
                            usersName, e.getWinningPlayerName(),
                            usersName.equals(e.getWinningPlayerName())? "Won":"Lost"
                    ));
            remove(leaveGameButton);

            viewScoreboardButton.addActionListener((e2) -> {
                HashMap<String,Integer> highScores;
                try{
                    highScores = gameObject.getLeaderBoard();
                }catch (Exception ex){
                    highScores = new HashMap<>();
                }

                mainWindow.switchToLeaderBoard(highScores);
            });

            add(viewScoreboardButton);

            //Update main window
            mainWindow.revalidate();
            mainWindow.repaint();
        });

        gamePanel.addRePaintRequestListener((e) -> {
            mainWindow.revalidate();
            mainWindow.repaint();
            mainWindow.pack();
        });

        gamePanel.addJoinServerFailedListener((e) -> {
            JOptionPane.showMessageDialog(null,e.getConnectionFailureReason());
            mainWindow.switchToStartScreen();
        });

        leaveGameButton.addActionListener((e) ->{
            try{
                gameObject.leaveGame();
                mainWindow.switchToStartScreen();
            }catch (Exception ex1){
                JOptionPane.showMessageDialog(null,"Something went wrong when trying to leave the game: " +
                ex1.getMessage());
            }
        });

        add(gamePanel);
        gamePanel.joinServer();
        this.gameGrid = gameObject.getGameGrid();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try{
            Player userPlayer = gameGrid.getPlayerOnGrid(usersName);
            String scoreString = String.format("Score: %s", userPlayer.getScore());
            scoreLabel.setText(scoreString);
        }catch (Exception e){
            System.out.println(String.format(
                    "==== NOT ON GRID ==== Name: %s Reason: %s", usersName,e.getMessage()
            ));
        }
    }
}
