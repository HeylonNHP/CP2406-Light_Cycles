package com.company;

import java.awt.*;
import java.net.DatagramPacket;
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
    }

    public void startGame(){

    }

    private void broadcastGameState(){
        String message = "Hello world!!!!!!!!!";
        try{
            InetAddress multicastGroup = InetAddress.getByName(multicastAddress);
            MulticastSocket multicastSocket = new MulticastSocket(multicastPort);
            multicastSocket.joinGroup(multicastGroup);
            DatagramPacket packetToTransmit = new DatagramPacket(message.getBytes(), message.length(),multicastGroup,multicastPort);

            multicastSocket.send(packetToTransmit);
            
        }catch (Exception e){

        }


    }
}