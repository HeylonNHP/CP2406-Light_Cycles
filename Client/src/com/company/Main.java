package com.company;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static JFrame mainWindow;
    public static void main(String[] args) {
	// write your code here
        mainWindow = new JFrame();
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.pack();

        Dimension windowSize = new Dimension();
        windowSize.width = 500;
        windowSize.height = 500;

        mainWindow.setSize(windowSize);

        LightCyclesGame newGame = new LightCyclesGame();

        mainWindow.setVisible(true);
    } 
}
