package com.company;

import java.awt.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;

public class LightCyclesGame {
    GameGrid gameGrid;
    ArrayList<Player> playerList;
    String multicastAddress = "239.69.69.69";
    int multicastPort = 56969;
    public LightCyclesGame(Dimension gridDimensions){
        gameGrid = new GameGrid(gridDimensions);
        playerList = new ArrayList<>();

        startGame();

    }

    public void startGame(){
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
            byte[] incomingBuffer = new byte[1024];
            DatagramSocket socket = new DatagramSocket(56971);
            DatagramPacket incomingRequest = new DatagramPacket(incomingBuffer,incomingBuffer.length);
            socket.receive(incomingRequest);
            System.out.println("Got it");

            String clientRequest = new String(incomingBuffer);
            String[] requestComponents = clientRequest.split(" ");

            System.out.println(clientRequest);


            if(clientRequest.contains("ADD USER")){
                addPlayerToGame(requestComponents[2]);
            }

        }catch (Exception e){

        }
    }

    private void addPlayerToGame(String playerName){
        Player newPlayer = new Player(playerName);
        playerList.add(newPlayer);
    }
}
