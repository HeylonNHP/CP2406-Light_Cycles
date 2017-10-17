package com.company.VisibleGameObjects;

import java.awt.*;
import java.awt.color.ColorSpace;

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
        Color jetwallColor = getParentPlayer().getColour();

        int red = (int)(jetwallColor.getRed() / 2f);
        int green = (int)(jetwallColor.getGreen() / 2f);
        int blue = (int)(jetwallColor.getBlue() / 2f);

        jetwallColor = new Color(red,green,blue);

        g.setColor(jetwallColor);
        if(direction == JetWallDirection.HORIZONTAL){
            g.fillRect(position.width-5,position.height-2,10,4);
        }else {
            g.fillRect(position.width-2,position.height-5,4,10);
        }

    }
}
