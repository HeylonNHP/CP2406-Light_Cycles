package com.company;

import com.company.GameStateReceiver.GameState;
import com.company.GameStateReceiver.GameStateReceiver;
import com.company.GameStateReceiver.GameStateUpdated;

import java.awt.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;

public class LightCyclesGame {
    String multicastAddress = "239.69.69.69";
    int multicastPort = 56969;
    GameStateReceiver receiver = new GameStateReceiver();
    public LightCyclesGame(){
        receiver.addGameStateUpdateListener(e -> {receivedNewGameState(e);});
        receiver.start();

        //test
        getServerResponse("ADD USER Heylon");

        getServerResponse("REMOVE USER Heylon");
    }

    public void receivedNewGameState(GameStateUpdated e){
        GameState gameState = e.getGameState();

        for (String playerName: gameState.getPlayerNames()){
            Dimension playerCoords = gameState.getPlayerCoordinates(playerName);
            System.out.println(String.format("Player: %s x: %s y:%s", playerName, playerCoords.width, playerCoords.height));
        }

        System.out.println("The event listener worked.");
    }

    public static String getServerResponse(String requestMessage){
        /*Sends a request to the game server and returns the response*/
        try {
            System.out.println("Sending");
            InetAddress destinationAddress = InetAddress.getByName("127.0.0.1");
            DatagramSocket socket = new DatagramSocket(56970);
            DatagramPacket packet = new DatagramPacket(requestMessage.getBytes(),requestMessage.length(),destinationAddress,56971);
            socket.send(packet);
            System.out.println("Sent");

            byte[] responseBuffer = new byte[1024];
            DatagramPacket responsePacket = new DatagramPacket(responseBuffer,responseBuffer.length);

            //Set the timeout incase we never receive a response
            socket.setSoTimeout(2 * 1000); //2 second timeout

            socket.receive(responsePacket);

            String responseString = new String(responseBuffer);
            responseString = responseString.trim();

            System.out.println(String.format("Server response: %s",responseString));

            //Testing only
            socket.close();
        }catch (Exception e){
            System.out.println("Getting server response failed." + e.getMessage());
        }

        return "";
    }
}
