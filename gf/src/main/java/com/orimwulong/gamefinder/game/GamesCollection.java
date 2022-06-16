package com.orimwulong.gamefinder.game;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GamesCollection {

    private static Logger logger = LoggerFactory.getLogger(GamesCollection.class);

    private Map<String, Game> collection;

    public void addGame(Game game) {
        if (this.collection == null) {
            this.collection = new HashMap<String, Game>();
        }
        String gameName = game.getName();
        if (!this.collection.containsKey(game.getName())) {
            logger.debug("Adding game to collection [" + gameName + "]");
            this.collection.put(gameName, game);
        } else {
            logger.debug("Adding played minutes to existing game [" + gameName + "]");
            this.collection.get(gameName).addPlayedMinutes(game.getTotalMinutesPlayed());
        }
    }

    public int getNumberOfGames() {
        if (this.collection == null) {
            return 0;
        }
        return this.collection.size();
    }

}
