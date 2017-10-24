package game.client.VisibleGameObjects;

import javax.swing.event.EventListenerList;
import java.awt.*;
import java.util.ArrayList;
import java.util.EventObject;

public class GameGrid {
    EventListenerList listenerList = new EventListenerList();
    Dimension gridDimensions;
    ArrayList<Player> playerList = new ArrayList<>();
    ArrayList<JetWall> jetWallList = new ArrayList<>();
    public GameGrid(Dimension gridSize){
        gridDimensions = gridSize;
    }

    public void addPlayerToGrid(Player player){
        //Check to see if the player exists on the grid, and if so do nothing
        for(Player playerOnGrid: playerList){
            if(player == playerOnGrid){
                return;
            }
        }

        //Otherwise add the player to the grid
        playerList.add(player);
        raiseGridUpdatedListener();
    }

    public void removePlayerFromGrid(Player player){
        /*Remove the requested player, and their jetwall from the grid*/
        ArrayList<JetWall> jetwallsToRemove = new ArrayList<>();
        //Remove their jetwall
        for (JetWall jetWall: jetWallList){
            if(jetWall.getParentPlayer() == player){
                jetwallsToRemove.add(jetWall);
            }
        }

        for(JetWall jetWall: jetwallsToRemove){
            jetWallList.remove(jetWall);
        }

        //Remove player
        playerList.remove(player);
        raiseGridUpdatedListener();
    }

    public Player getPlayerOnGrid(String playerName) throws Exception{
        for(Player player: playerList){
            String name = player.getName();
            if(name.equals(playerName)){
                return player;
            }
        }
        throw new Exception("Player not found on the grid!");
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public void addJetWallToGrid(JetWall jetWall){
        jetWallList.add(jetWall);
        raiseGridUpdatedListener();
    }

    public Dimension getGridDimensions() {
        return gridDimensions;
    }

    public void draw(Graphics2D g){
        final int squareDist = 5;
        final int squareSize = 50;
        //Draw the grid background
        g.setColor(Color.black);
        g.fillRect(0,0,gridDimensions.width,gridDimensions.height);

        g.setColor(new Color(0,0,100));
        //draw squares
        for(int x = squareDist; x < gridDimensions.width; x += squareSize + squareDist){
            for (int y = squareDist; y < gridDimensions.height; y += squareSize + squareDist){
                g.fillRect(x,y,squareSize,squareSize);
            }
        }


        //Draw jetwalls
        for(JetWall jetWall: jetWallList){
            jetWall.draw(g);
        }

        //Draw light cycles
        for(Player player: playerList){
            player.draw(g);
        }
    }

    public void addGridUpdatedListener(GameGridUpdatedEvent e){
        listenerList.add(GameGridUpdatedEvent.class,e);
    }

    private void raiseGridUpdatedListener(){
        EventObject e = new EventObject(this);
        for(GameGridUpdatedEvent listener: listenerList.getListeners(GameGridUpdatedEvent.class)){
            listener.gameGridUpdated(e);
        }
    }
}
