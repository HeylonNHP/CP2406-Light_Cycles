package com.company;

import java.awt.*;

public class GameGrid {
    Object[][] gridArray;
    public GameGrid(Dimension dimensions){
        generateGridArray(dimensions);
    }

    private void generateGridArray(Dimension gridDimensions){
        gridArray = new Object[gridDimensions.width][gridDimensions.height];
    }
}
