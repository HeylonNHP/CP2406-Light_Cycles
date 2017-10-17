package com.company.VisibleGameObjects;

import java.awt.*;
import java.util.ArrayList;

public class GameGrid {
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
    }

    public Dimension getGridDimensions() {
        return gridDimensions;
    }

    public void draw(Graphics2D g){
        //Draw the grid background
        g.setColor(Color.blue);
        g.fillRect(0,0,gridDimensions.width,gridDimensions.height);

        //Draw jetwalls
        for(JetWall jetWall: jetWallList){
            jetWall.draw(g);
        }

        //Draw light cycles
        for(Player player: playerList){
            player.draw(g);
        }
    }
}
