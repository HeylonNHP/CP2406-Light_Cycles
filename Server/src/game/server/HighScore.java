package game.server;

public class HighScore implements Comparable{
    private String playerName;
    private int playerScore;

    public HighScore(String playerName,int playerScore){
        setPlayerName(playerName);
        setPlayerScore(playerScore);
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    @Override
    public int compareTo(Object o) {
        HighScore highScore = (HighScore)o;

        return highScore.getPlayerScore()-getPlayerScore();
    }
}
