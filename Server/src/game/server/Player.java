package game.server;

enum PlayerDirection {UP,DOWN,LEFT,RIGHT}
enum PlayerSpeed {STOPPED,SLOW,FAST}

public class Player {
    private String name;
    private PlayerDirection direction;
    private boolean jetWallEnabled;
    private PlayerSpeed speed;
    private boolean isWinner = false;
    private boolean canMoveThisTurn = true;

    /*All these methods CANNOT be private despite what Intellij thinks
    * They're all being referenced from outside of this class*/
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
        canMoveThisTurn = false;
        this.speed = PlayerSpeed.SLOW;
    }

    public void setMovingSpeedFast(){
        canMoveThisTurn = true;
        this.speed = PlayerSpeed.FAST;
    }

    public PlayerSpeed getMovingSpeed(){
        return this.speed;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
    }

    public void setCanMoveThisTurn(boolean canMoveThisTurn) {
        this.canMoveThisTurn = canMoveThisTurn;
    }

    public boolean isCanMoveThisTurn() {
        return canMoveThisTurn;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
