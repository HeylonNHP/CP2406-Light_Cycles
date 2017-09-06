package com.company;

import java.awt.*;

enum JetWallDirection {HORIZONTAL,VERTICAL}

public class JetWall {
    Player parentPlayer;
    Dimension position;
    JetWallDirection direction;
    public JetWall(Player parentPlayer, Dimension initialPosition, JetWallDirection initialDirection){
        this.parentPlayer = parentPlayer;
        this.position = initialPosition;
        this.direction = initialDirection;
    }

    public Player getParentPlayer() {
        return parentPlayer;
    }

    public Dimension getPosition() {
        return position;
    }
}
