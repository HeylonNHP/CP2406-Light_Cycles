package com.company;

import java.awt.*;
import java.util.ArrayList;

public class GameGrid {
    private Object[][] gridArray;
    private Dimension gridSize;
    public GameGrid(Dimension dimensions){
        generateGridArray(dimensions);
        gridSize = dimensions;
    }

    private void generateGridArray(Dimension gridDimensions){
        gridArray = new Object[gridDimensions.width][gridDimensions.height];
    }

    public Dimension getGridSize(){
        return gridSize;
    }

    public void addPlayerAtRandomPosition(Player player){
        /*Used to initially add a player to the grid
        * The player will be facing away from the outer wall
        * and will be placed into a random position that isn't
        * already occupied by another player/object on the grid*/
        int xPos;
        int yPos;
        int gridWidth = gridSize.width;
        int gridHeight = gridSize.height;

        xPos = (int)Math.round(Math.random() * gridWidth);
        yPos = (int)Math.round(Math.random() * gridHeight);

        while (isGridLocationOccupied(xPos,yPos)){
            xPos = (int)Math.round(Math.random() * gridWidth);
            yPos = (int)Math.round(Math.random() * gridHeight);
        }

        /*Set player orientation so that it points away from the wall*/
        if(xPos > (gridWidth/2)){
            //Player is closer to the right
            player.setDirection(PlayerDirection.LEFT);
        }else {
            //Player is closer to the left
            player.setDirection(PlayerDirection.RIGHT);
        }

        if(yPos > (gridHeight/2)){
            //Player is closer to the bottom
            player.setDirection(PlayerDirection.UP);
        }else {
            //Player is closer to the top
            player.setDirection(PlayerDirection.DOWN);
        }

        System.out.println(String.format("addPlayerAtRandomPosition - Adding player at x: %s y: %s",xPos,yPos));

        setGridLocationToItem(player,xPos,yPos);
    }

    public void removePlayerFromGrid(Player player){
        /*Remove player and all of their jet wall from the grid*/
        for(int x = 0; x < gridSize.width; x++) {
            for (int y = 0; y < gridSize.height; y++) {
                Object currentObject = gridArray[x][y];
                if(currentObject instanceof Player){
                    if(currentObject == player){
                        setGridLocationToEmpty(x,y);
                    }
                }else if (currentObject instanceof JetWall){
                    JetWall currentJetWall = (JetWall)currentObject;
                    if(currentJetWall.getParentPlayer() == player){
                        setGridLocationToEmpty(x,y);
                    }
                }
            }
        }
    }

    public Dimension getLocationOfItemOnGrid(Object item) throws Exception{
        /*Searches the grid by reference for the first occurrence of item and returns the position
         * Will throw an exception if the item isn't found on the grid */
        for(int x = 0; x < gridSize.width; x++){
            for(int y = 0; y < gridSize.height; y++){
                if(gridArray[x][y] == item){
                    Dimension itemLocation = new Dimension();
                    itemLocation.setSize(x,y);
                    return itemLocation;
                }
            }
        }
        throw new Exception("Item not found on grid!");
    }

    private boolean isGridLocationOccupied(int x, int y){
        if(gridArray[x][y] == null){
            return false;
        }else{
            return true;
        }
    }

    private void setGridLocationToItem(Object item, int x, int y){
        gridArray[x][y] = item;
    }

    private void setGridLocationToEmpty(int x, int y){
        gridArray[x][y] = null;
    }

    public void progressGame(){
        /*This method when called will be responsible for:
        * Moving the light cycles (Player) on the grid according to their speed and direction
        * Creating the jet wall (JetWall) behind the light cycles as they move*/

        /*Used for keeping track of players that have already been moved in this turn
        * so they don't accidentally get moved again if the for loop encounters its new position*/
        ArrayList<Player> alreadyMovedPlayersList = new ArrayList<>();

        for (int x = 0; x < gridSize.width; x++){
            for (int y = 0; y < gridSize.height; y++){

                Object currentObject = gridArray[x][y];

                if(currentObject instanceof Player){
                    Player player = (Player)currentObject;

                    //Determine by how much to move the player
                    int movementDistance;
                    switch (player.getMovingSpeed()){
                        case FAST:
                            movementDistance = 2;
                            break;
                        case SLOW:
                            movementDistance = 1;
                            break;
                        default:
                            movementDistance = 0;
                            break;
                    }

                    //Determine the players new x/y coords based on its direction and speed
                    System.out.println("progressGame - Player direction currently is " + player.getDirection().toString());
                    int newXposition = x;
                    int newYposition = y;
                    switch (player.getDirection()){
                        case LEFT:
                            newXposition = x-movementDistance;
                            newYposition = y;
                            break;
                        case RIGHT:
                            newXposition = x+movementDistance;
                            newYposition = y;
                            break;
                        case DOWN:
                            newXposition = x;
                            newYposition = y+movementDistance;
                            break;
                        case UP:
                            newXposition = x;
                            newYposition = y-movementDistance;
                    }

                    //Move player to new position
                    if(player.getMovingSpeed() != PlayerSpeed.STOPPED && !alreadyMovedPlayersList.contains(player)){
                        /*If the target location on the grid is occupied,
                         or the target location is outside of the grid
                         the player gets removed from grid because it has crashed*/
                        if(newXposition == gridSize.width
                                || newXposition < 0
                                || newYposition == gridSize.height
                                || newYposition < 0
                                || isGridLocationOccupied(newXposition,newYposition)){
                            removePlayerFromGrid(player);
                            System.out.println(String.format("\n ------Player %s has crashed at position x: %s y: %s------ \n",
                                    player.getName(), x,y));
                            continue;
                        }

                        setGridLocationToItem(player,newXposition,newYposition);
                        alreadyMovedPlayersList.add(player);
                        if(player.isJetWallEnabled()){
                            JetWall jetWall = new JetWall(player);
                            setGridLocationToItem(jetWall,x,y);
                        }else{
                            setGridLocationToEmpty(x,y);
                        }
                        System.out.println(String.format("progressGame - Found Player %s at x: %s y: %s\n" +
                                "moving to new position at x: %s y: %s", player.getName(),x,y, newXposition, newYposition));
                    }
                }
            }
        }
    }
}
