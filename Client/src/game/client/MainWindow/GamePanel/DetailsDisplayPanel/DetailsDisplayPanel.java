package game.client.MainWindow.GamePanel.DetailsDisplayPanel;

import game.client.LightCyclesGame;
import game.client.MainWindow.GamePanel.GamePanel;
import game.client.MainWindow.Main;

import javax.swing.*;
import java.awt.*;

public class DetailsDisplayPanel extends JPanel {
    JLabel scoreLabel = new JLabel("Score");
    JLabel userName = new JLabel("Name");
    JButton leaveGameButton = new JButton("Leave game");
    Main mainWindow;
    GamePanel gamePanel;
    public DetailsDisplayPanel(Main mainWindow, LightCyclesGame gameObject){
        //super(new GridLayout(2,3));
        super(new FlowLayout());
        this.mainWindow = mainWindow;
        this.gamePanel = new GamePanel(gameObject);

        add(scoreLabel);
        add(userName);
        add(leaveGameButton);
    }

    public void joinServer(){
        gamePanel.addRePaintRequestListener((e) -> {
            mainWindow.revalidate();
            mainWindow.repaint();
            mainWindow.pack();
        });

        gamePanel.addJoinServerFailedListener((e) -> {
            JOptionPane.showMessageDialog(null,e.getConnectionFailureReason());
            mainWindow.switchToStartScreen();
        });

        add(gamePanel);
        gamePanel.joinServer();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    }
}
