package com.company;

public class Player {
    String name;
    public Player(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
