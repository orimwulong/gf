package com.orimwulong.gamefinder.platform;

import java.util.List;
import java.util.Map;

import com.orimwulong.gamefinder.Game;

public interface Platform {

    boolean configure(Map<String, String> configMap);
    String getRawOwnedGamesList();
    List<Game> getOwnedGamesList() throws Exception;

}
