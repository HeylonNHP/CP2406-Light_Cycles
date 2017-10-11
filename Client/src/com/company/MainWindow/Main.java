package com.company.MainWindow;

import com.company.LightCyclesGame;
import com.company.MainWindow.GamePanel.GamePanel;
import com.company.MainWindow.JoinGamePanel.JoinGamePanel;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class Main extends JFrame{
    private JoinGamePanel startScreen = new JoinGamePanel();
    private GamePanel gameScreenPanel;
    public Main(){
        super("Light cycles game");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        startGame();

        startScreen.addJoinGameListener((e) -> {
            JOptionPane.showMessageDialog(this,e.getChosenPlayerName());
            LightCyclesGame newGame = new LightCyclesGame(e.getChosenPlayerName());
            gameScreenPanel = new GamePanel(newGame);

            gameScreenPanel.addJoinServerFailedListener((e1) ->{
                System.out.println("Hi");
                remove(gameScreenPanel);
                add(startScreen);
                repaint();
            });

            //Remove the join game panel from screen
            remove(startScreen);
            //Add game panel to screen
            add(gameScreenPanel);
            repaint();

            gameScreenPanel.joinServer();
        });

        add(startScreen);
        pack();
        setVisible(true);
    }
    public static void main(String[] args) {
        new Main();
    }

    public static void startGame(){
        /*INSTRCUTIONS FOR TESTING
        *
        * Please start the Server app first and click the Start Server! button on
        * the Server BEFORE starting this Client app
        * Read the Servers console output for updates on the player position and to
        * see whether the player crashed. The game state is also displayed in a GUI
        * message box that you can ignore. Enter your desired player commands into the
         * Clients console when prompted to control your light cycle in order to test it*/

        Scanner userInput = new Scanner(System.in);

        //Ask user for their name, and add them to the server
        System.out.print("Enter the name you'd like to use: ");
        String usersName = userInput.nextLine();

        LightCyclesGame newGame = new LightCyclesGame(usersName);

        try {
            newGame.joinServer();


            /*TEST code for testing out the game from the console
            * Please ignore the infinite loop warning this is intended
            * on being infinite for now*/
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
