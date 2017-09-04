package com.company;

import java.awt.*;

public class GameGrid {
    Object[][] gridArray;
    Dimension gridSize;
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
}
