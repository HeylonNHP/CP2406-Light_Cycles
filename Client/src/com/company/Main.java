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

        //Ask user for their name, and add them to the server
        System.out.print("Enter the name you'd like to use: ");
        String usersName = userInput.nextLine();

        LightCyclesGame newGame = new LightCyclesGame(usersName);

        try {
            newGame.joinServer();


            //TEST code for testing out the game from the console
            String commandsList = "What would you like to do?\n" +
                    "Turn: left, right\n" +
                    "Speed: fast, slow\n" +
                    "Jetwall: on, off";
            while (true){
                System.out.println(commandsList);
                System.out.print(">>> ");
                String userChoice = userInput.nextLine();

                switch (userChoice.toLowerCase()){
                    case "left":
                        newGame.turnLeft();
                        break;
                    case "right":
                        newGame.turnRight();
                        break;
                    case "fast":
                        newGame.beginMovingQuickly();
                        break;
                    case "slow":
                        newGame.beginMovingSlowly();
                        break;
                    case "on":
                        newGame.turnOnJetwall();
                        break;
                    case "off":
                        newGame.turnOffJetwall();
                        break;
                }
            }

        }catch (Exception e){
            System.out.println(String.format("Couldn't join server because: %s", e.getMessage()));
        }
    }
}
