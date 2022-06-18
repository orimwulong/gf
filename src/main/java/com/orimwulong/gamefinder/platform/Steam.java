package com.orimwulong.gamefinder.platform;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.gson.stream.JsonReader;
import com.orimwulong.gamefinder.game.Game;
import com.orimwulong.gamefinder.game.GamesCollection;
import com.orimwulong.gamefinder.utils.HttpsHelper;

public class Steam implements Platform {

    private static final Logger LOGGER = LoggerFactory.getLogger(Steam.class);

    private String steamID64;
    private String webAPIKey;
    private int totalGamesCount;

    @Override
    public String getName() {
        return "Steam";
    }

    @Override
    public boolean configure(Map<String, String> configMap) {
        if (configMap == null) {
            return false;
        }

        this.steamID64 = configMap.get("steam.steamid64");
        this.webAPIKey = configMap.get("steam.webapi.key");

        if (Strings.isNullOrEmpty(this.steamID64) || Strings.isNullOrEmpty(this.webAPIKey)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("configMap didn't not contain expected keys or values were empty.");
            }
            return false;
        }

        return true;
    }

    @Override
    public String getRawOwnedGamesList() {
        String result;
        String baseUrl = "https://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?format=json&include_appinfo=true&include_played_free_games=true";
        String urlWithTokens = baseUrl +"&steamid=" + this.steamID64 + "&key=" + this.webAPIKey;
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Retrieving owned games list");
        }
        result = HttpsHelper.getHttpsContent(urlWithTokens);
        if (Strings.isNullOrEmpty(result) && LOGGER.isErrorEnabled()) {
            LOGGER.error("Unable to retrieve ownder games list. Base URL was [" + baseUrl + "]");
        }
        return result;
    }

    @Override
    public void addOwnedGamesToCollection(GamesCollection collection) {
        String gamesList = getRawOwnedGamesList();
        JsonReader reader = new JsonReader(new StringReader(gamesList));
        try {
            readRoot(reader, collection);
            reader.close();
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Steam games count ["+ this.totalGamesCount + "]. Loaded collection games count [" + collection.getNumberOfGames() + "].");
            }
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Exception while parsing content", e);
            }
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("Exception while closing reader", e);
                }
            }
        }
    }

    private void readRoot(JsonReader reader, GamesCollection collection) throws IOException {
        reader.beginObject();
        // response
        reader.nextName();
        readResponse(reader, collection);
        reader.endObject();
    }

    private void readResponse(JsonReader reader, GamesCollection collection) throws IOException {
        reader.beginObject();
        // game_count
        reader.nextName();
        this.totalGamesCount = reader.nextInt();
        readGames(reader, collection);
        reader.endObject();
    }

    private void readGames(JsonReader reader, GamesCollection collection) throws IOException {
        // games
        reader.nextName();
        reader.beginArray();
        while (reader.hasNext()) {
            readGame(reader, collection);
        }
        reader.endArray();
    }

    private void readGame(JsonReader reader, GamesCollection collection) throws IOException {
        String gameName = null;
        long totalMinutesPlayed = -1L;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if ("name".equals(name)) {
                gameName = reader.nextString();
            } else if ("playtime_forever".equals(name)) {
                totalMinutesPlayed = reader.nextLong();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        collection.addGame(new Game(gameName, totalMinutesPlayed));
    }

}
