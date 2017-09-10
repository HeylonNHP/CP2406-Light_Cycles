package com.company;

public class JetWall {
    private Player parentPlayer;
    public JetWall(Player parentPlayer){
        this.parentPlayer = parentPlayer;
    }

    public Player getParentPlayer() {
        return parentPlayer;
    }
}
