package com.orimwulong.gamefinder.game;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.orimwulong.gamefinder.util.TimeHelper;

public class GamesCollection {

    private static final Logger LOGGER = LoggerFactory.getLogger(GamesCollection.class);

    private SortedMap<String, Game> collection;
    private long totalPlayTime;

    public void addGame(Game game) {
        if (this.collection == null) {
            this.collection = new TreeMap<>();
        }
        String gameName = game.getName();
        if (this.collection.containsKey(game.getName())) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Adding played minutes to existing game [" + gameName + "]");
            }
            Game gameInCollection = this.collection.get(gameName);
            gameInCollection.addPlayedMinutes(game.getTotalMinutesPlayed());
            gameInCollection.addPlatforms(game.getOnPlatforms());
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Adding game to collection [" + gameName + "]");
            }
            this.collection.put(gameName, game);
        }
        this.totalPlayTime += game.getTotalMinutesPlayed();
    }

    public int getNumberOfGames() {
        if (this.collection == null) {
            return 0;
        }
        return this.collection.size();
    }

    public void logTotalPlayTime() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Total play time across all games " + TimeHelper.getDurationText(this.totalPlayTime));
        }
    }

    public void logLadder(int number) {
        if (number > 0) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Logging the [" + number + "] games played most");
            }
            logNLadder(number);
        } else {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Number of games to list for the ladder needs to be a positive number");
            }
        }
    }

    public void logNeverPlayed(int number, long neverPlayedMins) {
        int logAll = -1;
        if (number == logAll) {
            logAllNeverPlayed(neverPlayedMins);
        } else {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Logging [" + number + "] games never played");
            }
            logNNeverPlayed(number, neverPlayedMins);
        }
    }

    private void logAllNeverPlayed(long neverPlayedMins) {
        int i = 0;
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Logging all games never played");
        }

        for (Game game : collection.values()) {
            if (game.getTotalMinutesPlayed() <= neverPlayedMins && LOGGER.isInfoEnabled()) {
                LOGGER.info(game.getName());
                i++;
            }
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Total number of games never played [" + i + "] out of [" + this.getNumberOfGames() + "] games");
        }
    }

    private void logNNeverPlayed(int number, long neverPlayedMins) {
        List<Game> randomizedList = Lists.newArrayList(collection.values());
        Collections.shuffle(randomizedList, new Random());
        Iterator<Game> it = randomizedList.iterator();
        int i = 0;

        while (it.hasNext() && i < number) {
            Game game = it.next();
            if (game.getTotalMinutesPlayed() <= neverPlayedMins) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(game.getName());
                }
                i++;
            }
        }

        if (!it.hasNext() && i < number && LOGGER.isDebugEnabled()) {
            LOGGER.debug("No more games in the collection");
        }
    }

    private void logNLadder(int number) {
        List<Game> sortedList = Lists.newArrayList(collection.values());
        Collections.sort(sortedList, new PlayTimeComparator());
        Iterator<Game> it = sortedList.iterator();
        int i = 0;

        while (it.hasNext() && i < number) {
            Game game = it.next();
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(game.getName() + " [" + TimeHelper.getDurationText(game.getTotalMinutesPlayed()) + "]");
            }
            i++;
        }

        if (!it.hasNext() && i < number && LOGGER.isDebugEnabled()) {
            LOGGER.debug("No more games in the collection");
        }
    }

    public ImmutableList<Game> getGamesList() {
        return ImmutableList.copyOf(collection.values());
    }

}
