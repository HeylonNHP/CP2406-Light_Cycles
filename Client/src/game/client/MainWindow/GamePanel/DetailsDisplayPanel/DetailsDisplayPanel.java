package game.client.MainWindow.GamePanel.DetailsDisplayPanel;

import game.client.HighScore;
import game.client.LightCyclesGame;
import game.client.MainWindow.GamePanel.GamePanel;
import game.client.MainWindow.Main;
import game.client.VisibleGameObjects.GameGrid;
import game.client.VisibleGameObjects.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DetailsDisplayPanel extends JPanel {
    JLabel scoreLabel = new JLabel("Score");
    JLabel userName = new JLabel("Name");
    JButton leaveGameButton = new JButton("Leave game");
    JButton viewScoreboardButton = new JButton("View scoreboard");
    JPanel hudPanel = new JPanel(new FlowLayout());
    Main mainWindow;
    GamePanel gamePanel;

    private String usersName;
    private int usersScore;
    private GameGrid gameGrid;
    private LightCyclesGame gameObject;
    public DetailsDisplayPanel(Main mainWindow, LightCyclesGame gameObject){
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.mainWindow = mainWindow;
        this.gamePanel = new GamePanel(gameObject);
        this.gameObject = gameObject;
        this.usersName = gameObject.getUsersName();

        userName.setText("Name: " + gameObject.getUsersName());
        hudPanel.add(scoreLabel);
        hudPanel.add(userName);
        hudPanel.add(leaveGameButton);
        add(hudPanel);
    }

    public void joinServer(){
        gameObject.addGameOverListener((e) ->{
            boolean hasWon = usersName.equals(e.getWinningPlayerName());

            if(hasWon) {
                boolean postScore = promptUserToPostScore();
                //System.out.printf("Post score response: %s\n", postScore);
                if(postScore){
                    //Post score to leaderboard
                    try{
                        gameObject.postScoreToLeaderBoard(usersScore);
                    }catch (Exception ex){
                        JOptionPane.showMessageDialog(this, "Failed to post score!");
                    }
                }
            }else{
                JOptionPane.showMessageDialog(this,
                        "<html>You lost. The game has ended.<br>" +
                                "Please proceed to hang your head in shame.</html>");
            }
            hudPanel.remove(leaveGameButton);

            viewScoreboardButton.addActionListener((e2) -> {
                ArrayList<HighScore> highScores;
                try{
                    highScores = gameObject.getLeaderBoard();
                }catch (Exception ex){
                    highScores = new ArrayList<>();
                }

                mainWindow.switchToLeaderBoard(highScores);
            });

            hudPanel.add(viewScoreboardButton);

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
            JOptionPane.showMessageDialog(null,"DetailsDisplayPanel: " +
                    e.getConnectionFailureReason());
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

    private boolean promptUserToPostScore(){
        Object[] options = {"Post score", "No thanks"};

        int choice = JOptionPane.showOptionDialog(this,
                "<html><b>You have won!</b><br>You may now post your score to the leader board.</html>",
                "You win!", JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE,null,
                options,options[0]);

        return choice == 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try{
            Player userPlayer = gameGrid.getPlayerOnGrid(usersName);
            usersScore = userPlayer.getScore();
            String scoreString = String.format("Score: %s", usersScore);
            scoreLabel.setText(scoreString);
        }catch (Exception e){
            System.out.println(String.format(
                    "==== NOT ON GRID ==== Name: %s Reason: %s", usersName,e.getMessage()
            ));
        }
    }
}
