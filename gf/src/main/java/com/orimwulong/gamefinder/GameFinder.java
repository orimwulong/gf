package com.orimwulong.gamefinder;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.orimwulong.gamefinder.game.GamesCollection;
import com.orimwulong.gamefinder.platform.Steam;

public class GameFinder {

    private static final Logger logger = LoggerFactory.getLogger(GameFinder.class);

    private Steam steam;
    private GamesCollection games;
    public static void main(String[] args) {
        GameFinder gf = new GameFinder();
        if (gf.init()) {
            gf.run();
        }
        gf.close();
    }

    public boolean init() {
        boolean initComplete = false;
        logger.info("Initialising GameFinder...");

        String propertiesFileName = "gf.properties";
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(propertiesFileName));
            initComplete = true;
        } catch (IOException e) {
            logger.error("Unable to load properties [" + propertiesFileName + "]");
        }

        steam = new Steam();
        initComplete = initComplete && steam.configure(Maps.fromProperties(properties));
        this.games = new GamesCollection();
        return initComplete;
    }

    public void run() {
        logger.info("Getting the games info...");
        steam.addOwnedGamesToCollection(games);
    }

    public void close() {
        logger.info("Bye for now!");
    }

}
