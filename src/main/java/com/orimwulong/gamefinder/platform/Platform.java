package com.orimwulong.gamefinder.platform;

import java.util.Map;

import com.orimwulong.gamefinder.game.GamesCollection;

public interface Platform {

    String getName();
    boolean configure(Map<String, String> configMap);
    void addOwnedGamesToCollection(GamesCollection collection);

}
