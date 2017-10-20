package game.client.VisibleGameObjects;

import java.awt.*;

public class Player {
    private String name;
    private Color colour;
    private Dimension position;
    private PlayerDirection direction;
    private boolean jetwallEnabled = true;
    private int score = 0;

    public Player(String name, Dimension initialPosition, PlayerDirection initialDirection){
        //Use a random colour
        this(name,initialPosition,initialDirection,new Color((int)(Math.random() * 0x1000000)));
    }

    public Player(String name, Dimension initialPosition, PlayerDirection initialDirection,
                  Color colour){
        this.name = name;
        this.position = initialPosition;
        this.direction = initialDirection;
        this.colour = colour;
    }

    private void incrementScore(){
        score += 10;
    }

    public int getScore() {
        return score;
    }

    public void setPosition(Dimension newPosition){
        //Set direction based on movement relative to last position
        if(position.width > newPosition.width){
            setDirection(PlayerDirection.LEFT);
        }else if(position.width < newPosition.width){
            setDirection(PlayerDirection.RIGHT);
        }else if(position.height > newPosition.height){
            setDirection(PlayerDirection.UP);
        }else if(position.height < newPosition.height){
            setDirection(PlayerDirection.DOWN);
        }

        //If moved with jetwall enabled update score
        if((position.width != newPosition.width || position.height != newPosition.height) &&
                isJetwallEnabled()){
            incrementScore();
        }

        //Set new position
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

    public Color getColour() {
        return colour;
    }

    public void draw(Graphics2D g){
        g.setColor(getColour());
        g.fillRect(position.width-1,position.height-1,3,3);
    }
}
