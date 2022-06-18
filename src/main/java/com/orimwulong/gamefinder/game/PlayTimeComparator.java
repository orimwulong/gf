package com.orimwulong.gamefinder.game;

import java.util.Comparator;

public class PlayTimeComparator implements Comparator<Game> {

    @Override
    public int compare(Game g1, Game g2) {
        return Long.compare(g2.getTotalMinutesPlayed(), g1.getTotalMinutesPlayed());
    }

}
