package com.company.VisibleGameObjects;

import java.awt.*;

public class JetWall {
    private Player parentPlayer;
    private Dimension position;
    private JetWallDirection direction;
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

    public void draw(Graphics2D g){
        g.setColor(Color.GREEN);
        if(direction == JetWallDirection.HORIZONTAL){
            g.fillRect(position.width-5,position.height-2,10,4);
        }else {
            g.fillRect(position.width-2,position.height-5,4,10);
        }

    }
}
