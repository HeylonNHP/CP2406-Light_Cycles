package com.company.GameStateReceiver;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class GameStateReceiver extends Thread {
    private EventListenerList listenerList = new EventListenerList();
    String multicastAddress = "239.69.69.69";
    int multicastPort = 56969;
    public GameStateReceiver(){

    }
    @Override
    public void run(){
        while (true){
            try{
                byte[] dataBuffer = new byte[1024];
                InetAddress multicastGroup = InetAddress.getByName(multicastAddress);
                MulticastSocket multicastSocket = new MulticastSocket(multicastPort);
                multicastSocket.joinGroup(multicastGroup);
                DatagramPacket receivedPacket = new DatagramPacket(dataBuffer,dataBuffer.length);
                multicastSocket.receive(receivedPacket);

                GameStateUpdated newUpdate = new GameStateUpdated(this);
                raiseGameStateUpdated(newUpdate);

                JOptionPane.showMessageDialog(null, new String(dataBuffer), "alert", JOptionPane.INFORMATION_MESSAGE);
                //JOptionPane outputWindow = new JOptionPane(new String(dataBuffer));
                //outputWindow.setVisible(true);
            }catch (Exception e){

            }
        }
    }

    public void addGameStateUpdateListener(GameStateUpdateListener e){
        listenerList.add(GameStateUpdateListener.class,e);
    }

    public void removeGameStateUpdateListener(GameStateUpdateListener e){
        listenerList.remove(GameStateUpdateListener.class,e);
    }

    private void raiseGameStateUpdated(GameStateUpdated e){
        GameStateUpdateListener[] listeners = listenerList.getListeners(GameStateUpdateListener.class);
        for (GameStateUpdateListener listener: listeners) {
            listener.GameStateUpdate(e);
        }
    }
}
