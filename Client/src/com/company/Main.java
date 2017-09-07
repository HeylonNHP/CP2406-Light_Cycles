package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

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
        startGame();
        mainWindow.setVisible(true);
    }

    public static void startGame(){
        Scanner userInput = new Scanner(System.in);
        LightCyclesGame newGame = new LightCyclesGame();

        //Ask user for their name, and add them to the server
        System.out.print("Enter the name you'd like to use: ");
        String usersName = userInput.nextLine();
        try{
            newGame.joinServer(usersName);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
