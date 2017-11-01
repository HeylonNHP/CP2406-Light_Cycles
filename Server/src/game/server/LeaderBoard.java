package game.server;

import java.util.ArrayList;

public class LeaderBoard {
    /*private HashMap<String,Integer> highScores = new HashMap<>();*/
    private ArrayList<HighScore> highScores = new ArrayList<>();
    public LeaderBoard(){

    }

    public void addHighScore(HighScore highScore){
        highScores.add(highScore);
    }

    public ArrayList<HighScore> getHighScores() {
        return highScores;
    }
}
