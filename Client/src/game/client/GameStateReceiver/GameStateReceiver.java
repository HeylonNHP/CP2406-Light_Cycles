package game.client.GameStateReceiver;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class GameStateReceiver extends Thread {
    private EventListenerList listenerList = new EventListenerList();

    private MulticastSocket multicastSocket;
    private boolean running = true;
    public GameStateReceiver(){

    }
    @Override
    public void run(){
        final String multicastAddress = "239.69.69.69";
        final int multicastPort = 56969;
        try{
            multicastSocket = new MulticastSocket(multicastPort);
            InetAddress multicastGroup = InetAddress.getByName(multicastAddress);
            multicastSocket.joinGroup(multicastGroup);
        }catch (IOException e){
            System.out.println("GameStateReceiver - failed to create socket!!!\n" +
                    "The game won't be able to listen to the server for game updates!!!");
            return;
        }

        while (true){
            try{
                if(!running){
                    return;
                }
                byte[] dataBuffer = new byte[1024];
                DatagramPacket receivedPacket = new DatagramPacket(dataBuffer,dataBuffer.length);
                multicastSocket.receive(receivedPacket);

                //System.out.println(receivedPacket.getAddress().toString());

                String bufferString = new String(dataBuffer);

                GameState gameState = interpretGameStateFromString(bufferString);
                GameStateUpdated newUpdate = new GameStateUpdated(this, gameState);
                raiseGameStateUpdated(newUpdate);
            }catch (Exception e){
                System.out.println("Game state receiver stopped");
            }
        }
    }

    private static GameState interpretGameStateFromString(String stateData){
        //System.out.printf("Game state: %s\n", stateData);
        GameState gameState = new GameState();
        //Remove any whitespace of null chars at the beginning or end
        stateData = stateData.trim();

        if(stateData.equals("")){
            return gameState;
        }

        String[] perPlayerData = stateData.split(" ");

        for(String playerData: perPlayerData){
            String[] playerAttributes = playerData.split(",");
            String playerName = playerAttributes[0];
            int xPos = Integer.parseInt(playerAttributes[1]);
            int yPos = Integer.parseInt(playerAttributes[2]);
            boolean jetWallEnabled = (playerAttributes[3].equals("on"));
            gameState.addPlayer(playerName,xPos,yPos,jetWallEnabled);
        }
        return gameState;
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

    public void close(){
        running = false;
        listenerList = null;
        multicastSocket.close();
    }
}
