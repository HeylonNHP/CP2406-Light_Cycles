package com.company;

import java.awt.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;

enum CurrentGameState {IDLE, WAITING_FOR_USERS, PLAYING, GAME_OVER}

public class LightCyclesGame {
    GameGrid gameGrid;
    ArrayList<Player> playerList;
    CurrentGameState currentGameState = CurrentGameState.IDLE;
    String multicastAddress = "239.69.69.69";
    int multicastPort = 56969;
    LeaderBoard leaderBoard;
    public LightCyclesGame(Dimension gridDimensions){
        gameGrid = new GameGrid(gridDimensions);
        playerList = new ArrayList<>();
        leaderBoard = new LeaderBoard();

        startGame();

    }

    public void startGame(){
        currentGameState = CurrentGameState.PLAYING;
        broadcastGameState();
        Thread requestsThread = new Thread(() ->{
            startHandlingDirectRequests();
        });
        requestsThread.start();
    }

    private void broadcastGameState(){
        String message = "Jack,10,10 Jill,12,10 Tron,10,14";
        try{
            InetAddress multicastGroup = InetAddress.getByName(multicastAddress);
            MulticastSocket multicastSocket = new MulticastSocket(multicastPort);
            multicastSocket.joinGroup(multicastGroup);
            DatagramPacket packetToTransmit = new DatagramPacket(message.getBytes(), message.length(),multicastGroup,multicastPort);

            multicastSocket.send(packetToTransmit);

        }catch (Exception e){

        }


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
                    System.out.println("Got it");

                    String clientRequest = new String(incomingBuffer);
                    //Don't trim it and there will be null chars at the end, because the buffer has a set length
                    clientRequest = clientRequest.trim();
                    String[] requestComponents = clientRequest.split(" "); //Client request split into parts by a space character

                    System.out.println(clientRequest);

                    String response = "";

                    if(clientRequest.contains("ADD USER")){
                        String userName = requestComponents[2];
                        addPlayerToGame(userName);
                        response = "OKAY";
                        System.out.println("Added user: " + userName);
                    }else if(clientRequest.contains("REMOVE USER")){
                        String userName = requestComponents[2];
                        removePlayerFromGame(userName);
                        response = "OKAY";
                        System.out.println(String.format("Removed user: %s Players in game: %s",userName, playerList.size()));
                    }else if(clientRequest.equals("GRID SIZE")){
                        Dimension gridDimensions = gameGrid.getGridSize();
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
                                response = "GAME OVER";
                                break;
                            case WAITING_FOR_USERS:
                                response = "WAITING FOR USERS";
                                break;
                        }
                    }else if(clientRequest.contains("SAVE SCORE")){
                        String scoreName = requestComponents[2];
                        int scoreValue = Integer.parseInt(requestComponents[3]);
                        leaderBoard.addHighScore(scoreName,scoreValue);
                        System.out.println(String.format("Added score for: %s Score: %s", scoreName,scoreValue));
                        response = "OKAY";
                    }else if(clientRequest.contains("USER")){
                        /*Do not respond to these requests*/
                        String userName = requestComponents[1];

                        if(requestComponents[2].equals("TURN")){
                            //Player requests to turn their light cycle
                            if(requestComponents[3].equals("left")){
                                System.out.println(String.format("Player %s turned %s", userName, "left"));
                            }else if(requestComponents[3].equals("right")){
                                System.out.println(String.format("Player %s turned %s", userName, "right"));
                            }
                        }else if(requestComponents[2].equals("GO")){
                            //Player requests to change the speed of their light cycle
                            if(requestComponents[3].equals("faster")){
                                System.out.println("Player " + userName + " has sped up");
                            }else if(requestComponents[3].equals("slower")){
                                System.out.println("Player " + userName + " has slowed down");
                            }
                        }else if(requestComponents[2].equals("JETWALL")){
                            //Player requests to turn their jet wall on or off
                            if(requestComponents[3].equals("on")){
                                System.out.println("Player " + userName + " turned their jetwall on");
                            }else if(requestComponents[3].equals("off")){
                                System.out.println("Player " + userName + " turned their jetwall off");
                            }
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

    private void addPlayerToGame(String playerName){
        Player newPlayer = new Player(playerName);
        playerList.add(newPlayer);
    }

    private void removePlayerFromGame(String playerName){
        for(Player player: playerList){
            if(player.getName().equals(playerName)){
                playerList.remove(player);
                break;
            }
        }
    }
}
