package com.company;

import com.company.GameStateReceiver.GameState;
import com.company.GameStateReceiver.GameStateReceiver;
import com.company.GameStateReceiver.GameStateUpdated;

import java.awt.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;

enum CurrentGameState {IDLE, WAITING_FOR_USERS, PLAYING, GAME_OVER}

public class LightCyclesGame {
    GameStateReceiver receiver = new GameStateReceiver();
    public LightCyclesGame(){
        receiver.addGameStateUpdateListener(e -> {receivedNewGameState(e);});
        receiver.start();
        /*
        //test
        getServerResponse("ADD USER Heylon");

        getServerResponse("REMOVE USER Heylon");

        getServerResponse("GRID SIZE");

        getServerResponse("GAME STATE");

        getServerResponse("SAVE SCORE Heylon 160");

        getServerResponse("USER Heylon TURN left");
        getServerResponse("USER Heylon TURN right");

        getServerResponse("USER Heylon GO faster");
        getServerResponse("USER Heylon GO slower");

        getServerResponse("USER name JETWALL off");
        getServerResponse("USER name JETWALL on");

        try{
            System.out.println(getGameState());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        */
        try{
            joinServer("Heylon");
        }catch (Exception e){
            System.out.println(String.format(
                    "Something bad happened: %s", e.getMessage()
            ));
        }


    }

    private void receivedNewGameState(GameStateUpdated e){
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

            /*Code for receiving response from server*/
            byte[] responseBuffer = new byte[1024];
            DatagramPacket responsePacket = new DatagramPacket(responseBuffer,responseBuffer.length);

            //Set the timeout incase we never receive a response
            socket.setSoTimeout(2 * 1000); //2 second timeout

            try{
                socket.receive(responsePacket);
            }catch (Exception ex){
                //Response timed out :(
                socket.close();
                System.out.println(String.format("This happened: %s", ex.getMessage()));
                return "";
            }

            String responseString = new String(responseBuffer);
            responseString = responseString.trim();

            System.out.println(String.format("Server response: %s",responseString));

            //Testing only
            socket.close();

            return responseString;
        }catch (Exception e){
            System.out.println("Getting server response failed." + e.getMessage());
        }

        return "";
    }

    private static CurrentGameState getGameState() throws Exception{
        /*Asks the server for the current game state
        * Will throw an exception if the server doesn't respond*/
        String response = getServerResponse("GAME STATE");

        switch (response){
            case "IDLE":
                return CurrentGameState.IDLE;
            case "PLAYING":
                return CurrentGameState.PLAYING;
            case "GAME OVER":
                return CurrentGameState.GAME_OVER;
            case "WAITING FOR USERS":
                return CurrentGameState.WAITING_FOR_USERS;
            default:
                throw new Exception("The server did not respond.");
        }
    }

    public void joinServer(String yourPlayerName) throws Exception{
        /*This method will ask the server to add you to the game, with the specified name
        * If the server is not waiting for players, it throws an exception
        * If the server didn't respond with OKAY, it throws an exception*/

        if(getGameState() == CurrentGameState.WAITING_FOR_USERS){
            String response = getServerResponse("ADD USER Heylon");
            if(!response.equals("OKAY")){
                throw new Exception("The following issue occurred when trying to " +
                        "create a new user on the server: " + response);
            }
        }else{
            throw new Exception("The server is not accepting new users at this time.");
        }
    }
}
