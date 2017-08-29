package com.company;

import javax.swing.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class LightCyclesGame {
    String multicastAddress = "239.69.69.69";
    int multicastPort = 56969;
    public LightCyclesGame(){
        receiveGameState();
    }

    private void receiveGameState(){
        try{
            byte[] dataBuffer = new byte[1024];
            InetAddress multicastGroup = InetAddress.getByName(multicastAddress);
            MulticastSocket multicastSocket = new MulticastSocket(multicastPort);
            multicastSocket.joinGroup(multicastGroup);
            DatagramPacket receivedPacket = new DatagramPacket(dataBuffer,dataBuffer.length);
            multicastSocket.receive(receivedPacket);

            JOptionPane.showMessageDialog(null, new String(dataBuffer), "alert", JOptionPane.INFORMATION_MESSAGE);
            //JOptionPane outputWindow = new JOptionPane(new String(dataBuffer));
            //outputWindow.setVisible(true);
        }catch (Exception e){

        }
    }
}
