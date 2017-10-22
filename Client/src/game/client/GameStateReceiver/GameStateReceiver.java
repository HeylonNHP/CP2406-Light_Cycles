package game.client.GameStateReceiver;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class GameStateReceiver extends Thread {
    private EventListenerList listenerList = new EventListenerList();
    private final String multicastAddress = "239.69.69.69";
    private final int multicastPort = 56969;
    public GameStateReceiver(){

    }
    @Override
    public void run(){
        MulticastSocket multicastSocket;
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
                byte[] dataBuffer = new byte[1024];
                DatagramPacket receivedPacket = new DatagramPacket(dataBuffer,dataBuffer.length);
                multicastSocket.receive(receivedPacket);

                //System.out.println(receivedPacket.getAddress().toString());

                String bufferString = new String(dataBuffer);

                GameState gameState = interpretGameStateFromString(bufferString);
                GameStateUpdated newUpdate = new GameStateUpdated(this, gameState);
                raiseGameStateUpdated(newUpdate);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static GameState interpretGameStateFromString(String stateData){
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
        System.out.println("Game state event listener fired");
        GameStateUpdateListener[] listeners = listenerList.getListeners(GameStateUpdateListener.class);
        for (GameStateUpdateListener listener: listeners) {
            listener.GameStateUpdate(e);
        }
    }
}
