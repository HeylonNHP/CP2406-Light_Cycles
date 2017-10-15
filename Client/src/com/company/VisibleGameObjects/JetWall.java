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
        g.fillRect(position.width,position.height,1,1);
    }
}
