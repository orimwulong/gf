package com.orimwulong.gamefinder.platform;

import java.util.List;
import java.util.Map;

import javax.naming.OperationNotSupportedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.orimwulong.gamefinder.Game;
import com.orimwulong.gamefinder.utils.HttpsApi;

public class Steam implements Platform {

    private static final Logger logger = LoggerFactory.getLogger(Steam.class);

    private String steamID64;
    private String webAPIKey;

    @Override
    public boolean configure(Map<String, String> configMap) {
        if (configMap == null)
            return false;

        this.steamID64 = configMap.get("steam.steamid64");
        this.webAPIKey = configMap.get("steam.webapi.key");

        if (Strings.isNullOrEmpty(this.steamID64) || Strings.isNullOrEmpty(this.webAPIKey)) {
            logger.debug("configMap didn't not contain expected keys or values were empty.");
            return false;
        }

        return true;
    }

    @Override
    public String getRawOwnedGamesList() {
        String result = null;
        String baseUrl = "https://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?format=json&include_appinfo=true&include_played_free_games=true";
        String urlWithTokens = baseUrl +"&steamid=" + this.steamID64 + "&key=" + this.webAPIKey;
        logger.info("Retrieving owned games list");
        result = HttpsApi.getHttpsContent(urlWithTokens);
        if (Strings.isNullOrEmpty(result))
            logger.error("Unable to retrieve ownder games list. Base URL was [" + baseUrl + "]");
        return result;
    }

    @Override
    public List<Game> getOwnedGamesList() throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Not yet implemented");
    }

}
