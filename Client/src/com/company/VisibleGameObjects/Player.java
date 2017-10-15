package com.company.VisibleGameObjects;

import java.awt.*;

public class Player {
    private String name;
    private Dimension position;
    private PlayerDirection direction;
    private boolean jetwallEnabled = true;

    public Player(String name, Dimension initialPosition, PlayerDirection initialDirection){
        this.name = name;
        this.position = initialPosition;
        this.direction = initialDirection;
    }

    public void setPosition(Dimension newPosition){
        this.position = newPosition;
    }

    public Dimension getPosition() {
        return position;
    }

    public void setDirection(PlayerDirection newDirection){
        this.direction = newDirection;
    }

    public PlayerDirection getDirection() {
        return direction;
    }

    public boolean isJetwallEnabled() {
        return jetwallEnabled;
    }

    public void setJetwallEnabled(boolean jetwallEnabled) {
        this.jetwallEnabled = jetwallEnabled;
    }

    public String getName(){
        return this.name;
    }

    public void draw(Graphics2D g){
        g.setColor(Color.red);
        g.fillRect(position.width-1,position.height-1,3,3);
    }
}
