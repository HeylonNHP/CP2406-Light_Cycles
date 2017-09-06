package com.company;

import java.awt.*;

enum PlayerDirection {UP,DOWN,LEFT,RIGHT}

public class Player {
    String name;
    Dimension position;
    PlayerDirection direction;

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

    public String getName(){
        return this.name;
    }
}
