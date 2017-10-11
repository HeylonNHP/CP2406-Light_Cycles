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

    public void addPlayerToGrid(Player player, Dimension locationOnGrid){
        //Check to see if the player exists on the grid, and if so do nothing
        for(Player playerOnGrid: playerList){
            if(player == playerOnGrid){
                return;
            }
        }

        //Otherwise add the player to the grid
        playerList.add(player);
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

    public void addJetWallToGrid(JetWall jetWall){
        jetWallList.add(jetWall);
    }
}
