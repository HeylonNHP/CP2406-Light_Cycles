package com.company;

enum PlayerDirection {UP,DOWN,LEFT,RIGHT}

public class Player {
    String name;
    PlayerDirection direction;
    boolean jetWallEnabled;
    public Player(String name){
        this.name = name;
        setDirection(PlayerDirection.UP);
        enableJetWall();
    }

    public String getName(){
        return name;
    }

    public void setDirection(PlayerDirection direction){
        this.direction = direction;
    }

    public PlayerDirection getDirection(){
        return this.direction;
    }

    public void enableJetWall(){
        this.jetWallEnabled = true;
    }

    public void disableJetWall(){
        this.jetWallEnabled = false;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
