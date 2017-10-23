package game.server;

import javax.swing.*;
import java.awt.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.HashMap;

enum CurrentGameState {IDLE, WAITING_FOR_USERS, PLAYING, GAME_OVER}

public class LightCyclesGame {
    private GameGrid gameGrid;
    private ArrayList<Player> playerList;
    private CurrentGameState currentGameState = CurrentGameState.IDLE;
    private final String multicastAddress = "239.69.69.69";
    private final int multicastPort = 56969;
    private LeaderBoard leaderBoard;

    private Timer gameStartTimer;
    private final int playersRequiredForGameStart = 2;

    //I.E - if equals 10, there will be 10 pixels for every location on the grid
    private final int gridShrinkFactor = 10;

    public LightCyclesGame(Dimension gridDimensions){
        Dimension actualGridSize = new Dimension(gridDimensions.width/gridShrinkFactor,
                gridDimensions.height/gridShrinkFactor);
        gameGrid = new GameGrid(actualGridSize);
        playerList = new ArrayList<>();
        leaderBoard = new LeaderBoard();

        //Begin waiting for users to add themselves to the game
        currentGameState = CurrentGameState.WAITING_FOR_USERS;

        //Start listening for direct requests - for the time being we're waiting for users (clients) to add themselves to the game
        Thread requestsThread = new Thread(() ->{
            startHandlingDirectRequests();
        });
        requestsThread.start();

        //initialise game start timer
        gameStartTimer = new Timer(10000, (e) -> {
           startGame();
           gameStartTimer.stop();
        });
    }

    public void startGame(){
        currentGameState = CurrentGameState.PLAYING;
        Thread gameUpdateThread;
        gameUpdateThread = new Thread(() -> {

        //Begin progressing the game and broadcasting updates while the game is being played
            broadcastGameState();

            while (currentGameState == CurrentGameState.PLAYING){
                gameGrid.progressGame();

                broadcastGameState();

                try {
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    System.out.println(String.format("startGame - Interrupted: %s", e.getMessage()));
                }

            }

        });
        gameUpdateThread.start();
    }

    private void broadcastGameState(){
        String broadcastMessage = "";
        int playersLeftOnGrid = 0;
        for(Player player: playerList){
            try{
                String playerName = player.getName();
                //This will throw an exception if the player doesn't exist on the grid
                Dimension playerPosition = gameGrid.getLocationOfItemOnGrid(player);
                playersLeftOnGrid++;

                playerPosition = new Dimension(playerPosition.width*gridShrinkFactor,
                        playerPosition.height*gridShrinkFactor);
                String jetwallState;

                if(player.isJetWallEnabled()){
                    jetwallState = "on";
                }else {
                    jetwallState = "off";
                }

                broadcastMessage += String.format("%s,%s,%s,%s ",playerName, playerPosition.width,playerPosition.height, jetwallState);
            }catch (Exception e){
                System.out.println(String.format(
                        "Generating broadcast message: %s", e.getMessage()
                ));
            }
        }

        if(broadcastMessage.equals("")){
            //There are probably no players left on the grid if this is the case
            //End the game - might as well
            currentGameState = CurrentGameState.GAME_OVER;

            System.out.println("Empty broadcast message! Ending game.");
        }else if(playersLeftOnGrid <= 1 && playersLeftOnGrid < playersRequiredForGameStart){
            //If there's only one player left on the grid, and the game was set to start with more than one player - also end the game
            currentGameState = CurrentGameState.GAME_OVER;
            for(Player player:playerList){
                try{
                    gameGrid.getLocationOfItemOnGrid(player);
                    player.setWinner(true);
                }catch (Exception e){
                    System.out.printf("Player %s not found on grid - they must have lost\n", player.getName());
                }
            }
        }

        System.out.printf("Players currently on the grid: %s\n", playersLeftOnGrid);

        try{
            InetAddress multicastGroup = InetAddress.getByName(multicastAddress);
            MulticastSocket multicastSocket = new MulticastSocket(multicastPort);
            multicastSocket.joinGroup(multicastGroup);
            DatagramPacket packetToTransmit = new DatagramPacket(broadcastMessage.getBytes(), broadcastMessage.length(),multicastGroup,multicastPort);

            multicastSocket.send(packetToTransmit);

            multicastSocket.close();

        }catch (Exception e){
            System.out.println(String.format(
                    "Sending broadcast message: %s", e.getMessage()
            ));
        }
        System.out.println(String.format("Broadcast message: %s", broadcastMessage));
    }

