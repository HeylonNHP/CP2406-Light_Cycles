package com.company;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static JFrame mainWindow;
    public static GridLayout mainWindowGridLayout;
    public static void main(String[] args) {
        // write your code here
        mainWindow = new JFrame();
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.pack();

        Dimension windowSize = new Dimension();
        windowSize.width = 300;
        windowSize.height = 200;

        mainWindow.setSize(windowSize);

        mainWindowGridLayout = new GridLayout(0,2);
        mainWindow.setLayout(mainWindowGridLayout);

        JLabel widthLabel = new JLabel("Grid width");
        JTextField widthTextInput = new JTextField();
        widthTextInput.setText("500");
        JLabel heightLabel = new JLabel("Grid height");
        JTextField heightTextInput = new JTextField();
        heightTextInput.setText("500");

        mainWindow.add(widthLabel);
        mainWindow.add(widthTextInput);
        mainWindow.add(heightLabel);
        mainWindow.add(heightTextInput);

        JButton startGameButton = new JButton("Start game!");
        mainWindow.add(startGameButton);


        mainWindow.setVisible(true);
    }
}
