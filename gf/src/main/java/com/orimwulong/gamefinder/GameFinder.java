package com.orimwulong.gamefinder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.orimwulong.gamefinder.game.GamesCollection;
import com.orimwulong.gamefinder.platform.Steam;

public class GameFinder {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameFinder.class);

    private Steam steam;
    private GamesCollection games;
    public static void main(final String[] args) {
        GameFinder gf = new GameFinder();
        if (gf.init()) {
            gf.run();
        }
        gf.close();
    }

    public boolean init() {
        boolean initComplete = false;
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Initialising GameFinder...");
        }
        String propertiesFileName = "gf.properties";
        Properties properties = new Properties();
        try {
            properties.load(Files.newInputStream(Paths.get(propertiesFileName)));
            initComplete = true;
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Unable to load properties [" + propertiesFileName + "]");
            }
        }

        steam = new Steam();
        initComplete = initComplete && steam.configure(Maps.fromProperties(properties));
        this.games = new GamesCollection();
        return initComplete;
    }

    public void run() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Getting the games info...");
        }
        steam.addOwnedGamesToCollection(games);
    }

    public void close() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Bye for now!");
        }
    }

}
