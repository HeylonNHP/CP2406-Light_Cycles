package game.server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
    public static JFrame mainWindow;
    public static GridLayout mainWindowGridLayout;
    static JTextField widthTextInput = new JTextField();
    static JTextField heightTextInput = new JTextField();
    static JButton startGameButton = new JButton("Start server!");
    static LightCyclesGame newGame;
    public static void main(String[] args) {
        // write your code here
        mainWindow = new JFrame();
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.pack();

        Dimension windowSize = new Dimension();
        windowSize.width = 350;
        windowSize.height = 120;

        mainWindow.setSize(windowSize);

        mainWindowGridLayout = new GridLayout(0,2);
        mainWindow.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        //Make components fill their cells
        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;

        JLabel widthLabel = new JLabel("Grid width");

        widthTextInput.setText("500");
        JLabel heightLabel = new JLabel("Grid height");

        heightTextInput.setText("500");

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainWindow.add(widthLabel,gbc);
        gbc.gridy = 1;
        mainWindow.add(heightLabel,gbc);
        gbc.gridy = 0;
        gbc.gridx = 1;
        mainWindow.add(widthTextInput,gbc);
        gbc.gridy = 1;
        mainWindow.add(heightTextInput,gbc);

        startGameButton.addActionListener(e -> {startServerHandler(e);});

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        mainWindow.add(startGameButton,gbc);


        mainWindow.setVisible(true);
    }

    private static Dimension getGameGridDimensionsFromUI(){
        Dimension chosenDimensions = new Dimension();
        chosenDimensions.width = Integer.parseInt(widthTextInput.getText());
        chosenDimensions.height = Integer.parseInt(heightTextInput.getText());
        return chosenDimensions;
    }

    private static void startServerHandler(ActionEvent e){
        final String startNewGameString = "Start new game";
        final String gameRunningString = "Game is running";
        if (startGameButton.getText().equals(startNewGameString)){
            try{
                newGame.restartGame();
                startGameButton.setText(gameRunningString);
                startGameButton.setEnabled(false);
            }catch (Exception ex){
                JOptionPane.showMessageDialog(null, ex);
            }

        }else {
            System.out.println(String.format("Width: %s Height: %s", widthTextInput.getText(), heightTextInput.getText()));
            Dimension gameGridDimensions = getGameGridDimensionsFromUI();

            //Ensure they're divisible by 10
            if(gameGridDimensions.width % 10 != 0 || gameGridDimensions.height % 10 != 0){
                JOptionPane.showMessageDialog(null,
                        "Game grid dimensions must be divisible by 10");
                return;
            }

            newGame = new LightCyclesGame(getGameGridDimensionsFromUI());
            startGameButton.setText(gameRunningString);
            startGameButton.setEnabled(false);
            widthTextInput.setEnabled(false);
            heightTextInput.setEnabled(false);

            newGame.addGameOverListener((e1) -> {
                startGameButton.setText(startNewGameString);
                startGameButton.setEnabled(true);
            });
        }
    }
}
