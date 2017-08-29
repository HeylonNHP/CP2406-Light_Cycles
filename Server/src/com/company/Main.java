package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
    public static JFrame mainWindow;
    public static GridLayout mainWindowGridLayout;
    static JTextField widthTextInput = new JTextField();
    static JTextField heightTextInput = new JTextField();
    public static void main(String[] args) {
        // write your code here
        mainWindow = new JFrame();
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.pack();

        Dimension windowSize = new Dimension();
        windowSize.width = 350;
        windowSize.height = 120;

        mainWindow.setSize(windowSize);

        mainWindowGridLayout = new GridLayout(0,2);
        mainWindow.setLayout(mainWindowGridLayout);

        JLabel widthLabel = new JLabel("Grid width");

        widthTextInput.setText("500");
        JLabel heightLabel = new JLabel("Grid height");

        heightTextInput.setText("500");

        mainWindow.add(widthLabel);
        mainWindow.add(widthTextInput);
        mainWindow.add(heightLabel);
        mainWindow.add(heightTextInput);

        JButton startGameButton = new JButton("Start server!");

        startGameButton.addActionListener(e -> {startServerHandler(e);});

        mainWindow.add(startGameButton);


        mainWindow.setVisible(true);
    }

    private static Dimension getGameGridDimensionsFromUI(){
        Dimension chosenDimensions = new Dimension();
        chosenDimensions.width = Integer.parseInt(widthTextInput.getText());
        chosenDimensions.height = Integer.parseInt(heightTextInput.getText());
        return chosenDimensions;
    }

    private static void startServerHandler(ActionEvent e){
        System.out.println("Button clicked!");
        System.out.println(String.format("Width: %s Height: %s", widthTextInput.getText(), heightTextInput.getText()));
        LightCyclesGame newGame = new LightCyclesGame(getGameGridDimensionsFromUI());
    }
}
