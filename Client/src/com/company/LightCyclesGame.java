package com.company;

import com.company.GameStateReceiver.GameState;
import com.company.GameStateReceiver.GameStateReceiver;
import com.company.GameStateReceiver.GameStateUpdated;
import com.company.GameStateReceiver.PlayerState;
import com.company.VisibleGameObjects.GameGrid;

import java.awt.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

enum CurrentGameState {IDLE, WAITING_FOR_USERS, PLAYING, GAME_OVER}

public class LightCyclesGame {
    private GameStateReceiver receiver = new GameStateReceiver();
    private GameGrid gameGrid;
    private ServerRequester serverRequester = new ServerRequester();
    private String usersName;
    public LightCyclesGame(String userName){
        this.usersName = userName;
        receiver.addGameStateUpdateListener(e -> {receivedNewGameState(e);});
        receiver.start();
    }

    private void receivedNewGameState(GameStateUpdated e){
        GameState gameState = e.getGameState();
        for(PlayerState playerState: gameState.getPlayerStates()){
            System.out.println(String.format("Player: %s x: %s y:%s jetwall enabled: %s",
                    playerState.getName(),playerState.getPosition().width,
                    playerState.getPosition().height, playerState.isJetwallEnabled()));
        }
    }

    private static String getServerResponse(String requestMessage){
        /*Sends a request to the game server and returns the response*/

        try {
            System.out.println("Sending " + requestMessage);
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

    private static CurrentGameState getServerGameState() throws Exception{
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

    public void joinServer() throws Exception{
        /*This method will ask the server to add you to the game, with the specified name
        * If the server is not waiting for players, it throws an exception
        * If the server didn't respond with OKAY, it throws an exception*/

        if(getServerGameState() == CurrentGameState.WAITING_FOR_USERS){
            String response = getServerResponse("ADD USER " + usersName);
            if(!response.equals("OKAY")){
                throw new Exception("The following issue occurred when trying to " +
                        "create a new user on the server: " + response);
            }
            //TESTING - delete code afterwards
            getServerResponse(String.format("USER %s GO slower", usersName));
            getServerResponse(String.format("USER %s TURN left", usersName));
            getServerResponse(String.format("USER %s TURN right", usersName));


        }else{
            throw new Exception("The server is not accepting new users at this time.");
        }
    }

    public void turnLeft(){
        /*Request server to turn your player left*/
        //getServerResponse(String.format("USER %s TURN left", usersName));
        try{
            serverRequester.sendNonRespondingRequest(String.format("USER %s TURN left", usersName));
        }catch (Exception e){

        }
    }

    public void turnRight(){
        /*Request server to turn your player right*/
        //getServerResponse(String.format("USER %s TURN right", usersName));
        try{
            serverRequester.sendNonRespondingRequest(String.format("USER %s TURN right", usersName));
        }catch (Exception e){

        }
    }

    public void beginMovingSlowly(){
        /*Request server to set your player speed to slow*/
        //getServerResponse(String.format("USER %s GO slower", usersName));
        try{
            serverRequester.sendNonRespondingRequest(String.format("USER %s GO slower", usersName));
        }catch (Exception e){

        }
    }

    public void beginMovingQuickly(){
        /*Request server to set your player speed to fast*/
        //getServerResponse(String.format("USER %s GO faster", usersName));
        try{
            serverRequester.sendNonRespondingRequest(String.format("USER %s GO faster", usersName));
        }catch (Exception e){

        }
    }

    public void turnOnJetwall(){
        /*Request server to turn on your jet wall*/
        //getServerResponse(String.format("USER %s JETWALL on", usersName));
        try{
            serverRequester.sendNonRespondingRequest(String.format("USER %s JETWALL on", usersName));
        }catch (Exception e){

        }
    }

    public void turnOffJetwall(){
        /*Request server to turn off your jet wall*/
        //getServerResponse(String.format("USER %s JETWALL off", usersName));
        try{
            serverRequester.sendNonRespondingRequest(String.format("USER %s JETWALL off", usersName));
        }catch (Exception e){

        }
    }
}