    private void startHandlingDirectRequests(){
        /*Handle direct requests from the client and provide responses when appropriate
        E.G.
        Client request: USER name TURN left Response: None
        Client request: ADD USER name Response: OKAY
         */
            try{
                System.out.println("Receiving");
                DatagramSocket socket = new DatagramSocket(56971);

                while (true){
                    byte[] incomingBuffer = new byte[1024];
                    DatagramPacket incomingRequest = new DatagramPacket(incomingBuffer,incomingBuffer.length);
                    socket.receive(incomingRequest);
                    System.out.println("--- Received request ---");

                    String clientRequest = new String(incomingBuffer);
                    //Don't trim it and there will be null chars at the end, because the buffer has a set length
                    clientRequest = clientRequest.trim();
                    String[] requestComponents = clientRequest.split(" "); //Client request split into parts by a space character

                    System.out.println(clientRequest);

                    String response = "";

                    if(clientRequest.contains("ADD USER")){
                        if(currentGameState == CurrentGameState.WAITING_FOR_USERS){
                            String userName = requestComponents[2];
                            try{
                                addPlayerToGame(userName);
                                response = "OKAY";
                                System.out.println("Added user: " + userName);
                            }catch (Exception e){
                                response = "FAILED " + e.getMessage();
                                System.out.println("Player " + userName + " already exists");
                            }

                        }else{
                            response = "FAILED The server isn't accepting new players at this time";
                        }

                    }else if(clientRequest.contains("REMOVE USER")){
                        try{
                            String userName = requestComponents[2];
                            removePlayerFromGame(userName);
                            response = "OKAY";
                            System.out.println(String.format("Removed user: %s Players in game: %s",userName, playerList.size()));
                        }catch (Exception e){
                            response = "FAILED " + e.getMessage();
                        }
                    }else if(clientRequest.equals("GRID SIZE")){
                        Dimension gridDimensions = gameGrid.getGridSize();
                        gridDimensions = new Dimension(gridDimensions.width*gridShrinkFactor,
                                gridDimensions.height*gridShrinkFactor);

                        response = String.format("%s %s", gridDimensions.width, gridDimensions.height);
                    }else if(clientRequest.contains("GAME STATE")){
                        switch (currentGameState){
                            case IDLE:
                                response = "IDLE";
                                break;
                            case PLAYING:
                                response = "PLAYING";
                                break;
                            case GAME_OVER:
                                //Get winning player
                                boolean winnerFound = false;
                                for(Player player: playerList){
                                    if(player.isWinner()){
                                        response = "GAME OVER " + player.getName();
                                        winnerFound = true;
                                    }
                                }
                                if(!winnerFound){
                                    response = "GAME OVER -";
                                }
                                break;
                            case WAITING_FOR_USERS:
                                response = "WAITING FOR USERS";
                                break;
                        }
                    }else if(clientRequest.contains("SAVE SCORE")) {
                        String scoreName = requestComponents[2];
                        int scoreValue = Integer.parseInt(requestComponents[3]);
                        leaderBoard.addHighScore(scoreName, scoreValue);
                        System.out.println(String.format("Added score for: %s Score: %s", scoreName, scoreValue));
                        response = "OKAY";
                    }else if(clientRequest.contains("GET LEADERBOARD")){
                        //Send back all the scores on the leaderboard
                        HashMap<String,Integer> scores = leaderBoard.getHighScores();
                        if(scores.size() < 1){
                            response = "FAILED No scores on leaderboard yet";
                        }else{
                            response = "OKAY:";
                            for(String playerName:scores.keySet()){
                                response += String.format("%s,%s ", playerName,
                                        scores.get(playerName));
                            }
                        }

                    }else if(clientRequest.contains("USER")){
                        /*Do not respond to these requests*/
                        String userName = requestComponents[1]; //Is the player name the client specified
                        try{
                            Player player = getPlayerByName(userName);

                            if(requestComponents[2].equals("TURN")){
                                //Player requests to turn their light cycle
                                if(requestComponents[3].equals("left")){

                                        player.turnLeft();
                                        System.out.println(String.format("Player %s turned %s", userName, "left"));

                                }else if(requestComponents[3].equals("right")){

                                        player.turnRight();
                                        System.out.println(String.format("Player %s turned %s", userName, "right"));

                                }
                            }else if(requestComponents[2].equals("GO")){
                                //Player requests to change the speed of their light cycle
                                if(requestComponents[3].equals("faster")){

                                        player.setMovingSpeedFast();
                                        System.out.println("Player " + userName + " has sped up");

                                }else if(requestComponents[3].equals("slower")){

                                        player.setMovingSpeedSlow();
                                        System.out.println("Player " + userName + " has slowed down");

                                }
                            }else if(requestComponents[2].equals("JETWALL")){
                                //Player requests to turn their jet wall on or off
                                if(requestComponents[3].equals("on")){
                                    player.enableJetWall();
                                    System.out.println("Player " + userName + " turned their jetwall on");
                                }else if(requestComponents[3].equals("off")){
                                    player.disableJetWall();
                                    System.out.println("Player " + userName + " turned their jetwall off");
                                }
                            }
                        }catch (Exception e){
                            System.out.println("USER request - Something bad happened: " + e.getMessage());
                        }

                    }

                    if(!response.equals("")){
                        InetAddress clientAddress = incomingRequest.getAddress();
                        int clientPort = incomingRequest.getPort();
                        DatagramPacket responsePacket = new DatagramPacket(response.getBytes(),response.length(),clientAddress,clientPort);
                        socket.send(responsePacket);
                    }
                }
            }catch (Exception e){

            }
    }

    private void addPlayerToGame(String playerName) throws Exception{
        Player newPlayer = new Player(playerName);

        for(Player player:playerList){
            if(player.getName().equals(newPlayer.getName())){
                //Player with this name is already in the game
                throw new Exception("A player with this name already exists!");
            }
        }

        playerList.add(newPlayer);
        //Also add the player to the grid
        gameGrid.addPlayerAtRandomPosition(newPlayer);
        //TESTING
        if(playerList.size() > (playersRequiredForGameStart-1)){
            gameStartTimer.stop();
            gameStartTimer.start();
        }
    }

    private void removePlayerFromGame(String playerName) throws Exception{
        Player player = getPlayerByName(playerName);
        playerList.remove(player);
        gameGrid.removePlayerFromGrid(player);
    }

    private Player getPlayerByName(String playerName) throws Exception{
        for(Player player: playerList){
            if(player.getName().equals(playerName)){
                return player;
            }
        }
        throw new Exception("Cannot find a player with that name!");
    }
}
