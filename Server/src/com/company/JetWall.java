package com.company;

enum JetWallDirection {HORIZONTAL,VERTICAL}

public class JetWall {
    Player parentPlayer;
    JetWallDirection direction;
    public JetWall(Player parentPlayer, JetWallDirection direction){
        this.parentPlayer = parentPlayer;
        this.direction = direction;
    }

    public Player getParentPlayer() {
        return parentPlayer;
    }
}
