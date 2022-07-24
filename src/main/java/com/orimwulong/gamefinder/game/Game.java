package com.orimwulong.gamefinder.game;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class Game {

    private final String name;
    private long totalMinutesPlayed;
    private final String imgIconUrl;
    private final Set<String> onPlatforms;

    public Game(String name, long totalMinutesPlayed, String imgIconUrl, String platformName) {
        this.name = name;
        this.totalMinutesPlayed = totalMinutesPlayed;
        this.imgIconUrl = imgIconUrl;
        this.onPlatforms = new HashSet<>();
        this.onPlatforms.add(platformName);
    }

    public String getName() {
        return name;
    }

    public long getTotalMinutesPlayed() {
        return totalMinutesPlayed;
    }

    public String getImgIconUrl() {
        return imgIconUrl;
    }

    public ImmutableSet<String> getOnPlatforms() {
        return ImmutableSet.copyOf(onPlatforms);
    }

    public void addPlayedMinutes(long minutesToAdd) {
        this.totalMinutesPlayed += minutesToAdd;
    }

    public boolean addPlatforms(Set<String> platformNames) {
        return this.onPlatforms.addAll(platformNames);
    }

    public boolean isOnPlatform(String platformName) {
        if (this.onPlatforms == null) {
            return false;
        }
        return this.onPlatforms.contains(platformName);
    }

    @Override
    public String toString() {
        return "Game [name=" + name + ", onPlatforms=" + onPlatforms + ", totalMinutesPlayed=" + totalMinutesPlayed + ", imgIconUrl=" + imgIconUrl + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((onPlatforms == null) ? 0 : onPlatforms.hashCode());
        result = prime * result + (int) (totalMinutesPlayed ^ (totalMinutesPlayed >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Game other = (Game) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

}
