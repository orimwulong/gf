package com.orimwulong.gamefinder;

public class Game {

    private String name;
    private long totalMinPlayed;

    public Game(String name, long totalMinPlayed) {
        this.name = name;
        this.totalMinPlayed = totalMinPlayed;
    }

    public String getName() {
        return name;
    }

    public long getTotalMinPlayed() {
        return totalMinPlayed;
    }

    @Override
    public String toString() {
        return "Game [name=" + name + ", totalMinPlayed=" + totalMinPlayed + "]";
    }

}
