package com.company;

enum PlayerDirection {UP,DOWN,LEFT,RIGHT}
enum PlayerSpeed {STOPPED,SLOW,FAST}

public class Player {
    String name;
    PlayerDirection direction;
    boolean jetWallEnabled;
    PlayerSpeed speed;
    public Player(String name){
        this.name = name;
        this.speed = PlayerSpeed.STOPPED;
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

    public void turnLeft(){
        switch (getDirection()){
            case UP:
                setDirection(PlayerDirection.LEFT);
                break;
            case LEFT:
                setDirection(PlayerDirection.DOWN);
                break;
            case DOWN:
                setDirection(PlayerDirection.RIGHT);
                break;
            case RIGHT:
                setDirection(PlayerDirection.UP);
                break;
        }
    }

    public void turnRight(){
        switch (getDirection()){
            case UP:
                setDirection(PlayerDirection.RIGHT);
                break;
            case RIGHT:
                setDirection(PlayerDirection.DOWN);
                break;
            case DOWN:
                setDirection(PlayerDirection.LEFT);
                break;
            case LEFT:
                setDirection(PlayerDirection.UP);
                break;
        }
    }

    public void enableJetWall(){
        this.jetWallEnabled = true;
    }

    public void disableJetWall(){
        this.jetWallEnabled = false;
    }

    public boolean isJetWallEnabled(){
        return jetWallEnabled;
    }

    public void setMovingSpeedSlow(){
        this.speed = PlayerSpeed.SLOW;
    }

    public void setMovingSpeedFast(){
        this.speed = PlayerSpeed.FAST;
    }

    public PlayerSpeed getMovingSpeed(){
        return this.speed;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
