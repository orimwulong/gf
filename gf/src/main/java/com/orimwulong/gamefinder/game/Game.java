package com.orimwulong.gamefinder.game;

import java.util.HashSet;
import java.util.Set;

public class Game {

    private String name;
    private long totalMinutesPlayed;
    private Set<String> onPlatform;

    public Game(String name, long totalMinutesPlayed) {
        this.name = name;
        this.totalMinutesPlayed = totalMinutesPlayed;
    }

    public String getName() {
        return name;
    }

    public long getTotalMinutesPlayed() {
        return totalMinutesPlayed;
    }

    public void addPlayedMinutes(long minutesToAdd) {
        this.totalMinutesPlayed += minutesToAdd;
    }

    public boolean addPlatform(String platformName) {
        if (this.onPlatform == null) {
            this.onPlatform = new HashSet<String>();
        }
        return this.onPlatform.add(platformName);
    }

    public boolean isOnPlatform(String platformName) {
        if (this.onPlatform == null) {
            return false;
        }
        return this.onPlatform.contains(platformName);
    }

    @Override
    public String toString() {
        return "Game [name=" + name + ", onPlatform=" + onPlatform + ", totalMinPlayed=" + totalMinutesPlayed + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((onPlatform == null) ? 0 : onPlatform.hashCode());
        result = prime * result + (int) (totalMinutesPlayed ^ (totalMinutesPlayed >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Game other = (Game) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
